import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class GAFitnessCalc {
//    static double getFitness(int[] features, GAParameters gaParameters) {
//        return generateRandomFloat(0.1f,0.99f);
//    }
//    public static float generateRandomFloat(float min, float max) {
//        if (min >= max)
//            throw new IllegalArgumentException("max must be greater than min");
//        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
//        if (result >= max) // correct for rounding
//            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
//        return result;
//    }

//     Calculate individuals' fitness by comparing it to our candidate solution
    static double getFitness(int[] features, GAParameters gaParameters) {
        String[] weight_tokens = gaParameters.weights.split(",");

        ////////////////
        // Parameters //
        ////////////////
        BufferedReader reader = null;
        int no_of_features = features.length;
        int no_of_features_selected = 0;
        double overall_performance_sum = 0;
        String parameters_to_be_deleted = ""; // concatenated string of features to be removed

        //////////////////////////////////////////////////////////////////////////////
        // Among the selected features, find the ones which contain all null values //
        // (those which appear as string in the arff file) and set them in the 'individual' to 0
        //////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < no_of_features; i++) { // for each feature
            if (features[i] == 1) { // if this feature is not selected to be removed by GA
                no_of_features_selected++;
                int count = 0;
                for (int j = 0; j < gaParameters.no_of_os_instances; j++) { // for each instance
                    boolean feature_is_null = true;
                    // check if this feature is all null (string in arff)
                    try {
                        reader = new BufferedReader(new FileReader(gaParameters.work_folder + "/train_instance_" + (j+1)));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty()) {
                                String[] tokens = line.split(",");
                                if (!(tokens[i].equals("?"))) {
                                    feature_is_null = false;
                                    break;
                                }
                            }
                        }

                        if (feature_is_null) {
                            count++;
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // if all null, then set it to be removed in the 'parameters_to_be_deleted'
                if (count > 0)
                    features[i] = 0;
            }
        }

        //////////////////////////////////////////////////////
        // Check if all features are selected to be removed //
        //////////////////////////////////////////////////////
        int no_of_zeros_in_chromosome = 0;
        // for each feature
        for (int feature : features)
            if (feature == 0)
                no_of_zeros_in_chromosome++;

        // If all features are selected to be removed, return 0's
        if (no_of_zeros_in_chromosome == no_of_features)
            return 0;

        // If all features are not selected to be removed
        if (no_of_zeros_in_chromosome > 0) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Find the positions of the features to be removed and set them to the 'parameters_to_be_deleted' string //
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            for (int i = 0; i < no_of_features; i++) {
                if (features[i] == 0) {
                    parameters_to_be_deleted += (i+1);
                    parameters_to_be_deleted += ",";
                }
            }
            // Remove the last comma generated by the for loop
            parameters_to_be_deleted = parameters_to_be_deleted.substring(0, parameters_to_be_deleted.length()-1); // remove the last comma
        }

        ////////////////
        // TRAIN TEST //
        ////////////////
        for (int test_instance_no = 0; test_instance_no < gaParameters.no_of_os_instances; test_instance_no++) { // for each instance
            double performance_sum = 0;

            // For each test instance
            for (int train_instance_no = 0; train_instance_no < gaParameters.no_of_os_instances; train_instance_no++) {
                if (train_instance_no != test_instance_no) {
                    try {
                        performance_sum += new ClassifyML().train_test(gaParameters.work_folder + "/train_instance_" + (train_instance_no+1) + ".arff", gaParameters.work_folder + "/train_instance_" + (test_instance_no+1) + ".arff", parameters_to_be_deleted, gaParameters.classifier, false, true).get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            overall_performance_sum += performance_sum / (gaParameters.no_of_os_instances - 1);
        }

        double result = overall_performance_sum / gaParameters.no_of_os_instances;

        ////////////////////////
        // Return the results //
        ////////////////////////
        double classification_result = result * Double.parseDouble(weight_tokens[0]);
        double feature_result = (((no_of_features - no_of_features_selected) / (double) no_of_features) * 100) * Double.parseDouble(weight_tokens[1]);

        System.out.println("Result:" + classification_result + " " +feature_result);
        return classification_result + feature_result;
    }
}
