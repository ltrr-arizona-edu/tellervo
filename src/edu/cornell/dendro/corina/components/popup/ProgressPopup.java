/**
 * Created at Jan 19, 2011, 12:30:02 PM
 */
package edu.cornell.dendro.corina.components.popup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

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
	
	public ProgressPopup(Frame argParent, boolean argModal, ProgressPopupModel argModel){
		this(argParent, argModal, argModel, null);
	}
	
	public ProgressPopup(Frame argParent, boolean argModal, ProgressPopupModel argModel, Dimension argPrefSize){
		super(argParent, argModal);
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
		
		setLayout(new GridLayout(0, 1));
		
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		add(label);
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(progress, "Center");
		bottom.add(cancelButton, "East");
		add(bottom);
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
