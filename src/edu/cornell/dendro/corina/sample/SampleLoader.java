package edu.cornell.dendro.corina.sample;

import java.io.IOException;

public interface SampleLoader {
	/**
	 * Get a String-based representation of the 'path' information for the file
	 * For File-based samples, this is a file name
	 * For URL-based samples, this is its URI.toString(); 
	 * @return
	 */
	//public String getLocationRepresentation();

	// self explanatory
	public Sample load() throws IOException;
	public BaseSample loadBasic() throws IOException;
	public boolean save(Sample s) throws IOException;

	// populate any internal loader data from this sample
	public void preload(BaseSample bs);
	
	public String getName();
	public String getShortName();
}
