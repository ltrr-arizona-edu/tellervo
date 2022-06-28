
package net.opengis.gml;

import java.util.ArrayList;
import java.util.List;
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
 *             A container for an array of surface patches.
 *          
 * 
 * <p>Java class for SurfacePatchArrayPropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SurfacePatchArrayPropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}_SurfacePatch" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SurfacePatchArrayPropertyType", propOrder = {
    "surfacePatches"
})
@XmlRootElement(name = "patches")
public class Patches
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElementRef(name = "_SurfacePatch", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractSurfacePatchType>> surfacePatches;

    /**
     * Gets the value of the surfacePatches property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the surfacePatches property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    get_SurfacePatches().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractSurfacePatchType }{@code >}
     * {@link JAXBElement }{@code <}{@link PolygonPatchType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractSurfacePatchType>> get_SurfacePatches() {
        if (surfacePatches == null) {
            surfacePatches = new ArrayList<JAXBElement<? extends AbstractSurfacePatchType>>();
        }
        return this.surfacePatches;
    }

    public boolean isSet_SurfacePatches() {
        return ((this.surfacePatches!= null)&&(!this.surfacePatches.isEmpty()));
    }

    public void unset_SurfacePatches() {
        this.surfacePatches = null;
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
            List<JAXBElement<? extends AbstractSurfacePatchType>> the_SurfacePatches;
            the_SurfacePatches = (this.isSet_SurfacePatches()?this.get_SurfacePatches():null);
            strategy.appendField(locator, this, "surfacePatches", buffer, the_SurfacePatches);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Patches)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Patches that = ((Patches) object);
        {
            List<JAXBElement<? extends AbstractSurfacePatchType>> lhs_SurfacePatches;
            lhs_SurfacePatches = (this.isSet_SurfacePatches()?this.get_SurfacePatches():null);
            List<JAXBElement<? extends AbstractSurfacePatchType>> rhs_SurfacePatches;
            rhs_SurfacePatches = (that.isSet_SurfacePatches()?that.get_SurfacePatches():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "surfacePatches", lhs_SurfacePatches), LocatorUtils.property(thatLocator, "surfacePatches", rhs_SurfacePatches), lhs_SurfacePatches, rhs_SurfacePatches)) {
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
            List<JAXBElement<? extends AbstractSurfacePatchType>> the_SurfacePatches;
            the_SurfacePatches = (this.isSet_SurfacePatches()?this.get_SurfacePatches():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "surfacePatches", the_SurfacePatches), currentHashCode, the_SurfacePatches);
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
        if (draftCopy instanceof Patches) {
            final Patches copy = ((Patches) draftCopy);
            if (this.isSet_SurfacePatches()) {
                List<JAXBElement<? extends AbstractSurfacePatchType>> source_SurfacePatches;
                source_SurfacePatches = (this.isSet_SurfacePatches()?this.get_SurfacePatches():null);
                @SuppressWarnings("unchecked")
                List<JAXBElement<? extends AbstractSurfacePatchType>> copy_SurfacePatches = ((List<JAXBElement<? extends AbstractSurfacePatchType>> ) strategy.copy(LocatorUtils.property(locator, "surfacePatches", source_SurfacePatches), source_SurfacePatches));
                copy.unset_SurfacePatches();
                List<JAXBElement<? extends AbstractSurfacePatchType>> unique_SurfacePatchesl = copy.get_SurfacePatches();
                unique_SurfacePatchesl.addAll(copy_SurfacePatches);
            } else {
                copy.unset_SurfacePatches();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Patches();
    }

    /**
     * Sets the value of the _SurfacePatches property.
     * 
     * @param surfacePatches
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSurfacePatchType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PolygonPatchType }{@code >}
     *     
     */
    public void set_SurfacePatches(List<JAXBElement<? extends AbstractSurfacePatchType>> surfacePatches) {
        this.surfacePatches = surfacePatches;
    }

}
