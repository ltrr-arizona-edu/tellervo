package corina.prefs.components;

import corina.prefs.Prefs;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.*;

// TODO: clean up imports
// TODO: gpl header
// TODO: javadoc
// TODO: rename to Check(Box?)PrefComponent?

public class BoolPrefComponent extends JCheckBox implements ActionListener {

    private String pref;

    public BoolPrefComponent(String name, String pref) {
	super(name);

        this.pref = pref;

        // set initial value
        if ("true".equals(Prefs.getPref(pref))) // (null means false)
            setSelected(true);

        // listen for user clicks
	addActionListener(this);
    }

    // list of widgets which this checkbox also dims/undims
    private List controlees = new ArrayList();

    // tell the checkbox that it should dim this component when unchecked
    public void controls(JComponent component) {
	controlees.add(component);

	if (!isSelected())
	    component.setEnabled(false);
    }

    // on check/uncheck...
    public void actionPerformed(ActionEvent e) {
	// enable/disable all the subsequent controls, as requested
        for (int i=0; i<controlees.size(); i++)
            ((JComponent) controlees.get(i)).setEnabled(isSelected());

	// set myself
        Prefs.setPref(pref, String.valueOf(isSelected()));
    }
}
