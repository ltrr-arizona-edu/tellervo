package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.manip.ReconcileWindow;
import org.tellervo.desktop.manip.Reverse;
import org.tellervo.desktop.manip.TruncateDialog;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;

public class ToolsReconcileAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private final AbstractEditor editor;
	
	public ToolsReconcileAction(AbstractEditor editor) {
        super("Reconcile", Builder.getIcon("reconcile.png", 22));
        putValue(SHORT_DESCRIPTION, "Reconcile the current series");
        this.editor = editor;
    }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		editor.stopMeasuring();
		
		DBBrowser browser = new DBBrowser(editor, true, false);

		// select the site we're in
		if(editor.getSample().meta().hasSiteCode()) 
			browser.selectSiteByCode(editor.getSample().meta().getSiteCode());
		
		browser.setTitle(I18n.getText("question.chooseReference"));
		browser.setVisible(true);
		
		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
			ElementList toOpen = browser.getSelectedElements();

			if(toOpen.size() < 1)
				return;

			// load it
			Sample reference;
			try {
				reference = toOpen.get(0).load();
			} catch (IOException ioe) {
				Alert.error(I18n.getText("error.loadingSample"),
						I18n.getText("error.cantOpenFile") +": " + ioe.getMessage());
				return;
			}

			OpenRecent.sampleOpened(new SeriesDescriptor(reference), "reconcile");
			
			// open it for fun times
			new ReconcileWindow(editor.getSample(), reference);
		}
	}
};
		
		
