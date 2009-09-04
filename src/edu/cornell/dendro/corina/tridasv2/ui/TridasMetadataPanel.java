package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
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

import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;
import com.lowagie.text.Font;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.tridasv2.TridasCloner;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.tridasv2.ui.support.ScrollableJButtonBar;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityDeriver;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityListHolder;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ArrayListModel;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

@SuppressWarnings("serial")
public class TridasMetadataPanel extends JPanel implements PropertyChangeListener {
	/** The sample we're working on */
	private Sample s;
	/** Our property sheet panel (contains table and description) */
	private PropertySheetPanel propertiesPanel;
	/** Our properties table */
	private CorinaPropertySheetTable propertiesTable;
	/** The sidebar that has buttons for sample, series, etc */
	private JButtonBar buttonBar;
	/** The current entity selected */
	private EditType currentMode;
	
	/** A copy of the entity that we're currently editing */
	private ITridas temporaryEditingEntity;
	/** A copy of the entity that we're currently selecting */
	private ITridas temporarySelectingEntity;
	
	private TridasEntityListHolder lists;
	
	private JPanel topbar;
	private JPanel bottombar;

	//// TOP PANEL ITEMS
	private JLabel topLabel;
	private EntityListComboBox topChooser;
	private ChoiceComboBoxActionListener topChooserListener;
	private JButton changeButton;
	private JButton cancelChangeButton;
	/** true if combo box is enabled for changing, false otherwise */
	private boolean changingTop;
	private static final String CHANGE_STATE = "Change...";
	private static final String OK_STATE = "Choose";
	
	/** The lock/unlock button for making changes to the currently selected entity */
	private JToggleButton editEntity;
	/** Text associated with lock/unlock button */
	private JLabel editEntityText;
	/** The save button when unlocked */
	private JButton editEntitySave;
	/** The cancel button when unlocked */
	private JButton editEntityCancel;

	/**
	 * Constructor: Wrap around the given sample
	 * 
	 * @param s
	 */
	public TridasMetadataPanel(Sample s) {
		this.s = s;	

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
		
		mainPanel.add(topbar, BorderLayout.NORTH);
		mainPanel.add(propertiesPanel, BorderLayout.CENTER);
		mainPanel.add(bottombar, BorderLayout.SOUTH);
		
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
	protected void enableEditing(boolean enabled) {
		propertiesTable.setEditable(enabled);

		editEntityText.setFont(editEntityText.getFont().deriveFont(Font.BOLD));
		editEntityText.setText(enabled ? "Currently editing this " + currentMode.getTitle() 
				: "Click the lock to edit this " + currentMode.getTitle());
		
		if(enabled) {
			ITridas currentEntity = currentMode.getEntity(s);
			if(currentEntity instanceof ITridasSeries)
				temporaryEditingEntity = TridasCloner.cloneSeriesRefValues((ITridasSeries) currentEntity);
			else
				temporaryEditingEntity = TridasCloner.clone(currentEntity);

			// user chose to edit without choosing 'new', so be nice and make a new one for them
			if(temporaryEditingEntity == null && topChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				temporaryEditingEntity = currentMode.newInstance(s);
				populateNewEntity(currentMode, temporaryEditingEntity);
			}

			if(temporaryEditingEntity != null)
				propertiesPanel.readFromObject(temporaryEditingEntity);
		}
		else {
			temporaryEditingEntity = null;
						
			// don't display anything if we have nothingk!
			ITridas entity = currentMode.getEntity(s);
			if(entity != null)
				propertiesPanel.readFromObject(entity);
		}

		// disable choosing other stuff while we're editing
		// but only if something else doesn't disable it first
		if(topChooser.isEnabled())
			topChooser.setEnabled(!enabled);
		changeButton.setEnabled(!enabled);
		
		// show/hide our buttons
		editEntitySave.setEnabled(true);
		editEntityCancel.setEnabled(true);
		editEntitySave.setVisible(enabled);
		editEntityCancel.setVisible(enabled);
	}
	
	private void initBars() {
		topbar = new JPanel();
		bottombar = new JPanel();
		topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));
		bottombar.setLayout(new BoxLayout(bottombar, BoxLayout.X_AXIS));

		////////// TOP BAR
		topLabel = new JLabel("Initializing...");
		topChooser = new EntityListComboBox();
		changeButton = new JButton(CHANGE_STATE);
		cancelChangeButton = new JButton("Revert");
		topbar.add(topLabel);
		topbar.add(Box.createHorizontalStrut(6));
		topbar.add(topChooser);
		topbar.add(Box.createHorizontalStrut(6));
		topbar.add(changeButton);
		topbar.add(Box.createHorizontalStrut(6));
		topbar.add(cancelChangeButton);
		topbar.add(Box.createHorizontalGlue());
		topbar.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
		
		changingTop = false;
		cancelChangeButton.setVisible(false);
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(changingTop)
					chooseButtonPressed();
				else
					changeButtonPressed();
				
			}
		});
		
		cancelChangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changingTop = false;
				
				// revert back
				temporarySelectingEntity = null;
				// reselect the old thing (this calls topChooser's actionlistener, be careful!)
				selectInCombo(currentMode.getEntity(s));
				
				editEntity.setEnabled(!changingTop);
				topChooser.setEnabled(changingTop);
				changeButton.setText(changingTop ? OK_STATE : CHANGE_STATE);
				cancelChangeButton.setVisible(changingTop);
			}			
		});
		

		topChooserListener = new ChoiceComboBoxActionListener(this);
		topChooser.addActionListener(topChooserListener);
		
		////////// BOTTOM BAR
		editEntity = new JToggleButton();
		editEntity.setIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("lock.png", Builder.ICONS, 32)));
		editEntity.setSelectedIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("unlock.png", Builder.ICONS, 32)));
		editEntity.setBorderPainted(false);
		editEntity.setContentAreaFilled(false);
		editEntity.setFocusable(false);
		
		editEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!editEntity.isSelected() && currentMode.hasChanged()) {
					if(!warnLosingChanges()) {
						editEntity.setSelected(true);
						return;
					}
					else {
						currentMode.clearChanged();
					}
				}
				enableEditing(editEntity.isSelected());
			}			
		});
		
		bottombar.add(editEntity);
		
		editEntityText = new JLabel("initializing...");
		editEntityText.setLabelFor(editEntity);
		bottombar.add(editEntityText);
	
		editEntitySave = new JButton("Save changes");
		editEntityCancel = new JButton("Cancel");
		
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
				currentMode.clearChanged();
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
	 * Called by either button press to update the UI state
	 */
	private void chooseOrCancelUIUpdate() {
		// disable/enable editing
		editEntity.setEnabled(!changingTop);
		topChooser.setEnabled(changingTop);
		changeButton.setText(changingTop ? OK_STATE : CHANGE_STATE);
		cancelChangeButton.setVisible(changingTop);		
	}
	
	/**
	 * Called when the 'Change' button is pressed
	 * Activates the combo box, allowing for changes
	 */
	private void changeButtonPressed() {
		
		// Make sure we populate our combobox with everything from the server
		populateComboAndSelect(true);
		
		if(topChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM)
			propertiesTable.setPreviewText("CHOOSE");
		else
			propertiesTable.setPreviewText("PREVIEW");
		propertiesTable.setPreviewing(true);
		
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
				!(temporarySelectingEntity.equals(currentMode.getEntity(s))) &&
				temporarySelectingEntity.getTitle().contains("(")) {
			
			JOptionPane.showMessageDialog(this, "Sorry, you may not choose a legacy entity." +
					"\nIf you don't understand what this means, please speak to someone\n" +
					"before creating a new entity.", 
					"Legacy problem", JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		//
		// END HACK
		//
		
		propertiesTable.setPreviewing(false);
		propertiesTable.setPreviewText(null);
		
		// the user is changing away...
		if(temporarySelectingEntity != null && !temporarySelectingEntity.equals(currentMode.getEntity(s))) {
			currentMode.setEntity(s, temporarySelectingEntity);
			temporarySelectingEntity = null;
			
			// user selected 'new'
			if(topChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				flaggedAsNew = true;
				disableBelowEnableAbove(currentMode);
				
				// remove any associated bits BELOW the 'new'
				for(EditType et = currentMode.next(); et != null; et = et.next())
					et.setEntity(s, null);
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
						et.setEntity(s, null);
				}
			}
			
			// we modified the series...
			s.setModified();
			// notify the sample that it changed
			s.fireSampleMetadataChanged();
		}
		
		// we're done changing...
		changingTop = false;

		// update the UI...
		chooseOrCancelUIUpdate();

		// if it's new, start editing right away
		if(flaggedAsNew)
			editEntity.doClick();		
	}
	
	/**
	 * Populate any parameters in a newly created entity
	 * @param type the current EditType
	 * @param entity the corresponding entity
	 */
	protected void populateNewEntity(EditType type, ITridas entity) {
		entity.setTitle("New " + type.displayTitle);
	}
	
	private void initPropertiesPanel() {
		propertiesTable = new CorinaPropertySheetTable();
		
		propertiesPanel = new PropertySheetPanel(propertiesTable);

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
		if(temporarySelectingEntity == null || temporarySelectingEntity.equals(currentMode.getEntity(s)))
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				"Any changes to your selection will be lost.\nTo save, you must press the choose button.\n" +
				"Are you sure you wish to continue with this action?",
				"The new selection will be lost.", 
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
				"Any changes you have made will be lost.\nTo save, you must press the save button.\n" +
				"Are you sure you wish to continue with this action?",
				"Changes will be lost!", 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return (ret == JOptionPane.YES_OPTION);
	}
	
	private void doSave() {
		if(temporaryEditingEntity == null)
			throw new IllegalStateException();
		
		// if nothing actually changed, just ignore it like a cancel
		if(!currentMode.hasChanged()) {			
			editEntityCancel.doClick();
			return;
		}
		
		propertiesPanel.writeToObject(temporaryEditingEntity);

		// are we saving something new?
		boolean isNew = false;
		EditType prevMode = currentMode.previous();
		ITridas parentEntity = (prevMode == null) ? null : prevMode.getEntity(s);
		
		// logic for saving...
		if(currentMode == EditType.DERIVED_SERIES || currentMode == EditType.MEASUREMENT_SERIES) {
			// nice and easy... save the series directly into the sample, that's it!
			currentMode.setEntity(s, temporaryEditingEntity);
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
			CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);

			// query the resource
			resource.query();
			dialog.setVisible(true);
			
			// on failure, just return
			if(!dialog.isSuccessful()) {
				JOptionPane.showMessageDialog(this, "There was an error while saving your changes.\r\n" +
						"Error: " + dialog.getFailException().getLocalizedMessage(),
						"Error saving", JOptionPane.ERROR_MESSAGE);
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
			currentMode.setEntity(s, temporaryEditingEntity);
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
			
			lists.prepareChildList(temporaryEditingEntity);
			EditType next = currentMode.next();
			if(next != null)
				disableBelowEnableAbove(currentMode.next());
			
			// also, add it to the valid combo box...
		}
		
		// on success...
		currentMode.clearChanged();
		editEntity.setSelected(false);
		enableEditing(false);
		populateComboAndSelect(false);
		temporaryEditingEntity = null;
		
		s.setModified();
		s.fireSampleMetadataChanged();
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
		return new EntityResource<T>(entity, CorinaRequestType.UPDATE, type);
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
				list = lists.getChildList(currentMode.previous().getEntity(s), goRemote);
			} catch (Exception e) {
				new Bug(e);
				list = null;
			}	

			// get what we already have selected
			ITridas singleton = mode.getEntity(s);
			
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
		Object obj = topChooser.getSelectedItem();
		
		// if it's new, create a new, empty instance
		if(obj == EntityListComboBox.NEW_ITEM) {
			temporarySelectingEntity = currentMode.newInstance(s);
			populateNewEntity(currentMode, temporarySelectingEntity);
			
			if(propertiesTable.isPreviewing())
				propertiesTable.setPreviewText("NEW...");
		}
		else if(obj instanceof ITridas) {
			temporarySelectingEntity = (ITridas) obj;
			
			//
			// HACK
			// Warn the user about legacy cruft
			//
			if(temporarySelectingEntity.getTitle().contains("(") &&
					!(temporarySelectingEntity instanceof TridasObject)) {
				if(propertiesTable.isPreviewing())
					propertiesTable.setPreviewText("LEGACY");				
			}
			else {
				// this part isn't a hack...
				if(propertiesTable.isPreviewing())
					propertiesTable.setPreviewText("PREVIEW");
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
	@SuppressWarnings("unchecked")
	private void selectInCombo(ITridas entity) {
		// disable actionListener firing when we change combobox selection
		topChooserListener.setEnabled(false);
		
		try {
			if (entity == null) {
				topChooser.setSelectedItem(EntityListComboBox.NEW_ITEM);
				return;
			}

			ArrayListModel<Object> model = ((ArrayListModel<Object>) topChooser.getModel());

			// find it in the list...
			ITridas listEntity;
			if ((listEntity = entityInList(entity, model)) != null) {
				topChooser.setSelectedItem(listEntity);
				return;
			}

			// blech, it wasn't in the list -> add it
			model.add(2, entity);
			topChooser.setSelectedItem(entity);
		} finally {
			// deal with the selection
			handleComboSelection(false);
			// re-enable combo box actionlistener firing
			topChooserListener.setEnabled(true);
		}
	}

	/**
	 * Populate the combo box with a list
	 * @param goRemote ensure we have remote stuff in the list
	 */
	private void populateComboAndSelect(boolean goRemote) {
		// get the list of stuff that goes in the box
		List<? extends ITridas> entityList = getEntityList(goRemote);
		
		topChooser.setList(entityList);

		// select what we already have, if it exists
		ITridas selectedEntity = currentMode.getEntity(s);
		
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
			
			if(currentMode.isBrandNew(s)) {
				// if it's brand new and the user has indicated they don't
				// care about the changes, destroy it; it's useless!
				currentMode.setEntity(s, null);
			}
		
		}
		
		// cancel out of changing (eg object) if the user decides to..
		if(changingTop) {
			if(!alreadyWarned && !warnLosingSelection()) {
				// act like the user clicked the old button again
				currentMode.getButton().doClick();
				return;				
			}
			
			cancelChangeButton.doClick();
		}
		
		// clear off any overlay text we might have
		propertiesTable.setPreviewing(false);
		propertiesTable.setPreviewText(null);
		
		// load the new current entity
		currentMode = type;

		// derive a property list
        List<TridasEntityProperty> properties = TridasEntityDeriver.buildDerivationList(type.getType());
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
        // set properties and load from entity
		propertiesPanel.setProperties(propArray);

		// handle top bar
		if(currentMode == EditType.DERIVED_SERIES || currentMode == EditType.MEASUREMENT_SERIES) {
			topChooser.setVisible(false);
			changeButton.setVisible(false);
			topLabel.setText("Metadata for " + currentMode.getTitle() + " " + s.getMetaString(Metadata.TITLE));
			topLabel.setLabelFor(null);
		}
		else {
			// metadata for a remote object
			topChooser.setVisible(true);
			
			String prefix;
			LabCode labcode = s.getMeta(Metadata.LABCODE, LabCode.class);
			
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
			
			topLabel.setText("Metadata for " + currentMode.getTitle() + prefix);
			topLabel.setLabelFor(topChooser);
			
			changeButton.setVisible(true);
			changeButton.setEnabled(true);
			topChooser.setEnabled(false);				
			
			populateComboAndSelect(false);
			
			// if don't have this entity, enable the list by default
			if(currentMode.getEntity(s) == null)
				changeButton.doClick();
		}		
		
		// by default, disable editing except on series
		// note that editEntity loads the properties into the panel for us
		switch(type) {
		case MEASUREMENT_SERIES:
		case DERIVED_SERIES:
			enableEditing(true);
			editEntity.setSelected(true);
			break;
			
		default:
			enableEditing(false);
			editEntity.setSelected(false);
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
	private AbstractButton addButton(final EditType type) {
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
		
		buttonBar.setUI(new BlueishButtonBarUI());
		
		AbstractButton button;
		ButtonGroup buttons = new ButtonGroup();

		// special case for derived series
		if(isDerived) {
			button = addButton(EditType.DERIVED_SERIES);
			buttons.add(button);
			buttonBar.add(button);
			button.doClick();
		}
		else {
			for(EditType t : EditType.values()) {
				// skip over these series types if we're not dealing with them
				if(t == EditType.DERIVED_SERIES)
					continue;
			
				button = addButton(t);
				buttons.add(button);
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

	protected static enum EditType {
		OBJECT(TridasObject.class, "Object", "object.png", Metadata.OBJECT),
		ELEMENT(TridasElement.class, "Element", "element.png", Metadata.ELEMENT),
		SAMPLE(TridasSample.class, "Sample", "sample.png", Metadata.SAMPLE),
		RADIUS(TridasRadius.class, "Radius", "radius.png", Metadata.RADIUS),
		MEASUREMENT_SERIES(TridasMeasurementSeries.class, "Series", "measurementseries.png", null),
		DERIVED_SERIES(TridasDerivedSeries.class, "Derived Series", "derivedseries.png", null),
		BOX(WSIBox.class, "Box", "box.png", Metadata.BOX);
		
		
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
					
				case BOX:
					// ignore this...
					return;
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
			if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES || this == BOX)
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
			return !(getEntity(s).isSetIdentifier());
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
}
