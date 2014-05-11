/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Vinh-PC
 */
public class CollaborativeFiltering {

    public static double[][] fillFullMatrix(double[][] sparseMatrix, int numNeightbour) throws Exception {
        HashMap KNN = new HashMap();

        for (int i = 0; i < sparseMatrix.length; i++) {
            HashMap PCC = computePCC(sparseMatrix, i);
            PCC = GeneralUtility.sortHashMap(PCC);
            int counter = 0;
            for (Iterator it = PCC.keySet().iterator(); it.hasNext();) {
                String item = (String) it.next();
                KNN.put(item, PCC.get(item));
                counter++;
                if (counter >= numNeightbour) {
                    break;
                }
            }
            for (int j = 0; j < sparseMatrix[i].length; j++) {
                if (sparseMatrix[i][j] == 0) {
                    sparseMatrix[i][j] = computePredict(sparseMatrix, i, j, KNN);
                }
            }
        }
        return sparseMatrix;
    }

    /**
     *
     * @param sparseMatrix
     * @param i
     * @return
     */
    public static HashMap computePCC(double[][] sparseMatrix, int i) {
        HashMap PCC = new HashMap();

        for (int j = 0; j < sparseMatrix.length; j++) {
            List iValue = new ArrayList();
            List jValue = new ArrayList();
            if (i != j) {
                for (int k = 0; k < sparseMatrix[i].length; k++) {
                    if (sparseMatrix[i][k] != 0 && sparseMatrix[j][k] != 0) {
                        iValue.add(sparseMatrix[i][k]);
                        jValue.add(sparseMatrix[j][k]);
                    }
                }
                double pccValue = ir.utilities.Stats.pearsonCorrelation(score(iValue), score(jValue));
                PCC.put(String.valueOf(j), pccValue);
            }
        }

        return PCC;
    }

    /**
     *
     * @param sparseMatrix
     * @param i
     * @param j
     * @param KNN
     * @return
     */
    public static double computePredict(double[][] sparseMatrix, int i, int j, HashMap KNN) {
        double predict = 0;
        double numerator = 0;
        double denominator = 0;

        List iValue = new ArrayList();
        for (int n = 0; n < sparseMatrix[i].length; n++) {
            if (sparseMatrix[i][n] != 0) {
                iValue.add(sparseMatrix[i][n]);
            }
        }
        predict += ir.utilities.Stats.mean(score(iValue));
        for (Iterator it = KNN.keySet().iterator(); it.hasNext();) {
            String item = (String) it.next();

            List jValue = new ArrayList();
            for (int n = 0; n < sparseMatrix[Integer.valueOf(item)].length; n++) {
                if (sparseMatrix[Integer.valueOf(item)][n] != 0) {
                    jValue.add(sparseMatrix[i][n]);
                }
            }
            double mean = ir.utilities.Stats.mean(score(jValue));
            System.out.println("mean" + mean);
            numerator += (double) (sparseMatrix[Integer.valueOf(item)][j] - mean) * (double) KNN.get(item);
            denominator += (double) KNN.get(item);
        }
        predict += numerator / denominator;

        System.out.println("tu" + numerator);
        System.out.println("mau" + denominator);
        System.out.println("tuyen doan" + predict);

        return predict;
    }

    /**
     * This method get double array of item.
     *
     * @param value
     * @return
     */
    public static double[] score(List<Double> value) {
        double[] temp = new double[value.size()];
        for (int i = 0; i < value.size(); i++) {
            temp[i] = (double) value.get(i);
        }
        return temp;
    }
}
