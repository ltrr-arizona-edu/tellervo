/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
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
