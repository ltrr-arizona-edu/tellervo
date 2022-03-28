package org.tellervo.desktop.bulkdataentry.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromBoxEvent;
import org.tellervo.desktop.bulkdataentry.model.AbstractBulkImportTableModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.components.popup.ProgressPopup;
import org.tellervo.desktop.components.popup.ProgressPopupModel;
import org.tellervo.desktop.gui.widgets.WSIBoxPicker;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.wsi.util.WSIQuery;
import org.tellervo.schema.WSIBox;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.dmurph.mvc.model.MVCArrayList;

public class PopulateFromBoxCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(PopulateFromBoxCommand.class);

	
	@Override
	public void execute(MVCEvent argEvent) {
		
		PopulateFromBoxEvent event = (PopulateFromBoxEvent) argEvent;
		populate(event);
		
	}
	
	private void populate(PopulateFromBoxEvent event)
	{
        
        WSIBox box = WSIBoxPicker.pickBox(null, "Box Picker");
        
		if(box==null || box.getIdentifier()==null || box.getIdentifier().getValue()==null)
		{
			log.debug("Nothing selected");
			return;
		}
		else 
		{
			
			log.debug("Box: "+box.getTitle()+" selected");
			
		}
		
		
		List<TridasObject> entities = WSIQuery.getSamplesOfBox(box);
		
		populateSample(event, box, entities);
		
	}
	
	
	
	private void populateSample(PopulateFromBoxEvent event, WSIBox box, List<TridasObject> entities)
	{
        	HashSet<TridasIdentifier> objecthash = new HashSet<TridasIdentifier>();
        	HashSet<TridasIdentifier> elementhash = new HashSet<TridasIdentifier>();

		
			Double total = (double) entities.size();
	
			MVCArrayList<Object> orows = (MVCArrayList<Object>) event.omodel.getRows();					
			MVCArrayList<Object> erows = (MVCArrayList<Object>) event.emodel.getRows();					
			MVCArrayList<Object> srows = (MVCArrayList<Object>) event.smodel.getRows();					

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
				for(TridasObject o : entities)
				{

					if(!objecthash.contains(o.getIdentifier()))
					{	
						objecthash.add(o.getIdentifier());		
						SingleObjectModel newrow = (SingleObjectModel) event.omodel.createRowInstance();
						newrow.populateFromTridasObject(o);
						newrow.setDirty(false);
						
						try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.omodel.getTableModel();
							otm.setSelected(newrow, false);
						} catch (Exception ex){}
						
						orows.add(newrow);
						
					}

					if(dialogModel.isCanceled()){
						break;
					}
					
					ArrayList<TridasElement> elements = TridasUtils.getElementList(o);
				
					
					for(TridasElement e : elements)
					{
						if(!elementhash.contains(e.getIdentifier()))
						{	
							elementhash.add(e.getIdentifier());		
							SingleElementModel newrow = (SingleElementModel) event.emodel.createRowInstance();
							newrow.populateFromTridasElement(e);
							newrow.setProperty(SingleSampleModel.OBJECT, o);
							newrow.setDirty(false);
							
							try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.emodel.getTableModel();
								otm.setSelected(newrow, false);
							} catch (Exception ex){}
							
							erows.add(newrow);
							
						}
						
						for(TridasSample s : e.getSamples())
						{
							SingleSampleModel newrow = (SingleSampleModel) event.smodel.createRowInstance();
							newrow.populateFromTridasSample(s);
							newrow.setProperty(SingleSampleModel.OBJECT, o);
							newrow.setProperty(SingleSampleModel.ELEMENT, e);
							newrow.setDirty(false);

							
							try{AbstractBulkImportTableModel otm = (AbstractBulkImportTableModel) event.smodel.getTableModel();
								otm.setSelected(newrow, false);
							} catch (Exception ex){}
							
							srows.add(newrow);
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
			}
	}
	

}
