/**
 * Created at Jan 19, 2011, 2:13:48 PM
 */
package edu.cornell.dendro.corina.io.control;

import org.tridas.io.naming.INamingConvention;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.tracking.ITrackable;

import edu.cornell.dendro.corina.io.model.ExportModel;

/**
 * @author Daniel
 *
 */
public class ExportEvent extends MVCEvent implements ITrackable {
	
	public final String encoding;
	public final String format;
	public final boolean grouped;
	public final ExportModel model;
	public final INamingConvention naming;
	
	public ExportEvent(ExportModel argModel, String argEncoding, String argFormat, INamingConvention argName, boolean argGrouped){
		super(IOController.EXPORT);
		encoding = argEncoding;
		format = argFormat;
		grouped = argGrouped;
		model = argModel;
		naming = argName;
	}
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
	 */
	@Override
	public String getTrackingCategory() {
		return "Export";
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
	 */
	@Override
	public String getTrackingAction() {
		return encoding;
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
	 */
	@Override
	public String getTrackingLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
	 */
	@Override
	public Integer getTrackingValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
