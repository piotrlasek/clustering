package util;

import org.dmtools.clustering.NBC.Point;
import org.dmtools.clustering.old.ISpatialObject;


public class Distance {
    
    private static double table[][];

	// ---------------------------
    public static double getDistance(ISpatialObject a, ISpatialObject b) {
        return Math.sqrt(Distance2(a, b));
    }
    
    public static double getDistanceMink(ISpatialObject a, ISpatialObject b, int m) {
        return Distance.sqrtMink(DistanceM2(a, b, m), m);
    }
    
    
    public static double Distance(ISpatialObject a, ISpatialObject b) {
    	return getDistance(a, b);
    }
    
    public static double DistanceMink(ISpatialObject a, ISpatialObject b, int m) {
        return getDistanceMink(a, b, m);
    }
    
    public static double sqrt(double dist) {
        return Math.sqrt(dist);
    }
    
    public static double sqrtMink(double dist, double m)
    {
        return Math.pow(dist,  1/m);
        /*if (m == 2)
            return Math.sqrt(dist);
        else if (m == 1)
            return dist;
        else {
            System.out.println("sqrtMink ERROR");
            return -1;
        }*/
    }
    
    public static void init(int length) {
        Distance.table = new double[length][];
        for(int i = 0; i < table.length; i++) {
            table[i] = new double[length];
        }
    }
    
    public static double Distance2(ISpatialObject a, ISpatialObject b) {
        double dist = 0;
        double[] ac = a.getCoordinates();
        double[] bc = b.getCoordinates();
        for(int i = 0; i < ac.length; i++) {
            dist += Math.pow(ac[i] - bc[i], 2);
        }
        return dist;
    }

    public static double DistanceM2(ISpatialObject a, ISpatialObject b, int m) {
        double dist = 0;
        double[] ac = a.getCoordinates();
        double[] bc = b.getCoordinates();
        for(int i = 0; i < ac.length; i++) {
            dist += Math.pow(ac[i] - bc[i], m);
            //dist += Math.pow(ac[i] - bc[i], 2);
        }
        return dist;
    }

	public static double DistancePacket2(Point p, Point q) {

		double[] d1 = p.getCoordinates();
		double[] d2 = q.getCoordinates();
		
		double dist = 0;
		
		for(int i = 0; i < d1.length; i++) {
			if (d1[i] != d2[i]) dist++;
		}
		
		return dist / 6;
	}

    public static double DistancePacket(Point p, Point q) {
    	return DistancePacket2(p, q);
    }
}
