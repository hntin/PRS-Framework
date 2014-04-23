/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.utility.alogirthm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
/**
 *
 * @author NhocZoe
 */
public class TopNNeighbor {
    
    /**
     * This is method find k max element of the row in matrix
     *
     * @param a
     * @param k
     * @return
     */
    public static ArrayList<ArrayList<Double>> solveFindTopN(double a[][], int k) {
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

    public static ArrayList<ArrayList<Double>> solveFindTopN(
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

    public static double[][] solefindTopN(double a[][], int k) {
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
    public static double solveFindSumTopN(List a, int k) {
        double sum;
        Collections.sort(a);
        List temp = new ArrayList();
        for (int i = a.size() - 1; i > a.size() - 1 - k; i--) {
            temp.add(a.get(i));
        }
        for (int i = 0; i < temp.size(); i++) {
            sum = +(double) temp.get(i);
        }
        return 0;
    }

}
