package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;

public class MemoEditor extends AbstractPropertyEditor {

	private final static Logger log = LoggerFactory.getLogger(MemoEditor.class);

	private String theString;
	private JTextField textField;
	private JButton btnEdit;
	private JButton btnDelete;
	
	public MemoEditor()
	{

	
		editor = new JPanel();
		((JPanel)editor).setLayout(new MigLayout("insets 0", "[315.00px,grow,fill][::60px][::60px]", "[0px,0px]"));
		
		textField = new JTextField();
		((JPanel)editor).add(textField, "cell 0 0,alignx left,aligny center");
		textField.setColumns(10);
		
		btnEdit = ComponentFactory.Helper.getFactory().createMiniButton();
		btnEdit.setText("...");
		((JPanel)editor).add(btnEdit, "cell 1 0,alignx right,aligny top");
		
		btnDelete = ComponentFactory.Helper.getFactory().createMiniButton();
		btnDelete.setText("X");
		((JPanel)editor).add(btnDelete, "cell 2 0,alignx left,aligny top");
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setToNull();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				MemoEditorDialog dialog = new MemoEditorDialog(SwingUtilities.getWindowAncestor(editor), theString);
								
				// cancelled...
				if(!dialog.hasResults())return;
				
	
				setValue(dialog.getString());

			}
		});
		

		
		textField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				
				// TODO Auto-generated method stub
				int id = arg0.getKeyCode();
				if(id == KeyEvent.VK_ENTER)
				{

					
					String oldValue = theString;
					theString = textField.getText();
					firePropertyChange(oldValue, theString);

				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) { }
			@Override
			public void keyTyped(KeyEvent arg0) { }

		});

	}
	
	@Override
	public Object getValue() {
		return theString;
	}
	
	@Override
	public void setValue(Object value) {

		String oldValue = theString;
		theString = (String) value;
		textField.setText((String) value);
		firePropertyChange(oldValue, theString);
		
	}
	
	private void setToNull() {
		setValue(null);
	}
}
