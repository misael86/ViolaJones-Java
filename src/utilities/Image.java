package utilities;

import global.Strings;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image 
{

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
	
	public static Matrix[] getIntegralImages(Matrix[] matrixes)
    {
		Matrix[] integralImages = new Matrix[matrixes.length];
		
		for (int i = 0; i < integralImages.length; i++)
		{
			integralImages[i] = getIntegralImage(matrixes[i]);
		}
		
		return integralImages;
    }
	
	public static void saveImage(BufferedImage bi, String filename) {
		
		filename = "test/debug/"+filename+".png";
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
	
}
