package org.dmtools.clustering.algorithm.CNBC;

import java.util.ArrayList;

import org.dmtools.clustering.model.IConstraintObject;
import org.dmtools.clustering.algorithm.NBC.NBCRTreePoint;
import org.dmtools.clustering.old.Cell;
import org.dmtools.clustering.model.ISpatialObject;

import spatialindex.spatialindex.Point;
import spatialindex.spatialindex.SpatialIndex;


/**
 * @author Piotr Lasek
 *
 */
public class CNBCRTreePoint extends NBCRTreePoint implements IConstraintObject {

	//public boolean wasDeferred;
	private IConstraintObject parentCL;
	//public boolean clVisited = false;
	public ArrayList<CNBCRTreePoint> reverseNeighbors = new ArrayList<CNBCRTreePoint>();
	//public boolean isCL = false;

	/**
	 *
	 * @param pCoords
     */
	public CNBCRTreePoint(double[] pCoords) {
		super(pCoords);
	}

	/**
	 *
	 * @param pCoords
	 * @param c
     */
	public CNBCRTreePoint(double[] pCoords, int c) {
		super(pCoords, c);
	}

	/**
	 *
	 * @param o
	 * @return
     */
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point pt = (Point) o;

			if (pt.m_pCoords.length != m_pCoords.length) return false;

			for (int cIndex = 0; cIndex < m_pCoords.length; cIndex++) {
				if (m_pCoords[cIndex] < pt.m_pCoords[cIndex] - SpatialIndex.EPSILON ||
						m_pCoords[cIndex] > pt.m_pCoords[cIndex] + SpatialIndex.EPSILON) return false;
			}

			return true;
		}

		return false;
	}

	// For C-NBC we actually do not need to implement the below methods.

	@Override
	public boolean isCannotLinkPoint() {
		return false;
	}

	@Override
	public boolean isMustLinkPoint() {
		return false;
	}

	@Override
	public boolean wasDeferred() {
		return false;
	}

	@Override
	public void isCannotLinkPoint(boolean isCannotLinkPoint) {

	}

	@Override
	public void isMustLinkPoint(boolean isMustLinkPoint) {

	}

	@Override
	public void wasDeferred(boolean wasDeferred) {

	}

	@Override
	public IConstraintObject getParentCannotLinkPoint() {
		return parentCL;
	}

	@Override
	public void setParentCannotLinkPoint(IConstraintObject parentCannotLinkPoint) {
		this.parentCL = parentCannotLinkPoint;
	}

	@Override
	public void setValues(double[] values) {

	}

	@Override
	public double[] getValues() {
		return this.m_pCoords;
	}

	@Override
	public void setParentCellCoordinates(int[] coordinates) {

	}

	@Override
	public int[] getParentCellCoordinates() {
		return new int[0];
	}

	@Override
	public Cell getParentCell() {
		return null;
	}

	@Override
	public void setParentCell(Cell c) {

	}

	@Override
	public void setApproximation(Long approximation) {

	}

	@Override
	public Long getApproximation() {
		return null;
	}

	@Override
	public void setClusterId(int clusterId) {

	}

	@Override
	public int getClusterId() {
		return 0;
	}

	@Override
	public ArrayList<ISpatialObject> getNeighbors(int k) {
		return null;
	}

}
