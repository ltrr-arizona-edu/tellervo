package edu.cornell.dendro.cpgdb;

/**
 * Silly class that mimics toStringBuilder, for debugging
 */
public class ParamStringBuilder {
	private StringBuilder builder = new StringBuilder();
	
	public ParamStringBuilder append(String name, Object value) {
		if(builder.length() > 0)
			builder.append(", ");
		builder.append(name);
		builder.append(": ");
		
		if(value == null) {
			builder.append("<NULL>");
		}
		else {
			builder.append("[");
			builder.append(value);
			builder.append("]");
		}
		
		return this;
	}
	
	public String toString() {
		return builder.toString();
	}
}
