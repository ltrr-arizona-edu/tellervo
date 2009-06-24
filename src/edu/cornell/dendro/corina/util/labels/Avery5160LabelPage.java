package edu.cornell.dendro.corina.util.labels;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class Avery5160LabelPage implements LabelPage {
	private final static float dpi = 72.0f;
	
	private final static float H = 1.0f * dpi;
	private final static float W = 2.6f * dpi;
	
	private final static float LEFTRIGHTMARGIN = .17f * dpi;
	private final static float TOPBOTTOMMARGIN = .50f * dpi;
	
	private final static float LABELGAP = .17f * dpi;
	
	public float getLabelHeight() {
		return H;
	}

	public float getLabelHorizontalGap() {
		return LABELGAP;
	}

	public float getLabelVerticalGap() {
		return 0;
	}

	public float getLabelWidth() {
		return W;
	}

	public float getPageBottomMargin() {
		return TOPBOTTOMMARGIN;
	}

	public float getPageLeftMargin() {
		return LEFTRIGHTMARGIN;
	}

	public float getPageRightMargin() {
		return LEFTRIGHTMARGIN;
	}

	public Rectangle getPageSize() {
		return PageSize.LETTER;
	}

	public float getPageTopMargin() {
		return TOPBOTTOMMARGIN;
	}

}
