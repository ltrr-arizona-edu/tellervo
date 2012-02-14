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
package edu.cornell.dendro.corina.bulkImport.command;

import java.io.IOException;
import java.util.ArrayList;

import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;
import com.lowagie.text.DocumentException;

import edu.cornell.dendro.corina.bulkImport.control.PrintSampleBarcodesEvent;
import edu.cornell.dendro.corina.bulkImport.model.SampleModel;
import edu.cornell.dendro.corina.bulkImport.model.SingleSampleModel;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.labels.PDFLabelMaker;

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
		
		ArrayList<TridasSample> printList = new ArrayList<TridasSample>();
		MVCArrayList<SingleSampleModel> rows = ((SampleModel)event.model).getRows();
		Boolean warnNotImported = false;
		
		// Compile a list of the selected rows that have been imported
		for(SingleSampleModel row : rows)
		{
			if (row.getImported()!=null)
			{
				TridasSample sample = new TridasSample();
				row.populateToTridasSample(sample);
				printList.add(sample);	
			}
			else
			{
				warnNotImported = true;
			}
		}
				
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
			Alert.error(I18n.getText("error"), "There are no selected samples that can have their barcodes printed");
		}
		
	}
	
}
