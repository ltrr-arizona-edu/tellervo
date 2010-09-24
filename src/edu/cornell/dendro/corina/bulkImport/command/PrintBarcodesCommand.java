/**
 * Created at Aug 1, 2010, 3:10:04 AM
 */
package edu.cornell.dendro.corina.bulkImport.command;

import java.io.IOException;
import java.util.ArrayList;

import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;
import com.lowagie.text.DocumentException;

import edu.cornell.dendro.corina.bulkImport.control.AddRowEvent;
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
@SuppressWarnings("unchecked")
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
		
		if(warnNotImported)
		{
			Alert.message(I18n.getText("warning"), "Only samples that have been imported can have barcodes printed");
		}
		
		if(printList.size()>0)
		{
				try {
					PDFLabelMaker.preview(printList);
				} catch (DocumentException e) {
					Alert.error(I18n.getText("error"), "Error creating barcode labels");
					e.printStackTrace();
				} catch (IOException e) {
					Alert.error(I18n.getText("error"), "Error creating barcode labels");
					e.printStackTrace();
				}
		}
		else
		{
			Alert.error(I18n.getText("error"), "There are no selected samples that can have their barcodes printed");
		}
		
	}
	
}
