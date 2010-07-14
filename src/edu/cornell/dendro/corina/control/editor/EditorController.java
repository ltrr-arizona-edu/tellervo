/**
 * 2:50:18 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.control.editor;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.ObjectEvent;
import com.dmurph.mvc.control.FrontController;

import edu.cornell.dendro.corina.model.editor.AnnotationTableModel;
import edu.cornell.dendro.corina.model.editor.EditorModel;
import edu.cornell.dendro.corina.view.editor.RingAnnotations;

/**
 *
 * @author daniel
 */
public class EditorController extends FrontController{
	public static final String ANNOTATIONS_APPLY_EVENT = "ANNOTATIONS_APPLY_EVENT";
	public static final String ANNOTATIONS_CANCEL_EVENT = "ANNOTATIONS_CANCEL_EVENT";
	public static final String ANNOTATIONS_ADD_EDIT_CUSTOM_EVENT = "ANNOTATIONS_CUSTOM_EVENT";
	public static final String ANNOTATIONS_CUSTOM_TEXT_CHANGE_EVENT = "ANNOTATIONS_CUSTOM_TEXT_CHANGE_EVENT";

	private EditorModel model = EditorModel.getInstance();
	
	private boolean isDerivedSet = true;
	
	public EditorController(){
		registerCommand( ANNOTATIONS_APPLY_EVENT, "applyButtonPushed");
		registerCommand( ANNOTATIONS_CANCEL_EVENT, "cancelButtonPushed");
		registerCommand( ANNOTATIONS_ADD_EDIT_CUSTOM_EVENT, "customButtomPushed");
		registerCommand( ANNOTATIONS_CUSTOM_TEXT_CHANGE_EVENT, "customTextChanged");
	}
	
	public void applyButtonPushed(MVCEvent argEvent){
		System.out.println("apply button");
	}
	
	public void cancelButtonPushed(MVCEvent argEvent){
		System.out.println("cancel button");
	}
	
	public void customButtomPushed(MVCEvent argEvent){
		System.out.println("custom button");
	}
	
	@SuppressWarnings("unchecked")
	public void customTextChanged(MVCEvent argEvent){
		ObjectEvent<String> event = (ObjectEvent<String>) argEvent;
		System.out.println("text changed: '"+event.getValue()+"'");
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
		
		tblModel.addRow( new Object[]{"Test","Test again", true, 19});
		
		model.setAnnotationsTableModel( tblModel);
		
		JFrame frame = new JFrame();
    	frame.setDefaultCloseOperation( 3);
    	RingAnnotations ra = new RingAnnotations();
    	frame.add(ra);
    	frame.setVisible(true);
    	ToolTipManager.sharedInstance().setDismissDelay( 10000);
    	ToolTipManager.sharedInstance().setInitialDelay( 1000);
	}
	
	public static void main(String[] args){
		EditorController controller = new EditorController();
		controller.showAnnotationWindow();
	}
}
