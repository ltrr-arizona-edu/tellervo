package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.AWTEvent;

import org.tridas.interfaces.ITridas;

public class TridasSelectEvent extends AWTEvent {


	private static final long serialVersionUID = 1026781147608262652L;
	ITridas entity;
	
	
	public TridasSelectEvent(Object source, int id) {
		super(source, id);
		// TODO Auto-generated constructor stub
	}
	
	public TridasSelectEvent(Object source, int id, ITridas entity) {
		super(source, id);
		this.entity = entity;
	}
	
	public ITridas getEntity()
	{
		return entity;
	}

}
