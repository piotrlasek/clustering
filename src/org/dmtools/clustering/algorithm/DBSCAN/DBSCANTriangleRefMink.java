package org.dmtools.clustering.algorithm.DBSCAN;

import java.util.ArrayList;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.algorithm.NBC.Point;
import org.dmtools.clustering.model.IClusteringParameters;


import util.Distance;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANTriangleRefMink extends DBSCANTriangle {

    // System variables
    public static final String NAME = "TI_DBSCAN_REF_MINK            ";

    int m = 0;
    
    ArrayList<Point> RefPoints = new ArrayList<Point>();

    public String getName() {
        return DBSCANTriangleRefMink.NAME;
    }

    /**
     * 
     */
    @Override
    protected void TI_DBSCAN() {

        Integer ClusterId = nextId(CDMCluster.NOISE);

        // set all points as UNCLASSIFIED
        for (Point point : D) {
            point.ClusterId = CDMCluster.UNCLASSIFIED;
            // for i = 1 to |RefPoints| do
            for (int i = 0; i < RefPoints.size(); i++) {
                // p.Dists[i] = Distance(p,RefPoints[i]);
                //point.Dists.add(Distance.Distance(point, RefPoints.get(i)));\
                point.Dists.add(Distance.DistanceMink(point, RefPoints.get(i), m));
                // endfor
            }
        }

        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            if (p != null && p.ClusterId == CDMCluster.UNCLASSIFIED) {
                if (TI_ExpandCluster(D, p, ClusterId, Eps, MinPts)) {
                    ClusterId = nextId(ClusterId);
                }
            }
        }
    }

    @Override
    protected void createIndex() {

        // create reference points table
        determineBorderCoordinates();        
        createRefPoints();
        sortAllPointsInD(RefPoints.get(0));
    }

    public void createRefPoints() {
        // TODO
        // Wyznaczanie punkt�w referencyjnych:
        // Warto�� atrybutu w danym wymiarze: maksymalna, pozosta�e warto�ci
        // atrybut�w: minimalne.
        
        zero = new Point(minCoordinates);
        RefPoints.add(zero);
        
        double minCoord = 0;
        
// -------------------------------------------------------------
//        for (int i = 0; i < nDim; i++) {
//            int j = 0;
//            Point minPoint = null;
//            for (IClusteringObject co : input) {
//                Point point = new Point(co.getSpatialObject().getValues().clone());
//                if (j == 0) {
//                    minCoord = point.getValues()[i];
//                }
//                if (point.getValues()[i] <= minCoord) {
//                    minCoord = point.getValues()[i];
//                    minPoint = point;
//                }
//                j++;
//            }
//            RefPoints.add(minPoint);
//        }

// -------------------------------------------------------------
        for (int i = 1; i < nDim; i++) {
            double[] coordinates = minCoordinates.clone();
            coordinates[i] = maxCoordinates[i];
            Point minPoint = new Point(coordinates);
            RefPoints.add(minPoint);
        }
   
// -------------------------------------------------------------
//        for(int i = 0; i < nDim; i++) {
//            double r = Math.random() * input.size();
//            ArrayList<IClusteringObject> in = (ArrayList<IClusteringObject>) input;
//            IClusteringObject ob = in.get((int) r);
//            Point p = new Point(ob.getSpatialObject().getValues().clone());
//            RefPoints.add(p);
//        }
        
    }

    @Override
    public ArrayList<Point> TI_Backward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        // function TI-Backward-Neighborhood(D, point p, Eps, MinPts)
        /* assert: D is ordered non-decreasingly w.r.t. dist */
        // seeds = {};
        ArrayList<Point> seeds = new ArrayList<Point>();
        // backwardThreshold = p.Dists[1] - Eps
        double backwardThreshold = p.Dists.get(0) - Eps;
        // for each point q in the ordered set D starting from
        // the point immediately preceding point p until
        // the first point in D do
        for (int index = p.pos - 1; index >= 0; index--) {
            Point q = D.get(index);
            if (q != null) {
                // if q.Dists[1] < backwardThreshold then
                // break;
                // endif
                if (q.Dists.get(0) < backwardThreshold) {
                    break;
                }
                // candidateNeighbor = true;
                boolean candidateNeighbor = true;
                // i = 2;
                int i = 1;
                // while candidateNeighbor and (i <= |p.Dists[i]|) do
                while (candidateNeighbor && (i < p.Dists.size())) {
                    // if |q.Dists[i] � p.Dists[i]| > Eps then
                    if (q.Dists.get(i) - p.Dists.get(i) > Eps) {
                        // candidateNeighbor = false
                        candidateNeighbor = false;
                        // else
                    } else {
                        // i = i + 1
                        i = i + 1;
                        // endif
                    }
                    // while
                }

                // if candidateNeighbor then
                if (candidateNeighbor) {
                    // if Distance2(q, p) <= Eps2 then
                    if (Distance.DistanceM2(q, p, m) <= (Eps * Eps)) {
                        // append q to seeds;
                        seeds.add(q);
                        // endif
                    }
                    // endif
                }

            }
            // endfor
        }
        // return seeds
        return seeds;
    }

    @Override
    public ArrayList<Point> TI_Forward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        // function TI-Forward-Neighborhood(D, point p, Eps, MinPts)
        /* assert: D is ordered non-decreasingly w.r.t. Dists[1] */
        // seeds = {};
        ArrayList<Point> seeds = new ArrayList<Point>();
        // forwardThreshold = Eps + p.Dists[1]
        double forwardThreshold = Eps + p.Dists.get(0);
        // for each point q in the ordered set D starting from
        // the point immediately following point p until
        // the last point in D do
        for (int index = p.pos + 1; index < D.size() - 1; index++) {
            Point q = D.get(index);
            if (q != null) {
                // if q.Dists[1] > forwardThreshold then
                if (q.Dists.get(0) > forwardThreshold) {
                    // break;
                    break;
                    // endif
                }
                // candidateNeighbor = true;
                boolean candidateNeighbor = true;
                // i = 2;
                int i = 1;
                // while candidateNeighbor and (i <= |p.Dists[i]|) do
                while (candidateNeighbor && (i < p.Dists.size())) {
                    // if |q.Dists[i] � p.Dists[i]| > Eps then
                    if (q.Dists.get(i) - p.Dists.get(i) > Eps) {
                        // candidateNeighbor = false
                        candidateNeighbor = false;
                        // else
                    } else {
                        // i = i + 1
                        i = i + 1;
                        // endif
                    }
                    // while
                }
                // if candidateNeighbor then
                if (candidateNeighbor) {
                    // if Distance2(q, p) <= Eps2 then
                    if (Distance.DistanceM2(q, p, m) <= (Eps * Eps)) {
                        // append q to seeds;
                        seeds.add(q);
                        // endif
                    }
                    // endif
                }
            }
            // endfor
        }
        // return seeds
        return seeds;
    }
    
    
    @Override
    public void setParameters(IClusteringParameters parameters)
    {
        super.setParameters(parameters);
        m = new Integer(parameters.getValue("m")).intValue();
    }
}
