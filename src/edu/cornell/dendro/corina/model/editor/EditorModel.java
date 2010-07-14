/**
 * Created at 1:51:17 PM, Mar 10, 2010
 */
package edu.cornell.dendro.corina.model.editor;

import com.dmurph.mvc.ICloneable;
import com.dmurph.mvc.model.AbstractRevertableModel;

/**
 *
 * @author daniel
 */
public class EditorModel extends AbstractRevertableModel {
	public final static String ANNOTATIONS_TABLE_MODEL = "annotationTableModel";
	public final static String CUSTOM_NOTE = "customNote";
	
	private final static EditorModel model = new EditorModel();
	
	private AnnotationTableModel annotationTableModel;
	private String customNote = "";

	private EditorModel(){
		
	}
	
	public boolean isDerivedSet(){
		return true;
	}
	
	public String getCustomNote() {
		return customNote;
	}

	public void setCustomNote( String argCustomNote) {
		String old = customNote;
		customNote = argCustomNote;
		super.firePropertyChange( CUSTOM_NOTE, old, customNote);
	}
	
	public AnnotationTableModel getAnnotationsTableModel(){
		return annotationTableModel;
	}
	
	public void setAnnotationsTableModel(AnnotationTableModel argModel){
		AnnotationTableModel old = annotationTableModel;
		annotationTableModel = argModel;
		super.firePropertyChange( ANNOTATIONS_TABLE_MODEL, old, annotationTableModel);
	}
	
	public final static EditorModel getInstance(){
		return model;
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractRevertableModel#clone()
	 */
	@Override
	public ICloneable clone() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see com.dmurph.mvc.model.AbstractRevertableModel#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object setProperty(String argPropertyName, Object argValue) {
		Object old = null;
		if(argPropertyName.equals(ANNOTATIONS_TABLE_MODEL)){
			old = annotationTableModel;
			annotationTableModel = (AnnotationTableModel) argValue;
		}else if(argPropertyName.equals(CUSTOM_NOTE)){
			old = customNote;
			customNote = (String) argValue;
		}
		return old;
	}
	
	/**
	 * @see com.dmurph.mvc.ICloneable#cloneFrom(com.dmurph.mvc.ICloneable)
	 */
	@Override
	public void cloneFrom(ICloneable arg0) {}
}
