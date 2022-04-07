

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {
    public static Map<String, Double> map = new HashMap<>();
    //have to complete in the next step

    public double solutionfrommachinelearning(int[] features_input,GAParameters gaParameters)
    {
        Arrays.sort(features_input);

        // Checking if we already have a solution for the feature set selected
        for (String key : map.keySet())
            if(key.equals(Arrays.toString(features_input)))
                return map.get(Arrays.toString(features_input));

        int[] features = new int[gaParameters.no_of_features];

        // initialize array
        for(int i=0;i<gaParameters.no_of_features;i++)
            features[i]=0;

        // fill in 'features' array
        for(int i=0;i<gaParameters.no_of_features;i++)
            if(features_input[i]>0)
                features[features_input[i]-1]=1;

        double fitness = GAFitnessCalc.getFitness(features, gaParameters);

        map.put(Arrays.toString(features_input), fitness);

        return fitness;

    }

    public static float generateRandomFloat(float min, float max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
    }
}
