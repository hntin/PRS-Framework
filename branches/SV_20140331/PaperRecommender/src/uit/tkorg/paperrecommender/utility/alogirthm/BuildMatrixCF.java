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
import uit.tkorg.paperrecommender.utility.Weighting;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.PearsonCorrelation;

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
    public static List<Double> pearsonPaperTarget(double[][] matrixInput, int paperTarget) throws Exception {
        List PCC = new ArrayList();
        for (int i = 0; i < matrixInput.length; i++) {
                // compute PCC paper target and cit
            // if (i !=paperTarget) {
            PCC.add(GeneralUtility.standardizePearson(PearsonCorrelation.pearsonCorrelation(matrixInput[paperTarget], matrixInput[i])));
            //  }
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
    public static double[][] imputeFullMatrix(double[][] matrixInput, int neighborhood) throws Exception {
        List arrayPearson = new ArrayList();
        List<Double> neighbor = new ArrayList();
        for (int i = 0; i < matrixInput.length; i++) {
            arrayPearson = pearsonPaperTarget(matrixInput, i);
          //  neighbor = TopNNeighbor.solveFindTopN(arrayPearson, neighborhood);
            double numerator = 0;
            for (int j = 0; j < matrixInput[i].length; j++) {
                if (i == j) {
                    matrixInput[i][j] = 1.00;
                } else {
                    for (int l = 0; l < matrixInput.length; l++) {
                        for (int k = 0; k < neighborhood; k++) {
                            if (neighbor.get(k) == arrayPearson.get(l)) {
                                numerator = +((double) neighbor.get(k) * (matrixInput[l][j] - PearsonCorrelation.mean(matrixInput[l])));
                            }
                        }
                    }
                  //   matrixInput[i][j] = PearsonCorrelation.mean(matrixInput[i]) + numerator / TopNNeighbor.solveFindSumTopN(neighbor);
                     matrixInput[j][i] = matrixInput[i][j];
                }
            }
        }
        return matrixInput;
    }
}
