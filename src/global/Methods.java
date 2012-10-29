package global;

import java.util.Arrays;

import utilities.Matrix;

public class Methods 
{

	public static double[] concat(double[] first, double[] second) 
	{
		double[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static Matrix[] concat(Matrix[] first, Matrix[] second) 
	{
		Matrix[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
}
