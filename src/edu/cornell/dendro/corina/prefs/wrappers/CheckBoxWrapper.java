package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

// this one is nice and simple! :)

public class CheckBoxWrapper extends ItemWrapper<Boolean> {
	public CheckBoxWrapper(JCheckBox cb, String prefName, Boolean defaultValue) {
		super(prefName, defaultValue, Boolean.class);
		
		cb.setSelected(getValue());
		cb.addItemListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		setValue(((AbstractButton)e.getSource()).isSelected());
	}
}
