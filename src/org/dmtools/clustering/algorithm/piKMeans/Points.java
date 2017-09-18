package org.dmtools.clustering.algorithm.piKMeans; /**
 * Created by Nasim on 5/4/2017.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Points extends JPanel {

    private int _final;
	private ArrayList<double[]> data;
    private ArrayList<double[]> tempPoints;
    private Color[] color = {Color.magenta,Color.CYAN,Color.BLUE,Color.RED,Color.PINK,Color.green,Color.BLACK};

    public Points(ArrayList<double[]> data, ArrayList<double[]> tmpPoints, int _final){
        this.data = new ArrayList<double[]>();
        this.data = data;
        tempPoints = tmpPoints;
        this._final = _final;
        //System.out.println("round: "+_final);
        setBackground(Color.white); 
        
    }

    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        
        
        //super.setOpaque(true);
        //super.setBackground(Color.RED);


        double minX , minY, maxX, maxY;

        //System.out.println("data.size()++++++++++++++++++++++++++: "+data.size());
        minX = data.get(0)[0];
        minY = data.get(0)[1];
        maxX = data.get(0)[0];
        maxY = data.get(0)[1];

        /*for(double[] point: data){

            if(point[0] < minX){
                minX = point[0];
            }else if(point[0] > maxX){
                maxX = point[0];
            }else if(point[1] < minY){
                minY = point[1];
            }else if(point[1] > maxY){
                maxY = point[1];
            }

        }*/

        for(double[] point: data){

            if(point[2] < minX){
                minX = point[2];
            }else if(point[2] > maxX){
                maxX = point[2];
            }else if(point[3] < minY){
                minY = point[3];
            }else if(point[3] > maxY){
                maxY = point[3];
            }

            if(point[4] < minX){
                minX = point[4];
            }else if(point[4] > maxX){
                maxX = point[4];
            }else if(point[5] < minY){
                minY = point[5];
            }else if(point[5] > maxY){
                maxY = point[5];
            }

            if(point[6] < minX){
                minX = point[6];
            }else if(point[6] > maxX){
                maxX = point[6];
            }else if(point[7] < minY){
                minY = point[7];
            }else if(point[7] > maxY){
                maxY = point[7];
            }

            if(point[8] < minX){
                minX = point[8];
            }else if(point[8] > maxX){
                maxX = point[8];
            }else if(point[9] < minY){
                minY = point[9];
            }else if(point[9] > maxY){
                maxY = point[9];
            }

        }

        double min = Math.min(minX, minY);
        double max = Math.max(maxX, maxY);

        //System.out.println("Min: "+min);
        //System.out.println("Max: "+max);

        Graphics2D g2d = (Graphics2D) g;
        //Graphics2D g3d = (Graphics2D) g;

        //g2d.setColor(Color.red);

        for (double[] point: data) {
        	if(point[15] == -1){
        		g2d.setColor(Color.black);
        		double normX = normalize(point[0], min, max, 760);
                double normY = 760 - normalize(point[1], min, max,760);
                Ellipse2D.Double shape = new Ellipse2D.Double(normX, normY, 10, 10);
                g2d.draw(shape);
        	}else{
        		g2d.setColor(color[(int) point[15]]);
        		double normX = normalize(point[0], min, max, 760);
                double normY = 760 - normalize(point[1], min, max,760);
                Ellipse2D.Double shape = new Ellipse2D.Double(normX, normY, 4, 4);
                g2d.fill(shape);
        	}
        	
            //double normX = normalize(point[0], minX, maxX, 960);
            //double normY = 510 - normalize(point[1], minY, maxY,510);
            
            //g2d.drawString(String.valueOf((int)point[10]), (int)shape.x, (int)shape.y);
        }

        if(_final == 0){
        //g2d.setColor(Color.black);
        for (double[] point: data) {
        	g2d.setColor(Color.gray);
        	
        	
            //double normX = normalize(point[0], minX, maxX, 960);
            //double normY = 510 - normalize(point[1], minY, maxY,510);
        	double x1,x2,x3,x4;
        	double y1,y2,y3,y4;
        	
            double normX = normalize(point[2], min, max, 760);
            double normY = 760 - normalize(point[3], min, max,760);
            x1 = normX;
            y1 = normY;
            Ellipse2D.Double shape = new Ellipse2D.Double(normX, normY, 5, 5);
            //g2d.draw(shape);
            //g2d.drawString(String.valueOf((int)point[2]) + "," + String.valueOf((int)point[3]), (int)shape.x, (int)shape.y);
            
            normX = normalize(point[4], min, max, 760);
            normY = 760 - normalize(point[5], min, max,760);
            x2 = normX;
            y2 = normY;
            shape = new Ellipse2D.Double(normX, normY, 5, 5);
            //g2d.draw(shape);
            //g2d.drawString(String.valueOf((int)point[4]) + "," + String.valueOf((int)point[5]), (int)shape.x, (int)shape.y);
            
            normX = normalize(point[6], min, max, 760);
            normY = 760 - normalize(point[7], min, max,760);
            x3 = normX;
            y3 = normY;
            shape = new Ellipse2D.Double(normX, normY, 5, 5);
            
            //g2d.draw(shape);
            //g2d.drawString(String.valueOf((int)point[6]) + "," + String.valueOf((int)point[7]), (int)shape.x, (int)shape.y);
            
            normX = normalize(point[8], min, max, 760);
            normY = 760 - normalize(point[9], min, max,760);
            x4 = normX;
            y4 = normY;
            shape = new Ellipse2D.Double(normX, normY, 5, 5);
            //g2d.draw(shape);
            //g2d.drawString(String.valueOf((int)point[8]) + "," + String.valueOf((int)point[9]), (int) shape.x, (int)shape.y);
            g2d.draw(new Line2D.Double(x1, y1, x2, y2));
            g2d.draw(new Line2D.Double(x1, y1, x3, y3));
            g2d.draw(new Line2D.Double(x2, y2, x4, y4));
            g2d.draw(new Line2D.Double(x3, y3, x4, y4));
        }
        }

        g2d.setColor(Color.green);

        int i = 0;

        for (double[] tmpPoint: tempPoints) {
            double normX = normalize(tmpPoint[0], min, max, 760);
            double normY = 760 - normalize(tmpPoint[1], min, max,760);
            Ellipse2D.Double shape = new Ellipse2D.Double(normX, normY, 10, 10);
            g2d.setColor(color[tempPoints.indexOf(tmpPoint)]);
            //System.out.println("point center: ("+i+" "+ tmpPoint[0]+","+tmpPoint[1]+")");
            i++;
            g2d.fill(shape);
            //g2d.drawString(String.valueOf((int)tmpPoint[0]) + "," + String.valueOf((int)tmpPoint[1]), (int) shape.x, (int)shape.y);
        }

        /*for (int i = 0; i <= 100; i++) {
            Dimension size = getSize();
            int w = size.width ;
            int h = size.height;

            Random r = new Random();
            int x = Math.abs(r.nextInt()) % w;
            int y = Math.abs(r.nextInt()) % h;
            //g2d.drawLine(x, y, x, y);
            //Graphics2D gd = (Graphics2D)g;
            //gd.drawOval(5, 5, 100, 100);

            //g3d.fillRect(30, 20, 50, 50);
            //g3d.fillRect(120, 20, 90, 60);
            //g3d.fillRoundRect(250, 20, 70, 60, 25, 25);
            //g3d.fill(new Ellipse2D.Double(10, 100, 80, 100));
            //g3d.fillArc(120, 130, 110, 100, 5, 150);


        }
        g3d.fillOval(-1, -1, 5, 5);
        g3d.fillOval(579, 357, 5, 5);
        g3d.fillOval(979, 557, 5, 5);*/
    }

    private int normalize(double number, double min, double max, double maxx){
        Double d;
        d = ((number - min)/(max - min))*(maxx);
        return d.intValue();
    }

}
