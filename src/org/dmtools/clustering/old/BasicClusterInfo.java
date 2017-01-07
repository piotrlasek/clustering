package org.dmtools.clustering.old;

import org.dmtools.clustering.model.IClusterInfo;

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
