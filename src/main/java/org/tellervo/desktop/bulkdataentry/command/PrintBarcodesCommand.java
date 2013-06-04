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
package org.tellervo.desktop.bulkdataentry.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tellervo.desktop.bulkdataentry.control.PrintSampleBarcodesEvent;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.labels.PDFLabelMaker;
import org.tellervo.desktop.util.labels.ui.SampleLabelPrintingUI;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;
import com.lowagie.text.DocumentException;


/**
 * @author daniel
 *
 */
public class PrintBarcodesCommand implements ICommand {
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		PrintSampleBarcodesEvent event = (PrintSampleBarcodesEvent) argEvent;
		
		
		MVCArrayList<SingleSampleModel> rows = ((SampleModel)event.model).getRows();
		Boolean warnNotImported = false;
		
		// Compile a list of the selected rows that have been imported
		ArrayList<TridasObject> objList = new ArrayList<TridasObject>();
		TridasObject[] arr = new TridasObject[0];
		ArrayList<TridasSample> smpList = new ArrayList<TridasSample>();
		int i=0;
		for(SingleSampleModel row : rows)
		{
		
			if (row.getImported()!=null)
			{
				TridasSample sample = new TridasSample();
				row.populateToTridasSample(sample);
					
				// Find all samples for an element 
		    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
		    	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, sample.getIdentifier().getValue().toString());

		    	// we want a sample return here
				EntitySearchResource<TridasObject> resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
				resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
				
				TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource, i, rows.size());
				resource.query();	
				dialog.setVisible(true);
				
				if(!dialog.isSuccessful()) 
				{ 
					System.out.println("Error getting samples");
					return;
				}
				
				objList.addAll(resource.getAssociatedResult());
				smpList.add(sample);
			}
			else
			{
				warnNotImported = true;
			}
			
			i++;
		}
		
		List<TridasSample> sampList = SampleLabelPrintingUI.getSamplesList(objList, arr, smpList);
		ArrayList<TridasSample> printList = new ArrayList<TridasSample>();
		printList.addAll(sampList);	

		if(printList.size()>0)
		{
			
			if(warnNotImported)
			{
				Alert.message(I18n.getText("warning"), I18n.getText("bulkimport.barcodeWarning"));
			}
			
				try {
					PDFLabelMaker.preview(printList);
				} catch (DocumentException e) {
					Alert.error(I18n.getText("error"), I18n.getText("bulkimport.barcodeGenericWarning"));
					e.printStackTrace();
				} catch (IOException e) {
					Alert.error(I18n.getText("error"), I18n.getText("bulkimport.barcodeGenericWarning"));
					e.printStackTrace();
				}
		}
		else
		{
			Alert.error(I18n.getText("error"), "There are no selected samples that can have their barcodes printed. " +
											   "Barcodes can only be created for samples that have been imported into the database.");
		}
		
	}
	
}
