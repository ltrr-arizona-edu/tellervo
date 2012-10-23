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
/**
 * 
 */
package org.tellervo.desktop.wsi.tellervo;

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
	PASSWORD_UPDATED_REQUIRED(109),
	PASSWORD_UPGRADE_REQUIRED(110),
	
	SERVER_ERROR(666),
	SERVER_BUG(667),
	
	SQL_ERROR(701),
	FEATURE_NOT_IMPLEMENTED(702),
	INVALID_XML_CREATED(703),
	CONFIGURATION_ERROR(704),
	
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
	MUST_BE_ONE_ADMINISTRATOR(912),
	
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
