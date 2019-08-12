package org.tellervo.desktop.labelgen;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

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
	
	enum ItemType{
		BOX, SAMPLE, GENERIC
	}
	
	float barcodeSize = 0.7f;
	

	
	public AbstractTellervoLabelStyle(String name, String description, ItemType itemType)
	{
		this.name = name;
		this.description = description;
		this.itemType = itemType;
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
	 * Use style to create a PDF output stream for the list of items provided
	 * 
	 * @param output
	 * @param items
	 * @throws Exception
	 */
	public abstract void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception;

}
