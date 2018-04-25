package NeuralNetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class SaveAndLoad {

    public static boolean writeObject (Object o, String fileName){
        //Write object to file
        try{
            FileOutputStream fos = new FileOutputStream(new File(fileName + ".dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            fos.close();
            return true;
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static Object readObject(String fileName){
        //Read object from file
        try{
            FileInputStream fis = new FileInputStream(new File(fileName + ".dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            fis.close();
            ois.close();
            return o;
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveImage(BufferedImage image,String fileName){
        try{
            File file = new File(fileName + ".png");
            ImageIO.write(image,"png",file);
            return true;
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
