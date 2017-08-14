/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.bulkdataentry.model;

import org.tellervo.desktop.bulkdataentry.view.BulkDataEntryWindow;
import org.tellervo.desktop.bulkdataentry.view.ColumnChooserView;

/**
 * Top level model that contains:
 *  - ObjectModel 
 *  - ElementModel
 *  - SampleModel
 *  - ColumnChooserView
 *  - BulkDataEntryWindow
 * 
 * @author daniel
 *
 */
public class BulkImportModel {
	private static BulkImportModel model = null;
	
	private final ObjectModel objectModel;
	private final ElementModel elementModel;
	private final SampleModel sampleModel;
	private volatile ColumnChooserView currentColumnChooserView = null;
	private BulkDataEntryWindow bulkDataEntryWindow;
	
	private BulkImportModel(){
		objectModel = new ObjectModel();
		elementModel = new ElementModel();
		sampleModel = new SampleModel(); 
	}
	
	public ObjectModel getObjectModel(){
		return objectModel;
	}
	
	public ElementModel getElementModel(){
		return elementModel;
	}
	
	public SampleModel getSampleModel(){
		return sampleModel;
	}

	public void setCurrColumnChooser(ColumnChooserView currColumnChooser) {
		this.currentColumnChooserView = currColumnChooser;
	}

	public ColumnChooserView getCurrColumnChooser() {
		return currentColumnChooserView;
	}

	public void setMainView(BulkDataEntryWindow mainView) {
		this.bulkDataEntryWindow = mainView;
	}

	public BulkDataEntryWindow getMainView() {
		return bulkDataEntryWindow;
	}

	public static BulkImportModel getInstance(){
		if(model == null){
			model = new BulkImportModel();
		}
		return model;
	}
}
