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
package org.tellervo.desktop.wsi.tellervo;

public class WebPermissionsException extends WebInterfaceException {
	private static final long serialVersionUID = 1L;
	
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
