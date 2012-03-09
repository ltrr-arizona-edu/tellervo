package org.tellervo.desktop.gui.widgets;

import java.awt.Window;

import javax.swing.JDialog;

import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;

public class TridasEntityPickerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * Show the TridasEntityPicker dialog.  Dialog returns a Tridas entity
	 * of a type specified by the clazz and acceptableType parameters
	 * 
	 * @param parent
	 * @param title
	 * @param clazz
	 * @param acceptabletype
	 * @return
	 */
	public static ITridas pickEntity(Window parent, String title,
			Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype)
	{
		
		JDialog dialog = new JDialog();
		dialog.setTitle("Entity Picker");
		TridasEntityPickerPanel panel = new TridasEntityPickerPanel(dialog, clazz, acceptabletype);
		dialog.getContentPane().add(panel);
		dialog.setIconImage(Builder.getApplicationIcon());

        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        
        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	

        return panel.getEntity();

	}
	
	/**
	 * Show the TridasEntityPicker dialog.  Dialog returns a Tridas entity
	 * of a specific type specified by the clazz parameter
	 * 
	 * @param parent
	 * @param title
	 * @param clazz
	 * @return
	 */
	public static ITridas pickSpecificEntity(Window parent, String title,
			Class<? extends ITridas> clazz)
	{
		
		JDialog dialog = new JDialog();
		dialog.setTitle("Entity Picker");
		TridasEntityPickerPanel panel = new TridasEntityPickerPanel(dialog, clazz, EntitiesAccepted.SPECIFIED_ENTITY_ONLY);
		dialog.getContentPane().add(panel);
		dialog.setIconImage(Builder.getApplicationIcon());

        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(parent);
        dialog.pack();
        
        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	

        return panel.getEntity();

	}
	
}
