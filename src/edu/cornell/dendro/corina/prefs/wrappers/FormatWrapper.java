package edu.cornell.dendro.corina.prefs.wrappers;

import java.awt.event.ItemEvent;
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class FormatWrapper extends ItemWrapper<String> {
	public FormatWrapper(JComboBox cbo, String prefName, Object defaultValue) {
		super(prefName, defaultValue, String.class);
		
		// show a sample for each format thingy...
		String[] formats = new String[FORMAT_STRINGS.length];
		int selectedIdx = -1;
		
		for(int i = 0; i < FORMAT_STRINGS.length; i++) {
			formats[i] = new DecimalFormat(FORMAT_STRINGS[i]).format(SAMPLE_NUMBER);
			if(FORMAT_STRINGS[i].equals(getValue()))
				selectedIdx = i;
		}
		
		cbo.setModel(new DefaultComboBoxModel(formats));
		if(selectedIdx >= 0)
			cbo.setSelectedIndex(selectedIdx);
		
		cbo.addItemListener(this);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		int selectedIdx = ((JComboBox) e.getSource()).getSelectedIndex();
		
		if(selectedIdx >= 0)
			setValue(FORMAT_STRINGS[selectedIdx]);
		else
			setValue(null);
	}

    private final static String FORMAT_STRINGS[] = new String[] {
        "0.0", "0.00", "0.000", "0.0000", "0.00000",
        "0%", "0.0%", "0.00%", "0.000%",
    };

    private final static float SAMPLE_NUMBER = 0.49152f;
}
