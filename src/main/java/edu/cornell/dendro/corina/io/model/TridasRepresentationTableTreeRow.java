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
package edu.cornell.dendro.corina.io.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.model.HashModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityListHolder;

public class TridasRepresentationTableTreeRow extends HashModel{


	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasRepresentationTableTreeRow.class);

	public static final String NODE = "treeNode";
	public static final String IMPORT_STATUS_ACTION = "importStatusAction";
	public static final String CURRENT_ENTITY = "currentEntity";
	public static final String PARENT_ENTITY = "parentEntity";
	public static final String CLASS = "entityClass";
	public static final String ENTITY_LIST = "entityList";
	
	
	public enum ImportStatus{
		IGNORE("Ignored"),
		STORED_IN_DATABASE("Stored in database"),
		PENDING("Attention required"),
		UNSUPPORTED("Unsupported - ignored"),
		UNKNOWN("Unknown");
		
		String name;
		ImportStatus(String name)
		{
			this.name = name;
		}
		
		public String toString()
		{
			return this.name;
		}
	}
	
	public TridasRepresentationTableTreeRow(DefaultMutableTreeNode node, 
			ImportStatus action)
	{
		registerProperty(TridasRepresentationTableTreeRow.NODE, PropertyType.READ_WRITE);
		registerProperty(TridasRepresentationTableTreeRow.IMPORT_STATUS_ACTION, PropertyType.READ_WRITE);
		registerProperty(TridasRepresentationTableTreeRow.CURRENT_ENTITY, PropertyType.READ_WRITE);
		registerProperty(TridasRepresentationTableTreeRow.PARENT_ENTITY, PropertyType.READ_WRITE);
		registerProperty(TridasRepresentationTableTreeRow.CLASS, PropertyType.READ_WRITE);
		registerProperty(TridasRepresentationTableTreeRow.ENTITY_LIST, PropertyType.READ_WRITE, new ArrayList<ITridas>());
	
		
		if(node==null )
		{
			log.warn("TridasRepresentationTableTreeRow node is null");
		}
		
		setNode(node);
		setImportStatus(action);


	}

	public ITridas getCurrentEntity()
	{
		return (ITridas) getProperty(TridasRepresentationTableTreeRow.CURRENT_ENTITY);
	}
	
	public ITridas getParentEntity()
	{
		return (ITridas) getProperty(TridasRepresentationTableTreeRow.PARENT_ENTITY);
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends ITridas> getCurrentEntityClass()
	{
		return (Class<? extends ITridas>) getProperty(TridasRepresentationTableTreeRow.CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ITridas> getEntityList()
	{
		return (ArrayList<ITridas>) getProperty(TridasRepresentationTableTreeRow.ENTITY_LIST);
	}
	
	
	public void setCurrentEntity(ITridas entity)
	{
		if(entity==null) log.warn("Setting current entity to null");
		
		setProperty(TridasRepresentationTableTreeRow.CURRENT_ENTITY, entity);
		setProperty(TridasRepresentationTableTreeRow.CLASS, entity.getClass());
	}
	
	private void setParentEntity(ITridas entity)
	{
		
		setProperty(TridasRepresentationTableTreeRow.PARENT_ENTITY, entity);
		populateEntityList();
	}
	
	public void setEntityList(ArrayList<? extends ITridas> list)
	{
		setProperty(TridasRepresentationTableTreeRow.ENTITY_LIST, list);
	}
	
	private void populateEntityList()
	{
		List<? extends ITridas> entities = null;
		TridasEntityListHolder lists = new TridasEntityListHolder();
		
		ITridas currentEntity = getCurrentEntity();
		ITridas parentEntity = getParentEntity();
		
		if (currentEntity instanceof TridasProject)
		{
			// This entity is a project so set list to null as 
			// projects aren't supported
		}
		else if(currentEntity instanceof TridasObject)
		{
			// This entity is an object so grab object dictionary
			entities = App.tridasObjects.getObjectList();
		}
		
		// Otherwise, check that the parent is already in db by checking
		// the identifier domain and set list accordingly 
		try{
			if (parentEntity.getIdentifier().getDomain().equals(App.domain))
			{
				entities = lists.getChildList(parentEntity, true);
			}
		} catch (Exception e)
		{	}	
		
		setEntityList(sortList(entities));
	}
	
	/**
	 * Sort a list of ITridas objects
	 * 
	 * @param list
	 */
	private ArrayList<? extends ITridas> sortList(List<? extends ITridas> list) {
		// Sort list intelligently
		ArrayList<ITridas> entities = new ArrayList<ITridas>();
		
		if(list==null || list.isEmpty()) return null;
		
		for(int i=0; i<list.size(); i++)
		{
			ITridas e = list.get(i);	
			entities.add(e);
		}
		
		
		
		if(!(entities.get(0) instanceof TridasObject))
		{
			TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
					TridasComparator.NullBehavior.NULLS_LAST, 
					TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
			
			Collections.sort(entities, numSorter);
		}

		return entities;
	}
	
	public boolean isCurrentEntityNew()
	{
		ITridas currentEntity = getCurrentEntity();
		log.debug("Checking is entity is new or now");
		
		if(currentEntity==null)
		{
			log.debug("No entity");
			return false;

		}
		
		if (!currentEntity.isSetIdentifier())
		{
			return true;
		}
		else if (!currentEntity.getIdentifier().isSetDomain())
		{
			log.debug("No domain");
			return true;
		}
		else if (!currentEntity.getIdentifier().getDomain().equals(App.domain))
		{
			log.debug("Different domain - this one is: "+currentEntity.getIdentifier().getDomain()+" and not "+ App.domain);
			return true;
		}
		return false;
	}
	
	
	
	public void setNode(DefaultMutableTreeNode node)
	{
		setProperty(TridasRepresentationTableTreeRow.NODE, node);
		
		ITridas entity = (ITridas) getDefaultMutableTreeNode().getUserObject();
		ITridas parent = null;
		try{
			parent = (ITridas) ((DefaultMutableTreeNode)getDefaultMutableTreeNode().getParent()).getUserObject();
		} catch (Exception e){ }
		
		setCurrentEntity(entity);
		setParentEntity(parent);
		
	}
	
	
	public void setImportStatus(ImportStatus action)
	{
		setProperty(TridasRepresentationTableTreeRow.IMPORT_STATUS_ACTION, action);
	}
	
	
	public DefaultMutableTreeNode getDefaultMutableTreeNode()
	{
		return (DefaultMutableTreeNode) getProperty(TridasRepresentationTableTreeRow.NODE);
	}
	
	public ImportStatus getImportStatus()
	{
		return (ImportStatus) getProperty(TridasRepresentationTableTreeRow.IMPORT_STATUS_ACTION);
	}
	
	
}
