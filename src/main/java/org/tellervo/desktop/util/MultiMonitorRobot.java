package org.tellervo.desktop.util;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiMonitorRobot {

	private final static Logger log = LoggerFactory.getLogger(MultiMonitorRobot.class);

	
	public static void moveMouseToVirtualCoords(Point p)
	{
		GraphicsDevice dev = getDeviceForPoint(p);
		
		try {
			Robot robot = new Robot(dev);
			Point newPoint = getPointForPoint(p);
			
			
			robot.mouseMove(newPoint.x, newPoint.y);
			
			
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private static GraphicsDevice getDeviceForPoint(Point p)
	{
		GraphicsEnvironment e 
	     = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice[] devices = e.getScreenDevices();
	
		Rectangle displayBounds = null;
		GraphicsDevice theGraphicsDevice= null;
		
		//now get the configurations for each device
		for (GraphicsDevice device: devices) { 
	
		    GraphicsConfiguration[] configurations =
		        device.getConfigurations();
		    for (GraphicsConfiguration config: configurations) {
		        Rectangle gcBounds = config.getBounds();

		        
		        if(gcBounds.contains(p)) {
		        	return device;
		        }
		    }
		}
	
		return null;
	}
	
	
	private static Point getPointForPoint(Point p)
	{
		GraphicsEnvironment e 
	     = GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice[] devices = e.getScreenDevices();
	
		Rectangle displayBounds = null;
		GraphicsDevice theGraphicsDevice= null;
		
		//now get the configurations for each device
		for (GraphicsDevice device: devices) { 
	
		    GraphicsConfiguration[] configurations =
		        device.getConfigurations();
		    for (GraphicsConfiguration config: configurations) {
		        Rectangle gcBounds = config.getBounds();

		        
		        if(gcBounds.contains(p)) {
		        	return new Point(p.x-gcBounds.x, p.y-gcBounds.y);
		        }
		    }
		}
	
		return null;
	}
	
}
