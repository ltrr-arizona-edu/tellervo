
package org.tridas.schema;

import java.math.BigDecimal;
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}statValue"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}type"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}significanceLevel" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}usedSoftware"/>
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
    "statValue",
    "type",
    "significanceLevel",
    "usedSoftware"
})
@XmlRootElement(name = "statFoundation")
public class TridasStatFoundation
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected BigDecimal statValue;
    @XmlElement(required = true)
    protected ControlledVoc type;
    protected BigDecimal significanceLevel;
    @XmlElement(required = true)
    protected String usedSoftware;

    /**
     * Gets the value of the statValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStatValue() {
        return statValue;
    }

    /**
     * Sets the value of the statValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStatValue(BigDecimal value) {
        this.statValue = value;
    }

    public boolean isSetStatValue() {
        return (this.statValue!= null);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledVoc }
     *     
     */
    public ControlledVoc getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setType(ControlledVoc value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the significanceLevel property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSignificanceLevel() {
        return significanceLevel;
    }

    /**
     * Sets the value of the significanceLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSignificanceLevel(BigDecimal value) {
        this.significanceLevel = value;
    }

    public boolean isSetSignificanceLevel() {
        return (this.significanceLevel!= null);
    }

    /**
     * Gets the value of the usedSoftware property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedSoftware() {
        return usedSoftware;
    }

    /**
     * Sets the value of the usedSoftware property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedSoftware(String value) {
        this.usedSoftware = value;
    }

    public boolean isSetUsedSoftware() {
        return (this.usedSoftware!= null);
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
            BigDecimal theStatValue;
            theStatValue = this.getStatValue();
            strategy.appendField(locator, this, "statValue", buffer, theStatValue);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            strategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            BigDecimal theSignificanceLevel;
            theSignificanceLevel = this.getSignificanceLevel();
            strategy.appendField(locator, this, "significanceLevel", buffer, theSignificanceLevel);
        }
        {
            String theUsedSoftware;
            theUsedSoftware = this.getUsedSoftware();
            strategy.appendField(locator, this, "usedSoftware", buffer, theUsedSoftware);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasStatFoundation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasStatFoundation that = ((TridasStatFoundation) object);
        {
            BigDecimal lhsStatValue;
            lhsStatValue = this.getStatValue();
            BigDecimal rhsStatValue;
            rhsStatValue = that.getStatValue();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "statValue", lhsStatValue), LocatorUtils.property(thatLocator, "statValue", rhsStatValue), lhsStatValue, rhsStatValue)) {
                return false;
            }
        }
        {
            ControlledVoc lhsType;
            lhsType = this.getType();
            ControlledVoc rhsType;
            rhsType = that.getType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            BigDecimal lhsSignificanceLevel;
            lhsSignificanceLevel = this.getSignificanceLevel();
            BigDecimal rhsSignificanceLevel;
            rhsSignificanceLevel = that.getSignificanceLevel();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "significanceLevel", lhsSignificanceLevel), LocatorUtils.property(thatLocator, "significanceLevel", rhsSignificanceLevel), lhsSignificanceLevel, rhsSignificanceLevel)) {
                return false;
            }
        }
        {
            String lhsUsedSoftware;
            lhsUsedSoftware = this.getUsedSoftware();
            String rhsUsedSoftware;
            rhsUsedSoftware = that.getUsedSoftware();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "usedSoftware", lhsUsedSoftware), LocatorUtils.property(thatLocator, "usedSoftware", rhsUsedSoftware), lhsUsedSoftware, rhsUsedSoftware)) {
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
            BigDecimal theStatValue;
            theStatValue = this.getStatValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "statValue", theStatValue), currentHashCode, theStatValue);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            BigDecimal theSignificanceLevel;
            theSignificanceLevel = this.getSignificanceLevel();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "significanceLevel", theSignificanceLevel), currentHashCode, theSignificanceLevel);
        }
        {
            String theUsedSoftware;
            theUsedSoftware = this.getUsedSoftware();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "usedSoftware", theUsedSoftware), currentHashCode, theUsedSoftware);
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
        if (draftCopy instanceof TridasStatFoundation) {
            final TridasStatFoundation copy = ((TridasStatFoundation) draftCopy);
            if (this.isSetStatValue()) {
                BigDecimal sourceStatValue;
                sourceStatValue = this.getStatValue();
                BigDecimal copyStatValue = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "statValue", sourceStatValue), sourceStatValue));
                copy.setStatValue(copyStatValue);
            } else {
                copy.statValue = null;
            }
            if (this.isSetType()) {
                ControlledVoc sourceType;
                sourceType = this.getType();
                ControlledVoc copyType = ((ControlledVoc) strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
                copy.setType(copyType);
            } else {
                copy.type = null;
            }
            if (this.isSetSignificanceLevel()) {
                BigDecimal sourceSignificanceLevel;
                sourceSignificanceLevel = this.getSignificanceLevel();
                BigDecimal copySignificanceLevel = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "significanceLevel", sourceSignificanceLevel), sourceSignificanceLevel));
                copy.setSignificanceLevel(copySignificanceLevel);
            } else {
                copy.significanceLevel = null;
            }
            if (this.isSetUsedSoftware()) {
                String sourceUsedSoftware;
                sourceUsedSoftware = this.getUsedSoftware();
                String copyUsedSoftware = ((String) strategy.copy(LocatorUtils.property(locator, "usedSoftware", sourceUsedSoftware), sourceUsedSoftware));
                copy.setUsedSoftware(copyUsedSoftware);
            } else {
                copy.usedSoftware = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasStatFoundation();
    }

}
