
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
 *             A Surface is a 2-dimensional primitive and is composed
 *             of one or more surface patches. The surface patches are
 *             connected to one another.
 *             The orientation of the surface is positive ("up"). The
 *             orientation of a surface chooses an "up" direction
 *             through the choice of the upward normal, which, if the
 *             surface is not a cycle, is the side of the surface from
 *             which the exterior boundary appears counterclockwise.
 *             Reversal of the surface orientation reverses the curve
 *             orientation of each boundary component, and interchanges
 *             the conceptual "up" and "down" direction of the surface.
 *             If the surface is the boundary of a solid, the "up"
 *             direction is usually outward. For closed surfaces, which
 *             have no boundary, the up direction is that of the surface
 *             patches, which must be consistent with one another. Its
 *             included surface patches describe the interior structure
 *             of the Surface.
 *          
 * 
 * <p>Java class for SurfaceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SurfaceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractSurfaceType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}patches"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurfaceType", propOrder = {
    "patches"
})
public class SurfaceType
    extends AbstractSurfaceType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected Patches patches;

    /**
     * 
     *                         This element encapsulates the patches of the
     *                         surface.
     *                      
     * 
     * @return
     *     possible object is
     *     {@link Patches }
     *     
     */
    public Patches getPatches() {
        return patches;
    }

    /**
     * Sets the value of the patches property.
     * 
     * @param value
     *     allowed object is
     *     {@link Patches }
     *     
     */
    public void setPatches(Patches value) {
        this.patches = value;
    }

    public boolean isSetPatches() {
        return (this.patches!= null);
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
            Patches thePatches;
            thePatches = this.getPatches();
            strategy.appendField(locator, this, "patches", buffer, thePatches);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SurfaceType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final SurfaceType that = ((SurfaceType) object);
        {
            Patches lhsPatches;
            lhsPatches = this.getPatches();
            Patches rhsPatches;
            rhsPatches = that.getPatches();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "patches", lhsPatches), LocatorUtils.property(thatLocator, "patches", rhsPatches), lhsPatches, rhsPatches)) {
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
            Patches thePatches;
            thePatches = this.getPatches();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "patches", thePatches), currentHashCode, thePatches);
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
        if (draftCopy instanceof SurfaceType) {
            final SurfaceType copy = ((SurfaceType) draftCopy);
            if (this.isSetPatches()) {
                Patches sourcePatches;
                sourcePatches = this.getPatches();
                Patches copyPatches = ((Patches) strategy.copy(LocatorUtils.property(locator, "patches", sourcePatches), sourcePatches));
                copy.setPatches(copyPatches);
            } else {
                copy.patches = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new SurfaceType();
    }

}
