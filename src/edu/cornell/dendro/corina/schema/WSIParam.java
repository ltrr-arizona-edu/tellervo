
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" type="{http://dendro.cornell.edu/schema/corina/1.0}searchParameterName" />
 *       &lt;attribute name="operator" type="{http://dendro.cornell.edu/schema/corina/1.0}searchOperator" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "param")
public class WSIParam
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlAttribute(name = "name")
    protected SearchParameterName name;
    @XmlAttribute(name = "operator")
    protected SearchOperator operator;
    @XmlAttribute(name = "value")
    protected String value;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link SearchParameterName }
     *     
     */
    public SearchParameterName getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchParameterName }
     *     
     */
    public void setName(SearchParameterName value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link SearchOperator }
     *     
     */
    public SearchOperator getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchOperator }
     *     
     */
    public void setOperator(SearchOperator value) {
        this.operator = value;
    }

    public boolean isSetOperator() {
        return (this.operator!= null);
    }

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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIParam)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIParam that = ((WSIParam) object);
        equalsBuilder.append(this.getName(), that.getName());
        equalsBuilder.append(this.getOperator(), that.getOperator());
        equalsBuilder.append(this.getValue(), that.getValue());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIParam)) {
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
        hashCodeBuilder.append(this.getName());
        hashCodeBuilder.append(this.getOperator());
        hashCodeBuilder.append(this.getValue());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            SearchParameterName theName;
            theName = this.getName();
            toStringBuilder.append("name", theName);
        }
        {
            SearchOperator theOperator;
            theOperator = this.getOperator();
            toStringBuilder.append("operator", theOperator);
        }
        {
            String theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIParam copy = ((target == null)?((WSIParam) createCopy()):((WSIParam) target));
        if (this.isSetName()) {
            SearchParameterName sourceName;
            sourceName = this.getName();
            SearchParameterName copyName = ((SearchParameterName) copyBuilder.copy(sourceName));
            copy.setName(copyName);
        } else {
            copy.name = null;
        }
        if (this.isSetOperator()) {
            SearchOperator sourceOperator;
            sourceOperator = this.getOperator();
            SearchOperator copyOperator = ((SearchOperator) copyBuilder.copy(sourceOperator));
            copy.setOperator(copyOperator);
        } else {
            copy.operator = null;
        }
        if (this.isSetValue()) {
            String sourceValue;
            sourceValue = this.getValue();
            String copyValue = ((String) copyBuilder.copy(sourceValue));
            copy.setValue(copyValue);
        } else {
            copy.value = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIParam();
    }

}
