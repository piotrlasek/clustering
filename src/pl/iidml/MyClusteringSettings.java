package pl.iidml;

import java.util.Collection;
import java.util.Date;

import javax.datamining.AttributeRetrievalType;
import javax.datamining.JDMException;
import javax.datamining.LogicalAttributeUsage;
import javax.datamining.MiningFunction;
import javax.datamining.NamedObject;
import javax.datamining.OutlierTreatment;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;
import javax.datamining.clustering.AggregationFunction;
import javax.datamining.clustering.AttributeComparisonFunction;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.clustering.SimilarityMatrix;
import javax.datamining.data.Interval;
import javax.datamining.data.LogicalData;

public class MyClusteringSettings implements ClusteringSettings {

	@Override
	public AlgorithmSettings getAlgorithmSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAttributeNames(AttributeRetrievalType arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDesiredExecutionTimeInMinutes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection getLogicalAttributes(LogicalAttributeUsage arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalData getLogicalData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogicalDataName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MiningFunction getMiningFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interval getOutlierIdentification(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutlierTreatment getOutlierTreatment(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicalAttributeUsage getUsage(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getWeight(String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getWeightAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAlgorithmSettings(AlgorithmSettings arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDesiredExecutionTimeInMinutes(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogicalDataName(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutlierIdentification(String arg0, Interval arg1)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOutlierTreatment(String arg0, OutlierTreatment arg1)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUsage(String arg0, LogicalAttributeUsage arg1)
			throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWeight(String arg0, double arg1) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWeightAttribute(String arg0) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public VerificationReport verify() {
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
	public AggregationFunction getAggregationFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeComparisonFunction getAttributeComparisonFunction(
			String arg0) throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getMaxClusterCaseCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxLevels() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxNumberOfClusters() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMinClusterCaseCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SimilarityMatrix getSimilarityMatrix(String arg0)
			throws JDMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAggregationFunction(AggregationFunction arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttributeComparisonFunction(String arg0,
			AttributeComparisonFunction arg1) throws JDMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxClusterCaseCount(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxLevels(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxNumberOfClusters(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMinClusterCaseCount(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSimilarityMatrix(String arg0, SimilarityMatrix arg1)
			throws JDMException {
		// TODO Auto-generated method stub

	}

}
