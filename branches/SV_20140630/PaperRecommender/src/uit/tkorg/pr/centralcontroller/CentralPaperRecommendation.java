/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uit.tkorg.pr.centralcontroller;
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
import uit.tkorg.pr.dataimex.NUSDataset2;
import uit.tkorg.pr.dataimex.PRGeneralFile;
import uit.tkorg.pr.datapreparation.cbf.AuthorFVComputation;
import uit.tkorg.pr.datapreparation.cbf.PaperFVComputation;
import uit.tkorg.pr.datapreparation.cf.CFRatingMatrixComputation;
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
public class CentralPaperRecommendation {
        public double gama;// tham so gama cho 
        public int weightingAuthor;// trong so author
        public int weightingPaper;// trong so paper
        public int combiningAuthor;// phuong thuc combining author
        public int combiningPaper; // phuong thuc combining paper
        public int topN;// topN recommend
        public int rank;// rank K evaluation
        public String resultEvaluation;// giu ket qua danh gia
        public String DatasetFolder;
        public String SaveDataFolder;
        public String fileNamePapers; 
        public String fileNamePaperCitePaper;
        public String fileNameAuthorTestSet; 
        public String fileNameGroundTruth;
        public String fileNameAuthorship;
        public String fileNameAuthorCitePaper;
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
                    // neu gui chon la dataset sample thi goi lenh nay load Data set len form
                    case importDatasetSample:// luc dau tab import data training va testing se bi vo hieu hoa chuc nang
                          DatasetFolder = "SampleData\\";
                          fileNameAuthorTestSet = DatasetFolder + "Authors.csv";
                          fileNameGroundTruth = DatasetFolder + "GroundTruth.csv";
                          fileNameAuthorship = DatasetFolder + "Paper_Cite_Author.csv";
                          fileNamePapers = DatasetFolder + "Papers.csv";
                          fileNamePaperCitePaper = DatasetFolder +"Paper_Cite_Paper.csv";
                      
                        break;
                    case importDatasetFromScource://neu dc chon thi active panel,set cac file data voi duong dan dc chon
                        
                        break;
                    case importData:
                        authors = MASDataset1.readAuthorListTestSet(fileNameAuthorTestSet, fileNameGroundTruth, fileNameAuthorship);
                        papers = MASDataset1.readPaperList(fileNamePapers,fileNamePaperCitePaper);
                        PRGeneralFile.writePaperAbstractToTextFile(papers, dirPapers);
                        PaperFVComputation.clearPaperAbstract(papers);
                        TextPreprocessUtility.parallelProcess(dirPapers, dirPreProcessedPaper, true, true);
                        TextVectorizationByMahoutTerminalUtility.textVectorizeFiles(dirPreProcessedPaper, sequenceDir, vectorDir);
                        HashMap<String, HashMapVector> vectorizedDocuments = MahoutFile.readMahoutVectorFiles(vectorDir);
                        PaperFVComputation.setTFIDFVectorForAllPapers(papers, vectorizedDocuments);
                        break;
                    case stopImportData:
                        break;
                    case dataPreparationCB:
                        break;
                    case contructUserProfile:
                        HashSet<String> paperIdsOfAuthorTestSet = AuthorFVComputation.getPaperIdsOfAuthors(authors);
                        PaperFVComputation.computeFeatureVectorForAllPapers(papers, paperIdsOfAuthorTestSet,combiningAuthor,1);
                        AuthorFVComputation.computeFVForAllAuthors(authors,papers,weightingAuthor,gama);
                        break;
                    case contructPaperFV:
                        PaperFVComputation.computeFeatureVectorForAllPapers(papers,null,combiningPaper,weightingPaper);
                        break;
                    case saveModel:
                        break;
                    case dataPreparationCF:
                        break;
                    case contructMatrixInput:
                        break;
                    case loadExistentMatrix:
                        break;
                    case saveMatrixToFile:
                        break;
                    case loadModel:
                        break;
                    case recommendation:
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
                    case help:
                        break;
                    case computeTFIDF:
                        break;
                    case drawChart:
                        break;
                    case reset:
                        break;
                    default:
                        
                    
                 }
                } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            response[0] = "Fail.";
            }
            return response;
        }
}
