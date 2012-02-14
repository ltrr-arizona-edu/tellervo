package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * Editor for enabling easier editing of strings in metadata browser
 * 
 * @author pwb48
 *
 */
public class MemoEditor extends AbstractPropertyEditor {


	private String theString;
	private JTextField textField;
	private JButton btnEdit;
	private JButton btnDelete;
	private MemoEditorDialog dialog;
	
	public MemoEditor()
	{

	
		editor = new JPanel();
		((JPanel)editor).setLayout(new MigLayout("insets 0", "[315.00px,grow,fill][::60px][::60px]", "[0px,0px]"));
		
		textField = new JTextField();
		((JPanel)editor).add(textField, "cell 0 0,alignx left,aligny center");
		textField.setColumns(10);
		
		btnEdit = ComponentFactory.Helper.getFactory().createMiniButton();
		btnEdit.setIcon(Builder.getIcon("note.png", 16));
		btnEdit.setText("");
		((JPanel)editor).add(btnEdit, "cell 1 0,alignx right,aligny top");
		
		btnDelete = ComponentFactory.Helper.getFactory().createMiniButton();
		btnDelete.setIcon(Builder.getIcon("cancel.png", 16));
		btnDelete.setText("");
		((JPanel)editor).add(btnDelete, "cell 2 0,alignx left,aligny top");
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setToNull();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchEditDialog();
			}
		});
		

		textField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
	
				int id = arg0.getKeyCode();
				if(id == KeyEvent.VK_ENTER || id == KeyEvent.VK_TAB)
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
		
		textField.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount()>=2)
				{
					
					if(dialog==null)
					{
						launchEditDialog();
					}
					else
					{
						dialog.setVisible(false);
						dialog = null;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {	}

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) {	}

			@Override
			public void mouseReleased(MouseEvent arg0) { }
			
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
	
	private void launchEditDialog()
	{
		dialog = new MemoEditorDialog(editor, theString);
		if(!dialog.hasResults())return;
		setValue(dialog.getString());
		dialog = null;		
	}
}
