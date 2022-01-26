package NLP;

import java.io.IOException;

public class MLP {

    double[] input;
    double[] hidden;
    double outer;
    double[][] wInputHidden;
    double[] wHiddenOuter;
//    double[][] patterns = {
//            {0,0}, {0,0}, {1,0}, {1,1}
//    };
//    double[] answer = {0, 0, 1, 0};

    Integer[][] patterns = DataSet.getTokenizedDataStudy();
    Integer[] answer = DataSet.getTokenizedAnswerData();

    Integer[][] training = DataSet.getTokenizedDataTraining();

    public MLP(){
        input = new double[patterns[0].length];
        hidden = new double[100];
        wInputHidden = new double[input.length][hidden.length];
        wHiddenOuter = new double[hidden.length];

        initializationWeight();
        study();

        double s = 0;

        for(int p = 0; p < training.length; p++) {
            for (int i = 0; i < input.length; i++) {
                input[i] = training[p][i];
            }
            if (outer > 0.51)
                outer = 1;
            else outer = 0;
            if(answer[p+2726] == outer)
                s++;

//        for(int p = 0; p < training.length; p++) {
//            if(answer[p] == outer)
//                s++;
//            for (int i = 0; i < input.length; i++) {
//                input[i] = patterns[p][i];
//            }

            countOuter();

//            System.out.println(outer);
        }
        System.out.println(s/ training.length);
    }

    public void initializationWeight(){
        for (int i = 0; i < wInputHidden.length; i++){
            for (int j = 0; j < wInputHidden[i].length; j++){
                wInputHidden[i][j] = Math.random() * 1 - 0.5;
            }
        }
        for (int i = 0; i < wHiddenOuter.length; i++){
            wHiddenOuter[i] = Math.random() * 1 - 0.5;
        }
    }

//    public void countOuter(){
//        for (int i = 0; i < hidden.length; i++){
//            hidden[i] = 0;
//            for (int j = 0; j < input.length; j++){
//                hidden[i] += input[j] * wInputHidden[j][i];
//            }
//            if(hidden[i] > 0.5) hidden[i] = 1; else hidden[i] = 0;
//        }
//
//        outer = 0;
//
//        for (int i = 0; i < hidden.length; i++){
//            outer += hidden[i]*wHiddenOuter[i];
//        }
//        if (outer > 0.5) outer = 1; else outer = 0;
//    }
    public void countOuter(){
        for (int i = 0; i < hidden.length; i++){
//            hidden[i] = 0;
            for (int j = 0; j < input.length; j++){
                hidden[i] += input[j] * wInputHidden[j][i];
//                hidden[i] = (1/(1+Math.exp(-hidden[i])));
            }
//            double f = (1/1+Math.exp(-hidden[i]));
            hidden[i] = (1/(1+Math.exp(-hidden[i])));
        }

        outer = 0;

        for (int i = 0; i < hidden.length; i++){
            outer += hidden[i]*wHiddenOuter[i];
//            outer = (1/(1+Math.exp(-outer)));
//            outer +=
        }
//        double c = (1/(1+Math.exp(-outer)));
        outer = (1/(1+Math.exp(-outer)));
//        if (outer > 0.51) outer = 1; else outer = 0;
    }

    public void study(){
        double[] err = new double[hidden.length];
        double gError = 0;
        int count = 0;
        double errOH = 0;
        double teta = 0;
        do{
            gError = 0;
            for(int p = 0; p < patterns.length; p++){
                for(int i = 0; i < input.length; i++){
                    input[i] = patterns[p][i];
                }

                countOuter();

//                double lError = answer[p] - outer;
//                double lError = outer - answer[p];
                gError += (Math.pow((outer - answer[p]), 2))/training.length;

//                for (int i = 0; i < hidden.length; i++){
//                    err[i] = lError * wHiddenOuter[i];
//                }

                errOH += (answer[p] - outer)*outer*(1-outer);

//
//                for (int i = 0; i < hidden.length; i++){
//                    wHiddenOuter[i] += 0.1 * lError * hidden[i]; //корр весов на выходном 39
//                }
//
//                for(int i = 0; i < input.length; i++){
//                    for (int j = 0; j < hidden.length; j++){
//                        wInputHidden[i][j] +=0.1 * err[j] * input[i]; //обуч коэф * знач ошибки скрытом слое * н  а значение входного нейрона
//                    }
//                }

            }

            for (int i = 0; i < hidden.length; i++){
                teta = hidden[i]*(1-hidden[i])*errOH;
            }

            for(int p = 0; p < patterns.length; p++){
                for (int i = 0; i < hidden.length; i++){
                    wHiddenOuter[i] += 0.1 * errOH * hidden[i]; //корр весов на выходном 39
                }

                for(int i = 0; i < input.length; i++){
                    for (int j = 0; j < hidden.length; j++){
                        wInputHidden[i][j] += 0.1 * teta * input[i]; //обуч коэф * знач ошибки скрытом слое * н  а значение входного нейрона
                    }
                }
            }
            count++;
//            System.out.println(count);
        }
        while (count !=500);
        System.out.println("gError = " + gError);
        System.out.println("Epochs = " + count);
    }

    public static void main(String[] args) throws IOException {
        DataSet dataSet = new DataSet();
//        dataSet.readCSV();
//        dataSet.createTokenWord();
//        dataSet.creteBeforeTokenizedData();
//        dataSet.creteTokenizedData();

        MLP mlp = new MLP();
    }
}
