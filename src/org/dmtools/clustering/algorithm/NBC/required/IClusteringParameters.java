package org.dmtools.clustering.algorithm.NBC.required;

import java.util.Enumeration;

public interface IClusteringParameters {

	public String getValue(String name);
	
	public IClusteringParameters setValue(String name, String value);
	
	public IClusteringParameters sv(String name, String value);
	
	public Enumeration<Object> keys();
	
	public void remove(String key);
	
	public String toString();
}
