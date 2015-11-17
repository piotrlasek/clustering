package org.dmtools.clustering.NBC.required;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class BasicClusteringData implements IClusteringData
{
	ArrayList<IClusteringObject> al;

	@Override
	public Collection<IClusteringObject> get() {

		return al;
	}

	@Override
	public void reset() {
		al.clear();	
	}

	@Override
	public void set(Collection<IClusteringObject> collection)
	{
		al = (ArrayList<IClusteringObject>) collection;		
	}

}
