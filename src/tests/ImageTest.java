package tests;

import junit.framework.Assert;
import global.DebugInfo1;
import global.Enumerators;

import org.junit.Test;

import utilities.Data;
import utilities.Image;
import utilities.Matrix;

public class ImageTest {

	@Test
    public void TestGetIntegralImage1()
    {
        Matrix m001 = new Matrix(4, 4, new double[] { 5, 2, 5, 2, 3, 6, 3, 6, 5, 2, 5, 2, 3, 6, 3, 6 });
        Matrix i001 = new Matrix(4, 4, new double[] { 5, 7, 12, 14, 8, 16, 24, 32, 13, 23, 36, 46, 16, 32, 48, 64 });
        Matrix i002 = Image.getIntegralImage(m001);
        Assert.assertTrue(i001.getIsEqual(i002));
    }

	@Test
    public void TestGetIntegralImage2()
    {
        double eps = 0.000001;

        Matrix face1ref = DebugInfo1.im;
        Matrix integral1ref = DebugInfo1.ii_mm;
        
        Matrix face1 = Data.getImageMatrix("face00001.bmp", Enumerators.DataSet.pFace).getNormal();
        Matrix integral1 = Image.getIntegralImage(face1);
        
        Image.saveImage(face1.getRescale(0, 255) , "face1");
        Image.saveImage(face1ref.getRescale(0, 255) , "face1ref");
        Image.saveImage(integral1.getRescale(0, 255) , "integral1");
        Image.saveImage(integral1ref.getRescale(0, 255) , "integral1ref");
        
        
        Assert.assertTrue((integral1.getSubtraction(integral1ref)).getAbsMatrix().getSum() < eps); 
    }

}
