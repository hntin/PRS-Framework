/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.evaluation;

import java.io.Serializable;
import java.util.HashMap;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.utility.evaluation.NDCG;
import uit.tkorg.paperrecommender.utility.evaluation.ReciprocalRank;

/**
 * This class handles all logics for evaluation of recommendation results.
 * Method:
 * - NDCG: 
 * + input: authors' ground truth list and recommendation list, n where NDCG computed at.
 * + output: NDCG.
 * - MRR: 
 * + input: authors' ground truth list and recommendation list.
 * + output: MRR.
 * @author THNghiep
 */
public class Evaluator implements Serializable {

    private HashMap<String, Author> authors;

    public Evaluator() {
        this.authors = null;
    }
    
    /**
     * This method computes NDCG at position n.
     * If n == 5 or 10 then save ndcg to author list.
     * @param authors
     * @param n
     * @return ndcg
     */
    public double NDCG(HashMap<String, Author> authorsInput, int n) {
        double ndcg = 0;
        
        double currentNDCG = 0;
        this.authors = authorsInput;
        if (n == 5) {
            for (String key : authorsInput.keySet()) {
                currentNDCG = NDCG.computeNDCG(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth(), n);
                authors.get(key).setNdcg5(currentNDCG);
                ndcg += currentNDCG;
            }
        } else if (n == 10) {
            for (String key : authorsInput.keySet()) {
                currentNDCG = NDCG.computeNDCG(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth(), n);
                authors.get(key).setNdcg10(currentNDCG);
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
     * This method computes MRR.
     * @param authors
     * @return 
     */
    public double MRR(HashMap<String, Author> authorsInput) {
        double mrr = 0;

        double currentRR = 0;
        this.authors = authorsInput;
        for (String key : authorsInput.keySet()) {
            currentRR = ReciprocalRank.computeRR(authorsInput.get(key).getRecommendation(), authorsInput.get(key).getGroundTruth());
            authors.get(key).setRr(currentRR);
            mrr += currentRR;
        }
        mrr = mrr / authorsInput.size();

        return mrr;
    }

    /**
     * @return the authors
     */
    public HashMap<String, Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(HashMap<String, Author> authors) {
        this.authors = authors;
    }
}
