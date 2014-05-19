/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.utility.general;

import uit.tkorg.pr.model.Paper;
import uit.tkorg.pr.model.Author;
import java.util.List;

/**
 * This class builds inputMatrix user-paper or paper-paper.
 *
 * @author Minh
 */
public class InputMatrix {

    /**
     * This method builds InputMatrix between paper and paper.
     *
     * @param papers
     * @return inputMatrix
     * @throws Exception
     */
    public static double[][] buildInputMatrix(List<Paper> papers) throws Exception {
        double[][] inputMatrix = new double[papers.size()][papers.size()];
        
        for (int i = 0; i < papers.size(); i++) {
            for (int j = 0; j < papers.size(); j++) {
                if (papers.get(i).getCitation().contains(papers.get(j).getPaperId())) {
                    double cosine = Weighting.computeCosine(papers.get(i).getContent(), papers.get(j).getContent());
                    inputMatrix[i][j] = GeneralUtility.standardizeSimilarityValue(cosine);
                }
            }
        }

        return inputMatrix;
    }

    /**
     * This method builds inputMatrix between user and paper.
     *
     * @param users
     * @param papers
     * @return inputMatrix
     * @throws java.lang.Exception
     */
    public static double[][] buildInputMatrix(List<Author> users, List<Paper> papers) throws Exception {

        double[][] inputMatrix = new double[users.size()][papers.size()];
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < papers.size(); j++) {
                if (users.get(i).getGroundTruth().contains(papers.get(j).getPaperId())) {
                    double cosine = Weighting.computeCosine(users.get(i).getFeatureVector(), papers.get(j).getFeatureVector());
                    inputMatrix[i][j] = GeneralUtility.standardizeSimilarityValue(cosine);
                }
            }
        }

        return inputMatrix;
    }
}
