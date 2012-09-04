package org.tellervo.desktop.util;

import java.io.BufferedInputStream;
import java.io.File;
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
	
    /**
     * Play a beep for when barcode reader has scanned a barcode
     */
    public static void playBarcodeBeep(){   	
    	playSoundFileFromResources("barcodeScan.wav");
    }
    
    /**
     * Play a sound associated with a measurement
     */
    public static void playMeasureSound(){	
    	playSoundFileFromResources("measureRing.wav");
    }

    /**
     * Play the sound associated with the measurement of the 
     * 10th ring in a sequence.
     */
    public static void playMeasureDecadeSound(){
    	playSoundFileFromResources("measureDecade.wav");
    }
    
    /**
     * Play the sound associated with a measurement error
     */
    public static void playMeasureErrorSound(){
    	playSoundFileFromResources("measureError.wav");
    }

    /**
     * Play the sound associated with the measuring platform
     * initializing
     */
    public static void playMeasureInitSound(){
    	playSoundFileFromResources("initPlatform.wav");
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
