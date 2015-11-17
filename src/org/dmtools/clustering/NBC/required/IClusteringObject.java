package org.dmtools.clustering.NBC.required;

public interface IClusteringObject
{
	public ISpatialObject getSpatialObject();
	
	public IClusterInfo getClusterInfo();
	
	public void setSpatialObject(ISpatialObject spatialObject);
	
	public void setClusterInfo(IClusterInfo clusterInfo);
}
