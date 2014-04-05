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
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 *
 * @author Minh
 */
public class PearsonCorrelation {
     /**
      * This is method write cosine between paper and paper's cit
      * @param paperInput
      * @return matrix with cosine 
      * @throws Exception 
      */
    
   public static double [][] writeCosinReal (List <Paper> paperInput) throws Exception
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
   /**
    * This is method compute PCC between paperTarget and paperCit
    * @param paperInput
    * @param matrixCit
    * @param paperTarget
    * @return 
    */
   public static List<Double> pearsonPapertarget(List <Paper> paperInput,int paperTarget) throws Exception
   {
      List PCC = new ArrayList();
      double [] average= null;
      double[][] matrixCit= writeCosinReal(paperInput);
      int matrixSize= paperInput.size();
    
      for (int i=0;i< matrixSize;i++)
          for(int j=0;j< matrixSize;j++)
          {
            int n=  paperInput.get(i).getCitation().size();
            double sum =+ matrixCit[i][j];
            average[i] =sum/n;
           // compute PCC paper target and cit
           double covar = +(matrixCit[paperTarget][j]- average[paperTarget])*(matrixCit[i][j]- average[i]);
		   double sttdevPaperTarget =+ Math.pow((matrixCit[paperTarget][j]- average[i]),2);
		   double sttdevPaperCit =+ Math.pow((matrixCit[i][j]- average[i+1]),2);
		   double pccTemp = covar/Math.sqrt(sttdevPaperTarget*sttdevPaperCit);
		   PCC.add(pccTemp);
          }
      return PCC;
   }
   //moi paper recommend se la  mot bai bao muc tieu, tìm bài báo muc tieu cua moi paper luu vao author
// viet ham chon so lang gieng  cho mỗi paper ung vien co trong dataset
   //compute gia tri dien vao ma tran
   // kiem tra neu nhung paper trong tap hang xom co trong cit cua paper target thi bo qua
   //
   public static double [][] builMatrixPaperCit(List<Paper> paperInput, int neighborhood) throws Exception
   {
       int matrixSize = paperInput.size();
       double [][] matrixBuild= new double [matrixSize][matrixSize];
       List arrayPearson= new ArrayList();
       while(matrixSize !=0){
           for(int i=0;i<matrixSize;i++)
               for (int j=0; j< matrixSize; j++)
           {
             arrayPearson = pearsonPapertarget(paperInput,i);
             Collections.sort(arrayPearson);
               
           }
      }
      
       return matrixBuild;
       
   
    }
}
