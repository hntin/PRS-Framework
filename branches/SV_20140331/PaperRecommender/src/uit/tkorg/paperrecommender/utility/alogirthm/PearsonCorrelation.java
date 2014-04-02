/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.utility.alogirthm;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.controller.datapreparation.PaperFV;
import uit.tkorg.paperrecommender.utility.Weighting;
import uit.tkorg.paperrecommender.utility.dataimport.flatfile.ImportDataset1;
import uit.tkorg.paperrecommender.constant.PaperRecommenerConstant;
import java.io.*;
import java.util.*;
/**
 *
 * @author Minh
 */
public class PearsonCorrelation {
     /**
     * This is a method write  cosin between paper and cit on matrix cit
     * @param list paper
     * @ return  matrix between paper and cit
     */
    
   public static double [][] buildMatrixCit (List <Paper> paperInput) throws Exception
    {
        int sizeMatrix = paperInput.size();// kich thuoc matran paper- cit
        double [][] matrixCit = new double[sizeMatrix][sizeMatrix];
        for(int i=0;i< sizeMatrix;i++)
            for (int j= 0;j< paperInput.size();j++)
            {
              //  kiem tra xem paper j co phai la cit cua paper i hay k neu co tinh cosin dien vao matrix
                if(paperInput.get(i).getCitation().contains(paperInput.get(j).getPaperId()))
                    matrixCit[i][j]= Weighting.computeCosine(paperInput.get(i).getContent(),
                            paperInput.get(j).getContent());
            }
        return matrixCit;
    }
   public static void PearsonCorrelation()
   {
       
   }
}
