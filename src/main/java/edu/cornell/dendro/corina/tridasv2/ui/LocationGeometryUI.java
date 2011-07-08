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
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

/*
 * LocationGeometry.java
 *
 * Created on August 3, 2009, 11:01 AM
 */



/**
 *
 * @author  peterbrewer
 */
public class LocationGeometryUI extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	
	/** Creates new form LocationGeometry */
    public LocationGeometryUI() {
        initComponents();
     
        // change to degrees using unicode!
        lblLatDeg.setText("\u00B0");
        lblLatDeg1.setText("\u00B0");
        lblLongDeg.setText("\u00B0");
        lblLatDeg2.setText("\u00B0");
        
        // make doubley spinners have a min of 3 decimal places shown and a max of 5 (no more max makes sense) 
        ((JSpinner.NumberEditor) spnDDLat.getEditor()).getFormat().setMinimumFractionDigits(4);
        ((JSpinner.NumberEditor) spnDDLat.getEditor()).getFormat().setMaximumFractionDigits(6);
        ((JSpinner.NumberEditor) spnDDLong.getEditor()).getFormat().setMinimumFractionDigits(4);
        ((JSpinner.NumberEditor) spnDDLong.getEditor()).getFormat().setMaximumFractionDigits(6);
        
        
        ((JSpinner.NumberEditor) spnDMSLatMin.getEditor()).getFormat().setMinimumFractionDigits(0);
        ((JSpinner.NumberEditor) spnDMSLatMin.getEditor()).getFormat().setMaximumFractionDigits(2);
        ((JSpinner.NumberEditor) spnDMSLatSec.getEditor()).getFormat().setMinimumFractionDigits(4);
        ((JSpinner.NumberEditor) spnDMSLatSec.getEditor()).getFormat().setMaximumFractionDigits(6);
        ((JSpinner.NumberEditor) spnDMSLongMin.getEditor()).getFormat().setMinimumFractionDigits(0);
        ((JSpinner.NumberEditor) spnDMSLongMin.getEditor()).getFormat().setMaximumFractionDigits(2);
        ((JSpinner.NumberEditor) spnDMSLongSec.getEditor()).getFormat().setMinimumFractionDigits(4);
        ((JSpinner.NumberEditor) spnDMSLongSec.getEditor()).getFormat().setMaximumFractionDigits(6);
       
        spnDMSLatMin.setModel(new SpinnerNumberModel(0.0d, 0.0d, 59.99d, 0.1));
        spnDMSLatSec.setModel(new SpinnerNumberModel(0.0d, 0.0d, 59.99d, 0.1));
        spnDMSLongMin.setModel(new SpinnerNumberModel(0.0d, 0.0d, 59.99d, 0.1));
        spnDMSLongSec.setModel(new SpinnerNumberModel(0.0d, 0.0d, 59.99d, 0.1));
        setLayout(new MigLayout("", "[130px][97px,grow][183.00px][125.00px]", "[23px][25px][24px][24px][24px][98px,grow][94px,grow][42.00px]"));
        add(radManual, "cell 1 0,alignx left,aligny top");
        add(radGPS, "cell 2 0,growx,aligny top");
        add(lblRadio, "cell 0 0 4 1,alignx left,aligny center");
        add(panelDecDeg, "cell 0 6 3 1,alignx left,aligny top");
        panelDecDeg.setLayout(new MigLayout("insets 0", "[130px][120px][7px]", "[23px][23px]"));
        panelDecDeg.add(lblDDLong, "cell 0 1,alignx left,aligny center");
        panelDecDeg.add(lblDDLat, "cell 0 0,growx,aligny center");
        panelDecDeg.add(spnDDLat, "cell 1 0,growx,aligny bottom");
        panelDecDeg.add(spnDDLong, "cell 1 1,growx,aligny bottom");
        panelDecDeg.add(lblLatDeg1, "cell 2 0,growx,aligny top");
        panelDecDeg.add(lblLatDeg2, "cell 2 1,growx,aligny top");
        add(panelButton, "cell 0 7 4 1,growx,aligny top");
        add(panelDMS, "cell 0 5 3 1,growx,aligny top");
        panelDMS.setLayout(new MigLayout("insets 0", "[130px:130px][89px][9px][89px][4px][89px][8px]", "[34px][34px]"));
        panelDMS.add(lblLat, "cell 0 0,alignx left,aligny center");
        panelDMS.add(lblLong, "cell 0 1,alignx left,aligny center");
        panelDMS.add(spnDMSLatDeg, "cell 1 0,growx,aligny center");
        panelDMS.add(lblLatDeg, "cell 2 0,alignx left,aligny center");
        panelDMS.add(spnDMSLatMin, "cell 3 0,growx,aligny center");
        panelDMS.add(lblLatMin, "cell 4 0,alignx left,growy");
        panelDMS.add(spnDMSLatSec, "cell 5 0,growx,aligny center");
        panelDMS.add(lblLatSec, "cell 6 0,alignx left,aligny center");
        panelDMS.add(spnDMSLongDeg, "cell 1 1,growx,aligny center");
        panelDMS.add(lblLongDeg, "cell 2 1,alignx left,aligny center");
        panelDMS.add(spnDMSLongMin, "cell 3 1,growx,aligny center");
        panelDMS.add(lblLongMin, "cell 4 1,alignx left,growy");
        panelDMS.add(spnDMSLongSec, "cell 5 1,growx,aligny center");
        panelDMS.add(lblLongSec, "cell 6 1,alignx left,aligny center");
        add(lblGPSFilename, "cell 0 1,growx,aligny center");
        add(lblWaypoint, "cell 0 2,growx,aligny center");
        add(lblDatum, "cell 0 3,growx,aligny center");
        add(lblFormat, "cell 0 4,growx,aligny center");
        add(cboLatLongStyle, "cell 1 4 3 1,growx,aligny top");
        add(cboDatum, "cell 1 3 3 1,growx,aligny top");
        add(cboWaypoint, "cell 1 2 3 1,growx,aligny top");
        add(txtGPSFilename, "cell 1 1 2 1,growx,aligny center");
        add(btnGPSBrowse, "cell 3 1,growx,aligny top");

    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        panelDMS = new JPanel();
        lblLong = new JLabel();
        lblLat = new JLabel();
        spnDMSLatDeg = new JSpinner();
        lblLatDeg = new JLabel();
        spnDMSLatMin = new JSpinner();
        lblLatMin = new JLabel();
        spnDMSLatSec = new JSpinner();
        lblLatSec = new JLabel();
        spnDMSLongDeg = new JSpinner();
        lblLongDeg = new JLabel();
        spnDMSLongMin = new JSpinner();
        lblLongMin = new JLabel();
        spnDMSLongSec = new JSpinner();
        lblLongSec = new JLabel();
        cboLatLongStyle = new JComboBox();
        lblFormat = new JLabel();
        txtGPSFilename = new JTextField();
        lblGPSFilename = new JLabel();
        btnGPSBrowse = new JButton();
        cboDatum = new JComboBox();
        lblDatum = new JLabel();
        cboWaypoint = new JComboBox();
        lblWaypoint = new JLabel();
        radGPS = new JRadioButton();
        radManual = new JRadioButton();
        panelButton = new JPanel();
        btnOK = new JButton();
        btnCancel = new JButton();
        btnViewMap = new JButton();
        lblRadio = new JLabel();
        panelDecDeg = new JPanel();
        lblDDLong = new JLabel();
        lblDDLat = new JLabel();
        spnDDLat = new JSpinner();
        lblLatDeg1 = new JLabel();
        spnDDLong = new JSpinner();
        lblLatDeg2 = new JLabel();

        setMaximumSize(new Dimension(650, 500));
        setMinimumSize(new Dimension(650, 350));

        lblLong.setText("Longitude:");

        lblLat.setText("Latitude:");

        spnDMSLatDeg.setModel(new SpinnerNumberModel(0, -90, 90, 1));

        lblLatDeg.setFont(new Font("Lucida Grande", 0, 18));
        lblLatDeg.setText("''");

        spnDMSLatMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        lblLatMin.setFont(new Font("Lucida Grande", 0, 18));
        lblLatMin.setText("'");

        spnDMSLatSec.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        lblLatSec.setFont(new Font("Lucida Grande", 0, 18));
        lblLatSec.setText("''");

        spnDMSLongDeg.setModel(new SpinnerNumberModel(0, -180, 180, 1));

        lblLongDeg.setFont(new Font("Lucida Grande", 0, 18));
        lblLongDeg.setText("''");

        spnDMSLongMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        lblLongMin.setFont(new Font("Lucida Grande", 0, 18));
        lblLongMin.setText("'");

        spnDMSLongSec.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        lblLongSec.setFont(new Font("Lucida Grande", 0, 18));
        lblLongSec.setText("''");

        cboLatLongStyle.setModel(new DefaultComboBoxModel(new String[] { "Decimal degrees", "Degrees, minutes and seconds" }));

        lblFormat.setText("Format:");

        lblGPSFilename.setText("GPS filename:");

        btnGPSBrowse.setText("Browse");

        cboDatum.setModel(new DefaultComboBoxModel(new String[] { "WGS84" }));

        lblDatum.setText("Datum:");

        lblWaypoint.setText("Waypoint:");

        radGPS.setText("using waypoint from GPS");

        radManual.setText("manually");

        btnOK.setText("OK");

        btnCancel.setText("Cancel");

        btnViewMap.setText("View on map");

        GroupLayout gl_panelButton = new GroupLayout(panelButton);
        panelButton.setLayout(gl_panelButton);
        gl_panelButton.setHorizontalGroup(
            gl_panelButton.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, gl_panelButton.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnViewMap)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                .addComponent(btnCancel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOK)
                .addContainerGap())
        );
        gl_panelButton.setVerticalGroup(
            gl_panelButton.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, gl_panelButton.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(gl_panelButton.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel)
                    .addComponent(btnViewMap))
                .addContainerGap())
        );

        lblRadio.setText("Set location:");

        lblDDLong.setText("Longitude:");

        lblDDLat.setText("Latitude:");
        lblDDLat.setPreferredSize(new Dimension(85, 16));

        spnDDLat.setModel(new SpinnerNumberModel(0.0d, -90.0d, 90.0d, 0.1));

        lblLatDeg1.setFont(new Font("Lucida Grande", 0, 18)); // NOI18N
        lblLatDeg1.setText("''");

        spnDDLong.setModel(new SpinnerNumberModel(0.0d, -180.0d, 180.0d, 0.1));

        lblLatDeg2.setFont(new Font("Lucida Grande", 0, 18)); // NOI18N
        lblLatDeg2.setText("''");
    }// </editor-fold>


    
    // Variables declaration - do not modify
    protected JButton btnCancel;
    protected JButton btnGPSBrowse;
    protected JButton btnOK;
    protected JButton btnViewMap;
    protected JComboBox cboDatum;
    protected JComboBox cboLatLongStyle;
    protected JComboBox cboWaypoint;
    protected JLabel lblDDLat;
    protected JLabel lblDDLong;
    protected JLabel lblDatum;
    protected JLabel lblFormat;
    protected JLabel lblGPSFilename;
    protected JLabel lblLat;
    protected JLabel lblLatDeg;
    protected JLabel lblLatDeg1;
    protected JLabel lblLatDeg2;
    protected JLabel lblLatMin;
    protected JLabel lblLatSec;
    protected JLabel lblLong;
    protected JLabel lblLongDeg;
    protected JLabel lblLongMin;
    protected JLabel lblLongSec;
    protected JLabel lblRadio;
    protected JLabel lblWaypoint;
    protected JPanel panelButton;
    protected JPanel panelDMS;
    protected JPanel panelDecDeg;
    protected JRadioButton radGPS;
    protected JRadioButton radManual;
    protected JSpinner spnDDLat;
    protected JSpinner spnDDLong;
    protected JSpinner spnDMSLatDeg;
    protected JSpinner spnDMSLatMin;
    protected JSpinner spnDMSLatSec;
    protected JSpinner spnDMSLongDeg;
    protected JSpinner spnDMSLongMin;
    protected JSpinner spnDMSLongSec;
    protected JTextField txtGPSFilename;
    // End of variables declaration
    
}
