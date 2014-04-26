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
import static uit.tkorg.paperrecommender.utility.alogirthm.BuildMatrixCF.imputeFullMatrix;
import uit.tkorg.paperrecommender.utility.alogirthm.TopNNeighbor;
import static uit.tkorg.paperrecommender.utility.alogirthm.TopNNeighbor.*;


/**
 *
 * @author NhocZoe
 */
public class FindPotentialPaper {
     /**
      * This is method find potential paper for recommend paper
      * @param matrixBuild
      * @param neighbor
      * @param k
      * @throws Exception 
      */

    public static void findPotentialCitPaper(List <Paper> items,double[][] matrixBuild, int neighbor, int k) throws Exception {
        double[][] matrixPaper =imputeFullMatrix(matrixBuild,neighbor);
        for (int i = 0; i < matrixBuild.length; i++) {
            List potentialPaper = new ArrayList();
            List <Double> tmp= TopNNeighbor.solveFindTopN(matrixPaper[i], k);
            for (int j = 0; j < matrixBuild.length; j++) {
                for (int l = 0; l < k; l++) {
                    if (tmp.get(l) == matrixPaper[i][j]) {
                        potentialPaper.add(items.get(j).getPaperId());
                    }
                }
            }
            items.get(i).setCitationPotential(potentialPaper);
        }
    }
}
