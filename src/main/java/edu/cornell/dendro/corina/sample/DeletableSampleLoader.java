package edu.cornell.dendro.corina.sample;

import java.io.IOException;

public interface DeletableSampleLoader extends SampleLoader {
	/** Delete the given from the server */
	public boolean delete() throws IOException;
}
