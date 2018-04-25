package NeuralNetwork;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageController{

    public static float[][] readImagePixels(String fileName){
        try {
            File file = new File(fileName);
            BufferedImage image = ImageIO.read(file);
            float[][] pixels = new float[image.getWidth()][image.getHeight()];
            for(int x = 0; x < image.getWidth(); x++){
                for(int y = 0; y < image.getHeight(); y++){
                    pixels[x][y] = getPixel(image,x,y);
                }
            }
            return pixels;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static float[][] readImagePixels(BufferedImage image){
        float[][] pixels = new float[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                pixels[x][y] = getPixel(image,x,y);
            }
        }
        return pixels;
    }

    public static float[] readImagePixels1D(String fileName){
        return convertTo1D(readImagePixels(fileName));
    }

    public static float[] readImagePixels1D(BufferedImage image){
        return convertTo1D(readImagePixels(image));
    }

    //-----Helper Functions-----
    private static float[] convertTo1D(float[][] a){
        ArrayList<Float> list = new ArrayList<Float>();
        for(int x = 0; x < a.length; x++){
            for(int y = 0; y < a[x].length; y++){
                list.add(a[x][y]);
            }
        }

        float[] vector = new float[list.size()];
        for(int i = 0; i < vector.length; i++){
            vector[i] = list.get(i);
        }
        return vector;
    }

    private static float getPixel(BufferedImage image, int x, int y){
        int color = image.getRGB(x,y);
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = (color & 0x00000ff);
        return map(((red + green + blue)/3f),0 ,255,0,1);
    }

    private static float getPixel(int r,int g,int b){
        int red = (r & 0x00ff0000) >> 16;
        int green = (g & 0x0000ff00) >> 8;
        int blue = (b & 0x00000ff);
        return map(((red + green + blue)/3f),0 ,255,0,1);
    }

    private static float map(float value, float from1, float to1, float from2, float to2){
        return (value - from1)/(to1-from1) *(to2-from2) + from2;
    }

    public static BufferedImage decodeImage(String imageString){
        BufferedImage image = null;
        byte[] imageByte;

        try{
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            return image;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
