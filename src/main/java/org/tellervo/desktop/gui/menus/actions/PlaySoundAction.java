package org.tellervo.desktop.gui.menus.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;

public class PlaySoundAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private final SystemSound snd;
	
	/**
	 * Constructor for menus
	 * 
	 * @param frame
	 */
	public PlaySoundAction(SystemSound snd) {
        super("Play", Builder.getIcon("play.png", 16));
                
        this.snd = snd;
        putValue(SHORT_DESCRIPTION, "Play sound");
        
    }
	
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(snd!=null)
		{
			SoundUtil.playSystemSound(snd);
		}

		
	}

}