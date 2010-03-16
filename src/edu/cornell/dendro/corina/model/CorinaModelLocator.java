package edu.cornell.dendro.corina.model;

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
}
