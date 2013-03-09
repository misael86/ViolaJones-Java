package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import models.AdaBoostRespons;
import models.FeatureIndex;
import models.ScanImageRespons;
import models.WeakClassifier;

import global.Enumerators.DataSet;
import global.Enumerators.FeatureType;
import global.Strings;

public class AdaBoost 
{

	public static int getClassification(Matrix featureValues, AdaBoostRespons[] adaBoostResponses)
    {
        double sum = 0;
        double alphaSum = 0;
        for (AdaBoostRespons adaBoostRespons : adaBoostResponses)
        {
            sum += adaBoostRespons.alpha * 
            		getWeakClassification(featureValues.getValue(adaBoostRespons.featureIndex.j), 
            							  adaBoostRespons.weakClassifier.parity, 
            							  adaBoostRespons.weakClassifier.threshold);
            alphaSum += adaBoostRespons.alpha;
        }

        return sum > (alphaSum / 2.0) ? 1 : 0;
    }
	
	public static int getWeakClassification(double featureValue, double parity, double threshold)
    {
        return parity * featureValue < parity * threshold ? 1 : 0;
    }
	
	public static Matrix getWeakClassifications(Matrix featureValues, double parity, double threshold)
    {
		double[] data = new double[featureValues.getNrVals()];
		for(int i = 1; i <= featureValues.getNrVals(); i++)
		{
			data[i - 1] = getWeakClassification(featureValues.getValue(i), parity, threshold);
		}

        return new Matrix(data.length, 1, data);
    }
	
	// TESTED
	public static WeakClassifier learnWeakClassifier(Matrix allFeatureValues, Matrix weights, Matrix isFaceList, int j)
    {
        Matrix featureRow = allFeatureValues.getRow(j);

        return learnWeakClassifier(featureRow, weights, isFaceList);
    }
	
	// TESTED
	public static WeakClassifier learnWeakClassifier(Matrix featureValues, Matrix weights, Matrix isFaceList)
    {
        if (featureValues.getNrVals() != weights.getNrVals() ||
        	featureValues.getNrVals() != isFaceList.getNrVals()) 
        { 
        	String errorMessage = "Error at AdaBoost.learnWeakClassifier" + Strings.newline +
					"Feature values: " + featureValues.getNrVals() + Strings.newline +
					"Weights: " + weights.getNrVals() + Strings.newline +
					"Is face list: " + isFaceList.getNrVals();
			
			throw new RuntimeException(errorMessage); 
        }

        Matrix muPosMatrix = weights.getDotMultiplication(isFaceList);
        double muPosValue = muPosMatrix.getDotMultiplication(featureValues).getSum() / muPosMatrix.getSum();

        Matrix muNegMatrix = weights.getSubtraction(muPosMatrix);
        double muNegValue = muNegMatrix.getDotMultiplication(featureValues).getSum() / muNegMatrix.getSum();

        double threshold = (muPosValue + muNegValue) / 2.0;

        double errorNeg = isFaceList.getSubtraction(getWeakClassifications(featureValues, -1, threshold)).getAbsMatrix().getDotMultiplication(weights).getSum();
        double errorPos = isFaceList.getSubtraction(getWeakClassifications(featureValues, 1, threshold)).getAbsMatrix().getDotMultiplication(weights).getSum();

        double error = Math.min(errorNeg, errorPos);
        int pStar = error == errorNeg ? -1 : 1;

        return new WeakClassifier(pStar, error, threshold);
    }
	
	// TESTED
	public static AdaBoostRespons[] executeAdaBoost(Matrix[] integralImages, Feature[] allFeatures, Matrix isFaceList, int nrNegative, int nrWeakClassifiers)
    {
		FeatureType[] featureTypes = FeatureType.values();
		
        InitiateFeatureValues(integralImages, allFeatures, featureTypes);

        AdaBoostRespons[] adaBoostRespons = new AdaBoostRespons[nrWeakClassifiers];

        Matrix weights = getInitializedWeights(isFaceList.getNrVals() - nrNegative, nrNegative);

        for (int t = 0; t < nrWeakClassifiers; t++)
        {
        	if(weights.getSum() == 0) 
        	{
        		weights.setSeveralValues(1, weights.getNrVals(), 1.0 / weights.getNrVals());
        	}
        	else
        	{
        		weights = weights.getDivision(weights.getSum());
        	}
        	
        	// CHANGED
        	// weights = weights.getNormal();
        	// CHANGED
            // weights = weights.getDivision(weights.getSum());

            FeatureIndex bestFeatureIndex = new FeatureIndex(FeatureType.type1, Integer.MAX_VALUE);
            WeakClassifier bestClassifier = new WeakClassifier(Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            for (FeatureType featureType : featureTypes)
            {
            	System.out.println("F: " + featureType + ", T: " + t);
            	
                Matrix allFeatureValuesOfSameType = Matrix.loadMatrix("FeatureValues_" + featureType.name());

                for (int j = 1; j <= allFeatureValuesOfSameType.getNrRows(); j++)
                {
                    WeakClassifier weakClassifier = learnWeakClassifier(allFeatureValuesOfSameType, weights, isFaceList, j);
                    if (weakClassifier.error < bestClassifier.error)
                    {                  	
                        bestClassifier = weakClassifier;
                        bestFeatureIndex.featureType = featureType;
                        bestFeatureIndex.j = j;
                    }
                }
            }
            
            System.out.println("---------------");
            System.out.println("Best classifier");
            System.out.println("Feature type: " + bestFeatureIndex.featureType);
            System.out.println("J: " + bestFeatureIndex.j);
            System.out.println("Number Of Features: " + Matrix.loadMatrix("FeatureValues_" + bestFeatureIndex.featureType.name()).getNrRows());
            System.out.println("---------------");

            double beta = bestClassifier.error / (1.0 - bestClassifier.error);
            double alpha = Math.log(1.0 / beta);

            Matrix featureValuesOfSameType = Matrix.loadMatrix("FeatureValues_" + bestFeatureIndex.featureType.name());
            for (int i = 1; i <= featureValuesOfSameType.getNrCols(); i++)
            {
                int classification = getWeakClassification(featureValuesOfSameType.getValue(i, bestFeatureIndex.j), bestClassifier.parity, bestClassifier.threshold);
                
                if(Math.abs(classification - isFaceList.getValue(i)) == 0 && beta != 0)
                {
                	weights.setValue(i, weights.getValue(i) * beta);
                }
            }

            adaBoostRespons[t] = new AdaBoostRespons(bestClassifier, alpha, bestFeatureIndex);
        }
        
        AdaBoostRespons.saveAdaBoostResponsArray(adaBoostRespons, "strongClassifier");

        return adaBoostRespons;
    }
	
	// TESTED
	public static double ApplyDetector(AdaBoostRespons[] weakClassifiers, Matrix integralImage)
	{
		List<Feature[]> allSeparatedFeatures = Feature.getAllSeparatedFeatures(19,19);
		
		double result = 0;
		
		for(AdaBoostRespons weakClassifier : weakClassifiers)
		{
			int featureType = weakClassifier.featureIndex.featureType.ordinal();
			int featureIndex = weakClassifier.featureIndex.j;
			
			Feature currentFeature = allSeparatedFeatures.get(featureType)[featureIndex - 1];
			double featureValue = Feature.getFeatureValue(integralImage, currentFeature);			
			
			result += weakClassifier.alpha * getWeakClassification(featureValue, weakClassifier.weakClassifier.parity, weakClassifier.weakClassifier.threshold);
		}
		
		return result;
	}
	
    public static double computeROC1(AdaBoostRespons[] weakClassifiers, Matrix[] pFaces, Matrix[] nFaces) 
    {
    	double posScore = 0;
    	for (Matrix pFace : pFaces) 
        {
			Matrix image = pFace;
			image = image.getNormal();
			image = Image.getIntegralImage(image);
			  
			posScore += ApplyDetector(weakClassifiers, image);
        }
    	
    	double negScore = 0;
    	for (Matrix nFace : nFaces) 
        {
			Matrix image = nFace;
			image = image.getNormal();
			image = Image.getIntegralImage(image);
			  
			negScore += ApplyDetector(weakClassifiers, image);
        }
    	
    	double deltaThreshold = (negScore / (double)nFaces.length) - (posScore / (double)pFaces.length);
    	double threshold = (negScore / (double)nFaces.length) + (deltaThreshold / 2.0);
    	
    	Matrix result = new Matrix(1, 1, new double[] { threshold });
    	saveThreshold(result, 1);
    	
    	System.out.println("ROC1: " + threshold);
    	
    	return threshold;
    }
    
    public static double computeROC2(AdaBoostRespons[] weakClassifiers, Matrix[] pFaces, Matrix[] nFaces) 
    {
    	double thresholdMin = Double.POSITIVE_INFINITY;
    	double thresholdMax = Double.NEGATIVE_INFINITY;
    	
    	double[] trueScores = new double[pFaces.length];
    	for (int i = 0; i < pFaces.length; i++) 
        {
			Matrix image = pFaces[i];
			image = image.getNormal();
			image = Image.getIntegralImage(image);
			  
			double score = ApplyDetector(weakClassifiers, image);
			thresholdMin = Math.min(thresholdMin, score);
			thresholdMax = Math.max(thresholdMax, score);
			trueScores[i] = score;
        }
    	
    	double[] falseScores = new double[nFaces.length];
    	for (int i = 0; i < nFaces.length; i++) 
        {
			Matrix image = nFaces[i];
			image = image.getNormal();
			image = Image.getIntegralImage(image);
			  
			double score = ApplyDetector(weakClassifiers, image);
			thresholdMin = Math.min(thresholdMin, score);
			thresholdMax = Math.max(thresholdMax, score);
			falseScores[i] = score;
        }
  
    	double steps = 1000;
    	double deltaThreshold = (thresholdMax - thresholdMin) / steps;
    	double maxTruePositiveRate = Double.NEGATIVE_INFINITY;
    	double minFalsePositiveRate = Double.POSITIVE_INFINITY;
    	double bestThreshold = 0;
    	
    	for (double testThreshold = thresholdMin; testThreshold <= thresholdMax; testThreshold += deltaThreshold) 
    	{
    		int truePositive = 0;
    		int falsePositive = 0;
    		
    		for(double trueScore : trueScores) 
    		{
    			if (trueScore >= testThreshold) { truePositive++; }
       		}
    		for(double falseScore : falseScores) 
    		{
    			if (falseScore >= testThreshold) { falsePositive++; }
    		}
    		
    		double truePositiveRate = truePositive / (double) pFaces.length;
            double falsePositiveRate = falsePositive / (double) nFaces.length;
    		
            if (truePositiveRate >= 0.7 && falsePositiveRate <= minFalsePositiveRate) 
            {
            	maxTruePositiveRate = truePositiveRate;
            	minFalsePositiveRate = falsePositiveRate;
            	bestThreshold = testThreshold;
            }    
    	}
    	
    	Matrix result = new Matrix(1, 1, new double[] { bestThreshold });
    	saveThreshold(result, 2);
    	
    	System.out.println("ROC2: " + bestThreshold);
    	System.out.println("True Positive Rate: " + maxTruePositiveRate);
    	System.out.println("False Positive Rate: " + minFalsePositiveRate);

    	return bestThreshold;
    	
    }
	
    public static ScanImageRespons[] ScanImageFixedSize(AdaBoostRespons[] weakClassifiers, Matrix image, double threshold) 
    {	
    	ArrayList<ScanImageRespons> respons = new ArrayList<ScanImageRespons>();
    	
    	for(int x = 1; x < image.getNrCols() - 19; x += 2)
    	{
    		for(int y = 1; y < image.getNrRows() - 19; y += 2)
    		{
    			Matrix subImage = image.getSubMatrix(x, y, 19, 19);
    			subImage = subImage.getNormal();
    			Matrix subIntegralImage = Image.getIntegralImage(subImage);
    			double ans = ApplyDetector(weakClassifiers, subIntegralImage);
    			
    			if (ans >= threshold) {
    				respons.add(new ScanImageRespons(x, y, 19));
    			}
    		}
    	}
    	
    	return respons.toArray(new ScanImageRespons[respons.size()]);
    }
    
    // TESTED
    public static void displayDetections(BufferedImage bi, ScanImageRespons[] detections, String filename) 
    {
        Graphics2D graphics = bi.createGraphics();
        graphics.setColor(Color.red);

        for (ScanImageRespons detection : detections) 
        {
        	graphics.drawRect(detection.x, detection.y, detection.size, detection.size);
		}

        Image.saveImage(bi, filename);
    }
    
    // TESTED
    public static void saveThreshold(Matrix threshold, int number) 
    {
    	Matrix.saveMatrix(threshold, "threshold_" + number);
    }
    
    // TESTED
    public static Matrix loadThreshold(int number) 
    {
    	return Matrix.loadMatrix("threshold_" + number);
    }
    
    // TESTED
    public static void saveStrongClassifier(AdaBoostRespons[] adaBoostRespons) 
    {
    	AdaBoostRespons.saveAdaBoostResponsArray(adaBoostRespons, "strongClassifier");
	}
    
    // TESTED
    public static AdaBoostRespons[] loadStrongClassifier()
    {
    	return AdaBoostRespons.loadAdaBoostResponsArray("strongClassifier");
    }
    
    public static void findFaces(AdaBoostRespons[] weakClassifiers, double threshold, DataSet dataSet, String filename, boolean group) 
    {	
        BufferedImage image = Data.getBufferedImage(Strings.dataURL[dataSet.ordinal()] + filename);
        image = Data.getGrayImage(image);
        Matrix imageMatrix = Image.getImageMatrix(image);
        
        ScanImageRespons[] detections = AdaBoost.ScanImageFixedSize(weakClassifiers, imageMatrix, threshold);
        displayDetections(image, detections, filename + "_detections");
    }
    
	private static void InitiateFeatureValues(Matrix[] integralImages, Feature[] allFeatures, FeatureType[] featureTypes)
    {
        for (FeatureType featureType : featureTypes)
        {
        	List<Feature> allFeaturesOfSameType = new ArrayList<Feature>();
        	for(Feature feature : allFeatures)
        	{
        		if(feature.type == featureType)
        		{
        			allFeaturesOfSameType.add(feature);
        		}
        	}
        	
        	if(allFeaturesOfSameType.size() > 0)
        	{
	        	Matrix allFeatureValuesOfSameType = Feature.getAllFeatureValues(integralImages, allFeaturesOfSameType.toArray(new Feature[allFeaturesOfSameType.size()]));
	        	Feature.saveFeatureValues(allFeatureValuesOfSameType, featureType.name());
        	}
        }    
    }
	
	// TESTED
	public static Matrix getInitializedWeights(int nrPos, int nrNeg)
    {
        double val_p = 1.0 / (2.0 * (double)nrPos);
        double val_n = 1.0 / (2.0 * (double)nrNeg);

        double[] weights = new double[nrNeg + nrPos];

        for (int i = 0; i < weights.length; i++)
        {
            weights[i] = i < nrPos ? val_p : val_n;
        }

        return new Matrix(weights.length, 1, weights);
    }

	// TESTED
    public static Matrix getInitializedIsFaceList(int nrPos, int nrNeg)
    {
        double[] isFaceList = new double[nrNeg + nrPos];

        for (int i = 0; i < nrPos; i++)
        {
            isFaceList[i] = 1;
        }

        return new Matrix(isFaceList.length, 1, isFaceList);
    }
	
}
