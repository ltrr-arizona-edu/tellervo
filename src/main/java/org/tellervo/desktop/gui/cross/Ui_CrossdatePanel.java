/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui.cross;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingx.JXTable;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.cross.ShotgunCrossdate;
import org.tellervo.desktop.ui.I18n;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;



/*
 * Ui_CrossdatePanel.java
 *
 * Created on January 13, 2010, 1:29 PM
 */



/**
 *
 * @author  peterbrewer
 */
@SuppressWarnings("serial")
public class Ui_CrossdatePanel extends javax.swing.JPanel {
    
    /** Creates new form Ui_CrossdatePanel */
    public Ui_CrossdatePanel() {
        initComponents();
        internationalizeComponents();
               
        this.cboViewType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				set1To1ViewType(cboViewType.getSelectedIndex());
			}
		});
        
        this.cboViewAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				set1ToNViewType(cboViewAs.getSelectedIndex());
			}
		});
        
        cboViewType.setSelectedIndex(0);
        cboViewAs.setSelectedIndex(0);
    }
    
    
    private void set1To1ViewType(int index)
    {
    	panelSignificantScores.setVisible(false);
    	panelAllScores.setVisible(false);
    	panelHistogram.setVisible(false);
    	
    	switch (index)
    	{
	    	case 0: panelSignificantScores.setVisible(true); break;
	    	case 1: panelAllScores.setVisible(true); break;
	    	case 2: panelHistogram.setVisible(true); break;
    	}
    }
    
    private void set1ToNViewType(int index)
    {
    	panel1ToNTable.setVisible(false);
    	panelStatMap.setVisible(false);
    	
    	switch(index)
    	{
    		case 0: panel1ToNTable.setVisible(true); break;
    		case 1: panelStatMap.setVisible(true); break;
    	}
    	
    }
    
    private void internationalizeComponents()
    {
    	if(!App.isInitialized()) App.init();
    	this.lblPrimary.setText(I18n.getText("crossdate.floatingSeries")+":");
    	this.lblSecondary.setText(I18n.getText("crossdate.referenceSeries")+":");
    	this.btnAddRemoveSeries.setText(I18n.getText("crossdate.addRemoveSeries"));
    	this.btnResetPosition.setText(I18n.getText("crossdate.resetPosition"));
    	this.lblStat.setText(I18n.getText("crossdate.preferredStat")+":");
    	this.btnCancel.setText(I18n.getText("general.cancel"));
    	this.btnOk.setText(I18n.getText("crossdate.applyCrossdate"));
    	this.lblViewAs.setText(I18n.getText("crossdate.viewAs")+":");
    	this.lblMatchType.setText(I18n.getText("crossdate.matchType")+":");
    	this.lblViewType.setText(I18n.getText("crossdate.viewAs")+":");
    	cboViewType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { I18n.getText("crossdate.sigScoresOnly"), I18n.getText("crossdate.allScores"), I18n.getText("crossdate.histogramOfScores") }));
        cboMatchType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { I18n.getText("crossdate.scoresInCurrentChronologicalPosition"), I18n.getText("crossdate.bestScoreInAnyPosition") }));
        cboViewAs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Table", "Map" }));
    	this.paneStatistics.setTitleAt(0, I18n.getText("crossdate.1-to-1"));
    	this.paneStatistics.setTitleAt(1, I18n.getText("crossdate.1-to-n"));
    	this.paneStatistics.setTitleAt(2, I18n.getText("crossdate.n-to-n"));
    	
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelButtons = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        lblStat = new javax.swing.JLabel();
        cboDisplayStats = new javax.swing.JComboBox();
        paneTablesAndCharts = new javax.swing.JSplitPane();
        paneStatistics = new javax.swing.JTabbedPane();
        panel1To1 = new javax.swing.JPanel();
        lblViewType = new javax.swing.JLabel();
        cboViewType = new javax.swing.JComboBox();
        panelStats = new javax.swing.JPanel();
        panelHistogram = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblHistogram = new javax.swing.JTable();
        panelSignificantScores = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSignificantScores = new JXTable();
        panelAllScores = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblAllScores = new javax.swing.JTable();
        panel1ToN = new javax.swing.JPanel();
        lblMatchType = new javax.swing.JLabel();
        cboMatchType = new javax.swing.JComboBox();
        cboViewAs = new javax.swing.JComboBox();
        lblViewAs = new javax.swing.JLabel();
        panel1ToNTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl1ToNStats = new javax.swing.JTable();
        panelStatMap = new javax.swing.JPanel();
        panelNToN = new JPanel();
        panelChart = new javax.swing.JPanel();
        panelDetails = new javax.swing.JPanel();
        btnSwap = new javax.swing.JButton();
        lblPrimary = new javax.swing.JLabel();
        lblSecondary = new javax.swing.JLabel();
        cboFloating = new javax.swing.JComboBox();
        cboReference = new javax.swing.JComboBox();
        btnAddRemoveSeries = new javax.swing.JButton();
        btnResetPosition = new javax.swing.JButton();
        scrollInfo = new javax.swing.JScrollPane();
        txtInfo = new javax.swing.JTextArea();

        btnCancel.setText("Cancel");

        btnOk.setText("Apply Crossdate");

        lblStat.setText("Preferred statistic:");

        cboDisplayStats.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "T-Score", "R-Score", "D-Score" }));

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(lblStat)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cboDisplayStats, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 396, Short.MAX_VALUE)
                .add(btnCancel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOk)
                .addContainerGap())
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnOk)
                    .add(btnCancel)
                    .add(lblStat)
                    .add(cboDisplayStats, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneTablesAndCharts.setDividerLocation(180);
        paneTablesAndCharts.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        paneTablesAndCharts.setResizeWeight(0.5);
        paneTablesAndCharts.setOneTouchExpandable(true);

        paneStatistics.setMinimumSize(new java.awt.Dimension(0, 0));
        paneStatistics.setPreferredSize(new java.awt.Dimension(150, 88));

        lblViewType.setText("View as:");

        cboViewType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Significant scores only", "All scores", "Histogram of scores" }));

        tblHistogram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "T-score", "#", "Histogram"
            }
        ));
        jScrollPane6.setViewportView(tblHistogram);

        org.jdesktop.layout.GroupLayout panelHistogramLayout = new org.jdesktop.layout.GroupLayout(panelHistogram);
        panelHistogram.setLayout(panelHistogramLayout);
        panelHistogramLayout.setHorizontalGroup(
            panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHistogramLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelHistogramLayout.setVerticalGroup(
            panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHistogramLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        tblSignificantScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Range", "Overlap", "Trend", "T-Score", "R-Value", "D-Score", "Weiserjahre"
            }
        ));
        jScrollPane2.setViewportView(tblSignificantScores);

        org.jdesktop.layout.GroupLayout panelSignificantScoresLayout = new org.jdesktop.layout.GroupLayout(panelSignificantScores);
        panelSignificantScores.setLayout(panelSignificantScoresLayout);
        panelSignificantScoresLayout.setHorizontalGroup(
            panelSignificantScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSignificantScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSignificantScoresLayout.setVerticalGroup(
            panelSignificantScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSignificantScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        tblAllScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Year", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
            }
        ));
        tblAllScores.setPreferredSize(new java.awt.Dimension(100, 64));
        jScrollPane5.setViewportView(tblAllScores);

        org.jdesktop.layout.GroupLayout panelAllScoresLayout = new org.jdesktop.layout.GroupLayout(panelAllScores);
        panelAllScores.setLayout(panelAllScoresLayout);
        panelAllScoresLayout.setHorizontalGroup(
            panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAllScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelAllScoresLayout.setVerticalGroup(
            panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAllScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout panelStatsLayout = new org.jdesktop.layout.GroupLayout(panelStats);
        panelStats.setLayout(panelStatsLayout);
        panelStatsLayout.setHorizontalGroup(
            panelStatsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelStatsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAllScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelSignificantScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelHistogram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelStatsLayout.setVerticalGroup(
            panelStatsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelStatsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelStatsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelAllScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelSignificantScores, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelHistogram, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout panel1To1Layout = new org.jdesktop.layout.GroupLayout(panel1To1);
        panel1To1.setLayout(panel1To1Layout);
        panel1To1Layout.setHorizontalGroup(
            panel1To1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1To1Layout.createSequentialGroup()
                .addContainerGap()
                .add(panel1To1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelStats, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panel1To1Layout.createSequentialGroup()
                        .add(lblViewType)
                        .add(18, 18, 18)
                        .add(cboViewType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel1To1Layout.setVerticalGroup(
            panel1To1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1To1Layout.createSequentialGroup()
                .addContainerGap()
                .add(panel1To1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblViewType)
                    .add(cboViewType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelStats, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneStatistics.addTab("Floating against a reference series (1 to 1)", panel1To1);

        lblMatchType.setText("Match type:");

        cboMatchType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Scores in current chronological positions", "Best score in any chronological position" }));

        cboViewAs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Table", "Map" }));

        lblViewAs.setText("View as:");

        tbl1ToNStats.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Series", "T-Score", "R-Value", "Trend", "Overlap", "Distance"
            }
        ));
        jScrollPane1.setViewportView(tbl1ToNStats);

        org.jdesktop.layout.GroupLayout panel1ToNTableLayout = new org.jdesktop.layout.GroupLayout(panel1ToNTable);
        panel1ToNTable.setLayout(panel1ToNTableLayout);
        panel1ToNTableLayout.setHorizontalGroup(
            panel1ToNTableLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1ToNTableLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel1ToNTableLayout.setVerticalGroup(
            panel1ToNTableLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1ToNTableLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout panelStatMapLayout = new org.jdesktop.layout.GroupLayout(panelStatMap);
        panelStatMap.setLayout(panelStatMapLayout);
        panelStatMapLayout.setHorizontalGroup(
            panelStatMapLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 432, Short.MAX_VALUE)
        );
        panelStatMapLayout.setVerticalGroup(
            panelStatMapLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 289, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout panel1ToNLayout = new org.jdesktop.layout.GroupLayout(panel1ToN);
        panel1ToN.setLayout(panel1ToNLayout);
        panel1ToNLayout.setHorizontalGroup(
            panel1ToNLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1ToNLayout.createSequentialGroup()
                .addContainerGap()
                .add(panel1ToNLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panel1ToNLayout.createSequentialGroup()
                        .add(panel1ToNTable, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelStatMap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(panel1ToNLayout.createSequentialGroup()
                        .add(lblMatchType)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cboMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(lblViewAs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cboViewAs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel1ToNLayout.setVerticalGroup(
            panel1ToNLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panel1ToNLayout.createSequentialGroup()
                .addContainerGap()
                .add(panel1ToNLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblMatchType)
                    .add(cboMatchType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblViewAs)
                    .add(cboViewAs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panel1ToNLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panel1ToNTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(panelStatMap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        paneStatistics.addTab("Floating against all reference series (1 to n)", panel1ToN);

        paneStatistics.addTab("Compare all series (n to n)", panelNToN);
        panelNToN.setLayout(new BorderLayout(0, 0));

        paneTablesAndCharts.setLeftComponent(paneStatistics);

        panelChart.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout panelChartLayout = new org.jdesktop.layout.GroupLayout(panelChart);
        panelChart.setLayout(panelChartLayout);
        panelChartLayout.setHorizontalGroup(
            panelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 862, Short.MAX_VALUE)
        );
        panelChartLayout.setVerticalGroup(
            panelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 223, Short.MAX_VALUE)
        );

        paneTablesAndCharts.setRightComponent(panelChart);

        btnSwap.setMaximumSize(new java.awt.Dimension(40, 29));
        btnSwap.setMinimumSize(new java.awt.Dimension(40, 29));
        btnSwap.setPreferredSize(new java.awt.Dimension(40, 29));

        lblPrimary.setText("Floating series:");

        lblSecondary.setText("Reference series (fixed):");

        cboFloating.setModel(new javax.swing.DefaultComboBoxModel(new String[] { App.getLabCodePrefix()+"ABC-1-A" }));

        cboReference.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "My Master Chronology" }));
        cboReference.setMinimumSize(new java.awt.Dimension(131, 27));
        cboReference.setPreferredSize(new java.awt.Dimension(131, 27));

        btnAddRemoveSeries.setText("Add / Remove series");

        btnResetPosition.setText("Reset positions");
        btnResetPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPositionActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout panelDetailsLayout = new org.jdesktop.layout.GroupLayout(panelDetails);
        panelDetails.setLayout(panelDetailsLayout);
        panelDetailsLayout.setHorizontalGroup(
            panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelDetailsLayout.createSequentialGroup()
                        .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblPrimary)
                            .add(cboFloating, 0, 420, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSwap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblSecondary)
                            .add(cboReference, 0, 386, Short.MAX_VALUE)))
                    .add(panelDetailsLayout.createSequentialGroup()
                        .add(btnAddRemoveSeries)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnResetPosition)))
                .addContainerGap())
        );
        panelDetailsLayout.setVerticalGroup(
            panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblPrimary)
                    .add(lblSecondary))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(btnSwap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboFloating, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cboReference, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(panelDetailsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnAddRemoveSeries)
                    .add(btnResetPosition)))
        );

        txtInfo.setColumns(20);
        txtInfo.setRows(5);
        txtInfo.setFocusable(false);
        scrollInfo.setViewportView(txtInfo);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(paneTablesAndCharts, 0, 0, Short.MAX_VALUE)
                    .add(panelDetails, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(scrollInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelDetails, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(paneTablesAndCharts, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnResetPositionActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnAddRemoveSeries;
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnOk;
    protected javax.swing.JButton btnResetPosition;
    protected javax.swing.JButton btnSwap;
    protected javax.swing.JComboBox cboDisplayStats;
    protected javax.swing.JComboBox cboFloating;
    protected javax.swing.JComboBox cboMatchType;
    protected javax.swing.JComboBox cboReference;
    protected javax.swing.JComboBox cboViewAs;
    protected javax.swing.JComboBox cboViewType;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JScrollPane jScrollPane5;
    protected javax.swing.JScrollPane jScrollPane6;
    protected javax.swing.JLabel lblMatchType;
    protected javax.swing.JLabel lblPrimary;
    protected javax.swing.JLabel lblSecondary;
    protected javax.swing.JLabel lblStat;
    protected javax.swing.JLabel lblViewAs;
    protected javax.swing.JLabel lblViewType;
    protected javax.swing.JTabbedPane paneStatistics;
    protected javax.swing.JSplitPane paneTablesAndCharts;
    protected javax.swing.JPanel panel1To1;
    protected javax.swing.JPanel panel1ToN;
    protected javax.swing.JPanel panel1ToNTable;
    protected javax.swing.JPanel panelAllScores;
    protected javax.swing.JPanel panelButtons;
    protected javax.swing.JPanel panelChart;
    protected javax.swing.JPanel panelDetails;
    protected javax.swing.JPanel panelHistogram;
    protected JPanel panelNToN;
    protected javax.swing.JPanel panelSignificantScores;
    protected javax.swing.JPanel panelStatMap;
    protected javax.swing.JPanel panelStats;
    protected javax.swing.JScrollPane scrollInfo;
    protected JXTable tblSignificantScores;
    protected javax.swing.JTable tbl1ToNStats;
    protected javax.swing.JTable tblAllScores;
    protected javax.swing.JTable tblHistogram;
    protected javax.swing.JTextArea txtInfo;
}
