package org.tellervo.desktop.odk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.AbstractListModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;


public class ODKFieldListModel extends AbstractListModel<ODKFieldInterface> implements Serializable {

	private static final long serialVersionUID = 1L;
	ArrayList<ODKFieldInterface> fields;
	private static final Logger log = LoggerFactory.getLogger(ODKFieldListModel.class);

	public ODKFieldListModel()
	{
		fields = new ArrayList<ODKFieldInterface>();
	}
	
	public ODKFieldListModel(ArrayList<ODKFieldInterface> fields)
	{
		this.fields = fields;
		Collections.sort(this.fields);
		this.fireIntervalAdded(this, 0, fields.size()-1);
	}
	
	public void addField(ODKFieldInterface field)
	{
		fields.add(field);
		Collections.sort(this.fields);
		this.fireIntervalAdded(this, 0, fields.size()-1);
	}
	
	public void addAllFields(Collection<ODKFieldInterface> fields)
	{
		if(fields==null || fields.size()==0) return;
		
		this.fields.addAll(fields);
		Collections.sort(this.fields);
		this.fireIntervalAdded(this, 0, fields.size()-1);

	}
	
	public void removeField(AbstractODKField field)
	{
		int index = this.fields.indexOf(field);
		
		if(index==-1)
		{
			//log.debug("Cannont remove field from list as it's not in the list");
			return;
		}
		
		this.fields.remove(field);
		try{
			this.fireIntervalRemoved(this, index, index);
		} catch (IndexOutOfBoundsException e)
		{
			//log.debug("Index out of bounds exception.  Trying to remove index: "+index+" when list size is now "+getSize());
		}
		//log.debug("Removed field at index "+index+ " the size of list is now "+getSize());

		Collections.sort(this.fields);

	}
	
	public void removeField(int index)
	{
		this.fields.remove(index);
		
		try{
			this.fireIntervalRemoved(this, index, index);
		} catch (IndexOutOfBoundsException e)
		{
			//log.debug("Index out of bounds exception.  Trying to remove index: "+index+" when list size is now "+getSize());
		}
		
		//log.debug("Removed field at index "+index+ " the size of list is now "+getSize());
		
		Collections.sort(this.fields);

	}
	
	public void removeFields(Collection<ODKFieldInterface> fields)
	{
		this.fields.removeAll(fields);
		if(fields.size()==0)
		{
			this.fireIntervalRemoved(this, 0, 0);
		}
		else
		{
			this.fireIntervalRemoved(this, 0, fields.size()-1);
		}
		//log.debug("Removed a bunch of fields, the size of list is now "+getSize());
		Collections.sort(this.fields);

	}
	
	@Override
	public ODKFieldInterface getElementAt(int i) {
		try{
			return fields.get(i);
		} catch (IndexOutOfBoundsException e)
		{
			//log.debug("IndexOutOfBoundsException fired when trying to get index "+i+ " when size = "+getSize());
		}
		return null;
	}

	@Override
	public int getSize() {
		return fields.size();
	}
	
	public Collection<ODKFieldInterface> getAllFields()
	{
		return fields;
	}
	
	public void swapEntries(int i, int j)
	{
		log.debug("Swapping entries: "+i+" and "+j);
		
		log.debug("List before swap: ");
		log.debug(fields.toString());
		
		Collections.swap(this.fields, i, j);
		
		log.debug("List after swap: ");
		log.debug(fields.toString());
		
		Collections.sort(this.fields);

		fireContentsChanged(this, i, j);

	}

}
