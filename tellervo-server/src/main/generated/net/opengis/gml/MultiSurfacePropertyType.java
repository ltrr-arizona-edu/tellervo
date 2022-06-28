
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            MultiSurfaceType theMultiSurface;
            theMultiSurface = this.getMultiSurface();
            strategy.appendField(locator, this, "multiSurface", buffer, theMultiSurface);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiSurfacePropertyType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final MultiSurfacePropertyType that = ((MultiSurfacePropertyType) object);
        {
            MultiSurfaceType lhsMultiSurface;
            lhsMultiSurface = this.getMultiSurface();
            MultiSurfaceType rhsMultiSurface;
            rhsMultiSurface = that.getMultiSurface();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "multiSurface", lhsMultiSurface), LocatorUtils.property(thatLocator, "multiSurface", rhsMultiSurface), lhsMultiSurface, rhsMultiSurface)) {
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
            MultiSurfaceType theMultiSurface;
            theMultiSurface = this.getMultiSurface();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "multiSurface", theMultiSurface), currentHashCode, theMultiSurface);
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
        if (draftCopy instanceof MultiSurfacePropertyType) {
            final MultiSurfacePropertyType copy = ((MultiSurfacePropertyType) draftCopy);
            if (this.isSetMultiSurface()) {
                MultiSurfaceType sourceMultiSurface;
                sourceMultiSurface = this.getMultiSurface();
                MultiSurfaceType copyMultiSurface = ((MultiSurfaceType) strategy.copy(LocatorUtils.property(locator, "multiSurface", sourceMultiSurface), sourceMultiSurface));
                copy.setMultiSurface(copyMultiSurface);
            } else {
                copy.multiSurface = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiSurfacePropertyType();
    }

}
