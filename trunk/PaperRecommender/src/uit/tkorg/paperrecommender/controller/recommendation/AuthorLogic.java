/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.recommendation;

import ir.vsr.HashMapVector;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.FlatFileData.ImportDataset1;
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
public class AuthorLogic implements Serializable {

    // Key of this hash map is paper id.
    // Value of this hash map is the relevant paper object.
    private HashMap<String, Author> authors;

    public AuthorLogic() {
        this.authors = null;
    }

    /**
     * @return the authors
     */
    public HashMap<String, Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors the authors to set
     */
    public void setAuthors(HashMap<String, Author> authors) {
        this.authors = authors;
    }

    public void buildListOfAuthors(String Dataset1Folder) throws IOException {
        setAuthors(ImportDataset1.buildListOfAuthors(Dataset1Folder));
    }

    /**
     * This method computes and set value for all authors' full feature vector
     * (after combining citation and reference papers).
     *
     * @param weightingScheme 0: linear; 1: cosine; 2: rpy
     */
    public void computeAllAuthorsFeatureVector(int weightingScheme) {
        for (String key : authors.keySet()) {
            authors.get(key).setFeatureVector(computeAuthorFeatureVector(authors.get(key).getAuthorId(), weightingScheme));
        }
    }

    /**
     * This method compute final feature vector by combining citation and
     * reference.
     *
     * @param authorId
     * @param weightingScheme 0: linear; 1: cosine; 2: rpy
     * @return list represents feature vector.
     */
    public HashMapVector computeAuthorFeatureVector(String authorId, int weightingScheme) {
        HashMapVector featureVector = null;
        if (weightingScheme == 0) {
            if (authorId.contains("y")) {
                featureVector=computeJuniorFeatureVectorWithLinear(authorId);
            } else {
                featureVector=computeSeniorFeatureVectorWithLinear(authorId);
            }
        } else if (weightingScheme == 1) {
            if (authorId.contains("y")) {
                featureVector=computeJuniorFeatureVectorWithCosine(authorId);
            } else {
                featureVector=computeSeniorFeatureVectorWithCosine(authorId);
            }
        } else {
            if (authorId.contains("y")) {
                featureVector=computeJuniorFeatureVectorWithRPY(authorId);
            } else {
                featureVector=computeSeniorFeatureVectorWithRPY(authorId);
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
    public HashMapVector computeJuniorFeatureVectorWithLinear(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        Paper authorPaper = new Paper();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List reference = authorPaper.getReference();//get list of reference papers of author's paper 
        featureVector.add(sumFeatureVectorWithLinear(reference));//add featureVector with featureVector of reference papers of author's paper 
        return featureVector;
    }

    /**
     * This method compute Junior Feature Vector with cosine weight
     *
     * @param authorId
     * @return featureVector
     */
    public HashMapVector computeJuniorFeatureVectorWithCosine(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        Paper authorPaper = new Paper();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List reference = authorPaper.getReference();//get list of reference papers of author's paper 
        featureVector.add(sumFeatureVectorWithCosine(authorPaper, reference));//add featureVector with featureVector of reference papers of author's paper 
        return featureVector;
    }

    /**
     * This method compute Junior Feature Vector with RPY weight
     *
     * @param authorId
     * @return featureVector
     */
    public HashMapVector computeJuniorFeatureVectorWithRPY(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        Paper authorPaper = new Paper();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPaper = (Paper) author.getPaper().get(0);//get paper of junior researchers 
        featureVector = authorPaper.getContent();
        List reference = authorPaper.getReference();//get list of reference papers of author's paper 
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
    public HashMapVector computeSeniorFeatureVectorWithLinear(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers = new ArrayList();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithLinear(citation));//add featureVector with featureVector of citation papers of author's paper 
            List reference = paper.getReference();//get list of reference papers of author's paper 
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
    public HashMapVector computeSeniorFeatureVectorWithCosine(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers = new ArrayList();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithCosine(paper, citation));//add featureVector with featureVector of citation papers of author's paper 
            List reference = paper.getReference();//get list of reference papers of author's paper 
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
    public HashMapVector computeSeniorFeatureVectorWithRPY(String authorId) {
        HashMapVector featureVector = new HashMapVector();
        List<Paper> authorPapers = new ArrayList();
        Author author = getAuthors().get(authorId);//get author has Id equally authorId in ListofPapers
        authorPapers = author.getPaper();//get list of papers of senior researchers 
        for (Paper paper : authorPapers) {
            featureVector.add(paper.getContent());
            List citation = paper.getCitation();//get list of citation papers of author's paper 
            featureVector.add(sumFeatureVectorWithRPY(paper, citation));//add featureVector with featureVector of citation papers of author's paper 
            List reference = paper.getReference();//get list of reference papers of author's paper 
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
    public HashMapVector sumFeatureVectorWithLinear(List<Paper> papers) {
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
    public HashMapVector sumFeatureVectorWithCosine(Paper cpaper, List<Paper> papers) {
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
    public HashMapVector sumFeatureVectorWithRPY(Paper cpaper, List<Paper> papers) {
        HashMapVector featureVector = new HashMapVector();
        for (Paper paper : papers) {
            double rpy = Weighting.computeRPY(cpaper.getYear(), paper.getYear());
            featureVector.addScaled(paper.getContent(), rpy);
        }
        return featureVector;
    }
}
