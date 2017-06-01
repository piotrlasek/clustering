package org.dmtools.clustering.algorithm.CDBSCAN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.algorithm.CNBC.MyFrame2;
import org.dmtools.clustering.model.IClusteringData;
import org.dmtools.clustering.model.IClusteringDataSource;
import org.dmtools.clustering.old.DataSourceManager;
import org.dmtools.datamining.data.CDMFilePhysicalDataSet;
import util.Dump;

import javax.datamining.JDMException;
import javax.datamining.MiningObject;
import javax.datamining.clustering.ClusteringSettings;
import javax.datamining.data.PhysicalAttribute;
import javax.datamining.data.PhysicalDataSet;
import java.util.ArrayList;
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
    private ArrayList<double[]> data;

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

        prepareData();

        CDBSCANRTree dbscan = new CDBSCANRTree();

        DataSourceManager dsm = new DataSourceManager();

        dsm.readData("abc", data);
        dsm.setActiveDataSource("abc");

        IClusteringDataSource cds = dsm.getActiveDataSource();

        dbscan.setData(cds.getData());
        dbscan.setEps(Eps);
        dbscan.setMinPts(MinPts);
        dbscan.run();

        IClusteringData cd = dbscan.getResult();
        String uri = getPhysicalDataSet().getURI();

        String dumpFileName = "cdbscan-rtree-" + getPhysicalDataSet().getDescription() + ".csv";
        Dump.toFile(cd.get(), dumpFileName, true);

        InstanceConstraints ic = dbscan.getConstraints();

        ArrayList<CNBCRTreePoint> result = dbscan.getDataset();

        // Show result
		MyFrame2.plotResult(result, max[0], ic, null, null, null);
		return null;
    }

    /**
     *
     */
    public void prepareData()
	{
		ArrayList<Object[]> rawData =
				((CDMFilePhysicalDataSet) getPhysicalDataSet()).getData();
		data = new ArrayList<double[]>();

		int i = 0;
		for(Object[] rawRecord : rawData) {
			double[] record = new double[attributes.size() + 1];
			int d = 0;
			for(PhysicalAttribute attribute : attributes)
			{
				record[d] = new Double(rawData.get(i)[d].toString());
				if (min[d] == 0)
					min[d] = record[d];
				else
				if (min[d] > record[d]) min[d] = record[d];
				if (max[d] < record[d]) max[d] = record[d];
				d++;
			}
			record[d] = -1; // UNCLUSTERED
			data.add(record);
			i++;
		}
	}
}
