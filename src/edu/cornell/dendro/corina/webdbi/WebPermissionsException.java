package edu.cornell.dendro.corina.webdbi;

public class WebPermissionsException extends WebInterfaceException {

	private String nonce;

	public WebPermissionsException(int messageCode, String messageText) {
		this(messageCode, messageText, null);
	}

	public WebPermissionsException(int messageCode, String messageText, String nonce) {
		super(messageCode, messageText);
		
		this.nonce = nonce;
	}

	public String getNonce() {
		return nonce;
	}

}
