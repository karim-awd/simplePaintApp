package primaryPaint;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;

public class Control extends JPanel implements ActionListener,MouseMotionListener{

	private String chosen;
	private String shapeType;
	private View view;
	private MyDrawingEngine engine;
	private Point startDrag, endDrag;
	private JCheckBox makeTransparentCheckBox;
	private JTextField Warning;
	private int red=0;
	private int green=0;
	private int blue =0;

	private JTextArea shapeProperties;
	  
	private String mode =null;
	private Cshape extraShape;
	private int editedShapeIndex;
	private Cshape newSelector;
	private boolean flag=false; //a flag used when a shaped is moved to assure that only the first position is remembered for the undo action
	private boolean flagResize=false;
	
	private JScrollPane scrollPane;
	private JTextArea  textArea;
	
	public Control(View v, MyDrawingEngine e) throws ClassNotFoundException, IOException {

		this.view = v;
		this.engine = e;
		view.getContentPane().setLayout(null);
		this.setBounds(0, 150, 1980,880);
	
		/****************************CheckBoxes*****************************/
		/*Stroke CheckBox*/
		JCheckBox stroke = new JCheckBox("Stroke");
		stroke.setBounds(250, 90, 65, 15);
		view.getContentPane().add(stroke);
		
		/*Transparent CheckBox*/
		makeTransparentCheckBox = new JCheckBox("Transparent");
		makeTransparentCheckBox.setBounds(380, 90, 95, 15);
		view.getContentPane().add(makeTransparentCheckBox);
		
		/*Regulars CheckBox*/
		JCheckBox regulars = new JCheckBox("Regular");
		regulars.setBounds(315, 90, 70, 15);
		view.getContentPane().add(regulars);
		
		//List Of Shapes On Canvas
		scrollPane = new JScrollPane();
		scrollPane.setBounds(1250,10,80, 100 );
		JLabel scrollPaneLabel = new JLabel("Shapes Drawn");
		scrollPaneLabel.setBounds(1250, 110, 90, 16);
		view.add(scrollPaneLabel);
		scrollPane.setPreferredSize(new Dimension(80,50));
		view.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setText("");
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		/****************************TextFields*****************************/
		/*Warning*/
		Warning = new JTextField();
		Warning.setBounds(1780, 30, 125, 60);
		Warning.setHorizontalAlignment(SwingConstants.CENTER);
		Warning.setFont(new Font("Arial", Font.BOLD, 19));
		Warning.setText("Pick a Shape!");
		Warning.setBackground(Color.RED);
		Warning.setForeground(Color.WHITE);
		Warning.setEditable(false);
		Warning.setVisible(false);
		view.add(Warning);
		Warning.setColumns(10);
		
		shapeProperties = new JTextArea();
	    shapeProperties.setEditable(false);
	    shapeProperties.setBackground(Color.WHITE);
	    shapeProperties.setBounds(1400,15, 150, 80);
	    shapeProperties.setVisible(true);
	    view.add(shapeProperties); 
	    JLabel shapePropertiesLabel = new JLabel("Shape Details");
	    shapePropertiesLabel.setBounds(1430, 100, 90, 16);
		view.add(shapePropertiesLabel);
	
	    //Register for mouse events on blankArea and panel.
	    view.addMouseMotionListener(this);
	    addMouseMotionListener(this);
	    
	    setPreferredSize(new Dimension(450, 450));
	    setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	
		
		/*Sample color shower*/
		JTextField selectedColorSample = new JTextField();
		selectedColorSample.setBackground(new Color(red,green,blue));
		selectedColorSample.setEditable(false);
		selectedColorSample.setEnabled(false);
		selectedColorSample.setBounds(780, 20, 130, 70);
		selectedColorSample.setHorizontalAlignment(SwingConstants.CENTER);
		selectedColorSample.setFont(new Font("Mistral", Font.BOLD, 20));
		selectedColorSample.setDisabledTextColor(new Color(Math.abs(255-red),Math.abs(255-green),Math.abs(255-blue)));
		selectedColorSample.setText("Color Sample");
		view.add(selectedColorSample);
		
		/****************************Sliders*****************************/
		/* red Color Slider*/
		JLabel redLabel = new JLabel("red");
		redLabel.setForeground(Color.RED);
		redLabel.setBounds(920, 15, 56, 16);
		view.add(redLabel);
		JSlider redSlider = new JSlider(JSlider.HORIZONTAL, 0, 255,0);
		redSlider.setMajorTickSpacing(25);
		redSlider.setMinorTickSpacing(5);
		redSlider.setPaintTicks(true);
		redSlider.setBounds(955, 10,200, 30);
		view.add(redSlider);
		redSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider slide = (JSlider) e.getSource();
				red=slide.getValue();
				selectedColorSample.setBackground(new Color(red,green,blue));
				selectedColorSample.setDisabledTextColor(new Color(Math.abs(255-red),Math.abs(255-green),Math.abs(255-blue)));
			}
		});
		
		/* green Color Slider*/
		JLabel greenLabel = new JLabel("green");
		greenLabel.setForeground(Color.GREEN);
		greenLabel.setBounds(920, 45, 56, 16);
		view.add(greenLabel);
		JSlider greenSlider = new JSlider(JSlider.HORIZONTAL, 0, 255,0);
		greenSlider.setMajorTickSpacing(25);
		greenSlider.setMinorTickSpacing(5);
		greenSlider.setPaintTicks(true);
		greenSlider.setBounds(955, 40,200, 30);
		view.add(greenSlider);
		greenSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider slide = (JSlider) e.getSource();
				green=slide.getValue();
				selectedColorSample.setBackground(new Color(red,green,blue));
				selectedColorSample.setDisabledTextColor(new Color(Math.abs(255-red),Math.abs(255-green),Math.abs(255-blue)));
			}
		});
		
		/* blue Color Slider*/
		JLabel blueLabel = new JLabel("blue");
		blueLabel.setForeground(Color.BLUE);
		blueLabel.setBounds(920, 75, 56, 16);
		view.add(blueLabel);
		JSlider blueSlider = new JSlider(JSlider.HORIZONTAL, 0, 255,0);
		blueSlider.setMajorTickSpacing(25);
		blueSlider.setMinorTickSpacing(5);
		blueSlider.setPaintTicks(true);
		blueSlider.setBounds(955, 70,200, 30);
		view.add(blueSlider);
		blueSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JSlider slide = (JSlider) e.getSource();
				blue=slide.getValue();
				selectedColorSample.setBackground(new Color(red,green,blue));
				selectedColorSample.setDisabledTextColor(new Color(Math.abs(255-red),Math.abs(255-green),Math.abs(255-blue)));
			}
		});
		
		/****************************Buttons*****************************/
		/*Undo Button*/
		JButton undo = new JButton();
		Icon imageUndo = new ImageIcon(getClass().getResource("/Undo.png"));
		undo.setIcon(imageUndo);
		undo.setBounds(50, 20, 41, 33);
		view.add(undo);
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engine.undo();
				view.repaint();
			}
		});

		
		/*Redo Button*/
		JButton redo = new JButton();
		Icon imageRedo = new ImageIcon(getClass().getResource("/Redo.png"));
		redo.setIcon(imageRedo);
		redo.setBounds(100, 20, 41, 33);
		view.add(redo);
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engine.redo();
				view.repaint();
			}
		});
		
		
		
		/*Move Button*/
		JButton move = new JButton("Move");
		move.setBounds(550, 50, 70, 25);
		view.add(move);
		move.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode="move";
				view.repaint();
			}
		});
		/*Remove Button*/
		JButton remove = new JButton("Remove");
		remove.setBounds(590, 80, 80, 25);
		view.add(remove);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				primaryPaint.Shape[] shapesArr = engine.getShapes();
				engine.removeShape(shapesArr[editedShapeIndex]);
				int lastShapeIndex = shapesArr.length-1;
				if (shapesArr.length>0 &&shapesArr[lastShapeIndex].getClass().getSimpleName().equals("SelectorShape")) {
					
					engine.removeShape(shapesArr[lastShapeIndex]); 
					engine.removeSelect();
				}
				mode="null";
				view.repaint();
			}
		});
		
		/*FixSize Button*/
		JButton fixSize = new JButton("FixSize");
		JButton select = new JButton("Select&Resize");
		fixSize.setVisible(false);
		fixSize.setBounds(550, 20, 150, 25);
		view.add(fixSize);
		fixSize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				primaryPaint.Shape[] shapesArr = engine.getShapes();
				int lastShapeIndex = shapesArr.length-1;
				if (shapesArr.length>0 &&shapesArr[lastShapeIndex].getClass().getSimpleName().equals("SelectorShape")) engine.removeShape(shapesArr[lastShapeIndex]);
				select.setVisible(true);
				fixSize.setVisible(false);
				mode=null;
				view.repaint();
			}
		});
		
		/*Select&Resize Button*/
		select.setBounds(550, 20, 150, 25);
		view.add(select);
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select.setVisible(false);
				fixSize.setVisible(true);
				mode="selection";
				view.repaint();
			}
		});
		
		/*Rotate Button*/
		JButton rotate = new JButton("Rotate");
		rotate.setBounds(623, 50, 75, 25);
		view.add(rotate);
		rotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				primaryPaint.Shape[] shapesArr = engine.getShapes();
				try {
					Cshape edit=(Cshape) shapesArr[editedShapeIndex].clone();
					Map<String, Double> prop = edit.getProperties();
					double x1=prop.get("Y1");
					double x2=prop.get("Y2");
					double y1=prop.get("X1");
					double y2=prop.get("X2");
					double w = prop.get("Width");
					double l=prop.get("Length");
					prop.put("Width", l);
					prop.put("Length", w);
					prop.put("X1", x1);
					prop.put("X2", x2);
					prop.put("Y1", y1);
					prop.put("Y2", y2);
					newSelector.setProperties(prop);
					edit.setProperties(prop);
					engine.updateShape(shapesArr[editedShapeIndex], edit);
					view.repaint();
					primaryPaint.Shape[] array = engine.getShapes();
					int lastShapeIndex = array.length-1;
					if (array.length>0 &&array[lastShapeIndex].getClass().getSimpleName().equals("SelectorShape")) {
						engine.removeShape(array[lastShapeIndex]); //5ly l remove dy mttsglsh fl undo
						engine.removeSelect();
					}
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		/*ellipse button*/
		JButton circle = new JButton();
		Icon imageCircle = new ImageIcon(getClass().getResource("/Ellipse.png"));
		circle.setIcon(imageCircle);
		circle.setBounds(260, 20, 60, 60);
		view.add(circle);
		circle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapeType = "Ellipse";
			}
		});
		
		/*rectangle button*/
		Icon imageRect = new ImageIcon(getClass().getResource("/Rectangle.png"));
		JButton rectangle = new JButton();
		rectangle.setIcon(imageRect);
	
		rectangle.setBounds(330, 20, 60, 60);
		view.add(rectangle);
		rectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapeType = "Rectangle";
			}
		});
		
		/*Line button*/
		JButton Line = new JButton();
		Icon imageLine = new ImageIcon(getClass().getResource("/Line.png"));
		Line.setIcon(imageLine);
		Line.setBounds(400, 20, 60, 60);
		view.add(Line);
		Line.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shapeType = "Line";
			}
		});
		
		/*save button*/
		JButton save = new JButton("Save");
		Icon imageSave = new ImageIcon(getClass().getResource("/Save.png"));
		save.setIcon(imageSave);
		save.setBounds(10, 65, 85, 25);
		view.add(save);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Save");
				fc.setSelectedFile(new File("save.XML"));
				int returnVal = fc.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    File file = fc.getSelectedFile();
				    
				    try {
				    	engine.save(file.getPath());
				    }catch (Exception ex)
				    {
				    	JOptionPane.showMessageDialog(null, ex.getMessage());
				    }
				     
				 }
				 
				
			}
		});
		
		/*load button*/
		JButton load = new JButton("load");
		Icon imageLoad = new ImageIcon(getClass().getResource("/Load.png"));
		load.setIcon(imageLoad);
		load.setBounds(100, 65, 90, 25);
		view.add(load);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("load");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "XML", "XML");
				fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(null);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    File file = fc.getSelectedFile();
				    
				    try {
				    	engine.load(file.getPath());
				    }catch (Exception ex)
				    {
				    	JOptionPane.showMessageDialog(null, ex.getMessage());
				    }
				     
				 }
				 
				
			}
		});
		
		/*ScreenCapture button*/
		JButton ScreenCapture = new JButton("ScreenCapture");
		Icon imageSC = new ImageIcon(getClass().getResource("/Camera.png"));
		ScreenCapture.setIcon(imageSC);
		ScreenCapture.setBounds(1600, 30, 150, 25);
		view.add(ScreenCapture);
		ScreenCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Dimension d = view.getSize();
				BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
				view.print( image.getGraphics());
				final JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(new File("Capture.PNG"));
				fc.setDialogTitle("ScreenCapture");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						 "JPG & PNG Images", "jpg", "png");
				fc.setFileFilter(filter);
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				    File file = fc.getSelectedFile();
				    try {
				    	ImageIO.write(image, "png", new File(file.getPath()));
				    }catch (Exception ex)
				    {
				    	JOptionPane.showMessageDialog(null, ex.getMessage());
				    }
				 }
			}
		});

		
				
		/****************************MouseActions*****************************/
		/* Mouse Drawing */
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (mode=="selection")
				{
					primaryPaint.Shape[] shapesArr = engine.getShapes();
					for (int k=0;k<shapesArr.length;k++)
					{
						int x1=shapesArr[k].getProperties().get("X1").intValue();
						int y1=shapesArr[k].getProperties().get("Y1").intValue();
						int x2=shapesArr[k].getProperties().get("X2").intValue();
						int y2=shapesArr[k].getProperties().get("Y2").intValue();
						if (e.getX()>Math.min(x1, x2) && e.getX()<Math.max(x1, x2) && e.getY()>Math.min(y1, y2) &&e.getY()<Math.max(y1, y2))
						{
							try {
								extraShape=(Cshape) shapesArr[k].clone();
								editedShapeIndex=k;
							} catch (CloneNotSupportedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							startDrag = new Point(x1, y1);
							endDrag = new Point(x2, y2);
							shapeType="Rectangle";
							view.repaint();
						}
					}
				}else if (mode=="edit")
				{
					primaryPaint.Shape[] shapesArr = engine.getShapes();
					int lastShapeIndex = shapesArr.length-1;
					int x1=shapesArr[lastShapeIndex].getProperties().get("X1").intValue();
					int y1=shapesArr[lastShapeIndex].getProperties().get("Y1").intValue();
					int x2=shapesArr[lastShapeIndex].getProperties().get("X2").intValue();
					int y2=shapesArr[lastShapeIndex].getProperties().get("Y2").intValue();
					
					startDrag=null;
					endDrag=null;
					if ( (e.getX()>=Math.min(x1, x2) || e.getX()<=Math.max(x1, x2))&& (e.getY()>=Math.min(y1, y2) ||e.getY()<=Math.max(y1, y2)) )
					{
						startDrag = new Point(Math.min(x1, x2), Math.min(y1, y2));
						endDrag = startDrag;
						shapeType="Rectangle";
						view.repaint();
					}
					
				}
				else if (mode==null){
					startDrag = new Point(e.getX(), e.getY());
					endDrag = startDrag;
					Warning.setVisible(false);
					view.repaint();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (mode=="selection")
				{
					primaryPaint.Shape[] shapesArr = engine.getShapes();
					int x1= (int) startDrag.getX();
					int y1= (int) startDrag.getY();
					int x2=shapesArr[editedShapeIndex].getProperties().get("X2").intValue();
					int y2=shapesArr[editedShapeIndex].getProperties().get("Y2").intValue();
					Point p = new Point(Math.min(x1, x2), Math.min(y1, y2));
					HashMap<String, Double> prop = new HashMap<String, Double>();
					prop.put("Width", (double) Math.abs(x1 - x2));
					prop.put("Length", (double) Math.abs(y1 - y2));
					prop.put("X1",(double) x1);
					prop.put("Y1",(double) y1);
					prop.put("X2",(double) x2);
					prop.put("Y2",(double) y2);
					prop.put("startAngle", 0.0);
					prop.put("arcAngle", 360.0);					
					//Black Stroke
					Color c = Color.black;
					newSelector= new SelectorShape();
					newSelector.setPosition(p);
					newSelector.setProperties(prop);
					newSelector.setColor(c);
					engine.addShape(newSelector);
					startDrag = null;
					endDrag = null;
					view.repaint();
					shapeType=null;
					mode="edit";
					primaryPaint.Shape[] array = engine.getShapes();
					int selectIndex = array.length-1;
					if (array.length>0 &&array[selectIndex].getClass().getSimpleName().equals("SelectorShape")) {	
						engine.removeSelect();
					}
				}else if (mode=="edit")
				{
					primaryPaint.Shape[] shapesArr = engine.getShapes();
					int x1 ;
					int y1 ;
					int x2 ;
					int y2 ;
					if (e.getX()>shapesArr[editedShapeIndex].getProperties().get("X1").intValue())
					{
						x1 = startDrag.x;
						x2 = e.getX();
					}else 
					{
						x1=e.getX();
						x2=shapesArr[editedShapeIndex].getProperties().get("X2").intValue();
					}
					if (e.getY()>shapesArr[editedShapeIndex].getProperties().get("Y1").intValue())
					{
						y1 = startDrag.y;
						y2 = e.getY();
					}else 
					{
						y1=e.getY();
						y2=shapesArr[editedShapeIndex].getProperties().get("Y2").intValue();
					}
					Point p = new Point((int)Math.min(x1, x2), (int)Math.min(y1, y2));
					HashMap<String, Double> prop = new HashMap<String, Double>();
					prop.put("Width", (double) Math.abs(x1 - x2));
					prop.put("Length", (double) Math.abs(y1 - y2));
					prop.put("X1",(double) x1);
					prop.put("Y1",(double) y1);
					prop.put("X2",(double) x2);
					prop.put("Y2",(double) y2);
					prop.put("startAngle", 0.0);
					prop.put("arcAngle", 360.0);
					extraShape.setPosition(p);
					extraShape.setProperties(prop);
					int lastShapeIndex = shapesArr.length-1;
					newSelector = (Cshape) shapesArr[lastShapeIndex];
					newSelector.setPosition(p);
					newSelector.setProperties(prop);
					newSelector.setColor(Color.black);
					engine.updateShape(shapesArr[editedShapeIndex], extraShape);
					startDrag = null;
					endDrag = null;
					view.repaint();
					
					primaryPaint.Shape[] array = engine.getShapes();
					int lastIndex = array.length-1;
					if (array.length>0 &&array[lastIndex].getClass().getSimpleName().equals("SelectorShape")) {
						engine.removeShape(array[lastIndex]);
						engine.removeSelect();
					}
					if(flagResize) {
						engine.removeRedundunt();
					}
					flagResize=true;
				}
				else if (mode == null){
					/* shape properties */
					int x1 = startDrag.x;
					int x2 = e.getX();
					int y1 = startDrag.y;
					int y2 = e.getY();
					Point p = new Point(Math.min(x1, x2), Math.min(y1, y2));
					HashMap<String, Double> prop = new HashMap<String, Double>();
					prop.put("Width", (double) Math.abs(x1 - x2));
					prop.put("Length", (double) Math.abs(y1 - y2));
					prop.put("X1",(double) x1);
					prop.put("Y1",(double) y1);
					prop.put("X2",(double) x2);
					prop.put("Y2",(double) y2);
					prop.put("startAngle", 0.0);
					prop.put("arcAngle", 360.0);
					
					Color fill = new Color(red,green,blue);
					if (makeTransparentCheckBox.isSelected()) fill=new Color(red ,green , blue , 50);
					
					//Black Stroke
					Color c = Color.black;
					//No Stroke
					if (!stroke.isSelected()) c =new Color(red,green,blue);
					if (regulars.isSelected() && shapeType!="Line") 
					{
						int sideLength = prop.get("Length").intValue();
						prop.put("Width",(double)sideLength );
						prop.put("X2",(double) x1+sideLength);
						prop.put("Y2",(double) y1+sideLength);						
					};
					
					Cshape shape;
					switch (shapeType)
					{
					case "Rectangle":
						shape = new Rectangle();
						break;
					case "Ellipse":
						shape = new Ellipse();
						break;
					case "Line":
						shape = new Line();
						break;
					default:
						shape = null;
						
					}
					/* setting shape (control calls model)*/
					shape.setPosition(p);
					shape.setProperties(prop);
					shape.setColor(c);
					shape.setFillColor(fill);
					engine.addShape(shape);
					startDrag = null;
					endDrag = null;
					view.repaint();
				}
				}
				

		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
	        public void mouseDragged(MouseEvent e) {
	        	if (mode=="move")
				{
	        		System.out.println(editedShapeIndex);
					try {
						primaryPaint.Shape[] shapesArr = engine.getShapes();
		        		Cshape edit;
						edit = (Cshape) shapesArr[editedShapeIndex].clone();
						Map<String, Double> prop = edit.getProperties();
						int x1=prop.get("X1").intValue();
						int x2=prop.get("X2").intValue();
						int y1=prop.get("Y1").intValue();
						int y2=prop.get("Y2").intValue();
						int w =prop.get("Width").intValue();
						int l=prop.get("Length").intValue();
						float avgx ;		
						float avgy ;					
						avgx=e.getX();
						
						avgy=e.getY();
						
						x1= (int) (avgx);
						x2= (int) (avgx+(w));
						y1= (int) (avgy);
						y2= (int) (avgy+(l));
						Point p = new Point((int)Math.min(x1, x2),(int) Math.min(y1, y2));
						prop.put("X1", (double)x1);
						prop.put("X2", (double)x2);
						prop.put("Y1", (double)y1);
						prop.put("Y2", (double)y2);
						newSelector.setPosition(p);
						newSelector.setProperties(prop);
						edit.setPosition(p);
						edit.setProperties(prop);
						engine.updateShape(shapesArr[editedShapeIndex], edit);
						primaryPaint.Shape[] array = engine.getShapes();
						int lastShapeIndex = array.length-1;
						if (array.length>0 &&array[lastShapeIndex].getClass().getSimpleName().equals("SelectorShape")) {
							engine.removeShape(array[lastShapeIndex]);
							engine.removeSelect();
						}
						if(flag) {
							engine.removeRedundunt();
						}
						flag=true;
						view.repaint();
						
					} catch (CloneNotSupportedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
				else {
					endDrag = new Point(e.getX(), e.getY());
			          view.repaint();
				}
	        }
	      });
		
		view.add(this);
		view.setVisible(true);

		
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
		primaryPaint.Shape[] shapesArr = engine.getShapes();
		textArea.setText("");
		for (int k=0;k<shapesArr.length;k++)
		{
			
			if (!(textArea.getText().contains(shapesArr[k].getClass().getSimpleName()))) textArea.append(shapesArr[k].getClass().getSimpleName()+'1'+'\n');
			else 
			{
				StringBuilder string = new StringBuilder();
				int index=textArea.getText().lastIndexOf(shapesArr[k].getClass().getSimpleName());
				index+=(shapesArr[k].getClass().getSimpleName().length());
				while (index<textArea.getText().length()&&(textArea.getText().charAt(index)>='0'&&textArea.getText().charAt(index)<='9'))
				{
					string.append(textArea.getText().charAt(index));
					index++;
				}
				int num = Integer.parseInt(string.toString())+1;
				textArea.append(shapesArr[k].getClass().getSimpleName()+num+'\n');
			};
		}


		if (endDrag!=null && startDrag!=null && mode!="move")
		{
			//g2.setStroke(new BasicStroke(2));
			Shape r;
	        if ((shapeType==(null)) ) Warning.setVisible(true);
	        g2.setPaint(Color.GRAY);
			switch (shapeType)
			{
			case "Rectangle":
				r = new Rectangle2D.Float(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x-endDrag.x), Math.abs(startDrag.y-endDrag.y));
				break;
			case "Ellipse":
				r = new Ellipse2D.Float(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x-endDrag.x), Math.abs(startDrag.y-endDrag.y));
				break;
			case "Line":
				r = new Line2D.Float(startDrag.x,startDrag.y, endDrag.x, endDrag.y);
				break;
			
			default :
				r = new Rectangle2D.Float(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x-endDrag.x), Math.abs(startDrag.y-endDrag.y));
			}
			g2.draw(r);
		}
		engine.refresh(g);
	}


	
	
	int x = 0;
	@SuppressWarnings("rawtypes")
	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
		
		chosen =(String)cb.getSelectedItem();
		cb.removeItem(chosen);
		if(!chosen.equals("Choose Shape")) {	
			JButton butt = new JButton(chosen);
			System.out.println(chosen);
			butt.setBounds(x, 140, 150, 25);
			x+=155;
			view.add(butt);
			butt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					shapeType = butt.getText();
					}
			});
		}
		
	}


    void eventOutput(String eventDescription, MouseEvent e) {
    	
    	primaryPaint.Shape[] shapesArr = engine.getShapes();
		for (int k=0;k<shapesArr.length;k++)
		{
			int x1=shapesArr[k].getProperties().get("X1").intValue();
			int y1=shapesArr[k].getProperties().get("Y1").intValue();
			int x2=shapesArr[k].getProperties().get("X2").intValue();
			int y2=shapesArr[k].getProperties().get("Y2").intValue();
			if (e.getX()>Math.min(x1, x2) && e.getX()<Math.max(x1, x2) 
					&& e.getY()>Math.min(y1, y2) &&e.getY()<Math.max(y1, y2)
					&&!shapesArr[k].getClass().getSimpleName().equals("SelectorShape"))
			{
				Map<String,Double> map =shapesArr[k].getProperties(); 
				
				/*textArea to array of strings*/
				String[] textAreaString = textArea.getText().split("\n");
				shapeProperties.setText(textAreaString[k]);
				if(shapesArr[k].getClass().getSimpleName().equals("Line")) {
					int length =  (int) Math.abs(Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2)));
					shapeProperties.append("\n"+"Length = "+ length);
				}
				else {
					shapeProperties.append("\nWidth = "+ map.get("Width").intValue());
					shapeProperties.append("\n"+"Length = "+ map.get("Length").intValue());
				}	
			}	
		}
      
    }
    
    public void mouseDragged(MouseEvent e) {
    //    eventOutput("Mouse dragged", e);
    }

	@Override
	public void mouseMoved(MouseEvent e) {	
	    eventOutput("Mouse Moved", e);
	}

}
