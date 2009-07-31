
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
 *             A property that has a collection of curves as its value domain
 *             shall contain an appropriate geometry element encapsulated in
 *             an element of this type.
 *          
 * 
 * <p>Java class for MultiCurvePropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiCurvePropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}MultiCurve"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiCurvePropertyType", propOrder = {
    "multiCurve"
})
public class MultiCurvePropertyType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "MultiCurve", required = true)
    protected MultiCurveType multiCurve;

    /**
     * Gets the value of the multiCurve property.
     * 
     * @return
     *     possible object is
     *     {@link MultiCurveType }
     *     
     */
    public MultiCurveType getMultiCurve() {
        return multiCurve;
    }

    /**
     * Sets the value of the multiCurve property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiCurveType }
     *     
     */
    public void setMultiCurve(MultiCurveType value) {
        this.multiCurve = value;
    }

    public boolean isSetMultiCurve() {
        return (this.multiCurve!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MultiCurvePropertyType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final MultiCurvePropertyType that = ((MultiCurvePropertyType) object);
        equalsBuilder.append(this.getMultiCurve(), that.getMultiCurve());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MultiCurvePropertyType)) {
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
        hashCodeBuilder.append(this.getMultiCurve());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            MultiCurveType theMultiCurve;
            theMultiCurve = this.getMultiCurve();
            toStringBuilder.append("multiCurve", theMultiCurve);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MultiCurvePropertyType copy = ((target == null)?((MultiCurvePropertyType) createCopy()):((MultiCurvePropertyType) target));
        if (this.isSetMultiCurve()) {
            MultiCurveType sourceMultiCurve;
            sourceMultiCurve = this.getMultiCurve();
            MultiCurveType copyMultiCurve = ((MultiCurveType) copyBuilder.copy(sourceMultiCurve));
            copy.setMultiCurve(copyMultiCurve);
        } else {
            copy.multiCurve = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MultiCurvePropertyType();
    }

}
