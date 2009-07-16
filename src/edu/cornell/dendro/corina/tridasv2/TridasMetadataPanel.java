package edu.cornell.dendro.corina.tridasv2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.ArrayListModel;

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
		add(buttonBar, BorderLayout.WEST);
		
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
	private void enableEditing(boolean enabled) {
		propertiesTable.setEditable(enabled);

		editEntityText.setFont(editEntityText.getFont().deriveFont(Font.BOLD));
		editEntityText.setText(enabled ? "Currently editing this " + currentMode.getTitle() 
				: "Click the lock to edit this " + currentMode.getTitle());
		
		if(enabled) {
			temporaryEditingEntity = (ITridas) TridasCloner.clone(currentMode.getEntity(s));

			// user chose to edit without choosing 'new', so be nice and make a new one for them
			if(temporaryEditingEntity == null && topChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				temporaryEditingEntity = currentMode.newInstance();
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
				boolean flaggedAsNew = false;
				
				// the user is changing away...
				if(changingTop && temporarySelectingEntity != null && !temporarySelectingEntity.equals(currentMode.getEntity(s))) {
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
				}
				
				// Make sure we populate our combobox with everything from the server
				if(!changingTop) {
					populateComboAndSelect(true);
				}
				
				changingTop = !changingTop;

				// now: false if we're done changing
				// true if we're starting to change
				
				// disable/enable editing
				editEntity.setEnabled(!changingTop);
				topChooser.setEnabled(changingTop);
				changeButton.setText(changingTop ? OK_STATE : CHANGE_STATE);
				cancelChangeButton.setVisible(changingTop);

				// if it's new, start editing right away
				if(flaggedAsNew)
					editEntity.doClick();
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
		editEntity.setIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("lock.png", "Icons")));
		editEntity.setSelectedIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("unlock.png", "Icons")));
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
		
		// logic for saving...
		if(currentMode == EditType.DERIVED_SERIES || currentMode == EditType.MEASUREMENT_SERIES) {
			// nice and easy... save the series directly into the sample, that's it!
			currentMode.setEntity(s, temporaryEditingEntity);			
		}
		else {
			// save to the server
			// TODO: Save to the server!
			
			currentMode.setEntity(s, temporaryEditingEntity);
		}
		
		// on success...
		currentMode.clearChanged();
		editEntity.setSelected(false);
		enableEditing(false);
		populateComboAndSelect(false);
		temporaryEditingEntity = null;
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
			temporarySelectingEntity = currentMode.newInstance();
		}
		else if(obj instanceof ITridas) {
			temporarySelectingEntity = (ITridas) obj;
			
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
		// otherwise, if one thing is in the list, select it only
		if(selectedEntity == null && entityList.size() == 1)
			selectedEntity = entityList.get(0);
		selectInCombo(selectedEntity);		
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
		
		if(currentMode != null) {
			// prompt if the user wants to save any changes they made
			if(currentMode.hasChanged()) {
				if(!warnLosingChanges()) {
					// act like the user clicked the old button again
					currentMode.getButton().doClick();
					return;
				}
				else
					currentMode.clearChanged();
			}
		
		}
		
		// cancel out of changing (eg object) if the user decides to..
		if(changingTop) {
			if(!warnLosingSelection()) {
				// act like the user clicked the old button again
				currentMode.getButton().doClick();
				return;				
			}
			else
				cancelChangeButton.doClick();
		}
		
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
			
			topLabel.setText("Metadata for " + currentMode.getTitle());
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
		buttonBar = new JButtonBar(JButtonBar.VERTICAL);
		
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
		
		buttonBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), 
				BorderFactory.createEmptyBorder(4, 4, 4, 4)));
	}
	
	/**
	 * Convenient way to handle various types
	 * 
	 * @author Lucas Madar
	 */

	private static enum EditType {
		OBJECT(TridasObject.class, "Object", "tridas/object.png", Metadata.OBJECT),
		ELEMENT(TridasElement.class, "Element", "tridas/element.png", Metadata.ELEMENT),
		SAMPLE(TridasSample.class, "Sample", "tridas/sample.png", Metadata.SAMPLE),
		RADIUS(TridasRadius.class, "Radius", "tridas/radius.png", Metadata.RADIUS),
		MEASUREMENT_SERIES(TridasMeasurementSeries.class, "Series", "tridas/measurementseries.png", null),
		DERIVED_SERIES(TridasDerivedSeries.class, "Derived Series", "tridas/derivedseries.png", null);
		
		
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
			return Builder.getIcon(iconPath, "Icons");
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
			
			return (ITridas) s.getMeta(metadataTag);
		}
		
		/**
		 * Sets the entity in the appropriate place in this sample
		 * 
		 * @param s
		 * @param entity
		 */
		public void setEntity(Sample s, ITridas entity) {
			if(this == DERIVED_SERIES || this == MEASUREMENT_SERIES)
				s.setSeries((ITridasSeries) entity);
			else
				s.setMeta(metadataTag, entity);
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
		
		public ITridas newInstance() {
			try {
				ITridas entity = type.newInstance();
				
				entity.setTitle("New " + displayTitle);
				
				return entity;
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
