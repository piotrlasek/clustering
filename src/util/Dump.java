package util;

import org.dmtools.clustering.model.IClusteringObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


public class Dump {
    public static void toFile(Collection<IClusteringObject> Dataset, String fName, boolean ignoreNoise) {
        String filePath = Workspace.getWorkspacePath() + "/data/experiment/" + fName;
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            for (IClusteringObject ico : Dataset) {
                double[] coord = ico.getSpatialObject().getValues();
                int clusterId = ico.getClusterInfo().getClusterId();

                if (ignoreNoise && clusterId < 0)
                    continue;

                String toSave = Arrays.toString(coord);
                toSave = toSave.replace("[", "").replace("]", "");
                toSave += ", " + clusterId;
                writer.write(toSave+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
