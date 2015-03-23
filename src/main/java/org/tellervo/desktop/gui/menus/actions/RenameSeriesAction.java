package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.CopyDialog;
import org.tellervo.desktop.io.TwoColumn;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PureStringWriter;
import org.tellervo.desktop.util.TextClipboard;

public class RenameSeriesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private AbstractEditor editor;
	
	public RenameSeriesAction(AbstractEditor editor) {
        super("Copy", Builder.getIcon("editcopy.png", 22));
		putValue(SHORT_DESCRIPTION, "Copy");
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.editor = editor;
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

	
}