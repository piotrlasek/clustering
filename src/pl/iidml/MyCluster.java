package pl.iidml;

import javax.datamining.JDMException;
import javax.datamining.clustering.Cluster;
import javax.datamining.rule.Predicate;
import javax.datamining.rule.Rule;
import javax.datamining.statistics.AttributeStatisticsSet;

public class MyCluster implements Cluster {

	@Override
	public Cluster[] getAncestors() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getCaseCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 *  Returns the centroid point of the specified categorical attribute
	 *  for a specific category value for the cluster.
	 */
	@Override
	public Double getCentroidCoordinate(String arg0) throws JDMException {
		return Math.random();
	}

	@Override
	public Double getCentroidCoordinate(String arg0, Object arg1)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cluster[] getChildren() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getClusterId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cluster getParent() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rule getRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Predicate getSplitPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeStatisticsSet getStatistics() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getSupport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

}
