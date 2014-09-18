
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
import org.tridas.adapters.IntegerAdapter;


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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}ringCount" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}averageRingWidth" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}nrOfUnmeasuredInnerRings" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}nrOfUnmeasuredOuterRings" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}pith"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}heartwood"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sapwood"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}bark"/>
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
    "ringCount",
    "averageRingWidth",
    "nrOfUnmeasuredInnerRings",
    "nrOfUnmeasuredOuterRings",
    "pith",
    "heartwood",
    "sapwood",
    "bark"
})
@XmlRootElement(name = "woodCompleteness")
public class TridasWoodCompleteness
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer ringCount;
    protected Double averageRingWidth;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer nrOfUnmeasuredInnerRings;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer nrOfUnmeasuredOuterRings;
    @XmlElement(required = true)
    protected TridasPith pith;
    @XmlElement(required = true)
    protected TridasHeartwood heartwood;
    @XmlElement(required = true)
    protected TridasSapwood sapwood;
    @XmlElement(required = true)
    protected TridasBark bark;

    /**
     * Gets the value of the ringCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getRingCount() {
        return ringCount;
    }

    /**
     * Sets the value of the ringCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRingCount(Integer value) {
        this.ringCount = value;
    }

    public boolean isSetRingCount() {
        return (this.ringCount!= null);
    }

    /**
     * Gets the value of the averageRingWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAverageRingWidth() {
        return averageRingWidth;
    }

    /**
     * Sets the value of the averageRingWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAverageRingWidth(Double value) {
        this.averageRingWidth = value;
    }

    public boolean isSetAverageRingWidth() {
        return (this.averageRingWidth!= null);
    }

    /**
     * Gets the value of the nrOfUnmeasuredInnerRings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNrOfUnmeasuredInnerRings() {
        return nrOfUnmeasuredInnerRings;
    }

    /**
     * Sets the value of the nrOfUnmeasuredInnerRings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrOfUnmeasuredInnerRings(Integer value) {
        this.nrOfUnmeasuredInnerRings = value;
    }

    public boolean isSetNrOfUnmeasuredInnerRings() {
        return (this.nrOfUnmeasuredInnerRings!= null);
    }

    /**
     * Gets the value of the nrOfUnmeasuredOuterRings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNrOfUnmeasuredOuterRings() {
        return nrOfUnmeasuredOuterRings;
    }

    /**
     * Sets the value of the nrOfUnmeasuredOuterRings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrOfUnmeasuredOuterRings(Integer value) {
        this.nrOfUnmeasuredOuterRings = value;
    }

    public boolean isSetNrOfUnmeasuredOuterRings() {
        return (this.nrOfUnmeasuredOuterRings!= null);
    }

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
            Integer theRingCount;
            theRingCount = this.getRingCount();
            strategy.appendField(locator, this, "ringCount", buffer, theRingCount);
        }
        {
            Double theAverageRingWidth;
            theAverageRingWidth = this.getAverageRingWidth();
            strategy.appendField(locator, this, "averageRingWidth", buffer, theAverageRingWidth);
        }
        {
            Integer theNrOfUnmeasuredInnerRings;
            theNrOfUnmeasuredInnerRings = this.getNrOfUnmeasuredInnerRings();
            strategy.appendField(locator, this, "nrOfUnmeasuredInnerRings", buffer, theNrOfUnmeasuredInnerRings);
        }
        {
            Integer theNrOfUnmeasuredOuterRings;
            theNrOfUnmeasuredOuterRings = this.getNrOfUnmeasuredOuterRings();
            strategy.appendField(locator, this, "nrOfUnmeasuredOuterRings", buffer, theNrOfUnmeasuredOuterRings);
        }
        {
            TridasPith thePith;
            thePith = this.getPith();
            strategy.appendField(locator, this, "pith", buffer, thePith);
        }
        {
            TridasHeartwood theHeartwood;
            theHeartwood = this.getHeartwood();
            strategy.appendField(locator, this, "heartwood", buffer, theHeartwood);
        }
        {
            TridasSapwood theSapwood;
            theSapwood = this.getSapwood();
            strategy.appendField(locator, this, "sapwood", buffer, theSapwood);
        }
        {
            TridasBark theBark;
            theBark = this.getBark();
            strategy.appendField(locator, this, "bark", buffer, theBark);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasWoodCompleteness)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasWoodCompleteness that = ((TridasWoodCompleteness) object);
        {
            Integer lhsRingCount;
            lhsRingCount = this.getRingCount();
            Integer rhsRingCount;
            rhsRingCount = that.getRingCount();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "ringCount", lhsRingCount), LocatorUtils.property(thatLocator, "ringCount", rhsRingCount), lhsRingCount, rhsRingCount)) {
                return false;
            }
        }
        {
            Double lhsAverageRingWidth;
            lhsAverageRingWidth = this.getAverageRingWidth();
            Double rhsAverageRingWidth;
            rhsAverageRingWidth = that.getAverageRingWidth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "averageRingWidth", lhsAverageRingWidth), LocatorUtils.property(thatLocator, "averageRingWidth", rhsAverageRingWidth), lhsAverageRingWidth, rhsAverageRingWidth)) {
                return false;
            }
        }
        {
            Integer lhsNrOfUnmeasuredInnerRings;
            lhsNrOfUnmeasuredInnerRings = this.getNrOfUnmeasuredInnerRings();
            Integer rhsNrOfUnmeasuredInnerRings;
            rhsNrOfUnmeasuredInnerRings = that.getNrOfUnmeasuredInnerRings();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nrOfUnmeasuredInnerRings", lhsNrOfUnmeasuredInnerRings), LocatorUtils.property(thatLocator, "nrOfUnmeasuredInnerRings", rhsNrOfUnmeasuredInnerRings), lhsNrOfUnmeasuredInnerRings, rhsNrOfUnmeasuredInnerRings)) {
                return false;
            }
        }
        {
            Integer lhsNrOfUnmeasuredOuterRings;
            lhsNrOfUnmeasuredOuterRings = this.getNrOfUnmeasuredOuterRings();
            Integer rhsNrOfUnmeasuredOuterRings;
            rhsNrOfUnmeasuredOuterRings = that.getNrOfUnmeasuredOuterRings();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nrOfUnmeasuredOuterRings", lhsNrOfUnmeasuredOuterRings), LocatorUtils.property(thatLocator, "nrOfUnmeasuredOuterRings", rhsNrOfUnmeasuredOuterRings), lhsNrOfUnmeasuredOuterRings, rhsNrOfUnmeasuredOuterRings)) {
                return false;
            }
        }
        {
            TridasPith lhsPith;
            lhsPith = this.getPith();
            TridasPith rhsPith;
            rhsPith = that.getPith();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "pith", lhsPith), LocatorUtils.property(thatLocator, "pith", rhsPith), lhsPith, rhsPith)) {
                return false;
            }
        }
        {
            TridasHeartwood lhsHeartwood;
            lhsHeartwood = this.getHeartwood();
            TridasHeartwood rhsHeartwood;
            rhsHeartwood = that.getHeartwood();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "heartwood", lhsHeartwood), LocatorUtils.property(thatLocator, "heartwood", rhsHeartwood), lhsHeartwood, rhsHeartwood)) {
                return false;
            }
        }
        {
            TridasSapwood lhsSapwood;
            lhsSapwood = this.getSapwood();
            TridasSapwood rhsSapwood;
            rhsSapwood = that.getSapwood();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sapwood", lhsSapwood), LocatorUtils.property(thatLocator, "sapwood", rhsSapwood), lhsSapwood, rhsSapwood)) {
                return false;
            }
        }
        {
            TridasBark lhsBark;
            lhsBark = this.getBark();
            TridasBark rhsBark;
            rhsBark = that.getBark();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "bark", lhsBark), LocatorUtils.property(thatLocator, "bark", rhsBark), lhsBark, rhsBark)) {
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
            Integer theRingCount;
            theRingCount = this.getRingCount();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "ringCount", theRingCount), currentHashCode, theRingCount);
        }
        {
            Double theAverageRingWidth;
            theAverageRingWidth = this.getAverageRingWidth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "averageRingWidth", theAverageRingWidth), currentHashCode, theAverageRingWidth);
        }
        {
            Integer theNrOfUnmeasuredInnerRings;
            theNrOfUnmeasuredInnerRings = this.getNrOfUnmeasuredInnerRings();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nrOfUnmeasuredInnerRings", theNrOfUnmeasuredInnerRings), currentHashCode, theNrOfUnmeasuredInnerRings);
        }
        {
            Integer theNrOfUnmeasuredOuterRings;
            theNrOfUnmeasuredOuterRings = this.getNrOfUnmeasuredOuterRings();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nrOfUnmeasuredOuterRings", theNrOfUnmeasuredOuterRings), currentHashCode, theNrOfUnmeasuredOuterRings);
        }
        {
            TridasPith thePith;
            thePith = this.getPith();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "pith", thePith), currentHashCode, thePith);
        }
        {
            TridasHeartwood theHeartwood;
            theHeartwood = this.getHeartwood();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "heartwood", theHeartwood), currentHashCode, theHeartwood);
        }
        {
            TridasSapwood theSapwood;
            theSapwood = this.getSapwood();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sapwood", theSapwood), currentHashCode, theSapwood);
        }
        {
            TridasBark theBark;
            theBark = this.getBark();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "bark", theBark), currentHashCode, theBark);
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
        if (draftCopy instanceof TridasWoodCompleteness) {
            final TridasWoodCompleteness copy = ((TridasWoodCompleteness) draftCopy);
            if (this.isSetRingCount()) {
                Integer sourceRingCount;
                sourceRingCount = this.getRingCount();
                Integer copyRingCount = ((Integer) strategy.copy(LocatorUtils.property(locator, "ringCount", sourceRingCount), sourceRingCount));
                copy.setRingCount(copyRingCount);
            } else {
                copy.ringCount = null;
            }
            if (this.isSetAverageRingWidth()) {
                Double sourceAverageRingWidth;
                sourceAverageRingWidth = this.getAverageRingWidth();
                Double copyAverageRingWidth = ((Double) strategy.copy(LocatorUtils.property(locator, "averageRingWidth", sourceAverageRingWidth), sourceAverageRingWidth));
                copy.setAverageRingWidth(copyAverageRingWidth);
            } else {
                copy.averageRingWidth = null;
            }
            if (this.isSetNrOfUnmeasuredInnerRings()) {
                Integer sourceNrOfUnmeasuredInnerRings;
                sourceNrOfUnmeasuredInnerRings = this.getNrOfUnmeasuredInnerRings();
                Integer copyNrOfUnmeasuredInnerRings = ((Integer) strategy.copy(LocatorUtils.property(locator, "nrOfUnmeasuredInnerRings", sourceNrOfUnmeasuredInnerRings), sourceNrOfUnmeasuredInnerRings));
                copy.setNrOfUnmeasuredInnerRings(copyNrOfUnmeasuredInnerRings);
            } else {
                copy.nrOfUnmeasuredInnerRings = null;
            }
            if (this.isSetNrOfUnmeasuredOuterRings()) {
                Integer sourceNrOfUnmeasuredOuterRings;
                sourceNrOfUnmeasuredOuterRings = this.getNrOfUnmeasuredOuterRings();
                Integer copyNrOfUnmeasuredOuterRings = ((Integer) strategy.copy(LocatorUtils.property(locator, "nrOfUnmeasuredOuterRings", sourceNrOfUnmeasuredOuterRings), sourceNrOfUnmeasuredOuterRings));
                copy.setNrOfUnmeasuredOuterRings(copyNrOfUnmeasuredOuterRings);
            } else {
                copy.nrOfUnmeasuredOuterRings = null;
            }
            if (this.isSetPith()) {
                TridasPith sourcePith;
                sourcePith = this.getPith();
                TridasPith copyPith = ((TridasPith) strategy.copy(LocatorUtils.property(locator, "pith", sourcePith), sourcePith));
                copy.setPith(copyPith);
            } else {
                copy.pith = null;
            }
            if (this.isSetHeartwood()) {
                TridasHeartwood sourceHeartwood;
                sourceHeartwood = this.getHeartwood();
                TridasHeartwood copyHeartwood = ((TridasHeartwood) strategy.copy(LocatorUtils.property(locator, "heartwood", sourceHeartwood), sourceHeartwood));
                copy.setHeartwood(copyHeartwood);
            } else {
                copy.heartwood = null;
            }
            if (this.isSetSapwood()) {
                TridasSapwood sourceSapwood;
                sourceSapwood = this.getSapwood();
                TridasSapwood copySapwood = ((TridasSapwood) strategy.copy(LocatorUtils.property(locator, "sapwood", sourceSapwood), sourceSapwood));
                copy.setSapwood(copySapwood);
            } else {
                copy.sapwood = null;
            }
            if (this.isSetBark()) {
                TridasBark sourceBark;
                sourceBark = this.getBark();
                TridasBark copyBark = ((TridasBark) strategy.copy(LocatorUtils.property(locator, "bark", sourceBark), sourceBark));
                copy.setBark(copyBark);
            } else {
                copy.bark = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasWoodCompleteness();
    }

}
