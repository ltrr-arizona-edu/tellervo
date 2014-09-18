
package org.tridas.schema;

import java.math.BigDecimal;
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
 *     &lt;extension base="{http://www.tridas.org/1.2.2}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}woodCompleteness" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}azimuth" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "woodCompleteness",
    "azimuth",
    "genericFields",
    "measurementSeries"
})
@XmlRootElement(name = "radius")
public class TridasRadius
    extends TridasEntity
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected TridasWoodCompleteness woodCompleteness;
    protected BigDecimal azimuth;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected List<TridasMeasurementSeries> measurementSeries;

    /**
     * Gets the value of the woodCompleteness property.
     * 
     * @return
     *     possible object is
     *     {@link TridasWoodCompleteness }
     *     
     */
    public TridasWoodCompleteness getWoodCompleteness() {
        return woodCompleteness;
    }

    /**
     * Sets the value of the woodCompleteness property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasWoodCompleteness }
     *     
     */
    public void setWoodCompleteness(TridasWoodCompleteness value) {
        this.woodCompleteness = value;
    }

    public boolean isSetWoodCompleteness() {
        return (this.woodCompleteness!= null);
    }

    /**
     * Gets the value of the azimuth property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAzimuth() {
        return azimuth;
    }

    /**
     * Sets the value of the azimuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAzimuth(BigDecimal value) {
        this.azimuth = value;
    }

    public boolean isSetAzimuth() {
        return (this.azimuth!= null);
    }

    /**
     * Gets the value of the genericFields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericFields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasGenericField }
     * 
     * 
     */
    public List<TridasGenericField> getGenericFields() {
        if (genericFields == null) {
            genericFields = new ArrayList<TridasGenericField>();
        }
        return this.genericFields;
    }

    public boolean isSetGenericFields() {
        return ((this.genericFields!= null)&&(!this.genericFields.isEmpty()));
    }

    public void unsetGenericFields() {
        this.genericFields = null;
    }

    /**
     * Gets the value of the measurementSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the measurementSeries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMeasurementSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasMeasurementSeries }
     * 
     * 
     */
    public List<TridasMeasurementSeries> getMeasurementSeries() {
        if (measurementSeries == null) {
            measurementSeries = new ArrayList<TridasMeasurementSeries>();
        }
        return this.measurementSeries;
    }

    public boolean isSetMeasurementSeries() {
        return ((this.measurementSeries!= null)&&(!this.measurementSeries.isEmpty()));
    }

    public void unsetMeasurementSeries() {
        this.measurementSeries = null;
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
        super.appendFields(locator, buffer, strategy);
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            strategy.appendField(locator, this, "woodCompleteness", buffer, theWoodCompleteness);
        }
        {
            BigDecimal theAzimuth;
            theAzimuth = this.getAzimuth();
            strategy.appendField(locator, this, "azimuth", buffer, theAzimuth);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            strategy.appendField(locator, this, "genericFields", buffer, theGenericFields);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
            strategy.appendField(locator, this, "measurementSeries", buffer, theMeasurementSeries);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasRadius)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasRadius that = ((TridasRadius) object);
        {
            TridasWoodCompleteness lhsWoodCompleteness;
            lhsWoodCompleteness = this.getWoodCompleteness();
            TridasWoodCompleteness rhsWoodCompleteness;
            rhsWoodCompleteness = that.getWoodCompleteness();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "woodCompleteness", lhsWoodCompleteness), LocatorUtils.property(thatLocator, "woodCompleteness", rhsWoodCompleteness), lhsWoodCompleteness, rhsWoodCompleteness)) {
                return false;
            }
        }
        {
            BigDecimal lhsAzimuth;
            lhsAzimuth = this.getAzimuth();
            BigDecimal rhsAzimuth;
            rhsAzimuth = that.getAzimuth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "azimuth", lhsAzimuth), LocatorUtils.property(thatLocator, "azimuth", rhsAzimuth), lhsAzimuth, rhsAzimuth)) {
                return false;
            }
        }
        {
            List<TridasGenericField> lhsGenericFields;
            lhsGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            List<TridasGenericField> rhsGenericFields;
            rhsGenericFields = (that.isSetGenericFields()?that.getGenericFields():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "genericFields", lhsGenericFields), LocatorUtils.property(thatLocator, "genericFields", rhsGenericFields), lhsGenericFields, rhsGenericFields)) {
                return false;
            }
        }
        {
            List<TridasMeasurementSeries> lhsMeasurementSeries;
            lhsMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
            List<TridasMeasurementSeries> rhsMeasurementSeries;
            rhsMeasurementSeries = (that.isSetMeasurementSeries()?that.getMeasurementSeries():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "measurementSeries", lhsMeasurementSeries), LocatorUtils.property(thatLocator, "measurementSeries", rhsMeasurementSeries), lhsMeasurementSeries, rhsMeasurementSeries)) {
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
        int currentHashCode = super.hashCode(locator, strategy);
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "woodCompleteness", theWoodCompleteness), currentHashCode, theWoodCompleteness);
        }
        {
            BigDecimal theAzimuth;
            theAzimuth = this.getAzimuth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "azimuth", theAzimuth), currentHashCode, theAzimuth);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "genericFields", theGenericFields), currentHashCode, theGenericFields);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measurementSeries", theMeasurementSeries), currentHashCode, theMeasurementSeries);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof TridasRadius) {
            final TridasRadius copy = ((TridasRadius) draftCopy);
            if (this.isSetWoodCompleteness()) {
                TridasWoodCompleteness sourceWoodCompleteness;
                sourceWoodCompleteness = this.getWoodCompleteness();
                TridasWoodCompleteness copyWoodCompleteness = ((TridasWoodCompleteness) strategy.copy(LocatorUtils.property(locator, "woodCompleteness", sourceWoodCompleteness), sourceWoodCompleteness));
                copy.setWoodCompleteness(copyWoodCompleteness);
            } else {
                copy.woodCompleteness = null;
            }
            if (this.isSetAzimuth()) {
                BigDecimal sourceAzimuth;
                sourceAzimuth = this.getAzimuth();
                BigDecimal copyAzimuth = ((BigDecimal) strategy.copy(LocatorUtils.property(locator, "azimuth", sourceAzimuth), sourceAzimuth));
                copy.setAzimuth(copyAzimuth);
            } else {
                copy.azimuth = null;
            }
            if (this.isSetGenericFields()) {
                List<TridasGenericField> sourceGenericFields;
                sourceGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
                @SuppressWarnings("unchecked")
                List<TridasGenericField> copyGenericFields = ((List<TridasGenericField> ) strategy.copy(LocatorUtils.property(locator, "genericFields", sourceGenericFields), sourceGenericFields));
                copy.unsetGenericFields();
                List<TridasGenericField> uniqueGenericFieldsl = copy.getGenericFields();
                uniqueGenericFieldsl.addAll(copyGenericFields);
            } else {
                copy.unsetGenericFields();
            }
            if (this.isSetMeasurementSeries()) {
                List<TridasMeasurementSeries> sourceMeasurementSeries;
                sourceMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
                @SuppressWarnings("unchecked")
                List<TridasMeasurementSeries> copyMeasurementSeries = ((List<TridasMeasurementSeries> ) strategy.copy(LocatorUtils.property(locator, "measurementSeries", sourceMeasurementSeries), sourceMeasurementSeries));
                copy.unsetMeasurementSeries();
                List<TridasMeasurementSeries> uniqueMeasurementSeriesl = copy.getMeasurementSeries();
                uniqueMeasurementSeriesl.addAll(copyMeasurementSeries);
            } else {
                copy.unsetMeasurementSeries();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasRadius();
    }

    /**
     * Sets the value of the genericFields property.
     * 
     * @param genericFields
     *     allowed object is
     *     {@link TridasGenericField }
     *     
     */
    public void setGenericFields(List<TridasGenericField> genericFields) {
        this.genericFields = genericFields;
    }

    /**
     * Sets the value of the measurementSeries property.
     * 
     * @param measurementSeries
     *     allowed object is
     *     {@link TridasMeasurementSeries }
     *     
     */
    public void setMeasurementSeries(List<TridasMeasurementSeries> measurementSeries) {
        this.measurementSeries = measurementSeries;
    }

}
