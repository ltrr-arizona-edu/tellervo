package edu.cornell.dendro.corina.gis;

import gov.nasa.worldwind.examples.AnnotationControls.AppFrame;
import gov.nasa.worldwind.examples.AnnotationControls.ContentAnnotation;
import gov.nasa.worldwind.examples.util.AudioPlayerAnnotation;
import gov.nasa.worldwind.examples.util.AudioPlayerAnnotationController;

public class TridasContentAnnotation extends ContentAnnotation {

    protected Object source;
	
	public TridasContentAnnotation(AppFrame appFrame, AudioPlayerAnnotation annnotation,
            AudioPlayerAnnotationController controller, Object source) {
       
		super(appFrame, annnotation, controller);
        this.source = source;


	}

}
