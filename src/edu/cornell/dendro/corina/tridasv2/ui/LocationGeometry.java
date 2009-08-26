package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.spatial.Location;


public class LocationGeometry extends LocationGeometryUI implements ActionListener, ChangeListener {
  
	private JFrame dialog;
	private Location loc = new Location();
	final JFileChooser fc = new JFileChooser();
	final GPXFileFilter filter = new GPXFileFilter();
	
	public  void showDialog() {
		// does it already exist? just bring it to the front
		if(dialog != null) {
			dialog.setVisible(true);
			dialog.setExtendedState(JFrame.NORMAL);
			dialog.toFront();
			
			return;
		}
		
		// construct a new dialog!
		dialog = new JFrame("Location Geometry");
		dialog.setIconImage(Builder.getImage("Preferences16.gif"));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// steal the content pane from an instance of PreferencesDialog
		LocationGeometry lg = new LocationGeometry();
		lg.setDefaults();	
		
		dialog.setContentPane(lg);
		
		
		dialog.pack();
		
		Center.center(dialog);
		dialog.setVisible(true);
		
		// on close, set our internal static thingy to null
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				synchronized(dialog) {
					dialog = null;
				}
			}
		});
	}
    
	public void setDefaults(){
		
	    //Group the radio buttons.
	    ButtonGroup radgroup = new ButtonGroup();
	    radgroup.add(radGPS);
	    radgroup.add(radManual);
	    radManual.setSelected(true);

   	    cboLatLongStyle.setEnabled(true);
	    radGPS.setEnabled(false);

	    
	    // Poke the radio buttons and lat long style to set fields
	    radManualActionPerformed(null);
	    latLongStyleActionPerformed(null);
	    
        radGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radGPSActionPerformed(evt);
            }
        });
        
        radManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radManualActionPerformed(evt);
            }
        });
        
        cboLatLongStyle.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		latLongStyleActionPerformed(evt);
        	}
        });
        
        spnDMSLatDeg.addChangeListener(this);  
        spnDMSLatMin.addChangeListener(this);
        spnDMSLatSec.addChangeListener(this);
        spnDMSLongDeg.addChangeListener(this);       
        spnDMSLongMin.addChangeListener(this);
        spnDMSLongSec.addChangeListener(this);
        
        spnDDLat.addChangeListener(this);
        spnDDLong.addChangeListener(this);
        
        btnGPSBrowse.addActionListener(this);
        btnViewMap.addActionListener(this);
        
	}
	
	private void latLongStyleActionPerformed(java.awt.event.ActionEvent evt){
		
		switch(cboLatLongStyle.getSelectedIndex())
		{
		case 0:
			this.panelDecDeg.setVisible(true);
			this.panelDMS.setVisible(false);
			break;
		case 1:
			this.panelDecDeg.setVisible(false);
			this.panelDMS.setVisible(true);
			break;
		}
	}
	
    private void radGPSActionPerformed(java.awt.event.ActionEvent evt) {
        
    	this.setLatLongEnabled(false);
    	this.lblGPSFilename.setEnabled(true);    	
    	this.txtGPSFilename.setEnabled(true);
    	this.lblWaypoint.setEnabled(true);
    	this.btnGPSBrowse.setEnabled(true);
    	this.cboWaypoint.setEnabled(true);
 	
    }
  
    private void radManualActionPerformed(java.awt.event.ActionEvent evt){
    	
    	this.setLatLongEnabled(true);
    	this.lblGPSFilename.setEnabled(false);
    	this.txtGPSFilename.setEnabled(false);
    	this.btnGPSBrowse.setEnabled(false);
    	this.lblWaypoint.setEnabled(false);
    	this.cboWaypoint.setEnabled(false);
    	
    }
    
    private void setLatLongEnabled(Boolean b){
    	this.spnDDLat.setEnabled(b);
    	this.spnDDLong.setEnabled(b);
    	this.spnDMSLatDeg.setEnabled(b);
    	this.spnDMSLatMin.setEnabled(b);
    	this.spnDMSLatSec.setEnabled(b);
    	this.spnDMSLongDeg.setEnabled(b);
    	this.spnDMSLongMin.setEnabled(b);
    	this.spnDMSLongSec.setEnabled(b);
    }

	private void setDMSFromDD(){

	}
	
	private void setDDFromDMS(){
		
		Double ddlat;
		Double ddlong;
				
		Double dmslatdeg = Double.valueOf(this.spnDMSLatDeg.getValue().toString());
		Double dmslatmin = Double.valueOf(this.spnDMSLatMin.getValue().toString());
		Double dmslatsec = Double.valueOf(this.spnDMSLatSec.getValue().toString());
		Double dmslongdeg = Double.valueOf(this.spnDMSLongDeg.getValue().toString());
		Double dmslongmin = Double.valueOf(this.spnDMSLongMin.getValue().toString());
		Double dmslongsec = Double.valueOf(this.spnDMSLongSec.getValue().toString());
		
		// Convert lat
		if(dmslatdeg > 0){
			ddlat = dmslatdeg + (dmslatmin/60) + ((dmslatsec/3600));
		}
		else if(dmslatdeg < 0){
			ddlat = dmslatdeg - (dmslatmin/60) + ((dmslatsec/3600));
		}
		else{
			ddlat = 0.0;
		}
		
		// Covert long
		if(dmslongdeg > 0){
			ddlong = dmslongdeg + (dmslongmin/60) + ((dmslongsec/60)/60);
		}
		else if(dmslongdeg < 0){
			ddlong = dmslongdeg - (dmslongmin/60) + ((dmslongsec/60)/60);
		}
		else{
			ddlong = 0.0;
		}
		
		// Set spinners
		spnDDLat.setValue(ddlat);
		spnDDLong.setValue(ddlong);
		
		// Set members
		//loc.setLatitudeAsDegrees(ddlat.floatValue());
		//loc.setLongitudeAsDegrees(ddlong.floatValue());
		
		
		
	}
	
	
	
	
	public static void main(String[] args) {
	    App.platform = new Platform();
	    App.platform.init();	    
		App.init(null, null);
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LocationGeometry lg = new LocationGeometry();

                lg.showDialog();
            }
        });
	    
		
		

		
	}

	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if( evt.getSource() == btnGPSBrowse)
		{
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(LocationGeometry.this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				txtGPSFilename.setText(fc.getSelectedFile().toString());
				
			}
		}
		
		if (evt.getSource() == btnViewMap){
			// Open map window for current location
		}
		
		if (evt.getSource() == btnCancel){
			// Close window without changing latlong
		}
		
		if (evt.getSource() == btnOK){
			// Close window and return latlong
		}
		
	}
	
	
	

	public void stateChanged(ChangeEvent evt) {
		// DMS values changed so set decimal degrees
		if( (evt.getSource() == spnDMSLatDeg) || 
		    (evt.getSource() == spnDMSLatMin) || 
		    (evt.getSource() == spnDMSLatSec) || 
		    (evt.getSource() == spnDMSLongDeg) ||
		    (evt.getSource() == spnDMSLongMin) ||
		    (evt.getSource() == spnDMSLongSec) )
		{

			setDDFromDMS();
			loc.setLatitudeAsDegrees(Float.parseFloat(spnDDLat.getValue().toString()));
			loc.setLongitudeAsDegrees(Float.parseFloat(spnDDLong.getValue().toString()));			
		}
		
		// Decimal degress changed so set DMS values
		if( (evt.getSource() == spnDDLat) || 
			(evt.getSource() == spnDDLong) )
		{			
			setDMSFromDD();
			loc.setLatitudeAsDegrees(Float.parseFloat(spnDDLat.getValue().toString()));
			loc.setLongitudeAsDegrees(Float.parseFloat(spnDDLong.getValue().toString()));

		}
		
		
	}
}


class GPXFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".gpx");
    }
    
    public String getDescription() {
        return ".gpx GPS eXchange format";
    }
}
