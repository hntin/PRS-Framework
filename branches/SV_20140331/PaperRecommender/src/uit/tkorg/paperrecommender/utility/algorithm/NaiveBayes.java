/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.GeneralUtility;

/**
 *
 * @author Minh
 */
public class NaiveBayes {

    private HashMap<String, int[]> trainingExamples;
    private final String[] labels = {"yes", "no"};

    /**
     * This method initializes for NaiveBayes class.
     *
     */
    public NaiveBayes() {
        trainingExamples = new HashMap<>();
    }

    /**
     * This method builds training matrix.
     *
     * @param examples
     * @param favoritePapers
     * @param papers
     * @return
     * @throws java.io.IOException
     */
    public HashMap<String, HashMap<String, Vector>> conditionalProbs(List<Paper> examples, List<Paper> favoritePapers, HashMap<String, Paper> papers) throws IOException {
        int numExamples = examples.size();
        int currentExample = 0;
        List<String> labelList = new ArrayList<>();
        List<String> favPapers = new ArrayList<>();

        for (Paper favPaper : favoritePapers) {
            favPapers.add(favPaper.getPaperId());
        }

        for (Paper paper : examples) {
            for (String word : paper.getContent().hashMap.keySet()) {
                if (!trainingExamples.containsKey(word)) {
                    int[] words = new int[numExamples];
                    words[currentExample] = 1;
                    trainingExamples.put(word, words);
                } else {
                    trainingExamples.get(word)[currentExample] = 1;
                }
            }

            if (favPapers.contains(paper.getPaperId())) {
                labelList.add("yes");
            } else {
                labelList.add("no");
            }

            currentExample++;
        }

        HashMap<String, HashMap<String, Vector>> modelBayes = new HashMap<>();
        for (String label : labels) {
            List<Integer> labelPositions = new ArrayList<>();

            for (int i = 0; i < labelList.size(); i++) {
                if (labelList.get(i).equals(label)) {
                    labelPositions.add(i);
                }
            }

            HashMap<String, Vector> vectorLabels = new HashMap<>();

            for (String word : trainingExamples.keySet()) {
                int labelCounts = 0;

                for (int i = 0; i < labelPositions.size(); i++) {
                    if (trainingExamples.get(word)[labelPositions.get(i)] == 1) {
                        labelCounts++;
                    }
                }

                Vector probs = new Vector();
                probs.add(computeProb(labelCounts, labelPositions.size(), 2));
                probs.add(computeProb(labelPositions.size() - labelCounts, numExamples, 2));
                vectorLabels.put(word, probs);
            }

            modelBayes.put(label, vectorLabels);
        }

        return modelBayes;
    }

    /**
     *
     * @param paper
     * @param conditionalProbs
     * @param labelProbs
     * @return
     * @throws Exception
     */
    public String predict(Paper paper, HashMap<String, HashMap<String, Vector>> conditionalProbs, HashMap<String, Double> labelProbs) throws Exception {
        HashMap<String, Double> probs = new HashMap<>();

        for (String label : labels) {
            double prob = labelProbs.get(label);
            for (String word : paper.getContent().hashMap.keySet()) {
                if (trainingExamples.containsKey(word)) {
                    prob *= (double) conditionalProbs.get(label).get(word).get(0);
                }
            }
            probs.put(label, prob);
        }
        probs = GeneralUtility.sortHashMap(probs);

        return (String) probs.keySet().toArray()[0];
    }

    /**
     * This method computes probabilities for labels.
     *
     * @param examples
     * @param favoritePapers
     * @return probs
     */
    public HashMap<String, Double> labelProbs(List<Paper> examples, List<Paper> favoritePapers) {
        HashMap<String, Double> probs = new HashMap<>();
        List<String> favPapers = new ArrayList<>();
        int lableCounts = 0;

        for (Paper favPaper : favoritePapers) {
            favPapers.add(favPaper.getPaperId());
        }
        for (Paper paper : examples) {
            if (favPapers.contains(paper.getPaperId())) {
                lableCounts++;
            }
        }

        probs.put(labels[0], computeProb(lableCounts, examples.size(), labels.length));
        probs.put(labels[1], computeProb(examples.size() - lableCounts, examples.size(), labels.length));

        return probs;
    }

    /**
     * This method computes a probability which has Laplace smoothing.
     *
     * @param labelCounts
     * @param numExamples
     * @param numValues
     * @return prob
     */
    public double computeProb(int labelCounts, int numExamples, int numValues) {
        double prob = 0;
        prob = (double) (labelCounts + 1) / (numExamples + numValues);
        return prob;
    }
}
