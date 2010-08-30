package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.manip.ReconcileWindow;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;

public class TridasAnnotationController extends AbstractTridasAnnotationController {


    private ITridas entity;

    public TridasAnnotationController(WorldWindow worldWindow, TridasAnnotation annotation, ITridas entity)
    {
        super(worldWindow, annotation);

        this.setEntity(entity);
    }

    public TridasAnnotationController(WorldWindow worldWindow, TridasAnnotation annotation)
    {
        this(worldWindow, annotation, null);
    }

    public ITridas getEntity()
    {
    	return entity;
    }

    public void setEntity(ITridas entity)
    {
        this.entity = entity;
    }

    //**************************************************************//
    //********************  Action Listener  ***********************//
    //**************************************************************//

    @SuppressWarnings({"StringEquality"})
    public void onActionPerformed(ActionEvent e)
    {
        

        if (e.getActionCommand() == "searchForSeries")
        {
        	TridasObjectEx obj = null;
        	if(entity instanceof TridasObjectEx)
        	{
        		obj = (TridasObjectEx) entity;
        	}
        	else
        	{
        		Alert.message("Error", "Error opening database browser");
        	}
        	
			DBBrowser browser = new DBBrowser(new Frame(), true);

			// select the site we're in
			browser.selectSiteByCode(obj.getLabCode());
						
			browser.setVisible(true);
			

        }

        //super.onActionPerformed(e);
        
    }







}
