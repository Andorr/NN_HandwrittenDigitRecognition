package NeuralNetwork;

import java.io.File;
import java.util.Random;

public class Main {

    public static void main(String[] args){
        //NeuralNetwork nn = new NeuralNetwork(new int[]{28*28,35,35,10},0);
        NeuralNetwork nn = (NeuralNetwork)SaveAndLoad.readObject("nn");
        //handwrittenDigitsTraining(nn,false);
        handwrittenDigitsTesting(nn);
    }

    private static void xorTest(NeuralNetwork nn){
        for(int i = 0; i < 5000; i++){
            nn.train(new float[]{0,0,0},new float[]{0});
            nn.train(new float[]{0,0,1},new float[]{1});
            nn.train(new float[]{0,1,0},new float[]{1});
            nn.train(new float[]{1,0,0},new float[]{1});
            nn.train(new float[]{0,1,1},new float[]{0});
            nn.train(new float[]{1,0,1},new float[]{0});
            nn.train(new float[]{1,1,0},new float[]{0});
            nn.train(new float[]{1,1,1},new float[]{1});
        }

        System.out.println(nn.feed(new float[]{0,0,0})[0]);
        System.out.println(nn.feed(new float[]{0,1,0})[0]);
        System.out.println(nn.feed(new float[]{1,1,1})[0]);
    }

    private static void andTest(NeuralNetwork nn){
        for(int i = 0; i < 5000; i++){
            nn.train(new float[]{0,0},new float[]{0});
            nn.train(new float[]{1,0},new float[]{0});
            nn.train(new float[]{0,1},new float[]{0});
            nn.train(new float[]{1,1},new float[]{1});
        }

        System.out.println(nn.feed(new float[]{0,0})[0]);
        System.out.println(nn.feed(new float[]{0,1})[0]);
        System.out.println(nn.feed(new float[]{1,0})[0]);
        System.out.println(nn.feed(new float[]{1,1})[0]);
    }

    private static void handwrittenDigitsTraining(NeuralNetwork nn,boolean saveToFile){
        //Training
        System.out.println("Starting to train");
        int curFolder = 0;
        int curFileIndex = 0;
        int iteration = 0;
        final int totalIterations = 1000000;
        final Random random = new Random();
        while(iteration != totalIterations){
            String filename = getNextFileName(curFolder,curFileIndex,true);
            float[] image = ImageController.readImagePixels1D(filename);
            float[] expected = new float[]{0,0,0,0,0,0,0,0,0,0};
            expected[curFolder] = 1;
            nn.train(image,expected);
            curFolder = random.nextInt(10);
            curFileIndex = random.nextInt(5400);
            iteration++;
        }

        if(saveToFile){
            SaveAndLoad.writeObject(nn,"nn");
        }

    }

    private static void handwrittenDigitsTesting(NeuralNetwork nn){
        //Testing
        System.out.println("Starting to test");
        int iteration = 0;
        final int totalIterations = 20000;
        int correctAnswers = 0;
        int curFolder;
        int curFileIndex;
        final Random random = new Random();
        while(iteration != totalIterations){
            curFolder = random.nextInt(10);
            curFileIndex = random.nextInt(800);
            String filename = getNextFileName(curFolder,curFileIndex,false);
            float[] image = ImageController.readImagePixels1D(filename);
            float[] result = nn.feed(image);
            int answer = getIndexOfMaxVal(result);
            if(answer == curFolder){
                correctAnswers++;
            }
            iteration++;
        }
        float accuracy = (float)correctAnswers/(float)totalIterations;
        System.out.println("Accuracy: " + accuracy + "\nCorrect Answers: " + correctAnswers + "\\" + totalIterations);
    }

    //----Helper Functions----
    private static void printArray(float[] a){
        String output = "|";
        for(int i = 0; i < a.length; i++){
            output += i + ": " + a[i] + " |";
        }
        System.out.println(output);
    }

    private static String getNextFileName(int curFolder, int curFileIndex,boolean isTraining){
        String subFolder = ((isTraining)? "training\\" : "testing\\");
        return "dataset\\" + subFolder + curFolder + "\\" + (new File("dataset\\" + subFolder + curFolder).listFiles()[curFileIndex]).getName();
    }

    private static int getIndexOfMaxVal(float[] input){
        float biggest = -1;
        int index = 0;
        for(int i = 0; i < input.length; i++){
            if(input[i] > biggest){
                index = i;
                biggest = input[i];
            }
        }
        return index;
    }
}
