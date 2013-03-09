package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import models.RandomImageListRespons;

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

	// TESTED
	public static RandomImageListRespons getRandomImageList(String[] imageList, int nrImages)
	{
		List<String> images = new LinkedList<String>(Arrays.asList(imageList));
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

		images.removeAll(randomImages);
		
		RandomImageListRespons respons = new RandomImageListRespons(randomImages.toArray(new String[randomImages.size()]), 
																	images.toArray(new String[images.size()]));
		
		return respons;
	}

	// TESTED
	public static Matrix[] getImageMatrixList(String[] imageList, DataSet dataSet)
	{
		Matrix[] imageMatrixList = new Matrix[imageList.length];
		for(int i = 0; i < imageList.length; i++)
		{
			imageMatrixList[i] = getImageMatrix(imageList[i], dataSet);
		}

		return imageMatrixList;
	}

	// TESTED
	public static Matrix getImageMatrix(String image, DataSet dataSet)
	{
		BufferedImage buffImage = getBufferedImage(Strings.dataURL[dataSet.ordinal()] + image);
		getGrayImage(buffImage);

		Matrix matrixImage = new Matrix(buffImage.getWidth(), buffImage.getHeight());

		for (int col = 1; col <= matrixImage.getNrCols(); col++)
		{
			for (int row = 1; row <= matrixImage.getNrRows(); row++)
			{
				int val = (new Color(buffImage.getRGB(col - 1, row - 1))).getRed();
				matrixImage.setValue(col, row, val);
			}
		}

		return matrixImage;
	}

	// TESTED
	public static BufferedImage getBufferedImage(String image) 
	{	
		File file = new File(image);
		BufferedImage inData = null;

		try { inData = ImageIO.read( file ); }
		catch (IOException e) 
		{ 
			String errorMessage = "Error at Data.getBufferedImage" + Strings.newline +
					"File: " + image + Strings.newline +
					e.getMessage() + Strings.newline +
					e.getStackTrace();

			throw new RuntimeException(errorMessage);
		}

		BufferedImage newImage = new BufferedImage(inData.getWidth(), inData.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(inData, 0, 0, null);
		g.dispose();
		
		return newImage;
	}

	// TESTED
	public static BufferedImage getGrayImage(BufferedImage im) 
	{
		BufferedImage result = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		op.filter(im, result);
		return result;
	}

	// TESTED
	public static Matrix[] getNormalisedImageMatrixList(String[] imageList, DataSet dataSet)
	{
		return getNormalisedImageMatrixList(getImageMatrixList(imageList, dataSet));
	}

	// TESTED
	public static Matrix[] getNormalisedImageMatrixList(Matrix[] matrixList)
	{
		Matrix[] result = new Matrix[matrixList.length];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = matrixList[i].getNormal();
		}

		return result;
	}

}
