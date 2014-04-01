/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.evaluation;

import java.util.List;

/**
 *
 * @author Vinh-PC
 */
public class Precision {

    // Prevent instantiation.
    private Precision() {
    }

    /**
     * This method computes precision based on relevant documents retrieved and
     * total retrieved documents
     *
     * @param relList
     * @param retList
     * @return prec
     */
    public static double computePrecision(List relList, List retList) {
        double prec = 0;

        for (int i = 0; i < relList.size(); i++) {
            if (retList.contains(relList.get(i))) {
                prec++;
            }
        }
        prec = (double) prec / retList.size();

        return prec;
    }

    /**
     * This method computes precision with threshold k based on relevant documents retrieved and k retrieved documents
     * @param relList
     * @param retList
     * @param k
     * @return preck
     */
    public static double computePrecisionK(List relList, List retList, int k) {
        double preck = 0;
        
        if (k > retList.size()) {
            k = retList.size();
        }
        
        for (int i = 0; i < k; i++) {
            if (relList.contains(retList.get(i))) {
                preck++;
            }
        }
        preck = (double) preck / k;

        return preck;
    }
}
