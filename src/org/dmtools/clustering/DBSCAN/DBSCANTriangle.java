package org.dmtools.clustering.DBSCAN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.dmtools.clustering.NBC.Point;
import org.dmtools.clustering.old.BasicClusterInfo;
import org.dmtools.clustering.old.BasicClusteringData;
import org.dmtools.clustering.old.BasicClusteringObject;
import org.dmtools.clustering.old.IClusteringData;
import org.dmtools.clustering.old.IClusteringObject;
import org.dmtools.clustering.old.ISpatialObject;

import util.Distance;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class DBSCANTriangle extends DBSCANBase {

    // System variables
    public static final String NAME = "TI_DBSCAN     ";
    Collection<IClusteringObject> input;

    // DBSCAN variables
    ArrayList<Point>  D = new ArrayList<Point>();
    ArrayList<Point>  D1 = new ArrayList<Point>();
    Point             zero;
    double[]          maxCoordinates;
    double[]          minCoordinates;
    
    /**
     * 
     */
    protected void TI_DBSCAN() {
        // Algorithm TI-DBSCAN(set of points D, Eps, MinPts);
        // /* assert: 0 denotes a referential point, */
        // /* e.g. the point with all coordinates equal to 0 */
        // ClusterId = label of first cluster;
        // for each point p in set D do
        // p.dist = Distance(p,0);
        // p.NeighborsNo = 1;
        // p.Border = {};
        // endfor
        // sort all points in D non-decreasingly w.r.t. attribute dist;
        // for each point p in the ordered set D starting from
        // the first point until last point in D do
        // if TI-ExpandCluster(D, p, ClusterId, Eps, MinPts) then
        // ClusterId = NextId(ClusterId)
        // endif
        // endfor

        Integer ClusterId = nextId(NOISE);

        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            p.dist = Distance.Distance(p, zero);
        }

        // sorting in createIndex

        // set all points as UNCLASSIFIED
        for (Point point : D) {
            point.ClusterId = UNCLASSIFIED;
        }

        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            if (p != null && p.ClusterId == UNCLASSIFIED) {
                if (TI_ExpandCluster(D, p, ClusterId, Eps, MinPts)) {
                    ClusterId = nextId(ClusterId);
                }
            }
        }
    }

    // function TI-Neighborhood(D, point p, Eps, MinPts)
    // return TI-Backward-Neighborhood(D, p, Eps, MinPts) +
    // TI-Forward-Neighborhood(D, p, Eps, MinPts)
    public ArrayList<Point> TI_Neighborhood(ArrayList<Point> D, Point p,
            double Eps, int MinPts) {
        ArrayList<Point> backwardNeighborhood = TI_Backward_Neighborhood(D, p,
                Eps, MinPts);
        ArrayList<Point> forwardNeighborhood = TI_Forward_Neighborhood(D, p,
                Eps, MinPts);
        ArrayList<Point> neighborhood = new ArrayList<Point>(
                forwardNeighborhood);
        neighborhood.addAll(backwardNeighborhood);
        return neighborhood;
    }

    public ArrayList<Point> TI_Backward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        // function TI-Backward-Neighborhood(D, point p, Eps, MinPts)
        /* assert: D is ordered non-decreasingly w.r.t. dist */
        // seeds = {};
        ArrayList<Point> seeds = new ArrayList<Point>();
        // backwardThreshold = p.dist - Eps;
        double backwardThreshold = p.dist - Eps;
        // for each point q in the ordered set D starting from
        // the point immediately preceding point p until
        // the first point in D do
        for (int i = p.pos - 1; i >= 0; i--) {
            Point q = D.get(i);
            if (q != null) {
                // if q.dist < backwardThreshold then // p.dist – q.dist > Eps
                // break;
                // endif
                if (q.dist < backwardThreshold) {
                    break;
                }
                // if Distance2(q, p) <= Eps2 then
                // append q to seeds;
                // endif
                if (Distance.Distance2(q, p) <= (Eps * Eps)) {
                    seeds.add(q);
                }
            }
            // endfor
        }
        // return seeds
        return seeds;
    }

    public ArrayList<Point> TI_Forward_Neighborhood(ArrayList<Point> D,
            Point p, double Eps, int MinPts) {
        // function TI-Forward-Neighborhood(D, point p, Eps, MinPts)
        /* assert: D is ordered non-decreasingly w.r.t. dist */
        // seeds = {};
        ArrayList<Point> seeds = new ArrayList<Point>();
        // forwardThreshold = Eps + p.dist;
        double forwardThreshold = Eps + p.dist;
        // for each point q in the ordered set D starting from
        // the point immediately following point p until
        // the last point in D do
        for (int i = p.pos + 1; i < D.size(); i++) {
            Point q = D.get(i);
            if (q != null) {
                // if q.dist > forwardThreshold then // q.dist – p.dist > Eps?
                // break;
                // endif
                if (q.dist > forwardThreshold) {
                    break;
                }
                // if Distance2(q, p) <= Eps2 then
                // append q to seeds;
                // endif
                if (Distance.Distance2(q, p) <= (Eps * Eps)) {
                    seeds.add(q);
                }
            }
            // endfor
            // return seeds
        }
        return seeds;
    }

    /**
     * 
     * 
     * @param SetOfPoints
     * @param Point
     * @param ClusterId
     * @param Eps
     * @param MinPts
     * @return
     */
    // ExpandCluster(SetOfPoints, Point, ClId, Eps, MinPts) : Boolean;
    protected boolean TI_ExpandCluster(ArrayList<Point> D, Point p,
            Integer ClId, Double Eps, Integer MinPts) {
        // function TI-ExpandCluster(D, point p, ClId, Eps, MinPts)
        /* Assert: TI-Neighborhood does not include p */
        // seeds = TI-Neighborhood(D, p, Eps, MinPts);
        ArrayList<Point> seeds = TI_Neighborhood(D, p, Eps, MinPts);
        // ArrayList<Point> seeds = TI_Neighborhood(D, p, Eps, MinPts);
        // p.NeighborsNo = p.NeighborsNo + |seeds|; // include p itself
        p.NeighborsNo = p.NeighborsNo + seeds.size();
        // if p.NeighborsNo < MinPts then
        if (p.NeighborsNo < MinPts) {
            // p.ClusterId = NOISE;
            p.ClusterId = NOISE;
            // for each point q in seeds do
            for (Point q : seeds) {
                // append p to q.Border
                q.Border.add(p);
                // q.NeighborsNo = q.NeighborsNo + 1
                q.NeighborsNo = q.NeighborsNo + 1;
                // endfor
            }
            // move p from D to D’; // D’ stores analyzed points
            // return FALSE
            return false;
            // else
        } else {
            // p.ClusterId = ClId;
            p.ClusterId = ClId;
            // for each point q in seeds do
            for (Point q : seeds) {
                // q.ClusterId = ClId;
                q.ClusterId = ClId;
                // q.NeighborsNo = q.NeighborsNo + 1;
                q.NeighborsNo = q.NeighborsNo + 1;
                // endfor
            }
            // for each point q in p.Border do
            while (p.Border.size() > 0) {
                Point q = p.Border.get(0);
                // D’.q.ClusterId = ClId; //assign cluster id to q in D’
                q.ClusterId = ClId;
                D1.add(q);
                q.pos2 = D1.size() - 1;
                // delete q from p.Border;
                p.Border.remove(0);
                // endfor
            }
            // move p from D to D’; // D’ stores analyzed points
            // Point p1 = D.remove(p.pos);
            Point p1 = D.get(p.pos);
            D.set(p1.pos, null);
            p1.pos = -1;
            D1.add(p1);
            p1.pos2 = D1.size() - 1;
            // while |seeds| > 0 do
            while (seeds.size() > 0) {
                // curPoint = first point in seeds;
                Point curPoint = seeds.get(0);
                // curSeeds = TI-Neighborhood(D, curPoint, Eps, MinPts);
                ArrayList<Point> curSeeds = TI_Neighborhood(D, curPoint, Eps,
                        MinPts);
                // curPoint.NeighborsNo = curPoint.NeighborsNo + |curSeeds|;
                curPoint.NeighborsNo = curPoint.NeighborsNo + curSeeds.size();
                // if curPoint.NeighborsNo < MinPts then
                if (curPoint.NeighborsNo < MinPts) {
                    // for each point q in curSeeds do
                    for (Point q : curSeeds) {
                        // append p to q.Border
                        q.Border.add(p);
                        // q.NeighborsNo = q.NeighborsNo + 1
                        q.NeighborsNo = q.NeighborsNo + 1;
                        // endfor
                    }
                    // for each point q in p.Border do
                    // delete q from p.Border;
                    // endfor
                    p.Border.clear();
                    // else
                } else {
                    // for each point q in curSeeds do
                    while (curSeeds.size() > 0) {
                        // for(Point q:curSeeds) {
                        Point q = curSeeds.get(0);
                        // q.NeighborsNo = q.NeighborsNo + 1;
                        q.NeighborsNo = q.NeighborsNo + 1;
                        // if q.ClusterId = UNCLASSIFIED then
                        if (q.ClusterId == UNCLASSIFIED) {
                            // q.ClusterId = ClId;
                            q.ClusterId = ClId;
                            // move q from curSeeds to seeds;
                            Point q1 = curSeeds.remove(0);
                            seeds.add(q1);
                            // else
                        } else {
                            // delete q from curSeeds;
                            curSeeds.remove(0);
                            // endif
                        }
                        // endfor
                    }
                    // for each point q in p.Border do

                    while (p.Border.size() > 0) {
                        Point q = p.Border.get(0);
                        // D’.q.ClusterId = ClId;//assign cl.id to q in D’
                        D1.get(q.pos2).ClusterId = ClId;
                        // delete q from p.Border;
                        p.Border.remove(q.pos);
                        // endfor
                    }
                    // endif
                }
                // move curPoint from D to D’; // D’ stores analyzed points
                D1.add(curPoint);
                curPoint.pos2 = D1.size() - 1;
                // delete curPoint from seeds;
                seeds.remove(0);
                // endwhile
            }
            // return TRUE
            return true;
            // endif
        }
    }

    /**
     * 
     * @param Point
     * @param Eps
     * @return
     */
    private ArrayList<ISpatialObject> regionQuery(ISpatialObject Point,
            double Eps) {
        ArrayList<ISpatialObject> neighbors = (ArrayList<ISpatialObject>) isp
                .getNeighbors(Point, Eps);
        return neighbors;
    }

    /**
     * 
     * @param Points
     * @param ClId
     * @return
     */
    public void changeClId(ArrayList<ISpatialObject> Points, int ClId) {
        for (ISpatialObject p : Points)
            changeClId(p, ClId);
    }

    /**
     * 
     * @param Point
     * @param NOISE
     */
    public void changeClId(ISpatialObject Point, int ClId) {
        // DbscanSpatialObject o = (DbscanSpatialObject) Point;
    }

    /**
     * 
     * @param Point
     * @return
     */
    public int getClId(ISpatialObject Point) {
        return Point.getValue();
    }

    /**
     * 
     * @param Point
     * @param ClId
     */
    public void setClId(ISpatialObject Point, int ClId) {
        Point.setValue(ClId);
    }

    // ---------------------------------------------------------------------------------

    /**
     * 
     * @param refPoint
     */
    public void sortAllPointsInD(Point refPoint) {
        // TODO
        createSortedTableD(input, refPoint);
    }

    
    /**
     * 
     * @param input
     * @param refPoint
     */
    public void createSortedTableD(Collection<IClusteringObject> input,
            Point refPoint) {

        for (IClusteringObject o : input) {

            Point point = new Point(o.getSpatialObject().getCoordinates());

            double distance = Distance.Distance(refPoint, point);

            point.dist = distance;

            if (D.size() == 0) {
                D.add(point);
            } else {
                for (int i = 0; i < D.size(); i++) {
                    Point tip = D.get(i);

                    if (distance < tip.dist) {
                        // insert before the current point
                        D.add(i, point);
                        if (i > 0) {
                            Point pr = D.get(i - 1);
                        }
                        break;
                    } else if (i == D.size() - 1) {
                        // insert at the end
                        D.add(point);
                        break;
                    } else {
                        // compare to the next point
                        ;
                    }
                }
            }
        }
        for (int i = 0; i < D.size(); i++) {
            Point p = D.get(i);
            p.pos = i;
        }

    }

    @Override
    protected void DBSCAN() {
        TI_DBSCAN();
    }

    // ---------------------------

    @Override
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();
        
        for (Point p : D) {
            if (p != null) {
                BasicClusteringObject bco = new BasicClusteringObject();
                bco.setSpatialObject(p);
                BasicClusterInfo bci = new BasicClusterInfo();
                bci.setClusterId(p.ClusterId);
                bco.setClusterInfo(bci);
                al.add(bco);
            }
        }
        
        for (Point p : D1) {
            BasicClusteringObject bco = new BasicClusteringObject();
            bco.setSpatialObject(p);
            BasicClusterInfo bci = new BasicClusterInfo();
            bci.setClusterId(p.ClusterId);
            bco.setClusterInfo(bci);
            al.add(bco);
        }
        bcd.set(al);
        return bcd;
    }

    @Override
    public void setData(IClusteringData data) {

        nDim = data.get().iterator().next().getSpatialObject().getCoordinates().length;

        this.data = (BasicClusteringData) data;

        input = data.get();

        nDim = input.iterator().next().getSpatialObject().getCoordinates().length;
        
        logger.indexStart();
        createIndex();
        logger.indexEnd();
    }

    public void addLines() {
        // 
    }

    @Override
    protected void createIndex() {
        // this.isp = new TriangleIndex();
        determineBorderCoordinates();
        zero = new Point(minCoordinates);
        sortAllPointsInD(zero);
    }

    protected void determineBorderCoordinates() {
        minCoordinates = input.iterator().next().getSpatialObject().getCoordinates().clone();
        maxCoordinates = minCoordinates.clone(); 

        for (IClusteringObject o : input) {
            double[] coordinates = o.getSpatialObject().getCoordinates();
            for(int i = 0; i < coordinates.length; i++) {
                
                if (minCoordinates[i] > coordinates[i]) {
                    minCoordinates[i] = coordinates[i];
                }
                    
                if (maxCoordinates[i] < coordinates[i]) {
                    maxCoordinates[i] = coordinates[i];
                }
            }
        }
    }

    @Override
    public String getName() {
        return DBSCANTriangle.NAME;
    }
}
