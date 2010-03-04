
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
 *             Curve is a 1-dimensional primitive. Curves are continuous,
 *             connected, and have a measurable length in terms of the
 *             coordinate system. 
 *             A curve is composed of one or more curve segments.
 *             The curve segments are connected to one another, with the
 *             end point of each segment except the last being the start
 *             point of the next segment in the segment list.
 *             The orientation of the curve is positive.
 *          
 * 
 * <p>Java class for CurveType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CurveType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractCurveType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}segments"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CurveType", propOrder = {
    "segments"
})
public class CurveType
    extends AbstractCurveType
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected Segments segments;

    /**
     * 
     *                         This element encapsulates the segments of the
     *                         curve.
     *                      
     * 
     * @return
     *     possible object is
     *     {@link Segments }
     *     
     */
    public Segments getSegments() {
        return segments;
    }

    /**
     * Sets the value of the segments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Segments }
     *     
     */
    public void setSegments(Segments value) {
        this.segments = value;
    }

    public boolean isSetSegments() {
        return (this.segments!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof CurveType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final CurveType that = ((CurveType) object);
        equalsBuilder.append(this.getSegments(), that.getSegments());
    }

    public boolean equals(Object object) {
        if (!(object instanceof CurveType)) {
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
        hashCodeBuilder.append(this.getSegments());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            Segments theSegments;
            theSegments = this.getSegments();
            toStringBuilder.append("segments", theSegments);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final CurveType copy = ((target == null)?((CurveType) createCopy()):((CurveType) target));
        super.copyTo(copy, copyBuilder);
        if (this.isSetSegments()) {
            Segments sourceSegments;
            sourceSegments = this.getSegments();
            Segments copySegments = ((Segments) copyBuilder.copy(sourceSegments));
            copy.setSegments(copySegments);
        } else {
            copy.segments = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new CurveType();
    }

}
