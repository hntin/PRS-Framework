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
public class Recall {

    // Prevent instantiation.
    private Recall() {
    }

    /**
     * This method computes recall based on relevant documents retrieved and
     * total relevant documents
     *
     * @param relList
     * @param retList
     * @return rec
     */
    public static double computeRecall(List relList, List retList) {
        double rec = 0;

        for (int i = 0; i < relList.size(); i++) {
            if (retList.contains(relList.get(i))) {
                rec++;
            }
        }
        rec = (double) rec / relList.size();
        
        return rec;
    }
}
