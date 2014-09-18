
package net.opengis.gml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            MultiCurveType theMultiCurve;
            theMultiCurve = this.getMultiCurve();
            strategy.appendField(locator, this, "multiCurve", buffer, theMultiCurve);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiCurvePropertyType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final MultiCurvePropertyType that = ((MultiCurvePropertyType) object);
        {
            MultiCurveType lhsMultiCurve;
            lhsMultiCurve = this.getMultiCurve();
            MultiCurveType rhsMultiCurve;
            rhsMultiCurve = that.getMultiCurve();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "multiCurve", lhsMultiCurve), LocatorUtils.property(thatLocator, "multiCurve", rhsMultiCurve), lhsMultiCurve, rhsMultiCurve)) {
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
            MultiCurveType theMultiCurve;
            theMultiCurve = this.getMultiCurve();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "multiCurve", theMultiCurve), currentHashCode, theMultiCurve);
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
        if (draftCopy instanceof MultiCurvePropertyType) {
            final MultiCurvePropertyType copy = ((MultiCurvePropertyType) draftCopy);
            if (this.isSetMultiCurve()) {
                MultiCurveType sourceMultiCurve;
                sourceMultiCurve = this.getMultiCurve();
                MultiCurveType copyMultiCurve = ((MultiCurveType) strategy.copy(LocatorUtils.property(locator, "multiCurve", sourceMultiCurve), sourceMultiCurve));
                copy.setMultiCurve(copyMultiCurve);
            } else {
                copy.multiCurve = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiCurvePropertyType();
    }

}
