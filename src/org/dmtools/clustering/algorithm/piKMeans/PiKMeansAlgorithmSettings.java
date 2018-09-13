package org.dmtools.clustering.algorithm.piKMeans;

import org.dmtools.clustering.CDMBaseAlgorithmSettings;

import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.base.AlgorithmSettings;

public class PiKMeansAlgorithmSettings extends CDMBaseAlgorithmSettings implements AlgorithmSettings {

	public static final String NAME = "pikMeans";
	private int k;
	private int maxIterations;
	private int deepest;
	private int depth;


    static {
		try {
			MiningAlgorithm.addExtension(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
	}

	private int starting;

	@Override
	public MiningAlgorithm getMiningAlgorithm() {
		try {
			return MiningAlgorithm.valueOf(NAME);
		} catch (JDMException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VerificationReport verify() {
		return null;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setDeepest(int deepest) {
		this.deepest = deepest;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setStarting(int starting) {
		this.starting = starting;
	}

	public int getStarting() {
		return starting;
	}

	public int getDeepest() {
		return deepest;
	}

	public int getDepth() {
		return depth;
	}
}

