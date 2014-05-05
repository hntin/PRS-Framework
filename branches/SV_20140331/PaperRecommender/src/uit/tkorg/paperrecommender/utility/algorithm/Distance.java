/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.paperrecommender.utility.algorithm;

import java.util.HashMap;
import java.util.List;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.utility.GeneralUtility;
import uit.tkorg.paperrecommender.utility.PearsonCorrelation;
import uit.tkorg.paperrecommender.utility.Weighting;

/**
 *
 * @author Minh
 */
public class Distance {
     /**
     * This is method compute distance between user and user or item and item
     *
     * @param matrixInput
     * @param paperTarget
     * @return
     * @throws Exception
     */
    public static HashMap pearsonPaper(List<Paper> papers,double[][] matrixInput, int paperTarget) throws Exception {
        HashMap PCC = new HashMap();
        for (int i = 0; i < matrixInput.length; i++) {
            // compute PCC paper target and cit
            if (i !=paperTarget) 
            PCC.put(papers.get(i).getPaperId(),GeneralUtility.standardizePearson(PearsonCorrelation.pearsonCorrelation(matrixInput[paperTarget], matrixInput[i])));
        }
        return PCC;
    }
    public static HashMap pearsonUser(List <Author> authors, List<Paper> papers, double [][] matrixInput, int userTarget) throws Exception{
        HashMap distance= new HashMap();
        for(int i= 0; i< authors.size();i++)
                if(i!= userTarget)
                    distance.put(authors.get(i).getAuthorId(),GeneralUtility.standardizePearson(PearsonCorrelation.pearsonCorrelation(matrixInput[userTarget], matrixInput[i])));
        return distance;
        
    }
    public static HashMap distanceCosinePaper(List<Paper> papers,double [][] matrixInput, int paperTarget) throws Exception{
       
         HashMap distance= new HashMap();
        for(int i= 0; i< papers.size();i++)
                if(i!= paperTarget)
                    distance.put(papers.get(i).getPaperId(),Weighting.computeCosine(papers.get(paperTarget).getFeatureVector(),papers.get(i).getFeatureVector()));
        return distance;
    }
    public static HashMap distanceCosineUser(List<Author> authors, List<Paper> papers, double [][] matrixInput, int userTarget) throws Exception{
         HashMap distance= new HashMap();
        for(int i= 0; i< authors.size();i++)
                if(i!= userTarget)
                    distance.put(authors.get(i).getAuthorId(),Weighting.computeCosine(authors.get(userTarget).getFeatureVector(),papers.get(i).getFeatureVector()));
        return distance;
        
        
    }

}
