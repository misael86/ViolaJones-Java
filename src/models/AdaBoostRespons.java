package models;

import global.Enumerators.FeatureType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilities.Matrix;

public class AdaBoostRespons implements Serializable
{
	private static final long serialVersionUID = 1L;
	public WeakClassifier weakClassifier;
    public double alpha;
    public FeatureIndex featureIndex;

    public AdaBoostRespons(WeakClassifier weakClassifier, double alpha, FeatureIndex featureIndex)
    {
        this.weakClassifier = weakClassifier;
        this.alpha = alpha;
        this.featureIndex = featureIndex;
    }
    
    public static void saveAdaBoostResponsArray(AdaBoostRespons[] adaBoostRespons, String fileName) 
    {
    	for(int i = 0; i < adaBoostRespons.length; i++)
    	{
    		saveAdaBoostRespons(adaBoostRespons[i], fileName + "_" + i);
    	}
    }
    
    public static AdaBoostRespons[] loadAdaBoostResponsArray(String fileName) 
    {
    	List<AdaBoostRespons> adaBoostRespons = new ArrayList<AdaBoostRespons>();
    	
    	try
    	{
    		int i = 0;
    		while(true)
    		{
    			adaBoostRespons.add(loadAdaBoostRespons(fileName + "_" + i++)); 		
    		}
    	}
    	catch(Exception ex)
    	{
    		return adaBoostRespons.toArray(new AdaBoostRespons[adaBoostRespons.size()]);
    	}
    }
    
	public static void saveAdaBoostRespons(AdaBoostRespons adaBoostRespons, String fileName) 
    {
		double weakClassifierError = adaBoostRespons.weakClassifier.error;
		double weakClassifierThreshold = adaBoostRespons.weakClassifier.threshold;
		double weakClassifierParity = adaBoostRespons.weakClassifier.parity;
		double alpha = adaBoostRespons.alpha;
		double featureType = adaBoostRespons.featureIndex.featureType.ordinal();
		double featureIndex= adaBoostRespons.featureIndex.j;
		
		Matrix result = new Matrix(6, 1, new double[] { weakClassifierError, weakClassifierThreshold, weakClassifierParity, alpha, featureType, featureIndex });
		Matrix.saveMatrix(result, fileName);
    }

	public static AdaBoostRespons loadAdaBoostRespons( String fileName ) 
    {
    	Matrix result = Matrix.loadMatrix(fileName);
    	
    	WeakClassifier weakClassifier = new WeakClassifier((int)result.getValue(3), result.getValue(1), result.getValue(2));
    	FeatureIndex featureIndex = new FeatureIndex(FeatureType.values()[(int)result.getValue(5)], (int)result.getValue(6));
    	double alpha = result.getValue(4);
    	
    	return new AdaBoostRespons(weakClassifier, alpha, featureIndex);
    }
	
}
