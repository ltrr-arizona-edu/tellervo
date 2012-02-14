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
package org.tellervo.desktop.hardware;

import java.util.EventObject;

import org.tellervo.desktop.hardware.AbstractSerialMeasuringDevice.DataDirection;


public class SerialSampleIOEvent extends EventObject {
	
	private static final long serialVersionUID = -6055117055932450549L;

	// sent when a new sample exists
	// value is an Integer with a new sample value 
	public final static int NEW_SAMPLE_EVENT = 1;
	
	// sent when something timed out while reading the sample
	// value is ignored
	public final static int BAD_SAMPLE_EVENT = 2;
		
	// initializing stuff.
	// int argument, value is try #
	public final static int INITIALIZING_EVENT = 3;
	// no arguments
	public final static int INITIALIZED_EVENT = 4;
	public final static int UPDATED_CURRENT_VALUE_EVENT =5;
	
	// generic error. string argument.
	public final static int ERROR = 100;
	
	public final static int RAW_DATA = 99;
	
	private int type;
	private Object value;
	private DataDirection dir = DataDirection.RECEIVED;
	
	public SerialSampleIOEvent(Object source, int type, Object value){
		super(source);
		
		this.type = type;
		this.value = value;
	}
	
	public SerialSampleIOEvent(Object source, int type, Object value, DataDirection dir) {
		super(source);
		
		this.type = type;
		this.value = value;
		this.dir = dir;
	}
	
	public Object getValue() {
		return value;
	}
	
	public int getType() {
		return type;
	}
	
	public DataDirection getDataDirection(){
		return dir;
	}
}
