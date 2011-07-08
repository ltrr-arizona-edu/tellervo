package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.l2fprod.common.swing.ComponentFactory;

import net.miginfocom.swing.MigLayout;

public class MemoCell extends JPanel {
	private static final long serialVersionUID = 1L;
	public JTextField textField;
	public JButton btnEdit;
	public JButton btnDelete;
	
	
	/**
	 * Create the panel.
	 */
	public MemoCell() {
		setLayout(new MigLayout("insets 0", "[315.00px,grow,fill][::60px][::60px]", "[0px,0px]"));
		
		textField = new JTextField();
		add(textField, "cell 0 0,alignx left,aligny center");
		textField.setColumns(10);
		
		btnEdit = ComponentFactory.Helper.getFactory().createMiniButton();
		btnEdit.setText("...");
		add(btnEdit, "cell 1 0,alignx right,aligny top");
		
		btnDelete = ComponentFactory.Helper.getFactory().createMiniButton();
		btnDelete.setText("X");
		add(btnDelete, "cell 2 0,alignx left,aligny top");

	}

}
