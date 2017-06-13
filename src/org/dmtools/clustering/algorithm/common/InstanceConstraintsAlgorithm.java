package org.dmtools.clustering.algorithm.common;

import org.dmtools.clustering.algorithm.CNBC.CNBCRTreePoint;
import org.dmtools.clustering.algorithm.CNBC.InstanceConstraints;
import org.dmtools.clustering.model.IConstraintObject;
import util.SetConstraints;

import java.util.ArrayList;

/**
 * Created by Piotr Lasek on 05.06.2017.
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
            log.info("Setting random constraints (" + icn + ")");
            randomConstraints(icn, icn, this.Dataset.size());
        } else {
            log.warn("No constraints set.");
        }
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
