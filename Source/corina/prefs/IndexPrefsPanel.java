package corina.prefs;

import corina.Year;
import corina.Range;
import corina.Sample;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;

import javax.swing.*;

public class IndexPrefsPanel extends JPanel {

    /*

    front-end:

    ???

    back-end:

    corina.index.lowpass
                .polydegs
		.cubicfactor

    */

    public IndexPrefsPanel() {
	setLayout(new FlowLayout());

	JPanel controls = new JPanel();
	controls.setLayout(new GridLayout(0, 2, 6, 6));

	controls.add(new JLabel("High-pass filter weights:", SwingConstants.RIGHT));
	controls.add(new JLabel("WRITE ME"));
	controls.add(new JLabel("Polynomial degrees:", SwingConstants.RIGHT));
	controls.add(new JLabel("WRITE ME"));
	controls.add(new JLabel("Cubic spline s-value:", SwingConstants.RIGHT));
	controls.add(new JLabel("WRITE ME"));

	// ----------------------------------------

	JPanel preview = new JPanel();
	// WRITE ME

	// ----------------------------------------

	add(controls);
	add(Box.createHorizontalStrut(10));
	add(new JSeparator(JSeparator.VERTICAL));
	add(Box.createHorizontalStrut(10));
	add(preview);
    }

}
