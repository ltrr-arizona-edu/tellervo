
package org.tellervo.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.tridas.schema.ControlledVoc;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="region" type="{http://www.tridas.org/1.2.2}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "regions"
})
@XmlRootElement(name = "regionDictionary")
public class WSIRegionDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "region")
    protected List<ControlledVoc> regions;

    /**
     * Gets the value of the regions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getRegions() {
        if (regions == null) {
            regions = new ArrayList<ControlledVoc>();
        }
        return this.regions;
    }

    public boolean isSetRegions() {
        return ((this.regions!= null)&&(!this.regions.isEmpty()));
    }

    public void unsetRegions() {
        this.regions = null;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            List<ControlledVoc> theRegions;
            theRegions = (this.isSetRegions()?this.getRegions():null);
            strategy.appendField(locator, this, "regions", buffer, theRegions);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIRegionDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIRegionDictionary that = ((WSIRegionDictionary) object);
        {
            List<ControlledVoc> lhsRegions;
            lhsRegions = (this.isSetRegions()?this.getRegions():null);
            List<ControlledVoc> rhsRegions;
            rhsRegions = (that.isSetRegions()?that.getRegions():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "regions", lhsRegions), LocatorUtils.property(thatLocator, "regions", rhsRegions), lhsRegions, rhsRegions)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            List<ControlledVoc> theRegions;
            theRegions = (this.isSetRegions()?this.getRegions():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "regions", theRegions), currentHashCode, theRegions);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof WSIRegionDictionary) {
            final WSIRegionDictionary copy = ((WSIRegionDictionary) draftCopy);
            if (this.isSetRegions()) {
                List<ControlledVoc> sourceRegions;
                sourceRegions = (this.isSetRegions()?this.getRegions():null);
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyRegions = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "regions", sourceRegions), sourceRegions));
                copy.unsetRegions();
                List<ControlledVoc> uniqueRegionsl = copy.getRegions();
                uniqueRegionsl.addAll(copyRegions);
            } else {
                copy.unsetRegions();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIRegionDictionary();
    }

    /**
     * Sets the value of the regions property.
     * 
     * @param regions
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setRegions(List<ControlledVoc> regions) {
        this.regions = regions;
    }

}
