package NLP2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MultiLayerPerceptron {
    private static final int LIMIT_OF_STEPS = 10000;
    private static final double MINIMUM_ACCEPTABLE_ERROR = 0.01;

    public String name;

    // Matrix with all input values
    double entries[][];

    // Vector with respective expected output values
    double expectedOutputs[];

    // Matrix with all weight corrections of last iteration
    double weightsCorrections[][];

    // Neuron weights
    private double weights[][] = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };

    // Matrix with all gradients of each weight
    private double gradients[][];

    // Vector with deltas of each neuron (H1, H1, O1)
    private double deltas[];

    public int step = 0;

    public double[][] getTestEntries() {
        return testEntries;
    }

    public double[] getTestOutputs() {
        return testOutputs;
    }

    private double[][] testEntries;
    private double[] testOutputs;


    public MultiLayerPerceptron(String name, double [][] entries, double[] expectedOuts, double[][] testEntries, double[] testOutputs ) {
        this.name = name;

        this.entries = entries;
        this.expectedOutputs = expectedOuts;

        this.testEntries = testEntries;
        this.testOutputs = testOutputs;

        this.weightsCorrections = new double[5][5];
        this.gradients = new double[5][5];
        this.deltas = new double[5];

        this.generateRandomWeights();
    }

    private void calculateDeltas(int inputLineIndex){ // pad indica a linha com as entradas
        double result = calculateNeuralNetworkResult(entries[inputLineIndex]);
        double error = expectedOutputs[inputLineIndex] - result;

        deltas[4] = (result * (1- result)) * error;

        double Y0 = calculateSingleNeuronResult(0, entries[inputLineIndex]);
        double Y1 = calculateSingleNeuronResult(1, entries[inputLineIndex]);
        double Y2 = calculateSingleNeuronResult(2, entries[inputLineIndex]);
        double Y3 = calculateSingleNeuronResult(3, entries[inputLineIndex]);

        deltas[0] = ( Y0 * (1 - Y0)) * weights[1][4] * deltas[4];
        deltas[1] = ( Y1 * (1 - Y1)) * weights[2][4] * deltas[4];
        deltas[2] = ( Y2 * (1 - Y2)) * weights[3][4] * deltas[4];
        deltas[3] = ( Y3 * (1 - Y3)) * weights[4][4] * deltas[4];
    }

    private void calculateGradients(int inputLineIndex){
        // calculus of gradients
        calculateDeltas(inputLineIndex); // calculate the deltas
        double Y0 = calculateSingleNeuronResult(0, entries[inputLineIndex]);//saída de H1
        double Y1 = calculateSingleNeuronResult(1, entries[inputLineIndex]);//saída de H2
        double Y2 = calculateSingleNeuronResult(2, entries[inputLineIndex]);//saída de H3
        double Y3 = calculateSingleNeuronResult(3, entries[inputLineIndex]);//saída de H4

        // gradients of bias (w0)
        gradients[0][0] =  deltas[0] * 1; //H1
        gradients[0][1] =  deltas[1] * 1; //H2
        gradients[0][2] =  deltas[2] * 1; //H3
        gradients[0][3] =  deltas[2] * 1; //H4
        gradients[0][4] =  deltas[2] * 1; //O1

        // gradients for w1
        gradients[1][0] =  deltas[0] * entries[inputLineIndex][0];// layer H1
        gradients[1][1] =  deltas[1] * entries[inputLineIndex][0];// layer H2
        gradients[1][2] =  deltas[2] * entries[inputLineIndex][0];// layer H3
        gradients[1][3] =  deltas[3] * entries[inputLineIndex][0];// layer H4
        gradients[1][4] =  deltas[4] * Y0; // layer O1
        // gradients for w2
        gradients[2][0] =  deltas[0] * entries[inputLineIndex][1]; // layer H1
        gradients[2][1] =  deltas[1] * entries[inputLineIndex][1]; // layer H2
        gradients[2][2] =  deltas[2] * entries[inputLineIndex][1]; // layer H3
        gradients[2][3] =  deltas[3] * entries[inputLineIndex][1]; // layer H4
        gradients[2][4] =  deltas[4] * Y1; // layer O1

        // gradients for w3
        gradients[3][0] =  deltas[0] * entries[inputLineIndex][2]; // layer H1
        gradients[3][1] =  deltas[1] * entries[inputLineIndex][2]; // layer H2
        gradients[3][2] =  deltas[2] * entries[inputLineIndex][2]; // layer H3
        gradients[3][3] =  deltas[3] * entries[inputLineIndex][2]; // layer H4
        gradients[3][4] =  deltas[4] * Y2; // layer O1

        // gradients for w4
        gradients[4][0] =  deltas[0] * entries[inputLineIndex][3]; // layer H1
        gradients[4][1] =  deltas[1] * entries[inputLineIndex][3]; // layer H2
        gradients[4][2] =  deltas[2] * entries[inputLineIndex][3]; // layer H3
        gradients[4][3] =  deltas[3] * entries[inputLineIndex][3]; // layer H4
        gradients[4][4] =  deltas[4] * Y3; // layer O1
    }

    void updateWeights( int inputLineIndex, double learningRate, double alpha) {
        calculateGradients(inputLineIndex);
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++){
                double weightVariation = learningRate * gradients[i][j] + alpha * weightsCorrections[i][j];
                weightsCorrections[i][j] = weightVariation;
                weights[i][j] = weights[i][j] + weightVariation;
            }
        }
    }

    private void generateRandomWeights() {
        Random rd = new Random();
        for (int i = 0; i < weights.length; i++)
            for (int j = 0; j < weights[i].length; j++)
                this.weights[i][j] = -1 + (1 - (-1)) * rd.nextFloat();
    }

    /**
     * Sigmoid function
     * @param sum
     * @return
     */
    double passSumTroughTheActivationFunction(double sum) {
        return 1 / (1 + Math.pow(2.718281828, -sum));
    }

    double calculateSingleNeuronResult(int weightColumn, double inputSet[] ) {
        // Get the bias weight
        double sum = weights[0][weightColumn];

        for (int i = 1; i < inputSet.length; i++)
            sum += weights[i][weightColumn] * inputSet[i - 1];

        return passSumTroughTheActivationFunction(sum);
    }

    /**
     * Process neural network result given the input values
     * @param inputSet the set of inputs to be processed by neural network
     * @return neural network result
     */
    double calculateNeuralNetworkResult(double inputSet[]) {
        double h1Result = calculateSingleNeuronResult(0, inputSet);
        double h2Result = calculateSingleNeuronResult(1, inputSet);
        double h3Result = calculateSingleNeuronResult(2, inputSet);
        double h4Result = calculateSingleNeuronResult(3, inputSet);

        double inputSetForOutputNeuron[] = new double[] { h1Result, h2Result, h3Result, h4Result };

        // Just passing the h1 and h2 outputs as entries for output neuron
        return calculateSingleNeuronResult(4, inputSetForOutputNeuron );
    }

    double calculateMeanSquareError() {
        double sum = 0;

        for (int i=0; i < entries.length; i++) {
            double result = calculateNeuralNetworkResult(entries[i]);
            sum += Math.pow(expectedOutputs[i] - result, 2); // Sum of errors squares
        }


        try(FileWriter writer = new FileWriter("gError.txt", true)){
            Double value = sum / entries.length;
            writer.write(String.valueOf(value));
            writer.append("\n");

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(sum / entries.length);
        // Mean of sum of errors squares
        return sum / entries.length;

    }

    double[] submitTests() {

        double[] outputs = new double[testOutputs.length];

        for (int i = 0; i < testEntries.length; i++){

            outputs[i] = calculateNeuralNetworkResult(testEntries[i]);

        }

        return outputs;
    }

    boolean hasConverged() {
        double meanSquareError = calculateMeanSquareError();

        if(meanSquareError > MINIMUM_ACCEPTABLE_ERROR)
            return false;

        return true;
    }


    void trainNeuralNetwork (double learningRate, double alpha) {

        step = 0;

        while (!hasConverged() && step < LIMIT_OF_STEPS) {

            for (int i = 0; i < entries.length; i++)
                updateWeights(i, learningRate, alpha);

            step++;
        }
    }
}
