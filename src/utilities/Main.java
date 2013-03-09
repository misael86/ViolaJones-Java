package utilities;

import models.AdaBoostRespons;
import models.RandomImageListRespons;
import global.Enumerators.DataSet;

public class Main 
{
	public static void main(String[] args) 
	{
		int pFaces = 3000;
		int nFaces = 5000;
		int T = 200;
		
		String[] pFacesList = Data.getImageList(DataSet.pFace);
		String[] nFacesList = Data.getImageList(DataSet.nFace);
		
		RandomImageListRespons pFacesRespons = Data.getRandomImageList(pFacesList, pFaces);
		RandomImageListRespons nFacesRespons = Data.getRandomImageList(nFacesList, nFaces);
		
        Matrix[] pImages = Data.getNormalisedImageMatrixList(pFacesRespons.learnImages, DataSet.pFace);
        Matrix[] nImages = Data.getNormalisedImageMatrixList(nFacesRespons.learnImages, DataSet.nFace);
        
        Matrix[] posIntegralImages = Image.getIntegralImages(pImages);
        Matrix[] negIntegralImages = Image.getIntegralImages(nImages);
        
        Matrix[] integralImages = Matrix.getJoinedLists(posIntegralImages, negIntegralImages);
        
        Matrix isFaceList = AdaBoost.getInitializedIsFaceList(posIntegralImages.length, negIntegralImages.length);
        
        Feature[] allFeatures = Feature.getAllFeatures(19, 19);
        
		AdaBoostRespons[] weakClassifiers = AdaBoost.executeAdaBoost(integralImages, allFeatures, isFaceList, nFaces, T);
		
		
		
		
		pImages = Data.getNormalisedImageMatrixList(pFacesRespons.testImages, DataSet.pFace);
        nImages = Data.getNormalisedImageMatrixList(nFacesRespons.testImages, DataSet.nFace);
        
        posIntegralImages = Image.getIntegralImages(pImages);
        negIntegralImages = Image.getIntegralImages(nImages);
		
	 	double threshold = AdaBoost.computeROC2(weakClassifiers, posIntegralImages, negIntegralImages);

		AdaBoost.findFaces(weakClassifiers, threshold, DataSet.test, "many_small_chris.png", false);		
	}
}
