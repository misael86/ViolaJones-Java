package utilities;

import java.util.ArrayList;
import java.util.List;

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

        return sum > (alphaSum / 2) ? 1 : 0;
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
	
	public static WeakClassifier learnWeakClassifier(Matrix allFeatureValues, Matrix weights, Matrix isFaceList, int j)
    {
        Matrix featureRow = allFeatureValues.getRow(j);

        return learnWeakClassifier(featureRow, weights, isFaceList);
    }
	
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
	
	public static AdaBoostRespons[] executeAdaBoost(Matrix[] integralImages, Feature[] allFeatures, Matrix isFaceList, int nrNegative, int nrWeakClassifiers)
    {
		FeatureType[] featureTypes = FeatureType.values();
		
        InitiateFeatureValues(integralImages, allFeatures, featureTypes);

        AdaBoostRespons[] adaBoostRespons = new AdaBoostRespons[nrWeakClassifiers];

        Matrix weights = getInitializedWeights(isFaceList.getNrVals() - nrNegative, nrNegative);

        for (int t = 0; t < nrWeakClassifiers; t++)
        {
            weights = weights.getDivision(weights.getSum());

            FeatureIndex bestFeatureIndex = new FeatureIndex(FeatureType.type1, Integer.MAX_VALUE);
            WeakClassifier bestClassifier = new WeakClassifier(Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            for (FeatureType featureType : featureTypes)
            {
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

            double beta = bestClassifier.error / (1 - bestClassifier.error);
            double alpha = Math.log(1 / beta);

            Matrix featureValuesOfSameType = Matrix.loadMatrix("FeatureValues_" + bestFeatureIndex.featureType.name());
            for (int i = 1; i <= featureValuesOfSameType.getNrCols(); i++)
            {
                int classification = getWeakClassification(featureValuesOfSameType.getValue(i, bestFeatureIndex.j), bestClassifier.parity, bestClassifier.threshold);
                weights.setValue(i, Math.abs(classification - isFaceList.getValue(i)) == 0 ? weights.getValue(i) * beta : weights.getValue(i));
            }

            adaBoostRespons[t] = new AdaBoostRespons(bestClassifier, alpha, bestFeatureIndex);
        }

        return adaBoostRespons;
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
	
	        	Matrix.saveMatrix(allFeatureValuesOfSameType, "FeatureValues_" + featureType.name());
        	}
        }    
    }
	
	public static Matrix getInitializedWeights(int nrPos, int nrNeg)
    {
        double val_p = 1.0 / (double)(2 * nrPos);
        double val_n = 1.0 / (double)(2 * nrNeg);

        double[] weights = new double[nrNeg + nrPos];

        for (int i = 0; i < weights.length; i++)
        {
            weights[i] = i < nrPos ? val_p : val_n;
        }

        return new Matrix(weights.length, 1, weights);
    }

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
