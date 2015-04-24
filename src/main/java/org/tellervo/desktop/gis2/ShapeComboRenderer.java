package org.tellervo.desktop.gis2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ShapeComboRenderer extends JPanel implements ListCellRenderer {
	private JLabel labelItem = new JLabel();
	private String shape="Cylinder";


	  public ShapeComboRenderer() {
	    super();
	    setBorder(new CompoundBorder(
		        new MatteBorder(2, 10, 2, 10, Color.white), new LineBorder(
		            Color.white)));
        labelItem.setOpaque(false);
        labelItem.setHorizontalAlignment(JLabel.LEFT);
        add(labelItem);
	  }

	  public Component getListCellRendererComponent(JList list, Object obj,
	      int row, boolean sel, boolean hasFocus) {

	  if(obj instanceof String)
		   shape = (String) obj;

		  labelItem.setText(shape);			 
		 
	    return this;
	  }
	  
	  
	  
}
