package org.tellervo.desktop.util;

import java.io.File; 
import java.io.IOException; 
import java.io.InputStream;

import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.DataLine; 
import javax.sound.sampled.FloatControl; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.SourceDataLine; 
import javax.sound.sampled.UnsupportedAudioFileException; 
 
public class ClipPlayer extends Thread { 
 
    private File soundFile;
    private Position curPosition;
    private InputStream soundFileStream;
 
    private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb 
 
    enum Position { 
        LEFT, RIGHT, NORMAL
    };
 
    public ClipPlayer(String wavfile) { 
    	openFile(wavfile);
        curPosition = Position.NORMAL;
    } 
 
    public ClipPlayer(String wavfile, Position p) { 
    	openFile(wavfile);
        curPosition = p;
    } 
    
    public ClipPlayer(File file)
    {
    	soundFile = file;
    	curPosition = Position.NORMAL;
    }
    
    public ClipPlayer(InputStream stream)
    {
    	soundFileStream = stream;
    	curPosition = Position.NORMAL;
    }
    
    private void openFile(String filename)
    {
        soundFile = new File(filename);
        if (!soundFile.exists()) { 
            System.err.println("Wave file not found: " + filename);
            return;
        } 
    }
 
    public void run() { 
 
    	if(soundFile==null && soundFileStream==null) return;
    	
    	AudioInputStream audioInputStream = null;
    	if(soundFileStream!=null)
    	{
            try { 
                audioInputStream = AudioSystem.getAudioInputStream(soundFileStream);
            } catch (UnsupportedAudioFileException e1) { 
                e1.printStackTrace();
                return;
            } catch (IOException e1) { 
                e1.printStackTrace();
                return;
            }
    	}
    	else
    	{
            try { 
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            } catch (UnsupportedAudioFileException e1) { 
                e1.printStackTrace();
                return;
            } catch (IOException e1) { 
                e1.printStackTrace();
                return;
            }
    	}

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try { 
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (LineUnavailableException e) { 
            e.printStackTrace();
            return;
        } catch (Exception e) { 
            e.printStackTrace();
            return;
        } 
 
        if (auline.isControlSupported(FloatControl.Type.PAN)) { 
            FloatControl pan = (FloatControl) auline
                    .getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT) 
                pan.setValue(1.0f);
            else if (curPosition == Position.LEFT) 
                pan.setValue(-1.0f);
        } 
 
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
 
        try { 
            while (nBytesRead != -1) { 
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) 
                    auline.write(abData, 0, nBytesRead);
            } 
        } catch (IOException e) { 
            e.printStackTrace();
            return;
        } finally { 
            auline.drain();
            auline.close();
        } 
 
    } 
} 