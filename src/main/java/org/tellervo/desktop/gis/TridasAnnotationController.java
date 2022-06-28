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
package org.tellervo.desktop.gis;

import gov.nasa.worldwind.WorldWindow;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.gui.dbbrowse.MetadataBrowser;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tridas.interfaces.ITridas;

public class TridasAnnotationController extends AbstractTridasAnnotationController {

	private Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor normalCursor = Cursor.getDefaultCursor();
    private ITridas entity;

    public TridasAnnotationController(WorldWindow worldWindow, TellervoAnnotation annotation, ITridas entity)
    {
        super(worldWindow, annotation);

        this.setEntity(entity);
    }

    public TridasAnnotationController(WorldWindow worldWindow, TellervoAnnotation annotation)
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
			browser.setVisible(success);
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
					
					FullEditor editor = FullEditor.getInstance();
					editor.addSample(s);
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
