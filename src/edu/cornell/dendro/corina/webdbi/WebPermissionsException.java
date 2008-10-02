package edu.cornell.dendro.corina.webdbi;

public class WebPermissionsException extends WebInterfaceException {

	private String nonce;
	private String seq;

	public WebPermissionsException(int messageCode, String messageText) {
		this(messageCode, messageText, null, null);
	}

	public WebPermissionsException(int messageCode, String messageText, String nonce, String nonceSeq) {
		super(messageCode, messageText);
		
		this.nonce = nonce;
		this.seq = nonceSeq;
	}

	public String getNonce() {
		return nonce;
	}

	public String getNonceSeq() {
		return seq;
	}
}
