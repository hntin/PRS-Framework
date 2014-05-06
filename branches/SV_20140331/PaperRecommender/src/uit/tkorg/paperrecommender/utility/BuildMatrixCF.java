/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

import uit.tkorg.paperrecommender.model.Paper;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.PearsonCorrelation;
import uit.tkorg.paperrecommender.utility.algorithm.Knn;

/**
 *
 * @author Minh
 */
public class BuildMatrixCF {

    /**
     * This is method impute value to full matrix
     *
     * @param items
     * @param matrixInput
     * @param neighbor
     * @return full matrix
     * @throws Exception
     */
    public static double[][] imputeFullMatrix(List<Paper> items, double[][] matrixInput, int neighbor) throws Exception {
        //  HashMap arrayPearson = new HashMap();
        List<HashMap> arrayPearson = new ArrayList();
        List<String> listNeighbor = new ArrayList();
        for (int i = 0; i < matrixInput.length; i++) {
            arrayPearson.add(Distance.pearsonPaper(items, matrixInput, i));
            listNeighbor = Knn.nearestPapers(arrayPearson.get(i), neighbor);
            double numerator = 0;
            double sumPCCNeighbor = 0;
            for (int j = 0; j < matrixInput[i].length; j++) {
                if (i == j) {
                    matrixInput[i][j] = 1.00;
                } else {
                    for (int l = 0; l < matrixInput.length; l++) {
                        for (int k = 0; k < neighbor; k++) {
                            if (listNeighbor.get(k).equals(items.get(l).getPaperId())) {
                                numerator += ((double) arrayPearson.get(i).get(listNeighbor.get(k)) * (matrixInput[l][j] - PearsonCorrelation.mean(matrixInput[l])));
                                sumPCCNeighbor += (double) arrayPearson.get(i).get(listNeighbor.get(k));
                            }
                        }
                    }
                    matrixInput[i][j] = GeneralUtility.standardizeSimilarityValue(PearsonCorrelation.mean(matrixInput[i]) + numerator / sumPCCNeighbor);
                    matrixInput[j][i] = matrixInput[i][j];
                }
            }
        }
        return matrixInput;
    }

    public static double[][] imputeFullMatrixWithCosine(List<Paper> items, double[][] matrixInput, int neighbor) throws Exception {
        HashMap distances = new HashMap();
        List<String> listNeighbor = new ArrayList();
        for (int i = 0; i < matrixInput.length; i++) {
            distances = Distance.cosinePaper(items, matrixInput, i);
            listNeighbor = Knn.nearestPapers(distances, neighbor);
            double numerator = 0;
            double sumPCCNeighbor = 0;
            for (int j = 0; j < matrixInput[i].length; j++) {
                if (i == j) {
                    matrixInput[i][j] = 1.00;
                } else {
                    for (int l = 0; l < matrixInput.length; l++) {
                        for (int k = 0; k < neighbor; k++) {
                            if (listNeighbor.get(k).equals(items.get(l).getPaperId())) {
                                numerator += ((double) distances.get(listNeighbor.get(k)) * (matrixInput[l][j] - PearsonCorrelation.mean(matrixInput[l])));
                                sumPCCNeighbor += (double) distances.get(listNeighbor.get(k));
                            }
                        }
                    }
                    matrixInput[i][j] = PearsonCorrelation.mean(matrixInput[i]) + numerator / sumPCCNeighbor;
                    //   matrixInput[j][i] = matrixInput[i][j];
                }
            }
        }
        return matrixInput;

    }

}
