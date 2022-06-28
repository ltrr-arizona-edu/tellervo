package org.tellervo.desktop;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.tellervo.desktop.ui.Builder;

import ij.ImagePlus;
import ij.Menus;
import ij.gui.ImageWindow;
import ij.gui.PointRoi;

public class DendroImageWindow extends ImageWindow implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPopupMenu popup;
	
	
	public DendroImageWindow(final ImagePlus imp) {
		super(imp);
		JToolBar toolbar = new JToolBar();
		toolbar.add(new JButton("File"));
		
		add(toolbar, BorderLayout.NORTH);
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Tellervo Image");
		
		
		JMenuItem mi;

	    popup = new JPopupMenu();

	    mi = new JMenuItem("Delete");
	    mi.setActionCommand("delete");
	    mi.addActionListener(this);
	    popup.add(mi);
	    
            mi = new JMenuItem("Cut");
            mi.addActionListener(this);
	    popup.add(mi);

            mi = new JMenuItem("Copy");
            mi.addActionListener(this);
	    popup.add(mi);

	    popup.addSeparator();

            mi = new JMenuItem("Paste");
            mi.addActionListener(this);
	    popup.add(mi);

	    
	    this.getCanvas().addMouseListener(new MouseAdapter() {
	         public void mouseClicked(MouseEvent e) {  
	        	 
	        	 if(SwingUtilities.isRightMouseButton(e))
	        	 {
	        		 popup.show(getCanvas(), e.getX(), e.getY());
	        		 return;
	        	 }
	        	 
	        	/* double magnification = getCanvas().getMagnification();
	        	 
	        	 Point loc = getCanvas().getCursorLoc();
	 			 double factor = 1/ magnification;
	 			 
	        	 
	        	 //PointRoi roi = new PointRoi(e.getX()* factor, e.getY()*factor);
	 			PointRoi roi = new PointRoi(loc.getX(), loc.getY());
	        	 
	        	 roi.setName("1989");
	        	 imp.getOverlay().add(roi);*/
	        	 
	        	 
	         }               
	      });

	    

	}

	
	
	@Override
	public void drawInfo(Graphics g) 
	{
		
	}



	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("delete"))
		{
			
		}
	}
}
