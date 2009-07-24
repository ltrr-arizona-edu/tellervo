
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}variable"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.tridas.org/1.3}unitless"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}unit"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.tridas.org/1.3}value" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "variable",
    "unit",
    "unitless",
    "values"
})
@XmlRootElement(name = "values")
@TridasEditProperties(machineOnly = true)
public class TridasValues
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected TridasVariable variable;
    protected TridasUnit unit;
    protected TridasUnitless unitless;
    @XmlElement(name = "value", required = true)
    protected List<TridasValue> values;

    /**
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVariable }
     *     
     */
    public TridasVariable getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVariable }
     *     
     */
    public void setVariable(TridasVariable value) {
        this.variable = value;
    }

    public boolean isSetVariable() {
        return (this.variable!= null);
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link TridasUnit }
     *     
     */
    public TridasUnit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasUnit }
     *     
     */
    public void setUnit(TridasUnit value) {
        this.unit = value;
    }

    public boolean isSetUnit() {
        return (this.unit!= null);
    }

    /**
     * Gets the value of the unitless property.
     * 
     * @return
     *     possible object is
     *     {@link TridasUnitless }
     *     
     */
    public TridasUnitless getUnitless() {
        return unitless;
    }

    /**
     * Sets the value of the unitless property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasUnitless }
     *     
     */
    public void setUnitless(TridasUnitless value) {
        this.unitless = value;
    }

    public boolean isSetUnitless() {
        return (this.unitless!= null);
    }

    /**
     * Gets the value of the values property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the values property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasValue }
     * 
     * 
     */
    public List<TridasValue> getValues() {
        if (values == null) {
            values = new ArrayList<TridasValue>();
        }
        return this.values;
    }

    public boolean isSetValues() {
        return ((this.values!= null)&&(!this.values.isEmpty()));
    }

    public void unsetValues() {
        this.values = null;
    }

    /**
     * Sets the value of the values property.
     * 
     * @param values
     *     allowed object is
     *     {@link TridasValue }
     *     
     */
    public void setValues(List<TridasValue> values) {
        this.values = values;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasValues)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasValues that = ((TridasValues) object);
        equalsBuilder.append(this.getVariable(), that.getVariable());
        equalsBuilder.append(this.getUnit(), that.getUnit());
        equalsBuilder.append(this.getUnitless(), that.getUnitless());
        equalsBuilder.append(this.getValues(), that.getValues());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasValues)) {
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
        hashCodeBuilder.append(this.getVariable());
        hashCodeBuilder.append(this.getUnit());
        hashCodeBuilder.append(this.getUnitless());
        hashCodeBuilder.append(this.getValues());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasVariable theVariable;
            theVariable = this.getVariable();
            toStringBuilder.append("variable", theVariable);
        }
        {
            TridasUnit theUnit;
            theUnit = this.getUnit();
            toStringBuilder.append("unit", theUnit);
        }
        {
            TridasUnitless theUnitless;
            theUnitless = this.getUnitless();
            toStringBuilder.append("unitless", theUnitless);
        }
        {
            List<TridasValue> theValues;
            theValues = this.getValues();
            toStringBuilder.append("values", theValues);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasValues copy = ((target == null)?((TridasValues) createCopy()):((TridasValues) target));
        {
            TridasVariable sourceVariable;
            sourceVariable = this.getVariable();
            TridasVariable copyVariable = ((TridasVariable) copyBuilder.copy(sourceVariable));
            copy.setVariable(copyVariable);
        }
        {
            TridasUnit sourceUnit;
            sourceUnit = this.getUnit();
            TridasUnit copyUnit = ((TridasUnit) copyBuilder.copy(sourceUnit));
            copy.setUnit(copyUnit);
        }
        {
            TridasUnitless sourceUnitless;
            sourceUnitless = this.getUnitless();
            TridasUnitless copyUnitless = ((TridasUnitless) copyBuilder.copy(sourceUnitless));
            copy.setUnitless(copyUnitless);
        }
        {
            List<TridasValue> sourceValues;
            sourceValues = this.getValues();
            List<TridasValue> copyValues = ((List<TridasValue> ) copyBuilder.copy(sourceValues));
            copy.setValues(copyValues);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasValues();
    }

}
