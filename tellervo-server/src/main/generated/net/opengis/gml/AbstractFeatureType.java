
package net.opengis.gml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *             An abstract feature provides a set of common properties,
 *             including id, name and description inherited from
 *             AbstractGMLType, plus boundedBy.  A concrete feature type
 *             must derive from this type and specify additional properties
 *             in an application schema.
 *          
 * 
 * <p>Java class for AbstractFeatureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractFeatureType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractFeatureBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}boundedBy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractFeatureType", propOrder = {
    "boundedBy"
})
public abstract class AbstractFeatureType
    extends AbstractFeatureBaseType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected BoundedBy boundedBy;

    /**
     * Gets the value of the boundedBy property.
     * 
     * @return
     *     possible object is
     *     {@link BoundedBy }
     *     
     */
    public BoundedBy getBoundedBy() {
        return boundedBy;
    }

    /**
     * Sets the value of the boundedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoundedBy }
     *     
     */
    public void setBoundedBy(BoundedBy value) {
        this.boundedBy = value;
    }

    public boolean isSetBoundedBy() {
        return (this.boundedBy!= null);
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
        super.appendFields(locator, buffer, strategy);
        {
            BoundedBy theBoundedBy;
            theBoundedBy = this.getBoundedBy();
            strategy.appendField(locator, this, "boundedBy", buffer, theBoundedBy);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof AbstractFeatureType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final AbstractFeatureType that = ((AbstractFeatureType) object);
        {
            BoundedBy lhsBoundedBy;
            lhsBoundedBy = this.getBoundedBy();
            BoundedBy rhsBoundedBy;
            rhsBoundedBy = that.getBoundedBy();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "boundedBy", lhsBoundedBy), LocatorUtils.property(thatLocator, "boundedBy", rhsBoundedBy), lhsBoundedBy, rhsBoundedBy)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            BoundedBy theBoundedBy;
            theBoundedBy = this.getBoundedBy();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "boundedBy", theBoundedBy), currentHashCode, theBoundedBy);
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
        if (null == target) {
            throw new IllegalArgumentException("Target argument must not be null for abstract copyable classes.");
        }
        super.copyTo(locator, target, strategy);
        if (target instanceof AbstractFeatureType) {
            final AbstractFeatureType copy = ((AbstractFeatureType) target);
            if (this.isSetBoundedBy()) {
                BoundedBy sourceBoundedBy;
                sourceBoundedBy = this.getBoundedBy();
                BoundedBy copyBoundedBy = ((BoundedBy) strategy.copy(LocatorUtils.property(locator, "boundedBy", sourceBoundedBy), sourceBoundedBy));
                copy.setBoundedBy(copyBoundedBy);
            } else {
                copy.boundedBy = null;
            }
        }
        return target;
    }

}
