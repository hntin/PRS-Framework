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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
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
    public static boolean CheckImportData(ImportFiles importFile, final String path) throws IOException {
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

//File author.csv has format idAuthor|||nameAuthor
    public static boolean fileAuthors(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 2 || !NumericUtility.isNum(tokens[0])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

//File author_paper.csv has format idAuthor|||idPaper
    public static boolean fileAuthorPaper(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 2 || !NumericUtility.isNum(tokens[0]) || !NumericUtility.isNum(tokens[1])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

//File author_cite_paper.csv has format idAuthor|||IdPaper|||Year
    public static boolean fileAuthorCitePaper(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 3 || !NumericUtility.isNum(tokens[0]) || !NumericUtility.isNum(tokens[1])
                            || !NumericUtility.isNum(tokens[2])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

//File paper.csv has format idPaper|||title|||content|||year
    public static boolean filePapers(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 4 || !NumericUtility.isNum(tokens[0]) || !NumericUtility.isNum(tokens[3])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

//File paper_cite_paper.csv has format idPaper|||idPaper
    public static boolean filePaperCitePaper(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 2 || !NumericUtility.isNum(tokens[0]) || !NumericUtility.isNum(tokens[1])
                            || tokens[0].equals(tokens[1])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }

//File groundtruth.csv has format idAuthor|||idPaper
    public static boolean fileGroundTruth(final String path) throws FileNotFoundException, IOException {
        boolean check = true;
        final File fileLog = new File("Temp\\log.txt");

        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);
        FileReader file = new FileReader(new File(path));
        BufferedReader textReader = new BufferedReader(file);
        String line = null;
        int numline = 0;
        while ((line = textReader.readLine()) != null) {
            final String[] tokens = line.split("\\|\\|\\|");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if (tokens.length != 2 || !NumericUtility.isNum(tokens[0]) || !NumericUtility.isNum(tokens[1])) {
                        String error = "File '" + path + "' not correct format\nReason:\n Correct at line ";
                        try {
                            FileUtils.writeStringToFile(fileLog, error, "UTF8", true);
                        } catch (IOException ex) {
                            Logger.getLogger(CheckError.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            if (fileLog.exists()) {
                FileUtils.writeStringToFile(fileLog, String.valueOf(numline), "UTF8", true);
                check = false;
                break;
            }
            numline++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return check;
    }
}
