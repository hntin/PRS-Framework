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
    

    public static List solveFindTopN(double [] a, int k) {
         Arrays.sort(a);
         List tmp = new ArrayList();
                for (int j = a.length - 2; j > a.length - 2 - k; j--) 
                     tmp.add(a[j]); 
            return tmp ;
    }
   
   public static List solveFindTopN(List a, int k) {
        List ans = new ArrayList();
            Collections.sort(a);
            for (int j = a.size() - 2; j > a.size() - 2 - k; j--) {
                 ans.add(a.get(j));    
    }
       return ans;
   }
    public static double solveFindSumTopN(double [] sumK) {
        double sum=0.0;
        for (int i = 0; i < sumK.length; i++) {
            sum = + sumK[i];
        }
        return sum;
    }
 public static double solveFindSumTopN(List<Double> sumK) {
        double sum=0.0;
        for (int i = 0; i < sumK.size(); i++) {
            sum = + sumK.get(i);
        }
        return sum;
    }
}
