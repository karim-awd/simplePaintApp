package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class Ellipse extends Cshape {
	
	public void draw (Graphics g) {
		// TODO Auto-generated method stub
		
			((Graphics2D) g).setColor(getFillColor());
	        ((Graphics2D) g).fillArc((int) p.getX(),
	                (int) p.getY(),
	                (int) prop.get("Width").intValue(),
	                (int) prop.get("Length").intValue(),
	                (int) prop.get("startAngle").intValue(), 
	                (int) prop.get("arcAngle").intValue()
	        		);
	        

	        ((Graphics2D) g).setStroke(new BasicStroke(2));
	        ((Graphics2D) g).setColor(getColor());
	        ((Graphics2D) g).drawArc((int) p.getX(),
	                (int) p.getY(),
	                (int) prop.get("Width").intValue(),
	                (int) prop.get("Length").intValue(),
	                (int) prop.get("startAngle").intValue(), 
	                (int) prop.get("arcAngle").intValue()
	                
	        		);
	}
	
	public Object clone() throws CloneNotSupportedException{
		
		 Shape copy = new Ellipse();
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
