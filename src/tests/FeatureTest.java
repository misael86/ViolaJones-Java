package tests;

import global.DebugInfo1;
import global.DebugInfo2;
import global.DebugInfo3;
import global.Enumerators;
import global.Enumerators.FeatureType;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import utilities.Data;
import utilities.Feature;
import utilities.Image;
import utilities.Matrix;

public class FeatureTest {

	@Test
	public void TestGetBoxSum()
    {
        Matrix m001 = new Matrix(3, 2, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix i001 = Image.getIntegralImage(m001);
        Matrix m002 = m001.getSubMatrix(2, 1, 2, 2);
        
        Assert.assertTrue(Feature.getBoxSum(i001, 2, 1, 2, 2) == m002.getSum());
    }

	@Test
	public void TestGetFeatures()
    {
        List<Feature> type1 = Feature.getFeatures(19, 19, FeatureType.type1);
        List<Feature> type2 = Feature.getFeatures(19, 19, FeatureType.type2);
        List<Feature> type3 = Feature.getFeatures(19, 19, FeatureType.type3);
        List<Feature> type4 = Feature.getFeatures(19, 19, FeatureType.type4);
        
        int nrFeatures = type1.size() + type2.size() + type3.size() + type4.size();
        
        Assert.assertTrue(nrFeatures == 32746);
    }
	
	@Test
	public void TestGetFeatureValue1()
    {
        double eps = 0.000001;

        Feature feature1 = new Feature(FeatureType.type1, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature2 = new Feature(FeatureType.type2, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature3 = new Feature(FeatureType.type3, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature4 = new Feature(FeatureType.type4, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);

        double f1 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature1);
        double f2 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature2);
        double f3 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature3);
        double f4 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature4);

        double f1ref = DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y + DebugInfo2.h, DebugInfo2.w, DebugInfo2.h).getSum();
        double f2ref = DebugInfo1.im.getSubMatrix(DebugInfo2.x + DebugInfo2.w, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum();
        double f3ref = DebugInfo1.im.getSubMatrix(DebugInfo2.x + DebugInfo2.w, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x + 2 * DebugInfo2.w, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum();
        double f4ref = DebugInfo1.im.getSubMatrix(DebugInfo2.x + DebugInfo2.w, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() +
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y + DebugInfo2.h, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h).getSum() -
                       DebugInfo1.im.getSubMatrix(DebugInfo2.x + DebugInfo2.w, DebugInfo2.y + DebugInfo2.h, DebugInfo2.w, DebugInfo2.h).getSum();
        
        Assert.assertTrue(Math.abs(f1 - f1ref) < eps);
        Assert.assertTrue(Math.abs(f2 - f2ref) < eps);
        Assert.assertTrue(Math.abs(f3 - f3ref) < eps);
        Assert.assertTrue(Math.abs(f4 - f4ref) < eps);
    }
	
	@Test
	public void TestGetFeatureValue2()
    {
        double eps = 0.000001;

        Feature feature1 = new Feature(FeatureType.type1, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature2 = new Feature(FeatureType.type2, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature3 = new Feature(FeatureType.type3, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);
        Feature feature4 = new Feature(FeatureType.type4, DebugInfo2.x, DebugInfo2.y, DebugInfo2.w, DebugInfo2.h);

        double f1 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature1);
        double f2 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature2);
        double f3 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature3);
        double f4 = Feature.getFeatureValue(DebugInfo1.ii_mm, feature4);

        Assert.assertTrue(Math.abs(DebugInfo2.f1 - f1) < eps);
        Assert.assertTrue(Math.abs(DebugInfo2.f2 - f2) < eps);
        Assert.assertTrue(Math.abs(DebugInfo2.f3 - f3) < eps);
        Assert.assertTrue(Math.abs(DebugInfo2.f4 - f4) < eps);
    }
	
	//@Test
	public void TestGetFeatureValues()
    {
        double eps = 0.000001;

        String[] names = Arrays.copyOfRange(Data.getImageList(Enumerators.DataSet.pFace), 0, 100);
        Matrix[] images = Data.getNormalisedImageMatrixList(names, Enumerators.DataSet.pFace);
        Matrix[] integrals = Image.getIntegralImages(images);
        Matrix values = Feature.getFeatureValues(integrals, DebugInfo3.ftype);

        Assert.assertTrue((values.getSubtraction(DebugInfo3.fs)).getAbsMatrix().getSum() < eps);
    }
	
}
