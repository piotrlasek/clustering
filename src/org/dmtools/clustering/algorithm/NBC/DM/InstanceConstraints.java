package org.dmtools.clustering.algorithm.NBC.DM;

import java.util.ArrayList;

/**
 * 
 * @author Piotrek
 *
 */
public class InstanceConstraints {

	ArrayList<Integer> cl1 = new ArrayList<Integer>();
	ArrayList<Integer> cl2 = new ArrayList<Integer>();
	
	ArrayList<Integer> ml1 = new ArrayList<Integer>();
	ArrayList<Integer> ml2 = new ArrayList<Integer>();
	
	
	ArrayList<Integer> r1 = new ArrayList<Integer>();
	ArrayList<Integer> r2 = new ArrayList<Integer>();
	ArrayList<Integer> r3 = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	public InstanceConstraints() {
		
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 */
	public void addCannotLinkPoints(int p1, int p2) {
		
		cl1.add(p1);
		cl2.add(p2);
	}
	
	public void addRelative(int p1, int p2, int p3)
	{
		r1.add(p1);
		r2.add(p2);
		r3.add(p3);
	}
	
    
    /**
	 * 
	 * @param p1
	 * @param p2
	 */
	public void addMustLinkPoints(int p1, int p2) {
		ml1.add(p1);
		ml2.add(p2);
	}
	
	
}
