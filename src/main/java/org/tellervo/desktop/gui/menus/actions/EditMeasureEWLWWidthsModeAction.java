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

public class EditMeasureEWLWWidthsModeAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor editor;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public EditMeasureEWLWWidthsModeAction(AbstractEditor abstractEditor) {
       // super(I18n.getText("menus.edit.start_measuring"), Builder.getIcon("measure.png", 22));
        super("Whole ring widths");
                
        this.editor = abstractEditor;
        putValue(SHORT_DESCRIPTION, "Whole ring widths");
        putValue(Action.SELECTED_KEY, true);
        
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Sample sample = editor.getSample();
		
		if (sample.containsSubAnnualData()          && 
				sample.getEarlywoodWidthData()!=null    &&
				sample.getLatewoodWidthData()!=null     &&
			   (sample.getEarlywoodWidthData().size()>0 || 
			    sample.getLatewoodWidthData().size()>0)    )
			{

				int n = JOptionPane.showConfirmDialog(editor, 
						"Switching to ring width measuring mode " +
						"will delete any\n"+
						"early/late wood values.\n\n"+
						"Are you sure you want to continue?");
		
				if(n != JOptionPane.YES_OPTION) 
				{
					putValue(Action.SELECTED_KEY, true);
					return;
				}
			}
				
			sample.setToAnnualMode();
			
			App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.RING_WIDTH.toString());
			sample.fireMeasurementVariableChanged();		
			
	}
		
	

}