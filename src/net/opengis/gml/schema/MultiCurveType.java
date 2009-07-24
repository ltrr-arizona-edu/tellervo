
package net.opengis.gml.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof MultiCurveType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final MultiCurveType that = ((MultiCurveType) object);
        equalsBuilder.append(this.getCurveMembers(), that.getCurveMembers());
    }

    public boolean equals(Object object) {
        if (!(object instanceof MultiCurveType)) {
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
        hashCodeBuilder.append(this.getCurveMembers());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            List<CurveMember> theCurveMembers;
            theCurveMembers = this.getCurveMembers();
            toStringBuilder.append("curveMembers", theCurveMembers);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final MultiCurveType copy = ((target == null)?((MultiCurveType) createCopy()):((MultiCurveType) target));
        super.copyTo(copy, copyBuilder);
        {
            List<CurveMember> sourceCurveMembers;
            sourceCurveMembers = this.getCurveMembers();
            List<CurveMember> copyCurveMembers = ((List<CurveMember> ) copyBuilder.copy(sourceCurveMembers));
            copy.setCurveMembers(copyCurveMembers);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new MultiCurveType();
    }

}
