package org.dmtools.clustering.old;

import java.util.ArrayList;
import java.util.Collection;

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
