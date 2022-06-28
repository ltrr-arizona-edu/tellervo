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
package org.tellervo.desktop.ui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.tellervo.desktop.gui.Layout;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.util.Center;


@SuppressWarnings("serial")
public class AskNumber extends JDialog {
	private JSpinner spinner;
	boolean isOk = false;
	
	public AskNumber(Frame parent, String title, String text, int defaultValue) {
		super(parent, title, true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		JPanel top;
		
		spinner = new JSpinner(new SpinnerNumberModel(defaultValue, 0, 9999, 1));
		spinner.setValue(new Integer(defaultValue));
		
		JLabel label = new JLabel(text);
		label.setLabelFor(spinner);
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		top = Layout.borderLayout(null, label, null, spinner, null);
		
		JButton cancel = Builder.makeButton("cancel");
		final JButton ok = Builder.makeButton("ok");
		
		//JPanel buttons = Layout.buttonLayout(null, null, cancel, ok);
		JPanel buttons = Layout.flowLayoutR(ok, cancel);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		JPanel fixed = Layout.borderLayout(top, null, null, null, buttons);
		fixed.setBorder(BorderFactory.createEmptyBorder(10, 14, 6, 14));
		add(fixed);
		
		AbstractAction okCancel = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				isOk = (e.getSource() == ok);
				dispose();
			}
		};
		ok.addActionListener(okCancel);
		cancel.addActionListener(okCancel);

		pack();

		// center it...
		if(parent == null)
			Center.center(this);
		else
			Center.center(this, parent);
		
		setVisible(true);
	}
	
	public static int getNumber(Frame parent, String title, String text, int defaultValue) throws UserCancelledException {
		AskNumber a = new AskNumber(parent, title, text, defaultValue);
		
		if(a.isOk == false)
			throw new UserCancelledException();
				
		return ((Integer)(a.spinner.getValue())).intValue();
	}

}
