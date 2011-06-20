package edu.cornell.dendro.corina.util.labels;

import com.lowagie.text.Rectangle;

public interface LabelPage {
	public float getLabelHeight();
	public float getLabelWidth();
	
	public float getPageTopMargin();
	public float getPageBottomMargin();
	public float getPageLeftMargin();
	public float getPageRightMargin();
	
	public float getLabelHorizontalGap();
	public float getLabelVerticalGap();
	
	public Rectangle getPageSize();
}
