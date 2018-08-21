package org.tellervo.desktop.labelgen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tridas.schema.TridasSample;


public class LGWizardSamplePicker extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JList lstSelected;
	private JList lstAvailable;
	protected ArrayListModel<TridasSample> selModel = new ArrayListModel<TridasSample>();
	protected ArrayListModel<TridasSample> availModel = new ArrayListModel<TridasSample>();
	private JButton btnAddAll;
	private JButton btnAddOne;
	private JButton btnRemoveOne;
	private JButton btnRemoveAll;
	
	/**
	 * Create the panel.
	 */
	public LGWizardSamplePicker() {
		super("Step 2 - Which samples?", 
				"The next step is to define which samples you would like labels for.");
		setLayout(new MigLayout("", "[243.00,grow][45.00,center][grow]", "[][grow][53.00,top][grow]"));
		
		JLabel lblAvailable = new JLabel("Available:");
		add(lblAvailable, "cell 0 0");
		
		JLabel lblSelected = new JLabel("Selected:");
		add(lblSelected, "cell 2 0");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 1 3,grow");
		
		lstAvailable = new JList();
		scrollPane.setViewportView(lstAvailable);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 1 2,grow");
		panel.setLayout(new MigLayout("", "[fill][]", "[][][][]"));
		
		btnAddAll = new JButton(">>");
		panel.add(btnAddAll, "cell 0 0");
		
		btnAddOne = new JButton(">");
		btnAddOne.addActionListener(this);
		panel.add(btnAddOne, "cell 0 1");
		
		btnRemoveOne = new JButton("<");
		btnRemoveOne.addActionListener(this);
		panel.add(btnRemoveOne, "cell 0 2");
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel.add(btnRemoveAll, "cell 0 3");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "cell 2 1 1 3,grow");
		
		lstSelected = new JList();
		scrollPane_1.setViewportView(lstSelected);
		
		populateBoxList();
		
		
		
			
	}
	
	private void updateGUI()
	{
		
	}

	public ArrayList<TridasSample> getSamples()
	{
		return null;
	}

	public void actionPerformed(ActionEvent evt) {

		
		if(evt.getSource() == btnAddOne){

			for (Object obj : lstAvailable.getSelectedValues())
			{
				TridasSample myobj = (TridasSample) obj;
				selModel.add(myobj);
				availModel.remove(myobj);
			}
			
			
		}
			
		if(evt.getSource() == btnRemoveOne)
		{
			for (Object obj : lstSelected.getSelectedValues())
			{
				TridasSample myobj = (TridasSample) obj;
				availModel.add(myobj);
				selModel.remove(myobj);

			}
		}
		
	}

	private void populateBoxList(){
		
	    // Grab box dictionary
	    //ArrayList<WSIBox> boxlist = (ArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(boxlist, numSorter);*/
	    
	    // Set up available list
		//availModel.addAll(boxlist);
		
		
		lstAvailable.setModel(availModel);
		lstAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lstAvailable.setCellRenderer(new TridasListCellRenderer());
		
		
		// Set up selected list
		lstSelected.setModel(selModel);
		lstSelected.setCellRenderer(new TridasListCellRenderer());
	    }

   
	




}
