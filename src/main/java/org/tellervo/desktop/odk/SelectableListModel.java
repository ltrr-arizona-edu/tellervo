package org.tellervo.desktop.odk;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;

import org.apache.commons.lang.ArrayUtils;

public class SelectableListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	ArrayList<SelectableChoice> choices = new ArrayList<SelectableChoice>();
	
	public SelectableListModel()
	{
		
	}
	

	public SelectableListModel(ArrayList<SelectableChoice> choices)
	{
		setChoices(choices); 
	}
	

	public void setChoices(ArrayList<SelectableChoice> choices)
	{
		if(choices==null)
		{
			this.choices = new ArrayList<SelectableChoice>();
		}
		else
		{
			this.choices = choices;
		}
	}
	
	public void clearChoices()
	{
		setChoices(null);
	}
	
	public void setInclusiveSelectedAt(int[] indices)
	{
		ArrayList<Integer> selIndices = new ArrayList<Integer>();
		
		for(int i: indices)
		{
			selIndices.add(i);
		}
		
		for(int i=0; i<choices.size(); i++)
		{
			if(selIndices.contains(i))
			{
				setSelectedAt(i, true);
			}
			else
			{
				setSelectedAt(i, false);
			}
		}
		

	}
	
	public void setSelectedAt(int index, boolean selected)
	{
		SelectableChoice item = (SelectableChoice) getElementAt(index);
		item.setSelected(selected);
	}
	
	public void addElement(SelectableChoice item)
	{
		choices.add(item);
	}
	
	public void addAllElements(Collection<SelectableChoice> items)
	{
		choices.addAll(items);
	}
	
	public void removeElement(SelectableChoice item)
	{
		choices.remove(item);
	}
	
	public void removeElementAt(int index)
	{
		choices.remove(index);
	}
	
	public ArrayList<SelectableChoice> getAllChoices()
	{
		return this.choices;
	}
	
	
	public ArrayList<SelectableChoice> getSelectedChoices()
	{
		ArrayList<SelectableChoice> sel = new ArrayList<SelectableChoice>();
		
		for(SelectableChoice choice : choices)
		{
			if(choice.isSelected()) sel.add(choice);
		}
		
		return sel;
	}
	
	public int[] getSelectedIndices()
	{
		ArrayList<Integer> sel = new ArrayList<Integer>();
		
		for(int i=0; i<choices.size(); i++)
		{
			if(choices.get(i).isSelected())
			{
				sel.add(i);
			}
		}
			
		return ArrayUtils.toPrimitive(sel.toArray(new Integer[sel.size()]));
		
	}
	
	
	@Override
	public Object getElementAt(int arg0) {
		return choices.get(arg0);
	}

	@Override
	public int getSize() {
		return choices.size();
	}

}
