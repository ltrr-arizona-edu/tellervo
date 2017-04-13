package org.tellervo.desktop.curation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetPanel;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetTable;
import org.tellervo.desktop.tridasv2.ui.TridasProjectRenderer;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyEditorFactory;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyRendererFactory;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityDeriver;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasLaboratory;
import org.tridas.schema.TridasLaboratory.Name;
import org.tridas.schema.TridasProject;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;

public class ProjectBrowserDialog extends JDialog implements PropertyChangeListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList<TridasProject> lstProjects;
	private JPanel propHolder;
	private TellervoPropertySheetPanel propertiesPanel;
	private TellervoPropertySheetTable propertiesTable;
	private JButton btnNew;
	private JButton btnRemove;
	private TridasProject temporaryEditingEntity;
	private boolean isDirty = false;
	private JSplitPane splitPane;
	
	
	public ProjectBrowserDialog(Boolean createNew)
	{
		initGUI();
		initFactories();
		
		if (createNew)
		{
			addNewProject();
			splitPane.setDividerLocation(0);
		}

	}
	
	private void initGUI()
	{
		initPropertyPanel();
		
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			splitPane = new JSplitPane();
			splitPane.setOneTouchExpandable(true);
			contentPanel.add(splitPane, "cell 0 0,grow");
			{
				
				splitPane.setRightComponent(propHolder);
			}
			{
				JPanel panelChoose = new JPanel();
				splitPane.setLeftComponent(panelChoose);
				panelChoose.setLayout(new MigLayout("", "[228.00,grow,right]", "[][309.00,grow][]"));
				{
					JLabel lblProjects = new JLabel("Projects:");
					panelChoose.add(lblProjects, "cell 0 0,alignx left");
				}
				{
					JScrollPane scrollPane = new JScrollPane();
					panelChoose.add(scrollPane, "cell 0 1,grow");
					{
						lstProjects = new JList<TridasProject>();
						
						App.tridasProjects.getMutableObjectList().addPropertyChangeListener(new PropertyChangeListener(){

							@Override
							public void propertyChange(PropertyChangeEvent arg0) {
								updateProjectList();
							}						
							
						});
						updateProjectList();

						lstProjects.setCellRenderer(new TridasProjectRenderer());
						
						 ListSelectionModel listSelectionModel = lstProjects.getSelectionModel();
						 listSelectionModel.addListSelectionListener(new ListSelectionListener(){

								@Override
								public void valueChanged(ListSelectionEvent arg0) {
									
									
									if(lstProjects.isSelectionEmpty())
									{
										setProject(null);
									}
									else
									{
										setProject((TridasProject) lstProjects.getSelectedValue());
									}
								}
                            	
                            });

						
						scrollPane.setViewportView(lstProjects);
					}
				}
				{
					JPanel panel = new JPanel();
					panelChoose.add(panel, "cell 0 2,grow");
					panel.setLayout(new MigLayout("", "[81px,grow,fill][grow,fill]", "[26px]"));
					{
						btnNew = new JButton("Add");
						panel.add(btnNew, "cell 0 0,alignx left,aligny top");
						btnNew.setIcon(Builder.getIcon("edit_add.png", 16));
						btnNew.setActionCommand("New");
						{
							btnRemove = new JButton("Delete");
							panel.add(btnRemove, "cell 1 0");
							btnRemove.setIcon(Builder.getIcon("edit_remove.png", 16));
						}
						btnNew.addActionListener(this);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Save changes");
				btnSave.setActionCommand("Save");
				btnSave.addActionListener(this);
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
			}
			{
				JButton btnClose = new JButton("Close");
				btnClose.setActionCommand("Close");
				btnClose.addActionListener(this);
				buttonPane.add(btnClose);
			}
		}
		
		setTitle("Project Browser");
		this.setIconImage(Builder.getApplicationIcon());
		setProject(null);
	}
	
	
	private void initPropertyPanel()
	{
		// Create table and panel to hold it
		propHolder = new JPanel();

		propertiesTable = new TellervoPropertySheetTable();
		propHolder.setLayout(new BorderLayout(0, 0));
		propertiesPanel = new TellervoPropertySheetPanel(propertiesTable);

		// Set various properties of the properties panel!
		propertiesPanel.setRestoreToggleStates(true);
		propertiesPanel.setToolBarVisible(false);
		propertiesPanel.setDescriptionVisible(true);
		propertiesPanel.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
		propertiesPanel.getTable().setRowHeight(24);

		propertiesPanel.getTable().addPropertyChangeListener(this);
		propHolder.add(propertiesPanel);
		
		// derive a property list
        List<TridasEntityProperty> properties = TridasEntityDeriver.buildDerivationList(TridasProject.class);
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
        // set properties and load from entity
		propertiesPanel.setProperties(propArray);
		propertiesTable.expandAllBranches(true);
		
		
	}
	
	private void initFactories()
	{
		propertiesPanel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		propertiesPanel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
	}
	
	private void addNewProject()
	{
		TridasProject p = new TridasProject();
		p.setTitle("New Project");
		
		ArrayList<ControlledVoc> types = new ArrayList<ControlledVoc>();
		ControlledVoc type = new ControlledVoc();
		type.setValue("Unsupported");
		types.add(type);
		p.setTypes(types);
		
		
		
		ArrayList<TridasLaboratory> laboratories = new ArrayList<TridasLaboratory>();
		TridasLaboratory lab = new TridasLaboratory();
		Name name = new Name();
		name.setAcronym("Unknown");
		name.setValue("Unknown");
		lab.setName(name);
		
		TridasAddress address = new TridasAddress();
		address.setAddressLine1("Unknown");
		address.setAddressLine1("Unknown");
		
		lab.setAddress(address);
		
		laboratories.add(lab);
		
		
		
		
		p.setLaboratories(laboratories);
	
		p.setCategory(type);
		p.setInvestigator("Unknown");
		p.setPeriod("Unknown");
		
		
		
		App.tridasProjects.addTridasProject(p);
		
		updateProjectList();
		
		lstProjects.setSelectedValue(p, true);
		isDirty = true;
	}
	
	
	private void setProject(TridasProject entity)
	{
		
		if(isDirty)
		{
			// warn
		}
		

		
		// Add data to table from entity
		if(entity!=null)
		{
			propertiesPanel.readFromObject(entity);
			propertiesPanel.setEnabled(true);
			//editEntity.setVisible(true);
		}
		else
		{
			propertiesPanel.setEnabled(false);
			//editEntity.setVisible(false);
		}
		
		this.temporaryEditingEntity = entity;
		isDirty = false;
		
	}
	
	private void updateProjectList()
	{
		DefaultListModel<TridasProject> projModel = new DefaultListModel<TridasProject>();

		
		for(TridasProject p : App.tridasProjects.getMutableObjectList())
		{
			projModel.addElement(p);
		}
		
		lstProjects.setModel(projModel);
		lstProjects.requestFocus();
		setProject((TridasProject) lstProjects.getSelectedValue());
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		isDirty = true;
		
	}


	private void doSave()
	{
		
		if(temporaryEditingEntity == null)
			throw new IllegalStateException();
		
		propertiesPanel.writeToObject(temporaryEditingEntity);
		
		


		if (temporaryEditingEntity==null) 
		{
			isDirty = false;
			return;
		}
		
		if(!isDirty)
		{
			// Nothing to do
			return;
		}
		
		// is it new? (no identifier?)
		boolean isNew = !temporaryEditingEntity.isSetIdentifier();
			
		
		EntityResource<TridasProject> resource;
		
		if(isNew)
			resource =  new EntityResource<TridasProject>(temporaryEditingEntity,TellervoRequestType.CREATE, TridasProject.class);
		else
			resource = new EntityResource<TridasProject>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasProject.class);

		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(this);
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, resource);

		// query the resource
		resource.query();
		dialog.setVisible(true);
		
		// on failure, just return
		if(!dialog.isSuccessful()) {
			JOptionPane.showMessageDialog(this, I18n.getText("error.savingChanges") + System.lineSeparator() +
					dialog.getFailException().getLocalizedMessage(),
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// replace the saved result
		temporaryEditingEntity = resource.getAssociatedResult();
		
		// sanity check the result
		if(temporaryEditingEntity == null) {
			new BugDialog(new IllegalStateException("CREATE or UPDATE entity returned null"));
			return;
		}
		
		// take the return value and save it
		App.tridasProjects.updateTridasProject(temporaryEditingEntity);
		isDirty = false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Save"))
		{
			if(isDirty)
			{
				
				doSave();
			}
			else
			{
				// Nothing changed
				return;
			}
		}
		else if (e.getActionCommand().equals("Close"))
		{
			if(isDirty)
			{
				// warn
			}
			
			this.dispose();
		}
		else if (e.getActionCommand().equals("New"))
		{
			this.addNewProject();
		}
	}

}
