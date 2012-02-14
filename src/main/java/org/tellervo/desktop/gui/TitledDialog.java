/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TitledDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TitledDialog dialog = new TitledDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TitledDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel titlePanel = new JPanel();
			titlePanel.setMinimumSize(new Dimension(5, 50));
			titlePanel.setBackground(Color.WHITE);
			getContentPane().add(titlePanel, BorderLayout.NORTH);
			titlePanel.setLayout(new MigLayout("novisualpadding", "[0][355.00px][126.00,right]", "[grow][46.00px]"));
			{
				JLabel lblNewLabel = new JLabel("New label");
				titlePanel.add(lblNewLabel, "cell 0 0 2 1,growx,aligny top");
			}
			
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			titlePanel.add(panel, "cell 2 0 1 2,grow");
			panel.setLayout(null);
			
			JLabel lblNewLabel_2 = new JLabel("New label");
			lblNewLabel_2.setIcon(new ImageIcon("/tmp/org.eclipse.jface_3.5.2.M20100120-0800/org/eclipse/jface/dialogs/images/title_banner.gif"));
			lblNewLabel_2.setBounds(26, 12, 70, 55);
			panel.add(lblNewLabel_2);
			
			JLabel lblNewLabel_1 = new JLabel("New label");
			lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 10));
			titlePanel.add(lblNewLabel_1, "cell 1 1,aligny top");
		}
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
