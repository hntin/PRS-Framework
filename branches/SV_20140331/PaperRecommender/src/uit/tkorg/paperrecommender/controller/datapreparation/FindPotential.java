/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.controller.datapreparation;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.algorithm.BuildMatrixCF;
import uit.tkorg.paperrecommender.utility.algorithm.Knn;
/**
 *
 * @author NhocZoe
 */
public class FindPotential {
      public static HashMap solveToFindTopN (List<Paper> items, double[][] matrixInput,int paper){
        HashMap tmp =new HashMap();
        for (int i=0;i< items.size();i++)
                if(!items.get(paper).getCitation().contains(items.get(i).getPaperId())){
                    tmp.put(items.get(i).getPaperId(),matrixInput[paper][i]);
                }
        return tmp;    
    }
     /**
      * This is method find potential paper for recommend paper
      * @param matrixBuild
      * @param neighbor
      * @param k
      * @throws Exception 
      */

    public static void findPotentialCitPaper(List <Paper> items,double[][] matrixBuild, int neighbor, int k) throws Exception {
        double[][] matrixPaper =BuildMatrixCF.imputeFullMatrix(items,matrixBuild,neighbor);
        for (int i = 0; i < matrixBuild.length; i++) {
            HashMap tmp = solveToFindTopN(items, matrixBuild,i);
            List <String> listPotentialPaper= Knn.nearestPapers(tmp, k);
            items.get(i).setCitationPotential(listPotentialPaper);
        }
    }
    
}
