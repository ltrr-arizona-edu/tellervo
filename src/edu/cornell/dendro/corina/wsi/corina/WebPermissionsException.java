package edu.cornell.dendro.corina.wsi.corina;

public class WebPermissionsException extends WebInterfaceException {
	private final String nonce;
	private final String seq;

	public WebPermissionsException(WebInterfaceCode messageCode, String messageText) {
		this(messageCode, messageText, null, null);
	}

	public WebPermissionsException(WebInterfaceCode messageCode, String messageText, String nonce, String nonceSeq) {
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
