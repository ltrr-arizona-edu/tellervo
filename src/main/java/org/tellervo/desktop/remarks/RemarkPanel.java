package org.tellervo.desktop.remarks;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

public class RemarkPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtFreeTextRemark;

	/**
	 * Create the panel.
	 */
	public RemarkPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		add(tabbedPane);
		
		JPanel currentRingRemarksPanel = new JPanel();
		currentRingRemarksPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tabbedPane.addTab("Remarks", null, currentRingRemarksPanel, null);
		currentRingRemarksPanel.setLayout(new MigLayout("", "[3px,grow,fill]", "[3px,grow,fill][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		currentRingRemarksPanel.add(scrollPane, "cell 0 0,grow");
		
		JPanel panelRemarkList = new JPanel();
		scrollPane.setViewportView(panelRemarkList);
		panelRemarkList.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		JLabel lblOf = new JLabel("0 of 3");
		panelRemarkList.add(lblOf, "cell 0 0");
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		panelRemarkList.add(chckbxNewCheckBox, "cell 1 0");
		
		JPanel addRemarkPanel = new JPanel();
		currentRingRemarksPanel.add(addRemarkPanel, "cell 0 1,grow");
		addRemarkPanel.setLayout(new MigLayout("", "[114px,grow,fill][61px]", "[25px]"));
		
		txtFreeTextRemark = new JTextField();
		addRemarkPanel.add(txtFreeTextRemark, "cell 0 0,alignx left,aligny center");
		txtFreeTextRemark.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		addRemarkPanel.add(btnAdd, "cell 1 0,alignx left,aligny top");
		
		JPanel remarkSettingsPanel = new JPanel();
		remarkSettingsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tabbedPane.addTab("Settings", null, remarkSettingsPanel, null);
		remarkSettingsPanel.setLayout(new MigLayout("", "[grow]", "[][][grow][]"));
		
		JPanel showRemarkThresholdPanel = new JPanel();
		remarkSettingsPanel.add(showRemarkThresholdPanel, "cell 0 0,grow");
		showRemarkThresholdPanel.setLayout(new MigLayout("", "[grow][]", "[][]"));
		
		JLabel lblShowRemarksWhen = new JLabel("Show remarks when present in...");
		showRemarkThresholdPanel.add(lblShowRemarksWhen, "cell 0 0");
		
		JLabel lblGreaterThan = new JLabel(">");
		showRemarkThresholdPanel.add(lblGreaterThan, "flowx,cell 0 1");
		
		JSpinner spnRemarkThreshold = new JSpinner();
		spnRemarkThreshold.setModel(new SpinnerNumberModel(80, 0, 100, 1));
		showRemarkThresholdPanel.add(spnRemarkThreshold, "cell 0 1");
		
		JLabel lblOfConstituent = new JLabel("% of constituent series");
		showRemarkThresholdPanel.add(lblOfConstituent, "cell 0 1");
		
		JLabel lblShowTheFollowing = new JLabel("Show the following remarks on screen when present:");
		remarkSettingsPanel.add(lblShowTheFollowing, "cell 0 1");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		remarkSettingsPanel.add(scrollPane_1, "cell 0 2,grow");
		
		JPanel panel = new JPanel();
		scrollPane_1.setViewportView(panel);
		
		JButton btnRevertToDefaults = new JButton("Revert to defaults");
		remarkSettingsPanel.add(btnRevertToDefaults, "cell 0 3,alignx right");

	}
}
