package org.dmtools.clustering.old;

public class BasicClusterInfo implements IClusterInfo 
{
	int clusterId = -1;
	int previousClusterId = -1;

	@Override
	public int getClusterId() {
		return clusterId;
	}
	
	public int getPreviousClusterId() {
		return previousClusterId;
	}

	@Override
	public void setClusterId(int id) {
		previousClusterId = clusterId;
		clusterId = id;		
	}

}
