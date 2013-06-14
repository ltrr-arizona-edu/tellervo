package org.tellervo.desktop.tridasv2.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;


public class ImagePreviewPanel extends JPanel {
    
	private final static Logger log = LoggerFactory.getLogger(ImagePreviewPanel.class);
	
	private static final long serialVersionUID = 1L;
	private Image image;
    private Image scaledImage;
    private int imageWidth = 0;
    private int imageHeight = 0;
    //private long paintCount = 0;
    
    //constructor
    public ImagePreviewPanel() {
        super();
        setLayout(new MigLayout("", "[10px,grow,center]", "[232.00px,grow,center]"));
    }
    
    public void clearImage()
    {
    	image = null;
    	scaledImage = null;
    	imageWidth =0;
    	imageHeight =0;
    }
    
    public void loadImage(String file) throws IOException {

        image = ImageIO.read(new File(file));
        //might be a situation where image isn't fully loaded, and
        //  should check for that before setting...
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        setScaledImage();
    }
    
    public void loadPlaceholderImage() 
    {
    	image = Builder.getIconAsImage("questionmark.png", 64);
    	if(image==null) {
    		
    		log.error("Unable to load placeholder image");
    		return;
    	}
    	
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        setScaledImage();
    	
    }
    
    public boolean loadImage(URI uri)
    {
    	if(uri==null) 
    	{
    		loadPlaceholderImage();
    		return false;
    	}
    	
    	File f = null;
    	try{
    		f = new File(uri);  	
    	} catch (IllegalArgumentException ex)
    	{
    		loadPlaceholderImage();
    		return false;
    	}
    	
    	if(f.length() > 3145728.0)
    	{
    		// > 3mb
    		log.debug("Image too large to preview.");
    		loadPlaceholderImage();
    		return false;
    	}
    	else
    	{
    		try{
    			image = ImageIO.read(f);
    		} catch (IOException ex)
    		{
        		loadPlaceholderImage();
        		return false;

    		}
    	}
    	
    	if(image==null) {
    		
    		loadPlaceholderImage();
    		return false;
    	}
    	
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight(this);
        setScaledImage();
        return true;
    }
    
    //e.g., containing frame might call this from formComponentResized
    public void scaleImage() {
        setScaledImage();
    }
    
    //override paintComponent
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if ( scaledImage != null ) {
            //System.out.println("ImagePanel paintComponent " + ++paintCount);
            g.drawImage(scaledImage, 0, 0, this);
        }
    }
    
    private void setScaledImage() {
        if ( image != null ) {

            //use floats so division below won't round
            float iw = imageWidth;
            float ih = imageHeight;
            float pw = this.getWidth();   //panel width
            float ph = this.getHeight();  //panel height
            
            
            if ( pw < iw || ph < ih ) {
                
                /* compare some ratios and then decide which side of image to anchor to panel
                   and scale the other side
                   (this is all based on empirical observations and not at all grounded in theory)*/
                
                //System.out.println("pw/ph=" + pw/ph + ", iw/ih=" + iw/ih);

                if ( (pw / ph) > (iw / ih) ) {
                    iw = -1;
                    ih = ph;
                } else {
                    iw = pw;
                    ih = -1;
                }
                
                //prevent errors if panel is 0 wide or high
                if (iw == 0) {
                    iw = -1;
                }
                if (ih == 0) {
                    ih = -1;
                }
                
                scaledImage = image.getScaledInstance(
                            new Float(iw).intValue(), new Float(ih).intValue(), Image.SCALE_DEFAULT);
                
            } else {
                scaledImage = image;
            }
        
            //System.out.println("iw = " + iw + ", ih = " + ih + ", pw = " + pw + ", ph = " + ph);
        }
    }
    
}
