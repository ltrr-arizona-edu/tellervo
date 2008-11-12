package edu.cornell.dendro.corina.site;

import java.util.EventObject;

/**
   An event signifying that an intermediateObject has changed
*/
public class IntermediateObjectEvent extends EventObject {
    /**
       Construct a new IntermediateObjectEvent with the specified source.
       @param source the IntermediateObject this event is about
    */
	public IntermediateObjectEvent(GenericIntermediateObject source) {
		super(source);
	}
}
