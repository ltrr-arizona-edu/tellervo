package org.tellervo.desktop.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.model.MVCArrayList;

import net.miginfocom.swing.MigLayout;

public class TridasEntityPickerPanel extends JPanel implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 1L;
	private EventListenerList tridasListeners = new EventListenerList();
	private TellervoCodePanel codePanel;
	private JRadioButton radPickByCode;
	private JRadioButton radPickByHierarchy;
	private TridasEntityBrowsePanel browsePanel;
	private Window parent;
	private static final Logger log = LoggerFactory.getLogger(TridasEntityPickerPanel.class);
	private ITridas entity;
	private ArrayList<ITridas> entityList;
	public boolean returnAsList;
	private final Class<? extends ITridas> expectedClass;
	private final EntitiesAccepted entitiesAccepted;
	private JLabel lblWarning;
	private JLabel lblIcon;
	private boolean shutdownOnSelect = true;
	private MVCArrayList<WSIBox> boxList;

	public static enum EntitiesAccepted {
		SPECIFIED_ENTITY_ONLY,
		SPECIFIED_ENTITY_UP_TO_PROJECT,
		SPECIFIED_ENTITY_DOWN_TO_SERIES,
		ALL
	}
	
	public void setShutdownOnSelect(Boolean b)
	{
		this.shutdownOnSelect = b;
	}
	
	/**
	 * Create the panel.
	 */
	public TridasEntityPickerPanel(Window parent, Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype){

		this.parent = parent;
		entitiesAccepted = acceptabletype;
		expectedClass = clazz;
				

		setLayout(new MigLayout("hidemode 3", "[::128.00,right][10px,grow,fill]", "[][][][][::100px][::100px,grow]"));
		
		ButtonGroup group = new ButtonGroup();
		
		lblIcon = new JLabel("");
		lblIcon.setIcon(Builder.getIcon("barcodehd.png", 128));
		add(lblIcon, "cell 0 0 1 6,alignx right,aligny top");
		
		lblWarning = new JLabel("");
		lblWarning.setForeground(Color.RED);
		add(lblWarning, "cell 1 0");
		
		radPickByCode = new JRadioButton("Pick by lab code or barcode");
		radPickByCode.setSelected(true);
		radPickByCode.setActionCommand("pickbycode");
		radPickByCode.addActionListener(this);
		radPickByCode.setFocusable(false);
		group.add(radPickByCode);
		
		add(radPickByCode, "cell 1 1,aligny bottom");
		
		radPickByHierarchy = new JRadioButton("Pick by hierarchy");
		radPickByHierarchy.setActionCommand("pickbyhierarchy");
		radPickByHierarchy.addActionListener(this);
		radPickByHierarchy.setFocusable(false);
		group.add(radPickByHierarchy);
		add(radPickByHierarchy, "cell 1 2,aligny top");
		
		codePanel = new TellervoCodePanel();
		add(codePanel, "cell 1 4,growx");
		codePanel.addTridasSelectListener(this);
		
		// Set up box list model etc
		boxList = (MVCArrayList<WSIBox>) Dictionary.getMutableDictionary("boxDictionary");
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(boxList, numSorter);
	
		WSIBox[] arr = new WSIBox[boxList.size()];
		
		for (int i=0; i<boxList.size(); i++)
		{
			arr[i] = boxList.get(i);
		}
		TridasListCellRenderer tlcr = new TridasListCellRenderer();
		
		
		browsePanel = new TridasEntityBrowsePanel(clazz, acceptabletype);
		add(browsePanel, "cell 1 5");
		browsePanel.addTridasSelectListener(this);

		
		showHidePanels();
		
		
	}
	
	/**
	 * Hide icon and buttons in browse panel
	 * 
	 * @param b
	 */
	public void setMinimalGui(Boolean b)
	{
		setIconVisible(!b);
		browsePanel.setSelectButtonVisible(!b);
		browsePanel.setResetButtonVisible(!b);
	}
		
	
	/**
	 * Add a listener 
	 * 
	 * @param listener
	 */
	public void addTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.add(TridasSelectListener.class, listener);
	}
	
	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.remove(TridasSelectListener.class, listener);
	}
	
	/**
	 * Fire a selected entity event
	 * 
	 * @param event
	 */
	protected void fireTridasSelectListener(TridasSelectEvent event)
	{
	     Object[] listeners = tridasListeners.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==TridasSelectListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((TridasSelectListener)listeners[i+1]).entitySelected(event);
	          }            
	     }

	}
	
	/**
	 * Get the entity specified by the user
	 * 
	 * @return
	 */
	public ITridas getEntity()
	{		
		return entity;
	}
	
	public ArrayList<ITridas> getEntityList()
	{
		return this.entityList;
	}
		
	private void showHidePanels()
	{
		if(radPickByCode.isSelected())
		{
			codePanel.setVisible(true);
			browsePanel.setVisible(false);
			codePanel.setFocus();
			
		}

		else if (this.radPickByHierarchy.isSelected())
		{
			codePanel.setVisible(false);
			browsePanel.setVisible(true);
			codePanel.setFocus();
		}
		
		parent.setMinimumSize(new Dimension(600,100));
		parent.pack();
		parent.repaint();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if( (arg0.getActionCommand().equals("pickbycode"))       || 
		    (arg0.getActionCommand().equals("pickbyhierarchy"))   || 
		    (arg0.getActionCommand().equals("pickbybox"))
		  )
		{
			showHidePanels();
		}

	}
	
		


	public void forceFireEvent()
	{
		if(this.radPickByCode.isSelected())
		{
			codePanel.forceFireEvent();
		}
		else
		{
			browsePanel.fireItemSelected();
		}
	}


	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		try {
			ArrayList<ITridas> entitylist = event.getEntityList();
			if(entitylist.size()==0)
			{
				setWarning("No records match");
				return;
			}
			else if(entitylist.size()>1 && this.returnAsList==false)
			{
				setWarning("Ambiguous - multiple matches");
				return;
			}
			else
			{
				setWarning(null);
			}
			
			
			entity = event.getEntity();
			entityList = event.getEntityList();
			
			
		} catch (Exception e) {

			
			
		}
		

		
		returnEntity();
	}
	/**
	 * Check that the specified entity is valid in terms of the level
	 * of the class that is expected to be returned.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean checkEntityIsValid(ITridas ent)
	{
		if(ent==null)
		{
			return false;
		}
		else if (expectedClass.equals(ITridas.class))
		{
			return true;
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.ALL))
		{
			return true;
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_ONLY))
		{
			if(ent.getClass().equals(expectedClass) )
			{
				return true;
			}
			else if (ent.getClass().equals(TridasObjectEx.class) && expectedClass.equals(TridasObject.class))
			{
				return true;
			}
			else if (ent.getClass().equals(TridasObject.class) && expectedClass.equals(TridasObjectEx.class))
			{
				return true;
			}
			else
			{
				log.error("Invalid entity - a "+TridasTreeViewPanel.getFriendlyClassName(expectedClass)+" is expected");
				return false;
			}
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_DOWN_TO_SERIES))
		{
			Integer e1 = TridasUtils.getDepth((Class<ITridas>) ent.getClass());
			Integer e2 = TridasUtils.getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)>=0;
			
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT))
		{
			Integer e1 = TridasUtils.getDepth((Class<ITridas>) ent.getClass());
			Integer e2 = TridasUtils.getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)<=0;
		}
		return false;
	}
	
	public void setIconVisible(Boolean b)
	{
		lblIcon.setVisible(b);
	}
	
	private void returnEntity()
	{

			if(checkEntityIsValid(entity))
			{
				fireTridasSelectListener(new TridasSelectEvent(this, TridasSelectEvent.ENTITY_SELECTED, entity));
				
				if(this.shutdownOnSelect) parent.setVisible(false);
			}
			else
			{
				if(entity!=null)
				{
					@SuppressWarnings("unchecked")
					String givenClassName = TridasTreeViewPanel.getFriendlyClassName((Class<ITridas>) entity.getClass()).toLowerCase();
					String expectedClassName = TridasTreeViewPanel.getFriendlyClassName(expectedClass).toLowerCase();

					String modifier = "";			
					
					if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT))
					{
						modifier = " (or above)";
					}
					else if (entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_DOWN_TO_SERIES))
					{
						modifier = " (or below)";
					}
					setWarning("The code you entered was for a TRiDaS " + givenClassName + "\n"+
							"when a TRiDaS "+ expectedClassName + modifier + " was expected");
				}
				else
				{
					setWarning("No records match");
				}
				

			}
			
	
	}
	
	/**
	 * Set the warning label.  If you want to remove a warning set to null and the label
	 * will be hidden.
	 * 
	 * @param text
	 */
	private void setWarning(String text)
	{
		// If warning is null then hide, otherwise show
		lblWarning.setVisible(!(text==null));
		
		lblWarning.setText(text);
	}
	
	
}
