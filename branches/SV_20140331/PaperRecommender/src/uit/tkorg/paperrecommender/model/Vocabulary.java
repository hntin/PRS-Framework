/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uit.tkorg.paperrecommender.utility.dataimport.flatfile.ImportDataset1;

/**
 *
 * @author THNghiep This class represents the vocabulary with all keywords.
 * Data: list of all keywords. Method: - Build vocabulary list.
 */
public class Vocabulary implements Serializable {

    private List vocabulary;

    /**
     * Default constructor used for serializable.
     */
    public void Vocabulary() {
        vocabulary = new ArrayList();
    }

    /**
     * @return the vocabulary
     */
    public List getVocabulary() {
        return vocabulary;
    }

    /**
     * @param vocabulary the vocabulary to set
     */
    public void setVocabulary(List vocabulary) {
        this.vocabulary = vocabulary;
        Collections.sort(this.vocabulary);
    }
}
