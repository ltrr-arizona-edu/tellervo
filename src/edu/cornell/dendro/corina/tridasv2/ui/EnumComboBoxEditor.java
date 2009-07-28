package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class EnumComboBoxEditor extends DefaultCellEditor {
	public EnumComboBoxEditor(Class<?> enumClass) {
		super(getComboBox(enumClass));
	}
	
	public static JComboBox getComboBox(Class<?> enumClass) {
		JComboBox box = new JComboBox(enumClass.getEnumConstants());
		
		box.setRenderer(new EnumComboBoxItemRenderer());
		
		return box;
	}
}
