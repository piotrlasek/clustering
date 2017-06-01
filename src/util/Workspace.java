package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Piotr Lasek on 01.03.2017.
 */
public class Workspace {

    private final static Logger logger = LogManager.getLogger("Workspace");

    public static HashMap<String, String> argsMap = new HashMap();

	public static void readArgs(String[] args) {
	    logger.info("Reading arguments...");
	    if (args != null) {
			for (String arg : args) {
				String[] kv = arg.split("=");
				String k = kv[0];
				String v = kv[1];
				k = k.trim();
				v = v.trim();
                argsMap.put(k, v);
				logger.info("   " + k + ":" + v);
			}
		}
	}

    /**
     * Returns current path.
     * @return
     */
    public static String getWorkspacePath() {
        String currentWorkspacePath = argsMap.get("workspacepath");
        if (currentWorkspacePath == null) {
            try {
                currentWorkspacePath = new java.io.File(".").getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("Workspace path set to: " + currentWorkspacePath);
        return currentWorkspacePath;
    }

    /**
     *
     * @return
     */
    public static String getDataFilePath() {
        return argsMap.get("data");
    }

    /**
     *
     * @return
     */
    public static String getAlgorithm() {
        return argsMap.get("algorithm");
    }

    /**
     *
     * @return
     */
    public static HashMap<String, String> getParameters() {
        HashMap<String, String> parameters = new HashMap();
        if (argsMap != null && argsMap.get("parameters") != null) {
            String[] kvPairStrings = argsMap.get("parameters").split(";");
            for (String kvPairString : kvPairStrings) {
                String[] kvPair = kvPairString.split(":");
                parameters.put(kvPair[0], kvPair[1]);
                logger.info(">>>> " + kvPair[0]);
            }
        }
        return parameters;
    }


}
