import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class FeedForward
{
    /**
     * Application to recognize the number drawn in an image
     * 
     */
	public static void main (String args[]) throws IOException
    {
        final String inputNumber = "numbers/9.png";
        final String hiddenWeightsFile = "hidden-weights.txt";
        final String outputWeightsFile = "output-weights.txt";
        
        double[][] hiddenWeights = fileReader(hiddenWeightsFile);
        double[][] outputWeights = fileReader(outputWeightsFile);
        BufferedImage img = ImageIO.read(new File(inputNumber));
        double[] dummy = null;
        double[] X = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), dummy);
        int[] inputImage = new int[X.length];
        for(int i = 0; i < X.length; i++)
        {
            if(X[i] <= 150.0)
            {
                inputImage[i] = 0;
            }
            else
            {
                inputImage[i] = 1;
            }
        }
        double[] hiddenLayer = nextLayer(X, hiddenWeights);
        double[] outputLayer = nextLayer(hiddenLayer, outputWeights);
        System.out.println(Arrays.toString(outputLayer));
        int result = indexOfLargest(outputLayer);
        System.out.println(result);
    }
    
    /**
     * Reads the provided file row by row and
     * output it into an ArrayList
     * 
     * @param inputFile  the file to read from
     * @return the ArrayList filled with rows
     */
    public static double[][] fileReader (String inputFile) throws IOException
    {
        ArrayList<String[]> fileText = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line;
        while((line = br.readLine()) != null)
        {
            String[] lineArray = line.split(" ");
            fileText.add(lineArray);
        }
        String[][] fileTextArray = new String[fileText.size()][];
        for(int i = 0; i < fileTextArray.length; i ++)
        {
            String[] row = fileText.get(i);
            fileTextArray[i] = row;
        }
        double[][] fileNumArray = new double[fileTextArray.length][fileTextArray[0].length];
        for(int i = 0; i < fileTextArray.length; i++)
        {
            for(int j = 0; j < fileTextArray[0].length; j++)
            {
                fileNumArray[i][j] = Double.parseDouble(fileTextArray[i][j]);
            }
        }
        
        return fileNumArray;
    }
    
    /**
     * Finds the index of the largest
     * value in an array using the linear search
     * algorithm
     * 
     * @param array the array to search
     * @return the index of the largest attribute
     */
    public static int indexOfLargest(double[] array)
    {
        int greatest = 0;
        for(int i = 1; i < array.length; i++)
        {
            if(array[greatest] < array[i])
            {
                greatest = i;
            }
        }
        return greatest;
    }
    
    /**
     * Converts from the current layer
     * to the next layer based on the weights and
     * current layer
     * 
     * @param currentLayer the current layer
     * @param weights the weights for the next values
     * @return the next layer
     */
    public static double[] nextLayer(double[] currentLayer, double[][] weights)
    {
        double[] outputLayer = new double[weights.length];
        for(int i = 0; i < weights.length; i++)
        {
            for(int j = 0; j < weights[0].length-1; j++)
            {
                //outputLayer[i] += currentLayer[j]*weights[i][j] + weights[i][weights.length-1];
                outputLayer[i] += currentLayer[j]*weights[i][j];
            }
            outputLayer[i] += weights[i][weights.length-1];
            outputLayer[i] = function(outputLayer[i]);
        }
        return outputLayer;
    }
    
    /**
     * Computes the result of the given function
     * 
     * @param input the number to put through the function
     * @return the result of the function
     */
    public static double function(double input)
    {
        double output = 1.0/(1 + Math.pow(Math.E, -1*input));
        return output;
    }
}

