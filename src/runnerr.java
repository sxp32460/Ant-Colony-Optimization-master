public class runnerr {
    public static void main(String[] args){

        GAParameters gaParameters = new GAParameters();
        gaParameters.no_of_features=1000;
        gaParameters.maximum_number_of_ants=16;
        gaParameters.maximum_iterations=100;
        AntColonyOptimization antTSP = new AntColonyOptimization(gaParameters);
        antTSP.startAntOptimization();

    }
}
