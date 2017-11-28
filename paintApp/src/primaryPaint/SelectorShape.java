package primaryPaint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SelectorShape extends Cshape{
	
	public void draw (Graphics g)
	{
		
			((Graphics2D) g).setColor(getFillColor());
	        ((Graphics2D) g).setStroke(new BasicStroke(2));
	        ((Graphics2D) g).setColor(getColor());
	        ((Graphics2D) g).drawRect((int) p.getX(),
	                (int) p.getY(),
	                (int) prop.get("Width").intValue(),
	                (int) prop.get("Length").intValue());
		
	}

}
