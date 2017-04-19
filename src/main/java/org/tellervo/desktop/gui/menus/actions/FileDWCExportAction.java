package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tdwg.DWCExporter;

public class FileDWCExportAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	public FileDWCExportAction()
	{
		super("Export to Darwin Core File");
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		
		DWCExporter.exportObject();
	    
		

		
	}


}
