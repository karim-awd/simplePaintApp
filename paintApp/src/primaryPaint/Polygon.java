package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class Polygon extends Cshape{	
	public void draw (Graphics g)
	{
		int [] xList = new int[3];
		int [] yList = new int [3];
		xList[0]= (int) prop.get("X1").intValue();
		yList[0]= (int) prop.get("Y1").intValue();
		xList[1]= (int) prop.get("X2").intValue();
		yList[1]= (int) prop.get("Y2").intValue();
		xList[2]= (int) prop.get("X3").intValue();
		yList[2]= (int) prop.get("Y3").intValue();
		
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        ((Graphics2D) g).setColor(getFillColor());
        ((Graphics2D) g).fillPolygon(xList,yList,3);
        ((Graphics2D) g).drawPolygon(xList,yList,3);	
	}
	public Object clone() throws CloneNotSupportedException{
		
		 Shape copy = new Line();
	        copy.setColor(c);
	        copy.setFillColor(fill);
	        copy.setPosition(p);
	        Map newprop = new HashMap<>();
	        for (Map.Entry s: prop.entrySet())
	            newprop.put(s.getKey(), s.getValue());
	        copy.setProperties(newprop);		
		return copy;
		
	}

}
