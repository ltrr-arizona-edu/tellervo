
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
 * 				A controlled vocabulary is used to limit users to a pick list of values
 * 			
 * 
 * <p>Java class for controlledVoc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="controlledVoc">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="normalStd" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="normalId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="normal" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "controlledVoc", propOrder = {
    "value"
})
@XmlSeeAlso({
    TridasMeasuringMethod.class,
    TridasShape.class,
    TridasUnit.class,
    TridasVariable.class,
    TridasCategory.class,
    TridasRemark.class
})
public class ControlledVoc
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlValue
    protected String value;
    @XmlAttribute(name = "normalStd")
    @XmlSchemaType(name = "anySimpleType")
    protected String normalStd;
    @XmlAttribute(name = "normalId")
    @XmlSchemaType(name = "anySimpleType")
    protected String normalId;
    @XmlAttribute(name = "normal")
    @XmlSchemaType(name = "anySimpleType")
    protected String normal;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value!= null);
    }

    /**
     * Gets the value of the normalStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormalStd() {
        return normalStd;
    }

    /**
     * Sets the value of the normalStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormalStd(String value) {
        this.normalStd = value;
    }

    public boolean isSetNormalStd() {
        return (this.normalStd!= null);
    }

    /**
     * Gets the value of the normalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormalId() {
        return normalId;
    }

    /**
     * Sets the value of the normalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormalId(String value) {
        this.normalId = value;
    }

    public boolean isSetNormalId() {
        return (this.normalId!= null);
    }

    /**
     * Gets the value of the normal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNormal() {
        return normal;
    }

    /**
     * Sets the value of the normal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNormal(String value) {
        this.normal = value;
    }

    public boolean isSetNormal() {
        return (this.normal!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof ControlledVoc)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ControlledVoc that = ((ControlledVoc) object);
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getNormalStd(), that.getNormalStd());
        equalsBuilder.append(this.getNormalId(), that.getNormalId());
        equalsBuilder.append(this.getNormal(), that.getNormal());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ControlledVoc)) {
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
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getNormalStd());
        hashCodeBuilder.append(this.getNormalId());
        hashCodeBuilder.append(this.getNormal());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
        {
            String theNormalStd;
            theNormalStd = this.getNormalStd();
            toStringBuilder.append("normalStd", theNormalStd);
        }
        {
            String theNormalId;
            theNormalId = this.getNormalId();
            toStringBuilder.append("normalId", theNormalId);
        }
        {
            String theNormal;
            theNormal = this.getNormal();
            toStringBuilder.append("normal", theNormal);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final ControlledVoc copy = ((target == null)?((ControlledVoc) createCopy()):((ControlledVoc) target));
        if (this.isSetValue()) {
            String sourceValue;
            sourceValue = this.getValue();
            String copyValue = ((String) copyBuilder.copy(sourceValue));
            copy.setValue(copyValue);
        } else {
            copy.value = null;
        }
        if (this.isSetNormalStd()) {
            String sourceNormalStd;
            sourceNormalStd = this.getNormalStd();
            String copyNormalStd = ((String) copyBuilder.copy(sourceNormalStd));
            copy.setNormalStd(copyNormalStd);
        } else {
            copy.normalStd = null;
        }
        if (this.isSetNormalId()) {
            String sourceNormalId;
            sourceNormalId = this.getNormalId();
            String copyNormalId = ((String) copyBuilder.copy(sourceNormalId));
            copy.setNormalId(copyNormalId);
        } else {
            copy.normalId = null;
        }
        if (this.isSetNormal()) {
            String sourceNormal;
            sourceNormal = this.getNormal();
            String copyNormal = ((String) copyBuilder.copy(sourceNormal));
            copy.setNormal(copyNormal);
        } else {
            copy.normal = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new ControlledVoc();
    }

}
