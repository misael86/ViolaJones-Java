package utilities;

import global.Strings;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image 
{

	// TESTED
	public static Matrix getIntegralImage(Matrix matrix)
    {
        Matrix integralImage = new Matrix(matrix.getNrCols(), matrix.getNrRows());

        for (int y, x = 1; x <= matrix.getNrCols(); x++)
        {
            for (y = 1; y <= matrix.getNrRows(); y++)
            {
            	double value = matrix.getValue(x, y);
            	value += x > 1 ? integralImage.getValue(x - 1, y) : 0;
            	value += y > 1 ? integralImage.getValue(x, y - 1) : 0;
            	value -= x > 1 && y > 1 ? integralImage.getValue(x - 1, y - 1) : 0;
            	integralImage.setValue(x, y, value);
            }
        }

        return integralImage;
    }
	
	// TESTED
	public static Matrix[] getIntegralImages(Matrix[] matrixes)
    {
		Matrix[] integralImages = new Matrix[matrixes.length];
		
		for (int i = 0; i < integralImages.length; i++)
		{
			integralImages[i] = getIntegralImage(matrixes[i]);
		}
		
		return integralImages;
    }
	
	public static void saveImage(Matrix image, String filename) 
	{
		BufferedImage bi = new BufferedImage(image.getNrCols(), image.getNrRows(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < image.getNrCols(); x++)
		{
			for(int y = 0; y < image.getNrRows(); y++)
			{
				int colorValue = (int)image.getValue(x + 1, y + 1);
				Color color = new Color(colorValue, colorValue, colorValue);
				bi.setRGB(x, y, color.getRGB());
			}
		}
		
		saveImage(bi, filename);
	}
	
	// TESTED
	public static void saveImage(BufferedImage bi, String filename) 
	{
		filename = "images/test/debug/" + filename + ".png";
		try 
		{ 
			ImageIO.write(bi, "png", new File(filename)); 
		} 
		catch (IOException e) 
		{
			String errorMessage = "Error at Image.saveImage" + Strings.newline +
					e.getMessage() + Strings.newline +
					e.getStackTrace();
			
			throw new RuntimeException(errorMessage); 
		}
	}
	
	public static Matrix getImageMatrix(BufferedImage image) 
	{
	    image = Data.getGrayImage(image);
		
	    Matrix result = new Matrix(image.getWidth(), image.getHeight());
	    for(int y = 0; y < image.getHeight(); y++)
	    {
	    	for(int x = 0; x < image.getWidth(); x++)
	    	{
	    		double val = new Color(image.getRGB(x, y)).getBlue();
	    		result.setValue(x + 1, y + 1, val);
	    	}
	    }
	
		//res_matrix.rescale(0, 255);
		
		return result.getNormal();
	}
	
}
