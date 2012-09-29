package utilities;

public class WeakClassifier
{

    public int parity;
    public double error;
    public double threshold;

    public WeakClassifier(int parity, double error, double threshold)
    {
        this.parity = parity;
        this.error = error;
        this.threshold = threshold;
    }
	
}
