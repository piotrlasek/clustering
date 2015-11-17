package org.dmtools.clustering.NBC;

import spatialindex.spatialindex.Point;


/**
 * 
 * @author Piotr Lasek
 *
 */
public class NBCRTreePoint extends Point
{
	public int clst_no;
	public double ndf = -1;
	public int numberOfkNB;
	public int numberOfRkNB;
	public boolean visited;
	

	public void reset() {
		numberOfkNB = 0;
		numberOfRkNB = 0;
		ndf = -1;
		clst_no = -1;
	}

	/** Creates a new instance of MyPoint */
	public NBCRTreePoint(double[] pCoords, int c) {
		super(pCoords);
		this.clst_no = c;
		this.numberOfRkNB = 0;
		this.numberOfkNB = 0;
		this.visited = false;

	}

	public NBCRTreePoint(double[] pCoords) {
		super(pCoords);
		this.numberOfRkNB = 0;
		this.numberOfkNB = 0;
		this.visited = false;
		clst_no = -1;
	}

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

	public String excell() {
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
	}
}
