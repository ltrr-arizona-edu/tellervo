package corina.gui;

import corina.gui.layouts.ButtonLayout;

import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;

// TODO: gpl header, javadoc!

// a factory for easily creating panels of a given layout.
// for example,
// JPanel p = Layout.borderLayout(null,
//                                null, content, null,
//                                Layout.buttonLayout(cancel, ok));

// TODO: if cancel comes before ok, and os=win32, swap them?
// TODO: accept 1, 2, 3, ... (...5?) components directly (not in array)

// WRITEME: i'd like to be able to put an integer in any slot for a strut, but i can't.
// -- BETTER: class Strut(w,h) -- e.g., Layout.flowLayoutL("Value:", Strut.w(4), myField);

// TODO: i tend to need to use temp vars for the output of these methods,
// simply to set custom borders on them.  i think going to an XML generator
// might actually help with that.

// FIXME: consolidate componentOrGlue(), componentOrString() -- it
// should be pretty much the same inputs for any of these methods.

public class Layout {
    // add a component, if non-null, or add some glue
    private static Component componentOrGlue(Component c) {
	return (c != null ? c : Box.createHorizontalGlue());
    }

    // --------
    // button layout
    public static JPanel buttonLayout(Component c1) {
	JPanel p = new JPanel(new ButtonLayout());
	p.add(componentOrGlue(c1));
	return p;
    }
    public static JPanel buttonLayout(Component c1, Component c2) {
	JPanel p = new JPanel(new ButtonLayout());
	p.add(componentOrGlue(c1));
	p.add(componentOrGlue(c2));
	return p;
    }
    public static JPanel buttonLayout(Component c1, Component c2, Component c3) {
	JPanel p = new JPanel(new ButtonLayout());
	p.add(componentOrGlue(c1));
	p.add(componentOrGlue(c2));
	p.add(componentOrGlue(c3));
	return p;
    }
    public static JPanel buttonLayout(Component c1, Component c2, Component c3,
				      Component c4) {
	JPanel p = new JPanel(new ButtonLayout());
	p.add(componentOrGlue(c1));
	p.add(componentOrGlue(c2));
	p.add(componentOrGlue(c3));
	p.add(componentOrGlue(c4));
	return p;
    }
    public static JPanel buttonLayout(Component c1, Component c2, Component c3,
				      Component c4, Component c5) {
	JPanel p = new JPanel(new ButtonLayout());
	p.add(componentOrGlue(c1));
	p.add(componentOrGlue(c2));
	p.add(componentOrGlue(c3));
	p.add(componentOrGlue(c4));
	p.add(componentOrGlue(c5));
	return p;
    }

    // --------
    // border layout is verbose, too.  let's see what we can do about that.
    public static JPanel borderLayout(             Component n,
				      Component w, Component c, Component e,
						   Component s) {
	JPanel p = new JPanel(new BorderLayout()); // is this default, anyway?
	if (n != null)
	    p.add(n, BorderLayout.NORTH);
	if (w != null)
	    p.add(w, BorderLayout.WEST);
	if (c != null)
	    p.add(c, BorderLayout.CENTER);
	if (e != null)
	    p.add(e, BorderLayout.EAST);
	if (s != null)
	    p.add(s, BorderLayout.SOUTH);
	return p;
    }

    // --------
    // box layout...
    public static JPanel boxLayoutY(Component c1) {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(c1);
	return p;
    }
    public static JPanel boxLayoutY(Component c1, Component c2) {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(c1);
	p.add(c2);
	return p;
    }
    public static JPanel boxLayoutY(Component c1, Component c2, Component c3) {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(c1);
	p.add(c2);
	p.add(c3);
	return p;
    }
    public static JPanel boxLayoutY(Component c1, Component c2, Component c3,
				    Component c4) {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	p.add(c1);
	p.add(c2);
	p.add(c3);
	p.add(c4);
	return p;
    }

    // -------------
    // flow layout...
    public static JPanel flowLayoutL(Object o1) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	p.add(componentOrString(o1));
	return p;
    }
    public static JPanel flowLayoutL(Object o1, Object o2) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	p.add(componentOrString(o1));
	p.add(componentOrString(o2));
	return p;
    }
    public static JPanel flowLayoutL(Object o1, Object o2, Object o3) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	p.add(componentOrString(o1));
	p.add(componentOrString(o2));
	p.add(componentOrString(o3));
	return p;
    }
    public static JPanel flowLayoutL(Object o1, Object o2, Object o3, Object o4) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	p.add(componentOrString(o1));
	p.add(componentOrString(o2));
	p.add(componentOrString(o3));
	p.add(componentOrString(o4));
	return p;
    }
    public static JPanel flowLayoutL(Object o1, Object o2, Object o3,
				     Object o4, Object o5) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	p.add(componentOrString(o1));
	p.add(componentOrString(o2));
	p.add(componentOrString(o3));
	p.add(componentOrString(o4));
	p.add(componentOrString(o5));
	return p;
    }

    public static JPanel flowLayoutC(Object o1) {
	JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
	p.add(componentOrString(o1));
	return p;
    }

    // FIXME: in ANY layout, a string should be made into a jlabel automatically.
    private static Component componentOrString(Object o) {
	return (o instanceof String ? new JLabel((String) o) : (Component) o);
    }

    // -------------
    // grid layout...
    public static JPanel gridLayout(Component o[][]) {
	int rows = o.length;
	int cols = o[0].length;
	JPanel p = new JPanel(new GridLayout(rows, cols));
	for (int i=0; i<rows; i++)
	    for (int j=0; j<cols; j++)
		p.add(o[i][j]);
	return p;
    }

    public static JPanel gridLayout(Component o[][], int hgap, int vgap) {
	int rows = o.length;
	int cols = o[0].length;
	JPanel p = new JPanel(new GridLayout(rows, cols, hgap, vgap));
	for (int i=0; i<rows; i++)
	    for (int j=0; j<cols; j++)
		p.add(o[i][j]);
	return p;
    }
}
