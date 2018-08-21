package org.tellervo.desktop.labelgen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.schema.WSIBox;
import org.tridas.schema.TridasSample;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.Color;


public class LGWizardBoxPicker extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JList lstSelected;
	private JList lstAvailable;
	protected ArrayListModel<WSIBox> selModel = new ArrayListModel<WSIBox>();
	protected ArrayListModel<WSIBox> availModel = new ArrayListModel<WSIBox>();
	private JButton btnAddAll;
	private JButton btnAddOne;
	private JButton btnRemoveOne;
	private JButton btnRemoveAll;
	
	/**
	 * Create the panel.
	 */
	public LGWizardBoxPicker() {
		super("Step 2 - Which boxes?", 
				"The next step is to define which boxes you would like labels for.");
		setLayout(new MigLayout("", "[grow][45.00,center][grow]", "[][grow][53.00,top][grow]"));
		
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
		btnAddAll.addActionListener(this);
		panel.add(btnAddAll, "cell 0 0");
		
		btnAddOne = new JButton(">");
		btnAddOne.addActionListener(this);
		panel.add(btnAddOne, "cell 0 1");
		
		btnRemoveOne = new JButton("<");
		btnRemoveOne.addActionListener(this);
		panel.add(btnRemoveOne, "cell 0 2");
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.addActionListener(this);
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

	public ArrayList<WSIBox> getBoxes()
	{
		ArrayList<WSIBox> items = new ArrayList<WSIBox>();
		
		for(int i=0; i<selModel.size(); i++)
		{
			items.add(selModel.get(i));
		}
		
		return items;
	}

	public void actionPerformed(ActionEvent evt) {

		
		if(evt.getSource() == btnAddOne){

			for (Object obj : lstAvailable.getSelectedValues())
			{
				WSIBox myobj = (WSIBox) obj;
				selModel.add(myobj);
				availModel.remove(myobj);
				sortAvailableBoxList();
			}
			
			
		}
			
		if(evt.getSource() == btnRemoveOne)
		{
			for (Object obj : lstSelected.getSelectedValues())
			{
				WSIBox myobj = (WSIBox) obj;
				availModel.add(myobj);
				selModel.remove(myobj);
				sortAvailableBoxList();

			}
		}
		
		if(evt.getSource() == btnAddAll)
		{
			ArrayList<WSIBox> listadd = new ArrayList<WSIBox>();
			
			for(int i=0; i<availModel.getSize(); i++)
			{
				listadd.add(availModel.get(i));
			}
					
			selModel.addAll(listadd);
			availModel.removeAll(listadd);
			lstAvailable.repaint();
			lstSelected.repaint();
		}
		
		if(evt.getSource() == btnRemoveAll)
		{
			ArrayList<WSIBox> listremove = new ArrayList<WSIBox>();
			
			for(int i=0; i<selModel.getSize(); i++)
			{
				listremove.add(selModel.get(i));
			}
					
			availModel.addAll(listremove);
			selModel.removeAll(listremove);
			lstAvailable.repaint();
			lstSelected.repaint();
		}
		
	}

	private void populateBoxList(){
		
	    // Grab box dictionary
	    ArrayList<WSIBox> boxlist = (ArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(boxlist, numSorter);
	    
	    // Set up available list
		availModel.addAll(boxlist);
		
		
		lstAvailable.setModel(availModel);
		lstAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lstAvailable.setCellRenderer(new TridasListCellRenderer());
		
		
		// Set up selected list
		lstSelected.setModel(selModel);
		lstSelected.setCellRenderer(new TridasListCellRenderer());
	    }

    private void sortAvailableBoxList(){
		// Sort list intelligently
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.SITE_CODES_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(availModel, numSorter);
		

    }
	
    @Override
	public boolean doPageValidation(){
		
    	if(this.selModel.getSize()>0)
    	{
    		return true;
    	}
    	
    	

		Alert.error("Error", "You must select one or more boxes to continue.");
		return false;
		
	}



}
