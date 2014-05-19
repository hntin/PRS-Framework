/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.evaluation;

import java.util.HashMap;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.utility.evaluation.AveragePrecision;
import uit.tkorg.pr.utility.evaluation.NDCG;
import uit.tkorg.pr.utility.evaluation.Precision;
import uit.tkorg.pr.utility.evaluation.Recall;
import uit.tkorg.pr.utility.evaluation.ReciprocalRank;

/**
 * This class handles all logics for evaluation of recommendation results.
 * Method: - NDCG: + input: authors' ground truth list and recommendation list,
 * n where NDCG computed at. + output: NDCG. - MRR: + input: authors' ground
 * truth list and recommendation list. + output: MRR.- Precision: + input:
 * authors' ground truth list and recommendation list. + output: Precision. -
 * Recall: + input: authors' ground truth list and recommendation list. +
 * output: Recall. - MAP: + input: authors' ground truth list and recommendation
 * list. + output: MAP.
 *
 * @author THNghiep
 */
public class Evaluator {

    // Prevent instantiation.
    private Evaluator() {
    }

    /**
     * This method computes NDCG at position n. If n == 5 or 10 then save ndcg
     * to author list. Note: this method return the ndcg value and change the
     * author hashmap input directly.
     *
     * @param authorsInput
     * @param n
     * @return ndcg
     * @throws java.lang.Exception
     */
    public static double NDCG(HashMap<String, Author> authorsInput, int n) throws Exception {
        double ndcg = 0;

        double currentNDCG = 0;
        if (n == 5) {
            for (String key : authorsInput.keySet()) {
                currentNDCG = NDCG.computeNDCG(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth(), n);
                authorsInput.get(key).setNdcg5(currentNDCG);
                ndcg += currentNDCG;
            }
        } else if (n == 10) {
            for (String key : authorsInput.keySet()) {
                currentNDCG = NDCG.computeNDCG(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth(), n);
                authorsInput.get(key).setNdcg10(currentNDCG);
                ndcg += currentNDCG;
            }

        } else {
            for (String key : authorsInput.keySet()) {
                ndcg += NDCG.computeNDCG(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth(), n);
            }
        }
        // Compute average.
        ndcg = ndcg / authorsInput.size();

        return ndcg;
    }

    /**
     * This method computes MRR. Note: this method return the mrr value and
     * change the author hashmap input directly.
     *
     * @param authorsInput
     * @return
     * @throws java.lang.Exception
     */
    public static double MRR(HashMap<String, Author> authorsInput) throws Exception {
        double mrr = 0;

        double currentRR = 0;
        for (String key : authorsInput.keySet()) {
            currentRR = ReciprocalRank.computeRR(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth());
            authorsInput.get(key).setRr(currentRR);
            mrr += currentRR;
        }
        mrr = mrr / authorsInput.size();

        return mrr;
    }

    /**
     * This method computes Precision. Note: this method return the precision
     * value and change the author hashmap input directly.
     *
     * @param authorsInput
     * @return
     */
    public static double Precision(HashMap<String, Author> authorsInput) {
        double prec = 0;

        double currentPREC = 0;

        for (String authorId : authorsInput.keySet()) {
            currentPREC = Precision.computePrecision(authorsInput.get(authorId).getRecommendation(), authorsInput.get(authorId).getGroundTruth());
            authorsInput.get(authorId).setPrecision(currentPREC);
            prec += currentPREC;
        }

        return prec / authorsInput.size();
    }

    /**
     * This method computes Recall. Note: this method return the recall value
     * and change the author hashmap input directly.
     *
     * @param authorsInput
     * @return
     */
    public static double Recall(HashMap<String, Author> authorsInput) {
        double rec = 0;

        double currentREC = 0;

        for (String authorId : authorsInput.keySet()) {
            currentREC = Recall.computeRecall(authorsInput.get(authorId).getRecommendation(), authorsInput.get(authorId).getGroundTruth());
            authorsInput.get(authorId).setRecall(currentREC);
            rec += currentREC;
        }

        return rec / authorsInput.size();
    }

    /**
     * This method computes Map. Note: this method return the map value and
     * change the author hashmap input directly.
     *
     * @param authorsInput
     * @param k
     * @return
     */
    public static double MAP(HashMap<String, Author> authorsInput, int k) {
        double map = 0;

        double currentMAP = 0;

        for (String authorId : authorsInput.keySet()) {
            currentMAP = AveragePrecision.computeAP(authorsInput.get(authorId).getRecommendation(), authorsInput.get(authorId).getGroundTruth(), k);
            authorsInput.get(authorId).setMAP(currentMAP);
            map += currentMAP;
        }

        return map / authorsInput.size();
    }
}
