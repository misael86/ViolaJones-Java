package tests;

import global.DebugInfo1;
import global.Enumerators;

import org.junit.Assert;
import org.junit.Test;

import utilities.Data;
import utilities.Matrix;

public class DataTest {

	@Test
	public void TestGetRandomImages()
    {
        String[] images = Data.getImageList(Enumerators.DataSet.nFace);
        String[] randomImages1 = Data.getRandomImageList(images, 100);
        String[] randomImages2 = Data.getRandomImageList(images, 100);
        Assert.assertNotSame(randomImages1, randomImages2);
    }

	@Test
    public void TestGetImage()
    {
        double eps = 0.000001;

        Matrix face1ref = DebugInfo1.im;
        Matrix face1 = Data.getImageMatrix("face00001.bmp", Enumerators.DataSet.pFace).getNormal();
        face1 = face1.getRescale(face1ref.getMinValue(), face1ref.getMaxValue());

        Assert.assertTrue(face1.getSubtraction(face1ref).getAbsMatrix().getSum() < eps); 
    }
	
}
