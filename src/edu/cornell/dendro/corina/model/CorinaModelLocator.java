package edu.cornell.dendro.corina.model;

import edu.cornell.dendro.corina.mvc.model.AbstractModel;

/**
 *
 * @author daniel
 */
public class CorinaModelLocator extends AbstractModel{
	private static final CorinaModelLocator model = new CorinaModelLocator();
	
	private CorinaModelLocator(){
		
	}
	
	
	public static CorinaModelLocator getInstance(){
		return model;
	}

	@Override
	protected void revert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void save() {
		// TODO Auto-generated method stub
		
	}
}
