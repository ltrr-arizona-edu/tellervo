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

package edu.cornell.dendro.corina.editor;

import java.util.List;
import javax.swing.JTextField;

import java.awt.EventQueue;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
   An autocompleting text field.  You give it a list of possible
   completions (like web browsers do, for URLs), and it otherwise is
   just like a normal text field.

   <h2>Left to do</h2>
   <ul>
     <li>record frequencies, per dictionary, so the common stuff will
         eventually come up first?
   </ul>

   @see edu.cornell.dendro.corina.tridas.LegacySiteDB

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class AutoComplete extends JTextField {
    // the list of words to look for matches in
    private List dict;
 
    /**
       Make a new autocompleting text field.

       @param text the initial text for the field
       @param columns the number of columns of text to show
       @param dictionary a list of words to autocomplete
    */
    public AutoComplete(String text, int columns, List dictionary) {
	super(text, columns);
	useDictionary(dictionary);
    }

    private void useDictionary(List dictionary) {
	// sort the dictionary first?
	this.dict = dictionary;

	getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {
		    // ignore -- ??
		}
		public void insertUpdate(DocumentEvent e) {
		    // if inserted at end,
		    if (e.getOffset() + 1 == getText().length()) {

			// and matches something from dict
			String completion = matchFromDictionary(getText());
			if (completion == null)
			    return;

			// add it -- but you can't change a document
			// in a documentlistener, or an illegal state
			// exception gets thrown.  (the documentation
			// doesn't seem to mention this rather
			// important fact!)

			final String glue = completion;
			final int curs = e.getOffset();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
				    setText(glue);
				    setSelectionStart(curs+1);
				    setSelectionEnd(getText().length());
				}
			    });
		    }
		}
		public void removeUpdate(DocumentEvent e) {
		    // ignore
		}
	    });
    }

    // (or null, if nothing matches)
    private String matchFromDictionary(String text) {
	String completion = null;
	for (int i=0; i<dict.size(); i++) {
	    if (((String) dict.get(i)).toUpperCase().startsWith(text.toUpperCase())) {
		return (String) dict.get(i);
	    }
	}
	return null;
    }
}
