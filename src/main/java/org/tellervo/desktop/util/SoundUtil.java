package org.tellervo.desktop.util;

import java.applet.Applet;
import java.applet.AudioClip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.TridasObject;

/**
 * Simple class for providing convenient access to sound files
 * 
 * @author pwb48
 *
 */
public class SoundUtil {
	private final static Logger log = LoggerFactory.getLogger(SoundUtil.class);

	/**
	 * Play the specified AudioClip
	 * 
	 * @param beep
	 */
	public static void playAudioClip(AudioClip beep)
	{
		if(beep != null)
		{
			beep.play();
		}
	}
	
    /**
     * Play a beep for when barcode reader has scanned a barcode
     */
    public static void playBarcodeBeep(){
    	AudioClip beep = getSoundFromResources("checkout.wav");
		if(beep != null)
			beep.play();

    }
    
    /**
     * Get a sound associated with a measurement
     * 
     * @return
     */
    public static AudioClip getMeasureSound()
    {
    	return getSoundFromResources("meas1.wav");
    }
    
    /**
     * Play a sound associated with a measurement
     */
    public static void playMeasureSound(){	
    	SoundUtil.playAudioClip(getMeasureSound());
    }
    
    /**
     * Get the sound associated with the measurement of the 
     * 10th ring in a sequence.
     * 
     * @return
     */
    public static AudioClip getMeasureDecadeSound()
    {
    	return getSoundFromResources("measdec.wav");
    }
    
    /**
     * Play the sound associated with the measurement of the 
     * 10th ring in a sequence.
     */
    public static void playMeasureDecadeSound(){
    	SoundUtil.playAudioClip(getMeasureDecadeSound());
    }
    
    /**
     * Get the sound associated with a measurement error
     * 
     * @return
     */
    public static AudioClip getMeasureErrorSound()
    {
    	return getSoundFromResources("measerr.wav");
    }
    
    /**
     * Play the sound associated with a measurement error
     */
    public static void playMeasureErrorSound(){
    	SoundUtil.playAudioClip(getMeasureErrorSound());
    }
    
    /**
     * Get the sound associated with the measuring platform
     * initializing
     * 
     * @return
     */
    public static AudioClip getMeasureInitSound()
    {
    	return getSoundFromResources("measinit.wav");
    }
    
    /**
     * Play the sound associated with the measuring platform
     * initializing
     */
    public static void playMeasureInitSound()
    {
    	SoundUtil.playAudioClip(getMeasureInitSound());
    }
        
    /**
     * Get the sound for the specified filename from the Sound resources folder
     * 
     * @param str
     * @return
     */
    private static AudioClip getSoundFromResources(String str)
    {
    	AudioClip beep;
		try {	
			// play this to indicate measuring is on...
			beep = Applet.newAudioClip(TridasObject.class.getClassLoader().getResource("Sounds/"+str));
			return beep;
		} catch (Exception ae) { 
			log.debug("Failed to get the requested sound file");
			log.debug(ae.getMessage());
		}
		
		return null;
    }
    
    	
}
