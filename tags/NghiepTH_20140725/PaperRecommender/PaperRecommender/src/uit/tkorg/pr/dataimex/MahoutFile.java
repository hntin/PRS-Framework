/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.dataimex;

import com.mysql.jdbc.StringUtils;
import ir.vsr.HashMapVector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import uit.tkorg.pr.model.Author;

/**
 *
 * @author THNghiep
 */
public class MahoutFile {

    private MahoutFile() {}

    /**
     * Read vector created by mahout.
     * @param vectorDir: directory outputted by mahout.
     * @return HashMap Integer key and String word.
     * @throws Exception 
     */
    public static HashMap<Integer, String> readMahoutDictionaryFiles(String vectorDir) throws Exception {
        HashMap<Integer, String> dictMap = new HashMap();

        Configuration conf = new Configuration();
        SequenceFile.Reader reader = new SequenceFile.Reader(FileSystem.get(conf), new Path(vectorDir + "\\dictionary.file-0"), conf);
        Text term = new Text();
        IntWritable dictKey = new IntWritable();

        // Note: sequence file mapping from term to its key code.
        // our map will map from key code to term.
        while (reader.next(term, dictKey)) {
            dictMap.put(Integer.valueOf(dictKey.toString()), term.toString());
        }
        reader.close();

        return dictMap;
    }

    /**
     * Read vector created by mahout.
     * @param vectorDir: directory outputted by mahout.
     * @return HashMap document's tf-idf vector.
     * @throws Exception 
     */
    public static HashMap<String, HashMapVector> readMahoutVectorFiles(String vectorDir) throws Exception {
        HashMap<String, HashMapVector> vectorizedDocuments = new HashMap<>();
        
        Configuration conf = new Configuration();
        SequenceFile.Reader reader = new SequenceFile.Reader(FileSystem.get(conf), new Path(vectorDir + "\\tfidf-vectors\\part-r-00000"), conf);
        Text key = new Text(); // document id.
        VectorWritable value = new VectorWritable(); // document content.
        while (reader.next(key, value)) {
            Vector vector = value.get();
            String documentId = key.toString();
            documentId = documentId.substring(documentId.lastIndexOf("/") + 1, documentId.length() - 4);
            // Other way: using regex.
//            Pattern pattern = Pattern.compile(".*/(\\d+)\\.txt");
//            Matcher matcher = pattern.matcher(documentId);
//            if (matcher.find()) {
//                documentId = matcher.group(1);
//            }
            HashMapVector vectorContent = new HashMapVector();
            Iterator<Vector.Element> iter = vector.nonZeroes().iterator();
            while (iter.hasNext()) {
                Vector.Element element = iter.next();
                vectorContent.increment(String.valueOf(element.index()), element.get());
            }
            vectorizedDocuments.put(documentId, vectorContent);
        }
        reader.close();
        
        return vectorizedDocuments;
    }
    
    public static void readMahoutCFRating(String MahoutCFRatingMatrixPredictionFile, HashMap<String, Author> authorTestSet) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(MahoutCFRatingMatrixPredictionFile))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                if ((line == null) || (line.equals(""))) {
                    break;
                }
                
                String[] str = line.split(",");
                String authorId = str[0];
                String paperId = str[1];
                String rating = str[2];
                
                if (authorTestSet.containsKey(authorId)) {
                    authorTestSet.get(authorId).getRecommendationList().add(paperId);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
