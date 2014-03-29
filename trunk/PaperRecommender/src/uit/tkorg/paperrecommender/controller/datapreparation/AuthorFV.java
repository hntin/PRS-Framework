/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.datapreparation;

import ir.vsr.HashMapVector;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.Weighting;

/**
 * This class handles all logics for author object. 
 * Data: List of author: junior and senior. 
 * Method: 
 * - Build list authors. 
 * - Compute author feature vector.
 *
 * @author THNghiep
 */
public class AuthorFV {

    // Prevent instantiation.
    private AuthorFV() {
    }

    /**
     * This method computes and set value for all authors' full feature vector
     * (after combining citation and reference papers).
     *
     * @param weightingScheme 0: linear; 1: cosine; 2: rpy
     */
    public static HashMap<String, Author> computeAllAuthorsFeatureVector(HashMap<String, Author> authorsInput, int weightingScheme) throws Exception {
        HashMap<String, Author> authors = authorsInput;
        for (String key : authorsInput.keySet()) {
            authors.get(key).setFeatureVector(computeAuthorFeatureVector(authorsInput, authors.get(key).getAuthorId(), weightingScheme));
        }
        return authors;
    }

    /**
     * This method compute final feature vector by combining citation and
     * reference.
     *
     * @param authorId
     * @param weightingScheme 0: linear; 1: cosine; 2: rpy
     * @return list represents feature vector.
     */
    private static HashMapVector computeAuthorFeatureVector(HashMap<String, Author> authorsInput, String authorId, int weightingScheme) throws Exception {
        HashMapVector featureVector;
        if (weightingScheme == 0) {
            if (authorId.contains("y")) {
                featureVector = computeJuniorFeatureVectorWithLinear(authorsInput, authorId);
            } else {
                featureVector = computeSeniorFeatureVectorWithLinear(authorsInput, authorId);
            }
        } else if (weightingScheme == 1) {
            if (authorId.contains("y")) {
                featureVector = computeJuniorFeatureVectorWithCosine(authorsInput, authorId);
            } else {
                featureVector = computeSeniorFeatureVectorWithCosine(authorsInput, authorId);
            }
        } else {
            if (authorId.contains("y")) {
                featureVector = computeJuniorFeatureVectorWithRPY(authorsInput, authorId);
            } else {
                featureVector = computeSeniorFeatureVectorWithRPY(authorsInput, authorId);
            }
        }
        return featureVector;
    }
    //==================================================================================================================================================
    /**
     * This method compute Junior Feature Vector with linear weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeJuniorFeatureVectorWithLinear(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector;
        Paper authorPaper;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List<Paper> reference = authorPaper.getReference();//get list of reference papers of author's paper 
        featureVector.add(sumFeatureVectorWithLinear(reference));//add featureVector with featureVector of reference papers of author's paper 
        return featureVector;
    }

    /**
     * This method compute Junior Feature Vector with cosine weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeJuniorFeatureVectorWithCosine(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector;
        Paper authorPaper;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List<Paper> reference = authorPaper.getReference();//get list of reference papers of author's paper 
        featureVector.add(sumFeatureVectorWithCosine(authorPaper, reference));//add featureVector with featureVector of reference papers of author's paper 
        return featureVector;
    }

    /**
     * This method compute Junior Feature Vector with RPY weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeJuniorFeatureVectorWithRPY(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector;
        Paper authorPaper;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List<Paper> reference = authorPaper.getReference();//get list of reference papers of author's paper 
        featureVector.add(sumFeatureVectorWithRPY(authorPaper, reference));//add featureVector with featureVector of reference papers of author's paper 
        return featureVector;
    }
    //======================================================================================================================================================

    /**
     * This method compute Senior Feature Vector with linear weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeSeniorFeatureVectorWithLinear(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List<Paper> citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithLinear(citation));//add featureVector with featureVector of citation papers of author's paper 
            List<Paper> reference = paper.getReference();//get list of reference papers of author's paper 
            featureVector.add(sumFeatureVectorWithLinear(reference));//add featureVector with featureVector of reference papers of author's paper 
        }
        return featureVector;
    }

    /**
     * This method compute Senior Feature Vector with cosine weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeSeniorFeatureVectorWithCosine(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List<Paper> citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithCosine(paper, citation));//add featureVector with featureVector of citation papers of author's paper 
            List<Paper> reference = paper.getReference();//get list of reference papers of author's paper 
            featureVector.add(sumFeatureVectorWithCosine(paper, reference));//add featureVector with featureVector of reference papers of author's paper 
        }
        return featureVector;
    }

    /**
     * This method compute Senior Feature Vector with RPY weight
     *
     * @param authorId
     * @return featureVector
     */
    private static HashMapVector computeSeniorFeatureVectorWithRPY(HashMap<String, Author> authorsInput, String authorId) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers;
        Author author = authorsInput.get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List<Paper> citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithRPY(paper, citation));//add featureVector with featureVector of citation papers of author's paper 
            List<Paper> reference = paper.getReference();//get list of reference papers of author's paper 
            featureVector.add(sumFeatureVectorWithRPY(paper, reference));//add featureVector with featureVector of reference papers of author's paper 
        }
        return featureVector;
    }
    //==================================================================================================================================================
    /**
     * This method compute sum of Papers(Citation or Reference Paper) with
     * linear weight
     *
     * @param papers
     * @return featureVector
     */
    private static HashMapVector sumFeatureVectorWithLinear(List<Paper> papers) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        for (Paper paper : papers) {
            featureVector.add(paper.getContent());
        }
        return featureVector;
    }

    /**
     * This method compute sum of Papers(Citation or Reference Paper) with
     * cosine weight
     *
     * @param cpaper
     * @param papers
     * @return featureVector
     */
    private static HashMapVector sumFeatureVectorWithCosine(Paper cpaper, List<Paper> papers) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        for (Paper paper : papers) {
            double cosine = Weighting.computeCosine(cpaper.getContent(), paper.getContent());
            featureVector.addScaled(paper.getContent(), cosine);
        }
        return featureVector;
    }

    /**
     * This method compute sum of Papers(Citation or Reference Paper) with rpy
     * weight
     *
     * @param cpaper
     * @param papers
     * @return featureVector
     */
    private static HashMapVector sumFeatureVectorWithRPY(Paper cpaper, List<Paper> papers) throws Exception {
        HashMapVector featureVector = new HashMapVector();
        for (Paper paper : papers) {
            double rpy = Weighting.computeRPY(cpaper.getYear(), paper.getYear());
            featureVector.addScaled(paper.getContent(), rpy);
        }
        return featureVector;
    }
}
