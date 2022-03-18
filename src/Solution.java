import com.sun.tools.javac.util.ArrayUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {
    public static Map<String, Double> map = new HashMap<>();
    //have to complete in the next step

    public double solutionfrommachinelearning(int[] features_input,GAParameters gaParameters)
    {

        //ad2dom = new Random();//ourfunction(mandatoryfeatures(features[]))
        Arrays.sort(features_input);
        int features[] =new int[gaParameters.no_of_features];
        for(int i=0;i<gaParameters.no_of_features;i++)
        {
            features[i]=0;
        }
        for(int i=0;i<gaParameters.no_of_features;i++)
        {

            if(features_input[i]>0)
            {
                features[features_input[i]-1]=1;
            }
        }

        //Checking if we have a solution with us
        for ( String key : map.keySet() ) {
            if(key.equals(Arrays.toString(features))) {
                return map.get(Arrays.toString(features));
            }
        }
        //else running for solution and adding it to the map


        // SET THE PARAMETERS HERE
        double fitness = GAFitnessCalc.getFitness(features, gaParameters);

        map.put(Arrays.toString(features), fitness);

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
