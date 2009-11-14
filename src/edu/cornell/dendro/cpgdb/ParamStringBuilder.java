package edu.cornell.dendro.cpgdb;

/**
 * Silly class that mimics toStringBuilder, for debugging
 */
public class ParamStringBuilder {
	public static enum Mode {
		NORMAL,
		SQL
	}

	private StringBuilder builder = new StringBuilder();
	private final Mode mode;
	private boolean hasParam;
	
	public ParamStringBuilder() {
		this(Mode.NORMAL);
	}
	
	public ParamStringBuilder(Mode mode) {
		this.mode = mode;
		
		hasParam = false;
	}
	
	public ParamStringBuilder append(String name, Object value) {
		if(hasParam)
			builder.append(", ");
		else
			hasParam = true;
		
		switch(mode)
		{
		case NORMAL:
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
			break;
			
		case SQL:
			if(value == null) {
				builder.append("NULL");
			}
			else if(value instanceof Number) {
				builder.append(value);
			}
			else {
				builder.append("'");
				builder.append(value);
				builder.append("'");
			}
		}
		
		return this;
	}
	
	public String toString() {
		if(mode == Mode.SQL) {
			return "(" + builder.toString() + ")";
		}
		return builder.toString();
	}
}
