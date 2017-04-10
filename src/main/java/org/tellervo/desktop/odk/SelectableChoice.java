package org.tellervo.desktop.odk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;

import com.jidesoft.swing.Selectable;

public class SelectableChoice implements Selectable, Serializable {

	private static final long serialVersionUID = 1L;
	boolean selected = false;
	boolean enabled = true;
	Object item;
	String synonym = null;

	public SelectableChoice(Object item)
	{
		this.item = item;
	}
	
	public void setSynonym(String str)
	{
		this.synonym = str;
	}
	
	public String getSynonym()
	{
		return synonym;
	}
	
	public String toStringIgnoreSynonym()
	{
		String real = "";

		if(item instanceof ControlledVoc)
		{
			real = ((ControlledVoc)item).getNormal();
		}
		else if (item instanceof NormalTridasShape)
		{
			real =  ((NormalTridasShape)item).value();
		}
		else if (item instanceof NormalTridasLocationType)
		{
			real =  ((NormalTridasLocationType)item).value();
		}
		else
		{
			real = item.toString();
		}
		
		return real;
	}
	
	public String toString()
	{
		String real = toStringIgnoreSynonym();
		
		
		if(this.synonym!=null)
		{
			return this.synonym + " ("+real+")";
		}
		else
		{
			return real;
		}
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
