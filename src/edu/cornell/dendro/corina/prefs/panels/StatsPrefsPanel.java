package edu.cornell.dendro.corina.prefs.panels;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.prefs.wrappers.CheckBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.ColorComboBoxWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.FormatWrapper;
import edu.cornell.dendro.corina.prefs.wrappers.SpinnerWrapper;
import edu.cornell.dendro.corina.ui.I18n;

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
	public StatsPrefsPanel() {
		super(I18n.getText("preferences.statistics"), 
				"chart.png", 
				"Set preferences for the display and handling of statistics");
		
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
		new FormatWrapper(cboTScore, "corina.cross.tscore.format", "0.00");
		new FormatWrapper(cboRScore, "corina.cross.rvalue.format", "0.00");
		new FormatWrapper(cboTrend, "corina.cross.trend.format", "0.0%");
		new FormatWrapper(cboDScore, "corina.cross.dscore.format", "0.00");
		new FormatWrapper(cboWJ, "corina.cross.weiserjahre.format", "0.0%");
		new SpinnerWrapper(spnMinOverlap, "corina.cross.overlap", 15);
		new SpinnerWrapper(spnMinOverlapDScore, "corina.cross.d-overlap", 100);
		new CheckBoxWrapper(chkHighlightSig, Prefs.GRID_HIGHLIGHT, true);
		new ColorComboBoxWrapper(cboHighlightColor, Prefs.GRID_HIGHLIGHTCOLOR, Color.green);
	}

}
