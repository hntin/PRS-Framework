/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

import java.util.HashMap;
import java.util.Iterator;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Vocabulary;

/**
 *
 * @author Minh
 */
public class NaiveBayes {

    private int[][] matrixcells;
    private int rows;
    private int cols;
    private String label[][];

    /**
     * This method initializes for NaiveBayes class.
     *
     */
    public NaiveBayes() {
    }

    /**
     * This method builds training matrix.
     *
     * @param vocabulary
     * @param authors
     */
    public void buildTrainingMatrix(HashMap<String, Author> authors, Vocabulary vocabulary) {

        this.rows = trainingMatrixRows(authors);
        this.cols = vocabulary.getVocabulary().size();
        this.matrixcells = new int[rows][cols];
        this.label = new String[rows][1];

        int i = 0;
        for (String author : authors.keySet()) {
            for (Iterator it = authors.get(author).getPaper().iterator(); it.hasNext();) {
                Paper paper = (Paper) it.next();
                addSample(vocabulary, author, paper, i);
                i++;
                if (paper.getCitation() != null) {
                    for (Iterator ite = paper.getCitation().iterator(); ite.hasNext();) {
                        Paper paperCit = (Paper) ite.next();
                        addSample(vocabulary, author, (Paper) paperCit, i);
                        i++;
                    }
                }
                if (paper.getReference() != null) {
                    for (Iterator ite = paper.getReference().iterator(); ite.hasNext();) {
                        Paper paperRef = (Paper) ite.next();
                        addSample(vocabulary, author, (Paper) paperRef, i);
                        i++;
                    }
                }

            }
        }
    }

    /**
     * This method adds a training sample into training matrix.
     *
     * @param vocabulary
     * @param authorId
     * @param paper
     * @param i
     */
    public void addSample(Vocabulary vocabulary, String authorId, Paper paper, int i) {

        for (int j = 0; j < cols; j++) {
            if (paper.getContent().hashMap.keySet().contains((String) vocabulary.getVocabulary().get(j))) {
                matrixcells[i][j] = 1;
            } else {
                matrixcells[i][j] = 0;
            }
        }

        label[i][0] = authorId;
    }

    /**
     * This method computes computePCi for PCi
     *
     * @param authors
     * @return prob
     */
    public HashMap<String, Double> computePCi(HashMap<String, Author> authors) {
        HashMap<String, Double> prob = new HashMap<>();

        int k = 0;

        for (String author : authors.keySet()) {
            int num = 0;
            for (int i = 0; i < rows; i++) {
                if (label[i][0].equals(author)) {
                    num++;
                }
            }
            prob.put(author, computeProbability(num, rows, authors.keySet().size()));
            k++;
        }

        return prob;
    }

    /**
     * This method computes computePCi for paper.
     *
     * @param authors
     * @param vocabulary
     * @return model
     */
    public NaiveBayesModel[][] bayesModel(HashMap<String, Author> authors, Vocabulary vocabulary) {

        NaiveBayesModel model[][] = new NaiveBayesModel[authors.keySet().size()][cols];

        int num_author = 0;

        for (String author : authors.keySet()) {
            int num = 0;
            for (int k = 0; k < rows; k++) {
                if (label[k][0].equals(author)) {
                    num++;
                }
            }
            for (int j = 0; j < cols; j++) {
                int numkey = 0;
                for (int k = 0; k < rows; k++) {
                    if (label[k][0].equals(author) && matrixcells[k][j] == 0) {
                        numkey++;
                    }
                }
                NaiveBayesModel bayesmodel = new NaiveBayesModel();
                bayesmodel.setAuthor(author);
                bayesmodel.setKeyword((String) vocabulary.getVocabulary().get(j));
                bayesmodel.setProb_0(computeProbability(numkey, num, 2));
                bayesmodel.setProb_1(1 - bayesmodel.getProb_0());
                model[num_author][j] = bayesmodel;
            }
            num_author++;
        }

        return model;
    }

    /**
     * This method computes probability.
     *
     * @param numLabel
     * @param rows
     * @param i
     * @return prob
     */
    public double computeProbability(int numLabel, int rows, int i) {
        double prob = 0;

        prob = (double) (numLabel + 1) / (rows + i);

        return prob;
    }

    /**
     * This method returns number rows of training matrix.
     *
     * @param authors
     * @return matrixRows
     */
    public int trainingMatrixRows(HashMap<String, Author> authors) {
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
