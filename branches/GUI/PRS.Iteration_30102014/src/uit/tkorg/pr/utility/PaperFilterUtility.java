/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.utility;

import java.util.HashMap;
import java.util.HashSet;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.utility.general.WeightingUtility;

/**
 *
 * @author THNghiep
 */
public class PaperFilterUtility {
    
    private PaperFilterUtility() {}
    
    public static void filterOldPaper(HashMap<String, Author> authors, 
            HashMap<String, Paper> papers, 
            int cutYear) throws Exception {

        for (String idPaper : papers.keySet()) {
            if (papers.get(idPaper).getYear() < cutYear) {
                for (String idAuthor : authors.keySet()) {
                    authors.get(idAuthor).getCbfSimHM().remove(idPaper);
                    authors.get(idAuthor).getCfRatingHM().remove(idPaper);
                    authors.get(idAuthor).getCbfCfHybridHM().remove(idPaper);
                    authors.get(idAuthor).getTrustedPaperHM().remove(idPaper);
                    authors.get(idAuthor).getCbfTrustHybridHM().remove(idPaper);
                    authors.get(idAuthor).getCbfTrustHybridV2HM().remove(idPaper);
                    authors.get(idAuthor).getFinalRecommendingScoreHM().remove(idPaper);
                }
            }
        }
    }

    public static void filterPaperRatingListByTestSet(HashMap<String, Author> authors, 
            HashSet<String> paperIdsInTestSet) throws Exception {

        for (Author authorObj : authors.values()) {
            for (String idPaper : authorObj.getCbfSimHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getCbfSimHM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getCfRatingHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getCfRatingHM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getCbfCfHybridHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getCbfCfHybridHM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getTrustedPaperHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getTrustedPaperHM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getCbfTrustHybridHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getCbfTrustHybridHM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getCbfTrustHybridV2HM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getCbfTrustHybridV2HM().remove(idPaper);
                }
            }
            for (String idPaper : authorObj.getFinalRecommendingScoreHM().keySet()) {
                if (!paperIdsInTestSet.contains(idPaper)) {
                    authorObj.getFinalRecommendingScoreHM().remove(idPaper);
                }
            }
        }
    }
}
