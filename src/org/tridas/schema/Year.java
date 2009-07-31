
package org.tridas.schema;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
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
 * Data type for storing year.  Includes a separate suffix attribute and optional certainty value
 * 
 * <p>Java class for year complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="year">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>positiveInteger">
 *       &lt;attribute name="certainty" type="{http://www.tridas.org/1.3}certainty" />
 *       &lt;attribute name="suffix" use="required" type="{http://www.tridas.org/1.3}datingSuffix" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "year", propOrder = {
    "value"
})
public class Year
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlValue
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger value;
    @XmlAttribute(name = "certainty")
    protected Certainty certainty;
    @XmlAttribute(name = "suffix", required = true)
    protected DatingSuffix suffix;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setValue(BigInteger value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value!= null);
    }

    /**
     * Gets the value of the certainty property.
     * 
     * @return
     *     possible object is
     *     {@link Certainty }
     *     
     */
    public Certainty getCertainty() {
        return certainty;
    }

    /**
     * Sets the value of the certainty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Certainty }
     *     
     */
    public void setCertainty(Certainty value) {
        this.certainty = value;
    }

    public boolean isSetCertainty() {
        return (this.certainty!= null);
    }

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link DatingSuffix }
     *     
     */
    public DatingSuffix getSuffix() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatingSuffix }
     *     
     */
    public void setSuffix(DatingSuffix value) {
        this.suffix = value;
    }

    public boolean isSetSuffix() {
        return (this.suffix!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Year)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Year that = ((Year) object);
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getCertainty(), that.getCertainty());
        equalsBuilder.append(this.getSuffix(), that.getSuffix());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Year)) {
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
        hashCodeBuilder.append(this.getCertainty());
        hashCodeBuilder.append(this.getSuffix());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            BigInteger theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
        {
            Certainty theCertainty;
            theCertainty = this.getCertainty();
            toStringBuilder.append("certainty", theCertainty);
        }
        {
            DatingSuffix theSuffix;
            theSuffix = this.getSuffix();
            toStringBuilder.append("suffix", theSuffix);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final Year copy = ((target == null)?((Year) createCopy()):((Year) target));
        if (this.isSetValue()) {
            BigInteger sourceValue;
            sourceValue = this.getValue();
            BigInteger copyValue = ((BigInteger) copyBuilder.copy(sourceValue));
            copy.setValue(copyValue);
        } else {
            copy.value = null;
        }
        if (this.isSetCertainty()) {
            Certainty sourceCertainty;
            sourceCertainty = this.getCertainty();
            Certainty copyCertainty = ((Certainty) copyBuilder.copy(sourceCertainty));
            copy.setCertainty(copyCertainty);
        } else {
            copy.certainty = null;
        }
        if (this.isSetSuffix()) {
            DatingSuffix sourceSuffix;
            sourceSuffix = this.getSuffix();
            DatingSuffix copySuffix = ((DatingSuffix) copyBuilder.copy(sourceSuffix));
            copy.setSuffix(copySuffix);
        } else {
            copy.suffix = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new Year();
    }

}
