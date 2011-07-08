package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Window;
import java.awt.Dialog;
import java.awt.Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JTextArea;

import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;

import net.miginfocom.swing.MigLayout;

public class MemoEditorDialog extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private String theString;
	private JDialog dialog;
	private Boolean hasResults = false;
	private JTextArea textArea;
	
	public MemoEditorDialog(Window parent, String str) 
	{
		// construct a new dialog!
		if (parent instanceof Frame || parent == null)
			dialog = new JDialog((Frame) parent, "Memo", true);
		else
			dialog = new JDialog((Dialog) parent, "Memo", true);

		//dialog.setIconImage(Builder.getImage("Preferences16.gif"));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		dialog.setContentPane(this);
		
		
		dialog.setIconImage(Builder.getApplicationIcon());
		theString = str;
		
		
		
		this.setLayout(new MigLayout("", "[446px]", "[236px][37px]"));
		
		textArea = new JTextArea();
		textArea.setText(theString);
		
		add(textArea, "cell 0 0,grow");
		
		JPanel panel = new JPanel();
		add(panel, "cell 0 1,alignx right,aligny top");
		panel.setLayout(new MigLayout("", "[234.00,grow,fill][98.00px][117px]", "[25px]"));
		
		JButton btnOk = new JButton("OK");
		panel.add(btnOk, "cell 1 0,growx,aligny top");
		
		JButton btnCancel = new JButton("Cancel");
		panel.add(btnCancel, "cell 2 0,growx,aligny top");
		

		
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hasResults = false;
				dialog.dispose();
				dialog = null;
			}
		});
		
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Close window and return string
				hasResults = true;
				theString = textArea.getText();
				dialog.dispose();
				dialog = null;
			}			
		});
		
		dialog.pack();

		Center.center(dialog);
		dialog.setVisible(true);
	}
	
	/**
	 * @return true if the user clicked OK
	 */
	public boolean hasResults() {
		return hasResults;
	}
	
	
	public String getString()
	{
		return theString;
	}

}
