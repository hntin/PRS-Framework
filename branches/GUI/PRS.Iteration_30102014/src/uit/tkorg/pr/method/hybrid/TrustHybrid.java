/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.method.hybrid;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import uit.tkorg.pr.method.GenericRecommender;
import uit.tkorg.pr.method.cbf.FeatureVectorSimilarity;
import uit.tkorg.pr.model.Author;
import uit.tkorg.utility.general.HashMapUtility;

/**
 *
 * @author Administrator
 */
public class TrustHybrid {
    
    private static Integer countThread = 0;

    /**
     * @return the countThread
     */
    public static Integer getCountThread() {
        return countThread;
    }

    /**
     * @param aCountThread the countThread to set
     */
    public static void setCountThread(Integer aCountThread) {
        countThread = aCountThread;
    }

    private TrustHybrid() {}
    
    /**
     * 
     * @param authors
     * @param alpha
     * @param combinationScheme 1: linear, 2: basedOnConfidence, 3: basedOnConfidence and linear.
     * @throws Exception 
     */
    public static void computeTrustedAuthorHMLinearCombinationAndPutIntoModelForAuthorList(
            HashMap<String, Author> authors, 
            final float alpha, final int combinationScheme) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);

        System.out.println("NUM OF AUTHOR: " + authors.size());

        HashMapUtility.setCountThread(0);
        for (String authorId : authors.keySet()) {
            final Author authorObj = authors.get(authorId);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (combinationScheme == 1) {
                            HashMapUtility.combineLinearTwoHashMap(authorObj.getCoAuthorRSSHM(), 
                                    authorObj.getCitationAuthorRSSHM(), 
                                    alpha, 
                                    authorObj.getTrustedAuthorHM());
                        } else if (combinationScheme == 2) {
                            HashMapUtility.combineBasedOnConfidenceTwoHashMap(authorObj.getCoAuthorRSSHM(), 
                                    authorObj.getCitationAuthorRSSHM(), 
                                    authorObj.getTrustedAuthorHM());
                        } else if (combinationScheme == 3) {
                            HashMapUtility.combineBasedOnConfidenceAndLinearTwoHashMap(authorObj.getCoAuthorRSSHM(), 
                                    authorObj.getCitationAuthorRSSHM(), 
                                    alpha, 
                                    authorObj.getTrustedAuthorHM());
                        } else if (combinationScheme == 4) {
                            HashMapUtility.combineBasedOnConfidenceTwoHashMapV2(authorObj.getCoAuthorRSSHM(), 
                                    authorObj.getCitationAuthorRSSHM(), 
                                    authorObj.getTrustedAuthorHM());
                        } else if (combinationScheme == 5) {
                            HashMapUtility.combineBasedOnConfidenceAndLinearTwoHashMapV2(authorObj.getCoAuthorRSSHM(), 
                                    authorObj.getCitationAuthorRSSHM(), 
                                    alpha, 
                                    authorObj.getTrustedAuthorHM());
                        }
                        // Normalize
                        HashMapUtility.minNormalizeHashMap(authorObj.getTrustedAuthorHM());
                    } catch (Exception ex) {
                        Logger.getLogger(FeatureVectorSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    public static void computeMetaTrustedAuthorHMAndPutIntoModelForAuthorList(
            HashMap<String, Author> authors, 
            final HashMap<String, HashMap<String, Float>> referenceRSSNet, 
            final int metaTrustType,
            final float alpha) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);

        System.out.println("NUM OF AUTHOR: " + authors.size());

        HashMapUtility.setCountThread(0);
        for (String authorId : authors.keySet()) {
            final Author authorObj = authors.get(authorId);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        final HashMap<String, Float> metaTrustAuthorHM = new HashMap<>();
                        computeMetaTrustedAuthorsForOneAuthor(referenceRSSNet, authorObj, metaTrustAuthorHM, metaTrustType);
                        // metaTrustAuthorHM is temp here, so normalize here.
                        HashMapUtility.minNormalizeHashMap(metaTrustAuthorHM);
                        // With 2 types of meta trust: only COMBINE LINEAR citation author with meta trusted author.
                        HashMapUtility.combineLinearTwoHashMap(authorObj.getCitationAuthorRSSHM(), 
                                metaTrustAuthorHM, alpha, authorObj.getTrustedAuthorHM());
                        // Normalize
                        HashMapUtility.minNormalizeHashMap(authorObj.getTrustedAuthorHM());
                    } catch (Exception ex) {
                        Logger.getLogger(FeatureVectorSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    public static void computeMetaTrustedAuthorsForOneAuthor(
            HashMap<String, HashMap<String, Float>> referenceRSSNet, 
            Author ai, 
            HashMap<String, Float> outputHM,
            int metaTrustType) throws Exception {

        // citation author of coauthor.
        if (metaTrustType == 1) {
            for (String au : ai.getCoAuthorRSSHM().keySet()) {
                if (referenceRSSNet.containsKey(au)) {
                    for (String aj : referenceRSSNet.get(au).keySet()) {
                        Float combinedValue = ai.getCoAuthorRSSHM().get(au) * referenceRSSNet.get(au).get(aj);
                        if (outputHM.containsKey(aj)) {
                            outputHM.put(aj, outputHM.get(aj) + combinedValue);
                        } else {
                            outputHM.put(aj, combinedValue);
                        }
                    }
                }
            }

            // scaling
            for (String aj : outputHM.keySet()) {
                outputHM.put(aj, outputHM.get(aj) / ai.getCoAuthorRSSHM().size());
            }
            
        // citation author of citation author.
        } else if (metaTrustType == 2) {
            for (String au : ai.getCitationAuthorRSSHM().keySet()) {
                if (referenceRSSNet.containsKey(au)) {
                    for (String aj : referenceRSSNet.get(au).keySet()) {
                        Float combinedValue = ai.getCitationAuthorRSSHM().get(au) * referenceRSSNet.get(au).get(aj);
                        if (outputHM.containsKey(aj)) {
                            outputHM.put(aj, outputHM.get(aj) + combinedValue);
                        } else {
                            outputHM.put(aj, combinedValue);
                        }
                    }
                }
            }

            // scaling
            for (String aj : outputHM.keySet()) {
                outputHM.put(aj, outputHM.get(aj) / ai.getCitationAuthorRSSHM().size());
            }
        }
        
        synchronized (getCountThread()) {
            System.out.println("Thread No. " + countThread++ + " Done. " + (new Date(System.currentTimeMillis()).toString()));
        }
    }

    public static void computeTrustedPaperHMAndPutIntoModelForAuthorList(
            final HashMap<String, Author> authors, 
            final int howToTrustPaper, final HashSet<String> paperIdsInTestSet) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);

        System.out.println("NUM OF AUTHOR: " + authors.size());

        for (String authorId : authors.keySet()) {
            final Author authorObj = authors.get(authorId);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        computeTrustedPaperHM(authors, authorObj, howToTrustPaper);
                        for (String idPaper : authorObj.getTrustedPaperHM().keySet()) {
                            if (!paperIdsInTestSet.contains(idPaper)) {
                                authorObj.getTrustedPaperHM().remove(idPaper);
                            }
                        }
                        // Normalize
                        HashMapUtility.minNormalizeHashMap(authorObj.getTrustedPaperHM());
                    } catch (Exception ex) {
                        Logger.getLogger(FeatureVectorSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    /**
     * 
     * @param authors
     * @param author
     * @param howToTrustPaper 1: average trusted author, 2: max trusted author.
     * @throws Exception 
     */
    private static void computeTrustedPaperHM(HashMap<String, Author> authors, 
            Author author, int howToTrustPaper) throws Exception {

        HashMap<String, Integer> paperTrustedAuthorCount = new HashMap<>();
        
        for (String authorId : author.getTrustedAuthorHM().keySet()) {
            if (authors.containsKey(authorId)) {
                for (String paperId : (List<String>) authors.get(authorId).getPaperList()) {
                    if (author.getTrustedPaperHM().containsKey(paperId)) {
                        if (howToTrustPaper == 1) {
                            author.getTrustedPaperHM().put(paperId, 
                                    author.getTrustedPaperHM().get(paperId) + author.getTrustedAuthorHM().get(authorId));
                            paperTrustedAuthorCount.put(paperId, 
                                    paperTrustedAuthorCount.get(paperId) + 1);
                        } else if (howToTrustPaper == 2) {
                            if (author.getTrustedPaperHM().get(paperId) < author.getTrustedAuthorHM().get(authorId)) {
                                author.getTrustedPaperHM().put(paperId, author.getTrustedAuthorHM().get(authorId));
                            }
                        }
                    } else {
                        author.getTrustedPaperHM().put(paperId, author.getTrustedAuthorHM().get(authorId));
                        paperTrustedAuthorCount.put(paperId, 1);
                    }
                }
            }
        }
        
        if (howToTrustPaper == 1) {
            for (String paperId : author.getTrustedPaperHM().keySet()) {
                author.getTrustedPaperHM().put(paperId, 
                        author.getTrustedPaperHM().get(paperId) / paperTrustedAuthorCount.get(paperId));
            }
        }

        synchronized (getCountThread()) {
            System.out.println(countThread++ + ". " + (new Date(System.currentTimeMillis()).toString()) + " DONE for authorId: " + author.getAuthorId());
        }
    }
    
    /**
     * 
     * @param authors
     * @param alpha
     * @param combinationScheme 1: linear, 2: basedOnConfidence, 3: basedOnConfidence and linear.
     * @throws Exception 
     */
    public static void computeCBFTrustLinearCombinationAndPutIntoModelForAuthorList(
            HashMap<String, Author> authors, 
            final float alpha, final int combinationScheme) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);

        System.out.println("NUM OF AUTHOR: " + authors.size());

        HashMapUtility.setCountThread(0);
        for (String authorId : authors.keySet()) {
            final Author authorObj = authors.get(authorId);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (combinationScheme == 1) {
                            HashMapUtility.combineLinearTwoHashMap(authorObj.getCbfSimHM(), 
                                    authorObj.getTrustedPaperHM(), 
                                    alpha, 
                                    authorObj.getCbfTrustHybridHM());
                        } else if (combinationScheme == 2) {
                            HashMapUtility.combineBasedOnConfidenceTwoHashMap(authorObj.getCbfSimHM(), 
                                    authorObj.getTrustedPaperHM(), 
                                    authorObj.getCbfTrustHybridHM());
                        } else if (combinationScheme == 3) {
                            HashMapUtility.combineBasedOnConfidenceAndLinearTwoHashMap(authorObj.getCbfSimHM(), 
                                    authorObj.getTrustedPaperHM(), 
                                    alpha, 
                                    authorObj.getCbfTrustHybridHM());
                        } else if (combinationScheme == 4) {
                            HashMapUtility.combineBasedOnConfidenceTwoHashMapV2(authorObj.getCbfSimHM(), 
                                    authorObj.getTrustedPaperHM(), 
                                    authorObj.getCbfTrustHybridHM());
                        } else if (combinationScheme == 5) {
                            HashMapUtility.combineBasedOnConfidenceAndLinearTwoHashMapV2(authorObj.getCbfSimHM(), 
                                    authorObj.getTrustedPaperHM(), 
                                    alpha, 
                                    authorObj.getCbfTrustHybridHM());
                        }
                        // Normalize
                        HashMapUtility.minNormalizeHashMap(authorObj.getCbfTrustHybridHM());
                    } catch (Exception ex) {
                        Logger.getLogger(FeatureVectorSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
    
    /**
     * 
     * @param authors
     * @throws Exception 
     */
    public static void computeCBFTrustHybridV2AndPutIntoModelForAuthorList(HashMap<String, Author> authors) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        int numOfProcessors = runtime.availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numOfProcessors - 1);

        System.out.println("NUM OF AUTHOR: " + authors.size());

        HashMapUtility.setCountThread(0);
        for (String authorId : authors.keySet()) {
            final Author authorObj = authors.get(authorId);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (String paperId : authorObj.getTrustedPaperHM().keySet()) {
                            Float cbfSim = authorObj.getCbfSimHM().get(paperId);
                            if (cbfSim == null) {
                                cbfSim = new Float(0);
                            } else {
                                cbfSim = new Float(cbfSim);
                            }
                            authorObj.getCbfTrustHybridV2HM().put(paperId, cbfSim);
                        }
                        // Normalize
                        HashMapUtility.minNormalizeHashMap(authorObj.getCbfTrustHybridV2HM());
                    } catch (Exception ex) {
                        Logger.getLogger(FeatureVectorSimilarity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }

    public static void trustRecommendToAuthorList(HashMap<String, Author> authorTestSet, 
            int topNRecommend) 
            throws IOException, TasteException, Exception {
        GenericRecommender.generateRecommendationForAuthorList(authorTestSet, topNRecommend, 3);
    }
    
    public static void trustHybridRecommendToAuthorList(
            HashMap<String, Author> authorTestSet, int topNRecommend) 
            throws IOException, TasteException, Exception {
        GenericRecommender.generateRecommendationForAuthorList(authorTestSet, topNRecommend, 4);
    }
    
    public static void trustHybridRecommendToAuthorListV2(
            HashMap<String, Author> authorTestSet, int topNRecommend) 
            throws IOException, TasteException, Exception {
        GenericRecommender.generateRecommendationForAuthorList(authorTestSet, topNRecommend, 5);
    }
}
