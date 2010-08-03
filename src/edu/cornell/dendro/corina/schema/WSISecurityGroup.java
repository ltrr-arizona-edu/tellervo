
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isActive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "securityGroup")
public class WSISecurityGroup
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "isActive")
    protected Boolean isActive;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description!= null);
    }

    /**
     * Gets the value of the isActive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the value of the isActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    public boolean isSetIsActive() {
        return (this.isActive!= null);
    }

    public void unsetIsActive() {
        this.isActive = null;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSISecurityGroup)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSISecurityGroup that = ((WSISecurityGroup) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getName(), that.getName());
        equalsBuilder.append(this.getDescription(), that.getDescription());
        equalsBuilder.append(this.isIsActive(), that.isIsActive());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSISecurityGroup)) {
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
        hashCodeBuilder.append(this.getId());
        hashCodeBuilder.append(this.getName());
        hashCodeBuilder.append(this.getDescription());
        hashCodeBuilder.append(this.isIsActive());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
        {
            String theName;
            theName = this.getName();
            toStringBuilder.append("name", theName);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            toStringBuilder.append("description", theDescription);
        }
        {
            Boolean theIsActive;
            theIsActive = this.isIsActive();
            toStringBuilder.append("isActive", theIsActive);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSISecurityGroup copy = ((target == null)?((WSISecurityGroup) createCopy()):((WSISecurityGroup) target));
        if (this.isSetId()) {
            String sourceId;
            sourceId = this.getId();
            String copyId = ((String) copyBuilder.copy(sourceId));
            copy.setId(copyId);
        } else {
            copy.id = null;
        }
        if (this.isSetName()) {
            String sourceName;
            sourceName = this.getName();
            String copyName = ((String) copyBuilder.copy(sourceName));
            copy.setName(copyName);
        } else {
            copy.name = null;
        }
        if (this.isSetDescription()) {
            String sourceDescription;
            sourceDescription = this.getDescription();
            String copyDescription = ((String) copyBuilder.copy(sourceDescription));
            copy.setDescription(copyDescription);
        } else {
            copy.description = null;
        }
        if (this.isSetIsActive()) {
            Boolean sourceIsActive;
            sourceIsActive = this.isIsActive();
            Boolean copyIsActive = ((Boolean) copyBuilder.copy(sourceIsActive));
            copy.setIsActive(copyIsActive);
        } else {
            copy.unsetIsActive();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSISecurityGroup();
    }

}
