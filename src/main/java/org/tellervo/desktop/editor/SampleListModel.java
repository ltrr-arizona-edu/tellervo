package org.tellervo.desktop.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import javax.swing.AbstractListModel;

import org.tellervo.desktop.sample.Sample;

import edu.emory.mathcs.backport.java.util.Collections;

public class SampleListModel extends AbstractListModel<Sample> {

	private static final long serialVersionUID = 1L;
	private ArrayList<Sample> list = new ArrayList<Sample>();
	private boolean dirty=false;
	
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
		dirty = true;
		fireContentsChanged(this, 0, this.getSize());

	}
	public void addElement(int row, Sample sample)
	{
		list.add(row, sample);
		dirty = true;
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public ArrayList<Sample> getSamples()
	{
		return list;
	}
	
	public void addAll(Collection<Sample> items)
	{
		list.addAll(items);
		dirty = true;
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public boolean isDirty()
	{
		return dirty;
	}
	
	public void setDirty(boolean b)
	{
		dirty = b;
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
		dirty = true;
		fireContentsChanged(this, 0, this.getSize());

	}
	
	public void remove(int i)
	{
		list.remove(i);
		dirty = true;
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
