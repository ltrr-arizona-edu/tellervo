/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Lucas Madar
 *
 */
public class ListComboBoxRenderer extends AbstractComboBoxRenderer {
	
	private static final long serialVersionUID = 1L;

	private ListComboBoxItemRenderer renderer;
	private JLabel dropdown;
	private boolean required;

	public ListComboBoxRenderer(boolean required) {
		renderer = new ListComboBoxItemRenderer();
		
		this.required = required;
		
        Icon icon = Builder.getIcon("dropdown.png", Builder.ICONS, 22);
        if(icon == null)
        	dropdown = new JLabel("[...]");
        else
        	dropdown = new JLabel(icon);
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(renderer);
        add(Box.createHorizontalGlue());
        add(dropdown);
	}

	@Override
	public JComponent getDropdownBox() {
		return dropdown;
	}

	@Override
	public ComboBoxItemRenderer getRenderer() {
		return renderer;
	}

	@Override
	public boolean isRequired() {
		return required;
	}
	
}
