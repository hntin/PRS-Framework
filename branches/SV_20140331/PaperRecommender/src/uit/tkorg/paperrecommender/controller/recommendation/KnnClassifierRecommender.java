/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.utility.alogirthm.AlgorithmKnn;

/**
 * This class handles logic for recommending papers to each author.
 *
 * @author Vinh-PC
 */
public class KnnClassifierRecommender {

    // Prevent instantiation.
    private KnnClassifierRecommender() throws Exception {
    }

    /**
     * This method builds recommend list for all authors.
     *
     * @param authors
     * @param k
     * @return
     * @throws Exception
     */
    public static HashMap<String, Author> buildAllRecommendationLists(HashMap<String, Author> authors, int k) throws Exception {
        HashMap<String, HashMap> knnList = AlgorithmKnn.buildAllListOfSimilarity(authors, k);
        for (String authorId : authors.keySet()) {
            authors.get(authorId).setRecommendation(buildRecommdationList(authors, knnList,authorId));
        }
        return authors;
    }

    /**
     * This method builds recommend list for an author.
     *
     * @param authors
     * @param knnList
     * @param authorId
     * @return recommendationPapers
     */
    public static List<String> buildRecommdationList(HashMap<String, Author> authors, HashMap<String, HashMap> knnList, String authorId) {
        List<String> recommendationPapers = new ArrayList();
        for (Object authorNN : knnList.get(authorId).keySet()) {
            recommendationPapers.addAll(authors.get((String) authorNN).getGroundTruth());
        }
        //recommendationPapers=getCommonPapers(recommendationPapers, knnList.size());
        return recommendationPapers;
    }

    /**
     * This method gets recommended paper for an author.
     *
     * @param recommendationPapers
     * @param k
     * @return
     */
    public static List<String> getCommonPapers(List<String> recommendationPapers, int k) {
        List<String> papers = new ArrayList<>();
        for (String paperId : recommendationPapers) {
            if (Collections.frequency(recommendationPapers, paperId) == k) {
                papers.add(paperId);
            }
        }
        return papers;
    }
}
