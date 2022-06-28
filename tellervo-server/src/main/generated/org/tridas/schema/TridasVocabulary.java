
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
 *       &lt;all>
 *         &lt;element name="project.category" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}category" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="project.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="object.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="element.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="sample.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="derivedSeries.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="element.taxon" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}taxon" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="element.shape" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}shape" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="measurementSeries.measuringMethod" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}measuringMethod" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="values.variable" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}variable" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="values.remark" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}remark" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="location.type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}locationType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="global.unit" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.2}unit" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "vocabulary")
public class TridasVocabulary
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "project.category")
    protected TridasVocabulary.ProjectCategory projectCategory;
    @XmlElement(name = "project.type")
    protected TridasVocabulary.ProjectType projectType;
    @XmlElement(name = "object.type")
    protected TridasVocabulary.ObjectType objectType;
    @XmlElement(name = "element.type")
    protected TridasVocabulary.ElementType elementType;
    @XmlElement(name = "sample.type")
    protected TridasVocabulary.SampleType sampleType;
    @XmlElement(name = "derivedSeries.type")
    protected TridasVocabulary.DerivedSeriesType derivedSeriesType;
    @XmlElement(name = "element.taxon")
    protected TridasVocabulary.ElementTaxon elementTaxon;
    @XmlElement(name = "element.shape")
    protected TridasVocabulary.ElementShape elementShape;
    @XmlElement(name = "measurementSeries.measuringMethod")
    protected TridasVocabulary.MeasurementSeriesMeasuringMethod measurementSeriesMeasuringMethod;
    @XmlElement(name = "values.variable")
    protected TridasVocabulary.ValuesVariable valuesVariable;
    @XmlElement(name = "values.remark")
    protected TridasVocabulary.ValuesRemark valuesRemark;
    @XmlElement(name = "location.type")
    protected TridasVocabulary.LocationType locationType;
    @XmlElement(name = "global.unit")
    protected TridasVocabulary.GlobalUnit globalUnit;

    /**
     * Gets the value of the projectCategory property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ProjectCategory }
     *     
     */
    public TridasVocabulary.ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    /**
     * Sets the value of the projectCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ProjectCategory }
     *     
     */
    public void setProjectCategory(TridasVocabulary.ProjectCategory value) {
        this.projectCategory = value;
    }

    public boolean isSetProjectCategory() {
        return (this.projectCategory!= null);
    }

    /**
     * Gets the value of the projectType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ProjectType }
     *     
     */
    public TridasVocabulary.ProjectType getProjectType() {
        return projectType;
    }

    /**
     * Sets the value of the projectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ProjectType }
     *     
     */
    public void setProjectType(TridasVocabulary.ProjectType value) {
        this.projectType = value;
    }

    public boolean isSetProjectType() {
        return (this.projectType!= null);
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ObjectType }
     *     
     */
    public TridasVocabulary.ObjectType getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ObjectType }
     *     
     */
    public void setObjectType(TridasVocabulary.ObjectType value) {
        this.objectType = value;
    }

    public boolean isSetObjectType() {
        return (this.objectType!= null);
    }

    /**
     * Gets the value of the elementType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ElementType }
     *     
     */
    public TridasVocabulary.ElementType getElementType() {
        return elementType;
    }

    /**
     * Sets the value of the elementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ElementType }
     *     
     */
    public void setElementType(TridasVocabulary.ElementType value) {
        this.elementType = value;
    }

    public boolean isSetElementType() {
        return (this.elementType!= null);
    }

    /**
     * Gets the value of the sampleType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.SampleType }
     *     
     */
    public TridasVocabulary.SampleType getSampleType() {
        return sampleType;
    }

    /**
     * Sets the value of the sampleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.SampleType }
     *     
     */
    public void setSampleType(TridasVocabulary.SampleType value) {
        this.sampleType = value;
    }

    public boolean isSetSampleType() {
        return (this.sampleType!= null);
    }

    /**
     * Gets the value of the derivedSeriesType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.DerivedSeriesType }
     *     
     */
    public TridasVocabulary.DerivedSeriesType getDerivedSeriesType() {
        return derivedSeriesType;
    }

    /**
     * Sets the value of the derivedSeriesType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.DerivedSeriesType }
     *     
     */
    public void setDerivedSeriesType(TridasVocabulary.DerivedSeriesType value) {
        this.derivedSeriesType = value;
    }

    public boolean isSetDerivedSeriesType() {
        return (this.derivedSeriesType!= null);
    }

    /**
     * Gets the value of the elementTaxon property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ElementTaxon }
     *     
     */
    public TridasVocabulary.ElementTaxon getElementTaxon() {
        return elementTaxon;
    }

    /**
     * Sets the value of the elementTaxon property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ElementTaxon }
     *     
     */
    public void setElementTaxon(TridasVocabulary.ElementTaxon value) {
        this.elementTaxon = value;
    }

    public boolean isSetElementTaxon() {
        return (this.elementTaxon!= null);
    }

    /**
     * Gets the value of the elementShape property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ElementShape }
     *     
     */
    public TridasVocabulary.ElementShape getElementShape() {
        return elementShape;
    }

    /**
     * Sets the value of the elementShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ElementShape }
     *     
     */
    public void setElementShape(TridasVocabulary.ElementShape value) {
        this.elementShape = value;
    }

    public boolean isSetElementShape() {
        return (this.elementShape!= null);
    }

    /**
     * Gets the value of the measurementSeriesMeasuringMethod property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.MeasurementSeriesMeasuringMethod }
     *     
     */
    public TridasVocabulary.MeasurementSeriesMeasuringMethod getMeasurementSeriesMeasuringMethod() {
        return measurementSeriesMeasuringMethod;
    }

    /**
     * Sets the value of the measurementSeriesMeasuringMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.MeasurementSeriesMeasuringMethod }
     *     
     */
    public void setMeasurementSeriesMeasuringMethod(TridasVocabulary.MeasurementSeriesMeasuringMethod value) {
        this.measurementSeriesMeasuringMethod = value;
    }

    public boolean isSetMeasurementSeriesMeasuringMethod() {
        return (this.measurementSeriesMeasuringMethod!= null);
    }

    /**
     * Gets the value of the valuesVariable property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ValuesVariable }
     *     
     */
    public TridasVocabulary.ValuesVariable getValuesVariable() {
        return valuesVariable;
    }

    /**
     * Sets the value of the valuesVariable property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ValuesVariable }
     *     
     */
    public void setValuesVariable(TridasVocabulary.ValuesVariable value) {
        this.valuesVariable = value;
    }

    public boolean isSetValuesVariable() {
        return (this.valuesVariable!= null);
    }

    /**
     * Gets the value of the valuesRemark property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.ValuesRemark }
     *     
     */
    public TridasVocabulary.ValuesRemark getValuesRemark() {
        return valuesRemark;
    }

    /**
     * Sets the value of the valuesRemark property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.ValuesRemark }
     *     
     */
    public void setValuesRemark(TridasVocabulary.ValuesRemark value) {
        this.valuesRemark = value;
    }

    public boolean isSetValuesRemark() {
        return (this.valuesRemark!= null);
    }

    /**
     * Gets the value of the locationType property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.LocationType }
     *     
     */
    public TridasVocabulary.LocationType getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.LocationType }
     *     
     */
    public void setLocationType(TridasVocabulary.LocationType value) {
        this.locationType = value;
    }

    public boolean isSetLocationType() {
        return (this.locationType!= null);
    }

    /**
     * Gets the value of the globalUnit property.
     * 
     * @return
     *     possible object is
     *     {@link TridasVocabulary.GlobalUnit }
     *     
     */
    public TridasVocabulary.GlobalUnit getGlobalUnit() {
        return globalUnit;
    }

    /**
     * Sets the value of the globalUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasVocabulary.GlobalUnit }
     *     
     */
    public void setGlobalUnit(TridasVocabulary.GlobalUnit value) {
        this.globalUnit = value;
    }

    public boolean isSetGlobalUnit() {
        return (this.globalUnit!= null);
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
            TridasVocabulary.ProjectCategory theProjectCategory;
            theProjectCategory = this.getProjectCategory();
            strategy.appendField(locator, this, "projectCategory", buffer, theProjectCategory);
        }
        {
            TridasVocabulary.ProjectType theProjectType;
            theProjectType = this.getProjectType();
            strategy.appendField(locator, this, "projectType", buffer, theProjectType);
        }
        {
            TridasVocabulary.ObjectType theObjectType;
            theObjectType = this.getObjectType();
            strategy.appendField(locator, this, "objectType", buffer, theObjectType);
        }
        {
            TridasVocabulary.ElementType theElementType;
            theElementType = this.getElementType();
            strategy.appendField(locator, this, "elementType", buffer, theElementType);
        }
        {
            TridasVocabulary.SampleType theSampleType;
            theSampleType = this.getSampleType();
            strategy.appendField(locator, this, "sampleType", buffer, theSampleType);
        }
        {
            TridasVocabulary.DerivedSeriesType theDerivedSeriesType;
            theDerivedSeriesType = this.getDerivedSeriesType();
            strategy.appendField(locator, this, "derivedSeriesType", buffer, theDerivedSeriesType);
        }
        {
            TridasVocabulary.ElementTaxon theElementTaxon;
            theElementTaxon = this.getElementTaxon();
            strategy.appendField(locator, this, "elementTaxon", buffer, theElementTaxon);
        }
        {
            TridasVocabulary.ElementShape theElementShape;
            theElementShape = this.getElementShape();
            strategy.appendField(locator, this, "elementShape", buffer, theElementShape);
        }
        {
            TridasVocabulary.MeasurementSeriesMeasuringMethod theMeasurementSeriesMeasuringMethod;
            theMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
            strategy.appendField(locator, this, "measurementSeriesMeasuringMethod", buffer, theMeasurementSeriesMeasuringMethod);
        }
        {
            TridasVocabulary.ValuesVariable theValuesVariable;
            theValuesVariable = this.getValuesVariable();
            strategy.appendField(locator, this, "valuesVariable", buffer, theValuesVariable);
        }
        {
            TridasVocabulary.ValuesRemark theValuesRemark;
            theValuesRemark = this.getValuesRemark();
            strategy.appendField(locator, this, "valuesRemark", buffer, theValuesRemark);
        }
        {
            TridasVocabulary.LocationType theLocationType;
            theLocationType = this.getLocationType();
            strategy.appendField(locator, this, "locationType", buffer, theLocationType);
        }
        {
            TridasVocabulary.GlobalUnit theGlobalUnit;
            theGlobalUnit = this.getGlobalUnit();
            strategy.appendField(locator, this, "globalUnit", buffer, theGlobalUnit);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasVocabulary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasVocabulary that = ((TridasVocabulary) object);
        {
            TridasVocabulary.ProjectCategory lhsProjectCategory;
            lhsProjectCategory = this.getProjectCategory();
            TridasVocabulary.ProjectCategory rhsProjectCategory;
            rhsProjectCategory = that.getProjectCategory();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "projectCategory", lhsProjectCategory), LocatorUtils.property(thatLocator, "projectCategory", rhsProjectCategory), lhsProjectCategory, rhsProjectCategory)) {
                return false;
            }
        }
        {
            TridasVocabulary.ProjectType lhsProjectType;
            lhsProjectType = this.getProjectType();
            TridasVocabulary.ProjectType rhsProjectType;
            rhsProjectType = that.getProjectType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "projectType", lhsProjectType), LocatorUtils.property(thatLocator, "projectType", rhsProjectType), lhsProjectType, rhsProjectType)) {
                return false;
            }
        }
        {
            TridasVocabulary.ObjectType lhsObjectType;
            lhsObjectType = this.getObjectType();
            TridasVocabulary.ObjectType rhsObjectType;
            rhsObjectType = that.getObjectType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "objectType", lhsObjectType), LocatorUtils.property(thatLocator, "objectType", rhsObjectType), lhsObjectType, rhsObjectType)) {
                return false;
            }
        }
        {
            TridasVocabulary.ElementType lhsElementType;
            lhsElementType = this.getElementType();
            TridasVocabulary.ElementType rhsElementType;
            rhsElementType = that.getElementType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elementType", lhsElementType), LocatorUtils.property(thatLocator, "elementType", rhsElementType), lhsElementType, rhsElementType)) {
                return false;
            }
        }
        {
            TridasVocabulary.SampleType lhsSampleType;
            lhsSampleType = this.getSampleType();
            TridasVocabulary.SampleType rhsSampleType;
            rhsSampleType = that.getSampleType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sampleType", lhsSampleType), LocatorUtils.property(thatLocator, "sampleType", rhsSampleType), lhsSampleType, rhsSampleType)) {
                return false;
            }
        }
        {
            TridasVocabulary.DerivedSeriesType lhsDerivedSeriesType;
            lhsDerivedSeriesType = this.getDerivedSeriesType();
            TridasVocabulary.DerivedSeriesType rhsDerivedSeriesType;
            rhsDerivedSeriesType = that.getDerivedSeriesType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "derivedSeriesType", lhsDerivedSeriesType), LocatorUtils.property(thatLocator, "derivedSeriesType", rhsDerivedSeriesType), lhsDerivedSeriesType, rhsDerivedSeriesType)) {
                return false;
            }
        }
        {
            TridasVocabulary.ElementTaxon lhsElementTaxon;
            lhsElementTaxon = this.getElementTaxon();
            TridasVocabulary.ElementTaxon rhsElementTaxon;
            rhsElementTaxon = that.getElementTaxon();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elementTaxon", lhsElementTaxon), LocatorUtils.property(thatLocator, "elementTaxon", rhsElementTaxon), lhsElementTaxon, rhsElementTaxon)) {
                return false;
            }
        }
        {
            TridasVocabulary.ElementShape lhsElementShape;
            lhsElementShape = this.getElementShape();
            TridasVocabulary.ElementShape rhsElementShape;
            rhsElementShape = that.getElementShape();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elementShape", lhsElementShape), LocatorUtils.property(thatLocator, "elementShape", rhsElementShape), lhsElementShape, rhsElementShape)) {
                return false;
            }
        }
        {
            TridasVocabulary.MeasurementSeriesMeasuringMethod lhsMeasurementSeriesMeasuringMethod;
            lhsMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
            TridasVocabulary.MeasurementSeriesMeasuringMethod rhsMeasurementSeriesMeasuringMethod;
            rhsMeasurementSeriesMeasuringMethod = that.getMeasurementSeriesMeasuringMethod();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "measurementSeriesMeasuringMethod", lhsMeasurementSeriesMeasuringMethod), LocatorUtils.property(thatLocator, "measurementSeriesMeasuringMethod", rhsMeasurementSeriesMeasuringMethod), lhsMeasurementSeriesMeasuringMethod, rhsMeasurementSeriesMeasuringMethod)) {
                return false;
            }
        }
        {
            TridasVocabulary.ValuesVariable lhsValuesVariable;
            lhsValuesVariable = this.getValuesVariable();
            TridasVocabulary.ValuesVariable rhsValuesVariable;
            rhsValuesVariable = that.getValuesVariable();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "valuesVariable", lhsValuesVariable), LocatorUtils.property(thatLocator, "valuesVariable", rhsValuesVariable), lhsValuesVariable, rhsValuesVariable)) {
                return false;
            }
        }
        {
            TridasVocabulary.ValuesRemark lhsValuesRemark;
            lhsValuesRemark = this.getValuesRemark();
            TridasVocabulary.ValuesRemark rhsValuesRemark;
            rhsValuesRemark = that.getValuesRemark();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "valuesRemark", lhsValuesRemark), LocatorUtils.property(thatLocator, "valuesRemark", rhsValuesRemark), lhsValuesRemark, rhsValuesRemark)) {
                return false;
            }
        }
        {
            TridasVocabulary.LocationType lhsLocationType;
            lhsLocationType = this.getLocationType();
            TridasVocabulary.LocationType rhsLocationType;
            rhsLocationType = that.getLocationType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "locationType", lhsLocationType), LocatorUtils.property(thatLocator, "locationType", rhsLocationType), lhsLocationType, rhsLocationType)) {
                return false;
            }
        }
        {
            TridasVocabulary.GlobalUnit lhsGlobalUnit;
            lhsGlobalUnit = this.getGlobalUnit();
            TridasVocabulary.GlobalUnit rhsGlobalUnit;
            rhsGlobalUnit = that.getGlobalUnit();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "globalUnit", lhsGlobalUnit), LocatorUtils.property(thatLocator, "globalUnit", rhsGlobalUnit), lhsGlobalUnit, rhsGlobalUnit)) {
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
            TridasVocabulary.ProjectCategory theProjectCategory;
            theProjectCategory = this.getProjectCategory();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "projectCategory", theProjectCategory), currentHashCode, theProjectCategory);
        }
        {
            TridasVocabulary.ProjectType theProjectType;
            theProjectType = this.getProjectType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "projectType", theProjectType), currentHashCode, theProjectType);
        }
        {
            TridasVocabulary.ObjectType theObjectType;
            theObjectType = this.getObjectType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "objectType", theObjectType), currentHashCode, theObjectType);
        }
        {
            TridasVocabulary.ElementType theElementType;
            theElementType = this.getElementType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elementType", theElementType), currentHashCode, theElementType);
        }
        {
            TridasVocabulary.SampleType theSampleType;
            theSampleType = this.getSampleType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sampleType", theSampleType), currentHashCode, theSampleType);
        }
        {
            TridasVocabulary.DerivedSeriesType theDerivedSeriesType;
            theDerivedSeriesType = this.getDerivedSeriesType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "derivedSeriesType", theDerivedSeriesType), currentHashCode, theDerivedSeriesType);
        }
        {
            TridasVocabulary.ElementTaxon theElementTaxon;
            theElementTaxon = this.getElementTaxon();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elementTaxon", theElementTaxon), currentHashCode, theElementTaxon);
        }
        {
            TridasVocabulary.ElementShape theElementShape;
            theElementShape = this.getElementShape();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elementShape", theElementShape), currentHashCode, theElementShape);
        }
        {
            TridasVocabulary.MeasurementSeriesMeasuringMethod theMeasurementSeriesMeasuringMethod;
            theMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measurementSeriesMeasuringMethod", theMeasurementSeriesMeasuringMethod), currentHashCode, theMeasurementSeriesMeasuringMethod);
        }
        {
            TridasVocabulary.ValuesVariable theValuesVariable;
            theValuesVariable = this.getValuesVariable();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "valuesVariable", theValuesVariable), currentHashCode, theValuesVariable);
        }
        {
            TridasVocabulary.ValuesRemark theValuesRemark;
            theValuesRemark = this.getValuesRemark();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "valuesRemark", theValuesRemark), currentHashCode, theValuesRemark);
        }
        {
            TridasVocabulary.LocationType theLocationType;
            theLocationType = this.getLocationType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationType", theLocationType), currentHashCode, theLocationType);
        }
        {
            TridasVocabulary.GlobalUnit theGlobalUnit;
            theGlobalUnit = this.getGlobalUnit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "globalUnit", theGlobalUnit), currentHashCode, theGlobalUnit);
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
        if (draftCopy instanceof TridasVocabulary) {
            final TridasVocabulary copy = ((TridasVocabulary) draftCopy);
            if (this.isSetProjectCategory()) {
                TridasVocabulary.ProjectCategory sourceProjectCategory;
                sourceProjectCategory = this.getProjectCategory();
                TridasVocabulary.ProjectCategory copyProjectCategory = ((TridasVocabulary.ProjectCategory) strategy.copy(LocatorUtils.property(locator, "projectCategory", sourceProjectCategory), sourceProjectCategory));
                copy.setProjectCategory(copyProjectCategory);
            } else {
                copy.projectCategory = null;
            }
            if (this.isSetProjectType()) {
                TridasVocabulary.ProjectType sourceProjectType;
                sourceProjectType = this.getProjectType();
                TridasVocabulary.ProjectType copyProjectType = ((TridasVocabulary.ProjectType) strategy.copy(LocatorUtils.property(locator, "projectType", sourceProjectType), sourceProjectType));
                copy.setProjectType(copyProjectType);
            } else {
                copy.projectType = null;
            }
            if (this.isSetObjectType()) {
                TridasVocabulary.ObjectType sourceObjectType;
                sourceObjectType = this.getObjectType();
                TridasVocabulary.ObjectType copyObjectType = ((TridasVocabulary.ObjectType) strategy.copy(LocatorUtils.property(locator, "objectType", sourceObjectType), sourceObjectType));
                copy.setObjectType(copyObjectType);
            } else {
                copy.objectType = null;
            }
            if (this.isSetElementType()) {
                TridasVocabulary.ElementType sourceElementType;
                sourceElementType = this.getElementType();
                TridasVocabulary.ElementType copyElementType = ((TridasVocabulary.ElementType) strategy.copy(LocatorUtils.property(locator, "elementType", sourceElementType), sourceElementType));
                copy.setElementType(copyElementType);
            } else {
                copy.elementType = null;
            }
            if (this.isSetSampleType()) {
                TridasVocabulary.SampleType sourceSampleType;
                sourceSampleType = this.getSampleType();
                TridasVocabulary.SampleType copySampleType = ((TridasVocabulary.SampleType) strategy.copy(LocatorUtils.property(locator, "sampleType", sourceSampleType), sourceSampleType));
                copy.setSampleType(copySampleType);
            } else {
                copy.sampleType = null;
            }
            if (this.isSetDerivedSeriesType()) {
                TridasVocabulary.DerivedSeriesType sourceDerivedSeriesType;
                sourceDerivedSeriesType = this.getDerivedSeriesType();
                TridasVocabulary.DerivedSeriesType copyDerivedSeriesType = ((TridasVocabulary.DerivedSeriesType) strategy.copy(LocatorUtils.property(locator, "derivedSeriesType", sourceDerivedSeriesType), sourceDerivedSeriesType));
                copy.setDerivedSeriesType(copyDerivedSeriesType);
            } else {
                copy.derivedSeriesType = null;
            }
            if (this.isSetElementTaxon()) {
                TridasVocabulary.ElementTaxon sourceElementTaxon;
                sourceElementTaxon = this.getElementTaxon();
                TridasVocabulary.ElementTaxon copyElementTaxon = ((TridasVocabulary.ElementTaxon) strategy.copy(LocatorUtils.property(locator, "elementTaxon", sourceElementTaxon), sourceElementTaxon));
                copy.setElementTaxon(copyElementTaxon);
            } else {
                copy.elementTaxon = null;
            }
            if (this.isSetElementShape()) {
                TridasVocabulary.ElementShape sourceElementShape;
                sourceElementShape = this.getElementShape();
                TridasVocabulary.ElementShape copyElementShape = ((TridasVocabulary.ElementShape) strategy.copy(LocatorUtils.property(locator, "elementShape", sourceElementShape), sourceElementShape));
                copy.setElementShape(copyElementShape);
            } else {
                copy.elementShape = null;
            }
            if (this.isSetMeasurementSeriesMeasuringMethod()) {
                TridasVocabulary.MeasurementSeriesMeasuringMethod sourceMeasurementSeriesMeasuringMethod;
                sourceMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
                TridasVocabulary.MeasurementSeriesMeasuringMethod copyMeasurementSeriesMeasuringMethod = ((TridasVocabulary.MeasurementSeriesMeasuringMethod) strategy.copy(LocatorUtils.property(locator, "measurementSeriesMeasuringMethod", sourceMeasurementSeriesMeasuringMethod), sourceMeasurementSeriesMeasuringMethod));
                copy.setMeasurementSeriesMeasuringMethod(copyMeasurementSeriesMeasuringMethod);
            } else {
                copy.measurementSeriesMeasuringMethod = null;
            }
            if (this.isSetValuesVariable()) {
                TridasVocabulary.ValuesVariable sourceValuesVariable;
                sourceValuesVariable = this.getValuesVariable();
                TridasVocabulary.ValuesVariable copyValuesVariable = ((TridasVocabulary.ValuesVariable) strategy.copy(LocatorUtils.property(locator, "valuesVariable", sourceValuesVariable), sourceValuesVariable));
                copy.setValuesVariable(copyValuesVariable);
            } else {
                copy.valuesVariable = null;
            }
            if (this.isSetValuesRemark()) {
                TridasVocabulary.ValuesRemark sourceValuesRemark;
                sourceValuesRemark = this.getValuesRemark();
                TridasVocabulary.ValuesRemark copyValuesRemark = ((TridasVocabulary.ValuesRemark) strategy.copy(LocatorUtils.property(locator, "valuesRemark", sourceValuesRemark), sourceValuesRemark));
                copy.setValuesRemark(copyValuesRemark);
            } else {
                copy.valuesRemark = null;
            }
            if (this.isSetLocationType()) {
                TridasVocabulary.LocationType sourceLocationType;
                sourceLocationType = this.getLocationType();
                TridasVocabulary.LocationType copyLocationType = ((TridasVocabulary.LocationType) strategy.copy(LocatorUtils.property(locator, "locationType", sourceLocationType), sourceLocationType));
                copy.setLocationType(copyLocationType);
            } else {
                copy.locationType = null;
            }
            if (this.isSetGlobalUnit()) {
                TridasVocabulary.GlobalUnit sourceGlobalUnit;
                sourceGlobalUnit = this.getGlobalUnit();
                TridasVocabulary.GlobalUnit copyGlobalUnit = ((TridasVocabulary.GlobalUnit) strategy.copy(LocatorUtils.property(locator, "globalUnit", sourceGlobalUnit), sourceGlobalUnit));
                copy.setGlobalUnit(copyGlobalUnit);
            } else {
                copy.globalUnit = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasVocabulary();
    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
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
        "types"
    })
    public static class DerivedSeriesType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "type", required = true)
        protected List<ControlledVoc> types;

        /**
         * Gets the value of the types property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the types property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTypes() {
            if (types == null) {
                types = new ArrayList<ControlledVoc>();
            }
            return this.types;
        }

        public boolean isSetTypes() {
            return ((this.types!= null)&&(!this.types.isEmpty()));
        }

        public void unsetTypes() {
            this.types = null;
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                strategy.appendField(locator, this, "types", buffer, theTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.DerivedSeriesType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.DerivedSeriesType that = ((TridasVocabulary.DerivedSeriesType) object);
            {
                List<ControlledVoc> lhsTypes;
                lhsTypes = (this.isSetTypes()?this.getTypes():null);
                List<ControlledVoc> rhsTypes;
                rhsTypes = (that.isSetTypes()?that.getTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
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
            if (draftCopy instanceof TridasVocabulary.DerivedSeriesType) {
                final TridasVocabulary.DerivedSeriesType copy = ((TridasVocabulary.DerivedSeriesType) draftCopy);
                if (this.isSetTypes()) {
                    List<ControlledVoc> sourceTypes;
                    sourceTypes = (this.isSetTypes()?this.getTypes():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "types", sourceTypes), sourceTypes));
                    copy.unsetTypes();
                    List<ControlledVoc> uniqueTypesl = copy.getTypes();
                    uniqueTypesl.addAll(copyTypes);
                } else {
                    copy.unsetTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.DerivedSeriesType();
        }

        /**
         * Sets the value of the types property.
         * 
         * @param types
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTypes(List<ControlledVoc> types) {
            this.types = types;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}shape" maxOccurs="unbounded"/>
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
        "shapes"
    })
    public static class ElementShape
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "shape", required = true)
        protected List<TridasShape> shapes;

        /**
         * Gets the value of the shapes property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the shapes property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getShapes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TridasShape }
         * 
         * 
         */
        public List<TridasShape> getShapes() {
            if (shapes == null) {
                shapes = new ArrayList<TridasShape>();
            }
            return this.shapes;
        }

        public boolean isSetShapes() {
            return ((this.shapes!= null)&&(!this.shapes.isEmpty()));
        }

        public void unsetShapes() {
            this.shapes = null;
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
                List<TridasShape> theShapes;
                theShapes = (this.isSetShapes()?this.getShapes():null);
                strategy.appendField(locator, this, "shapes", buffer, theShapes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ElementShape)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ElementShape that = ((TridasVocabulary.ElementShape) object);
            {
                List<TridasShape> lhsShapes;
                lhsShapes = (this.isSetShapes()?this.getShapes():null);
                List<TridasShape> rhsShapes;
                rhsShapes = (that.isSetShapes()?that.getShapes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "shapes", lhsShapes), LocatorUtils.property(thatLocator, "shapes", rhsShapes), lhsShapes, rhsShapes)) {
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
                List<TridasShape> theShapes;
                theShapes = (this.isSetShapes()?this.getShapes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shapes", theShapes), currentHashCode, theShapes);
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
            if (draftCopy instanceof TridasVocabulary.ElementShape) {
                final TridasVocabulary.ElementShape copy = ((TridasVocabulary.ElementShape) draftCopy);
                if (this.isSetShapes()) {
                    List<TridasShape> sourceShapes;
                    sourceShapes = (this.isSetShapes()?this.getShapes():null);
                    @SuppressWarnings("unchecked")
                    List<TridasShape> copyShapes = ((List<TridasShape> ) strategy.copy(LocatorUtils.property(locator, "shapes", sourceShapes), sourceShapes));
                    copy.unsetShapes();
                    List<TridasShape> uniqueShapesl = copy.getShapes();
                    uniqueShapesl.addAll(copyShapes);
                } else {
                    copy.unsetShapes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ElementShape();
        }

        /**
         * Sets the value of the shapes property.
         * 
         * @param shapes
         *     allowed object is
         *     {@link TridasShape }
         *     
         */
        public void setShapes(List<TridasShape> shapes) {
            this.shapes = shapes;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}taxon" maxOccurs="unbounded"/>
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
        "taxons"
    })
    public static class ElementTaxon
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "taxon", required = true)
        protected List<ControlledVoc> taxons;

        /**
         * Gets the value of the taxons property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the taxons property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTaxons().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTaxons() {
            if (taxons == null) {
                taxons = new ArrayList<ControlledVoc>();
            }
            return this.taxons;
        }

        public boolean isSetTaxons() {
            return ((this.taxons!= null)&&(!this.taxons.isEmpty()));
        }

        public void unsetTaxons() {
            this.taxons = null;
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
                List<ControlledVoc> theTaxons;
                theTaxons = (this.isSetTaxons()?this.getTaxons():null);
                strategy.appendField(locator, this, "taxons", buffer, theTaxons);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ElementTaxon)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ElementTaxon that = ((TridasVocabulary.ElementTaxon) object);
            {
                List<ControlledVoc> lhsTaxons;
                lhsTaxons = (this.isSetTaxons()?this.getTaxons():null);
                List<ControlledVoc> rhsTaxons;
                rhsTaxons = (that.isSetTaxons()?that.getTaxons():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "taxons", lhsTaxons), LocatorUtils.property(thatLocator, "taxons", rhsTaxons), lhsTaxons, rhsTaxons)) {
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
                List<ControlledVoc> theTaxons;
                theTaxons = (this.isSetTaxons()?this.getTaxons():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "taxons", theTaxons), currentHashCode, theTaxons);
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
            if (draftCopy instanceof TridasVocabulary.ElementTaxon) {
                final TridasVocabulary.ElementTaxon copy = ((TridasVocabulary.ElementTaxon) draftCopy);
                if (this.isSetTaxons()) {
                    List<ControlledVoc> sourceTaxons;
                    sourceTaxons = (this.isSetTaxons()?this.getTaxons():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTaxons = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "taxons", sourceTaxons), sourceTaxons));
                    copy.unsetTaxons();
                    List<ControlledVoc> uniqueTaxonsl = copy.getTaxons();
                    uniqueTaxonsl.addAll(copyTaxons);
                } else {
                    copy.unsetTaxons();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ElementTaxon();
        }

        /**
         * Sets the value of the taxons property.
         * 
         * @param taxons
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTaxons(List<ControlledVoc> taxons) {
            this.taxons = taxons;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
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
        "types"
    })
    public static class ElementType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "type", required = true)
        protected List<ControlledVoc> types;

        /**
         * Gets the value of the types property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the types property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTypes() {
            if (types == null) {
                types = new ArrayList<ControlledVoc>();
            }
            return this.types;
        }

        public boolean isSetTypes() {
            return ((this.types!= null)&&(!this.types.isEmpty()));
        }

        public void unsetTypes() {
            this.types = null;
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                strategy.appendField(locator, this, "types", buffer, theTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ElementType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ElementType that = ((TridasVocabulary.ElementType) object);
            {
                List<ControlledVoc> lhsTypes;
                lhsTypes = (this.isSetTypes()?this.getTypes():null);
                List<ControlledVoc> rhsTypes;
                rhsTypes = (that.isSetTypes()?that.getTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
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
            if (draftCopy instanceof TridasVocabulary.ElementType) {
                final TridasVocabulary.ElementType copy = ((TridasVocabulary.ElementType) draftCopy);
                if (this.isSetTypes()) {
                    List<ControlledVoc> sourceTypes;
                    sourceTypes = (this.isSetTypes()?this.getTypes():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "types", sourceTypes), sourceTypes));
                    copy.unsetTypes();
                    List<ControlledVoc> uniqueTypesl = copy.getTypes();
                    uniqueTypesl.addAll(copyTypes);
                } else {
                    copy.unsetTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ElementType();
        }

        /**
         * Sets the value of the types property.
         * 
         * @param types
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTypes(List<ControlledVoc> types) {
            this.types = types;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}unit" maxOccurs="unbounded"/>
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
        "units"
    })
    public static class GlobalUnit
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "unit", required = true)
        protected List<TridasUnit> units;

        /**
         * Gets the value of the units property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the units property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUnits().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TridasUnit }
         * 
         * 
         */
        public List<TridasUnit> getUnits() {
            if (units == null) {
                units = new ArrayList<TridasUnit>();
            }
            return this.units;
        }

        public boolean isSetUnits() {
            return ((this.units!= null)&&(!this.units.isEmpty()));
        }

        public void unsetUnits() {
            this.units = null;
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
                List<TridasUnit> theUnits;
                theUnits = (this.isSetUnits()?this.getUnits():null);
                strategy.appendField(locator, this, "units", buffer, theUnits);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.GlobalUnit)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.GlobalUnit that = ((TridasVocabulary.GlobalUnit) object);
            {
                List<TridasUnit> lhsUnits;
                lhsUnits = (this.isSetUnits()?this.getUnits():null);
                List<TridasUnit> rhsUnits;
                rhsUnits = (that.isSetUnits()?that.getUnits():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "units", lhsUnits), LocatorUtils.property(thatLocator, "units", rhsUnits), lhsUnits, rhsUnits)) {
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
                List<TridasUnit> theUnits;
                theUnits = (this.isSetUnits()?this.getUnits():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "units", theUnits), currentHashCode, theUnits);
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
            if (draftCopy instanceof TridasVocabulary.GlobalUnit) {
                final TridasVocabulary.GlobalUnit copy = ((TridasVocabulary.GlobalUnit) draftCopy);
                if (this.isSetUnits()) {
                    List<TridasUnit> sourceUnits;
                    sourceUnits = (this.isSetUnits()?this.getUnits():null);
                    @SuppressWarnings("unchecked")
                    List<TridasUnit> copyUnits = ((List<TridasUnit> ) strategy.copy(LocatorUtils.property(locator, "units", sourceUnits), sourceUnits));
                    copy.unsetUnits();
                    List<TridasUnit> uniqueUnitsl = copy.getUnits();
                    uniqueUnitsl.addAll(copyUnits);
                } else {
                    copy.unsetUnits();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.GlobalUnit();
        }

        /**
         * Sets the value of the units property.
         * 
         * @param units
         *     allowed object is
         *     {@link TridasUnit }
         *     
         */
        public void setUnits(List<TridasUnit> units) {
            this.units = units;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}locationType" maxOccurs="unbounded"/>
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
        "locationTypes"
    })
    public static class LocationType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "locationType", required = true)
        protected List<NormalTridasLocationType> locationTypes;

        /**
         * Gets the value of the locationTypes property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the locationTypes property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLocationTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NormalTridasLocationType }
         * 
         * 
         */
        public List<NormalTridasLocationType> getLocationTypes() {
            if (locationTypes == null) {
                locationTypes = new ArrayList<NormalTridasLocationType>();
            }
            return this.locationTypes;
        }

        public boolean isSetLocationTypes() {
            return ((this.locationTypes!= null)&&(!this.locationTypes.isEmpty()));
        }

        public void unsetLocationTypes() {
            this.locationTypes = null;
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
                List<NormalTridasLocationType> theLocationTypes;
                theLocationTypes = (this.isSetLocationTypes()?this.getLocationTypes():null);
                strategy.appendField(locator, this, "locationTypes", buffer, theLocationTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.LocationType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.LocationType that = ((TridasVocabulary.LocationType) object);
            {
                List<NormalTridasLocationType> lhsLocationTypes;
                lhsLocationTypes = (this.isSetLocationTypes()?this.getLocationTypes():null);
                List<NormalTridasLocationType> rhsLocationTypes;
                rhsLocationTypes = (that.isSetLocationTypes()?that.getLocationTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "locationTypes", lhsLocationTypes), LocatorUtils.property(thatLocator, "locationTypes", rhsLocationTypes), lhsLocationTypes, rhsLocationTypes)) {
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
                List<NormalTridasLocationType> theLocationTypes;
                theLocationTypes = (this.isSetLocationTypes()?this.getLocationTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationTypes", theLocationTypes), currentHashCode, theLocationTypes);
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
            if (draftCopy instanceof TridasVocabulary.LocationType) {
                final TridasVocabulary.LocationType copy = ((TridasVocabulary.LocationType) draftCopy);
                if (this.isSetLocationTypes()) {
                    List<NormalTridasLocationType> sourceLocationTypes;
                    sourceLocationTypes = (this.isSetLocationTypes()?this.getLocationTypes():null);
                    @SuppressWarnings("unchecked")
                    List<NormalTridasLocationType> copyLocationTypes = ((List<NormalTridasLocationType> ) strategy.copy(LocatorUtils.property(locator, "locationTypes", sourceLocationTypes), sourceLocationTypes));
                    copy.unsetLocationTypes();
                    List<NormalTridasLocationType> uniqueLocationTypesl = copy.getLocationTypes();
                    uniqueLocationTypesl.addAll(copyLocationTypes);
                } else {
                    copy.unsetLocationTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.LocationType();
        }

        /**
         * Sets the value of the locationTypes property.
         * 
         * @param locationTypes
         *     allowed object is
         *     {@link NormalTridasLocationType }
         *     
         */
        public void setLocationTypes(List<NormalTridasLocationType> locationTypes) {
            this.locationTypes = locationTypes;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}measuringMethod" maxOccurs="unbounded"/>
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
        "measuringMethods"
    })
    public static class MeasurementSeriesMeasuringMethod
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "measuringMethod", required = true)
        protected List<TridasMeasuringMethod> measuringMethods;

        /**
         * Gets the value of the measuringMethods property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the measuringMethods property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMeasuringMethods().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TridasMeasuringMethod }
         * 
         * 
         */
        public List<TridasMeasuringMethod> getMeasuringMethods() {
            if (measuringMethods == null) {
                measuringMethods = new ArrayList<TridasMeasuringMethod>();
            }
            return this.measuringMethods;
        }

        public boolean isSetMeasuringMethods() {
            return ((this.measuringMethods!= null)&&(!this.measuringMethods.isEmpty()));
        }

        public void unsetMeasuringMethods() {
            this.measuringMethods = null;
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
                List<TridasMeasuringMethod> theMeasuringMethods;
                theMeasuringMethods = (this.isSetMeasuringMethods()?this.getMeasuringMethods():null);
                strategy.appendField(locator, this, "measuringMethods", buffer, theMeasuringMethods);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.MeasurementSeriesMeasuringMethod)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.MeasurementSeriesMeasuringMethod that = ((TridasVocabulary.MeasurementSeriesMeasuringMethod) object);
            {
                List<TridasMeasuringMethod> lhsMeasuringMethods;
                lhsMeasuringMethods = (this.isSetMeasuringMethods()?this.getMeasuringMethods():null);
                List<TridasMeasuringMethod> rhsMeasuringMethods;
                rhsMeasuringMethods = (that.isSetMeasuringMethods()?that.getMeasuringMethods():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "measuringMethods", lhsMeasuringMethods), LocatorUtils.property(thatLocator, "measuringMethods", rhsMeasuringMethods), lhsMeasuringMethods, rhsMeasuringMethods)) {
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
                List<TridasMeasuringMethod> theMeasuringMethods;
                theMeasuringMethods = (this.isSetMeasuringMethods()?this.getMeasuringMethods():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measuringMethods", theMeasuringMethods), currentHashCode, theMeasuringMethods);
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
            if (draftCopy instanceof TridasVocabulary.MeasurementSeriesMeasuringMethod) {
                final TridasVocabulary.MeasurementSeriesMeasuringMethod copy = ((TridasVocabulary.MeasurementSeriesMeasuringMethod) draftCopy);
                if (this.isSetMeasuringMethods()) {
                    List<TridasMeasuringMethod> sourceMeasuringMethods;
                    sourceMeasuringMethods = (this.isSetMeasuringMethods()?this.getMeasuringMethods():null);
                    @SuppressWarnings("unchecked")
                    List<TridasMeasuringMethod> copyMeasuringMethods = ((List<TridasMeasuringMethod> ) strategy.copy(LocatorUtils.property(locator, "measuringMethods", sourceMeasuringMethods), sourceMeasuringMethods));
                    copy.unsetMeasuringMethods();
                    List<TridasMeasuringMethod> uniqueMeasuringMethodsl = copy.getMeasuringMethods();
                    uniqueMeasuringMethodsl.addAll(copyMeasuringMethods);
                } else {
                    copy.unsetMeasuringMethods();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.MeasurementSeriesMeasuringMethod();
        }

        /**
         * Sets the value of the measuringMethods property.
         * 
         * @param measuringMethods
         *     allowed object is
         *     {@link TridasMeasuringMethod }
         *     
         */
        public void setMeasuringMethods(List<TridasMeasuringMethod> measuringMethods) {
            this.measuringMethods = measuringMethods;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
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
        "types"
    })
    public static class ObjectType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "type", required = true)
        protected List<ControlledVoc> types;

        /**
         * Gets the value of the types property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the types property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTypes() {
            if (types == null) {
                types = new ArrayList<ControlledVoc>();
            }
            return this.types;
        }

        public boolean isSetTypes() {
            return ((this.types!= null)&&(!this.types.isEmpty()));
        }

        public void unsetTypes() {
            this.types = null;
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                strategy.appendField(locator, this, "types", buffer, theTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ObjectType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ObjectType that = ((TridasVocabulary.ObjectType) object);
            {
                List<ControlledVoc> lhsTypes;
                lhsTypes = (this.isSetTypes()?this.getTypes():null);
                List<ControlledVoc> rhsTypes;
                rhsTypes = (that.isSetTypes()?that.getTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
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
            if (draftCopy instanceof TridasVocabulary.ObjectType) {
                final TridasVocabulary.ObjectType copy = ((TridasVocabulary.ObjectType) draftCopy);
                if (this.isSetTypes()) {
                    List<ControlledVoc> sourceTypes;
                    sourceTypes = (this.isSetTypes()?this.getTypes():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "types", sourceTypes), sourceTypes));
                    copy.unsetTypes();
                    List<ControlledVoc> uniqueTypesl = copy.getTypes();
                    uniqueTypesl.addAll(copyTypes);
                } else {
                    copy.unsetTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ObjectType();
        }

        /**
         * Sets the value of the types property.
         * 
         * @param types
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTypes(List<ControlledVoc> types) {
            this.types = types;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}category" maxOccurs="unbounded"/>
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
        "categories"
    })
    public static class ProjectCategory
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "category", required = true)
        protected List<ControlledVoc> categories;

        /**
         * Gets the value of the categories property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the categories property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategories().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getCategories() {
            if (categories == null) {
                categories = new ArrayList<ControlledVoc>();
            }
            return this.categories;
        }

        public boolean isSetCategories() {
            return ((this.categories!= null)&&(!this.categories.isEmpty()));
        }

        public void unsetCategories() {
            this.categories = null;
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
                List<ControlledVoc> theCategories;
                theCategories = (this.isSetCategories()?this.getCategories():null);
                strategy.appendField(locator, this, "categories", buffer, theCategories);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ProjectCategory)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ProjectCategory that = ((TridasVocabulary.ProjectCategory) object);
            {
                List<ControlledVoc> lhsCategories;
                lhsCategories = (this.isSetCategories()?this.getCategories():null);
                List<ControlledVoc> rhsCategories;
                rhsCategories = (that.isSetCategories()?that.getCategories():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "categories", lhsCategories), LocatorUtils.property(thatLocator, "categories", rhsCategories), lhsCategories, rhsCategories)) {
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
                List<ControlledVoc> theCategories;
                theCategories = (this.isSetCategories()?this.getCategories():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "categories", theCategories), currentHashCode, theCategories);
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
            if (draftCopy instanceof TridasVocabulary.ProjectCategory) {
                final TridasVocabulary.ProjectCategory copy = ((TridasVocabulary.ProjectCategory) draftCopy);
                if (this.isSetCategories()) {
                    List<ControlledVoc> sourceCategories;
                    sourceCategories = (this.isSetCategories()?this.getCategories():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyCategories = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "categories", sourceCategories), sourceCategories));
                    copy.unsetCategories();
                    List<ControlledVoc> uniqueCategoriesl = copy.getCategories();
                    uniqueCategoriesl.addAll(copyCategories);
                } else {
                    copy.unsetCategories();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ProjectCategory();
        }

        /**
         * Sets the value of the categories property.
         * 
         * @param categories
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setCategories(List<ControlledVoc> categories) {
            this.categories = categories;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
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
        "types"
    })
    public static class ProjectType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "type", required = true)
        protected List<ControlledVoc> types;

        /**
         * Gets the value of the types property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the types property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTypes() {
            if (types == null) {
                types = new ArrayList<ControlledVoc>();
            }
            return this.types;
        }

        public boolean isSetTypes() {
            return ((this.types!= null)&&(!this.types.isEmpty()));
        }

        public void unsetTypes() {
            this.types = null;
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                strategy.appendField(locator, this, "types", buffer, theTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ProjectType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ProjectType that = ((TridasVocabulary.ProjectType) object);
            {
                List<ControlledVoc> lhsTypes;
                lhsTypes = (this.isSetTypes()?this.getTypes():null);
                List<ControlledVoc> rhsTypes;
                rhsTypes = (that.isSetTypes()?that.getTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
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
            if (draftCopy instanceof TridasVocabulary.ProjectType) {
                final TridasVocabulary.ProjectType copy = ((TridasVocabulary.ProjectType) draftCopy);
                if (this.isSetTypes()) {
                    List<ControlledVoc> sourceTypes;
                    sourceTypes = (this.isSetTypes()?this.getTypes():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "types", sourceTypes), sourceTypes));
                    copy.unsetTypes();
                    List<ControlledVoc> uniqueTypesl = copy.getTypes();
                    uniqueTypesl.addAll(copyTypes);
                } else {
                    copy.unsetTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ProjectType();
        }

        /**
         * Sets the value of the types property.
         * 
         * @param types
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTypes(List<ControlledVoc> types) {
            this.types = types;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
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
        "types"
    })
    public static class SampleType
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "type", required = true)
        protected List<ControlledVoc> types;

        /**
         * Gets the value of the types property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the types property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTypes().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ControlledVoc }
         * 
         * 
         */
        public List<ControlledVoc> getTypes() {
            if (types == null) {
                types = new ArrayList<ControlledVoc>();
            }
            return this.types;
        }

        public boolean isSetTypes() {
            return ((this.types!= null)&&(!this.types.isEmpty()));
        }

        public void unsetTypes() {
            this.types = null;
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                strategy.appendField(locator, this, "types", buffer, theTypes);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.SampleType)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.SampleType that = ((TridasVocabulary.SampleType) object);
            {
                List<ControlledVoc> lhsTypes;
                lhsTypes = (this.isSetTypes()?this.getTypes():null);
                List<ControlledVoc> rhsTypes;
                rhsTypes = (that.isSetTypes()?that.getTypes():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
                List<ControlledVoc> theTypes;
                theTypes = (this.isSetTypes()?this.getTypes():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
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
            if (draftCopy instanceof TridasVocabulary.SampleType) {
                final TridasVocabulary.SampleType copy = ((TridasVocabulary.SampleType) draftCopy);
                if (this.isSetTypes()) {
                    List<ControlledVoc> sourceTypes;
                    sourceTypes = (this.isSetTypes()?this.getTypes():null);
                    @SuppressWarnings("unchecked")
                    List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) strategy.copy(LocatorUtils.property(locator, "types", sourceTypes), sourceTypes));
                    copy.unsetTypes();
                    List<ControlledVoc> uniqueTypesl = copy.getTypes();
                    uniqueTypesl.addAll(copyTypes);
                } else {
                    copy.unsetTypes();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.SampleType();
        }

        /**
         * Sets the value of the types property.
         * 
         * @param types
         *     allowed object is
         *     {@link ControlledVoc }
         *     
         */
        public void setTypes(List<ControlledVoc> types) {
            this.types = types;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}remark" maxOccurs="unbounded"/>
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
        "remarks"
    })
    public static class ValuesRemark
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "remark", required = true)
        protected List<TridasRemark> remarks;

        /**
         * Gets the value of the remarks property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the remarks property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRemarks().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TridasRemark }
         * 
         * 
         */
        public List<TridasRemark> getRemarks() {
            if (remarks == null) {
                remarks = new ArrayList<TridasRemark>();
            }
            return this.remarks;
        }

        public boolean isSetRemarks() {
            return ((this.remarks!= null)&&(!this.remarks.isEmpty()));
        }

        public void unsetRemarks() {
            this.remarks = null;
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
                List<TridasRemark> theRemarks;
                theRemarks = (this.isSetRemarks()?this.getRemarks():null);
                strategy.appendField(locator, this, "remarks", buffer, theRemarks);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ValuesRemark)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ValuesRemark that = ((TridasVocabulary.ValuesRemark) object);
            {
                List<TridasRemark> lhsRemarks;
                lhsRemarks = (this.isSetRemarks()?this.getRemarks():null);
                List<TridasRemark> rhsRemarks;
                rhsRemarks = (that.isSetRemarks()?that.getRemarks():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "remarks", lhsRemarks), LocatorUtils.property(thatLocator, "remarks", rhsRemarks), lhsRemarks, rhsRemarks)) {
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
                List<TridasRemark> theRemarks;
                theRemarks = (this.isSetRemarks()?this.getRemarks():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "remarks", theRemarks), currentHashCode, theRemarks);
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
            if (draftCopy instanceof TridasVocabulary.ValuesRemark) {
                final TridasVocabulary.ValuesRemark copy = ((TridasVocabulary.ValuesRemark) draftCopy);
                if (this.isSetRemarks()) {
                    List<TridasRemark> sourceRemarks;
                    sourceRemarks = (this.isSetRemarks()?this.getRemarks():null);
                    @SuppressWarnings("unchecked")
                    List<TridasRemark> copyRemarks = ((List<TridasRemark> ) strategy.copy(LocatorUtils.property(locator, "remarks", sourceRemarks), sourceRemarks));
                    copy.unsetRemarks();
                    List<TridasRemark> uniqueRemarksl = copy.getRemarks();
                    uniqueRemarksl.addAll(copyRemarks);
                } else {
                    copy.unsetRemarks();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ValuesRemark();
        }

        /**
         * Sets the value of the remarks property.
         * 
         * @param remarks
         *     allowed object is
         *     {@link TridasRemark }
         *     
         */
        public void setRemarks(List<TridasRemark> remarks) {
            this.remarks = remarks;
        }

    }


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
     *         &lt;element ref="{http://www.tridas.org/1.2.2}variable" maxOccurs="unbounded"/>
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
        "variables"
    })
    public static class ValuesVariable
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(name = "variable", required = true)
        protected List<TridasVariable> variables;

        /**
         * Gets the value of the variables property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the variables property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVariables().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TridasVariable }
         * 
         * 
         */
        public List<TridasVariable> getVariables() {
            if (variables == null) {
                variables = new ArrayList<TridasVariable>();
            }
            return this.variables;
        }

        public boolean isSetVariables() {
            return ((this.variables!= null)&&(!this.variables.isEmpty()));
        }

        public void unsetVariables() {
            this.variables = null;
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
                List<TridasVariable> theVariables;
                theVariables = (this.isSetVariables()?this.getVariables():null);
                strategy.appendField(locator, this, "variables", buffer, theVariables);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof TridasVocabulary.ValuesVariable)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final TridasVocabulary.ValuesVariable that = ((TridasVocabulary.ValuesVariable) object);
            {
                List<TridasVariable> lhsVariables;
                lhsVariables = (this.isSetVariables()?this.getVariables():null);
                List<TridasVariable> rhsVariables;
                rhsVariables = (that.isSetVariables()?that.getVariables():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "variables", lhsVariables), LocatorUtils.property(thatLocator, "variables", rhsVariables), lhsVariables, rhsVariables)) {
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
                List<TridasVariable> theVariables;
                theVariables = (this.isSetVariables()?this.getVariables():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "variables", theVariables), currentHashCode, theVariables);
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
            if (draftCopy instanceof TridasVocabulary.ValuesVariable) {
                final TridasVocabulary.ValuesVariable copy = ((TridasVocabulary.ValuesVariable) draftCopy);
                if (this.isSetVariables()) {
                    List<TridasVariable> sourceVariables;
                    sourceVariables = (this.isSetVariables()?this.getVariables():null);
                    @SuppressWarnings("unchecked")
                    List<TridasVariable> copyVariables = ((List<TridasVariable> ) strategy.copy(LocatorUtils.property(locator, "variables", sourceVariables), sourceVariables));
                    copy.unsetVariables();
                    List<TridasVariable> uniqueVariablesl = copy.getVariables();
                    uniqueVariablesl.addAll(copyVariables);
                } else {
                    copy.unsetVariables();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new TridasVocabulary.ValuesVariable();
        }

        /**
         * Sets the value of the variables property.
         * 
         * @param variables
         *     allowed object is
         *     {@link TridasVariable }
         *     
         */
        public void setVariables(List<TridasVariable> variables) {
            this.variables = variables;
        }

    }

}
