package org.tellervo.desktop.editor;

import java.util.Comparator;

import org.tellervo.desktop.sample.Sample;

public class EndYearComparator implements Comparator<Sample> {

	@Override
	public int compare(Sample o1, Sample o2) {
		return o1.getRange().getEnd().compareTo(o2.getRange().getEnd());
	}



}