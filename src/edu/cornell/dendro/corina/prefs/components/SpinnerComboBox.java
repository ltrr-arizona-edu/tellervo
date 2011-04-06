package edu.cornell.dendro.corina.prefs.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 *
 * BUG: http://developer.java.sun.com/developer/bugParade/bugs/4987340.html
 * DecimalFormat returns Longs for whole numbers with fractional parts so we must
 * keep track of whether the spinner model was initialized with an integer or floating point
 * model, and then make sure to make calls to return the correct types in setValue
 */
@SuppressWarnings("serial")
public class SpinnerComboBox extends JComboBox {
  private JSpinner spinner;
  private Format inputFormat;
  private Format outputFormat;
  private boolean isFloatingPoint;
  private boolean isNumberModel;
  private ComboBoxEditor scbeditor = new SpinnerComboBoxEditor();
  private ActionListener actionListener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      setEditable(true);
      // this looks like it simply queues/fires an event, so we shouldn't have to
      // invoke this in SwingUtilities.invokeLater
      ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().requestFocus();
    }
  };
  private ChangeListener changeListener = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      //System.out.println("stateChanged " + spinner.getValue());
      setSelectedItem(spinner.getValue());
      //    XXX: this particular line doesn't appear to be working,
      // the intention is to resize when a new value, which may be wider
      // or narrower, is entered
      validate();
      repaint();
      /*SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          // XXX: this particular line doesn't appear to be working,
          // the intention is to resize when a new value, which may be wider
          // or narrower, is entered
          validate();
          
          repaint();
        }
      });*/
    }
  };
  private FocusListener focusListener = new FocusAdapter() {
    @Override
	public void focusLost(FocusEvent fe) {
      //System.out.println("textfield focus lost");
      setEditable(false);
      repaint();
      /*SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          System.out.println("setting editable false");
          setEditable(false);
          // XXX: this particular line doesn't appear to be working,
          // the intention is to resize when a new value, which may be wider
          // or narrower, is entered
          validate();
          
          repaint();
        }
      });*/
    }
  };

  private class FormattingComboBoxModel implements ComboBoxModel {
    private ComboBoxModel internal;
    public FormattingComboBoxModel(ComboBoxModel model) {
      internal = model;
    }
    
    public Object getElementAt(int i) {
      Object o = outputFormat.format(internal.getElementAt(i));
      //System.out.println("getElementAt " + i + ": " + o);
      return o;
    }
    
    public Object getSelectedItem() {
      //System.out.println("getSelectedItem " + internal.getSelectedItem());
      Object o = outputFormat.format(internal.getSelectedItem());
      //System.out.println("getSelectedItem formatted: " + o);
      return o;
      //return internal.getSelectedItem();
    }
    
    public void setSelectedItem(Object o) {
      //System.out.println("setSelectedItem: " + o + " " + o.getClass());
      if (o instanceof String) {
        try {
          outputFormat.parseObject((String) o);
          //System.out.println("PARSED OBJECT: " + p + " " + p.getClass());
          internal.setSelectedItem(outputFormat.parseObject((String)o));
        } catch (ParseException pe) {
          pe.printStackTrace();
        }
      } else {
        internal.setSelectedItem(o);  
      }
    }
    
    public int getSize() {
      return internal.getSize();
    }
    
    public void removeListDataListener(ListDataListener ldl) {
      internal.removeListDataListener(ldl);
    }
    
    public void addListDataListener(ListDataListener ldl) {
      internal.addListDataListener(ldl);
    }
  }

  private class SpinnerComboBoxEditor extends BasicComboBoxEditor {
    @Override
	public Component getEditorComponent() {
      return spinner; 
    }

    @Override
	public Object getItem() {
      Object value = spinner.getValue();
      //System.out.println("getItem: " + value + " " + value.getClass());
      Object o = outputFormat.format(value);
      //System.out.println("getItem formatted: " + o);
      return o;
    }

    @Override
	public void setItem(Object anObject) {
      //System.out.println("setItem: " + anObject + " " + anObject.getClass());
      if (anObject instanceof String) {
        try {
          Object o = inputFormat.parseObject((String) anObject);
          // HACK: have to fix up DecimalFormat parsed object because
          // it may return Long whole number with fraction
          if (isNumberModel) {
            Number number = (Number) o; 
          
            //System.out.println("SetItem PARSED: " + number + " " + number.getClass());
            if (isFloatingPoint) {
              if (!(number instanceof Double) && !(number instanceof Float))
                o = new Double(number.doubleValue());
            } else {
              if (!(number instanceof Integer) && !(number instanceof Long))
                o = new Long(number.longValue());
            }
          }
          spinner.setValue(o);
        } catch (ParseException pe) {
          RuntimeException t = new IllegalArgumentException("Illegal input: " + anObject);
          t.initCause(pe);
          throw t;
        }
      } else {
        throw new RuntimeException("asdfasdasdf");
        //spinner.setValue(anObject);
      }
    }      
  }

  public SpinnerComboBox() {
    super();
    spinner = new JSpinner();
    initSpinner();
    initComboBox();
  } 

  public SpinnerComboBox(ComboBoxModel aModel) {
    super(aModel);
    spinner = new JSpinner();
    initSpinner();
    initComboBox();
  } 

  public SpinnerComboBox(Object[] items) {
    super(items);
    //System.out.println("**** Setting spinner number model");
    if (items instanceof Float[] || items instanceof Double[]) {
      spinner = new JSpinner(new SpinnerNumberModel(0d, 0d, Double.MAX_VALUE, 0.1d));
      isFloatingPoint = true;
    } else {
      spinner = new JSpinner();
    }
    initSpinner();
    initComboBox();
  }

  @SuppressWarnings("unchecked")
public SpinnerComboBox(Vector items) {
    super(items);
    initSpinner();
    initComboBox();
  }

  private void initSpinner() {
    spinner.setBorder(BorderFactory.createEmptyBorder());
    Component textfield = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
    //textfield.removeFocusListener(focusListener); 
    textfield.addFocusListener(focusListener);
    
    //spinner.removeChangeListener(changeListener);
    spinner.addChangeListener(changeListener);
    
    if (spinner.getModel() instanceof SpinnerNumberModel) isNumberModel = true;
  }

  private void initComboBox() {
    setEditor(scbeditor);
    addMouseListener(new MouseAdapter() {
      @Override
	public void mouseClicked(MouseEvent me) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            setEditable(true);
            repaint();
          }
        });
      }
    });

    if (inputFormat != null && outputFormat != null) {
      ComboBoxModel model = getModel();
      if (!(model instanceof FormattingComboBoxModel)) {
        //System.out.println("Setting FormattingComboBoxModel");
        setModel(new FormattingComboBoxModel(model));
      }
    }

    removeActionListener(actionListener);
    addActionListener(actionListener);
  }

  public JSpinner getSpinner() {
    return spinner;
  }

  public void setFormats(Format inputFormat, Format outputFormat) {
    this.inputFormat = inputFormat;  
    this.outputFormat = outputFormat;
    //System.out.println("Spinner model: " + spinner.getModel());
    spinner.setModel(spinner.getModel());
    //System.out.println("**** Setting spinner editor");
    if (inputFormat instanceof DecimalFormat) {
      spinner.setEditor(new JSpinner.NumberEditor(spinner, ((DecimalFormat) inputFormat).toPattern()));
    } else if (outputFormat instanceof SimpleDateFormat) {
      spinner.setEditor(new JSpinner.DateEditor(spinner, ((SimpleDateFormat) inputFormat).toPattern()));
    }
    initSpinner();
    initComboBox();
  }

  public Format getInputFormat() {
    return inputFormat;  
  }

  public Format getOutputFormat() {
    return outputFormat;
  }
}