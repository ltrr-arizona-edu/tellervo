package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class EnumComboBoxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

	public EnumComboBoxEditor(Class<?> enumClass) {
		super(getComboBox(enumClass));
	}
	
	public static JComboBox getComboBox(Class<?> enumClass) {
		JComboBox box = new JComboBox(enumClass.getEnumConstants());
		
		box.setRenderer(new EnumComboBoxItemRenderer());
		
		return box;
	}
}
