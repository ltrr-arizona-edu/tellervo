package org.tellervo.desktop.odk;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.AbstractListModel;

import org.tellervo.desktop.odk.fields.AbstractODKField;

public class ODKFieldListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	ArrayList<AbstractODKField> fields;
	
	public ODKFieldListModel()
	{
		fields = new ArrayList<AbstractODKField>();
	}
	
	public ODKFieldListModel(ArrayList<AbstractODKField> fields)
	{
		this.fields = fields;
	}
	
	public void addField(AbstractODKField field)
	{
		fields.add(field);
		this.fireIntervalAdded(this, 0, fields.size()-1);
	}
	
	public void addAllFields(Collection<AbstractODKField> fields)
	{
		this.fields.addAll(fields);
		this.fireIntervalAdded(this, 0, fields.size()-1);

	}
	
	public void removeField(AbstractODKField field)
	{
		this.fields.remove(field);
		if(fields.size()==0)
		{
			this.fireIntervalRemoved(this, 0, 0);
		}
		else
		{
			this.fireIntervalRemoved(this, 0, fields.size()-1);
		}	}
	
	public void removeField(int index)
	{
		this.fields.remove(index);
		if(fields.size()==0)
		{
			this.fireIntervalRemoved(this, 0, 0);
		}
		else
		{
			this.fireIntervalRemoved(this, 0, fields.size()-1);
		}
	}
	
	public void removeFields(Collection<AbstractODKField> fields)
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

	}
	
	@Override
	public AbstractODKField getElementAt(int arg0) {
		return fields.get(arg0);
	}

	@Override
	public int getSize() {
		return fields.size();
	}
	
	public ArrayList<AbstractODKField> getAllFields()
	{
		return fields;
	}

}
