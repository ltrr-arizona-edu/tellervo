package edu.cornell.dendro.corina.util.labels;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class PetesCustomLabels implements LabelPage {
	private final static float dpi = 72.0f;
	private final static float mmpd = dpi / 25.4f;

	public float getLabelHeight() {
		return 8 * mmpd;
	}

	public float getLabelHorizontalGap() {
		return 10 * mmpd;
	}

	public float getLabelVerticalGap() {
		return 0;
	}

	public float getLabelWidth() {
		return 100 * mmpd;
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
