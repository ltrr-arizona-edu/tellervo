/**
 * 2:50:18 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.control.editor;

import edu.cornell.dendro.corina.control.AbstractController;
import edu.cornell.dendro.corina.event.CEvent;
import edu.cornell.dendro.corina.model.editor.EditorModel;

/**
 *
 * @author daniel
 */
public class EditorController extends AbstractController{

	private EditorModel model = EditorModel.getInstance();
	
	public EditorController(){
		registerEventKey( AnnotationsApplyEvent.ANNOTATIONS_APPLY);
	}
	
	/**
	 * @see edu.cornell.dendro.corina.event.IEventListener#eventReceived(edu.cornell.dendro.corina.event.CEvent)
	 */
	@Override
	public void eventReceived( CEvent argEvent) {
		
	}
	
	
	private String[] derivedColumnNames = { "Annotation", "Type", "Filter", "Threshold" };
    private boolean[] derivedCanEdit = { false, false, true, true };
    private String[] concreteColumnNames = { "Annotation", "Type", "Filter" };
    private boolean[] concreteCanEdit = { false, false, true };
	
	public void showAnnotationWindow(){
		
	}
}
