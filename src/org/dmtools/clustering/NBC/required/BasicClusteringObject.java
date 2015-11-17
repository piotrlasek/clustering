package org.dmtools.clustering.NBC.required;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class BasicClusteringObject implements IClusteringObject
{
	BasicClusterInfo bci;
	ISpatialObject so; 
	
	@Override
	public IClusterInfo getClusterInfo() {
		// TODO Auto-generated method stub
		return bci;
	}

	@Override
	public ISpatialObject getSpatialObject() {
		return so;
	}

	@Override
	public void setClusterInfo(IClusterInfo clusterInfo) {
		bci = (BasicClusterInfo) clusterInfo;
		
	}

	@Override
	public void setSpatialObject(ISpatialObject spatialObject) {
		so = spatialObject;
	}


}
