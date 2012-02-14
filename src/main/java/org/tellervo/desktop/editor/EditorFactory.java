/**
 * 
 */
package org.tellervo.desktop.editor;

import java.awt.Container;
import java.awt.Cursor;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.CorinaWsiTridasElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSIEntity;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.support.XMLDateUtils;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.labels.LabBarcode;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


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
		
		public boolean barcodeScanSuccessful(){
			if(object!=null)
			{
				return true;
			}
			
			return false;
		}
		
		/**
		 * Populate the given Corina sample from the information we acquired
		 * @param s
		 */
		public void populateCorinaSample(Sample s) {
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
		public WSIBox loadBoxFromSample(TridasSample sample) {
			TridasGenericField field = GenericFieldUtils.findField(sample, "tellervo.boxID");
			
			if(field == null)
				return null;
			
			// create an entity for reading
			WSIEntity entity = new WSIEntity();
			entity.setId(field.getValue());
			entity.setType(EntityType.BOX);
			
			// get a box resource
			EntityResource<WSIBox> rsrc = new EntityResource<WSIBox>(entity,
					TellervoRequestType.READ, WSIBox.class);
			
			// we want it minimal 
			// (because we're asking for a box and don't want its massive amount of info
			rsrc.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
			
			TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(dialog, rsrc);
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
					TellervoRequestType.READ, TridasObject.class);
			
			// we want it comprehensive 
			// (because we're asking for a sample and getting back an object->sample tree)
			rsrc.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
			
			TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(dialog, rsrc);
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
			String reason = I18n.getText("error.noError");
			
			if(barcodeText.length() == 0) {
				reason = I18n.getText("error.noBarcode");
			}
			else {
				try {
					LabBarcode.DecodedBarcode barcode = LabBarcode.decode(barcodeText);
					
					if(barcode.uuidType != LabBarcode.Type.SAMPLE) {
						reason = MessageFormat.format(I18n.getText("error.incorrectBarcodeType"),
								new Object[] { I18n.getText("tridas.sample") });
						//reason = "You may only use a SAMPLE barcode (not " + barcode.uuidType + ")";
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
							reason = I18n.getText("error.loadingData");
					}
					
				} catch (IllegalArgumentException iae) {
					reason = iae.getMessage();
				}
			}
			
			if(!success) {
				int result = JOptionPane.showConfirmDialog(dialog, reason + ".\n" + 
						I18n.getText("question.scanNewBarcode"), 
						I18n.getText("error.invalidBarcode"), 
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
	
	public static void newSeries(Container container) {
		
		container.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// should we get this elsewhere?
		String title = "["+I18n.getText("editor.newSeries")+ "]";

		BarcodeDialogResult result = null;
    	if(!App.prefs.getBooleanPref(PrefKey.BARCODES_DISABLED, false))
    	{
			final JDialog dialog = new JDialog();
			final ScanBarcodeUI barcodeUI = new ScanBarcodeUI(dialog);
			
			dialog.setTitle(I18n.getText("barcode"));
			dialog.setIconImage(Builder.getApplicationIcon());
			dialog.setContentPane(barcodeUI);
			dialog.setResizable(false);
			dialog.pack();
			dialog.setModal(true);
			Center.center(dialog);
			dialog.setVisible(true);
			result = barcodeUI.getResult();
	
			// no success, so just ignore..
			if(!result.success){
				container.setCursor(Cursor.getDefaultCursor());
				return;
			}
    	}

		/*
		if(!result.barcodeScanSuccessful())
		{
			Sample sample = new Sample();
			new Editor(sample).setVisible(true);
		}
		*/
		
		
		// make a new measurement series
		TridasMeasurementSeries series = new TridasMeasurementSeries();
		series.setTitle(title);

		// set up the new measuring method
		TridasMeasuringMethod method = new TridasMeasuringMethod();
		method.setNormalTridas(NormalTridasMeasuringMethod.MEASURING_PLATFORM);
		series.setMeasuringMethod(method);

		// set the measuring date to today
		series.setMeasuringDate(XMLDateUtils.toDate(new Date(), null));

		// make dataset ref, based on our series
		Sample sample = new Sample(series);

		// default title
		sample.setMeta(Metadata.TITLE, I18n.getText("general.newEntry")+": " + title);

		// attach anything we loaded in the above dialog
		if(result!=null)
		{
			result.populateCorinaSample(sample);
		}
		
		// setup our loader and series identifier
		CorinaWsiTridasElement.attachNewSample(sample);

		// start the editor
		new Editor(sample).setVisible(true);
		
		container.setCursor(Cursor.getDefaultCursor());
	}
	

}
