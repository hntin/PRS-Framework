/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

import java.util.List;
import uit.tkorg.paperrecommender.utility.GeneralUtility;

/**
 *
 * @author Vinh-PC
 */
public class PearsonCorrelation {

    /**
     * Return the mean of the argument values
     *
     * @param values
     * @return
     */
    public static double mean(double[] values) {
        double sum = 0;
        int count=0;
        for (double value : values) {
            if (value!=0){
                 sum = sum + value;
                 count++;
            }  
        }
        if (count==0)
            return 0;
        else
        return sum / count;
    }

    /**
     * Return the standard deviation of the argument values
     *
     * @param values
     * @return
     */
    public static double standardDeviation(double[] values) {
        double sum = 0;
        int count=0;
        double mean = mean(values);
        if (mean==0)
            return 0;
        else{
             for (double value : values) {
                sum = sum + Math.pow(value - mean, 2);
                count++;
            }
            return Math.sqrt(sum / count);
        }    
    }
    
    /**
     * Return the covariance between the vectors x and y.
     *
     * @param x
     * @param y
     * @return
     */
    public static double covariance(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Covariance error: Vectors not of the same length");
        }
        
        double sum = 0;
        int count=0;
        double xMean = mean(x);
        double yMean = mean(y);
        if (xMean ==0 || yMean==0)
            return 0;
        else{
                for (int i = 0; i < x.length; i++) {
                sum = sum + (x[i] - xMean) * (y[i] - yMean);
                count++;
            }
        return sum / count;
        }
     
    }
   
    /**
     * Return the Pearson Correlation between the vectors x and y.
     *
     * @param x
     * @param y
     * @return
     */
    public static double pearsonCorrelation(double[] x, double[] y) {
        if (standardDeviation(x)==0 ||standardDeviation(y)==0) return 0;
        else
        return covariance(x, y) / (standardDeviation(x) * standardDeviation(y));
    }
/**
 * 
 * @param averageTarget
 * @param covarNeighbor
 * @param meanNeighbor
 * @param cosine
 * @return 
 */
    public static double predictionRating(double averageTarget, double[] covarNeighbor, double[] meanNeighbor, double[] cosine) {
        double temp = 0.0;
        for (int i = 0; i < covarNeighbor.length; i++) {
            temp = +((cosine[i] - meanNeighbor[i]) * covarNeighbor[i]);   
        }
    return ( averageTarget + temp/ GeneralUtility.solveFindSumTopN(covarNeighbor));

    }
}
