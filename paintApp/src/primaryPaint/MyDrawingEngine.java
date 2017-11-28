package primaryPaint;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class MyDrawingEngine implements DrawingEngine {

	
	private View view ;
	private ArrayList<Shape> shapesOnCanvas = new ArrayList<Shape>();
	private LinkedList<Shape> shapesRemoved = new LinkedList<Shape>();
	@SuppressWarnings("rawtypes")
	private Stack undo = new Stack();
	@SuppressWarnings("rawtypes")
	private Stack redo = new Stack();
	private int undos = 0;
	private ArrayList<Integer> undoOperations = new ArrayList<Integer>();
	private ArrayList<Integer> redoOperations = new ArrayList<Integer>();
	private ArrayList<Integer> order = new ArrayList<Integer>();
	private ArrayList<Integer> redoOrder = new ArrayList<Integer>();
	
	public MyDrawingEngine(View v)
	{
		this.view=v;
	}
	
	
	@Override
	public void refresh(Graphics canvas) {
		for(int i = 0;i<shapesOnCanvas.size();i++) {
			if(!shapesOnCanvas.get(i).equals(null)) {
				shapesOnCanvas.get(i).draw(canvas);
			}
		}
		System.out.println("on canvas:"+shapesOnCanvas.size());
		System.out.println("undo :"+undo.size());
		System.out.println("redo "+redo.size());
	}

	@Override
	public void addShape(Shape shape) {
		if(!shape.equals(null)) {
		shapesOnCanvas.add(shape);
		order.add(shapesOnCanvas.indexOf(shape));
		undoOperations.add(0);
		undos = 0;
		System.out.println(shape.getClass().getSimpleName());
		if(!shape.getClass().getSimpleName().equals("SelectorShape")) {
			redoOperations.clear();
			redo.clear();
			redoOrder.clear();
		}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeShape(Shape shape) {
		if(shapesOnCanvas.contains(shape)) {
			//if(undoOperations.size()<20) {
				undo.push(shape);
				order.add(shapesOnCanvas.indexOf(shape));
				undoOperations.add(1);
				
	//}
			undos = 0;
			redoOperations.clear();
			redo.clear();
			redoOrder.clear();
			shapesOnCanvas.remove(shape);
		}
	}
	public void removeSelect() {
		if (undo.size()>0 && undo.get(undo.size()-1).getClass().getSimpleName().equals("SelectorShape")) undo.pop();
		order.remove(order.size()-1);
		undoOperations.remove(undoOperations.size()-1);
	}
	public void removeRedundunt() {
		if (undo.size()>0) undo.pop();
		order.remove(order.size()-1);
		undoOperations.remove(undoOperations.size()-1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		if(shapesOnCanvas.contains(oldShape)) {
			int oldShapeIndex = shapesOnCanvas.indexOf(oldShape);
			
	//		if(undoOperations.size()<20) {
				undo.push(oldShape);
				undoOperations.add(2);
				order.add(oldShapeIndex);
				
	//		}
		    undos = 0;
			redoOperations.clear();
			redo.clear();
			redoOrder.clear();
			shapesOnCanvas.remove(oldShape);
			shapesOnCanvas.add(oldShapeIndex,newShape);
		}
	}

	@Override
	public Shape[] getShapes() {
		Shape[] shapesArr = new Shape[shapesOnCanvas.size()];
		for(int i =0;i<shapesOnCanvas.size();i++) {
			shapesArr[i] = shapesOnCanvas.get(i);
		}
		return shapesArr;
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		 @SuppressWarnings({ "rawtypes", "unchecked" })
		List<Class<? extends Shape>> list =new LinkedList();
		 list.add(Ellipse.class);
		 list.add(Rectangle.class);
		 list.add(Line.class);
		 
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void undo() {
		if(undoOperations.size()>0 && undos < 20) {
			for(int i=0;i<undoOperations.size();i++) {
			}
			int x = undoOperations.remove(undoOperations.size()-1);
			redoOperations.add(x);
			System.out.println("x:"+x);
		switch(x) {
			case 0: redo.push(shapesOnCanvas.remove(shapesOnCanvas.size()-1));
					redoOrder.add(order.size()-1);
					break;
			case 1: int ordered_1 = order.remove(order.size()-1);
					shapesOnCanvas.add(ordered_1,(Shape) undo.pop());
					redoOrder.add(ordered_1);
					break;
					
			case 2: int ordered = order.remove(order.size()-1);
					// ordered instead of order.remove(order.size()-1)
					redo.push(shapesOnCanvas.remove(ordered));	
					redoOrder.add(ordered);
					shapesOnCanvas.add(ordered,(Shape) undo.pop());
					break;
			
		}
		undos++;	
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void redo() {
		if(redoOperations.size()>0 ) {
			int x =redoOperations.remove(redoOperations.size()-1);
			undoOperations.add(x);
			switch(x) {
				case 0: shapesOnCanvas.add((Shape) redo.pop());
						order.add(redoOrder.remove(redoOrder.size()-1));
						break;
				case 1: int index = redoOrder.remove(redoOrder.size()-1);
						order.add(index);
						undo.push(shapesOnCanvas.remove(index));
						break;
				case 2: int indexUpdate = redoOrder.remove(redoOrder.size()-1);
						undo.push(shapesOnCanvas.remove(indexUpdate));
						order.add(indexUpdate);
						shapesOnCanvas.add(indexUpdate, (Shape) redo.pop());
						break;
				
			}
			undos--;	
		}
	}

	@Override
	public void save(String path) {
		// TODO Auto-generated method stub
		
		try {
		Document doc = new Document();
		Element theRoot = new Element("paintData");
		doc.setRootElement(theRoot);
		
		Element save = new Element("shapesOnCanvas");
		
		int i =1;
		for (Shape s: shapesOnCanvas)
		{
			Element shape = new Element("shape"+i++);
			Element shapeType = new Element("shapeType");
			shapeType.addContent(""+s.getClass().getSimpleName());
			Element colorRed = new Element("colorRed");
			colorRed.addContent(""+s.getColor().getRed());
			Element colorGreen = new Element("colorGreen");
			colorGreen.addContent(""+s.getColor().getGreen());
			Element colorBlue = new Element("colorBlue");
			colorBlue.addContent(""+s.getColor().getBlue());
			Element fillRed = new Element("fillRed");
			fillRed.addContent(""+s.getFillColor().getRed());
			Element fillGreen = new Element("fillGreen");
			fillGreen.addContent(""+s.getFillColor().getGreen());
			Element fillBlue = new Element("fillBlue");
			fillBlue.addContent(""+s.getFillColor().getBlue());
			Element alpha = new Element("alpha");
			alpha.addContent(""+s.getFillColor().getAlpha());
			Element positionX = new Element("positionX");
			positionX.addContent(""+s.getPosition().x);
			Element positionY = new Element("positionY");
			positionY.addContent(""+s.getPosition().y);
			Element properties = new Element("properties");
			properties.addContent(s.getProperties().toString());
			shape.addContent(shapeType);
			shape.addContent(colorRed);
			shape.addContent(colorGreen);
			shape.addContent(colorBlue);
			shape.addContent(fillRed);
			shape.addContent(fillGreen);
			shape.addContent(fillBlue);
			shape.addContent(alpha);
			shape.addContent(positionX);
			shape.addContent(positionY);
			shape.addContent(properties);
			save.addContent(shape);
		}
		
		Element shapesNum = new Element("shapesNum");
		shapesNum.addContent(""+(--i));
		save.addContent(shapesNum);
		theRoot.addContent(save);
		
		XMLOutputter xmlOutPut = new XMLOutputter(Format.getPrettyFormat());
		
		xmlOutPut.output(doc, new FileOutputStream(new File (path)));

		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void load(String path) {
		// TODO Auto-generated method stub
		
		this.shapesOnCanvas.clear();
		
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document readDoc = builder.build(new File (path));
			
			int shapesNUm = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChildText("shapesNum"));
			for (int j =0;j<shapesNUm;j++)
			{
				int r = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("colorRed"));
				int g = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("colorGreen"));
				int b = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("colorBlue"));
				Color color = new Color(r,g,b);
				r = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("fillRed"));
				g = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("fillGreen"));
				b = Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("fillBlue"));
				int a =Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("alpha"));
				Color fill = new Color(r,g,b,a);
				
				int x =Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("positionX"));
				int y =Integer.parseInt(readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("positionY"));
				Point position = new Point(x,y);
				
				String propString = readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("properties");
				propString= propString.substring(1, propString.length()-1);
				String[] keyValuePairs = propString.split(",");
				Map<String, Double> prop=new HashMap<String, Double>();
				
				for (String pair : keyValuePairs)
				{
					String[] entry = pair.split("="); 
					prop.put(entry[0].trim(), Double.parseDouble(entry[1].trim()));
				}
				String shapeType = readDoc.getRootElement().getChild("shapesOnCanvas").getChild("shape"+(j+1)).getChildText("shapeType");
				Cshape loadedShape ;
				switch (shapeType)
				{
				case "Rectangle":
					loadedShape = new Rectangle();
					break;
				case "Ellipse":
					loadedShape = new Ellipse();
					break;
				case "Line":
					loadedShape = new Line();
					break;
				default:
					loadedShape = null;
					
				}
				loadedShape.setPosition(position);
				loadedShape.setProperties(prop);
				loadedShape.setColor(color);
				loadedShape.setFillColor(fill);
				this.addShape(loadedShape);
				view.repaint();
				undoOperations.add(0);
				undos = 0;
				redoOperations.clear();
				redo.clear();
				redoOrder.clear();
			}
			
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

}

