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
package edu.cornell.dendro.corina.io.command;



import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ImportMergeEntitiesEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;

public class MergeEntitiesCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportMergeEntitiesEvent event = (ImportMergeEntitiesEvent) argEvent;
		System.out.println("Merge event called");

		TridasProject root = (TridasProject) ((DefaultMutableTreeNode)event.model.getTreeModel().getRoot()).getUserObject();
		
		ITridas entityType = null;
		try {
			entityType = event.getValue().newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Merging "+entityType.getClass());
		

		if(entityType instanceof TridasObject)
		{
			System.out.println("Merging objects in tree");
			if (!root.isSetObjects()) return;
			if (!(root.getObjects().size()>1)) return;
		
			ArrayList<TridasObject> objlist = (ArrayList<TridasObject>) root.getObjects();
			for(int i = 1; i<objlist.size(); i++)
			{
				System.out.println("Merging object no. "+i);
				TridasObject obj = objlist.get(i);
				if(!obj.isSetElements()) continue;
				objlist.get(0).getElements().addAll(obj.getElements());
			}
			
			ArrayList<TridasObject> objlistnew = new ArrayList<TridasObject>();
			objlistnew.add(objlist.get(0));
			root.setObjects(objlistnew);
			System.out.println("Set root of tree to new root");
			TridasRepresentationTreeModel tm = new TridasRepresentationTreeModel(root);
			event.model.setTreeModel(tm);
			
			
		}

		
	}

}
