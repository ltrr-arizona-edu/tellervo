package corina.util;

import java.util.List;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Box;

// interface:
// -- alignment: left/center/right/(leading/trailing?)
// -- valignment?
// -- justification, for left-aligned, centered text?

// result is boxlayout/y inside a (borderlayout?)

public class JLinedLabel extends JPanel {

    // (strategy: if \r's but no \n's, replace all \r's with \n's; otherwise, just remove all \r's)
    // treat \n, \n\r, \r\n, or \r as newlines
    // but 2 newlines in a row are unique ("a\n\nb" is "a", a blank line, then "b")
    // are trailing newlines significant?  ok, chief!
    private static List chop(String str) {
	// if \r's but no \n's, replace all \r's with \n's
	if (str.indexOf('\r') != -1 && str.indexOf('\n') == -1)
	    str = str.replace('\r', '\n');

	// TODO: remove all \r's, now

	List l = new ArrayList();

	int start = 0, newline;
	do {
	    newline = str.indexOf('\n', start);

	    if (newline == -1) {
		l.add(str.substring(start));
		break;
	    } else {
		l.add(str.substring(start, newline));
		start = newline + 1;
	    }

	} while (newline != -1 && start < str.length());

	return l;
    }

    public JLinedLabel(String s) {
	// chop s into lines
	List text = chop(s);

	// nested layout manager
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

	setLayout(new BorderLayout());
	add(p, BorderLayout.CENTER);

	// set layout manager, and add lines
	for (int i=0; i<text.size(); i++) {
	    JLabel label = new JLabel((String) text.get(i));
	    lines.add(label);
	    p.add(label);
	}
    }

    // list of labels, for setting font, etc.
    private List lines = new ArrayList();

    // also set fonts for all sub-jlabels
    public void setFont(Font f) {
	super.setFont(f);
	if (lines == null)
	    System.out.println("lines is fucking null");
	else
	    for (int i=0; i<lines.size(); i++) {
		((JLabel) lines.get(i)).setFont(f);
	    }
    }

    public static void main(String args[]) {
	JFrame f = new JFrame();
	f.getContentPane().add(new JLinedLabel("abc\ndefghi\njkl"));
	// f.getContentPane().add(new JLabel("abc"));
	f.pack();
	f.show();
    }
}
