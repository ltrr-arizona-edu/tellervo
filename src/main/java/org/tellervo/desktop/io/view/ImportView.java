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
package org.tellervo.desktop.io.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jsyntaxpane.DefaultSyntaxKit;
import net.miginfocom.swing.MigLayout;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.gui.widgets.TridasTreeViewPanel;
import org.tellervo.desktop.io.ConversionWarningTableModel;
import org.tellervo.desktop.io.LineHighlighter;
import org.tellervo.desktop.io.control.ExpandImportTreeEvent;
import org.tellervo.desktop.io.control.FileSelectedEvent;
import org.tellervo.desktop.io.control.ImportEntitySaveEvent;
import org.tellervo.desktop.io.control.ImportMergeEntitiesEvent;
import org.tellervo.desktop.io.control.ImportNodeSelectedEvent;
import org.tellervo.desktop.io.control.ImportSwapEntityEvent;
import org.tellervo.desktop.io.control.ReplaceHierarchyEvent;
import org.tellervo.desktop.io.model.ImportEntityListComboBox;
import org.tellervo.desktop.io.model.ImportModel;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow.ImportStatus;
import org.tellervo.desktop.io.model.TridasRepresentationTreeModel;
import org.tellervo.desktop.model.TellervoModelLocator;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetPanel;
import org.tellervo.desktop.tridasv2.ui.TellervoPropertySheetTable;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyEditorFactory;
import org.tellervo.desktop.tridasv2.ui.TridasPropertyRendererFactory;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityDeriver;
import org.tellervo.desktop.tridasv2.ui.support.TridasEntityProperty;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.FilterableComboBoxModel;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.PopupListener;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.enums.Charsets;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.exceptions.InvalidDendroFileException.PointerType;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVC;
import com.itextpdf.text.Font;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheet;

public class ImportView extends JFrame{

	private final static Logger log = LoggerFactory.getLogger(ImportView.class);

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
	private TellervoPropertySheetPanel propertiesPanel;
	
	/** Our properties table */
	private TellervoPropertySheetTable propertiesTable;
	
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
	private JButton btnMergeObjects;
	private JButton btnExpandNodes;
	private JButton btnContractNodes;
	private JButton btnAssignByCode;
	private JButton btnReparseFile;
	
	private JTextPane txtErrorMessage;
	private ImportEntityListComboBox topChooser;
	private JButton changeButton;
	private JButton cancelChangeButton;
		
	private static final String CHANGE_STATE = I18n.getText("general.change");
	private static final String CHOOSE_STATE = I18n.getText("general.choose");
	private boolean changingTop;
	private ChoiceComboBoxActionListener topChooserListener;
	private JEditorPane dataPane;
	private JScrollPane scrollPane_1;
	private JComboBox cboEncoding;
	private JLabel lblCharacterEncoding;
	
	/**
	 * Standard constructor for Import Dialog with no file provided
	 */
	public ImportView()
	{

		topChooserListener = new ChoiceComboBoxActionListener(this);
		model = TellervoModelLocator.getInstance().getImportModel();
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
		if(usingOldImporter(file, fileType)) return;
		
		topChooserListener = new ChoiceComboBoxActionListener(this);
		model = TellervoModelLocator.getInstance().getImportModel();
		//MVC.showEventMonitor();
		initComponents();
		linkModel();
		initListeners();
		initLocale();
		FileSelectedEvent event = new FileSelectedEvent(model, file, fileType);
		event.dispatch();
	}
	
	/**
	 * 
	 * @param file
	 * @param fileType
	 * @return
	 */
	private Boolean usingOldImporter(File file, String fileType)
	{
		
		
		return false;
	}
	
	/**
	 * Initialise the GUI components
	 */
	private void initComponents()
	{
		DefaultSyntaxKit.initKit();
		
		setTitle("Import to Database");
		setBounds(100, 100, 804, 734);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		{
			
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
				lblTopBarSummaryTitle = new JLabel("");
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
				propertiesTable = new TellervoPropertySheetTable();
				propertiesPanel = new TellervoPropertySheetPanel(propertiesTable);
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
				panelData.setLayout(new BorderLayout(0, 0));
				
				scrollPane_1 = new JScrollPane();
				panelData.add(scrollPane_1);
				
				dataPane = new JEditorPane();
				dataPane.setEditable(false);
				scrollPane_1.setViewportView(dataPane);
				tabbedPane.setEnabledAt(1, false);
			}
			panelOrigFile = new JPanel();
			tabbedPane.insertTab("Original file", null, panelOrigFile, null, 0);
			panelOrigFile.setLayout(new MigLayout("", "[159.00px][173.00][grow]", "[fill][][707px,grow,fill]"));
		
			
			cboEncoding = new JComboBox();			
			lblCharacterEncoding = new JLabel("Character encoding:");
			panelOrigFile.add(lblCharacterEncoding, "flowx,cell 0 1,alignx left");
			for (String s : Charsets.getReadingCharsets()) {
				if (s.equals(Charset.defaultCharset().displayName())) {
					continue;
				}
				cboEncoding.addItem(s);
			}
			
			panelOrigFile.add(cboEncoding, "cell 1 1 2 1,alignx left");
			{

					
			}
			
			txtErrorMessage = new JTextPane();
			txtErrorMessage.setForeground(Color.RED);
			txtErrorMessage.setVisible(false);
			panelOrigFile.add(txtErrorMessage, "cell 0 0 3 1,growx,aligny top");
			{
				panelTreeTable = new JPanel();
				panelTreeTable.setBorder(new EmptyBorder(5, 5, 5, 5));
				horizSplitPane.setLeftComponent(panelTreeTable);
				panelTreeTable.setLayout(new BorderLayout(0, 0));
				{
					//btnMergeObjects = new JButton("Merge objects together");
					//panelTreeTable.add(btnMergeObjects, BorderLayout.SOUTH);
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
		setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
		setIconImage(Builder.getApplicationIcon());
		
		chooseOrCancelUIUpdate();
		enableEditing(false);
		updateConversionWarningsTable();
		
		
		initToolbar();
		
		
	}
	
	/**
	 * Set up the toolbar
	 */
	private void initToolbar()
	{
		JToolBar toolBar = new JToolBar("Toolbar");
		
		btnMergeObjects = new JButton("Merge all objects");
		btnMergeObjects.setToolTipText("Merge all objects");
		btnMergeObjects.setIcon(Builder.getIcon("merge.png", 22));
		toolBar.add(btnMergeObjects);
		
		btnExpandNodes = new JButton("Expand nodes");
		btnExpandNodes.setToolTipText("Expand nodes");
		btnExpandNodes.setIcon(Builder.getIcon("view_tree.png", 22));
		toolBar.add(btnExpandNodes);
		
		btnContractNodes = new JButton("Contract nodes");
		btnContractNodes.setToolTipText("Contract nodes");
		btnContractNodes.setIcon(Builder.getIcon("contract_tree.png", 22));
		toolBar.add(btnContractNodes);
		
		
		btnAssignByCode = new JButton("Assign by code");
		btnAssignByCode.setToolTipText("Assign by code");
		btnAssignByCode.setIcon(Builder.getIcon("barcode.png", 22));
		toolBar.add(btnAssignByCode);
		
		btnReparseFile = new JButton("Reparse file");
		btnReparseFile.setToolTipText("Reparse file");
		btnReparseFile.setIcon(Builder.getIcon("reload.png", 22));
		toolBar.add(btnReparseFile);
		
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
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
					updateOriginalFile(null);
				}
				else if (name.equals(ImportModel.CONVERSION_WARNINGS))
				{
					// New conversion warnings so set the table
					updateConversionWarningsTable();
				}
				else if (name.equals(ImportModel.SELECTED_ROW))
				{
					// An entity in the tree-table has been selected so 
					// update the metadata panel
														
					// Check that the tree table is expanded to show the selected node
					int selRow = treeTable.getSelectedRow();
					DefaultMutableTreeNode guiSelectedEntity = 
						(DefaultMutableTreeNode) treeTable.getValueAt(selRow, 0);
					DefaultMutableTreeNode modelSelectedEntity = model.getSelectedRow().getDefaultMutableTreeNode();
					if((guiSelectedEntity==null) || (!guiSelectedEntity.equals(modelSelectedEntity)))
					{
						TreePath path =  getPath(modelSelectedEntity);
						treeTable.getOutlineModel().getTreePathSupport().expandPath(path);
					}
					
					// Set up entity model listeners
					linkRowModel();
					
					// Update the metadata panel
					updateMetadataPanel();			
					updateEntityChooser();		
					
					// Enable/disable assign by code button depending on selected entity type
					
					if((modelSelectedEntity.getUserObject() instanceof TridasMeasurementSeries) || 
							(modelSelectedEntity.getUserObject() instanceof TridasSample))
					{
						btnAssignByCode.setEnabled(true);
					}
					else 
					{
						btnAssignByCode.setEnabled(false);
					}
					
					
				}
				else if (name.equals(ImportModel.TREE_MODEL))
				{
					// Tree model has changed so update the tree-table
					updateTreeTable();
				}
				else if (name.equals(ImportModel.INVALID_FILE_EXCEPTION))
				{
					setGUIForInvalidFile();
				}
				


				
			}


		});
	}
	
	private void linkRowModel()
	{
				
		model.getSelectedRow().addPropertyChangeListener(new PropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent evt) {
				String name = evt.getPropertyName();

				if(name.equals(TridasRepresentationTableTreeRow.CURRENT_ENTITY))
				{
					propertiesPanel.readFromObject(model.getSelectedRow().getCurrentEntity());
				}
				
			}
		});
	}
	
	/**
	 * Set up the MVC listeners for dispatching events from this GUI
	 */
	private void initListeners()
	{
		final Component glue = (Component) this;
		final JFrame glue2 = this;
		
		// Listen to entities being selected in tree table
		this.treeTable.addMouseListener(new PopupListener() {
						
			@Override
			public void mouseClicked(MouseEvent e) {		
				selectCurrentEntity();
			}
			
	       	@Override
			public void showPopup(MouseEvent e) 
			{
				log.debug("showPopup called");
				
				int selRow = treeTable.getSelectedRow();
				DefaultMutableTreeNode selectedEntity = (DefaultMutableTreeNode) treeTable.getValueAt(selRow, 0);	
				showPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), selectedEntity);
				
			}
		});

				
		// Listen for set from db button press
		this.btnFinish.addActionListener(new ActionListener() {
		
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Integer outstanding = model.getCountOutstandingEntities();
				String plural="y that has";
				if(outstanding>1)plural = "ies that have";
								
				if(outstanding>0)
				{
					Object[] options = {"Yes",
					                    "No",
					                    "Cancel"};
					int n = JOptionPane.showOptionDialog(glue,
					    "You still have "+outstanding+" entit"+plural+" not been imported.\n"					
					    + "Are you sure you want to close the import dialog?",
					    "Outstanding entities",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[2]);
					
					if(n==JOptionPane.YES_OPTION) dispose();

				}
				else
				{
					dispose();
				}
				
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
					TridasRepresentationTableTreeRow oldrow = model.getSelectedRow();	
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
			public void actionPerformed(ActionEvent e) {
				changingTop = false;
				chooseOrCancelUIUpdate();
			}
		});
		
		editEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(editEntity.isSelected())
				{
					log.debug("Starting to edit entity");
				}
				else
				{
					log.debug("Stopping entity edit");
				}

				enableEditing(editEntity.isSelected());	
			}			
		});
		
		editEntitySave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Need to replace 'null' below with this dialog
								
				saveChanges();
				
			}
		});

		editEntityCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isUserOkWithLoosingAnyChanges())
				{
					model.getSelectedRow().revertChanges();
					editEntity.setSelected(false);
					enableEditing(false);
					selectCurrentEntity();
				}
			}
		});
		
		btnMergeObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImportMergeEntitiesEvent event = new ImportMergeEntitiesEvent(model, TridasObject.class);
				event.dispatch();
			}		
		});
		
		btnExpandNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpandImportTreeEvent e2 = new ExpandImportTreeEvent(true, model.getTreeModel(), treeTable);
				e2.dispatch();
			}		
		});
		
		btnContractNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpandImportTreeEvent e2 = new ExpandImportTreeEvent(false, model.getTreeModel(), treeTable);
				e2.dispatch();
			}		
		});
		
		
		btnAssignByCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode modelSelectedEntity = model.getSelectedRow().getDefaultMutableTreeNode();
				updateHierarchyByCodeDialog(modelSelectedEntity, glue2);
			}		
		});
		
		btnReparseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//default icon, custom title
				int n = JOptionPane.showConfirmDialog(
				    glue2,
				    "Any changes you've not committed to the database will be lost.\n"+
				    "Are you sure you want to continue?",
				    "Confirm",
				    JOptionPane.YES_NO_OPTION);
				
				if(n==JOptionPane.YES_OPTION)
				{
				
					File file = null;
					String fileType = null;
					try{
					 file = model.getFileToImport();
					 fileType = model.getFileType();
					} catch (Exception e2)
					{
						Alert.error("Error", "Unable to reparse file: "+e2.getLocalizedMessage());
					}
					FileSelectedEvent event = new FileSelectedEvent(model, file, fileType);
					event.dispatch();
				}
				
			}		
		});

	}
	
	
	private void selectCurrentEntity()
	{
		// Check the user isn't part way through changing this entity
		if(!isUserOkWithLoosingAnyChanges()) return;
		
		if(changingTop)
		{
			changingTop =false;
			chooseOrCancelUIUpdate();
		}
		int selRow = treeTable.getSelectedRow();
		DefaultMutableTreeNode selectedEntity = (DefaultMutableTreeNode) treeTable.getValueAt(selRow, 0);	
		ImportStatus status = (ImportStatus) treeTable.getValueAt(selRow, 1);
		TridasRepresentationTableTreeRow row = new TridasRepresentationTableTreeRow(selectedEntity, status);		
        ImportNodeSelectedEvent event = new ImportNodeSelectedEvent(model, row);
        event.dispatch();
        model.setSelectedRow(row);
	}
	
	private void saveChanges()
	{
		ITridas entity = model.getSelectedRow().getCurrentEntity();
		propertiesPanel.writeToObject(entity);
		log.debug("About to save entity " + entity.getTitle());
		
		if(entity instanceof TridasMeasurementSeries)
		{
			TridasMeasurementSeries en = (TridasMeasurementSeries) entity;
			if(en.isSetValues())
			{
				if(en.getValues().get(0).isSetVariable())
				{
					TridasVariable variable = new TridasVariable();
					variable.setNormalTridas(NormalTridasVariable.RING_WIDTH);
					en.getValues().get(0).setVariable(variable);
				}
			}
			
			model.getSelectedRow().setCurrentEntity(en);
		}
		
		ImportEntitySaveEvent event = new ImportEntitySaveEvent(model, null);
		event.dispatch();
	}
	
	
	/**
	 * Localise the GUI components
	 */
	private void initLocale()
	{
		
	}
	
	protected void showPopupMenu(JComponent source, int x, int y, final DefaultMutableTreeNode selectedEntity)
	{
        // define the popup
        JPopupMenu popupMenu = new JPopupMenu();
        final JFrame glue = this;
        
        if(selectedEntity.getUserObject() instanceof TridasMeasurementSeries)
        {
	        JMenuItem setHierarchyMenu = new JMenuItem("Set hierarchy by labcode");
	        
	        setHierarchyMenu.addActionListener(new ActionListener(){
	        
				@Override
				public void actionPerformed(ActionEvent arg0) {

					updateHierarchyByCodeDialog(selectedEntity, glue);
					
				}
	        	
	        });
	        
	        popupMenu.add(setHierarchyMenu);
	        popupMenu.setOpaque(true);
	        popupMenu.setLightWeightPopupEnabled(false);

	        popupMenu.show(source, x, y);
        }
	}
	
	
 private void updateHierarchyByCodeDialog(DefaultMutableTreeNode selectedEntity, JFrame glue)
 {
	 
		ITridas newParent = null;
		if((selectedEntity.getUserObject() instanceof TridasMeasurementSeries))
		{
 
				newParent = TridasEntityPickerDialog.pickEntity(glue, 
						"Select entity", 
						ITridasSeries.class, 
						EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
				
				log.debug("User wants to set hierarchy of "+ 
						((ITridas) selectedEntity.getUserObject()).getTitle() +
						" using labcode for " + newParent.getTitle());
	 	}
		else if((selectedEntity.getUserObject() instanceof TridasSample))
		{
			 
			newParent = TridasEntityPickerDialog.pickSpecificEntity(this, 
					"Select entity", 
					TridasSample.class);
			
			log.debug("User wants to set hierarchy of "+ 
					((ITridas) selectedEntity.getUserObject()).getTitle() +
					" using labcode for " + newParent.getTitle());
		}
	 
	 	if(newParent==null) return;
	 
		ReplaceHierarchyEvent event = new ReplaceHierarchyEvent(model, glue, selectedEntity ,newParent );
		event.dispatch();
		
		ExpandImportTreeEvent e2 = new ExpandImportTreeEvent(true, model.getTreeModel(), treeTable);
		e2.dispatch();

		return;
 }
	
	
	/**
	 * Set up the conversion warnings table with the provided warnings
	 * @param warnings
	 */
	private void updateConversionWarningsTable()
	{
		ConversionWarning[] warnings = model.getConversionWarnings();
		if(warnings==null) 
		{
			vertSplitPane.setDividerLocation(1.0);
			return;
		}
		if(warnings.length==0) {
			vertSplitPane.setDividerLocation(1.0);
			return;
		}
		
		

		
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
		
		vertSplitPane.setDividerLocation(0.8);
	}
	
	
	/**
	 * Update the data panel to show the ring width values
	 * 
	 * @param row
	 */
	private void updateDataPanel() 
	{
				
		ITridas entity = (ITridas) model.getSelectedRow().getDefaultMutableTreeNode().getUserObject();
		
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
		
		log.debug("Values group count = "+series.getValues().size());
		log.debug("Value count = "+series.getValues().get(0).getValues().size());
		
		TridasVariable var = new TridasVariable();
		var.setNormalTridas(NormalTridasVariable.RING_WIDTH);
	
		
		TridasValues seriesGroup = series.getValues().get(0);
		seriesGroup.setVariable(var);
		

		List<TridasValue> values = seriesGroup.getValues();
		ArrayList<Number> valuesint = new ArrayList<Number>();
		ArrayList<Integer> countint = new ArrayList<Integer>();
		StringBuilder datavals = new StringBuilder();
		
		if(seriesGroup.isSetUnit())
		{
			if(seriesGroup.getUnit().isSetNormalTridas())
			{
				datavals.append("Units="+seriesGroup.getUnit().getNormalTridas().toString()+"\n\n");
			}
		}
		
		for(TridasValue v : values)
		{
			valuesint.add(Integer.valueOf(v.getValue()));
			countint.add(v.getCount());
			datavals.append(v.getValue()+"\n");
		}

		dataPane.setText(datavals.toString());

		//panelData.setLayout(new BorderLayout());
		//panelData.add(dataPane, BorderLayout.CENTER);
		tabbedPane.setEnabledAt(2, true);
	}
	
	/**
	 * Set the metadata panel to show the provided entity
	 * @param parentNode 
	 * @param entity
	 */
	private void updateMetadataPanel() {

		if(!isUserOkWithLoosingAnyChanges())
		{
			return;
		}
		
		// Enable or disable the data page depending on current entity type
		updateDataPanel();	
		
		// Set the text in the top bar
		//setTopBarSummaryTitle(parentNode);
		
		// Extract the entity and entity type from the select table tree row
		ITridas currentEntity = model.getSelectedRow().getCurrentEntity();
		Class<? extends ITridas> currentEntityType = model.getSelectedRow().getCurrentEntityClass();

		
		// Extract the parent entity 
		try{
			ITridas parentEntity = model.getSelectedRow().getParentEntity();
			log.debug("Set parent entity successfully to: " + parentEntity.getTitle());
		} catch (Exception e){
			log.warn("Unable to set parent entity");
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
		if(model.getSelectedRow().getImportStatus().equals(ImportStatus.PENDING))
		{
			propertiesTable.setHasWatermark(true);
			propertiesTable.setWatermarkText(I18n.getText("general.preview").toUpperCase());
		}
		else if(model.getSelectedRow().getImportStatus().equals(ImportStatus.IGNORE))
		{
			propertiesTable.setHasWatermark(true);
			propertiesTable.setWatermarkText("IGNORED");
		}
		else
		{
			propertiesTable.setHasWatermark(false);
		}
			
		// Update the combo box list
		//ImportEntityListChangedEvent ev = new ImportEntityListChangedEvent(model.getCurrentEntityModel(), currentEntity, parentEntity);
		//ev.dispatch();
		
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
	private void updateEntityChooser()
	{
		List<? extends ITridas> entities = model.getSelectedRow().getEntityList();
		topChooser.setList(entities);
		ITridas currentEntity = model.getSelectedRow().getCurrentEntity();
		ITridas parentEntity = model.getSelectedRow().getParentEntity();

		if (currentEntity instanceof TridasProject)
		{
			// This entity is a project so set list to null as 
			// projects aren't supported
			setTopChooserEnabled(false, "Projects are not supported in Tellervo");
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
			else
			{
				setTopChooserEnabled(false, "Fix parent entity first");
			}
		} catch (Exception e)
		{
			setTopChooserEnabled(false, "Fix parent entity first");
			return;
		}	
		
		this.topbar.repaint();
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
		lblTopBarSummaryTitle.setText(message);
	}
	
	
	/**
	 * Set the tree-table panel to show the data specified in the 
	 * provided treeModel.
	 * @param treeModel
	 */
	private void updateTreeTable() {
		
		TridasRepresentationTreeModel treeModel = model.getTreeModel();
		
		OutlineModel mdl = DefaultOutlineModel.createOutlineModel(
	    		treeModel, treeModel, true, "TRiDaS Entities");
		
		
		this.treeTable.setModel(mdl);
		ExpandImportTreeEvent e2 = new ExpandImportTreeEvent(true, model.getTreeModel(), treeTable);
		e2.dispatch();
	}
	
	
	/**
	 * Update the GUI to show the original legacy file.  If the highlightline 
	 * integer is included then this line will be highlighted for the user.
	 * Typically used to show an error.
	 * 
	 * @param highlightline
	 */
	private void updateOriginalFile(Integer highlightline)
	{
		final JEditorPane txtOriginalFile = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(txtOriginalFile);
		try{
		panelOrigFile.remove(3);} catch (Exception e){}
		panelOrigFile.add(scrollPane, "cell 0 2 3 1,grow");
		
		txtOriginalFile.setEditable(false);
		txtOriginalFile.setFont(new java.awt.Font("Courier", 0, 12));
		
		txtOriginalFile.setContentType("text/xml");

		// Original file has changed so update GUI to show its contents
		String contents = model.getFileToImportContents();
        if(contents!=null)
        {
        	txtOriginalFile.setText(contents);
        }
		
        if(highlightline!=null)
        {
			try{
				txtOriginalFile.addCaretListener(new LineHighlighter(Color.red, highlightline ));
				this.tabbedPane.setSelectedIndex(0);
				int pos=1;
				for(int i=0; i<highlightline; i++)
				{
					 pos = txtOriginalFile.getText().indexOf("\n", pos+1);
				}
				
				txtOriginalFile.setCaretPosition(pos);
			} catch (NumberFormatException ex)
			{}
        }
	}
	
	/**
	 * Set the GUI to illustrate an invalid file
	 * @param fileException
	 */
	private void setGUIForInvalidFile() {
		
		InvalidDendroFileException e = model.getFileException();
		
		if(e==null) return;
		
		String error = e.getLocalizedMessage();
		// Try and highlight the line in the input file that is to blame if poss
		if(	e.getPointerType().equals(PointerType.LINE) 
				&& e.getPointerNumber()!=null 
				&& !e.getPointerNumber().equals("0"))
		{
			Integer line = null;
			try{
			 line = Integer.parseInt(e.getPointerNumber());
			 error+="\n\n"+"The file is shown below with the problematic line highlighted.";
			} catch (NumberFormatException ex)
			{}
			updateOriginalFile(line);
			
			
			
		}
		
		tabbedPane.setEnabledAt(1, false);
		horizSplitPane.setDividerLocation(0.0);
		panelTreeTable.setVisible(false);
		panelWarnings.setVisible(false);

		
		txtErrorMessage.setVisible(true);
		txtErrorMessage.setText(error);
		pack();
		this.tabbedPane.setSelectedIndex(0);
		
	}	
	
	/** Scale an icon down to 20x20 */
	private ImageIcon scaleIcon20x20(ImageIcon icon) {
		return new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
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
		ITridas currentEntity = model.getSelectedRow().getCurrentEntity();
		
		try {
			if (currentEntity == null) {
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.isSetIdentifier())
			{
				log.debug("No identifier");
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.getIdentifier().isSetDomain())
			{
				log.debug("No domain");
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else if (!currentEntity.getIdentifier().getDomain().equals(App.domain))
			{
				log.debug("Different domain - this one is: "+currentEntity.getIdentifier().getDomain()+ " and not "+App.domain);
				topChooser.setSelectedItem(ImportEntityListComboBox.NEW_ITEM);
				return;
			}
			else
			{
				log.debug("Correct domain: "+currentEntity.getIdentifier().getDomain());
				
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
			{
				
				panel.handleComboSelection(true);
			}
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
	/*	Object obj = topChooser.getSelectedItem();
		
		//fixes bug where combobox is hidden in derived series
		if(obj == null){
			return;
		}
		else if(obj instanceof ITridas) 
		{
			
			temporarySelectingEntity = (ITridas) obj;
			
			if(propertiesTable.hasWatermark()){
					propertiesTable.setWatermarkText(I18n.getText("general.preview"));
			}				

			// start loading the list of children right away
			//if(loadChildren && currentMode != EditType.RADIUS)
			//	lists.prepareChildList(temporarySelectingEntity);	
			propertiesPanel.readFromObject(temporarySelectingEntity);	
		}
		
			*/
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
	@SuppressWarnings("unchecked")
	private TreePath getPath(TreeNode node) { 
		@SuppressWarnings("rawtypes")
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
		
		String entityname = "entity";
		try{
			entityname = TridasTreeViewPanel.getFriendlyClassName(model.getSelectedRow().getCurrentEntityClass());
		} catch (Exception e){}
		
		editEntityText.setFont(editEntityText.getFont().deriveFont(Font.BOLD));
		editEntityText.setText(enabled ? I18n.getText("metadata.currentlyEditingThis") + " " + entityname
				: I18n.getText("metadata.clickLockToEdit") + " " +entityname);
				
		editEntity.setSelected(enabled);
		
		
		if(enabled) 
		{
			// Remove any 'preview' watermark
			propertiesTable.setHasWatermark(false);
		}


		// disable choosing other stuff while we're editing
		// but only if something else doesn't disable it first
		if(topChooser.isEnabled())
		{
			topChooser.setEnabled(!enabled);
			changeButton.setEnabled(!enabled);
		}
		
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

		if(changingTop|| editEntity.isSelected())
		{
			int ret = JOptionPane.showConfirmDialog(this, 
					I18n.getText("question.confirmChangeForm"), 
					I18n.getText("question.continue"), 
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if(ret==JOptionPane.YES_OPTION)
			{
				changingTop =false;
				enableEditing(false);
				chooseOrCancelUIUpdate();
			}
			
			
			return (ret == JOptionPane.YES_OPTION);
		}		
		else
		{
			enableEditing(false);
			return true;
		}
		
	}
	
	/**
	 * @return true if the user wants to lose the new selection, false otherwise
	 */
	@SuppressWarnings("unused")
	private boolean isUserOkWithLoosingSelection() {
		
		// nothing new has been selected, nothing to lose
		Class<? extends ITridas> currentEntityType = model.getSelectedRow().getCurrentEntityClass();
		
		if(!model.getSelectedRow().isDirty())
			return true;
		
		int ret = JOptionPane.showConfirmDialog(this, 
				I18n.getText("question.confirmChangeForm"), 
				I18n.getText("question.continue"), 
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		return (ret == JOptionPane.YES_OPTION);
	}

	

}
