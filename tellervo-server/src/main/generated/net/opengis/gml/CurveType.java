
package net.opengis.gml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
        super.appendFields(locator, buffer, strategy);
        {
            Segments theSegments;
            theSegments = this.getSegments();
            strategy.appendField(locator, this, "segments", buffer, theSegments);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof CurveType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final CurveType that = ((CurveType) object);
        {
            Segments lhsSegments;
            lhsSegments = this.getSegments();
            Segments rhsSegments;
            rhsSegments = that.getSegments();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "segments", lhsSegments), LocatorUtils.property(thatLocator, "segments", rhsSegments), lhsSegments, rhsSegments)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            Segments theSegments;
            theSegments = this.getSegments();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "segments", theSegments), currentHashCode, theSegments);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof CurveType) {
            final CurveType copy = ((CurveType) draftCopy);
            if (this.isSetSegments()) {
                Segments sourceSegments;
                sourceSegments = this.getSegments();
                Segments copySegments = ((Segments) strategy.copy(LocatorUtils.property(locator, "segments", sourceSegments), sourceSegments));
                copy.setSegments(copySegments);
            } else {
                copy.segments = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new CurveType();
    }

}
