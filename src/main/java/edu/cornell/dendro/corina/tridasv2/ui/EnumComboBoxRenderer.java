/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
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
public class EnumComboBoxRenderer extends AbstractComboBoxRenderer {
	private static final long serialVersionUID = 1L;

	private EnumComboBoxItemRenderer renderer;
	private JLabel dropdown;
	private boolean required;

	public EnumComboBoxRenderer(boolean required) {
		renderer = new EnumComboBoxItemRenderer();
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
		return (ComboBoxItemRenderer) renderer;
	}

	@Override
	public boolean isRequired() {
		return required;
	}
	
	
}
