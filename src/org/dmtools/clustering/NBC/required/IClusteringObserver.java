package org.dmtools.clustering.NBC.required;

import java.util.Collection;

/**
 * 
 * @author Piotr Lasek
 *
 */
public interface IClusteringObserver
{
	
	/**
	 * 
	 */
	public void handleNotify(Object object);
		
	/**
	 * 
	 */
	public void handleNotify(IClusteringData data);
	
	/**
	 * 
	 * @param message
	 */
	public void handleNotify(String message);
}
