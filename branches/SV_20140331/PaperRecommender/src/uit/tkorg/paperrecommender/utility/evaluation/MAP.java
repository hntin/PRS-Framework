/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.evaluation;

import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;

/**
 *
 * @author Vinh-PC
 */
public class MAP {

    // Prevent instantiation.
    private MAP() {
    }

    /**
     * This method computes average precision with threshold k
     *
     * @param relList
     * @param retList
     * @param k
     * @return ap
     */
    public static double computeAP(List relList, List retList, int k) {
        double ap = 0;

        if (k > retList.size()) {
            k = retList.size();
        }

        for (int i = 0; i < k;) {
            i = i + 2;
            ap += Precision.computePrecisionK(relList, retList, i + 1);
        }
        ap = (double) ap / k;

        return ap;
    }

    /**
     * This method computes MAP for each author
     * @param authors
     * @param k
     * @return map
     */
    public static double computeMAP(HashMap<String, Author> authors, int k) {
        double map = 0;

        for (String authorId : authors.keySet()) {
            List groundTruth = authors.get(authorId).getGroundTruth();
            List recommendation = authors.get(authorId).getRecommendation();
            map += computeAP(groundTruth, recommendation, k);
        }
        map = map / authors.keySet().size();

        return map;
    }
}
