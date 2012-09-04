package org.tellervo.desktop.util;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.schema.TridasObject;

public class FontUtil {

	private final static Logger log = LoggerFactory.getLogger(FontUtil.class);	
	private final static ClassLoader cl = org.tellervo.desktop.ui.Builder.class.getClassLoader();

	
	public static Font getFont(String filename)
	{
		Font realFont = null;
		Font baseFont = null;
		
		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream(filename));
			baseFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
			realFont = baseFont.deriveFont(Font.BOLD, 20);
		} catch (Exception ex) {
	        ex.printStackTrace();
	        log.error("Font failed to load");
	    }
		
		return realFont;
	}
	
	
	public static Font getFont(String filename, int size)
	{
		Font realFont = null;
		Font baseFont = null;
		
		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream(filename));
			baseFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
			realFont = baseFont.deriveFont(Font.PLAIN, size);
		} catch (Exception ex) {
	        ex.printStackTrace();
	        log.error("Font failed to load");
	    }
		
		return realFont;
	}
	
	
	public static Font getFont(String filename, int size, int style)
	{
		Font realFont = null;
		Font baseFont = null;
		
		try {
			InputStream myStream = new BufferedInputStream(new FileInputStream(filename));
			baseFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
			realFont = baseFont.deriveFont(style, size);
		} catch (Exception ex) {
	        ex.printStackTrace();
	        log.error("Font failed to load");
	    }
		
		return realFont;
	}
	
	public static Font getFontFromResources(String fontname, int size, int style)
	{		
		Font realFont = null;
		Font baseFont = null;
		
		try {
			InputStream myStream = new BufferedInputStream(cl.getResourceAsStream("Fonts/"+fontname));
			baseFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
			realFont = baseFont.deriveFont(style, size);
		} catch (Exception ex) {
	        ex.printStackTrace();
	        log.error("Font failed to load");
	    }
		
		return realFont;
		
	}
}
