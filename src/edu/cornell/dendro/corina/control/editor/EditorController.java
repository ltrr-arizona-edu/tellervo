/**
 * 2:50:18 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.control.editor;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import edu.cornell.dendro.corina.model.editor.AnnotationTableModel;
import edu.cornell.dendro.corina.model.editor.EditorModel;
import edu.cornell.dendro.corina.mvc.control.CEvent;
import edu.cornell.dendro.corina.mvc.control.FrontController;
import edu.cornell.dendro.corina.view.editor.RingAnnotations;

/**
 *
 * @author daniel
 */
public class EditorController extends FrontController{

	private EditorModel model = EditorModel.getInstance();
	
	private boolean isDerivedSet = false;
	
	public EditorController(){
		registerEventKey( AnnotationsApplyEvent.ANNOTATIONS_APPLY, "applyButtonPushed");
	}
	
	public void applyButtonPushed(CEvent argEvent){
		System.out.println("I WILL SAVE YOU!!");
	}
	
	
	private String[] derivedColumnNames = { "Annotation", "Type", "Filter", "Threshold" };
	private Class<?>[] derivedColumnClasses = { String.class, String.class, Boolean.class, Integer.class };
    private boolean[] derivedCanEdit = { false, false, true, true };
    private String[] concreteColumnNames = { "Annotation", "Type", "Filter" };
	private Class<?>[] concreteColumnClasses = { String.class, String.class, Boolean.class};
    private boolean[] concreteCanEdit = { false, false, true };
	
	public void showAnnotationWindow(){
		
		AnnotationTableModel tblModel;
		if(isDerivedSet){
			tblModel = new AnnotationTableModel( derivedColumnNames, derivedColumnClasses, derivedCanEdit);
		}else{
			tblModel = new AnnotationTableModel( concreteColumnNames, concreteColumnClasses, concreteCanEdit);
		}
		
		model.setAnnotationsTableModel( tblModel);
		
		JFrame frame = new JFrame();
    	frame.setDefaultCloseOperation( 3);
    	RingAnnotations ra = new RingAnnotations();
    	frame.add(ra);
    	frame.setVisible(true);
    	ToolTipManager.sharedInstance().setDismissDelay( 10000);
    	ToolTipManager.sharedInstance().setInitialDelay( 1000);
	}
}
