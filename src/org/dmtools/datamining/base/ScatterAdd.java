package org.dmtools.datamining.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dmtools.clustering.CDMBasicClusteringAlgorithm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class ScatterAdd extends JFrame {

    private static final int N = 8;
    private static final int SIZE = 345;
    private static final String title = "Dataset";

    ArrayList<double[]> data;
    ArrayList<double[]> tempPoints;

    protected final static Logger log = LogManager.getLogger(CDMBasicClusteringAlgorithm.class.getSimpleName());

    public ScatterAdd(String s, ArrayList<double[]> data, ArrayList<double[]> tempPoints) {
        super(s);
        this.data = data;
        this.tempPoints = tempPoints;
        final ChartPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(SIZE, SIZE));
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private ChartPanel createDemoPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            title, "X", "Y", createSampleData(),
            PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.LIGHT_GRAY);
        
        renderer.setToolTipGenerator(new XYToolTipGenerator()
        {
			@Override
			public String generateToolTip(XYDataset arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                return "abc";
            }
        }
        );
        
        renderer.setBaseItemLabelsVisible(true);
        
        for (int i = 0; i <= 20; i++) {
        	renderer.setSeriesShape(i, new Ellipse2D.Float(-1,-1,1,1));
        }
                
        adjustAxis((NumberAxis) xyPlot.getDomainAxis(), true);
        adjustAxis((NumberAxis) xyPlot.getRangeAxis(), false);
        //xyPlot.setBackgroundPaint(Color.white);
        return new ChartPanel(jfreechart);
    }

    private void adjustAxis(NumberAxis axis, boolean vertical) {
        //axis.setRange(0, 10.0);
        axis.setTickUnit(new NumberTickUnit(0.5));
        axis.setVerticalTickLabels(vertical);
    }

    private XYDataset createSampleData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        
        ArrayList<Double> clusters = new ArrayList<Double>();
        clusters.add((double) 0);
        
        ArrayList<XYSeries> tempSeries = new ArrayList<XYSeries>();

        XYSeries noise = new XYSeries("NOISE");
        
        tempSeries.add(noise);
        
        for(double[] point : data) {
        	if (point[point.length-1] >= 0)
        		if (!clusters.contains(point[point.length-1]))
        			clusters.add(point[point.length - 1]);
        }


        tempSeries.add(new XYSeries("Noise"));

        for(int i = 0; i < clusters.size(); i++)
        {
        	tempSeries.add(new XYSeries("Cluster " + i));
        	//System.out.println("Cluster " + i);
        }
        
        // ADD NOISE

        for(double[] point : data) {
        	int id = (int) point[point.length-1] + 1;
            XYSeries s = tempSeries.get(id);
            s.add(new XYDataItem(point[0], point[1]));
        	//System.out.println(Arrays.toString(point));
        }
        
        //
        for(XYSeries s : tempSeries) {
        	xySeriesCollection.addSeries(s);	
        }
	                
        //xySeriesCollection.addSeries(added);
        
        return xySeriesCollection;
    }  
}
