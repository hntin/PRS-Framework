/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.Weighting;

/**
 *
 * @author Minh
 */
public class Knn {

    /**
     *
     * @param cpaper
     * @param authors
     * @return
     * @throws java.lang.Exception
     */
    public HashMap<String, Double> hashMapDistance(Paper cpaper, HashMap<String, Author> authors) throws Exception {

        HashMap<String, Double> distances = new HashMap<>();

        for (String authorId : authors.keySet()) {
            for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
                Paper paper = (Paper) it.next();
                double sim = Weighting.computeCosine(cpaper.getContent(), paper.getContent());
                distances.put(paper.getPaperId(), sim);
                if (paper.getCitation() != null) {
                    for (Iterator ite = paper.getCitation().iterator(); ite.hasNext();) {
                        Paper paperCit = (Paper) ite.next();
                        distances.put(paperCit.getPaperId(), sim);
                    }
                }
                if (paper.getReference() != null) {
                    for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                        Paper paperRef = (Paper) ite.next();
                        distances.put(paperRef.getPaperId(), sim);
                    }
                }

            }
        }

        return distances;
    }

    public static List<String> nearestPapers(HashMap<String, Double> hashMapDistance, int k) throws Exception {
        HashMap<String, Double> sortDistance = new HashMap<>();
        List<String> topPapers = new ArrayList<>();

        sortDistance = GeneralUtility.sortHashMap(hashMapDistance);
        // Take top five papers.
        int counter = 0;
        for (String paperId : sortDistance.keySet()) {
            topPapers.add(paperId);
            counter++;
            if (counter >= k) {
                break;
            }
        }
        return topPapers;
    }

    public boolean check(String authorId, String paperId, HashMap<String, Author> authors) {

        for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
            Paper paper = (Paper) it.next();
            if (paper.getPaperId().equals(paperId)) {
                return true;
            }
            if (paper.getCitation() != null) {
                for (Iterator ite = paper.getCitation().iterator(); ite.hasNext();) {
                    Paper paperCit = (Paper) ite.next();
                    if (paperCit.getPaperId().equals(paperId)) {
                        return true;
                    }
                }
            }
            if (paper.getReference() != null) {
                for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                    Paper paperRef = (Paper) ite.next();
                    if (paperRef.getPaperId().equals(paperId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public HashMap<String, Integer> getLables(HashMap<String, Author> authors, HashMap<String, Double> hashMapDistance, int k) throws Exception {
        HashMap<String, Integer> labels = new HashMap<>();
        for (String paper : nearestPapers(hashMapDistance, k)) {
            for (String author : authors.keySet()) {
                if (check(author, paper, authors)) {
                    if (!labels.containsKey(author)) {
                        labels.put(author, new Integer(1));
                    } else {
                       labels.put(author, labels.get(author)+1);
                    }
                }
            }
        }

        return labels;
    }

    /**
     * This method returns number rows of training matrix.
     *
     * @param authors
     * @return matrixRows
     */
    public int getNumPaper(HashMap<String, Author> authors) {
        int matrixRows = 0;
        for (String authorId : authors.keySet()) {
            matrixRows += authors.get(authorId).getPaper().size();
            for (Iterator it = authors.get(authorId).getPaper().iterator(); it.hasNext();) {
                Paper paper = (Paper) it.next();
                if (paper.getCitation() != null) {
                    matrixRows += paper.getCitation().size();
                }
                if (paper.getReference() != null) {
                    matrixRows += paper.getReference().size();
                }
            }
        }
        return matrixRows;
    }
}
