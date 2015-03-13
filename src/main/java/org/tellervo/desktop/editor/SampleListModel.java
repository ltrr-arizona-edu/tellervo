package org.tellervo.desktop.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import javax.swing.AbstractListModel;

import org.tellervo.desktop.sample.Sample;

import edu.emory.mathcs.backport.java.util.Collections;

public class SampleListModel extends AbstractListModel<Sample> {

	private static final long serialVersionUID = 1L;
	ArrayList<Sample> list = new ArrayList<Sample>();
	
	public SampleListModel(Collection<Sample> list)
	{
		this.list = (ArrayList<Sample>) list;
		
	}
	
	public SampleListModel()
	{
		
	}
	
	public void addElement(Sample sample)
	{
		list.add(sample);
	}
	
	@Override
	public Sample getElementAt(int i) {
		return list.get(i);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	public void sortAscending(Comparator<Sample> comp)
	{
		Collections.sort(list, comp);
	}

	public void sortDescending(Comparator<Sample> comp)
	{
		Collections.sort(list, comp);
		Collections.reverse(list);
	}
	
	
	
}
