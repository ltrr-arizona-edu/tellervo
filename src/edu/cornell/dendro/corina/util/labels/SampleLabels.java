package edu.cornell.dendro.corina.util.labels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.lowagie.text.DocumentException;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.pdf.PrintablePDF;
import edu.cornell.dendro.corina.util.test.PrintReportFramework;

public class SampleLabels {

	
	public static void getLabels(Boolean printLabels, String vmid)
	{
		
		String domain = "dendro.cornell.edu/dev/";
		Sample samp = null;
		
		try {
			samp = PrintReportFramework.getSampleForID(domain, vmid);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// create the series report
		File outputFile = null;
		PDFLabelMaker labels = null;
		
		// open file
		try {
			outputFile = File.createTempFile("samplelabels", ".pdf");
			labels = new PDFLabelMaker(new CornellSampleLabelPage(), outputFile);		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(printLabels) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			for(int i = 10; i < 20; i++) {				
				labels.addUUIDBarcode("Cell " + i, LabBarcode.Type.SAMPLE, UUID.fromString(samp.getIdentifier().toString()));
			}
			
			
			try {
				labels.finish();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				PrintablePDF pdf = PrintablePDF.fromByteArray(output.toByteArray());

				// true means show printer dialog, false means just print using the default printer
				pdf.print(true);
			} catch (Exception e) {
				e.printStackTrace();
				Alert.error("Printing error", "An error occured during printing.\n  See error log for further details.");
			}
		}
		else {
			// probably better to use a chooser dialog here...
			
			for(int i = 10; i < 20; i++) {				
				labels.addUUIDBarcode("Cell " + i, LabBarcode.Type.SAMPLE, UUID.fromString(samp.getIdentifier().toString()));
			}			
			
			try {
				labels.finish();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				FileOutputStream output = new FileOutputStream(outputFile);
				
				App.platform.openFile(outputFile);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				Alert.error("Error", "An error occurred while generating the series report.\n  See error log for further details.");
				return;
			}
		}
		
	}
	
	public static void main(String args[]) {
		try {
			
			SampleLabels m = new SampleLabels();
			
			m.getLabels(false, "01946b69-d86d-5812-a5f6-21ca6f8fa483");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
