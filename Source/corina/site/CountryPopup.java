//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.site;

import corina.ui.I18n;

import java.util.Arrays;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
   A popup menu of countries.

   <p>(Actually, a "JComboBox", though it's not a combination of
   anything).</p>

   <p>Shows all the countries referenced by Sites, along with "None"
   and "Other...".  If "Other..." is chosen, a dialog (CountryDialog)
   is presented allowing the user to choose any country.</p>

   <p>TODO:</p>
   <ul>
     <li>make sure the |code| member invariants are respected
     <li>if >25 countries, use only the most common 25? (needs sitedb support)
     <li>refactor siteinfo.java to use this?
     <li>use a custom renderer to show flags, too?
   </ul>

   @see corina.site.Country
   @see corina.site.CountryDialog

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class CountryPopup extends JComboBox {

    private JDialog parent;

    /**
       Make a new popup containing all the countries used by sites.
    */
    public CountryPopup(JDialog parent) {
	// display all country names used by sites
	String items[] = getCountryNames();
	items = sandwichWithNoneAndOther(items);
	ComboBoxModel model = new DefaultComboBoxModel(items);
	setModel(model);

	// don't use scrollbars unless i really need them.
	setMaximumRowCount(50);

	// store parent
	this.parent = parent;

	// add "other..." handler
	addOtherHandler();
    }

    /**
       Make a new popup containing all the countries used by sites,
       and set it to a particular value.

       @param code the country to set it to
    */
    public CountryPopup(JDialog parent, String code) {
	this(parent);

	// set the initial value
	setCountry(code);
    }

    /**
       Change the popup to a new country.

       <p>BUG: fails if the country isn't already in the list.</p>

       @param country the code (e.g., "GR") of the country
       @exception IllegalArgumentException if it's not a valid country
       code, or null
    */
    public void setCountry(String code) {
	this.code = code;

	if (code == null)
	    setSelectedIndex(0);
	else {
		try {
			setSelectedItem(Country.getName(code));
		} catch (IllegalArgumentException iae) {
			setSelectedItem(Country.badCountry(code));
		}
	}
    }

    /**
       Returns the country code, or null if "None" is selected.

       @return the code of the country, or null, meaning "None" is
       selected
    */
    public String getCountry() {
    	if (getSelectedIndex() == 0) {
	    	return null;
		}
    	else {
            try {
            	String result = Country.getCode((String) getSelectedItem());
            	return result;
            } catch (IllegalArgumentException iee) {
            	return Country.badCode((String) getSelectedItem());
            }    		
    	}
    }
    
    // returns an array of the country names in the sitedb, sorted
    private String[] getCountryNames() {
        String codes[] = SiteDB.getSiteDB().getCountries();
        String countries[] = new String[codes.length];
        for (int i=0; i<codes.length; i++) {
        	try {
        		countries[i] = Country.getName(codes[i]);
        	}
        	catch (IllegalArgumentException ex) {
        		countries[i] = Country.badCountry(codes[i]);
        	}
        	
        }
            
        Arrays.sort(countries);
	return countries;
    }

    // REFACTOR: wouldn't this be simpler if getCountries() simply
    // returned a list? -- (n.b., DefaultComboBoxModel takes a Vector,
    // but not a List.)

    // return a new array, with "None" being the first element
    // and "Other..." being the last
    private String[] sandwichWithNoneAndOther(String input[]) {
	String output[] = new String[input.length+2];
	output[0] = I18n.getText("site_none"); // TODO: make this the same as countrydialog, somehow
	for (int i=0; i<input.length; i++)
	    output[i+1] = input[i];
	output[output.length-1] = I18n.getText("site_other...");
	return output;
    }

    // the currently selected country code
    private String code = null;

    private void addOtherHandler() {
        addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int index = getSelectedIndex();

		// if it's not "other...", just update |code|
		if (index != getItemCount() - 1) {
		    if (index == 0)
			code = null;
		    else
		    	try {
		    		code = Country.getCode((String) getItemAt(index));
		    	} catch (IllegalArgumentException iee) {
		    		code = null;
		    	}
		    return;
		}

		// now handle "other...":

		// REDUNDANT now!
		String country = code;

		// hide the popup before showing a dialog
		hidePopup();

		// ask the user for a different country;
		// this returns a code (like "GR"), or null.
		// (if the user cancels, simply returns |country|.)
		country = CountryDialog.showDialog(parent,
						   country);

		// whatever it is, it can't stay on "Other...". 
		// select "none", its value, or add it to the popup.

		// case 0: it's <none>.  easy.
		if (country == null) {
		    setSelectedIndex(0);
		    code = null; // REFACTOR!
		    return;
		}

		// case 1: it's in the list.  not too hard.
		// REFACTOR: can i use something like 'contains here?
		String name;
		try {
			name = Country.getName(country);
			for (int i=1; i<getItemCount(); i++) {
		    	if (getItemAt(i).equals(name)) {
		    	setSelectedIndex(i);
				code = country;
				return;
		    	}
			}
		} catch (IllegalArgumentException iee) {
			name = "<uknown country code " + country + ">";
		}

		// case 2: it's not in the list.  ugh.
		// look for where it belongs.
		int i; // (i'll need this outside of the loop)
		for (i=1; i<getItemCount(); i++) {
		    if (((String) getItemAt(i)).compareTo(name) > 0)
			break;
		}
		// then insert it, and select it
		insertItemAt(name, i);
		setSelectedIndex(i);
		code = country; // !!!

		// note: even if the user changed a value so no site
		// contains a particular country, i DON'T remove it
		// from the popup here.  rationale: if you've got 1
		// site with a particular country, and change it, one
		// of the most common things you'd want to be able to
		// do is easily change it back.  since dialogs don't
		// have Edit->Undo, we leave it there.
            }
        });
    }
}
