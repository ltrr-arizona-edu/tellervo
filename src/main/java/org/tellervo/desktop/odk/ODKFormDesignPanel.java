package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKFields;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasObject;

import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.ListSelectionModel;



public class ODKFormDesignPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField txtFieldName;
	private JTextField txtFieldCode;
	private JTextField txtType;
	private JCheckBox chkRequired;
	private JTextArea txtDescription;
	private JList lstAvailableFields;
	private JList lstSelectedFields;
	private ODKFieldListModel availableFieldsModel;
	private ODKFieldListModel selectedFieldsModel;
	private static final Logger log = LoggerFactory.getLogger(ODKFormDesignPanel.class);
	private JComboBox txtDefault;
    private DefaultListModel _model;
    private CheckBoxList cbxlstChoices;
    final private JDialog parent;
    
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
		panelMain.setLayout(new MigLayout("", "[][grow,fill]", "[][grow]"));
		
		JLabel lblFormType = new JLabel("Build form for:");
		panelMain.add(lblFormType, "cell 0 0");
		
		JComboBox cboFormType = new JComboBox();
		cboFormType.setModel(new DefaultComboBoxModel(new String[] {"Objects", "Elements and samples"}));
		panelMain.add(cboFormType, "cell 1 0");
		
		JPanel panelFieldPicker = new JPanel();
		panelMain.add(panelFieldPicker, "cell 0 1 2 1,grow");
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
			public void valueChanged(ListSelectionEvent arg0) {
				setDetailsPanel((AbstractODKField) lstAvailableFields.getSelectedValue());
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
			public void valueChanged(ListSelectionEvent arg0) {
				setDetailsPanel((AbstractODKField) lstSelectedFields.getSelectedValue());

			}
			
		});
		scrollPaneSelected.setViewportView(lstSelectedFields);
		
		JPanel panelFieldOptions = new JPanel();
		panelFieldOptions.setBorder(new TitledBorder(UIManager.getBorder("EditorPane.border"), "Field details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(panelFieldOptions);
		panelFieldOptions.setLayout(new MigLayout("", "[right][grow][]", "[][][][][][grow][grow]"));
		
		JLabel lblFieldNameDisplayed = new JLabel("Name displayed to user:");
		panelFieldOptions.add(lblFieldNameDisplayed, "cell 0 0,alignx trailing");
		
		txtFieldName = new JTextField();
		txtFieldName.setEditable(false);
		panelFieldOptions.add(txtFieldName, "cell 1 0 2 1,growx");
		txtFieldName.setColumns(10);
		
		JLabel lblInternalFieldCode = new JLabel("Internal code name:");
		panelFieldOptions.add(lblInternalFieldCode, "cell 0 1,alignx trailing");
		
		txtFieldCode = new JTextField();
		txtFieldCode.setEditable(false);
		panelFieldOptions.add(txtFieldCode, "cell 1 1 2 1,growx");
		txtFieldCode.setColumns(10);
		
		JLabel lblType = new JLabel("Data type:");
		panelFieldOptions.add(lblType, "cell 0 2,alignx trailing");
		
		txtType = new JTextField();
		txtType.setEditable(false);
		panelFieldOptions.add(txtType, "cell 1 2 2 1,growx");
		txtType.setColumns(10);
		
		JLabel lblDefaultValue = new JLabel("Default value:");
		panelFieldOptions.add(lblDefaultValue, "cell 0 3,alignx trailing");
		
		txtDefault = new JComboBox();
		txtDefault.setEnabled(false);
		txtDefault.setEditable(false);
		panelFieldOptions.add(txtDefault, "cell 1 3 2 1,growx");
		
		JLabel lblIsFieldMandatory = new JLabel("Is field mandatory?:");
		panelFieldOptions.add(lblIsFieldMandatory, "cell 0 4");
		
		chkRequired = new JCheckBox("");
		chkRequired.setEnabled(false);
		panelFieldOptions.add(chkRequired, "cell 1 4");
		
		JLabel lblOptionsToInclude = new JLabel("Options to include:");
		panelFieldOptions.add(lblOptionsToInclude, "cell 0 5,aligny top");
		
		CheckBoxList list = createCheckBoxList();

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		panelFieldOptions.add(scrollPane, "cell 1 5,grow");
		
		JButton btnAll = new JButton("");
		btnAll.setIcon(Builder.getIcon("selectall.png", 16));
		btnAll.setActionCommand("SelectAllChoices");
		btnAll.addActionListener(this);
		panelFieldOptions.add(btnAll, "flowy,cell 2 5,aligny top");
		
		JLabel lblDescription = new JLabel("Description:");
		panelFieldOptions.add(lblDescription, "cell 0 6,alignx right,aligny top");
		
		txtDescription = new JTextArea();
		txtDescription.setWrapStyleWord(true);
		txtDescription.setLineWrap(true);
		txtDescription.setEditable(false);
		panelFieldOptions.add(txtDescription, "cell 1 6,grow");
		
		JButton btnNone = new JButton("");
		btnNone.setActionCommand("SelectNoChoices");
		btnNone.addActionListener(this);
		btnNone.setIcon(Builder.getIcon("selectnone.png", 16));

		panelFieldOptions.add(btnNone, "cell 2 5");
		
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

	}
	
	
	private void setDetailsPanel(AbstractODKField field)
	{
		if(field==null)
		{
			this.txtFieldName.setText("");
			this.txtFieldCode.setText("");
			this.txtDescription.setText("");
			this.chkRequired.setSelected(false);
			this.txtType.setText("");
			return;
		}
		this.txtFieldName.setText(field.getFieldName());
		this.txtFieldCode.setText(field.getFieldCode());
		this.txtDescription.setText(field.getFieldDescription());
		this.chkRequired.setSelected(field.isFieldRequired());
		this.txtType.setText(field.getFieldType().toString());
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
			ArrayList<AbstractODKField> fields =  availableFieldsModel.getAllFields();
			if(fields==null || fields.size()==0) return;
			selectedFieldsModel.addAllFields(fields);
			availableFieldsModel.removeFields(fields);
		}
		else if(evt.getActionCommand().equals("RemoveOne"))
		{
			AbstractODKField field =  (AbstractODKField) lstSelectedFields.getSelectedValue();
			if(field==null) return;
			availableFieldsModel.addField(field);
			selectedFieldsModel.removeField(field);
		}
		else if(evt.getActionCommand().equals("RemoveAll"))
		{
			ArrayList<AbstractODKField> fields =  selectedFieldsModel.getAllFields();
			if(fields==null || fields.size()==0) return;
			availableFieldsModel.addAllFields(fields);
			selectedFieldsModel.removeFields(fields);
		}
		else if(evt.getActionCommand().equals("Save"))
		{
			for(int i=0; i<_model.getSize(); i++)
			{
				SelectableChoice choice = (SelectableChoice) _model.get(i);
				
				if(choice.isSelected())
				{
					log.debug("Choice: "+choice.toString());
				}
			}
			
		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			parent.dispose();
		}
		else if(evt.getActionCommand().equals("SelectAllChoices"))
		{
			DefaultListModel newModel = new DefaultListModel();

			for(int i=0; i<_model.getSize(); i++)
			{
				SelectableChoice item = ((SelectableChoice)_model.get(i));
				item.setSelected(true);
				newModel.addElement(item);
			}
			_model = newModel;
			cbxlstChoices.setModel(_model);
			cbxlstChoices.repaint();
		}
		else if(evt.getActionCommand().equals("SelectNoChoices"))
		{
			DefaultListModel newModel = new DefaultListModel();

			for(int i=0; i<_model.getSize(); i++)
			{
				SelectableChoice item = ((SelectableChoice)_model.get(i));
				item.setSelected(false);
				newModel.addElement(item);
			}
			_model = newModel;
			cbxlstChoices.setModel(_model);
			cbxlstChoices.repaint();

		}
	}
	
    private CheckBoxList createCheckBoxList() {
        _model = new DefaultListModel();
        SelectableChoice[] choice = {new SelectableChoice("aa"), 
        		new SelectableChoice("bb"), 
        		new SelectableChoice("cc"),
        		new SelectableChoice("dd"),
        		new SelectableChoice("e"),
        		new SelectableChoice("f"),
        		new SelectableChoice("g"),
        		new SelectableChoice("h")};
        for (SelectableChoice s : choice) {
            _model.addElement(s);
        }
        cbxlstChoices = new CheckBoxList(_model);
        cbxlstChoices.getCheckBoxListSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


        SearchableUtils.installSearchable(cbxlstChoices);
        cbxlstChoices.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
//                   
                	// Clear all selections             
                	for(int i=0; i < _model.getSize(); i++)
                	{
                        SelectableChoice item = (SelectableChoice)_model.get(i);
                        item.setSelected(false);
                	}  
                	
                	// No reselect all checked items
                	int[] selected = cbxlstChoices.getCheckBoxListSelectedIndices();
                    for (int i = 0; i < selected.length; i++) 
                    {
                        SelectableChoice item = (SelectableChoice)_model.get(selected[i]);
                        item.setSelected(true);
                    }
                }
            }
        });
        cbxlstChoices.setCheckBoxListSelectedIndices(new int[]{2, 3, 20});
        return cbxlstChoices;
    }
    

	
}


