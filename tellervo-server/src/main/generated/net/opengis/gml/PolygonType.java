
package net.opengis.gml;

import java.util.ArrayList;
import java.util.List;
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
 *             A Polygon is a special surface that is defined by a single
 *             surface patch. The boundary of this patch is coplanar and
 *             the polygon uses planar interpolation in its interior. It
 *             is backwards compatible with the Polygon of GML 2.
 *          
 * 
 * <p>Java class for PolygonType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolygonType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractSurfaceType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}exterior" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}interior" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolygonType", propOrder = {
    "exterior",
    "interiors"
})
public class PolygonType
    extends AbstractSurfaceType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected AbstractRingPropertyType exterior;
    @XmlElement(name = "interior")
    protected List<AbstractRingPropertyType> interiors;

    /**
     * Gets the value of the exterior property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractRingPropertyType }
     *     
     */
    public AbstractRingPropertyType getExterior() {
        return exterior;
    }

    /**
     * Sets the value of the exterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractRingPropertyType }
     *     
     */
    public void setExterior(AbstractRingPropertyType value) {
        this.exterior = value;
    }

    public boolean isSetExterior() {
        return (this.exterior!= null);
    }

    /**
     * Gets the value of the interiors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interiors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInteriors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractRingPropertyType }
     * 
     * 
     */
    public List<AbstractRingPropertyType> getInteriors() {
        if (interiors == null) {
            interiors = new ArrayList<AbstractRingPropertyType>();
        }
        return this.interiors;
    }

    public boolean isSetInteriors() {
        return ((this.interiors!= null)&&(!this.interiors.isEmpty()));
    }

    public void unsetInteriors() {
        this.interiors = null;
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
            AbstractRingPropertyType theExterior;
            theExterior = this.getExterior();
            strategy.appendField(locator, this, "exterior", buffer, theExterior);
        }
        {
            List<AbstractRingPropertyType> theInteriors;
            theInteriors = (this.isSetInteriors()?this.getInteriors():null);
            strategy.appendField(locator, this, "interiors", buffer, theInteriors);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof PolygonType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final PolygonType that = ((PolygonType) object);
        {
            AbstractRingPropertyType lhsExterior;
            lhsExterior = this.getExterior();
            AbstractRingPropertyType rhsExterior;
            rhsExterior = that.getExterior();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "exterior", lhsExterior), LocatorUtils.property(thatLocator, "exterior", rhsExterior), lhsExterior, rhsExterior)) {
                return false;
            }
        }
        {
            List<AbstractRingPropertyType> lhsInteriors;
            lhsInteriors = (this.isSetInteriors()?this.getInteriors():null);
            List<AbstractRingPropertyType> rhsInteriors;
            rhsInteriors = (that.isSetInteriors()?that.getInteriors():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "interiors", lhsInteriors), LocatorUtils.property(thatLocator, "interiors", rhsInteriors), lhsInteriors, rhsInteriors)) {
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
            AbstractRingPropertyType theExterior;
            theExterior = this.getExterior();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "exterior", theExterior), currentHashCode, theExterior);
        }
        {
            List<AbstractRingPropertyType> theInteriors;
            theInteriors = (this.isSetInteriors()?this.getInteriors():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "interiors", theInteriors), currentHashCode, theInteriors);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof PolygonType) {
            final PolygonType copy = ((PolygonType) draftCopy);
            if (this.isSetExterior()) {
                AbstractRingPropertyType sourceExterior;
                sourceExterior = this.getExterior();
                AbstractRingPropertyType copyExterior = ((AbstractRingPropertyType) strategy.copy(LocatorUtils.property(locator, "exterior", sourceExterior), sourceExterior));
                copy.setExterior(copyExterior);
            } else {
                copy.exterior = null;
            }
            if (this.isSetInteriors()) {
                List<AbstractRingPropertyType> sourceInteriors;
                sourceInteriors = (this.isSetInteriors()?this.getInteriors():null);
                @SuppressWarnings("unchecked")
                List<AbstractRingPropertyType> copyInteriors = ((List<AbstractRingPropertyType> ) strategy.copy(LocatorUtils.property(locator, "interiors", sourceInteriors), sourceInteriors));
                copy.unsetInteriors();
                List<AbstractRingPropertyType> uniqueInteriorsl = copy.getInteriors();
                uniqueInteriorsl.addAll(copyInteriors);
            } else {
                copy.unsetInteriors();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new PolygonType();
    }

    /**
     * Sets the value of the interiors property.
     * 
     * @param interiors
     *     allowed object is
     *     {@link AbstractRingPropertyType }
     *     
     */
    public void setInteriors(List<AbstractRingPropertyType> interiors) {
        this.interiors = interiors;
    }

}
