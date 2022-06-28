package org.tellervo.desktop.editor;

import java.util.Comparator;

import org.tellervo.desktop.sample.Sample;

public class LengthComparator implements Comparator<Sample> {

	@Override
	public int compare(Sample o1, Sample o2) {
		
		Integer o1length = o1.getRange().getSpan();
		Integer o2length = o2.getRange().getSpan();
		
		return o1length.compareTo(o2length);
	}



}