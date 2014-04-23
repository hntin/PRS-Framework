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

public class BuildMatrixCF  {
        
    /**
     * This is method build MatrixInput
     * @param items1
     * @param items2
     * @return matrixInput
     * @throws java.lang.Exception
     */
    public static double [][] buildMatrixInput(List <Paper> items1, List<Paper> items2) throws Exception
    {
         double [][] matrixInput= new double[items1.size()][items2.size()];
        for (int i=0;i < items1.size();i++)
            for (int j=0;j<items2.size();j++)
            {
                // checked paper j is paper cit of paper i compute cosine to write to the matrix
                if (items1.get(i).getCitation().contains(items2.get(j).getPaperId())) {
                    matrixInput[i][j] = GeneralUtility.standardizeSimilarityValue(Weighting.computeCosine(items1.get(i).getContent(),
                            items2.get(j).getContent()));
                }       
            }
            return matrixInput;
    }
//       public static double[][] buildMatrixInput(List <Author> users, List <Paper> items)
//    {
//        double [][] matrixBuild= new double[users.size()][items.size()];
//        for (int i=0;i < users.size();i++)
//            for (int j=0;j<items.size();j++)
//            {
//                      
//
//            }
//            return matrixBuild;
//    }
  /**
   * This is method compute Pearson for paper target
   * @param matrixInput
   * @param paperTarget
   * @return
   * @throws Exception 
   */
    public static List<Double> pearsonPaperTarget(double[][] matrixInput, int paperTarget) throws Exception {
        List PCC = new ArrayList();
        double pccTemp = 0.0;
        for (int i = 0; i < matrixInput.length; i++) {
            for (int j = 0; j < matrixInput[i].length; j++) {
                // compute PCC paper target and cit
                if (i != paperTarget) {
                    pccTemp = GeneralUtility.standardizePearson(PearsonCorrelation.pearsonCorrelation(matrixInput[paperTarget], matrixInput[i]));
                }
                PCC.add(pccTemp);
            }
        }
        return PCC;
    }

/**
 * This is method impute value to full matrix
 * @param matrixBuild
 * @param neighborhood
 * @return full matrix
 * @throws Exception 
 */
    public static double[][] imputeFullMatrix(double[][] matrixBuild, int neighborhood) throws Exception {
        return null;
       
    }
}
