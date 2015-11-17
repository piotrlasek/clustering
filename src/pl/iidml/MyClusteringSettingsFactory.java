package pl.iidml;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.AttributeComparisonFunction;
import javax.datamining.clustering.ClusteringCapability;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.clustering.ClusteringSettingsFactory;


public class MyClusteringSettingsFactory implements ClusteringSettingsFactory {

	@Override
	public ClusteringSettings create() throws JDMException {
		// TODO Auto-generated method stub
		//return null;
		return new MyClusteringSettings();
	}

	@Override
	public boolean supportsCapability(ClusteringCapability arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCapability(AggregationFunction arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCapability(AttributeComparisonFunction arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCapability(MiningAlgorithm arg0,
			ClusteringCapability arg1) throws JDMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCapability(AggregationFunction arg0,
			AttributeComparisonFunction arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
