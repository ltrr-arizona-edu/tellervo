package org.tellervo.desktop.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

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
    	URL f = TridasObject.class.getClassLoader().getResource("Sounds/"+file);
    	try {
			new ClipPlayer(new File(f.toURI()), 1);
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
    	try {
			new ClipPlayer(file, 0);
		} catch (Exception e) {
			log.error("unable to play sound file : "+file);
			e.printStackTrace();
		}
    }
}
