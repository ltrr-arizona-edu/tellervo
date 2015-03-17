package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.ui.Builder;

public class RemarkToggleAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final AbstractEditor e;
	private final static Logger log = LoggerFactory.getLogger(RemarkToggleAction.class);

	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public RemarkToggleAction(AbstractEditor e) {
        //super(I18n.getText("menus.edit.toggleremarks"), Builder.getIcon("note.png", 22));
        super("Hide/show ring remarks panel", Builder.getIcon("note.png", 22));
                
        this.e = e;
        //putValue(SHORT_DESCRIPTION, I18n.getText("menus.edit.toggleremarks"));
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.edit.toggleremarks")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.edit.toggleremarks"));
  
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		SeriesDataMatrix sdv = e.getSeriesDataMatrix();
		
		if(sdv!=null)
		{
			log.debug("Toggling remarks panel");
			sdv.toggleRemarks();
		}
		else
		{
			log.debug("SampleDataView is null so can't toggle remarks panel");

		}
		
	}

}