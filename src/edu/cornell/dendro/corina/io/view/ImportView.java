package edu.cornell.dendro.corina.io.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
import javax.swing.table.TableModel;

import jsyntaxpane.DefaultSyntaxKit;

import org.netbeans.swing.outline.Outline;
import org.tridas.interfaces.ITridas;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.schema.TridasProject;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVC;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectEvent;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasSelectListener;
import edu.cornell.dendro.corina.io.ConversionWarningTableModel;
import edu.cornell.dendro.corina.io.TridasFileImportPanel;
import edu.cornell.dendro.corina.io.control.FileSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportEntitySelectedEvent;
import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow.ImportAction;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.tridasv2.ui.CorinaPropertySheetTable;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyEditorFactory;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyRendererFactory;
import edu.cornell.dendro.corina.ui.Alert;

import javax.swing.BoxLayout;

public class ImportView extends JDialog{

	private static final long serialVersionUID = -9142222993569420620L;
	
	/** The MVC Import Model **/
	private ImportModel model;
	
	/** Main panel into which everything is placed **/
	private final JPanel contentPanel = new JPanel();
	
	/** Panel and tree-table (outline) for displaying tridas representation of file **/
	private JPanel panelTreeTable; 
	private Outline treeTable;

	/** Panel and text area for displaying original legacy file **/
	private JPanel panelOrigFile;
	private JTextArea originalFilePane;
	
	/** Panel and table for displaying conversion warnings **/
	private JPanel panelWarnings;
	private JTable tblWarnings;
	
	/** Tabbed pane for **/
	private JTabbedPane tabbedPane;
	
	/** Left/Right split pane**/
	private JSplitPane horizSplitPane;
	
	/** Top/Bottom split pane **/
	private JSplitPane vertSplitPane;
	
	/** Our property sheet panel (contains table and description) */
	private PropertySheetPanel propertiesPanel;
	
	/** Our properties table */
	private CorinaPropertySheetTable propertiesTable;
	
	/** Panel containing the edit/save changes/cancel buttons for the current entity */
	private JPanel bottombar;
	
	/** The lock/unlock button for making changes to the currently selected entity */
	private JToggleButton editEntity;
	
	/** The save button when unlocked */
	private JButton editEntitySave;
	
	/** The cancel button when unlocked */
	private JButton editEntityCancel;
	
	/** Text associated with lock/unlock button */
	private JLabel editEntityText;
	
	/** Panel for ok/cancel/finish buttons **/
	private JLabel lblConversionWarnings;
	private JLabel lblTridasRepresentationOf;
		
	private JButton btnFinish;
	private JButton btnSetFromDB;
	
	
	/**
	 * Standard constructor for Import Dialog with no file provided
	 */
	public ImportView()
	{
		model = CorinaModelLocator.getInstance().getImportModel();
		MVC.showEventMonitor();
		initComponents();
		linkModel();
		initListeners();
		initLocale();
	}
	
	/**
	 * Constructor with file already specified
	 * @param file
	 */
	public ImportView(File file)
	{
		model = CorinaModelLocator.getInstance().getImportModel();
		MVC.showEventMonitor();
		initComponents();
		linkModel();
		initListeners();
		initLocale();
		FileSelectedEvent event = new FileSelectedEvent(file, model);
		event.dispatch();
	}
	
	/**
	 * Initialise the GUI components
	 */
	private void initComponents()
	{
		setTitle("Import to Database");
		setBounds(100, 100, 804, 734);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		{
			{
				{
					{
						DefaultSyntaxKit.initKit();
					}
				}
			}
		}
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			vertSplitPane = new JSplitPane();
			vertSplitPane.setOneTouchExpandable(true);
			vertSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			contentPanel.add(vertSplitPane);
			{
				panelWarnings = new JPanel();
				panelWarnings.setBorder(new EmptyBorder(5, 5, 5, 5));
				vertSplitPane.setRightComponent(panelWarnings);
				panelWarnings.setLayout(new BorderLayout(0, 8));
				{
					lblConversionWarnings = new JLabel("Conversion warnings:");
					panelWarnings.add(lblConversionWarnings, BorderLayout.NORTH);
				}
				{
					JScrollPane scrollPane = new JScrollPane();
					lblConversionWarnings.setLabelFor(scrollPane);
					panelWarnings.add(scrollPane);
					{
						tblWarnings = new JTable();
						scrollPane.setViewportView(tblWarnings);
					}
				}
			}
			horizSplitPane = new JSplitPane();
			vertSplitPane.setLeftComponent(horizSplitPane);
			horizSplitPane.setOneTouchExpandable(true);
			horizSplitPane.setBorder(null);
			horizSplitPane.setDividerLocation(0.4);
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBorder(null);
			horizSplitPane.setRightComponent(tabbedPane);
			{
				JPanel panelMetadata = new JPanel();
				panelMetadata.setLayout(new BorderLayout());
				
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
				
				panelMetadata.add(propertiesPanel, BorderLayout.CENTER);
				tabbedPane.addTab("Extracted TRiDaS Metadata", null, panelMetadata, null);
				
			}
			{
				JPanel panelData = new JPanel();
				tabbedPane.addTab("Extracted Data", null, panelData, null);
				tabbedPane.setEnabledAt(1, false);
			}
			panelOrigFile = new JPanel();
			tabbedPane.insertTab("Original file", null, panelOrigFile, null, 0);
			panelOrigFile.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panelOrigFile.add(scrollPane, BorderLayout.CENTER);
					
					originalFilePane = new JTextArea();
					scrollPane.setViewportView(originalFilePane);
					originalFilePane.setEditable(false);
					originalFilePane.setFont(new java.awt.Font("Courier", 0, 12));
			}
			{
				panelTreeTable = new JPanel();
				panelTreeTable.setBorder(new EmptyBorder(5, 5, 5, 5));
				horizSplitPane.setLeftComponent(panelTreeTable);
				panelTreeTable.setLayout(new BorderLayout(0, 0));
				{
					btnSetFromDB = new JButton("Set current entity from database");
					panelTreeTable.add(btnSetFromDB, BorderLayout.SOUTH);
				}
				{
					lblTridasRepresentationOf = new JLabel("TRiDaS representation of file:");
					panelTreeTable.add(lblTridasRepresentationOf, BorderLayout.NORTH);
					
				    treeTable = new Outline();
				    
				    // Set the renderdata
				    treeTable.setRenderDataProvider(new TridasOutlineRenderData());

				    // Add tree to scroll pane
				    JScrollPane treeTableScrollPane = new JScrollPane();
				    treeTableScrollPane.setViewportView(treeTable);    
				    panelTreeTable.add(treeTableScrollPane, BorderLayout.CENTER);
					
				}
			}
		}
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			{
				btnFinish = new JButton("Finish");
				buttonPanel.add(btnFinish);
			}
		}

		pack();
		//this.setSize(new Dimension(800,600));
		vertSplitPane.setDividerLocation(0.6);
	}
	
	/**
	 * Link to MVC Model so that we can update the GUI when events are detected
	 */
	private void linkModel()
	{	
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String name = evt.getPropertyName();

				if(name.equals(ImportModel.ORIGINAL_FILE))
				{
					// Original file has changed so GUI to show its contents
                    originalFilePane.setText(model.getFileToImportContents());
				}
				else if (name.equals(ImportModel.CONVERSION_WARNINGS))
				{
					// New conversion warnings so set the table
					updateConversionWarningsTable(model.getConversionWarnings());
				}
				else if (name.equals(ImportModel.SELECTED_NODE))
				{
					// An entity in the tree-table has been selected so 
					// update the metadata panel
					updateMetadataPanel(model.getSelectedNode().entity);
				}
				else if (name.equals(ImportModel.TREE_MODEL))
				{
					// Tree model has changed so update the tree-table
					updateTreeTable(model.getTreeModel());
				}
				
			}	
		});
		

	}
	
	/**
	 * Set up the MVC listeners for dispatching events from this GUI
	 */
	private void initListeners()
	{
		// Listen to entities being selected in tree table
		this.treeTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) { }
			
			@Override
			public void mousePressed(MouseEvent e) { }
			
			@Override
			public void mouseExited(MouseEvent e) {	}
			
			@Override
			public void mouseEntered(MouseEvent e) { }
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int selRow = treeTable.getSelectedRow();
				Object selectedEntity = treeTable.getValueAt(selRow, 0);	
				ImportAction selectedAction = (ImportAction) treeTable.getValueAt(selRow, 1);

				if(selectedEntity instanceof TridasProject)
				{
					// We don't import Projects so override action to 'Ignore'
					TridasRepresentationTableTreeRow row = 
						new TridasRepresentationTableTreeRow((ITridas)selectedEntity, ImportAction.IGNORE);
                    ImportEntitySelectedEvent event = new ImportEntitySelectedEvent(row, model);
                    event.dispatch();
				}
				else if (selectedEntity instanceof ITridas)
				{
					TridasRepresentationTableTreeRow row = 
						new TridasRepresentationTableTreeRow((ITridas)selectedEntity, selectedAction);
                    ImportEntitySelectedEvent event = new ImportEntitySelectedEvent(row, model);
                    event.dispatch();
				}
				
			}
		});

		
		// Listen for finish button press
		this.btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
		});
		
		// Listen for set from db button press
		this.btnFinish.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Alert.message("blah", "Button pressed");
				
			}
		});
		
		
	}
	
	/**
	 * Localise the GUI components
	 */
	private void initLocale()
	{
		
	}
	
	/**
	 * Set up the conversion warnings table with the provided warnings
	 * @param warnings
	 */
	private void updateConversionWarningsTable(ConversionWarning[] warnings)
	{
		ConversionWarningTableModel cwModel = new ConversionWarningTableModel(warnings);
		tblWarnings.setModel(cwModel);
	}
	
	/**
	 * Set the metadata panel to show the provided entity
	 * @param entity
	 */
	private void updateMetadataPanel(ITridas entity) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Set the tree-table panel to show the data specified in the 
	 * provided treeModel.
	 * @param treeModel
	 */
	private void updateTreeTable(TridasRepresentationTreeModel treeModel) {
		// TODO Auto-generated method stub
		
	}
	
}
