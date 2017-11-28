package primaryPaint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MVC {
	
	public static void main (String [] args) throws ClassNotFoundException, IOException
	{
		View appView = new View ();
		MyDrawingEngine engine = new MyDrawingEngine(appView);
		Control control = new Control (appView,engine);
	}

}
