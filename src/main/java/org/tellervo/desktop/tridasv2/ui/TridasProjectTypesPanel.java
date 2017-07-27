package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.odk.SelectableChoice;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tridas.schema.ControlledVoc;

import com.dmurph.mvc.model.MVCArrayList;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.SearchableUtils;

public class TridasProjectTypesPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasProjectTypesPanel.class);
    private CheckBoxList cbxlstChoices;
    private JScrollPane choicesScrollPane;
	private Boolean hasResults;
	private ArrayList<SelectableChoice> choices = new ArrayList<SelectableChoice>();
    
    
	/**
	 * Create the dialog.
	 */
	public TridasProjectTypesPanel(ArrayList<ControlledVoc> selectedItems) {	
		
		
		
		setupGUI(selectedItems);
		
	}
	
	public CheckBoxList getCheckBoxList()
	{
		return cbxlstChoices;
	}
	
	
	private void setupGUI(ArrayList<ControlledVoc> selectedlist)
	{
		ArrayList<Object> dictionary = Dictionary.getMutableDictionary("projectTypeDictionary");
		choices = SelectableChoice.makeObjectsSelectable(dictionary, false);
		setSelectedChoices(selectedlist);
		
		cbxlstChoices = new CheckBoxList(choices.toArray(new SelectableChoice[choices.size()]));
        SearchableUtils.installSearchable(cbxlstChoices);
		cbxlstChoices.getCheckBoxListSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		cbxlstChoices.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                if (!e.getValueIsAdjusting()) {
        	
	                	setSelectedChoices(cbxlstChoices.getCheckBoxListSelectedValues());

	                }
	            }
	        });
		cbxlstChoices.setCheckBoxListSelectedIndices(getSelectedIndices());
	
		JScrollPane choicesScrollPane = new JScrollPane();
		choicesScrollPane.setViewportView(cbxlstChoices);
		setLayout(new BorderLayout(0, 0));        
		add(choicesScrollPane);   
		
		cbxlstChoices.repaint();
		choicesScrollPane.revalidate();
		
	}
	
	private int[] getSelectedIndices()
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		int i = -1;
		for(SelectableChoice c : choices)
		{
			i++;
			
			if(c.isSelected())
			{
				indices.add(i);
			}
			
		}
		return ArrayUtils.toPrimitive(indices.toArray(new Integer[indices.size()]));
	}
	
	private void setSelectedChoices(ArrayList<ControlledVoc> selected)
	{
		for(SelectableChoice c : choices)
		{
			c.setSelected(false);
		}
		
		if(selected==null ) return;


		for(ControlledVoc cv : selected)
		{	
			log.debug("Looking for "+cv.getNormal());
			for(SelectableChoice ch : choices)
			{
				try{
					ControlledVoc cv2 = (ControlledVoc) ch.getItem();
					if(cv.getNormal().equals(cv2.getNormal()) && cv.getNormalId().equals(cv2.getNormalId()))
					{
						ch.setSelected(true);
						continue;
					}
				} catch (NullPointerException e)
				{
					log.debug("Error setting selected choices");
				}
			}
		}
		
		
	}
	
	private void setSelectedChoices(Object[] selchoice)
	{
		for(SelectableChoice c : choices)
		{
			c.setSelected(false);
		}
		
		for(Object c : selchoice)
		{
			if(choices.contains(c))
			{
				choices.get(choices.indexOf(c)).setSelected(true);
			}
			else
			{
				log.error("The selected choice is not one of the possible choices");
			}
		}
	}


	public ArrayList<ControlledVoc> getList()
	{
		ArrayList<ControlledVoc> lst = new ArrayList<ControlledVoc>();
		for(SelectableChoice choice : choices)
		{
			if(choice.isSelected()) lst.add((ControlledVoc) choice.getItem());
		}
		log.debug("Returning list with "+lst.size()+ " items");
		
		return lst;

	}
			
	
	public Boolean getHasResults() {
		return hasResults;
	}

	public void setHasResults(Boolean hasResults) {
		this.hasResults = hasResults;
	}
}

	