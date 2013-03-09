package global;

public class Methods {

	public static double[] concat(double[] A, double[] B) 
	{
		   int aLen = A.length;
		   int bLen = B.length;
		   double[] C= new double[aLen+bLen];
		   System.arraycopy(A, 0, C, 0, aLen);
		   System.arraycopy(B, 0, C, aLen, bLen);
		   return C;
	}

}
