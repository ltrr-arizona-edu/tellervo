
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
 *             A property that has a surface as its value domain shall contain 
 *             an appropriate geometry element encapsulated in an element
 *             of this type.
 *          
 * 
 * <p>Java class for SurfacePropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SurfacePropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}_Surface"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurfacePropertyType", propOrder = {
    "surface"
})
@XmlRootElement(name = "surfaceMember")
public class SurfaceMember
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElementRef(name = "_Surface", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSurfaceType> surface;

    /**
     * Gets the value of the _Surface property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PolygonType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SurfaceType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractSurfaceType> get_Surface() {
        return surface;
    }

    /**
     * Sets the value of the _Surface property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PolygonType }{@code >}
     *     {@link JAXBElement }{@code <}{@link SurfaceType }{@code >}
     *     
     */
    public void set_Surface(JAXBElement<? extends AbstractSurfaceType> value) {
        this.surface = ((JAXBElement<? extends AbstractSurfaceType> ) value);
    }

    public boolean isSet_Surface() {
        return (this.surface!= null);
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
            JAXBElement<? extends AbstractSurfaceType> the_Surface;
            the_Surface = this.get_Surface();
            strategy.appendField(locator, this, "surface", buffer, the_Surface);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SurfaceMember)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SurfaceMember that = ((SurfaceMember) object);
        {
            JAXBElement<? extends AbstractSurfaceType> lhs_Surface;
            lhs_Surface = this.get_Surface();
            JAXBElement<? extends AbstractSurfaceType> rhs_Surface;
            rhs_Surface = that.get_Surface();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "surface", lhs_Surface), LocatorUtils.property(thatLocator, "surface", rhs_Surface), lhs_Surface, rhs_Surface)) {
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
            JAXBElement<? extends AbstractSurfaceType> the_Surface;
            the_Surface = this.get_Surface();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "surface", the_Surface), currentHashCode, the_Surface);
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
        if (draftCopy instanceof SurfaceMember) {
            final SurfaceMember copy = ((SurfaceMember) draftCopy);
            if (this.isSet_Surface()) {
                JAXBElement<? extends AbstractSurfaceType> source_Surface;
                source_Surface = this.get_Surface();
                @SuppressWarnings("unchecked")
                JAXBElement<? extends AbstractSurfaceType> copy_Surface = ((JAXBElement<? extends AbstractSurfaceType> ) strategy.copy(LocatorUtils.property(locator, "surface", source_Surface), source_Surface));
                copy.set_Surface(copy_Surface);
            } else {
                copy.surface = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new SurfaceMember();
    }

}
