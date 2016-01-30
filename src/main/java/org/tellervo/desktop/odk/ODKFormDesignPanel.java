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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.miginfocom.swing.MigLayout;

import org.codehaus.plexus.util.FileUtils;
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
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.xml.sax.SAXException;

import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.SearchableUtils;



public class ODKFormDesignPanel extends JPanel implements ActionListener, Serializable{

	private static final long serialVersionUID = 1L;
	private JTextField txtFieldName;
	private JCheckBox chkRequired;
	private JTextArea txtDescription;
	private JList lstAvailableFields;
	private JList lstSelectedFields;
	private ODKFieldListModel availableFieldsModel;
	private ODKFieldListModel selectedFieldsModel;
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
    
    
	/**
	 * Create the panel.
	 */
	public ODKFormDesignPanel(JDialog theparent) {
		setLayout(new BorderLayout(0, 0));
		this.parent = theparent;
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		add(splitPane);
		
		JPanel panelMain = new JPanel();
		splitPane.setLeftComponent(panelMain);
		panelMain.setLayout(new MigLayout("", "[][grow,fill]", "[][][][grow]"));
		
		JLabel lblFormName = new JLabel("Form name:");
		panelMain.add(lblFormName, "cell 0 1,alignx trailing");
		
		txtFormName = new JTextField();
		txtFormName.setText("My form name");
		panelMain.add(txtFormName, "cell 1 1,growx");
		txtFormName.setColumns(10);
		
		JLabel lblFormType = new JLabel("Build form for:");
		panelMain.add(lblFormType, "cell 0 2");
		
		cboFormType = new JComboBox();
		cboFormType.setModel(new DefaultComboBoxModel(new String[] {"Objects", "Elements and samples"}));
		cboFormType.setActionCommand("FormTypeChanged");
		cboFormType.addActionListener(this);
		panelMain.add(cboFormType, "cell 1 2");
		
		JPanel panelFieldPicker = new JPanel();
		panelMain.add(panelFieldPicker, "cell 0 3 2 1,grow");
		panelFieldPicker.setLayout(new MigLayout("", "[300px,grow,fill][70px:70px:70px][300px,grow,fill]", "[][grow,center][]"));
		
		JLabel lblAvailableFields = new JLabel("Available fields:");
		panelFieldPicker.add(lblAvailableFields, "cell 0 0");
		
		JLabel lblSelectedFields = new JLabel("Selected fields:");
		panelFieldPicker.add(lblSelectedFields, "flowx,cell 2 0");
		
		scrollPaneAvailable = new JScrollPane();
		panelFieldPicker.add(scrollPaneAvailable, "cell 0 1,grow");
		
		lstAvailableFields = new JList();
		lstAvailableFields.setCellRenderer(new ODKFieldRenderer());
		lstAvailableFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
		lstAvailableFields.setModel(availableFieldsModel);

		lstAvailableFields.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				
				/*if(evt.getValueIsAdjusting()) return;
								
				selectedField = (AbstractODKField) lstAvailableFields.getSelectedValue();
				setDetailsPanel();*/
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
						lstSelectedFields.setSelectedIndex(lstSelectedFields.getModel().getSize()-1);
						selectedField = (AbstractODKField) lstSelectedFields.getSelectedValue();
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
		
		scrollPaneAvailable.setViewportView(lstAvailableFields);
		
		JPanel panelAddRemove = new JPanel();
		panelFieldPicker.add(panelAddRemove, "cell 1 1,growx,aligny center");
		panelAddRemove.setLayout(new MigLayout("", "[fill]", "[25px][][][]"));
		
		JButton btnAddAll = new JButton("");
		btnAddAll.setIcon(Builder.getIcon("2rightarrow.png", 22));
		btnAddAll.setActionCommand("AddAll");
		btnAddAll.addActionListener(this);
		panelAddRemove.add(btnAddAll, "cell 0 0,growx,aligny top");
		
		JButton btnAddOne = new JButton("");
		btnAddOne.setIcon(Builder.getIcon("1rightarrow.png", 22));
		btnAddOne.setActionCommand("AddOne");
		btnAddOne.addActionListener(this);
		panelAddRemove.add(btnAddOne, "cell 0 1");
		
		JButton btnRemoveOne = new JButton("");
		btnRemoveOne.setActionCommand("RemoveOne");
		btnRemoveOne.addActionListener(this);
		btnRemoveOne.setIcon(Builder.getIcon("1leftarrow.png", 22));
		panelAddRemove.add(btnRemoveOne, "cell 0 2");
		
		JButton btnRemoveAll = new JButton("");
		btnRemoveAll.setActionCommand("RemoveAll");
		btnRemoveAll.addActionListener(this);
		btnRemoveAll.setIcon(Builder.getIcon("2leftarrow.png", 22));
		panelAddRemove.add(btnRemoveAll, "cell 0 3");
		
		scrollPaneSelected = new JScrollPane();
		panelFieldPicker.add(scrollPaneSelected, "cell 2 1,grow");
		
		lstSelectedFields = new JList();
		lstSelectedFields.setCellRenderer(new ODKFieldRenderer());
		lstSelectedFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedFieldsModel = new ODKFieldListModel();
		lstSelectedFields.setModel(selectedFieldsModel);
		
		lstSelectedFields.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent evt) {

				if(evt.getValueIsAdjusting()) return;
				
				log.debug("Selected fields list item selected");

				selectedField = (AbstractODKField) lstSelectedFields.getSelectedValue();
				setDetailsPanel();

			}
			
		});
		
		lstSelectedFields.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent evt) {
				
				if(evt.isPopupTrigger())
				{
					
				}
				else
				{
					if(evt.getClickCount()>1)
					{
						removeOneField();
						lstAvailableFields.setSelectedIndex(lstAvailableFields.getModel().getSize()-1);
						selectedField = (AbstractODKField) lstAvailableFields.getSelectedValue();
						setDetailsPanel();
						JScrollBar vertical = scrollPaneAvailable.getVerticalScrollBar();
						vertical.setValue( vertical.getMaximum() );
					}
					else
					{
						selectedField = (AbstractODKField) lstSelectedFields.getSelectedValue();
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
		
		scrollPaneSelected.setViewportView(lstSelectedFields);
		
		JButton btnAddUserDefined = new JButton("Add user defined field");
		btnAddUserDefined.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent evt) {
				
				boolean isObjectField = cboFormType.getSelectedIndex()==0;
				
				ODKUserDefinedFieldDialog dialog = new ODKUserDefinedFieldDialog(isObjectField, parent);
				dialog.setVisible(true);
				
				if(dialog.success)
				{
					selectedFieldsModel.addField(dialog.getField());
				}
				else
				{
					return;
				}
				
			}
			
		});
		panelFieldPicker.add(btnAddUserDefined, "cell 2 2,alignx right");
		
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
		
		txtFieldName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
								
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				fieldNameChanged();
				
			}
			
		});
		
		panelFieldOptions.add(txtFieldName, "cell 2 1,growx");
		txtFieldName.setColumns(10);
		
		chkRequired = new JCheckBox("Required");
		chkRequired.setEnabled(false);
		panelFieldOptions.add(chkRequired, "cell 2 2");
		
		chkHideField = new JCheckBox("Hide field from user (default value required if ticked)");
		chkHideField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				commitFieldChanges();
				
			}
			
		});
		panelFieldOptions.add(chkHideField, "cell 2 3,aligny top");
		
		JLabel lblDescription = new JLabel("Description:");
		panelFieldOptions.add(lblDescription, "cell 0 4 2 1,alignx right,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelFieldOptions.add(scrollPane, "cell 2 4,grow");
		
		txtDescription = new JTextArea();
		scrollPane.setViewportView(txtDescription);
		txtDescription.setLineWrap(true);
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
		txtDescription.setWrapStyleWord(true);
						
		lblDefaultValue = new JLabel("Default value:");
		panelFieldOptions.add(lblDefaultValue, "cell 0 5 2 1,alignx trailing,aligny center");
		defModel = new DefaultComboBoxModel();
		
		JPanel panel_1 = new JPanel();
		panelFieldOptions.add(panel_1, "cell 2 5,grow");
		panel_1.setLayout(new MigLayout("hidemode 3, insets 0", "[grow,fill]", "[center][center]"));
		
		txtDefault = new JTextField();
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
		
		panel_1.add(txtDefault, "cell 0 0,growx,aligny top");
		txtDefault.setColumns(10);
		
		cboDefault = new JComboBox();
		cboDefault.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				commitFieldChanges();
				
			}
			
		});
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
		add(panel, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Generate ODK Form");
		btnSave.setActionCommand("Save");
		btnSave.addActionListener(this);
		panel.setLayout(new MigLayout("", "[][][grow][][][73px]", "[25px]"));
		
		JButton btnLoadFormDef = new JButton("Load Form Design");
		panel.add(btnLoadFormDef, "cell 0 0,alignx left,aligny top");
		btnLoadFormDef.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try{
					loadFormDefinition();
				} catch (InvalidClassException ice)
				{
					Alert.error("Error loading form", "The form definition you are trying to load appears to have been created "
							+ "by a different version of Tellervo.");
				}
				catch (Exception ex)
				{
					Alert.error("Error loading form", "Error loading form definition. Note that only .odkform definitions "
							+ "generated by Tellervo are supported.  You cannot load forms from ODK XML files.");
					ex.printStackTrace();
				}

				
			}
			
		});
		
		JButton btnSaveFormDefinition = new JButton("Save form Design");
		panel.add(btnSaveFormDefinition, "cell 1 0");
		btnSaveFormDefinition.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					saveFormDefinition();
				} catch (IOException e) {
					Alert.error("Error saving", "Error saving form definition");
					e.printStackTrace();

				}
			}
			
		});
		
		JButton btnUploadForm = new JButton("Upload Form");
		btnUploadForm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				uploadForm();
				
			}
			
		});
		panel.add(btnUploadForm, "cell 3 0");
		panel.add(btnSave, "cell 4 0,alignx left,aligny top");
		
		JButton btnCancel = new JButton("Close");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panel.add(btnCancel, "cell 5 0,alignx left,aligny top");
		
		// Make sure all mandatory fields are selected
		this.addAllFields();
		this.removeAllNonMandatoryFields();
		setChoiceGUIVisible(false);


	}
	
	private void uploadForm()
	{
		String contents = ODKFormGenerator.generate(txtFormName.getText(), (ArrayList<ODKFieldInterface>) this.selectedFieldsModel.getAllFields(), null);
		

		WSIOdkFormDefinition odkform = new WSIOdkFormDefinition();
		
		odkform.setName(txtFormName.getText());
		

		
		org.w3c.dom.Element formdef = null;
		try {
			formdef = DocumentBuilderFactory
				    .newInstance()
				    .newDocumentBuilder()
				    .parse(new ByteArrayInputStream(contents.getBytes()))
				    .getDocumentElement();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		 
		
		String lastVisitedFolder = App.prefs.getPref(PrefKey.FOLDER_LAST_SAVE, null);

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
			ODKFieldListModel list = ((ODKSerializedForm) obj).getListModel();
			String formTitle = ((ODKSerializedForm) obj).getFormTitle();
			
			if(formTitle!=null) txtFormName.setText(formTitle);
			if(list.getSize()==0) return;
			
			if(list.getElementAt(0).getTridasClass().equals(TridasObject.class)) {
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
						
			selectedFieldsModel = (ODKFieldListModel) list;
			lstSelectedFields.setModel(selectedFieldsModel);
					
			
			
			
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
			obj_out.writeObject ( new ODKSerializedForm(selectedFieldsModel, txtFormName.getText()) );
		
			obj_out.close();
			
			return true;
    	}


    	return false;

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
		
		this.lstSelectedFields.repaint();
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
		selectedFieldsModel.addAllFields(fields);
		availableFieldsModel.removeFields(fields);
	}
	
	private void fieldNameChanged()
	{
		if(selectedField==null) return;
		selectedField.setName(this.txtFieldName.getText());
		
		for(ODKFieldInterface field : this.selectedFieldsModel.getAllFields())
		{
			if(selectedField.getFieldCode().equals(field.getFieldCode()))
			{
				field.setName(this.txtFieldName.getText());
				this.lstSelectedFields.repaint();
			}
		}
		
	}
		
	private void removeAllNonMandatoryFields()
	{
		ArrayList<ODKFieldInterface> fields =  (ArrayList<ODKFieldInterface>) selectedFieldsModel.getAllFields();
		ArrayList<ODKFieldInterface> fields2 =  new ArrayList<ODKFieldInterface>();

		if(fields==null || fields.size()==0) return;
		
		for(ODKFieldInterface field : fields)
		{
			if(!field.isFieldRequired())
			{
				fields2.add(field);
			}
		}
		
		availableFieldsModel.addAllFields(fields2);
		selectedFieldsModel.removeFields(fields2);
	}
	
	private void addOneField()
	{
		AbstractODKField field =  (AbstractODKField) lstAvailableFields.getSelectedValue();
		if(field==null) return;
		selectedFieldsModel.addField(field);
		availableFieldsModel.removeField(field);
	}
	
	private void removeOneField()
	{
		AbstractODKField field =  (AbstractODKField) lstSelectedFields.getSelectedValue();
		if(field==null) return;
		if(field.isFieldRequired()) return;
		availableFieldsModel.addField(field);
		selectedFieldsModel.removeField(field);
	}
	
	@Override
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
		else if(evt.getActionCommand().equals("Save"))
		{
			doSave();
			
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
		else if (evt.getActionCommand().equals("DefaultChosen"))
		{
			Object item = cboDefault.getSelectedItem();
			if(item!=null) selectedField.setDefaultValue(item);
		}
		else if (evt.getActionCommand().equals("FormTypeChanged"))
		{

			if(this.cboFormType.getSelectedIndex()==0)
			{
				log.debug("Setting gui for object form creation");
				availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
				selectedFieldsModel = new ODKFieldListModel();
				this.lstAvailableFields.setModel(availableFieldsModel);
				this.lstSelectedFields.setModel(selectedFieldsModel);
				this.addAllFields();
				this.removeAllNonMandatoryFields();
			}
			else
			{
				log.debug("Setting gui for element form creation");
				ArrayList<ODKFieldInterface> fields = ODKFields.getFields(TridasElement.class);
				fields.addAll(ODKFields.getFields(TridasSample.class));
				fields.addAll(ODKFields.getFields(TridasRadius.class));
				availableFieldsModel = new ODKFieldListModel(fields);
				selectedFieldsModel = new ODKFieldListModel();
				this.lstAvailableFields.setModel(availableFieldsModel);
				this.lstSelectedFields.setModel(selectedFieldsModel);

				this.addAllFields();
				this.removeAllNonMandatoryFields();
			}
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
    		
    		if(this.cboFormType.getSelectedIndex()==0)
    		{
    			// Generate Objects form
    			ODKFormGenerator.generate(file, txtFormName.getText(), (ArrayList<ODKFieldInterface>) this.selectedFieldsModel.getAllFields(), null);
    		}
    		else
    		{
    			// Generate Elements/Samples form			
    			ArrayList<ODKFieldInterface> mainFields = new ArrayList<ODKFieldInterface>();
    			ArrayList<ODKFieldInterface> secondaryFields = new ArrayList<ODKFieldInterface>();
    			for(ODKFieldInterface field : this.selectedFieldsModel.getAllFields())
    			{
    				if(field.getTridasClass().equals(TridasElement.class))
    				{
    					mainFields.add(field);
    				}
    				
    				if(field.getTridasClass().equals(TridasSample.class) || 
    						field.getTridasClass().equals(TridasRadius.class)) 
    				{
    					secondaryFields.add(field);
    				}

    			}
    			ODKFormGenerator.generate(file, txtFormName.getText(), mainFields, secondaryFields);

    		}
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
		
		/*dialog.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}

			@Override
			public void windowClosing(WindowEvent evt) {
				handleClose();

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			private void handleClose()
			{
				//Custom button text
				Object[] options = {"Yes",
				                    "No",
				                    "Cancel"};
				int n = JOptionPane.showOptionDialog(dialog,
				    "Would you like to save this form as a Tellervo form definition file?\n"
				    + "You will need this if you'd like to modify this form without having\n"
				    + "to start from scratch.  Note you cannot modify the ODK XML file in\n"
				    + "Tellervo.",
				    "Save form definition",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[0]);
				
				if(n == JOptionPane.CANCEL_OPTION)
				{
					return;
				}
				else if (n==JOptionPane.YES_OPTION)
				{
					boolean result;
					try {
						result = panel.saveFormDefinition();
					} catch (IOException e) {
						Alert.error("Error saving", "Error saving form definition");
						return;
					}
					
					if(!result) return;
				}
				else if (n==JOptionPane.NO_OPTION)
				{
					
				}
				
				dialog.dispose();
			}
			
		});*/
		
		dialog.pack();
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		
		
	}

}




