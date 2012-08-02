/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.widgets.ManagementTreeViewPanel;
import org.tellervo.desktop.gui.widgets.TridasTreeViewPanel;
import org.tellervo.desktop.io.control.ExpandImportTreeEvent;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.desktop.tridasv2.TridasCloner;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetTable;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyEditorFactory;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyRendererFactory;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityDeriver;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils.TreeDepth;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.lowagie.text.Font;



/**
 * Dialog for browsing the metadata for all entities in the database.
 *
 * @author  peterbrewer
 */
public class MetadataBrowser extends javax.swing.JDialog implements PropertyChangeListener, TridasSelectListener {
	
	private final static Logger log = LoggerFactory.getLogger(MetadataBrowser.class);

	private static final long serialVersionUID = 8940640945613031936L;
	/** Panel containing the tree view of the entities in the database	 */
	private ManagementTreeViewPanel treepanel;
	/** Our property sheet panel (contains table and description) */
	private PropertySheetPanel propertiesPanel;
	/** Our properties table */
	private TellervoPropertySheetTable propertiesTable;
	/** Panel containing the edit/save changes/cancel buttons for the current entity */
	private JPanel bottombar;
	/** The lock/unlock button for making changes to the currently selected entity */
	private JToggleButton editEntity;
	/** Text associated with lock/unlock button */
	private JLabel editEntityText; 
	/** The save button when unlocked */
	private JButton editEntitySave;
	/** The cancel button when unlocked */
	private JButton editEntityCancel;
	/** A copy of the entity that we're currently editing */
	private ITridas temporaryEditingEntity;
	/** Whether the current entity has been changed */
	private Boolean hasChanged = false;
	/** The current entity */
	private ITridas currentEntity;
	/** Class of the current entity */
	private Class<? extends ITridas> currentEntityType;
	DefaultMutableTreeNode nodeSelected;
	private JFrame parent;
	
    public MetadataBrowser(JFrame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        setupGui();
        pack();
        
    }
    
    /**
     * Set up the GUI components
     */
    public void setupGui()
    {
    	// Set up tree panel    		
    	
    	treepanel = new ManagementTreeViewPanel(((Window) this), TreeDepth.SERIES, true, "View metadata");
    	//treepanel.setObjectList(ObjectListMode.TOP_LEVEL_ONLY);
    	treepanel.addTridasSelectListener(this);
    	leftPane.add(treepanel, BorderLayout.CENTER);
    	
    	// Set up metadata panel
		JPanel mainPanel = new JPanel();  
		mainPanel.setLayout(new BorderLayout());
		initPropertiesPanel();
		mainPanel.add(propertiesPanel, BorderLayout.CENTER);
		mainPanel.add(bottombar, BorderLayout.SOUTH);
		rightPane.add(mainPanel, BorderLayout.CENTER);  	
		
    	// Set up dialog
    	setTitle("Metadata Browser");
    	
    	setLocationRelativeTo(null);
    	setIconImage(Builder.getApplicationIcon());
    	
    	// Disable editing to start with and populate page with empty object
    	setEntity(null, TridasObject.class);
		enableEditing(false);
		
		// Set up listeners
    	this.btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				// Only close if they don't mind loosing changes (if any)
				if(warnLosingChanges())
				{
					dispose();	
				}
				
			}
		}); 	
    }
    
    /**
     * Set up the properties panel
     */
	private void initPropertiesPanel() {
		
		// Create table and panel to hold it
		propertiesTable = new TellervoPropertySheetTable();
		propertiesPanel = new PropertySheetPanel(propertiesTable);

		// Set various properties of the properties panel!
		propertiesPanel.setRestoreToggleStates(true);
		propertiesPanel.setToolBarVisible(false);
		propertiesPanel.setDescriptionVisible(true);
		propertiesPanel.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
		propertiesPanel.getTable().setRowHeight(24);
		propertiesPanel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		propertiesPanel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
		propertiesPanel.getTable().addPropertyChangeListener(this);
		
		// Set up button bar 
		setupButtonBar();
		
		
		
	}
	
	/**
	 * Set the current entity that we are browsing 
	 * 
	 * @param entity
	 * @param type
	 */
	public void setEntity(ITridas entity, Class<? extends ITridas> type)
	{
		setEntity(entity, type, false);
	}
	
	public void hideTree()
	{
		this.splitPane.setDividerLocation(0);
	}
	
	/**
	 * Set the current entity that we are browsing and whether the user should be
	 * warned or not
	 * 
	 * @param entity
	 * @param type
	 * @param silent
	 */
	private void setEntity(ITridas entity, Class<? extends ITridas> type, Boolean silent)
	{	
		if(!silent)
		{
			if(!warnLosingChanges())
			{
				return;
			}
		}

		hasChanged = false;
		
		currentEntityType = type;
		currentEntity = entity;
		
		// Swapping entities so disable editing
		this.enableEditing(false);	
		
		// derive a property list
        List<TridasEntityProperty> properties = TridasEntityDeriver.buildDerivationList(type);
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
        // set properties and load from entity
		propertiesPanel.setProperties(propArray);
		propertiesTable.expandAllBranches(true);
		
		// Add data to table from entity
		if(entity!=null)
		{
			propertiesPanel.readFromObject(entity);
			propertiesPanel.setEnabled(true);
			editEntity.setVisible(true);
		}
		else
		{
			propertiesPanel.setEnabled(false);
			editEntity.setVisible(false);
		}
		
		
	}
	
	/**
	 * Set up the button bar
	 */
	private void setupButtonBar() {
		bottombar = new JPanel();
		bottombar.setLayout(new BoxLayout(bottombar, BoxLayout.X_AXIS));

		editEntity = new JToggleButton();
		editEntity.setIcon(Builder.getIcon("lock.png", Builder.ICONS, 22));
		editEntity.setSelectedIcon(Builder.getIcon("unlock.png", Builder.ICONS, 22));
		editEntity.setBorderPainted(false);
		editEntity.setContentAreaFilled(false);
		editEntity.setFocusable(false);
		
		editEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!editEntity.isSelected() && hasChanged) {
					/*if(!warnLosingChanges()) {
						editEntity.setSelected(true);
						return;
					}
					else {
						editEntity.setSelected(false);
						hasChanged = false;
					}*/
					doSave();
				}
				enableEditing(editEntity.isSelected());
			}			
		});
		
		bottombar.add(editEntity);
		
		editEntityText = new JLabel(I18n.getText("general.initializing").toLowerCase());
		editEntityText.setLabelFor(editEntity);
		bottombar.add(editEntityText);
	
		editEntitySave = new JButton(I18n.getText("general.saveChanges"));
		editEntityCancel = new JButton(I18n.getText("general.cancel"));
		
		// don't let an errant enter key fire these buttons!
		editEntitySave.setDefaultCapable(false);
		editEntityCancel.setDefaultCapable(false);
		
		editEntitySave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});

		editEntityCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasChanged = false;
				editEntity.setSelected(false);
				enableEditing(false);				
			}
		});

		bottombar.add(Box.createHorizontalGlue());
		bottombar.add(editEntitySave);
		bottombar.add(Box.createHorizontalStrut(6));
		bottombar.add(editEntityCancel);
		bottombar.add(Box.createHorizontalStrut(6));
	}
	
	/**
	 * Save changes to the current entity
	 */
	private void doSave() {
		Class<? extends ITridas> type;
		
		if(temporaryEditingEntity == null)
			throw new IllegalStateException();
		
		// if nothing actually changed, just ignore it like a cancel
		if(!hasChanged) {			
			editEntityCancel.doClick();
			return;
		}
		
		propertiesPanel.writeToObject(temporaryEditingEntity);
		
		// the resource we'll use
		EntityResource<? extends ITridas> resource;
		
		if(temporaryEditingEntity instanceof TridasObject)
		{
			resource = new EntityResource<TridasObject>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasObject.class);
			type = TridasObject.class;
		}
		else if (temporaryEditingEntity instanceof TridasElement)
		{
			resource = new EntityResource<TridasElement>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasElement.class);
			type = TridasElement.class;
		}
		else if (temporaryEditingEntity instanceof TridasSample)
		{
			resource = new EntityResource<TridasSample>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasSample.class);
			type = TridasSample.class;
		}
		else if (temporaryEditingEntity instanceof TridasRadius)
		{
			resource = new EntityResource<TridasRadius>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasRadius.class);
			type = TridasRadius.class;
		}
		else if (temporaryEditingEntity instanceof TridasMeasurementSeries)
		{
			resource = new EntityResource<TridasMeasurementSeries>(temporaryEditingEntity, TellervoRequestType.UPDATE, TridasMeasurementSeries.class);
			type = TridasMeasurementSeries.class;
		}
		else 
		{
			log.debug("Can't save entity.  Unsupported entity class.");
			return;
		}
		
		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(this);
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, resource);

		// query the resource
		resource.query();
		dialog.setVisible(true);
		
		// on failure, just return
		if(!dialog.isSuccessful()) {
			JOptionPane.showMessageDialog(this, I18n.getText("error.savingChanges") + "\r\n" +
					I18n.getText("error") +": " + dialog.getFailException().getLocalizedMessage(),
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// replace the saved result
		temporaryEditingEntity = resource.getAssociatedResult();
		
		// sanity check the result
		if(temporaryEditingEntity == null) {
			new Bug(new IllegalStateException("CREATE or UPDATE entity returned null"));
			return;
		}
		
		setEntity(temporaryEditingEntity, type, true);
		
		// Inform the tree to update itself
		if(nodeSelected!=null)
		{
			if(nodeSelected.getParent().equals(nodeSelected.getRoot()))
			{
				this.treepanel.refreshNode(nodeSelected);
			}
			else
			{
				this.treepanel.refreshNode((DefaultMutableTreeNode)nodeSelected.getParent());
			}
		}
		

		
		hasChanged=false;
	}

	
	/**
	 * @return true if the user wants to lose changes, false otherwise
	 */
	private boolean warnLosingChanges() {
		if(!hasChanged)
			return true;
		
		if(this.currentEntity==null)
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				I18n.getText("question.confirmChangeForm"), 
				I18n.getText("question.continue"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if(ret== JOptionPane.YES_OPTION)
		{
			hasChanged=false;
		}
		
		return (ret == JOptionPane.YES_OPTION);
	}
	
	/**
	 * Called to enable editing
	 * Responsible for loading the duplicate copy into the editor
	 * 
	 * @param enable
	 */
	@SuppressWarnings("unchecked")
	protected void enableEditing(boolean enable) {
		
		propertiesTable.setEditable(enable);
		
		// show/hide our buttons
		editEntitySave.setEnabled(true);
		editEntityCancel.setEnabled(true);
		editEntitySave.setVisible(enable);
		editEntityCancel.setVisible(enable);
		
		if(currentEntity==null)
		{
			editEntityText.setText(null);
		}
		else
		{
			editEntityText.setFont(editEntityText.getFont().deriveFont(Font.BOLD));
			editEntityText.setText(enable ? I18n.getText("metadata.currentlyEditingThis") + " " + TridasTreeViewPanel.getFriendlyClassName(currentEntityType).toLowerCase()   
					: I18n.getText("metadata.clickLockToEdit")  + " " + TridasTreeViewPanel.getFriendlyClassName(currentEntityType).toLowerCase());
		}		
		editEntity.setSelected(enable);
		
		if(enable) {
			if(currentEntity==null) return;
			
			if(currentEntity instanceof ITridasSeries)
				temporaryEditingEntity = TridasCloner.cloneSeriesRefValues((ITridasSeries) currentEntity, (Class<? extends ITridasSeries>) currentEntity.getClass());
			else
				temporaryEditingEntity = TridasCloner.clone(currentEntity, currentEntity.getClass());

			if(temporaryEditingEntity != null)
				propertiesPanel.readFromObject(temporaryEditingEntity);
		}
		else {
			temporaryEditingEntity = null;

			
			// don't display anything if we have nothingk!
			if(currentEntity != null)
			{
				propertiesPanel.readFromObject(currentEntity);
			}
			else
			{
				return;
			}
		}

		

	}
	
	
	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		nodeSelected = event.getTreeNode();
		
		try {
			ITridas entity = event.getEntity();
			
			if(entity instanceof TridasObject)
			{
				this.setEntity(entity, TridasObject.class);
			}
			else if (entity instanceof TridasElement)
			{
				this.setEntity(entity, TridasElement.class);
			}
			else if (entity instanceof TridasSample)
			{
				this.setEntity(entity, TridasSample.class);
			}
			else if (entity instanceof TridasRadius)
			{
				this.setEntity(entity, TridasRadius.class);
			}
			else if (entity instanceof TridasMeasurementSeries)
			{
				this.setEntity(entity, TridasMeasurementSeries.class);
			}
			else if (entity instanceof TridasDerivedSeries)
			{
				this.setEntity(entity, TridasDerivedSeries.class);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if(!(evt.getPropertyName().equals("Frame.active")))
		{
			//System.out.println("Property changed");
		}
		else
		{
			return;
		}
		
		Object n;
		
		try{
		log.debug("oldval: "+evt.getOldValue().toString());
		log.debug("newval: "+evt.getNewValue().toString());
		} catch (NullPointerException e)
		{
			
		}
		
		if(evt.getOldValue()!=null)
		{
			if(evt.getOldValue().toString().contains("SystemColorProxy"))
			{
				return;
			}
		}
		
		if(evt.getOldValue() == null && (n = evt.getNewValue()) != null && n.toString().equals(""))
		{
			return;
		}
		
		hasChanged = true;
					
		
	}
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        leftPane = new java.awt.Panel();
        rightPane = new java.awt.Panel();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        splitPane.setDividerLocation(400);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        leftPane.setLayout(new java.awt.BorderLayout());
        splitPane.setLeftComponent(leftPane);

        rightPane.setLayout(new java.awt.BorderLayout());
        splitPane.setRightComponent(rightPane);

        btnOk.setText("OK");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                    .add(btnOk))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnOk;
    protected java.awt.Panel leftPane;
    protected java.awt.Panel rightPane;
    protected javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables


	

    
}
