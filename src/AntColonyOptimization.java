import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * default
 * private double c = 1.0;             //number of trails
 * private double alpha = 1;           //pheromone importance
 * private double beta = 5;            //distance priority
 * private double evaporation = 0.5;
 * private double Q = 500;             //pheromone left on trail per ant
 * private double antFactor = 0.8;     //no of ants per node
 * private double randomFactor = 0.01; //introducing randomness
 * private int maxIterations = 1000;
 */

public class AntColonyOptimization {
    public String s="";
    //pheromone left on trail per ant
    private double antFactor;     //no of ants per node
    private int randomFactor; //introducing randomness
    private int maxIterations;
    private int numberOfFeatures;
    private int numberOfStartingAnts;
    private int graph[];
    private int trails[];
    private List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private int currentIndex;
    private int[] bestFeatures;
    private double bestSolution=0.0;
    private int maxNumberOfAnts;

    public AntColonyOptimization(int numberOfFeatures,int maxNumberOfAnts)
    {
        this.numberOfStartingAnts=numberOfStartingAnts;
        this.maxNumberOfAnts=maxNumberOfAnts;

        for(int i=0;i<numberOfStartingAnts;i++)//intitilizings ants at random points
            ants.add(new Ant(numberOfFeatures));
        generatefeatures(numberOfFeatures);
        currentIndex = 0;
    }

    public int[] generatefeatures(int n)
    {
        int[] features = new int[n];

        for(int i=0;i<n;i++)
        {
            features[i]=i+1;
        }
        return features;
    }
    /**
     * Perform ant optimization
     */
    public void startAntOptimization()
    {
        solve();
    }
    /**
     * Update the best solution
     */
    private void updateBest()
    {
        if (bestFeatures== null)
        {
            bestFeatures = ants.get(0).trail;
            bestSolution = ants.get(0).trailsolution();
        }

        for (Ant a : ants)
        {
            if (a.trailsolution() < bestSolution)
            {
                bestSolution = a.trailsolution();
                bestFeatures = a.trail.clone();
            }
        }
    }
    private void clearTrails()
    {
        for(int i=0;i<numberOfFeatures;i++)
        {
            trails[i]=0;
        }
    }
    public int[] solve()
    {
        setupAnts();
        clearTrails();
        for(int i=0;i<maxIterations;i++)
        {
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nBest solution: " + (bestSolution));
        s+=("\nBest tour order: " + Arrays.toString(bestFeatures));
        return bestFeatures.clone();
    }
}

