package org.dmtools.clustering;

import java.util.ArrayList;
import java.util.HashMap;

import javax.datamining.JDMException;
import javax.datamining.clustering.Cluster;
import javax.datamining.rule.Predicate;
import javax.datamining.rule.Rule;
import javax.datamining.rule.RuleTranslationFormat;
import javax.datamining.statistics.AttributeStatisticsSet;

public class CDMCluster implements Cluster {
	
	String name;
	HashMap<String, Double> centroidCoordinate;
	long caseCount;
	ArrayList<Cluster> children;
	private int clusterId;
	private int level;
	private Cluster parent;
	private boolean isRoot;
	
	public CDMCluster()
	{
		
	}
	
	public CDMCluster(String name)
	{
		this.name = name;
	}

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

	public void setCaseCount(long caseCount)
	{
		this.caseCount = caseCount;
	}
	
	/**
	 *  Returns the centroid point of the specified categorical attribute
	 *  for a specific category value for the cluster.
	 */
	@Override
	public Double getCentroidCoordinate(String attributeName)
			throws JDMException {
		return centroidCoordinate.get(attributeName);
	}

	@Override
	public Double getCentroidCoordinate(String attributeName, Object arg1)
			throws JDMException {
		return getCentroidCoordinate(attributeName);
	}
	
	public void setCentroidCoordinate(String attributeName, Double value)
	{
		centroidCoordinate.put(attributeName, value);
	}
	

	@Override
	public Cluster[] getChildren() throws JDMException {
		// TODO Auto-generated method stub
		return (Cluster[]) children.toArray();
	}
	
	public void addChild(Cluster cluster)
	{
		children.add(cluster);
	}

	@Override
	public int getClusterId() {
		return clusterId;
	}
	
	public void setClusterId(int clusterId)
	{
		this.clusterId = clusterId;
	}

	@Override
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public Cluster getParent() throws JDMException {
		return parent;
	}
	
	public void setParent(Cluster parent)
	{
		this.parent = parent;
	}

	@Override
	public Rule getRule() {
		return null;
	}

	@Override
	public Predicate getSplitPredicate() {
		return null;
	}

	@Override
	public AttributeStatisticsSet getStatistics() throws JDMException {
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
		return isRoot;
	}
	
	public void isRoot(boolean isRoot)
	{
		this.isRoot = isRoot;
	}

}
