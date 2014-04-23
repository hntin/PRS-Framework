/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.controller.datapreparation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.alogirthm.BuildMatrixCF;


/**
 *
 * @author NhocZoe
 */
public class findPotentialPaper {
     /**
     * This is method find all potential citation of all paper
     *
     * @param paperInput
     * @param matrixBuild
     * @param average
     * @param neighbor
     * @param k
     * @throws Exception
     */

//    public static void findPotentialCitPaper(List<Paper> paperInput, double[][] matrixBuild, double[] average, int neighbor, int k) throws Exception {
//
//        double[][] matrixPaper = builMatrixPaperCit(paperInput, matrixBuild, average, neighbor);
//        double[][] tempMatrix = findKMax(matrixPaper, k);
//        for (int i = 0; i < paperInput.size(); i++) {
//            List potentialPaper = new ArrayList();
//            for (int j = 0; j < paperInput.size(); j++) {
//                for (int l = 0; l < k; l++) {
//                    if (matrixPaper[i][j] == tempMatrix[i][l]) {
//                        potentialPaper.add(paperInput.get(j).getPaperId());
//                        paperInput.get(i).setCitationPotential(potentialPaper);
//                    }
//                }
//            }
//        }
//    }
}
