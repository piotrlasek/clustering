package org.dmtools.clustering.NBC;

import org.dmtools.clustering.old.BasicSpatialObject;
import org.dmtools.clustering.old.ISpatialObject;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class NbcSpatialObject extends BasicSpatialObject
{
	int clst_no;
	double ndf = -1;
	int SizeOfkNB;
	int SizeOfRkNB;
	boolean cand;

	protected NbcSpatialObject(double[] coordinates)
	{
		super(coordinates);
		this.SizeOfRkNB = 0;
		this.SizeOfkNB = 0;
		clst_no = -1;
	}
	
	public NbcSpatialObject(ISpatialObject spatialObject)
	{
		super(spatialObject.getCoordinates());
		this.SizeOfRkNB = 0;
		this.SizeOfkNB = 0;
		clst_no = -1;
	}
	
	public String excell() {
		String s = "";

		double coord[] = this.getCoordinates();
		for (int i = 0; i < coord.length; i++) {
			s += ( i == 0 ) ? coord[i] : "\t" + coord[i];
		}
		//s += "\t" + this.clst_no;
		s += "\t" + this.ndf;

		return s;
	}
}
