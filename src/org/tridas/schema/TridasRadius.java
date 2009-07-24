
package org.tridas.schema;

import java.io.Serializable;
import java.math.BigDecimal;
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


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.3}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}woodCompleteness" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}azimuth" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasRadius)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasRadius that = ((TridasRadius) object);
        equalsBuilder.append(this.getWoodCompleteness(), that.getWoodCompleteness());
        equalsBuilder.append(this.getAzimuth(), that.getAzimuth());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getMeasurementSeries(), that.getMeasurementSeries());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasRadius)) {
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
        super.hashCode(hashCodeBuilder);
        hashCodeBuilder.append(this.getWoodCompleteness());
        hashCodeBuilder.append(this.getAzimuth());
        hashCodeBuilder.append(this.getGenericFields());
        hashCodeBuilder.append(this.getMeasurementSeries());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            toStringBuilder.append("woodCompleteness", theWoodCompleteness);
        }
        {
            BigDecimal theAzimuth;
            theAzimuth = this.getAzimuth();
            toStringBuilder.append("azimuth", theAzimuth);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = this.getGenericFields();
            toStringBuilder.append("genericFields", theGenericFields);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = this.getMeasurementSeries();
            toStringBuilder.append("measurementSeries", theMeasurementSeries);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasRadius copy = ((target == null)?((TridasRadius) createCopy()):((TridasRadius) target));
        super.copyTo(copy, copyBuilder);
        {
            TridasWoodCompleteness sourceWoodCompleteness;
            sourceWoodCompleteness = this.getWoodCompleteness();
            TridasWoodCompleteness copyWoodCompleteness = ((TridasWoodCompleteness) copyBuilder.copy(sourceWoodCompleteness));
            copy.setWoodCompleteness(copyWoodCompleteness);
        }
        {
            BigDecimal sourceAzimuth;
            sourceAzimuth = this.getAzimuth();
            BigDecimal copyAzimuth = ((BigDecimal) copyBuilder.copy(sourceAzimuth));
            copy.setAzimuth(copyAzimuth);
        }
        {
            List<TridasGenericField> sourceGenericFields;
            sourceGenericFields = this.getGenericFields();
            List<TridasGenericField> copyGenericFields = ((List<TridasGenericField> ) copyBuilder.copy(sourceGenericFields));
            copy.setGenericFields(copyGenericFields);
        }
        {
            List<TridasMeasurementSeries> sourceMeasurementSeries;
            sourceMeasurementSeries = this.getMeasurementSeries();
            List<TridasMeasurementSeries> copyMeasurementSeries = ((List<TridasMeasurementSeries> ) copyBuilder.copy(sourceMeasurementSeries));
            copy.setMeasurementSeries(copyMeasurementSeries);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasRadius();
    }

}
