
package net.opengis.gml.schema;

import java.io.Serializable;
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElementRef(name = "_Surface", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSurfaceType> surface;

    /**
     * Gets the value of the _Surface property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PolygonType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link SurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSurfaceType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PolygonType }{@code >}
     *     
     */
    public void set_Surface(JAXBElement<? extends AbstractSurfaceType> value) {
        this.surface = ((JAXBElement<? extends AbstractSurfaceType> ) value);
    }

    public boolean isSet_Surface() {
        return (this.surface!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SurfaceMember)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SurfaceMember that = ((SurfaceMember) object);
        equalsBuilder.append(this.get_Surface(), that.get_Surface());
    }

    public boolean equals(Object object) {
        if (!(object instanceof SurfaceMember)) {
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
        hashCodeBuilder.append(this.get_Surface());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            JAXBElement<? extends AbstractSurfaceType> the_Surface;
            the_Surface = this.get_Surface();
            toStringBuilder.append("surface", the_Surface);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final SurfaceMember copy = ((target == null)?((SurfaceMember) createCopy()):((SurfaceMember) target));
        if (this.isSet_Surface()) {
            JAXBElement<? extends AbstractSurfaceType> source_Surface;
            source_Surface = this.get_Surface();
            @SuppressWarnings("unchecked")
            JAXBElement<? extends AbstractSurfaceType> copy_Surface = ((JAXBElement<? extends AbstractSurfaceType> ) copyBuilder.copy(source_Surface));
            copy.set_Surface(copy_Surface);
        } else {
            copy.surface = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new SurfaceMember();
    }

}
