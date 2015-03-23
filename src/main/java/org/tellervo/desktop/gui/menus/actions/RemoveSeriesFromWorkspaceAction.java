package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.AbstractEditor;
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
			
			editor.getLstSamples().repaint();
			editor.itemSelected();
		} catch (Exception ex)
		{
			log.error("Error removing sample from workspace");
			
		}
	}

}
