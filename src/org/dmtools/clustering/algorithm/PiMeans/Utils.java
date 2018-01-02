package org.dmtools.clustering.algorithm.PiMeans;

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
    public static String layerToString(HashMap<Long, PiBin> layer) {
        StringBuilder result = new StringBuilder();
        for(Map.Entry<Long, PiBin> entry : layer.entrySet()) {
            PiBin piBin = entry.getValue();
            result.append(piBin.getX()).append(",");
            result.append(piBin.getY()).append(",");
            result.append(piBin.getPointsCount()).append("\n");
        }
        return result.toString();
    }
}
