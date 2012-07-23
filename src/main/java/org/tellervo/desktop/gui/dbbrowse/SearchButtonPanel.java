package org.tellervo.desktop.gui.dbbrowse;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class SearchButtonPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public final JButton btnAddAnotherCriteria;
	public JButton btnReset;
	public final JButton btnSearch;
	private JPanel panel;
	private JLabel lblLimitResponseTo;
	public JSpinner spnLimit;
	private JLabel lblRecordsAndSkip;
	public JSpinner spnSkip;
	
	/**
	 * Create the panel.
	 */
	public SearchButtonPanel() {
		setForeground(Color.GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new MigLayout("", "[grow][grow][][]", "[][]"));
		
		panel = new JPanel();
		add(panel, "cell 0 0 4 1,grow");
		panel.setLayout(new MigLayout("", "[128px][][grow]", "[15px,center][]"));
		
		lblLimitResponseTo = new JLabel("Limit responses to: ");
		panel.add(lblLimitResponseTo, "cell 0 0,alignx left,aligny center");
		
		spnLimit = new JSpinner();
		spnLimit.setModel(new SpinnerNumberModel(100, 1, 500, 10));
		panel.add(spnLimit, "cell 1 0,growx,aligny center");
		
		lblRecordsAndSkip = new JLabel("Skip the first:");
		panel.add(lblRecordsAndSkip, "cell 0 1,aligny center");
		
		spnSkip = new JSpinner();
		spnSkip.setModel(new SpinnerNumberModel(0, 0, 10000, 10));
		panel.add(spnSkip, "cell 1 1,aligny center");
		
		btnAddAnotherCriteria = new JButton("Add another criteria");
		add(btnAddAnotherCriteria, "cell 0 1");
		
		btnReset = new JButton("Reset");
		add(btnReset, "cell 2 1");
		
		btnSearch = new JButton("Search");
		add(btnSearch, "cell 3 1");

	}

}
