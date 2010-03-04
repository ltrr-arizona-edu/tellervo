
package org.tridas.schema;

import java.io.Serializable;
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
 *         &lt;element ref="{http://www.tridas.org/1.3}pith"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}heartwood"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}sapwood"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}bark"/>
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
    "pith",
    "heartwood",
    "sapwood",
    "bark"
})
@XmlRootElement(name = "woodCompleteness")
public class TridasWoodCompleteness
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected TridasPith pith;
    @XmlElement(required = true)
    protected TridasHeartwood heartwood;
    @XmlElement(required = true)
    protected TridasSapwood sapwood;
    @XmlElement(required = true)
    protected TridasBark bark;

    /**
     * Gets the value of the pith property.
     * 
     * @return
     *     possible object is
     *     {@link TridasPith }
     *     
     */
    public TridasPith getPith() {
        return pith;
    }

    /**
     * Sets the value of the pith property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasPith }
     *     
     */
    public void setPith(TridasPith value) {
        this.pith = value;
    }

    public boolean isSetPith() {
        return (this.pith!= null);
    }

    /**
     * Gets the value of the heartwood property.
     * 
     * @return
     *     possible object is
     *     {@link TridasHeartwood }
     *     
     */
    public TridasHeartwood getHeartwood() {
        return heartwood;
    }

    /**
     * Sets the value of the heartwood property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasHeartwood }
     *     
     */
    public void setHeartwood(TridasHeartwood value) {
        this.heartwood = value;
    }

    public boolean isSetHeartwood() {
        return (this.heartwood!= null);
    }

    /**
     * Gets the value of the sapwood property.
     * 
     * @return
     *     possible object is
     *     {@link TridasSapwood }
     *     
     */
    public TridasSapwood getSapwood() {
        return sapwood;
    }

    /**
     * Sets the value of the sapwood property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasSapwood }
     *     
     */
    public void setSapwood(TridasSapwood value) {
        this.sapwood = value;
    }

    public boolean isSetSapwood() {
        return (this.sapwood!= null);
    }

    /**
     * Gets the value of the bark property.
     * 
     * @return
     *     possible object is
     *     {@link TridasBark }
     *     
     */
    public TridasBark getBark() {
        return bark;
    }

    /**
     * Sets the value of the bark property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasBark }
     *     
     */
    public void setBark(TridasBark value) {
        this.bark = value;
    }

    public boolean isSetBark() {
        return (this.bark!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasWoodCompleteness)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasWoodCompleteness that = ((TridasWoodCompleteness) object);
        equalsBuilder.append(this.getPith(), that.getPith());
        equalsBuilder.append(this.getHeartwood(), that.getHeartwood());
        equalsBuilder.append(this.getSapwood(), that.getSapwood());
        equalsBuilder.append(this.getBark(), that.getBark());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasWoodCompleteness)) {
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
        hashCodeBuilder.append(this.getPith());
        hashCodeBuilder.append(this.getHeartwood());
        hashCodeBuilder.append(this.getSapwood());
        hashCodeBuilder.append(this.getBark());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasPith thePith;
            thePith = this.getPith();
            toStringBuilder.append("pith", thePith);
        }
        {
            TridasHeartwood theHeartwood;
            theHeartwood = this.getHeartwood();
            toStringBuilder.append("heartwood", theHeartwood);
        }
        {
            TridasSapwood theSapwood;
            theSapwood = this.getSapwood();
            toStringBuilder.append("sapwood", theSapwood);
        }
        {
            TridasBark theBark;
            theBark = this.getBark();
            toStringBuilder.append("bark", theBark);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasWoodCompleteness copy = ((target == null)?((TridasWoodCompleteness) createCopy()):((TridasWoodCompleteness) target));
        if (this.isSetPith()) {
            TridasPith sourcePith;
            sourcePith = this.getPith();
            TridasPith copyPith = ((TridasPith) copyBuilder.copy(sourcePith));
            copy.setPith(copyPith);
        } else {
            copy.pith = null;
        }
        if (this.isSetHeartwood()) {
            TridasHeartwood sourceHeartwood;
            sourceHeartwood = this.getHeartwood();
            TridasHeartwood copyHeartwood = ((TridasHeartwood) copyBuilder.copy(sourceHeartwood));
            copy.setHeartwood(copyHeartwood);
        } else {
            copy.heartwood = null;
        }
        if (this.isSetSapwood()) {
            TridasSapwood sourceSapwood;
            sourceSapwood = this.getSapwood();
            TridasSapwood copySapwood = ((TridasSapwood) copyBuilder.copy(sourceSapwood));
            copy.setSapwood(copySapwood);
        } else {
            copy.sapwood = null;
        }
        if (this.isSetBark()) {
            TridasBark sourceBark;
            sourceBark = this.getBark();
            TridasBark copyBark = ((TridasBark) copyBuilder.copy(sourceBark));
            copy.setBark(copyBark);
        } else {
            copy.bark = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasWoodCompleteness();
    }

}
