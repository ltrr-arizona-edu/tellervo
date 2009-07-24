
package net.opengis.gml.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *             A PolygonPatch is a surface patch that is defined by
 *             a set of boundary curves and an underlying surface to
 *             which these curves adhere. The curves are coplanar and
 *             the polygon uses planar interpolation in its interior.
 *             Implements GM_Polygon of ISO 19107. 
 *          
 * 
 * <p>Java class for PolygonPatchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolygonPatchType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractSurfacePatchType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}exterior" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}interior" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="interpolation" type="{http://www.opengis.net/gml}SurfaceInterpolationType" fixed="planar" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolygonPatchType", propOrder = {
    "exterior",
    "interiors"
})
public class PolygonPatchType
    extends AbstractSurfacePatchType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected AbstractRingPropertyType exterior;
    @XmlElement(name = "interior")
    protected List<AbstractRingPropertyType> interiors;
    @XmlAttribute(name = "interpolation")
    protected SurfaceInterpolationType interpolation;

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

    /**
     * Gets the value of the interpolation property.
     * 
     * @return
     *     possible object is
     *     {@link SurfaceInterpolationType }
     *     
     */
    public SurfaceInterpolationType getInterpolation() {
        if (interpolation == null) {
            return SurfaceInterpolationType.PLANAR;
        } else {
            return interpolation;
        }
    }

    /**
     * Sets the value of the interpolation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SurfaceInterpolationType }
     *     
     */
    public void setInterpolation(SurfaceInterpolationType value) {
        this.interpolation = value;
    }

    public boolean isSetInterpolation() {
        return (this.interpolation!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof PolygonPatchType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final PolygonPatchType that = ((PolygonPatchType) object);
        equalsBuilder.append(this.getExterior(), that.getExterior());
        equalsBuilder.append(this.getInteriors(), that.getInteriors());
        equalsBuilder.append(this.getInterpolation(), that.getInterpolation());
    }

    public boolean equals(Object object) {
        if (!(object instanceof PolygonPatchType)) {
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
        hashCodeBuilder.append(this.getExterior());
        hashCodeBuilder.append(this.getInteriors());
        hashCodeBuilder.append(this.getInterpolation());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            AbstractRingPropertyType theExterior;
            theExterior = this.getExterior();
            toStringBuilder.append("exterior", theExterior);
        }
        {
            List<AbstractRingPropertyType> theInteriors;
            theInteriors = this.getInteriors();
            toStringBuilder.append("interiors", theInteriors);
        }
        {
            SurfaceInterpolationType theInterpolation;
            theInterpolation = this.getInterpolation();
            toStringBuilder.append("interpolation", theInterpolation);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final PolygonPatchType copy = ((target == null)?((PolygonPatchType) createCopy()):((PolygonPatchType) target));
        super.copyTo(copy, copyBuilder);
        {
            AbstractRingPropertyType sourceExterior;
            sourceExterior = this.getExterior();
            AbstractRingPropertyType copyExterior = ((AbstractRingPropertyType) copyBuilder.copy(sourceExterior));
            copy.setExterior(copyExterior);
        }
        {
            List<AbstractRingPropertyType> sourceInteriors;
            sourceInteriors = this.getInteriors();
            List<AbstractRingPropertyType> copyInteriors = ((List<AbstractRingPropertyType> ) copyBuilder.copy(sourceInteriors));
            copy.unsetInteriors();
            List<AbstractRingPropertyType> uniqueInteriorsl = copy.getInteriors();
            uniqueInteriorsl.addAll(copyInteriors);
        }
        {
            SurfaceInterpolationType sourceInterpolation;
            sourceInterpolation = this.getInterpolation();
            SurfaceInterpolationType copyInterpolation = ((SurfaceInterpolationType) copyBuilder.copy(sourceInterpolation));
            copy.setInterpolation(copyInterpolation);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new PolygonPatchType();
    }

}
