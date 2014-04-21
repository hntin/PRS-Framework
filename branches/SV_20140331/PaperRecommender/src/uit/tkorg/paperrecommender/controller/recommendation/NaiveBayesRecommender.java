/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.recommendation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Vocabulary;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.alogirthm.NaiveBayesModel;
import uit.tkorg.paperrecommender.utility.alogirthm.NaiveBayes;

/**
 *
 * @author Vinh-PC
 */
public class NaiveBayesRecommender {

    /**
     *
     * @param authorsInput
     * @param papers
     * @param vocabulary
     * @return
     * @throws java.lang.Exception
     */
    public static HashMap<String, Author> buildAllRecommendationLists(HashMap<String, Author> authorsInput, HashMap<String, Paper> papers, Vocabulary vocabulary) throws Exception {
        HashMap<String, Author> authors = authorsInput;
        HashMap<String, List> result = new HashMap<>();
        HashMap<String, List> resultauthor = new HashMap<>();

        NaiveBayes naivebayes = new NaiveBayes();

        naivebayes.buildTrainingMatrix(authors, vocabulary);
        HashMap<String, Double> computePCi = naivebayes.computePCi(authors);
        NaiveBayesModel[][] bayesModel = naivebayes.bayesModel(authors, vocabulary);

        for (String paper : papers.keySet()) {
            result.put(paper, classify(computePCi, bayesModel, vocabulary, papers.get(paper)));
        }

        for (String paper : result.keySet()) {
            for (Iterator it = result.get(paper).iterator(); it.hasNext();) {
                String author = (String) it.next();
                if (!resultauthor.containsKey(author)) {
                    resultauthor.put(author, new ArrayList());
                    resultauthor.get(author).add(paper);
                } else {
                    resultauthor.get(author).add(paper);
                }
            }
        }

        for (String author : authors.keySet()) {
            authors.get(author).setRecommendation(resultauthor.get(author));
        }

        return authors;
    }
    /*for (String author : classify(computePCi, bayesModel, vocabulary, papers.get(paper))) {
     authors.get(author).getRecommendation().add(paper);
     }*/

    /**
     * This method computes probability for a paper.
     *
     * @param computePCi
     * @param bayesModel
     * @param vocabulary
     * @param paper
     * @return topAuthors
     * @throws java.lang.Exception
     */
    public static List<String> classify(HashMap<String, Double> computePCi, NaiveBayesModel[][] bayesModel, Vocabulary vocabulary, Paper paper) throws Exception {

        HashMap<String, Double> probabilities = new HashMap<>();
        List<String> topAuthors = new ArrayList<>();
        int cols = vocabulary.getVocabulary().size();
        int i = 0;

        for (String labelpci : computePCi.keySet()) {
            double prob = computePCi.get(labelpci);
            for (int j = 0; j < cols; j++) {
                if (paper.getContent().hashMap.containsKey((String) vocabulary.getVocabulary().get(j))) {
                    prob *= bayesModel[i][j].getProb_1();
                } else {
                    prob *= bayesModel[i][j].getProb_0();
                }
            }
            probabilities.put(labelpci, prob);
            i++;
        }

        probabilities = GeneralUtility.sortHashMap(probabilities);

        // Take top five papers.
        int counter = 0;
        for (String authorId : probabilities.keySet()) {
            topAuthors.add(authorId);
            counter++;
            if (counter >= 5) {
                break;
            }
        }
        return topAuthors;
    }
}
