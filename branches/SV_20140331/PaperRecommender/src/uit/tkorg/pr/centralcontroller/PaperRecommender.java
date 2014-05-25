/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.centralcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uit.tkorg.pr.constant.PaperRecommenerConstant;
import uit.tkorg.pr.datapreparation.ComputeAuthorFV;
import uit.tkorg.pr.datapreparation.ComputePaperFV;
import uit.tkorg.pr.evaluation.Evaluator;
import uit.tkorg.pr.method.cb.ContentBasedRecommender;
import uit.tkorg.pr.model.Author;
import uit.tkorg.pr.model.Paper;
import uit.tkorg.pr.utility.general.Serializer;
import uit.tkorg.pr.dataimport.NUSDataset1;
import uit.tkorg.pr.method.cf.memorybased.KNNCF;
import uit.tkorg.pr.method.cf.memorybased.SVD;

/**
 *
 * @author THNghiep Central controller. Main entry class used for testing. Also
 * control all traffic from gui.
 */
public class PaperRecommender {

    // Key of this hash map is paper id.
    // Value of this hash map is the relevant paper object.
    private HashMap<String, Author> authors;

    // Key of this hash map is paper id.
    // Value of this hash map is the relevant paper object.
    private HashMap<String, Paper> papers;

    /**
     * This method is used as a entry point for testing.
     *
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        //KNNCF.CoPearsonRecommend("E:\\movies.csv", 2, 5, "E:\\result.txt");
        SVD.SVDRecommendation("E:\\test1.csv",3, 5,0.3,3,"E:\\ output4.txt");
    }

    /**
     * This method handles all request from gui.
     *
     * @param request
     * @param param
     * @return response: result of request after processing.
     */
    public String[] centralController(String request, String param) {
        String[] response = new String[2];

        String Dataset1Folder;
        String SaveDataFolder;

        try {
            switch (request) {

                // Dataset 1: data import.
                case "Read paper":
                    // Read param to get dataset 1 folder.
                    if ((param != null) && !(param.isEmpty())) {
                        Dataset1Folder = param;
                    } else {
                        Dataset1Folder = PaperRecommenerConstant.DATASETFOLDER;
                    }
                    papers = NUSDataset1.buildListOfPapers(Dataset1Folder);
                    response[0] = "Success.";
                    break;
                case "Read author":
                    // Read param to get dataset 1 folder.
                    if ((param != null) && !(param.isEmpty())) {
                        Dataset1Folder = param;
                    } else {
                        Dataset1Folder = PaperRecommenerConstant.DATASETFOLDER;
                    }
                    authors = NUSDataset1.buildListOfAuthors(Dataset1Folder);
                    response[0] = "Success.";
                    break;
                case "Save paper":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    Serializer.saveObjectToFile(papers, SaveDataFolder + "\\Papers.dat");
                    response[0] = "Success.";
                    break;
                case "Save author":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    Serializer.saveObjectToFile(authors, SaveDataFolder + "\\Authors.dat");
                    response[0] = "Success.";
                    break;
                case "Load paper":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    papers = (HashMap<String, Paper>) Serializer.loadObjectFromFile(SaveDataFolder + "\\Papers.dat");
                    response[0] = "Success.";
                    break;
                case "Load author":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    authors = (HashMap<String, Author>) Serializer.loadObjectFromFile(SaveDataFolder + "\\Authors.dat");
                    response[0] = "Success.";
                    break;

                // Dataset 1: data preparation.
                case "Paper FV linear":
                    papers = ComputePaperFV.computeAllPapersFeatureVector(papers, 0);
                    response[0] = "Success.";
                    break;
                case "Paper FV cosine":
                    papers = ComputePaperFV.computeAllPapersFeatureVector(papers, 1);
                    response[0] = "Success.";
                    break;
                case "Paper FV RPY":
                    papers = ComputePaperFV.computeAllPapersFeatureVector(papers, 2);
                    response[0] = "Success.";
                    break;
                case "Author FV linear":
                    authors = ComputeAuthorFV.computeAllAuthorsFeatureVector(authors, 0);
                    response[0] = "Success.";
                    break;
                case "Author FV cosine":
                    authors = ComputeAuthorFV.computeAllAuthorsFeatureVector(authors, 1);
                    response[0] = "Success.";
                    break;
                case "Author FV RPY":
                    authors = ComputeAuthorFV.computeAllAuthorsFeatureVector(authors, 2);
                    response[0] = "Success.";
                    break;
                case "Recommend":
                    authors = ContentBasedRecommender.buildAllRecommendationLists(authors, papers);
                    response[0] = "Success.";
                    break;
                case "NDCG5":
                    response[1] = String.valueOf(Evaluator.NDCG(authors, 5));
                    response[0] = "Success.";
                    break;
                case "NDCG10":
                    response[1] = String.valueOf(Evaluator.NDCG(authors, 10));
                    response[0] = "Success.";
                    break;
                case "MRR":
                    response[1] = String.valueOf(Evaluator.MRR(authors));
                    response[0] = "Success.";
                    break;
                case "Precision":
                    response[1] = String.valueOf(Evaluator.Precision(authors));
                    response[0] = "Success.";
                    break;
                case "Recall":
                    response[1] = String.valueOf(Evaluator.Recall(authors));
                    response[0] = "Success.";
                    break;
                case "MAP":
                    response[1] = String.valueOf(Evaluator.MAP(authors, 10));
                    response[0] = "Success.";
                    break;
                default:
                    response[0] = "Unknown.";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
            response[0] = "Fail.";
            return response;
        }

        return response;
    }
}
