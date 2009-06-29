
package net.opengis.gml.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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
     * 
     *                         This element encapsulates the patches of the
     *                         surface.
     *                      
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SurfaceType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final SurfaceType that = ((SurfaceType) object);
        equalsBuilder.append(this.getPatches(), that.getPatches());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SurfaceType)) {
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
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getPatches());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            Patches thePatches;
            thePatches = this.getPatches();
            toStringBuilder.append("patches", thePatches);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SurfaceType copy = ((target == null)?((SurfaceType) createCopy()):((SurfaceType) target));
        super.copyTo(copy, copyBuilder);
        {
            Patches sourcePatches;
            sourcePatches = this.getPatches();
            Patches copyPatches = ((Patches) copyBuilder.copy(sourcePatches));
            copy.setPatches(copyPatches);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SurfaceType();
    }

}
