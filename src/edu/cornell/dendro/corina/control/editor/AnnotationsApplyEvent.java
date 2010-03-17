/**
 * 3:29:16 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.control.editor;

import edu.cornell.dendro.corina.event.CEvent;

/**
 *
 * @author daniel
 */
public class AnnotationsApplyEvent extends CEvent {
	public static final String ANNOTATIONS_APPLY = "ANNOTATIONS_APPLY";
	
	public AnnotationsApplyEvent(){
		super(ANNOTATIONS_APPLY);
	}
}
