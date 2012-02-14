
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
 *         &lt;element name="datingType" type="{http://www.tridas.org/1.2.2}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
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
    "datingTypes"
})
@XmlRootElement(name = "datingTypeDictionary")
public class WSIDatingTypeDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "datingType")
    protected List<ControlledVoc> datingTypes;

    /**
     * Gets the value of the datingTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datingTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatingTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getDatingTypes() {
        if (datingTypes == null) {
            datingTypes = new ArrayList<ControlledVoc>();
        }
        return this.datingTypes;
    }

    public boolean isSetDatingTypes() {
        return ((this.datingTypes!= null)&&(!this.datingTypes.isEmpty()));
    }

    public void unsetDatingTypes() {
        this.datingTypes = null;
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
            List<ControlledVoc> theDatingTypes;
            theDatingTypes = (this.isSetDatingTypes()?this.getDatingTypes():null);
            strategy.appendField(locator, this, "datingTypes", buffer, theDatingTypes);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIDatingTypeDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIDatingTypeDictionary that = ((WSIDatingTypeDictionary) object);
        {
            List<ControlledVoc> lhsDatingTypes;
            lhsDatingTypes = (this.isSetDatingTypes()?this.getDatingTypes():null);
            List<ControlledVoc> rhsDatingTypes;
            rhsDatingTypes = (that.isSetDatingTypes()?that.getDatingTypes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "datingTypes", lhsDatingTypes), LocatorUtils.property(thatLocator, "datingTypes", rhsDatingTypes), lhsDatingTypes, rhsDatingTypes)) {
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
            List<ControlledVoc> theDatingTypes;
            theDatingTypes = (this.isSetDatingTypes()?this.getDatingTypes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "datingTypes", theDatingTypes), currentHashCode, theDatingTypes);
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
        if (draftCopy instanceof WSIDatingTypeDictionary) {
            final WSIDatingTypeDictionary copy = ((WSIDatingTypeDictionary) draftCopy);
            if (this.isSetDatingTypes()) {
                List<ControlledVoc> sourceDatingTypes;
                sourceDatingTypes = (this.isSetDatingTypes()?this.getDatingTypes():null);
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyDatingTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "datingTypes", sourceDatingTypes), sourceDatingTypes));
                copy.unsetDatingTypes();
                List<ControlledVoc> uniqueDatingTypesl = copy.getDatingTypes();
                uniqueDatingTypesl.addAll(copyDatingTypes);
            } else {
                copy.unsetDatingTypes();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIDatingTypeDictionary();
    }

    /**
     * Sets the value of the datingTypes property.
     * 
     * @param datingTypes
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setDatingTypes(List<ControlledVoc> datingTypes) {
        this.datingTypes = datingTypes;
    }

}
