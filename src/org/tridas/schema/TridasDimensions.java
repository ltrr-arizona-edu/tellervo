
package org.tridas.schema;

import java.io.Serializable;
import java.math.BigDecimal;
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
 *         &lt;element ref="{http://www.tridas.org/1.3}unit"/>
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasDimensions)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasDimensions that = ((TridasDimensions) object);
        equalsBuilder.append(this.getUnit(), that.getUnit());
        equalsBuilder.append(this.getHeight(), that.getHeight());
        equalsBuilder.append(this.getWidth(), that.getWidth());
        equalsBuilder.append(this.getDepth(), that.getDepth());
        equalsBuilder.append(this.getDiameter(), that.getDiameter());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasDimensions)) {
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
        hashCodeBuilder.append(this.getUnit());
        hashCodeBuilder.append(this.getHeight());
        hashCodeBuilder.append(this.getWidth());
        hashCodeBuilder.append(this.getDepth());
        hashCodeBuilder.append(this.getDiameter());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasUnit theUnit;
            theUnit = this.getUnit();
            toStringBuilder.append("unit", theUnit);
        }
        {
            BigDecimal theHeight;
            theHeight = this.getHeight();
            toStringBuilder.append("height", theHeight);
        }
        {
            BigDecimal theWidth;
            theWidth = this.getWidth();
            toStringBuilder.append("width", theWidth);
        }
        {
            BigDecimal theDepth;
            theDepth = this.getDepth();
            toStringBuilder.append("depth", theDepth);
        }
        {
            BigDecimal theDiameter;
            theDiameter = this.getDiameter();
            toStringBuilder.append("diameter", theDiameter);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasDimensions copy = ((target == null)?((TridasDimensions) createCopy()):((TridasDimensions) target));
        if (this.isSetUnit()) {
            TridasUnit sourceUnit;
            sourceUnit = this.getUnit();
            TridasUnit copyUnit = ((TridasUnit) copyBuilder.copy(sourceUnit));
            copy.setUnit(copyUnit);
        } else {
            copy.unit = null;
        }
        if (this.isSetHeight()) {
            BigDecimal sourceHeight;
            sourceHeight = this.getHeight();
            BigDecimal copyHeight = ((BigDecimal) copyBuilder.copy(sourceHeight));
            copy.setHeight(copyHeight);
        } else {
            copy.height = null;
        }
        if (this.isSetWidth()) {
            BigDecimal sourceWidth;
            sourceWidth = this.getWidth();
            BigDecimal copyWidth = ((BigDecimal) copyBuilder.copy(sourceWidth));
            copy.setWidth(copyWidth);
        } else {
            copy.width = null;
        }
        if (this.isSetDepth()) {
            BigDecimal sourceDepth;
            sourceDepth = this.getDepth();
            BigDecimal copyDepth = ((BigDecimal) copyBuilder.copy(sourceDepth));
            copy.setDepth(copyDepth);
        } else {
            copy.depth = null;
        }
        if (this.isSetDiameter()) {
            BigDecimal sourceDiameter;
            sourceDiameter = this.getDiameter();
            BigDecimal copyDiameter = ((BigDecimal) copyBuilder.copy(sourceDiameter));
            copy.setDiameter(copyDiameter);
        } else {
            copy.diameter = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasDimensions();
    }

}
