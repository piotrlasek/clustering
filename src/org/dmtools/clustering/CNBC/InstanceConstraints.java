package org.dmtools.clustering.CNBC;

import java.util.ArrayList;

/**
 * 
 * @author Piotrek
 *
 */
public class InstanceConstraints {

	ArrayList<CNBCRTreePoint> cl1 = new ArrayList<CNBCRTreePoint>();
	ArrayList<CNBCRTreePoint> cl2 = new ArrayList<CNBCRTreePoint>();
	
	ArrayList<CNBCRTreePoint> ml1 = new ArrayList<CNBCRTreePoint>();
	ArrayList<CNBCRTreePoint> ml2 = new ArrayList<CNBCRTreePoint>();
	
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
	public void addCannotLinkPoints(CNBCRTreePoint p1, CNBCRTreePoint p2) {
		p1.isCL = true;
		p2.isCL = true;
		cl1.add(p1);
		cl2.add(p2);
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean cannotLink(CNBCRTreePoint p1, CNBCRTreePoint p2) {
		boolean cannotLink = false;
		
		if (p1.clst_no >= 0 && p2.clst_no >= 0) {
		
			int p1Pos = cl1.indexOf(p1);
			int p2Pos = cl2.indexOf(p2);
			
			if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos)
			{
				cannotLink = true;			
			}
			
			p1Pos = cl1.indexOf(p2);
			p2Pos = cl2.indexOf(p1);
			
			if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos)
			{
				cannotLink = true;			
			}
		
		}
		
		return cannotLink;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean cannotLinkExt(CNBCRTreePoint p1, CNBCRTreePoint p2) {		
		boolean cl1 = cannotLink(p1, p2);
		boolean cl2 = cannotLink(p1.parentCL, p2);
		boolean cl3 = cannotLink(p1.parentCL, p2.parentCL);
		boolean cl4 = cannotLink(p1, p2.parentCL);
		//return cl3;
		return cl1 || cl2 || cl3 || cl4;
	}	
	
	/**
	 * 
	 * @param set
	 * @param ls
	 * @return
	 */
    public boolean existsCannotLink(ArrayList<CNBCRTreePoint> set)
    {
    	boolean exists = false;
    	
    	for(CNBCRTreePoint p0 : set)
    	{
    		if (p0.m_pCoords[0] == 443) {
    			System.out.println("found");
    			
    		}
    		for(CNBCRTreePoint p1 : set)
    		{
    			boolean cannotLink = cannotLink(p0, p1);
    			if (cannotLink)
    			{
    				exists = true;
    				break;
    			}
    		}
    		if (exists)
    			break;
    	}
    	
    	return exists;    	
    }
    
    public void add(double[] p0, double[] p1, boolean mustLink)
    {
    	CNBCRTreePoint r0 = new CNBCRTreePoint(p0, CNBCRTree.UNCLASSIFIED);
    	CNBCRTreePoint r1 = new CNBCRTreePoint(p1, CNBCRTree.UNCLASSIFIED);
    	if (mustLink) {
    		this.addMustLinkPoints(r0, r1);
    	} else {
    		this.addCannotLinkPoints(r0,  r1);
    	}
    }
    
    
    /**
	 * 
	 * @param p1
	 * @param p2
	 */
	public void addMustLinkPoints(CNBCRTreePoint p1, CNBCRTreePoint p2) {
		ml1.add(p1);
		ml2.add(p2);
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean mustLink(CNBCRTreePoint p1, CNBCRTreePoint p2) {
		boolean mustLink = false;
		
		int p1Pos = ml1.indexOf(p1);
		int p2Pos = ml2.indexOf(p2);
		
		if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos)
		{
			mustLink = true;			
		}
		
		p1Pos = ml1.indexOf(p2);
		p2Pos = ml2.indexOf(p1);
		
		if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos)
		{
			mustLink = true;			
		}
		
		return mustLink;
	}	
	
	/**
	 * 
	 * @param set
	 * @param ls
	 * @return
	 */
    public ArrayList<CNBCRTreePoint> getMustLinkObjects(CNBCRTreePoint p)
    {
    	ArrayList<CNBCRTreePoint> mustLinkObjects = new ArrayList<CNBCRTreePoint>();
    	
    	for (int i = 0; i < ml1.size(); i++) {
    		CNBCRTreePoint p0 = ml1.get(i);
    		CNBCRTreePoint p1 = ml2.get(i);
    		
    		if (p0.equals(p)) {
    			mustLinkObjects.add(p1);
    		} else if (p1.equals(p)) {
    			mustLinkObjects.add(p0);
    		}
    	}
    	
    	return mustLinkObjects;
    }
    
    
	
	/**
	 * 
	 * @param set
	 * @param ls
	 * @return
	 */
    public ArrayList<CNBCRTreePoint> getCannotLinkObjects(CNBCRTreePoint p)
    {
    	ArrayList<CNBCRTreePoint> cannotLinkObjects = new ArrayList<CNBCRTreePoint>();
    	
    	for (int i = 0; i < cl1.size(); i++) {
    		CNBCRTreePoint p0 = cl1.get(i);
    		CNBCRTreePoint p1 = cl2.get(i);
    		
    		if (p0.equals(p) && !p1.equals(p) && !cannotLinkObjects.contains(p1)) {
    			cannotLinkObjects.add(p1);
    		} else if (p1.equals(p) && !p0.equals(p) && !cannotLinkObjects.contains(p1)) {
    			cannotLinkObjects.add(p0);
    		}
    	}
    	
    	return cannotLinkObjects;
    }
    
    
    
	/**
	 * 
	 * @param set
	 * @param ls
	 * @return
	 */
    public boolean existsMustLink(ArrayList<CNBCRTreePoint> set)
    {
    	boolean exists = false;
    	
    	for(CNBCRTreePoint p0 : set)
    	{
    		for(CNBCRTreePoint p1 : set)
    		{
    			boolean mustLink = mustLink(p0, p1);
    			if (mustLink)
    			{
    				exists = true;
    				break;
    			}
    		}
    		if (exists)
    			break;
    	}
    	
    	return exists;    	
    }

	public boolean cannotAsignToGroup(CNBCRTreePoint p, int clst_no) {
		boolean cannot = false;
		
		ArrayList<CNBCRTreePoint> cl = getCannotLinkObjects(p);
		
		for(CNBCRTreePoint pt:cl) {
			if (pt.clst_no >= 0 && pt.clst_no == clst_no) {
				cannot = true;
				break;
			}
		}
		
		return cannot;
		
		
		/*boolean cannotLink = false;
		
		int p1Pos = cl1.indexOf(p);
		int p2Pos = cl2.indexOf(p);
		
		if (p1Pos != -1) {
			CNBCRTreePoint p2 = cl2.get(p1Pos);
			if (p2.clst_no == clst_no) {
				cannotLink = true;
			}
		} else if (p2Pos != -1) {
			CNBCRTreePoint p2 = cl1.get(p2Pos);
			if (p2.clst_no == clst_no) {
				
				cannotLink = true;
			}
		}
		
		return cannotLink;*/
	}
}
