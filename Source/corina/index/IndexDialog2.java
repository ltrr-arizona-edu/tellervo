package corina.index;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import corina.Sample;
import corina.core.App;
import corina.graph.Graph;
import corina.graph.GrapherPanel;
import corina.gui.Layout;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.ui.Builder;

// south=buttons
// center=graph (preview)
// west(east?)={north=radio, south=options}

/*
  exponential: (none)
  polynomial: degree
  cubic spline: smoothing
  floating avg: num (odd)
  high-pass: values (any) -- how?
*/

public class IndexDialog2 extends JDialog {

    private JPanel left;
    private JPanel pick;
    private JComponent options;

    private void makePicker() {
	JRadioButton r1 = new JRadioButton("Exponential");
	JRadioButton r2 = new JRadioButton("Polynomial");
	JRadioButton r3 = new JRadioButton("Cubic Spline");
	JRadioButton r4 = new JRadioButton("Floating Average");
	JRadioButton r5 = new JRadioButton("High-Pass Filter");
	ButtonGroup bg = new ButtonGroup();
	bg.add(r1);
	bg.add(r2);
	bg.add(r3);
	bg.add(r4);
	bg.add(r5);

	r1.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    left.remove(options);
		    options = new ExponentialOptions();
		    left.add(options);

		    Exponential exp = new Exponential(sample);
		    exp.run();
		    useIndex(exp);

		    invalidate();
		    repaint();
 		    // toolkit.sync?  nope.
		}
	    });

	r2.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    left.remove(options);
		    options = new PolynomialOptions();
		    left.add(options);

		    Polynomial p = new Polynomial(sample, 6);
		    p.run();
		    useIndex(p);

		    invalidate();
		    repaint();
		}
	    });

	r3.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    left.remove(options);
		    options = new SplineOptions();
		    left.add(options);

		    CubicSpline c = new CubicSpline(sample);
		    c.run();
		    useIndex(c);

		    invalidate();
		    repaint();
		}
	    });

	r4.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    left.remove(options);
		    options = new FloatingOptions();
		    left.add(options);

		    Floating f = new Floating(sample, 11);
		    f.run();
		    useIndex(f);

		    left.invalidate();
		    left.repaint();
		}
	    });

	r5.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    left.remove(options);
		    options = new HighPassOptions();
		    left.add(options);

		    left.invalidate();
		    left.repaint();
		}
	    });

	JPanel box = new JPanel();
	box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
	box.add(r1);
	box.add(r2);
	box.add(r3);
	box.add(r4);
	box.add(r5);

	pick = new JPanel(new BorderLayout());
	pick.add(new JLabel("Pick index type:"), BorderLayout.NORTH);
	pick.add(Box.createHorizontalStrut(12), BorderLayout.WEST);
	pick.add(box);
	pick.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

	r1.setSelected(true);
    }

    // raw Numbers: the index itself
    private java.util.List index = new java.util.ArrayList();

    private void useIndex(Index newIndex) {
	if (index.isEmpty())
	    // first time, index is empty, so add all
	    index.addAll(newIndex.data);
	else
	    // after that, just copy in this data
	    java.util.Collections.copy(index, newIndex.data);

	// repaint
	preview.repaint();
    }

    // exponential options
    private static class ExponentialOptions extends JPanel {
	// no options
    }

    // "Spline Options: ... Tight ---*---, Loose
    private class SplineOptions extends JPanel {
	// think: tight=1e-inf ... loose=1e-0
	SplineOptions() {
	    setLayout(new BorderLayout()); //  FlowLayout());

	    final JSlider s = new JSlider(JSlider.VERTICAL, -320, 0, -160);
	    s.setInverted(true);

	    JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    center.add(s);

	    add(new JLabel("Tight", JLabel.CENTER), BorderLayout.NORTH);
	    add(center, BorderLayout.CENTER);
	    add(new JLabel("Loose", JLabel.CENTER), BorderLayout.SOUTH);

	    s.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			double exp = (double) s.getValue() / 10.0;
			double sval = Math.pow(10, exp);

			// TODO: change this if pref changes
			// BUT: don't get stuck in an infinite loop!
			// (is that my job or prefs'?)

			/*
			  NOTE: this is just (1) for show, because it's an
			  impressive demonstration of my new prefs api,
			  (2) as a speed test, since if it can handle this,
			  it'll probably work well for normal uses, and
			  (3) a correctness test (though i'm fairly certain
			  it's working).

			  so when i'm done getting prefs working, remove
			  this.  proper behavior: when the dialog is closed,
			  set the pref.
			*/

			App.prefs.setPref("corina.index.cubicfactor", String.valueOf(sval));
			// "1e" + s.getValue());

			CubicSpline cs = new CubicSpline(sample);
			cs.run();
			// System.out.println("-- chi^2 = " + cs.getChi2());
			// System.out.println("-- rho = " + cs.getR());

			useIndex(cs);
		    }
		});

	    App.prefs.addPrefsListener(new PrefsListener() {
		    public void prefChanged(PrefsEvent e) {
			if (e.getPref().equals("corina.index.cubicfactor")) {
			    String p = App.prefs.getPref("corina.index.cubicfactor");
			    double sval = Double.parseDouble(p);
			    double real = 10 * Math.log(sval) / Math.log(10);
			    s.setValue((int) real);
			    // System.out.println("-- c.i.cubicfactor changed!");
			}
		    }
		});
	}
    }

    // "Polynomial Options: Degree: [0]
    private class PolynomialOptions extends JPanel {
	PolynomialOptions() {
	    setLayout(new FlowLayout(FlowLayout.LEFT));

	    final int N = 12;
	    java.util.Vector v = new java.util.Vector();
	    for (int i=0; i<=N; i++)
		v.add(String.valueOf(i));
	    final JComboBox b = new JComboBox(v);
	    b.setSelectedIndex(6); // EXTRACT CONST!
	    b.setMaximumRowCount(N+1);

	    add(new JLabel("Degree:"));
	    add(b);

	    b.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			int degree = b.getSelectedIndex();

			Polynomial p = new Polynomial(sample, degree);
			p.run();
			// System.out.println("-- chi^2 = " + cs.getChi2());
			// System.out.println("-- rho = " + cs.getR());

			useIndex(p);
		    }
		});
	}
    }

    // "Floating avg Options: years ---*---
    private class FloatingOptions extends JPanel {
	FloatingOptions() {
	    setLayout(new BorderLayout());

	    final JSlider s = new JSlider(JSlider.VERTICAL, 0, 12, 5); // (use 2*x+1)
	    s.setInverted(true);

	    add(new JLabel("1"), BorderLayout.NORTH);
	    add(s, BorderLayout.CENTER);
	    add(new JLabel("25"), BorderLayout.SOUTH);

	    s.addChangeListener(new ChangeListener() {
		    public void stateChanged(ChangeEvent e) {
			int points = s.getValue() * 2 + 1;

			Floating f = new Floating(sample, points);
			f.run();
			// System.out.println("-- chi^2 = " + cs.getChi2());
			// System.out.println("-- rho = " + cs.getR());

			useIndex(f);
		    }
		});
	}
    }

    // any number of values?
    private static class HighPassOptions extends JPanel {
	// WRITEME
    }

    private JPanel p;

    private Sample sample;

    public IndexDialog2(Sample s /*, JFrame owner*/) {
        // super(owner);
        // setModal(true);
	setTitle("Index of: " + s);

	this.sample = s;

	p = new JPanel(new BorderLayout());
	setContentPane(p);
 
	{
	    JButton help = Builder.makeButton("help");
	    JButton cancel = Builder.makeButton("cancel");
	    JButton ok = Builder.makeButton("ok");
	    JPanel b = Layout.buttonLayout(help, null, cancel, ok);
	    b.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
	    p.add(b, BorderLayout.SOUTH);
	}

	{
	    makePicker();
	    options = new SplineOptions(); // (why don't i get this for free, above?)

	    left = new JPanel(new GridLayout(0, 1));
	    left.add(pick);
	    left.add(options);

	    p.add(left, BorderLayout.WEST);
	}

	{
	    // make list of graphs, for grapher panel
	    java.util.List l = new java.util.ArrayList();
	    l.add(new Graph(s));
	    Graph ii = new Graph(index, s.range.getStart(), "--index--");
	    ii.scale = 0.1f;
	    l.add(ii);

	    // create graph preview for |l|
	    preview = new GrapherPanel(l, new corina.graph.PlotAgents(), new JFrame());
	    ((GrapherPanel) preview).setScroller(new JScrollPane());
	    p.add(preview, BorderLayout.CENTER);
	}

	pack();
	setSize(640, 480);
	show();
    }

    private JComponent preview;

    public static void main(String args[]) throws Exception {
	// corina.browser.ITRDB itrdb = new corina.browser.ITRDB();
	// Sample s = itrdb.getSample("measurements/europe/yugo004.rwl");
	Sample s = new Sample(args[0]);
	new IndexDialog2(s);

	Sample s2 = new Sample(args[1]);
	new IndexDialog2(s2);
    }
}
