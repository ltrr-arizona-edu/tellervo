package org.tellervo.desktop.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasGenericField;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;


/**
 * Editor for enabling easier editing of strings in metadata browser
 * 
 * @author pwb48
 *
 */
public class TridasObjectGenericFieldEditor extends AbstractPropertyEditor {
	
	
	protected ArrayList<TridasGenericField> theFieldList = new ArrayList<TridasGenericField>();
	protected JTextField textField;
	private JButton btnEdit;
	private JButton btnDelete;
	protected MemoEditorDialog dialog;
	
	
	public TridasObjectGenericFieldEditor()
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
					ArrayList<TridasGenericField> oldValue = theFieldList;
					setField(buildGenericField(GenericFieldKey.OBJECT_LAB_CODE, GenericFieldDataType.STRING, textField.getText()));
					firePropertyChange(oldValue, getField());
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
	
	
	public static TridasGenericField buildGenericField(GenericFieldKey key, GenericFieldDataType datatype, String value)
	{
		TridasGenericField gf = new TridasGenericField();
		gf.setName(key.getValue());
		gf.setValue(value);
		gf.setType(datatype.getValue());

		return gf;
		
	}
	
	private TridasGenericField getField()
	{
		return theFieldList.get(0);
	}
	
	private void setField(TridasGenericField gf)
	{
		theFieldList.clear();
		theFieldList.add(gf);
	}
	
	
	@Override
	public Object getValue() {
				
		return getField().getValue();
	}
	
	@Override
	public void setValue(Object value) {

		ArrayList<TridasGenericField> oldValue = theFieldList;
		TridasGenericField newValue = null;
		
		if(value instanceof String)
		{
			newValue = buildGenericField(GenericFieldKey.OBJECT_LAB_CODE, GenericFieldDataType.STRING, (String) value);
			
		}
		else if (value instanceof ArrayList)
		{
			for(Object v2 : (ArrayList<Object>) value)
			{
				if(v2 instanceof TridasGenericField)
				{
					if(((TridasGenericField) v2).getName().equals(GenericFieldKey.OBJECT_LAB_CODE.getValue()))
					{
						newValue = buildGenericField(GenericFieldKey.OBJECT_LAB_CODE, GenericFieldDataType.STRING, ((TridasGenericField)v2).getValue());
					}
				}
			}
		}
		
		
		setField(newValue);
		try{
		textField.setText(getField().getValue());
		} catch (NullPointerException e)
		{
			textField.setText("");
		}
		firePropertyChange(oldValue, getField());
		
	}
	
	private void setToNull() {
		setValue(null);
	}
	
	private void launchEditDialog()
	{
		dialog = new MemoEditorDialog(editor, getField().getValue());
		if(!dialog.hasResults())return;
		setValue(dialog.getString());
		dialog = null;		
	}
	
	
	
	/**
	 * Keys to the commonly used generic field names
	 * 
	 * @author pwb48
	 *
	 */
	public enum GenericFieldKey{
		
		OBJECT_LAB_CODE("tellervo.objectLabCode");
			
		private String key;
		
		private GenericFieldKey(String key)
		{
			this.key = key;
		}
		
		public String getValue()
		{
			return key;
		}
	}
	
	/**
	 * XML data type options for GenericFields
	 * 
	 * @author pwb48
	 *
	 */
	public enum GenericFieldDataType{
		
		STRING("xs:string"),
		INTEGER("xs:int"),
		TOKEN("xs:token"),
		DATE_TIME("xs:datTime"),
		BOOLEAN("xs:boolean"),
		DOUBLE("xs:double");
		
			
		private String key;
		
		private GenericFieldDataType(String key)
		{
			this.key = key;
		}
		
		public String getValue()
		{
			return key;
		}
	}

}
