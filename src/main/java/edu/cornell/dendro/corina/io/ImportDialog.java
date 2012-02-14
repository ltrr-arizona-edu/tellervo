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
package edu.cornell.dendro.corina.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import jsyntaxpane.DefaultSyntaxKit;

import org.apache.commons.lang.WordUtils;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.exceptions.InvalidDendroFileException.PointerType;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.lowagie.text.Font;

import edu.cornell.dendro.corina.gui.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.TridasSelectListener;
import edu.cornell.dendro.corina.gui.hierarchy.TridasTreeViewPanel;
import edu.cornell.dendro.corina.tridasv2.TridasCloner;
import edu.cornell.dendro.corina.tridasv2.ui.CorinaPropertySheetTable;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyEditorFactory;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyRendererFactory;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityDeriver;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

public class ImportDialog extends JDialog implements PropertyChangeListener, TridasSelectListener{

	private static final long serialVersionUID = -9142222993569420620L;
	private final JPanel contentPanel = new JPanel();
	private JPanel panelTreeTable; 
	private JTextArea originalFilePane;
	private TridasFileImportPanel tridasreptree;
	private ConversionWarningTableModel warningsModel = new ConversionWarningTableModel();
	private JSplitPane splitPaneVert; 
	private JPanel panelWarnings;
	private JPanel panelOrigFile;
	private JTabbedPane tabbedPane;
	private JSplitPane horizSplitPane;
	
	/** Our property sheet panel (contains table and description) */
	private PropertySheetPanel propertiesPanel;
	/** Our properties table */
	private CorinaPropertySheetTable propertiesTable;
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
	private JTable tblWarnings;
	DefaultMutableTreeNode nodeSelected;

	
	/**
	 * Create the dialog.
	 */
	public ImportDialog(File file, AbstractDendroFileReader reader) {

		initGui();

		
		setFile(file, reader);
		
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);

		pack();
		tridasreptree.addTridasSelectListener(this);
		
	}
	
	public void setFile(File file, AbstractDendroFileReader reader)
	{
		String origfile = getContents(file);
		originalFilePane.setText(origfile);
		
		try {
			reader.loadFile(file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(file.getAbsolutePath(), e);
			this.dispose();
		} catch (InvalidDendroFileException e) {
			Alert.error("Invalid File", WordUtils.wrap(e.getLocalizedMessage(), 70));		
			// Try and highlight the line in the input file that is to blame if poss
			if(	e.getPointerType().equals(PointerType.LINE) 
					&& e.getPointerNumber()!=null 
					&& !e.getPointerNumber().equals("0"))
			{
				try{
					Integer linenum = Integer.parseInt(e.getPointerNumber());

				
					originalFilePane.addCaretListener(new LineHighlighter(Color.red, linenum));
					this.tabbedPane.setSelectedIndex(2);
					int pos=1;
					for(int i=0; i<linenum; i++)
					{
						 pos = originalFilePane.getText().indexOf("\n", pos+1);
					}
					originalFilePane.setCaretPosition(pos);
					
					} catch (NumberFormatException ex)
				{}
				
				
				tabbedPane.setEnabledAt(0, false);
				this.horizSplitPane.setDividerLocation(0);
				this.panelTreeTable.setVisible(false);
				this.panelWarnings.setVisible(false);	
				pack();
			}
		}
		catch(NullPointerException e)
		{
			Alert.error("Invalid File", e.getLocalizedMessage());
		}
		
		
		
		
		tridasreptree = new TridasFileImportPanel(reader.getProjects()[0]);
		
		setWarnings(reader.getWarnings());
		
		panelTreeTable.add(tridasreptree, BorderLayout.CENTER);
		
		
		
	}
	
	
	
	private void setWarnings(ConversionWarning[] warnings)
	{
			

		
		warningsModel = new ConversionWarningTableModel(warnings);
		tblWarnings.setModel(warningsModel);
		tblWarnings.setColumnSelectionAllowed(false);
		tblWarnings.setRowSelectionAllowed(true);
		tblWarnings.setDefaultRenderer(String.class, new RowShadingRenderer());
		//tblWarnings.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
		//updateRowHeights(tblWarnings);
		
		/*tblWarnings.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		tblWarnings.getColumnModel().getColumn(0).sizeWidthToFit();
		tblWarnings.getColumnModel().getColumn(1).sizeWidthToFit();
		tblWarnings.getColumnModel().getColumn(2).setPreferredWidth(1000);*/
		tblWarnings.setRowHeight(40);
		
		// Show or hide Warnings panel as necessary	
		if(warnings.length==0)
		{
			splitPaneVert.setDividerLocation(0.99);
		}
		else
		{
			splitPaneVert.setDividerLocation(0.7);
		}
		
	}
	
    /**
     * Set up the properties panel
     */
	private void initPropertiesPanel() {
		
		// Create table and panel to hold it
		propertiesTable = new CorinaPropertySheetTable();
		propertiesPanel = new PropertySheetPanel(propertiesTable);
		propertiesPanel.getTable().setEnabled(false);

		// Set various properties of the properties panel!
		propertiesPanel.setRestoreToggleStates(true);
		propertiesPanel.setToolBarVisible(false);
		propertiesPanel.setDescriptionVisible(true);
		propertiesPanel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		propertiesPanel.getTable().setRowHeight(24);
		propertiesPanel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		propertiesPanel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
		propertiesPanel.getTable().addPropertyChangeListener(this);
		
		// Set up button bar 
		setupButtonBar();
		
		
		
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
					if(!warnLosingChanges()) {
						editEntity.setSelected(true);
						return;
					}
					else {
						editEntity.setSelected(false);
						hasChanged = false;
					}
				}
				//enableEditing(editEntity.isSelected());
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
				//doSave();
			}
		});

		editEntityCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hasChanged = false;
				editEntity.setSelected(false);
				//enableEditing(false);				
			}
		});

		bottombar.add(Box.createHorizontalGlue());
		bottombar.add(editEntitySave);
		bottombar.add(Box.createHorizontalStrut(6));
		bottombar.add(editEntityCancel);
		bottombar.add(Box.createHorizontalStrut(6));
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
	
	  static public String getContents(File aFile) {
		    //...checks on aFile are elided
		    StringBuilder contents = new StringBuilder();
		    
		    try {
		      //use buffering, reading one line at a time
		      //FileReader always assumes default encoding is OK!
		      BufferedReader input =  new BufferedReader(new FileReader(aFile));
		      try {
		        String line = null; //not declared within while loop
		        /*
		        * readLine is a bit quirky :
		        * it returns the content of a line MINUS the newline.
		        * it returns null only for the END of the stream.
		        * it returns an empty String if two newlines appear in a row.
		        */
		        while (( line = input.readLine()) != null){
		          contents.append(line);
		          contents.append(System.getProperty("line.separator"));
		        }
		      }
		      finally {
		        input.close();
		      }
		    }
		    catch (IOException ex){
		      ex.printStackTrace();
		    }
		    
		    return contents.toString();
		  }


	private void initGui()
	{
		setTitle("Import to Database");
		setBounds(100, 100, 804, 734);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			horizSplitPane = new JSplitPane();
			horizSplitPane.setOneTouchExpandable(true);
			horizSplitPane.setBorder(null);
			horizSplitPane.setDividerLocation(0.4);
			contentPanel.add(horizSplitPane);
			{
				splitPaneVert = new JSplitPane();
				splitPaneVert.setOneTouchExpandable(true);
				splitPaneVert.setBorder(null);
				splitPaneVert.setOrientation(JSplitPane.VERTICAL_SPLIT);
				horizSplitPane.setLeftComponent(splitPaneVert);
				{
					panelTreeTable = new JPanel();
					panelTreeTable.setBorder(new TitledBorder(null, "TRiDaS Representation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					splitPaneVert.setLeftComponent(panelTreeTable);
					panelTreeTable.setLayout(new BorderLayout(0, 0));
					{
						JButton btnSetFromDB = new JButton("Set current entity from database");
						panelTreeTable.add(btnSetFromDB, BorderLayout.SOUTH);
					}
				}
				{
					panelWarnings = new JPanel();
					panelWarnings.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Warnings", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					splitPaneVert.setRightComponent(panelWarnings);
					panelWarnings.setLayout(new BorderLayout(0, 0));
					{
						JScrollPane scrollPane = new JScrollPane();
						panelWarnings.add(scrollPane, BorderLayout.CENTER);
						{
							tblWarnings = new JTable();
							scrollPane.setViewportView(tblWarnings);
						}
					}
				}
			}
			{
				tabbedPane = new JTabbedPane(JTabbedPane.TOP);
				tabbedPane.setBorder(null);
				horizSplitPane.setRightComponent(tabbedPane);
				{
					JPanel panelMetadata = new JPanel();
					panelMetadata.setLayout(new BorderLayout());
					initPropertiesPanel();
					
					panelMetadata.add(propertiesPanel, BorderLayout.CENTER);
					tabbedPane.addTab("Metadata", null, panelMetadata, null);
					
				}
				{
					JPanel panelData = new JPanel();
					tabbedPane.addTab("Data", null, panelData, null);
					tabbedPane.setEnabledAt(1, false);
				}
				{
					panelOrigFile = new JPanel();
					tabbedPane.addTab("Original file", null, panelOrigFile, null);
					panelOrigFile.setLayout(new BorderLayout(0, 0));
					{
						DefaultSyntaxKit.initKit();
						

						
						
					}
					{
						JScrollPane scrollPane = new JScrollPane();
						panelOrigFile.add(scrollPane, BorderLayout.CENTER);
						
							
							originalFilePane = new JTextArea();
							scrollPane.setViewportView(originalFilePane);
							originalFilePane.setEditable(false);
							originalFilePane.setFont(new java.awt.Font("Courier", 0, 12));
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton button = new JButton("Finish");
				buttonPane.add(button);
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unused")
	private static void updateRowHeights(JTable table)
	{
	    try
	    {
	        for (int row = 0; row < table.getRowCount(); row++)
	        {
	            int rowHeight = table.getRowHeight();

	            for (int column = 0; column < table.getColumnCount(); column++)
	            {
	                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
	                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
	            }

	            table.setRowHeight(row, rowHeight);
	        }
	    }
	    catch(ClassCastException e) {}
	}

	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		nodeSelected = event.getTreeNode();
		
		try {
			ITridas entity = event.getEntity();
			
			if(entity instanceof TridasObject || entity instanceof TridasObjectEx)
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

}
