package org.tellervo.desktop.labelgen;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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
	
	enum ItemType{
		BOX, SAMPLE, GENERIC
	}
	
	
	
	public AbstractTellervoLabelStyle(String name, String description, ItemType itemType)
	{
		this.name = name;
		this.description = description;
		this.itemType = itemType;
	}
	
	/**
	 * Get image of full page of labels
	 * 
	 * @return
	 */
	public Icon getPageImage()
	{
		return null;
	}
	
	/**
	 * Get image of individual label
	 * 
	 * @return
	 */
	public Icon getLabelImage()
	{
		return null;
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
	 * Use style to create a PDF output stream for the list of items provided
	 * 
	 * @param output
	 * @param items
	 * @throws Exception
	 */
	public abstract void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception;

}
