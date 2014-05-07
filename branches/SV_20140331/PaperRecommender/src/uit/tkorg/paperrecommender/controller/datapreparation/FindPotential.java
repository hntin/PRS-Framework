/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.datapreparation;

import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.BuildMatrixCF;
import uit.tkorg.paperrecommender.utility.algorithm.Knn;

/**
 *
 * @author Minh
 */
public class FindPotential {

    /**
     * This method finds potential candidate papers.
     *
     * @param papers
     * @param inputMatrix
     * @param targetPaper
     * @return potentialCandidatePapers
     */
    public static HashMap findPotentialCandiatePaper(List<Paper> papers, double[][] inputMatrix, int targetPaper) {
        HashMap potentialCandidatePapers = new HashMap();

        for (int i = 0; i < papers.size(); i++) {
            if (!papers.get(targetPaper).getCitation().contains(papers.get(i).getPaperId())) {
                potentialCandidatePapers.put(papers.get(i).getPaperId(), inputMatrix[targetPaper][i]);
            }
        }
        return potentialCandidatePapers;
    }

    /**
     * This method finds potential paper for recommend paper.
     *
     * @param papers
     * @param inputMatrix
     * @param numNeighbour
     * @param numPotential
     * @throws Exception
     */
    public static void findPotentialCitationPaper(List<Paper> papers, double[][] inputMatrix, int numNeighbour, int numPotential) throws Exception {
        BuildMatrixCF.inputFullMatrix(papers, inputMatrix, numNeighbour);
        
        for (int i = 0; i < inputMatrix.length; i++) {
            HashMap potentialCandidatePapers = findPotentialCandiatePaper(papers, inputMatrix, i);
            List<String> potentialList = Knn.nearestPapers(potentialCandidatePapers, numPotential);
            papers.get(i).setPotentialCitation(potentialList);
        }
    }
}
