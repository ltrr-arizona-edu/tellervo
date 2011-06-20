package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

public class RadioButtonWrapper extends ActionWrapper<String> {

	public RadioButtonWrapper(JRadioButton buttons[], String prefName, Object defaultValue) {
		super(prefName, defaultValue, String.class);

		String selectedValue = getValue();
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].getActionCommand().equalsIgnoreCase(selectedValue))
				buttons[i].setSelected(true);
			
			buttons[i].addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setValue(e.getActionCommand());
	}
}
