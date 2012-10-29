package tests;

import java.util.Arrays;
import java.util.List;

import global.Methods;
import global.Enumerators.DataSet;

import org.junit.Assert;
import org.junit.Test;

import utilities.AdaBoost;
import utilities.AdaBoostRespons;
import utilities.Data;
import utilities.Feature;
import utilities.Image;
import utilities.Matrix;
import utilities.WeakClassifier;

public class AdaBoostTest {

	@Test
	public void TestGetInitializedWeights()
    {
        int nrNeg = 5000;
        int nrPos = 3000;

        Matrix weights = AdaBoost.getInitializedWeights(nrPos, nrNeg);

        Assert.assertTrue(Math.abs(weights.getSum() - 1) < 0.0001);
    }

	@Test
	public void TestGetInitializedIsFaceList()
    {
        int nrNeg = 5000;
        int nrPos = 3000;

        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(nrPos, nrNeg);

        Assert.assertTrue(isFaceList.getSum() == nrPos);
    }
	
	@Test
	public void TestLearnWeakClassifier()
    {
        int nrNeg = 5000;
        int nrPos = 3000;

        Feature ftype = Feature.getAllFeatures(19, 19).get(12028);

        Matrix weights = AdaBoost.getInitializedWeights(nrPos, nrNeg);
        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(nrPos, nrNeg);

        String[] faceNeg = Arrays.copyOfRange(Data.getImageList(DataSet.nFace), 0, nrNeg);
        String[] facePos = Arrays.copyOfRange(Data.getImageList(DataSet.pFace), 0, nrPos);

        Matrix[] faceNegIntegrals = Image.getIntegralImages(Data.getNormalisedImageMatrixList(faceNeg, DataSet.nFace));
        Matrix[] facePosIntegrals = Image.getIntegralImages(Data.getNormalisedImageMatrixList(facePos, DataSet.pFace));

        Matrix featureValuesNeg = Feature.getFeatureValues(faceNegIntegrals, ftype);
        Matrix featureValuesPos = Feature.getFeatureValues(facePosIntegrals, ftype);
        
        Matrix featureValuesAll = new Matrix(featureValuesPos.getNrVals() + featureValuesNeg.getNrVals(), 1, 
        									 Methods.concat(featureValuesPos.getData(), featureValuesNeg.getData()));

        WeakClassifier weakClassifier = AdaBoost.learnWeakClassifier(featureValuesAll, weights, isFaceList);

        double meanPos = featureValuesPos.getSum() / (double)nrPos;
        double meanNeg = featureValuesNeg.getSum() / (double)nrNeg;
        double meanTot = (meanPos + meanNeg) / 2.0;

        Assert.assertTrue(Math.abs(weakClassifier.threshold - meanTot) < 0.001);
        //Assert.assertTrue(Math.abs(weakClassifier.threshold - (-3.6453)) < 0.001);
        Assert.assertTrue(weakClassifier.parity == 1);
    }
	
	@Test
	public void TestAdaBoost()
    {
        int T = 10;
        //Matrix Thetas = new Matrix(3, 3, new double[] {9.980000000000000, -0.079477305003370,  0.010000000000000,
        //                                               4.770000000000000,  0.031892257638319, -0.010000000000000,
        //                                               8.490000000000000, -0.055953021563651,  0.010000000000000});
        
        double[] alphas = new double[] { 1.694004004780836, 1.188907795146310, 0.982307485873930 };

        int featureSize = 19;

        List<Feature> features = Feature.getAllFeatures(featureSize, featureSize);
        Feature[] allFeatures = features.toArray(new Feature[features.size()]); //.GetRange(0, 1000).ToArray();

        String[] negFaceList = Data.getImageList(DataSet.nFace);
        String[] posFaceList = Data.getImageList(DataSet.pFace);

        Matrix[] negImages = Data.getNormalisedImageMatrixList(negFaceList, DataSet.nFace);
        Matrix[] posImages = Data.getNormalisedImageMatrixList(posFaceList, DataSet.pFace);

        Matrix[] negIntegralImages = Image.getIntegralImages(negImages);
        Matrix[] posIntegralImages = Image.getIntegralImages(posImages);

        Matrix[] integralImages = Methods.concat(posIntegralImages, negIntegralImages);

        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(posIntegralImages.length, negIntegralImages.length);
        //Matrix allFeatureValues = Feature.getAllFeatureValues(integral_images, allFeatures);

        AdaBoostRespons[] adaBoostResponses = AdaBoost.executeAdaBoost(integralImages, allFeatures, isFaceList, negIntegralImages.length, T);

        for (int i = 0; i < adaBoostResponses.length; i++)
        {
            Feature.saveFeaturePic(Feature.getFeature(adaBoostResponses[i].featureIndex.j, adaBoostResponses[i].featureIndex.featureType,  featureSize, featureSize), featureSize, featureSize, "feature" + i + ".jpg");
        }

        double sumAlpha = 0;
        for (int i = 0; i < adaBoostResponses.length; i++)
        {
        	sumAlpha += Math.abs(adaBoostResponses[i].alpha - alphas[i]);
        }

        double eps = 0.000001;
        Assert.assertTrue(sumAlpha < eps);
    }
}
