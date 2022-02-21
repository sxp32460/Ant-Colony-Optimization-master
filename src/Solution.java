import com.sun.tools.javac.util.ArrayUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {
    public static Map<String, Double> map = new HashMap<>();
    //have to complete in the next step
    public double solutionfrommachinelearning(int features[])
    {
        //ad2dom = new Random();//ourfunction(mandatoryfeatures(features[]))
        Arrays.sort(features);
        Collections.reverse(Arrays.asList(features));
        double sol=0.0f;

        //Checking if we have a solution with us
        for ( String key : map.keySet() ) {
            if(key.equals(Arrays.toString(features)))
            {
                return map.get(Arrays.toString(features));
            }
        }
        //else running for solution and adding it to the map
        sol=generateRandomFloat(0.1f,0.999f);
        map.put(Arrays.toString(features), sol);

        return sol;

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
