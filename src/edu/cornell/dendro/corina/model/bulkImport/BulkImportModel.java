/**
 * Created at Jul 24, 2010, 3:40:48 PM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import com.dmurph.mvc.model.AbstractModel;

/**
 * @author daniel
 *
 */
public class BulkImportModel extends AbstractModel {
	private static BulkImportModel model = null;
	
	private final ObjectModel objectModel;
	
	private BulkImportModel(){
		objectModel = new ObjectModel();
	}
	
	public ObjectModel getObjectModel(){
		return objectModel;
	}
	
	public static BulkImportModel getInstance(){
		if(model == null){
			model = new BulkImportModel();
		}
		return model;
	}
}
