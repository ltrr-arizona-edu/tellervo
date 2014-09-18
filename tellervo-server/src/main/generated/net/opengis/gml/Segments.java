
package net.opengis.gml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

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
            List<JAXBElement<? extends AbstractCurveSegmentType>> the_CurveSegments;
            the_CurveSegments = (this.isSet_CurveSegments()?this.get_CurveSegments():null);
            strategy.appendField(locator, this, "curveSegments", buffer, the_CurveSegments);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Segments)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Segments that = ((Segments) object);
        {
            List<JAXBElement<? extends AbstractCurveSegmentType>> lhs_CurveSegments;
            lhs_CurveSegments = (this.isSet_CurveSegments()?this.get_CurveSegments():null);
            List<JAXBElement<? extends AbstractCurveSegmentType>> rhs_CurveSegments;
            rhs_CurveSegments = (that.isSet_CurveSegments()?that.get_CurveSegments():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "curveSegments", lhs_CurveSegments), LocatorUtils.property(thatLocator, "curveSegments", rhs_CurveSegments), lhs_CurveSegments, rhs_CurveSegments)) {
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
            List<JAXBElement<? extends AbstractCurveSegmentType>> the_CurveSegments;
            the_CurveSegments = (this.isSet_CurveSegments()?this.get_CurveSegments():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "curveSegments", the_CurveSegments), currentHashCode, the_CurveSegments);
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
        if (draftCopy instanceof Segments) {
            final Segments copy = ((Segments) draftCopy);
            if (this.isSet_CurveSegments()) {
                List<JAXBElement<? extends AbstractCurveSegmentType>> source_CurveSegments;
                source_CurveSegments = (this.isSet_CurveSegments()?this.get_CurveSegments():null);
                @SuppressWarnings("unchecked")
                List<JAXBElement<? extends AbstractCurveSegmentType>> copy_CurveSegments = ((List<JAXBElement<? extends AbstractCurveSegmentType>> ) strategy.copy(LocatorUtils.property(locator, "curveSegments", source_CurveSegments), source_CurveSegments));
                copy.unset_CurveSegments();
                List<JAXBElement<? extends AbstractCurveSegmentType>> unique_CurveSegmentsl = copy.get_CurveSegments();
                unique_CurveSegmentsl.addAll(copy_CurveSegments);
            } else {
                copy.unset_CurveSegments();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new Segments();
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

}
