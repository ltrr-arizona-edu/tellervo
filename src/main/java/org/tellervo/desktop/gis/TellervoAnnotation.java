/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gis;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwindx.examples.util.ImageAnnotation;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.WWTexture;
import gov.nasa.worldwind.util.Logging;

import java.awt.event.ActionListener;

import javax.media.opengl.GL;

public class TellervoAnnotation extends GlobeAnnotation implements ActionListener{

    //rotected ImageAnnotation busyImage = new BusyImage(BUSY_IMAGE_PATH);

    protected static final String CLOSE_IMAGE_PATH = "Icons/16x16/cancel.png";
    protected static final String SEARCHFORSERIES_IMAGE_PATH = "Icons/searchforseries.png";
    protected static final String VIEWMETADATA_IMAGE_PATH = "Icons/viewmetadata.png";
    protected static final String PIXEL22_MASK_PATH = "Icons/16x16/button-depressed-mask.png";

    protected static final String BUSY_IMAGE_PATH = "Icons/16x16/loadingindicator.gif";
    protected static final String PIXEL16_MASK_PATH = "Icons/16x16/button-depressed-mask.png";

    protected static final String CLOSE_TOOLTIP_TEXT = "Close window";
    protected static final String SEARCHFORSERIES_TOOLTIP_TEXT = "Search for associated series";
    protected static final String VIEWMETADATA_TOOLTIP_TEXT = "View metadata";

    
    protected boolean busy;

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    
	public TellervoAnnotation(Position position)
	{
		super("", position);
	
        this.setBusy(false);
	}
	


    public boolean isBusy()
    {
        return this.busy;
    }

    public void setBusy(boolean busy)
    {
        this.busy = busy;
        //this.getBusyImage().getAttributes().setVisible(busy);
    }

    public ImageAnnotation getBusyImage()
    {
        //return this.busyImage;
    	return null;
    }
    
    public java.awt.event.ActionListener[] getActionListeners()
    {
        return this.listenerList.getListeners(java.awt.event.ActionListener.class);
    }

    public void addActionListener(java.awt.event.ActionListener listener)
    {
        this.listenerList.add(java.awt.event.ActionListener.class, listener);
    }

    public void removeActionListener(java.awt.event.ActionListener listener)
    {
        this.listenerList.remove(java.awt.event.ActionListener.class, listener);
    }

    //**************************************************************//
    //********************  Action Listener  ***********************//
    //**************************************************************//

    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        // Notify my listeners of the event.
        this.fireActionPerformed(e);
    }

    protected void fireActionPerformed(java.awt.event.ActionEvent e)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == java.awt.event.ActionListener.class)
            {
                ((java.awt.event.ActionListener) listeners[i+1]).actionPerformed(e);
            }
        }
    }



    //**************************************************************//
    //********************  Busy Image  ****************************//
    //**************************************************************//

    protected static class BusyImage extends ImageAnnotation
    {
        protected Angle angle;
        protected Angle increment;
        protected long lastFrameTime;

        public BusyImage(Object imageSource)
        {
            super(imageSource);
            this.setUseMipmaps(false);

            this.angle = Angle.ZERO;
            this.increment = Angle.fromDegrees(300);
        }

        public Angle getAngle()
        {
            return this.angle;
        }

        public void setAngle(Angle angle)
        {
            if (angle == null)
            {
                String message = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
            }

            double a = angle.degrees % 360;
            a = (a > 180) ? (a - 360) : (a < -180 ? 360 + a : a);
            this.angle = Angle.fromDegrees(a);
        }

        public Angle getIncrement()
        {
            return this.increment;
        }

        public void setIncrement(Angle angle)
        {
            if (angle == null)
            {
                String message = Logging.getMessage("nullValue.AngleIsNull");
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
            }
            
            this.increment = angle;
        }

        public void drawContent(DrawContext dc, int width, int height, double opacity, Position pickPosition)
        {
            super.drawContent(dc, width, height, opacity, pickPosition);
            this.updateState(dc);
        }

        /*protected void transformBackgroundImageCoordsToAnnotationCoords(DrawContext dc, int width, int height,
            WWTexture texture)
        {
            GL gl = dc.getGL();

            // Rotate around an axis originating from the center of the image and coming out of the screen.
            double hw = (double) texture.getWidth(dc) / 2d;
            double hh = (double) texture.getHeight(dc) / 2d;
            gl.glTranslated(hw, hh, 0);
            gl.glRotated(-this.getAngle().degrees, 0, 0, 1);
            gl.glTranslated(-hw, -hh, 0);

            super.transformBackgroundImageCoordsToAnnotationCoords(dc, width, height, texture);
        }*/

        protected void updateState(DrawContext dc)
        {
            // Increment the angle by a fixed increment each frame.
            Angle increment = this.getIncrement();
            increment = this.adjustAngleIncrement(dc, increment);
            this.setAngle(this.getAngle().add(increment));

            // Fire a property change to force a repaint.
            dc.getView().firePropertyChange(AVKey.VIEW, null, dc.getView());

            // Update the frame time stamp.
            this.lastFrameTime = dc.getFrameTimeStamp();
        }

        protected Angle adjustAngleIncrement(DrawContext dc, Angle unitsPerSecond)
        {
            long millis = dc.getFrameTimeStamp() - this.lastFrameTime;
            double seconds = millis / 1000.0;
            double degrees = seconds * unitsPerSecond.degrees;

            return Angle.fromDegrees(degrees);
        }
    }
}
