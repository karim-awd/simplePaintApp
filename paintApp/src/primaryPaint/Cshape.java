package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;


public  class Cshape extends JComponent implements Shape {
	
	protected Point p;
	protected Map<String, Double> prop;
	protected Color c;
	protected Color fill;
	
	
	@Override
	public void setPosition(Point position) {
		// TODO Auto-generated method stub
		this.p=position;	
	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return p;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		// TODO Auto-generated method stub
		this.prop=properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		// TODO Auto-generated method stub
		return prop;
	}

	@Override
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		this.c=color;
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return c;
	}

	@Override
	public void setFillColor(Color color) {
		// TODO Auto-generated method stub
		
		this.fill=color;
	}

	@Override
	public Color getFillColor() {
		// TODO Auto-generated method stub
		return this.fill;
	}
	
	@Override
	public void  draw(Graphics g) {} ;
	
	public Object clone() throws CloneNotSupportedException{
		return null;
		
	}
}
