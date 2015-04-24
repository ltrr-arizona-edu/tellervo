package org.tellervo.desktop.gis2;

import gov.nasa.worldwind.render.markers.BasicMarkerShape;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.tellervo.desktop.ui.Builder;

public class ShapeComboRenderer extends JLabel implements ListCellRenderer {
	private JLabel labelItem = new JLabel();

	  public ShapeComboRenderer() {
	    super();
	    this.setOpaque(true);
	    this.setBackground(Color.WHITE);
	  }

	  public Component getListCellRendererComponent(JList list, Object obj,
	      int row, boolean sel, boolean hasFocus) {

	  if(obj instanceof String)
	  {
		  
		  if(obj.equals(BasicMarkerShape.CYLINDER))
		  {
			  this.setIcon(Builder.getIcon("map-cyclinder.png", 22));
			  this.setText(" - Cylinder");
			  return this;

		  }
		  else if(obj.equals(BasicMarkerShape.CUBE))
		  {
			  this.setIcon(Builder.getIcon("map-cube.png", 22));
			  this.setText(" - Cube");
			  return this;
		  }
		  else if(obj.equals(BasicMarkerShape.CONE))
		  {
			  this.setIcon(Builder.getIcon("map-cone.png", 22));
			  this.setText(" - Cone");
			  return this;
		  }
		  else if(obj.equals(BasicMarkerShape.SPHERE))
		  {
			  this.setIcon(Builder.getIcon("map-sphere.png", 22));
			  this.setText(" - Sphere");
			  return this;
		  }
		   
	  }
	  
	    this.setIcon(null);
	  	this.setText(obj.toString());
	  	return this;
	  }
	  
	  
}
