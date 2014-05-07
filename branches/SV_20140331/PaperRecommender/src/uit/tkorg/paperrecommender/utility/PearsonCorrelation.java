/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

/**
 * @author Minh
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
//        int count=0;
        double mean = mean(values);
        if (mean==0)
            return 0;
        else{
             for (double value : values) {
                if(value !=0)
                sum = sum + Math.pow(value - mean, 2);
//                count++;
            }
            return Math.sqrt(sum);
//            return Math.sqrt(sum / count);
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
        double xMean = mean(x);
        double yMean = mean(y);
        if (xMean ==0 || yMean==0)
            return 0;
        else{ 
                for (int i = 0; i < x.length; i++) {
                if (x[i]!=0 && y[i]!=0)
                sum = sum + (x[i] - xMean) * (y[i] - yMean);
            }
        return sum ;// x.length;
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
     * @param sumK
=======
 * @return 
 */
    public static double solveFindSumTopN(double [] sumK) {
        double sum=0.0;
        for (int i = 0; i < sumK.length; i++) {
            sum = + sumK[i];
        }
        return sum;
    }
    public static double predictionRating(double meanTarget, double[] covarNeighbor, double[] meanNeighbor, double [] rating) {
        double temp = 0.0;
        for (int i = 0; i < covarNeighbor.length; i++) {
            temp = +((rating[i] - meanNeighbor[i]) * covarNeighbor[i]);   
        }
    return ( meanTarget + temp/ solveFindSumTopN(covarNeighbor));
    }
}
