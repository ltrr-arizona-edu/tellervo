/**
 * Created at Jan 19, 2011, 7:18:40 PM
 */
package org.tellervo.desktop.io.command;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.io.control.OpenExportEvent;
import org.tellervo.desktop.io.model.ExportModel;
import org.tellervo.desktop.io.view.ExportView;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;




/**
 * @author Daniel
 *
 */
public class OpenExportCommand implements ICommand {
	
	private static final Logger log = LoggerFactory.getLogger(OpenExportCommand.class);
	
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		Sample s = null;
		Collection<Sample> samples = null;
		if( argEvent instanceof OpenExportEvent){
			s = ((OpenExportEvent) argEvent).sample;
			samples = ((OpenExportEvent) argEvent).allSamples;
		}
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e) {
			log.warn("Couldn't split off, not a big deal", e);
		} catch (IncorrectThreadException e) {
			log.warn("Couldn't split off, not a big deal", e);
		}
		ElementList list = null;

		if(s == null){
			DBBrowser browser = new DBBrowser(true, true);
			browser.setVisible(true);
			
			if(browser.getReturnStatus() == DBBrowser.RET_OK) {
				list = browser.getSelectedElements();
				
			}else{
				log.info("DBBrowser cancelled");
			}
		}else{
			//list = ElementList.singletonList(new Element((BaseSample) s)); // what am I doing here...
			list = new ElementList();
		}
		
		if(samples!=null)
		{
			ExportModel model = new ExportModel();
			
			for(Sample x : samples)
			{
				Element el = new Element(x);
				list.add(el);
			}
			model.setElements(list);
			
			ExportView view = new ExportView(model);
			model.setExportView(view);
			view.setVisible(true);
		}
		else if(list != null && !list.isEmpty()){
			ExportModel model = new ExportModel();
			model.setElements(list);
			
			ExportView view = new ExportView(model);
			model.setExportView(view);
			view.setVisible(true);
		}
	}
	
}
