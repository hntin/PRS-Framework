/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

import ir.utilities.Stats;
import uit.tkorg.paperrecommender.model.Paper;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import uit.tkorg.paperrecommender.utility.algorithm.Knn;

/**
 * This class build collaborative filtering (CF) matrix.
 *
 * @author Minh
 */
public class BuildMatrixCF {

    /**
     * This method inputs value to full matrix.
     *
     * @param papers
     * @param inputMatrix
     * @param numNeighbour
     * @return inputMatrix
     * @throws Exception
     */
    public static double[][] inputFullMatrix(List<Paper> papers, double[][] inputMatrix, int numNeighbour) throws Exception {
        List<HashMap> pearsonMatrix = new ArrayList();
        List<String> neighbourList = new ArrayList();

        for (int i = 0; i < inputMatrix.length; i++) {
            pearsonMatrix.add(Distance.pearsonPaper(papers, inputMatrix, i));
            neighbourList = Knn.nearestPapers(pearsonMatrix.get(i), numNeighbour);
            double numerator = 0;
            double denominator = 0;

            for (int j = 0; j < inputMatrix[i].length; j++) {
                if (i == j) {
                    inputMatrix[i][j] = 1.00;
                } else if (inputMatrix[i][j] == 0.0) {
                    for (int k = 0; k < numNeighbour; k++) {
                        for (int l = 0; l < inputMatrix.length; l++) {
                            if (neighbourList.get(k).equals(papers.get(l).getPaperId())) {
                                numerator += ((double) pearsonMatrix.get(i).get(neighbourList.get(k)) * (inputMatrix[l][j] - Stats.mean(inputMatrix[l])));
                                denominator += (double) pearsonMatrix.get(i).get(neighbourList.get(k));
                            }
                        }
                    }
                    inputMatrix[i][j] = GeneralUtility.standardizeSimilarityValue(Stats.mean(inputMatrix[i]) + numerator / denominator);
                    inputMatrix[j][i] = inputMatrix[i][j];
                }
            }
        }

        return inputMatrix;
    }

    /**
     * This method inputs value to full matrix.
     *
     * @param papers
     * @param inputMatrix
     * @param numNeighbour
     * @return inputMatrix
     * @throws Exception
     */
    public static double[][] inputFullMatrixWithCosine(List<Paper> papers, double[][] inputMatrix, int numNeighbour) throws Exception {
        List<HashMap> cosineMatrix = new ArrayList();
        List<String> neighbourList = new ArrayList();

        for (int i = 0; i < inputMatrix.length; i++) {
            cosineMatrix.add(Distance.cosinePaper(papers, inputMatrix, i));
            neighbourList = Knn.nearestPapers(cosineMatrix.get(i), numNeighbour);
            double numerator = 0;
            double denominator = 0;
            for (int j = 0; j < inputMatrix[i].length; j++) {
                if (i == j) {
                    inputMatrix[i][j] = 1.00;
                } else if (inputMatrix[i][j] == 0.0) {
                    for (int k = 0; k < numNeighbour; k++) {
                        for (int l = 0; l < inputMatrix.length; l++) {
                            if (neighbourList.get(k).equals(papers.get(l).getPaperId())) {
                                numerator += ((double) cosineMatrix.get(i).get(neighbourList.get(k)) * (inputMatrix[l][j] - Stats.mean(inputMatrix[l])));
                                denominator += (double) cosineMatrix.get(i).get(neighbourList.get(k));
                            }
                        }
                    }
                    inputMatrix[i][j] = Stats.mean(inputMatrix[i]) + numerator / denominator;
                    inputMatrix[j][i] = inputMatrix[i][j];
                }
            }
        }

        return inputMatrix;
    }
}
