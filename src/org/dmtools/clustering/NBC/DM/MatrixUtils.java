package org.dmtools.clustering.NBC.DM;

import Jama.*;

public class MatrixUtils {

	static public void print(Matrix M, String name)
	{
		System.out.println("Matrix " + name);
		for (int i = 0; i < M.getRowDimension(); i++)
		{
			for (int j = 0; j <M.getColumnDimension(); j++)
			{
				System.out.print(String.format("%.3f ", M.get(i,j)));
				
			}
			System.out.println("");
		}
	}
}
