package org.tellervo.desktop.odk;

import java.util.ArrayList;
import java.util.Collection;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;

import com.jidesoft.swing.Selectable;

public class SelectableChoice implements Selectable {

	boolean selected = false;
	boolean enabled = true;
	Object item;
	
	public SelectableChoice(Object item)
	{
		this.item = item;
	}
	
	public String toString()
	{
		if(item instanceof ControlledVoc)
		{
			return ((ControlledVoc)item).getNormal();
		}
		else if (item instanceof NormalTridasShape)
		{
			return ((NormalTridasShape)item).value();
		}
		else if (item instanceof NormalTridasLocationType)
		{
			return ((NormalTridasLocationType)item).value();
		}
		
		return item.toString();
	}
	
	@Override
	public void invertSelected() {
		selected = !selected;

	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setEnabled(boolean b) {
		enabled = b;

	}

	@Override
	public void setSelected(boolean b) {
		selected = b;

	}
	

	/**
	 * Convert an array list of objects into an array list of selectable choices
	 *  
	 * @param objects
	 * @return
	 */
	public static ArrayList<SelectableChoice> makeObjectsSelectable(Collection<Object> objects)
	{
		ArrayList<SelectableChoice> choices = new ArrayList<SelectableChoice>();
		
		for(Object o : objects)
		{
			SelectableChoice choice = new SelectableChoice(o);
			choice.setSelected(true);
			choices.add(choice);
		}
		
		return choices;	
	}
	
	public Object getItem()
	{
		return item;
	}
}
