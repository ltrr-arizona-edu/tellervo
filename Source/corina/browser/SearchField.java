package corina.browser;

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
            public void insertUpdate(DocumentEvent e) { typed(); }
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
    private static String[] parseIntoWords(String text) {
        StringTokenizer tok = new StringTokenizer(text, " ");
        int n = tok.countTokens();
        String words[] = new String[n];
        for (int i=0; i<n; i++)
            words[i] = tok.nextToken();
        return words;
    }
}
