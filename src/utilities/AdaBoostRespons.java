package utilities;

public class AdaBoostRespons 
{

    public WeakClassifier weakClassifier;
    public double alpha;
    public FeatureIndex featureIndex;

    public AdaBoostRespons(WeakClassifier weakClassifier, double alpha, FeatureIndex featureIndex)
    {
        this.weakClassifier = weakClassifier;
        this.alpha = alpha;
        this.featureIndex = featureIndex;
    }
	
}
