package org.tellervo.desktop.util.labels.v2;

import java.util.ArrayList;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

/**
 * Class defining layout for sample labels.  Labels consist of three portions (left, center, right).  Each portion can
 * have 0, 1, or 2 LabelItems.  
 * 
 * @author pbrewer
 *
 */
public class SampleLabelLayout {

	private Rectangle pageSize = PageSize.LETTER;
	
	private ArrayList<LabelItem> itemsLeft = new ArrayList<LabelItem>();
	private ArrayList<LabelItem> itemsCenter = new ArrayList<LabelItem>();
	private ArrayList<LabelItem> itemsRight = new ArrayList<LabelItem>();
	private int leftMargin = 5;
	private int rightMargin = 5;
	private int topMargin = 5;
	private int bottomMargin = 5;
	
	public SampleLabelLayout()
	{	
	}
		
	public void setTucsonStandardLayout()
	{
		itemsLeft.add(LabelItem.PI_AND_COLLECTION_YEAR);
		itemsLeft.add(LabelItem.OBJECT_NAME);
		itemsCenter.add(LabelItem.BARCODE);
		itemsRight.add(LabelItem.LABCODE_STANDARD);
		pageSize = PageSize.LETTER;
		
		leftMargin = 5;
		rightMargin = 5;
		topMargin = 5;
		bottomMargin = 5;
	}
	
	/**
	 * Set the page size. 
	 * 
	 * @param pageSize
	 */
	public void setPageSize(Rectangle pageSize)
	{
		this.pageSize = pageSize;
	}
	
	public Rectangle getPageSize()
	{
		return this.pageSize;
	}
	
	/**
	 * Set the LabelItem(s) to appear in the left portion of the label
	 * 
	 * @param itemsCenter
	 */
	public void setLeftItems(ArrayList<LabelItem> itemsLeft)
	{
		if(itemsLeft.size()>2)
		{
			throw new ArrayStoreException("Maximum of two items only");
		}
		this.itemsLeft = itemsLeft;
	}
	
	/**
	 * Set the LabelItem(s) to appear in the right portion of the label
	 * 
	 * @param itemsCenter
	 */
	public void setRightItems(ArrayList<LabelItem> itemsRight)
	{
		if(itemsRight.size()>2)
		{
			throw new ArrayStoreException("Maximum of two items only");
		}
		this.itemsRight = itemsRight;
	}
	
	/**
	 * Set the LabelItem(s) to appear in the center portion of the label
	 * 
	 * @param itemsCenter
	 */
	public void setCenterItems(ArrayList<LabelItem> itemsCenter)
	{
		if(itemsCenter.size()>2)
		{
			throw new ArrayStoreException("Maximum of two items only");
		}
		this.itemsCenter = itemsCenter;
	}
	
	
	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}


	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}


	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}


	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}


	enum LabelItem{
		BARCODE,
		LABCODE_STANDARD,
		LABCODE_INC_SUBOBJ,
		PI,
		PROJECT_NAME,
		DATABASE_NAME,
		OBJECT_NAME,
		OBJECT_AND_SUBOBJECT_NAME,
		CUSTOM_TEXT,
		COLLECTION_YEAR,
		PI_AND_COLLECTION_YEAR
	}
	
}
