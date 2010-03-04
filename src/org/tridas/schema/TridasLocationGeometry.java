
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.opengis.gml.schema.PointType;
import net.opengis.gml.schema.PolygonType;
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
import org.tridas.annotations.TridasEditProperties;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.opengis.net/gml}Point"/>
 *         &lt;element ref="{http://www.opengis.net/gml}Polygon"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "polygon",
    "point"
})
@XmlRootElement(name = "locationGeometry")
@TridasEditProperties(finalType = true)
public class TridasLocationGeometry
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "Polygon", namespace = "http://www.opengis.net/gml")
    protected PolygonType polygon;
    @XmlElement(name = "Point", namespace = "http://www.opengis.net/gml")
    protected PointType point;

    /**
     * Gets the value of the polygon property.
     * 
     * @return
     *     possible object is
     *     {@link PolygonType }
     *     
     */
    public PolygonType getPolygon() {
        return polygon;
    }

    /**
     * Sets the value of the polygon property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolygonType }
     *     
     */
    public void setPolygon(PolygonType value) {
        this.polygon = value;
    }

    public boolean isSetPolygon() {
        return (this.polygon!= null);
    }

    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link PointType }
     *     
     */
    public PointType getPoint() {
        return point;
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointType }
     *     
     */
    public void setPoint(PointType value) {
        this.point = value;
    }

    public boolean isSetPoint() {
        return (this.point!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasLocationGeometry)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasLocationGeometry that = ((TridasLocationGeometry) object);
        equalsBuilder.append(this.getPolygon(), that.getPolygon());
        equalsBuilder.append(this.getPoint(), that.getPoint());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasLocationGeometry)) {
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
        hashCodeBuilder.append(this.getPolygon());
        hashCodeBuilder.append(this.getPoint());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            PolygonType thePolygon;
            thePolygon = this.getPolygon();
            toStringBuilder.append("polygon", thePolygon);
        }
        {
            PointType thePoint;
            thePoint = this.getPoint();
            toStringBuilder.append("point", thePoint);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasLocationGeometry copy = ((target == null)?((TridasLocationGeometry) createCopy()):((TridasLocationGeometry) target));
        if (this.isSetPolygon()) {
            PolygonType sourcePolygon;
            sourcePolygon = this.getPolygon();
            PolygonType copyPolygon = ((PolygonType) copyBuilder.copy(sourcePolygon));
            copy.setPolygon(copyPolygon);
        } else {
            copy.polygon = null;
        }
        if (this.isSetPoint()) {
            PointType sourcePoint;
            sourcePoint = this.getPoint();
            PointType copyPoint = ((PointType) copyBuilder.copy(sourcePoint));
            copy.setPoint(copyPoint);
        } else {
            copy.point = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasLocationGeometry();
    }

}
