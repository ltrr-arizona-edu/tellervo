
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}unit"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="diameter" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *             &lt;element name="depth" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;/sequence>
 *         &lt;/choice>
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
    "unit",
    "height",
    "width",
    "depth",
    "diameter"
})
@XmlRootElement(name = "dimensions")
public class TridasDimensions
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected TridasUnit unit;
    @XmlElement(required = true)
    protected BigDecimal height;
    protected BigDecimal width;
    protected BigDecimal depth;
    protected BigDecimal diameter;

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
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHeight(BigDecimal value) {
        this.height = value;
    }

    public boolean isSetHeight() {
        return (this.height!= null);
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWidth(BigDecimal value) {
        this.width = value;
    }

    public boolean isSetWidth() {
        return (this.width!= null);
    }

    /**
     * Gets the value of the depth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDepth() {
        return depth;
    }

    /**
     * Sets the value of the depth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDepth(BigDecimal value) {
        this.depth = value;
    }

    public boolean isSetDepth() {
        return (this.depth!= null);
    }

    /**
     * Gets the value of the diameter property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiameter() {
        return diameter;
    }

    /**
     * Sets the value of the diameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiameter(BigDecimal value) {
        this.diameter = value;
    }

    public boolean isSetDiameter() {
        return (this.diameter!= null);
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
            TridasUnit theUnit;
            theUnit = this.getUnit();
            strategy.appendField(locator, this, "unit", buffer, theUnit);
        }
        {
            BigDecimal theHeight;
            theHeight = this.getHeight();
            strategy.appendField(locator, this, "height", buffer, theHeight);
        }
        {
            BigDecimal theWidth;
            theWidth = this.getWidth();
            strategy.appendField(locator, this, "width", buffer, theWidth);
        }
        {
            BigDecimal theDepth;
            theDepth = this.getDepth();
            strategy.appendField(locator, this, "depth", buffer, theDepth);
        }
        {
            BigDecimal theDiameter;
            theDiameter = this.getDiameter();
            strategy.appendField(locator, this, "diameter", buffer, theDiameter);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasDimensions)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasDimensions that = ((TridasDimensions) object);
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
            BigDecimal lhsHeight;
            lhsHeight = this.getHeight();
            BigDecimal rhsHeight;
            rhsHeight = that.getHeight();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "height", lhsHeight), LocatorUtils.property(thatLocator, "height", rhsHeight), lhsHeight, rhsHeight)) {
                return false;
            }
        }
        {
            BigDecimal lhsWidth;
            lhsWidth = this.getWidth();
            BigDecimal rhsWidth;
            rhsWidth = that.getWidth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "width", lhsWidth), LocatorUtils.property(thatLocator, "width", rhsWidth), lhsWidth, rhsWidth)) {
                return false;
            }
        }
        {
            BigDecimal lhsDepth;
            lhsDepth = this.getDepth();
            BigDecimal rhsDepth;
            rhsDepth = that.getDepth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "depth", lhsDepth), LocatorUtils.property(thatLocator, "depth", rhsDepth), lhsDepth, rhsDepth)) {
                return false;
            }
        }
        {
            BigDecimal lhsDiameter;
            lhsDiameter = this.getDiameter();
            BigDecimal rhsDiameter;
            rhsDiameter = that.getDiameter();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "diameter", lhsDiameter), LocatorUtils.property(thatLocator, "diameter", rhsDiameter), lhsDiameter, rhsDiameter)) {
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
            TridasUnit theUnit;
            theUnit = this.getUnit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "unit", theUnit), currentHashCode, theUnit);
        }
        {
            BigDecimal theHeight;
            theHeight = this.getHeight();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "height", theHeight), currentHashCode, theHeight);
        }
        {
            BigDecimal theWidth;
            theWidth = this.getWidth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "width", theWidth), currentHashCode, theWidth);
        }
        {
            BigDecimal theDepth;
            theDepth = this.getDepth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "depth", theDepth), currentHashCode, theDepth);
        }
        {
            BigDecimal theDiameter;
            theDiameter = this.getDiameter();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "diameter", theDiameter), currentHashCode, theDiameter);
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
        if (draftCopy instanceof TridasDimensions) {
            final TridasDimensions copy = ((TridasDimensions) draftCopy);
            if (this.isSetUnit()) {
                TridasUnit sourceUnit;
                sourceUnit = this.getUnit();
                TridasUnit copyUnit = ((TridasUnit) strategy.copy(LocatorUtils.property(locator, "unit", sourceUnit), sourceUnit));
                copy.setUnit(copyUnit);
            } else {
                copy.unit = null;
            }
            if (this.isSetHeight()) {
                BigDecimal sourceHeight;
                sourceHeight = this.getHeight();
                BigDecimal copyHeight = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "height", sourceHeight), sourceHeight));
                copy.setHeight(copyHeight);
            } else {
                copy.height = null;
            }
            if (this.isSetWidth()) {
                BigDecimal sourceWidth;
                sourceWidth = this.getWidth();
                BigDecimal copyWidth = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "width", sourceWidth), sourceWidth));
                copy.setWidth(copyWidth);
            } else {
                copy.width = null;
            }
            if (this.isSetDepth()) {
                BigDecimal sourceDepth;
                sourceDepth = this.getDepth();
                BigDecimal copyDepth = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "depth", sourceDepth), sourceDepth));
                copy.setDepth(copyDepth);
            } else {
                copy.depth = null;
            }
            if (this.isSetDiameter()) {
                BigDecimal sourceDiameter;
                sourceDiameter = this.getDiameter();
                BigDecimal copyDiameter = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "diameter", sourceDiameter), sourceDiameter));
                copy.setDiameter(copyDiameter);
            } else {
                copy.diameter = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasDimensions();
    }

}
