package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilderFactory;

import net.miginfocom.swing.MigLayout;

import org.codehaus.plexus.util.FileUtils;
import org.jdesktop.swingx.JXList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tellervo.desktop.odk.fields.ODKFields;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ExtensionFileFilter;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.WSIOdkFormDefinitionResource;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIOdkFormDefinition;
import org.tridas.io.gui.view.JToolbarButton;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.SearchableUtils;

import edu.emory.mathcs.backport.java.util.Collections;

import javax.swing.JToolBar;



public class ODKFormDesignPanel extends JPanel implements ActionListener, Serializable{

	private static final long serialVersionUID = 1L;
	private JTextField txtFieldName;
	private JCheckBox chkRequired;
	private JTextArea txtDescription;
	private JXList lstAvailableFields;
	private ODKFieldListModel availableFieldsModel;
	//private ODKFieldListModel selectedFieldsModel;
	private ODKTreeModel selectedFieldsTreeModel;
	private JTree tree;
	private static final Logger log = LoggerFactory.getLogger(ODKFormDesignPanel.class);
	private JComboBox cboDefault;
    private CheckBoxList cbxlstChoices;
    final private JDialog parent;
    private JScrollPane choicesScrollPane;
    private ODKFieldInterface selectedField;
    private JTextField txtFormName;
    private JTextField txtDefault;
    private DefaultComboBoxModel defModel;
    private JLabel lblOptionsToInclude;
    private JButton btnAll;
    private JButton btnNone;
    private JLabel lblDefaultValue;
    private JComboBox cboFormType;
    private JCheckBox chkHideField;
    private boolean quietFieldChangeFlag = false;
    private JLabel lblIcon;
    private JScrollPane scrollPaneAvailable;
    private JScrollPane scrollPaneSelected;
    private JLabel lblCodename;
    private JCheckBox chkMakePublic;
    
    
	/**
	 * Create the panel.
	 */
	public ODKFormDesignPanel(JDialog theparent) {
		this.parent = theparent;

		initGUI();
	}
	
	private void initGUI()
	{
		setLayout(new MigLayout("", "[833px,grow]", "[22.00px][fill][525px,grow][44px]"));
		
		
		JMenuBar menuBar = new JMenuBar();
		this.add(menuBar, "cell 0 0,growx,aligny top");
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem miLoadFormDesign = new JMenuItem("Load Tellervo form design");
		miLoadFormDesign.setIcon(Builder.getIcon("fileopen.png", 22));
		miLoadFormDesign.setActionCommand("Load Form Design");
		miLoadFormDesign.addActionListener(this);
		mnFile.add(miLoadFormDesign);
		
		JMenuItem miSaveFormDesign = new JMenuItem("Save Tellervo form design");
		miSaveFormDesign.setActionCommand("Save Form Design");
		miSaveFormDesign.setIcon(Builder.getIcon("filesave.png", 22));
		miSaveFormDesign.addActionListener(this);
		mnFile.add(miSaveFormDesign);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmSaveAsOdk = new JMenuItem("Save as ODK XML file");
		mntmSaveAsOdk.setActionCommand("SaveODKFormFile");
		mntmSaveAsOdk.setIcon(Builder.getIcon("odk-logo.png", 22));
		mntmSaveAsOdk.addActionListener(this);
		mnFile.add(mntmSaveAsOdk);
		
		JMenuItem mntmUploadOdkForm = new JMenuItem("Upload ODK form to server");
		mntmUploadOdkForm.setActionCommand("UploadForm");
		mntmUploadOdkForm.setIcon(Builder.getIcon("uploadtoserver.png", 22));
		mntmUploadOdkForm.addActionListener(this);
		mnFile.add(mntmUploadOdkForm);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setActionCommand("Cancel");
		mntmClose.setIcon(Builder.getIcon("button_cancel.png", 22));
		mntmClose.addActionListener(this);
		mnFile.add(mntmClose);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		add(toolBar, "flowx,cell 0 1,growx");
		
		JToolbarButton btnUpload = new JToolbarButton("UploadForm", (ImageIcon) Builder.getIcon("uploadtoserver.png", 22));
		btnUpload.setToolTipText("Upload form to Tellervo server");
		btnUpload.addActionListener(this);
		toolBar.add(btnUpload);
		
		JToolbarButton btnAddUserDefined = new JToolbarButton("AddUserDefinedField", (ImageIcon) Builder.getIcon("add_user.png", 22));
		btnAddUserDefined.setToolTipText("Add user defined field");
		btnAddUserDefined.addActionListener(this);
		toolBar.add(btnAddUserDefined);
		
		JToolbarButton btnGroup = new JToolbarButton("Group",  (ImageIcon) Builder.getIcon("group.png", 22));
		btnGroup.setToolTipText("Group selected fields");
		btnGroup.addActionListener(this);
		toolBar.add(btnGroup);
		
		JToolbarButton btnRename = new JToolbarButton("Rename", (ImageIcon) Builder.getIcon("rename.png", 22));
		btnRename.setToolTipText("Rename selected group or field");
		btnRename.addActionListener(this);
		toolBar.add(btnRename);
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, "cell 0 2,grow");
		
		JPanel panelMain = new JPanel();
		splitPane.setLeftComponent(panelMain);
		panelMain.setLayout(new MigLayout("", "[][grow,fill]", "[][68.00][]"));
		
		JLabel lblFormName = new JLabel("Form name:");
		panelMain.add(lblFormName, "cell 0 0,alignx trailing");
		
		txtFormName = new JTextField();
		txtFormName.setText("My form name");
		panelMain.add(txtFormName, "flowx,cell 1 0,growx");
		txtFormName.setColumns(10);
		
		JLabel lblFormType = new JLabel("Build form for:");
		panelMain.add(lblFormType, "cell 0 1");
		
		cboFormType = new JComboBox();
		cboFormType.setModel(new DefaultComboBoxModel(new String[] {"Objects", "Elements and samples"}));
		cboFormType.setActionCommand("FormTypeChanged");
		cboFormType.addActionListener(this);
		panelMain.add(cboFormType, "flowx,cell 1 1");
		
		JPanel panelFieldPicker = new JPanel();
		panelMain.add(panelFieldPicker, "cell 0 2 2 1,grow");
		panelFieldPicker.setLayout(new MigLayout("", "[300px,grow,fill][][300px,grow,fill][]", "[][grow,top][]"));
		
		JLabel lblAvailableFields = new JLabel("Available fields:");
		panelFieldPicker.add(lblAvailableFields, "cell 0 0");
		
		JLabel lblSelectedFields = new JLabel("Selected fields:");
		panelFieldPicker.add(lblSelectedFields, "flowx,cell 2 0");
		
		scrollPaneAvailable = new JScrollPane();
		panelFieldPicker.add(scrollPaneAvailable, "cell 0 1,grow");
		
		lstAvailableFields = new JXList();
		lstAvailableFields.setCellRenderer(new ODKFieldRenderer());
		lstAvailableFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstAvailableFields.setAutoCreateRowSorter(true);
		availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
		lstAvailableFields.setModel(availableFieldsModel);

	
		scrollPaneAvailable.setViewportView(lstAvailableFields);
		
		JPanel panelAddRemove = new JPanel();
		panelFieldPicker.add(panelAddRemove, "cell 1 1,growx,aligny center");
		panelAddRemove.setLayout(new MigLayout("fill, debug, insets 0", "[fill]", "[25px][][][]"));
		
		JButton btnAddAll = new JButton("");
		btnAddAll.setMargin(new java.awt.Insets(1, 2, 1, 2));
		btnAddAll.setIcon(Builder.getIcon("2rightarrow.png", 16));
		btnAddAll.setActionCommand("AddAll");
		btnAddAll.addActionListener(this);
		panelAddRemove.add(btnAddAll, "cell 0 0,growx,aligny top");
		
		JButton btnAddOne = new JButton("");
		btnAddOne.setIcon(Builder.getIcon("1rightarrow.png", 16));
		btnAddOne.setActionCommand("AddOne");
		btnAddOne.addActionListener(this);
		panelAddRemove.add(btnAddOne, "cell 0 1");
		
		JButton btnRemoveOne = new JButton("");
		btnRemoveOne.setActionCommand("RemoveOne");
		btnRemoveOne.addActionListener(this);
		btnRemoveOne.setIcon(Builder.getIcon("1leftarrow.png", 16));
		panelAddRemove.add(btnRemoveOne, "cell 0 2");
		
		JButton btnRemoveAll = new JButton("");
		btnRemoveAll.setActionCommand("RemoveAll");
		btnRemoveAll.addActionListener(this);
		btnRemoveAll.setIcon(Builder.getIcon("2leftarrow.png", 16));
		panelAddRemove.add(btnRemoveAll, "cell 0 3");
		
		scrollPaneSelected = new JScrollPane();
		panelFieldPicker.add(scrollPaneSelected, "flowx,cell 2 1,grow");
		tree = new JTree();
		scrollPaneSelected.setViewportView(tree);
		tree.setCellRenderer(new ODKTreeCellRenderer());
		tree.setRootVisible(false);
		
		JPopupMenu menu = new JPopupMenu();
		JMenuItem miGroupFields = new JMenuItem("Group fields");
		miGroupFields.setIcon(Builder.getIcon("group.png", 22));
		miGroupFields.setActionCommand("Group");
		miGroupFields.addActionListener(this);
		
		JMenuItem miRemoveField = new JMenuItem("Remove");
		miRemoveField.setActionCommand("RemoveOne");
		miRemoveField.addActionListener(this);
		
		JMenuItem miRemoveAllField = new JMenuItem("Remove all fields");
		miRemoveAllField.setActionCommand("RemoveAll");
		miRemoveAllField.addActionListener(this);
		
		JMenuItem miRename = new JMenuItem("Rename");
		miRename.setIcon(Builder.getIcon("rename.png", 22));
		miRename.setActionCommand("Rename");
		miRename.addActionListener(this);
		
		
		menu.add(miGroupFields);
		menu.add(miRemoveField);
		menu.add(miRemoveAllField);
		menu.add(miRename);

		
		tree.setComponentPopupMenu(menu);
		
		
		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode();
		selectedFieldsTreeModel = new ODKTreeModel(treeRoot, TridasObject.class);
		selectedFieldsTreeModel.addTreeModelListener(new ODKTreeModelListener(tree));
		
		JPanel panel_3 = new JPanel();
		panelFieldPicker.add(panel_3, "cell 3 1,grow");
		panel_3.setLayout(new MigLayout("fill, insets 0", "[]", "[top][top][grow]"));
		
		JButton btnUp = new JButton("");
		btnUp.setIcon(Builder.getIcon("1uparrow.png", 16));
		btnUp.setActionCommand("MoveUp");
		btnUp.addActionListener(this);
		panel_3.add(btnUp, "cell 0 0,alignx left,aligny top");
		
		JButton btnDown = new JButton("");
		btnDown.setIcon(Builder.getIcon("1downarrow.png", 16));
		btnDown.setActionCommand("MoveDown");
		btnDown.addActionListener(this);
		panel_3.add(btnDown, "cell 0 1,alignx left,aligny top");
		
		chkMakePublic = new JCheckBox("Make available to other database users");
		panelMain.add(chkMakePublic, "cell 1 1,growx");
		
		JScrollPane fieldOptionsScrollPane = new JScrollPane();
		fieldOptionsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel panelFieldOptions = new JPanel();
		fieldOptionsScrollPane.setViewportView(panelFieldOptions);
		panelFieldOptions.setBorder(new TitledBorder(UIManager.getBorder("EditorPane.border"), "Field details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(fieldOptionsScrollPane);
		panelFieldOptions.setLayout(new MigLayout("hidemode 2", "[59.00][81.00:81.00:81.00,right][239.00,grow][50px:50px:50px]", "[][][][][59.00:59.00:59.00][center][grow]"));
		
		lblCodename = new JLabel("");
		lblCodename.setForeground(Color.GRAY);
		lblCodename.setFont(new Font("Dialog", Font.BOLD, 8));
		panelFieldOptions.add(lblCodename, "cell 2 0,alignx right");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelFieldOptions.add(panel_2, "cell 0 1 1 3,alignx center,growy");
		panel_2.setLayout(new MigLayout("", "[][70px:70px:70px]", "[][][]"));
		
		lblIcon = new JLabel("");
		panel_2.add(lblIcon, "cell 0 0 2 3,alignx center,aligny center");
		lblIcon.setIcon(Builder.getIcon("letters.png", 64));
		
		JLabel lblFieldNameDisplayed = new JLabel("Name:");
		panelFieldOptions.add(lblFieldNameDisplayed, "cell 1 1,alignx trailing");
		
		txtFieldName = new JTextField();
		txtFieldName.setActionCommand("FieldNameChanged");
		//txtFieldName.addActionListener(this);
		
		
		
		panelFieldOptions.add(txtFieldName, "cell 2 1 2 1,growx");
		txtFieldName.setColumns(10);
		
		chkRequired = new JCheckBox("Required");
		chkRequired.setEnabled(false);
		panelFieldOptions.add(chkRequired, "cell 2 2");
		
		chkHideField = new JCheckBox("Hide field from user (default value required if ticked)");
		
		panelFieldOptions.add(chkHideField, "cell 2 3,aligny top");
		
		JLabel lblDescription = new JLabel("Description:");
		panelFieldOptions.add(lblDescription, "cell 0 4 2 1,alignx right,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelFieldOptions.add(scrollPane, "cell 2 4 2 1,grow");
		
		txtDescription = new JTextArea();
		scrollPane.setViewportView(txtDescription);
		txtDescription.setLineWrap(true);
		
		txtDescription.setWrapStyleWord(true);
						
		lblDefaultValue = new JLabel("Default value:");
		panelFieldOptions.add(lblDefaultValue, "cell 0 5 2 1,alignx trailing,aligny center");
		defModel = new DefaultComboBoxModel();
		
		JPanel panel_1 = new JPanel();
		panelFieldOptions.add(panel_1, "cell 2 5 2 1,grow");
		panel_1.setLayout(new MigLayout("hidemode 3, insets 0", "[grow,fill]", "[center][center]"));
		
		txtDefault = new JTextField();
		
		
		panel_1.add(txtDefault, "cell 0 0,growx,aligny top");
		txtDefault.setColumns(10);
		
		cboDefault = new JComboBox();
		
		panel_1.add(cboDefault, "flowy,cell 0 1,growx");
		cboDefault.setVisible(false);
		//cboDefault.addActionListener(this);
		cboDefault.setActionCommand("DefaultChosen");
		
		lblOptionsToInclude = new JLabel("Options to include:");

		panelFieldOptions.add(lblOptionsToInclude, "cell 0 6 2 1,alignx right,aligny top");
		
		btnAll = new JButton("");
		btnAll.setIcon(Builder.getIcon("selectall.png", 16));
		btnAll.setActionCommand("SelectAllChoices");
		btnAll.addActionListener(this);
		
		choicesScrollPane = new JScrollPane();
		panelFieldOptions.add(choicesScrollPane, "cell 2 6,grow");
		choicesScrollPane.setOpaque(false);
		
		cbxlstChoices = new CheckBoxList();
		
				choicesScrollPane.setViewportView(cbxlstChoices);
		panelFieldOptions.add(btnAll, "flowy,cell 3 6,aligny top");
		
		btnNone = new JButton("");
		btnNone.setActionCommand("SelectNoChoices");
		btnNone.addActionListener(this);
		btnNone.setIcon(Builder.getIcon("selectnone.png", 16));

		panelFieldOptions.add(btnNone, "cell 3 6");
		
		JPanel panel = new JPanel();
		add(panel, "cell 0 3,growx,aligny top");
		panel.setLayout(new MigLayout("", "[][][grow][][73px]", "[25px]"));
		
		JButton btnUploadForm = new JButton("Upload Form");
		btnUploadForm.setActionCommand("UploadForm");
		btnUploadForm.setIcon(Builder.getIcon("uploadtoserver.png", 22));
		btnUploadForm.addActionListener(this);
		panel.add(btnUploadForm, "cell 3 0");
		
		JButton btnCancel = new JButton("Close");
		btnCancel.setActionCommand("Cancel");
		btnCancel.setIcon(Builder.getIcon("button_cancel.png", 22));
		btnCancel.addActionListener(this);
		panel.add(btnCancel, "cell 4 0,alignx left,aligny top");
		
		
		initListeners();
		
		// Make sure all mandatory fields are selected
		this.addAllFields();
		this.removeAllNonMandatoryFields();
		setChoiceGUIVisible(false);

		this.formTypeChanged();
		
	}
	
	
	private void uploadForm()
	{
		
		String contents = ODKFormGenerator.generate(txtFormName.getText(), selectedFieldsTreeModel);
		log.debug("Form contents");
		log.debug(contents);
		
		WSIOdkFormDefinition odkform = new WSIOdkFormDefinition();
		
		odkform.setName(txtFormName.getText());
		

		
		org.w3c.dom.Element formdef = null;
		try {
			formdef = DocumentBuilderFactory
				    .newInstance()
				    .newDocumentBuilder()
				    .parse(new ByteArrayInputStream(contents.getBytes()))
				    .getDocumentElement();
		} catch (Exception e) {
			log.error("Error creating ODK form definition.");
		}
		
		odkform.setAny(formdef);
			
		// Create resource
		WSIOdkFormDefinitionResource resource = new WSIOdkFormDefinitionResource(odkform, TellervoRequestType.CREATE);

		// set up a dialog...
		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(this.parent, resource);

		resource.query();
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			Alert.error("Error", dialog.getFailException().getMessage());
			return;
		}
		
		
	}
	
	private void loadFormDefinition() throws ClassNotFoundException, IOException
	{
		 
		
	/*	String lastVisitedFolder = App.prefs.getPref(PrefKey.FOLDER_LAST_SAVE, null);

		final JFileChooser fc = new JFileChooser(lastVisitedFolder);

		fc.setFileFilter(new ExtensionFileFilter("Tellervo ODK Form Definition (*.odkform)", new String[] { "odkform"}));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		
		int response = fc.showOpenDialog(parent);
		
		if(response==JFileChooser.CANCEL_OPTION) return;
		
		File file = fc.getSelectedFile();
		
		// Read from disk using FileInputStream
		
		FileInputStream f_in;

		f_in = new 
			FileInputStream(file);
		
		// Read object using ObjectInputStream
		ObjectInputStream obj_in = 
			new ObjectInputStream (f_in);

		// Read an object
		Object obj = obj_in.readObject();

		try{
		if (obj instanceof ODKSerializedForm)
		{
			ODKTreeModel model = ((ODKSerializedForm) obj).getTreeModel();
			String formTitle = ((ODKSerializedForm) obj).getFormTitle();
			
			if(formTitle!=null) txtFormName.setText(formTitle);
						
			if(model.getClassType().equals(TridasObject.class)) {
				cboFormType.setSelectedIndex(0);
				availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
			}
			else
			{
				cboFormType.setSelectedIndex(1);
				ArrayList<ODKFieldInterface> fields = ODKFields.getFields(TridasElement.class);
				fields.addAll(ODKFields.getFields(TridasSample.class));
				fields.addAll(ODKFields.getFields(TridasRadius.class));
				availableFieldsModel = new ODKFieldListModel(fields);
			}
						
			selectedFieldsTreeModel = (ODKTreeModel) model;
			tree.setModel(selectedFieldsTreeModel);
								
			CopyOnWriteArrayList<ODKFieldInterface> newavail = new CopyOnWriteArrayList<ODKFieldInterface>();
			for(ODKFieldInterface field : availableFieldsModel.getAllFields())
			{
				newavail.add(field);
			}
						
			for(ODKFieldInterface selectedfield : selectedFieldsModel.getAllFields())
			{
				for(ODKFieldInterface availablefield : newavail)
				{
					if(selectedfield.getFieldCode().equals(availablefield.getFieldCode()))
					{
						newavail.remove(availablefield);
					}
				}
				
			}
			
			availableFieldsModel =  new ODKFieldListModel();
			
			log.debug("Number of available fields left="+newavail.size());
			availableFieldsModel.addAllFields(newavail);
			this.lstAvailableFields.setModel(availableFieldsModel);
		}
		
		} finally{
			obj_in.close();	
		}
*/
	}
	
	public boolean saveFormDefinition() throws IOException
	{
		
		File file = getOutputFile(new ExtensionFileFilter("Tellervo ODK Form Definition (*.odkform)", new String[] { "odkform"}), this);
    	
    	if(file!=null) 
    	{
			
			// Write to disk with FileOutputStream
			FileOutputStream f_out;
		
			f_out = new 
				FileOutputStream(file);
			
			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new
				ObjectOutputStream (f_out);
		
			// Write object out to disk
			obj_out.writeObject ( new ODKSerializedForm(selectedFieldsTreeModel, txtFormName.getText()) );
		
			obj_out.close();
			
			return true;
    	}


    	return false;

	}
	
	private void initListeners()
	{
		cboDefault.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				commitFieldChanges();
				
			}
			
		});
		
		txtDefault.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}

			@Override
			public void insertUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}

			@Override
			public void removeUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}
			
		});
		
		txtDescription.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}

			@Override
			public void insertUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}

			@Override
			public void removeUpdate(DocumentEvent evt) {
				commitFieldChanges();
				
			}
			
		});
		
		chkHideField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				commitFieldChanges();
				
			}
			
		});
		
		txtFieldName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
								
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				fieldNameChanged();
				
			}
			
		});
				
		lstAvailableFields.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				
				/*if(evt.getValueIsAdjusting()) return;
								
				selectedField = (AbstractODKField) lstAvailableFields.getSelectedValue();
				setDetailsPanel();*/
			}
			
		});
		
		this.tree.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.isPopupTrigger())
				{
					
				}
				else
				{
					
					AbstractODKTreeNode node = (AbstractODKTreeNode) tree.getLastSelectedPathComponent();
					if(node instanceof ODKFieldNode)
					{
						selectedField = (ODKFieldInterface) ((ODKFieldNode)node).getUserObject();
						setDetailsPanel();
					}
					
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {				
			}

			@Override
			public void mouseReleased(MouseEvent e) {				
			}

			@Override
			public void mouseEntered(MouseEvent e) {				
			}

			@Override
			public void mouseExited(MouseEvent e) {				
			}
			
		});
		
		lstAvailableFields.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				
				if(evt.isPopupTrigger())
				{
					
				}
				else
				{
					if(evt.getClickCount()>1)
					{
						addOneField();
						//lstSelectedFields.setSelectedIndex(lstSelectedFields.getModel().getSize()-1);
						//selectedField = (AbstractODKField) lstSelectedFields.getSelectedValue();
						setDetailsPanel();
						JScrollBar vertical = scrollPaneSelected.getVerticalScrollBar();
						vertical.setValue( vertical.getMaximum() );
					}
					else
					{
						selectedField = (AbstractODKField) lstAvailableFields.getSelectedValue();
						setDetailsPanel();
					}
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) { }

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) { }

			@Override
			public void mouseReleased(MouseEvent arg0) { }
			
		});
		
	}
	
	private void commitFieldChanges()
	{
		log.debug("commitFieldChanges called");
		if(selectedField==null) {
			log.debug("selectedField is null so not committing changes");
			return;
		}

		if(quietFieldChangeFlag==true) {
			log.debug("quietFieldChangeFlag is on so not committing changes");

			return;
		}
		
		
		selectedField.setDescription(this.txtDescription.getText());
		
		
		log.debug("Is txtDefault visible? "+txtDefault.isVisible());
		log.debug("Is cboDefault visible? "+cboDefault.isVisible());

		if(this.txtDefault.isVisible())
		{
			selectedField.setDefaultValue(txtDefault.getText());
		}
		else if (this.cboDefault.isVisible())
		{
			selectedField.setDefaultValue(cboDefault.getSelectedItem());
		}
		
		selectedField.setIsFieldHidden(this.chkHideField.isSelected());
		
		this.tree.repaint();
		this.lstAvailableFields.repaint();
	}
	
	private void setDetailsPanel()
	{
		quietFieldChangeFlag = true;

		// Handle null fields first
		if(selectedField==null)
		{
			this.txtFieldName.setText("");
			this.txtDescription.setText("");
			this.chkRequired.setSelected(false);
			this.txtDefault.setText("");
			this.chkHideField.setSelected(false);
			this.cboDefault.setSelectedIndex(-1);
			this.lblIcon.setIcon(Builder.getIcon("letters.png", 64));
			quietFieldChangeFlag = false;
			
			this.txtFieldName.setEnabled(false);
			this.txtDescription.setEnabled(false);
			this.txtDefault.setEnabled(false);
			this.chkHideField.setEnabled(false);
			this.cboDefault.setEnabled(false);
			this.lblIcon.setEnabled(false);
			this.cbxlstChoices.setEnabled(false);
			this.btnAll.setEnabled(false);
			this.btnNone.setEnabled(false);
			lblCodename.setText("");
			return;
		}
		else
		{
			this.txtFieldName.setEnabled(true);
			this.txtDescription.setEnabled(true);
			this.txtDefault.setEnabled(true);
			this.chkHideField.setEnabled(true);
			this.cboDefault.setEnabled(true);
			this.lblIcon.setEnabled(true);
			this.cbxlstChoices.setEnabled(true);
			this.btnAll.setEnabled(true);
			this.btnNone.setEnabled(true);
		}
		
		log.debug("Field is hidden status: "+selectedField.isFieldHidden());
		
		lblCodename.setText(selectedField.getFieldCode());
		
		// Handle fields that are the same regardless of data type
		this.txtFieldName.setText(selectedField.getFieldName());
		this.txtDescription.setText(selectedField.getFieldDescription());
		this.chkRequired.setSelected(selectedField.isFieldRequired());
		this.chkHideField.setSelected(selectedField.isFieldHidden());		

		if(selectedField.getFieldType().equals(ODKDataType.SELECT_ONE))
		{
			lblIcon.setIcon(Builder.getIcon("list.png", 64));
			ArrayList<SelectableChoice> choices = ((AbstractODKChoiceField)selectedField).getAvailableChoices();
			this.cbxlstChoices = new CheckBoxList(choices.toArray(new SelectableChoice[choices.size()]));
			this.choicesScrollPane.setViewportView(cbxlstChoices);
			cbxlstChoices.setCheckBoxListSelectedIndices(((AbstractODKChoiceField)selectedField).getSelectedChoicesIndices());
			
			cbxlstChoices.getCheckBoxListSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	        SearchableUtils.installSearchable(cbxlstChoices);
	        cbxlstChoices.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                if (!e.getValueIsAdjusting()) {
	                                 	
	                	if(selectedField instanceof AbstractODKChoiceField)
	                	{
	                		((AbstractODKChoiceField)selectedField).setSelectedChoices(cbxlstChoices.getCheckBoxListSelectedValues());
	                		updateDefaultCombo();
	                	}
	                    
	                }
	            }
	        });
			
			cbxlstChoices.repaint();
			choicesScrollPane.revalidate();

			updateDefaultCombo();

			cboDefault.setVisible(true);
			lblDefaultValue.setVisible(true);
			txtDefault.setVisible(false);

			setChoiceGUIVisible(true);	
		}
		else if(selectedField.getFieldType().equals(ODKDataType.STRING))
		{
			lblIcon.setIcon(Builder.getIcon("letters.png", 64));
			
			if(selectedField.getDefaultValue()!=null)
			{
				this.txtDefault.setText(selectedField.getDefaultValue().toString());
			}
			else
			{
				this.txtDefault.setText("");
			}

			cboDefault.setVisible(false);
			txtDefault.setVisible(true);
			lblDefaultValue.setVisible(true);

			setChoiceGUIVisible(false);
		}
		else if(selectedField.getFieldType().equals(ODKDataType.IMAGE) || 
				selectedField.getFieldType().equals(ODKDataType.AUDIO) ||
				selectedField.getFieldType().equals(ODKDataType.VIDEO) ||
				selectedField.getFieldType().equals(ODKDataType.LOCATION))
		{
			if(selectedField.getFieldType().equals(ODKDataType.IMAGE)) lblIcon.setIcon(Builder.getIcon("photo.png", 64));
			if(selectedField.getFieldType().equals(ODKDataType.AUDIO)) lblIcon.setIcon(Builder.getIcon("sound.png", 64));
			if(selectedField.getFieldType().equals(ODKDataType.VIDEO)) lblIcon.setIcon(Builder.getIcon("movie.png", 64));
			if(selectedField.getFieldType().equals(ODKDataType.LOCATION)) lblIcon.setIcon(Builder.getIcon("pin.png", 64));

			txtDefault.setVisible(false);
			cboDefault.setVisible(false);
			lblDefaultValue.setVisible(false);
			setChoiceGUIVisible(false);
		}
		else if(selectedField.getFieldType().equals(ODKDataType.INTEGER) || 
				selectedField.getFieldType().equals(ODKDataType.DECIMAL))
		{
			lblIcon.setIcon(Builder.getIcon("numbers.png", 64));
			txtDefault.setVisible(false);
			cboDefault.setVisible(false);
			lblDefaultValue.setVisible(false);
			setChoiceGUIVisible(false);
		}
		else
		{
			log.error("Fields of data type "+selectedField.getFieldType()+" are not yet supported");
			setChoiceGUIVisible(false);

		}
			
		quietFieldChangeFlag = false;

	}
	
	private void updateDefaultCombo()
	{
		if(selectedField.getFieldType().equals(ODKDataType.SELECT_ONE) || 
				selectedField.getFieldType().equals(ODKDataType.SELECT_MANY))
		{
			AbstractODKChoiceField field = (AbstractODKChoiceField)selectedField;
			defModel.removeAllElements();
			for(SelectableChoice choice : field.getSelectedChoices())
			{
				defModel.addElement(choice);
			}
			cboDefault.setModel(defModel);
			
			if(field.getDefaultValue()!=null)
			{
				cboDefault.setSelectedItem(field.getDefaultValue());
			}
			else
			{
				cboDefault.setSelectedIndex(-1);
			}
		}

	}
	
	/**
	 * Show or hide all the choice gui components 
	 * 
	 * @param b
	 */
	private void setChoiceGUIVisible(boolean b)
	{
		this.choicesScrollPane.setVisible(b);
		this.lblOptionsToInclude.setVisible(b);
		this.btnAll.setVisible(b);
		this.btnNone.setVisible(b);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {

		App.init();

		JDialog dialog = new JDialog();
		ODKFormDesignPanel panel = new ODKFormDesignPanel(dialog);

		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.pack();
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setTitle("ODK Form Builder");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

	}
	
	private void addAllFields()
	{
		ArrayList<ODKFieldInterface> fields =  (ArrayList<ODKFieldInterface>) availableFieldsModel.getAllFields();
		if(fields==null || fields.size()==0) return;	
		
		
		Collections.reverse(fields);
		
		for(ODKFieldInterface field : fields)
		{
			log.debug("Adding field "+field.getFieldName()+" to tree");
			//selectedFieldsTreeModel.addNodeToRoot(field);
			
			AbstractODKTreeNode newChild = new ODKFieldNode(field);
			selectedFieldsTreeModel.insertNodeInto(newChild, (MutableTreeNode) selectedFieldsTreeModel.getRoot(), 0);
		}
		
		this.selectedFieldsTreeModel.reload();
		availableFieldsModel.removeFields(fields);
		
		expandTree();
		
	}
	
	private void fieldNameChanged()
	{
		if(selectedField==null) return;
		selectedField.setName(this.txtFieldName.getText());
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent(); 
		
		//TODO
		/*for(ODKFieldInterface field : this.selectedFieldsModel.getAllFields())
		{
			if(selectedField.getFieldCode().equals(field.getFieldCode()))
			{
				field.setName(this.txtFieldName.getText());
				this.lstSelectedFields.repaint();
			}
		}*/
		
		
	}
		
	private void removeAllNonMandatoryFields()
	{		
		recurseRemoveNonMandatoryNodes((DefaultMutableTreeNode) this.selectedFieldsTreeModel.getRoot());	
	}
	
	private void recurseRemoveNonMandatoryNodes(DefaultMutableTreeNode node)
	{
		log.debug("The node '"+node.toString()+"' has "+node.getChildCount()+" children");
		
		Enumeration en = node.depthFirstEnumeration();
		
		while(en.hasMoreElements())
		{
			DefaultMutableTreeNode childnode = (DefaultMutableTreeNode) en.nextElement();
					
			if(childnode.getUserObject() instanceof ODKFieldInterface)
			{
				ODKFieldInterface obj = (ODKFieldInterface) childnode.getUserObject();
				if(obj.isFieldRequired()) {
					log.debug(obj.getFieldName() + " is mandatory so leaving in tree");
					continue;
				}
				else
				{
					log.debug(obj.getFieldName() + " is optional so deleting from tree");
					
					try{
						ODKFieldInterface field = (ODKFieldInterface) childnode.getUserObject();
						availableFieldsModel.addField(field);
					} catch (Exception e)
					{
						
					}
					this.selectedFieldsTreeModel.removeNodeFromParent(childnode);
					recurseRemoveNonMandatoryNodes(node);
				}
			}
			else if (childnode instanceof ODKBranchNode && childnode.getParent()!=null)
			{				
				if(((ODKBranchNode)childnode).isDeletable()) this.selectedFieldsTreeModel.removeNodeFromParent(childnode);
			}
			else{
				log.debug("Object is not ODKFieldInterface");
			}
		}
	}
	
	private void addOneField()
	{
		AbstractODKField field =  (AbstractODKField) lstAvailableFields.getSelectedValue();
		if(field==null) return;
		availableFieldsModel.removeField(field);

		selectedFieldsTreeModel.addFieldAsNodeToRoot(field);		
		this.selectedFieldsTreeModel.reload();		
		expandTree();
	}
	
	private void removeOneField()
	{
		AbstractODKField field;
		
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
            	
            	if(currentNode.getUserObject() instanceof ODKFieldInterface)
            	{
            		field = (AbstractODKField)currentNode.getUserObject();
            		
            		if(!field.isFieldRequired())
            		{           			
                        this.selectedFieldsTreeModel.removeNodeFromParent(currentNode);
            			availableFieldsModel.addField(field);		
                    }
            	}
            	else if (currentNode instanceof ODKBranchNode)
            	{
            		if(!((ODKBranchNode) currentNode).isDeletable()) {
            			Alert.error("Error", "This group is not deleteable");
            			return;
            		}
            		
            		ArrayList<ODKFieldNode> childnodes = new ArrayList<ODKFieldNode>();
            		
            		for(int i=0; i<currentNode.getChildCount(); i++)
            		{
            			childnodes.add((ODKFieldNode) currentNode.getChildAt(i));
            		}
            		
            		for(ODKFieldNode node : childnodes)
            		{
            			ODKFieldInterface f = (ODKFieldInterface) node.getUserObject();
            			if(f.isFieldRequired())
            			{
            				selectedFieldsTreeModel.addNodeToRoot(node);
            			}
            			else 
            			{
            				availableFieldsModel.addField(f);
            			}
            		}

            		this.selectedFieldsTreeModel.removeNodeFromParent(currentNode);
              	}
            }
        } 
		
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("AddOne"))
		{
			addOneField();
		}
		else if(evt.getActionCommand().equals("AddAll"))
		{
			addAllFields();
		}
		else if(evt.getActionCommand().equals("RemoveOne"))
		{
			removeOneField();
		}
		else if(evt.getActionCommand().equals("RemoveAll"))
		{
			removeAllNonMandatoryFields();
		}
		else if(evt.getActionCommand().equals("FieldNameChanged"))
		{
			fieldNameChanged();
		}
		else if(evt.getActionCommand().equals("SaveODKFormFile"))
		{
			doSave();
		}
		else if(evt.getActionCommand().equals("MoveUp"))
		{
			moveUp();
		}
		else if(evt.getActionCommand().equals("MoveDown"))
		{
			moveDown();
		}
		else if(evt.getActionCommand().equals("Rename"))
		{
			renameField();
		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			parent.setVisible(false);
		}
		else if(evt.getActionCommand().equals("SelectAllChoices"))
		{
			cbxlstChoices.selectAll();
		}
		else if(evt.getActionCommand().equals("SelectNoChoices"))
		{
			cbxlstChoices.selectNone();
		}
		else if (evt.getActionCommand().equals("AddUserDefinedField"))
		{
			addUserDefinedField();
		}
		else if (evt.getActionCommand().equals("Group"))
		{
			groupFields();
		}
		else if (evt.getActionCommand().equals("DefaultChosen"))
		{
			Object item = cboDefault.getSelectedItem();
			if(item!=null) selectedField.setDefaultValue(item);
		}
		else if (evt.getActionCommand().equals("UploadForm"))
		{
			uploadForm();
		}
		else if (evt.getActionCommand().equals("Save Form Design"))
		{
			try {
				saveFormDefinition();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (evt.getActionCommand().equals("Load Form Design"))
		{
			try {
				loadFormDefinition();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else if (evt.getActionCommand().equals("FormTypeChanged"))
		{
			formTypeChanged();
		}
	}
	
	private void formTypeChanged()
	{
		if(this.cboFormType.getSelectedIndex()==0)
		{
			log.debug("Setting gui for object form creation");
			
			((DefaultMutableTreeNode)this.selectedFieldsTreeModel.getRoot()).removeAllChildren();
			this.selectedFieldsTreeModel = new ODKTreeModel(new DefaultMutableTreeNode(), TridasObject.class);
			tree.setModel(selectedFieldsTreeModel);
			selectedFieldsTreeModel.addTreeModelListener(new ODKTreeModelListener(tree));

			
			availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
			this.lstAvailableFields.setModel(availableFieldsModel);
			this.addAllFields();
			this.removeAllNonMandatoryFields();
		}
		else
		{
			log.debug("Setting gui for element form creation");
			((DefaultMutableTreeNode)this.selectedFieldsTreeModel.getRoot()).removeAllChildren();
			this.selectedFieldsTreeModel = new ODKTreeModel(new DefaultMutableTreeNode(), TridasSample.class);
			tree.setModel(selectedFieldsTreeModel);
			selectedFieldsTreeModel.addTreeModelListener(new ODKTreeModelListener(tree));

			
			ArrayList<ODKFieldInterface> fields = ODKFields.getFields(TridasElement.class);
			fields.addAll(ODKFields.getFields(TridasSample.class));
			fields.addAll(ODKFields.getFields(TridasRadius.class));
			availableFieldsModel = new ODKFieldListModel(fields);
			this.lstAvailableFields.setModel(availableFieldsModel);

			this.addAllFields();
			this.removeAllNonMandatoryFields();
		}
	}
	
    private void doSave()
    {
    	
    	File file = getOutputFile(new ExtensionFileFilter("Open Data Kit form definition (*.xml)", new String[] { "xml"}), this);
    	
    	if(file!=null) 
    	{
    		// Make sure we have a .xml extension
    		if(!FileUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("xml"))
    		{
    			file = new File(file.getAbsoluteFile()+".xml");
    		}
    		
			ODKFormGenerator.generate(file, txtFormName.getText(), selectedFieldsTreeModel);

    	}
    	
    }
	
	private void addUserDefinedField()
	{
		boolean isObjectField = cboFormType.getSelectedIndex()==0;
		
		ODKUserDefinedFieldDialog dialog = new ODKUserDefinedFieldDialog(isObjectField, parent);
		dialog.setVisible(true);
		
		if(dialog.success)
		{
			selectedFieldsTreeModel.addFieldAsNodeToRoot(dialog.getField());
		}
		else
		{
			return;
		}
	}
	
	
	/**
	 * Prompt the user for an output filename
	 * 
	 * @param filter
	 * @return
	 */
	public static File getOutputFile(FileFilter filter, Component parent)
	{
		String lastVisitedFolder = App.prefs.getPref(PrefKey.FOLDER_LAST_SAVE, null);
		File outputFile;
		
		//Create a file chooser
		final JFileChooser fc = new JFileChooser(lastVisitedFolder);
		
		
		fc.setAcceptAllFileFilterUsed(false);

		if(filter !=null)
		{
			fc.addChoosableFileFilter(filter);
			fc.setFileFilter(filter);
		}
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setDialogTitle("Save as...");
		
		//In response to a button click:
		int returnVal = fc.showSaveDialog(parent);
		
		if(returnVal==JFileChooser.APPROVE_OPTION)
		{
			outputFile = fc.getSelectedFile();
			
			if(FileUtils.getExtension(outputFile.getAbsolutePath())== "")
			{
				log.debug("Output file extension not set by user");

				if(fc.getFileFilter().getDescription().equals("Open Data Kit (ODK) form file (*.xml)"))
				{
					log.debug("Adding odk extension to output file name");
					outputFile = new File(outputFile.getAbsolutePath()+".xml");
				}
				else if (fc.getFileFilter().getDescription().equals("Tellervo ODK Form Definition (*.odkform)"))
				{
					log.debug("Adding odkform extension to output file name");
					outputFile = new File(outputFile.getAbsolutePath()+".odkform");
				}				
			}
			else
			{
				log.debug("Output file extension set by user to '"+FileUtils.getExtension(outputFile.getAbsolutePath())+"'");
			}
			
			
			App.prefs.setPref(PrefKey.FOLDER_LAST_SAVE, outputFile.getAbsolutePath());
		}
		else
		{
			return null;
		}
		
		if(outputFile.exists())
		{
			Object[] options = {"Overwrite",
			        "No", "Cancel"};
			int response = JOptionPane.showOptionDialog(parent,
			"The file '"+outputFile.getName()+"' already exists.  Are you sure you want to overwrite?",
			"Confirm",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,     //do not use a custom Icon
			options,  //the titles of buttons
			options[0]); //default button title
	
			if(response != JOptionPane.YES_OPTION)
			{
				return null;				
			}
		}
		
		
		return outputFile;
	}
	
	public static void showDialog(Component parent)
	{
		final JDialog dialog = new JDialog();
		final ODKFormDesignPanel panel = new ODKFormDesignPanel(dialog);
		
		dialog.setIconImage(Builder.getApplicationIcon());
		dialog.setTitle("ODK Form Builder");
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
			
		dialog.pack();
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		
		
	}
	
	private void moveUp()
	{
		this.selectedFieldsTreeModel.moveFieldUp((AbstractODKTreeNode) tree.getLastSelectedPathComponent());
		this.expandTree();
	}
	
	private void moveDown()
	{
		this.selectedFieldsTreeModel.moveFieldDown((AbstractODKTreeNode) tree.getLastSelectedPathComponent());
		this.expandTree();

	}
	
	private void expandTree(){

		for (int i = 0; i < tree.getRowCount(); i++) {
		    tree.expandRow(i);
		}
	}
	
	private void groupFields()
	{
		
		// Group
		TreePath[] selectionpaths = tree.getSelectionPaths();
		
		for(TreePath path : selectionpaths)
		{
			AbstractODKTreeNode node = (AbstractODKTreeNode) path.getLastPathComponent();		
			if(node instanceof ODKBranchNode || node.getParent() instanceof ODKBranchNode ) 
			{
				Alert.error("Group Error", "Field groups cannot be nested");
				return;
			}
		}
		

		String groupname = JOptionPane.showInputDialog(this,
                "Name for group:\n",
                "Group",
                JOptionPane.PLAIN_MESSAGE);
		
		if(groupname==null) return;
		
		ODKBranchNode groupNode = new ODKBranchNode(true, false, groupname, TridasObject.class);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) selectedFieldsTreeModel.getRoot();
		this.selectedFieldsTreeModel.insertNodeInto((AbstractODKTreeNode)groupNode, root, root.getChildCount());
		
		
				
		for(TreePath path : selectionpaths)
		{
			AbstractODKTreeNode node = (AbstractODKTreeNode) path.getLastPathComponent();		
			selectedFieldsTreeModel.removeNodeFromParent(node);
			selectedFieldsTreeModel.insertNodeInto(node, groupNode, groupNode.getChildCount());
		}
		
	}
	
	private void renameField()
	{
		AbstractODKTreeNode node = (AbstractODKTreeNode) tree.getLastSelectedPathComponent();
		
		if(node instanceof ODKBranchNode)
		{
			String groupname = JOptionPane.showInputDialog(this,
	                "Name for group:\n",
	                "Group",
	                JOptionPane.PLAIN_MESSAGE);
			
			if(groupname==null || groupname.length()==0) return;
			
			node.setUserObject(new String(groupname));
		}
		else if (node instanceof ODKFieldNode)
		{
			String fieldname = JOptionPane.showInputDialog(this,
	                "New name for field:\n",
	                "Rename field",
	                JOptionPane.PLAIN_MESSAGE);
			
			if(fieldname==null || fieldname.length()==0) return;
			
			ODKFieldInterface obj = (ODKFieldInterface) node.getUserObject();
			obj.setName(fieldname);
			selectedField = obj;
			setDetailsPanel();
		}
		
	}

}




