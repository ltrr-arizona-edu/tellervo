package edu.cornell.dendro.corina.util.labels;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class CornellSampleLabelPage implements LabelPage {
	private final static float dpi = 72.0f;
	private final static float mmpd = dpi / 25.4f;

	public float getLabelHeight() {
		return 8 * mmpd;
		//return 20 * mmpd;
	}

	public float getLabelHorizontalGap() {
		return 0 * mmpd;
	}

	public float getLabelVerticalGap() {
		return 0;
	}

	public float getLabelWidth() {
		return 70 * mmpd;
	}

	public float getPageBottomMargin() {
		return 0;
	}

	public float getPageLeftMargin() {
		return 0;
	}

	public float getPageRightMargin() {
		return 0;
	}

	public Rectangle getPageSize() {
		return PageSize.LETTER;
	}

	public float getPageTopMargin() {
		return 0;
	}

}
