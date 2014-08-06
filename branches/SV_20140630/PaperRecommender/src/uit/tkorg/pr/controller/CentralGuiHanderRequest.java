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
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.utility.textvectorization.TextPreprocessUtility;
import uit.tkorg.utility.textvectorization.TextVectorizationByMahoutTerminalUtility;

/**
 *
 * @author Zoe
 */
public class CentralGuiHanderRequest {

    public double gama;// tham so gama cho 
    public double pruning;//tham so deu chinh cho pruning cho paper
    public int weightingAuthor = 0;// trong so author
    public int weightingPaper = 0;// trong so paper
    public int combiningAuthor = 0;// phuong thuc combining author
    public int combiningPaper = 0; // phuong thuc combining paper
    public int recommendationMethod = 1; //1: CBF, 2: CF
    public int cfMethod;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
    public int topNRecommend;// topNRecommend RECOMMEND
    public int topRank;// topRank K EVALUATE
    public int kNeighbor;// so hang xom
    public int methodEvaluation;// phuong phap danh gia
    public String SaveDataFolder;//
    public String fileNamePapers; //File 1
    public String fileNamePaperCitePaper;// File 2
    public String fileNameAuthors;// File 3
    public String fileNameAuthorPaper;// File 4
    public String fileNameAuthorCitePaper;// File 5
    public String fileNameGroundTruth;// File 6
    public String dirPapers = "Temp\\Text";
    public String dirPreProcessedPaper = "Temp\\Preprocess";
    public String sequenceDir = "Temp\\Sequence";
    public String vectorDir = "Temp\\VectorDir";
    public String MahoutCFDir = "Temp\\MahoutDir";
    public String fileNameEvaluationResult="Temp\\ResultEvaluation.txt";// ten file ket qua danh gia
    public String fileNameRecommenList="Temp\\RecommendationList.txt";// ten file danh sach RECOMMEND
    public String fileNameMatrixExistent;
    public HashMap<String, Paper> papers = new HashMap<>();
    public HashMap<String, Author> authors = new HashMap<>();
    public HashMap<String, Paper> papersOfAuthors = new HashMap<>();

    public CentralGuiHanderRequest() {
        gama = 0;// tham so gama cho 
        pruning = 0;//tham so deu chinh cho pruning cho paper
        weightingAuthor = 0;// trong so author
        weightingPaper = 0;// trong so paper
        combiningAuthor = 0;// phuong thuc combining author
        combiningPaper = 0; // phuong thuc combining paper
        recommendationMethod = 1; //1: CBF, 2: CF
        cfMethod = 1;//1: KNN Pearson, 2: KNN Cosine, 3: SVD
        topNRecommend = 0;// topNRecommend RECOMMEND
        topRank = 0;// topRank K EVALUATE
        kNeighbor = 0;// so hang xom
        methodEvaluation = 0;// phuong phap danh gia
        SaveDataFolder = null;//
        fileNamePapers = null; //File 1
        fileNamePaperCitePaper = null;// File 2
        fileNameAuthors = null;// File 3
        fileNameAuthorPaper = null;// File 4
        fileNameAuthorCitePaper = null;// File 5
        fileNameGroundTruth = null;// File 6
        fileNameEvaluationResult = null;// ten file ket qua danh gia
        fileNameRecommenList = null;// ten file danh sach RECOMMEND
        papers = new HashMap<>();
        authors = new HashMap<>();
        papersOfAuthors = new HashMap<>();
    }

    public String[] guiHanderResquest(Options request) {
        String[] response = new String[2];

        try {
            switch (request) {
                case IMPORT_DATA: // import du lieu cac filename se dc truyen tu giao dien
                    authors = MASDataset1.readAuthorListTestSet(fileNameAuthors, fileNameGroundTruth, fileNameAuthorPaper);
                    papers = MASDataset1.readPaperList(fileNamePapers, fileNamePaperCitePaper);
                    PRGeneralFile.writePaperAbstractToTextFile(papers, dirPapers);
                    CBFPaperFVComputation.clearPaperAbstract(papers);
                    TextPreprocessUtility.parallelProcess(dirPapers, dirPreProcessedPaper, true, true);
                    TextVectorizationByMahoutTerminalUtility.textVectorizeFiles(dirPreProcessedPaper, sequenceDir, vectorDir);
                    HashMap<String, HashMapVector> vectorizedDocuments = MahoutFile.readMahoutVectorFiles(vectorDir);
                    CBFPaperFVComputation.setTFIDFVectorForAllPapers(papers, vectorizedDocuments);
                    break;
                case STOP_IMPORT_DATA:
                    break;
                case CONSTRUCT_AUTHOR_PROFILE: // xay dung profile nguoi dung
                    HashSet<String> paperIdsOfAuthorTestSet = CBFAuthorFVComputation.getPaperIdsOfAuthors(authors);
                    //CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet, combiningPaper, weightingPaper,pruning);
                    CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet, combiningPaper, weightingPaper, 0.0);
                    CBFAuthorFVComputation.computeFVForAllAuthors(authors, papers, weightingAuthor, gama);
                    break;
                case CONSTRUCT_PAPER_FV:// xay dung vector dac trung cho bai bao
                    //CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers,null,combiningPaper,weightingPaper,pruning);
                    CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, null, combiningPaper, weightingPaper, 0.0);
                    break;
                case SAVE_MODEL:// save model
                    break;
                case CONSTRUCT_MATRIX_CF: // build matrix input
                    CFController.cfPrepareMatrix(fileNameAuthorCitePaper,MahoutCFDir + "\\CFRatingMatrixOriginal.txt");
                    break;
                case LOAD_EXISTENT_MODEL:// load mot matrix da co san
                    File userPreferencesFile = new File(fileNameMatrixExistent);
                    DataModel dataModel = new FileDataModel(userPreferencesFile);
                   
                    break;
                case LOAD_MODEL:
                    response[0] = "Scucess";
                    break;
                case RECOMMEND:
                    recommend();
                    break;
                case EVALUATE:
                    response[1] = Evaluation(authors, methodEvaluation, topRank).toString();
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
                    FileUtils.writeStringToFile(new File(fileNameRecommenList), recommendList.toString(), "UTF8", true);
                    break;
                case SAVE_EVALUATION_RESULT:
                      FileUtils.writeStringToFile(new File(fileNameEvaluationResult),Evaluation(authors, methodEvaluation, topRank).toString(), "UTF8", true);
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
        if (recommendationMethod == 1) {
            //Content - based method
            FeatureVectorSimilarity.computeCBFSimAndPutIntoModelForAuthorList(authors, papers, 0);
            FeatureVectorSimilarity.generateRecommendationForAuthorList(authors, topNRecommend);
        } else if (recommendationMethod == 2) {
            //CF method
            HashSet<String> paperIds = new HashSet<>();
            paperIds = CBFAuthorFVComputation.getPaperIdsTestSet(authors);
            if (cfMethod == 1) {
                //CF method with KNN Pearson
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds,kNeighbor);
            } else if (cfMethod == 2) {
                //CF method with KNN Cosine
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds,kNeighbor);
            } else if (cfMethod == 3) {
                //CF method with SVD
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds,kNeighbor);
            }
        }
    }
   
    public StringBuilder Evaluation(HashMap<String, Author> authors, int method, int rank) throws Exception {
        StringBuilder evaluationResult = new StringBuilder();
        evaluationResult.append("\r\nMeasure\t").append("TopRank\t").append("Result").append("\r\n");
        if (method == 0) {
           evaluationResult.append("Precision\t").append(rank).append("\t")
            .append(Evaluator.computeMeanPrecisionTopN(authors,rank)).append("\r\n")
            .append("Recall\t").append(rank).append("\t").append(Evaluator.computeMeanRecallTopN(authors,rank)).append("\r\n")
            .append("F1\t").append("").append("\t").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n")
            .append("MAP\t").append(rank).append("\t").append(Evaluator.computeMAP(authors, 10)).append("\r\n")
            .append("NDCG\t").append(rank).append("\t").append(Evaluator.computeMeanNDCG(authors,rank)).append("\r\n")
            .append("MRR\t").append("").append("\t").append(Evaluator.computeMRR(authors)).append("\r\n");
        }
        else if (method == 1) {
            evaluationResult.append("Precision\t").append(rank).append("\t")
                    .append(Evaluator.computeMeanPrecisionTopN(authors, rank)).append("\r\n");
        } else if (method == 2) {
            evaluationResult.append("Recall\t").append(rank).append("\t")
                    .append(Evaluator.computeMeanRecallTopN(authors, rank)).append("\r\n");
        } else if (method == 3) {
            evaluationResult.append("F1\t").append("").append("\t").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n");
        } else if (method == 4) {
            evaluationResult.append("MAP\t").append(rank).append("\t").append(Evaluator.computeMAP(authors, 10)).append("\r\n");        
        } else if (method == 5) {
            evaluationResult.append("NDCG\t").append(rank).append("\t").append(Evaluator.computeMeanNDCG(authors,rank)).append("\r\n");  
        } else if (method == 6) {
            evaluationResult.append("MRR\t").append("").append("\t").append(Evaluator.computeMRR(authors)).append("\r\n");
        } 
        return evaluationResult;
    }

    
}
