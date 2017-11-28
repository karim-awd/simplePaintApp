package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class Line extends Cshape{
	
	public void draw (Graphics g)
	{
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        ((Graphics2D) g).setColor(getFillColor());
        ((Graphics2D) g).drawLine(
        		(int) prop.get("X1").intValue(),
        		(int) prop.get("Y1").intValue(),
                (int) prop.get("X2").intValue(),
                (int) prop.get("Y2").intValue());
		
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
