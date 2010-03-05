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
	UNSUPPORTED_REQUEST(104),
	BAD_SERVER_NONCE(105),
	USER_UNKNOWN(106),
	UNSUPPORTED_CLIENT(107),
	UNSUPPORTED_CLIENT_VERSION(108),
	
	SERVER_ERROR(666),
	SERVER_BUG(667),
	
	SQL_ERROR(701),
	FEATURE_NOT_IMPLEMENTED(702),
	INVALID_XML_CREATED(703),
	
	INVALID_USER_PARAMETERS(901),
	MISSING_USER_PARAMETERS(902),
	NO_MATCH(903),
	NOT_ENOUGH_PARAMETERS(904),
	INVALID_XML_REQUEST(905),
	RECORD_ALREADY_EXISTS(906),
	FOREIGN_KEY_VIOLATION(907),
	UNIQUE_CONSTRAINT_VIOLATION(908),
	CHECK_CONSTRAINT_VIOLATION(909),
	INVALID_DATA_TYPE(910),
	VERSION_ALREADY_EXISTS(911),
	
	PHP_WARNING(998),
	PHP_ERROR(999);
	
	private final int numericCode;
	
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
