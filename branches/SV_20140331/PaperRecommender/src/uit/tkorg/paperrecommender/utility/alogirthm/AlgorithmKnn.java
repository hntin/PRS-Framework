/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import java.util.HashMap;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.Weighting;

/**
 *
 * @author Minh
 */
public class AlgorithmKnn {

    /**
     * This method computes similarity between an author and an author.
     *
     * @param author1
     * @param author2
     * @return similarity
     * @throws java.lang.Exception
     */
    public static double computeSimilarity(Author author1, Author author2) throws Exception {
        double similarity = 0;

        similarity = Weighting.computeCosine(author1.getFeatureVector(), author2.getFeatureVector());

        return similarity;
    }

    /**
     * This method builds similarity list for an author.
     *
     * @param authors
     * @param author1
     * @return hashMapSim
     * @throws Exception
     */
    public static HashMap<String, Double> buildListOfSimilarity(HashMap<String, Author> authors, Author author1) throws Exception {
        HashMap<String, Double> hashMapSim = new HashMap<>();

        for (String authorId : authors.keySet()) {
            if (!author1.getAuthorId().equals(authorId)) {
                hashMapSim.put(authorId, computeSimilarity(author1, authors.get(authorId)));
            }
        }

        return hashMapSim;
    }

    /**
     * This method builds similarity list for all authors.
     *
     * @param authors
     * @param k
     * @return
     * @throws Exception
     */
    public static HashMap<String, HashMap> buildAllListOfSimilarity(HashMap<String, Author> authors, int k) throws Exception {
        HashMap<String, HashMap> hashMapSims = new HashMap<>();

        for (String authorId : authors.keySet()) {
            HashMap<String, Double> temp = buildListOfSimilarity(authors, authors.get(authorId));
            hashMapSims.put(authorId, Knn(GeneralUtility.sortHashMap(temp),k));
        }

        return hashMapSims;
    }

    public static HashMap<String, Double> Knn(HashMap<String, Double> temp, int k) {
        HashMap<String, Double> hashMapK = new HashMap<>();

        // Take top k papers.
        int counter = 0;
        for (String paperId : temp.keySet()) {
            hashMapK.put(paperId, temp.get(paperId));
            counter++;
            if (counter >= k) {
                break;
            }
        }

        return hashMapK;
    }
}
