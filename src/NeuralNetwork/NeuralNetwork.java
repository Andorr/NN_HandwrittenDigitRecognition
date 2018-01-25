package NeuralNetwork;

public class NeuralNetwork implements java.io.Serializable{

    private static final float LEARNING_RATE = 0.033333f;
    private final int FUNCTION;
    protected static final int TANH = 0;
    protected static final int SIGMOID = 1;
    protected static final int RELU = 2;

    private float[][] inputs; //Every layer's inputs
    private float[][] outputs; //Every layer's output
    private float[][][] weights; //All the weights
    private int[] numOfOutputs; //Every layer's number of outputs
    private int[] numOfInputs; //Every layer's number of inputs

    public NeuralNetwork(int[] layers, int function){
        checkValidLayerInput(layers); //Check if input is a valid layer-input
        FUNCTION = function;

        //Initializes the layers in the neural network
        inputs = new float[layers.length-1][];
        outputs = new float[layers.length-1][];
        weights = new float[layers.length-1][][];
        numOfInputs = new int[layers.length-1];
        numOfOutputs = new int[layers.length-1];
        for(int i = 0; i < layers.length-1; i++){
            inputs[i] = new float[layers[i]];
            outputs[i] = new float[layers[i+1]];
            weights[i] = new float[layers[i+1]][layers[i]];
            numOfOutputs[i] = layers[i+1];
            numOfInputs[i] = layers[i];
        }

        initializeWeights(); //Randomizes the weights
    }

    public void train(float[] inputs,float[] expected){
        feed(inputs);
        backPropagation(expected);
    }

    public float[] feed(float[] input){
        checkFeedInput(input); //Checks if the feed-input is valid and correct (in size)
        //Feeds the input to the first layer
        feedToLayer(input,0);

        //Feeds the rest of the layers
        for(int i = 1; i < outputs.length; i++){
            feedToLayer(outputs[i-1],i); //Feeds the outputs of the previous layer
        }
        return outputs[outputs.length-1]; //Returns the last layer's output
    }

    private void feedToLayer(float[] input,int layerIndex){
        inputs[layerIndex] = input;
        for(int j = 0; j < numOfOutputs[layerIndex]; j++){
            outputs[layerIndex][j] = 0; //Resets the value of the neuron
            for(int k = 0; k < numOfInputs[layerIndex]; k++){
                outputs[layerIndex][j] += inputs[layerIndex][k]*weights[layerIndex][j][k]; //Adds all the inputs*weights to the neuron
            }
            outputs[layerIndex][j] = activationFunction(outputs[layerIndex][j]); //Maps the value with the selected mapping-function
        }
    }

    private void backPropagation(float[] expected){
        checkExpectedInput(expected); //Checks if the expected input is correct and valid (in size)

        float[][] error = create2DArray(); //The errors of the last output and the expected
        float[][] gamma = create2DArray(); //Neuron's calculated derivatives
        float[][][] weightsDelta = create3DArray(); //The calculated change of the weights


        for(int i = outputs.length- 1; i >= 0; i--){ //Loops through the layers backwards
            //If is the last layer
            if(i == outputs.length -1){
                //Calculate all the error derives and the gamma
                for(int j = 0; j < numOfOutputs[i]; j++){
                    error[i][j] = outputs[i][j] - expected[j];
                    gamma[i][j] = error[i][j]*derivative(outputs[i][j]);
                }
            }
            //If is not the last layer
            else{
                //Calculates the gamma
                for(int j = 0; j < numOfOutputs[i]; j++){
                    gamma[i][j] = 0;
                    for(int k = 0; k < gamma[i+1].length; k++){
                        gamma[i][j] += gamma[i+1][k]*weights[i+1][k][j]; //Adds the gammas from the next layer and all of it weights
                    }
                    gamma[i][j] *= derivative(outputs[i][j]);
                }
            }

            //Calculates the deltaWeights
            for(int j = 0; j < numOfOutputs[i]; j++){
                for(int k = 0; k < numOfInputs[i]; k++){
                    weightsDelta[i][j][k] = gamma[i][j]*inputs[i][k];
                }
            }
        }

        //Updates the weights
        for(int i = 0; i < outputs.length; i++){
            for(int j = 0; j < numOfOutputs[i]; j++){
                for(int k = 0; k < numOfInputs[i]; k++){
                    weights[i][j][k] -= weightsDelta[i][j][k]*LEARNING_RATE; //Subtracts the weightDeltas times the learningRate
                }
            }
        }
    }

    //-----Helper functions-----
    private void initializeWeights(){
        for(int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = (float)Math.random()-0.5f;
                }
            }
        }
    }

    //Maps values with either tanH or sigmoid
    private float activationFunction(float value){
        switch (FUNCTION){
            case NeuralNetwork.TANH: //TanH (hyperbolic tangent)
                return (float)Math.tanh(value);
            case NeuralNetwork.SIGMOID: //Sigmoid
                return sigmoid(value);
            default: //RELU
                return Math.max(0,value);
        }
    }

    //Derives values of the selected mapping-function
    private float derivative(float value){
        switch (FUNCTION){
            case NeuralNetwork.TANH: //Derivative of TanH
                return 1f-(value*value);
            case NeuralNetwork.SIGMOID: //Derivative of the Sigmoid-function
                return (float)(1-Math.pow(sigmoid(value),2));
            default: //Derivative of the RELU-function
                return (float)((value > 0)? 1 : 0);
        }
    }

    private float sigmoid(float value){
        return (float)(2/(1+Math.exp(-2*value))-1);
    }

    private float[][] create2DArray(){
        float[][] array = new float[outputs.length][];
        for(int i = 0; i < array.length; i++){
            array[i] = new float[numOfOutputs[i]];
        }
        return array;
    }

    private float[][][] create3DArray(){
        float[][][] array = new float[outputs.length][][];
        for(int i = 0; i < array.length; i++){
            array[i] = new float[numOfOutputs[i]][numOfInputs[i]];
        }
        return array;
    }

    public String toString(){
        String output = "";
        for(int layer = 0; layer < outputs.length; layer++){
            output += "\nLayer:";
            for(int neuron = 0; neuron < numOfOutputs[layer]; neuron++){
                output += " [" + outputs[layer][neuron] + "] ";
            }
        }
        return output;
    }

    //Checks if the input layer is correct
    private void checkValidLayerInput(int[] layers){
        //Checks the input length (must contain a input layer and a output layer)
        if(layers.length < 2){
            throw new IllegalArgumentException("Array too short");
        }
        //Checks for layer-sizes less than 1
        for(int i = 0; i < layers.length; i++){
            if(layers[i] < 1){
                throw  new IllegalArgumentException("Array contains values less than 1");
            }
        }
    }

    private void checkFeedInput(float[] input){
        //Checks if the length of the input is equal to the networks input-size
        if(input.length != inputs[0].length){
            throw new IllegalArgumentException("Input length is incorrect! Correct length is: " + inputs[0].length + ", but current length is " + input.length + ".");
        }
    }

    private void checkExpectedInput(float[] expected){
        //Checks if the length of the input is equals to the last layer's output size
        if(expected.length != outputs[outputs.length-1].length){
            throw new IllegalArgumentException("Expected input length is incorrect! Correct length is: " + outputs[outputs.length-1].length + ", but current length is " + expected.length + ".");
        }
    }
}
