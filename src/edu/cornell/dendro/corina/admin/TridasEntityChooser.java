package edu.cornell.dendro.corina.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.miginfocom.swing.MigLayout;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.gui.dbbrowse.CorinaCodePanel;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectListener;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasTreeViewPanel;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasTreeViewPanel.TreeDepth;
import edu.cornell.dendro.corina.ui.Builder;

public class TridasEntityChooser extends JDialog implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 3079094155424090769L;
	private CorinaCodePanel codePanel;
	private Class<? extends ITridas> expectedClass;
	private EntitiesAccepted entitiesAccepted = EntitiesAccepted.ALL;
	private JButton okButton;
	private JButton cancelButton;
	private ITridas entity;
	private JTextPane lblWarning;
	private JFrame parent;
	

	public static enum EntitiesAccepted {
		SPECIFIED_ENTITY_ONLY,
		SPECIFIED_ENTITY_AND_ABOVE,
		SPECIFIED_ENTITY_AND_BELOW,
		ALL
	}
	
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public TridasEntityChooser(JFrame parent){
		super(parent, true);
		this.parent = parent;
		setupGui();

	}
	
	public TridasEntityChooser(JFrame parent, Class<? extends ITridas> clazz, EntitiesAccepted ea){
		super(parent, true);
		this.parent = parent;

		entitiesAccepted = ea;
		expectedClass = clazz;
		setupGui();
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
	
	
	/**
	 * Set up the GUI components
	 */
	private void setupGui(){

		setBounds(100, 100, 450, 300);
		this.getRootPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getRootPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			this.getRootPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new MigLayout("", "[][][grow]", "[][][][][][27.00][][][]"));
			{
				JPanel panelIcon = new JPanel();
				panelIcon.setBorder(null);
				panel.add(panelIcon, "cell 0 0 1 8,alignx center,aligny top");
				{
					JLabel lblIcon = new JLabel("");
					lblIcon.setIcon(Builder.getIcon("barcode.png", 128));
					panelIcon.add(lblIcon);
				}
			}
			{
				JLabel lblScanBarcodeOr = new JLabel("Scan barcode or type lab code:");
				panel.add(lblScanBarcodeOr, "cell 2 2");
			}
			{
				lblWarning = new JTextPane();
				lblWarning.setEditable(false);
				lblWarning.setForeground(Color.RED);
				lblWarning.setBackground(null);
				lblWarning.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
				panel.add(lblWarning, "cell 2 3");
			}
			{
				codePanel = new CorinaCodePanel(this);
				panel.add(codePanel, "cell 2 5,growx,aligny top");
				
				codePanel.addTridasSelectListener(this);
				
			}
		}
		
		this.setLocationRelativeTo(parent);
		this.pack();
		codePanel.setFocus();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel"))
		{
			entity = null;
			dispose();
		}
		else if (e.getActionCommand().equals("OK"))
		{
			codePanel.processLabCode();
		}
		
	}
	
	
	/**
	 * Show the TridasEntityChooser dialog. Returns a Tridas entity
	 * specified by the user, an this can be of any type.
	 *  
	 * @param parent
	 * @param title
	 * @return
	 */
	public static ITridas showDialog(JFrame parent, String title)
	{
		
        TridasEntityChooser dialog = new TridasEntityChooser(parent);

        dialog.setTitle(title);
        dialog.setVisible(true); // blocks until user brings dialog down...

        return dialog.getEntity();

	}
	
	/**
	 * Show the TridasEntityChooser dialog.  Dialog returns a Tridas entity
	 * of a type specified by the clazz and acceptableType parameters
	 * 
	 * @param parent
	 * @param title
	 * @param clazz
	 * @param acceptabletype
	 * @return
	 */
	public static ITridas showDialog(JFrame parent, String title,
			Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype)
	{
		
        TridasEntityChooser dialog = new TridasEntityChooser(parent, clazz, acceptabletype);
        dialog.setTitle(title);

        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	dialog.setLocationRelativeTo(parent);

        return dialog.getEntity();

	}

	@SuppressWarnings("unchecked")
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
			
			if(checkEntityIsValid())
			{
				dispose();
			}
			else
			{
				if(entity!=null)
				{
					String givenClassName = TridasTreeViewPanel.getFriendlyClassName((Class<ITridas>) entity.getClass()).toLowerCase();
					String expectedClassName = TridasTreeViewPanel.getFriendlyClassName(expectedClass).toLowerCase();

					String modifier = "";			
					
					if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_ABOVE))
					{
						modifier = " (or above)";
					}
					else if (entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_BELOW))
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setWarning(String text)
	{
		lblWarning.setText(text);
		pack();
	}
	
	
	/**
	 * Check that the specified entity is valid in terms of the level
	 * of the class that is expected to be returned.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean checkEntityIsValid()
	{
		if(entity==null)
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
			if(entity.getClass().equals(expectedClass) )
			{
				return true;
			}
			else if (entity.getClass().equals(TridasObjectEx.class) && expectedClass.equals(TridasObject.class))
			{
				return true;
			}
			else if (entity.getClass().equals(TridasObject.class) && expectedClass.equals(TridasObjectEx.class))
			{
				return true;
			}
			else
			{
				setWarning("Invalid entity - a "+TridasTreeViewPanel.getFriendlyClassName(expectedClass)+" is expected");
				return false;
			}
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_BELOW))
		{
			Integer e1 = getDepth((Class<ITridas>) entity.getClass());
			Integer e2 = getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)>=0;
			
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_ABOVE))
		{
			Integer e1 = getDepth((Class<ITridas>) entity.getClass());
			Integer e2 = getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)<=0;
		}
		return false;
	}
	
	
	/** 
	 * Get the level of the Tridas class as an integer where
	 * 1=object through to 5=series.  If a class is given that is 
	 * not in the Tridas hierarchy then 0 is returned.
	 * 
	 * @param e1
	 * @return
	 */
	public int getDepth(Class<? extends ITridas> e1)
	{
		if(e1==null) return 0;
		
		if(e1.equals(TridasObject.class) || e1.equals(TridasObjectEx.class))
		{
			return TreeDepth.OBJECT.getDepth();
		}
		else if(e1.equals(TridasElement.class) )
		{
			return TreeDepth.ELEMENT.getDepth();
		}
		else if(e1.equals(TridasSample.class) )
		{
			return TreeDepth.SAMPLE.getDepth();
		}
		else if(e1.equals(TridasRadius.class) )
		{
			return TreeDepth.RADIUS.getDepth();
		}
		else if(e1.equals(TridasMeasurementSeries.class) || e1.equals(TridasDerivedSeries.class))
		{
			return TreeDepth.SERIES.getDepth();
		}
		return 0;
	}
	
}
