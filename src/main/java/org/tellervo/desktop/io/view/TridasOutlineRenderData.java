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
package org.tellervo.desktop.io.view;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.netbeans.swing.outline.RenderDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


/**
 * RenderData for TRiDaS tree/table (outline) representation
 * 
 * @author peterbrewer
 */
public class TridasOutlineRenderData implements RenderDataProvider {
 
	private final static Logger log = LoggerFactory.getLogger(TridasOutlineRenderData.class);

	Icon databaseIcon;
	Icon objectIcon;
	Icon elementIcon;
	Icon sampleIcon;
	Icon radiusIcon;
	Icon mseriesIcon;
	Icon dseriesIcon;

	
	public TridasOutlineRenderData()
	{
		databaseIcon = ((ImageIcon) Builder.getIcon("database.png", Builder.ICONS, 16));
		objectIcon   = ((ImageIcon) Builder.getIcon("object.png",   Builder.ICONS, 16));
		elementIcon  = ((ImageIcon) Builder.getIcon("element.png",  Builder.ICONS, 16));
		sampleIcon   = ((ImageIcon) Builder.getIcon("sample.png",   Builder.ICONS, 16));
		radiusIcon   = ((ImageIcon) Builder.getIcon("radius.png",   Builder.ICONS, 16));
		mseriesIcon  = ((ImageIcon) Builder.getIcon("measurementseries.png",   Builder.ICONS, 16));
		dseriesIcon  = ((ImageIcon) Builder.getIcon("derivedseries.png",   Builder.ICONS, 16));
	}
	
    @Override
    public java.awt.Color getBackground(Object o) {
        return null;
    }

    @Override
    public String getDisplayName(Object o) {
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) o;
        ITridas entity = null;
        try{
           entity = (ITridas) dmtn.getUserObject();
        } catch (Exception e)
        {
        	log.error("Error getting display name in TridasOutlineRenderData - getUserObject is null");
        	return "";
        }
        
		if(entity instanceof TridasObject)
		{
			TridasObject obj = ((TridasObject)entity);
			for(TridasGenericField gf : obj.getGenericFields())
			{
				if (gf.getName().equals("corina.objectLabCode"))
				{
					return gf.getValue() + " - "+obj.getTitle();
				}
			}
			return obj.getTitle();
		}
		else if(entity instanceof ITridas)
		{
		    return ((ITridas)entity).getTitle();
		}
		else 
		{
			return entity.toString();
		}
        
    }

    @Override
    public java.awt.Color getForeground(Object o) {
        return null;
    }

    @Override
    public javax.swing.Icon getIcon(Object o) {
        
    	ITridas entity = (ITridas) ((DefaultMutableTreeNode)o).getUserObject();
    	
        if(entity instanceof TridasProject)
        {
        	return databaseIcon;
        }
        else if(entity instanceof TridasObject)
        {
        	return objectIcon;
        }
        else if(entity instanceof TridasElement)
        {
        	return elementIcon;
        }
        else if(entity instanceof TridasSample)
        {
        	return sampleIcon;
        }
        else if(entity instanceof TridasRadius)
        {
        	return radiusIcon;
        }
        else if(entity instanceof TridasMeasurementSeries)
        {
        	return mseriesIcon;
        }
        else if(entity instanceof TridasDerivedSeries)
        {
        	return dseriesIcon;
        }
  	
    	
    	return null;

    }

    @Override
    public String getTooltipText(Object o) {
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) o;
    	return ((ITridas)dmtn.getUserObject()).getTitle();
    }

    @Override
    public boolean isHtmlDisplayName(Object o) {
        return false;
    }

}
