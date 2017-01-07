package org.dmtools.clustering.algorithm.NBC;

import org.dmtools.clustering.model.ISpatialObject;
import org.dmtools.clustering.old.Cell;
import spatialindex.spatialindex.Point;

import java.util.ArrayList;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCRTreePoint extends Point implements ISpatialObject {
	private int clst_no;
	public double ndf = -1;
	public int numberOfkNB;
	public int numberOfRkNB;
	public boolean visited;

	/**
	 *
	 */
	public void reset() {
		numberOfkNB = 0;
		numberOfRkNB = 0;
		ndf = -1;
		clst_no = -1;
	}

	/**
	 * Creates a new instance of MyPoint
	 */
	public NBCRTreePoint(double[] pCoords, int c) {
		super(pCoords);
		this.clst_no = c;
		this.numberOfRkNB = 0;
		this.numberOfkNB = 0;
		this.visited = false;
	}

	/**
	 *
	 * @param pCoords
     */
	public NBCRTreePoint(double[] pCoords) {
		super(pCoords);
		this.numberOfRkNB = 0;
		this.numberOfkNB = 0;
		this.visited = false;
		clst_no = -1;
	}

	/**
	 *
	 * @return
     */
	public String toString() {
		String s = "";

		if (this.m_pCoords == null)
			return s;

		s += "(";
		for (int i = 0; i < this.m_pCoords.length; i++) {
			s += ( i == 0 ) ? this.m_pCoords[i] : ", " + this.m_pCoords[i];
		}
		s += ")";

		s += " : group = " + this.clst_no;
		
		s += ", ndf = " + this.ndf;

		return s;
	}

	@Override
	public void setValues(double[] values) {

	}

	@Override
	public double[] getValues() {
		return new double[0];
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
		this.clst_no = clusterId;
	}

	@Override
	public int getClusterId() {
		return clst_no;
	}

	@Override
	public ArrayList<ISpatialObject> getNeighbors(int k) {
		return null;
	}

	/*public String excell() {
		String s = "";

		if (this.m_pCoords == null)
			return s;

		for (int i = 0; i < this.m_pCoords.length; i++) {
			s += ( i == 0 ) ? this.m_pCoords[i] : "\t" + this.m_pCoords[i];
		}
		s += "\t" + this.clst_no;
		s += "\t" + this.ndf;

		return s;
	}

	public String toLine() {
		String s = "";

		if (this.m_pCoords == null)
			return s;

		for (int i = 0; i < this.m_pCoords.length; i++) {
			s += this.m_pCoords[i] + ";";
		}

		s += this.clst_no + ";\r\n";

		return s;
	} */
}
