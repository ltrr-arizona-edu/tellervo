/**
 * 
 */
package edu.cornell.dendro.corina.wsi.corina;

/**
 * Error codes returned by the Corina webservice
 */
public enum WebInterfaceCode {
	UNKNOWN(-1),
	AUTHENTICATION_FAILED(101),
	LOGIN_REQUIRED(102),
	PERMISSION_DENIED(103),
	BAD_SERVER_NONCE(105),
	PHP_WARNING(998),
	PHP_ERROR(999);
	
	private int numericCode;
	
	private WebInterfaceCode(int numericCode) {
		this.numericCode = numericCode;
	}
	
	/**
	 * Get the numeric code associated with this error
	 * 
	 * @return The code associated with this error
	 */
	public int getNumericCode() {
		return numericCode;
	}
	
	/**
	 * Retrieves a web interface result code by numeric id, or
	 * returns UNKNOWN if none was found
	 * 
	 * @param numericCode
	 * @return 
	 */
	public static WebInterfaceCode byNumericCode(int numericCode) {
		for(WebInterfaceCode code : values()) {
			if(code.numericCode == numericCode)
				return code;
		}
		
		return UNKNOWN;
	}
}
