package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.Pos;

import org.tridas.schema.TridasLocationGeometry;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;

public class LocationGeometry extends LocationGeometryUI implements
		ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;

	private JDialog dialog;
	private boolean hasResults = false;
	final JFileChooser fc = new JFileChooser();
	final GPXFileFilter filter = new GPXFileFilter();

	public void showDialog(Window parent, TridasLocationGeometry geometry) {
		// construct a new dialog!
		if (parent instanceof Frame || parent == null)
			dialog = new JDialog((Frame) parent, "Location Geometry", true);
		else
			dialog = new JDialog((Dialog) parent, "Location Geometry", true);

		dialog.setIconImage(Builder.getImage("Preferences16.gif"));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.setContentPane(this);
		setDefaults();

		if(geometry != null)
			setFromGeometry(geometry);
		
		dialog.pack();

		Center.center(dialog);
		dialog.setVisible(true);
	}

	/**
	 * Get the information in this dialog as represented by a TridasLocationGeometry
	 * @return a geometry
	 * @throws IllegalStateException if the dialog hasn't been used properly
	 */
	public TridasLocationGeometry getGeometry() {
		if(!hasResults)
			throw new IllegalStateException();
		
		TridasLocationGeometry geometry = new TridasLocationGeometry();
		PointType point = new PointType();
		geometry.setPoint(point);
		
		Pos pos = new Pos();
		point.setPos(pos);
		
		pos.getValues().add(Double.valueOf(spnDDLat.getValue().toString()));
		pos.getValues().add(Double.valueOf(spnDDLong.getValue().toString()));
		
		return geometry;
	}
	
	private void setFromGeometry(TridasLocationGeometry geometry) {
		if(!geometry.isSetPoint() && geometry.getPoint().isSetPos() && geometry.getPoint().getPos().isSetValues())
			return;
		
		List<Double> latLong = geometry.getPoint().getPos().getValues();
		// must be a tuple of lat, long!
		if(latLong.size() != 2)
			return;
		
		double lat = latLong.get(0);
		double lng = latLong.get(1);
		
		spnDDLat.setValue(lat);
		spnDDLong.setValue(lng);
		
		// listeners handle the rest!
	}
	
	private void setDefaults() {

		// Group the radio buttons.
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

		cboLatLongStyle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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
		
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	private void latLongStyleActionPerformed(java.awt.event.ActionEvent evt) {

		switch (cboLatLongStyle.getSelectedIndex()) {
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

	private void radManualActionPerformed(java.awt.event.ActionEvent evt) {

		this.setLatLongEnabled(true);
		this.lblGPSFilename.setEnabled(false);
		this.txtGPSFilename.setEnabled(false);
		this.btnGPSBrowse.setEnabled(false);
		this.lblWaypoint.setEnabled(false);
		this.cboWaypoint.setEnabled(false);

	}

	private void setLatLongEnabled(Boolean b) {
		this.spnDDLat.setEnabled(b);
		this.spnDDLong.setEnabled(b);
		this.spnDMSLatDeg.setEnabled(b);
		this.spnDMSLatMin.setEnabled(b);
		this.spnDMSLatSec.setEnabled(b);
		this.spnDMSLongDeg.setEnabled(b);
		this.spnDMSLongMin.setEnabled(b);
		this.spnDMSLongSec.setEnabled(b);
	}

	private void setDMSFromDD() {
		double dd_lat = Double.parseDouble(spnDDLat.getValue().toString());
		double dd_long = Double.parseDouble(spnDDLong.getValue().toString());
		
		// remember the signs...
		int latMultiplier = (dd_lat < 0) ? -1 : 1;
		int longMultiplier = (dd_long < 0) ? -1 : 1;
		
		// chop off the signs...
		dd_lat = Math.abs(dd_lat);
		dd_long = Math.abs(dd_long);
		
		// ok, now we have a number like 12.34567
		// we can use floor because we abs()'d
		
		// do lat first
		double dms_lat_deg = Math.floor(dd_lat);
		double dms_lat_min = Math.floor((dd_lat - dms_lat_deg) * 60d);
		double dms_lat_sec = Math.round((((dd_lat - dms_lat_deg) * 60d) - dms_lat_min) * 60d);
		
		spnDMSLatDeg.setValue((int) dms_lat_deg * latMultiplier);
		spnDMSLatMin.setValue((int) dms_lat_min);
		spnDMSLatSec.setValue((int) dms_lat_sec);
		
		// long, same idea
		double dms_long_deg = Math.floor(dd_long);
		double dms_long_min = Math.floor((dd_long - dms_long_deg) * 60d);
		double dms_long_sec = Math.round((((dd_long - dms_long_deg) * 60d) - dms_long_min) * 60d);
		
		spnDMSLongDeg.setValue((int) dms_long_deg * longMultiplier);
		spnDMSLongMin.setValue((int) dms_long_min);
		spnDMSLongSec.setValue((int) dms_long_sec);

	}

	private void setDDFromDMS() {
		// ok, these should all be integers!
		int dms_lat_deg = Integer.parseInt(spnDMSLatDeg.getValue().toString());
		int dms_lat_min = Integer.parseInt(spnDMSLatMin.getValue().toString());
		int dms_lat_sec = Integer.parseInt(spnDMSLatSec.getValue().toString());
		int dms_long_deg = Integer.parseInt(spnDMSLongDeg.getValue().toString());
		int dms_long_min = Integer.parseInt(spnDMSLongMin.getValue().toString());
		int dms_long_sec = Integer.parseInt(spnDMSLongSec.getValue().toString());

		// remember the signs...
		double latMultiplier = (dms_lat_deg < 0) ? -1 : 1;
		double longMultiplier = (dms_long_deg < 0) ? -1 : 1;

		// now, chop off the signs
		dms_lat_deg = Math.abs(dms_lat_deg);
		dms_long_deg = Math.abs(dms_long_deg);
		
		double ddlat = latMultiplier * (dms_lat_deg + (dms_lat_min / 60d) + (dms_lat_sec / 3600d));
		double ddlong = longMultiplier * (dms_long_deg + (dms_long_min / 60d) + (dms_long_sec / 3600d));
		
		// Set spinners
		spnDDLat.setValue(ddlat);
		spnDDLong.setValue(ddlong);
	}

	public static void main(String[] args) {
		App.platform = new Platform();
		App.platform.init();
		App.init(null, null);

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				LocationGeometry lg = new LocationGeometry();

				lg.showDialog(null, null);
			}
		});

	}

	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getSource() == btnGPSBrowse) {
			fc.setFileFilter(filter);
			int returnVal = fc.showOpenDialog(LocationGeometry.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				txtGPSFilename.setText(fc.getSelectedFile().toString());

			}
		}

		if (evt.getSource() == btnViewMap) {
			// Open map window for current location
		}

		if (evt.getSource() == btnCancel) {			
			// Close window without changing latlong
			hasResults = false;

			dialog.dispose();
			dialog = null;
		}

		if (evt.getSource() == btnOK) {
			// sanity check: 0N 0E is in the ocean off the coast of africa
			// it's likely that this is a user setting invalid information.
			if(Math.abs(Double.parseDouble(spnDDLat.getValue().toString())) < 0.000001d) {
				JOptionPane.showMessageDialog(this, "Invalid latitude", 
						"You did not find any trees in the ocean.", JOptionPane.ERROR_MESSAGE);
				spnDDLat.requestFocusInWindow();
				return;
			}
			
			if(Math.abs(Double.parseDouble(spnDDLong.getValue().toString())) < 0.000001d) {
				JOptionPane.showMessageDialog(this, "Invalid longitude", 
						"You did not find any trees in the ocean.", JOptionPane.ERROR_MESSAGE);
				spnDDLong.requestFocusInWindow();
				return;
			}
			
			// Close window and return latlong
			hasResults = true;
			
			dialog.dispose();
			dialog = null;
		}

	}

	private boolean isChangingSpinners = false;
	
	public void stateChanged(ChangeEvent evt) {
		// need to prevent the infinite loop that occurs when spinners change each other...
		if(isChangingSpinners)
			return;
		
		isChangingSpinners = true;
		
		try {
			// DMS values changed so set decimal degrees
			if ((evt.getSource() == spnDMSLatDeg)
					|| (evt.getSource() == spnDMSLatMin)
					|| (evt.getSource() == spnDMSLatSec)
					|| (evt.getSource() == spnDMSLongDeg)
					|| (evt.getSource() == spnDMSLongMin)
					|| (evt.getSource() == spnDMSLongSec)) {

				setDDFromDMS();
				/*
				 * loc.setLatitudeAsDegrees(Float.parseFloat(spnDDLat.getValue()
				 * .toString()));
				 * loc.setLongitudeAsDegrees(Float.parseFloat(spnDDLong
				 * .getValue() .toString()));
				 */
			}

			// Decimal degress changed so set DMS values
			if ((evt.getSource() == spnDDLat) || (evt.getSource() == spnDDLong)) {
				setDMSFromDD();
				/*
				 * loc.setLatitudeAsDegrees(Float.parseFloat(spnDDLat.getValue()
				 * .toString()));
				 * loc.setLongitudeAsDegrees(Float.parseFloat(spnDDLong
				 * .getValue() .toString()));
				 */
			}
		} finally {
			isChangingSpinners = false;
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
