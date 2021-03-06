package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import global.Strings;

public class Matrix implements Serializable
{

	/*
		V A R I A B L E S
	*/
	
	private static final long serialVersionUID = 1L;

	private int nrRows;
	
	private int nrCols;
	
	private double[] data;

	/*
		C O N S T R U C T O R S
	*/

	public Matrix() 
	{

	}

	// TESTED
	public Matrix(int nrCols, int nrRows)
	{
		this.setSize(nrCols, nrRows);
	}

	// TESTED
	public Matrix(int nrCols, int nrRows, double[] data)
	{
		this.setSize(nrCols, nrRows);
		this.setData(data);
	}

	// TESTED
	public Matrix(Matrix m)
	{
		this.setSize(m.getNrCols(), m.getNrRows());
		this.setData(m.getData());	
	}

	// TESTED
	public Matrix(Matrix m1, Matrix m2)
    {
        if (m1.nrCols != m2.nrCols)
        {
			String errorMessage = "Error at Matrix(Matrix, Matrix)" + Strings.newline +
					"Matrix 1 nr columns: " + m1.nrCols + Strings.newline +
					"Matrix 2 nr columns: " + m2.nrCols;
			
			throw new RuntimeException(errorMessage);
		}

        this.nrCols = m1.nrCols;
        this.nrRows = m1.nrRows + m2.nrRows;
        this.data = new double[this.getNrVals()];
        this.setData(m1.data, m2.data);
    }
	
	/*
		S E T
	*/

	// TESTED
	private void setSize(int nrCols, int nrRows)
	{
		if (nrCols < 1 || nrRows < 1)
		{
			String errorMessage = "Error at Matrix.setSize" + Strings.newline +
					"Columns: " + nrCols + Strings.newline +
					"Rows: " + nrRows;

			throw new RuntimeException(errorMessage);
		}

		this.nrCols = nrCols;
		this.nrRows = nrRows;
		this.data = new double[this.getNrVals()];
	}

	// TESTED
	private void setData(double[] data)
	{		
		if(this.data.length != data.length)
		{
			String errorMessage = "Error at Matrix.setData" + Strings.newline +
					"data in: " + data.length + Strings.newline +
					"data: " + this.data.length;

			throw new RuntimeException(errorMessage);

		}

		System.arraycopy(data, 0, this.data, 0, this.getNrVals());	
	}

	// TESTED
	private void setData(double[] data1, double[] data2)
	{
		this.data = new double[data1.length + data2.length];
		System.arraycopy(data1, 0, this.data, 0, data1.length);
		System.arraycopy(data2, 0, this.data, data1.length, data2.length);
	}

	// TESTED
	public void setValue(int index, double value)
	{
        if (index > this.getNrVals() || index < 1)
        {
        	String errorMessage = "Error at Matrix.setValue" + Strings.newline +
					"Index: " + index + Strings.newline +
					"Nr values: " + this.getNrVals();
			
			throw new RuntimeException(errorMessage);
        }

        this.data[index - 1] = value;
	}

	// TESTED
	public void setValue(int colNr, int rowNr, double value)
	{
		if (colNr < 1 || colNr > this.nrCols || rowNr < 1 || rowNr > this.nrRows)
		{
			String errorMessage = "Error at Matrix.setValue" + Strings.newline +
					"Column: " + colNr + Strings.newline +
					"Row: " + rowNr + Strings.newline +
					"Nr columns: " + this.nrCols + Strings.newline +
					"Nr rows: " + this.nrRows;
			
			throw new RuntimeException(errorMessage);
		}


		this.data[(rowNr-1) * this.nrCols + colNr - 1] = value;
	}
	
	// TESTED
	public void setSeveralValues(int startIndex, int endIndex, double value) 
	{
		if (0 >= startIndex || startIndex >= endIndex || endIndex > this.getNrVals())
		{
			String errorMessage = "Error at Matrix.setSeveralValues" + Strings.newline +
					"Start index: " + startIndex + Strings.newline +
					"End index: " + endIndex + Strings.newline +
					"Nr vals: " + this.getNrVals();
			
			throw new RuntimeException(errorMessage);
		}
		
		for(int i = startIndex; i <= endIndex; i++) {
			this.setValue(i, value);
		}
	}
	
	// TESTED
	public void setRow(int row, Matrix value)
    {
        if (row < 1 || row > this.nrRows)
        {
			String errorMessage = "Error at Matrix.setRow" + Strings.newline +
					"Nr Rows: " + this.nrRows + Strings.newline +
					"Row: " + row;
			
			throw new RuntimeException(errorMessage);
		}

        int startIndex = (row - 1) * this.nrCols;
        int copyLenght = this.nrCols;

        System.arraycopy(value.data, 0, this.data, startIndex, copyLenght);
    }

	// TESTED
	public void setRows(int rowStart, int nrRows, Matrix value)
    {
        if (rowStart < 1 || nrRows < 1 || rowStart + nrRows - 1 > this.nrRows)
        {
        	String errorMessage = "Error at Matrix.setRows" + Strings.newline +
        			"Unmatching dimensions" + Strings.newline +
        			"Nr rows: " + this.nrRows + Strings.newline +
        			"Row start: " + rowStart + Strings.newline +
        			"Nr rows to set: " + nrRows;
        	
                	throw new RuntimeException(errorMessage);
        }

        int startIndex = (rowStart - 1) * this.nrCols;
        int copyLenght = nrRows * this.nrCols;

        System.arraycopy(value.data, 0, this.data, startIndex, copyLenght);
    }
	
	// TESTED
	public void setSubMatrix(int colNr, int rowNr, int width, int height, Matrix value)
	{
		if (colNr < 1 || colNr > this.nrCols ||
        	rowNr < 1 || rowNr > this.nrRows ||
        	width < 1 || width + colNr - 1 > nrCols ||
        	height < 1 || height + rowNr - 1 > nrRows)
        {
        	String errorMessage = "Error at Matrix.setSubMatrix" + Strings.newline +
					"Column: " + colNr + Strings.newline +
					"Row: " + rowNr + Strings.newline +
					"Width: " + width + Strings.newline +
					"Height: " + height + Strings.newline +
					"Nr columns: " + this.nrCols + Strings.newline +
					"Nr rows: " + this.nrRows;
			
			throw new RuntimeException(errorMessage);
        }
		
		int col, row;
        for (col = colNr; col < colNr + width; col++)
        {
            for (row = rowNr; row < rowNr + height; row++)
            {  
            	this.setValue(col, row, value.getValue(col - colNr + 1, row - rowNr + 1));
            }
        }
	}
	
	/*
		G E T
	*/

	// TESTED
	public int getNrRows()
	{
		return this.nrRows;
	}

	// TESTED
	public int getNrCols()
	{
		return this.nrCols;
	}

	// TESTED
	public int getNrVals()
	{
		return this.nrCols * this.nrRows;
	}

	// TESTED
	public double[] getData()
	{
		return this.data;
	}

	// TESTED
	public double getValue(int index)
	{
        if (index > this.getNrVals() || index < 1)
        {
        	String errorMessage = "Error at Matrix.getValue" + Strings.newline +
					"Index: " + index + Strings.newline +
					"Nr values: " + this.getNrVals();
			
			throw new RuntimeException(errorMessage);
        }

        return this.data[index - 1];
	}
	
	// TESTED
	public double getValue(int colNr, int rowNr)
	{
		if (colNr < 1 || colNr > this.nrCols || rowNr < 1 || rowNr > this.nrRows)
		{
			String errorMessage = "Error at Matrix.getValue" + Strings.newline +
					"Column: " + colNr + Strings.newline +
					"Row: " + rowNr + Strings.newline +
					"Nr columns: " + this.nrCols + Strings.newline +
					"Nr rows: " + this.nrRows;
			
			throw new RuntimeException(errorMessage);
		}

		return this.data[(rowNr - 1) * this.nrCols + colNr - 1];
	}

	// TESTED
	public double getStandardDeviation()
    {
        if (this.getNrVals() - 1 == 0)
        {
        	String errorMessage = "Error at Matrix.getStandardDeviation" + Strings.newline +
					"Division by zero";

			throw new RuntimeException(errorMessage);
        }
        
        double result = this.getSubtraction(this.getMean()).getPower(2).getSum() / (double)(this.getNrVals() - 1);
        return Math.sqrt(result);
    }
	
	// TESTED
	public double getMaxValue()
    {
		double max = Double.NEGATIVE_INFINITY;
		for(int i = 0; i < this.data.length; i++)
		{
			if(this.data[i] > max)
			{
				max = this.data[i];
			}
		}
		
        return max;
    }
	
	// TESTED
	public double getMinValue()
    {
        double min = Double.MAX_VALUE;
        for(int i = 0; i < this.data.length; i++)
        {
        	if(this.data[i] < min)
        	{
        		min = this.data[i];
        	}
        }
        
        return min;
    }
	
	// TESTED
	public double getMean()
    {
        if (this.getNrVals() == 0)
        {
        	String errorMessage = "Error at Matrix.getStandardDeviation" + Strings.newline +
					"Division by zero";

			throw new RuntimeException(errorMessage);
        }

        return this.getSum() / (double)this.getNrVals();
    }
	
	// TESTED
	public double getSum()
    {
		double sum = 0;
		for(int i = 0; i < this.data.length; i++)
		{
			sum += this.data[i];
		}
		
        return sum;
    }
	
	// TESTED
	public boolean getIsRow()
	{
		return this.nrRows == 1;
	}

	// TESTED
	public boolean getIsEqual(Matrix m)
	{	
		boolean cols = this.nrCols == m.nrCols;
        boolean rows = this.nrRows == m.nrRows;
        
        if (!(cols && rows)) { return false; }

        boolean data = true;
        for (int i = 1; i <= this.getNrVals(); i++)
        {
            if (this.getValue(i) != m.getValue(i))
            {           	
                data = false;
                break;
            }
        }

        return data;
	}
	
	// TESTED
	public static boolean EqualsList(Matrix[] matrixList1, Matrix[] matrixList2)
    {
        if (matrixList1.length != matrixList2.length) { return false; }

        for (int i = 0; i < matrixList1.length; i++)
        {
            if (!matrixList1[i].getIsEqual(matrixList2[i])) 
            { 
            	return false; 
            }
        }

        return true;
    }
	
	// TESTED
	public Matrix getAbsMatrix()
	{
		Matrix result = new Matrix(this.nrCols, this.nrRows);
		for (int i = 0; i < this.getNrVals(); i++)
		{
			result.data[i] = Math.abs(this.data[i]);
		}

		return result;
	}

	// TESTED
	public Matrix getDotMultiplication(Matrix m)
    {
        if (this.nrCols != m.nrCols || this.nrRows != m.nrRows)
        {
        	String errorMessage = "Error at Matrix.dotMultiplication" + Strings.newline +
        			"Unmatching dimensions" + Strings.newline +
        			"Matrix 1: " + this.nrRows + "x" + this.nrCols + Strings.newline +
        			"Matrix 2: " + m.nrRows + "x" + m.nrCols;
			
			throw new RuntimeException(errorMessage);
        }
            
        double[] data = new double[this.getNrVals()];
        for (int i = 0; i < this.getNrVals(); i++)
        {
            data[i] = this.data[i] * m.data[i];
        }

        return new Matrix(this.nrCols, m.nrRows, data);
    }
	
	// TESTED	
	public Matrix getDotDivision(Matrix m)
    {
        if (this.nrCols != m.nrCols || this.nrRows != m.nrRows)
        {
        	String errorMessage = "Error at Matrix.dotDivision" + Strings.newline +
        			"Unmatching dimensions" + Strings.newline +
        			"Matrix 1: " + this.nrRows + "x" + this.nrCols + Strings.newline +
        			"Matrix 2: " + m.nrRows + "x" + m.nrCols;
			
			throw new RuntimeException(errorMessage);
        }

        double[] data = new double[this.getNrVals()];
        for (int i = 0; i < this.getNrVals(); i++)
        {
            data[i] = this.data[i] / m.data[i];
        }

        return new Matrix(this.nrCols, this.nrRows, data);
    }
	
	// TESTED
	public Matrix getRoundedMatrix(int numberOfDecimals)
    {
        Matrix result = new Matrix(this.nrCols, this.nrRows);
        for (int i = 0; i < this.getNrVals(); i++)
        {
        	result.data[i] = new BigDecimal(this.data[i]).setScale(numberOfDecimals, RoundingMode.HALF_UP).doubleValue();
        }

        return result;
    }
	
	// TESTED
	public Matrix getRow(int row)
    {
		if (row < 1 || row > this.nrRows)
        {
        	String errorMessage = "Error at Matrix.getRow" + Strings.newline +
					"Row: " + row + Strings.newline +
					"Nr rows: " + this.nrRows;
			
			throw new RuntimeException(errorMessage);
        }

        int startIndex = (row - 1) * this.nrCols;
        int copyLenght = this.nrCols;

        double[] result = new double[copyLenght];
        
        System.arraycopy(this.data, startIndex, result, 0, copyLenght);
        
        return new Matrix(this.nrCols, 1, result);
    }
	
	// TESTED
	public Matrix getRows(int rowStart, int nrRows)
    {
        if (rowStart < 1 || nrRows < 1 || rowStart + nrRows - 1 > this.nrRows)
        {
        	String errorMessage = "Error at Matrix.getRows" + Strings.newline +
			"Unmatching dimensions" + Strings.newline +
			"Nr rows: " + this.nrRows + Strings.newline +
			"Row start: " + rowStart + Strings.newline +
			"Nr rows to get: " + nrRows;
	
        	throw new RuntimeException(errorMessage);
        }

        int startIndex = (rowStart - 1) * this.nrCols;
        int copyLenght = nrRows * this.nrCols;

        double[] result = new double[copyLenght];
        System.arraycopy(this.data, startIndex, result, 0, copyLenght);

        return new Matrix(this.nrCols, nrRows, result);
    }
	
	// TESTED
	public Matrix getRescale(double minValue, double maxValue) 
	{	
		// ----------------------------------------------------------------------- //
		if(minValue == maxValue) { throw new IllegalArgumentException("Min == Max"); }
		// ----------------------------------------------------------------------- //
		
		double max = this.getMaxValue();
	    double min = this.getMinValue();
	    double div = max - min;
	    
		if(div == 0) { return new Matrix(this.nrCols, this.nrRows); }
	    
		Matrix result = new Matrix(this);
		result = result.getSubtraction(min);
		result = result.getDivision(div);
		result = result.getMultiplication(maxValue - minValue);
		result = result.getAddition(minValue);
		
		return result;
	}
	
	// TESTED
	public Matrix getNormal()
    {
        double std = this.getStandardDeviation();
        double mean = this.getMean();

        return std == 0 ? this.getSubtraction(mean) : this.getSubtraction(mean).getDivision(std);
    }
	
	// TESTED
	public Matrix getSubtraction(double val)
	{
        Matrix result = new Matrix(this);

        if (val == 0)
        {
        	return result;
        }

        for (int i = 0; i < result.getNrVals(); i++)
        {
            result.data[i] -= val;
        }

        return result;
    }
	
	// TESTED
	public Matrix getSubtraction(Matrix m)
	{
		if (this.nrCols != m.nrCols || this.nrRows != m.nrRows)
        {
			String errorMessage = "Error at Matrix.getSubtraction" + Strings.newline +
					"Unmatching dimensions";

			throw new RuntimeException(errorMessage);
        }

        double[] data = new double[m.getNrVals()];
        for (int i = 0; i < m.getNrVals(); i++)
        {
        	data[i] = this.data[i] - m.data[i];
        }

        return new Matrix(m.nrCols, m.nrRows, data);
	}
	
	// TESTED
	public Matrix getAddition(double val)
	{
        Matrix result = new Matrix(this);

        if (val == 0)
        {
        	return result;
        }

        for (int i = 0; i < this.getNrVals(); i++)
        {
            result.data[i] += val;
        }

        return result;
    }
	
	// TESTED
	public Matrix getAddition(Matrix m)
	{
		if (this.nrCols != m.nrCols || this.nrRows != m.nrRows)
        {
			String errorMessage = "Error at Matrix.getAddition" + Strings.newline +
					"Unmatching dimensions";

			throw new RuntimeException(errorMessage);
        }

        double[] data = new double[m.getNrVals()];
        for (int i = 0; i < m.getNrVals(); i++)
        {
        	data[i] = m.data[i] + this.data[i];
        }

        return new Matrix(m.nrCols, m.nrRows, data);
	}

	// TESTED
	public Matrix getDivision(double val)
    {
		if (val == 0)
		{
			String errorMessage = "Error at Matrix.getMatrixDivision" + Strings.newline +
					"Division by zero";

			throw new RuntimeException(errorMessage);
		}

        Matrix result = new Matrix(this.nrCols, this.nrRows, this.data);

        if (val == 1)
        {
            return result;
        }

        for (int i = 0; i < this.getNrVals(); i++)
        {
            result.data[i] /= val;
        }

        return result;
    }
	
	// TESTED
	public Matrix getPower(double val)
    {
        double[] result = new double[this.getNrVals()];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = Math.pow(this.data[i], val);
        }

        return new Matrix(this.nrCols, this.nrRows, result);
    }
	
	// TESTED
	public Matrix getSubMatrix(int colNr, int rowNr, int width, int height)
    {
		if (colNr < 1 || colNr > this.nrCols ||
        	rowNr < 1 || rowNr > this.nrRows ||
        	width < 1 || width + colNr - 1 > nrCols ||
        	height < 1 || height + rowNr - 1 > nrRows)
        {
        	String errorMessage = "Error at Matrix.getSubMatrix" + Strings.newline +
					"Column: " + (colNr + 1) + Strings.newline +
					"Row: " + (rowNr + 1) + Strings.newline +
					"Width: " + width + Strings.newline +
					"Height: " + height + Strings.newline +
					"Nr columns: " + this.nrCols + Strings.newline +
					"Nr rows: " + this.nrRows;
			
			throw new RuntimeException(errorMessage);
        }

        Matrix result = new Matrix(width, height);

        int col, row;
        for (col = colNr; col < colNr + width; col++)
        {
            for (row = rowNr; row < rowNr + height; row++)
            {           	
                result.setValue(col - colNr + 1, row - rowNr + 1, this.getValue(col, row));
            }
        }

        return result;
    }
	
	// TESTED
	public Matrix getMergedMatrixes(Matrix m)
	{
		return new Matrix(this, m);
	}

	// TESTED
	public Matrix getMultiplication(Matrix m)
    {
        int i, j, k;

        if (this.nrCols != m.nrRows)
        {
        	String errorMessage = "Error at Matrix.getMultiplication" + Strings.newline +
					"Unmatching dimensions";

			throw new RuntimeException(errorMessage);
        }

        Matrix resultMatrix = new Matrix(m.nrCols, this.nrRows);

        for (i = 1; i <= this.nrRows; i++)
        {
            for (j = 1; j <= m.nrCols; j++)
            {
                double sum = 0;
                for (k = 1; k <= this.nrCols; k++)
                {
                    sum += this.getValue(k, i) * m.getValue(j, k);
                }
                resultMatrix.setValue(j, i, sum);
            }
        }

        return resultMatrix;
    }
	
	// TESTED
	public Matrix getMultiplication(double val)
    {
        if (val == 0)
        {
            return new Matrix(this.nrCols, this.nrRows, new double[this.getNrVals()]);
        }
        else if (val == 1)
        {
        	return new Matrix(this);
        }
        else
        {
        	Matrix result = new Matrix(this);
	        for (int i = 0; i < this.getNrVals(); i++)
	        {
	            result.data[i] *= val;
	        }
	
	        return result;
        }
    }
	
	// TESTED
	public Matrix getRemoveRow(int rowNr)
	{
        if (rowNr > this.nrRows || rowNr < 1)
        {
        	String errorMessage = "Error at Matrix.getRemoveRow" + Strings.newline +
					"Unmatching dimensions" + Strings.newline +
					"Remove row: " + rowNr + Strings.newline +
					"Nr rows: " + this.nrRows;

			throw new RuntimeException(errorMessage);
        }

        Matrix result = new Matrix(this.nrCols, this.nrRows - 1);

        if (rowNr > 1)
        {
        	result.setRows(1, rowNr - 1, this.getRows(1, rowNr - 1));
        }
        if (rowNr < this.nrRows)
        {
        	result.setRows(rowNr, result.nrRows - rowNr + 1, this.getRows(rowNr + 1, this.nrRows - rowNr));
        }

        return result;
    }
	
	// TESTED
	public void getPrintOut()
	{
		System.out.println("Matrix: " + this.nrCols + "x" + this.nrRows);
		for (int y = 1; y <= this.nrRows; y++)
        {
			for (int x = 1; x <= this.nrCols; x++)
        	{
				if (this.getValue(x, y) > 0) { System.out.print(" "); }
                System.out.print(this.getValue(x, y) + " \t");
            }
            System.out.println();
        }
    }

	// TESTED
	public static Matrix[] getJoinedLists(Matrix[] m1, Matrix[] m2)
	{
		Matrix[] result = new Matrix[m1.length + m2.length];
		
		for(int i = 0; i < m1.length; i++)
		{
			result[i] = m1[i];
		}
		for(int i = 0; i < m2.length; i++)
		{
			result[m1.length + i] = m2[i];
		}
		
		return result;
	}
	
	/*
		SAVE & LOAD
	*/
	
	// TESTED
	public static void saveMatrix(Matrix matrix, String fileName) 
    {
		fileName = "data/" + fileName;
		
	    try 
	    {
		    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		    objectOutputStream.writeObject(matrix);
		    objectOutputStream.flush();
		    objectOutputStream.close();
	        fileOutputStream.close();
	    } 
	    catch(Exception e)
	    {
        	String errorMessage = "Error at Matrix.saveMatrix" + Strings.newline +
					"Filename: " + fileName + Strings.newline +
					"Message: " + e.getMessage() + Strings.newline +
					e.getStackTrace();

			throw new RuntimeException(errorMessage);
        }
	}
	
	// TESTED
    @SuppressWarnings("unchecked")
	public static <T extends Matrix> T loadMatrix( String fileName ) 
    {
        T result;
        
        fileName = "data/" + fileName;
        
        try 
        {
        	FileInputStream FileInputStream = new FileInputStream(fileName);
            ObjectInputStream ObjectInputStream = new ObjectInputStream(FileInputStream);
            
            result = (T)ObjectInputStream.readObject();
            
            if( ObjectInputStream.available() !=  0 ) 
            {
            	System.err.println("File not completely read?"); 
    			System.exit(0);
            }
            
            ObjectInputStream.close();
            return (T) result;
        } 
        catch(Exception e)
	    {
        	String errorMessage = "Error at Matrix.loadMatrix" + Strings.newline +
					"Filename: " + fileName + Strings.newline +
					"Message: " + e.getMessage() + Strings.newline +
					e.getStackTrace();

			throw new RuntimeException(errorMessage);
        }
    }
	
}
