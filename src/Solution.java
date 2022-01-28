import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {

    //have to complete in the next step
    public double solutionfrommachinelearning(int features[])
    {
        //ad2dom = new Random();//ourfunction(mandatoryfeatures(features[]))

        return generateRandomFloat(0.1f,0.999f);

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
