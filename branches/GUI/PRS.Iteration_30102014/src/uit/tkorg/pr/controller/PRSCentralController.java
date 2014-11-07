/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.controller;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import uit.tkorg.pr.constant.Options;
import uit.tkorg.pr.dataimex.MASDataset1;
import uit.tkorg.pr.datapreparation.CBFAuthorFVComputation;
import uit.tkorg.pr.datapreparation.CBFPaperFVComputation;
import uit.tkorg.pr.datapreparation.TrustDataModelPreparation;
import uit.tkorg.pr.evaluation.Evaluator;
import uit.tkorg.pr.method.cbf.FeatureVectorSimilarity;
import uit.tkorg.pr.method.cf.CF;
import uit.tkorg.pr.method.hybrid.CBFCF;
import uit.tkorg.pr.method.hybrid.TrustHybrid;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;

/**
 *
 * @author Zoe
 */
public class PRSCentralController {

    //<editor-fold defaultstate="collapsed" desc="Parameters of PRSCentralController">
    public String fileNameAuthors;// File 1
    public String fileNameAuthorPaper;// File 2
    public String fileNameAuthorCitePaper;// File 3
    public String fileNamePapers; //File 4
    public String fileNamePaperCitePaper;// File 5
    public String fileNameGroundTruth;// File 6

    public String dirPapers = "Temp\\Text";
    public String dirPreProcessedPaper = "Temp\\Preprocess";
    public String sequenceDir = "Temp\\Sequence";
    public String vectorDir = "Temp\\VectorDir";
    public String MahoutCFDir = "Temp\\MahoutDir";
    public String _fileName_EvaluationResult = "Temp\\ResultEvaluation.txt";
    public String _fileName_RecommendationList = "Temp\\RecommendationList.txt";

    public HashMap<String, Paper> papers = new HashMap<>();
    public HashMap<String, Author> authors = new HashMap<>();
    HashSet<String> paperIdsOfAuthorTestSet = new HashSet<>();
    HashSet<String> paperIdsInTestSet = new HashSet<>();

    public HashMap<String, Author> authorsCB = new HashMap<>();
    public HashMap<String, Author> authorsHybrid = new HashMap<>();
    public HashMap<String, Author> authorsCFP = new HashMap<>();
    public HashMap<String, Author> authorsCFC = new HashMap<>();
    public HashMap<String, Author> authorsCFSVD = new HashMap<>();
    public HashMap<String, Author> authorsTrustbased=new HashMap<String, Author>();
    public HashMap<String, Author> authorsHybridTrustbased=new HashMap<String, Author>();

    public int algorithm_Recommendation; //1: CBF, 2: CF, 3: CBFCFHybrid

    public int combinePaperOfAuthor;
    public int weightingPaperOfAuthor;
    public int timeAware;
    public double gamma;
    public int combineCandiatePaper;
    public int weightingCandidatePaper;
    public double pruning;

    public int cfMethod;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
    public int kNeighbourhood;
    public int f;
    public double l;
    public int i;

    public float alpha;
    public int combineHybrid;

    public int topRecommend;

    public int measure_Evaluation;
    public int topRank;
    
    public int combinationScheme = 1;
    public int howToTrustAuthor = 1;
    public int howToTrustPaper = 2;

    //</editor-fold>
    public PRSCentralController() {
        algorithm_Recommendation = 1; //1: CBF, 2: CF, 3: Hybrid

        combinePaperOfAuthor = 0;// combine paper of author
        weightingPaperOfAuthor = 0;// weighting combine paper of author
        timeAware = 0;// combine paper of author
        gamma = 0;// gamma for timeAware
        combineCandiatePaper = 0; // combine candiate paper
        weightingCandidatePaper = 0;// weighting combine candiate paper
        pruning = 0.0;// pruning citation or preference paper for all paper

        cfMethod = 1;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
        kNeighbourhood = 8;// number of neighbhood
        f = 5;//SVD
        l = 0.001;//SVD
        i = 100;//SVD

        alpha = (float) 0.9;//Hybrid
        combineHybrid = 1;//Hybrid

        topRecommend = 100;// top Recommmed

        measure_Evaluation = 0;// evaluation method
        topRank = 0;// top Rank

//        fileNamePapers = null; //File 1
//        fileNamePaperCitePaper = null;// File 2
//        fileNameAuthors = null;// File 3
//        fileNameAuthorPaper = null;// File 4
//        fileNameAuthorCitePaper = null;// File 5
//        fileNameGroundTruth = null;// File 6
        
        fileNamePapers = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Training] Paper_Before_2006.csv"; //File 1
        fileNamePaperCitePaper = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Training] Paper_Cite_Paper_Before_2006.csv";// File 2
        fileNameAuthors = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Testing] 1000Authors.csv";// File 3
        fileNameAuthorPaper = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Training] Author_Paper_Before_2006.csv";// File 4
        fileNameAuthorCitePaper = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Training] Author_Cite_Paper_Before_2006.csv";// File 5
        fileNameGroundTruth = "C:\\3.PRS-Experiment\\Dataset1 - MAS\\PRS Experimental data\\T0-T1\\[Testing] Ground_Truth_2006_2008_New_Citation.csv";// File 6

        papers = new HashMap<>();
        authors = new HashMap<>();
        paperIdsOfAuthorTestSet = new HashSet<>();
        paperIdsInTestSet = new HashSet<>();

        authorsCFP = new HashMap<>();
        authorsCFC = new HashMap<>();
        authorsCFSVD = new HashMap<>();
        authorsTrustbased=new HashMap<>();
        authorsHybridTrustbased=new HashMap<>();
    }

    public String[] guiHandlerRequest(Options request) {
        String[] response = new String[2];
        try {
            switch (request) {
                case IMPORT_DATA:
                    long startTime;
                    long estimatedTime;
                    //Step 1: read list 1000 authors for test set.
                    System.out.println("Begin reading author test set...");
                    startTime = System.nanoTime();
                    authors = MASDataset1.readAuthorListTestSet(fileNameAuthors, fileNameGroundTruth, fileNameAuthorPaper);
                    estimatedTime = System.nanoTime() - startTime;
                    System.out.println("Reading author test set elapsed time: " + estimatedTime / 1000000000 + " seconds");
                    System.out.println("End reading author test set.");
                    // Step 2:
                    // - Read content of papers from [Training] Paper_Before_2006.csv
                    // - Store metadata of all papers into HashMap<String, Paper> papers
                    System.out.println("Begin reading paper list...");
                    startTime = System.nanoTime();
                    papers = MASDataset1.readPaperList(fileNamePapers, fileNamePaperCitePaper);
                    estimatedTime = System.nanoTime() - startTime;
                    System.out.println("Reading paper list elapsed time: " + estimatedTime / 1000000000 + " seconds");
                    System.out.println("End reading paper list.");
                    // Step 3: 
                    // Compute TF-IDF for MAS papers.
                    //CBFPaperFVComputation.computeTFIDFFromPaperAbstract(papers, dirPapers, dirPreProcessedPaper, sequenceDir, vectorDir);
                    CBFPaperFVComputation.readTFIDFFromMahoutFile(papers, vectorDir);
                    // Clear no longer in use objects.
                    // Always clear abstract.
                    CBFPaperFVComputation.clearPaperAbstract(papers);
                    // Step 4:
                    // Get list of papers to process.
                    paperIdsOfAuthorTestSet = CBFAuthorFVComputation.getPaperIdsOfAuthors(authors);
                    paperIdsInTestSet = CBFAuthorFVComputation.getPaperIdsTestSet(authors);
                    response[0] = "Importing dataset is successed. Please choose tab Recommendation to recommend and generate recommendation list.";
                    break;
                case RECOMMEND:
                    recommend();
                    response[0] = "Generating recommendation list is successed. Please choose tab Evaluation to evaluate algorithms.";
                    break;
                case EVALUATE:
                    response[1] = evaluate(authors, measure_Evaluation, topRank).toString();
                    response[0] = "Evaluation is successed.";
                    break;
                case ANALYSE_ERROR:
                    break;
                case HELP:
                    break;
                case SAVE_RECOMMENDATION_LIST:
                    StringBuilder recommendList = new StringBuilder();
                    for (String authorId : authors.keySet()) {
                        recommendList.append(authorId + ":\n").append(authors.get(authorId).getRecommendationList().toString() + "\r\n");
                    }
                    FileUtils.writeStringToFile(new File(_fileName_RecommendationList), recommendList.toString(), "UTF8", true);
                    response[0] = "Saving is successed.";
                    break;
                case SAVE_EVALUATION_RESULT:
                    FileUtils.writeStringToFile(new File(_fileName_EvaluationResult), evaluate(authors, measure_Evaluation, topRank).toString(), "UTF8", true);
                    response[0] = "Saving is successed.";
                    break;
                default:
                    response[0] = "Unknown.";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            response[0] = "Fail.";
        }
        return response;
    }

    public void recommend() throws Exception {
        if (algorithm_Recommendation == 1) {
            //Content - based
            long startTime;
            long estimatedTime;
            System.out.println("Begin CBF recommendation...");
            startTime = System.nanoTime();

            for (String authorId : authors.keySet()) {
                authors.get(authorId).getRecommendationList().clear();
                authors.get(authorId).getCbfSimHM();
            }
            
            CBFController.cbfComputeRecommendingScore(authors, papers,
                    paperIdsOfAuthorTestSet, paperIdsInTestSet,
                    combinePaperOfAuthor, weightingPaperOfAuthor,
                    timeAware, gamma,
                    combineCandiatePaper, weightingCandidatePaper, 0,
                    pruning);
            FeatureVectorSimilarity.generateRecommendationForAuthorList(authors, topRecommend);
            
            estimatedTime = System.nanoTime() - startTime;
            System.out.println("CBF recommendation elapsed time: " + estimatedTime / 1000000000 + " seconds");
            System.out.println("End CBF recommendation.");
        } else if (algorithm_Recommendation == 2) {
            //CF method
            long startTime;
            long estimatedTime;
            System.out.println("Begin CF recommendation...");
            startTime = System.nanoTime();

            for (String authorId : authors.keySet()) {
                authors.get(authorId).getRecommendationList().clear();
                authors.get(authorId).getCfRatingHM().clear();
            }

            CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod,
                    authors, paperIdsInTestSet);
            CF.cfRecommendToAuthorList(authors, topRecommend);

            estimatedTime = System.nanoTime() - startTime;
            System.out.println("CF recommendation elapsed time: " + estimatedTime / 1000000000 + " seconds");
            System.out.println("End CF recommendation.");
        } else if (algorithm_Recommendation == 3) {
            //Hybrid Method
            float alpha_temp = (float) alpha;
            long startTime;
            long estimatedTime;
            System.out.println("Begin Hybrid recommendation...");
            startTime = System.nanoTime();

            for (String authorId : authors.keySet()) {
                authors.get(authorId).getRecommendationList().clear();
                authors.get(authorId).getCbfSimHM().clear();
                authors.get(authorId).getCfRatingHM().clear();
                authors.get(authorId).getCbfCfHybridHM().clear();
            }

            CBFController.cbfComputeRecommendingScore(authors, papers,
                    paperIdsOfAuthorTestSet, paperIdsInTestSet,
                    combinePaperOfAuthor, weightingPaperOfAuthor,
                    timeAware, gamma,
                    combineCandiatePaper, weightingCandidatePaper, 0,
                    pruning);
            
            CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir,
                    cfMethod, authors, paperIdsInTestSet);
            
            CBFCF.computeCBFCFCombinationAndPutIntoModelForAuthorList(authors, alpha_temp, combineHybrid);
            CBFCF.cbfcfHybridRecommendToAuthorList(authors, topRecommend);

            estimatedTime = System.nanoTime() - startTime;
            System.out.println("Hybrid recommendation elapsed time: " + estimatedTime / 1000000000 + " seconds");
            System.out.println("End Hybrid recommendation.");
        }else if (algorithm_Recommendation == 4) {
            //<editor-fold defaultstate="collapsed" desc="TRUST BASED">
            TrustDataModelPreparation.computeCoAuthorRSSHM(authors, fileNameAuthorPaper, fileNamePapers);
            HashMap<String, HashMap<String, Float>> referenceRSSNet = new HashMap<>();
            TrustDataModelPreparation.computeCitationAuthorRSSHM(authors, fileNameAuthorPaper, fileNamePaperCitePaper, referenceRSSNet);
            
            //int combinationScheme = 1;
            float alpha_temp = (float) alpha;
            //int howToTrustAuthor = 1;
            //int howToTrustPaper = 2;
            
            if (howToTrustAuthor == 1) {
                TrustHybrid.computeTrustedAuthorHMLinearCombinationAndPutIntoModelForAuthorList(authors, alpha_temp, combinationScheme);
            } else if (howToTrustAuthor == 2) {
                int metaTrustType = 1;
                TrustHybrid.computeMetaTrustedAuthorHMAndPutIntoModelForAuthorList(authors, referenceRSSNet, metaTrustType, alpha_temp);
            } else if (howToTrustAuthor == 3) {
                int metaTrustType = 2;
                TrustHybrid.computeMetaTrustedAuthorHMAndPutIntoModelForAuthorList(authors, referenceRSSNet, metaTrustType, alpha_temp);
            }
            
            TrustHybrid.computeTrustedPaperHMAndPutIntoModelForAuthorList(authors, howToTrustPaper);

            TrustHybrid.trustRecommendToAuthorList(authors, topRecommend);
            
            //</editor-fold>
        } else if (algorithm_Recommendation == 5) {
            //<editor-fold defaultstate="collapsed" desc="TRUST BASED LINEAR COMBINATION">           
            CBFController.cbfComputeRecommendingScore(authors, papers,
                    paperIdsOfAuthorTestSet, paperIdsInTestSet,
                    combinePaperOfAuthor, weightingPaperOfAuthor,
                    timeAware, gamma,
                    combineCandiatePaper, weightingCandidatePaper, 0,
                    pruning);
            TrustDataModelPreparation.computeCoAuthorRSSHM(authors, fileNameAuthors, fileNamePapers);
            HashMap<String, HashMap<String, Float>> referenceRSSNet = new HashMap<>();
            TrustDataModelPreparation.computeCitationAuthorRSSHM(authors, fileNameAuthors, fileNamePaperCitePaper, referenceRSSNet);

            //int combinationScheme = 1;
            float alpha_temp = (float) alpha;
            //int howToTrustAuthor = 1;
            //int howToTrustPaper = 2;
            
            if (howToTrustAuthor == 1) {
                TrustHybrid.computeTrustedAuthorHMLinearCombinationAndPutIntoModelForAuthorList(authors, alpha_temp, combinationScheme);
            } else if (howToTrustAuthor == 2) {
                int metaTrustType = 1;
                TrustHybrid.computeMetaTrustedAuthorHMAndPutIntoModelForAuthorList(authors, referenceRSSNet, metaTrustType, alpha_temp);
            } else if (howToTrustAuthor == 3) {
                int metaTrustType = 2;
                TrustHybrid.computeMetaTrustedAuthorHMAndPutIntoModelForAuthorList(authors, referenceRSSNet, metaTrustType, alpha_temp);
            }
            
            TrustHybrid.computeTrustedPaperHMAndPutIntoModelForAuthorList(authors, howToTrustPaper);

            combinationScheme = 1;
            alpha = (float) 0.3;
            TrustHybrid.computeCBFTrustLinearCombinationAndPutIntoModelForAuthorList(authors, alpha, combinationScheme);

            TrustHybrid.trustHybridRecommendToAuthorList(authors, topRecommend);
            
            //</editor-fold>
        }
    }
    
    //evaluation accuracy of recommendation algorithm
    public StringBuilder evaluate(HashMap<String, Author> authors, int measure_Evaluation, int topRank) throws Exception {
        StringBuilder evaluationResult = new StringBuilder();
        if (measure_Evaluation == 1) {
            double evaluation = Evaluator.computeMeanPrecisionTopN(authors, topRank);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("Precision\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        } else if (measure_Evaluation == 2) {
            double evaluation = Evaluator.computeMeanRecallTopN(authors, topRank);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("Recall\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        } else if (measure_Evaluation == 3) {
            double evaluation = Evaluator.computeMeanFMeasure(authors, 1);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("F1\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        } else if (measure_Evaluation == 4) {
            double evaluation = Evaluator.computeMAP(authors, topRank);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("MAP\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        } else if (measure_Evaluation == 5) {
            double evaluation = Evaluator.computeMeanNDCG(authors, topRank);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("NDCG\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        } else if (measure_Evaluation == 6) {
            double evaluation = Evaluator.computeMRR(authors);
            evaluation = Math.round(evaluation * 1000) / 1000.0d;

            evaluationResult.append("MRR\t").append(topRank).append("\t")
                    .append(evaluation).append("\r\n");
        }
        return evaluationResult;
    }
}
