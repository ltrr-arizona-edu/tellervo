
package net.opengis.gml.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *             A MultiSurface is defined by one or more Surfaces,
 *             referenced through surfaceMember elements.
 *          
 * 
 * <p>Java class for MultiSurfaceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiSurfaceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractGeometricAggregateType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}surfaceMember" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiSurfaceType", propOrder = {
    "surfaceMembers"
})
public class MultiSurfaceType
    extends AbstractGeometricAggregateType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "surfaceMember")
    protected List<SurfaceMember> surfaceMembers;

    /**
     * Gets the value of the surfaceMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the surfaceMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSurfaceMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SurfaceMember }
     * 
     * 
     */
    public List<SurfaceMember> getSurfaceMembers() {
        if (surfaceMembers == null) {
            surfaceMembers = new ArrayList<SurfaceMember>();
        }
        return this.surfaceMembers;
    }

    public boolean isSetSurfaceMembers() {
        return ((this.surfaceMembers!= null)&&(!this.surfaceMembers.isEmpty()));
    }

    public void unsetSurfaceMembers() {
        this.surfaceMembers = null;
    }

    /**
     * Sets the value of the surfaceMembers property.
     * 
     * @param surfaceMembers
     *     allowed object is
     *     {@link SurfaceMember }
     *     
     */
    public void setSurfaceMembers(List<SurfaceMember> surfaceMembers) {
        this.surfaceMembers = surfaceMembers;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MultiSurfaceType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final MultiSurfaceType that = ((MultiSurfaceType) object);
        equalsBuilder.append(this.getSurfaceMembers(), that.getSurfaceMembers());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MultiSurfaceType)) {
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
        hashCodeBuilder.append(this.getSurfaceMembers());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            List<SurfaceMember> theSurfaceMembers;
            theSurfaceMembers = this.getSurfaceMembers();
            toStringBuilder.append("surfaceMembers", theSurfaceMembers);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MultiSurfaceType copy = ((target == null)?((MultiSurfaceType) createCopy()):((MultiSurfaceType) target));
        super.copyTo(copy, copyBuilder);
        if (this.isSetSurfaceMembers()) {
            List<SurfaceMember> sourceSurfaceMembers;
            sourceSurfaceMembers = this.getSurfaceMembers();
            @SuppressWarnings("unchecked")
            List<SurfaceMember> copySurfaceMembers = ((List<SurfaceMember> ) copyBuilder.copy(sourceSurfaceMembers));
            copy.setSurfaceMembers(copySurfaceMembers);
        } else {
            copy.unsetSurfaceMembers();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MultiSurfaceType();
    }

}
