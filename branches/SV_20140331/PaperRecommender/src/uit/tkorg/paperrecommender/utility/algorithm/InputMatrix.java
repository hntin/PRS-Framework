/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.utility.algorithm;
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
/**
 *
 * @author NhocZoe
 */
public class InputMatrix {
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
}
