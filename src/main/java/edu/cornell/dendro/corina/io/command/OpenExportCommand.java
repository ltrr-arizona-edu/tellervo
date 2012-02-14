/**
 * Created at Jan 19, 2011, 7:18:40 PM
 */
package edu.cornell.dendro.corina.io.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.io.control.OpenExportEvent;
import edu.cornell.dendro.corina.io.model.ExportModel;
import edu.cornell.dendro.corina.io.view.ExportView;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.BaseSample;



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
		if( argEvent instanceof OpenExportEvent){
			s = ((OpenExportEvent) argEvent).sample;
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
			list = ElementList.singletonList(new Element((BaseSample) s)); // what am I doing here...
		}
		
		if(list != null && !list.isEmpty()){
			ExportModel model = new ExportModel();
			model.setElements(list);
			
			ExportView view = new ExportView(model);
			model.setExportView(view);
			view.setVisible(true);
		}
	}
	
}
