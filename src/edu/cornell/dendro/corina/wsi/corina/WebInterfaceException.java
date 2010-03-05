package edu.cornell.dendro.corina.wsi.corina;

import edu.cornell.dendro.corina.wsi.ResourceException;

public class WebInterfaceException extends ResourceException {
	private static final long serialVersionUID = -3144827856931446583L;
	
	protected WebInterfaceCode messageCode;
	protected String messageText;
	
	public WebInterfaceException(WebInterfaceCode messageCode, String messageText) {
		super("\n\n"+ messageText + "\nError code: " + messageCode);
		
		this.messageCode = messageCode;
		this.messageText = messageText;
	}

	public WebInterfaceCode getMessageCode() {
		return messageCode;
	}

	public String getMessageText() {
		return messageText;
	}	
}
