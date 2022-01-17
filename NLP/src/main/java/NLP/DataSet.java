package NLP;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataSet {

    int num_words = 10000; //максимальное количество слов;
    static int max_news_len = 150; //максимальная длина новости;
    int nb_classes = 2; //количество классов новостей;
    int count;

    private String csvFile = "D:\\Univer_Zaochka\\NIR\\3_semestr\\NLP\\dataset.csv";
    private String line = "";

    private static String [] correctAnswerString;
    private static String [] words;
    private String wordsInOne;

    private String [] allWords;
    private Map<String, Integer> tokenWord = new HashMap<String, Integer>();

    private String[][] beforeTokenizedData;
    private static Integer[][] tokenizedData;
    private static Integer[] tokenizedAnswerData;

    public DataSet() throws IOException {
        readCSV();
        createTokenWord();
        creteBeforeTokenizedData();
        creteTokenizedData();
    }


    public void readCSV() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String [] arraysCorrectAnswerString = new String[0]; //correctAnswerString
        String [] arraysWords = new String[0]; //words
        int i = 0;
        while ((line = br.readLine()) != null){
            String[] cols = line.split(";");
            if(cols[3].equals("Игры") || cols[3].equals("Искусство")){
                arraysCorrectAnswerString = Arrays.copyOf(arraysCorrectAnswerString, arraysCorrectAnswerString.length+1);
                arraysWords = Arrays.copyOf(arraysWords, arraysWords.length+1);
                arraysCorrectAnswerString[i] = cols[3];
                arraysWords[i] = cols[2];
                i++;
            }
        }

        correctAnswerString = arraysCorrectAnswerString;
        words = arraysWords;
//        System.out.println(Arrays.toString(correctAnswerString));
//        System.out.println(i);

//        while ((line = br.readLine()) != null){
//            String[] cols = line.split(";");
//            if(cols[3].equals("Игры") || cols[3].equals("Искусство")){
//                words = Arrays.copyOf(words, words.length+1);
//                words[0] += cols[2];
//            }
//        }
//        System.out.println(Arrays.toString(words));

//        System.out.println(words[5]);

//        System.out.println(words.length);

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < words.length; j++){
            stringBuilder.append(words[j]);
        }
        wordsInOne = stringBuilder.toString();

        String taboo = "1234567890!@#$%^&*()_-+!№;%:?*.«»—/\\\"~";
        for (char c : taboo.toCharArray()){
            wordsInOne = wordsInOne.replace(c, ' ');
        }

//        System.out.println(wordsInOne);

    }

    public void saveWordsInFile(){
        try(FileWriter writer = new FileWriter("wordsInOne.txt", false)){
            writer.write(wordsInOne);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTokenWord (){
        allWords = wordsInOne.split(" ");
        int key = 1;
        for (int i = 0; i < allWords.length; i++){
            if (allWords[i].length() > 3){
                tokenWord.put(allWords[i], key);
                key++;
            }
        }
//        for (Map.Entry<Integer, String> item : tokenWord.entrySet()){
//            System.out.printf("Key: %s Value: %s \n", item.getKey(), item.getValue());
//        }
//        System.out.println(tokenWord.keySet());
//        System.out.println(allWords.length);
    }

    public void creteBeforeTokenizedData(){
        beforeTokenizedData = new String[words.length][max_news_len];
//        System.out.println(words.length);
        for(int i = 0; i < words.length; i++){
            String string = words[i];
            String [] stringsArrays = string.split("\\s*(\\s|,|!|\"|:|-|\\.)\\s*");
            for (int j = 0; j < max_news_len; j++){
                stringsArrays = Arrays.copyOf(stringsArrays, max_news_len);
                if (stringsArrays[j] == null)
                    stringsArrays[j] = "0";
                beforeTokenizedData[i][j] = stringsArrays[j];
            }
        }
//        for (int i = 0; i < 5; i++){
//            for (int j = 0; j < 15; j++){
//                System.out.print(beforeTokenizedData[i][j] + " ");
//            }
//            System.out.println(" ");
//        }
    }

    public void creteTokenizedData(){
        tokenizedData = new Integer[words.length][max_news_len];
        for (int i = 0; i < words.length; i++){
            for (int j = 0; j < max_news_len; j++){
                tokenizedData[i][j] = tokenWord.get(beforeTokenizedData[i][j]);
                if(tokenWord.get(beforeTokenizedData[i][j]) == null)
                    tokenizedData[i][j] = 0;
            }
        }
//        for (int i = 0; i < 5; i++){
//            for (int j = 0; j < 15; j++){
//                System.out.print(tokenizedData[i][j] + " ");
//            }
//            System.out.println(" ");
//        }
    }

    public static Integer[] getTokenizedAnswerData(){
        tokenizedAnswerData = new Integer[words.length];
        for (int i = 0; i < words.length; i++){
            if (correctAnswerString[i].equals("Игры")){
                tokenizedAnswerData[i] = 0;
            }
            if (correctAnswerString[i].equals("Искусство")){
                tokenizedAnswerData[i] = 1;
            }
        }
//        for (int i = 0; i < 5; i++){
//            System.out.println(tokenizedAnswerData[i]);
//        }
//        for (int i = 0; i < 5; i++){
//            System.out.println(correctAnswerString[i]);
//        }
        return tokenizedAnswerData;
    }

    public static Integer[][] getTokenizedDataStudy(){
        Integer[][] tokenizedDataStudy = new Integer[2726][max_news_len];
        for (int i = 0; i < 2726; i++){
            for (int j = 0; j < max_news_len; j++){
                tokenizedDataStudy[i][j] = tokenizedData[i][j];
            }
        }
        return tokenizedDataStudy;
    }

    public static Integer[][] getTokenizedDataTraining(){
        Integer[][] tokenizedDataTraining = new Integer[1169][max_news_len];
        for (int i = 0; i < 1169; i++){
            for (int j = 0; j < max_news_len; j++){
                tokenizedDataTraining[i][j] = tokenizedData[i+2726][j];
            }
        }
        return tokenizedDataTraining;
    }
}
