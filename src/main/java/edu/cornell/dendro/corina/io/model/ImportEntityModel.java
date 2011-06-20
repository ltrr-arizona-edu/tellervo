package edu.cornell.dendro.corina.io.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.model.HashModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.tridasv2.ui.support.TridasEntityListHolder;

public class ImportEntityModel extends HashModel {

	private static final long serialVersionUID = 1L;

	public static final String CURRENT_ENTITY = "currentEntity";
	public static final String PARENT_ENTITY = "parentEntity";
	public static final String CLASS = "entityClass";
	public static final String ENTITY_LIST = "entityList";


	public ImportEntityModel(ITridas entity, ITridas parent)
	{
		registerProperty(ImportEntityModel.CURRENT_ENTITY, PropertyType.READ_WRITE);
		registerProperty(ImportEntityModel.PARENT_ENTITY, PropertyType.READ_WRITE);
		registerProperty(ImportEntityModel.CLASS, PropertyType.READ_WRITE);
		registerProperty(ImportEntityModel.ENTITY_LIST, PropertyType.READ_WRITE, new ArrayList<ITridas>());
		
		setCurrentEntity(entity);
		setParentEntity(parent);
	}
	
	public ITridas getCurrentEntity()
	{
		return (ITridas) getProperty(ImportEntityModel.CURRENT_ENTITY);
	}
	
	public ITridas getParentEntity()
	{
		return (ITridas) getProperty(ImportEntityModel.PARENT_ENTITY);
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends ITridas> getCurrentEntityClass()
	{
		return (Class<? extends ITridas>) getProperty(ImportEntityModel.CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ITridas> getEntityList()
	{
		return (ArrayList<ITridas>) getProperty(ImportEntityModel.ENTITY_LIST);
	}
	
	
	public void setCurrentEntity(ITridas entity)
	{
		setProperty(ImportEntityModel.CURRENT_ENTITY, entity);
		setProperty(ImportEntityModel.CLASS, entity.getClass());
	}
	
	public void setParentEntity(ITridas entity)
	{
		setProperty(ImportEntityModel.PARENT_ENTITY, entity);
		populateEntityList();
	}
	
	public void setEntityList(ArrayList<? extends ITridas> list)
	{
		setProperty(ImportEntityModel.ENTITY_LIST, list);
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
		
		if(currentEntity==null)
		{
			System.out.println("No entity");
			return false;

		}
		
		if (!currentEntity.isSetIdentifier())
		{
			return true;
		}
		else if (!currentEntity.getIdentifier().isSetDomain())
		{
			System.out.println("No domain");
			return true;
		}
		else if (!currentEntity.getIdentifier().getDomain().equals(App.domain))
		{
			System.out.println("Different domain - this one is: "+currentEntity.getIdentifier().getDomain());
			return true;
		}
		return false;
	}
	
}
