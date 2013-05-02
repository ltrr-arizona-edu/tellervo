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
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.NormalTridasDatingType;
import org.tridas.schema.NormalTridasUnit;




@SuppressWarnings("serial")
public class CalendarChooser extends JLabel implements SampleListener {
    private Sample sample;
    private  Boolean relative;
    private NormalTridasDatingType datingtype = NormalTridasDatingType.RELATIVE;
    
    public CalendarChooser(Sample s)    
    {
		this.sample = s;
		
		init();
    }
    
    private void init()
    {

		ITridasSeries series = sample.getSeries();
		
		
		try{
			datingtype = series.getInterpretation().getDating().getType();
		} catch (Exception e)
		{
			
		}

		
	
    	
    	relative = datingtype.equals(NormalTridasDatingType.RELATIVE);
    	
    	sample.addSampleListener(this);

    	JLabel label = this; // ...

    	/*addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
    			
    			if(relative)
    			{
    				return;
    			}
    			
    		    JPopupMenu popup = new JPopupMenu();

    		    // for each calendar, create a menuitem 
    		    for (int i=0; i<calendar_keys.length; i++) {
    			JMenuItem s = new JRadioButtonMenuItem(calendar_keys[i], i==state);
    			final int glue = i;

    			s.addActionListener(new AbstractAction() {
    				public void actionPerformed(ActionEvent e) {
    				    // store state locally
    				    state = glue;

    				    // store state in pref
    				    App.prefs.setPref(PrefKey.DISPLAY_DATING_SUFFIX, calendar_keys[state]);

    				    // update label
    				    label.setText(((JMenuItem) e.getSource()).getText());
    				    sample.fireDisplayCalendarChanged();
    				}
    			    });
    			popup.add(s);
    		    }

    		    
    		    popup.show(label, e.getX(), e.getY());
    		    
    		}
    	    });
*/
    		// which one?  read from prefs
    		String pref = App.prefs.getPref(PrefKey.DISPLAY_DATING_SUFFIX, "AD/BC");
    		if (pref != null) {
    		    for (int i=0; i<calendar_keys.length; i++) {
    				if (pref.equals(calendar_keys[i])) {
    				    state = i;
    				    break;
    				}
    		    }
    		}

    		// set initial text
    		if (relative)
    		{
    			setText("Calendar: Relative");
    		}
    		else
    		{
    			setText("Calendar: "+pref);			
    		}
    }
    

    private int state = 0; // which stat_*[] is being seen

    private final String NA = I18n.getText("editor.na");


    //
    // messy stuff
    //
    private String calendar_keys[] = new String[] {
    		    		
    		"AD/BC",
    		"CE/BCE",
    		"BP"
    };
    

    
    //
    // sample listener -- if sample changes, update stats
    //

    public void sampleDataChanged(SampleEvent e) {
    	init();

    }
    
    
    public void sampleRedated(SampleEvent e) {
    	init();
    }
    
    
    public void sampleMetadataChanged(SampleEvent e) {
		sampleDataChanged(e); 	
	
    }
    
    
    public void sampleElementsChanged(SampleEvent e) {
	sampleDataChanged(e);
    }


	public String getNA() {
		return NA;
	}


	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		init();
		
	}
	
	public static String getADSuffix()
	{
		// which one?  read from prefs
		String pref = App.prefs.getPref(PrefKey.DISPLAY_DATING_SUFFIX, "AD/BC");
		
		if(pref.equals("AD/BC"))
		{
			return "AD";
		}
		else
		{
			return "CE";
		}
		
	}

	public static String getBCSuffix()
	{
		// which one?  read from prefs
		String pref = App.prefs.getPref(PrefKey.DISPLAY_DATING_SUFFIX, "AD/BC");
		
		if(pref.equals("AD/BC"))
		{
			return "BC";
		}
		else
		{
			return "BCE";
		}
		
	}


}
