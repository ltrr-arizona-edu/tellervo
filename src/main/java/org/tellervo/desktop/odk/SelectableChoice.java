package org.tellervo.desktop.odk;

import com.jidesoft.swing.Selectable;

public class SelectableChoice implements Selectable {

	boolean selected = false;
	boolean enabled = true;
	String label;
	
	public SelectableChoice(String label)
	{
		this.label = label;
	}
	
	public String toString()
	{
		return label;
	}
	
	@Override
	public void invertSelected() {
		selected = !selected;

	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;

	}

	@Override
	public void setSelected(boolean b) {
		selected = b;

	}

}
