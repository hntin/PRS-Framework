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
        public int weightingAuthor;// trong so author
        public int weightingPaper;// trong so paper
        public int combiningAuthor;// phuong thuc combining author
        public int combiningPaper; // phuong thuc combining paper
        public int topN;// topN recommend
        public int rank;// rank K evaluation
        public int k;// so hang xom
        public String SaveDataFolder;//
        public String fileNamePapers; //File 1
        public String fileNamePaperCitePaper;// File 2
        public String fileNameAuthors;// File 3
        public String fileNameAuthorPaper;// File 4
        public String fileNameAuthorCitePaper;// File 5
        public String fileNameGroundTruth;// File 6
        public String dirPapers;
        public String dirPreProcessedPaper;
        public String sequenceDir;
        public String vectorDir;
        public String MahoutCFDir;
        public String fileNameEvaluationResult;
        public HashMap<String, Paper> papers = new HashMap<>();
        public HashMap<String, Author> authors = new HashMap<>();
        public HashMap<String, Paper> papersOfAuthors = new HashMap<>();
        public String[] guiHanderResquest(Options request)
        {
            String [] response = new String[2];
           
             try {
                switch (request) {
                    case importData: // import du lieu cac filename se dc truyen tu giao dien
                        authors = MASDataset1.readAuthorListTestSet(fileNameAuthors, fileNameGroundTruth, fileNameAuthorPaper);
                        papers = MASDataset1.readPaperList(fileNamePapers,fileNamePaperCitePaper);
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
                        CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet, combiningPaper, weightingPaper,pruning);
                        CBFAuthorFVComputation.computeFVForAllAuthors(authors,papers,weightingAuthor,gama);
                        break;
                    case contructPaperFV:// xay dung vector dac trung cho bai bao
                        CBFPaperFVComputation.computeFeatureVectorForAllPapers(papers,null,combiningPaper,weightingPaper,pruning);
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
                    case recommendationCB:
                          CBFPaperFVComputation.clearTFIDF(papers);
                          FeatureVectorSimilarity.computeCBFSimAndPutIntoModelForAuthorList(authors, papers,0);
                          FeatureVectorSimilarity.generateRecommendationForAuthorList(authors, topN);
                        break;
                    case recommendationCFKNN:/// xuat hien form de dien cac tham so k hang xom
                        
                        break;
                    case recommendationCFSVD:// xuat hien form de dien cac tham so cho SVD
                        break;
                    case precision:
                        StringBuilder evaluationResultPrecision = new StringBuilder();
                        evaluationResultPrecision.append("Precision\t").append("P@").append(rank).append(": ")
                        .append(Evaluator.computeMeanPrecisionTopN(authors,rank)).append("\r\n");
                        response[1]=evaluationResultPrecision.toString();
                        break;
                    case recall:
                        StringBuilder evaluationResultRecall = new StringBuilder();
                        evaluationResultRecall.append("Recall\t").append("R@").append(rank).append(": ")
                        .append(Evaluator.computeMeanRecallTopN(authors,rank)).append("\r\n");
                        response[1]=evaluationResultRecall.toString();
                        response[0] = "Success.";
                        break;
                    case f1 :
                        StringBuilder evaluationResultF1= new StringBuilder();
                        evaluationResultF1.append("F1\t").append("F1: ").append(Evaluator.computeMeanFMeasure(authors, 1)).append("\r\n");
                        response[1]= evaluationResultF1.toString();
                        break;
                    case map:
                        StringBuilder evaluationResultMAP = new StringBuilder();
                        evaluationResultMAP.append("MAP\t").append("MAP@10: ").append(Evaluator.computeMAP(authors, 10)).append("\r\n");
                        response[1]= evaluationResultMAP.toString();
                        response[0] = "Success.";
                        break;
                    case ndcg:
                        StringBuilder evaluationResultNDCG = new StringBuilder();
                        evaluationResultNDCG.append("NDCG\t").append("NDCG@").append(rank).append(": ").append(Evaluator.computeMeanNDCG(authors,rank)).append("\r\n");
                        response[1]= evaluationResultNDCG.toString();
                        response[0] = "Success.";
                        break;
                    case mrr:
                        StringBuilder evaluationResultMRR = new StringBuilder();
                        evaluationResultMRR.append("MRR\t").append(Evaluator.computeMRR(authors)).append("\r\n");
                        response[1] = evaluationResultMRR.toString();
                        response[0] = "Success.";
                        break;
                    case errorAnalysis:
                        break;
                    case help:
                        break;
                    case reset:
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
        public static void main(String[] args){

        }
}
