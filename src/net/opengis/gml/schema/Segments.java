
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
 *             A container for an array of curve segments.
 *          
 * 
 * <p>Java class for CurveSegmentArrayPropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurveSegmentArrayPropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}_CurveSegment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurveSegmentArrayPropertyType", propOrder = {
    "curveSegments"
})
@XmlRootElement(name = "segments")
public class Segments
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElementRef(name = "_CurveSegment", namespace = "http://www.opengis.net/gml", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractCurveSegmentType>> curveSegments;

    /**
     * Gets the value of the curveSegments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the curveSegments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    get_CurveSegments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link LineStringSegmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractCurveSegmentType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractCurveSegmentType>> get_CurveSegments() {
        if (curveSegments == null) {
            curveSegments = new ArrayList<JAXBElement<? extends AbstractCurveSegmentType>>();
        }
        return this.curveSegments;
    }

    public boolean isSet_CurveSegments() {
        return ((this.curveSegments!= null)&&(!this.curveSegments.isEmpty()));
    }

    public void unset_CurveSegments() {
        this.curveSegments = null;
    }

    /**
     * Sets the value of the _CurveSegments property.
     * 
     * @param curveSegments
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link LineStringSegmentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractCurveSegmentType }{@code >}
     *     
     */
    public void set_CurveSegments(List<JAXBElement<? extends AbstractCurveSegmentType>> curveSegments) {
        this.curveSegments = curveSegments;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Segments)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Segments that = ((Segments) object);
        equalsBuilder.append(this.get_CurveSegments(), that.get_CurveSegments());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Segments)) {
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
        hashCodeBuilder.append(this.get_CurveSegments());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<JAXBElement<? extends AbstractCurveSegmentType>> the_CurveSegments;
            the_CurveSegments = this.get_CurveSegments();
            toStringBuilder.append("curveSegments", the_CurveSegments);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final Segments copy = ((target == null)?((Segments) createCopy()):((Segments) target));
        {
            List<JAXBElement<? extends AbstractCurveSegmentType>> source_CurveSegments;
            source_CurveSegments = this.get_CurveSegments();
            List<JAXBElement<? extends AbstractCurveSegmentType>> copy_CurveSegments = ((List<JAXBElement<? extends AbstractCurveSegmentType>> ) copyBuilder.copy(source_CurveSegments));
            copy.set_CurveSegments(copy_CurveSegments);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new Segments();
    }

}
