package util;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.awt.AWTUtilities;

/**
 * 
 * @author Piotr Lasek
 *
 */
public class Painter extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<MyPoint> points = new ArrayList<MyPoint>();
	JPanel panel = new JPanel();
	
	/**
	 * 
	 */
	private class MyPoint {
		int x;
		int y;
		
		public MyPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * 
	 */
	public Painter() {
		super();
		
		this.add(panel);
		
		panel.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	int x = e.getX();
		    	int y = e.getY();
		    	
		        System.out.println("x: " + x + ", y: " + y);
		        panel.getGraphics().drawRect(x, y, 1, 1);
		        
		        MyPoint p = new MyPoint(x, y);
		        points.add(p);
		    }
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (MyPoint p : points) {
			int x = p.x;
			int y = p.y;
			panel.getGraphics().drawRect(x, y, 1, 1);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Painter p = new Painter();
		p.setSize(500,  500);
		p.pack();
		p.setVisible(true);
	    
	}
}
