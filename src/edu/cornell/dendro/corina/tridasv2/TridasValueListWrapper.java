package edu.cornell.dendro.corina.tridasv2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.tridas.schema.TridasValue;

public class TridasValueListWrapper { //implements List<Integer> {
	private List<TridasValue> list;
	
	public TridasValueListWrapper(List<TridasValue> list) {
		this.list = list;
		
	}
}
