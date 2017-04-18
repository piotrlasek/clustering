package org.dmtools.clustering.algorithm.NBC.DM;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.util.ArrayList;

//import org.dmtools.clustering.algorithm.NBC.DM.NBCRTreePoint;



public class DimensionsMatrix {
	
	private Matrix D;
	private Matrix D_ML;
	private Matrix Lambda;
	private Matrix V;
	
	private Matrix D_CL;
	public Matrix D_Distance;
	int numberOfDimensions;
	
	private double d_min;
	private double d_max;
	
	private int[] index;
	
	private int t = 5;
	double alpha = 0.7;

	/**
	 * 
	 * @param data
	 * @param constraints
	 * @param nod
	 */
	public DimensionsMatrix(ArrayList<double[]> data,
			InstanceConstraints constraints, int nod)
	{
		this.numberOfDimensions = nod;
		//this.t = (int)2 * (data.size()/3);
		System.out.println("t =" +  this.t);
		
		this.D = createD(data);
		
		//MatrixUtils.print(D, "D-dimension");
		
		D_ML = D.copy();		
		
		for(int i = 0; i < constraints.ml1.size(); i++)
		{
			D_ML.set((constraints.ml1.get(i)), (constraints.ml2.get(i)), d_min);
			//D_ML.set(data.indexOf(constraints.ml1.get(i)), data.indexOf(constraints.ml2.get(i)), d_min);1
			//System.out.println("D[" + (constraints.ml1.get(i)) + "," + (constraints.ml2.get(i)) + "]= " + d_min);
					
		}
		
		
		for(int i = 0; i < constraints.ml1.size(); i++)
		{
			restoreTriangle((constraints.ml1.get(i)), (constraints.ml1.get(i)), D_ML);
			//restoreTriangle(data.indexOf(constraints.ml1.get(i)), data.indexOf(constraints.ml1.get(i)), D_ML);				
		}
		
		getDiffusionMap(D);
		//MatrixUtils.print(D, "D");
		
		int[][] cannotlink = new int[constraints.cl1.size()][2];
		//System.out.println("cannotlink.length = " + cannotlink.length);
		for(int i = 0; i < cannotlink.length; i++)
		{
			cannotlink[i][0] = (constraints.cl1.get(i));
			cannotlink[i][1] = (constraints.cl2.get(i));
		}
		
		getDiffusionDistance(cannotlink);
		
		printCannotLink(D_CL, constraints,  data);
		
		D_CL.timesEquals(600);
		
		for(int ii =0; ii < D_ML.getRowDimension(); ii++)
		{
			for (int jj = 0; jj < D_ML.getColumnDimension(); jj++)
			{
				double elem = D_ML.get(ii, jj);
				elem = elem * elem;
				D_ML.set(ii, jj, elem);
			}
		}
		
		D_Distance = D_ML.plus(D_CL);
		
		
		for(int ii =0; ii < D_Distance.getRowDimension(); ii++)
		{
			for (int jj = 0; jj < D_Distance.getColumnDimension(); jj++)
			{
				double elem = D_Distance.get(ii, jj);
				elem = Math.sqrt(elem);
				D_Distance.set(ii, jj, elem);
			}
		}
		
		//MatrixUtils.print(D_ML, "D_ML");
		//MatrixUtils.print(Lambda, "Lambda");
		//MatrixUtils.print(V, "V");
		//Matrix D_CL_prim = D_CL.times(1000);
		//MatrixUtils.print(D_CL_prim, "D_CL - diffusion distance");
		//MatrixUtils.print(D_Distance, "D_Distance");
		//System.out.println("D_Distance size = (" + this.D_Distance.getRowDimension() + "," + this.D_Distance.getColumnDimension() + ")");
		//System.out.println("D_Distance[1,1]" + D_Distance.get(1,10));
		
	}
	/**
	 * 
	 * @param data
	 */
	private Matrix createD(ArrayList<double[]> data) {
		Matrix temp = new Matrix(data.size(), data.size());
		//System.out.println("size= " + data.size());
		
		for(int i =0; i < data.size(); i++)
		{
			for(int j = 0; j<data.size();j++)
			{
				double dim = getDimension(data.get(i), data.get(j));
				temp.set(i,  j, dim);
				
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
		System.out.println("d_min = " + d_min + "; d_max = " + d_max);
		return temp;
	}
	
	/***
	 * 
	 * @param constraints
	 */
	private void getDiffusionDistance(int[][] constraints)
	{
		D_CL = new Matrix(D.getRowDimension(), D.getColumnDimension());
		
		for (int i = 0; i < constraints.length; i++)
		{
			int c1 = constraints[i][0];
			int c2 = constraints[i][1];
			
			for(int x = 0; x < D_CL.getRowDimension(); x ++)
			{
				for(int y = 0; y < D_CL.getColumnDimension(); y++)
				{
					double dimx = getDistance(x, c1, c2);
					double dimy = getDistance(y, c1, c2); 
					
					double dim = D_CL.get(x, y);
					dim +=  (dimx-dimy);
					
					dim = dim * dim;
					
					D_CL.set(x, y, dim);
				}
			}
		}
	}
	
	/***
	 * 
	 * @param x
	 * @param c1
	 * @param c2
	 * @return
	 */
	private double getDistance(int x, int c1, int c2)
	{
		double PhiC2 = 0;
		double PhiC1 = 0;
		double dim;
		int ind;
		
		for (int i =0; i < t; i++)
		{
			ind = this.index[i];
			
			PhiC2 += Math.pow((Lambda.get(ind, 0)*V.get(x, ind) - Lambda.get(ind, 0)*V.get(c2, ind)), 2);
			PhiC1 += Math.pow((Lambda.get(ind, 0)*V.get(x, ind) - Lambda.get(ind, 0)*V.get(c1, ind)), 2);
		}
		
		PhiC2 = Math.sqrt(PhiC2);
		PhiC1 = Math.sqrt(PhiC1);
		
		dim = (PhiC2 - PhiC1)/(PhiC2 + PhiC1);
		
		return dim;
	}
	
	/***
	 * 
	 * @param i
	 * @param j
	 * @param dim_ml
	 */
	private void restoreTriangle(int i, int j, Matrix dim_ml)
	{
		for (int x =0; x < dim_ml.getRowDimension(); x++) 
		{
			for (int y = 0; y < dim_ml.getColumnDimension(); y++)
			{
				double new_dim = Math.min(dim_ml.get(x, y), dim_ml.get(x, i) + dim_ml.get(i, j) + dim_ml.get(j, y));
				//if(new_dim != dim_ml.get(x, y))
				//	System.out.println("D[" + x + "," + y + "] =" + new_dim);
				dim_ml.set(x, y, new_dim);
			}			
		}
	}
	
	/***
	 * 
	 * @param elem1
	 * @param elem2
	 * @return
	 */
	private double  getDimension(double[] elem1, double[] elem2)
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
	
	/**
	 * 
	 * @param lambda
	 * @return
	 */
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
	
	
	private void printCannotLink(Matrix distance,
			InstanceConstraints constraints,
			ArrayList<double[]> points) {
		
		/****
		double [] x = new double[points.size()];
		double [] y = new double[points.size()];
		Matrix Z = new Matrix(points.size(), points.size());
		
		int c1 = constraints.cl1.get(0);
		int c2 = constraints.cl2.get(0);
		
		for(int i = 0; i < x.length; i++)
		{
			x[i] = points.get(i)[0];
			y[i] = points.get(i)[1];
			
			double ic1 = distance.get(i, c1);
			double ic2 = distance.get(i, c2);
			
			Z.set(i, i, Math.abs(ic2) > Math.abs(ic2) ? ic2 : ic1);	
		}
		
		Matrix X =  new Matrix(x, x.length);
		Matrix Y = new Matrix(y, y.length);

		MatrixUtils.print(X, "X");
		MatrixUtils.print(Y, "Y");
		MatrixUtils.print(Z, "Z");
		/****/
	}
}
