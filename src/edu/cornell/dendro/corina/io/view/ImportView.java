package edu.cornell.dendro.corina.io.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jsyntaxpane.DefaultSyntaxKit;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.exceptions.InvalidDendroFileException.PointerType;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVC;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.lowagie.text.Font;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.editor.SampleDataView;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.io.ConversionWarningTableModel;
import edu.cornell.dendro.corina.io.LineHighlighter;
import edu.cornell.dendro.corina.io.control.FileSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportEntityListChangedEvent;
import edu.cornell.dendro.corina.io.control.ImportNodeSelectedEvent;
import edu.cornell.dendro.corina.io.control.ImportSwapEntityEvent;
import edu.cornell.dendro.corina.io.model.ImportEntityListComboBox;
import edu.cornell.dendro.corina.io.model.ImportModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow.ImportStatus;
import edu.cornell.dendro.corina.model.CorinaModelLocator;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.tridasv2.TridasCloner;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.tridasv2.ui.CorinaPropertySheetTable;
import edu.cornell.dendro.corina.tridasv2.ui.EntityListComboBox;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyEditorFactory;
import edu.cornell.dendro.corina.tridasv2.ui.TridasPropertyRendererFactory;
import edu.cornell.dendro.corina.tridasv2.ui.TridasMetadataPanel.EditType;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityDeriver;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityListHolder;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityProperty;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.FilterableComboBoxModel;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

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
	private JTextArea txtOriginalFile;
	
	/** Panel for displaying ring with data**/
	private JPanel panelData;
	
	/** Panel and table for displaying conversion warnings **/
	private JPanel panelWarnings;
	private JTable tblWarnings;
	
	/** Tabbed pane for original file, metadata and data tabs**/
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
	
	/** Panel containing combo box and buttons for selecting entities from db*/
	private JPanel topbar;
	
	/** The lock/unlock button for making changes to the currently selected entity */
	private JToggleButton editEntity;
	
	/** The save button when unlocked */
	private JButton editEntitySave;
	
	/** The cancel button when unlocked */
	private JButton editEntityCancel;
	
	/** Text associated with lock/unlock button */
	private JLabel editEntityText;

	/** Labels **/
	private JLabel lblConversionWarnings;
	private JLabel lblTridasRepresentationOf;
	private JLabel lblTopBarSummaryTitle;

	/** Finish button **/
	private JButton btnFinish;
	
	/** Set from db button **/
	private JButton btnSetFromDB;
	
	private JTextPane txtErrorMessage;
	private ImportEntityListComboBox topChooser;
	private JButton changeButton;
	private JButton cancelChangeButton;
	
	/** A copy of the entity that we're currently editing */
	private ITridas temporaryEditingEntity;
	/** A copy of the entity that we're currently selecting */
	private ITridas temporarySelectingEntity;
	private ITridas currentEntity;
	private ITridas parentEntity;
	private Class<? extends ITridas> currentEntityType;
	
	private TridasEntityListHolder lists = new TridasEntityListHolder();
	private static final String CHANGE_STATE = I18n.getText("general.change");
	private static final String CHOOSE_STATE = I18n.getText("general.choose");
	private boolean changingTop;
	private ChoiceComboBoxActionListener topChooserListener;
	
	/**
	 * Standard constructor for Import Dialog with no file provided
	 */
	public ImportView()
	{
		topChooserListener = new ChoiceComboBoxActionListener(this);
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
	public ImportView(File file, String fileType)
	{
		topChooserListener = new ChoiceComboBoxActionListener(this);

		model = CorinaModelLocator.getInstance().getImportModel();
		MVC.showEventMonitor();
		initComponents();
		linkModel();
		initListeners();
		initLocale();
		FileSelectedEvent event = new FileSelectedEvent(model, file, fileType);
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
			horizSplitPane.setDividerLocation(0.5);
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBorder(null);
			horizSplitPane.setRightComponent(tabbedPane);
			{			
				JPanel panelMetadata = new JPanel();
				panelMetadata.setLayout(new BorderLayout());
			
				topbar = new JPanel();
				bottombar = new JPanel();
				topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));
				bottombar.setLayout(new BoxLayout(bottombar, BoxLayout.X_AXIS));
				////////// TOP BAR
				lblTopBarSummaryTitle = new JLabel(I18n.getText("general.initializing"));
				topChooser = new ImportEntityListComboBox();
				changeButton = new JButton(CHANGE_STATE);
				cancelChangeButton = new JButton(I18n.getText("general.revert"));
				topbar.add(lblTopBarSummaryTitle);
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
				panelMetadata.add(topbar, BorderLayout.NORTH);
				
				////////// BOTTOM BAR
				editEntity = new JToggleButton();
				editEntity.setIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("lock.png", Builder.ICONS, 32)));
				editEntity.setSelectedIcon(scaleIcon20x20((ImageIcon) Builder.getIcon("unlock.png", Builder.ICONS, 32)));
				editEntity.setBorderPainted(false);
				editEntity.setContentAreaFilled(false);
				editEntity.setFocusable(false);
				
				bottombar.add(editEntity);
			
				editEntityText = new JLabel(I18n.getText("general.initializing").toLowerCase());
				editEntityText.setLabelFor(editEntity);
				
				editEntitySave = new JButton(I18n.getText("general.saveChanges"));
				editEntityCancel = new JButton(I18n.getText("general.cancel"));
				
				// don't let an errant enter key fire these buttons!
				editEntitySave.setDefaultCapable(false);
				editEntityCancel.setDefaultCapable(false);
				
				bottombar.add(editEntityText);
				bottombar.add(Box.createHorizontalGlue());
				bottombar.add(editEntitySave);
				bottombar.add(Box.createHorizontalStrut(6));
				bottombar.add(editEntityCancel);
				bottombar.add(Box.createHorizontalStrut(6));
				
				panelMetadata.add(bottombar, BorderLayout.SOUTH);
				
				// Create metadata table and panel to hold it
				propertiesTable = new CorinaPropertySheetTable();
				propertiesPanel = new PropertySheetPanel(propertiesTable);
				//propertiesPanel.getTable().setEnabled(false);

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
				panelData = new JPanel();
				tabbedPane.addTab("Extracted Data", null, panelData, null);
				tabbedPane.setEnabledAt(1, false);
			}
			panelOrigFile = new JPanel();
			tabbedPane.insertTab("Original file", null, panelOrigFile, null, 0);
			panelOrigFile.setLayout(new BorderLayout(10, 10));
			{
				JScrollPane scrollPane = new JScrollPane();
				panelOrigFile.add(scrollPane, BorderLayout.CENTER);
					
					txtOriginalFile = new JTextArea();
					scrollPane.setViewportView(txtOriginalFile);
					txtOriginalFile.setEditable(false);
					txtOriginalFile.setFont(new java.awt.Font("Courier", 0, 12));
			}
			
			txtErrorMessage = new JTextPane();
			txtErrorMessage.setForeground(Color.RED);
			txtErrorMessage.setVisible(false);
			panelOrigFile.add(txtErrorMessage, BorderLayout.NORTH);
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
		vertSplitPane.setDividerLocation(0.8);
		horizSplitPane.setDividerLocation(0.5);
		pack();
		chooseOrCancelUIUpdate();
		enableEditing(false);
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
                    txtOriginalFile.setText(model.getFileToImportContents());
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
														
					// Check that the tree table is expanded to show the selected node
					int selRow = treeTable.getSelectedRow();
					DefaultMutableTreeNode guiSelectedEntity = 
						(DefaultMutableTreeNode) treeTable.getValueAt(selRow, 0);
					DefaultMutableTreeNode modelSelectedEntity = model.getSelectedNode().node;
					if((guiSelectedEntity==null) || (!guiSelectedEntity.equals(modelSelectedEntity)))
					{
						TreePath path =  getPath(modelSelectedEntity);
						treeTable.getOutlineModel().getTreePathSupport().expandPath(path);
					}
					
					// Update the metadata panel
					updateMetadataPanel(model.getSelectedNode(), (DefaultMutableTreeNode) model.getSelectedNode().node.getParent());			

				}
				else if (name.equals(ImportModel.TREE_MODEL))
				{
					// Tree model has changed so update the tree-table
					updateTreeTable(model.getTreeModel());
				}
				else if (name.equals(ImportModel.INVALID_FILE_EXCEPTION))
				{
					setGUIForInvalidFile(model.getFileException());
				}
				else if (name.equals(ImportModel.ENTITY_CHOOSER_LIST))
				{
					updateEntityChooser(model.getEntityChooserList());
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
				
				// Check the user isn't part way through changing this entity
				if(!isUserOkWithLoosingAnyChanges()) return;

				if(changingTop)
				{
					changingTop =false;
					chooseOrCancelUIUpdate();
				}
				
				int selRow = treeTable.getSelectedRow();
				DefaultMutableTreeNode selectedEntity = 
					(DefaultMutableTreeNode) treeTable.getValueAt(selRow, 0);	
				ImportStatus status = (ImportStatus) treeTable.getValueAt(selRow, 1);
				TridasRepresentationTableTreeRow row = new TridasRepresentationTableTreeRow(selectedEntity, status);		
                ImportNodeSelectedEvent event = new ImportNodeSelectedEvent(model, row);
                event.dispatch();
				
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
		
		this.changeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(changeButton.getText().equals(CHANGE_STATE))
				{
					changingTop = true;
				}
				else if (changeButton.getText().equals(CHOOSE_STATE))
				{
					changingTop = false;
					propertiesTable.setHasWatermark(false);
					TridasRepresentationTableTreeRow oldrow = model.getSelectedNode();	
					DefaultMutableTreeNode node = new DefaultMutableTreeNode((ITridas) topChooser.getSelectedItem());
					TridasRepresentationTableTreeRow newrow = 
						new TridasRepresentationTableTreeRow(node, ImportStatus.STORED_IN_DATABASE);
					ImportSwapEntityEvent event = new ImportSwapEntityEvent(model, newrow, oldrow);
                    event.dispatch();

				}
				chooseOrCancelUIUpdate();	
			}
		});
		
		this.cancelChangeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changingTop = false;
				chooseOrCancelUIUpdate();

			}
		});
		
		editEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			/*	if(!editEntity.isSelected() && currentMode.hasChanged()) {
					if(!warnLosingChanges()) {
						editEntity.setSelected(true);
						return;
					}
					else {
						currentMode.clearChanged();
					}
				}*/
				enableEditing(editEntity.isSelected());
			}			
		});
		
		editEntitySave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});

		editEntityCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//currentMode.clearChanged();
				editEntity.setSelected(false);
				enableEditing(false);				
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
		lblConversionWarnings.setText("Conversion warnings ("+warnings.length+"):");
		TableColumn col = tblWarnings.getColumnModel().getColumn(0);
		int width = 70;
		col.setMinWidth(width); 
		col.setPreferredWidth(width);
		col = tblWarnings.getColumnModel().getColumn(1);
		col.setMinWidth(width); 
		col.setPreferredWidth(width);
		col = tblWarnings.getColumnModel().getColumn(2);
		col.setMinWidth(width); 
		col.setPreferredWidth(5000);
	}
	
	private void setTopBarSummaryTitle(DefaultMutableTreeNode parentNode)
	{
		lblTopBarSummaryTitle.setText("");
	}
	
	
	/**
	 * Update the data panel to show the ring width values
	 * 
	 * @param row
	 */
	private void updateDataPanel(
			TridasRepresentationTableTreeRow row) {
	
		ITridas entity = (ITridas) row.node.getUserObject();
		
		if(!(entity instanceof TridasMeasurementSeries))
		{
			// Not a series
			
			if(tabbedPane.getSelectedIndex()==2)
			{
				// Select the metadata tab if currently on the data tab
				tabbedPane.setSelectedIndex(1);
			}
			
			// Disable the data tab 
			tabbedPane.setEnabledAt(2, false);
			return;
		}
		
		TridasMeasurementSeries series = (TridasMeasurementSeries) entity;
		Sample s = new Sample(series);
		
		SampleDataView dataPanel = new SampleDataView(s);
		
		panelData.setLayout(new BorderLayout());
		panelData.add(dataPanel, BorderLayout.CENTER);
		tabbedPane.setEnabledAt(2, true);
	}
	
	/**
	 * Set the metadata panel to show the provided entity
	 * @param parentNode 
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	private void updateMetadataPanel(TridasRepresentationTableTreeRow row, DefaultMutableTreeNode parentNode) {
		/*if(!silent)
		{
			if(!warnLosingChanges())
			{
				
			}
		}*/
		
		// Enable or disable the data page depending on current entity type
		updateDataPanel(row);	
		
		boolean hasChanged = false;
		
		// Set the text in the top bar
		setTopBarSummaryTitle(parentNode);
		
		// Extract the entity and entity type from the select table tree row
		currentEntity = (ITridas) row.node.getUserObject();
		currentEntityType = (Class<? extends ITridas>) row.node.getUserObject().getClass();
		
		// Extract the parent entity 
		try{
			parentEntity = (ITridas) parentNode.getUserObject();
			System.out.println("Set parent entity successfully to: " + parentEntity.getTitle());
		} catch (Exception e){
			System.out.println("Unable to set parent entity");
		}
			
		// Swapping entities so disable editing
		//enableEditing(false);	
		
		// Derive a property list
        List<TridasEntityProperty> properties = TridasEntityDeriver.buildDerivationList(currentEntityType);
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
        // Set properties for current entity type
		propertiesPanel.setProperties(propArray);
		propertiesTable.expandAllBranches(true);
		
		// Add data to table from entity
		if(currentEntity!=null)
		{
			propertiesPanel.readFromObject(currentEntity);
			propertiesPanel.setEnabled(true);
			editEntity.setVisible(true);
		}
		else
		{
			propertiesPanel.setEnabled(false);
			editEntity.setVisible(false);
		}
		
		// Set the watermark text across metadata panel
		if(row.action.equals(ImportStatus.PENDING))
		{
			propertiesTable.setHasWatermark(true);
			propertiesTable.setWatermarkText(I18n.getText("general.preview").toUpperCase());
		}
		else if(row.action.equals(ImportStatus.IGNORE))
		{
			propertiesTable.setHasWatermark(true);
			propertiesTable.setWatermarkText("IGNORED");
		}
		else
		{
			propertiesTable.setHasWatermark(false);
		}
			
		// Update the combo box list
		ImportEntityListChangedEvent ev = new ImportEntityListChangedEvent(model, currentEntity, parentEntity);
		ev.dispatch();
		
		// Set the top chooser to the correct current value
		selectInTopChooser();
		
	}
	
	/**
	 * Called by either button press to update the UI state
	 */
	private void chooseOrCancelUIUpdate() {
		// disable/enable editing
		editEntity.setEnabled(!changingTop);
		topChooser.setEnabled(changingTop);
		changeButton.setText(changingTop ? CHOOSE_STATE : CHANGE_STATE);
		cancelChangeButton.setVisible(changingTop);	
		//propertiesTable.setEditable(changingTop);
		
	}
	
	/**
	 * Update the entity chooser combo box based on the current entity
	 * and its parent
	 * 
	 * @param thisEntity
	 * @param parentEntity
	 */
	private void updateEntityChooser(List<? extends ITridas> entities)
	{
		topChooser.setList(entities);
		if (currentEntity instanceof TridasProject)
		{
			// This entity is a project so set list to null as 
			// projects aren't supported
			setTopChooserEnabled(false, "Projects are not supported in Corina");
			return;
		}
		else if(currentEntity instanceof TridasObject)
		{
			// This entity is an object so grab object dictionary
			setTopChooserEnabled(true, null);
			return;
		}
		
		// Otherwise, check that the parent is already in db by checking
		// the identifier domain and set list accordingly 
		try{
			if (parentEntity.getIdentifier().getDomain().equals(App.domain))
			{
				if(currentEntity instanceof TridasMeasurementSeries)
				{
					setTopChooserEnabled(false, null);
					return;
				}
				else
				{
					setTopChooserEnabled(true, null);
					return;
				}
			}
		} catch (Exception e)
		{
			setTopChooserEnabled(false, "Fix parent entity first");
			return;
		}	
		
	}
	
	/**
	 * Set whether the top chooser combo box panel is enabled or not
	 * 
	 * @param b
	 * @param message
	 */
	private void setTopChooserEnabled(Boolean b, String message)
	{

		topChooser.setVisible(b);
		changeButton.setVisible(b);
		if(message !=null) lblTopBarSummaryTitle.setText(message);

	}
	
	
	
	/**
	 * Set the tree-table panel to show the data specified in the 
	 * provided treeModel.
	 * @param treeModel
	 */
	private void updateTreeTable(TridasRepresentationTreeModel treeModel) {
		
		OutlineModel mdl = DefaultOutlineModel.createOutlineModel(
	    		treeModel, treeModel, true, "TRiDaS Entities");
		
		this.treeTable.setModel(mdl);
	}
	
	/**
	 * Set the GUI to illustrate an invalid file
	 * @param fileException
	 */
	private void setGUIForInvalidFile(InvalidDendroFileException e) {
		
		String error = e.getLocalizedMessage();
		// Try and highlight the line in the input file that is to blame if poss
		if(	e.getPointerType().equals(PointerType.LINE) 
				&& e.getPointerNumber()!=null 
				&& e.getPointerNumber()!=0)
		{
			txtOriginalFile.addCaretListener(new LineHighlighter(Color.red, e.getPointerNumber()));
			this.tabbedPane.setSelectedIndex(0);
			int pos=1;
			for(int i=0; i<e.getPointerNumber(); i++)
			{
				 pos = txtOriginalFile.getText().indexOf("\n", pos+1);
			}
			
			txtOriginalFile.setCaretPosition(pos);
			tabbedPane.setEnabledAt(1, false);
			this.horizSplitPane.setDividerLocation(0);
			this.panelTreeTable.setVisible(false);
			this.panelWarnings.setVisible(false);	
			error+="\n\n"+"The file is shown below with the problematic line highlighted.";
			
		}
		
		txtErrorMessage.setVisible(true);
		txtErrorMessage.setText(error);
		pack();
		
	}	
	
	/** Scale an icon down to 20x20 */
	private ImageIcon scaleIcon20x20(ImageIcon icon) {
		return new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	}
	
	/**
	 * Determine in currentEntity is new by checking to see if it has a valid
	 * identifier for this domain.
	 * 
	 * @return
	 */
	private boolean isCurrentEntityNew()
	{
		if (!currentEntity.isSetIdentifier())
		{
			return true;
		}
		else if (!currentEntity.getIdentifier().isSetDomain())
		{
			System.out.println("No domain");
			return true;
		}
		else if (!currentEntity.getIdentifier().getDomain().equals(App.domain))
		{
			System.out.println("Different domain - this one is: "+currentEntity.getIdentifier().getDomain());
			return true;
		}
		return false;
	}
	
	/**
	 * Select an item in the combo box
	 * Use because TridasEntity.equals compares things we don't care about
	 * 
	 * @param thisEntity
	 */
	private void selectInTopChooser() {
		// disable actionListener firing when we change combobox selection
		topChooserListener.setEnabled(false);
		
		try {
			if (currentEntity == null) {
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.isSetIdentifier())
			{
				System.out.println("No identifier");
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.getIdentifier().isSetDomain())
			{
				System.out.println("No domain");
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.getIdentifier().getDomain().equals(App.domain))
			{
				System.out.println("Different domain - this one is: "+currentEntity.getIdentifier().getDomain());
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else
			{
				System.out.println("Correct domain: "+currentEntity.getIdentifier().getDomain());
				
			}
				

			FilterableComboBoxModel model = (FilterableComboBoxModel) topChooser.getModel();

			// find it in the list...
			ITridas listEntity;
			if ((listEntity = entityInList(currentEntity, model.getElements())) != null) {
				topChooser.setSelectedItem(listEntity);
				return;
			}

			// blech, it wasn't in the list -> add it
			model.insertElementAt(currentEntity, 2);
			topChooser.setSelectedItem(currentEntity);
		} finally {
			// deal with the selection
			handleComboSelection(false);
			// re-enable combo box actionlistener firing
			topChooserListener.setEnabled(true);
		}
	}
	
	/**
	 * Silly actionListener class that we can add/remove when we want to programmatically change the combo box
	 * without triggering side effects
	 */
	private class ChoiceComboBoxActionListener implements ActionListener {
		private final ImportView panel;
		private boolean enabled;
		
		public ChoiceComboBoxActionListener(ImportView panel) {
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
		
		//fixes bug where combobox is hidden in derived series
		if(obj == null){
			return;
		}
		
		if(obj instanceof ITridas) {
			temporarySelectingEntity = (ITridas) obj;
			
			if(propertiesTable.hasWatermark()){
					propertiesTable.setWatermarkText(I18n.getText("general.preview"));
			}				

			// start loading the list of children right away
			//if(loadChildren && currentMode != EditType.RADIUS)
			//	lists.prepareChildList(temporarySelectingEntity);	
			propertiesPanel.readFromObject(temporarySelectingEntity);	
		}
		
			
	}
	
	/**
	 * Compare entities based on type, title, and site code (if they're TridasObjectEx)
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
	 * Get the tree path for a node
	 * 
	 * @param node
	 * @return
	 */
	private TreePath getPath(TreeNode node) { 
		List list = new ArrayList(); 
		// Add all nodes to list 
		while (node != null) 
		{ 
			list.add(node); 
			node = node.getParent();
		} 
		Collections.reverse(list); 
		// Convert array of nodes to TreePath 
		return new TreePath(list.toArray());  
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
		editEntityText.setText(enabled ? I18n.getText("metadata.currentlyEditingThis") + " " + "entity" 
				: I18n.getText("metadata.clickLockToEdit") + " " + "entity");
		
		if(enabled) {
			propertiesTable.setHasWatermark(false);
			if(currentEntity instanceof ITridasSeries)
				temporaryEditingEntity = TridasCloner.cloneSeriesRefValues((ITridasSeries) currentEntity, (Class<? extends ITridasSeries>) currentEntity.getClass());
			else
				temporaryEditingEntity = TridasCloner.clone(currentEntity, currentEntity.getClass());

			// user chose to edit without choosing 'new', so be nice and make a new one for them
			/*if(temporaryEditingEntity == null && topChooser.getSelectedItem() == EntityListComboBox.NEW_ITEM) {
				temporaryEditingEntity = thisEntity;
				populateNewEntity(currentMode, temporaryEditingEntity);
			}*/

			if(temporaryEditingEntity != null)
				propertiesPanel.readFromObject(temporaryEditingEntity);
		}
		else {
			temporaryEditingEntity = null;
						
			// don't display anything if we have nothingk!
			if(currentEntity != null)
				propertiesPanel.readFromObject(currentEntity);
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
	
	
	/**
	 * @return true if the user wants to lose changes, false otherwise
	 */
	private boolean isUserOkWithLoosingAnyChanges() {

		if(changingTop)
		{
			int ret = JOptionPane.showConfirmDialog(this, 
					I18n.getText("question.confirmChangeForm"), 
					I18n.getText("question.continue"), 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			return (ret == JOptionPane.YES_OPTION);
		}
		else
		{
			return true;
		}
		
	}
	
	/**
	 * @return true if the user wants to lose the new selection, false otherwise
	 */
	private boolean isUserOkWithLoosingSelection() {
		
		// nothing new has been selected, nothing to lose
		if(temporarySelectingEntity == null || temporarySelectingEntity.equals(currentEntityType))
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				I18n.getText("question.confirmChangeForm"), 
				I18n.getText("question.continue"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return (ret == JOptionPane.YES_OPTION);
	}
	
	
	/**
	 * Remove any children from an entity ready for sending to the webservice
	 *  
	 * @param entity
	 * @return
	 */
	private ITridas removeChildren(ITridas entity)
	{
		System.out.println("removing children from "+entity.getTitle());
		if(entity instanceof TridasProject)
		{
			((TridasProject) entity).setDerivedSeries(null);
			((TridasProject) entity).setObjects(null);
		}
		else if (entity instanceof TridasObject)
		{
			((TridasObject) entity).setElements(null);
			((TridasObject) entity).setObjects(null);
		}
		else if (entity instanceof TridasElement)
		{
			((TridasElement) entity).setSamples(null);
		}
		else if (entity instanceof TridasSample)
		{
			((TridasSample) entity).setRadiuses(null);
		}
		else if (entity instanceof TridasRadius)
		{
			((TridasRadius) entity).setMeasurementSeries(null);
		}		
		return entity;
	}
	
	/**
	 * Do the actual save
	 */
	private void doSave() {
		if(temporaryEditingEntity == null)
			throw new IllegalStateException();
		
		// if nothing actually changed, just ignore it like a cancel
		/*if(!currentMode.hasChanged()) {			
			editEntityCancel.doClick();
			return;
		}*/
		
		propertiesPanel.writeToObject(temporaryEditingEntity);

		temporaryEditingEntity = removeChildren(temporaryEditingEntity);
		
		// are we saving something new?
		boolean isNew = isCurrentEntityNew();
		
		// Logic for saving to the server
		
		// sanity check to ensure parent entity being null means we're an object
		if(parentEntity == null && !currentEntityType.equals(TridasObject.class)) {
			new Bug(new IllegalStateException("parentEntity is null, but not an object"));
			return;
		}
				
		// the resource we'll use
		EntityResource<? extends ITridas> resource;
		
		if(isNew)
			resource = getNewAccessorResource(temporaryEditingEntity, parentEntity, 
					currentEntityType);
		else
			resource = getUpdateAccessorResource(temporaryEditingEntity, currentEntityType);

		// set up a dialog...
		Window parentWindow = SwingUtilities.getWindowAncestor(this);
		CorinaResourceAccessDialog dialog = CorinaResourceAccessDialog.forWindow(parentWindow, resource);

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
		currentEntity = temporaryEditingEntity;
		TridasRepresentationTableTreeRow oldrow = model.getSelectedNode();	
		DefaultMutableTreeNode node = new DefaultMutableTreeNode((ITridas) temporaryEditingEntity);
		TridasRepresentationTableTreeRow newrow = 
			new TridasRepresentationTableTreeRow(node, ImportStatus.STORED_IN_DATABASE);
		ImportSwapEntityEvent event = new ImportSwapEntityEvent(model, newrow, oldrow);
        event.dispatch();

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
			selectInTopChooser();
		
		}
		
		// on success...
		//currentMode.clearChanged();
		editEntity.setSelected(false);
		enableEditing(false);
		//populateComboAndSelect(false);
		//temporaryEditingEntity = null;

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
	

}
