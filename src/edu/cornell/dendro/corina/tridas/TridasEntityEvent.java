package edu.cornell.dendro.corina.tridas;

import java.util.EventObject;

/**
   An event signifying that an intermediateObject has changed
*/
public class TridasEntityEvent extends EventObject {
    /**
       Construct a new IntermediateObjectEvent with the specified source.
       @param source the IntermediateObject this event is about
    */
	public TridasEntityEvent(TridasEntityBase source) {
		super(source);
	}
}
