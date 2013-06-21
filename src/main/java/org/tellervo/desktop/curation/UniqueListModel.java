package org.tellervo.desktop.curation;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class UniqueListModel extends AbstractListModel<TridasSample> {

	private ArrayList<TridasSample> data = new ArrayList<TridasSample>();
	

	private static final long serialVersionUID = 1L;

	public UniqueListModel()
	{
		
	}

	
	public UniqueListModel(ArrayList<TridasSample> data)
	{
		this.data = data;
	}

	public void clear()
	{
		data = new ArrayList<TridasSample>();
	}
	
	
	public ArrayList<TridasSample> getAllSamples()
	{
		return data;
	}
	
	public void addElement(TridasSample item)
	{
		for(TridasSample s : data)
		{
			if(s.getIdentifier().getValue().equals(item.getIdentifier().getValue()))
			{
				return;
			}
		}
		
			
		data.add(item);
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(data, numSorter);	
		
		this.fireContentsChanged(this, 0, data.size());

	
	}
	
	public void removeElementAt(int ind)
	{
		data.remove(ind);
		this.fireContentsChanged(this, 0, data.size());
	}
	
	@Override
	public TridasSample getElementAt(int index) {
		return data.get(index);
	}

	
	@Override
	public int getSize() {
		return data.size();
	}
	
}
