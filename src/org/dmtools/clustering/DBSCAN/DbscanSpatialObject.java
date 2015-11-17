package org.dmtools.clustering.DBSCAN;

import lvaindex.vafile.ISpatialObject;

public class DbscanSpatialObject extends BasicSpatialObject
{
	/**
	 * Cluster id.
	 */
	int ClId;
	
	/**
	 * 
	 * @param coordinates
	 */
	public DbscanSpatialObject(double[] coordinates) {
		super(coordinates);
		ClId = -1;
	}
	
	/**
	 * 
	 * @param spatialObject
	 */
	public DbscanSpatialObject(ISpatialObject spatialObject)
	{
		super(spatialObject.getCoordinates());
		ClId = -1;
	}


}
