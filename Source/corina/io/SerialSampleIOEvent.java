package corina.io;

import java.util.EventObject;

public class SerialSampleIOEvent extends EventObject {
	
	// sent when a new sample exists
	// value is an Integer with a new sample value 
	public final static int NEW_SAMPLE_EVENT = 1;
	
	// sent when something timed out while reading the sample
	// value is ignored
	public final static int BAD_SAMPLE_EVENT = 2;
	
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
