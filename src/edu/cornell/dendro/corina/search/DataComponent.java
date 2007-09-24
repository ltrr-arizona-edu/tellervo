package edu.cornell.dendro.corina.search;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.MetadataTemplate;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.util.ColorUtils;
import edu.cornell.dendro.corina.ui.I18n;

import java.util.Date;
import java.util.Vector;
import java.util.Iterator;

import java.text.DateFormat;
import java.text.ParseException;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;

// note: this is approaching the abstraction level that it appears
// CLIM has, where you specify first WHAT you want to do/input, and
// only secondarily (optionally?) how you want it to look.

public class DataComponent {
  private static final CorinaLog log = new CorinaLog(DataComponent.class);

    // TODO: doesn't handle POPUP yet.
    public static Component makeComponent(int type) {
	switch (type) {
	case SearchDialog.NONE:
	    return new NoneComponent();
	case SearchDialog.YEAR:
	    return new YearComponent();
	case SearchDialog.NUMBER:
	    return new NumberComponent();
	case SearchDialog.LENGTH:
	    return new LengthComponent();
	case SearchDialog.DATE:
	    return new DateComponent();
	    //	case SearchDialog.POPUP:
	    //	    return new PopupComponent()
	default:
	    return new StringComponent();
	}
    }

    // slightly less of a hack
    public static Component makePopupComponent(Object rhsValues[]) {
	// put into vector
	Vector v = new Vector();
	for (int i=0; i<rhsValues.length; i++)
	    v.add(rhsValues[i]);

	return new PopupComponent(v);
    }
    private static class PopupComponent extends JComboBox implements AbstractDataComponent {
	public PopupComponent(Vector v) {
	    super(v);
	    setMaximumRowCount(v.size()); // just take them all
	}
	public Object getData() {
	    return getSelectedItem();
	}
    }

    // another hack
    public static Component makeMetaComponent(String fieldName) {
	JComboBox popup = new MetaComponent(fieldName);

	// REFACTOR: this looks incredibly similar to
	// MetadataPanel.makePopup()...

	// figure out field
	MetadataTemplate.Field f = null;
	Iterator ii = MetadataTemplate.getFields();
	while (ii.hasNext()) {
	    MetadataTemplate.Field field = (MetadataTemplate.Field) ii.next();
	    if (field.getDescription().equals(fieldName)) {
		f = field;
		break;
	    }
	}
	// (ASSUMEs: fieldName is legit)

    if (f != null) {
      // add to popup
      for (int i=0; i<f.values.length; i++)
        popup.addItem(I18n.getText("meta." + f.getVariable() + "." + f.values[i]));
    } else {
      log.error("MetadataTemplate.Field not found: " + fieldName);
    }

	return popup;
    }
    private static class MetaComponent extends JComboBox implements AbstractDataComponent {
	public MetaComponent(String field) {
	    this.field = field;
	}
	private String field; // metadata field
	public Object getData() {
	    // look up code in metadata/field that corresponds to this value
	    String text = (String) getSelectedItem();

	    // look up the field i've got -- REFACTOR: duplicate code!
	    MetadataTemplate.Field f = null;
	    Iterator ii = MetadataTemplate.getFields();
	    while (ii.hasNext()) {
		MetadataTemplate.Field ff = (MetadataTemplate.Field) ii.next();
		if (ff.getDescription().equals(field)) {
		    f = ff;
		    break;
		}
	    }

	    // look up key.myval
	    // HAVE: text=msg[f.variable+"."+code]
	    // WANT: code
	    String code = "xxx";
      if (f != null) {
        for (int i=0; i<f.values.length; i++) {
          if (I18n.getText("meta." + f.getVariable() +
                           "." + f.values[i]).equals(text)) {
            code = f.values[i];
            break;
          }
        }
      } else {
        log.error("MetadataTemplate.Field not found: " + field);
      }

	    return code;
	  }  
  }

    public static interface AbstractDataComponent {
	Object getData();
	// DESIGN: also need isDataValid() method?
    }

    private static class StringComponent extends JTextField implements AbstractDataComponent {
	public StringComponent() {
	    super(20);
	}
	public Object getData() {
	    String t = getText();
	    return t;
	}
    }

    private static class YearComponent extends JTextField implements AbstractDataComponent {
	public YearComponent() {
	    super("1001", 10);
	    selectAll();
	    // TODO: enforce [0-9] only
	    // TODO: add AD/BC popup to right side
	}
	public Object getData() {
	    String t = getText();
	    return new Year(t); // throws ??? if it's not valid?
	}
    }

    private static class DateComponent extends JTextField implements AbstractDataComponent {
	// FUTURE: make up/down incr/decr the date by days?
	// FUTURE: add "..." button to show a pop-up calendar?
	public DateComponent() {
	    super(10);
	    String today = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
	    setText(today);
	    selectAll();
	    /*
	    getDocument().addDocumentListener(new DocumentListener2() {
		    public void update() {
			try {
			    String t = getText();
			    System.out.println("testing " + t);
			    Date d = DateFormat.getDateInstance(DateFormat.SHORT).parse(t); // takes 1-3ms
			    // what's the time on this guy?
			    dateValid = true;
			    System.out.println("=> valid!");
			    setBackground(Color.white); // original color!
			} catch (ParseException pe) {
			    dateValid = false;
			    System.out.println("=> not valid!");
			    setBackground(ColorUtils.blend(Color.red, 0.3f, Color.white, 0.7f)); // orig color!
			}
 		    }
		});
	    */
	}
	private boolean dateValid=true;
	public boolean isDataValid() {
	    return dateValid;
	}
	public Object getData() {
	    String t = getText();
	    try {
		Date date = DateFormat.getDateInstance(DateFormat.SHORT).parse(t); // WARNING: throws parseexception!
		return date;
	    } catch (java.text.ParseException pe) {
		return null; // bad!
	    }
	}
    }

    private static class LengthComponent extends JPanel implements AbstractDataComponent {
	// BUG: requestFocus() on me doesn't requestFocus() on the text field
	// FUTURE: make up/down incr/decr it?
	public LengthComponent() {
	    setLayout(new FlowLayout(FlowLayout.LEFT));
	    setOpaque(false);

	    // TODO: enforce [0-9] only
	    field = new JTextField("100", 10);
	    field.selectAll();

	    add(field);
	    add(new JLabel(" years"));
	}
	private JTextField field;
	public Object getData() {
	    String t = field.getText();
	    Integer i = new Integer(t.trim()); // FUTURE: no need for trim() if i only accept [0-9]
	    return i;
	}
    }

    private static class NumberComponent extends JTextField implements AbstractDataComponent {
	// FUTURE: make up/down incr/decr?
	public NumberComponent() {
	    super("0", 10);
	    selectAll();
	    // TODO: enforce [0-9,'-'] only
	}
	public Object getData() {
	    String t = getText();
	    Integer i = new Integer(t.trim()); // FUTURE: no need for trim() if i only accept [0-9,'-']
	    return i;
	}
    }

    private static class NoneComponent extends Box.Filler implements AbstractDataComponent {
	private static final int WIDTH=100;
	public NoneComponent() {
	    super(new Dimension(WIDTH, 0), // this is basically what Box.createHorizontalStrut(width) does
		  new Dimension(WIDTH, 0),
		  new Dimension(WIDTH, Short.MAX_VALUE));
	}
	public Object getData() {
	    return null; // what else?
	}
    }
}
