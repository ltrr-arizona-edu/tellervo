/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.Pos;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gis.GISFrame;
import org.tellervo.desktop.gis.GPXParser;
import org.tellervo.desktop.gis.TridasMarkerLayerBuilder;
import org.tellervo.desktop.gis.GPXParser.GPXWaypoint;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasLocationGeometry;
import org.tridas.spatial.GMLPointSRSHandler;


public class LocationGeometry extends LocationGeometryUI implements
		ActionListener, ChangeListener {
	public LocationGeometry() {
	}
	private static final long serialVersionUID = 1L;

	private JDialog dialog;
	private boolean hasResults = false;
	private GISFrame map;
	final JFileChooser fc = new JFileChooser();
	final GPXFileFilter filter = new GPXFileFilter();

	public void showDialog(Component parent, TridasLocationGeometry geometry) {

		
		dialog = new JDialog();
		dialog.setTitle("Memo");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setUndecorated(true);
		
		dialog.setContentPane(this);
		
		
		dialog.setIconImage(Builder.getApplicationIcon());
		setDefaults();

		if(geometry != null)	
			setFromGeometry(geometry);
		
		
		dialog.setBounds(parent.getLocationOnScreen().x, 
				parent.getLocationOnScreen().y, 
				600, 
				400);
		dialog.pack();
		dialog.setVisible(true);
		
	}

	/**
	 * @return true if the user clicked OK
	 */
	public boolean hasResults() {
		return hasResults;
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
		
		// TODO: Actually implement detecting this and putting in the right standards
		point.setSrsName("EPSG:4326");
		
		
		
		Pos pos = new Pos();
		point.setPos(pos);
		
		pos.getValues().add(Double.valueOf(spnDDLong.getValue().toString()));
		pos.getValues().add(Double.valueOf(spnDDLat.getValue().toString()));
				
		return geometry;
	}
	
	private void setFromGeometry(TridasLocationGeometry geometry) {
		if(!geometry.isSetPoint())
			return;
		
		GMLPointSRSHandler pointHandler = new GMLPointSRSHandler(geometry.getPoint());

		
		spnDDLat.setValue(pointHandler.getWGS84LatCoord());
		spnDDLong.setValue(pointHandler.getWGS84LongCoord());
		
		// listeners handle the rest!
	}
	
	private void setDefaults() {

		// Group the radio buttons.
		ButtonGroup radgroup = new ButtonGroup();
		radgroup.add(radGPS);
		radgroup.add(radManual);
		radManual.setSelected(true);
		txtGPSFilename.setEditable(false);
		
		cboLatLongStyle.setEnabled(true);
		radGPS.setEnabled(true);
		btnViewMap.setEnabled(true);

		// Poke the radio buttons and lat long style to set fields
		radManualActionPerformed();
		latLongStyleActionPerformed();

		cboWaypoint.addActionListener(this);
		
		radGPS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				radGPSActionPerformed(evt);
			}
		});

		radManual.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				radManualActionPerformed();
			}
		});

		cboLatLongStyle.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				latLongStyleActionPerformed();
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

	private void latLongStyleActionPerformed() {

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
		
		dialog.pack();
		
	}
	
	
	

	private void radGPSActionPerformed(java.awt.event.ActionEvent evt) {

		this.setLatLongEnabled(false);
		this.lblGPSFilename.setEnabled(true);
		this.txtGPSFilename.setEnabled(true);
		this.lblWaypoint.setEnabled(true);
		this.btnGPSBrowse.setEnabled(true);
		this.cboWaypoint.setEnabled(true);
		this.cboDatum.setEnabled(false);
	}

	private void radManualActionPerformed() {

		this.setLatLongEnabled(true);
		this.lblGPSFilename.setEnabled(false);
		this.txtGPSFilename.setEnabled(false);
		this.btnGPSBrowse.setEnabled(false);
		this.lblWaypoint.setEnabled(false);
		this.cboWaypoint.setEnabled(false);
		this.cboDatum.setEnabled(false);

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
		
		spnDMSLatDeg.setValue((Double) dms_lat_deg * latMultiplier);
		spnDMSLatMin.setValue((Double) dms_lat_min);
		spnDMSLatSec.setValue((Double) dms_lat_sec);
		
		// long, same idea
		double dms_long_deg = Math.floor(dd_long);
		double dms_long_min = Math.floor((dd_long - dms_long_deg) * 60d);
		double dms_long_sec = Math.round((((dd_long - dms_long_deg) * 60d) - dms_long_min) * 60d);
		
		spnDMSLongDeg.setValue((Double) dms_long_deg * longMultiplier);
		spnDMSLongMin.setValue((Double) dms_long_min);
		spnDMSLongSec.setValue((Double) dms_long_sec);


	}

	private void setDDFromDMS() {
		// ok, these should all be integers!
		Double dms_lat_deg = Double.parseDouble(spnDMSLatDeg.getValue().toString());
		Double dms_lat_min = Double.parseDouble(spnDMSLatMin.getValue().toString());
		Double dms_lat_sec = Double.parseDouble(spnDMSLatSec.getValue().toString());
		Double dms_long_deg = Double.parseDouble(spnDMSLongDeg.getValue().toString());
		Double dms_long_min = Double.parseDouble(spnDMSLongMin.getValue().toString());
		Double dms_long_sec = Double.parseDouble(spnDMSLongSec.getValue().toString());

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

	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if (evt.getSource() == btnGPSBrowse) {
			
			File lastFolder = null;
			try{
				lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_GPS, null));
			} catch (Exception e){}
			
			JFileChooser fc = new JFileChooser(lastFolder);
			fc.setFileFilter(filter);
						
			int returnVal = fc.showOpenDialog(LocationGeometry.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				
				// Remember this folder for next time
				App.prefs.setPref(PrefKey.FOLDER_LAST_GPS, fc.getSelectedFile().getPath());

				// Parse GPX
				GPXParser parser = null;
				try {
					txtGPSFilename.setText(fc.getSelectedFile().toString());
					parser = new GPXParser(fc.getSelectedFile().toString());
				} catch (FileNotFoundException e) {
					Alert.error("File not found", "File not found");
				} catch (IOException e) {
					Alert.error("Error", "Error reading file");
				}
				
				ArrayList<GPXWaypoint> wplist = parser.getWaypoints();
				Collections.sort(wplist);
				
				// Grab waypoints from GPX
				for(GPXWaypoint wpt : wplist)
				{
					cboWaypoint.addItem(wpt);
				}
				
			}
		}

		if (evt.getSource() == btnViewMap) {
			// Open map window for current location
		
			if(map!=null) map.dispose();
			
			map = new GISFrame(TridasMarkerLayerBuilder.getMarkerLayerForLatLong(
							(Double)this.spnDDLat.getValue(),
							(Double)this.spnDDLong.getValue()), true,
							(Double)this.spnDDLat.getValue(),
							(Double)this.spnDDLong.getValue()
					);
			
			// Remove modality so we can see map dialog
			dialog.setModal(false);
			dialog.setVisible(false);
			dialog.setVisible(true);
			map.setVisible(true);
			
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
		
		if( evt.getSource() == this.cboWaypoint)
		{
			GPXWaypoint wpt = (GPXWaypoint) cboWaypoint.getSelectedItem();
			this.spnDDLat.setValue(wpt.getLatitude());
			this.spnDDLong.setValue(wpt.getLongitude());
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

class GPXFileFilter extends FileFilter {
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(".gpx");
	}

	public String getDescription() {
		return ".gpx GPS eXchange format";
	}
}
