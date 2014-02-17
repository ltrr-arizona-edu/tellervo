package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;

public class PickSampleDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox comboBox;
	private DefaultComboBoxModel model; 
	private Boolean cancelled = false;
	
	/**
	 * Create the dialog.
	 */
	public PickSampleDialog(Component parent, ArrayList<Sample> sampleList) {
		setModal(true);
		this.setIconImage(Builder.getApplicationIcon());
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
		{
			JLabel lblNewLabel = new JLabel("<html>This file contains "+sampleList.size()+" series. Would you like to open all of these or just one?");
			contentPanel.add(lblNewLabel, "cell 0 0");
		}
		{
			comboBox = new JComboBox();
			contentPanel.add(comboBox, "cell 0 1,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						cancelled=false;
						setVisible(false);
						
					}
					
				});
				
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						cancelled=true;
						setVisible(false);						
					}
					
				});
				buttonPane.add(cancelButton);
			}
		}
		
		populateCombo(sampleList);
		pack();
		this.setLocationRelativeTo(parent);
		this.setVisible(true);
	}
	
	private void populateCombo(ArrayList<Sample> sampleList)
	{
		if(sampleList==null || sampleList.size()==0) return;
		
		model = new DefaultComboBoxModel();
		
		model.addElement("All series");
		for(Sample s : sampleList)
		{
			model.addElement(s);
		}
		
		comboBox.setModel(model);
		
		
	}
	/**
	 * Did the user chose to open all the samples or not? 
	 * 
	 * @return
	 */
	public Boolean isOpeningAll()
	{
		Object sel = comboBox.getSelectedItem();
		
		if(sel instanceof String)
		{
			return true;
		}
		
		
		return false;
	}
	
	
	/**
	 * Get the sample that the user selected.  If they chose 'all samples' then
	 * this will return null
	 * 
	 * @return
	 */
	public Sample getSelectedSample()
	{

		Object sel = comboBox.getSelectedItem();
		
		if(sel instanceof String)
		{
			return null;
		}
		else if (sel instanceof Sample)
		{
			return (Sample)sel; 
		}
		
		return null;
		
	}
	
	/**
	 * Whether the user cancelled the dialog or not 
	 * 
	 * @return
	 */
	public Boolean isCancelled()
	{
		return cancelled;
	}

}
