/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.utility.alogirthm;

/**
 *
 * @author Vinh-PC
 */
public class NaiveBayesModel {

    private String author;
    private String keyword;
    private double prob_0;
    private double prob_1;

    public NaiveBayesModel() {

    }

    /**
     * Set author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Set keyword
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Set prob_0
     * @param prob_0
     */
    public void setProb_0(double prob_0) {
        this.prob_0 = prob_0;
    }

    /**
     * Set prob_1
     * @param prob_1
     */
    public void setProb_1(double prob_1) {
        this.prob_1 = prob_1;
    }

    /**
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     *
     * @return prob_0
     */
    public double getProb_0() {
        return prob_0;
    }

    /**
     *
     * @return prob_1
     */
    public double getProb_1() {
        return prob_1;
    }
}
