package org.tellervo.desktop.metadataexport;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;

import com.l2fprod.common.swing.ComponentFactory;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;

public class CompositeMetadataFieldEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {
   
	private String currentValue;
	private JPanel panel;
	private JButton button;
    protected static final String EDIT = "edit";
    private JTextField textField;
	
	public CompositeMetadataFieldEditor(){
		
		panel = new JPanel();
		
		
		 button = ComponentFactory.Helper.getFactory().createMiniButton();
		 button.setIcon(Builder.getIcon("edit.png", 16));
	        button.setActionCommand(EDIT);
	        button.addActionListener(this);
	    panel.setLayout(new MigLayout("insets 0", "[grow][34px]", "[10px,fill]"));
	    
	    textField = new JTextField();
	    panel.add(textField, "cell 0 0,growx");
	    textField.setColumns(10);
	    panel.add(button, "cell 1 0,alignx left,aligny top");
	    
	    
		 //remove enter pressed
        KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false);
        KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(up, "Up pressed");
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(tab, "Tab pressed");
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(down, "Down pressed");
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "Enter pressed");

        textField.getActionMap().put("Up pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) {
            	commit();
            }
        });
        textField.getActionMap().put("Tab pressed", new AbstractAction() {
        	private static final long serialVersionUID = 1L;
        	@Override
            public void actionPerformed(ActionEvent ae) {
        		commit();
            }
        });
        textField.getActionMap().put("Down pressed", new AbstractAction() {
        	private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent ae) {
        		commit();
            }
        });
        textField.getActionMap().put("Enter pressed", new AbstractAction() {
        	private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent ae) {
        		commit();
            }
        });
		
		
		textField.requestFocusInWindow();
		
	}
	
	@Override
	public Object getCellEditorValue() {
        return currentValue;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 if (EDIT.equals(e.getActionCommand())) {
			 Alert.message("DO", "Do something");

	     }
	}
	
	private void commit()
	{
		currentValue = textField.getText();
		
		fireEditingStopped();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		currentValue = (String) value;
		textField.setText(currentValue);
		
		
        return panel;
	}

}
