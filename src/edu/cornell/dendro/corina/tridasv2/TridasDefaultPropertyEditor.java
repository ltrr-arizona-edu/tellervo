package edu.cornell.dendro.corina.tridasv2;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.lowagie.text.Font;

public class TridasDefaultPropertyEditor extends AbstractPropertyEditor {

	private JLabel label;
	private JButton button;
	private Object value;

	public TridasDefaultPropertyEditor() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setOpaque(false);
		
		label = new JLabel("");
		label.setForeground(Color.GRAY.brighter());
		label.setFont(label.getFont().deriveFont(Font.ITALIC));
		label.setOpaque(false);
		panel.add(label);

		// set class editor
		editor = panel;

		button = new JButton("Remove");
		button.setMargin(new Insets(0,5,0,5));
		
		panel.add(Box.createHorizontalGlue());
		panel.add(button);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNull();
			}
		});
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		label.setText(value == null ? "" : " present");
		button.setVisible(value == null ? false : true);
	}

	protected void selectNull() {
		Object oldValue = value;
		label.setText("");
		value = null;
		firePropertyChange(oldValue, null);
	}
}

