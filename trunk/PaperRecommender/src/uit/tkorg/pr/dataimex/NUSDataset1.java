/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.dataimex;

import ir.vsr.HashMapVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import uit.tkorg.pr.constant.PRConstant;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.utility.general.NumericUtility;
import uit.tkorg.utility.general.TextFileUtility;

/**
 *
 * @author THNghiep 
 * This class contents all method to import data from dataset1.
 * Import process needs to filter out noisy data such as keywords longer than 50
 * characters.
 */
public class NUSDataset1 {

    // Prevent instantiation.
    private NUSDataset1() {

    }

    /**
     * This method read all keywords in all papers in the dataset 1 and return
     * them in an arraylist.
     *
     * @return allKeywords.
     * @throws java.io.FileNotFoundException
     */
    public static List readAllKeywords() throws Exception {
        List allKeywords = new ArrayList();
        List<String> ffile = TextFileUtility.getPathFile(new File(PRConstant.FOLDER_NUS_DATASET1));
        for (int i = 0; i < ffile.size(); i++) {
            FileReader file = new FileReader(ffile.get(i));
            BufferedReader textReader = new BufferedReader(file);
            String line;
            String[] tokens;
            while ((line = textReader.readLine()) != null) {
                tokens = line.split(" ");
                if (tokens.length == 2) {
                    allKeywords.add(tokens[0]);
                }
            }
        }
        return allKeywords;
    }

    /**
     * This method read dataset 1 folder, then for each paper, create a Paper
     * object and put it in the hashmap.
     *
     * HashMap Key: paper id (in file name) HashMap Value: paper object.
     *
     * @return the hashmap contents all papers.
     * @throws java.io.IOException
     */
    public static HashMap<String, Paper> buildListOfPapers(String Dataset1Folder) throws Exception {
        HashMap<String, Paper> papers;
        papers = readAllCandidatePapers(new File(Dataset1Folder + "\\RecCandidatePapersFV"));
        return papers;
    }

    /**
     * This method browse each file in candidate paper directory
     *
     * @param dir: address of candidate paper directory
     * @return papers: list of candidate papers
     * @throws java.io.IOException
     */
    private static HashMap<String, Paper> readAllCandidatePapers(File dir) throws Exception {
        HashMap<String, Paper> papers = new HashMap<>();
        File[] files = dir.listFiles();//Get list of all files in directory
        //Browse all files in directory
        for (File file : files) {
            String paperid = file.getName().replaceAll("_recfv.txt", "");
            Paper paper = new Paper();
            paper.setPaperId(paperid);//set PaperId for paper
            paper.setYear(paperYear(paperid));//set Year for paper
            paper.setTfidfVector(readFilePaper(file.getAbsoluteFile()));//set Content for paper
            paper.setCitation(addCitation(paperid));//set Citation for paper
            paper.setReference(addReference(paperid));//set Reference for paper
            papers.put(paperid, paper);
        }
        return papers;
    }

    /**
     * This method return year of candidate paper
     *
     * @param paperId
     * @return year of paper
     */
    private static int paperYear(String paperId) throws Exception {
        String year = paperId.substring(1, 3);
        
        if (NumericUtility.isNum(year)) {
            return Integer.parseInt("20" + year);
        } else {
            return -1;
        }
    }

    /**
     * This method read candidate paper file to set content for candidate paper
     *
     * @param file: address of file
     * @return content
     * @throws java.io.FileNotFoundException
     */
    private static HashMapVector readFilePaper(File file) throws Exception {
        HashMapVector content = new HashMapVector();
        String path = file.getAbsolutePath();

        // try-with-resources Statement.
        // A resource is an object that must be closed after the program is finished with it. 
        // The try-with-resources statement ensures that each resource is closed at the end of the statement.
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] str = line.split("\\s+");
                if (str.length == 2) {
                    if (str[0].length() < 30 && NumericUtility.isNum(str[1])) {
                        content.increment(str[0], Double.valueOf(str[1]));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + path);
            throw e;
        }
        return content;
    }

    /**
     * This method add Citation for paper
     *
     * @param paperId
     * @return citation
     * @throws java.io.FileNotFoundException
     */
    private static List<String> addCitation(String paperId) throws Exception {
        List<String> citation = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRConstant.FOLDER_NUS_DATASET1 + "\\InterLink\\acl.20080325.net"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ==> ");
                if (str[1].equals(paperId)) {
                    citation.add(str[0]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return citation;
    }

    /**
     * This method add Reference for paper
     *
     * @param paperId
     * @return reference
     * @throws java.io.FileNotFoundException
     */
    private static List<String> addReference(String paperId) throws Exception {
        List<String> reference = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRConstant.FOLDER_NUS_DATASET1 + "\\InterLink\\acl.20080325.net"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ==> ");
                if (str[0].equals(paperId)) {
                    reference.add(str[1]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return reference;
    }

    /**
     * This method find list paper ground truth of Author
     *
     * @param dir
     * @return a string is groundTruth of author
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<String> findGroundTruth(File dir) throws Exception {
        List<String> fileTruth = new ArrayList();
        File[] files = dir.listFiles();
        for (File file : files) {
            if ((file.isFile()) && (file.getName().contains("-rlv.txt"))) {
                BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    fileTruth.add(line);
                }
            }
        }
        return fileTruth;
    }

    /**
     * This is method find list paper citation of author
     *
     * @param dir
     * @return list<paper> cit of authors
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<Paper> findCitOfPaper(File dir) throws Exception {
        List cites = new ArrayList();
        
        File[] files = dir.listFiles();

        for (File file : files) {
            Paper paper = new Paper();
            paper.setPaperId(file.getName().replaceAll("_fv.txt|.txt", ""));
            paper.setPaperType("Citation");
            paper.setTfidfVector(readFilePaper(new File(file.getAbsolutePath())));
            
            cites.add(paper);
        }
        return cites;
    }

    /**
     * This is method find all paper reference of author
     *
     * @param dir
     * @return list<paper> reference of author
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<Paper> findRefOfPaper(File dir) throws Exception {
        List refs = new ArrayList();
        
        File[] files = dir.listFiles();

        for (File file : files) {
            Paper paper = new Paper();
            paper.setPaperId(file.getName().replaceAll("_fv.txt", ""));
            paper.setPaperType("Reference");
            paper.setTfidfVector(readFilePaper(new File(file.getAbsolutePath())));
            
            refs.add(paper);
        }
        
        return refs;

    }

    /**
     * This is method find all paper of author
     *
     * @param dir
     * @return list <paper> of author
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<Paper> findPaperOfAuthor(File dir) throws Exception {

        List<Paper> papers = new ArrayList();
        String authorId = dir.getName();// lay id cua Author

        if (authorId.contains("y")) {
            Paper paper = new Paper();
            paper.setPaperId(dir.getName() + "-1"); // set id paper Junior
            paper.setPaperType("Paper of junior");

            String refPath = dir.getAbsolutePath() + "\\" + authorId + "RefsFV";
            paper.setReference(findRefOfPaper(new File(refPath)));

            // ten duong dan den vector dac trung cua paper
            String pathVectorFv = dir.getAbsolutePath() + "\\" + authorId + "-1" + "_fv.txt";
            paper.setTfidfVector(readFilePaper(new File(pathVectorFv)));// set content cho paper i
            
            papers.add(paper);

        } else {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    Paper paper = new Paper();
                    
                    String paperId = file.getName();
                    paper.setPaperId(paperId); // set id paper i cua Senior
                    paper.setPaperType("Paper of senior");

                    String citePath = file.getAbsolutePath() + "\\" + paperId + "CitsFV";
                    paper.setCitation(findCitOfPaper(new File(citePath))); // set List cit cua  paper i
                    
                    String refPath = file.getAbsolutePath() + "\\" + paperId + "RefsFV";
                    paper.setReference(findRefOfPaper(new File(refPath))); // set List ref cua paper i
                    
                    String pathVectorFv = file.getAbsolutePath() + "\\" + file.getName() + "_fv.txt";
                    paper.setTfidfVector(readFilePaper(new File(pathVectorFv)));// set content cho paper i
                    
                    papers.add(paper);
                }
            }
        }
        return papers;
    }

    /**
     * This is method read all author
     *
     * @param dir
     * @return authors
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static HashMap<String, Author> readAllAuthor(File dir) throws Exception {
        HashMap<String, Author> authors = new HashMap<String, Author>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().equals("JuniorR")) {
                    File[] juniors = file.listFiles();
                    for (File junior : juniors) {
                        Author author = new Author();
                        
                        author.setAuthorId(junior.getName());
                        author.setAuthorType("Junior");
                        author.setGroundTruth(findGroundTruth(junior));
                        author.setPaper(findPaperOfAuthor(junior));
                        
                        authors.put(junior.getName(), author);
                    }
                } else if (file.getName().equals("SeniorR")) {
                    File[] seniors = file.listFiles();
                    for (File senior : seniors) {
                        Author author = new Author();
                        
                        author.setAuthorId(senior.getName());
                        author.setAuthorType("Senior");
                        author.setGroundTruth(findGroundTruth(senior));
                        author.setPaper(findPaperOfAuthor(senior));
                        
                        authors.put(senior.getName(), author);
                    }
                }
            }
        }
        return authors;
    }

    /**
     * This method read dataset 1 folder, then for each author, create an Author
     * object and put it in the hashmap.
     *
     * HashMap Key: author id (in file name) HashMap Value: author object.
     *
     * @return the hashmap contents all authors.
     * @throws java.io.IOException
     */
    public static HashMap<String, Author> buildListOfAuthors(String Dataset1Folder) throws Exception {
        HashMap<String, Author> authors = new HashMap();
        authors = readAllAuthor(new File(Dataset1Folder));
        return authors;
    }
}
