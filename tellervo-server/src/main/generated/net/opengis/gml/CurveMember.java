
package net.opengis.gml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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


/**
 * 
 *             A property that has a curve as its value domain shall contain
 *             an appropriate geometry element encapsulated in an element
 *             of this type.
 *          
 * 
 * <p>Java class for CurvePropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurvePropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}_Curve"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurvePropertyType", propOrder = {
    "curve"
})
@XmlRootElement(name = "curveMember")
public class CurveMember
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElementRef(name = "_Curve", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractCurveType> curve;

    /**
     * Gets the value of the _Curve property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LineStringType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CurveType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractCurveType> get_Curve() {
        return curve;
    }

    /**
     * Sets the value of the _Curve property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractCurveType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LineStringType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CurveType }{@code >}
     *     
     */
    public void set_Curve(JAXBElement<? extends AbstractCurveType> value) {
        this.curve = ((JAXBElement<? extends AbstractCurveType> ) value);
    }

    public boolean isSet_Curve() {
        return (this.curve!= null);
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
            JAXBElement<? extends AbstractCurveType> the_Curve;
            the_Curve = this.get_Curve();
            strategy.appendField(locator, this, "curve", buffer, the_Curve);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CurveMember)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final CurveMember that = ((CurveMember) object);
        {
            JAXBElement<? extends AbstractCurveType> lhs_Curve;
            lhs_Curve = this.get_Curve();
            JAXBElement<? extends AbstractCurveType> rhs_Curve;
            rhs_Curve = that.get_Curve();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "curve", lhs_Curve), LocatorUtils.property(thatLocator, "curve", rhs_Curve), lhs_Curve, rhs_Curve)) {
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
            JAXBElement<? extends AbstractCurveType> the_Curve;
            the_Curve = this.get_Curve();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "curve", the_Curve), currentHashCode, the_Curve);
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
        if (draftCopy instanceof CurveMember) {
            final CurveMember copy = ((CurveMember) draftCopy);
            if (this.isSet_Curve()) {
                JAXBElement<? extends AbstractCurveType> source_Curve;
                source_Curve = this.get_Curve();
                @SuppressWarnings("unchecked")
                JAXBElement<? extends AbstractCurveType> copy_Curve = ((JAXBElement<? extends AbstractCurveType> ) strategy.copy(LocatorUtils.property(locator, "curve", source_Curve), source_Curve));
                copy.set_Curve(copy_Curve);
            } else {
                copy.curve = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new CurveMember();
    }

}
