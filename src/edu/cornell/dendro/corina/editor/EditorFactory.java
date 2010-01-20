/**
 * 
 */
package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import edu.cornell.dendro.corina.tridasv2.support.XMLDateUtils;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

/**
 * A better way to start the editor for new series, instead of new Editor()...!
 * 
 * Also, maybe we could handle things like 
 * 	- next editor placement (on screen)
 *  - opening the same series in multiple places (instead of reloading?)
 * 
 * @author Lucas Madar
 * @version $Id$
 */
public class EditorFactory {
	private EditorFactory() {
		// don't instantiate!
	}

	public static class BarcodeDialogResult {
		public boolean success = false;
		
		public TridasObject object;
		public TridasObject[] objectArray;
		public TridasElement element;
		public TridasSample sample;
		public WSIBox box;

		private JDialog dialog;
		
		public BarcodeDialogResult(JDialog dialog) {
			this.dialog = dialog;
		}
		
		/**
		 * Populate the given Corina sample from the information we acquired
		 * @param s
		 */
		public void popupateCorinaSample(Sample s) {
			LabCode labcode = new LabCode();
			
			if(object != null) {
				s.setMeta(Metadata.OBJECT, object);
				s.setMeta(Metadata.OBJECT_ARRAY, objectArray);
				
				for(TridasObject obj : objectArray) {
					if(obj instanceof TridasObjectEx)
						labcode.appendSiteCode(((TridasObjectEx)obj).getLabCode());
					else
						labcode.appendSiteCode(obj.getTitle());
					
					labcode.appendSiteTitle(obj.getTitle());
				}
			}
			
			if(element != null) {
				s.setMeta(Metadata.ELEMENT, element);
				labcode.setElementCode(element.getTitle());
			}
			
			if(sample != null) {
				s.setMeta(Metadata.SAMPLE, sample);
				labcode.setSampleCode(sample.getTitle());
			}
			
			if(box != null)
				s.setMeta(Metadata.BOX, box);

			// title, if one is set...
			if(s.getSeries().isSetTitle())
				labcode.setSeriesCode(s.getSeries().getTitle());
			
			s.setMeta(Metadata.LABCODE, labcode);
			s.setMeta(Metadata.TITLE, LabCodeFormatter.getDefaultFormatter().format(labcode));
		}
		
		/**
		 * Given the TridasObject, populate our internal structure
		 * @param obj
		 */
		private void populateFromObject(TridasObject obj) {
			List<TridasObject> flatObjects = new ArrayList<TridasObject>();

			// start out with the toplevel object
			flatObjects.add(obj);
			
			// if it has children, keep traversing
			while(obj.isSetObjects()) {
				obj = obj.getObjects().get(0);
				flatObjects.add(obj);
			}
		
			object = obj;
			objectArray = flatObjects.toArray(new TridasObject[0]);
			
			if(!object.isSetElements())
				return;
			
			element = object.getElements().get(0);
			
			if(!element.isSetSamples())
				return;
			
			sample = element.getSamples().get(0);
			
			box = loadBoxFromSample(sample);
		}
		
		/**
		 * Given a sample, load its box
		 * @param sample
		 * @return a WSIBox, or null if it doesn't exist
		 */
		private WSIBox loadBoxFromSample(TridasSample sample) {
			TridasGenericField field = GenericFieldUtils.findField(sample, "corina.boxID");
			
			if(field == null)
				return null;
			
			// create an entity for reading
			WSIEntity entity = new WSIEntity();
			entity.setId(field.getValue());
			entity.setType(EntityType.BOX);
			
			// get a box resource
			EntityResource<WSIBox> rsrc = new EntityResource<WSIBox>(entity,
					CorinaRequestType.READ, WSIBox.class);
			
			// we want it minimal 
			// (because we're asking for a box and don't want its massive amount of info
			rsrc.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
			
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(dialog, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
				return rsrc.getAssociatedResult();
			
			// don't care if we failed...
			
			return null;
		}
		
		/**
		 * Given a UUID, load the sample associated with it comprehensively
		 * 
		 * @param uuid
		 * @return a TridasObject hierarchy, or null on failure
		 */
		private TridasObject loadFromUUID(UUID uuid) {
			// create an entity for reading
			WSIEntity entity = new WSIEntity();
			entity.setId(uuid.toString());
			entity.setType(EntityType.SAMPLE);
			
			// associate a resource
			EntityResource<TridasObject> rsrc = new EntityResource<TridasObject>(entity, 
					CorinaRequestType.READ, TridasObject.class);
			
			// we want it comprehensive 
			// (because we're asking for a sample and getting back an object->sample tree)
			rsrc.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
			
			CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(dialog, rsrc);
			rsrc.query();
			accdialog.setVisible(true);
			
			if(accdialog.isSuccessful())
				return rsrc.getAssociatedResult();
			
			JOptionPane.showMessageDialog(dialog, "Error loading: " + accdialog.getFailException().
					getLocalizedMessage(), "Unable to load from barcode", JOptionPane.ERROR_MESSAGE);
			
			return null;
		}
		
		/**
		 * Load from a base64 uuid
		 * @param barcodeText
		 * @return true if we succeeded in loading (or just aborted), false otherwise
		 */
		public boolean loadFromBarcode(String barcodeText) {
			String reason = "No error";
			
			if(barcodeText.length() == 0) {
				reason = "No barcode entered";
			}
			else {
				try {
					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(barcodeText);
					
					if(barcode.uuidType != LabBarcode.Type.SAMPLE) {
						reason = "You may only use a SAMPLE barcode (not " + barcode.uuidType + ")";
					}
					else {
						// yay, we have a valid barcode and barcode type!
						TridasObject obj = loadFromUUID(barcode.uuid);
						
						// yay, we loaded!
						if(obj != null) {
							populateFromObject(obj);
							success = true;
						}
						else
							reason = "Unable to load associated data";
					}
					
				} catch (IllegalArgumentException iae) {
					reason = iae.getMessage();
				}
			}
			
			if(!success) {
				int result = JOptionPane.showConfirmDialog(dialog, reason + ".\nWould you " +
						"like to try to scan a new barcode?", "Invalid barcode", 
						JOptionPane.YES_NO_CANCEL_OPTION);
				
				switch(result) {
				// try again
				case JOptionPane.YES_OPTION:
					return false;
					
				// nothing loaded, just skip out
				case JOptionPane.NO_OPTION:
					success = true;
					return true;
				
				// leave dialog
				case JOptionPane.CANCEL_OPTION:
				default:
					return true;
				}
			}
			
			// yay, successful!
			return true;
		}
	}
	
	public static void newSeries() {
		// should we get this elsewhere?
		String title = "[New series]";

		final JDialog dialog = new JDialog();
		final ScanBarcodeUI barcodeUI = new ScanBarcodeUI(dialog);
		
		dialog.setContentPane(barcodeUI);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setModal(true);
		Center.center(dialog);
		dialog.setVisible(true);
		BarcodeDialogResult result = barcodeUI.getResult();

		// no success, so just ignore..
		if(!result.success)
			return;
				
		// make a new measurement series
		TridasMeasurementSeries series = new TridasMeasurementSeries();
		series.setTitle(title);

		// set up the new measuring method
		TridasMeasuringMethod method = new TridasMeasuringMethod();
		method.setNormalTridas(NormalTridasMeasuringMethod.MEASURING___PLATFORM);
		series.setMeasuringMethod(method);

		// set the measuring date to today
		series.setMeasuringDate(XMLDateUtils.toDate(new Date(), null));

		// make dataset ref, based on our series
		Sample sample = new Sample(series);

		// default title
		sample.setMeta(Metadata.TITLE, "New entry: " + title);

		// attach anything we loaded in the above dialog
		result.popupateCorinaSample(sample);
		
		// setup our loader and series identifier
		CorinaWsiTridasElement.attachNewSample(sample);

		// start the editor
		new Editor(sample).setVisible(true);
	}
	

}
