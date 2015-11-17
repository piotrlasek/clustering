package org.dmtools.clustering.old;

import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;

public class BasicClusteringObject implements IClusteringObject
{
	BasicClusterInfo bci;
	ISpatialObject so; 
	
	HashMap<String, String> has = new HashMap();
	
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

	public void addParameter(String key, String value) {
		has.put(key, value);
		
	}
	
	public String getParameter(String key) {
		return has.get(key);
	}


}
