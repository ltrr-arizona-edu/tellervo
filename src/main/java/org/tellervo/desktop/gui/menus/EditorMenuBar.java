package org.tellervo.desktop.gui.menus;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public abstract class EditorMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	protected EditorActions actions;
	protected AbstractEditor editor;
	
	public EditorMenuBar(EditorActions actions, AbstractEditor editor)
	{
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        this.actions = actions;
        this.editor = editor;
	}
	
	
	
	protected JMenu getMeasureModeMenu()
	{
		JMenu measureModeMenu = Builder.makeMenu("menus.edit.measuremode");
		final JRadioButtonMenuItem btnRingWidth = new JRadioButtonMenuItem(I18n.getText("menus.edit.measuremode.ringwidth"));
		final JRadioButtonMenuItem btnEWLWWidth = new JRadioButtonMenuItem(I18n.getText("menus.edit.measuremode.ewlwwidth"));
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnRingWidth);
		group.add(btnEWLWWidth);
		
		measureModeMenu.add(btnRingWidth);
		measureModeMenu.add(btnEWLWWidth);
		
		final Sample sample = editor.getSample();

		btnRingWidth.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
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
						btnEWLWWidth.setSelected(true);
						return;
					}
				}
					
				sample.setToAnnualMode();
				
				App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.RING_WIDTH.toString());
				sample.fireMeasurementVariableChanged();		
				
			}
			
		});
		
		btnEWLWWidth.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sample.getRingWidthData().size()>0)
				{
					int n = JOptionPane.showConfirmDialog(editor, 
							"Switching to early/latewood measuring mode will\n"+"" +
							"delete any ring width values you have.\n\n"+
							"Are you sure you want to continue?");
			
					if(n != JOptionPane.YES_OPTION) 
					{
						btnRingWidth.setSelected(true);
						return;
					}
				}
					
				sample.setToSubAnnualMode();
				
				App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH.toString());
				sample.fireMeasurementVariableChanged();
				
				
			}
			
			
		});
		
		return measureModeMenu;
	}
	
}
