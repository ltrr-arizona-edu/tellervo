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
package edu.cornell.dendro.corina_indexing;

import java.util.List;

/**
 * @author Lucas Madar
 *
 */
public abstract class IndexFunction {
	
	/** The indexable which we intend to use to create our index */
	protected Indexable input;
	
	/** Our output, provided sequentially in this list */
	protected List<? extends Number> output;
	
	/** Create an index function 
	 * 
	 * @param input The indexable sample which we intend to use
	 */
	public IndexFunction(Indexable input) {
		setInput(input);
	}
	
	public void setInput(Indexable input) {
		this.input = input;
	}
	
	public List<? extends Number> getOutput() {
		return output;
	}
	
	/**
	 * @return a string to be used to identify this Indexing class in internationalization
	 */
	public abstract String getI18nTag();
	
	/**
	 * @return a string to be included after the i18n tag in parenthesis, or null of no such string
	 */
	public String getI18nTagTrailer() {
		return null;
	}
	
	/**
	 * Compute and populate our output variable. Results available via getOutput()
	 * 
	 * In our base class, we do some sanity checking.
	 * We cannot index twice, as some things rely on being set up properly first. This could be fixed,
	 * but seems unnecessary.
	 */
	public void doIndex() {
		System.out.println("Starting index function " + getI18nTag());
		if(input == null)
			throw new IllegalArgumentException("Index function has not been properly initilaized");
		if(alreadyIndexed)
			throw new IllegalArgumentException("Index function has already been run");
		else
			alreadyIndexed = true;
		
		index();
	}
	
	public abstract void index();
	
	private boolean alreadyIndexed = false;

	/**
	 * This legacy function gets the 'id' which was once used to identify this index
	 * function in Corina data files.
	 * @return our ID
	 */
	public abstract int getLegacyID(); /*{
		return 0;
	}*/
	
	/**
	 * Returns one of these:
	 * Horizontal Linear Polynomial-2 Polynomial-3 Polynomial-4 Polynomial-5
	 * Polynomial-6 NegativeExponential FloatingAverage Highpass CubicSpline
	 * 
	 * @return the string in our database to represent this index
	 */
	public abstract String getDatabaseRepresentation();
}
