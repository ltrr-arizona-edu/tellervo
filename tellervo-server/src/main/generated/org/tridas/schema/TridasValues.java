
package org.tridas.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}variable"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}unitless"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}unit"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}value" maxOccurs="unbounded" minOccurs="0"/>
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
public class TridasValues
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected TridasVariable variable;
    protected TridasUnit unit;
    protected TridasUnitless unitless;
    @XmlElement(name = "value")
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
            TridasVariable theVariable;
            theVariable = this.getVariable();
            strategy.appendField(locator, this, "variable", buffer, theVariable);
        }
        {
            TridasUnit theUnit;
            theUnit = this.getUnit();
            strategy.appendField(locator, this, "unit", buffer, theUnit);
        }
        {
            TridasUnitless theUnitless;
            theUnitless = this.getUnitless();
            strategy.appendField(locator, this, "unitless", buffer, theUnitless);
        }
        {
            List<TridasValue> theValues;
            theValues = (this.isSetValues()?this.getValues():null);
            strategy.appendField(locator, this, "values", buffer, theValues);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasValues)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasValues that = ((TridasValues) object);
        {
            TridasVariable lhsVariable;
            lhsVariable = this.getVariable();
            TridasVariable rhsVariable;
            rhsVariable = that.getVariable();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "variable", lhsVariable), LocatorUtils.property(thatLocator, "variable", rhsVariable), lhsVariable, rhsVariable)) {
                return false;
            }
        }
        {
            TridasUnit lhsUnit;
            lhsUnit = this.getUnit();
            TridasUnit rhsUnit;
            rhsUnit = that.getUnit();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "unit", lhsUnit), LocatorUtils.property(thatLocator, "unit", rhsUnit), lhsUnit, rhsUnit)) {
                return false;
            }
        }
        {
            TridasUnitless lhsUnitless;
            lhsUnitless = this.getUnitless();
            TridasUnitless rhsUnitless;
            rhsUnitless = that.getUnitless();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "unitless", lhsUnitless), LocatorUtils.property(thatLocator, "unitless", rhsUnitless), lhsUnitless, rhsUnitless)) {
                return false;
            }
        }
        {
            List<TridasValue> lhsValues;
            lhsValues = (this.isSetValues()?this.getValues():null);
            List<TridasValue> rhsValues;
            rhsValues = (that.isSetValues()?that.getValues():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "values", lhsValues), LocatorUtils.property(thatLocator, "values", rhsValues), lhsValues, rhsValues)) {
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
            TridasVariable theVariable;
            theVariable = this.getVariable();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "variable", theVariable), currentHashCode, theVariable);
        }
        {
            TridasUnit theUnit;
            theUnit = this.getUnit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "unit", theUnit), currentHashCode, theUnit);
        }
        {
            TridasUnitless theUnitless;
            theUnitless = this.getUnitless();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "unitless", theUnitless), currentHashCode, theUnitless);
        }
        {
            List<TridasValue> theValues;
            theValues = (this.isSetValues()?this.getValues():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "values", theValues), currentHashCode, theValues);
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
        if (draftCopy instanceof TridasValues) {
            final TridasValues copy = ((TridasValues) draftCopy);
            if (this.isSetVariable()) {
                TridasVariable sourceVariable;
                sourceVariable = this.getVariable();
                TridasVariable copyVariable = ((TridasVariable) strategy.copy(LocatorUtils.property(locator, "variable", sourceVariable), sourceVariable));
                copy.setVariable(copyVariable);
            } else {
                copy.variable = null;
            }
            if (this.isSetUnit()) {
                TridasUnit sourceUnit;
                sourceUnit = this.getUnit();
                TridasUnit copyUnit = ((TridasUnit) strategy.copy(LocatorUtils.property(locator, "unit", sourceUnit), sourceUnit));
                copy.setUnit(copyUnit);
            } else {
                copy.unit = null;
            }
            if (this.isSetUnitless()) {
                TridasUnitless sourceUnitless;
                sourceUnitless = this.getUnitless();
                TridasUnitless copyUnitless = ((TridasUnitless) strategy.copy(LocatorUtils.property(locator, "unitless", sourceUnitless), sourceUnitless));
                copy.setUnitless(copyUnitless);
            } else {
                copy.unitless = null;
            }
            if (this.isSetValues()) {
                List<TridasValue> sourceValues;
                sourceValues = (this.isSetValues()?this.getValues():null);
                @SuppressWarnings("unchecked")
                List<TridasValue> copyValues = ((List<TridasValue> ) strategy.copy(LocatorUtils.property(locator, "values", sourceValues), sourceValues));
                copy.unsetValues();
                List<TridasValue> uniqueValuesl = copy.getValues();
                uniqueValuesl.addAll(copyValues);
            } else {
                copy.unsetValues();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasValues();
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

}
