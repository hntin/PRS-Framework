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
 * This class content methods for computing metric related to AveragePrecision. Ref: 1.
 * http://www.stanford.edu/class/cs276/handouts/EvaluationNew-handout-6-per.pdf
 * 2. http://essay.utwente.nl/59711/1/MA_thesis_J_de_Wit.pdf Method: - computeAP
 *
 * @author Vinh-PC
 */
public class AveragePrecision {

    // Prevent instantiation.
    private AveragePrecision() {
    }

    /**
     * This method computes average precision with threshold k
     *
     * @param rankList
     * @param idealList
     * @param k
     * @return
     */
    public static double computeAP(List rankList, List idealList, int k) {
        double ap = 0;

        if (k > rankList.size()) {
            k = rankList.size();
        }

        for (int i = 0; i < k; i++) {
            ap += Precision.computePrecisionK(rankList, idealList, i + 1);
        }

        return (double) ap / k;
    }
}
