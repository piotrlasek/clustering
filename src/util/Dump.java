package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.model.IClusteringObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;



public class Dump {

    private final static Logger log = LogManager.getLogger(Dump.class.getName());

    /**
     *
     * @param Dataset
     * @param fName
     * @param ignoreNoise
     */
    public static void toFile(Collection<IClusteringObject> Dataset, String fName, boolean ignoreNoise) {
        String filePath = Workspace.getWorkspacePath() + "/data/experiment/" + fName;
        log.info("Saving results to: " + filePath);
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

    /**
     *
     * @param algorithmName
     * @param fileName
     * @param parameters
     * @return
     */
    public static String getDumpFileName(String algorithmName, String fileName,
                                         String parameters) {
        String fileNameNoExt = fileName.split("\\.")[0];
        String dumpFileName = fileNameNoExt + "_" + algorithmName + "_" + parameters + ".csv";
        return dumpFileName;
    }
}
