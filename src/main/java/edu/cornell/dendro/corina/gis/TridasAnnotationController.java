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
package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

import org.tridas.interfaces.ITridas;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.gui.dbbrowse.MetadataBrowser;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import gov.nasa.worldwind.WorldWindow;

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
