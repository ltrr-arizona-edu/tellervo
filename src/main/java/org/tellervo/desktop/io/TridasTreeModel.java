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
package org.tellervo.desktop.io;

import java.util.ArrayList;

import javax.swing.tree.TreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


public class TridasTreeModel implements TreeModel {
	  private final static Logger log = LoggerFactory.getLogger(TridasTreeModel.class);

	  private ITridas root;

	    public TridasTreeModel(ITridas root) {
	        this.root = root;
	        
	    }

	    @Override
	    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
	        //do nothing
	    }

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
	        log.error("getChild failing on index " + index +" of parent "+parent);
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
	    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
	        //do nothing
	    }

	    @Override
	    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
	        //do nothing
	    }

}
