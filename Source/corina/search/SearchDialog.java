package corina.search;

import corina.MetadataTemplate;
import corina.prefs.Prefs;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.gui.Layout;
import corina.gui.Help;
import corina.gui.Bug;
import corina.util.OKCancel;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Window;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.*;

/*
  design:

  [ ============ Search for Samples ============ ] <--- stage 3, rename to "Smart List Rules"
  Description: [                               ] <--- for stage 3 (smart lists) only

  If [any v] of the following conditions are met:

    + ---------------------------------------- +
    | [Title   v] [Contains v] [23   ] (-) (+) | <--- this (-) button is dimmed if there's only one rule
    | ---------------------------------------- |
    | [Author  v] [Contains v] [Peter] (-) (+) |
    + ---------------------------------------- +

  (  Help  )                 ( Cancel ) ((Search)) <--- "ok" for smart list rules dialog
  | ____________________________________________ |

  deployment strategy:
  -- stage 1:
     -- get it to work, slowly
     -- (plug it in to browser, perhaps)
     -- it'll need a JFrame to display results in
  (implement summary.java)
  -- stage 2:
     -- refactor to use summary.java
     -- it'll be blazingly fast now
     -- and users will love me
  (but that's not all)
  -- stage 3:
     -- there will be less reason to have an explicit "search" dialog
     -- add "smart lists"
     -- they'll automatically update
     -- example: "all files modified today"
     -- they'll live in the top-level of the browser
     -- store in user prefs (need simple, maybe single-line, serialization, then)

  design:

  [ ============ Corina Browser ============ ]
  | Source  | Site         | Folder          |
  | Library | ABC          |                 |
  | ITRDB   | ACM          |                 |
  | Modifi..| AMO          |                 | <--- the top half of this disappears when you select a smart list
  | Gordio..|--------------------------------|
  |         | Sample     Range      ...      |
  |         |                                |
  |         |                                |
  |         |                                |
  |_________|________________________________|

  but that's neither here nor there.  actually it's there, but not here.
 */

public class SearchDialog extends JDialog {

    private JComboBox anyAll;

    private JPanel makeAnyAllLine() {
	JLabel label1 = new JLabel(I18n.getText("search_if_1") + " ");
	anyAll = new JComboBox(new String[] { I18n.getText("search_all"),
					      I18n.getText("search_any") });
	JLabel label2 = new JLabel(" " + I18n.getText("search_if_2"));

	JPanel flow = Layout.flowLayoutL(label1, anyAll, label2);
	flow.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

	return flow;
    }

    private JButton help, cancel, ok; // these needed permanently?

    private JPanel makeButtons() {
	help = Builder.makeButton("help");
	cancel = Builder.makeButton("cancel");
	ok = Builder.makeButton("search");

	Help.addToButton(help, "searching");

	// TODO: also make it dispose-on-close (is that not the default for dialogs?)
	cancel.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	    });

	ok.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    dispose();

		    Search s = new Search();
		    s.setType(anyAll.getSelectedItem().equals(I18n.getText("search_any")) ? Search.ANY : Search.ALL);

		    for (int i=0; i<lines.size(); i++) {
			Line l = (Line) lines.get(i);

			// make a criterion -- REFACTOR!
			Criterion crit=null;

			// figure out what lhs type and comparison are
			int j = l.popup1.getSelectedIndex();
			int lhsType = getType(j);
			String comparison = (String) l.popup2.getSelectedItem();

			// what's the left field?
			String field = null;
			{
			    // given the (english) name of the LHS field,
			    // get the variable name.

			    // name of the field;
			    String name = (String) l.popup1.getSelectedItem();

			    // look up the key
			    Iterator ii = MetadataTemplate.getFields();
			    while (ii.hasNext()) {
				MetadataTemplate.Field f = (MetadataTemplate.Field) ii.next();
				if (f.getDescription().equals(name))
				    field = f.getVariable();
			    }

			    // if it's not a Metadata field, then what?  (hack!)
			    if (field == null) {
				if (name.equals("Filename"))
				    field = "filename";
				else if (name.equals("Date Modified"))
				    field = "moddate";
				else if (name.equals("Start Year"))
				    field = "start";
				else if (name.equals("End Year"))
				    field = "end";
				else if (name.equals("Length"))
				    field = "length";
				// TODO: any others?  (this is really icky!)
			    }
			}

			// (why's this so ugly?  the components have to extends various other components,
			// so getData() needs to be on an interface.  but i also want to be able to
			// construct them, and an interface can't be a factory.  so i'll always need
			// a class and an interface -- and sticking one inside the other is the nicest,
			// i think.)
			Object rhsValue = ((DataComponent.AbstractDataComponent) l.rhsField).getData();

			// make a Criterion object for (lhs-field, comparison, rhs-value).
			crit = Criterion.makeCriterion(lhsType,
						       field,
						       comparison,
						       rhsValue);
			// ASSUMES: rhsField is a DataComponent, which it isn't guaranteed to be (yet).

			// is this criterion fast to check?
			boolean fast = (field.equals("filename") || field.equals("moddate"));

			s.addCriterion(crit, fast);
		    }

		    // s.setFolder("/Users/kharris/Documents/Corina/corina/Demo Data/chil/");
		    // s.setFolder(System.getProperty("user.dir"));
		    s.setFolder(Prefs.getPref("corina.dir.data"));
		    s.run();
		}
	    });

	JPanel buttons = Layout.buttonLayout(help, null, cancel, ok);
	buttons.setBorder(BorderFactory.createEmptyBorder(14, 20, 18, 20));
	return buttons;
    }

    public SearchDialog() {
	setTitle("Find Samples");

	// center: criteria list
	lines = new ArrayList();
	linePanel = new JPanel(new GridLayout(0, 1));
	linePanel.setBackground(Color.white);
	linePanel.setOpaque(true);
	linePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), // outside
							       BorderFactory.createEmptyBorder(3, 5, 3, 5))); // inside

	// WRITEME: explain why the border/spacing is so messed up, why i can't use flowlayout,
	// and what's with the crazy border spacing (2/3/5).

	// add 1 line, to start out
	Line line = new Line(this);
	line.minus.setEnabled(false);
	lines.add(line);
	linePanel.add(line);

	setContentPane(Layout.borderLayout(makeAnyAllLine(),
					    Box.createHorizontalStrut(20), linePanel, Box.createHorizontalStrut(20),
					    makeButtons()));

	// ok/cancel
	OKCancel.addKeyboardDefaults(ok);

	pack();
	setResizable(false);
	show();
    }

    // panel to hold the lines
    private JPanel linePanel;
    private List lines;

    // REFACTOR: this is such a big class, shouldn't it be in its own file?
    private class Line extends JPanel {
	// so anybody can enable/disable it -- ugh
	JButton minus;

	// hmm...
	private Window window;

	// the popups
	private JComboBox popup1, popup2;

	Line(Window w) {
	    this.setOpaque(false);
	    this.setBorder(BorderFactory.createEmptyBorder(2, 0, 3, 0));

	    window = w;

	    // flow(left): popup, popup (changes!), textfield, -/+
	    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	    // put a bunch of file properties, plus all the metadata, into a list (copied partially from browser.java)

	    // TODO: this vector is read-only, and only used here.  make it static, and final (immutable?).
	    Vector metaFields = new Vector(); // JComboBox takes a Vector, but not a List (grr)
	    metaFields.add("Filename");
	    metaFields.add("Date Modified");

	    metaFields.add("Start Year");
	    metaFields.add("End Year");
	    metaFields.add("Length");

	    Iterator i = MetadataTemplate.getFields();
	    while (i.hasNext()) {
		metaFields.add(((MetadataTemplate.Field) i.next()).getDescription());
	    }

	    // FIXME: display these as they are now, but the back-end needs to know it by some other name, right?
	    popup1 = new JComboBox(metaFields);
	    popup1.setMaximumRowCount(metaFields.size()); // just take them all

	    // watch for events here
	    popup1.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// update popup2 from popup1's selection
			updateComparisonPopup();

			// update the third column, too.
			updateRHSComponent();
			// why isn't this called automatically?  because i disable
			// rhs-updating while changing the middle component --
			// otherwise it'd get updated at remove-all, and each add-item
			// (being slow if it worked, and throwing exceptions along the way)

			// repaint();
			restuff(window);
		    }
		});

	    // create popup2, the comparison popup, and set its initial values
	    popup2 = new JComboBox();
	    updateComparisonPopup();

	    popup2.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// just update the third component now
			if (!updatingPopup) {
			    updateRHSComponent();

			    // repaint();
			    restuff(window);
			}
		    }
		});

	    final Line glue = this;

	    minus = new JButton(Builder.getIcon("minus.png"));
	    minus.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// remove this line
			lines.remove(glue);
			linePanel.remove(glue);

			// pack/redraw
			restuff(window);

			// first minus should now be disabled, IF it's the only line left
			if (lines.size() == 1) {
			    Line first = (Line) lines.get(0);
			    first.minus.setEnabled(false);
			}
		    }
		});

	    JButton plus = new JButton(Builder.getIcon("plus.png"));
	    plus.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			// add a new line
			Line line = new Line(window);
			lines.add(line);
			linePanel.add(line);

			// pack/redraw
			restuff(window);

			// first minus should now be enabled
			Line first = (Line) lines.get(0);
			first.minus.setEnabled(true);
		    }
		});

	    // FIXME: this may not be a textfield (date mod / is within / {1 day, 2 days, ...}),
	    // or may even disappear (date mod / is today / ---).
	    // JTextField text = new JTextField("", 20);

	    this.add(popup1);
	    this.add(Box.createHorizontalStrut(5));
	    this.add(popup2);
	    this.add(Box.createHorizontalStrut(5));
	    this.add(new JLabel("--temp--"));
	    this.add(Box.createHorizontalStrut(5));
	    this.add(minus);
	    this.add(Box.createHorizontalStrut(5));
	    this.add(plus);

	    updateRHSComponent();
	}

	// need to disable listeners on popup2 while i do this
	private void updateComparisonPopup() {
	    // DESIGN: keep track of old type, and don't do anything if it's the same

	    updatingPopup = true;

	    // figure out what popup1 says
	    int i = popup1.getSelectedIndex(); // also: getSelectedItem()
	    int type = getType(i);

	    popup2.removeAllItems();

	    String c[] = Criterion.getComparisonsForType(type);
	    for (int j=0; j<c.length; j++)
		popup2.addItem(c[j]);

	    updatingPopup = false;

	    // (nope, no need to repaint)
	}
	private boolean updatingPopup=false;

	private Component rhsField;
	private int oldRHSType=-1; // make sure it's initially an invalid value!

	private void updateRHSComponent() {
	    // figure out what lhs type and comparison are
	    int i = popup1.getSelectedIndex();
	    int lhsType = getType(i);
	    String comparison = (String) popup2.getSelectedItem();

	    // ask the criterion class what the rhs type for this (lhs, crit) is
	    int rhsType = Criterion.getRHSType(lhsType, comparison);

	    // if rhs = old rhs type, don't do anything! -- unless it's a new popup
	    // BUG: if it's type=POPUP but it's the same popup, don't change it, either.
	    if (rhsType==oldRHSType && rhsType!=POPUP) {
		rhsField.requestFocus();
		return;
	    }
	    oldRHSType = rhsType;

	    if (rhsType == POPUP) {
		// REFACTOR: DataComponent should be able to handle this, too
		// (well, it does, kind of.)

		try {
		    // OLD HACK! -- no, wait, i need this here, unless i want to add
		    // all the metadata info to criterion.java, as well.
		    // -- well, it shouldn't use try/catch, anyway.
		    rhsField = DataComponent.makeMetaComponent((String) popup1.getSelectedItem());

		    // DESIGN: so, this shouldn't be simply POPUP.
		    // it should be META(?) and POPUP.

		} catch (Exception e) {
		    // new (and better) way

		    // make popup from criterion's values
		    Object o[] = Criterion.getRHSValues(lhsType, comparison);
		    /*
		    Vector v = new Vector();
		    for (int j=0; j<o.length; j++)
			v.add(o[j]);
		    rhsField = new JComboBox(v);
		    */

		    // new-new way:
		    rhsField = DataComponent.makePopupComponent(o);
		}

	    } else {
		// make a component to display/edit the type |rhsType|.
		// (the simplicity of the next line completely belie
		// how powerful, how effective the component truly is.)
		rhsField = DataComponent.makeComponent(rhsType);
	    }

	    // remove old component, add new rhs component
	    this.remove(4);
	    this.add(rhsField, 4);
	    // FIXME: hardcoded, and completely unintuitive because of all the spacing struts

	    // request-focus when done
	    rhsField.requestFocus();
	}

	// OOPS: go ahead and make a new popup, because this becomes the THIRD component,
	// not the second.  the popup should be "is"/"is not".
	// this used to be: makeMetaPopup().
	// now it's: DataComponent.makeMetaComponent()
    }

    /*
      to-do list, immediate:
      -- hook up front-end to back-end (is it written yet?)

      -- make search actually do something
      -- (write output window)
      -- can just print them to stdout for testing, but a table (updated in a worker thread) is ideal
      -- (write search routine)

      ====

      to-do list, future:
      -- after the text field, length should say "years"
      -- (i'm seeing a pattern ... number, lengthnumber, yearnumber?)

      -- make sure "find..." is in all windows, not just editor windows
      -- put this in the main window, especially (though it won't be around for much longer)
      -- document this in a manual section/chapter (file browser chapter?)

      -- [perf] if the only criteria are filename and/or moddate, don't load the file.
      ---- add "needs data?" flag for criteria?
      ---- no, better: Element.get(meta) loads file lazily

      -- clicking "plus" should add a line after that line, not at the end

      -- use working directory for default search root
      ---- fix it so using open-recent changes the working directory, as well

      -- "comments" "contains" is an awkward construction (what to do about it?  just singularize it?)

      -- for POPUP type, if there are only 2 possible values, the comparator popup should simply be the label "is" (!!!)
      ---- no, that's too hard, for no significant gain.
      -- try (slightly) harder to line up everything: they should only be skewed if i really need it

      -- real-time verification: only allow numbers to be typed in NUMBER fields, for example
      -- real-time verification: each line should be able to report if it's in a valid state or not: a blank NUMBER field, for example, isn't valid.  if any line is not valid, "ok"/"search" shouldn't be enabled.

      -- when i'm using this for smart lists, it might be useful to select only a few:
      ---- e.g., pick 10 masters of length>100, species=oak, by greatest length
     */

    // DESIGN: this should take the field name, like "unmeas_pre", or "start", not the index.
    // REFACTOR: this is only used by Line.
    private int getType(int selection) {
	int i = selection; // "selection" is such a long word...
	int type;
	if (i==2 || i==3)
	    type = YEAR;
	else if (i==4)
	    type = LENGTH;
	else if (i==8 || i==9 || i==14)
	    type = NUMBER;
	else if (i==1)
	    type = DATE;
	else if (i==7 || i==10 || i==12 || i==15 || i==16 || i==17 || i==18 || i==19) // 13=index, too, ideally
	    type = POPUP;
	else
	    type = TEXT;
	return type;
    }

    // types -- re-sort these when i'm done playing with them
    public static final int NONE = 0;
    public static final int TEXT = 1;
    public static final int NUMBER = 2;
    public static final int DATE = 3;
    public static final int YEAR = 4;
    public static final int POPUP = 5;
    public static final int LENGTH = 6;

    private static void restuff(Window w) {
	w.pack();
	w.invalidate();
	w.repaint();
	Toolkit.getDefaultToolkit().sync();
    }

    public static void main(String args[]) throws Exception {
	// metal: UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	SearchDialog d = new SearchDialog();
    }
}
