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
import java.util.Vector;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Vocabulary;
import uit.tkorg.paperrecommender.utility.alogirthm.NaiveBayes;

/**
 *
 * @author Vinh-PC
 */
public class NaiveBayesRecommender {

   /**
     * This method builds recommendation list for all authors.
     *
     * @param authorsInput
     * @param papers
     * @param vocabulary
     * @return
     * @throws Exception
     */
    public static HashMap<String, Author> buildALLRecommendationLists(HashMap<String, Author> authorsInput, HashMap<String, Paper> papers, Vocabulary vocabulary) throws Exception {
        HashMap<String, Author> authors = authorsInput;
        for (String authorId : authors.keySet()) {
            authors.get(authorId).setRecommendation(buildRecommendationList(authors, authors.get(authorId), papers));
        }
        return authors;
    }

    /**
     *
     * @param authors
     * @param author
     * @param papers
     * @return
     * @throws java.lang.Exception
     */
    public static List<String> buildRecommendationList(HashMap<String, Author> authors, Author author, HashMap<String, Paper> papers) throws Exception {
        List<Paper> examples = new ArrayList<>();
        List<String> favPapers = new ArrayList<>();
        List<String> recommendList = new ArrayList<>();

        for (String authorId : authors.keySet()) {
            if (author.getAuthorId().equals(authorId)) {
                for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
                    Paper paper = (Paper) it.next();
                    examples.add(paper);
                    favPapers.add(paper.getPaperId());

                    for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                        Paper paperRef = (Paper) ite.next();
                        examples.add(paperRef);
                        favPapers.add(paperRef.getPaperId());
                    }
                }
            } else {
                for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
                    Paper paper = (Paper) it.next();
                    examples.add(paper);

                    for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                        Paper paperRef = (Paper) ite.next();
                        examples.add(paperRef);
                    }
                }
            }
        }

        NaiveBayes bayes = new NaiveBayes();
        HashMap<String, Double> labelProbs = bayes.labelProbs(examples, favPapers);
        HashMap<String, HashMap<String, Vector>> conditionalProbs = bayes.conditionalProbs(examples, favPapers, papers);

        for (String paperId : papers.keySet()) {
            if (bayes.predict(papers.get(paperId), conditionalProbs, labelProbs).equals("yes")) {
                recommendList.add(paperId);
            }
        }

        return recommendList;
    }
}
