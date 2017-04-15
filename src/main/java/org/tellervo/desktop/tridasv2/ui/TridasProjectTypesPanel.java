package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.odk.SelectableChoice;
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
    
    
    
	/**
	 * Create the dialog.
	 */
	public TridasProjectTypesPanel(ArrayList<ControlledVoc> list) {	
		setupGUI();
		
	}
	
	public CheckBoxList getCheckBoxList()
	{
		return cbxlstChoices;
	}
	
	
	private void setupGUI()
	{
		MVCArrayList dictionary = Dictionary.getMutableDictionary("projectTypeDictionary");
		JScrollPane choicesScrollPane = new JScrollPane();

		
		ArrayList<SelectableChoice> choices = SelectableChoice.makeObjectsSelectable(dictionary) ;
		
		this.cbxlstChoices = new CheckBoxList(choices.toArray(new SelectableChoice[choices.size()]));
		choicesScrollPane.setViewportView(cbxlstChoices);
		//cbxlstChoices.setCheckBoxListSelectedIndices(((AbstractODKChoiceField)selectedField).getSelectedChoicesIndices());
		
		cbxlstChoices.getCheckBoxListSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        SearchableUtils.installSearchable(cbxlstChoices);
        cbxlstChoices.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                                 	
   
                    
                }
            }
        });
		setLayout(new BorderLayout(0, 0));
		
        
		add(choicesScrollPane);
		

        
        
		cbxlstChoices.repaint();
		choicesScrollPane.revalidate();
		
	}
	
	

	public ArrayList<ControlledVoc> getList()
	{
		return null;
	}
		
	
	public Boolean getHasResults() {
		return hasResults;
	}

	public void setHasResults(Boolean hasResults) {
		this.hasResults = hasResults;
	}
}

	