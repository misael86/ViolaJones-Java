package models;

import java.io.Serializable;

public class WeakClassifier implements Serializable
{

	private static final long serialVersionUID = 1L;
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
