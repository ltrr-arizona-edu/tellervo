package org.tellervo.desktop.labelgen;

import java.util.ArrayList;

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
	protected Document document = new Document();
	protected PdfContentByte cb;
	
	enum ItemType{
		BOX, SAMPLE
	}
	
	
	
	public AbstractTellervoLabelStyle(String name, String description, ItemType itemType)
	{
		this.name = name;
		this.description = description;
		this.itemType = itemType;
	}
	

	
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return getName();
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public ItemType getItemType()
	{
		return itemType;
	}
	
	
	public int getLabelCountPerPage()
	{
		return 1;
	}
	
	public abstract void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception;

}
