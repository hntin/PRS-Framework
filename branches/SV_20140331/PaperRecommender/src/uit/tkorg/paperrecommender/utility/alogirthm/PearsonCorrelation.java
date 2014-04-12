/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import uit.tkorg.paperrecommender.model.Paper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
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
            for (int j = 0; j < sizeMatrix; j++) {
                //  kiem tra xem paper j co phai la cit cua paper i hay k neu co tinh cosin dien vao matrix
                if (paperInput.get(i).getCitation().contains(paperInput.get(j).getPaperId())) {
                    matrixCit[i][j] = Weighting.computeCosine(paperInput.get(i).getContent(),
                            paperInput.get(j).getContent());
                }
            }
        }
        return matrixCit;
    }

    public static double[] averageCit(List<Paper> paperInput) throws Exception {
        double[][] matrixCit = writeCosinReal(paperInput);
        int matrixSize = paperInput.size();
        double[] average = new double[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            int n = paperInput.get(i).getCitation().size();
            for (int j = 0; j < matrixSize; j++) {
                double sum = +matrixCit[i][j];
                if(n!=0)
                average[i] = (double) sum / n;
                else 
                    average[i]=0.0;
            }
        }
        return average;
    }

    /**
     * This is method compute PCC between paperTarget and paperCit
     *
     * @param paperInput
     * @param paperTarget
     * @return
     * @throws java.lang.Exception
     */
    public static List<Double> pearsonPapertarget(List<Paper> paperInput, int paperTarget) throws Exception {
        List PCC = new ArrayList();
        double[][] matrixCit = null ;//= writeCosinReal(paperInput);
        int matrixSize = paperInput.size();
        double[] average = averageCit(paperInput);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {

                // compute PCC paper target and cit
                double covar = +((matrixCit[paperTarget][j] - average[paperTarget]) * (matrixCit[i][j] - average[i]));
                double sttdevPaperTarget = +Math.pow((matrixCit[paperTarget][j] - average[paperTarget]), 2);
                double sttdevPaperCit = +Math.pow((matrixCit[i][j] - average[i]), 2);
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
        double[][] matrixBuild  = writeCosinReal(paperInput);
        List arrayPearson = new ArrayList();
        List tempSort = new ArrayList();
        double sumNeighbor = 0;
        double[] average =  averageCit(paperInput);
        while (matrixSize != 0) {
            arrayPearson = pearsonPapertarget(paperInput, matrixSize - 1);
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
                        matrixBuild[matrixSize-1][j] = 1.00;
                    } else {
                        if (matrixBuild[matrixSize-1][j] == 0.00) {
                            for (int k = 0; k < neighborhood; k++) {

                                if (tempSort.get(k) == arrayPearson.get(i)) {
                                    matrixBuild[matrixSize-1][j] = average[i];
                                    double numerator = +((double) tempSort.get(k) * (matrixBuild[i][j] - average[i]));
                                    matrixBuild[matrixSize-1][j] = numerator / sumNeighbor;
                                    matrixBuild[j][matrixSize-1] = matrixBuild[matrixSize-1][j];
                                }
                            }
                        }
                    }
                }
            }

            matrixSize--;
        }
        return matrixBuild;
    }

    /**
     * This is method find k max element of the row in matrix
     *
     * @param a
     * @param k
     * @return
     */
    public static ArrayList<ArrayList<Double>> solveFindK(double a[][], int k) {
        ArrayList<ArrayList<Double>> ans = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            Arrays.sort(a[i]);
            ArrayList<Double> tmp = new ArrayList<>();
            for (int j = a[i].length - 1; j > a[i].length - 1 - k; j--) {
                tmp.add(a[i][j]);
            }
            ans.add(tmp);
        }
        return ans;
    }

    public static ArrayList<ArrayList<Double>> solveFindK(
            ArrayList<ArrayList<Double>> a, int k) {
        ArrayList<ArrayList<Double>> ans = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < a.get(i).size(); i++) {
            Collections.sort(a.get(i));
            ArrayList<Double> tmp = new ArrayList<>();
            for (int j = a.get(i).size() - 1; j > a.get(i).size() - 1 - k; j--) {
                tmp.add(a.get(i).get(j));
            }
            ans.add(tmp);
        }
        return ans;
    }

    public static double[][] findKMax(double a[][], int k) {
        double[][] ans = new double[a.length][k];
        for (int i = 0; i < a.length; i++) {
            Arrays.sort(a[i]);
            double[] tmp = new double[k];
            for (int j = a[i].length - 1; j > a[i].length - 1 - k; j--) {
                tmp[a[i].length - 1 - j] = a[i][j];
            }
            ans[i] = tmp;
        }
        return ans;
    }

    /**
     * This is method find all potential citation of all paper
     *
     * @param paperInput
     * @param neighbor
     * @param k
     * @throws Exception
     */
    public static void findPotentialCitPaper(List<Paper> paperInput, int neighbor, int k) throws Exception {
     
        double[][] matrixPaper = builMatrixPaperCit(paperInput, neighbor);
        double[][] tempMatrix = findKMax(matrixPaper, k);
        for (int i = 0; i < paperInput.size(); i++) {
            List potentialPaper = new ArrayList();
            for (int j = 0; j < paperInput.size(); j++) {
                for (int l = 0; l < k; l++) {
                    if (matrixPaper[i][j] == tempMatrix[i][k]) {
                        potentialPaper.add(paperInput.get(j).getPaperId());
                    }
                }
            }
            paperInput.get(i).setCitationPotential(potentialPaper);
        }
    }
}
