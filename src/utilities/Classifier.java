package utilities;

public class Classifier 
{

    public double[] alphas;
    public double[] thetas;
    public Feature[] features;
    public Feature[] all_features;

    public Classifier(double[] alphas, double[] thetas, Feature[] features, Feature[] all_features)
    {
        this.alphas = alphas;
        this.thetas = thetas;
        this.features = features;
        this.all_features = all_features;
    }
	
}
