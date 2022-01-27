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
    private int samplesubsetsize=3;

    public AntColonyOptimization(int numberOfFeatures,int maxNumberOfAnts)
    {
        this.numberOfStartingAnts=numberOfStartingAnts;
        this.maxNumberOfAnts=maxNumberOfAnts;

        for(int i=0;i<numberOfStartingAnts;i++)//intitilizings ants at random points
            ants.add(new Ant(numberOfFeatures));
        generatefeatures(numberOfFeatures);
        currentIndex = 0;
        for(int i=0;i<numberOfStartingAnts;i++)//intitilizings ants at random points
            ants.add(new Ant(numberOfFeatures));
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

    public int[] solve()
    {
        System.out.println("Started Optimization");
        setupAnts();
        System.out.println("Done with the initilizing of ants");
        for(int i=0;i<maxIterations;i++)
        {
            moveAnts();
            updateBest();
        }
        s+=("\nBest solution: " + (bestSolution));
        s+=("\nBest tour order: " + Arrays.toString(bestFeatures));
        return bestFeatures.clone();
    }
    private void setupAnts()
    {

        double tempsolution=0.0;//will use this variable in last
        for(Ant ant:ants) {
            for (int j = 0; j < 5; j++)
                ant.selectfeature(pickRandom(ant));//initilizing the ants at random places
            tempsolution = ant.trailsolution();
            ant.pheramone += tempsolution;//each ant will be initilized with five random fetures
            ant.antSolution=tempsolution;
            System.out.println("Initilized "+maxNumberOfAnts+" with the five random fetures each");
        }

    }
    private int pickRandom(Ant a)//will help pick the feture avoiding the previously selected feture
    {
        while(true)
        {
            int randomNumber =random.nextInt(numberOfFeatures);
            if(!a.isSelected(randomNumber))
            {
                return randomNumber;
            }
        }
    }
    private void moveAnts()
    {
        //step-1 we will decide that each ant will get how many chance to run based on the pheramone level
        int totalPheramone=0;
        for(Ant a:ants)
        {
            if(a.isChild)
                totalPheramone+=a.pheramone;
        }
        for(Ant a:ants)
        {
            if(a.isChild)
                a.antFactor=(int)(a.pheramone/totalPheramone)*maxNumberOfAnts;
        }
        //step-2 we will create child nodes and run the Ants based on ant factor
        for(Ant ant:ants) {
            if (ant.isChild && ant.antFactor>0){
                double tempsolution=0.0;//will use this variable in last
                //now creating ants based on the ant rank
                ant.isChild=false;
                //creating the ants running them and addind into the list
                for(int i=0;i<antFactor;i++)
                {
                    Ant a = ant;
                    a.isChild=true;
                    for (int j = 0; j < 5; j++)
                        ant.selectfeature(pickRandom(a));//initilizing the ants at random places
                    tempsolution = a.trailsolution();//runs for sollution
                    a.pheramone += tempsolution;//each ant will be initilized with five random fetures
                    a.antSolution=tempsolution;
                    ants.add(a);//adding ant to the list

                }

            }
        }

    }

}

