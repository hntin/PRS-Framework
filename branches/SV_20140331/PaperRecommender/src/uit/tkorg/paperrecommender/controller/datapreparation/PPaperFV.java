/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.controller.datapreparation;
import ir.vsr.HashMapVector;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.Weighting;
/**
 *
 * @author Minh
 */
public class PPaperFV {
     // Prevent instantiation.
    private PPaperFV() {
    }
/**
 * 
 * @param papersInput
 * @param alpha
 * @return
 * @throws Exception 
 */
    public static HashMap<String, Paper> computeAllPapersFeatureVector(HashMap<String, Paper> papersInput,double alpha) throws Exception {
        // Reuse papersInput, not constructing new hash map.
        HashMap<String, Paper> papers = papersInput;
        
        for (String key : papersInput.keySet()) {
            papers.get(key).setFeatureVector(computePaperFeatureVectorWithCosine(papersInput,key,alpha));
        }
        
        return papers;
    }
    /**
     * This method compute Paper Feature Vector with cosine weight
     *
     * @param papersInput
     * @param paperId
     * @return featureVector
     * @throws java.lang.Exception
     */
    public static HashMapVector computePaperFeatureVectorWithCosine(HashMap<String, Paper> papersInput, String paperId, double alpha) throws Exception {
        HashMapVector fP = new HashMapVector();
        HashMapVector fragFVector = new HashMapVector();
        HashMapVector  featureVector = new HashMapVector();      
        Paper paper = papersInput.get(paperId);//get paper has paperId in ListofPapers
        featureVector.add(paper.getContent());//assign HashMapVector featureVector equal HashMapVector paper
       
        List<String> citation = paper.getCitation();//get list of citation paper
        featureVector.add(sumFeatureVectorWithCosine(papersInput, paper, citation));//add featureVector with featureVector of citation papers
        fragFVector.add(sumFeatureVectorWithCosine(papersInput, paper, citation));
        List<String> reference = paper.getReference();//get list of reference paper
        featureVector.add(sumFeatureVectorWithCosine(papersInput, paper, reference));//add featureVector with featureVector of reference papers
        
        List <String> potential = paper.getCitationPotential();
        featureVector.add(sumFeatureVectorWithCosine(papersInput, paper, potential));//add featureVector with featureVector of potential papers
        fragFVector.addScaled(sumFeatureVectorWithCosine(papersInput, paper, potential),alpha );
        fP.addScaled(featureVector, 1- alpha);
        fP.add(fragFVector);
        return fP;
    }
    /**
     * This method compute sum of Papers(Citation or Reference Paper) with
     * cosine weight
     *
     * @param papersInput
     * @param cpaper
     * @param paperIds
     * @return featureVector
     * @throws java.lang.Exception
     */
    public static HashMapVector sumFeatureVectorWithCosine(HashMap<String, Paper> papersInput, Paper cpaper, List<String> paperIds) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        for (String paperId : paperIds) {
            if (papersInput.containsKey(paperId)) {
                double cosine = Weighting.computeCosine(cpaper.getContent(), papersInput.get(paperId).getContent());
                featureVector.addScaled(papersInput.get(paperId).getContent(), cosine);
            }
        }
        
        return featureVector;
    }
}