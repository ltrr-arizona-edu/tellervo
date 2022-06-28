
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
 *             A property that has a collection of points as its value
 *             domain shall contain an appropriate geometry element
 *             encapsulated in an element of this type.
 *          
 * 
 * <p>Java class for MultiPointPropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiPointPropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}MultiPoint"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiPointPropertyType", propOrder = {
    "multiPoint"
})
public class MultiPointPropertyType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "MultiPoint", required = true)
    protected MultiPointType multiPoint;

    /**
     * Gets the value of the multiPoint property.
     * 
     * @return
     *     possible object is
     *     {@link MultiPointType }
     *     
     */
    public MultiPointType getMultiPoint() {
        return multiPoint;
    }

    /**
     * Sets the value of the multiPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiPointType }
     *     
     */
    public void setMultiPoint(MultiPointType value) {
        this.multiPoint = value;
    }

    public boolean isSetMultiPoint() {
        return (this.multiPoint!= null);
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
            MultiPointType theMultiPoint;
            theMultiPoint = this.getMultiPoint();
            strategy.appendField(locator, this, "multiPoint", buffer, theMultiPoint);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiPointPropertyType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final MultiPointPropertyType that = ((MultiPointPropertyType) object);
        {
            MultiPointType lhsMultiPoint;
            lhsMultiPoint = this.getMultiPoint();
            MultiPointType rhsMultiPoint;
            rhsMultiPoint = that.getMultiPoint();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "multiPoint", lhsMultiPoint), LocatorUtils.property(thatLocator, "multiPoint", rhsMultiPoint), lhsMultiPoint, rhsMultiPoint)) {
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
            MultiPointType theMultiPoint;
            theMultiPoint = this.getMultiPoint();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "multiPoint", theMultiPoint), currentHashCode, theMultiPoint);
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
        if (draftCopy instanceof MultiPointPropertyType) {
            final MultiPointPropertyType copy = ((MultiPointPropertyType) draftCopy);
            if (this.isSetMultiPoint()) {
                MultiPointType sourceMultiPoint;
                sourceMultiPoint = this.getMultiPoint();
                MultiPointType copyMultiPoint = ((MultiPointType) strategy.copy(LocatorUtils.property(locator, "multiPoint", sourceMultiPoint), sourceMultiPoint));
                copy.setMultiPoint(copyMultiPoint);
            } else {
                copy.multiPoint = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiPointPropertyType();
    }

}
