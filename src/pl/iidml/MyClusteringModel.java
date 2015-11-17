package pl.iidml;

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

public class MyClusteringModel implements ClusteringModel {

	ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	ArrayList<Rule> rules = new ArrayList<Rule>();
	
	@Override
	public String getApplicationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeStatisticsSet getAttributeStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getBuildDuration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildSettings getBuildSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildSettings getEffectiveBuildSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMajorVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MiningFunction getMiningFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMinorVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelDetail getModelDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProviderName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProviderVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelSignature getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTaskIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUniqueIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getCreationDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreatorInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NamedObject getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Cluster getCluster(int clusterIndex) throws JDMException {
		
		return clusters.get(clusterIndex);
	}

	@Override
	public Collection<Cluster> getClusters() throws JDMException {
		
		for (int i = 0; i < 10; i++)
		{
			MyCluster mc1 = new MyCluster();
			clusters.add(mc1);
		}
		
		return clusters;
	}

	@Override
	public Collection getLeafClusters() throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfClusters() {
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
		// TODO Auto-generated method stub
		Collection<Rule> rules = new ArrayList<Rule>();
		
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
