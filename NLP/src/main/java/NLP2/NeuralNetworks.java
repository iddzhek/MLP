package NLP2;

public class NeuralNetworks {
    public MultiLayerPerceptron multiLayerPerceptron;

    private static String TRAIN_DATASET_PATH = "./iris_train.txt";
    private static String TEST_DATASET_PATH = "./iris_test.txt";
    private int numberOfNetworks;

    private double[][] entries;
    private double[] outputs;
    private double[][] testEntries;
    private double[] testOutputs;

    static NeuralNetworkTraining[] neuralNetworks;
    static Thread[] networkThreads;
    private NeuralNetworkReporting[] reports;

    public NeuralNetworks(int numberOfNetworks) {
        this.numberOfNetworks = numberOfNetworks;

        neuralNetworks = new NeuralNetworkTraining[numberOfNetworks];
        networkThreads = new Thread[numberOfNetworks];
        reports = new NeuralNetworkReporting[numberOfNetworks];

//        System.out.println("Calling the contructor");
        this.entries = ReadFile.readFileEntries(TRAIN_DATASET_PATH);
        this.outputs = ReadFile.readFileOutputs(TRAIN_DATASET_PATH);
        this.testEntries = ReadFile.readFileEntries(TEST_DATASET_PATH);
        this.testOutputs = ReadFile.readFileOutputs(TEST_DATASET_PATH);
    }

    private MultiLayerPerceptron createNeuralNetwork(String name) {
        return new MultiLayerPerceptron( name, entries, outputs, testEntries, testOutputs );
    }

    private void startAllNetworksTraining() {
        for (int i = 0; i < numberOfNetworks; i++)
            networkThreads[i].start();
    }

    private void awaitForAllNetworks() {
        try{

            for (int i = 0; i < numberOfNetworks; i++)
                networkThreads[i].join();

        } catch (InterruptedException e) {
            System.out.println("Network interrupted");
            e.printStackTrace();
        }
    }

    private NeuralNetworkReporting createReport(int networkIndex) {
        MultiLayerPerceptron mlp = neuralNetworks[networkIndex].multiLayerPerceptron;
        NeuralNetworkReporting report = new NeuralNetworkReporting(mlp);
        reports[networkIndex] = report;

        return report;
    };

    public void runAll() {
        // Creates the networks
        for (int i = 0; i < numberOfNetworks; i++)
            neuralNetworks[i] = new NeuralNetworkTraining(createNeuralNetwork("MLP-" + i));

        // Creates the threads
        for (int i = 0; i < numberOfNetworks; i++)
            networkThreads[i] = new Thread(neuralNetworks[i]);

        // Starts all networks training asynchronously
        startAllNetworksTraining();

        // Await for all networks training finish
        awaitForAllNetworks();
//        System.out.print(NeuralNetworkReporting.ANSI_RESET);

        System.out.println("\nTraining is over \n\nTestes: ");

        // Generates all reports
        for (int i = 0; i < numberOfNetworks; i++) {
            NeuralNetworkReporting report = createReport(i);
            report.log();
        }

        NeuralNetworkReporting bestMLPReport = NeuralNetworkReporting.findBestResult(reports);
        MultiLayerPerceptron bestMLP = bestMLPReport.multiLayerPerceptron;
        System.out.print("\n");
//        System.out.print(NeuralNetworkReporting.ANSI_GREEN);
        System.out.println("Better neural network: " + bestMLPReport.mlpName);
//        System.out.print(NeuralNetworkReporting.ANSI_RESET);

        NeuralNetworkReporting.countNetworksConversions(reports);
    }
}
