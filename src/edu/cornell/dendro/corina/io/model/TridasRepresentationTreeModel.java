package edu.cornell.dendro.corina.io.model;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.RowModel;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow.ImportStatus;

public class TridasRepresentationTreeModel implements TreeModel, RowModel{

	private static final long serialVersionUID = 1L;
	private final ITridas root;
	
	public TridasRepresentationTreeModel(ITridas root) {
		this.root = root;
	}

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
        ITridas f = (ITridas) node;
        switch (column) {
            case 0:
            	if(f instanceof TridasProject)
            	{
            		return ImportStatus.UNSUPPORTED;
            	}
            	else if (f instanceof TridasObject)
            	{
            		return ImportStatus.STORED_IN_DATABASE;
            	}
                return ImportStatus.PENDING;
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
	public void setValueFor(Object arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

    @SuppressWarnings("unchecked")
	@Override
    public Object getChild(Object parent, int index) {
        if(parent instanceof TridasProject)
        {
        	ArrayList<ITridas> childlist = new ArrayList<ITridas>();
        	childlist.addAll((ArrayList<? extends ITridas>) ((TridasProject)parent).getObjects());
        	childlist.addAll((ArrayList<? extends ITridas>) ((TridasProject)parent).getDerivedSeries());
        	return ((TridasProject)parent).getObjects().get(index);
        }
        else if(parent instanceof TridasObject)
        {
        	return ((TridasObject)parent).getElements().get(index);
        }
        else if(parent instanceof TridasElement)
        {
        	return ((TridasElement)parent).getSamples().get(index);
        }
        else if(parent instanceof TridasSample)
        {
        	return ((TridasSample)parent).getRadiuses().get(index);
        }
        else if(parent instanceof TridasRadius)
        {
        	return ((TridasRadius)parent).getMeasurementSeries().get(index);
        }
        System.out.println("getChild failing on index " + index +" of parent "+parent);
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof TridasProject)
        {
        	return ((TridasProject)parent).getObjects().size();
        }
        else if(parent instanceof TridasObject)
        {
        	return ((TridasObject)parent).getElements().size();
        }
        else if(parent instanceof TridasElement)
        {
        	return ((TridasElement)parent).getSamples().size();
        }
        else if(parent instanceof TridasSample)
        {
        	return ((TridasSample)parent).getRadiuses().size();
        }
        else if(parent instanceof TridasRadius)
        {
        	return ((TridasRadius)parent).getMeasurementSeries().size();
        }
        return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
    	
    	if(parent==null || child==null) return -1;
    	
    	ArrayList<? extends ITridas> childlist = null;
        if(parent instanceof TridasProject)
        {
        	childlist = (ArrayList<? extends ITridas>) ((TridasProject)parent).getObjects();
        }
        else if(parent instanceof TridasObject || (parent instanceof TridasObjectEx))
        {
        	childlist = (ArrayList<? extends ITridas>) ((TridasObject)parent).getElements();
        }
        else if(parent instanceof TridasElement)
        {
        	childlist = (ArrayList<? extends ITridas>) ((TridasElement)parent).getSamples();
        }
        else if(parent instanceof TridasSample)
        {
        	childlist = (ArrayList<? extends ITridas>) ((TridasSample)parent).getRadiuses();
        }
        else if(parent instanceof TridasRadius)
        {
        	childlist = (ArrayList<? extends ITridas>) ((TridasRadius)parent).getMeasurementSeries();
        }
        
        if(childlist==null || childlist.size()==0) return -1;
    	
        for(int i=0; i<childlist.size(); i++)
        {
        	if(childlist.get(i).equals(child)) return i;
        }
        
    	return -1;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        if(getChildCount(node)>0)
        {
        	return false;
        }
        else
        {
        	return true;
        }
    }

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
