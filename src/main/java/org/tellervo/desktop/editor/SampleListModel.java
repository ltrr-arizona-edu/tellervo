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
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public SampleListModel()
	{
		
	}
	
	public void addElement(Sample sample)
	{
		list.add(sample);
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public void addAll(Collection<Sample> items)
	{
		list.addAll(items);
		fireContentsChanged(this, 0, this.getSize());

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
		fireContentsChanged(this, 0, this.getSize());

	}

	public void sortDescending(Comparator<Sample> comp)
	{
		Collections.sort(list, comp);
		Collections.reverse(list);
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public void remove(Sample s)
	{
		list.remove(s);
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public void remove(int i)
	{
		list.remove(i);
		fireContentsChanged(this, 0, this.getSize());

	}
	

	public void fireContentsChanged(Object source, int index0, int index1) {
			super.fireContentsChanged(source, index0, index1);
		
	}

	public void fireIntervalAdded(Object source, int index0, int index1) {
			super.fireIntervalAdded(source, index0, index1);
		
	}

	public void fireIntervalRemoved(Object source, int index0, int index1) {
			super.fireIntervalAdded(source, index0, index1);
		
	}
	
}
