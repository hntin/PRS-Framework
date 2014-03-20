/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.model;

import ir.vsr.HashMapVector;
import java.util.List;

/**
 *
 * @author THNghiep
 * This class represents the author in general.
 * Data: author id, author type, list of ground-truth papers to recommend (List<String> of paper ids), 
 * list of papers of author (List<Paper>), each paper in list of papers of author has list of citation and reference papers.
 */
public class Author {
    private String authorId;
    private String authorType; // Junior or Senior.
    private List groundTruth; // List of String paper id.
    private List paper;
    private HashMapVector featureVector;

    /**
     * @return the authorId
     */
    public String getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId the authorId to set
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * @return the authorType
     */
    public String getAuthorType() {
        return authorType;
    }

    /**
     * @param authorType the authorType to set
     */
    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    /**
     * @return the groundTruth
     */
    public List getGroundTruth() {
        return groundTruth;
    }

    /**
     * @param groundTruth the groundTruth to set
     */
    public void setGroundTruth(List groundTruth) {
        this.groundTruth = groundTruth;
    }

    /**
     * @return the paper
     */
    public List getPaper() {
        return paper;
    }

    /**
     * @param paper the paper to set
     */
    public void setPaper(List paper) {
        this.paper = paper;
    }

    /**
     * @return the featureVector
     */
    public HashMapVector getFeatureVector() {
        return featureVector;
    }

    /**
     * @param featureVector the featureVector to set
     */
    public void setFeatureVector(HashMapVector featureVector) {
        this.featureVector = featureVector;
    }
}
