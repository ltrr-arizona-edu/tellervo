package org.tellervo.desktop.gis;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.examples.util.ButtonAnnotation;
import gov.nasa.worldwind.examples.util.ImageAnnotation;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.AnnotationFlowLayout;
import gov.nasa.worldwind.render.AnnotationNullLayout;
import gov.nasa.worldwind.render.FrameFactory;
import gov.nasa.worldwind.render.ScreenAnnotation;

import org.tellervo.desktop.admin.BoxCuration;
import org.tellervo.desktop.gis.TellervoAnnotation.BusyImage;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.util.TridasObjectEx;

public class TridasAnnotation extends TellervoAnnotation {

	private ITridas entity;
	private ScreenAnnotation titleLabel;
	private ScreenAnnotation timestampLabel;
    protected ButtonAnnotation closeButton;
	private ButtonAnnotation searchForSeries;
	private ButtonAnnotation viewMetadata;
	
	public TridasAnnotation(Position position, ITridas entity) {
		super(position);
		
		this.entity = entity; 
        this.initComponents();
        this.layoutComponents();

	}

    public ButtonAnnotation getCloseButton()
    {
        return this.closeButton;
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
    	
    	if (entity instanceof TridasObjectEx)
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
    			if (gf.getName().equals("tellervo.objectLabCode")){
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
    	try{
		content+= "<br><font color=\"#CCCCCC\">Created: " + BoxCuration.formatXMLGregorianCalendar(entity.getCreatedTimestamp().getValue()) +"";
		content+= "<br>Last Updated: " + BoxCuration.formatXMLGregorianCalendar(entity.getLastModifiedTimestamp().getValue()) +"";
    	} catch (Exception e){}
		return content;
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
	
}
