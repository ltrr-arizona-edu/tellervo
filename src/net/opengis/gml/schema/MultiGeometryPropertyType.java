
package net.opengis.gml.schema;

import java.io.Serializable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *             A property that has a geometric aggregate as its value domain
 *             shall contain an appropriate geometry element encapsulated in
 *             an element of this type.
 *          
 * 
 * <p>Java class for MultiGeometryPropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiGeometryPropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}_GeometricAggregate"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiGeometryPropertyType", propOrder = {
    "geometricAggregate"
})
public class MultiGeometryPropertyType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElementRef(name = "_GeometricAggregate", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractGeometricAggregateType> geometricAggregate;

    /**
     * Gets the value of the _GeometricAggregate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MultiSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiPointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGeometricAggregateType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractGeometricAggregateType> get_GeometricAggregate() {
        return geometricAggregate;
    }

    /**
     * Sets the value of the _GeometricAggregate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MultiSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiPointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGeometricAggregateType }{@code >}
     *     
     */
    public void set_GeometricAggregate(JAXBElement<? extends AbstractGeometricAggregateType> value) {
        this.geometricAggregate = ((JAXBElement<? extends AbstractGeometricAggregateType> ) value);
    }

    public boolean isSet_GeometricAggregate() {
        return (this.geometricAggregate!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MultiGeometryPropertyType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final MultiGeometryPropertyType that = ((MultiGeometryPropertyType) object);
        equalsBuilder.append(this.get_GeometricAggregate(), that.get_GeometricAggregate());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MultiGeometryPropertyType)) {
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
        hashCodeBuilder.append(this.get_GeometricAggregate());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            JAXBElement<? extends AbstractGeometricAggregateType> the_GeometricAggregate;
            the_GeometricAggregate = this.get_GeometricAggregate();
            toStringBuilder.append("geometricAggregate", the_GeometricAggregate);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MultiGeometryPropertyType copy = ((target == null)?((MultiGeometryPropertyType) createCopy()):((MultiGeometryPropertyType) target));
        {
            JAXBElement<? extends AbstractGeometricAggregateType> source_GeometricAggregate;
            source_GeometricAggregate = this.get_GeometricAggregate();
            JAXBElement<? extends AbstractGeometricAggregateType> copy_GeometricAggregate = ((JAXBElement<? extends AbstractGeometricAggregateType> ) copyBuilder.copy(source_GeometricAggregate));
            copy.set_GeometricAggregate(copy_GeometricAggregate);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MultiGeometryPropertyType();
    }

}
