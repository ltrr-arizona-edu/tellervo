package corina.prefs;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.*;

public class BooleanCheck extends JCheckBox implements ActionListener {

    public BooleanCheck(String name, String property) {
	super(name);

	addActionListener(this);

	if (Boolean.getBoolean(property))
	    setSelected(true);

	// WRITE ME
    }

    private List controlees = new ArrayList();

    public void controls(JComponent component) {
	controlees.add(component);

	if (!isSelected())
	    component.setEnabled(false);
    }

    // on check/uncheck...
    public void actionPerformed(ActionEvent e) {
	// enable/disable all the subsequent controls, as requested
	if (isSelected())
	    for (int i=0; i<controlees.size(); i++)
		((JComponent) controlees.get(i)).setEnabled(true);
	else
	    for (int i=0; i<controlees.size(); i++)
		((JComponent) controlees.get(i)).setEnabled(false);

	// set myself
	// WRITE ME
    }

}
