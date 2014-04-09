/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import uit.tkorg.paperrecommender.model.Paper;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import uit.tkorg.paperrecommender.utility.Weighting;

/**
 *
 * @author Minh
 */
public class PearsonCorrelation {

    /**
     * This is method write cosine between paper and paper's cit
     *
     * @param paperInput
     * @return matrix with cosine
     * @throws Exception
     */
    public static double[][] writeCosinReal(List<Paper> paperInput) throws Exception {
        int sizeMatrix = paperInput.size();// kich thuoc matran paper- cit
        double[][] matrixCit = new double[sizeMatrix][sizeMatrix];
        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < paperInput.size(); j++) {
                //  kiem tra xem paper j co phai la cit cua paper i hay k neu co tinh cosin dien vao matrix
                if (paperInput.get(i).getCitation().contains(paperInput.get(j).getPaperId())) {
                    matrixCit[i][j] = Weighting.computeCosine(paperInput.get(i).getContent(),
                            paperInput.get(j).getContent());
                }
                else matrixCit[i][j]=0.00;
            }
        }
        return matrixCit;
    }

    public static double[] averageCit(List<Paper> paperInput) throws Exception {
        double[][] matrixCit = writeCosinReal(paperInput);
        int matrixSize = paperInput.size();
        double[] average = new double[matrixSize];
        // average = new double[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int n = paperInput.get(i).getCitation().size();
                double sum = +matrixCit[i][j];
                average[i] = sum / n;
            }
        }
        return average;
    }

    /**
     * This is method compute PCC between paperTarget and paperCit
     *
     * @param paperInput
     * @param matrixCit
     * @param paperTarget
     * @return
     */
    public static List<Double> pearsonPapertarget(List<Paper> paperInput, int paperTarget) throws Exception {
        List PCC = new ArrayList();
        double[][] matrixCit = writeCosinReal(paperInput);
        int matrixSize = paperInput.size();
        double[] average = averageCit(paperInput);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {

                // compute PCC paper target and cit
                double covar = +(matrixCit[paperTarget][j] - average[paperTarget]) * (matrixCit[i][j] - average[i]);
                double sttdevPaperTarget = +Math.pow((matrixCit[paperTarget][j] - average[i]), 2);
                double sttdevPaperCit = +Math.pow((matrixCit[i][j] - average[i + 1]), 2);
                double pccTemp = covar / Math.sqrt(sttdevPaperTarget * sttdevPaperCit);

                PCC.add(pccTemp);
            }
        }
        return PCC;
    }

    //moi paper recommend se la  mot bai bao muc tieu, tìm bài báo muc tieu cua moi paper luu vao author
    // viet ham chon so lang gieng  cho mỗi paper ung vien co trong dataset
    //compute gia tri dien vao ma tran
    // kiem tra neu nhung paper trong tap hang xom co trong cit cua paper target thi bo qua
    //
    public static double[][] builMatrixPaperCit(List<Paper> paperInput, int neighborhood) throws Exception {
        int matrixSize = paperInput.size();
        double[][] matrixBuild = writeCosinReal(paperInput);
        List arrayPearson = new ArrayList();
        List tempSort = new ArrayList();
        double sumNeighbor = 0;
        double[] average = averageCit(paperInput);
        while (matrixSize != 0) {
            arrayPearson = pearsonPapertarget(paperInput, matrixSize);
            tempSort = arrayPearson;
            Collections.sort(tempSort, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    if (o1.hashCode() < o2.hashCode()) {
                        return 1;
                    } else if (o1 == o2) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            for (int k = 0; k < neighborhood; k++) {
                sumNeighbor = +(double) tempSort.get(k);
            }
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    if (matrixSize == j) {
                        matrixBuild[matrixSize][j] = 1.00;
                    } else {
                        if(matrixBuild[matrixSize][j]== 0.00)
                        for (int k = 0; k < neighborhood; k++) {

                            if (tempSort.get(k) == arrayPearson.get(i)) {
                                matrixBuild[matrixSize][j] = average[i];
                                double numerator =+ ((double) tempSort.get(k) * (matrixBuild[i][j] - average[i]));
                                matrixBuild[matrixSize][j]=numerator/sumNeighbor;
                                matrixBuild[j][matrixSize]=matrixBuild[matrixSize][j];
                            }
                        }
                    }
                }
            }

            matrixSize--;
        }
        return matrixBuild;
    }
    // tim paper Potential
    public static List <Paper> findPotentialCitPaper(List<Paper> paperInput, int neighbor, int k) throws Exception
    {
        List  potentialPaper= new ArrayList();
        double [][] matrixPaper= builMatrixPaperCit (paperInput, neighbor);
        for (int i=0;i<paperInput.size();i++)
          for (int j=0; j< paperInput.size();j++)
          {
              
          }
            
        return potentialPaper;
        
       
        
    }
}
