/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.gui;


import ir.utilities.Weight;
import ir.vsr.HashMapVector;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;
import uit.tkorg.pr.dataimex.MahoutFile;
import uit.tkorg.utility.textvectorization.TextPreprocessUtility;
import uit.tkorg.utility.textvectorization.TextVectorizationByMahoutTerminalUtility;

/**
 *
 * @author Vinh
 */
public class GuiUtilities {
//Save to file using JChooser
    public static String saveToFileJChooser() {
        String path = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save To File");
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    } else {
                        return file.getName().toLowerCase().endsWith(".dat");
                    }
                }

                @Override
                public String getDescription() {
                    return "Text Files (*.dat)";
                }
            });
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileChooser.getFileFilter().accept(fileToSave) || !fileToSave.getName().toLowerCase().endsWith(".dat")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".dat");
                }
                path = fileToSave.getAbsolutePath();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return path;
    }

//Save to file using JChooser
    public static String saveToFile() {
        String path = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save To File");
            FileFilter fileFilterText=new FileNameExtensionFilter("Text Files", "*.txt");
            FileFilter fileFilterCSV=new FileNameExtensionFilter("CVS Files", "*.csv");
            fileChooser.addChoosableFileFilter(fileFilterText);
            fileChooser.addChoosableFileFilter(fileFilterCSV);
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileChooser.getFileFilter().accept(fileToSave) || !fileToSave.getName().toLowerCase().endsWith(".dat")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".dat");
                }
                path = fileToSave.getAbsolutePath();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return path;
    }
    
//Load file using JChooser

    public static String loadFileJChooser() {
        String path = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int userSelection = fileChooser.showOpenDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath().toString();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return path;
    }

    //Choose folder using JChooser
    public static String chooseFolderJChooser(String title) {
        String path = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(title);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int userSelection = fileChooser.showOpenDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath().toString();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return path;
    }

    //Choose file using JChooser
    public static String chooseFileJChooser(String title) {
        String path = null;
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(title);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int userSelection = fileChooser.showOpenDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile().getAbsolutePath().toString();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return path;
    }

    //Create tfidf files from Mahout
    public static void createTFIDF(String pathText, String pathTFIDF) {
        String pathPreprocess = "Temp\\Preprocess";
        String pathSequence = "Temp\\Sequence";
        String pathVectorDir = "Temp\\VectorDir";

        try {
            if (pathTFIDF != null) {
                TextPreprocessUtility.parallelProcess(pathText, pathPreprocess, true, true);
                TextVectorizationByMahoutTerminalUtility.textVectorizeFiles(pathPreprocess, pathSequence, pathVectorDir);
                HashMap<String, HashMapVector> vectorizedPapers = MahoutFile.readMahoutVectorFiles(pathVectorDir);
                HashMap<Integer, String> dictMap = MahoutFile.readMahoutDictionaryFiles(pathVectorDir);

                for (String documentId : vectorizedPapers.keySet()) {
                    HashMapVector hashmapVector = vectorizedPapers.get(documentId);
                    StringBuffer fileTFIDF = new StringBuffer();
                    for (Map.Entry<String, Weight> entry : hashmapVector.entrySet()) {
                        fileTFIDF.append(dictMap.get(Integer.parseInt(entry.getKey())) + " " + entry.getValue().getValue()).append("\n");
                    }
                    FileUtils.writeStringToFile(new File(pathTFIDF + "\\" + documentId + ".txt"), fileTFIDF.toString(), "UTF8", false);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(IntroductionPRS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    //Doc so hang va so cot cua mot file matran va tra ve missing value
    public static double missingValueInMatrixCF(String pathMatrixCF, int numAuthors, int numPapers) throws FileNotFoundException {
        double missingValue = 0;
        BufferedReader reader = new BufferedReader(new FileReader(pathMatrixCF));
        String line = null;
        int numLine = 0;
        try {
            while ((line = reader.readLine()) != null) {
                numLine++;
            }
            missingValue = numLine / (numAuthors * numPapers);
        } catch (IOException ex) {
            Logger.getLogger(GuiUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return missingValue * 100;
    }
}
