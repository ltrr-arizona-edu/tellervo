package edu.cornell.dendro.corina.gis;

import edu.cornell.dendro.corina.admin.BoxCuration;
import edu.cornell.dendro.corina.gui.dbbrowse.TridasTreeViewPanel;
import edu.cornell.dendro.corina.ui.Builder;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.examples.util.ButtonAnnotation;
import gov.nasa.worldwind.examples.util.ImageAnnotation;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.AnnotationFlowLayout;
import gov.nasa.worldwind.render.AnnotationNullLayout;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.FrameFactory;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.render.WWTexture;
import gov.nasa.worldwind.util.Logging;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.media.opengl.GL;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

public class TridasAnnotation extends GlobeAnnotation implements ActionListener{

	ITridas entity;
	private ScreenAnnotation titleLabel;
	private ScreenAnnotation timestampLabel;
    protected ButtonAnnotation closeButton;
    protected ImageAnnotation busyImage;
	private ButtonAnnotation searchForSeries;
	private ButtonAnnotation viewMetadata;
	
    protected static final String CLOSE_IMAGE_PATH = "images/16x16-button-cancel.png";
    protected static final String SEARCHFORSERIES_IMAGE_PATH = "edu/cornell/dendro/corina_resources/Icons/searchforseries.png";
    protected static final String VIEWMETADATA_IMAGE_PATH = "edu/cornell/dendro/corina_resources/Icons/viewmetadata.png";
    protected static final String PIXEL22_MASK_PATH = "images/16x16-button-cancel.png";

    protected static final String BUSY_IMAGE_PATH = "images/indicator-16.gif";
    protected static final String PIXEL16_MASK_PATH = "images/16x16-button-depressed-mask.png";

    protected static final String CLOSE_TOOLTIP_TEXT = "Close window";
    protected static final String SEARCHFORSERIES_TOOLTIP_TEXT = "Search for associated series";
    protected static final String VIEWMETADATA_TOOLTIP_TEXT = "View metadata";

    
    protected boolean busy;

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    
	public TridasAnnotation(Position position, ITridas entity)
	{
		super("", position);
		this.entity = entity;
        this.initComponents();
        this.layoutComponents();
        this.setBusy(false);
	}
	


    public boolean isBusy()
    {
        return this.busy;
    }

    public void setBusy(boolean busy)
    {
        this.busy = busy;
        this.getBusyImage().getAttributes().setVisible(busy);
    }

    public ButtonAnnotation getCloseButton()
    {
        return this.closeButton;
    }

    public ImageAnnotation getBusyImage()
    {
        return this.busyImage;
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
    //********************  Annotation Components  *****************//
    //**************************************************************//

    protected void initComponents()
    {
    	this.titleLabel = new ScreenAnnotation("", new java.awt.Point());
    	this.setupTitle(this.titleLabel);
    	
    	this.timestampLabel = new ScreenAnnotation("", new java.awt.Point());
    	this.setupLabel(this.timestampLabel);
    	
        this.closeButton = new ButtonAnnotation(CLOSE_IMAGE_PATH, PIXEL16_MASK_PATH);
        this.closeButton.setActionCommand(AVKey.CLOSE);
        this.closeButton.addActionListener(this);
        this.closeButton.setToolTipText(CLOSE_TOOLTIP_TEXT);
        
        this.searchForSeries = new ButtonAnnotation(Builder.getIconURL("searchforseries.png", Builder.ICONS, 32), null);
        this.searchForSeries.setActionCommand("searchForSeries");
        this.searchForSeries.addActionListener(this);
        this.searchForSeries.setToolTipText(SEARCHFORSERIES_TOOLTIP_TEXT);
       
        this.viewMetadata = new ButtonAnnotation(Builder.getIconURL("viewmetadata.png", Builder.ICONS, 32), null);
        this.viewMetadata.setActionCommand("viewMetadata");
        this.viewMetadata.addActionListener(this);
        this.viewMetadata.setToolTipText(VIEWMETADATA_TOOLTIP_TEXT);
        
        this.busyImage = new BusyImage(BUSY_IMAGE_PATH);   
    }

    
    protected void layoutComponents()
    {
    	titleLabel.setText(getTitleContent());
    	
        AnnotationAttributes attribs = timestampLabel.getAttributes();
        attribs.setTextAlign(AVKey.LEFT);
    	timestampLabel.setText(getTimestampContent());
    	
        AnnotationNullLayout layout = new AnnotationNullLayout();
        this.setLayout(layout);
        closeButton.setEnabled(true);
        
        this.addChild(this.busyImage);
        this.addChild(this.closeButton);
        layout.setConstraint(this.busyImage, AVKey.NORTHWEST);
        layout.setConstraint(this.closeButton, AVKey.NORTHEAST);
        layout.setConstraint(titleLabel, AVKey.NORTH);       
        
        Annotation contentContainer = new ScreenAnnotation("", new java.awt.Point());
        {
            this.setupContainer(contentContainer);
            contentContainer.setLayout(new AnnotationFlowLayout(AVKey.VERTICAL, AVKey.LEFT, 15, 30	)); // hgap, vgap
            
            contentContainer.addChild(this.titleLabel);
            
            Annotation buttonContainer = new ScreenAnnotation("", new java.awt.Point());
            setupLabel(buttonContainer);
            buttonContainer.setLayout(new AnnotationFlowLayout(AVKey.HORIZONTAL, AVKey.CENTER, 30, 5)); // hgap, vgap
            buttonContainer.addChild(this.searchForSeries);
            buttonContainer.addChild(this.viewMetadata);
            
            contentContainer.addChild(buttonContainer);
            contentContainer.addChild(this.timestampLabel);
            
        }

        this.addChild(contentContainer);
        

        
    }


    private String getTitleContent()
    {
    	String content = "";
    	
    	if (entity instanceof TridasObject)
    	{
    		TridasObjectEx obj = (TridasObjectEx) entity;   		
    		if(obj.getLabCode()!=null)
    		{
    			content += "<font color=\"#04258E\"><b>"+obj.getLabCode()+"</b></font>";
    		}
    		
    		content+= "<br><b>"+obj.getTitle()+"</b><br/>";
    		
    		if(obj.isSetType())
    		{
    			content += "<font size=\"2\"><i>"+obj.getType().getNormal()+"</i></font><hr/><br/><br>";
    		}

    	}
    	else if (entity instanceof TridasElement)
    	{

    		TridasElement elem = (TridasElement) entity;
    		String objectCode = "??";
    		for(TridasGenericField gf : elem.getGenericFields())
    		{
    			if (gf.getName().equals("corina.objectLabCode")){
    				objectCode = gf.getValue().toString();
    			}
    		}
    		
    		content += "<b>"+objectCode+"-"+elem.getTitle()+"</b><br/>";
    		   		
    		if(elem.isSetTaxon())
    		{
    			content += "<font size=\"2\"><i>"+elem.getTaxon().getNormal()+"</i></font><hr/><br/><br>";
    		}
    		
    		if(elem.isSetType())
    		{
    			content += "<br><font size=\"2\">Element type : "+elem.getType().getNormal()+"</font><hr/><br/><br>";

    		}
    		
    	}
    	
    	return content;
    }
    
    private String getTimestampContent()
    {
    	String content = "";
		content+= "<br><font color=\"#CCCCCC\">Created: " + BoxCuration.formatXMLGregorianCalendar(entity.getCreatedTimestamp().getValue()) +"";
		content+= "<br>Last Updated: " + BoxCuration.formatXMLGregorianCalendar(entity.getLastModifiedTimestamp().getValue()) +"";
		return content;
    }
    
    protected void setupContainer(Annotation annotation)
    {
        AnnotationAttributes defaultAttribs = new AnnotationAttributes();
        this.setupDefaultAttributes(defaultAttribs);
        defaultAttribs.setAdjustWidthToText(Annotation.SIZE_FIT_TEXT);
        defaultAttribs.setSize(new java.awt.Dimension(300, 100));

        annotation.setPickEnabled(false);
        annotation.getAttributes().setDefaults(defaultAttribs);
    }

    protected void setupLabel(Annotation annotation)
    {
        AnnotationAttributes defaultAttribs = new AnnotationAttributes();
        this.setupDefaultAttributes(defaultAttribs);
        defaultAttribs.setAdjustWidthToText(Annotation.SIZE_FIT_TEXT);
        defaultAttribs.setSize(new java.awt.Dimension(300, 0));

        annotation.setPickEnabled(false);
        annotation.getAttributes().setDefaults(defaultAttribs);
    }

    protected void setupTitle(Annotation annotation)
    {
        this.setupLabel(annotation);

        AnnotationAttributes attribs = annotation.getAttributes();
        attribs.setFont(java.awt.Font.decode("Arial-15"));
        //attribs.setSize(new java.awt.Dimension(200, 50));
        attribs.setTextAlign(AVKey.LEFT);
        //attribs.setBackgroundColor(new java.awt.Color(5, 255, 255, 0));
    }
    
    protected void setupDefaultAttributes(AnnotationAttributes attributes)
    {
        java.awt.Color transparentBlack = new java.awt.Color(255, 255, 255, 150);

        attributes.setBackgroundColor(transparentBlack);
        attributes.setBorderColor(transparentBlack);
        attributes.setBorderWidth(0);
        attributes.setCornerRadius(0);
        attributes.setDrawOffset(new java.awt.Point(0, 0));
        attributes.setHighlightScale(1);
        attributes.setInsets(new java.awt.Insets(0, 0, 0, 0));
        attributes.setLeader(FrameFactory.LEADER_NONE);
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

        protected void transformBackgroundImageCoordsToAnnotationCoords(DrawContext dc, int width, int height,
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
        }

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
