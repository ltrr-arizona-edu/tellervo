/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.tridas.schema.TridasLocationGeometry;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;

/**
 * @author Lucas Madar
 *
 */
public class LocationGeometryEditor extends AbstractPropertyEditor {
	
	private LocationGeometryRenderer label;
	private JButton button;
	private TridasLocationGeometry geometry;
	
	/**
	 * 
	 */
	public LocationGeometryEditor() {
		editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
		((JPanel) editor).add("*", label = new LocationGeometryRenderer());
		label.setOpaque(false);
		((JPanel) editor).add(button = ComponentFactory.Helper.getFactory()
				.createMiniButton());
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGeometry();
			}
		});
		((JPanel) editor).add(button = ComponentFactory.Helper.getFactory()
				.createMiniButton());
		button.setText("X");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNull();
			}
		});
		((JPanel) editor).setOpaque(false);
	}
	
	@Override
	public Object getValue() {
		return geometry;
	}
	
	@Override
	public void setValue(Object value) {
		geometry = (TridasLocationGeometry) value;
		label.setValue(value);
	}
	
	/**
	 * Remove the geometry
	 */
	private void selectNull() {
		TridasLocationGeometry oldGeometry = geometry;
		label.setValue(null);
		geometry = null;
		
		firePropertyChange(oldGeometry, geometry);
	}
	
	/**
	 * Pop up a dialog and select a new geometry
	 */
	private void selectGeometry() {
		TridasLocationGeometry oldGeometry = geometry;
		LocationGeometry dialog = new LocationGeometry();
		
		// show the dialog...
		dialog.showDialog(SwingUtilities.getWindowAncestor(editor), geometry);
		
		// cancelled...
		if(!dialog.hasResults())
			return;
		
		geometry = dialog.getGeometry();
		label.setValue(geometry);
		firePropertyChange(oldGeometry, geometry);
	}
}
