package uit.tkorg.pr.constant;

import java.util.logging.Level;

public class PRConstant {

    public static final String FOLDER_NUS_DATASET1 = "D:\\LUAN VAN DAI HOC\\Tailieuluanvan\\DATASET\\20100825-SchPaperRecData\\20100825-SchPaperRecData";
    public static final String FOLDER_NUS_DATASET2= "E:\\Luan_van\\20131106-SchPaperRecData\\20131106-SchPaperRecData\\ScholarlyPaperRecData";
    public static final String FOLDER_MAS_DATASET1 = "C:\\Users\\Zoe\\Dropbox\\De tai Paper Recommendation\\Experiment\\Data\\Sample Data\\CSV\\Sample 2- Simple\\";
    public static final String SAVEDATAFOLDER = "E:\\ResE\\Paper recommendation\\Save Object Dataset 1";
    
    public static final String DB = "MYSQL";
    //public static final String DB = "SQLSERVER";

    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String DATABASE = "CSPublicationCrawler";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";

    public static final String HOSTMSSQLSERVER = "localhost";
    public static final String PORTMSSQLSERVER = "1433";
    public static final String DATABASEMSSQLSERVER = "CSPublicationCrawler";
    public static final String USERNAMEMSSQLSERVER = "sa";
    public static final String PASSWORDMSSQLSERVER = "12345";
     public static  DatasetType currentDatasetType = DatasetType.FileXML;
    //public static final Level LOGGING_LEVEL = Level.ALL;
    public static final Level LOGGING_LEVEL = Level.WARNING;
}