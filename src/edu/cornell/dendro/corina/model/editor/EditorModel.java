/**
 * Created at 1:51:17 PM, Mar 10, 2010
 */
package edu.cornell.dendro.corina.model.editor;

import edu.cornell.dendro.corina.mvc.model.AbstractModel;

/**
 *
 * @author daniel
 */
public class EditorModel extends AbstractModel {
	public final static String ANNOTATIONS_TABLE_MODEL = "annotationTableModel";
	public final static String CUSTOM_NOTE = "customNote";
	
	private final static EditorModel model = new EditorModel();
	
	private AnnotationTableModel cleanAnnotationTableModel;
	private AnnotationTableModel annotationTableModel;
	private String cleanCustomNote = "";
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
		updateDirty(cleanCustomNote != customNote);
		super.firePropertyChange( CUSTOM_NOTE, old, customNote);
	}
	
	public AnnotationTableModel getAnnotationsTableModel(){
		return annotationTableModel;
	}
	
	public void setAnnotationsTableModel(AnnotationTableModel argModel){
		AnnotationTableModel old = annotationTableModel;
		annotationTableModel = argModel;
		updateDirty(!cleanAnnotationTableModel.equals( annotationTableModel));
		super.firePropertyChange( ANNOTATIONS_TABLE_MODEL, old, annotationTableModel);
	}
	
	public final static EditorModel getInstance(){
		return model;
	}

	@Override
	protected void revert() {
		setCustomNote( cleanCustomNote );
		setAnnotationsTableModel( (AnnotationTableModel) cleanAnnotationTableModel.clone());
	}
	
	@Override
	protected void save() {
		cleanCustomNote = customNote;
		cleanAnnotationTableModel.cloneFrom( annotationTableModel);
	}
}
