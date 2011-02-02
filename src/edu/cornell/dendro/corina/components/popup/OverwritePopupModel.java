/**
 * Created on Feb 1, 2011, 7:39:15 PM
 */
package edu.cornell.dendro.corina.components.popup;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author Daniel Murphy
 *
 */
@SuppressWarnings("serial")
public class OverwritePopupModel extends AbstractModel {
	public static final int OVERWRITE = 1;
	public static final int RENAME = 2;
	public static final int IGNORE = 3;
	
	private int response = IGNORE;
	private String message = "";
	private boolean applyToAll = false;
	
	public OverwritePopupModel(){}

	/**
	 * @return the applyToAll
	 */
	public boolean isApplyToAll() {
		return applyToAll;
	}

	/**
	 * @param argApplyToAll the applyToAll to set
	 */
	public void setApplyToAll(boolean argApplyToAll) {
		boolean old = applyToAll;
		applyToAll = argApplyToAll;
		firePropertyChange("applyToAll", old, applyToAll);
	}

	/**
	 * @return the response
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param argMessage the message to set
	 */
	public void setMessage(String argMessage) {
		String old = message;
		message = argMessage;
		firePropertyChange("message", old, message);
	}

	/**
	 * @param argResponse the response to set
	 */
	public void setResponse(int argResponse) {
		int old = response;
		response = argResponse;
		firePropertyChange("response", old, response);
	}
}
