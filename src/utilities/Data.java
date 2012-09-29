package utilities;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import global.*;
import global.Enumerators.DataSet;

public abstract class Data {

	public static String[] getImageList(DataSet dataSet)
	{
		File folder = new File(Strings.dataURL[dataSet.ordinal()]);
	    File[] files = folder.listFiles();
	    ArrayList<String> names = new ArrayList<String>();
	    
	    for (File f : files) 
	    {
	    	if (f.isFile() && 
	    		(f.getName().endsWith(".jpg") ||
	    		 f.getName().endsWith(".jpeg") ||
	    		 f.getName().endsWith(".gif") ||
	    		 f.getName().endsWith(".bmp") ||
	    		 f.getName().endsWith(".tif") ||
	    		 f.getName().endsWith(".png") ))
	    	{
	    		names.add(f.getName());
	    	}
	    }
	    
	    return names.toArray(new String[names.size()]);
	}

	public static String[] getRandomImageList(String[] imageList, int nrImages)
    {
		List<String> images = Arrays.asList(imageList);
		List<String> randomImages = new ArrayList<String>();
		
        Random random = new Random();
        
        for (int i = 1; i <= nrImages;)
        {
        	int tempInt = random.nextInt(images.size() - 1);
            String nextImage = images.get(tempInt);
            if (!randomImages.contains(nextImage))
            {
            	randomImages.add(nextImage);
            	i++;
            }
        }

        return randomImages.toArray(new String[randomImages.size()]);
    }
	
	public static Matrix[] getImageMatrixList(String[] imageList, DataSet dataSet)
    {
        List<Matrix> imageMatrixList = new ArrayList<Matrix>();
        for(String image : imageList)
        {
            imageMatrixList.add( getImageMatrix(image, dataSet) );
        }
        
        return imageMatrixList.toArray(new Matrix[imageMatrixList.size()]);
    }
	
	public static Matrix getImageMatrix(String image, DataSet dataSet)
	{
		BufferedImage buffImage = getBufferedImage(Strings.dataURL[dataSet.ordinal()] + image);
		getGrayImage(buffImage);
		
		Matrix matrixImage = new Matrix(buffImage.getWidth(), buffImage.getHeight());

        for (int col = 1; col <= matrixImage.getNrCols(); col++)
        {
            for (int row = 1; row <= matrixImage.getNrRows(); row++)
            {
                matrixImage.setValue(col, row, buffImage.getRGB(col - 1, row - 1));
            }
        }

        return matrixImage;
	}
	
	public static BufferedImage getBufferedImage(String image) 
	{	
		File file = new File(image);
		BufferedImage buffImage = null;
		
		try { buffImage = ImageIO.read( file ); }
		catch (IOException e) 
		{ 
			String errorMessage = "Error at Data.getBufferedImage" + Strings.newline +
					"File: " + image + Strings.newline +
					e.getMessage() + Strings.newline +
					e.getStackTrace();
			
			throw new RuntimeException(errorMessage);
		}

		BufferedImage newImage = new BufferedImage(buffImage.getWidth(), buffImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = newImage.createGraphics();
		graphics2D.drawImage(buffImage, 0, 0, null);
		graphics2D.dispose();
		
		return newImage;
	}
	
	public static void getGrayImage(BufferedImage im) 
	{
		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		op.filter(im, im);
	}
		
	public static Matrix[] getNormalisedImageMatrixList(String[] imageList, DataSet dataSet)
    {
        return getNormalisedImageMatrixList(getImageMatrixList(imageList, dataSet));
    }

    public static Matrix[] getNormalisedImageMatrixList(Matrix[] matrixList)
    {
        List<Matrix> result = new ArrayList<Matrix>();

        for (Matrix matrix : matrixList)
        {
            try
            {
                result.add(matrix.getNormal());
            }
            catch (Exception e){ }
        }

        return result.toArray(new Matrix[result.size()]);
    }
}
