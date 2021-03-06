package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import global.Strings;
import global.Enumerators.FeatureType;

public class Feature 
{

	public FeatureType type;
    public int x, y, w, h;

    public Feature(FeatureType type, int x, int y, int w, int h)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    // TESTED
    public static double getBoxSum(Matrix integralImage, int colNr, int rowNr, int width, int height)
    {
        if (height < 1 || width < 1 || 
        	integralImage.getNrCols() < colNr + width - 1 ||
        	integralImage.getNrRows() < rowNr + height - 1)
        {
        	String errorMessage = "Error at Feature.getBoxSum" + Strings.newline +
					"Nr colums: " + integralImage.getNrCols() + Strings.newline +
					"Nr rows: " + integralImage.getNrRows() + Strings.newline +
					"Column: " + colNr + Strings.newline +
					"Row: " + rowNr + Strings.newline +
					"Width: " + width + Strings.newline +
					"Height: " + height + Strings.newline; 
			
			throw new RuntimeException(errorMessage);
        }

        double sum = integralImage.getValue(colNr + width - 1, rowNr + height - 1);
        sum -= rowNr > 1 ? integralImage.getValue(colNr + width - 1, rowNr - 1) : 0;
        sum -= colNr > 1 ? integralImage.getValue(colNr - 1, rowNr + height - 1) : 0;
        sum += colNr > 1 && rowNr > 1 ? integralImage.getValue(colNr - 1, rowNr - 1) : 0;

        return sum;
    }
	
    // TESTED
    public static Feature[] getFeatures(int featureWidth, int featureHeight, FeatureType type)
    {
        int x, y, w, h;

        List<Feature> all_features = new ArrayList<Feature>();

        switch (type)
        {
            case type1:
                for (w = 1; w <= featureWidth - 2; w++)
                    for (h = 1; h <= Math.floor(featureHeight / 2.0) - 2; h++)
                        for (x = 1; x < featureWidth - w; x++)
                            for (y = 1; y < featureHeight - 2 * h; y++)
                                all_features.add(new Feature(FeatureType.type1, x, y, w, h ));
                break;

            case type2:
                for (w = 1; w <= Math.floor(featureWidth / 2.0) - 2; w++)
                    for (h = 1; h <= featureHeight - 2; h++)
                        for (x = 1; x <= featureWidth - 2 * w - 1; x++)
                            for (y = 1; y <= featureHeight - h - 1; y++)
                                all_features.add(new Feature(FeatureType.type2, x, y, w, h));
                break;

            case type3:
                for (w = 1; w <= Math.floor(featureWidth / 3.0) - 2; w++)
                    for (h = 1; h <= featureHeight - 2; h++)
                        for (x = 1; x <= featureWidth - 3 * w - 1; x++)
                            for (y = 1; y <= featureHeight - h - 1; y++)
                                all_features.add(new Feature(FeatureType.type3, x, y, w, h));
                break;

            case type4:
                for (w = 1; w <= Math.floor(featureWidth / 2.0) - 2; w++)
                    for (h = 1; h <= Math.floor(featureHeight / 2.0) - 2; h++)
                        for (x = 1; x <= featureWidth - 2 * w - 1; x++)
                            for (y = 1; y <= featureHeight - 2 * h - 1; y++)
                                all_features.add(new Feature(FeatureType.type4, x, y, w, h));

                break;
        }

        return all_features.toArray(new Feature[all_features.size()]);
    }

    // TESTED
    public static Feature[] getAllFeatures(int featureWidth, int featureHeight)
    {
        List<Feature> allFeatures = new ArrayList<Feature>();

        allFeatures.addAll(Arrays.asList(getFeatures(featureWidth, featureHeight, FeatureType.type1)));
        allFeatures.addAll(Arrays.asList(getFeatures(featureWidth, featureHeight, FeatureType.type2)));
        allFeatures.addAll(Arrays.asList(getFeatures(featureWidth, featureHeight, FeatureType.type3)));
        allFeatures.addAll(Arrays.asList(getFeatures(featureWidth, featureHeight, FeatureType.type4)));

        return allFeatures.toArray(new Feature[allFeatures.size()]);
    }
    
    // TESTED
    public static List<Feature[]> getAllSeparatedFeatures(int featureWidth, int featureHeight)
    {
        List<Feature[]> allFeatures = new ArrayList<Feature[]>();

        allFeatures.add(getFeatures(featureWidth, featureHeight, FeatureType.type1));
        allFeatures.add(getFeatures(featureWidth, featureHeight, FeatureType.type2));
        allFeatures.add(getFeatures(featureWidth, featureHeight, FeatureType.type3));
        allFeatures.add(getFeatures(featureWidth, featureHeight, FeatureType.type4));

        return allFeatures;
    }
    
    // TESTED
    public static Feature getFeature(int index, int featureWidth, int featureHeight)
    {
        Feature[] allFeatures = getAllFeatures(featureWidth, featureHeight);
        if (index > allFeatures.length || index < 1) 
        { 
        	String errorMessage = "Error at Feature.getFeature" + Strings.newline +
					"Nr features: " + allFeatures.length + Strings.newline +
					"Index: " + index;
			
			throw new RuntimeException(errorMessage);
        }
        
        return allFeatures[index - 1];
    }

    // TESTED
    public static Feature getFeature(int index, FeatureType featureType, int featureWidth, int featureHeight)
    {
        Feature[] allFeatures = getFeatures(featureWidth, featureHeight, featureType);
        if (index > allFeatures.length || index < 1) 
        { 
        	String errorMessage = "Error at Feature.getFeature" + Strings.newline +
					"Nr features: " + allFeatures.length + Strings.newline +
					"Index: " + index;
			
			throw new RuntimeException(errorMessage);
        }
        
        return allFeatures[index - 1];
    }
    
    // TESTED
    public static double getFeatureValue(Matrix integralImage, Feature feature)
    {

        double val = 0;

        int x = feature.x;
        int y = feature.y;
        int w = feature.w;
        int h = feature.h;

        switch (feature.type)
        {
            case type1:
                val = getBoxSum(integralImage, x, y, w, h);
                val -= getBoxSum(integralImage, x, y + h, w, h);
                break;

            case type2:
                val = getBoxSum(integralImage, x + w, y, w, h);
                val -= getBoxSum(integralImage, x, y, w, h);
                break;

            case type3:
                val = getBoxSum(integralImage, x + w, y, w, h);
                val -= getBoxSum(integralImage, x, y, w, h);
                val -= getBoxSum(integralImage, x + 2 * w, y, w, h);
                break;

            case type4:
                val = getBoxSum(integralImage, x + w, y, w, h);
                val += getBoxSum(integralImage, x, y + h, w, h);
                val -= getBoxSum(integralImage, x, y, w, h);
                val -= getBoxSum(integralImage, x + w, y + h, w, h);
                break;
        }

        return val;
    }
    
    // TESTED
    public static Matrix getFeatureValues(Matrix[] integralImages, Feature feature)
    {
    	double[] values = new double[integralImages.length];
    	
    	for (int i = 0; i < values.length; i++)
    	{
    		values[i] = getFeatureValue(integralImages[i], feature);
    	}
    	
        return new Matrix(values.length, 1, values);
    }
    
    // TESTED
    public static Matrix getAllFeatureValues(Matrix[] integralImages, Feature[] allFeatures)
    {
        Matrix result = new Matrix(integralImages.length, allFeatures.length);

        for (int i = 1; i <= allFeatures.length; i++)
        { 
            result.setRow(i, getFeatureValues(integralImages, allFeatures[i-1]));
        }

        return result;
    }
    
    // TESTED
    public static void saveFeatureValues(Matrix featureValues, String featureType) 
    {
    	Matrix.saveMatrix(featureValues, "FeatureValues_" + featureType);
    }
    
    // TESTED
    public static Matrix loadFeatureValues(String featureType)
    {
    	return Matrix.loadMatrix("FeatureValues_" + featureType);
    }
    
    // TESTED
    public static void saveFeaturePic(Feature feature, int width, int height, String filename) {

        int x, y, w, h, k = 5;

        x = feature.x * k;
        y = feature.y * k;
        w = feature.w * k;
        h = feature.h * k;
        width = width * k;
        height = height * k;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();

        switch (feature.type) {
            case type1:
                g.setColor(Color.red);
                g.fillRect(x, y, w, h);
                g.setColor(Color.green);
                g.fillRect(x, y + h, w, h);
                break;

            case type2:
                g.setColor(Color.green);
                g.fillRect(x, y, w, h);
                g.setColor(Color.red);
                g.fillRect(x + w, y, w, h);
                break;

            case type3:
                g.setColor(Color.green);
                g.fillRect(x, y, w, h);
                g.fillRect(x + w + w, y, w, h);
                g.setColor(Color.red);
                g.fillRect(x + w, y, w, h);
                break;

            case type4:
                g.setColor(Color.green);
                g.fillRect(x, y, w, h);
                g.fillRect(x + w, y + h, w, h);
                g.setColor(Color.red);
                g.fillRect(x + w, y, w, h);
                g.fillRect(x, y + h, w, h);
                break;

            /*case type5:
            	g.setColor(Color.red);
            	g.fillRect(x, y, w, h);
            	g.setColor(Color.green);
            	g.fillRect(x, y + h, w, h);
            	break;

            case type6:
            	g.setColor(Color.green);
            	g.fillRect(x, y, w, h);
            	g.setColor(Color.red);
            	g.fillRect(x + w, y, w, h);
            	break;

            case type7:
            	g.setColor(Color.green);
            	g.fillRect(x, y, w, h);
            	g.fillRect(x + w + w, y, w, h);
            	g.setColor(Color.red);
            	g.fillRect(x + w, y, w, h);
            	break;

            case type8:
            	g.setColor(Color.green);
            	g.fillRect(x, y, w, h);
            	g.fillRect(x + w, y + h, w, h);
            	g.setColor(Color.red);
            	g.fillRect(x + w, y, w, h);
            	g.fillRect(x, y + h, w, h);
            	break;*/
        }

        Image.saveImage(bi, filename);
    }
    
}
