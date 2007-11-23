package edu.cornell.dendro.corina.webdbi;

public class LoginRequiredException extends WebInterfaceException {

	private String nonce;
	
	public LoginRequiredException(int messageCode, String messageText, String nonce) {
		super(messageCode, messageText);
		
		this.nonce = nonce;
	}

	public String getNonce() {
		return nonce;
	}

}
