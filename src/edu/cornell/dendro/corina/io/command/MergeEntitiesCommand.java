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
