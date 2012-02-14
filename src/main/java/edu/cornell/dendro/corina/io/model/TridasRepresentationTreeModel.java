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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.RowModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.editor.EditorEditMenu;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow.ImportStatus;

public class TridasRepresentationTreeModel extends DefaultTreeModel implements TreeModel, RowModel{

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(TridasRepresentationTreeModel.class);

	//private DefaultMutableTreeNode root;
	
	public TridasRepresentationTreeModel(TridasProject project) {
		super(new DefaultMutableTreeNode(project));
		
		if(project!=null)
		{
			if(project.isSetObjects())
			{
				for(TridasObject obj : project.getObjects())
				{
					addEntityToTree(root, obj);
				}
			}
		}
		
	}
	
	/**
	 * Recursively add entities to parent 
	 * 
	 * @param parent
	 * @param entity
	 */
	private void addEntityToTree(TreeNode parent, ITridas entity)
	{
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(entity);
		((DefaultMutableTreeNode) parent).add(newNode);
		
		if(entity instanceof TridasObject)
		{
			if(((TridasObject)entity).isSetElements())
			{
				for(TridasElement element : ((TridasObject) entity).getElements())
				{
					addEntityToTree(newNode, element);
				}
			}
		}
		else if(entity instanceof TridasElement)
		{
			if(((TridasElement)entity).isSetSamples())
			{
				for(TridasSample sample : ((TridasElement) entity).getSamples())
				{
					addEntityToTree(newNode, sample);
				}
			}
		}
		else if(entity instanceof TridasSample)
		{
			if(((TridasSample)entity).isSetRadiuses())
			{
				for(TridasRadius radius : ((TridasSample) entity).getRadiuses())
				{
					addEntityToTree(newNode, radius);
				}
			}
		}
		else if(entity instanceof TridasRadius)
		{
			if(((TridasRadius)entity).isSetMeasurementSeries())
			{
				for(TridasMeasurementSeries ms : ((TridasRadius) entity).getMeasurementSeries())
				{
					addEntityToTree(newNode, ms);
				}
			}
		}
	}

	@Override
	public boolean isLeaf(Object o)
	{
		if(o==null) return true;
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
		ITridas entity = (ITridas) node.getUserObject();
		
		if(!(entity instanceof ITridas))         return true;
		/*else if(entity instanceof TridasProject) return !((TridasProject) entity).isSetObjects();
		else if(entity instanceof TridasObject)  return !((TridasObject) entity).isSetElements();
		else if(entity instanceof TridasElement) return !((TridasElement) entity).isSetSamples();
		else if(entity instanceof TridasSample)  return !((TridasSample) entity).isSetRadiuses();
		else if(entity instanceof TridasRadius)  return !((TridasRadius) entity).isSetMeasurementSeries();
		else if(entity instanceof ITridasSeries) return true;		
		return false;*/
		
		if(entity instanceof ITridasSeries) return true;
		return false;
	}
	

	@SuppressWarnings("rawtypes")
	@Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImportStatus.class;
            default:
                assert false;
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

	@Override
	public String getColumnName(int arg0) {
		switch (arg0)
		{
		case 0: return "Status";
		default:
			throw new RuntimeException("Index out of range for getColumnName()");
		}
	}

    @Override
    public Object getValueFor(Object node, int column) {
    	DefaultMutableTreeNode dmtnode = (DefaultMutableTreeNode) node; 
        ITridas entity = (ITridas) dmtnode.getUserObject();
                
        
        switch (column) {
            case 0:
            	if(entity instanceof TridasProject)
            	{
            		return ImportStatus.UNSUPPORTED;
            	}
            	else if (entity instanceof ITridas)
            	{
            		   		
            		TridasIdentifier id = entity.getIdentifier();
            		if(id==null) return ImportStatus.PENDING;
            		if(!id.isSetDomain()) return ImportStatus.PENDING;
            		if(!id.isSetValue()) return ImportStatus.PENDING;
            		if(!id.getDomain().equals(App.domain)) return ImportStatus.PENDING;            
            		
            		return ImportStatus.STORED_IN_DATABASE;
            	}
                return ImportStatus.UNSUPPORTED;
            default:
                assert false;
        }
        return null;
    }

	@Override
	public boolean isCellEditable(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValueFor(Object arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
