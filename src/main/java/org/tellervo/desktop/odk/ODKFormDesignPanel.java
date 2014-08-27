package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKFields;
import org.tridas.schema.TridasObject;



public class ODKFormDesignPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ODKFormDesignPanel() {
		setLayout(new BorderLayout(0, 0));
		
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
		panelFieldPicker.setLayout(new MigLayout("", "[grow,fill][70px:70px:70px][grow,fill]", "[][grow,center]"));
		
		JLabel lblAvailableFields = new JLabel("Available fields:");
		panelFieldPicker.add(lblAvailableFields, "cell 0 0");
		
		JLabel lblSelectedFields = new JLabel("Selected fields:");
		panelFieldPicker.add(lblSelectedFields, "cell 2 0");
		
		JScrollPane scrollPaneAvailable = new JScrollPane();
		panelFieldPicker.add(scrollPaneAvailable, "cell 0 1,grow");
		
		JList lstAvailableFields = new JList();
		lstAvailableFields.setModel(new AbstractListModel() {
			Class<? extends AbstractODKField>[] values = ODKFields.getFieldsAsArray(TridasObject.class);
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPaneAvailable.setViewportView(lstAvailableFields);
		
		JPanel panelAddRemove = new JPanel();
		panelFieldPicker.add(panelAddRemove, "cell 1 1,growx,aligny center");
		panelAddRemove.setLayout(new MigLayout("", "[117px,fill]", "[25px][][][]"));
		
		JButton btnNewButton = new JButton(">>");
		panelAddRemove.add(btnNewButton, "cell 0 0,growx,aligny top");
		
		JButton button = new JButton(">");
		panelAddRemove.add(button, "cell 0 1");
		
		JButton button_1 = new JButton("<");
		panelAddRemove.add(button_1, "cell 0 2");
		
		JButton button_2 = new JButton("<<");
		panelAddRemove.add(button_2, "cell 0 3");
		
		JScrollPane scrollPaneSelected = new JScrollPane();
		panelFieldPicker.add(scrollPaneSelected, "cell 2 1,grow");
		
		JList lstSelectedFields = new JList();
		lstSelectedFields.setModel(new AbstractListModel() {
			String[] values = new String[] {"Code", "Title"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPaneSelected.setViewportView(lstSelectedFields);
		
		JPanel panelFieldOptions = new JPanel();
		splitPane.setRightComponent(panelFieldOptions);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Save");
		panel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel);

	}

}
