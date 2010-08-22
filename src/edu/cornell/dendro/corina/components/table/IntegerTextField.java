/**
 * Created at Aug 22, 2010, 3:45:48 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * @author Daniel
 *
 */
public class IntegerTextField extends JFormattedTextField {
	private static final long serialVersionUID = 1L;

	private NumberFormat nf;
 
	InputVerifier verifier = new InputVerifier() {
		public boolean shouldYieldFocus(JComponent input) {
	    	boolean inputOK = verify(input);
	    	
	        if (inputOK) {
	          return true;
	        }
	        
	        return false;
		}
		@Override
		public boolean verify(JComponent argInput) {
			String text = ((IntegerTextField) argInput).getText();
			Double num;
			try {
				num = ((IntegerTextField) argInput).getNumberFormat().parse( text).doubleValue();
			} catch( Exception ex) {
				return false;
			}
			return num != null;
		}
	};
	
	public IntegerTextField(boolean argCommitsOnValidEdit) {
 
		this.setInputVerifier( verifier);

		
		this.setFocusLostBehavior(PERSIST);
		
		nf = NumberFormat.getInstance();
		nf.setParseIntegerOnly(true);
		nf.setGroupingUsed(true);
		DefaultFormatter render = new NumberFormatter(nf);
		render.setCommitsOnValidEdit(argCommitsOnValidEdit);
 
		DefaultFormatterFactory fmtFactory = new DefaultFormatterFactory(
				render, render);
		setFormatterFactory(fmtFactory);
 
		setHorizontalAlignment(JFormattedTextField.RIGHT);
		setBorder(BorderFactory.createEmptyBorder());
	}
 
	public NumberFormat getNumberFormat() {
		return nf;
	}
}
