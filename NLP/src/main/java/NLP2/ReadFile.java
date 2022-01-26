package NLP2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {

    /**
     * Convert entries ArrayList to a built-in Array
     * @param entries entries from file
     * @return entries array dataset
     */
    private static double[][] convertEntriesToArray(ArrayList<double[]> entries) {
        double[][] converted = new double[entries.size()][4];

        for (int i = 0; i < converted.length; i++)
            for (int j = 0; j < converted[i].length; j++)
                converted[i][j] = entries.get(i)[j];

        return converted;
    }

    /**
     * Convert outputs Array List to a built-in Array
     * @param outputs outputs from file
     * @return outputs array dataset
     */
    private static double[] convertOutputsToArray(ArrayList<Double> outputs) {
        double [] converted = new double[outputs.size()];

        for (int i = 0; i < converted.length; i++)
            converted[i] = outputs.get(i);

        return converted;
    }

    public static double[][] readFileEntries(String fileName) {
        ArrayList<double[]> entries = new ArrayList<double[]>();

        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] dataSet = data.split(",");

                entries.add(new double[]{
                        Double.parseDouble(dataSet[0]),
                        Double.parseDouble(dataSet[1]),
                        Double.parseDouble(dataSet[2]),
                        Double.parseDouble(dataSet[3]),
                });
            }

            myReader.close();

            return convertEntriesToArray(entries);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public static double[] readFileOutputs(String fileName) {
        ArrayList<Double> expectedOutputs = new ArrayList<Double>();

        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] dataSet = data.split(",");

                expectedOutputs.add( ReadFile.mapOutputs(dataSet[4]) );
            }

            myReader.close();

            return convertOutputsToArray(expectedOutputs);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public static double mapOutputs(String flowerName) {
        switch (flowerName) {
            case "Iris-virginica":
                return 0.5;
            case "Iris-versicolor":
                return 1;
            case "Iris-setosa":
                return 0;
            default:
                return 0;
        }
    }
}
