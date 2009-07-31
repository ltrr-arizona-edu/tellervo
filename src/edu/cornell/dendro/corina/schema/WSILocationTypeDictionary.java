
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
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
 *         &lt;element name="locationType" type="{http://www.tridas.org/1.3}controlledVoc" maxOccurs="unbounded" minOccurs="0"/>
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
    "locationTypes"
})
@XmlRootElement(name = "locationTypeDictionary")
public class WSILocationTypeDictionary implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "locationType")
    protected List<ControlledVoc> locationTypes;

    /**
     * Gets the value of the locationTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getLocationTypes() {
        if (locationTypes == null) {
            locationTypes = new ArrayList<ControlledVoc>();
        }
        return this.locationTypes;
    }

    public boolean isSetLocationTypes() {
        return ((this.locationTypes!= null)&&(!this.locationTypes.isEmpty()));
    }

    public void unsetLocationTypes() {
        this.locationTypes = null;
    }

    /**
     * Sets the value of the locationTypes property.
     * 
     * @param locationTypes
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setLocationTypes(List<ControlledVoc> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSILocationTypeDictionary)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSILocationTypeDictionary that = ((WSILocationTypeDictionary) object);
        equalsBuilder.append(this.getLocationTypes(), that.getLocationTypes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSILocationTypeDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getLocationTypes());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<ControlledVoc> theLocationTypes;
            theLocationTypes = this.getLocationTypes();
            toStringBuilder.append("locationTypes", theLocationTypes);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSILocationTypeDictionary copy = ((target == null)?((WSILocationTypeDictionary) createCopy()):((WSILocationTypeDictionary) target));
        if (this.isSetLocationTypes()) {
            List<ControlledVoc> sourceLocationTypes;
            sourceLocationTypes = this.getLocationTypes();
            @SuppressWarnings("unchecked")
            List<ControlledVoc> copyLocationTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceLocationTypes));
            copy.setLocationTypes(copyLocationTypes);
        } else {
            copy.unsetLocationTypes();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSILocationTypeDictionary();
    }

}
