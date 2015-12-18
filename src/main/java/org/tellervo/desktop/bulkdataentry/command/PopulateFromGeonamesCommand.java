package org.tellervo.desktop.bulkdataentry.command;

import java.util.List;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromGeonamesEvent;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.IBulkImportSingleRowModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.components.popup.ProgressPopup;
import org.tellervo.desktop.components.popup.ProgressPopupModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.GeonamesUtil;
import org.tellervo.desktop.wsi.util.WSIQuery;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasObject;
import org.tridas.spatial.SpatialUtils;
import org.tridas.util.TridasObjectEx;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;

public class PopulateFromGeonamesCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(PopulateFromGeonamesCommand.class);
	private Integer counter = 0;
	
	@Override
	public void execute(MVCEvent argEvent) {
		
		PopulateFromGeonamesEvent event = (PopulateFromGeonamesEvent) argEvent;

		if (event.model instanceof ObjectModel)
		{
			populateObjects(event);
		}
		else if (event.model instanceof ElementModel)
		{
			populateElement(event);
		}

	}
	

	/*private void populateElement(PopulateFromGeonamesEvent event)
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
		
		
	}*/
	
	private void populateElement(PopulateFromGeonamesEvent event)
	{
		populateFromGeonames(event);
	}
	
	private void populateObjects(PopulateFromGeonamesEvent event)
	{
		populateFromGeonames(event);
	}
	
	
	private void populateFromGeonames(PopulateFromGeonamesEvent event)
	{
				
		MVCArrayList<IBulkImportSingleRowModel> rows = event.model.getRows();
			
		Double total = (double) rows.size();
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
			for(IBulkImportSingleRowModel row :rows)
			{
				if(dialogModel.isCanceled()){
					break;
				}
				
				Double latitude = null;
				Double longitude= null;
				String country= null;
				String city= null;
				String countryProp = null;
				String cityProp = null;
				if (event.model instanceof ObjectModel)
				{
					latitude = (Double) row.getProperty(SingleObjectModel.LATITUDE);
					longitude = (Double) row.getProperty(SingleObjectModel.LONGITUDE);
					country = (String) row.getProperty(SingleObjectModel.COUNTRY);
					city = (String) row.getProperty(SingleObjectModel.CITY_TOWN);
					countryProp = SingleObjectModel.COUNTRY;
					cityProp = SingleObjectModel.CITY_TOWN;
				}
				else if (event.model instanceof ElementModel)
				{
					latitude = (Double) row.getProperty(SingleElementModel.LATITUDE);
					longitude = (Double) row.getProperty(SingleElementModel.LONGITUDE);
					country = (String) row.getProperty(SingleElementModel.COUNTRY);
					city = (String) row.getProperty(SingleElementModel.CITY_TOWN);
					countryProp = SingleElementModel.COUNTRY;
					cityProp = SingleElementModel.CITY_TOWN;
				}

				if(latitude!=null && longitude!=null)
				{
					
					TridasLocation location = new TridasLocation();
					location.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(latitude, longitude));
										
					TridasAddress address = GeonamesUtil.getAddressForLocation(location);

					if(address!=null) 
					{
					
						if(address.isSetCountry() && country==null )
						{
							log.debug("Setting country to :"+address.getCountry());
							row.setProperty(countryProp, address.getCountry());
						}
						
						if(address.isSetCityOrTown() && city==null )
						{
							log.debug("Setting city to :"+address.getCityOrTown());
							row.setProperty(cityProp, address.getCityOrTown());
						}	
					}
				}
				

				
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
			
			AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
			otm.fireTableDataChanged();
		}
		
		
		try {
			for(IBulkImportSingleRowModel row :rows)
			{
				Double latitude = null;
				Double longitude= null;
				String country= null;
				String city= null;
				String countryProp = null;
				String cityProp = null;
				if (event.model instanceof ObjectModel)
				{
					latitude = (Double) row.getProperty(SingleObjectModel.LATITUDE);
					longitude = (Double) row.getProperty(SingleObjectModel.LONGITUDE);
					country = (String) row.getProperty(SingleObjectModel.COUNTRY);
					city = (String) row.getProperty(SingleObjectModel.CITY_TOWN);
					countryProp = SingleObjectModel.COUNTRY;
					cityProp = SingleObjectModel.CITY_TOWN;
				}
				else if (event.model instanceof ElementModel)
				{
					latitude = (Double) row.getProperty(SingleElementModel.LATITUDE);
					longitude = (Double) row.getProperty(SingleElementModel.LONGITUDE);
					country = (String) row.getProperty(SingleElementModel.COUNTRY);
					city = (String) row.getProperty(SingleElementModel.CITY_TOWN);
					countryProp = SingleElementModel.COUNTRY;
					cityProp = SingleElementModel.CITY_TOWN;
				}
								
				if(latitude!=null && longitude!=null)
				{
					
					TridasLocation location = new TridasLocation();
					location.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(latitude, longitude));
										
					TridasAddress address = GeonamesUtil.getAddressForLocation(location);

					if(address==null) continue;
					
					if(address.isSetCountry() && country==null )
					{
						log.debug("Setting country to :"+address.getCountry());
						row.setProperty(countryProp, address.getCountry());
					}
					
					if(address.isSetCityOrTown() && city==null )
					{
						log.debug("Setting city to :"+address.getCityOrTown());
						row.setProperty(cityProp, address.getCityOrTown());
					}	
				}
				
				AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
				otm.fireTableDataChanged();
			}
		

	
		} catch (Exception e) {
			log.error("Exception thrown while populating table", e);
			throw new RuntimeException(e);
		} finally {
			
		}
	}
	
	
	
	/*private void populateObjects(PopulateFromGeonamesEvent event)
	{

		MVCArrayList<SingleObjectModel> rows = event.model.getRows();
			
		Double total = (double) rows.size();
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
			for(SingleObjectModel row :rows)
			{
				if(dialogModel.isCanceled()){
					break;
				}
				
			
				Double latitude = (Double) row.getProperty(SingleObjectModel.LATITUDE);
				Double longitude = (Double) row.getProperty(SingleObjectModel.LONGITUDE);
				String country = (String) row.getProperty(SingleObjectModel.COUNTRY);
				String city = (String) row.getProperty(SingleObjectModel.CITY_TOWN);

				
				if(latitude!=null && longitude!=null)
				{
					
					TridasLocation location = new TridasLocation();
					location.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(latitude, longitude));
										
					TridasAddress address = GeonamesUtil.getAddressForLocation(location);

					if(address!=null) 
					{
					
						if(address.isSetCountry() && country==null )
						{
							log.debug("Setting country to :"+address.getCountry());
							row.setProperty(SingleObjectModel.COUNTRY, address.getCountry());
						}
						
						if(address.isSetCityOrTown() && city==null )
						{
							log.debug("Setting city to :"+address.getCityOrTown());
							row.setProperty(SingleObjectModel.CITY_TOWN, address.getCityOrTown());
						}	
					}
				}
				

				
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
			
			AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
			otm.fireTableDataChanged();
		}
		
		
		try {
			for(SingleObjectModel row :rows)
			{
				Double latitude = (Double) row.getProperty(SingleObjectModel.LATITUDE);
				Double longitude = (Double) row.getProperty(SingleObjectModel.LONGITUDE);
				String country = (String) row.getProperty(SingleObjectModel.COUNTRY);
				String city = (String) row.getProperty(SingleObjectModel.CITY_TOWN);

				
				if(latitude!=null && longitude!=null)
				{
					
					TridasLocation location = new TridasLocation();
					location.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(latitude, longitude));
										
					TridasAddress address = GeonamesUtil.getAddressForLocation(location);

					if(address==null) continue;
					
					if(address.isSetCountry() && country==null )
					{
						log.debug("Setting country to :"+address.getCountry());
						row.setProperty(SingleObjectModel.COUNTRY, address.getCountry());
					}
					
					if(address.isSetCityOrTown() && city==null )
					{
						log.debug("Setting city to :"+address.getCityOrTown());
						row.setProperty(SingleObjectModel.CITY_TOWN, address.getCityOrTown());
					}	
				}
				
				AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.model.getTableModel();
				otm.fireTableDataChanged();
			}
		

	
		} catch (Exception e) {
			log.error("Exception thrown while populating table", e);
			throw new RuntimeException(e);
		} finally {
			
		}
	}
	*/
	
	
	private void recurseObjects(ProgressPopupModel dialogModel, 
			PopulateFromGeonamesEvent event, TridasObject o, TridasObject parentObject, 
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
