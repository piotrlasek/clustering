package org.dmtools.clustering.NBC.DM;

import java.lang.Math;
import java.util.ArrayList;

//import org.dmtools.clustering.NBC.DM.NBCRTreePoint;









import Jama.*;


public class RelativeMatrix {

	Matrix D;
	Matrix D_R;
	public Matrix D_Relative;
	int numberOfDimensions;
	
	private Matrix D_ML;
	private Matrix Lambda;
	private Matrix V;
	
	private Matrix D_CL;
	public Matrix D_Distance;
	
	
	private int[] index;
	
	private int t = 2;
	double alpha = 0.7;
	
	double d_min;
	double d_max;
	
	public RelativeMatrix(ArrayList<double[]> data, InstanceConstraints constraints, int nod)
	{
		this.numberOfDimensions = nod;
		//this.t = (int)2 * (data.size()/3);
		System.out.println("t =" +  this.t);
		
		D = new Matrix(data.size(), data.size());
		//System.out.println("size= " + data.size());
		
		for(int i =0; i < data.size(); i++)
		{
			for(int j = 0; j<data.size();j++)
			{
				double dim = getDimension(data.get(i), data.get(j));
				D.set(i,  j, dim);
				
				if(d_min == 0)
					d_min = dim;
				else if(dim > 0 && dim < d_min)
				{
					d_min = dim;
					//System.out.println("d_min= " +  d_min);
				}
				
				if(d_max < dim)
					d_max = dim;
			}
		}
		
		getDiffusionMap(D);
		
		System.out.println("d_min = " + d_min + "; d_max = " + d_max);
		
		int[][] relative = new int[constraints.r1.size()][3];
		System.out.println("cannotlink.length = " + relative.length);
		for(int i = 0; i < relative.length; i++)
		{
			relative[i][0] = (constraints.r1.get(i));
			relative[i][1] = (constraints.r2.get(i));
			relative[i][2] = (constraints.r3.get(i));
		}
		
		getRelativeDistance(relative);
		//D.timesEquals(0.5);
		D_R.timesEquals(400);
		
		for(int ii =0; ii < D.getRowDimension(); ii++)
		{
			for (int jj = 0; jj < D.getColumnDimension(); jj++)
			{
				double elem = D.get(ii, jj);
				elem = elem * elem;
				D.set(ii, jj, elem);
			}
		}
		
		D_Relative = D.plus(D_R);
		
		for(int ii =0; ii < D_Relative.getRowDimension(); ii++)
		{
			for (int jj = 0; jj < D_Relative.getColumnDimension(); jj++)
			{
				double elem = D_Relative.get(ii, jj);
				elem = Math.sqrt(elem);
				D_Relative.set(ii, jj, elem);
			}
		}
		
		
		
		//D_Relative = D_R;
		
	}
	
	void getRelativeDistance(int[][] relative)
	{
		D_R = new Matrix(D.getRowDimension(), D.getColumnDimension());
		
		for (int i = 0; i < relative.length; i++)
		{
			int a_point = relative[i][0];
			int b_point = relative[i][1];
			int c_point = relative[i][2];
			
			for(int x = 0; x < D_R.getRowDimension(); x ++)
			{
				for(int y = 0; y < D_R.getColumnDimension(); y++)
				{
					double dimx = getDistance(x, a_point, b_point, c_point);
					double dimy = getDistance(y, a_point, b_point, c_point); 
					
					double dim = D_R.get(x, y);
					dim +=  (dimx-dimy);
					dim = dim * dim;
					D_R.set(x, y, dim);
				}
			}
		}
	}
	
	
	double getDistance(int x, int a, int b, int c)
	{
		double PhiXA = 0;
		double PhiXB = 0;
		double PhiXC = 0;
		
		int ind;
		
		for (int i =0; i < t; i++)
		{
			ind = this.index[i];
			
			PhiXA += Math.pow((Lambda.get(ind, 0)*V.get(x, ind) - Lambda.get(ind, 0)*V.get(a, ind)), 2);
			PhiXB += Math.pow((Lambda.get(ind, 0)*V.get(x, ind) - Lambda.get(ind, 0)*V.get(b, ind)), 2);
			PhiXC += Math.pow((this.Lambda.get(ind,  0)*this.V.get(x,  ind) - this.Lambda.get(ind,  0)*this.V.get(c, ind)), 2);
		}
		
		PhiXA = Math.sqrt(PhiXA);
		PhiXB = Math.sqrt(PhiXB);
		PhiXC = Math.sqrt(PhiXC);
		
		//dim = (PhiXA - PhiXB)/(PhiXA + PhiXB);
		
		
		double PhiXAB = PhiXA < PhiXB ? PhiXA : PhiXA;
		return Math.abs((PhiXC - PhiXAB)/(PhiXC+PhiXAB));		
	}
	
	double  getDimension(double[] elem1, double[] elem2)
	{
		double dim = 0;
		int l = elem1.length < numberOfDimensions ? elem1.length : numberOfDimensions;
		
		for (int i =0; i < l; i++)
		{
			dim += Math.pow(elem1[i] - elem2[i], 2);
		}
		
		return Math.sqrt(dim);
	}
	
	/**
	 * 
	 * @param dim
	 */
	private void getDiffusionMap(Matrix dim)
	{
		Matrix A = new Matrix(dim.getRowDimension(), dim.getColumnDimension());
		
		
		//double alpha = 0.7;
		double length;
		double value;
		
		double value_sum = .0;
		double d[] = new double[A.getColumnDimension()];
		//double dx = .0;
		
		for (int x = 0; x < A.getRowDimension(); x++)
		{
			for (int y=0; y < A.getColumnDimension(); y ++)
			{
				length = Math.pow(dim.get(x, y),2);
				value = Math.exp(-length/alpha);
				A.set(x, y, value);
				//System.out.println("A[" + x + "," + y + "] =" + value);
				value_sum += value;
				d[x] += value;
			}				
		}
		
		System.out.println("Value_sum = " + value_sum);
		//Matrix A_prim = A.times(10000);
		//MatrixUtils.print(A_prim, "A -diffuion map");
		
		for (int x = 0; x < A.getRowDimension(); x++)
		{
			for (int y=0; y < A.getColumnDimension(); y ++)
			{
				double valueA = A.get(x, y);
				A.set(x,  y, valueA/value_sum);
			}
		}
		
		Matrix Diag = new Matrix (d.length, d.length);
		//for (int x = 0; x < Diag.getColumnDimension(); x++)
		//{
		//	Diag.set(x, x, d[x]);
		//	System.out.println("Diag[" + x + "," + x + "] = " + d[x]);			
		//}
		
		for (int x = 0; x < Diag.getColumnDimension(); x++)
		{
			Diag.set(x, x, 1/d[x]);
			//System.out.println("Diag[" + x + "," + x + "] = " + 1/d[x]);			
		}
		
		//
		//MatrixUtils.print(A, "Diffusion Map Matrix A");
		Matrix DiagInverse = Diag.inverse();
		//MatrixUtils.print(Diag, "Diffusion Map Matrix Diag");
		//MatrixUtils.print(DiagInverse, "Diag^-1");
		Matrix P = DiagInverse.times(A);
		//Matrix P_prim = P.times(10000);
		//MatrixUtils.print(P, "Diffusion Map Matrix P");
		//System.out.println("P size = (" + P.getRowDimension() + "," + P.getColumnDimension() + ")");
		
		
		EigenvalueDecomposition ed = new EigenvalueDecomposition(P);
		
		//Matrix diagonaleigenvaluematrix = ed.getD();
		//MatrixUtils.print(diagonaleigenvaluematrix, "diagonal eigenvalue matrix");
		
		double [] lambda = ed.getRealEigenvalues();
		this.V = ed.getV();
		
		this.index = sortEigenvalues(lambda);
				
		//System.out.println("lambda size = (" + lambda.length + ")");
		//System.out.println("V size = (" + this.V.getRowDimension() + "," + this.V.getColumnDimension() + ")");
		
		//V.print(2, 3);
		
		this.Lambda =  new Matrix(lambda, lambda.length);
		//MatrixUtils.print(this.Lambda, "Lambda");
		
	}
	
	private int[] sortEigenvalues(double [] lambda)
	{
		double [] tosort = new double[lambda.length];
		int [] index = new int[lambda.length];
		for (int i =0; i < index.length; i++)
		{	
			index[i] = i;
			tosort[i] = lambda[i];
		}
		
		int size = tosort.length - 1;
        for (int i = 0; i < tosort.length - 1; i++) {
            for (int j = 0; j < size; j++) {
                if (tosort[j] < tosort[j + 1]) {
                    double temp = tosort[j];
                    tosort[j] = tosort[j + 1];
                    tosort[j + 1] = temp;
                    
                    int temp1 = index[j];
                    index[j] = index [j+1];
                    index[j+1]= temp1;
                }
            }
            size--;
        }
        
		return index;
		
	}
	
}
