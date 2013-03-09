package tests;

import global.Methods;
import global.Enumerators.DataSet;

import models.AdaBoostRespons;
import models.WeakClassifier;

import org.junit.Assert;
import org.junit.Test;

import utilities.AdaBoost;
import utilities.Data;
import utilities.Feature;
import utilities.Image;
import utilities.Matrix;

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
        int nrNeg = 4000;
        int nrPos = 2000;

        Feature ftype = Feature.getAllFeatures(19, 19)[12028];

        Matrix weights = AdaBoost.getInitializedWeights(nrPos, nrNeg);
        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(nrPos, nrNeg);
        
        String[] allFaceNeg = Data.getImageList(DataSet.nFace);
        String[] allFacePos = Data.getImageList(DataSet.pFace);
        
        String[] faceNeg = Data.getRandomImageList(allFaceNeg, nrNeg).learnImages;
        String[] facePos = Data.getRandomImageList(allFacePos, nrPos).learnImages;

        Matrix[] faceNegIntegrals = Image.getIntegralImages(Data.getNormalisedImageMatrixList(faceNeg, DataSet.nFace));
        Matrix[] facePosIntegrals = Image.getIntegralImages(Data.getNormalisedImageMatrixList(facePos, DataSet.pFace));
		        
        Matrix featureValuesNeg = Feature.getFeatureValues(faceNegIntegrals, ftype);
        Matrix featureValuesPos = Feature.getFeatureValues(facePosIntegrals, ftype);
        
        Matrix featureValuesAll = new Matrix(nrPos + nrNeg, 1, Methods.concat(featureValuesPos.getData(), featureValuesNeg.getData()));
        
        WeakClassifier weakClassifier = AdaBoost.learnWeakClassifier(featureValuesAll, weights, isFaceList);

        double meanPos = featureValuesPos.getSum() / (double)nrPos;
        double meanNeg = featureValuesNeg.getSum() / (double)nrNeg;
        double meanTot = (meanPos + meanNeg) / 2.0;
        
        System.out.println(weakClassifier.threshold);
        
        Assert.assertTrue(weakClassifier.parity == 1);
        Assert.assertTrue(Math.abs(weakClassifier.threshold - meanTot) < 0.001);
        Assert.assertTrue(Math.abs(weakClassifier.threshold - (-3.6453)) < 0.001);
    }
	
	@Test
	public void TestExecuteAdaBoost()
    {
        int T = 3;
        
        double[] alphas = new double[] { 1.694004004780836, 1.188907795146310, 0.982307485873930 };

        int featureSize = 19;

        Feature[] allFeatures = Feature.getAllFeatures(featureSize, featureSize);

        String[] negFaceList = Data.getImageList(DataSet.nFace);
        String[] posFaceList = Data.getImageList(DataSet.pFace);

        Matrix[] negImages = Data.getNormalisedImageMatrixList(negFaceList, DataSet.nFace);
        Matrix[] posImages = Data.getNormalisedImageMatrixList(posFaceList, DataSet.pFace);

        Matrix[] negIntegralImages = Image.getIntegralImages(negImages);
        Matrix[] posIntegralImages = Image.getIntegralImages(posImages);

        Matrix[] integralImages = Matrix.getJoinedLists(posIntegralImages, negIntegralImages);

        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(posIntegralImages.length, negIntegralImages.length);
        //Matrix allFeatureValues = Feature.getAllFeatureValues(integral_images, allFeatures);

        AdaBoostRespons[] adaBoostResponses = AdaBoost.executeAdaBoost(integralImages, allFeatures, isFaceList, negIntegralImages.length, T);
        
        AdaBoost.computeROC1(adaBoostResponses, posImages, negImages);
        AdaBoost.computeROC2(adaBoostResponses, posImages, negImages);
        
        for (int i = 0; i < adaBoostResponses.length; i++)
        {
            Feature.saveFeaturePic(Feature.getFeature(adaBoostResponses[i].featureIndex.j, adaBoostResponses[i].featureIndex.featureType,  featureSize, featureSize), featureSize, featureSize, "feature" + i);
        }

        double sumAlpha = 0;
        for (int i = 0; i < adaBoostResponses.length; i++)
        {
        	sumAlpha += Math.abs(adaBoostResponses[i].alpha - alphas[i]);
        }

        double eps = 0.000001;
        Assert.assertTrue(sumAlpha < eps);
    }
	
	@Test
	public void TestApplyDetector()
    {
		Matrix image = Data.getImageMatrix("face00001.bmp", DataSet.pFace);
		image = image.getNormal();
		image = Image.getIntegralImage(image);
		
		Matrix image2 = Data.getImageMatrix("B1_00001.bmp", DataSet.nFace);
		image2 = image2.getNormal();
		image2 = Image.getIntegralImage(image2);
		
		AdaBoostRespons[] strongClassifier = AdaBoostRespons.loadAdaBoostResponsArray("strongClassifier");
		
		double result = AdaBoost.ApplyDetector(strongClassifier, image);
		double result2 = AdaBoost.ApplyDetector(strongClassifier, image2);
		//double eps = 0.000001;
		
		Assert.assertTrue(result >= AdaBoost.loadThreshold(2).getValue(1));
		Assert.assertTrue(result2 < AdaBoost.loadThreshold(2).getValue(1));
		
		//Assert.assertTrue(Math.abs(result - 9.1409) < eps);
    }
}
