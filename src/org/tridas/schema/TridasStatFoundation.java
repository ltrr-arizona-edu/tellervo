
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
 *         &lt;element ref="{http://www.tridas.org/1.3}statValue"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}type"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}significanceLevel" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}usedSoftware"/>
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasStatFoundation)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasStatFoundation that = ((TridasStatFoundation) object);
        equalsBuilder.append(this.getStatValue(), that.getStatValue());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getSignificanceLevel(), that.getSignificanceLevel());
        equalsBuilder.append(this.getUsedSoftware(), that.getUsedSoftware());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasStatFoundation)) {
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
        hashCodeBuilder.append(this.getStatValue());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getSignificanceLevel());
        hashCodeBuilder.append(this.getUsedSoftware());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            BigDecimal theStatValue;
            theStatValue = this.getStatValue();
            toStringBuilder.append("statValue", theStatValue);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            BigDecimal theSignificanceLevel;
            theSignificanceLevel = this.getSignificanceLevel();
            toStringBuilder.append("significanceLevel", theSignificanceLevel);
        }
        {
            String theUsedSoftware;
            theUsedSoftware = this.getUsedSoftware();
            toStringBuilder.append("usedSoftware", theUsedSoftware);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasStatFoundation copy = ((target == null)?((TridasStatFoundation) createCopy()):((TridasStatFoundation) target));
        {
            BigDecimal sourceStatValue;
            sourceStatValue = this.getStatValue();
            BigDecimal copyStatValue = ((BigDecimal) copyBuilder.copy(sourceStatValue));
            copy.setStatValue(copyStatValue);
        }
        {
            ControlledVoc sourceType;
            sourceType = this.getType();
            ControlledVoc copyType = ((ControlledVoc) copyBuilder.copy(sourceType));
            copy.setType(copyType);
        }
        {
            BigDecimal sourceSignificanceLevel;
            sourceSignificanceLevel = this.getSignificanceLevel();
            BigDecimal copySignificanceLevel = ((BigDecimal) copyBuilder.copy(sourceSignificanceLevel));
            copy.setSignificanceLevel(copySignificanceLevel);
        }
        {
            String sourceUsedSoftware;
            sourceUsedSoftware = this.getUsedSoftware();
            String copyUsedSoftware = ((String) copyBuilder.copy(sourceUsedSoftware));
            copy.setUsedSoftware(copyUsedSoftware);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasStatFoundation();
    }

}
