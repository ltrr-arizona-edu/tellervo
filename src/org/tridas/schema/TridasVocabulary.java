
package org.tridas.schema;

import java.io.Serializable;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="project.category" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}category" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}taxon" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}shape" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}measuringMethod" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}variable" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}remark" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}locationType" maxOccurs="unbounded"/>
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
 *                   &lt;element ref="{http://www.tridas.org/1.2.1}unit" maxOccurs="unbounded"/>
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasVocabulary)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasVocabulary that = ((TridasVocabulary) object);
        equalsBuilder.append(this.getProjectCategory(), that.getProjectCategory());
        equalsBuilder.append(this.getProjectType(), that.getProjectType());
        equalsBuilder.append(this.getObjectType(), that.getObjectType());
        equalsBuilder.append(this.getElementType(), that.getElementType());
        equalsBuilder.append(this.getSampleType(), that.getSampleType());
        equalsBuilder.append(this.getDerivedSeriesType(), that.getDerivedSeriesType());
        equalsBuilder.append(this.getElementTaxon(), that.getElementTaxon());
        equalsBuilder.append(this.getElementShape(), that.getElementShape());
        equalsBuilder.append(this.getMeasurementSeriesMeasuringMethod(), that.getMeasurementSeriesMeasuringMethod());
        equalsBuilder.append(this.getValuesVariable(), that.getValuesVariable());
        equalsBuilder.append(this.getValuesRemark(), that.getValuesRemark());
        equalsBuilder.append(this.getLocationType(), that.getLocationType());
        equalsBuilder.append(this.getGlobalUnit(), that.getGlobalUnit());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasVocabulary)) {
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
        hashCodeBuilder.append(this.getProjectCategory());
        hashCodeBuilder.append(this.getProjectType());
        hashCodeBuilder.append(this.getObjectType());
        hashCodeBuilder.append(this.getElementType());
        hashCodeBuilder.append(this.getSampleType());
        hashCodeBuilder.append(this.getDerivedSeriesType());
        hashCodeBuilder.append(this.getElementTaxon());
        hashCodeBuilder.append(this.getElementShape());
        hashCodeBuilder.append(this.getMeasurementSeriesMeasuringMethod());
        hashCodeBuilder.append(this.getValuesVariable());
        hashCodeBuilder.append(this.getValuesRemark());
        hashCodeBuilder.append(this.getLocationType());
        hashCodeBuilder.append(this.getGlobalUnit());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasVocabulary.ProjectCategory theProjectCategory;
            theProjectCategory = this.getProjectCategory();
            toStringBuilder.append("projectCategory", theProjectCategory);
        }
        {
            TridasVocabulary.ProjectType theProjectType;
            theProjectType = this.getProjectType();
            toStringBuilder.append("projectType", theProjectType);
        }
        {
            TridasVocabulary.ObjectType theObjectType;
            theObjectType = this.getObjectType();
            toStringBuilder.append("objectType", theObjectType);
        }
        {
            TridasVocabulary.ElementType theElementType;
            theElementType = this.getElementType();
            toStringBuilder.append("elementType", theElementType);
        }
        {
            TridasVocabulary.SampleType theSampleType;
            theSampleType = this.getSampleType();
            toStringBuilder.append("sampleType", theSampleType);
        }
        {
            TridasVocabulary.DerivedSeriesType theDerivedSeriesType;
            theDerivedSeriesType = this.getDerivedSeriesType();
            toStringBuilder.append("derivedSeriesType", theDerivedSeriesType);
        }
        {
            TridasVocabulary.ElementTaxon theElementTaxon;
            theElementTaxon = this.getElementTaxon();
            toStringBuilder.append("elementTaxon", theElementTaxon);
        }
        {
            TridasVocabulary.ElementShape theElementShape;
            theElementShape = this.getElementShape();
            toStringBuilder.append("elementShape", theElementShape);
        }
        {
            TridasVocabulary.MeasurementSeriesMeasuringMethod theMeasurementSeriesMeasuringMethod;
            theMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
            toStringBuilder.append("measurementSeriesMeasuringMethod", theMeasurementSeriesMeasuringMethod);
        }
        {
            TridasVocabulary.ValuesVariable theValuesVariable;
            theValuesVariable = this.getValuesVariable();
            toStringBuilder.append("valuesVariable", theValuesVariable);
        }
        {
            TridasVocabulary.ValuesRemark theValuesRemark;
            theValuesRemark = this.getValuesRemark();
            toStringBuilder.append("valuesRemark", theValuesRemark);
        }
        {
            TridasVocabulary.LocationType theLocationType;
            theLocationType = this.getLocationType();
            toStringBuilder.append("locationType", theLocationType);
        }
        {
            TridasVocabulary.GlobalUnit theGlobalUnit;
            theGlobalUnit = this.getGlobalUnit();
            toStringBuilder.append("globalUnit", theGlobalUnit);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasVocabulary copy = ((target == null)?((TridasVocabulary) createCopy()):((TridasVocabulary) target));
        if (this.isSetProjectCategory()) {
            TridasVocabulary.ProjectCategory sourceProjectCategory;
            sourceProjectCategory = this.getProjectCategory();
            TridasVocabulary.ProjectCategory copyProjectCategory = ((TridasVocabulary.ProjectCategory) copyBuilder.copy(sourceProjectCategory));
            copy.setProjectCategory(copyProjectCategory);
        } else {
            copy.projectCategory = null;
        }
        if (this.isSetProjectType()) {
            TridasVocabulary.ProjectType sourceProjectType;
            sourceProjectType = this.getProjectType();
            TridasVocabulary.ProjectType copyProjectType = ((TridasVocabulary.ProjectType) copyBuilder.copy(sourceProjectType));
            copy.setProjectType(copyProjectType);
        } else {
            copy.projectType = null;
        }
        if (this.isSetObjectType()) {
            TridasVocabulary.ObjectType sourceObjectType;
            sourceObjectType = this.getObjectType();
            TridasVocabulary.ObjectType copyObjectType = ((TridasVocabulary.ObjectType) copyBuilder.copy(sourceObjectType));
            copy.setObjectType(copyObjectType);
        } else {
            copy.objectType = null;
        }
        if (this.isSetElementType()) {
            TridasVocabulary.ElementType sourceElementType;
            sourceElementType = this.getElementType();
            TridasVocabulary.ElementType copyElementType = ((TridasVocabulary.ElementType) copyBuilder.copy(sourceElementType));
            copy.setElementType(copyElementType);
        } else {
            copy.elementType = null;
        }
        if (this.isSetSampleType()) {
            TridasVocabulary.SampleType sourceSampleType;
            sourceSampleType = this.getSampleType();
            TridasVocabulary.SampleType copySampleType = ((TridasVocabulary.SampleType) copyBuilder.copy(sourceSampleType));
            copy.setSampleType(copySampleType);
        } else {
            copy.sampleType = null;
        }
        if (this.isSetDerivedSeriesType()) {
            TridasVocabulary.DerivedSeriesType sourceDerivedSeriesType;
            sourceDerivedSeriesType = this.getDerivedSeriesType();
            TridasVocabulary.DerivedSeriesType copyDerivedSeriesType = ((TridasVocabulary.DerivedSeriesType) copyBuilder.copy(sourceDerivedSeriesType));
            copy.setDerivedSeriesType(copyDerivedSeriesType);
        } else {
            copy.derivedSeriesType = null;
        }
        if (this.isSetElementTaxon()) {
            TridasVocabulary.ElementTaxon sourceElementTaxon;
            sourceElementTaxon = this.getElementTaxon();
            TridasVocabulary.ElementTaxon copyElementTaxon = ((TridasVocabulary.ElementTaxon) copyBuilder.copy(sourceElementTaxon));
            copy.setElementTaxon(copyElementTaxon);
        } else {
            copy.elementTaxon = null;
        }
        if (this.isSetElementShape()) {
            TridasVocabulary.ElementShape sourceElementShape;
            sourceElementShape = this.getElementShape();
            TridasVocabulary.ElementShape copyElementShape = ((TridasVocabulary.ElementShape) copyBuilder.copy(sourceElementShape));
            copy.setElementShape(copyElementShape);
        } else {
            copy.elementShape = null;
        }
        if (this.isSetMeasurementSeriesMeasuringMethod()) {
            TridasVocabulary.MeasurementSeriesMeasuringMethod sourceMeasurementSeriesMeasuringMethod;
            sourceMeasurementSeriesMeasuringMethod = this.getMeasurementSeriesMeasuringMethod();
            TridasVocabulary.MeasurementSeriesMeasuringMethod copyMeasurementSeriesMeasuringMethod = ((TridasVocabulary.MeasurementSeriesMeasuringMethod) copyBuilder.copy(sourceMeasurementSeriesMeasuringMethod));
            copy.setMeasurementSeriesMeasuringMethod(copyMeasurementSeriesMeasuringMethod);
        } else {
            copy.measurementSeriesMeasuringMethod = null;
        }
        if (this.isSetValuesVariable()) {
            TridasVocabulary.ValuesVariable sourceValuesVariable;
            sourceValuesVariable = this.getValuesVariable();
            TridasVocabulary.ValuesVariable copyValuesVariable = ((TridasVocabulary.ValuesVariable) copyBuilder.copy(sourceValuesVariable));
            copy.setValuesVariable(copyValuesVariable);
        } else {
            copy.valuesVariable = null;
        }
        if (this.isSetValuesRemark()) {
            TridasVocabulary.ValuesRemark sourceValuesRemark;
            sourceValuesRemark = this.getValuesRemark();
            TridasVocabulary.ValuesRemark copyValuesRemark = ((TridasVocabulary.ValuesRemark) copyBuilder.copy(sourceValuesRemark));
            copy.setValuesRemark(copyValuesRemark);
        } else {
            copy.valuesRemark = null;
        }
        if (this.isSetLocationType()) {
            TridasVocabulary.LocationType sourceLocationType;
            sourceLocationType = this.getLocationType();
            TridasVocabulary.LocationType copyLocationType = ((TridasVocabulary.LocationType) copyBuilder.copy(sourceLocationType));
            copy.setLocationType(copyLocationType);
        } else {
            copy.locationType = null;
        }
        if (this.isSetGlobalUnit()) {
            TridasVocabulary.GlobalUnit sourceGlobalUnit;
            sourceGlobalUnit = this.getGlobalUnit();
            TridasVocabulary.GlobalUnit copyGlobalUnit = ((TridasVocabulary.GlobalUnit) copyBuilder.copy(sourceGlobalUnit));
            copy.setGlobalUnit(copyGlobalUnit);
        } else {
            copy.globalUnit = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.DerivedSeriesType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.DerivedSeriesType that = ((TridasVocabulary.DerivedSeriesType) object);
            equalsBuilder.append(this.getTypes(), that.getTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.DerivedSeriesType)) {
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
            hashCodeBuilder.append(this.getTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTypes;
                theTypes = this.getTypes();
                toStringBuilder.append("types", theTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.DerivedSeriesType copy = ((target == null)?((TridasVocabulary.DerivedSeriesType) createCopy()):((TridasVocabulary.DerivedSeriesType) target));
            if (this.isSetTypes()) {
                List<ControlledVoc> sourceTypes;
                sourceTypes = this.getTypes();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceTypes));
                copy.setTypes(copyTypes);
            } else {
                copy.unsetTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.DerivedSeriesType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}shape" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ElementShape)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ElementShape that = ((TridasVocabulary.ElementShape) object);
            equalsBuilder.append(this.getShapes(), that.getShapes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ElementShape)) {
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
            hashCodeBuilder.append(this.getShapes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasShape> theShapes;
                theShapes = this.getShapes();
                toStringBuilder.append("shapes", theShapes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ElementShape copy = ((target == null)?((TridasVocabulary.ElementShape) createCopy()):((TridasVocabulary.ElementShape) target));
            if (this.isSetShapes()) {
                List<TridasShape> sourceShapes;
                sourceShapes = this.getShapes();
                @SuppressWarnings("unchecked")
                List<TridasShape> copyShapes = ((List<TridasShape> ) copyBuilder.copy(sourceShapes));
                copy.setShapes(copyShapes);
            } else {
                copy.unsetShapes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ElementShape();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}taxon" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ElementTaxon)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ElementTaxon that = ((TridasVocabulary.ElementTaxon) object);
            equalsBuilder.append(this.getTaxons(), that.getTaxons());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ElementTaxon)) {
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
            hashCodeBuilder.append(this.getTaxons());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTaxons;
                theTaxons = this.getTaxons();
                toStringBuilder.append("taxons", theTaxons);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ElementTaxon copy = ((target == null)?((TridasVocabulary.ElementTaxon) createCopy()):((TridasVocabulary.ElementTaxon) target));
            if (this.isSetTaxons()) {
                List<ControlledVoc> sourceTaxons;
                sourceTaxons = this.getTaxons();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTaxons = ((List<ControlledVoc> ) copyBuilder.copy(sourceTaxons));
                copy.setTaxons(copyTaxons);
            } else {
                copy.unsetTaxons();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ElementTaxon();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ElementType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ElementType that = ((TridasVocabulary.ElementType) object);
            equalsBuilder.append(this.getTypes(), that.getTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ElementType)) {
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
            hashCodeBuilder.append(this.getTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTypes;
                theTypes = this.getTypes();
                toStringBuilder.append("types", theTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ElementType copy = ((target == null)?((TridasVocabulary.ElementType) createCopy()):((TridasVocabulary.ElementType) target));
            if (this.isSetTypes()) {
                List<ControlledVoc> sourceTypes;
                sourceTypes = this.getTypes();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceTypes));
                copy.setTypes(copyTypes);
            } else {
                copy.unsetTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ElementType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}unit" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.GlobalUnit)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.GlobalUnit that = ((TridasVocabulary.GlobalUnit) object);
            equalsBuilder.append(this.getUnits(), that.getUnits());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.GlobalUnit)) {
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
            hashCodeBuilder.append(this.getUnits());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasUnit> theUnits;
                theUnits = this.getUnits();
                toStringBuilder.append("units", theUnits);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.GlobalUnit copy = ((target == null)?((TridasVocabulary.GlobalUnit) createCopy()):((TridasVocabulary.GlobalUnit) target));
            if (this.isSetUnits()) {
                List<TridasUnit> sourceUnits;
                sourceUnits = this.getUnits();
                @SuppressWarnings("unchecked")
                List<TridasUnit> copyUnits = ((List<TridasUnit> ) copyBuilder.copy(sourceUnits));
                copy.setUnits(copyUnits);
            } else {
                copy.unsetUnits();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.GlobalUnit();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}locationType" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.LocationType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.LocationType that = ((TridasVocabulary.LocationType) object);
            equalsBuilder.append(this.getLocationTypes(), that.getLocationTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.LocationType)) {
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
            hashCodeBuilder.append(this.getLocationTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<NormalTridasLocationType> theLocationTypes;
                theLocationTypes = this.getLocationTypes();
                toStringBuilder.append("locationTypes", theLocationTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.LocationType copy = ((target == null)?((TridasVocabulary.LocationType) createCopy()):((TridasVocabulary.LocationType) target));
            if (this.isSetLocationTypes()) {
                List<NormalTridasLocationType> sourceLocationTypes;
                sourceLocationTypes = this.getLocationTypes();
                @SuppressWarnings("unchecked")
                List<NormalTridasLocationType> copyLocationTypes = ((List<NormalTridasLocationType> ) copyBuilder.copy(sourceLocationTypes));
                copy.setLocationTypes(copyLocationTypes);
            } else {
                copy.unsetLocationTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.LocationType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}measuringMethod" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.MeasurementSeriesMeasuringMethod)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.MeasurementSeriesMeasuringMethod that = ((TridasVocabulary.MeasurementSeriesMeasuringMethod) object);
            equalsBuilder.append(this.getMeasuringMethods(), that.getMeasuringMethods());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.MeasurementSeriesMeasuringMethod)) {
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
            hashCodeBuilder.append(this.getMeasuringMethods());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasMeasuringMethod> theMeasuringMethods;
                theMeasuringMethods = this.getMeasuringMethods();
                toStringBuilder.append("measuringMethods", theMeasuringMethods);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.MeasurementSeriesMeasuringMethod copy = ((target == null)?((TridasVocabulary.MeasurementSeriesMeasuringMethod) createCopy()):((TridasVocabulary.MeasurementSeriesMeasuringMethod) target));
            if (this.isSetMeasuringMethods()) {
                List<TridasMeasuringMethod> sourceMeasuringMethods;
                sourceMeasuringMethods = this.getMeasuringMethods();
                @SuppressWarnings("unchecked")
                List<TridasMeasuringMethod> copyMeasuringMethods = ((List<TridasMeasuringMethod> ) copyBuilder.copy(sourceMeasuringMethods));
                copy.setMeasuringMethods(copyMeasuringMethods);
            } else {
                copy.unsetMeasuringMethods();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.MeasurementSeriesMeasuringMethod();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ObjectType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ObjectType that = ((TridasVocabulary.ObjectType) object);
            equalsBuilder.append(this.getTypes(), that.getTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ObjectType)) {
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
            hashCodeBuilder.append(this.getTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTypes;
                theTypes = this.getTypes();
                toStringBuilder.append("types", theTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ObjectType copy = ((target == null)?((TridasVocabulary.ObjectType) createCopy()):((TridasVocabulary.ObjectType) target));
            if (this.isSetTypes()) {
                List<ControlledVoc> sourceTypes;
                sourceTypes = this.getTypes();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceTypes));
                copy.setTypes(copyTypes);
            } else {
                copy.unsetTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ObjectType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}category" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlElement(name = "category", required = true)
        protected List<TridasCategory> categories;

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
         * {@link TridasCategory }
         * 
         * 
         */
        public List<TridasCategory> getCategories() {
            if (categories == null) {
                categories = new ArrayList<TridasCategory>();
            }
            return this.categories;
        }

        public boolean isSetCategories() {
            return ((this.categories!= null)&&(!this.categories.isEmpty()));
        }

        public void unsetCategories() {
            this.categories = null;
        }

        /**
         * Sets the value of the categories property.
         * 
         * @param categories
         *     allowed object is
         *     {@link TridasCategory }
         *     
         */
        public void setCategories(List<TridasCategory> categories) {
            this.categories = categories;
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ProjectCategory)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ProjectCategory that = ((TridasVocabulary.ProjectCategory) object);
            equalsBuilder.append(this.getCategories(), that.getCategories());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ProjectCategory)) {
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
            hashCodeBuilder.append(this.getCategories());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasCategory> theCategories;
                theCategories = this.getCategories();
                toStringBuilder.append("categories", theCategories);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ProjectCategory copy = ((target == null)?((TridasVocabulary.ProjectCategory) createCopy()):((TridasVocabulary.ProjectCategory) target));
            if (this.isSetCategories()) {
                List<TridasCategory> sourceCategories;
                sourceCategories = this.getCategories();
                @SuppressWarnings("unchecked")
                List<TridasCategory> copyCategories = ((List<TridasCategory> ) copyBuilder.copy(sourceCategories));
                copy.setCategories(copyCategories);
            } else {
                copy.unsetCategories();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ProjectCategory();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ProjectType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ProjectType that = ((TridasVocabulary.ProjectType) object);
            equalsBuilder.append(this.getTypes(), that.getTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ProjectType)) {
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
            hashCodeBuilder.append(this.getTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTypes;
                theTypes = this.getTypes();
                toStringBuilder.append("types", theTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ProjectType copy = ((target == null)?((TridasVocabulary.ProjectType) createCopy()):((TridasVocabulary.ProjectType) target));
            if (this.isSetTypes()) {
                List<ControlledVoc> sourceTypes;
                sourceTypes = this.getTypes();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceTypes));
                copy.setTypes(copyTypes);
            } else {
                copy.unsetTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ProjectType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}type" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.SampleType)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.SampleType that = ((TridasVocabulary.SampleType) object);
            equalsBuilder.append(this.getTypes(), that.getTypes());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.SampleType)) {
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
            hashCodeBuilder.append(this.getTypes());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<ControlledVoc> theTypes;
                theTypes = this.getTypes();
                toStringBuilder.append("types", theTypes);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.SampleType copy = ((target == null)?((TridasVocabulary.SampleType) createCopy()):((TridasVocabulary.SampleType) target));
            if (this.isSetTypes()) {
                List<ControlledVoc> sourceTypes;
                sourceTypes = this.getTypes();
                @SuppressWarnings("unchecked")
                List<ControlledVoc> copyTypes = ((List<ControlledVoc> ) copyBuilder.copy(sourceTypes));
                copy.setTypes(copyTypes);
            } else {
                copy.unsetTypes();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.SampleType();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}remark" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ValuesRemark)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ValuesRemark that = ((TridasVocabulary.ValuesRemark) object);
            equalsBuilder.append(this.getRemarks(), that.getRemarks());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ValuesRemark)) {
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
            hashCodeBuilder.append(this.getRemarks());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasRemark> theRemarks;
                theRemarks = this.getRemarks();
                toStringBuilder.append("remarks", theRemarks);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ValuesRemark copy = ((target == null)?((TridasVocabulary.ValuesRemark) createCopy()):((TridasVocabulary.ValuesRemark) target));
            if (this.isSetRemarks()) {
                List<TridasRemark> sourceRemarks;
                sourceRemarks = this.getRemarks();
                @SuppressWarnings("unchecked")
                List<TridasRemark> copyRemarks = ((List<TridasRemark> ) copyBuilder.copy(sourceRemarks));
                copy.setRemarks(copyRemarks);
            } else {
                copy.unsetRemarks();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ValuesRemark();
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
     *         &lt;element ref="{http://www.tridas.org/1.2.1}variable" maxOccurs="unbounded"/>
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
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
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

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof TridasVocabulary.ValuesVariable)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final TridasVocabulary.ValuesVariable that = ((TridasVocabulary.ValuesVariable) object);
            equalsBuilder.append(this.getVariables(), that.getVariables());
        }

        public boolean equals(Object object) {
            if (!(object instanceof TridasVocabulary.ValuesVariable)) {
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
            hashCodeBuilder.append(this.getVariables());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                List<TridasVariable> theVariables;
                theVariables = this.getVariables();
                toStringBuilder.append("variables", theVariables);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final TridasVocabulary.ValuesVariable copy = ((target == null)?((TridasVocabulary.ValuesVariable) createCopy()):((TridasVocabulary.ValuesVariable) target));
            if (this.isSetVariables()) {
                List<TridasVariable> sourceVariables;
                sourceVariables = this.getVariables();
                @SuppressWarnings("unchecked")
                List<TridasVariable> copyVariables = ((List<TridasVariable> ) copyBuilder.copy(sourceVariables));
                copy.setVariables(copyVariables);
            } else {
                copy.unsetVariables();
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new TridasVocabulary.ValuesVariable();
        }

    }

}
