/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.rec.cb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;

/**
 * This class handles logic for recommending papers to each author.
 *
 * @author Vinh-PC
 */
public class KnnRecommender {

    // Prevent instantiation.
    private KnnRecommender() throws Exception {
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
     //   HashMap<String, HashMap> knnList = Knn.buildAllListOfSimilarity(authors, k);
        for (String authorId : authors.keySet()) {
          //  authors.get(authorId).setRecommendation(buildRecommdationList(authors, knnList, authorId));
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

   
}
