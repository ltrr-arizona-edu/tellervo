
package net.opengis.gml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElementRef(name = "_GeometricAggregate", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractGeometricAggregateType> geometricAggregate;

    /**
     * Gets the value of the _GeometricAggregate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MultiPointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiSurfaceType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link MultiPointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MultiSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractGeometricAggregateType }{@code >}
     *     
     */
    public void set_GeometricAggregate(JAXBElement<? extends AbstractGeometricAggregateType> value) {
        this.geometricAggregate = ((JAXBElement<? extends AbstractGeometricAggregateType> ) value);
    }

    public boolean isSet_GeometricAggregate() {
        return (this.geometricAggregate!= null);
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
            JAXBElement<? extends AbstractGeometricAggregateType> the_GeometricAggregate;
            the_GeometricAggregate = this.get_GeometricAggregate();
            strategy.appendField(locator, this, "geometricAggregate", buffer, the_GeometricAggregate);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiGeometryPropertyType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final MultiGeometryPropertyType that = ((MultiGeometryPropertyType) object);
        {
            JAXBElement<? extends AbstractGeometricAggregateType> lhs_GeometricAggregate;
            lhs_GeometricAggregate = this.get_GeometricAggregate();
            JAXBElement<? extends AbstractGeometricAggregateType> rhs_GeometricAggregate;
            rhs_GeometricAggregate = that.get_GeometricAggregate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "geometricAggregate", lhs_GeometricAggregate), LocatorUtils.property(thatLocator, "geometricAggregate", rhs_GeometricAggregate), lhs_GeometricAggregate, rhs_GeometricAggregate)) {
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
            JAXBElement<? extends AbstractGeometricAggregateType> the_GeometricAggregate;
            the_GeometricAggregate = this.get_GeometricAggregate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "geometricAggregate", the_GeometricAggregate), currentHashCode, the_GeometricAggregate);
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
        if (draftCopy instanceof MultiGeometryPropertyType) {
            final MultiGeometryPropertyType copy = ((MultiGeometryPropertyType) draftCopy);
            if (this.isSet_GeometricAggregate()) {
                JAXBElement<? extends AbstractGeometricAggregateType> source_GeometricAggregate;
                source_GeometricAggregate = this.get_GeometricAggregate();
                @SuppressWarnings("unchecked")
                JAXBElement<? extends AbstractGeometricAggregateType> copy_GeometricAggregate = ((JAXBElement<? extends AbstractGeometricAggregateType> ) strategy.copy(LocatorUtils.property(locator, "geometricAggregate", source_GeometricAggregate), source_GeometricAggregate));
                copy.set_GeometricAggregate(copy_GeometricAggregate);
            } else {
                copy.geometricAggregate = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiGeometryPropertyType();
    }

}
