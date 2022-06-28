package org.tellervo.desktop.tridasv2.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;


/**
 * Dialog for easier editing of long string values in metadata
 * 
 * @author pwb48
 *
 */
public class MemoEditorDialog extends JPanel {
	
	private final static Logger log = LoggerFactory.getLogger(MemoEditorDialog.class);
	private static final long serialVersionUID = 1L;
	private String theString;
	private JDialog dialog;
	private Boolean hasResults = false;
	private JTextArea textArea;
	
	public MemoEditorDialog(Component parent, String str) 
	{

		dialog = new JDialog();
		dialog.setTitle("Memo");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setUndecorated(true);
		
		dialog.setContentPane(this);
		
		
		dialog.setIconImage(Builder.getApplicationIcon());
		theString = str;
		
		
		
		this.setLayout(new MigLayout("", "[446px,grow,fill]", "[236px,grow,fill][37px]"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, "cell 0 0,grow");
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		textArea.setText(theString);
		
		 //remove enter pressed
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        InputMap inputMap = textArea.getInputMap();
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "Enter pressed");
        textArea.getActionMap().put("Enter pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                commit();
            }
        });
        
        
        //add shift+enter keybinding can be on pressed or released i.e false or true
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK, true), "Shift+Enter released");
        textArea.getActionMap().put("Shift+Enter released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                textArea.setText(textArea.getText()+"\n");
            }
        });
		
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
				commit();
			}			
		});
		
		try{
		dialog.setBounds(parent.getLocationOnScreen().x, 
				parent.getLocationOnScreen().y, 
				parent.getWidth(), 
				200);
		} catch (Exception e)
		{
			log.debug("Unable to determine location of parent for memo editor. Show anyway.");
		}
		dialog.setVisible(true);
	}
	
	private void commit()
	{
		hasResults = true;
		theString = textArea.getText();
		dialog.dispose();
		dialog = null;
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
