
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
 *             A MultiCurve is defined by one or more Curves, referenced
 *             through curveMember elements.
 *          
 * 
 * <p>Java class for MultiCurveType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiCurveType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractGeometricAggregateType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}curveMember" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiCurveType", propOrder = {
    "curveMembers"
})
public class MultiCurveType
    extends AbstractGeometricAggregateType
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "curveMember")
    protected List<CurveMember> curveMembers;

    /**
     * Gets the value of the curveMembers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the curveMembers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCurveMembers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CurveMember }
     * 
     * 
     */
    public List<CurveMember> getCurveMembers() {
        if (curveMembers == null) {
            curveMembers = new ArrayList<CurveMember>();
        }
        return this.curveMembers;
    }

    public boolean isSetCurveMembers() {
        return ((this.curveMembers!= null)&&(!this.curveMembers.isEmpty()));
    }

    public void unsetCurveMembers() {
        this.curveMembers = null;
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
            List<CurveMember> theCurveMembers;
            theCurveMembers = (this.isSetCurveMembers()?this.getCurveMembers():null);
            strategy.appendField(locator, this, "curveMembers", buffer, theCurveMembers);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof MultiCurveType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final MultiCurveType that = ((MultiCurveType) object);
        {
            List<CurveMember> lhsCurveMembers;
            lhsCurveMembers = (this.isSetCurveMembers()?this.getCurveMembers():null);
            List<CurveMember> rhsCurveMembers;
            rhsCurveMembers = (that.isSetCurveMembers()?that.getCurveMembers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "curveMembers", lhsCurveMembers), LocatorUtils.property(thatLocator, "curveMembers", rhsCurveMembers), lhsCurveMembers, rhsCurveMembers)) {
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
            List<CurveMember> theCurveMembers;
            theCurveMembers = (this.isSetCurveMembers()?this.getCurveMembers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "curveMembers", theCurveMembers), currentHashCode, theCurveMembers);
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
        if (draftCopy instanceof MultiCurveType) {
            final MultiCurveType copy = ((MultiCurveType) draftCopy);
            if (this.isSetCurveMembers()) {
                List<CurveMember> sourceCurveMembers;
                sourceCurveMembers = (this.isSetCurveMembers()?this.getCurveMembers():null);
                @SuppressWarnings("unchecked")
                List<CurveMember> copyCurveMembers = ((List<CurveMember> ) strategy.copy(LocatorUtils.property(locator, "curveMembers", sourceCurveMembers), sourceCurveMembers));
                copy.unsetCurveMembers();
                List<CurveMember> uniqueCurveMembersl = copy.getCurveMembers();
                uniqueCurveMembersl.addAll(copyCurveMembers);
            } else {
                copy.unsetCurveMembers();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new MultiCurveType();
    }

    /**
     * Sets the value of the curveMembers property.
     * 
     * @param curveMembers
     *     allowed object is
     *     {@link CurveMember }
     *     
     */
    public void setCurveMembers(List<CurveMember> curveMembers) {
        this.curveMembers = curveMembers;
    }

}
