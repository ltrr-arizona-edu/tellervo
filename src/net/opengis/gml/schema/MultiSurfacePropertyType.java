
package net.opengis.gml.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 * 
 *             A property that has a collection of surfaces as its value
 *             domain shall contain an appropriate geometry element
 *             encapsulated in an element of this type.
 *          
 * 
 * <p>Java class for MultiSurfacePropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiSurfacePropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}MultiSurface"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiSurfacePropertyType", propOrder = {
    "multiSurface"
})
public class MultiSurfacePropertyType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "MultiSurface", required = true)
    protected MultiSurfaceType multiSurface;

    /**
     * Gets the value of the multiSurface property.
     * 
     * @return
     *     possible object is
     *     {@link MultiSurfaceType }
     *     
     */
    public MultiSurfaceType getMultiSurface() {
        return multiSurface;
    }

    /**
     * Sets the value of the multiSurface property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiSurfaceType }
     *     
     */
    public void setMultiSurface(MultiSurfaceType value) {
        this.multiSurface = value;
    }

    public boolean isSetMultiSurface() {
        return (this.multiSurface!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MultiSurfacePropertyType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final MultiSurfacePropertyType that = ((MultiSurfacePropertyType) object);
        equalsBuilder.append(this.getMultiSurface(), that.getMultiSurface());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MultiSurfacePropertyType)) {
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
        hashCodeBuilder.append(this.getMultiSurface());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            MultiSurfaceType theMultiSurface;
            theMultiSurface = this.getMultiSurface();
            toStringBuilder.append("multiSurface", theMultiSurface);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MultiSurfacePropertyType copy = ((target == null)?((MultiSurfacePropertyType) createCopy()):((MultiSurfacePropertyType) target));
        if (this.isSetMultiSurface()) {
            MultiSurfaceType sourceMultiSurface;
            sourceMultiSurface = this.getMultiSurface();
            MultiSurfaceType copyMultiSurface = ((MultiSurfaceType) copyBuilder.copy(sourceMultiSurface));
            copy.setMultiSurface(copyMultiSurface);
        } else {
            copy.multiSurface = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MultiSurfacePropertyType();
    }

}
