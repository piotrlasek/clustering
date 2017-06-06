package org.dmtools.clustering.algorithm.CNBC;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.model.IConstraintObject;
import java.util.ArrayList;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class InstanceConstraints {

	public ArrayList<IConstraintObject> cl1 = new ArrayList<IConstraintObject>();
	public ArrayList<IConstraintObject> cl2 = new ArrayList<IConstraintObject>();

	ArrayList<IConstraintObject> ml1 = new ArrayList<IConstraintObject>();
	ArrayList<IConstraintObject> ml2 = new ArrayList<IConstraintObject>();

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
	public void addCannotLinkPoints(IConstraintObject p1, IConstraintObject p2) {
		p1.isCannotLinkPoint(true);
		p2.isCannotLinkPoint(true);
		cl1.add(p1);
		cl2.add(p2);
	}


	/**
	 *
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean cannotLink(IConstraintObject p1, IConstraintObject p2) {
		boolean cannotLink = false;

		if (p1.getClusterId() > CDMCluster.NOISE && p2.getClusterId() > CDMCluster.NOISE) {
			int p1Pos = cl1.indexOf(p1);
			int p2Pos = cl2.indexOf(p2);

			if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos) {
				cannotLink = true;
			}

			p1Pos = cl1.indexOf(p2);
			p2Pos = cl2.indexOf(p1);

			if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos) {
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
	public boolean cannotLinkExt(IConstraintObject p1, IConstraintObject p2) {
		boolean cl1 = cannotLink(p1, p2);
		boolean cl2 = cannotLink(p1.getParentCannotLinkPoint(), p2);
		boolean cl3 = cannotLink(p1.getParentCannotLinkPoint(), p2.getParentCannotLinkPoint());
		boolean cl4 = cannotLink(p1, p2.getParentCannotLinkPoint());
		//return cl3;
		return cl1 || cl2 || cl3 || cl4;
	}

	/**
	 *
	 */
    public boolean existsCannotLink(ArrayList<? extends IConstraintObject> set) {
    	boolean exists = false;

    	for(IConstraintObject p0 : set) {
    		for(IConstraintObject p1 : set) {
    			boolean cannotLink = cannotLink(p0, p1);
    			if (cannotLink) {
    				exists = true;
    				break;
    			}
    		}
    		if (exists) {
				break;
			}
    	}

    	return exists;
    }

	/**
	 *
	 * @param p0
	 * @param p1
	 * @param mustLink
     */
    public void add(IConstraintObject p0, IConstraintObject p1, boolean mustLink) {
    	if (mustLink) {
    		this.addMustLinkPoints(p0, p1);
    	} else {
    		this.addCannotLinkPoints(p0,  p1);
    	}
    }

    /**
	 *
	 * @param p1
	 * @param p2
	 */
	public void addMustLinkPoints(IConstraintObject p1, IConstraintObject p2) {
		ml1.add(p1);
		ml2.add(p2);
	}

	/**
	 *
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean mustLink(IConstraintObject p1, IConstraintObject p2) {
		boolean mustLink = false;

		int p1Pos = ml1.indexOf(p1);
		int p2Pos = ml2.indexOf(p2);

		if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos) {
			mustLink = true;
		}

		p1Pos = ml1.indexOf(p2);
		p2Pos = ml2.indexOf(p1);

		if (p1Pos != -1 && p2Pos != -1 && p1Pos == p2Pos) {
			mustLink = true;
		}

		return mustLink;
	}

	/**
	 *
	 */
    public ArrayList<IConstraintObject> getMustLinkObjects(IConstraintObject p) {
    	ArrayList<IConstraintObject> mustLinkObjects = new ArrayList<IConstraintObject>();

    	for (int i = 0; i < ml1.size(); i++) {
    		IConstraintObject p0 = ml1.get(i);
    		IConstraintObject p1 = ml2.get(i);

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
	 */
    public ArrayList<IConstraintObject> getCannotLinkObjects(IConstraintObject p) {
    	ArrayList<IConstraintObject> cannotLinkObjects = new ArrayList<>();

    	for (int i = 0; i < cl1.size(); i++) {
    		IConstraintObject p0 = cl1.get(i);
    		IConstraintObject p1 = cl2.get(i);

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
	 */
    public boolean existsMustLink(ArrayList<IConstraintObject> set) {
    	boolean exists = false;

    	for(IConstraintObject p0 : set) {
    		for(IConstraintObject p1 : set) {
    			boolean mustLink = mustLink(p0, p1);
    			if (mustLink) {
    				exists = true;
    				break;
    			}
    		}

    		if (exists) {
				break;
			}
    	}

    	return exists;
    }

	/**
	 *
	 * @param p
	 * @param clst_no
	 * @return
	 */
	public boolean cannotAsignToGroup(IConstraintObject p, int clst_no) {
		boolean cannot = false;

		ArrayList<IConstraintObject> cl = getCannotLinkObjects(p);

		for(IConstraintObject pt:cl) {
			if (pt.getClusterId() > CDMCluster.NOISE && pt.getClusterId() == clst_no) {
				cannot = true;
				break;
			}
		}

		return cannot;
	}
}
