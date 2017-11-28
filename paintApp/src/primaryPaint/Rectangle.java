package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends Cshape{
	
	public void draw (Graphics g)
	{
		
			((Graphics2D) g).setColor(getFillColor());
	        ((Graphics2D) g).fillRect((int) p.getX(),
	                (int) p.getY(),
	                (int) prop.get("Width").intValue(),
	                (int) prop.get("Length").intValue());

	        ((Graphics2D) g).setStroke(new BasicStroke(2));
	        ((Graphics2D) g).setColor(getColor());
	        ((Graphics2D) g).drawRect((int) p.getX(),
	                (int) p.getY(),
	                (int) prop.get("Width").intValue(),
	                (int) prop.get("Length").intValue());
		
	}
	
	public Object clone() throws CloneNotSupportedException{
		
		 Shape copy = new Rectangle();
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
