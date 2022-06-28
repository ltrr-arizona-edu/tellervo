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
package org.tellervo.desktop.io.command;



import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.control.ImportMergeEntitiesEvent;
import org.tellervo.desktop.io.model.TridasRepresentationTreeModel;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class MergeEntitiesCommand implements ICommand {
	  private final static Logger log = LoggerFactory.getLogger(MergeEntitiesCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		ImportMergeEntitiesEvent event = (ImportMergeEntitiesEvent) argEvent;
		log.debug("Merge event called");

		TridasProject root = (TridasProject) ((DefaultMutableTreeNode)event.model.getTreeModel().getRoot()).getUserObject();
		
		ITridas entityType = null;
		try {
			entityType = event.getValue().newInstance();
		} catch (Exception e1) {
			log.error("Unable to determine entity type in merge entities command");
			e1.printStackTrace();
		} 
		
		log.debug("Merging "+entityType.getClass());
		

		if( (entityType instanceof TridasObject) ||
			(entityType instanceof TridasElement) ||
			(entityType instanceof TridasSample) ||
			(entityType instanceof TridasRadius)
		  )
		{
			log.debug("Merging objects in TRiDaS hierarchy");
			if (!root.isSetObjects()) return;
			if (!(root.getObjects().size()>1)) return;
		
			ArrayList<TridasObject> objlist = (ArrayList<TridasObject>) root.getObjects();
			for(int i = 1; i<objlist.size(); i++)
			{
				log.debug("Merging object no. "+i);
				TridasObject obj = objlist.get(i);
				if(!obj.isSetElements()) continue;
				objlist.get(0).getElements().addAll(obj.getElements());
			}
			
			ArrayList<TridasObject> objlistnew = new ArrayList<TridasObject>();
			objlistnew.add(objlist.get(0));
			root.setObjects(objlistnew);
			log.debug("Set root of tree to new root");
			TridasRepresentationTreeModel tm = new TridasRepresentationTreeModel(root);
			event.model.setTreeModel(tm);
			
			
		}
		
		// TODO work out how to manipulate the hierarchy
		/**
		if( (entityType instanceof TridasElement) ||
			(entityType instanceof TridasSample) ||
			(entityType instanceof TridasRadius)
		  )
		{
			log.debug("Merging elements in TRiDaS hierarchy");
			if (!root.isSetObjects()) return;
			
			// Should only be 1 object now
			TridasObject obj = (TridasObject) root.getObjects().get(0);
			if(!obj.isSetElements()) return;
			ArrayList<TridasElement> list = (ArrayList<TridasElement>) obj.getElements();
				
			for(int i = 1; i<list.size(); i++)
			{
				log.debug("Merging element no. "+i);
				TridasElement el = list.get(i);
				if(el.isSetSamples()) continue;
				list.get(0).getSamples().addAll(el.getSamples());
			}
		}
		*/
		
	}

}
