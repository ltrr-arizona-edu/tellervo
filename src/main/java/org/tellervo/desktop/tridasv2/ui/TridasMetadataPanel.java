/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractMetadataPanel;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.TridasCloner;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.support.ScrollableJButtonBar;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityDeriver;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityListHolder;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.FilterableComboBoxModel;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasBark;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasHeartwood;
import org.tridas.schema.TridasLastRingUnderBark;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasPith;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasWoodCompleteness;
import org.tridas.util.TridasObjectEx;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;
import com.lowagie.text.Font;


@SuppressWarnings("serial")
public class TridasMetadataPanel extends AbstractMetadataPanel implements PropertyChangeListener {
	/** The sample we're working on */
	private Sample sample;
	/** Our property sheet panel (contains table and description) */
	private TellervoPropertySheetPanel propertiesPanel;
	/** Our properties table */
	private TellervoPropertySheetTable propertiesTable;
	/** The sidebar that has buttons for sample, series, etc */
	private JButtonBar buttonBar;
	/** The current entity selected */
	private EditType currentMode;
	
	/** A copy of the entity that we're currently editing */
	private ITridas temporaryEditingEntity;
	/** A copy of the entity that we're currently selecting */
	private ITridas temporarySelectingEntity;
	
	private TridasEntityListHolder lists;
	
	private JPanel panelTopBar;
	private JPanel panelBottomBar;

	//// TOP PANEL ITEMS
	private JLabel topLabel;
	private EntityListComboBox cboTopChooser;
	private ChoiceComboBoxActionListener topChooserListener;
	private JButton btnSelectEntity;
	private JButton btnRevert;
	private JButton btnNewEntity;
	/** true if combo box is enabled for changing, false otherwise */
	private boolean changingTop;
	private static final String CHANGE_STATE = I18n.getText("general.change");
	private static final String OK_STATE = I18n.getText("general.choose");
	
	/** The lock/unlock button for making changes to the currently selected entity */
	private JToggleButton btnEditEntity;
	/** Text associated with lock/unlock button */
	private JLabel lblEditEntityText;
	/** The save button when unlocked */
	private JButton btnEditEntitySave;
	/** The cancel button when unlocked */
	private JButton btnEditEntityCancel;

	
	private ButtonGroup entityButtons;
	
	/**
	 * Constructor: Wrap around the given sample
	 * 
	 * @param s
	 */
	public TridasMetadataPanel(Sample s) {
		this.sample = s;	

		// ensure it has a lab code
		if(!s.hasMeta(Metadata.LABCODE)) {
			LabCode labcode = new LabCode();
			
			// populate it at least with series code
			labcode.setSeriesCode(s.getSeries().getTitle());
			s.setMeta(Metadata.LABCODE, labcode);
		}
		
		// select this in button handling routine
		currentMode = null;
		temporaryEditingEntity = temporarySelectingEntity = null;
		
		lists = new TridasEntityListHolder();
		// later, when we're added to a panel, tell the list holder
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				lists.setParentWindow(SwingUtilities.windowForComponent(TridasMetadataPanel.this));
			}
		});
		
		initBars();
		initPropertiesPanel();
		// only show a basic metadata editor for things that aren't DIRECT
		initButtonPanel(s.getSampleType() != SampleType.DIRECT);
		
		setLayout(new BorderLayout());
		
		// mainPanel has top, properties, bottom
		JPanel mainPanel = new JPanel();  
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(panelTopBar, BorderLayout.NORTH);
		mainPanel.add(propertiesPanel, BorderLayout.CENTER);
		mainPanel.add(panelBottomBar, BorderLayout.SOUTH);
		
		add(mainPanel, BorderLayout.CENTER);
		add(new JScrollPane(buttonBar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.WEST);
		
		// set up initial state!
		// first, ensure we have a series
		// (this shouldn't happen?)
		if(s.getSeries() == null)
			throw new IllegalArgumentException("MetadataPanel creation for sample without a series?");
		
		// then, make sure we set up our initial button state to make sense
		for(EditType type : EditType.values()) {
			if(type.getEntity(s) == null) {
				disableBelowEnableAbove(type);
				break;
			}
		}		
		
		// get notified when a property changes
		((PropertySheetTableModel)propertiesTable.getModel()).addPropertyChangeListener(this);
	}
	
	/** Scale an icon down to 20x20 */
	private ImageIcon scaleIcon20x20(ImageIcon icon) {
		return new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	}

	/**
	 * Called to enable editing
	 * Responsible for loading the duplicate copy into the editor
	 * 
	 * @param enabled
	 */
	@SuppressWarnings("unchecked")
	protected void enableEditing(boolean enabled) {
		propertiesTable.setEditable(enabled);

		lblEditEntityText.setFont(lblEditEntityText.getFont().deriveFont(Font.BOLD));
		lblEditEntityText.setText(enabled ? I18n.getText("metadata.currentlyEditingThis") + " " + currentMode.getTitle() 
				: I18n.getText("metadata.clickLockToEdit") + " " + currentMode.getTitle());
		
		if(enabled) {
			ITridas currentEntity = currentMode.getEntity(sample);
			if(currentEntity instanceof ITridasSeries)
				temporaryEditingEntity = TridasCloner.cloneSeriesRefValues((ITridasSeries) currentEntity, (Class<? extends ITridasSeries>) currentEntity.getClass());
			else
				temporaryEditingEntity = TridasCloner.clone(currentEntity, currentEntity.getClass());

			// user chose to edit without choosing 'new', so be nice and make a new one for them
			if(temporaryEditingEntity == null && cboTopChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				temporaryEditingEntity = currentMode.newInstance(sample);
				populateNewEntity(currentMode, temporaryEditingEntity);
			}

			if(temporaryEditingEntity != null)
				propertiesPanel.readFromObject(temporaryEditingEntity);
		}
		else {
			temporaryEditingEntity = null;
						
			// don't display anything if we have nothingk!
			ITridas entity = currentMode.getEntity(sample);
			if(entity != null)
				propertiesPanel.readFromObject(entity);
		}

		// disable choosing other stuff while we're editing
		// but only if something else doesn't disable it first
		if(cboTopChooser.isEnabled())
			cboTopChooser.setEnabled(!enabled);
		btnSelectEntity.setEnabled(!enabled);
		
		// show/hide our buttons
		btnEditEntitySave.setEnabled(true);
		btnEditEntityCancel.setEnabled(true);
		btnEditEntitySave.setVisible(enabled);
		btnEditEntityCancel.setVisible(enabled);
	}
	
	private void initBars() {
		panelTopBar = new JPanel();
		panelBottomBar = new JPanel();
		panelTopBar.setLayout(new BoxLayout(panelTopBar, BoxLayout.X_AXIS));
		panelBottomBar.setLayout(new BoxLayout(panelBottomBar, BoxLayout.X_AXIS));

		////////// TOP BAR
		topLabel = new JLabel(I18n.getText("general.initializing"));
		cboTopChooser = new EntityListComboBox();
		btnSelectEntity = new JButton(CHANGE_STATE);
		btnRevert = new JButton(I18n.getText("general.revert"));
		btnNewEntity = new JButton("New");
		panelTopBar.add(topLabel);
		panelTopBar.add(Box.createHorizontalStrut(6));
		panelTopBar.add(cboTopChooser);
		panelTopBar.add(Box.createHorizontalStrut(6));
		panelTopBar.add(btnSelectEntity);
		panelTopBar.add(Box.createHorizontalStrut(6));
		panelTopBar.add(btnNewEntity);
		panelTopBar.add(Box.createHorizontalStrut(6));
		panelTopBar.add(btnRevert);
		panelTopBar.add(Box.createHorizontalGlue());
		panelTopBar.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
		
		changingTop = false;
		btnRevert.setVisible(false);
		btnNewEntity.setVisible(false);
		
		btnSelectEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(changingTop)
					chooseButtonPressed();
				else
					changeButtonPressed();
				
			}
		});
		
		btnRevert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changingTop = false;
				
				// revert back
				temporarySelectingEntity = null;
				// reselect the old thing (this calls topChooser's actionlistener, be careful!)
				selectInCombo(currentMode.getEntity(sample));
				
				btnEditEntity.setEnabled(!changingTop);
				cboTopChooser.setEnabled(changingTop);
				btnSelectEntity.setText(changingTop ? OK_STATE : CHANGE_STATE);
				btnRevert.setVisible(changingTop);
				btnNewEntity.setVisible(changingTop);
			}			
		});
		
		btnNewEntity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectInCombo(null);
				
				if(changingTop)
					chooseButtonPressed();
				else
					changeButtonPressed();
			}
			
		});

		topChooserListener = new ChoiceComboBoxActionListener(this);
		cboTopChooser.addActionListener(topChooserListener);
		
		////////// BOTTOM BAR
		btnEditEntity = new JToggleButton();
		btnEditEntity.setIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("lock.png", Builder.ICONS, 32)));
		btnEditEntity.setSelectedIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("unlock.png", Builder.ICONS, 32)));
		btnEditEntity.setBorderPainted(false);
		btnEditEntity.setContentAreaFilled(false);
		btnEditEntity.setFocusable(false);
		
		btnEditEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!btnEditEntity.isSelected() && currentMode.hasChanged()) {
					if(!warnLosingChanges()) {
						btnEditEntity.setSelected(true);
						return;
					}
					else {
						currentMode.clearChanged();
					}
				}
				enableEditing(btnEditEntity.isSelected());
			}			
		});
		
		panelBottomBar.add(btnEditEntity);
		
		lblEditEntityText = new JLabel(I18n.getText("general.initializing").toLowerCase());
		lblEditEntityText.setLabelFor(btnEditEntity);
		panelBottomBar.add(lblEditEntityText);
	
		btnEditEntitySave = new JButton(I18n.getText("general.saveChanges"));
		btnEditEntityCancel = new JButton(I18n.getText("general.cancel"));
		
		// don't let an errant enter key fire these buttons!
		btnEditEntitySave.setDefaultCapable(false);
		btnEditEntityCancel.setDefaultCapable(false);
		
		btnEditEntitySave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});

		btnEditEntityCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentMode.clearChanged();
				btnEditEntity.setSelected(false);
				enableEditing(false);				
			}
		});

		panelBottomBar.add(Box.createHorizontalGlue());
		panelBottomBar.add(btnEditEntitySave);
		panelBottomBar.add(Box.createHorizontalStrut(6));
		panelBottomBar.add(btnEditEntityCancel);
		panelBottomBar.add(Box.createHorizontalStrut(6));
	}

	/**
	 * Called by either button press to update the UI state
	 */
	private void chooseOrCancelUIUpdate() {
		// disable/enable editing
		btnEditEntity.setEnabled(!changingTop);
		cboTopChooser.setEnabled(changingTop);
		btnSelectEntity.setText(changingTop ? OK_STATE : CHANGE_STATE);
		btnRevert.setVisible(changingTop);	
		btnNewEntity.setVisible(changingTop);
	}
	
	/**
	 * Called when the 'Change' button is pressed
	 * Activates the combo box, allowing for changes
	 */
	private void changeButtonPressed() {
		
		// Make sure we populate our combobox with everything from the server
		populateComboAndSelect(true);
		
		if(cboTopChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM)
			propertiesTable.setWatermarkText(I18n.getText("general.choose").toUpperCase());
		else
			propertiesTable.setWatermarkText(I18n.getText("general.preview").toUpperCase());
		propertiesTable.setHasWatermark(true);
		
		// we're starting to change...
		changingTop = true;

		chooseOrCancelUIUpdate();
	}
	
	/**
	 * Called when the 'Choose' button is pressed
	 * Sets the current entity to what is present in the combo box
	 */
	private void chooseButtonPressed() {
		boolean flaggedAsNew = false;

		// 
		// HACK:
		// Don't allow people to select 
		//
		if(temporarySelectingEntity != null && 
				!(temporarySelectingEntity instanceof TridasObject) &&
				!(temporarySelectingEntity.equals(currentMode.getEntity(sample))) &&
				temporarySelectingEntity.getTitle().contains("(")) {
			
			JOptionPane.showMessageDialog(this, I18n.getText("error.legacyEntityProblem"), 
					I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		//
		// END HACK
		//
		 
		
		
		
		//
		// ANOTHER HACK!!
		//		
		// If it's a new object, then ask for a lab code
		/*String code = null;
		if(  (temporarySelectingEntity instanceof TridasObject) && 
			 (cboTopChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM))
		{
			// TODO replace this with an actual field in the metadata table
			code = JOptionPane.showInputDialog("Please provide a code for this object");	
			if(code!=null)
			{				
				TridasGenericField gf = new TridasGenericField();
				gf.setName(TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE);
				gf.setValue(code);
				gf.setType("xs:string");
				((TridasObject)temporarySelectingEntity).getGenericFields().add(gf);
			}
		}*/
		//
		// END HACK
		// 
		
		
		propertiesTable.setHasWatermark(false);
		propertiesTable.setWatermarkText(null);
		
		// the user is changing away...
		if(temporarySelectingEntity != null && !temporarySelectingEntity.equals(currentMode.getEntity(sample))) 
		{
			currentMode.setEntity(sample, temporarySelectingEntity);
			temporarySelectingEntity = null;
			
			// user selected 'new'
			if(cboTopChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				flaggedAsNew = true;
				disableBelowEnableAbove(currentMode);
				
				// remove any associated bits BELOW the 'new'
				for(EditType et = currentMode.next(); et != null; et = et.next())
					et.setEntity(sample, null);
			}
			// user chose an existing item
			else {
				// do we have to do anything special for the next page?
				EditType next = currentMode.next();
				if(next != null) {
					// disable buttons that we can't use
					disableBelowEnableAbove(next);
				
					// remove any associated bits from the sample
					for(EditType et = next; et != null; et = et.next())
						et.setEntity(sample, null);
				}
				
				currentMode.clearChanged();
			}
			
			// we modified the series...
			sample.setModified();
			// notify the sample that it changed
			sample.fireSampleMetadataChanged();
		}
		
		// we're done changing...
		changingTop = false;

		// update the UI...
		chooseOrCancelUIUpdate();

		// if it's new, start editing right away
		if(flaggedAsNew)
			btnEditEntity.doClick();		
	}
	
	/**
	 * Populate any parameters in a newly created entity
	 * @param type the current EditType
	 * @param entity the corresponding entity
	 */
	protected void populateNewEntity(EditType type, ITridas entity) {

		entity.setTitle("New " + type.displayTitle);
		
		// Autopopulate TridasRadius features for ease of use
		if(entity instanceof TridasRadius)
		{
			TridasWoodCompleteness wc = new TridasWoodCompleteness();
			
			TridasPith pith = new TridasPith();
			pith.setPresence(ComplexPresenceAbsence.UNKNOWN);
			wc.setPith(pith);
			
			TridasHeartwood hw = new TridasHeartwood();
			hw.setPresence(ComplexPresenceAbsence.UNKNOWN);
			wc.setHeartwood(hw);
			
			TridasSapwood sw = new TridasSapwood();
			sw.setPresence(ComplexPresenceAbsence.UNKNOWN);
			TridasLastRingUnderBark lrub = new TridasLastRingUnderBark();
			lrub.setPresence(PresenceAbsence.UNKNOWN);
			sw.setLastRingUnderBark(lrub);
			wc.setSapwood(sw);
			
			TridasBark bark = new TridasBark();
			bark.setPresence(PresenceAbsence.UNKNOWN);
			wc.setBark(bark);
			
			((TridasRadius) entity).setWoodCompleteness(wc);
		}
				
	}
	
	private void initPropertiesPanel() {
		propertiesTable = new TellervoPropertySheetTable();
		
		propertiesPanel = new TellervoPropertySheetPanel(propertiesTable);

		// keep property collapse/expand states
		propertiesPanel.setRestoreToggleStates(true);
		
		propertiesPanel.setToolBarVisible(false);
		propertiesPanel.setDescriptionVisible(true);
		
		propertiesPanel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		
		propertiesPanel.getTable().setRowHeight(24);
		propertiesPanel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		propertiesPanel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
	}
	
	/**
	 * @return true if the user wants to lose the new selection, false otherwise
	 */
	private boolean warnLosingSelection() {
		
		// nothing new has been selected, nothing to lose
		if(temporarySelectingEntity == null || temporarySelectingEntity.equals(currentMode.getEntity(sample)))
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				I18n.getText("question.confirmChangeForm"), 
				I18n.getText("question.continue"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return (ret == JOptionPane.YES_OPTION);
	}
	
	/**
	 * @return true if the user wants to lose changes, false otherwise
	 */
	private boolean warnLosingChanges() {
		if(currentMode == null || !currentMode.hasChanged())
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				I18n.getText("question.confirmChangeForm"), 
				I18n.getText("question.continue"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return (ret == JOptionPane.YES_OPTION);
	}
	
	private void doSave() {
		if(temporaryEditingEntity == null)
			throw new IllegalStateException();
		
		// if nothing actually changed, just ignore it like a cancel
		if(!currentMode.hasChanged()) {			
			btnEditEntityCancel.doClick();
			return;
		}
		
		propertiesPanel.writeToObject(temporaryEditingEntity);

		// are we saving something new?
		boolean isNew = false;
		EditType prevMode = currentMode.previous();
		ITridas parentEntity = (prevMode == null) ? null : prevMode.getEntity(sample);
		
		// logic for saving...
		if(currentMode == EditType.DERIVED_SERIES || currentMode == EditType.MEASUREMENT_SERIES) {
			// nice and easy... save the series directly into the sample, that's it!
			currentMode.setEntity(sample, temporaryEditingEntity);
		}
		else {
			// ok, we're saving an entity. save to the server!
			
			// sanity check to ensure parent entity being null means we're an object
			if(parentEntity == null && currentMode != EditType.OBJECT) {
				new Bug(new IllegalStateException("parentEntity is null, but not an object"));
				return;
			}
			
			// is it new? (no identifier?)
			isNew = !temporaryEditingEntity.isSetIdentifier();
			
			// the resource we'll use
			EntityResource<? extends ITridas> resource;
			
			if(isNew)
				resource = getNewAccessorResource(temporaryEditingEntity, parentEntity, 
						currentMode.getType());
			else
				resource = getUpdateAccessorResource(temporaryEditingEntity, currentMode.getType());

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
			
			// take the return value and save it
			currentMode.setEntity(sample, temporaryEditingEntity);
		}

		// if it was new, re-enable our editing
		if(isNew) {
			// stick it in the combo box list
			if(parentEntity == null) {
				// it's an object...
				// TODO: Fix new objects?
			}
			else
				lists.appendChildToList(parentEntity, temporaryEditingEntity);
			
			// repopulate the combo box...
			populateComboAndSelect(false);
		
			EditType next = currentMode.next();
			if(next != null) {
				// only prepare a child list if we can have children... 
				lists.prepareChildList(temporaryEditingEntity);
				
				disableBelowEnableAbove(currentMode.next());
			}
			
			// also, add it to the valid combo box...
		}
		
		// on success...
		currentMode.clearChanged();
		btnEditEntity.setSelected(false);
		enableEditing(false);
		populateComboAndSelect(false);
		temporaryEditingEntity = null;
		
		sample.setModified();
		sample.fireSampleMetadataChanged();
	}

	/**
	 * For creating a new entity on the server
	 * 
	 * @param <T>
	 * @param entity
	 * @param parent
	 * @param type
	 * @return
	 */
	private <T extends ITridas> EntityResource<T> getNewAccessorResource(ITridas entity, ITridas parent, Class<T> type) {
		return new EntityResource<T>(entity, parent, type);
	}

	/**
	 * For updating an existing entity on the server
	 * 
	 * @param <T>
	 * @param entity
	 * @param type
	 * @return
	 */
	private <T extends ITridas> EntityResource<T> getUpdateAccessorResource(ITridas entity, Class<T> type) {
		return new EntityResource<T>(entity, TellervoRequestType.UPDATE, type);
	}
	
	/**
	 * Get an entity list for the current mode
	 * @param goRemote should we try and load from the remote server?
	 * @return
	 */
	private List<? extends ITridas> getEntityList(boolean goRemote) {
		return getEntityList(currentMode, goRemote);
	}

	/**
	 * Sort a list of ITridas objects
	 * 
	 * @param list
	 */
	private void sortList(List<? extends ITridas> list) {
		// Sort list intelligently
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		
		Collections.sort(list, numSorter);		
	}
	
	/**
	 * Get an entity list for the given mode
	 * Returns a list to populate the combo box
	 * @param mode the mode
	 * @param goRemote should we try and load from the remote server?
	 * @return
	 */
	private List<? extends ITridas> getEntityList(EditType mode, boolean goRemote) {
		switch(mode) {
		case OBJECT:
			return App.tridasObjects.getObjectList();
		
		case ELEMENT:
		case SAMPLE:
		case RADIUS: {
			List<ITridas> list;
			
			try {
				list = lists.getChildList(currentMode.previous().getEntity(sample), goRemote);
			} catch (Exception e) {
				new Bug(e);
				list = null;
			}	

			// get what we already have selected
			ITridas singleton = mode.getEntity(sample);
			
			// stuff in the list? keep it
			if(list != null && !list.isEmpty()) {
				// make sure the currently selected entity is represented in the list
				if(singleton != null && entityInList(singleton, list) == null)
					list.add(singleton);
		
				// sort it nicely
				sortList(list);
				
				return list;
			}
			
			// Ok, there's nothing 
			if(singleton != null)
				return Collections.singletonList(singleton);
			else
				return Collections.emptyList();
		}
			
		default:
			return Collections.emptyList();
		}
	}
	
	/**
	 * Compare entities based on type, title, and site code (of they're TridasObjectEx)
	 * @param e1
	 * @param e2
	 * @return
	 */
	private boolean matchEntities(ITridas e1, ITridas e2) {
		// either are null? -> no match
		if(e1 == null || e2 == null)
			return false;
		
		// easy way out: identity!
		if(e1 == e2)
			return true;
		
		// they're not related classes -> not equal!
		if(!(e1.getClass().isAssignableFrom(e2.getClass()) || e2.getClass().isAssignableFrom(e1.getClass())))
			return false;
		
		// compare lab codes for TridasObjectExes
		if(e1 instanceof TridasObjectEx && e2 instanceof TridasObjectEx) {
			TridasObjectEx obj1 = (TridasObjectEx) e1;
			TridasObjectEx obj2 = (TridasObjectEx) e2;
				
			// not the same lab code -> not equal
			if(obj1.hasLabCode() && obj2.hasLabCode() && !obj1.getLabCode().equals(obj2.getLabCode()))
				return false;
		}
			
		// we found a match!
		if(e1.getTitle().equals(e2.getTitle()))
			return true;
		
		return false;
	}
	
	/**
	 * Checks to see if the entity is in the given list
	 * 
	 * @param entity
	 * @param list
	 * @return The entity in the list (may be another instance) or null
	 */
	private ITridas entityInList(ITridas entity, List<?> list) {
		for(Object o : list) {
			if(o instanceof ITridas) {
				ITridas otherEntity = (ITridas) o;
				
				if(matchEntities(entity, otherEntity))
					return otherEntity;
			}
		}
		
		return null;
	}

	/**
	 * Initialize the state when we:
	 * a) choose something new in the combo box (load children!)
	 * b) programmatically set something in the combo box (don't load children!)
	 */
	private void handleComboSelection(boolean loadChildren) {
		Object obj = cboTopChooser.getSelectedItem();
		
		//fixes bug where combobox is hidden in derived series
		if(obj == null){
			return;
		}
		
		// if it's new, create a new, empty instance
		if(obj == EntityListComboBox.NEW_ITEM) {
			temporarySelectingEntity = currentMode.newInstance(sample);
			populateNewEntity(currentMode, temporarySelectingEntity);
						
			if(propertiesTable.hasWatermark())
				propertiesTable.setWatermarkText("NEW...");
		}
		else if(obj instanceof ITridas) {
			temporarySelectingEntity = (ITridas) obj;
			
			//
			// HACK
			// Warn the user about legacy cruft
			//
			if(temporarySelectingEntity.getTitle().contains("(") &&
					!(temporarySelectingEntity instanceof TridasObject)) {
				if(propertiesTable.hasWatermark())
					propertiesTable.setWatermarkText(I18n.getText("general.legacy"));				
			}
			else {
				// this part isn't a hack...
				if(propertiesTable.hasWatermark())
					propertiesTable.setWatermarkText(I18n.getText("general.preview"));
			}				
			//
			// END HACK
			//
			
			// start loading the list of children right away
			if(loadChildren && currentMode != EditType.RADIUS)
				lists.prepareChildList(temporarySelectingEntity);			
		}
		
		propertiesPanel.readFromObject(temporarySelectingEntity);		
	}
	
	/**
	 * Select an item in the combo box
	 * Use because TridasEntity.equals compares things we don't care about
	 * 
	 * @param entity
	 */
	private void selectInCombo(ITridas entity) {
		// disable actionListener firing when we change combobox selection
		topChooserListener.setEnabled(false);
		btnNewEntity.setVisible(false);
		
		try {
			if (entity == null) {
				cboTopChooser.setSelectedItem(EntityListComboBox.NEW_ITEM);
				return;
			}

			FilterableComboBoxModel model = (FilterableComboBoxModel) cboTopChooser.getModel();

			// find it in the list...
			ITridas listEntity;
			if ((listEntity = entityInList(entity, model.getElements())) != null) {
				cboTopChooser.setSelectedItem(listEntity);
				return;
			}

			// blech, it wasn't in the list -> add it
			model.insertElementAt(entity, 2);
			cboTopChooser.setSelectedItem(entity);
		} finally {
			// deal with the selection
			handleComboSelection(false);
			// re-enable combo box actionlistener firing
			topChooserListener.setEnabled(true);
			btnNewEntity.setVisible(true);

		}
	}

	/**
	 * Populate the combo box with a list
	 * @param goRemote ensure we have remote stuff in the list
	 */
	private void populateComboAndSelect(boolean goRemote) {
		// get the list of stuff that goes in the box
		List<? extends ITridas> entityList = getEntityList(goRemote);
		
		cboTopChooser.setList(entityList);

		// select what we already have, if it exists
		ITridas selectedEntity = currentMode.getEntity(sample);
		
		// otherwise, try to choose something nicely
		if(selectedEntity == null)
			selectedEntity = suggestSelectedEntity(currentMode, entityList);
		
		selectInCombo(selectedEntity);		
	}
	
	/**
	 * Suggest a selection for the given mode
	 * 
	 * @param mode the mode
	 * @param list a list of acceptable options for the mode
	 * @return null to choose 'new', a member of the list, or really any ITridas derived thing
	 */
	protected ITridas suggestSelectedEntity(EditType mode, List<? extends ITridas> list) {
		if(list.size() == 1)
			return list.get(0);
		
		return null;
	}
	
	/**
	 * Called whenever a button on the button bar is pushed
	 * 
	 * @param type
	 */
	private void buttonAction(EditType type) {
		// ignore duplicate clicks
		if(currentMode == type)
			return;
		
		// don't show two dialogs...
		boolean alreadyWarned = false;
		
		if(currentMode != null) {
			// prompt if the user wants to save any changes they made
			if(currentMode.hasChanged()) {
				if(!warnLosingChanges()) {
					// act like the user clicked the old button again
					currentMode.getButton().doClick();
					return;
				}
				
				currentMode.clearChanged();
				
				alreadyWarned = true;
			}
			
			if(currentMode.isBrandNew(sample) 
			   && !(currentMode == EditType.MEASUREMENT_SERIES 
					|| currentMode == EditType.DERIVED_SERIES) )  {
				// if it's brand new and the user has indicated they don't
				// care about the changes, destroy it; it's useless!
				// BUT only do this if it's a mid-level entity (don't touch samples!)
				currentMode.setEntity(sample, null);
			}
		
		}
		
		// cancel out of changing (eg object) if the user decides to..
		if(changingTop) {
			if(!alreadyWarned && !warnLosingSelection()) {
				// act like the user clicked the old button again
				currentMode.getButton().doClick();
				return;				
			}
			
			btnRevert.doClick();
		}
		
		// clear off any overlay text we might have
		propertiesTable.setHasWatermark(false);
		propertiesTable.setWatermarkText(null);
		
		// load the new current entity
		currentMode = type;

		// derive a property list
        List<TridasEntityProperty> properties = TridasEntityDeriver.buildDerivationList(type.getType());
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
        // set properties and load from entity
		propertiesPanel.setProperties(propArray);

		// handle top bar
		if(currentMode == EditType.DERIVED_SERIES || currentMode == EditType.MEASUREMENT_SERIES) {
			cboTopChooser.setVisible(false);
			btnSelectEntity.setVisible(false);
			topLabel.setText(I18n.getText("metadata.for")+ " " + currentMode.getTitle() + " " + sample.getMetaString(Metadata.TITLE));
			topLabel.setLabelFor(null);
		}
		else {
			// metadata for a remote object
			cboTopChooser.setVisible(true);
			
			String prefix;
			LabCode labcode = sample.getMeta(Metadata.LABCODE, LabCode.class);
			
			switch(currentMode) {				
			case ELEMENT:
				prefix = " " + LabCodeFormatter.getElementPrefixFormatter().format(labcode) + "-";
				break;
				
			case SAMPLE:
				prefix = " " + LabCodeFormatter.getSamplePrefixFormatter().format(labcode) + "-";
				break;
				
			case RADIUS:
				prefix = " " + LabCodeFormatter.getRadiusPrefixFormatter().format(labcode) + "-";
				break;
				
			// nothing for object..
			default:
				prefix = "";
				break;
			}
			
			topLabel.setText(I18n.getText("metadata.for")+ " " + currentMode.getTitle() + prefix);
			topLabel.setLabelFor(cboTopChooser);
			
			btnSelectEntity.setVisible(true);
			btnSelectEntity.setEnabled(true);
			cboTopChooser.setEnabled(false);				
			
			populateComboAndSelect(false);
			
			// if don't have this entity, enable the list by default
			if(currentMode.getEntity(sample) == null)
				btnSelectEntity.doClick();
		}		
		
		// by default, disable editing except on series
		// note that editEntity loads the properties into the panel for us
		switch(type) {
		case MEASUREMENT_SERIES:
		case DERIVED_SERIES:
			enableEditing(true);
			btnEditEntity.setSelected(true);
			break;
			
		default:
			enableEditing(false);
			btnEditEntity.setSelected(false);
			break;
		}
	}
	
	/**
	 * Disable buttons below, enable buttons above
	 * @param type
	 */
	private void disableBelowEnableAbove(EditType type) {
		boolean enabled = true;
		
		for(EditType t : EditType.values()) {
			t.enableAssociatedButton(enabled);
			
			if(t == type)
				enabled = false;
		}
	}
	
	/**
	 * Creates a button to the button panel
	 * @param type
	 * @return
	 */
	private AbstractButton createButton(final EditType type) {
 		Action action = new AbstractAction(type.getTitle(), type.getIcon()) {
			public void actionPerformed(ActionEvent e) {				
				buttonAction(type);
			}
		};
		

		
		JToggleButton button = new JToggleButton(action);
		type.associateButton(button);
		
		return button;
	}
	
	/**
	 * Set up the button panel
	 * @param isDerived
	 */
	private void initButtonPanel(boolean isDerived) {
		buttonBar = new ScrollableJButtonBar(JButtonBar.VERTICAL);
		//buttonBar = new JButtonBar(JButtonBar.VERTICAL);
		
		buttonBar.setUI(new BlueishButtonBarUI());
		

		
		AbstractButton button;
		entityButtons = new ButtonGroup();

		// special case for derived series
		if(isDerived) {
			button = createButton(EditType.DERIVED_SERIES);
			entityButtons.add(button);
			buttonBar.add(button);
			button.doClick();
		}
		else {
			for(EditType t : EditType.values()) {
				// skip over these series types if we're not dealing with them
				if(t == EditType.DERIVED_SERIES)
					continue;
			
				button = createButton(t);
				entityButtons.add(button);
				buttonBar.add(button);

				// start on measurement series
				if (t == EditType.MEASUREMENT_SERIES)
					button.doClick();
			}
		}
		
		buttonBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
	}
	
	/**
	 * Convenient way to handle various types
	 * 
	 * @author Lucas Madar
	 */

	public static enum EditType {
		OBJECT(TridasObjectEx.class, I18n.getText("tridas.object"), "object.png", Metadata.OBJECT),
		ELEMENT(TridasElement.class, I18n.getText("tridas.element"), "element.png", Metadata.ELEMENT),
		SAMPLE(TridasSample.class, I18n.getText("tridas.sample"), "sample.png", Metadata.SAMPLE),
		RADIUS(TridasRadius.class, I18n.getText("tridas.radius"), "radius.png", Metadata.RADIUS),
		MEASUREMENT_SERIES(TridasMeasurementSeries.class, I18n.getText("tridas.measurementSeries"), "measurementseries.png", null),
		DERIVED_SERIES(TridasDerivedSeries.class, I18n.getText("tridas.derivedSeries"), "derivedseries.png", null);
		//BOX(WSIBox.class, I18n.getText("general.box"), "box.png", Metadata.BOX);
		
		
		private Class<? extends ITridas> type;
		private String displayTitle;
		private String iconPath;
		private String metadataTag;
	
		/** The JToggleButton associated with this edit state in the button pane */
		private AbstractButton associatedButton;
		/** Has this been edited? */
		private boolean hasChanged;
			
		private EditType(Class<? extends ITridas> type, String displayTitle, String iconPath, String metadataTag) {
			this.type = type;
			this.displayTitle = displayTitle;
			this.iconPath = iconPath;
			this.metadataTag = metadataTag;
			
			associatedButton = null;
		}
				
		public Class<? extends ITridas> getType() {
			return type;
		}
		
		public String getTitle() {
			return displayTitle;
		}
		
		public Icon getIcon() {
			return Builder.getIcon(iconPath, Builder.ICONS, 48);
		}
		
		/** 
		 * Get an entity from the sample
		 * 
		 * @param s
		 * @return
		 */
		public ITridas getEntity(Sample s) {
			if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES)
				return s.getSeries();
			
			return s.getMeta(metadataTag, ITridas.class);
		}
		
		/**
		 * Sets the entity in the appropriate place in this sample
		 * 
		 * @param s
		 * @param entity
		 */
		public void setEntity(Sample s, ITridas entity) {			
			// labcode, for updating
			// it must have been set in constructor, so no error check
			LabCode labcode = s.getMeta(Metadata.LABCODE, LabCode.class);
			
			if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES) {
				s.setSeries((ITridasSeries) entity);
			
				// update series code
				labcode.setSeriesCode((entity == null) ? null : entity.getTitle());			
			}
			else {
				
				// special case for objects: have to rebuild object array
				if(this == OBJECT) {
					TridasObject[] objects = null;
					
					if(entity instanceof TridasObjectEx) {
						TridasObjectEx object = (TridasObjectEx) entity;
						List<TridasObjectEx> objList = new ArrayList<TridasObjectEx>();
						
						// add all objects to list from bottom->top
						while(object != null) {
							objList.add(object);
							object = object.getParent();
						}
						
						// reverse, so list is top->bottom
						Collections.reverse(objList);
		
						// list -> array
						objects = objList.toArray(new TridasObjectEx[0]);
					}
					else if(entity != null)
						objects = new TridasObject[] { (TridasObject) entity };
					
					s.setMeta(Metadata.OBJECT_ARRAY, objects);
				}
				
				// set the metadata tag
				s.setMeta(metadataTag, entity);
				
				switch (this) {
				case OBJECT:
					// site
					labcode.clearSites();
					if (s.hasMeta(Metadata.OBJECT_ARRAY)
							&& s.getMeta(Metadata.OBJECT_ARRAY) != null) {
						for (TridasObject obj : s.getMeta(
								Metadata.OBJECT_ARRAY, TridasObject[].class)) {
							if (obj instanceof TridasObjectEx) {
								labcode.appendSiteCode(((TridasObjectEx) obj)
										.getLabCode());
								labcode.appendSiteTitle(obj.getTitle());
							} else {
								labcode.appendSiteCode(obj.getTitle());
								labcode.appendSiteTitle(obj.getTitle());
							}
						}
					}
					break;
					
				case ELEMENT:
					labcode.setElementCode((entity == null) ? null : entity.getTitle());
					break;
					
				case SAMPLE:
					labcode.setSampleCode((entity == null) ? null : entity.getTitle());
					break;
					
				case RADIUS:
					labcode.setRadiusCode((entity == null) ? null : entity.getTitle());
					break;
					
				//case BOX:
					// ignore this...
					//return;
				}
			}
			
			s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
		}
		
		/**
		 * Associate a button with this type
		 * @param button
		 */
		public void associateButton(AbstractButton button) {
			associatedButton = button;
		}

		/**
		 * Get the associated button
		 * @return
		 */
		public AbstractButton getButton() {
			return associatedButton;
		}
		
		/**
		 * Enable or disable the associated button
		 * Has no effect on series
		 * @param enabled
		 */
		public void enableAssociatedButton(boolean enabled) {
			//if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES || this == BOX)
			if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES)
				return;
			
			if(associatedButton != null) {
				associatedButton.setEnabled(enabled);
			}
		}
		
		/**
		 * Called when our underlying property changes
		 */
		public void propertyChanged() {
			hasChanged = true;
		}
		
		public void clearChanged() {
			hasChanged = false;
		}
		
		public boolean hasChanged() {
			return hasChanged;
		}
		
		/**
		 * @param s
		 * @return true if the entity for this mode has no identifier
		 */
		public boolean isBrandNew(Sample s) {
			return !(getEntity(s) != null && getEntity(s).isSetIdentifier());
		}
		
		public ITridas newInstance(Sample s) {
			try {
				return type.newInstance();
			} catch (Exception e) {
				new Bug(e);
				return null;
			}
		}
		
		/**
		 * Gets the previous EditType in the order
		 * @return
		 */
		public EditType previous() {
			switch(this) {
			case ELEMENT:
				return OBJECT;
			case SAMPLE:
				return ELEMENT;
			case RADIUS:
				return SAMPLE;
			case MEASUREMENT_SERIES:
				return RADIUS;
			default:
				return null;
			}
		}
		
		/**
		 * Gets the next EditType in the order
		 * @return
		 */
		public EditType next() {
			switch(this) {
			case OBJECT:
				return ELEMENT;
			case ELEMENT:
				return SAMPLE;
			case SAMPLE:
				return RADIUS;
			default:
				return null;
			}
		}
	}
	
	/**
	 * Silly actionListener class that we can add/remove when we want to programmatically change the combo box
	 * without triggering side effects
	 */
	private class ChoiceComboBoxActionListener implements ActionListener {
		private final TridasMetadataPanel panel;
		private boolean enabled;
		
		public ChoiceComboBoxActionListener(TridasMetadataPanel panel) {
			this.panel = panel;
			this.enabled = true;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(enabled)
				panel.handleComboSelection(true);
		}
	}

	/**
	 * Called when someone changes something on our property list
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		Object n;
		if(evt.getOldValue() == null && (n = evt.getNewValue()) != null && n.toString().equals(""))
			return;
		
		if(currentMode == null)
			throw new IllegalStateException("Property changed with null mode??");

		currentMode.propertyChanged();
	}
	

	public void showPage(EditType type)
	{
		
		for (Enumeration<AbstractButton> e = entityButtons.getElements() ; e.hasMoreElements() ;) 
		{
			JToggleButton btn = (JToggleButton) e.nextElement();
			if(btn.getText().equals(type.getTitle()))
			{
				btn.setSelected(true);
				buttonAction(type);
				return;
			}
		}


		
	}
}
