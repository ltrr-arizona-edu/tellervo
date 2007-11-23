package edu.cornell.dendro.corina.webdbi;

import java.io.IOException;

public class WebInterfaceException extends IOException {

	protected int messageCode;
	protected String messageText;
	
	public WebInterfaceException(int messageCode, String messageText) {
		super(messageText + " (" + messageCode + ")");
		
		this.messageCode = messageCode;
		this.messageText = messageText;
	}

	public int getMessageCode() {
		return messageCode;
	}

	public String getMessageText() {
		return messageText;
	}
	
	public static final int ERROR_UNKNOWN = -1;
	public static final int ERROR_AUTHENTICATION_FAILED = 101;
	public static final int ERROR_LOGIN_REQUIRED = 102;
	public static final int ERROR_PERMISSION_DENIED = 103;
}
