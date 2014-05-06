/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility;

import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Author;

/**
 *
 * @author Minh
 */
public class Distance {

    /**
     * This method computes Pearson correlation coefficient (PCC) between paper
     * and paper.
     *
     * @param papers
     * @param inputMatrix
     * @param targetPaper
     * @return PCC
     * @throws Exception
     */
    public static HashMap pearsonPaper(List<Paper> papers, double[][] inputMatrix, int targetPaper) throws Exception {
        HashMap PCC = new HashMap();

        for (int i = 0; i < papers.size(); i++) {
            if (i != targetPaper) {
                double pearson = PearsonCorrelation.pearsonCorrelation(inputMatrix[targetPaper], inputMatrix[i]);
                PCC.put(papers.get(i).getPaperId(), GeneralUtility.standardizePearsonValue(pearson));
            }
        }

        return PCC;
    }

    /**
     * This method computes Pearson correlation coefficient (PCC) between user
     * and user.
     *
     * @param users
     * @param inputMatrix
     * @param targetUser
     * @return PCC
     * @throws Exception
     */
    public static HashMap pearsonUser(List<Author> users, double[][] inputMatrix, int targetUser) throws Exception {
        HashMap PCC = new HashMap();

        for (int i = 0; i < users.size(); i++) {
            if (i != targetUser) {
                double pearson = PearsonCorrelation.pearsonCorrelation(inputMatrix[targetUser], inputMatrix[i]);
                PCC.put(users.get(i).getAuthorId(), GeneralUtility.standardizePearsonValue(pearson));
            }
        }

        return PCC;
    }

    /**
     * This method computes similarity (Sim) between paper and paper.
     *
     * @param papers
     * @param inputMatrix
     * @param targetPaper
     * @return
     * @throws Exception
     */
    public static HashMap cosinePaper(List<Paper> papers, double[][] inputMatrix, int targetPaper) throws Exception {
        HashMap Sim = new HashMap();

        for (int i = 0; i < papers.size(); i++) {
            if (i != targetPaper) {
                double cosine = Weighting.computeCosine(papers.get(targetPaper).getFeatureVector(), papers.get(i).getFeatureVector());
                Sim.put(papers.get(i).getPaperId(), GeneralUtility.standardizeSimilarityValue(cosine));
            }
        }

        return Sim;
    }

    /**
     * This method computes similarity (Sim) between user and user.
     *
     * @param users
     * @param matrixInput
     * @param targetUser
     * @return
     * @throws Exception
     */
    public static HashMap cosineUser(List<Author> users, double[][] matrixInput, int targetUser) throws Exception {
        HashMap Sim = new HashMap();

        for (int i = 0; i < users.size(); i++) {
            if (i != targetUser) {
                double cosine = Weighting.computeCosine(users.get(targetUser).getFeatureVector(), users.get(i).getFeatureVector());
                Sim.put(users.get(i).getAuthorId(), GeneralUtility.standardizeSimilarityValue(cosine));
            }
        }

        return Sim;
    }
}