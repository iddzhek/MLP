package NLP2;

public class NeuralNetworkTraining implements Runnable{
    public MultiLayerPerceptron multiLayerPerceptron;

    public NeuralNetworkTraining(MultiLayerPerceptron b) {
        multiLayerPerceptron = b;
    }

    @Override
    public void run() {
//        System.out.print(NeuralNetworkReporting.ANSI_BLUE);
//        System.out.println("Starting workout [" + multiLayerPerceptron.name + "] ...");
        multiLayerPerceptron.trainNeuralNetwork(0.45, 0.2);
    }
}
