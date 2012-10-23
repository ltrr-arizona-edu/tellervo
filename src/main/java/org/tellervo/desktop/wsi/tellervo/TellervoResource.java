/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.wsi.tellervo;

import java.awt.Dialog;
import java.awt.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.LoginDialog;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestStatus;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIHeader;
import org.tellervo.schema.WSIMessage;
import org.tellervo.schema.WSINonce;
import org.tellervo.schema.WSIRequest;
import org.tellervo.schema.WSIRootElement;
import org.tellervo.desktop.util.DomToString;
import org.tellervo.desktop.wsi.DataAccessor;
import org.tellervo.desktop.wsi.Resource;
import org.tellervo.desktop.wsi.ResourceException;


/**
 * An implementation of a resource that uses the Tellervo webservice
 */

public abstract class TellervoResource extends Resource<WSIRootElement, WSIRootElement> {
	
    private final static Logger log = LoggerFactory.getLogger(TellervoResource.class);

	/**
	 * Construct a resource that prompts with a login dialog
	 * 
	 * @param resourceName
	 * @param queryType
	 */
	public TellervoResource(String resourceName, TellervoRequestType queryType) {
		this(resourceName, queryType, BadCredentialsBehavior.PROMPT_FOR_LOGIN);
	}

	/**
	 * Construct a resource with the specified behavior on bad credentials
	 * 
	 * @param resourceName
	 * @param queryType
	 * @param badCredentialsBehavior
	 */
	public TellervoResource(String resourceName, TellervoRequestType queryType,
			BadCredentialsBehavior badCredentialsBehavior) {
		super(resourceName);
		this.queryType = queryType;
		this.badCredentialsBehavior = badCredentialsBehavior;
	}
	
	/** The verb associated with this resource */
	private TellervoRequestType queryType;
	
	/**
	 * @return the verb associated with this resource
	 */
	public TellervoRequestType getQueryType() {
		return queryType;
	}

	/**
	 * DANGER: do not use this during the processing of a query
	 * 
	 * @param queryType the queryType to set
	 */
	protected void setQueryType(TellervoRequestType queryType) {
		this.queryType = queryType;
	}

	/**
	 * Populate a webservice request element for this resource
	 * 
	 * @param request
	 */
	protected abstract void populateRequest(WSIRequest request);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tellervo.desktop.wsi.Resource#getDataAccessor()
	 */
	@Override
	public DataAccessor<WSIRootElement, WSIRootElement> getDataAccessor() {
		// get a tellervo webservice accessor
		return new TellervoWsiAccessor(getResourceName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tellervo.desktop.wsi.Resource#getQueryObject()
	 */
	@Override
	protected final WSIRootElement getQueryObject() throws ResourceException {
		WSIRootElement root = new WSIRootElement();
		WSIRequest request = new WSIRequest();

		// set the request type
		request.setType(queryType);
		
		// populate this request with Tellervo stuff
		populateRequest(request);

		// if we have a request format override, implement it
		if (hasProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT)) {
			request.setFormat(getProperty(
					TellervoResourceProperties.ENTITY_REQUEST_FORMAT,
					TellervoRequestFormat.class));
		}
		
		// and send it off!
		root.setRequest(request);

		return root;
	}

	/**
	 * Handle a recoverable, credentials exception.
	 * 
	 * This function is responsible for calling queryFailed. It must not pop up
	 * any dialogs except for a login dialog.
	 * 
	 * @param w the exception
	 * @return true if we should try accessing the resource again, false if not.
	 * @throws UserCancelledException
	 * @throws WebPermissionsException
	 */
	private boolean handleCredentialsException(WebPermissionsException w)
			throws UserCancelledException, WebPermissionsException {
		log.error(w.getLocalizedMessage());
		WebInterfaceCode code = w.getMessageCode();

		// if we're set to prompt for login on failure, do so!
		if (badCredentialsBehavior == BadCredentialsBehavior.PROMPT_FOR_LOGIN) {
			// build a login dialog with the proper owner
			LoginDialog dialog;
			Window ownerWindow = getOwnerWindow();
			if (ownerWindow != null) {
				if (ownerWindow instanceof Dialog)
					dialog = new LoginDialog((Dialog) ownerWindow);
				else if (ownerWindow instanceof Dialog)
					dialog = new LoginDialog((Dialog) ownerWindow);
				else
					dialog = new LoginDialog();
			} else
				dialog = new LoginDialog();

			// dialog needs to know the nonce!
			dialog.setNonce(w.getNonce(), w.getNonceSeq());

			try {

				if (code == WebInterfaceCode.AUTHENTICATION_FAILED)
					dialog.doLogin("Invalid username or password", true);
				else
					dialog.doLogin("(for access to " + getResourceName() + ")",
							false);

				// dialog succeeded?
				return true;
			} catch (UserCancelledException uce) {
				log.debug("User cancelled query on " + getResourceName());
				throw uce;
			}
		}

		// no joy: throw it!
		throw w;
	}

	@Override
	protected PreprocessResult preprocessQuery(WSIRootElement object)
			throws ResourceException, UserCancelledException {
		// get the header
		WSIHeader header = object.getHeader();
		if (header == null)
			throw new ResourceException("Header is missing in request document");

		// simple enough: did this succeed?
		if (header.getStatus() == TellervoRequestStatus.OK)
			return PreprocessResult.SUCCESS; // whew, that was easy.

		// look for error codes...
		for (WSIMessage msg : header.getMessages()) {
			// determine the code
			WebInterfaceCode msgCode = WebInterfaceCode.byNumericCode(msg.getCode());
			String msgText = WSIMessageAsString(msg);

			switch (msgCode) {
			case AUTHENTICATION_FAILED:
			case LOGIN_REQUIRED: {
				WSINonce nonce = header.getNonce();

				// no nonce? can't continue
				if (nonce == null)
					throw new WebInterfaceException(msgCode,
							"Authentication problem, but server provided invalid authentication request");

				// generate an exception that we might throw...
				WebPermissionsException wpe = new WebPermissionsException(msgCode, 
						msgText, nonce.getValue(), nonce.getSeq());
				
				if(handleCredentialsException(wpe))
					return PreprocessResult.TRY_AGAIN;
			}

			case PERMISSION_DENIED:
				throw new WebPermissionsException(msgCode, msgText);
				
			// ignore PHP warnings?
			case PHP_WARNING:
				return PreprocessResult.SUCCESS;

			default:
				throw new WebInterfaceException(msgCode, msgText);
			}
		}

		// we shouldn't get here unless we got a non-ok result and no message
		// codes
		return PreprocessResult.FAILURE;
	}

	/**
	 * Concatenate the contents of a WSIMessage
	 * 
	 * @param msg
	 * @return
	 */
	private String WSIMessageAsString(WSIMessage msg) {
		StringBuffer sb = new StringBuffer();

		for (Object o : msg.getContent()) {
			if (o instanceof String)
				sb.append((String) o);
			else if (o instanceof org.w3c.dom.Element) {
				sb.append(new DomToString((org.w3c.dom.Element) o, false));
			}
		}

		return sb.toString();
	}

	/**
	 * The type behavior to perform when a query does not succeed due to
	 * missing/invalid credentials
	 */
	public static enum BadCredentialsBehavior {
		/** Pop up a dialog box and ask for login, if applicable */
		PROMPT_FOR_LOGIN,
		/** Fail and throw a ResourceException */
		JUST_FAIL
	}

	/** How we behave upon receiving invalid credentials */
	private BadCredentialsBehavior badCredentialsBehavior;
}
