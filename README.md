# NN_HandwrittenDigitRecognition
A Simple Neural Network

This is an implemention of a nerual network for recognizing handwritten digits.

### Example - Testing the Neural Network on and-logic.
```Java
NeuralNetwork nn = new NeuralNetwork(new int[]{2,10,10,1},NeuralNetwork.TANH);

//Training
for(int i = 0; i < 5000; i++){
    nn.train(new float[]{0,0},new float[]{0});
    nn.train(new float[]{1,0},new float[]{0});
    nn.train(new float[]{0,1},new float[]{0});
    nn.train(new float[]{1,1},new float[]{1});
}

//Printing out result
System.out.println(nn.feed(new float[]{0,0})[0]);
System.out.println(nn.feed(new float[]{0,1})[0]);
System.out.println(nn.feed(new float[]{1,0})[0]);
System.out.println(nn.feed(new float[]{1,1})[0]);

```

### Example - HandwrittenDigitRecognition
```Java
NeuralNetwork nn = new NeuralNetwork(new int[]{28*28,35,35,35,10},NeuralNetwork.TANH);


String filename = "myHandwrittenDigit.png";
float[] image = ImageController.readImagePixels1D(filename); //Converts pixels from image to 1D-float array of grayscale values
float[] expected = new float[]{0,1,0,0,0,0,0,0,0,0}; //Right answer is the digit 1
nn.train(image,expected);
```
