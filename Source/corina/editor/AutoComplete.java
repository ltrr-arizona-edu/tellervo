package corina.editor;

import corina.site.SiteDB;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JTextField;

import java.awt.EventQueue;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

// DEBUG ONLY
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
   An autocompleting text field.  You give it a list of possible completions,
   and it otherwise is just like a normal text field

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class AutoComplete extends JTextField {

    // TODO: record frequencies, per dictionary, so the common stuff will
    // eventually come up first?

    private List dict;
 
    /**
       Make a new autocompleting text field, using all of the site
       names from the database.

       @param text the initial text for the field
       @param columns the number of columns of text to show
       @param dictionary a list of words to autocomplete
    */
    public AutoComplete(String text, int columns) {
	this(text, columns, SiteDB.getSiteDB().getSiteNames());
    }

    /**
       Make a new autocompleting text field.

       @param text the initial text for the field
       @param columns the number of columns of text to show
       @param dictionary a list of words to autocomplete
    */
    public AutoComplete(String text, int columns, List dictionary) {
	super(text, columns);

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
			String completion = null;
			for (int i=0; i<dict.size(); i++) {
			    if (((String) dict.get(i)).startsWith(getText())) {
				completion = (String) dict.get(i);
				break;
			    }
			}

			if (completion == null)
			    return;

			// add it -- but you can't change a document in a documentlistener, or
			// an illegal state exception gets thrown.
			// (the documentation doesn't seem to mention
			// this important fact.)
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

    // DEBUG ONLY
    public static void main(String args[]) throws Exception {
	List dict = new ArrayList();
	dict.add("lorem");
	dict.add("ipsum");
	dict.add("dolor");
	dict.add("sit");
	dict.add("amet");

	JTextField a = new AutoComplete("", 30, dict);

	JFrame f = new JFrame();
	f.getContentPane().add(a, BorderLayout.NORTH);
	f.pack();
	f.show();

	a.requestFocus();
    }
}
