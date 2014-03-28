/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.paperrecommender.controller.central;
import ir.vsr.HashMapVector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static javax.swing.UIManager.get;
import uit.tkorg.paperrecommender.constant.PaperRecommenerConstant;
import uit.tkorg.paperrecommender.controller.evaluation.Evaluator;
import uit.tkorg.paperrecommender.controller.recommendation.AuthorLogic;
import uit.tkorg.paperrecommender.controller.recommendation.PaperLogic;
import uit.tkorg.paperrecommender.controller.recommendation.Recommender;
import uit.tkorg.paperrecommender.model.Author;
import uit.tkorg.paperrecommender.model.Paper;
import uit.tkorg.paperrecommender.utility.Serializer;
import uit.tkorg.paperrecommender.utility.FlatFileData.ImportDataset1;


/**
 *
 * @author THNghiep
 * Central controller.
 * Main entry class used for testing.
 * Also control all traffic from gui.
 */
public class PaperRecommender {
    private AuthorLogic authorLogic;
    private PaperLogic paperLogic;
    private Recommender recommender;
    private Evaluator evaluator;
    /**
     * @param args the command line arguments
     * This method is used as a entry point for testing.
     */
    public static void main(String[] args) { 
      
        
        
    }
    
    /**
     * This method handles all request from gui.
     * @param request
     * @param param 
     * @return response: result of request after processing.
     */
    
    public String[] centralController(String request, String param) {
        String[] response = new String[2];
        
        authorLogic = new AuthorLogic();
        paperLogic = new PaperLogic();
        recommender = new Recommender();
        evaluator = new Evaluator();
        
        String Dataset1Folder;
        String SaveDataFolder;
        
        try {
            switch (request) {
                case "Read paper":
                    // Read param to get dataset 1 folder.
                    if ((param != null) && !(param.isEmpty())) {
                        Dataset1Folder = param;
                    } else {
                        Dataset1Folder = PaperRecommenerConstant.DATASETFOLDER;
                    }
                    paperLogic.buildListOfPapers(Dataset1Folder);
                    response[0] = "Success.";
                    break;
                case "Read author":
                   // Read param to get dataset 1 folder.
                    if ((param != null) && !(param.isEmpty())) {
                        Dataset1Folder = param;
                    } else {
                        Dataset1Folder = PaperRecommenerConstant.DATASETFOLDER;
                    }
                    authorLogic.buildListOfAuthors(Dataset1Folder);
                    response[0] = "Success.";
                    break;
                case "Save paper":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    Serializer.saveObjectToFile(paperLogic, SaveDataFolder + "\\PaperLogic.dat");
                    response[0] = "Success.";
                    break;
                case "Save author":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    Serializer.saveObjectToFile(authorLogic, SaveDataFolder + "\\AuthorLogic.dat");
                    response[0] = "Success.";
                    break;
                case "Load paper":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    paperLogic = (PaperLogic) Serializer.loadObjectFromFile(SaveDataFolder + "\\PaperLogic.dat");
                    response[0] = "Success.";
                    break;
                case "Load author":
                    // Read param to get save data folder.
                    if ((param != null) && !(param.isEmpty())) {
                        SaveDataFolder = param;
                    } else {
                        SaveDataFolder = PaperRecommenerConstant.SAVEDATAFOLDER;
                    }
                    authorLogic = (AuthorLogic) Serializer.loadObjectFromFile(SaveDataFolder + "\\AuthorLogic.dat");
                    response[0] = "Success.";
                    break;
                case "Paper FV linear":
                   paperLogic.computeAllPapersFeatureVector(0);
                    response[0] = "Success.";
                    break;
                case "Paper FV cosine":
                    paperLogic.computeAllPapersFeatureVector(1);
                    response[0] = "Success.";
                    break;
                case "Paper FV RPY":
                    paperLogic.computeAllPapersFeatureVector(2);
                    response[0] = "Success.";
                    break;
                case "Author FV linear":
                    authorLogic.computeAllAuthorsFeatureVector(0);
                    response[0] = "Success.";
                    break;
                case "Author FV cosine":
                    authorLogic.computeAllAuthorsFeatureVector(1);
                    response[0] = "Success.";
                    break;
                case "Author FV RPY":
                    authorLogic.computeAllAuthorsFeatureVector(2);
                    response[0] = "Success.";
                    break;
                case "Recommend":
                    recommender.buildAllRecommendationLists(authorLogic.getAuthors(), paperLogic.getPapers());
                    response[0] = "Success.";
                    break;
                case "NDCG5":
                    response[1] = String.valueOf(evaluator.NDCG(authorLogic.getAuthors(), 5));
                    response[0] = "Success.";
                    break;
                case "NDCG10":
                    response[1] = String.valueOf(evaluator.NDCG(authorLogic.getAuthors(), 10));
                    response[0] = "Success.";
                    break;
                case "MRR":
                    response[1] = String.valueOf(evaluator.MRR(authorLogic.getAuthors()));
                    response[0] = "Success.";
                    break;
                default: 
                    response[0] = "Unknown.";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response[0] = "Fail.";
            return response;
        }
        
        return response;
    }
}
