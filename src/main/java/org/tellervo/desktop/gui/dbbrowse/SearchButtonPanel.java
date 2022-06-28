package org.tellervo.desktop.gui.dbbrowse;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButtonPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public final JButton btnAddAnotherCriteria;
	public JButton btnReset;
	public final JButton btnSearch;
	private JLabel lblLimitResponseTo;
	public JSpinner spnLimit;
	private JLabel lblRecordsAndSkip;
	public JSpinner spnSkip;
	private JButton btnPrevious;
	private JButton btnNext;
	
	/**
	 * Create the panel.
	 */
	public SearchButtonPanel() {
		setForeground(Color.GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new MigLayout("", "[][58.00,right][grow][110:110.00:110px,fill][110px:110px:110px,fill]", "[][][]"));
		
		btnAddAnotherCriteria = new JButton("Add another criteria");
		add(btnAddAnotherCriteria, "cell 3 0 2 1,alignx right");
		
		btnPrevious = new JButton("< Previous");
		
		btnPrevious.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				searchForPrevious();
				
			}
			
		});
		
		lblLimitResponseTo = new JLabel("Limit responses to: ");
		add(lblLimitResponseTo, "cell 0 1");
		
		spnLimit = new JSpinner();
		add(spnLimit, "cell 1 1,growx");
		spnLimit.setModel(new SpinnerNumberModel(100, 1, 2000, 10));
		
		add(btnPrevious, "cell 3 1");
		
		btnNext = new JButton("Next >");
		
		btnNext.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				searchForNext();
			}
			
			
		});
		
		add(btnNext, "cell 4 1");
		
		lblRecordsAndSkip = new JLabel("Skip the first:");
		add(lblRecordsAndSkip, "cell 0 2");
		
		spnSkip = new JSpinner();
		add(spnSkip, "cell 1 2,growx");
		spnSkip.setModel(new SpinnerNumberModel(0, 0, 10000, 10));
		
		btnReset = new JButton("Reset");
		add(btnReset, "cell 3 2,growx");
		
		btnSearch = new JButton("Search");
		add(btnSearch, "cell 4 2");

	}
	
	private void searchForPrevious()
	{
		Integer skip = (Integer) spnSkip.getValue();
		Integer limit = (Integer) spnLimit.getValue();
		
		if(skip-limit>-1)
		{
			this.spnSkip.setValue(skip-limit);
		}
	}

	private void searchForNext()
	{
		Integer skip = (Integer) spnSkip.getValue();
		Integer limit = (Integer) spnLimit.getValue();
		
		this.spnSkip.setValue(skip+limit);
		
		
	}
}
