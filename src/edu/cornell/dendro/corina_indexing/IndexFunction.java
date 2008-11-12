/**
 * 
 */
package edu.cornell.dendro.corina_indexing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Madar
 *
 */
public abstract class IndexFunction {
	
	/** The indexable which we intend to use to create our index */
	protected Indexable input;
	
	/** Our output, provided sequentially in this list */
	protected List output;
	
	/** Create an index function 
	 * 
	 * @param input The indexable sample which we intend to use
	 */
	public IndexFunction(Indexable input) {
		setInput(input);
	}
	
	public void setInput(Indexable input) {
		this.input = input;
		output = new ArrayList(input.getData().size());
	}
	
	public List getOutput() {
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
