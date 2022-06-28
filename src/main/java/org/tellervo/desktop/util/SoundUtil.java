package org.tellervo.desktop.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tridas.schema.TridasObject;

/**
 * Simple class for providing convenient access to sound files
 * 
 * @author pwb48
 *
 */
public class SoundUtil {
	private final static Logger log = LoggerFactory.getLogger(SoundUtil.class);
	private final static ClassLoader cl = org.tellervo.desktop.ui.Builder.class.getClassLoader();
	
	public enum SystemSound 
	{
		BARCODE_SCAN,
		MEASURING_PLATFORM_INIT,
		MEASURE_RING,
		MEASURE_DECADE,
		MEASURE_ERROR;
	}
	
	public static void playSystemSound(SystemSound snd)
	{
		
		switch(snd)
		{
		case BARCODE_SCAN:
			
	    	if(App.prefs.getPref(PrefKey.SOUND_BARCODE_FILE, null) == null || App.prefs.getBooleanPref(PrefKey.SOUND_USE_SYSTEM_DEFAULTS, false))
	    		playSoundFileFromResources("barcodeScan.wav");
	    	else
	    		playSoundFile(new File(App.prefs.getPref(PrefKey.SOUND_BARCODE_FILE, null)));
	    	break;
	    	
		case MEASURING_PLATFORM_INIT:
	    	if(App.prefs.getPref(PrefKey.SOUND_PLATFORM_INIT_FILE, null) == null || App.prefs.getBooleanPref(PrefKey.SOUND_USE_SYSTEM_DEFAULTS, false))
	    		playSoundFileFromResources("initPlatform.wav");
	    	else
	    		playSoundFile(new File(App.prefs.getPref(PrefKey.SOUND_PLATFORM_INIT_FILE, null)));
	    	break;
	    	
		case MEASURE_RING:
	    	if(App.prefs.getPref(PrefKey.SOUND_MEASURE_RING_FILE, null) == null || App.prefs.getBooleanPref(PrefKey.SOUND_USE_SYSTEM_DEFAULTS, false))
	    		playSoundFileFromResources("measureRing.wav");
	    	else
	    		playSoundFile(new File(App.prefs.getPref(PrefKey.SOUND_MEASURE_RING_FILE, null)));
	    	break;
			
		case MEASURE_DECADE:
	    	if(App.prefs.getPref(PrefKey.SOUND_MEASURE_DECADE_FILE, null) == null || App.prefs.getBooleanPref(PrefKey.SOUND_USE_SYSTEM_DEFAULTS, false))
	    		playSoundFileFromResources("measureDecade.wav");
	    	else
	    		playSoundFile(new File(App.prefs.getPref(PrefKey.SOUND_MEASURE_DECADE_FILE, null)));
	    	break;
	    	
		case MEASURE_ERROR:	    	
	    	if(App.prefs.getPref(PrefKey.SOUND_MEASURE_ERROR_FILE, null) == null || App.prefs.getBooleanPref(PrefKey.SOUND_USE_SYSTEM_DEFAULTS, false))
	    		playSoundFileFromResources("measureError.wav");
	    	else
	    		playSoundFile(new File(App.prefs.getPref(PrefKey.SOUND_MEASURE_ERROR_FILE, null)));
	    	break;
	    	
	    default:
	    	log.debug("Unknown system sound");
		}
		
		
		
	}
	

    /**
     * Play a sound from the Sounds resources folder by name (including ext)
     * 
     * @param file
     */
    public static void playSoundFileFromResources(String file)
    {
    	if(App.prefs.getBooleanPref(PrefKey.SOUND_ENABLED, true)==false) return;
    	
    	try {
    		InputStream myStream = new BufferedInputStream(cl.getResourceAsStream("Sounds/"+file));
    		new ClipPlayer(myStream).start();
    		 
		} catch (Exception e) {
			log.error("unable to play sound file : "+file);
			e.printStackTrace();
		}
    }
    
    /**
     * Play a sound file
     * 
     * @param file
     */
    public static void playSoundFile(File file)
    {
    	if(App.prefs.getBooleanPref(PrefKey.SOUND_ENABLED, true)==false) return;
    	
    	try {
			new ClipPlayer(file).start();
		} catch (Exception e) {
			log.error("unable to play sound file : "+file);
			e.printStackTrace();
		}
    }
    

}
