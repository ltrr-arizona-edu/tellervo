package edu.cornell.dendro.corina.wsi.corina;

import java.awt.Dialog;
import java.awt.Window;

import edu.cornell.dendro.corina.gui.LoginDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.schema.WSIHeader;
import edu.cornell.dendro.corina.schema.WSIMessage;
import edu.cornell.dendro.corina.schema.WSINonce;
import edu.cornell.dendro.corina.schema.WSIRequest;
import edu.cornell.dendro.corina.schema.WSIRootElement;
import edu.cornell.dendro.corina.util.DomToString;
import edu.cornell.dendro.corina.wsi.DataAccessor;
import edu.cornell.dendro.corina.wsi.Resource;
import edu.cornell.dendro.corina.wsi.ResourceException;

/**
 * An implementation of a resource that uses the Corina webservice
 */

public abstract class CorinaResource extends
		Resource<WSIRootElement, WSIRootElement> {
	/**
	 * Construct a resource that prompts with a login dialog
	 * 
	 * @param resourceName
	 * @param queryType
	 */
	public CorinaResource(String resourceName, ResourceQueryType queryType) {
		this(resourceName, queryType, BadCredentialsBehavior.PROMPT_FOR_LOGIN);
	}

	/**
	 * Construct a resource with the specified behavior on bad credentials
	 * 
	 * @param resourceName
	 * @param queryType
	 * @param badCredentialsBehavior
	 */
	public CorinaResource(String resourceName, ResourceQueryType queryType,
			BadCredentialsBehavior badCredentialsBehavior) {
		super(resourceName);
		this.queryType = queryType;
		this.badCredentialsBehavior = badCredentialsBehavior;
	}
	
	/** The verb associated with this resource */
	private ResourceQueryType queryType;
	
	/**
	 * @return the verb associated with this resource
	 */
	public ResourceQueryType getQueryType() {
		return queryType;
	}

	/**
	 * DANGER: do not use this during the processing of a query
	 * 
	 * @param queryType the queryType to set
	 */
	protected void setQueryType(ResourceQueryType queryType) {
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
	 * @see edu.cornell.dendro.corina.wsi.Resource#getDataAccessor()
	 */
	@Override
	public DataAccessor<WSIRootElement, WSIRootElement> getDataAccessor() {
		// get a corina webservice accessor
		return new CorinaWsiAccessor(getResourceName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cornell.dendro.corina.wsi.Resource#getQueryObject()
	 */
	@Override
	protected final WSIRootElement getQueryObject() throws ResourceException {
		WSIRootElement root = new WSIRootElement();
		WSIRequest request = new WSIRequest();

		// set the request type
		request.setType(queryType.getVerb());
		
		// populate this request with Corina stuff
		populateRequest(request);

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
		System.out.println(w);
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
				System.out.println("User cancelled query on " + getResourceName());
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
		if ("ok".equalsIgnoreCase(header.getStatus()))
			return PreprocessResult.SUCCESS; // whew, that was easy.

		// look for error codes...
		for (WSIMessage msg : header.getMessage()) {
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
							"Authentication problem, but server provided invalid authenitcation request");

				// generate an exception that we might throw...
				WebPermissionsException wpe = new WebPermissionsException(msgCode, 
						msgText, nonce.getValue(), nonce.getSeq());
				
				if(handleCredentialsException(wpe))
					return PreprocessResult.TRY_AGAIN;
			}

			case PERMISSION_DENIED:
				throw new WebPermissionsException(msgCode, msgText);

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
