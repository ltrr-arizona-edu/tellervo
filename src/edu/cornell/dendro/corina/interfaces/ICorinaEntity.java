package edu.cornell.dendro.corina.interfaces;

import org.jvnet.jaxb2_commons.lang.Copyable;
import org.tridas.schema.TridasIdentifier;


public interface ICorinaEntity extends Copyable {


    /**
     * Sets the value of the id property.
     * 
     * @param value     
     */
    public void setId(String value);
    
    public String getID();
}
