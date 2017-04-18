package util;

import java.io.IOException;

/**
 * Created by Piotr Lasek on 01.03.2017.
 */
public class Workspace {

    /**
     * Returns current path.
     * @return
     */
    public static String getWorkspacePath() {

        String currentWorkspacePath = null;
        try {
            currentWorkspacePath = new java.io.File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentWorkspacePath;
    }

}
