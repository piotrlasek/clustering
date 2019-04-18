package org.dmtools.clustering.algorithm.PiMeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Piotr on 31.10.2017.
 */
public class Utils {

    /**
     }
     *
     * @return
     */
    public static String layerToString(ArrayList<PiCluster> seeds, HashMap<Long, PiBin> layer, int run) {
        StringBuilder result = new StringBuilder();
        for(Map.Entry<Long, PiBin> entry : layer.entrySet()) {
            PiBin piBin = entry.getValue();
            result.append(piBin.getX()).append(",");
            result.append(piBin.getY()).append(",");
            PiCluster cluster = piBin.getClusters(run);
            if (cluster != null) {
                result.append(piBin.getClusters(run).getId() + ",");
            } else {
                result.append("-1,");
            }
            result.append(piBin.getPointsCount()).append("\n");
        }

        for(PiCluster seed : seeds) {
            result.append(seed.coordinates[0] + ",");
            result.append(seed.coordinates[1] + ",");
            result.append(-10 + ",");
            result.append(0 +"\n");
        }

        return result.toString();
    }

    /**
     *
     * @param assignedBins
     * @param bin
     */
    public static void getAssignedBins(ArrayList<PiBin> assignedBins, PiBin bin, int run) {
        if (bin.getClusters(run) != null) {
            assignedBins.add(bin);
        } else if (bin.getChildBins() != null) {
            for (PiBin child : bin.getChildBins()) {
                getAssignedBins(assignedBins, child, run);
            }
        }
    }

    /**
     *
     * @param seeds
     * @param layer
     * @return
     */
    public static String clusteredLayerToString(ArrayList<PiCluster> seeds, HashMap<Long, PiBin> layer, int run) {
        StringBuilder result = new StringBuilder();

        // x, y, s (size), id (clusterid), cnt (points count in a bin)
        result.append("x,y,s,id,cnt\n");

        for(Map.Entry<Long, PiBin> entry : layer.entrySet()) {
            PiBin piBin = entry.getValue();
            ArrayList<PiBin> assignedBins = new ArrayList<PiBin>();
            //assignedBins.add(piBin);
            Utils.getAssignedBins(assignedBins, piBin, run);
            for (PiBin assignedBin : assignedBins) {
                result.append(assignedBin.getX()).append(",");
                result.append(assignedBin.getY()).append(",");
                result.append(assignedBin.getSize() + ",");
                PiCluster cluster = assignedBin.getClusters(run);
                if (cluster != null) {
                    result.append(assignedBin.getClusters(run).getId() + ",");
                } else {
                    result.append("-1,");
                }
                result.append(piBin.getPointsCount()).append("\n");
            }
        }

        for(PiCluster seed : seeds) {
            result.append(seed.coordinates[0] + ",");
            result.append(seed.coordinates[1] + ",");
            result.append("5,");
            result.append(-2 + ",");
            result.append(0 +"\n");
        }

        return result.toString();
    }
}
