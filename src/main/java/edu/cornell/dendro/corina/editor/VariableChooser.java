/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.prefs.Prefs.PrefKey;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;

@SuppressWarnings("serial")
public class VariableChooser extends JLabel implements SampleListener {
    private Sample sample;
    private MeasurementVariable selectedState = MeasurementVariable.RING_WIDTH; // which variable is being seen
    
	public enum MeasurementVariable {
		RING_WIDTH("Ring width"),
		EARLYWOOD_WIDTH("Earlywood width"),
		LATEWOOD_WIDTH("Latewood width"),
		EARLY_AND_LATEWOOD_WIDTH("Early/latewood width");
		
		private final String value;
		
		private MeasurementVariable(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
		
		public static MeasurementVariable getPreferredVariable(Sample s)
		{
			if(!s.containsSubAnnualData())
			{
				return MeasurementVariable.RING_WIDTH;
			}
			
			String pref = App.prefs.getPref(PrefKey.MEASUREMENT_VARIABLE, null);
			if (pref != null) {
			    for (MeasurementVariable var: MeasurementVariable.values()) {
					if (pref.equals(var.toString())) {
					    return var;
					}
			    }
			}
			
			// Default to ring width
			return MeasurementVariable.RING_WIDTH;
		}
	}
       
    
    /**
     * Constructor for the variable chooser popup menu
     * @param s
     */
    public VariableChooser(Sample s) 
    {
		this.sample = s;
		
		
		sample.addSampleListener(this);
	
		final JLabel label = this; // ...
	
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
							
			    JPopupMenu popup = new JPopupMenu();
	
			    // for each variable, create a menuitem 
			    for (MeasurementVariable var : MeasurementVariable.values()) {
				JMenuItem s = new JRadioButtonMenuItem(var.toString(), var.equals(selectedState));
				final MeasurementVariable glue = var;
	
				// Disable all early/late wood options if there is no subannual data
				if(!var.equals(MeasurementVariable.RING_WIDTH))
				{
					s.setEnabled(sample.containsSubAnnualData());
				}
				
				s.addActionListener(new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
					    // store state locally
					    selectedState = glue;
	
					    // store state in pref
					    App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, selectedState.toString());
	
					    // update label
					    label.setText("Variable: "+((JMenuItem) e.getSource()).getText());
					    sample.fireDisplayUnitsChanged();
					}
				    });
				popup.add(s);
			    }
	
			    
			    popup.show(label, e.getX(), e.getY());
			    
			}
		    });
	
			// Get preferred variable to start with
			selectedState = MeasurementVariable.getPreferredVariable(sample);
				
			// set initial text
			setText("Variable: "+selectedState.toString());			
			
    }

    public void sampleDataChanged(SampleEvent e) {}
    
    
    public void sampleRedated(SampleEvent e) { 
    	sampleDataChanged(e);
    }
    
    
    public void sampleMetadataChanged(SampleEvent e) {
		sampleDataChanged(e); 	
    }
    
    
    public void sampleElementsChanged(SampleEvent e) {
    	sampleDataChanged(e);
    }


	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		sampleDataChanged(e);		
	}


	@Override
	public void measurementVariableChanged(SampleEvent e) {	
		// Get preferred variable to start with
		selectedState = MeasurementVariable.getPreferredVariable(sample);
			
		// set initial text
		setText("Variable: "+selectedState.toString());		
	}

}
