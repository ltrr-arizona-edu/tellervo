package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasObject;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.gui.dbbrowse.MetadataBrowser;
import edu.cornell.dendro.corina.manip.ReconcileWindow;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;

public class TridasAnnotationController extends AbstractTridasAnnotationController {

	private Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor normalCursor = Cursor.getDefaultCursor();
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

        	this.setCursor(busyCursor);
			DBBrowser browser = new DBBrowser(new Frame(), true);
			Boolean success = browser.doSearchForAssociatedSeries(entity);
			browser.setVisible(true);
			this.setCursor(normalCursor);
			
			if(success==false) {
				browser.dispose();
				return;
			}
			
			if(browser.getReturnStatus() == DBBrowser.RET_OK) {
				ElementList toOpen = browser.getSelectedElements();
				
				for(Element e1 : toOpen) {
					// load it
					Sample s;
					try {
						s = e1.load();
					} catch (IOException ioe) {
						Alert.error(I18n.getText("error.loadingSample"),
								I18n.getText("error.cantOpenFile") +":" + ioe.getMessage());
						continue;
					}

					OpenRecent.sampleOpened(new SeriesDescriptor(s));
					
					// open it
					new Editor(s);
				}
			}
			
			
        }
        else if (e.getActionCommand() == "viewMetadata")
        {

        	this.setCursor(busyCursor);
    		MetadataBrowser dialog = new MetadataBrowser(null, false);
    		dialog.setEntity(entity, entity.getClass());
    		dialog.hideTree();
    		dialog.setVisible(true);
    		this.setCursor(normalCursor);
        }
        
        
    }







}
