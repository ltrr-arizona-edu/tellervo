
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.opengis.gml.PointType;
import net.opengis.gml.PolygonType;
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
public class TridasLocationGeometry
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            PolygonType thePolygon;
            thePolygon = this.getPolygon();
            strategy.appendField(locator, this, "polygon", buffer, thePolygon);
        }
        {
            PointType thePoint;
            thePoint = this.getPoint();
            strategy.appendField(locator, this, "point", buffer, thePoint);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasLocationGeometry)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasLocationGeometry that = ((TridasLocationGeometry) object);
        {
            PolygonType lhsPolygon;
            lhsPolygon = this.getPolygon();
            PolygonType rhsPolygon;
            rhsPolygon = that.getPolygon();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "polygon", lhsPolygon), LocatorUtils.property(thatLocator, "polygon", rhsPolygon), lhsPolygon, rhsPolygon)) {
                return false;
            }
        }
        {
            PointType lhsPoint;
            lhsPoint = this.getPoint();
            PointType rhsPoint;
            rhsPoint = that.getPoint();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "point", lhsPoint), LocatorUtils.property(thatLocator, "point", rhsPoint), lhsPoint, rhsPoint)) {
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
            PolygonType thePolygon;
            thePolygon = this.getPolygon();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "polygon", thePolygon), currentHashCode, thePolygon);
        }
        {
            PointType thePoint;
            thePoint = this.getPoint();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "point", thePoint), currentHashCode, thePoint);
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
        if (draftCopy instanceof TridasLocationGeometry) {
            final TridasLocationGeometry copy = ((TridasLocationGeometry) draftCopy);
            if (this.isSetPolygon()) {
                PolygonType sourcePolygon;
                sourcePolygon = this.getPolygon();
                PolygonType copyPolygon = ((PolygonType) strategy.copy(LocatorUtils.property(locator, "polygon", sourcePolygon), sourcePolygon));
                copy.setPolygon(copyPolygon);
            } else {
                copy.polygon = null;
            }
            if (this.isSetPoint()) {
                PointType sourcePoint;
                sourcePoint = this.getPoint();
                PointType copyPoint = ((PointType) strategy.copy(LocatorUtils.property(locator, "point", sourcePoint), sourcePoint));
                copy.setPoint(copyPoint);
            } else {
                copy.point = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasLocationGeometry();
    }

}
