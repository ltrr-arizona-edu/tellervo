package org.tellervo.desktop;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.gui.Toolbar;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.platform.Platform;

public class ImageJTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		App.platform = new Platform();
	    App.platform.init();
	    
	    
		
		
		System.out.println("Starting load...");
		ImagePlus imp = IJ.openImage("/tmp/Tree_Rings.jpg");
		System.out.println("Finished load...");
		
	
		
		Overlay overlay = new Overlay();
		imp.setOverlay(overlay);
		DendroImageWindow window = new DendroImageWindow(imp);
		
		Line line = new Line(5, 5, 10, 10);
		overlay.add(line);
		
		//System.out.println("Line length = "+line.getLength());
					


		
		window.setVisible(true);
		
		
		
		
		
		ImageJ imagej = new ImageJ();
		
		
		//imagej.setIconImage(Builder.getApplicationIcon());
		
		//imagej.setVisible(true);
		
		//Toolbar toolbar = Toolbar.getInstance();
		
		//toolbar.setTool("multipoint");
		
		
		/*JFrame frame = new JFrame();
		JButton save = new JButton("Save");
		
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Overlay o = window.getImagePlus().getOverlay();
				debugPositions(o.toArray());
				
				
			}
			
		});
		
		frame.setLayout(new BorderLayout());
		frame.add(save, BorderLayout.CENTER);
		
		frame.setVisible(true);*/
		
		/*ImageCanvas canvas = new ImageCanvas(imp);
		JFrame frame = new JFrame();
		
		frame.add(canvas);
		frame.setVisible(true);
		
		
		*/
		
		//imp.show();  

		  
		// Without getting back a pointer, and automatically showing it:  
		//IJ.open("/path/to/image.tif");  
		// Same but from an URL  
		//IJ.open("http://www.example.org/path/to/image.tif");  
		
	}
	
	private static void debugPositions(Roi[] list)
	{
		int n = 1;
		for(Roi roi : list)
		{
			PointRoi proi = (PointRoi) roi;
			for(int i =0; i < proi.getNCoordinates(); i++)
			{
				System.out.println(n+ " - "+ i+ " - "+proi.getXCoordinates()[i] + " x "+ proi.getYCoordinates()[i]);
			}
			
		}
	}

}
