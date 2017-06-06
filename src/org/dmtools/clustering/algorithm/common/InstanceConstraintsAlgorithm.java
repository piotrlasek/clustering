package org.dmtools.clustering.algorithm.common;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.model.IConstraintObject;
import util.SetConstraints;

import java.util.ArrayList;

/**
 * Created by Piotr on 05.06.2017.
 */
public class InstanceConstraintsAlgorithm extends ClusteringAlgorithm {
    protected InstanceConstraints ic = new InstanceConstraints();
    protected ArrayList<IConstraintObject> deferred = new ArrayList<>();
    protected ArrayList<Integer> indexes = new ArrayList<Integer>();

    /**
     *
     * @return
     */
    public InstanceConstraints getConstraints() {
        return ic;
    }

    /**
     *
     * @param icInfo
     */
    public void setConstraints(String icInfo) {
        log.info("Instance constraints: " + icInfo);
        if (icInfo.startsWith("birch1")) {
            SetConstraints.birch1(Dataset, ic, "/data/experiment/constraintsBrich1.txt");
        } else if (icInfo.startsWith("birch2")) {
            SetConstraints.birch1(Dataset, ic, "/data/experiment/constraintsBrich2.txt");
        } else if (icInfo.startsWith("birch3")) {
            SetConstraints.birch1(Dataset, ic, "/data/experiment/constraintsBrich3.txt");
        } else if (icInfo.startsWith("random")) {
            int icn = 3;
            try {
                String[] icInfoNumber = icInfo.split("_");
                icn = new Integer(icInfoNumber[1]);
            } catch (Exception exception) {
                log.error(exception);
            }
            log.info("Setting random constraints (" + icn + "");
            randomConstraints(icn, icn, this.Dataset.size());
        } else {
            log.warn("No constraints set.");
        }
    }

    /**
     *
     */
    private void setConstraints() {
        CNBCRTreePoint p0 = new CNBCRTreePoint(new double[]{438.0, 259.0}, CDMCluster.UNCLASSIFIED);
        CNBCRTreePoint p1 = new CNBCRTreePoint(new double[]{440.0, 255.0}, CDMCluster.UNCLASSIFIED);
        CNBCRTreePoint p2, p3, p4;

        p0 = new CNBCRTreePoint(new double[]{443.0, 271.0}, CDMCluster.UNCLASSIFIED);
        p0 = (CNBCRTreePoint) Dataset.get(2730);
        p1 = new CNBCRTreePoint(new double[]{448.0, 278.0}, CDMCluster.UNCLASSIFIED);
        p2 = new CNBCRTreePoint(new double[]{421.0,333.0}, CDMCluster.UNCLASSIFIED);
        p3 = new CNBCRTreePoint(new double[]{433.0,325.0}, CDMCluster.UNCLASSIFIED);

        p0 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p0));
        p1 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p1));
        p2 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p2));
        p3 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p3));

        ic.addCannotLinkPoints(p0,  p1);
        ic.addCannotLinkPoints(p2,  p3);

        p0 = new CNBCRTreePoint(new double[]{457.0, 334.0}, CDMCluster.UNCLASSIFIED);
        p1 = new CNBCRTreePoint(new double[]{467.0, 340.0}, CDMCluster.UNCLASSIFIED);
        p2 = new CNBCRTreePoint(new double[]{475.0, 348.0}, CDMCluster.UNCLASSIFIED);
        p3 = new CNBCRTreePoint(new double[]{478.0, 356.0}, CDMCluster.UNCLASSIFIED);
        p4 = new CNBCRTreePoint(new double[]{508.0, 373.0}, CDMCluster.UNCLASSIFIED);

        p0 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p0));
        p1 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p1));
        p2 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p2));
        p3 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p3));
        p4 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p4));

        ic.addCannotLinkPoints(p0, p1);
        ic.addCannotLinkPoints(p1, p2);
        ic.addCannotLinkPoints(p2, p3);
        ic.addCannotLinkPoints(p3, p4);

        p0 = new CNBCRTreePoint(new double[]{326.0, 314.0}, CDMCluster.UNCLASSIFIED);
        p1 = new CNBCRTreePoint(new double[]{359.0, 369.0}, CDMCluster.UNCLASSIFIED);
        p0 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p0));
        p1 = (CNBCRTreePoint) Dataset.get(Dataset.indexOf(p1));
        ic.addMustLinkPoints(p0, p1);
    }

    /**
     *
     * @param mlCount
     * @param clCount
     * @param datasetSize
     */
    private void randomConstraints(int mlCount, int clCount, int datasetSize) {

        int index = 0;

        for (int i = 0; i < mlCount; i++) {
            index = drawIndex(datasetSize);
            CNBCRTreePoint p0 = (CNBCRTreePoint) Dataset.get(index);
            index = drawIndex(datasetSize);
            CNBCRTreePoint p1 = (CNBCRTreePoint) Dataset.get(index);
            ic.addMustLinkPoints(p0, p1);
        }

        for (int i = 0; i < clCount; i++) {
            index = drawIndex(datasetSize);
            CNBCRTreePoint p0 = (CNBCRTreePoint) Dataset.get(index);
            index = drawIndex(datasetSize);
            CNBCRTreePoint p1 = (CNBCRTreePoint) Dataset.get(index);
            ic.addCannotLinkPoints(p0, p1);
        }
    }

    /**
     *
     * @param datasetSize
     * @return
     */
    public int drawIndex(int datasetSize) {

        int index = -1;

        do {
            index = (int) (Math.random() * datasetSize);
        } while (indexes.contains(index));

        indexes.add(index);

        return index;
    }

}
