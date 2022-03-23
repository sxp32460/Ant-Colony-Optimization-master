import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        String work_folder = "./"; // default folder for workspace
        String protocol = null; // name of the TCP/IP protocol to be processed
        int classifier = 0; // default classifier (J48)

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-w":
                case "--workfolder":
                    work_folder = args[i + 1];
                    i++;
                    break;
                case "-p":
                case "--protocol":
                    protocol = args[i + 1];
                    i++;
                    break;
                case "-c":
                case "--classifier":
                    classifier = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                default:
                    System.out.println(ANSI_RED + "Unknown parameter '" + args[i] + "' " + ANSI_GREEN + "Type -h to see the help menu." + ANSI_RESET);
                    System.exit(0);
            }
        }

        String tsharkselected_features_list_path = work_folder + "/TsharkSelected/" + protocol;

        GAParameters gaParameters = new GAParameters();
        gaParameters.maximum_number_of_ants = 16;
        gaParameters.maximum_iterations = 100;
        gaParameters.weights = "0.95,0.05";
        gaParameters.classifier = classifier;
        gaParameters.tournamentSize = 5;
        gaParameters.uniformRate = 0.5;
        gaParameters.mutationRate = 0.05;
        gaParameters.work_folder = work_folder + "/features/" + protocol;
        if (new File(tsharkselected_features_list_path).exists())
            gaParameters.no_of_features = get_no_of_lines(tsharkselected_features_list_path);

        AntColonyOptimization antTSP = new AntColonyOptimization(gaParameters);
        antTSP.startAntOptimization();
    }

    // Returns the number of lines in a file
    public static int get_no_of_lines(String filename) {
        int no_of_lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = reader.readLine()) != null)
                if (!line.trim().isEmpty())
                    no_of_lines++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return no_of_lines;
    }
}
