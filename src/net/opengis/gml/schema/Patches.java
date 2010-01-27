
package net.opengis.gml.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;


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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Patches)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Patches that = ((Patches) object);
        equalsBuilder.append(this.get_SurfacePatches(), that.get_SurfacePatches());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Patches)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.get_SurfacePatches());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<JAXBElement<? extends AbstractSurfacePatchType>> the_SurfacePatches;
            the_SurfacePatches = this.get_SurfacePatches();
            toStringBuilder.append("surfacePatches", the_SurfacePatches);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final Patches copy = ((target == null)?((Patches) createCopy()):((Patches) target));
        if (this.isSet_SurfacePatches()) {
            List<JAXBElement<? extends AbstractSurfacePatchType>> source_SurfacePatches;
            source_SurfacePatches = this.get_SurfacePatches();
            @SuppressWarnings("unchecked")
            List<JAXBElement<? extends AbstractSurfacePatchType>> copy_SurfacePatches = ((List<JAXBElement<? extends AbstractSurfacePatchType>> ) copyBuilder.copy(source_SurfacePatches));
            copy.set_SurfacePatches(copy_SurfacePatches);
        } else {
            copy.unset_SurfacePatches();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new Patches();
    }

}
