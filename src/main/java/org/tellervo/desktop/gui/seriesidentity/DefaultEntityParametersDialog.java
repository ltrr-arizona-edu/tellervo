package org.tellervo.desktop.gui.seriesidentity;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.tellervo.desktop.components.table.ControlledVocDictionaryComboBox;
import org.tellervo.desktop.components.table.TridasObjectExRenderer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.DescriptiveDialog;
import org.tellervo.desktop.tridasv2.ui.TridasProjectRenderer;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.model.MVCArrayList;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class DefaultEntityParametersDialog extends DescriptiveDialog {

	private static final long serialVersionUID = 1L;
	public JComboBox<ControlledVoc> cboObjectType;
	public JComboBox<ControlledVoc> cboElementType;
	public JComboBox<ControlledVoc> cboTaxon;
	public JComboBox<ControlledVoc> cboSampleType;
	public JComboBox<TridasProject> cboProject;
	
	public DefaultEntityParametersDialog(Window parent) {
		super(parent, "Default Field Values", "You can set some of the most important field parameters for all newly generated entities "
				+ "using the form below.  Note these parameters will only be used for newly generated entities that are not yet in the "
				+ "database (i.e. all those marked with a red cross in the table.  All entities currently in the database will be used "
				+ "as they are.", null);
	

		initGUI();
		this.setModal(true);
	}
	
	public void setDefaultValues(ControlledVoc objectType)
	{
		cboObjectType.setSelectedItem(objectType);
	}
	
	private void initGUI()
	{
		getMainPanel().setLayout(new MigLayout("", "[right][grow]", "[][][][][][]"));
		
		JLabel lblProject = new JLabel("Project:");
		getMainPanel().add(lblProject, "cell 0 0,alignx trailing");
		
		
		MVCArrayList dic = App.tridasProjects.getMutableObjectList();
		cboProject = new JComboBox(dic.toArray());
		cboProject.setRenderer(new TridasProjectRenderer());
		getMainPanel().add(cboProject, "cell 1 0,growx");
		
		JLabel lblObjectType = new JLabel("Object type:");
		getMainPanel().add(lblObjectType, "cell 0 1,alignx trailing");
		
		cboObjectType = new ControlledVocDictionaryComboBox("objectTypeDictionary");
		getMainPanel().add(cboObjectType, "cell 1 1,growx");
		
		JLabel lblElementType = new JLabel("Element type:");
		getMainPanel().add(lblElementType, "cell 0 2,alignx trailing");
		
		cboElementType = new ControlledVocDictionaryComboBox("elementTypeDictionary");
		getMainPanel().add(cboElementType, "cell 1 2,growx");
		
		JLabel lblTaxon = new JLabel("Taxon:");
		getMainPanel().add(lblTaxon, "cell 0 3,alignx trailing");
		
		cboTaxon = new ControlledVocDictionaryComboBox("taxonDictionary");
		getMainPanel().add(cboTaxon, "cell 1 3,growx");
		
		JLabel lblSampleType = new JLabel("Sample type:");
		getMainPanel().add(lblSampleType, "cell 0 4,alignx trailing");
		
		cboSampleType =new ControlledVocDictionaryComboBox("sampleTypeDictionary");
		getMainPanel().add(cboSampleType, "cell 1 4,growx");
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("OK"))
		{
			this.setVisible(false);
		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			this.dispose();
		}
	}


}
