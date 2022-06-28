package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public class RemoveSeriesFromWorkspaceAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	protected final static Logger log = LoggerFactory.getLogger(RemoveSeriesFromWorkspaceAction.class);

	public RemoveSeriesFromWorkspaceAction(AbstractEditor editor) {
        super(I18n.getText("action.removeseriesfromworkspace"), Builder.getIcon("cancel.png", 16));
		putValue(SHORT_DESCRIPTION, I18n.getText("action.removeseriesfromworkspace.description"));
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {
	
		String question = null;
		
		// Confirm save
		if(editor instanceof LiteEditor)
		{
			question = "Are you sure you want to remove this series from the file?";
			
		}
		else if (editor instanceof FullEditor && !editor.isSaved())
		{
			question = "The series has unsaved changes.  Are you sure you want to remove\nit from the workspace without saving?";
		}
		
		if(question!=null)
		{
			Object[] options = {"Yes",
	                "No",
	                "Cancel"};
				int n = JOptionPane.showOptionDialog(editor,
				    question,
				    "Confirm",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[2]);
				
			if(n!=JOptionPane.YES_OPTION)
			{
				return;
			}
		}
		
		
		try{
			int selind = editor.getLstSamples().getSelectedIndex();
			editor.getSamplesModel().remove(editor.getLstSamples().getSelectedIndex());
			int count = editor.getSamplesModel().getSize();
			if(selind<count) {
				editor.getLstSamples().setSelectedIndex(selind);
			}
			else
			{
				editor.getLstSamples().setSelectedIndex(editor.getLstSamples().getLastVisibleIndex());
			}
			
			// Force item selected when list of samples is empty to clean tabs
			if(editor.getSamplesModel().getSize()==0)
			{
				editor.itemSelected();
			}
						
			editor.getLstSamples().repaint();
			editor.itemSelected();
		} catch (Exception ex)
		{
			log.error("Error removing sample from workspace");
			
		}
	}

}
