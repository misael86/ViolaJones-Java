package models;

import java.io.Serializable;

import global.Enumerators.FeatureType;

public class FeatureIndex implements Serializable
{
	private static final long serialVersionUID = 1L;
	public FeatureType featureType;
    public int j;

    public FeatureIndex(FeatureType featureType, int j)
    {
        this.featureType = featureType;
        this.j = j;
    }
	
}
