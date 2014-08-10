/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.controller;

import ir.vsr.HashMapVector;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import uit.tkorg.pr.constant.Options;
import uit.tkorg.pr.dataimex.MASDataset1;
import uit.tkorg.pr.dataimex.MahoutFile;
import uit.tkorg.pr.dataimex.PRGeneralFile;
import uit.tkorg.pr.datapreparation.CBFAuthorFVComputation;
import uit.tkorg.pr.datapreparation.CBFPaperFVComputation;
import uit.tkorg.pr.evaluation.Evaluator;
import uit.tkorg.pr.method.cbf.FeatureVectorSimilarity;
import uit.tkorg.pr.method.cf.CF;
import uit.tkorg.pr.method.hybrid.CBFCF;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.utility.textvectorization.TextPreprocessUtility;
import uit.tkorg.utility.textvectorization.TextVectorizationByMahoutTerminalUtility;

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
    
    public HashMap<String, Author> authorsCFP = new HashMap<>();
    public HashMap<String, Author> authorsCFC = new HashMap<>();
    public HashMap<String, Author> authorsCFSVD = new HashMap<>();

    public int algorithm_Recommendation; //1: CBF, 2: CF, 3: CBFCFHybrid

    public int combineAuthor;
    public int weightingAuthor;
    public int timeAware;
    public double gamma;
    public int combinePaper;
    public int weightingPaper;
    public double pruning;

    public int cfMethod;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
    public int kNeighbourhood;
    public int f;
    public double l;
    public int i;

    public double alpha;
    public int combineHybrid;

    public int topRecommend;

    public int measure_Evaluation;
    public int topRank;

    //</editor-fold>
    public PRSCentralController() {
        gamma = 0;// tham so gamma cho 
        pruning = 0.0;//tham so deu chinh cho pruning cho paper
        timeAware = 0;// trong so author
        weightingPaper = 0;// trong so paper
        combineAuthor = 0;// phuong thuc combining author
        combinePaper = 0; // phuong thuc combining paper
        algorithm_Recommendation = 1; //1: CBF, 2: CF
        cfMethod = 1;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
        topRecommend = 0;// topRecommend RECOMMEND
        topRank = 0;// topRank K EVALUATE
        kNeighbourhood = 8;// so hang xom
        measure_Evaluation = 0;// phuong phap danh gia
        alpha = 0.9f;
        combineHybrid = 1;
        fileNamePapers = null; //File 1
        fileNamePaperCitePaper = null;// File 2
        fileNameAuthors = null;// File 3
        fileNameAuthorPaper = null;// File 4
        fileNameAuthorCitePaper = null;// File 5
        fileNameGroundTruth = null;// File 6
        _fileName_EvaluationResult = null;// ten file ket qua danh gia
        _fileName_RecommendationList = null;// ten file danh sach RECOMMEND
        papers = new HashMap<>();
        authors = new HashMap<>();
        paperIdsOfAuthorTestSet = new HashSet<>();
        paperIdsInTestSet = new HashSet<>();
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
                    CBFPaperFVComputation.computeTFIDFFromPaperAbstract(papers, dirPapers, dirPreProcessedPaper, sequenceDir, vectorDir);
                    CBFPaperFVComputation.readTFIDFFromMahoutFile(papers, vectorDir);
                    // Clear no longer in use objects.
                    // Always clear abstract.
                    CBFPaperFVComputation.clearPaperAbstract(papers);
                    // Step 4:
                    // Get list of papers to process.
                    paperIdsOfAuthorTestSet = CBFAuthorFVComputation.getPaperIdsOfAuthors(authors);
                    paperIdsInTestSet = CBFAuthorFVComputation.getPaperIdsTestSet(authors);
                    break;
                case RECOMMEND:
                    recommend();
                    break;
                case EVALUATE:
                    response[1] = evaluate(authors, measure_Evaluation, topRank).toString();
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
                    break;
                case SAVE_EVALUATION_RESULT:
                    FileUtils.writeStringToFile(new File(_fileName_EvaluationResult), evaluate(authors, measure_Evaluation, topRank).toString(), "UTF8", true);
                    break;
                case RESET:
                    papers = new HashMap<>();
                    authors = new HashMap<>();
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

            CBFController.cbfComputeRecommendingScore(authors, papers,
                    paperIdsOfAuthorTestSet, paperIdsInTestSet,
                    combineAuthor, weightingAuthor,
                    timeAware, gamma,
                    combinePaper, weightingPaper, 0,
                    pruning);

            estimatedTime = System.nanoTime() - startTime;
            System.out.println("CBF recommendation elapsed time: " + estimatedTime / 1000000000 + " seconds");
            System.out.println("End CBF recommendation.");
        } else if (algorithm_Recommendation == 2) {
            //CF method
            long startTime;
            long estimatedTime;
            System.out.println("Begin CF recommendation...");
            startTime = System.nanoTime();

            CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod,
                    authors, paperIdsInTestSet, kNeighbourhood, f, l, i);
            CF.cfRecommendToAuthorList(authors, topRecommend);

            estimatedTime = System.nanoTime() - startTime;
            System.out.println("CF recommendation elapsed time: " + estimatedTime / 1000000000 + " seconds");
            System.out.println("End CF recommendation.");
        } else if (algorithm_Recommendation == 3) {
            float alpha_temp = (float) alpha;
            CBFController.cbfComputeRecommendingScore(authors, papers,
                    paperIdsOfAuthorTestSet, paperIdsInTestSet,
                    combineAuthor, weightingAuthor,
                    timeAware, gamma,
                    combinePaper, weightingPaper, 0,
                    pruning);
            CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir,
                    cfMethod, authors, paperIdsInTestSet, kNeighbourhood, f, l, i);
            alpha = (float) 0.9;
            CBFCF.computeCBFCFCombinationAndPutIntoModelForAuthorList(authors, alpha_temp, combineHybrid);
            CBFCF.cbfcfHybridRecommendToAuthorList(authors, topRecommend);
        }
    }

    //evaluation accuracy of recommendation algorithm
    public StringBuilder evaluate(HashMap<String, Author> authors, int measure_Evaluation, int topRank) throws Exception {
        StringBuilder evaluationResult = new StringBuilder();
        if (measure_Evaluation == 1) {
            evaluationResult.append("Precision\t").append(topRank).append("\t")
                    .append(Evaluator.computeMeanPrecisionTopN(authors, topRank)).append("\r\n");
        } else if (measure_Evaluation == 2) {
            evaluationResult.append("Recall\t").append(topRank).append("\t")
                    .append(Evaluator.computeMeanRecallTopN(authors, topRank)).append("\r\n");
        } else if (measure_Evaluation == 3) {
            evaluationResult.append("F1\t").append(topRank).append("\t").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n");
        } else if (measure_Evaluation == 4) {
            evaluationResult.append("MAP\t").append(topRank).append("\t").append(Evaluator.computeMAP(authors, topRank)).append("\r\n");
        } else if (measure_Evaluation == 5) {
            evaluationResult.append("NDCG\t").append(topRank).append("\t").append(Evaluator.computeMeanNDCG(authors, topRank)).append("\r\n");
        } else if (measure_Evaluation == 6) {
            evaluationResult.append("MRR\t").append(topRank).append("\t").append(Evaluator.computeMRR(authors)).append("\r\n");
        }
        return evaluationResult;
    }
}
