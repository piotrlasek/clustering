package org.dmtools.clustering.CNBC;

import java.util.ArrayList;

import org.dmtools.clustering.NBC.NBCRTreePoint;
import org.dmtools.clustering.old.Cell;
import org.dmtools.clustering.old.ISpatialObject;

import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.SpatialIndex;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class CNBCRTreePoint extends NBCRTreePoint implements ISpatialObject {

	public boolean wasDeferred;
	public CNBCRTreePoint parentCL;
	public boolean clVisited = false;
	public ArrayList<CNBCRTreePoint> referseNeighbors = new ArrayList<CNBCRTreePoint>();
	public boolean isCL = false;

	public CNBCRTreePoint(double[] pCoords) {
		super(pCoords);
	}
	
	public CNBCRTreePoint(double[] pCoords, int c) {
		super(pCoords, c);
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof Point)
		{
			Point pt = (Point) o;

			if (pt.m_pCoords.length != m_pCoords.length) return false;

			for (int cIndex = 0; cIndex < m_pCoords.length; cIndex++)
			{
				if (m_pCoords[cIndex] < pt.m_pCoords[cIndex] - SpatialIndex.EPSILON ||
						m_pCoords[cIndex] > pt.m_pCoords[cIndex] + SpatialIndex.EPSILON) return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public void setCoordinates(double[] coordinates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] getCoordinates() {
		return m_pCoords;
	}

	@Override
	public void setParentCellCoordinates(int[] coordinates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getParentCellCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cell getParentCell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentCell(Cell c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setApproximation(Long approximation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getApproximation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<ISpatialObject> getNeighbors(int k) {
		// TODO Auto-generated method stub
		return null;
	}
}
