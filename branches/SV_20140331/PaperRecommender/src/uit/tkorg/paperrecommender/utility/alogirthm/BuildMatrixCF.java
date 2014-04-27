/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Author;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import uit.tkorg.paperrecommender.utility.Weighting;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.PearsonCorrelation;
import uit.tkorg.paperrecommender.utility.alogirthm.Knn;

/**
 *
 * @author Minh
 */

public class BuildMatrixCF {

    /**
     * This is method build MatrixInput between paper and paper
     *
     * @param items
     * @return matrixInput
     * @throws Exception
     */
    public static double[][] buildMatrixInput(List<Paper> items) throws Exception {
        double[][] matrixInput = new double[items.size()][items.size()];
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                   if (i==j)
                    matrixInput[i][j]=1.00;
                // checked paper j is paper cit of paper i compute cosine to write to the matrix
                if (items.get(i).getCitation().contains(items.get(j).getPaperId())) {
                    matrixInput[i][j] = GeneralUtility.standardizeSimilarityValue(Weighting.computeCosine(items.get(i).getContent(),
                            items.get(j).getContent()));
                }
            }
        }
        return matrixInput;
    }

    /**
     * This is method build MatrixInput between user and paper
     *
     * @param users
     * @param items
     * @return
     */
    public static double[][] buildMatrixInput(List<Author> users, List<Paper> items) throws Exception {
        double[][] matrixInput = new double[users.size()][items.size()];
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                    if (users.get(i).getGroundTruth().contains(items.get(j).getPaperId())) {
                     matrixInput[i][j] = GeneralUtility.standardizeSimilarityValue(Weighting.computeCosine(users.get(i).getFeatureVector(),
                            items.get(j).getContent()));
                }
            }
        }
        return matrixInput;
    }

    /**
     * This is method compute Pearson for paper target
     *
     * @param matrixInput
     * @param paperTarget
     * @return
     * @throws Exception
     */
    public static HashMap pearsonPaperTarget(List<Paper> items,double[][] matrixInput, int paperTarget) throws Exception {
        HashMap PCC = new HashMap();
        for (int i = 0; i < matrixInput.length; i++) {
            // compute PCC paper target and cit
            if (i !=paperTarget) 
            PCC.put(items.get(i).getPaperId(),GeneralUtility.standardizePearson(PearsonCorrelation.pearsonCorrelation(matrixInput[paperTarget], matrixInput[i])));
        }
        return PCC;
    }

    /**
     * This is method impute value to full matrix
     *
     * @param matrixBuild
     * @param neighborhood
     * @return full matrix
     * @throws Exception
     */
    public static double[][] imputeFullMatrix(List <Paper> items,double[][] matrixInput, int neighbor) throws Exception {
        HashMap arrayPearson = new HashMap();
        List<String> listNeighbor = new ArrayList();
        for (int i = 0; i < matrixInput.length; i++) {
            arrayPearson = pearsonPaperTarget(items,matrixInput, i);
            listNeighbor = Knn.nearestPapers(arrayPearson,neighbor );
            double numerator = 0;
            double  sumPCCNeighbor =0;
            for (int j = 0; j < matrixInput[i].length; j++) {
                if (i == j) {
                    matrixInput[i][j] = 1.00;
                } else {
                    for (int l = 0; l < matrixInput.length; l++) {
                        for (int k = 0; k < neighbor; k++) {
                            if (listNeighbor.get(k) == items.get(l).getPaperId()) {
                                numerator +=((double) arrayPearson.get(listNeighbor.get(k)) * (matrixInput[l][j] - PearsonCorrelation.mean(matrixInput[l])));
                                sumPCCNeighbor += (double)arrayPearson.get(listNeighbor.get(k));
                            }
                        }
                    }
                   matrixInput[i][j] = PearsonCorrelation.mean(matrixInput[i]) + numerator / sumPCCNeighbor;
                   matrixInput[j][i] = matrixInput[i][j];
                }
            }
        }
        return matrixInput;
    }
}
