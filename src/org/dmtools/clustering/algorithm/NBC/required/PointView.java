package org.dmtools.clustering.algorithm.NBC.required;

public class PointView {
	
	public double[] m_pCoords;
	public int c;
	
	PointView(double[] coords, int cluster)
	{
		this.m_pCoords = coords;
		this.c = cluster;
	}
	
	public void reset()
	{
		c = -1;
	}
	
	public String toLine()
	{
		String s = new String();
		for (double d:m_pCoords)
		{
			s += d + ":" ;
		}
		s += c;
		return s;
	}
	
	public boolean equals(Object o)
	{
		boolean eq = true;
		
		PointView pv = (PointView) o;
		
		for(int i = 0; i < m_pCoords.length; i++)
		{
			if (m_pCoords[i] != pv.m_pCoords[i])
			{
				eq = false;
				break;
			}
		}
		return eq;
	}
	
	public int hashCode()
	{
		double hc=0;
		for(int i = 0; i < m_pCoords.length; i++)
		{
			hc += m_pCoords[i];
		}
		return (int) hc;
	}
	
}
