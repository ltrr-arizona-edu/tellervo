package org.tellervo.desktop.labelgen;

import java.util.ArrayList;

import javax.swing.Icon;

import org.tellervo.desktop.ui.Builder;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Abstract class that should be implemented by all types of box or sample label styles.  Primary method of this class is
 * outputPDFToStream() which uses the style to create  
 * 
 * @author pbrewer
 *
 */
public abstract class AbstractTellervoLabelStyle {

	
	protected final String name;
	protected final String description;
	protected final ItemType itemType;
	protected Document document;
	protected PdfContentByte cb;
	protected float barcodeSize = 0.7f;
	protected LabelSummarizationType summarizationType = LabelSummarizationType.ELEMENT;
	
	private final String configureableLine1Description;
	private final String configureableLine2Description;
	private final String configureableLine3Description;
	private final String configureableLine4Description;
	private final String configureableLine5Description;
	
	private String line1TextOverride = null;
	private String line2TextOverride = null;
	private String line3TextOverride = null;
	private String line4TextOverride = null;
	private String line5TextOverride = null;
	private boolean isLabelSummarizationTypeConfigurable = false; 
	
	
	enum LabelSummarizationType {
		ELEMENT, SAMPLE, EXTERNALID
	}
	
	enum ItemType{
		BOX, SAMPLE, GENERIC
	}

	/**
	 * Basic label with no configureable lines 
	 * 
	 * @param name
	 * @param description
	 * @param itemType
	 */
	public AbstractTellervoLabelStyle(String name, String description, ItemType itemType)
	{
		this.name = name;
		this.description = description;
		this.itemType = itemType;
		configureableLine1Description = null;
		configureableLine2Description = null;
		configureableLine3Description = null;
		configureableLine4Description = null;
		configureableLine5Description = null;
	}
	
	/**
	 * Constructor for a label with configureable lines of text.  If any of the lines
	 * are not used, set the descriptions to null  
	 *  
	 * @param name
	 * @param description
	 * @param itemType
	 * @param line1Description
	 * @param line2Description
	 * @param line3Description
	 * @param line4Description
	 */
	public AbstractTellervoLabelStyle(String name, String description, ItemType itemType,
			String line1Description, String line2Description, String line3Description, String line4Description, 
			String line5Description)
	{
		this.name = name;
		this.description = description;
		this.itemType = itemType;
		configureableLine1Description = line1Description;
		configureableLine2Description = line2Description;
		configureableLine3Description = line3Description;
		configureableLine4Description = line4Description;
		configureableLine5Description = line5Description;
	}
	
	/**
	 * Set how box labels should summarize contents 
	 * 
	 * @param type
	 */
	public void setLabelSummarizationType(LabelSummarizationType type)
	{
		this.summarizationType = type;
	}
	
	/**
	 * Get how box labels should summarize contents
	 * 
	 * @return
	 */
	public LabelSummarizationType getLabelSummarizationType()
	{
		return summarizationType;
	}
	
	/**
	 * Set the minimum bar size for barcode.  Default is 0.7f
	 * 
	 * 
	 * @param size
	 */
	public void setBarcodeSize(float size)
	{
		barcodeSize = size;
	}
	
	
	/**
	 * Get image of full page of labels
	 * 
	 * @return
	 */
	public Icon getPageImage()
	{
		
		try{
			return Builder.getImageAsIcon("/LabelStyles/"+this.getClass().getSimpleName()+"-page.png");
		} catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Get image of individual label
	 * 
	 * @return
	 */
	public Icon getLabelImage()
	{
		try{
			return Builder.getImageAsIcon("/LabelStyles/"+this.getClass().getSimpleName()+"-label.png");
		} catch (Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Get name of style
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return getName();
	}
	
	/**
	 * Get description of style
	 * 
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Get ItemType associated with this label style e.g. Box or Sample
	 * 
	 * @return
	 */
	public ItemType getItemType()
	{
		return itemType;
	}
	
	
	/**
	 * Get number of labels on one page
	 * 
	 * @return
	 */
	public int getLabelCountPerPage()
	{
		return 1;
	}
	
	/**
	 * Get the description for what line 1 is used for
	 * 
	 * @return
	 */
	public String getLine1Description()
	{
		if(hasConfigurableLine1())
		{
			return this.configureableLine1Description;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * Get the description for what line 2 is used for
	 * 
	 * @return
	 */
	public String getLine2Description()
	{
		if(hasConfigurableLine2())
		{
			return this.configureableLine2Description;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * Get the description for what line 3 is used for
	 * 
	 * @return
	 */
	public String getLine3Description()
	{
		if(hasConfigurableLine3())
		{
			return this.configureableLine3Description;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * Get the description for what line 4 is used for
	 * 
	 * @return
	 */
	public String getLine4Description()
	{
		if(hasConfigurableLine4())
		{
			return this.configureableLine4Description;
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * Get the description for what line 5 is used for
	 * 
	 * @return
	 */
	public String getLine5Description()
	{
		if(hasConfigurableLine5())
		{
			return this.configureableLine5Description;
		}
		else
		{
			return "";
		}
	}
	
	
	/**
	 * Has a configureable line 1 in the label
	 * 
	 * @return
	 */
	public boolean hasConfigurableLine1() {
				
		return configureableLine1Description!=null;
	}
	
	/**
	 * Has a configureable line 2 in the label
	 * 
	 * @return
	 */
	public boolean hasConfigurableLine2() {
		
		return configureableLine2Description!=null;
	}
	
	/**
	 * Has a configureable line 3 in the label
	 * 
	 * @return
	 */
	public boolean hasConfigurableLine3() {
		
		return configureableLine3Description!=null;
	}
	
	/**
	 * Has a configureable line 4 in the label
	 * 
	 * @return
	 */
	public boolean hasConfigurableLine4() {
		
		return configureableLine4Description!=null;
	}
	
	/**
	 * Has a configureable line 5 in the label
	 * 
	 * @return
	 */
	public boolean hasConfigurableLine5() {
		
		return configureableLine5Description!=null;
	}
	
	/**
	 * Use style to create a PDF output stream for the list of items provided
	 * 
	 * @param output
	 * @param items
	 * @throws Exception
	 */
	public abstract void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception;

	/**
	 * Get the text to use for line 1 of the label
	 * 
	 * @return
	 */
	public String getLine1OverrideText()
	{
		return line1TextOverride;
	}
	
	/**
	 * Get the text to use for line 2 of the label
	 * 
	 * @return
	 */
	public String getLine2OverrideText()
	{
		return line2TextOverride;
	}
	
	/**
	 * Get the text to use for line 3 of the label
	 * 
	 * @return
	 */
	public String getLine3OverrideText()
	{
		return line3TextOverride;
	}

	/**
	 * Get the text to use for line 4 of the label
	 * 
	 * @return
	 */
	public String getLine4OverrideText()
	{
		return line4TextOverride;
	}
	
	/**
	 * Get the text to use for line 5 of the label
	 * 
	 * @return
	 */
	public String getLine5OverrideText()
	{
		return line5TextOverride;
	}
	
	
	/**
	 * Override the value of the text in line 1 for this label.  If line 1
	 * is not configurable then an exception is thrown 
	 *  
	 * @param value
	 * @throws Exception
	 */
	public void setLine1OverrideText(String value) throws Exception {
		
		if(this.hasConfigurableLine1()==false)
		{
			throw new Exception("This label does not have a configureable line 1");
		}
		else
		{
			this.line1TextOverride = value;
		}
	}

	/**
	 * Override the value of the text in line 2 for this label.  If line 2
	 * is not configurable then an exception is thrown 
	 *  
	 * @param value
	 * @throws Exception
	 */
	public void setLine2OverrideText(String value) throws Exception {
		
		if(this.hasConfigurableLine2()==false)
		{
			throw new Exception("This label does not have a configureable line 2");
		}
		else
		{
			this.line2TextOverride = value;
		}
	}
	
	/**
	 * Override the value of the text in line 3 for this label.  If line 3
	 * is not configurable then an exception is thrown 
	 *  
	 * @param value
	 * @throws Exception
	 */
	public void setLine3OverrideText(String value) throws Exception {
		
		if(this.hasConfigurableLine3()==false)
		{
			throw new Exception("This label does not have a configureable line 3");
		}
		else
		{
			this.line3TextOverride = value;
		}
	}
	
	/**
	 * Override the value of the text in line 4 for this label.  If line 4
	 * is not configurable then an exception is thrown 
	 *  
	 * @param value
	 * @throws Exception
	 */
	public void setLine4OverrideText(String value) throws Exception {
		
		if(this.hasConfigurableLine4()==false)
		{
			throw new Exception("This label does not have a configureable line 4");
		}
		else
		{
			this.line4TextOverride = value;
		}
	}
	
	/**
	 * Override the value of the text in line 5 for this label.  If line 4
	 * is not configurable then an exception is thrown 
	 *  
	 * @param value
	 * @throws Exception
	 */
	public void setLine5OverrideText(String value) throws Exception {
		
		if(this.hasConfigurableLine5()==false)
		{
			throw new Exception("This label does not have a configureable line 5");
		}
		else
		{
			this.line5TextOverride = value;
		}
	}
	
	
	public void setIsLabelSummarizationTypeConfigurable(boolean b)
	{
		this.isLabelSummarizationTypeConfigurable = b;
	}
	
	public boolean isLabelSummarizationTypeConfigurable()
	{
		return this.isLabelSummarizationTypeConfigurable;
	}
	
	
}
