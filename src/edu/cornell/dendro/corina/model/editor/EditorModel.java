/**
 * Created at 1:51:17 PM, Mar 10, 2010
 */
package edu.cornell.dendro.corina.model.editor;

import edu.cornell.dendro.corina.model.AbstractModel;

/**
 *
 * @author daniel
 */
public class EditorModel extends AbstractModel {
	private final static EditorModel model = new EditorModel();
	
	private EditorModel(){

	}
	
	
	public final static EditorModel getInstance(){
		return model;
	}
}
