package org.dmtools.clustering.algorithm.CDBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.util.Collection;

/**
 * Created by Piotr Lasek on 30.05.2017.
 */
public class CDBSCANAlgorithm extends CDMBasicClusteringAlgorithm {

    private final static Logger log = LogManager.getLogger("CDBSCANAlgorithm");

    public static final String NAME = "C-DBSCAN";
    private Collection<PhysicalAttribute> attributes;
    private final int numberOfDimensions;
    private final double[] min;
    private final double[] max;
    private final CDBSCANAlgorithmSettings algorithmSettings;
    private double Eps;
    private int MinPts;

    public CDBSCANAlgorithm(ClusteringSettings clusteringSettings, PhysicalDataSet physicalDataSet) {
        super(clusteringSettings, physicalDataSet);

        try {
            attributes = physicalDataSet.getAttributes();
        } catch (JDMException e) {
            e.printStackTrace();
        }

        numberOfDimensions = attributes.size();

        min = new double[numberOfDimensions];
        max = new double[numberOfDimensions];

        algorithmSettings = (CDBSCANAlgorithmSettings) clusteringSettings.getAlgorithmSettings();
        Eps = algorithmSettings.getEps();
        MinPts = algorithmSettings.getMinPts();
    }

    @Override
    public MiningObject run() {
        log.info(NAME + " starting...");
        return null;
    }
}
