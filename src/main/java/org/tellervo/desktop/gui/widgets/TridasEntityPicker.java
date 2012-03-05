package org.tellervo.desktop.gui.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;
import javax.swing.JButton;

public class TridasEntityPicker extends JPanel implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 1L;
	private TellervoCodePanel codePanel;
	private JRadioButton radPickByCode;
	private JRadioButton radPickByHierarchy;
	private TellervoBrowsePickerPanel browsePanel;
	private Window parent;
	private static final Logger log = LoggerFactory.getLogger(TridasEntityPicker.class);
	private ITridas entity;
	private final Class<? extends ITridas> expectedClass;
	private final EntitiesAccepted entitiesAccepted;
	private JLabel lblWarning;
	private JLabel lblIcon;
	
	public static enum EntitiesAccepted {
		SPECIFIED_ENTITY_ONLY,
		SPECIFIED_ENTITY_UP_TO_PROJECT,
		SPECIFIED_ENTITY_DOWN_TO_SERIES,
		ALL
	}
	
	/**
	 * Create the panel.
	 */
	public TridasEntityPicker(Window parent, Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype){

		this.parent = parent;
		entitiesAccepted = acceptabletype;
		expectedClass = clazz;
				

		setLayout(new MigLayout("hidemode 3", "[128px:128px:128px,center][10px,grow,fill]", "[][][][::100px][::100px,grow]"));
		
		ButtonGroup group = new ButtonGroup();
		
		lblIcon = new JLabel("");
		lblIcon.setIcon(Builder.getIcon("barcodehd.png", 128));
		add(lblIcon, "cell 0 0 1 5,alignx center,aligny top");
		
		lblWarning = new JLabel("");
		lblWarning.setForeground(Color.RED);
		add(lblWarning, "cell 1 0");
		
		radPickByCode = new JRadioButton("Pick by lab code or barcode");
		radPickByCode.setSelected(true);
		radPickByCode.setActionCommand("pickbycode");
		radPickByCode.addActionListener(this);
		group.add(radPickByCode);
		
		add(radPickByCode, "cell 1 1,aligny bottom");
		
		radPickByHierarchy = new JRadioButton("Pick by hierarchy");
		radPickByHierarchy.setActionCommand("pickbyhierarchy");
		radPickByHierarchy.addActionListener(this);
		group.add(radPickByHierarchy);
		add(radPickByHierarchy, "cell 1 2,aligny top");
		
		codePanel = new TellervoCodePanel();
		add(codePanel, "cell 1 3,growx");
		codePanel.addTridasSelectListener(this);
		
		browsePanel = new TellervoBrowsePickerPanel(clazz, acceptabletype);
		add(browsePanel, "cell 1 4");
		browsePanel.addTridasSelectListener(this);

		
		showHidePanels();
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
		
	private void showHidePanels()
	{
		if(radPickByCode.isSelected())
		{
			codePanel.setVisible(true);
			browsePanel.setVisible(false);
			
		}
		else
		{
			codePanel.setVisible(false);
			browsePanel.setVisible(true);	
		}
		
		parent.setMinimumSize(new Dimension(600,100));
		parent.pack();
		parent.repaint();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if( (arg0.getActionCommand().equals("pickbycode"))       || 
		    (arg0.getActionCommand().equals("pickbyhierarchy"))
		  )
		{
			showHidePanels();
		}

	}
	
		
	/**
	 * Show the TridasEntityPicker dialog.  Dialog returns a Tridas entity
	 * of a type specified by the clazz and acceptableType parameters
	 * 
	 * @param parent
	 * @param title
	 * @param clazz
	 * @param acceptabletype
	 * @return
	 */
	public static ITridas showDialog(Window parent, String title,
			Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype)
	{
		
		JDialog dialog = new JDialog();
		dialog.setTitle("Entity Picker");
		TridasEntityPicker panel = new TridasEntityPicker(dialog, clazz, acceptabletype);
		dialog.getContentPane().add(panel);
		dialog.setIconImage(Builder.getApplicationIcon());

        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        
        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	

        return panel.getEntity();

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
			else if(entitylist.size()>1)
			{
				setWarning("Ambiguous - multiple matches");
				return;
			}
			
			
			entity = event.getEntity();
			
			
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
	
	
	private void returnEntity()
	{

			if(checkEntityIsValid(entity))
			{
				parent.dispose();
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
