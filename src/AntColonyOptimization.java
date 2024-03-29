import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static int numberOfFeatures;
    private int numberOfStartingAnts;
    private int graph[];
    private int trails[];
    public List<Ant> ants = new ArrayList<>();
    private Random random = new Random();
    private int currentIndex;
    private int[] bestFeatures;
    private double bestSolution=0.0;
    private int maxNumberOfAnts;
    private int samplesubsetsize=3;
    public static int rank =0;
    public GAParameters gaParameters;
    int max_threads=Runtime.getRuntime().availableProcessors()-1;
    ExecutorService WORKER_THREAD_POOL
            = Executors.newFixedThreadPool(max_threads);

    int fetures_to_be_slelected_in_each_loop;

    public AntColonyOptimization(GAParameters gaParameters)
    {
        this.gaParameters=gaParameters;
        this.numberOfFeatures=gaParameters.no_of_features;
        this.maxNumberOfAnts=gaParameters.maximum_number_of_ants;
        this.maxIterations=gaParameters.maximum_iterations;
        generatefeatures(numberOfFeatures);
        currentIndex = 0;
        System.out.println("Initilized "+gaParameters.maximum_number_of_ants+" "+gaParameters.maximum_iterations+ " "+ gaParameters.no_of_features);
        for(int i=0;i<maxNumberOfAnts;i++)//intitilizings ants at random points
            ants.add(new Ant(numberOfFeatures));
        fetures_to_be_slelected_in_each_loop=numberOfFeatures/maxIterations;

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
        Ant bestant=new Ant(numberOfFeatures);
        for (Ant a : ants)
        {
            if (a.antSolution  > bestSolution)
            {
                bestSolution = a.antSolution;
                bestFeatures = a.trail.clone();

                bestant=a;
                System.out.println("updated best solution " +bestSolution+" fetures "+Arrays.toString(bestFeatures));
            }
        }
        bestant.pheramone=bestant.pheramone*2;
    }

    public int[] solve()
    {
        System.out.println("Started Optimization");
        setupAnts();
        System.out.println("Done with the initilizing of ants");
        for(int i=0;i<maxIterations;i++)
        {
            System.out.println("Started Moving Ants");
            moveAnts();
            System.out.println("done with "+i+" loops" );
            updateBest();
        }
        s+=("\nBest solution: " + (bestSolution));
        s+=("\nBest tour order: " + Arrays.toString(bestFeatures));
        System.out.println(s);
        System.out.println(ants.size() );
        WORKER_THREAD_POOL.shutdown();
        return bestFeatures.clone();
    }
    private void setupAnts()
    {

        double tempsolution=0.0;//will use this variable in last
        for(Ant ant:ants) {
            for (int j = 0; j < numberOfFeatures%maxIterations; j++) {
                ant.selectfeature(pickRandom(ant));//initilizing the ants at random places
                tempsolution = ant.trailsolution(gaParameters);
                ant.pheramone += tempsolution;//each ant will be initilized with five random fetures
                ant.antSolution=tempsolution;
                rank++;
                ant.rank=rank;
            }



        }
        updateBest();
    }

    private int pickRandom(Ant a)//will help pick the feture avoiding the previously selected feture
    {
        int stuck=0;
        while(true)
        {
            int randomNumber =random.nextInt(numberOfFeatures+1);


            if(!a.isSelected(randomNumber) && randomNumber>0 && randomNumber<=numberOfFeatures)
            {

                return randomNumber;
            }

            stuck++;
        }
    }
    private void moveAnts()
    {
        //step-1 we will decide that each ant will get how many chance to run based on the pheramone level
        int totalPheramone=0;
        List<Ant> copy = new ArrayList<>();
        List<Ant> temp = new ArrayList<>();
        System.out.println("moving ants");
        for(Ant a:ants)
        {
            copy.add(a);
            if(a.isChild) {

                totalPheramone += a.pheramone;
            }
        }
        System.out.println(totalPheramone);
        for(Ant a:ants)
        {
            if(a.isChild)
                a.antFactor=(int)Math.round((a.pheramone/totalPheramone)*maxNumberOfAnts);
        }
        //step-2 we will create child nodes and run the Ants based on ant factor

        for(Ant ant:copy) {


            if (ant.isChild && ant.antFactor>0){

                //now creating ants based on the ant rank
                ant.isChild=false;
                //creating the ants running them and addind into the list
                for(int i=0;i<ant.antFactor;i++)
                {
                    Ant a = new Ant(ant,numberOfFeatures);
                    a.isChild=true;
                    for (int j = 0; j < fetures_to_be_slelected_in_each_loop; j++) {
                        a.selectfeature(pickRandom(a));//initilizing the ants at random places

                    }

                    rank++;
                    a.rank=rank;
                    temp.add(a);

                }

            }
        }
        Set < Callable < String >> callable = new HashSet <Callable< String >> ();
        //creatiting the threades to add to executer function
        for(Ant a:temp)
        {
//            tempsolution = a.trailsolution();//runs for sollution
//            a.pheramone += tempsolution;//each ant will be initilized with five random fetures
//            a.antSolution=tempsolution;

            //here we run adds in in the threads

            callable.add(new Callable < String > () {
                public String call() throws Exception {
                    double tempsolution=0.0;//will use this variable in last
                    tempsolution = a.trailsolution(gaParameters);//runs for sollution
                    a.pheramone += tempsolution;//each ant will be initilized with five random fetures
                    a.antSolution=tempsolution;
                    System.out.println("na na its worng");
                    return null;
                }
            });

            ants.add(a);
        }
        //running the threads
        try {
            java.util.List<Future<String>> futures = WORKER_THREAD_POOL.invokeAll(callable);


        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        //selecting a random ant to run;
        Ant randomAnt=null;
        int priorityRank=999999;
        for(Ant a:ants) {
        if(a.rank<priorityRank && a.isChild==true)
        {
            priorityRank=a.rank;
        }
        }
        for(Ant a: ants)
        {
            if(a.rank==priorityRank)
            {
                double tempsolution=0.0;
                randomAnt = new Ant(a,numberOfFeatures);
                randomAnt.isChild=true;
                for (int j = 0; j < fetures_to_be_slelected_in_each_loop; j++) {
                    a.selectfeature(pickRandom(randomAnt));//initilizing the ants at random places

                }
                tempsolution = randomAnt.trailsolution(gaParameters);//runs for sollution
                randomAnt.pheramone += tempsolution;//each ant will be initilized with five random fetures
                randomAnt.antSolution=tempsolution;
                rank++;
                randomAnt.rank=rank;
                a.isChild=false;

            }
        }
        if (randomAnt!=null)
            ants.add(randomAnt);
        System.out.println(totalPheramone);
    }

}

