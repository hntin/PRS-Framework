/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.gui;

import de.progra.charting.ChartEncoder;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.DefaultChartDataModel;
import de.progra.charting.render.LineChartRenderer;
import de.progra.charting.render.PlotChartRenderer;
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
import org.apache.commons.io.FileUtils;
import uit.tkorg.pr.dataimex.MahoutFile;
import uit.tkorg.utility.textvectorization.TextPreprocessUtility;
import uit.tkorg.utility.textvectorization.TextVectorizationByMahoutTerminalUtility;

/**
 *
 * @author Vinh
 */
public class GUIUtilities {
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
            Logger.getLogger(IntroducePRS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Draw chart with JOpenChart
    public static boolean drawChart(String title1) {
        String path = "Chart";
        int[][] model = {{0, 10000, 200000}, // Create data array
        {0, 20000, 100000}};

        double[] columns = {0.0, 100.0, 2000.0};  // Create x-axis values

        String[] rows = {"DataSet 1", "DataSet 2"}; // Create data set title

        String title = "A First Test";          // Create diagram title

        int width = 640;                        // Image size
        int height = 480;

        // Create data model
        DefaultChartDataModel data = new DefaultChartDataModel(model, columns, rows);

        data.setAutoScale(true);

        // Create chart with default coordinate system
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");

        // Add a line chart renderer
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(), data), 1);
        c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(), data), 2);

        // Set the chart size
        c.setBounds(new Rectangle(0, 0, width, height));

        // Export the chart as a PNG image
        try {
            ChartEncoder.createEncodedImage(new FileOutputStream("Chart\\second.png"), c, "png");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            Logger.getLogger(GUIUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return missingValue * 100;
    }
}
