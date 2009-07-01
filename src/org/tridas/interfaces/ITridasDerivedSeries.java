package org.tridas.interfaces;

import org.tridas.schema.ControlledVoc;

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
}
