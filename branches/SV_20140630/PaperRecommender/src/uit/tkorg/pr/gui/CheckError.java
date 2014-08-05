/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import uit.tkorg.pr.constant.ImportFiles;
import uit.tkorg.utility.general.NumericUtility;

/**
 *
 * @author Zoe
 */
public class CheckError {

    /**
     *
     * @param importFile
     * @param path
     * @return
     * @throws java.io.IOException
     */
    public static boolean CheckImportData(ImportFiles importFile, String path) throws IOException {
        switch (importFile) {
            case FILE_AUTHORS:
                return fileAuthors(path);
            case FILE_AUTHOR_PAPER:
                return fileAuthorPaper(path);
            case FILE_AUTHOR_CITE_PAPER:
                return fileAuthorCitePaper(path);
            case FILE_PAPERS:
                return filePapers(path);
            case FILE_PAPER_CITE_PAPER:
                return filePaperCitePaper(path);
            case FILE_GROUNDTRUTH:
                return fileGroundTruth(path);
            default:
                break;
        }
        return false;
    }

    public static boolean fileAuthors(String path) throws FileNotFoundException, IOException {
        boolean check = false;

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = textReader.readLine();
        while (textReader.readLine() != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if(tokens.length!=2 && !NumericUtility.isNum(tokens[0]))
                    {
                        
                    }
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

    public static boolean fileAuthorPaper(String path) {
        boolean check = false;

        return check;
    }

    public static boolean fileAuthorCitePaper(String path) {
        boolean check = false;

        return check;
    }

    public static boolean filePapers(String path) {
        boolean check = false;

        return check;
    }

    public static boolean filePaperCitePaper(String path) {
        boolean check = false;

        return check;
    }

    public static boolean fileGroundTruth(String path) {
        boolean check = false;

        return check;
    }
}
