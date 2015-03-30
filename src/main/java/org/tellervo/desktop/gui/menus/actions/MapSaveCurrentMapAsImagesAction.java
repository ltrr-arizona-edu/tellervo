package org.tellervo.desktop.gui.menus.actions;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwindx.examples.ScreenShots;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.ui.Builder;

import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;

public class MapSaveCurrentMapAsImagesAction extends AbstractAction implements RenderingListener {

	private static final long serialVersionUID = 1L;
	private FullEditor editor;
	private WorldWindowGLCanvas wwd;
	JFileChooser fileChooser;
	private File snapFile;
	
	public MapSaveCurrentMapAsImagesAction(FullEditor editor) {
        super("Save current map as images", Builder.getIcon("map.png", 22));
		putValue(SHORT_DESCRIPTION, "Save current map as images");
        this.editor = editor;

    }

	@Override
	public void actionPerformed(ActionEvent e) {		
		
  
        this.fileChooser = new JFileChooser();
        this.snapFile = this.chooseFile(editor);
		
	}
	
	 private File chooseFile(Component parentFrame)
	    {
	        File outFile = null;

	        try
	        {
	            while (true)
	            {
	                fileChooser.setDialogTitle("Save Screen Shot");
	                fileChooser.setSelectedFile(new File(composeSuggestedName()));
	                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Screenshot", "png"));
	                fileChooser.setAcceptAllFileFilterUsed(false);

	                int status = fileChooser.showSaveDialog(parentFrame);
	                if (status != JFileChooser.APPROVE_OPTION)
	                    return null;

	                outFile = fileChooser.getSelectedFile();
	                if (outFile == null) // Shouldn't happen, but include a reaction just in case
	                {
	                    JOptionPane.showMessageDialog(parentFrame, "Please select a location for the image file.",
	                        "No Location Selected", JOptionPane.ERROR_MESSAGE);
	                    continue;
	                }

	                if (!outFile.getPath().endsWith(".png"))
	                    outFile = new File(outFile.getPath() + ".png");

	                if (outFile.exists())
	                {
	                    status = JOptionPane.showConfirmDialog(parentFrame,
	                        "Replace existing file\n" + outFile.getName() + "?",
	                        "Overwrite Existing File?", JOptionPane.YES_NO_CANCEL_OPTION);
	                    if (status == JOptionPane.NO_OPTION)
	                        continue;
	                    if (status != JOptionPane.YES_OPTION)
	                        return null;
	                }
	                break;
	            }
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }

	        editor.getMapPanel().getWwd().removeRenderingListener(this); // ensure not to add a duplicate
	        editor.getMapPanel().getWwd().addRenderingListener(this);

	        return outFile;
	    }
	 
	    private String composeSuggestedName()
	    {
	        String baseName = "WWJSnapShot";
	        String suffix = ".png";

	        File currentDirectory = this.fileChooser.getCurrentDirectory();

	        File candidate = new File(currentDirectory.getPath() + File.separatorChar + baseName + suffix);
	        for (int i = 1; candidate.exists(); i++)
	        {
	            String sequence = String.format("%03d", i);
	            candidate = new File(currentDirectory.getPath() + File.separatorChar + baseName + sequence + suffix);
	        }

	        return candidate.getPath();
	    }
	    public void stageChanged(RenderingEvent event)
	    {
	        if (event.getStage().equals(RenderingEvent.AFTER_BUFFER_SWAP) && this.snapFile != null)
	        {
	            try
	            {
	                GLAutoDrawable glad = (GLAutoDrawable) event.getSource();
	                AWTGLReadBufferUtil glReadBufferUtil = new AWTGLReadBufferUtil(glad.getGLProfile(), false);
	                BufferedImage image = glReadBufferUtil.readPixelsToBufferedImage(glad.getGL(), true);
	                String suffix = WWIO.getSuffix(this.snapFile.getPath());
	                ImageIO.write(image, suffix, this.snapFile);
	                System.out.printf("Image saved to file %s\n", this.snapFile.getPath());
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	            finally
	            {
	                this.snapFile = null;
	                this.wwd.removeRenderingListener(this);
	            }
	        }
	    }

  
}
