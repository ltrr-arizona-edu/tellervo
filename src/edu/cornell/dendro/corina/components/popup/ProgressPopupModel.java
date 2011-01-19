/**
 * Created at Jan 19, 2011, 12:33:23 PM
 */
package edu.cornell.dendro.corina.components.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * Model for {@link ProgressPopup}, the view updates to changes on this
 * model, and if the popup is cancelled, then it sets the cancelled value
 * on this model to true.
 * @author Daniel
 *
 */
public class ProgressPopupModel extends AbstractModel {
	private static final long serialVersionUID = 1L;
	
	private int percent = 0;
	private String title = "";
	private String statusString = "";
	private String cancelString = "";


	private boolean canceled = false;
	
	/**
	 * @return the percent
	 */
	public int getPercent() {
		return percent;
	}
	/**
	 * @param argPercent the percent to set
	 */
	public void setPercent(int argPercent) {
		int old = percent;
		percent = argPercent;
		firePropertyChange("percent", old, percent);
	}
	/**
	 * @return the statusString
	 */
	public String getStatusString() {
		return statusString;
	}
	/**
	 * @param argStatusString the statusString to set
	 */
	public void setStatusString(String argStatusString) {
		String old = statusString;
		statusString = argStatusString;
		firePropertyChange("statusString", old, statusString);
	}
	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
	 * @param argCancelled the canceled to set
	 */
	public void setCanceled(boolean argCancelled) {
		boolean old = canceled;
		canceled = argCancelled;
		firePropertyChange("canceled", old, canceled);
	}
	
	public void setTitle(String argTitle) {
		String old = title;
		title = argTitle;
		firePropertyChange("title", old, title);
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the cancelString
	 */
	public String getCancelString() {
		return cancelString;
	}
	/**
	 * @param argCancelString the cancelString to set
	 */
	public void setCancelString(String argCancelString) {
		String old = cancelString;
		cancelString = argCancelString;
		firePropertyChange("cancelString", old, cancelString);
	}
}
