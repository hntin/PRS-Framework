/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.controller;

import ir.vsr.HashMapVector;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FileUtils;
import uit.tkorg.pr.constant.PRConstant;
import uit.tkorg.pr.constant.Options;
import uit.tkorg.pr.dataimex.MASDataset1;
import uit.tkorg.pr.dataimex.MahoutFile;
import uit.tkorg.pr.dataimex.NUSDataset1;
import uit.tkorg.pr.dataimex.PRGeneralFile;
import uit.tkorg.pr.datapreparation.CBFAuthorFVComputation;
import uit.tkorg.pr.datapreparation.CBFPaperFVComputation;
import uit.tkorg.pr.datapreparation.CFRatingMatrixComputation;
import uit.tkorg.pr.evaluation.Evaluator;
import uit.tkorg.pr.method.cbf.FeatureVectorSimilarity;
import uit.tkorg.pr.method.cf.KNNCF;
import uit.tkorg.pr.method.cf.SVDCF;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.utility.general.BinaryFileUtility;
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
    public int topNRecommend;// topNRecommend recommend
    public int topRank;// topRank K evaluation
    public int kNeighbor;// so hang xom
    public int methodEvaluation;// phuong phap danh gia
    public String authorId;
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
    public String fileNameEvaluationResult;// ten file ket qua danh gia
    public String fileNameRecommenList;// ten file danh sach recommend
    public HashMap<String, Paper> papers = new HashMap<>();
    public HashMap<String, Author> authors = new HashMap<>();
    public HashMap<String, Paper> papersOfAuthors = new HashMap<>();

    public String [] guiHanderResquest(Options request) {
        String[] response = new String[2];

        try {
            switch (request) {
                case importData: // import du lieu cac filename se dc truyen tu giao dien
                    authors = MASDataset1.readAuthorListTestSet(fileNameAuthors, fileNameGroundTruth, fileNameAuthorPaper);
                    papers = MASDataset1.readPaperList(fileNamePapers, fileNamePaperCitePaper);
                    PRGeneralFile.writePaperAbstractToTextFile(papers, dirPapers);
                    CBFPaperFVComputation.clearPaperAbstract(papers);
                    TextPreprocessUtility.parallelProcess(dirPapers, dirPreProcessedPaper, true, true);
                    TextVectorizationByMahoutTerminalUtility.textVectorizeFiles(dirPreProcessedPaper, sequenceDir, vectorDir);
                    HashMap<String, HashMapVector> vectorizedDocuments = MahoutFile.readMahoutVectorFiles(vectorDir);
                    CBFPaperFVComputation.setTFIDFVectorForAllPapers(papers, vectorizedDocuments);
                    break;
                case stopImportData:
                    break;
                case contructUserProfile: // xay dung profile nguoi dung
                    HashSet<String> paperIdsOfAuthorTestSet = CBFAuthorFVComputation.getPaperIdsOfAuthors(authors);
                    //  CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet, combiningPaper, weightingPaper,pruning);
                    CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet, combiningPaper, weightingPaper, 0.0);
                    CBFAuthorFVComputation.computeFVForAllAuthors(authors, papers, weightingAuthor, gama);
                    break;
                case contructPaperFV:// xay dung vector dac trung cho bai bao
                    //CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers,null,combiningPaper,weightingPaper,pruning);
                    CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, null, combiningPaper, weightingPaper, 0.0);
                    break;
                case saveModel:// save model
                    break;
                case contructMatrixInput: // build matrix input
                    CFController.cfPrepareMatrix(fileNameAuthorCitePaper, MahoutCFDir);
                    break;
                case loadExistentMatrix:// load mot matrix da co san
                    MahoutFile.readMahoutCFRating(MahoutCFDir, authors);
                    break;
                case loadModel:
                    break;
                case recommend:
                    recommend();
                    break;
                case showRecommendList:
                    response[1] = findListRecommendOfAuthor(authorId, authors).toString();
                    break;
                case evaluation:
                    response[1]= Evaluation(authors,methodEvaluation, topRank).toString();
                    break;
                case errorAnalysis:
                    break;
                case help:
                    break;
                case saveRecommendList:
                    StringBuilder recommendList = new StringBuilder();
                    for (String authorId: authors.keySet())
                        recommendList.append(authorId+":\n").append(authors.get(authorId).getRecommendationList().toString()+"\r\n");
                    FileUtils.writeStringToFile(new File(fileNameEvaluationResult), recommendList.toString(), "UTF8", true);
                    break;
                case reset:
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
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds);
            } else if (cfMethod == 2) {
                //CF method with KNN Cosine
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds);
            } else if (cfMethod == 3) {
                //CF method with SVD
                CFController.cfComputeRecommendingScore(fileNameAuthorCitePaper, MahoutCFDir, cfMethod, authors, paperIds);
            }
        }
    }
    
    public StringBuilder Evaluation(HashMap<String,Author> authors,int method,int rank) throws Exception{
        StringBuilder evaluationResult = new StringBuilder();
        if(method==0){
            evaluationResult.append("Precision\t").append("P@").append(rank).append(": ")
            .append(Evaluator.computeMeanPrecisionTopN(authors,rank)).append("\r\n");
        }else if(method==1) {   
            evaluationResult.append("Recall\t").append("R@").append(rank).append(": ")
            .append(Evaluator.computeMeanRecallTopN(authors,rank)).append("\r\n");
        } else if(method==2){
            StringBuilder evaluationResultF1 = new StringBuilder();
            evaluationResultF1.append("F1\t").append("F1: ").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n");
        }else if(method==3){
            evaluationResult.append("MAP\t").append("MAP@10: ").append(Evaluator.computeMAP(authors, 10)).append("\r\n");        
        }else if(method==4){
           evaluationResult.append("NDCG\t").append("NDCG@").append(rank).append(": ").append(Evaluator.computeMeanNDCG(authors,rank)).append("\r\n");  
        }else if(method==5){
           evaluationResult.append("MRR\t").append(Evaluator.computeMRR(authors)).append("\r\n");
        }else if(method==6){
           evaluationResult.append("Precision\t").append("P@").append(rank).append(": ")
            .append(Evaluator.computeMeanPrecisionTopN(authors,rank)).append("\r\n")
            .append("Recall\t").append("R@").append(rank).append(": ").append(Evaluator.computeMeanRecallTopN(authors,rank)).append("\r\n")
            .append("F1\t").append("F1: ").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n")
            .append("MAP\t").append("MAP@10: ").append(Evaluator.computeMAP(authors, 10)).append("\r\n")
            .append("NDCG\t").append("NDCG@").append(rank).append(": ").append(Evaluator.computeMeanNDCG(authors,rank)).append("\r\n")
            .append("MRR\t").append(Evaluator.computeMRR(authors)).append("\r\n");
        }
        return evaluationResult;            
    }

    public void reset() {
        weightingAuthor = 0;
        weightingPaper = 0;
        combiningAuthor = 0;
        combiningPaper = 0;
        recommendationMethod = 1;
        papers = new HashMap<>();
        authors = new HashMap<>();
    }

    public List<String> findListRecommendOfAuthor(String authorId, HashMap<String, Author> authors) {
        List listRecommend = authors.get(authorId).getRecommendationList();
        return listRecommend;
    }

    public static void main(String[] args) {

    }
}
