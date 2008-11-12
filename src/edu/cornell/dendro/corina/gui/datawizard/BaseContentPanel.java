	/**
 * 
 */
package edu.cornell.dendro.corina.gui.datawizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.UIManager;

import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.site.GenericIntermediateObject;
import edu.cornell.dendro.corina.site.Radius;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.Specimen;
import edu.cornell.dendro.corina.site.Subsite;
import edu.cornell.dendro.corina.site.Tree;

/**
 * @author lucasm
 *
 * Create a BoxLayout of FlowLayouts...
 *
 */
public class BaseContentPanel<OBJT extends GenericIntermediateObject> extends BasePanel implements WizardChildMonitor {
	/**
	 * 
	 */
	public BaseContentPanel(WizardPanelParent parent, 
			Class<? extends GenericIntermediateObject> contentClass) {
		
		this.wizardParent = parent;
		this.myPanel = (BaseEditorPanel<OBJT>) EditorPanelFactory.createPanelForClass(contentClass);
		this.contentClass = contentClass;

		initComponents();
		
		// default to this; something may change it later
		ourFormValidated = false;
		
		// slightly kludgy, but ok -- check our initial child form's state
		// AFTER we tell it to tell us when it changes!
		myPanel.setWizardToNotify(this);
		notifyChildFormStateChanged();
		
		// make something to populate our combo box
		this.comboPopulator = new ComboBoxPopulator(cboExistingList);
		
		// and please, don't grow larger than our parent in width!
		setPreferredSize(new Dimension(parent.getContainerPreferredSize().width, getPreferredSize().height));		
	}
	
	protected BaseContentPanel(WizardPanelParent parent) {
		// and please, don't grow larger than our parent in width!
		setPreferredSize(new Dimension(parent.getContainerPreferredSize().width, getPreferredSize().height));		
	}
	
	/**
	 * Set a component's maximum height to its preferred height
	 * @param comp
	 */
	private void setMaxHeight(Component comp) {
		Dimension d = comp.getPreferredSize();
		comp.setMaximumSize(new Dimension(Short.MAX_VALUE, d.height));
	}

    private void initComponents() {
		btnExisting = new JRadioButton("Existing: ", true);
		btnNew = new JRadioButton("New:", false);
		cboExistingList = new JComboBox(new DefaultComboBoxModel(
				new String[] { "Hello Dave!" }));
		
		// tie the buttons together
		ButtonGroup radios = new ButtonGroup();
		radios.add(btnExisting);
		radios.add(btnNew);
		
		// create a panel with the existing button and a combo box
		JPanel existingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		existingPanel.add(btnExisting);
		existingPanel.add(cboExistingList);
		
		// create a panel with the 'new' button
		JPanel newPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		newPanel.add(btnNew);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		buttonsPanel.add(existingPanel);
		buttonsPanel.add(newPanel);
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); //indent buttons a little
		
		// make sure the panels can't expand more
		setMaxHeight(buttonsPanel);
		setMaxHeight(myPanel);

		// now, create a simple little layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(buttonsPanel);
		add(myPanel);
		add(Box.createVerticalGlue());
		
		// now, make the radio buttons do what they should
		btnExisting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnExisting.isSelected()) {
					setChildrenEnabled(myPanel, false);
					cboExistingList.setEnabled(true);

					checkEverythingValid();
				}
			}			
		});
		
		// now, make the radio buttons do what they should
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnNew.isSelected()) {
					setChildrenEnabled(myPanel, true);
					cboExistingList.setEnabled(false);

					checkEverythingValid();
				}
			}			
		});
		
		cboExistingList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// we only care about state changes!
				if(e.getStateChange() != ItemEvent.SELECTED)
					return;
				
				checkEverythingValid();
			}
		});
		
		// default to existing box
		setChildrenEnabled(myPanel, false);
    }
    
    /**
     * Called right before we're about to be made visible, 
     * so we can perhaps do some last minute updates?
     */
    public void preVisibleNotify() {
		// finally, if we have any defaults, apply them to our editor panel
		Object o;
		if((o = wizardParent.getDefaultsForClass(contentClass)) != null) {
			setDefaultSelectionFrom((OBJT) o); // apply them to our combobox, perhaps?
			myPanel.setDefaultsFrom((OBJT) o); // and fill in our editor pane
		}
    }

    /**
     * Called when someone presses the 'next' button to choose the next panel
     * @return
     */
    public boolean verifyAndSelectNextPanel() {
    	if(!panelValid) {
    		new Bug(new IllegalArgumentException("Panel not valid, but next called!"));
    		return false;
    	}
    	
    	if(btnExisting.isSelected())
    		return true;
    	
    	if(btnNew.isSelected()) {
    		myPanel.commit();
    		if(!myPanel.didSucceed())
    			return false;
    		    		
    		// ok, our panel succeeded. Neat.
    		// add the item to the combo box and select it
    		OBJT newObject = myPanel.getNewObject();
    		cboExistingList.addItem(newObject);
    		cboExistingList.setSelectedItem(newObject);
    		
    		// choose an existing item
    		btnExisting.setSelected(true);
    		btnNew.setSelected(false);

    		// make sure our boxes are in a sane state
			setChildrenEnabled(myPanel, false);
			cboExistingList.setEnabled(true);
    		
    		return true;
    	}
    	
		new Bug(new IllegalArgumentException("verifyAndSelectNextPanel() failed spectacularly"));
		return false;    	
    }
    
    /**
     * Get the selected object in this panel
     * DON'T CALL BEFORE verifyAndSelectNextPanel()!!!
     * @return
     */
    public OBJT getPanelObject() {
    	Object selection = cboExistingList.getSelectedItem();
    	
    	if(selection == null)
    		throw new IllegalStateException("GetPanelObject() has no object?");
    	
    	if(!selection.getClass().equals(contentClass)) {
    		throw new IllegalStateException("GetPanelObject() not content class, instead it's " + selection.getClass().toString());
    	}
    	
    	return (OBJT) selection;
    }
    
    /**
     * is our form in a completely valid state?
     */
    private void checkEverythingValid() {
    	if(btnExisting.isSelected()) {
    		checkComboValid();
    		
    		if(ourFormValidated)
    			setPanelValid(true);
    		else
    			setPanelValid(false);
    	}
    	else if(btnNew.isSelected()) {    		
    		if(childFormValidated)
    			setPanelValid(true);
    		else
    			setPanelValid(false);
    	}
    }
    
    /**
     * check to see if our combobox is valid or not
     */
    private void checkComboValid() {
		// do we have a valid selection in the combo box?
		Object obj = cboExistingList.getSelectedItem();
		if(obj != null && obj.getClass().equals(contentClass))
			ourFormValidated = true;
		else
			ourFormValidated = false;
    }
    
    // if we have a validity change, make note of it and notify our parent!
    private void setPanelValid(boolean isValid) {
    	if(panelValid != isValid) {
    		panelValid = isValid;
    		
    		wizardParent.notifyPanelStateChanged(this);
    	}
    }
    
    /**
     * Is the panel valid (e.g., can we move to the next panel?)
     * @return
     */
    public boolean isPanelValid() {
    	return panelValid;
    }

    /**
     * Associate a parent object with this (needed if we're going to create something)
     * Also serves to notify us of a change in the parental selection
     * @param obj
     */
    public void setParentObject(GenericIntermediateObject obj) {
    	if(obj != parentObject) {
    		parentObject = obj;
    		
    		// on a new object, repopulate!
    		repopulate();
    		
    		// also, populate any sort of forms my panel has
    		myPanel.setParentObject(obj, contentClass);
    		myPanel.populate();
    		
    		// special case (I hate these)
    		// on subsites, choose Main as the default
    		if(obj instanceof Site) {
    			int len = cboExistingList.getModel().getSize();
    			
    			for(int i = 0; i < len; i++) {
    				// wow. that's a mouthful.
    				if(cboExistingList.getModel().getElementAt(i).toString().equalsIgnoreCase("main")) {
    					cboExistingList.setSelectedIndex(i);
    					
    					// this should enable our dialog to choose next...
    					checkEverythingValid();
    					break;
    				}
    			}
    		}
    		// otherwise, if we've only got one thing in the list
    		// select it (2 things, because default 'choose one' selector is there)
    		else if(cboExistingList.getModel().getSize() == 2) {
    			cboExistingList.setSelectedIndex(1);
				checkEverythingValid();
    		}
    	}
    }
    
    /**
     * Tries to set our combo box's default selection
     * @param o
     */
    private void setDefaultSelectionFrom(Object o) {
    	// don't even bother for any other weird type
    	if(!(o instanceof GenericIntermediateObject))
    		return;
    	
    	// don't bother if it's invalid!
    	if(o.toString().equals(GenericIntermediateObject.NAME_INVALID))
    		return;
    	
    	// go through our list and do case insensitive string compares
    	// if we match, match!
    	ListModel l = cboExistingList.getModel();
    	for(int i = 0; i < l.getSize(); i++) {
    		if(o.toString().equalsIgnoreCase(l.getElementAt(i).toString())) {
    			cboExistingList.setSelectedIndex(i);
    			checkEverythingValid();
    			return;
    		}
    	}
    	
    	// otherwise, it's probably better if we choose the editor
    	btnNew.doClick();
    }
    
    /**
     * Pass our child panel its prefix
     * @param parentPrefix
     */
    public void setParentPrefix(String parentPrefix) {
    	myPanel.populate(parentPrefix);
    }
    
    public String getPrefix() {
    	Object obj = cboExistingList.getSelectedItem();
    	
    	if(obj instanceof Site)
    		return ((Site)obj).getCode();
    	
    	return obj.toString();
    }
    
    /**
     * Repopulate our combo box!
     */
    public void repopulate() {
    	comboPopulator.populate(parentObject);
    }
        
    private Map<JTextField, Color> colorMap = new HashMap<JTextField, Color>();
    
    /**
     * Go through and enable/disable all children in component comp
     * Save their background colors if disabled; restore if enabled
     * @param comp
     * @param enabled
     */
    private void setChildrenEnabled(Component comp, boolean enabled) {
		if (comp instanceof Container) {
			Container container = (Container) comp;
			for (int idx = 0; idx < container.getComponentCount(); idx++) {
				setChildrenEnabled(container.getComponent(idx), enabled);
			}
		}
		
		// store background colors for jtextfields
		if(comp instanceof JTextField) {
			JTextField f = (JTextField) comp;
			
			if(enabled) {
				if(colorMap.containsKey(f)) {
					f.setBackground(colorMap.get(f));
					colorMap.remove(f);
				}
			}
			else if(!colorMap.containsKey(f)){ // only put it there if it's not there
				colorMap.put(f, f.getBackground());
				f.setBackground(UIManager.getColor("TextField.inactiveBackground"));
			}
		}

		comp.setEnabled(enabled);
	}
    
    /**
     * Get the class that this contentpanel encapsulates
     * @return
     */
    public Class<?> getContentClass() {
    	return contentClass;
    }

    /**
     * called when our child panel's validity state changes
     */
	public void notifyChildFormStateChanged() {
		childFormValidated = myPanel.isFormValidated();
		
		checkEverythingValid();
	}
    
	private JRadioButton btnExisting;
	private JRadioButton btnNew;
	private JComboBox cboExistingList;
	private boolean childFormValidated;
	private boolean ourFormValidated;
	
	private WizardPanelParent wizardParent;
	private BaseEditorPanel<OBJT> myPanel;
	private ComboBoxPopulator comboPopulator;
	private GenericIntermediateObject parentObject;

	private boolean panelValid = false;

	protected Class<?> contentClass; // so we can override!
}
