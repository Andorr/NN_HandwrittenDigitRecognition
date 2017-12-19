package NeuralNetwork;


import ReadAndWrite.SaveAndLoad;

import java.io.File;

public class Main {

    public static void main(String[] args){
        NeuralNetwork nn = new NeuralNetwork(new int[]{28*28,35,35,10},0);
        //NeuralNetwork nn = (NeuralNetwork)SaveAndLoad.readObject("nn");
        handwrittenDigitsTraining(nn);
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

    private static void handwrittenDigitsTraining(NeuralNetwork nn){
        //Training
        System.out.println("Starting to train");
        int curFolder = 0;
        int curFileIndex = 0;
        int iteration = 0;
        while(iteration != 1000000){
            String filename = getNextFileName(curFolder,curFileIndex,true);
            float[] image = ImageController.readImagePixels1D(filename);
            float[] expected = new float[]{0,0,0,0,0,0,0,0,0,0};
            expected[curFolder] = 1;
            nn.train(image,expected);
            curFolder = (int)((float)Math.random()*9);
            curFileIndex = (int)((float)Math.random()*5400);
            iteration++;
        }
        SaveAndLoad.writeObject(nn,"nn");
    }

    private static void handwrittenDigitsTesting(NeuralNetwork nn){
        //Testing
        System.out.println("Starting to test");
        int iteration = 0;
        int totalIterations = 10000;
        int correctAnswers = 0;
        int curFolder;
        int curFileIndex;
        while(iteration != totalIterations){
            curFolder = (int)((float)Math.random()*9);
            curFileIndex = (int)((float)Math.random()*800);
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
        System.out.println("Accuracy: " + accuracy);
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
