package org.tellervo.desktop.remarks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.editor.DecadalModel;
import org.tellervo.desktop.editor.UnitAwareDecadalModel;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.wrappers.CheckBoxWrapper;
import org.tellervo.desktop.prefs.wrappers.SpinnerWrapper;
import org.tellervo.desktop.sample.Sample;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasRemark;
import org.tridas.schema.TridasValue;

public class RemarkPanel extends JPanel implements ListSelectionListener, TableColumnModelListener {

	private static final long serialVersionUID = 1L;
	private JTextField txtFreeTextRemark;
	private JPanel panelRemarkList;
	private JTable table;
	private Sample sample; 
	private final static Logger log = LoggerFactory.getLogger(RemarkPanel.class);
	private Integer row;
	private Integer col;
	private JButton btnAdd;
	/**
	 * Create the panel.
	 */
	public RemarkPanel(JTable table, Sample s) {
		
		this.table = table;
		this.sample = s;
		init();
		setForTridasValue(getCurrentTridasValue());
		
		table.getSelectionModel().addListSelectionListener(this);
		table.getColumnModel().addColumnModelListener(this);
		
		row = table.getSelectedRow();
		col = table.getSelectedColumn();
		setForTridasValue(getCurrentTridasValue());
		
		
			
	}
	
	private TridasValue getCurrentTridasValue()
	{	
		if(col==null || row==null) return null;
		
		// bail out if not on data
		if (col<1 || col>10) {
		    return null;
		}
	
		// get year from (row,col)
		Year y = ((DecadalModel) table.getModel()).getYear(row, col);
		
		try{
			TridasValue ret = sample.getRingWidthValueForYear(y);
			return ret;
		} catch (IndexOutOfBoundsException e )
		{
			log.debug("Failed to get current tridas value for year "+y+ " from row "+row+" and col "+col);
			return null;
		}
		
		

	}
	
	private void setForTridasValue(final TridasValue value)
	{		
		if(value==null) return;
				
		//log.debug("Removing existing remarks from panel");
		panelRemarkList.removeAll();
		panelRemarkList.repaint();
		
		int i =0;
		for(final Remark remark : Remarks.getRemarks())
		{
			addItemToGui(remark, value, i);
			i++;
		}
		
		// Now include user remarks too
		for(TridasRemark r : value.getRemarks())
		{
			if(r.isSetNormalTridas() || r.isSetNormalStd()) continue;
			Remark rm = new UserRemark(r);
			addItemToGui(rm, value, i);
			i++;
		}
		
	}
	
	private void addItemToGui(final Remark remark, final TridasValue value, int i)
	{
		JLabel icon = new JLabel("");
		icon.setIcon(remark.getIcon());
		panelRemarkList.add(icon, "cell 0 "+i);
		
		String remarkStr = remark.getDisplayName().substring(0,1).toUpperCase()+remark.getDisplayName().substring(1);
			
		final JCheckBox chkRemark = new JCheckBox(remarkStr);
		Font font = new Font("Dialog", Font.ITALIC, 12);
		
		
		if(remark instanceof UserRemark) chkRemark.setFont(font);
		
		final Integer thisrow = row;
		final Integer thiscol = col;
		chkRemark.setSelected(remark.isRemarkSet(value));

		
		chkRemark.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean remove = remark.isRemarkSet(value);
				boolean inherited = remark.isRemarkInherited(value);
				boolean isuserremark = remark instanceof UserRemark;
				
				if(isuserremark){
				
					Object[] options = {"Yes",
							"No",
							"Cancel"};

					int n = JOptionPane.showOptionDialog(chkRemark, 
						"Are you sure you want to delete this remark?",
						"Delete remark",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);
										
					if(n==JOptionPane.YES_OPTION)
					{
						remark.removeRemark(value);
						panelRemarkList.remove(chkRemark);
						panelRemarkList.repaint();
					}
				}
				else if(remove) {
					chkRemark.setSelected(false);
					
					if(inherited)
						remark.overrideRemark(value);
					else
						remark.removeRemark(value);
				}
				else {
					chkRemark.setSelected(true);
					remark.applyRemark(value);
				}
				
				if(thisrow!=null && thiscol!=null)
				{
					((UnitAwareDecadalModel)table.getModel()).fireTableCellUpdated(thisrow, thiscol);
					
					// set the sample as modified
					sample.setModified();
					sample.fireSampleDataChanged();
					table.changeSelection(thisrow, thiscol, false, false);
				}
				else
				{
					log.debug("can't set remarks as row/col null");
				}

				
			}
			
		});
		
		if (!sample.isEditable())
		{
			chkRemark.setEnabled(false);
		}
		
		panelRemarkList.add(chkRemark, "cell 1 "+i);
		
	}
	
	private void init()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel currentRingRemarksPanel = new JPanel();
		add(currentRingRemarksPanel, BorderLayout.CENTER);
		currentRingRemarksPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		currentRingRemarksPanel.setLayout(new MigLayout("hidemode 3", "[grow,fill]", "[3px,grow,fill][]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Current ring remarks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		currentRingRemarksPanel.add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("hidemode 3", "[grow,fill]", "[grow,fill][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		panel.add(scrollPane, "cell 0 0");
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelRemarkList = new JPanel();
		panelRemarkList.setBorder(null);
		scrollPane.setViewportView(panelRemarkList);
		panelRemarkList.setLayout(new MigLayout("hidemode 3", "[][grow]", "[fill]"));
		
		
		
		JPanel addRemarkPanel = new JPanel();
		panel.add(addRemarkPanel, "cell 0 1");
		addRemarkPanel.setLayout(new MigLayout("hidemode 3", "[114px,grow,fill][61px]", "[25px]"));
		
		txtFreeTextRemark = new JTextField();
		addRemarkPanel.add(txtFreeTextRemark, "cell 0 0,alignx left,aligny center");
		txtFreeTextRemark.setColumns(10);
		

		
		btnAdd = new JButton("Add");
		addRemarkPanel.add(btnAdd, "cell 1 0,alignx left,aligny top");
		btnAdd.setEnabled(false);
		
		txtFreeTextRemark.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				 setAddButton();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				 setAddButton();
				
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				 setAddButton();
			}
			
		});
		
		txtFreeTextRemark.addKeyListener(new KeyAdapter(){

			@Override
			public void keyPressed(KeyEvent arg0) {					
			}
			@Override
			public void keyReleased(KeyEvent arg) {	
				if (arg.getKeyCode() == KeyEvent.VK_ENTER ) {
					log.debug("Enter pressed");
					addFreeTextComment();
				}
				else
				{
					log.debug("Something else pressed so ignoring");
					log.debug("Keycode was "+ arg.getKeyCode());
					log.debug("Keycode for enter is "+KeyEvent.VK_ENTER);
				}
			}
			@Override
			public void keyTyped(KeyEvent arg) {

			}
			
		});
		
		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				addFreeTextComment();

			}
			
		});
		
		JPanel showRemarkThresholdPanel = new JPanel();
		showRemarkThresholdPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Remark settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		currentRingRemarksPanel.add(showRemarkThresholdPanel, "cell 0 1");
		showRemarkThresholdPanel.setLayout(new MigLayout("", "[][][134px,grow]", "[20px][center][]"));
		
		JLabel lblShowRemarksWhen = new JLabel("Show remarks when present in:");
		showRemarkThresholdPanel.add(lblShowRemarksWhen, "cell 0 0 3 1,alignx left,aligny center");
		
		JLabel label = new JLabel(">");
		showRemarkThresholdPanel.add(label, "cell 0 1,alignx right");
		
		JSpinner spnRemarkThreshold = new JSpinner();
		spnRemarkThreshold.setModel(new SpinnerNumberModel(80, 0, 100, 1));
		new SpinnerWrapper(spnRemarkThreshold, PrefKey.DERIVED_REMARKS_THRESHOLD, 80);
		showRemarkThresholdPanel.add(spnRemarkThreshold, "cell 1 1,alignx center,aligny center");
		
		JLabel lblOfConstituent = new JLabel("% of constituent series");
		showRemarkThresholdPanel.add(lblOfConstituent, "cell 2 1,alignx left,aligny center");
		
		JCheckBox chkHidePinningAndRadiusShifts = new JCheckBox("Hide pinning and radius shifts");
		new CheckBoxWrapper(chkHidePinningAndRadiusShifts, PrefKey.HIDE_PINNING_AND_RADIUS_SHIFT_ICONS, false );
		showRemarkThresholdPanel.add(chkHidePinningAndRadiusShifts, "cell 0 2 3 1");

		if(sample.getSeries() instanceof TridasDerivedSeries)
		{
			showRemarkThresholdPanel.setVisible(true);
			txtFreeTextRemark.setVisible(false);
			btnAdd.setVisible(false);
			
		}
		else
		{
			showRemarkThresholdPanel.setVisible(false);
		}
	}

	private void setAddButton()
	{
		if(row<0 || col<0) 
		{
			btnAdd.setEnabled(false);
			return;
		}
		
		if (!sample.isEditable())
		{
			btnAdd.setEnabled(false);
			return;
		}
		
		
		String note = txtFreeTextRemark.getText();
		if(note==null) return;
		btnAdd.setEnabled(note.length()>0);
	}
	
	private void addFreeTextComment()
	{
		if(!btnAdd.isEnabled()) return;
		
		TridasValue value = getCurrentTridasValue();
		
		TridasRemark r = new TridasRemark();
		r.setValue(txtFreeTextRemark.getText());
		
		log.debug("Applying free text remark to value");
		value.getRemarks().add(r);
		
		sample.setModified();
		sample.fireSampleDataChanged();
		table.changeSelection(row, col, false, false);
		
		txtFreeTextRemark.setText("");
		
		setForTridasValue(value);
		
		table.changeSelection(row, col, false, false);
	}
	
	@Override
	public void columnAdded(TableColumnModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void columnMarginChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void columnMoved(TableColumnModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void columnRemoved(TableColumnModelEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
		
		row = table.getSelectedRow();
		col = table.getSelectedColumn();
		//log.debug("New cell clicked... ("+row+","+col+")");
		
		setForTridasValue(getCurrentTridasValue());
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting()) return;
		
        if (e.getSource() == table.getSelectionModel()
                && table.getRowSelectionAllowed()) {
              // Column selection changed
        	 // log.debug("Row selection event");
              int first = e.getFirstIndex();
              int last = e.getLastIndex();
          } else if (e.getSource() == table.getColumnModel().getSelectionModel()
                 && table.getColumnSelectionAllowed() ){
              // Row selection changed
        	  //log.debug("column selection event");

              int first = e.getFirstIndex();
              int last = e.getLastIndex();
          }
		
		int thisrow = table.getSelectedRow();
		int thiscol = table.getSelectedColumn();
		
		if(thisrow>-1 && thiscol>-1)
		{
			//log.debug("New cell clicked... ("+row+","+col+")");
			row = thisrow;
			col = thiscol;
			setForTridasValue(getCurrentTridasValue());
		}
		
	}
}
