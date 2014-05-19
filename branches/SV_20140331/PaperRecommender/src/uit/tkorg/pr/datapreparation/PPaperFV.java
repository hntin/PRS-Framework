/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.datapreparation;

import ir.vsr.HashMapVector;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.pr.utility.general.Weighting;

/**
 *
 * @author Minh
 */
public class PPaperFV {

    // Prevent instantiation.
    private PPaperFV() {
    }

    /**
     * This method computes feature vector for all papers.
     *
     * @param papersInput
     * @param alpha
     * @return
     * @throws Exception
     */
    public static HashMap<String, Paper> computeAllPapersFeatureVector(HashMap<String, Paper> papersInput, double alpha) throws Exception {
        HashMap<String, Paper> papers = papersInput;

        for (String paperId : papersInput.keySet()) {
            papers.get(paperId).setFeatureVector(computePaperFeatureVectorWithCosine(papersInput, paperId, alpha));
        }

        return papers;
    }

    /**
     * This method computes feature vector of paper with cosine weighting.
     *
     * @param papersInput
     * @param paperId
     * @param alpha
     * @return featureVector
     * @throws java.lang.Exception
     */
    public static HashMapVector computePaperFeatureVectorWithCosine(HashMap<String, Paper> papersInput, String paperId, double alpha) throws Exception {
        HashMapVector paperFV = new HashMapVector();
        HashMapVector fragFVector = new HashMapVector();
        HashMapVector fVector = new HashMapVector();
        
        Paper paper = papersInput.get(paperId);//get paper has Id is paperId in List of Papers
        fVector.add(paper.getContent());//assign HashMapVector featureVector equal HashMapVector paper
        
        List<String> citation = paper.getCitation();//get list of citation paper
        fVector.add(sumFeatureVectorWithCosine(papersInput, paper, citation));//add featureVector with featureVector of citation papers
        fragFVector.add(sumFeatureVectorWithCosine(papersInput, paper, citation));

        List<String> reference = paper.getReference();//get list of reference paper
        fVector.add(sumFeatureVectorWithCosine(papersInput, paper, reference));//add featureVector with featureVector of reference papers

        List<String> potential = paper.getPotentialCitation();
        fVector.add(sumFeatureVectorWithCosine(papersInput, paper, potential));//add featureVector with featureVector of potential papers
        fragFVector.addScaled(sumFeatureVectorWithCosine(papersInput, paper, potential), alpha);
        
        paperFV.addScaled(fVector, 1 - alpha);
        paperFV.add(fragFVector);
        
        return paperFV;
    }

    /**
     * This method compute sum of Papers(Citation or Reference Paper) with
     * cosine weight
     *
     * @param papersInput
     * @param paper
     * @param paperIds
     * @return featureVector
     * @throws java.lang.Exception
     */
    public static HashMapVector sumFeatureVectorWithCosine(HashMap<String, Paper> papersInput, Paper paper, List<String> paperIds) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        for (String paperId : paperIds) {
            if (papersInput.containsKey(paperId)) {
                double cosine = Weighting.computeCosine(paper.getContent(), papersInput.get(paperId).getContent());
                featureVector.addScaled(papersInput.get(paperId).getContent(), cosine);
            }
        }

        return featureVector;
    }
}
