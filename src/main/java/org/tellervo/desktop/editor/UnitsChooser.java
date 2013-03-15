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
package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.I18n;
import org.tridas.schema.NormalTridasUnit;


/*
  TODO:
  -- L10n?
  -- fix sample listener bug.
  -- (add keyboard shortcut for "next stat"?  something like accel-|, perhaps)
  -- rename: maybe "StatsLabel"?
*/

@SuppressWarnings("serial")
public class UnitsChooser extends JLabel implements SampleListener {
    private Sample sample;
    private  Boolean unitless;
    
    
    public UnitsChooser(Sample s) {
	this.sample = s;
	
	
	unitless = sample.isUnitless();
	
	sample.addSampleListener(this);

	final JLabel label = this; // ...

	addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			
			if(unitless)
			{
				return;
			}
			
		    JPopupMenu popup = new JPopupMenu();

		    // for each unit, create a menuitem 
		    for (int i=0; i<unit.length; i++) {
			JMenuItem s = new JRadioButtonMenuItem(unit[i], i==state);
			final int glue = i;

			s.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
				    // store state locally
				    state = glue;

				    // store state in pref
				    App.prefs.setPref(PrefKey.DISPLAY_UNITS, unit_keys[state]);

				    // update label
				    label.setText(I18n.getText("units")+ ": "+ ((JMenuItem) e.getSource()).getText());
				    sample.fireDisplayUnitsChanged();
				}
			    });
			popup.add(s);
		    }

		    
		    popup.show(label, e.getX(), e.getY());
		    
		}
	    });

		// which one?  read from prefs
		String pref = App.prefs.getPref(PrefKey.DISPLAY_UNITS, NormalTridasUnit.MICROMETRES.name().toString());
		if (pref != null) {
		    for (int i=0; i<unit.length; i++) {
				if (pref.equals(unit_keys[i])) {
				    state = i;
				    break;
				}
		    }
		}

		// set initial text
		if (unitless)
		{
			setText("Unitless");
		}
		else
		{
			setText(I18n.getText("units")+ ": " + unit[state]);			
		}
    }

    private int state = 0; // which stat_*[] is being seen

    private final String NA = I18n.getText("editor.na");


    //
    // messy stuff
    //
    private String unit_keys[] = new String[] {
    		
    		// Need to handle doubles rather than integers for this to work
    		// Also locale settings for decimal formats
    		//NormalTridasUnit.MILLIMETRES.name(),
    		
    		NormalTridasUnit.TENTH_MM.name(),
    		NormalTridasUnit.TWENTIETH_MM.name(),
    		NormalTridasUnit.FIFTIETH_MM.name(),
    		NormalTridasUnit.HUNDREDTH_MM.name(),
    		NormalTridasUnit.MICROMETRES.name()
    };
    
    private String unit[];
    {
		unit = new String[unit_keys.length];
		for (int i=0; i<unit_keys.length; i++)
		    unit[i] = I18n.getText("units."+unit_keys[i]);
    }
    
    private String stat_values[];
    {
		setStat_values(new String[unit.length]);
		// don't need to fill, constructor calls computeAllStats()
    }

    
    //
    // sample listener -- if sample changes, update stats
    //

    public void sampleDataChanged(SampleEvent e) {
    	// compute all?

    }
    
    
    public void sampleRedated(SampleEvent e) {
    	// doesn't change any stats
    }
    
    
    public void sampleMetadataChanged(SampleEvent e) {
		sampleDataChanged(e); // because if "indexed" is set, some
				      // stats become N/A -- inefficient,
				      // true, but simple
		// (Q: how much other work am i doing?  setting indexed->raw
		// seems simple enough, but if i do too many "simple" things
		// it could turn into a problem.)		
	
    }
    
    
    public void sampleElementsChanged(SampleEvent e) {
	sampleDataChanged(e);
    }


	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}


	public String getNA() {
		return NA;
	}


	public void setStat_values(String stat_values[]) {
		this.stat_values = stat_values;
	}


	public String[] getStat_values() {
		return stat_values;
	}


	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
