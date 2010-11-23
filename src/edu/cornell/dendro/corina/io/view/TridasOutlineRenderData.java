package edu.cornell.dendro.corina.io.view;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.netbeans.swing.outline.RenderDataProvider;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * RenderData for TRiDaS tree/table (outline) representation
 * 
 * @author peterbrewer
 */
public class TridasOutlineRenderData implements RenderDataProvider {

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
        return ((ITridas) o).getTitle();
    }

    @Override
    public java.awt.Color getForeground(Object o) {
        return null;
    }

    @Override
    public javax.swing.Icon getIcon(Object o) {
        
        if(o instanceof TridasProject)
        {
        	return databaseIcon;
        }
        else if(o instanceof TridasObject)
        {
        	return objectIcon;
        }
        else if(o instanceof TridasElement)
        {
        	return elementIcon;
        }
        else if(o instanceof TridasSample)
        {
        	return sampleIcon;
        }
        else if(o instanceof TridasRadius)
        {
        	return radiusIcon;
        }
        else if(o instanceof TridasMeasurementSeries)
        {
        	return mseriesIcon;
        }
        else if(o instanceof TridasDerivedSeries)
        {
        	return dseriesIcon;
        }
  	
    	
    	return null;

    }

    @Override
    public String getTooltipText(Object o) {
    	return ((ITridas) o).getTitle();
    }

    @Override
    public boolean isHtmlDisplayName(Object o) {
        return false;
    }

}