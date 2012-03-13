package org.tellervo.desktop.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class LoginFocusTraversalPolicy extends FocusTraversalPolicy {

	Component defaultComponent;
	ArrayList<Component> order;
	
	public LoginFocusTraversalPolicy(ArrayList<Component> order)
	{
		this.order=order;
	}
	
	public void setDefaultComponent(Component comp)
	{
		defaultComponent = comp;
	}
	
	
	@Override
	public Component getComponentAfter(Container arg0, Component comp) {
		
		if(!order.contains(comp)) return null;
		
		Iterator<Component> iterator = order.iterator();
		
		while(iterator.hasNext())
		{
			Component thiscomp = iterator.next();
			
			if(thiscomp.equals(comp))
			{
				if(iterator.hasNext())
				{
					return iterator.next();
				}
				else
				{
					return this.getFirstComponent(arg0);
				}
			}
		}

		return null;
	}

	@Override
	public Component getComponentBefore(Container arg0, Component comp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getDefaultComponent(Container arg0) {
		return defaultComponent;
	}

	@Override
	public Component getFirstComponent(Container arg0) {

		return defaultComponent;
	}

	@Override
	public Component getLastComponent(Container arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}