package edu.cornell.dendro.corina.prefs.wrappers;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;

public class SpinnerWrapper extends ChangeWrapper<Integer> {
	public SpinnerWrapper(JSpinner spinner, String prefName, Integer defaultValue) {
		super(prefName, defaultValue, Integer.class);
		
		spinner.setValue(getValue());
		spinner.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		setValue((Integer) ((JSpinner)e.getSource()).getValue());
	}

}
