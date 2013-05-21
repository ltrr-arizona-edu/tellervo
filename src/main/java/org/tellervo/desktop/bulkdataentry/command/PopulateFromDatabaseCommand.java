package org.tellervo.desktop.bulkdataentry.command;

import java.awt.image.SampleModel;
import java.util.List;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromDatabaseEvent;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.components.popup.ProgressPopup;
import org.tellervo.desktop.components.popup.ProgressPopupModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.io.command.ConvertCommand;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.util.WSIQuery;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;

public class PopulateFromDatabaseCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(PopulateFromDatabaseCommand.class);
	private Integer counter = 0;
	
	@Override
	public void execute(MVCEvent argEvent) {
		
		PopulateFromDatabaseEvent event = (PopulateFromDatabaseEvent) argEvent;

		if (event.model instanceof ObjectModel)
		{
			populateObjects(event);
		}
		else if (event.model instanceof ElementModel)
		{
			populateElement(event);
		}
		else if (event.model instanceof SampleModel)
		{
			
		}
	}
	
	private void populateSample(PopulateFromDatabaseEvent event)
	{
		TridasEntityPickerDialog pickDialog = new TridasEntityPickerDialog();
		
		TridasElement el = (TridasElement) pickDialog.pickSpecificEntity(null, "Pick Element", TridasElement.class);
		
		List<TridasSample> entities = WSIQuery.getSamplesOfElement(el);
		Double total = (double) entities.size();

		
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		
		event.model.getTableModel().selectAll();
		event.model.removeSelected();
		
		ProgressPopup storedProgressDialog = null;
		
		try {
			
			ProgressPopupModel dialogModel = new ProgressPopupModel();
			dialogModel.setCancelString(I18n.getText("io.convert.cancel"));
			
			final ProgressPopup progressDialog = new ProgressPopup(null, true, dialogModel);
			storedProgressDialog = progressDialog;
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					progressDialog.setVisible(true);
				}
			});
			while(!progressDialog.isVisible()){
				Thread.sleep(100);
			}
		
			int i =0;
			for(TridasSample s : entities)
			{
				if(dialogModel.isCanceled()){
					break;
				}
				
			
				SingleSampleModel newrow = (SingleSampleModel) event.model.createRowInstance();
				newrow.populateFromTridasSample(s);
				
				try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
				
				otm.setSelected(newrow, false);
				} catch (Exception ex){}
				rows.add(newrow);
				i++;
				
				
				Double percprog = (i / total)*100;
				dialogModel.setPercent(percprog.intValue());
			}
		} catch (Exception ex) {
			log.error("Exception thrown while converting", ex);
			throw new RuntimeException(ex);
		} finally {
			
			try{
				storedProgressDialog.dispose();
				
			} catch (Exception e)
			{
				log.debug("Exception caught while disposing of progress dialog");
			}
			
			if (storedProgressDialog != null) {
				storedProgressDialog.setVisible(false);
			}
		}
	}
	

	private void populateElement(PopulateFromDatabaseEvent event)
	{
		TridasEntityPickerDialog pickDialog = new TridasEntityPickerDialog();
		
		TridasObject o = (TridasObject) pickDialog.pickSpecificEntity(null, "Pick Object", TridasObject.class);
		
		List<TridasElement> entities = WSIQuery.getElementsOfObject(o);
		Double total = (double) entities.size();

		
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		
		event.model.getTableModel().selectAll();
		event.model.removeSelected();
		
		ProgressPopup storedProgressDialog = null;
		
		try {
			
			ProgressPopupModel dialogModel = new ProgressPopupModel();
			dialogModel.setCancelString(I18n.getText("io.convert.cancel"));
			
			final ProgressPopup progressDialog = new ProgressPopup(null, true, dialogModel);
			storedProgressDialog = progressDialog;
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					progressDialog.setVisible(true);
				}
			});
			while(!progressDialog.isVisible()){
				Thread.sleep(100);
			}
		
			int i =0;
			for(TridasElement e : entities)
			{
				if(dialogModel.isCanceled()){
					break;
				}
				
			
				SingleElementModel newrow = (SingleElementModel) event.model.createRowInstance();
				newrow.populateFromTridasElement(e);
				newrow.setProperty(SingleElementModel.OBJECT, o);
		

				try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
				
				otm.setSelected(newrow, false);
				
				} catch (Exception ex){}
				rows.add(newrow);
				i++;
				
				
				Double percprog = (i / total)*100;
				dialogModel.setPercent(percprog.intValue());
			}
		} catch (Exception ex) {
			log.error("Exception thrown while converting", ex);
			throw new RuntimeException(ex);
		} finally {
			
			try{
				storedProgressDialog.dispose();
				
			} catch (Exception e)
			{
				log.debug("Exception caught while disposing of progress dialog");
			}
			
			if (storedProgressDialog != null) {
				storedProgressDialog.setVisible(false);
			}
		}
		
		
	}
	
	private void populateObjects(PopulateFromDatabaseEvent event)
	{
		List<TridasObjectEx> entities = App.tridasObjects.getTopLevelObjectList();
		Double total = (double) App.tridasObjects.getObjectList().size();
		
		MVCArrayList<Object> rows = (MVCArrayList<Object>) event.model.getRows();
		
		event.model.getTableModel().selectAll();
		event.model.removeSelected();
		
		ProgressPopup storedConvertProgress = null;
		
		try {
			
			ProgressPopupModel dialogModel = new ProgressPopupModel();
			dialogModel.setCancelString(I18n.getText("io.convert.cancel"));
			
			final ProgressPopup convertProgress = new ProgressPopup(null, true, dialogModel);
			storedConvertProgress = convertProgress;
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					convertProgress.setVisible(true);
				}
			});
			while(!convertProgress.isVisible()){
				Thread.sleep(100);
			}
		
			counter =0;
			for(TridasObjectEx o : entities)
			{
				if(dialogModel.isCanceled()){
					break;
				}
				
				recurseObjects(dialogModel, event, o, null, rows, total);
				
			}
		} catch (Exception e) {
			log.error("Exception thrown while populating table", e);
			throw new RuntimeException(e);
		} finally {
			
			try{
				storedConvertProgress.dispose();
				
			} catch (Exception e)
			{
				log.debug("Exception caught while disposing of progress dialog");
			}
			
			if (storedConvertProgress != null) {
				storedConvertProgress.setVisible(false);
			}
		}
	}
	
	
	private void recurseObjects(ProgressPopupModel dialogModel, 
			PopulateFromDatabaseEvent event, TridasObject o, TridasObject parentObject, 
			MVCArrayList<Object> rows, Double total)
	{
		log.debug("recurse called with count = "+counter);

		SingleObjectModel newrow = (SingleObjectModel) event.model.createRowInstance();
		newrow.populateFromTridasObject(o);
		
		if(parentObject!=null)
		{
			newrow.setProperty(SingleObjectModel.PARENT_OBJECT, parentObject);
		}
		
		try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
		
		otm.setSelected(newrow, false);
		} catch (Exception e){}
		rows.add(newrow);
		counter++;
		
		
		Double percprog = (counter / total)*100;
		dialogModel.setPercent(percprog.intValue());
		
		
		if(o.isSetObjects())
		{
			for(TridasObject o2 : o.getObjects())
			{
				if(parentObject==null)
				{
					recurseObjects(dialogModel, event, o2, o, rows, total);
				}
				else
				{
					recurseObjects(dialogModel, event, o2, o, rows, total);
				}
			}
		}
	}
	
}
