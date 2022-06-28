/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer
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
package org.tellervo.desktop.components.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

/**
 * Popup progress window, updates from model and sets the cancelled value
 * on the model to true when cancelled.
 * @author Daniel
 *
 */
public class ProgressPopup extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JProgressBar progress;
	private JButton cancelButton;
	
	private final ProgressPopupModel model;
	
	/**
	 * @wbp.parser.constructor
	 */
	public ProgressPopup(Frame argParent, boolean argModal, ProgressPopupModel argModel){
		this(argParent, argModal, argModel, null);
	}
	
	public ProgressPopup(Frame argParent, boolean argModal, ProgressPopupModel argModel, Dimension argPrefSize){
		super(argParent, argModal);
		
		if(argModal)
		{
			    setUndecorated(true);
		}
		
		model = argModel;
		initializeComponents();
		linkModel();
		addListeners();
		if(argPrefSize != null){
			setPreferredSize(argPrefSize);
		}
		pack();
		setLocationRelativeTo(argParent);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		

	}
	
	private void initializeComponents() {
		label = new JLabel();
		progress = new JProgressBar();
		cancelButton = new JButton();
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		getContentPane().setLayout(new MigLayout("", "[260px]", "[][25px]"));
		
		getContentPane().add(label, "cell 0 0,grow");
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(progress, "Center");
		bottom.add(cancelButton, "East");
		getContentPane().add(bottom, "cell 0 1,grow");
	}
	
	private void linkModel(){
		setTitle(model.getTitle());
		progress.setValue(model.getPercent());
		label.setText(model.getStatusString());
		cancelButton.setText(model.getCancelString());
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent argEvt) {
				if(argEvt.getPropertyName().equals("title")){
					setTitle(argEvt.getNewValue().toString());
				}
				else if(argEvt.getPropertyName().equals("percent")){
					progress.setValue((Integer)argEvt.getNewValue());
				}
				else if(argEvt.getPropertyName().equals("statusString")){
					label.setText(argEvt.getNewValue().toString());
				}
				else if(argEvt.getPropertyName().equals("cancelString")){
					cancelButton.setText(argEvt.getNewValue().toString());
				}
			}
		});
	}
	
	private void addListeners(){
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent argE) {
				model.setCanceled(true);
			}
		});
	}
}
