package org.tridas.interfaces;

import java.util.List;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasLinkSeries;

public interface ITridasDerivedSeries extends ITridasSeries {
    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledVoc }
     *     
     */
    public ControlledVoc getType();

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setType(ControlledVoc value);

    public boolean isSetType();
    
    /**
     * Gets the value of the linkSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkSeries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasLinkSeries }
     * 
     * 
     */
    public List<TridasLinkSeries> getLinkSeries();

    public boolean isSetLinkSeries();

    public void unsetLinkSeries();
}
