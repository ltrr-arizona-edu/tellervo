package org.tellervo.desktop.gui.dbbrowse;

import java.util.Comparator;


public class SearchParameterComparator implements Comparator<SearchParameterNameEx> {
	

	@Override
	public int compare(SearchParameterNameEx t, SearchParameterNameEx t1) {
		
		return t.toString().compareTo(t1.toString());
	}
}
