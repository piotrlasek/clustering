package org.dmtools.clustering.algorithm.common;

/**
 * Created by Piotr on 06.06.2017.
 */


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains a method to generate N visually distinct colors and helper methods.
 *
 * @author Melinda Green
 */
public class ColorUtils {

    public static ArrayList<Color> createUniqueColors(int nColors)
    {
        System.out.println("Colors: " + nColors);
        ArrayList<Color> colors = new ArrayList();

        float deltaH = (float) 360 / (float) nColors;
        deltaH = deltaH / (float) 360;
        float hue = 0;
        for (int i = 0; i < nColors; i++) {
            Color c = Color.getHSBColor(hue, 1, 1);
            hue += deltaH;
            colors.add(c);
        }

        Collections.shuffle(colors);

        return colors;
    }
}
