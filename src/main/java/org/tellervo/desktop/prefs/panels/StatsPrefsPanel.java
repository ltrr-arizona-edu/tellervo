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
package org.tellervo.desktop.prefs.panels;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.ColorComboBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.FormatWrapper;
import org.tellervo.desktop.prefs.wrappers.SpinnerWrapper;
import org.tellervo.desktop.ui.I18n;

import net.miginfocom.swing.MigLayout;

public class StatsPrefsPanel extends AbstractPreferencesPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox cboWJ;
	private JComboBox cboDScore;
	private JComboBox cboTrend;
	private JComboBox cboRScore;
	private JComboBox cboTScore;
	private JCheckBox chkHighlightSig;
	private JComboBox cboHighlightColor;
	private JSpinner spnMinOverlapDScore;
	private JSpinner spnMinOverlap;
	
	/**
	 * Create the panel.
	 */
	public StatsPrefsPanel(final JDialog parent) {
		super(I18n.getText("preferences.statistics"), 
				"chart.png", 
				"Set preferences for the display and handling of statistics",
				parent);
		
		setLayout(new MigLayout("", "[225.00,grow][grow]", "[][grow]"));
		
		JPanel panelFormats = new JPanel();
		panelFormats.setBorder(new TitledBorder(null, "Number formats", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelFormats, "cell 0 0,grow");
		panelFormats.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		
		JLabel lblTScore = new JLabel("T score:");
		panelFormats.add(lblTScore, "cell 0 0,alignx trailing");
		
		cboTScore = new JComboBox();
        cboTScore.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
		panelFormats.add(cboTScore, "cell 1 0,growx");
		
		JLabel lblNewLabel = new JLabel("R score:");
		panelFormats.add(lblNewLabel, "cell 0 1,alignx trailing");
		
		cboRScore = new JComboBox();
        cboRScore.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
		panelFormats.add(cboRScore, "cell 1 1,growx");
		
		JLabel lblNewLabel_1 = new JLabel("Trend:");
		panelFormats.add(lblNewLabel_1, "cell 0 2,alignx trailing");
		
		cboTrend = new JComboBox();
        cboTrend.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
		panelFormats.add(cboTrend, "cell 1 2,growx");
		
		JLabel lblNewLabel_2 = new JLabel("D score:");
		panelFormats.add(lblNewLabel_2, "cell 0 3,alignx trailing");
		
		cboDScore = new JComboBox();
        cboDScore.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
		panelFormats.add(cboDScore, "cell 1 3,growx");
		
		JLabel lblWeiserjahre = new JLabel("Weiserjahre:");
		panelFormats.add(lblWeiserjahre, "cell 0 4,alignx trailing");
		
		cboWJ = new JComboBox();
        cboWJ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "0.49", "0.492", "0.4915", "0.49152", "49%", "49.2%", "49.15%", "49.152%" }));
		panelFormats.add(cboWJ, "cell 1 4,growx");
		
		JPanel panelSigScores = new JPanel();
		panelSigScores.setBorder(new TitledBorder(null, "Significant scores", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(panelSigScores, "cell 1 0,grow");
		panelSigScores.setLayout(new MigLayout("", "[grow][grow]", "[][][][]"));
		
		JLabel lblNewLabel_3 = new JLabel("Minimum years overlap:");
		panelSigScores.add(lblNewLabel_3, "cell 0 0,alignx right");
		
		spnMinOverlap = new JSpinner();
		spnMinOverlap.setModel(new SpinnerNumberModel(50, 1, 9999, 1));
		panelSigScores.add(spnMinOverlap, "cell 1 0,alignx right");
		
		JLabel lblMinYearsOverlap = new JLabel("Min. years overlap for D score:");
		panelSigScores.add(lblMinYearsOverlap, "cell 0 1,alignx right");
		
		spnMinOverlapDScore = new JSpinner();
		spnMinOverlapDScore.setModel(new SpinnerNumberModel(1, 1, 9999, 1));
		panelSigScores.add(spnMinOverlapDScore, "cell 1 1,alignx right");
		
		chkHighlightSig = new JCheckBox("Highlight significant years");
		panelSigScores.add(chkHighlightSig, "cell 0 2 2 1,alignx right");
		
		JLabel lblHighlightColor = new JLabel("Highlight color:");
		panelSigScores.add(lblHighlightColor, "cell 0 3,alignx trailing");
		
		cboHighlightColor = new JComboBox();
		panelSigScores.add(cboHighlightColor, "cell 1 3,growx");

		linkToPrefs();
	}
	

	
	private void linkToPrefs()
	{
		new FormatWrapper(cboTScore, PrefKey.STATS_FORMAT_TSCORE, "0.00");
		new FormatWrapper(cboRScore, PrefKey.STATS_FORMAT_RVALUE, "0.00");
		new FormatWrapper(cboTrend, PrefKey.STATS_FORMAT_TREND, "0.0%");
		new FormatWrapper(cboDScore, PrefKey.STATS_FORMAT_DSCORE, "0.00");
		new FormatWrapper(cboWJ, PrefKey.STATS_FORMAT_WEISERJAHRE, "0.0%");
		new SpinnerWrapper(spnMinOverlap, "tellervo.cross.overlap", 15);
		new SpinnerWrapper(spnMinOverlapDScore, "tellervo.cross.d-overlap", 100);
		new CheckBoxWrapper(chkHighlightSig, PrefKey.GRID_HIGHLIGHT, true);
		new ColorComboBoxWrapper(cboHighlightColor, PrefKey.GRID_HIGHLIGHTCOLOR, Color.green);
	}



	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
