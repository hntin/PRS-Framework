/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.FlatFileData;

import ir.vsr.HashMapVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.tkorg.paperrecommender.model.Paper;

/**
 *
 * @author THNghiep This class contents all method to import data from dataset1.
 * Import process needs to filter out noisy data such as keywords longer than 50
 * characters.
 */
public class ImportDataset1 {

    /**
     * This method read all keywords in all papers in the dataset 1 and return
     * them in an arraylist.
     *
     * @return allKeywords.
     */
    // Minh
    //Lấy đường dẫn của các file txt có trong thư mục
    public  static List<String> Getpathfile (File dir)
    {
           File[] files = dir.listFiles();
           List listfile= new ArrayList<String>();
           String name = null;
          for (int i=0; i< files.length;i++)
          {
              if (files[i].isFile())
                  name= files[i].getName().toString();
              if (name== "*.txt") listfile.add(files[i].getAbsolutePath());
          }
          return listfile;                 
    }
    public static List readAllKeywords() {
        List allKeywords = new ArrayList();
        //generate code here
        return allKeywords;
    }
    // @ Minh 
   //Doc du lieu tu file txt vao luu vao List
   public static List readAllKeywords(File dir)   throws FileNotFoundException, IOException
    {
        List <String> allKeywords = new ArrayList();     
        //generate code here
       List<String> ffile = Getpathfile(dir);
        for (int i=0; i< ffile.size();i++)
        try{
            FileReader file = new FileReader(ffile.get(i));
            BufferedReader textReader = new BufferedReader(file);
            String line;
            String[] tokens;
            while ((line = textReader.readLine()) != null)
            {
                tokens = line.split(" ");
                if (tokens.length == 2)
                {
                   allKeywords.add(tokens[0]);
                }
            }
        }catch(Exception ex)
        {
           System.out.println(ex.getMessage());
        }
        return allKeywords;
    }
     //@ author Minh
    // Xay dung tap tu vung chung cho tat ca cac paper
  
   public static HashMap <Integer, String> Vocabulary (File dir)throws IOException
   {
       HashMap <Integer,String> words = new HashMap<Integer,String>();
       List<String> key;
       Integer currId = 0;
       Integer id;
        try {
            key = readAllKeywords(dir);
             
       for(int i=0;i< key.size();i++)
       {
           id= Integer.valueOf(key.get(i));
        // Kiem tra xem value do da co chua neu chua thi them vao 
           if (key.get(i) != words.get(currId))
           {
            words.put(id,key.get(i));
            currId= id;
           }    
       }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportDataset1.class.getName()).log(Level.SEVERE, null, ex);
        }
          return words;   
    }
    /**
     * This method read dataset folder (from constant class), then for each
     * paper, create a Paper object and put it in the hashmap.
     *
     * HashMap Key: paper id (in file name) HashMap Value: paper object.
     * @return the hashmap contents all papers.
     * @throws java.io.IOException
     */
    public static HashMap<String, Paper> buildListOfPapers() throws IOException {
        HashMap<String, Paper> papers = new HashMap<String, Paper>();
        readAllCandidatePapers(new File("C:\\Users\\Vinh\\Desktop\\Tailieuluanvan\\20100825-SchPaperRecData\\20100825-SchPaperRecData\\RecCandidatePapersFV"), papers);
        return papers;
    }

    /**
     * This method browse each file in candidate paper directory
     *
     * @param dir: address of candidate paper directory
     * @param papers: store all candidate papers
     * @throws java.io.IOException
     */
    public static void readAllCandidatePapers(File dir, HashMap<String, Paper> papers) throws IOException {
        //Get list of all files in directory
        File[] files = dir.listFiles();
        //Browse all files in directory
        for (File file : files) {
            String paperid = file.getName().replaceAll("_recfv.txt", "");
            Paper paper = new Paper();
            paper.setPaperId(paperid);//set PaperId for paper
            paper.setYear(paperYear(paperid));//set Year for paper
            paper.setContent(readFilePaper(file.getAbsoluteFile()));//set Content for paper
            paper.setCitation(addCitation(file.getAbsolutePath()));//set Citation for paper
            paper.setReference(addReference(file.getAbsolutePath()));//set Reference for paper
            papers.put(paperid, paper);
        }
    }

    /**
     * This method read each file in candidate paper directory
     *
     * @param file: address of file
     * @return content
     * @throws java.io.FileNotFoundException
     */
    public static HashMapVector readFilePaper(File file) throws FileNotFoundException, IOException {
        HashMapVector content=new HashMapVector();
        String path = file.getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                content.increment(str[0], Double.valueOf(str[1]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

    /**
     * This method return year of candidate paper
     *
     * @param paperId
     * @return year of paper
     */
    public static int paperYear(String paperId) {
        paperId = paperId.substring(1, 2);
        return Integer.parseInt("20" + paperId);
    }

    /**
     * This method add Citation for paper
     *
     * @param paperId
     * @return citation
     * @throws java.io.FileNotFoundException
     */
    public static List<String> addCitation(String paperId) throws FileNotFoundException, IOException {
        List<String> citation = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vinh\\Desktop\\Tailieuluanvan\\20100825-SchPaperRecData\\20100825-SchPaperRecData\\InterLink\\acl.20080325.net"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ==> ");
                if (str[1].equals(paperId)) {
                    citation.add(str[0]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
    public static List<String> addReference(String paperId) throws FileNotFoundException, IOException {
        List<String> reference = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vinh\\Desktop\\Tailieuluanvan\\20100825-SchPaperRecData\\20100825-SchPaperRecData\\InterLink\\acl.20080325.net"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ==> ");
                if (str[0].equals(paperId)) {
                    reference.add(str[1]);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return reference;
    }
}
