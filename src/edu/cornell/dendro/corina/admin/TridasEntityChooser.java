package edu.cornell.dendro.corina.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JFormattedTextField;
import javax.swing.JWindow;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.gui.dbbrowse.CorinaCodePanel;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectListener;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasTreeViewPanel.TreeDepth;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasTreeViewPanel;
import java.awt.Font;

public class TridasEntityChooser extends JDialog implements ActionListener, TridasSelectListener{

	private static final long serialVersionUID = 3079094155424090769L;
	private CorinaCodePanel codePanel;
	private Class<? extends ITridas> expectedClass;
	private EntitiesAccepted entitiesAccepted = EntitiesAccepted.ALL;
	private JButton okButton;
	private JButton cancelButton;
	private ITridas entity;
	private JLabel lblWarning;
	

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
		
		setupGui();

	}
	
	public TridasEntityChooser(JFrame parent, Class<? extends ITridas> clazz, EntitiesAccepted ea){
		super(parent, true);
		
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
			panel.setLayout(new MigLayout("", "[][][grow]", "[][][][][][][][]"));
			{
				JPanel panelIcon = new JPanel();
				panelIcon.setBorder(null);
				panel.add(panelIcon, "cell 0 0 1 7,alignx center,aligny top");
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
				codePanel = new CorinaCodePanel();
				panel.add(codePanel, "cell 2 4,growx,aligny top");
				{
					lblWarning = new JLabel("");
					lblWarning.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
					lblWarning.setForeground(Color.RED);
					panel.add(lblWarning, "cell 2 6");
				}
				
				codePanel.addTridasSelectListener(this);
				
			}
		}
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
				
			dispose();
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

        return dialog.getEntity();

	}

	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		try {
			
			entity = event.getEntity();
			
			if(checkEntityIsValid())
			{
				dispose();
			}
			else
			{
				if(entity!=null)
				{
					String givenClassName = TridasTreeViewPanel.getFriendlyClassName((Class<ITridas>) entity.getClass());
					String expectedClassName = TridasTreeViewPanel.getFriendlyClassName(expectedClass);
					String modifier = "";
					if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_ABOVE))
					{
						modifier = " (and above)";
					}
					else if (entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_AND_BELOW))
					{
						modifier = " (and below)";
					}
					Alert.message("Error", "The entity you entered is a " + givenClassName + "\n"+
							"when a "+ expectedClassName + modifier + " was expected");
				}
				else
				{
					Alert.message("Error", "Selected entity is null");
				}
				

			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			else
			{
				lblWarning.setText("Invalid entity - a "+TridasTreeViewPanel.getFriendlyClassName(expectedClass)+" is expected");
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
