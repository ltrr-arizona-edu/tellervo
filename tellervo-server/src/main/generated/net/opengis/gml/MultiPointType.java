
package net.opengis.gml;

import java.util.ArrayList;
import java.util.List;
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
 *             A MultiPoint is defined by one or more Points, referenced
 *             through pointMember elements.
 *          
 * 
 * <p>Java class for MultiPointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiPointType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractGeometricAggregateType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}pointMember" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiPointType", propOrder = {
    "pointMembers"
})
public class MultiPointType
    extends AbstractGeometricAggregateType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "pointMember")
    protected List<PointMember> pointMembers;

    /**
     * Gets the value of the pointMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pointMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPointMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PointMember }
     * 
     * 
     */
    public List<PointMember> getPointMembers() {
        if (pointMembers == null) {
            pointMembers = new ArrayList<PointMember>();
        }
        return this.pointMembers;
    }

    public boolean isSetPointMembers() {
        return ((this.pointMembers!= null)&&(!this.pointMembers.isEmpty()));
    }

    public void unsetPointMembers() {
        this.pointMembers = null;
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
            List<PointMember> thePointMembers;
            thePointMembers = (this.isSetPointMembers()?this.getPointMembers():null);
            strategy.appendField(locator, this, "pointMembers", buffer, thePointMembers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiPointType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final MultiPointType that = ((MultiPointType) object);
        {
            List<PointMember> lhsPointMembers;
            lhsPointMembers = (this.isSetPointMembers()?this.getPointMembers():null);
            List<PointMember> rhsPointMembers;
            rhsPointMembers = (that.isSetPointMembers()?that.getPointMembers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "pointMembers", lhsPointMembers), LocatorUtils.property(thatLocator, "pointMembers", rhsPointMembers), lhsPointMembers, rhsPointMembers)) {
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
            List<PointMember> thePointMembers;
            thePointMembers = (this.isSetPointMembers()?this.getPointMembers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "pointMembers", thePointMembers), currentHashCode, thePointMembers);
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
        if (draftCopy instanceof MultiPointType) {
            final MultiPointType copy = ((MultiPointType) draftCopy);
            if (this.isSetPointMembers()) {
                List<PointMember> sourcePointMembers;
                sourcePointMembers = (this.isSetPointMembers()?this.getPointMembers():null);
                @SuppressWarnings("unchecked")
                List<PointMember> copyPointMembers = ((List<PointMember> ) strategy.copy(LocatorUtils.property(locator, "pointMembers", sourcePointMembers), sourcePointMembers));
                copy.unsetPointMembers();
                List<PointMember> uniquePointMembersl = copy.getPointMembers();
                uniquePointMembersl.addAll(copyPointMembers);
            } else {
                copy.unsetPointMembers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiPointType();
    }

    /**
     * Sets the value of the pointMembers property.
     * 
     * @param pointMembers
     *     allowed object is
     *     {@link PointMember }
     *     
     */
    public void setPointMembers(List<PointMember> pointMembers) {
        this.pointMembers = pointMembers;
    }

}
