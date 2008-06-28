package edu.cornell.dendro.corina.gui.newui;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class BasePanel extends JPanel {

	public BasePanel() {
		super();
	}

	public BasePanel(LayoutManager layout) {
		super(layout);
	}

	public BasePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public BasePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/**
	 * Convenience method: Force a JTextField to auto-capitalize 
	 * and not allow any whitespace chars
	 * @param field
	 */
	protected void setCapsNoWhitespace(JTextField field) {
		field.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char k = ke.getKeyChar();
				
				// don't allow any whitespace
				if(Character.isWhitespace(k)) {
					ke.consume(); // om nom nom nom!
					return;
				}
				
				// force uppercase
				ke.setKeyChar(Character.toUpperCase(k));
			}
		});
	}

	/**
	 * Only allow number characters, - in front, and one decimal point
	 * @param field
	 * @param canHaveSign
	 */
	protected void setNumbersOnly(JTextField field, boolean canHaveSign) {
		final JTextField gField = field;
		final boolean gCanHaveSign = canHaveSign;
		
		field.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char k = ke.getKeyChar();
	
				// always allow minuses
				if(Character.isDigit(k))
					return;
				
				// only allow a or at the beginning
				if(gCanHaveSign && k == KeyEvent.VK_MINUS) {
					if(gField.getCaretPosition() != 0)
						ke.consume();
					return;
				}
				
				// only allow one period
				if(k == KeyEvent.VK_PERIOD) {
					String txt = gField.getText();
					
					if(txt.indexOf(KeyEvent.VK_PERIOD) != -1)
						ke.consume();
					return;
				}
				
				// not here!
				ke.consume();
			}
		});
	}

	/**
	 * Convenience function: makes a JTextField select all
	 * when it receives focus
	 * @param field
	 */
	protected void setSelectAllOnFocus(JTextField field) {
		final JTextField glue = field;
		
		field.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent fe) {
				glue.selectAll();
			}
		});
	}

	/**
	 * Sets the background color of the field depending on its
	 * valid/invalid status
	 * @param field
	 * @param isGood
	 */
	protected void colorField(JComponent field, boolean isValid) {
		if(isValid) {
			field.setBackground(UIManager.getLookAndFeelDefaults().getColor("TextField.background"));
		} else {
			field.setBackground(new Color(255, 255, 200));
		}
	}

	/**
	 * Set a JSpinner model that takes a null value!
	 * @param spinner
	 */
	protected void setSpinnerIndeterminate(JSpinner spinner) {
		spinner.setModel(new AbstractSpinnerModel() {
			private final static String noValue = "-- Not Specified --";
			private String value = noValue;
			
			private Integer parse() {
				try {
					return new Integer(value);
				} catch (NumberFormatException nfe) {
					return null;
				}
			}
			
			public Object getNextValue() {
				Integer i = parse();
				
				if(i == null)
					return new Integer(0);
						
				i++;
				return i;
			}
	
			public Object getPreviousValue() {
				Integer i = parse();
	
				if(i == null || i == 0)
					return noValue;
				
				i--;
				return i;
			}
	
			public Object getValue() {
				Integer i = parse();
				
				return (i == null) ? value : i;
			}
	
			public void setValue(Object value) {
				try {
					Integer i = new Integer(value.toString());
	
					this.value = i.toString();
				} catch (NumberFormatException nfe) {
					this.value = noValue;
				}
				
				fireStateChanged();
			}			
		});
	}

}