
package org.tridas.schema;

import java.io.Serializable;
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
 *         &lt;element ref="{http://www.tridas.org/1.3}locationGeometry"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}locationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}locationPrecision" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}locationComment" minOccurs="0"/>
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
    "locationGeometry",
    "locationType",
    "locationPrecision",
    "locationComment"
})
@XmlRootElement(name = "location")
public class TridasLocation
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected TridasLocationGeometry locationGeometry;
    protected NormalTridasLocationType locationType;
    protected String locationPrecision;
    protected String locationComment;

    /**
     * Gets the value of the locationGeometry property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLocationGeometry }
     *     
     */
    public TridasLocationGeometry getLocationGeometry() {
        return locationGeometry;
    }

    /**
     * Sets the value of the locationGeometry property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLocationGeometry }
     *     
     */
    public void setLocationGeometry(TridasLocationGeometry value) {
        this.locationGeometry = value;
    }

    public boolean isSetLocationGeometry() {
        return (this.locationGeometry!= null);
    }

    /**
     * Gets the value of the locationType property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasLocationType }
     *     
     */
    public NormalTridasLocationType getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasLocationType }
     *     
     */
    public void setLocationType(NormalTridasLocationType value) {
        this.locationType = value;
    }

    public boolean isSetLocationType() {
        return (this.locationType!= null);
    }

    /**
     * Gets the value of the locationPrecision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationPrecision() {
        return locationPrecision;
    }

    /**
     * Sets the value of the locationPrecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationPrecision(String value) {
        this.locationPrecision = value;
    }

    public boolean isSetLocationPrecision() {
        return (this.locationPrecision!= null);
    }

    /**
     * Gets the value of the locationComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationComment() {
        return locationComment;
    }

    /**
     * Sets the value of the locationComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationComment(String value) {
        this.locationComment = value;
    }

    public boolean isSetLocationComment() {
        return (this.locationComment!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasLocation)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasLocation that = ((TridasLocation) object);
        equalsBuilder.append(this.getLocationGeometry(), that.getLocationGeometry());
        equalsBuilder.append(this.getLocationType(), that.getLocationType());
        equalsBuilder.append(this.getLocationPrecision(), that.getLocationPrecision());
        equalsBuilder.append(this.getLocationComment(), that.getLocationComment());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasLocation)) {
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
        hashCodeBuilder.append(this.getLocationGeometry());
        hashCodeBuilder.append(this.getLocationType());
        hashCodeBuilder.append(this.getLocationPrecision());
        hashCodeBuilder.append(this.getLocationComment());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasLocationGeometry theLocationGeometry;
            theLocationGeometry = this.getLocationGeometry();
            toStringBuilder.append("locationGeometry", theLocationGeometry);
        }
        {
            NormalTridasLocationType theLocationType;
            theLocationType = this.getLocationType();
            toStringBuilder.append("locationType", theLocationType);
        }
        {
            String theLocationPrecision;
            theLocationPrecision = this.getLocationPrecision();
            toStringBuilder.append("locationPrecision", theLocationPrecision);
        }
        {
            String theLocationComment;
            theLocationComment = this.getLocationComment();
            toStringBuilder.append("locationComment", theLocationComment);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasLocation copy = ((target == null)?((TridasLocation) createCopy()):((TridasLocation) target));
        if (this.isSetLocationGeometry()) {
            TridasLocationGeometry sourceLocationGeometry;
            sourceLocationGeometry = this.getLocationGeometry();
            TridasLocationGeometry copyLocationGeometry = ((TridasLocationGeometry) copyBuilder.copy(sourceLocationGeometry));
            copy.setLocationGeometry(copyLocationGeometry);
        } else {
            copy.locationGeometry = null;
        }
        if (this.isSetLocationType()) {
            NormalTridasLocationType sourceLocationType;
            sourceLocationType = this.getLocationType();
            NormalTridasLocationType copyLocationType = ((NormalTridasLocationType) copyBuilder.copy(sourceLocationType));
            copy.setLocationType(copyLocationType);
        } else {
            copy.locationType = null;
        }
        if (this.isSetLocationPrecision()) {
            String sourceLocationPrecision;
            sourceLocationPrecision = this.getLocationPrecision();
            String copyLocationPrecision = ((String) copyBuilder.copy(sourceLocationPrecision));
            copy.setLocationPrecision(copyLocationPrecision);
        } else {
            copy.locationPrecision = null;
        }
        if (this.isSetLocationComment()) {
            String sourceLocationComment;
            sourceLocationComment = this.getLocationComment();
            String copyLocationComment = ((String) copyBuilder.copy(sourceLocationComment));
            copy.setLocationComment(copyLocationComment);
        } else {
            copy.locationComment = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasLocation();
    }

}
