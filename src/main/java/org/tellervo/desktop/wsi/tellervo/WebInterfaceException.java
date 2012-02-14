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

import org.tellervo.desktop.wsi.ResourceException;

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
