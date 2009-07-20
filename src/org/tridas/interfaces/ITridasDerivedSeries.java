package org.tridas.interfaces;

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
     * @return
     *     possible object is
     *     {@link TridasLinkSeries }
     *     
     */
    public TridasLinkSeries getLinkSeries();

    /**
     * Sets the value of the linkSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLinkSeries }
     *     
     */
    public void setLinkSeries(TridasLinkSeries value);

    public boolean isSetLinkSeries();
}
