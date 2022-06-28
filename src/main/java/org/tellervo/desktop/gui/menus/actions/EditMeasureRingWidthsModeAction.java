package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;

public class EditMeasureRingWidthsModeAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public EditMeasureRingWidthsModeAction(AbstractEditor abstractEditor) {
       // super(I18n.getText("menus.edit.start_measuring"), Builder.getIcon("measure.png", 22));
        super("Early/latewood widths");
                
        this.editor = abstractEditor;
        putValue(SHORT_DESCRIPTION, "Early/latewood widths");
        putValue(Action.SELECTED_KEY, false);
        
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Sample sample = editor.getSample();
		
		if (sample.getRingWidthData().size()>0)
		{
			int n = JOptionPane.showConfirmDialog(editor, 
					"Switching to early/latewood measuring mode will\n"+"" +
					"delete any ring width values you have.\n\n"+
					"Are you sure you want to continue?");
	
			if(n != JOptionPane.YES_OPTION) 
			{
				 putValue(Action.SELECTED_KEY, true);
				return;
			}
		}
			
		sample.setToSubAnnualMode();
		
		App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH.toString());
		sample.fireMeasurementVariableChanged();
		
			
	}
		
	

}