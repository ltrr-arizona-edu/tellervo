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
package edu.cornell.dendro.corina.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;

public class Log4JViewer extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public Log4JViewer() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
			{
				JTextArea txtLog = new JTextArea();
				txtLog.setFont(new Font("Andale Mono", Font.PLAIN, 11));
				
				TextAreaLogger appender = new TextAreaLogger(new PatternLayout("%-6p : %m [%c{1}]%n"), txtLog);
				Logger.getRootLogger().addAppender(appender);
				scrollPane.setViewportView(txtLog);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						
					}
					
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
				getRootPane().setDefaultButton(btnClose);
			}
		}
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setIconImage(Builder.getApplicationIcon());
		setTitle(I18n.getText("menus.help.error_log"));
	}
	
	public static void showLogViewer()
	{
		Log4JViewer viewer = new Log4JViewer();
		viewer.setVisible(true);
		
		

	}

}
