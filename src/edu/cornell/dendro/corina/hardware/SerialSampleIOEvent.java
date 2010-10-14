package edu.cornell.dendro.corina.hardware;

import java.util.EventObject;

public class SerialSampleIOEvent extends EventObject {
	
	private static final long serialVersionUID = -6055117055932450549L;

	// sent when a new sample exists
	// value is an Integer with a new sample value 
	public final static int NEW_SAMPLE_EVENT = 1;
	
	// sent when something timed out while reading the sample
	// value is ignored
	public final static int BAD_SAMPLE_EVENT = 2;
	
	public final static int UPDATED_CURRENT_VALUE_EVENT =5;
	
	// initializing stuff.
	// int argument, value is try #
	public final static int INITIALIZING_EVENT = 3;
	// no arguments
	public final static int INITIALIZED_EVENT = 4;
	
	// generic error. string argument.
	public final static int ERROR = 100;
	
	private int type;
	private Object value;
	
	public SerialSampleIOEvent(Object source, int type, Object value) {
		super(source);
		
		this.type = type;
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public int getType() {
		return type;
	}
}
