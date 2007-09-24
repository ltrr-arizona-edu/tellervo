package edu.cornell.dendro.corina.browser;

import java.util.StringTokenizer;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchField extends JTextField {
    // reset text to ""
    public void reset() {
        setText("");
    }

    // is empty?
    public boolean isEmpty() {
        return getText().length()==0;
    }

    // construct new searchfield
    public SearchField(Browser browser, Component eventTarget) {
        super("", 7);

        // prevent it from stretching
        setMaximumSize(getPreferredSize());

        // call browser.dosearch() when something is typed
        final Browser glue = browser;
        getDocument().addDocumentListener(new DocumentListener() {
		public void changedUpdate(DocumentEvent e) { typed(); }
		public void insertUpdate(DocumentEvent e) {
		    try {

		    // if it's something typed at the end,
		    if (e.getOffset() == e.getDocument().getLength() - 1) {
			char c = e.getDocument().getText(e.getOffset(), 1).charAt(0);

			// -- if it's a space, do nothing but BUG: it should update the status bar ("focused")
			if (c == ' ')
			    return;

			// -- if it's a letter, restrict search to only things which are now visible
			glue.doSearchRestrict();

		    } else {
			// otherwise, just run the whole search
			typed();
		    }

		    } catch (javax.swing.text.BadLocationException ble) {
			// ignore -- can't happen
		    }
		}
		public void removeUpdate(DocumentEvent e) { typed(); }
		private void typed() {
		    glue.doSearch();
		}
	    });

        // pass arrow-key, etc., events on to eventTarget
        final Component table = eventTarget;
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_UP ||
                    code == KeyEvent.VK_PAGE_UP || code == KeyEvent.VK_PAGE_DOWN ||
                    code == KeyEvent.VK_ENTER) {
                    table.requestFocus(); // focus on the table, and ...
                    table.dispatchEvent(e); // ... pass arrow-key event to it.
                } else if (code == KeyEvent.VK_ESCAPE) {
                    // esc clears field.  very handy.
                    reset();
//                } else if (e.getModifiers() != 0) {
//                    table.dispatchEvent(e); // for control-<number> on win32, especially
//                    e.consume(); // but don't add to text field!
                }
            }
            // ignore keyReleased, keyPressed events
        });
    }

    // get text as words, all lower-case
    public String[] getTextAsWords() {
        return parseIntoWords(getText().toLowerCase());
    }

    // parse |text| into an array of  words
    // REFACTOR: StringUtils.splitBy() is very similar to this (except this trims the result)
    private static String[] parseIntoWords(String text) {
        StringTokenizer tok = new StringTokenizer(text, " ");
        int n = tok.countTokens();
        String words[] = new String[n];
        for (int i=0; i<n; i++)
            words[i] = tok.nextToken();
        return words;
    }
}
