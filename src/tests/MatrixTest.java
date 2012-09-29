package tests;

import org.junit.Assert;
import org.junit.Test;

import utilities.Matrix;

public class MatrixTest {
	
	@Test
	public void TestConstructor1()
    {
		Matrix m001 = new Matrix(2, 3);
        Matrix m002 = new Matrix(2, 3, new double[] { 0, 0, 0, 0, 0, 0 });
        Matrix m003 = new Matrix(2, 3, new double[] { 0, 0, 0, 0, 0, 1 });

        Assert.assertTrue(m001.getIsEqual(m002));
        Assert.assertFalse(m001.getIsEqual(m003));
    }
	
	@Test
	public void TestConstructor2()
    {
        Matrix m001 = new Matrix(2, 3);
        Matrix m002 = new Matrix(m001);
        
        Assert.assertTrue(m001.getIsEqual(m002));
    }
		
	@Test
	public void TestPlusMatrix()
    {
        Matrix m001 = new Matrix(2, 3);
        Matrix m002 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m003 = m001.getAddition(m002);
        
        Assert.assertTrue(m002.getIsEqual(m003));
    }
	
	@Test
	public void TestMinusMatrix()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        m001 = m001.getAddition(m002);
        m001 = m001.getSubtraction(m002);
        
        Assert.assertTrue(m001.getIsEqual(m002));
    }
	
	@Test
	public void TestPlusNumeric()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 2, 3, 4, 5, 6, 7 });
        m001 = m001.getAddition(1);
        
        Assert.assertTrue(m001.getIsEqual(m002));
    }
	
	@Test
	public void TestMinusNumeric()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 2, 3, 4, 5, 6, 7 });
        m002 = m002.getSubtraction(1);
        
        Assert.assertTrue(m001.getIsEqual(m002));
    }
	
	@Test
	public void TestPower()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = m001.getDotMultiplication(m001);
        Matrix m003 = m001.getPower(2);
        
        Assert.assertTrue(m002.getIsEqual(m003));
    }
	
	@Test
	public void TestDotDivision()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = m001.getDotMultiplication(m001);
        Matrix m003 = m002.getDotDivision(m001);
        
        Assert.assertTrue(m001.getIsEqual(m003));
    }
	
	@Test
	public void TestGetIndex()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        
        Assert.assertTrue(m001.getValue(1) == m001.getValue(1, 1));
        Assert.assertTrue(m001.getValue(4) == m001.getValue(2, 2));
        Assert.assertTrue(m001.getValue(6) == m001.getValue(2, 3));
    }
	
	@Test
	public void TestGetRows()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = m001.getRows(2, 2);
        Matrix m003 = new Matrix(2, 2, new double[] { 3, 4, 5, 6 });
        
        Assert.assertTrue(m002.getIsEqual(m003));
    }
	
	@Test
	public void TestGetRow()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m003 = m001.getRow(2);
        Matrix m004 = m001.getRow(3);
        Matrix m005 = new Matrix(2, 1, new double[] { 3, 4 });
        Matrix m006 = new Matrix(2, 1, new double[] { 5, 6 });
        
        Assert.assertTrue(m003.getIsEqual(m005));
        Assert.assertTrue(m004.getIsEqual(m006));
    }
	
	@Test
	public void TestStandarddeviation()
    {
        Matrix image = new Matrix(19, 19, new double[]
                {-0.4068,  0.2601,  1.0011,  1.6309,  1.7421,  1.5939,  1.9273,  2.2978,  2.5942,  2.5942,  2.2237,  1.2234,  0.8529,  0.0007, -0.2957,  0.2971, -0.4068, -0.5180, -1.1478,
                 -0.9255, -0.4809,  0.0378,  0.1860, -0.4068, -0.7032,  0.1860,  0.4453,  1.0381,  1.7421,  1.2975, -1.1478, -1.1849, -0.7403, -1.2960, -1.7406, -1.6295, -1.3701, -0.8885,
                 -0.8885, -0.9255, -1.2590, -0.4809, -1.2590, -1.5183, -1.0367,  0.1489, -0.1475,  2.2978,  0.0748, -1.2960, -0.6291, -0.9255, -1.0737, -0.8144, -1.2960, -0.8885, -1.3331,
                 -1.1108, -1.5554, -1.8148,  1.0752, -1.3331, -0.3698, -0.4809, -0.5180, -0.3327,  1.1493, -0.0363, -0.2216,  0.7047,  2.7424, -1.6665, -2.2594, -1.8148, -1.2960, -0.9626,
                 -0.6662, -1.9259, -0.9996, -0.5550, -0.2586, -0.1845, -0.9255, -0.7403, -0.6662,  1.1493,  0.1860, -1.1849, -0.5180,  0.5194, -0.5921, -0.8514, -1.4442, -1.6295, -0.8514,
                 -0.5921, -0.7773, -1.1108, -0.7032, -0.2586, -0.3698, -0.7032, -0.1475, -0.7032,  1.1863,  0.3342, -1.5183, -1.1849, -0.7032, -0.4439, -0.4068, -0.5550, -0.7403, -0.7403,
                  0.5194, -0.3698, -0.0363,  0.3712,  0.2601,  0.4824,  0.1489,  0.5565, -0.6291,  0.8529, -0.1475, -0.7773, -0.4809,  0.2971,  0.7047,  0.4083,  0.5565, -0.4439, -0.4439,
                  1.0752,  0.0378,  0.6676,  1.1493,  1.5568,  1.4827,  0.2601,  0.2971, -0.6662,  0.9270,  0.1489, -0.9996, -0.0734,  0.2971,  1.2234,  1.0381,  0.7047, -0.0734,  0.2230,
                  0.7417,  1.2234,  0.8158,  1.5568,  1.5939,  1.5198,  0.6676, -0.0363, -0.7032,  1.5568,  0.4453, -1.2219,  0.1489,  0.9640,  1.2975,  1.7421,  1.4827,  0.6676,  0.4824,
                  0.5194,  0.7047,  1.1493,  1.6309,  1.8532,  2.3719,  1.3716, -0.4439, -0.0734,  2.2237,  0.5194, -0.9255,  0.4453,  1.8162,  1.7791,  1.5198,  1.3345,  0.7417,  0.4824,
                  0.1860,  0.8158,  0.7417,  1.1122,  1.3716,  2.4831,  1.7050, -0.8144, -0.0363,  2.4831,  1.2604, -0.9255,  0.0378,  2.5201,  1.6680,  1.4457,  1.0381,  0.4824,  0.0748,
                  0.0378,  0.4824,  0.6306,  0.8158,  1.0381,  1.5568,  1.5939, -0.7773, -1.2590,  0.1860, -0.4068, -1.1108, -0.2216,  1.2604,  1.8162,  1.4086,  0.7417,  0.5194,  0.1489,
                  0.2601,  0.5565,  0.5194,  1.0381,  1.5568,  1.2975,  0.8529, -0.3327, -0.8144, -0.3327, -0.2216, -1.1849, -0.6662, -0.0734,  0.7047,  1.5198,  0.8899,  0.6306,  0.1489,
                 -0.1845,  0.3712,  0.4083,  0.8899,  0.7047, -0.1845, -0.8514, -0.7032, -0.0734,  0.1860,  0.1489, -0.2957, -0.8885, -0.8514, -0.7032, -0.2216,  0.5935,  0.4083, -0.2216,
                 -0.6662,  0.1489,  0.5565,  0.4453, -0.5550, -0.8144, -0.2957,  0.3342,  0.9640,  1.0011,  0.7417,  0.3712, -0.4068, -0.5550, -1.1108, -0.7403,  0.0007,  0.5194, -0.7032,
                 -0.9255, -0.1475,  0.4083, -0.1104, -0.6291, -1.1108, -1.5554, -1.3701, -0.8514, -0.2957, -0.8514, -1.4813, -1.8889, -2.0000, -1.5924, -0.9255,  0.3712,  0.2971, -1.1478,
                 -0.7403, -0.8144,  0.2230,  0.2230, -0.8885, -1.0737, -1.5183, -1.7777, -1.1849, -0.9255, -0.9996, -1.0737, -1.0737,  0.0378, -0.1104, -0.3698,  1.0381,  0.0748, -1.4813,
                 -0.9255, -1.1849, -0.1104,  0.4453, -0.3698, -0.2586, -0.1104, -0.2586,  0.2230,  0.2601,  0.0007,  0.0748, -0.5180,  0.3712,  0.0748, -0.2216,  0.6676, -0.4439, -1.5183,
                 -1.4813, -0.9996, -0.9255, -0.7773, -0.2216,  0.6306,  0.2971,  0.0007, -0.0734, -0.2586, -0.1475, -0.4439,  0.2971, -0.1845,  0.1860, -0.4809, -0.2216, -1.0737, -1.6295});

        Assert.assertTrue(1 == Math.round(image.getStandardDeviation()));
    }
	
	@Test
	public void TestMedian()
    {
        Matrix image = new Matrix(19, 19, new double[]
                {-0.4068,  0.2601,  1.0011,  1.6309,  1.7421,  1.5939,  1.9273,  2.2978,  2.5942,  2.5942,  2.2237,  1.2234,  0.8529,  0.0007, -0.2957,  0.2971, -0.4068, -0.5180, -1.1478,
                 -0.9255, -0.4809,  0.0378,  0.1860, -0.4068, -0.7032,  0.1860,  0.4453,  1.0381,  1.7421,  1.2975, -1.1478, -1.1849, -0.7403, -1.2960, -1.7406, -1.6295, -1.3701, -0.8885,
                 -0.8885, -0.9255, -1.2590, -0.4809, -1.2590, -1.5183, -1.0367,  0.1489, -0.1475,  2.2978,  0.0748, -1.2960, -0.6291, -0.9255, -1.0737, -0.8144, -1.2960, -0.8885, -1.3331,
                 -1.1108, -1.5554, -1.8148,  1.0752, -1.3331, -0.3698, -0.4809, -0.5180, -0.3327,  1.1493, -0.0363, -0.2216,  0.7047,  2.7424, -1.6665, -2.2594, -1.8148, -1.2960, -0.9626,
                 -0.6662, -1.9259, -0.9996, -0.5550, -0.2586, -0.1845, -0.9255, -0.7403, -0.6662,  1.1493,  0.1860, -1.1849, -0.5180,  0.5194, -0.5921, -0.8514, -1.4442, -1.6295, -0.8514,
                 -0.5921, -0.7773, -1.1108, -0.7032, -0.2586, -0.3698, -0.7032, -0.1475, -0.7032,  1.1863,  0.3342, -1.5183, -1.1849, -0.7032, -0.4439, -0.4068, -0.5550, -0.7403, -0.7403,
                  0.5194, -0.3698, -0.0363,  0.3712,  0.2601,  0.4824,  0.1489,  0.5565, -0.6291,  0.8529, -0.1475, -0.7773, -0.4809,  0.2971,  0.7047,  0.4083,  0.5565, -0.4439, -0.4439,
                  1.0752,  0.0378,  0.6676,  1.1493,  1.5568,  1.4827,  0.2601,  0.2971, -0.6662,  0.9270,  0.1489, -0.9996, -0.0734,  0.2971,  1.2234,  1.0381,  0.7047, -0.0734,  0.2230,
                  0.7417,  1.2234,  0.8158,  1.5568,  1.5939,  1.5198,  0.6676, -0.0363, -0.7032,  1.5568,  0.4453, -1.2219,  0.1489,  0.9640,  1.2975,  1.7421,  1.4827,  0.6676,  0.4824,
                  0.5194,  0.7047,  1.1493,  1.6309,  1.8532,  2.3719,  1.3716, -0.4439, -0.0734,  2.2237,  0.5194, -0.9255,  0.4453,  1.8162,  1.7791,  1.5198,  1.3345,  0.7417,  0.4824,
                  0.1860,  0.8158,  0.7417,  1.1122,  1.3716,  2.4831,  1.7050, -0.8144, -0.0363,  2.4831,  1.2604, -0.9255,  0.0378,  2.5201,  1.6680,  1.4457,  1.0381,  0.4824,  0.0748,
                  0.0378,  0.4824,  0.6306,  0.8158,  1.0381,  1.5568,  1.5939, -0.7773, -1.2590,  0.1860, -0.4068, -1.1108, -0.2216,  1.2604,  1.8162,  1.4086,  0.7417,  0.5194,  0.1489,
                  0.2601,  0.5565,  0.5194,  1.0381,  1.5568,  1.2975,  0.8529, -0.3327, -0.8144, -0.3327, -0.2216, -1.1849, -0.6662, -0.0734,  0.7047,  1.5198,  0.8899,  0.6306,  0.1489,
                 -0.1845,  0.3712,  0.4083,  0.8899,  0.7047, -0.1845, -0.8514, -0.7032, -0.0734,  0.1860,  0.1489, -0.2957, -0.8885, -0.8514, -0.7032, -0.2216,  0.5935,  0.4083, -0.2216,
                 -0.6662,  0.1489,  0.5565,  0.4453, -0.5550, -0.8144, -0.2957,  0.3342,  0.9640,  1.0011,  0.7417,  0.3712, -0.4068, -0.5550, -1.1108, -0.7403,  0.0007,  0.5194, -0.7032,
                 -0.9255, -0.1475,  0.4083, -0.1104, -0.6291, -1.1108, -1.5554, -1.3701, -0.8514, -0.2957, -0.8514, -1.4813, -1.8889, -2.0000, -1.5924, -0.9255,  0.3712,  0.2971, -1.1478,
                 -0.7403, -0.8144,  0.2230,  0.2230, -0.8885, -1.0737, -1.5183, -1.7777, -1.1849, -0.9255, -0.9996, -1.0737, -1.0737,  0.0378, -0.1104, -0.3698,  1.0381,  0.0748, -1.4813,
                 -0.9255, -1.1849, -0.1104,  0.4453, -0.3698, -0.2586, -0.1104, -0.2586,  0.2230,  0.2601,  0.0007,  0.0748, -0.5180,  0.3712,  0.0748, -0.2216,  0.6676, -0.4439, -1.5183,
                 -1.4813, -0.9996, -0.9255, -0.7773, -0.2216,  0.6306,  0.2971,  0.0007, -0.0734, -0.2586, -0.1475, -0.4439,  0.2971, -0.1845,  0.1860, -0.4809, -0.2216, -1.0737, -1.6295});

        Assert.assertTrue(0 == Math.round(image.getMean()));
    }
	
	@Test
	public void TestMultiplication()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 2, 4, 6, 8, 10, 12 });
        Matrix m003 = m001.getMultiplication(2);
        
        Assert.assertTrue(m002.getIsEqual(m003));
    }

	@Test
	public void TestDivision()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 2, 4, 6, 8, 10, 12 });
        Matrix m003 = m002.getDivision(2);
        
        Assert.assertTrue(m001.getIsEqual(m003));
    }

	@Test
	public void TestSetRow()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 1, 2, 3, 4, 5, 6 });
        Matrix m002 = new Matrix(2, 3, new double[] { 5, 5, 3, 4, 5, 6 });
        m001.setRow(1, new Matrix(2, 1, new double[] { 5, 5 }));
        
        Assert.assertTrue(m002.getIsEqual(m001));
    }
	
	@Test
	public void TestSetRows()
    {
        Matrix m001 = new Matrix(2, 3, new double[] { 5, 5, 5, 5, 5, 5 });
        Matrix m002 = new Matrix(2, 3, new double[] { 5, 5, 1, 2, 3, 4 });
        m001.setRows(2, 2, new Matrix(2, 2, new double[] { 1, 2, 3, 4 }));
        
        Assert.assertTrue(m001.getIsEqual(m002));
    }
	
	@Test
	public void TestMultiplicationMatrix()
    {
        Matrix m001 = new Matrix(3, 4, new double[] { 14, 9, 3, 2, 11, 15, 0, 12, 17, 5, 2, 3 });
        Matrix m002 = new Matrix(2, 3, new double[] { 12, 25, 9, 10, 8, 5 });
        Matrix m003 = new Matrix(2, 4, new double[] { 273, 455, 243, 235, 244, 205, 102, 160 });
        Matrix m004 = m001.getMultiplication(m002);
        
        Assert.assertTrue(m004.getIsEqual(m003));
    }
	
	@Test
	public void TestGetSubMatrix()
    {
        Matrix m001 = new Matrix(2, 2, new double[] { 9, 3, 11, 15 });
        Matrix m002 = new Matrix(3, 4, new double[] { 14, 9, 3, 2, 11, 15, 0, 12, 17, 5, 2, 3 });
        Matrix m003 = m002.getSubMatrix(2, 1, 2, 2);
        
        Assert.assertTrue(m003.getIsEqual(m001));
    }
	
	@Test
	public void TestSetSubMatrix()
    {
        Matrix m001 = new Matrix(2, 2, new double[] { 1, 2, 3, 4 });
        Matrix m002 = new Matrix(3, 4, new double[] { 14, 9, 3, 2, 11, 15, 0, 12, 17, 5, 2, 3 });
        m002.setSubMatrix(2, 1, 2, 2, m001);
        Matrix m003 = m002.getSubMatrix(2, 1, 2, 2);
        
        Assert.assertTrue(m003.getIsEqual(m001));
    }

	@Test
	public void TestRemoveRow()
    {
        Matrix m001 = new Matrix(3, 3, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        Matrix m002 = new Matrix(3, 2, new double[] { 4, 5, 6, 7, 8, 9 });
        Matrix m003 = new Matrix(3, 2, new double[] { 1, 2, 3, 7, 8, 9 });
        Matrix m004 = new Matrix(3, 2, new double[] { 1, 2, 3, 4, 5, 6 });
        
        Matrix m005 = m001.getRemoveRow(1);
        Matrix m006 = m001.getRemoveRow(2);
        Matrix m007 = m001.getRemoveRow(3);
        
        Assert.assertTrue(m005.getIsEqual(m002));
        Assert.assertTrue(m006.getIsEqual(m003));
        Assert.assertTrue(m007.getIsEqual(m004));
    }
	
	@Test
	public void TestMergeMatrix()
    {
        Matrix m001 = new Matrix(3, 1, new double[] { 1, 2, 3 });
        Matrix m002 = new Matrix(3, 2, new double[] { 4, 5, 6, 7, 8, 9 });
        Matrix m003 = new Matrix(3, 3, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        Matrix m004 = new Matrix(m001, m002);
        
        Assert.assertTrue(m003.getIsEqual(m004));
    }
	
	@Test
	public void TestGetSubmatrix()
    {
        Matrix m001 = new Matrix(3, 3, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        Matrix m002 = new Matrix(2, 3, new double[] { 2, 3, 5, 6, 8, 9 });
        Matrix m003 = m001.getSubMatrix(2, 1, 2, 3);

        Assert.assertTrue(m002.getIsEqual(m003));
    }
	
	@Test
	public void TestGetSum()
    {
        Matrix m001 = new Matrix(3, 3, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });

        Assert.assertTrue(m001.getSum() == 45);
    }
	
	@Test
	public void TestEqualsList()
    {
        Matrix m001 = new Matrix(3, 3, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
        Matrix m002 = new Matrix(1, 2, new double[] { 1, 2 });
        Matrix m003 = new Matrix(1, 2, new double[] { 1, 3 });

        Matrix[] am001 = new Matrix[] { m001, m002 };
        Matrix[] am002 = new Matrix[] { m001, m002 };
        Matrix[] am003 = new Matrix[] { m001, m003 };            

        Assert.assertTrue(Matrix.EqualsList(am001, am002));
        Assert.assertFalse(Matrix.EqualsList(am001, am003));
    }
}
