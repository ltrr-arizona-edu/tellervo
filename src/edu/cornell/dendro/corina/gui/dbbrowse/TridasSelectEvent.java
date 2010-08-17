package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.AWTEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mozilla.dom.UnsupportedException;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.io.InvalidDataException;

public class TridasSelectEvent extends AWTEvent {


	private static final long serialVersionUID = 1026781147608262652L;
	@SuppressWarnings("unchecked")
	ArrayList entityList;
	Boolean multiSelector = false;
	
	/**
	 * Standard constructor which is used to show that the specified
	 * entity was selected.
	 * 
	 * @param source
	 * @param id
	 * @param entity
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, TridasObject entity) {
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
	public TridasSelectEvent(Object source, int id, ITridas entity) {
		super(source, id);
		entityList = new ArrayList<ITridas>();
		entityList.add(entity);

	}
	
	/**
	 * Constructor used when multiple entities were selected
	 * 
	 * @param source
	 * @param id
	 * @param entityList
	 */
	@SuppressWarnings("unchecked")
	public TridasSelectEvent(Object source, int id, List<TridasObject> entityList) {
		super(source, id);
		this.entityList = new ArrayList<TridasObject>();
		this.entityList.addAll(entityList);
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
		else
		{
			return (ITridas) entityList.get(0);
		}
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
}
