package org.dmtools.clustering.DBSCAN;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;

import org.dmtools.clustering.CDMCluster;
import org.dmtools.clustering.old.BasicClusterInfo;
import org.dmtools.clustering.old.BasicClusteringData;
import org.dmtools.clustering.old.BasicClusteringObject;
import org.dmtools.clustering.old.BasicSpatialObject;
import org.dmtools.clustering.old.ClusteringLogger;
import org.dmtools.clustering.old.IClusteringAlgorithm;
import org.dmtools.clustering.old.IClusteringData;
import org.dmtools.clustering.old.IClusteringObject;
import org.dmtools.clustering.old.IClusteringParameters;
import org.dmtools.clustering.old.IClusteringObserver;
import org.dmtools.clustering.old.ISpatialIndex;
import org.dmtools.clustering.old.ISpatialObject;


/**
 * The DBSCAN algorithm base class.
 * 
 * @author Piotr Lasek
 * 
 */
public abstract class DBSCANBase implements IClusteringAlgorithm {

    /**
     * 
     */
    ArrayList<ISpatialObject> SetOfPoints;
    
    /**
     * 
     */
    String description;


    /**
     * 
     */
    //String desc = getName();

    /**
     * Number of dimensions.
     */
    int nDim = 0;

    /**
     * Number of bits per dimension.
     */
    int b = 0;

    /**
     * A reference to the index.
     */
    ISpatialIndex isp;

    /**
     * 
     */
    Double Eps = 0.0;

    /**
     * 
     */
    Integer MinPts = 0;

    /**
     * 
     */
    protected IClusteringObserver observer;

    /**
     * 
     */
    IClusteringParameters parameters;

    /**
     * 
     */
    IClusteringData data;

    /**
     * 
     */
    Graphics graphics = null;

    /**
     * 
     */
    ClusteringLogger logger = new ClusteringLogger(getName());
    
    /**
     * 
     */
    protected abstract void createIndex();

    @Override
    public IClusteringParameters getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void run() {
        logger.addDescription(this.getDescription());
        //long begin_time1 = System.currentTimeMillis();
        logger.clusteringStart();
        DBSCAN();
        long end_time1 = System.currentTimeMillis();
        logger.clusteringEnd();
        try { addLines(); } catch (Exception e) {e.printStackTrace();}
        //desc += " Total = " + (end_time1 - begin_time1) + " ms " + parameters.toString();
        try { observer.handleNotify(logger.getLog()); } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void setParameters(IClusteringParameters parameters) {
        this.parameters = parameters;
        try {
            b = new Integer(parameters.getValue("b")).intValue();
        } catch (Exception e) {
            b = -1;
        }
        Eps = new Double(new Integer(parameters.getValue("Eps")));
        MinPts = new Integer(parameters.getValue("minPts"));
        logger.setParameters(parameters.toString());
    }

    /**
     * 
     */
    protected int nextId(int id) {
        return ++id;
    }
     
    // DBSCAN specific methods
    // -----------------------------------------------------------------------

    protected void DBSCAN() {
        // SetOfPoints is UNCLASSIFIED
        Integer ClusterId = nextId(CDMCluster.NOISE);
        
        for (ISpatialObject point : SetOfPoints) {
            //BasicSpatialObject point = (DbscanSpatialObject) p;
            //point.ClId = UNCLASSIFIED;
            setClId(point, CDMCluster.UNCLASSIFIED);
        }

        // ClusterId := nextId(NOISE);
        // FOR i FROM 1 TO SetOfPoints.size DO
        // Point := SetOfPoints.get(i);
        for (ISpatialObject Point : SetOfPoints) {
            //DbscanSpatialObject Point = (DbscanSpatialObject) p;
            // IF Point.ClId = UNCLASSIFIED THEN
            ///if (Point.ClId == UNCLASSIFIED)
            if (getClId(Point) == CDMCluster.UNCLASSIFIED)
            // IF ExpandCluster(SetOfPoints, Point, ClusterId, Eps, MinPts) THEN
            {
                if (ExpandCluster(SetOfPoints, Point, ClusterId, Eps, MinPts)) {
                    // ClusterId := nextId(ClusterId)
                    ClusterId = nextId(ClusterId);
                    // END IF
                }
                // END IF
            }
        }
        // END FOR
        // END;
    } // DBSCAN

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
    private boolean ExpandCluster(ArrayList<ISpatialObject> SetOfPoints,
            ISpatialObject Point, Integer ClId, Double Eps, Integer MinPts) {
        // seeds :=SetOfPoints.regionQuery(Point,Eps);
        ArrayList<ISpatialObject> seeds1 = regionQuery(Point, Eps);
        ArrayList<ISpatialObject> seeds = new ArrayList<ISpatialObject>();
        seeds.addAll(seeds1);
        // IF seeds.size<MinPts THEN // no core point
        if (seeds.size() < MinPts) {
            // SetOfPoint.changeClId(Point,NOISE);
            changeClId(Point, CDMCluster.NOISE);
            // RETURN False;
            return false;
        }
        // ELSE // all points in seeds are density-reachable from Point
        else {
            // SetOfPoints.changeClIds(seeds,ClId);
            changeClId(seeds, ClId);
            // seeds.delete(Point);
            //seeds.remove(Point);
            changeClId(Point, ClId);
            // WHILE seeds <> Empty DO
            while (seeds.size() > 0) {
                // currentP := seeds.first();
                ISpatialObject currentP = seeds.get(0);
                // result := SetOfPoints.regionQuery(currentP, Eps);
                ArrayList<ISpatialObject> result = regionQuery(currentP, Eps);
                // IF result.size >= MinPts THEN
                if (result.size() >= MinPts) {
                    // FOR i FROM 1 TO result.size DO
                    for (int i = 1; i < result.size(); i++) {
                        // resultP := result.get(i);
                        //DbscanSpatialObject resultP = (DbscanSpatialObject) result
                        //        .get(i);
                        ISpatialObject resultP = result.get(i);
                        // IF resultP.ClId IN {UNCLASSIFIED, NOISE} THEN
                        if (getClId(resultP) == CDMCluster.UNCLASSIFIED
                                || getClId(resultP) == CDMCluster.NOISE) {
                            // IF resultP.ClId = UNCLASSIFIED THEN
                            if (getClId(resultP) == CDMCluster.UNCLASSIFIED) {
                                // seeds.append(resultP);
                                seeds.add(resultP);
                                // END IF;
                            }
                            // SetOfPoints.changeClId(resultP,ClId);
                            changeClId(resultP, ClId);
                            // END IF; // UNCLASSIFIED or NOISE
                        }
                        // END FOR;
                    }
                    // END IF; // result.size >= MinPts
                }
                // seeds.delete(currentP);
                seeds.remove(0);
                // END WHILE; // seeds <> Empty
            }
            // RETURN True;
            return true;
            // END IF
        }
        // END; // ExpandCluster
    }

    /**
     * 
     * @param Point
     * @param Eps
     * @return
     */
    private ArrayList<ISpatialObject> regionQuery(ISpatialObject Point,
            double Eps) {
        ArrayList<ISpatialObject> neighbors = (ArrayList<ISpatialObject>) isp.getNeighbors(Point, Eps);
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
     */
    public void changeClId(ISpatialObject Point, int ClId) {
        //DbscanSpatialObject o = (DbscanSpatialObject) Point;
        //o.ClId = ClId;
        setClId(Point, ClId);
    }

    /**
     * 
     * @param Point
     * @return
     */
    public int getClId(ISpatialObject Point)
    {
        return Point.getValue();
    }

    /**
     * 
     * @param Point
     * @param ClId
     */
    public void setClId(ISpatialObject Point, int ClId)
    {
        Point.setValue(ClId);
    }

    
    @Override
    public IClusteringData getResult() {
        BasicClusteringData bcd = new BasicClusteringData();
        ArrayList<IClusteringObject> al = new ArrayList<IClusteringObject>();
        for (Object o : SetOfPoints) {
            BasicSpatialObject mp = (BasicSpatialObject) o;
            // observer.handleNotify(mp.excell());
            BasicClusteringObject bco = new BasicClusteringObject();
            //BasicSpatialObject rso = new BasicSpatialObject(mp.getCoordinates());
            bco.setSpatialObject(mp);
            BasicClusterInfo bci = new BasicClusterInfo();
            bci.setClusterId(getClId(mp));
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

        Collection<IClusteringObject> input = data.get();
        SetOfPoints = new ArrayList<ISpatialObject>();

        for (IClusteringObject co : input) {
            SetOfPoints.add(new BasicSpatialObject(co.getSpatialObject().getCoordinates()));
        }

        logger.indexStart();
        createIndex();
        isp.add((ArrayList<ISpatialObject>) SetOfPoints);
        logger.indexEnd();
    }

    @Override
    public void setGraphics(Graphics g) {
        this.graphics = g;
    }

    @Override
    public void setObserver(IClusteringObserver observer) {
        this.observer = observer;
    }

    /**
     * 
     * @return
     */
    public double[] getCellSizes() {
        //return ((BasicVAFile) isp).getCellSizes();
    	return null;
    }

    /**
     * 
     */
    public void addLines() {
        this.observer.handleNotify(getCellSizes());
    }
    
    /**
     * 
     */
    public void addDescription(String description) {
        this.description = description;
    }
    
    /**
     * 
     */
    public String getDescription() {
        return description;
    }
}
