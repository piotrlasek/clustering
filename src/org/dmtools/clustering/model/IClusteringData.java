package org.dmtools.clustering.model;

import java.util.Collection;

public interface IClusteringData {
	
	/**
	 * 
	 */
	public void reset();
	
	/**
	 * 
	 */
	public void set(Collection<IClusteringObject> collection);
	
	/**
	 * 
	 * @return
	 */
	public Collection<IClusteringObject> get();
	
}
