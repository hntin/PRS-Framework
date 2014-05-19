/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.method.cbf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.pr.utility.algorithm.NaiveBayes;

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
     * @return
     * @throws Exception
     */
    public static HashMap<String, Author> buildALLRecommendationLists(HashMap<String, Author> authorsInput, HashMap<String, Paper> papers) throws Exception {
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
        List<Paper> favoritePapers = new ArrayList<>();
        List<String> recommendList = new ArrayList<>();

        for (String authorId : authors.keySet()) {
            if (author.getAuthorId().equals(authorId)) {
                for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
                    Paper paper = (Paper) it.next();
                    examples.add(paper);
                    favoritePapers.add(paper);

                    for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                        Paper paperRef = (Paper) ite.next();
                        examples.add(paperRef);
                        favoritePapers.add(paperRef);
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

        NaiveBayes naiveBayes = new NaiveBayes();
        HashMap<String, Double> labelProbs = naiveBayes.labelProbs(examples, favoritePapers);
        HashMap<String, HashMap<String, Vector>> conditionalProbs = naiveBayes.conditionalProbs(examples, favoritePapers, papers);
       
        for (String paperId : papers.keySet()) {
            if (naiveBayes.predict(papers.get(paperId), conditionalProbs, labelProbs).equals("yes")) {
                recommendList.add(paperId);
            }
        }

        return recommendList;
    }
}
