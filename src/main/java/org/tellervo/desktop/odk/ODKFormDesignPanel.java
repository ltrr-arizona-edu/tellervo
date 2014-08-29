package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.AbstractODKChoiceField;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;

import org.tellervo.desktop.odk.fields.ODKFields;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasObject;

import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.SearchableUtils;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.io.File;



public class ODKFormDesignPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField txtFieldName;
	private JCheckBox chkRequired;
	private JTextArea txtDescription;
	private JList lstAvailableFields;
	private JList lstSelectedFields;
	private ODKFieldListModel availableFieldsModel;
	private ODKFieldListModel selectedFieldsModel;
	private static final Logger log = LoggerFactory.getLogger(ODKFormDesignPanel.class);
	private JComboBox txtDefault;
    private DefaultListModel choiceModel;
    private CheckBoxList cbxlstChoices;
    final private JDialog parent;
    private JScrollPane choicesScrollPane;
    private ODKFieldInterface selectedField;
    private JTextField txtFormName;
    
    
	/**
	 * Create the panel.
	 */
	public ODKFormDesignPanel(JDialog parent) {
		setLayout(new BorderLayout(0, 0));
		this.parent = parent;
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);
		
		JPanel panelMain = new JPanel();
		splitPane.setLeftComponent(panelMain);
		panelMain.setLayout(new MigLayout("", "[][grow,fill]", "[][][grow]"));
		
		JLabel lblFormName = new JLabel("Form name:");
		panelMain.add(lblFormName, "cell 0 0,alignx trailing");
		
		txtFormName = new JTextField();
		txtFormName.setText("My form name");
		panelMain.add(txtFormName, "cell 1 0,growx");
		txtFormName.setColumns(10);
		
		JLabel lblFormType = new JLabel("Build form for:");
		panelMain.add(lblFormType, "cell 0 1");
		
		JComboBox cboFormType = new JComboBox();
		cboFormType.setModel(new DefaultComboBoxModel(new String[] {"Objects", "Elements and samples"}));
		panelMain.add(cboFormType, "cell 1 1");
		
		JPanel panelFieldPicker = new JPanel();
		panelMain.add(panelFieldPicker, "cell 0 2 2 1,grow");
		panelFieldPicker.setLayout(new MigLayout("", "[300px,grow,fill][70px:70px:70px][300px,grow,fill]", "[][grow,center]"));
		
		JLabel lblAvailableFields = new JLabel("Available fields:");
		panelFieldPicker.add(lblAvailableFields, "cell 0 0");
		
		JLabel lblSelectedFields = new JLabel("Selected fields:");
		panelFieldPicker.add(lblSelectedFields, "cell 2 0");
		
		JScrollPane scrollPaneAvailable = new JScrollPane();
		panelFieldPicker.add(scrollPaneAvailable, "cell 0 1,grow");
		
		lstAvailableFields = new JList();
		lstAvailableFields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		availableFieldsModel = new ODKFieldListModel(ODKFields.getFields(TridasObject.class));
		lstAvailableFields.setModel(availableFieldsModel);

		lstAvailableFields.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				
				if(evt.getValueIsAdjusting()) return;
				
				selectedField = (AbstractODKField) lstAvailableFields.getSelectedValue();
				setDetailsPanel();
			}
			
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
		
		JScrollPane scrollPaneSelected = new JScrollPane();
		panelFieldPicker.add(scrollPaneSelected, "cell 2 1,grow");
		
		lstSelectedFields = new JList();
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
		scrollPaneSelected.setViewportView(lstSelectedFields);
		
		JPanel panelFieldOptions = new JPanel();
		panelFieldOptions.setBorder(new TitledBorder(UIManager.getBorder("EditorPane.border"), "Field details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(panelFieldOptions);
		panelFieldOptions.setLayout(new MigLayout("", "[right][grow][]", "[][59.00:59.00:59.00][][][grow]"));
		
		JLabel lblFieldNameDisplayed = new JLabel("Name:");
		panelFieldOptions.add(lblFieldNameDisplayed, "cell 0 0,alignx trailing");
		
		txtFieldName = new JTextField();
		txtFieldName.setActionCommand("FieldNameChanged");
		txtFieldName.addActionListener(this);
		
		txtFieldName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
								
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				fieldNameChanged();
				
			}
			
		});
		
		panelFieldOptions.add(txtFieldName, "cell 1 0 2 1,growx");
		txtFieldName.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		panelFieldOptions.add(lblDescription, "cell 0 1,alignx right,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		panelFieldOptions.add(scrollPane, "cell 1 1 2 1,grow");
		
		txtDescription = new JTextArea();
		txtDescription.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		txtDescription.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		scrollPane.setViewportView(txtDescription);
		txtDescription.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent evt) {
				commitDescriptionChange();
				
			}

			@Override
			public void insertUpdate(DocumentEvent evt) {
				commitDescriptionChange();
				
			}

			@Override
			public void removeUpdate(DocumentEvent evt) {
				commitDescriptionChange();
				
			}
			
		});
		txtDescription.setWrapStyleWord(true);
		txtDescription.setLineWrap(true);
						
		JLabel lblDefaultValue = new JLabel("Default value:");
		panelFieldOptions.add(lblDefaultValue, "cell 0 2,alignx trailing");
		
		txtDefault = new JComboBox();
		txtDefault.setEnabled(false);
		txtDefault.setEditable(false);
		panelFieldOptions.add(txtDefault, "cell 1 2 2 1,growx");
		
		JLabel lblIsFieldMandatory = new JLabel("Is field mandatory?:");
		panelFieldOptions.add(lblIsFieldMandatory, "cell 0 3");
		
		chkRequired = new JCheckBox("");
		chkRequired.setEnabled(false);
		panelFieldOptions.add(chkRequired, "cell 1 3");
		
		JLabel lblOptionsToInclude = new JLabel("Options to include:");
		panelFieldOptions.add(lblOptionsToInclude, "cell 0 4,aligny top");
		
		choicesScrollPane = new JScrollPane();

		createCheckBoxList(null);

		
		choicesScrollPane.setViewportView(cbxlstChoices);
		panelFieldOptions.add(choicesScrollPane, "cell 1 4,grow");
		
		JButton btnAll = new JButton("");
		btnAll.setIcon(Builder.getIcon("selectall.png", 16));
		btnAll.setActionCommand("SelectAllChoices");
		btnAll.addActionListener(this);
		panelFieldOptions.add(btnAll, "flowy,cell 2 4,aligny top");
		
		JButton btnNone = new JButton("");
		btnNone.setActionCommand("SelectNoChoices");
		btnNone.addActionListener(this);
		btnNone.setIcon(Builder.getIcon("selectnone.png", 16));

		panelFieldOptions.add(btnNone, "cell 2 4");
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Save");
		btnSave.setActionCommand("Save");
		btnSave.addActionListener(this);
		panel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panel.add(btnCancel);
		
		// Make sure all mandatory fields are selected
		this.addAllFields();
		this.removeAllNonMandatoryFields();

	}
	
	private void commitDescriptionChange()
	{
		if(selectedField==null) return;
		selectedField.setDescription(this.txtDescription.getText());
		
	}
	
	private void setDetailsPanel()
	{
		if(selectedField==null)
		{
			this.txtFieldName.setText("");
			this.txtDescription.setText("");
			this.chkRequired.setSelected(false);
			return;
		}
		this.txtFieldName.setText(selectedField.getFieldName());
		this.txtDescription.setText(selectedField.getFieldDescription());
		this.chkRequired.setSelected(selectedField.isFieldRequired());
		
		if(selectedField instanceof AbstractODKChoiceField)
		{
			ArrayList<SelectableChoice> choices = ((AbstractODKChoiceField)selectedField).getAvailableChoices();
			createCheckBoxList(choices);
			log.debug("Number of choices = "+cbxlstChoices.getModel().getSize());
		}
		else
		{
			createCheckBoxList(null);
		}
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
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

	}
	
	private void addAllFields()
	{
		ArrayList<ODKFieldInterface> fields =  availableFieldsModel.getAllFields();
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
		ArrayList<ODKFieldInterface> fields =  selectedFieldsModel.getAllFields();
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
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("AddOne"))
		{
			AbstractODKField field =  (AbstractODKField) lstAvailableFields.getSelectedValue();
			if(field==null) return;
			selectedFieldsModel.addField(field);
			availableFieldsModel.removeField(field);
		}
		else if(evt.getActionCommand().equals("AddAll"))
		{
			addAllFields();
		}
		else if(evt.getActionCommand().equals("RemoveOne"))
		{
			AbstractODKField field =  (AbstractODKField) lstSelectedFields.getSelectedValue();
			if(field==null) return;
			if(field.isFieldRequired()) return;
			availableFieldsModel.addField(field);
			selectedFieldsModel.removeField(field);
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
			parent.dispose();
		}
		else if(evt.getActionCommand().equals("SelectAllChoices"))
		{
			DefaultListModel newModel = new DefaultListModel();

			for(int i=0; i<choiceModel.getSize(); i++)
			{
				SelectableChoice item = ((SelectableChoice)choiceModel.get(i));
				item.setSelected(true);
				newModel.addElement(item);
			}
			choiceModel = newModel;
			cbxlstChoices.setModel(choiceModel);
			cbxlstChoices.repaint();
		}
		else if(evt.getActionCommand().equals("SelectNoChoices"))
		{
			DefaultListModel newModel = new DefaultListModel();

			for(int i=0; i<choiceModel.getSize(); i++)
			{
				SelectableChoice item = ((SelectableChoice)choiceModel.get(i));
				item.setSelected(false);
				newModel.addElement(item);
			}
			choiceModel = newModel;
			cbxlstChoices.setModel(choiceModel);
			cbxlstChoices.repaint();

		}
	}
	
    private void createCheckBoxList(ArrayList<SelectableChoice> choices) {
       
    	if(choices==null) choices = new ArrayList<SelectableChoice>();
    	
    	choiceModel = new DefaultListModel();

        for (SelectableChoice s : choices) {
            choiceModel.addElement(s);
        }
        cbxlstChoices = new CheckBoxList(choiceModel);
        cbxlstChoices.getCheckBoxListSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


        SearchableUtils.installSearchable(cbxlstChoices);
        cbxlstChoices.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                 
                	// Clear all selections             
                	for(int i=0; i < choiceModel.getSize(); i++)
                	{
                        SelectableChoice item = (SelectableChoice)choiceModel.get(i);
                        item.setSelected(false);
                	}  
                	
                	// Now reselect all checked items
                	int[] selected = cbxlstChoices.getCheckBoxListSelectedIndices();
                    for (int i = 0; i < selected.length; i++) 
                    {
                        SelectableChoice item = (SelectableChoice)choiceModel.get(selected[i]);
                        item.setSelected(true);
                    }
                }
            }
        });
        
        choicesScrollPane.setViewportView(cbxlstChoices);
        // Disable if no choices
        cbxlstChoices.setEnabled(choices.size()>0);
        
    }
    

    private void doSave()
    {
    	File file = new File("/tmp/test.xml");
    	
    	ODKFormGenerator.generate(file, txtFormName.getText(), this.selectedFieldsModel.getAllFields());
    	
    }
	
	public JTextField getTxtFormName() {
		return txtFormName;
	}
}


