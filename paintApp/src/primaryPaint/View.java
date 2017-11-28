package primaryPaint;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class View extends JFrame {
	
	
	
	public View()
	{
		this.setTitle("Paint");
		this.setSize(1000,900);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	

}
