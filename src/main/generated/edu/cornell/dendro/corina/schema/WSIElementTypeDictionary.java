
package edu.cornell.dendro.corina.schema;

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
 *         &lt;element name="elementType" type="{http://www.tridas.org/1.2.2}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
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
    "elementTypes"
})
@XmlRootElement(name = "elementTypeDictionary")
public class WSIElementTypeDictionary implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "elementType")
    protected List<ControlledVoc> elementTypes;

    /**
     * Gets the value of the elementTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elementTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElementTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getElementTypes() {
        if (elementTypes == null) {
            elementTypes = new ArrayList<ControlledVoc>();
        }
        return this.elementTypes;
    }

    public boolean isSetElementTypes() {
        return ((this.elementTypes!= null)&&(!this.elementTypes.isEmpty()));
    }

    public void unsetElementTypes() {
        this.elementTypes = null;
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
            List<ControlledVoc> theElementTypes;
            theElementTypes = (this.isSetElementTypes()?this.getElementTypes():null);
            strategy.appendField(locator, this, "elementTypes", buffer, theElementTypes);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIElementTypeDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIElementTypeDictionary that = ((WSIElementTypeDictionary) object);
        {
            List<ControlledVoc> lhsElementTypes;
            lhsElementTypes = (this.isSetElementTypes()?this.getElementTypes():null);
            List<ControlledVoc> rhsElementTypes;
            rhsElementTypes = (that.isSetElementTypes()?that.getElementTypes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elementTypes", lhsElementTypes), LocatorUtils.property(thatLocator, "elementTypes", rhsElementTypes), lhsElementTypes, rhsElementTypes)) {
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
            List<ControlledVoc> theElementTypes;
            theElementTypes = (this.isSetElementTypes()?this.getElementTypes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elementTypes", theElementTypes), currentHashCode, theElementTypes);
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
        if (draftCopy instanceof WSIElementTypeDictionary) {
            final WSIElementTypeDictionary copy = ((WSIElementTypeDictionary) draftCopy);
            if (this.isSetElementTypes()) {
                List<ControlledVoc> sourceElementTypes;
                sourceElementTypes = (this.isSetElementTypes()?this.getElementTypes():null);
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyElementTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "elementTypes", sourceElementTypes), sourceElementTypes));
                copy.unsetElementTypes();
                List<ControlledVoc> uniqueElementTypesl = copy.getElementTypes();
                uniqueElementTypesl.addAll(copyElementTypes);
            } else {
                copy.unsetElementTypes();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIElementTypeDictionary();
    }

    /**
     * Sets the value of the elementTypes property.
     * 
     * @param elementTypes
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setElementTypes(List<ControlledVoc> elementTypes) {
        this.elementTypes = elementTypes;
    }

}
