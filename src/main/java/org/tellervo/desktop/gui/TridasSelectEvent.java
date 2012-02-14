/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui;

import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

public class TridasSelectEvent extends AWTEvent {

	public static final int ENTITY_SELECTED		= 1001;

	private static final long serialVersionUID = 1026781147608262652L;
	@SuppressWarnings("unchecked")
	ArrayList entityList;
	Boolean multiSelector = false;
	DefaultMutableTreeNode node;
	TridasSelectType type = TridasSelectType.NORMAL;
	
	
	public enum TridasSelectType{
		NORMAL,
		FORCED;
	}
	
	/**
	 * Standard constructor which is used to show that the specified
	 * entity was selected.
	 * 
	 * @param source
	 * @param id
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, TridasObjectEx entity) {
		super(source, id);
		entityList = new ArrayList<TridasObject>();
		entityList.add(entity);

	}
	
	/**
	 * Standard constructor which is used to show that the specified
	 * entity was selected.
	 * 
	 * @param source
	 * @param id
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, ITridas entity, DefaultMutableTreeNode node) {
		super(source, id);
		entityList = new ArrayList<ITridas>();
		entityList.add(entity);
		this.node = node;

	}
	
	/**
	 * Standard constructor which is used to show that the specified
	 * entity was selected.
	 * 
	 * @param source
	 * @param id
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, ITridas entity) {
		super(source, id);
		entityList = new ArrayList<ITridas>();
		entityList.add(entity);

	}
	
	/**
	 * Standard constructor which is used to show that the specified
	 * entity was selected.
	 * 
	 * @param source
	 * @param id
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, ITridas entity, TridasSelectType typ) {
		super(source, id);
		entityList = new ArrayList<ITridas>();
		entityList.add(entity);
		setSelectType(typ);

	}
	
	/**
	 * Constructor used when multiple entities were selected
	 * 
	 * @param source
	 * @param id
	 * @param entityList
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, List<TridasObjectEx> entityList) {
		super(source, id);
		this.entityList = new ArrayList<TridasObjectEx>();
		this.entityList.addAll(entityList);
		multiSelector = true;
	}
	
	/**
	 * Constructor used when multiple entities were selected
	 * 
	 * @param source
	 * @param id
	 * @param entityList
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, List<TridasObject> entityList, TridasSelectType typ) {
		super(source, id);
		this.entityList = new ArrayList<TridasObject>();
		this.entityList.addAll(entityList);
		setSelectType(typ);
		multiSelector = true;
	}
	
	
	
	/**
	 * Constructor for a null entity event.  This is used when you want
	 * to specify that no entity was selected.
	 * 
	 * @param source
	 * @param id
	 */
	public TridasSelectEvent(Object source, int id) {
		super(source, id);
		entityList = null;
	}
	
	/**
	 * Constructor for a null entity event.  This is used when you want
	 * to specify that no entity was selected.
	 * 
	 * @param source
	 * @param id
	 */
	public TridasSelectEvent(Object source, int id, TridasSelectType typ) {
		super(source, id);
		setSelectType(typ);
		entityList = null;
	}
	
	/**
	 * Get the  entity that was selected.  If no entity was selected this
	 * returns null. 
	 * 
	 * @return
	 */	
	public ITridas getEntity() throws Exception 
	{
		if(multiSelector)
		{
			 throw new Exception("This event has multiple selections use getEntityList() instead");		 
		}
		
		if(entityList==null)
		{
			return null;
		}
		
		if(entityList.get(0) instanceof TridasObjectEx)
		{
			TridasObjectEx obj = (TridasObjectEx) entityList.get(0);
			if(obj.isSetElements())
			{
				TridasElement el = obj.getElements().get(0);
				if(el.isSetSamples())
				{
					TridasSample samp = el.getSamples().get(0);
					if(samp.isSetRadiuses())
					{
						TridasRadius rad = samp.getRadiuses().get(0);
						if(rad.isSetMeasurementSeries())
						{
							return rad.getMeasurementSeries().get(0);
						}
						else 
						{
							return rad;
						}
					}
					else
					{
						return samp;
					}
				}
				else
				{
					return el;
				}
			}
			else
			{
				return obj;
			}
		}
		else
		{
			return (ITridas) entityList.get(0);
		}
	}
	
	public DefaultMutableTreeNode getTreeNode()
	{
		return node;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ITridas> getEntityList()
	{
		return entityList;
	}
	
	/**
	 * Does this event represent the selection of multiple entities?
	 * 
	 * @return
	 */
	public Boolean isMultiSelection()
	{
		return this.multiSelector;
	}
	
	public void setSelectType(TridasSelectType typ){
		type = typ;
	}
	
	public TridasSelectType getSelectType()
	{
		return type;
	}
}
