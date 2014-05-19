/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.tkorg.pr.utility.general;

import java.io.*;
import java.util.*;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

/**
 *
 * @author Vinh
 */
public class CF {

    public static void main(String[] args, int k, int n, String path) throws Exception {
        File userPreferencesFile = new File(path);
        System.out.println(userPreferencesFile.getAbsolutePath());
        DataModel dataModel = new FileDataModel(userPreferencesFile);

        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(k, userSimilarity, dataModel);

        // Create a generic user based recommender with the dataModel, the userNeighborhood and the userSimilarity
        Recommender genericRecommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
        BufferedWriter bw = new BufferedWriter(new FileWriter("result.txt"));

        // Recommend 5 items for each user
        for (LongPrimitiveIterator iterator = dataModel.getUserIDs(); iterator.hasNext();) {
            long userId = iterator.nextLong();

            // Generate a list of 5 recommendations for the user
            List<RecommendedItem> itemRecommendations = genericRecommender.recommend(userId, n);

            System.out.format("User Id: %d%n", userId);

            if (itemRecommendations.isEmpty()) {
                System.out.println("No recommendations for this user.");
                bw.write(userId + "\t" + " No recommendations for this user." + "\n");
            } else {
                // Display the list of recommendations
                for (RecommendedItem recommendedItem : itemRecommendations) {
                    System.out.format("Recommened Item Id %d. Strength of the preference: %f%n", recommendedItem.getItemID(), recommendedItem.getValue());
                    bw.write(userId + "\t" + recommendedItem.getItemID() + "\t" + recommendedItem.getValue() + "\n");
                }
            }
        }
        bw.close();
    }

    public static void saveCFMatrix() {

    }
}
