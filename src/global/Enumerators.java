package global;

public class Enumerators {

	public static enum DataSet 
	{
		pFace, 
		nFace,
		test
	}
	
	public static enum FeatureType
    {
        type1(1),
        type2(2),
        type3(3),
        type4(4);
        
        private final int _value;
        private FeatureType(int value) 
        {
            this._value = value;
        }
        
        public int getValue()
        {
        	return this._value;
        }
    }
}
