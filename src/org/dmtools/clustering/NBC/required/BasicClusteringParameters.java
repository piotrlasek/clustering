package org.dmtools.clustering.NBC.required;

import java.util.Enumeration;
import java.util.Properties;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class BasicClusteringParameters implements IClusteringParameters {

	Properties properties;
	
	public BasicClusteringParameters() {
		properties = new Properties();
	}
	
	@Override
	public String getValue(String name) {
		return properties.getProperty(name);
	}

	@Override
	public IClusteringParameters setValue(String name, String value) {
	    properties.setProperty(name, value);
	    return this;
	}

	@Override
	public Enumeration<Object> keys()
	{
		return properties.keys();
	}
	
	@Override
	public String toString()
	{
	    StringBuffer s = new StringBuffer();
	    Enumeration<Object> keys = keys();
	    
	    int pos = 0;
	    while (keys.hasMoreElements())
	    {
	        String k = (String) keys.nextElement();
	        String v = properties.getProperty(k);
	        
	        if (pos == 0) {
	            s.append(k + " = " + v);
	        } else {
	            s.append(", " + k + " = " + v);
	        }
	        pos++;
	    }
	    
	    while (s.length() < 30)
	    {
	        s.append(" ");
	    }

	    return s.toString();
	}
	
	/**
	 * 
	 */
	public void clear() {
	    properties.clear();
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public IClusteringParameters sv(String name, String value) {
	    return this.setValue(name, value);
	}
	
	@Override
	public void remove(String key) {
	    properties.remove(key);
	}
}
