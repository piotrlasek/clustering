package org.dmtools.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.MiningFunction;
import javax.datamining.NamedObject;
import javax.datamining.base.BuildSettings;
import javax.datamining.base.ModelDetail;
import javax.datamining.clustering.Cluster;
import javax.datamining.clustering.ClusteringModel;
import javax.datamining.clustering.ClusteringModelProperty;
import javax.datamining.data.ModelSignature;
import javax.datamining.rule.Rule;
import javax.datamining.statistics.AttributeStatisticsSet;

import org.dmtools.datamining.base.CDMAbstractModel;


public class CDMClusteringModel extends CDMAbstractModel implements ClusteringModel {

	ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	ArrayList<Rule> rules = new ArrayList<Rule>();
	
	@Override
	public Cluster getCluster(int clusterIndex) throws JDMException {
		return clusters.get(clusterIndex);
	}
	@Override
	public Collection getClusters() throws JDMException {
		return clusters;
	}
	@Override
	public Collection getLeafClusters() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getNumberOfClusters() {
		// TODO Auto-generated method stub
		return clusters.size();
	}
	@Override
	public int getNumberOfLevels() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Collection getRootClusters() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection getRules() throws JDMException {
		return rules;
	}
	@Override
	public Double getSimilarity(int arg0, int arg1) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasProperty(ClusteringModelProperty arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
