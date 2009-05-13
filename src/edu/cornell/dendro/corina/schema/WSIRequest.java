//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.08 at 01:25:56 PM PDT 
//


package edu.cornell.dendro.corina.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}searchParams" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}authenticate" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}entity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.tridas.org/1.2}project" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}element" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}sample" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}radius" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="dictionaries">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/choice>
 *       &lt;attribute name="type" type="{http://dendro.cornell.edu/schema/corina/1.0}corinaRequestType" />
 *       &lt;attribute name="format" type="{http://dendro.cornell.edu/schema/corina/1.0}corinaRequestFormat" />
 *       &lt;attribute name="parentEntityID" type="{http://www.w3.org/2001/XMLSchema}token" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "searchParams",
    "authenticate",
    "entity",
    "project",
    "object",
    "element",
    "sample",
    "radius",
    "measurementSeries",
    "derivedSeries",
    "dictionaries"
})
@XmlRootElement(name = "request")
public class WSIRequest {

    protected WSISearchParams searchParams;
    protected WSIAuthenticate authenticate;
    protected List<WSIEntity> entity;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasProject> project;
    @XmlElement(namespace = "http://www.tridas.org/1.2", type = TridasObjectEx.class)
    protected List<TridasObject> object;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasElement> element;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasSample> sample;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasRadius> radius;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasMeasurementSeries> measurementSeries;
    @XmlElement(namespace = "http://www.tridas.org/1.2")
    protected List<TridasDerivedSeries> derivedSeries;
    protected WSIRequest.Dictionaries dictionaries;
    @XmlAttribute(name = "type")
    protected CorinaRequestType type;
    @XmlAttribute(name = "format")
    protected CorinaRequestFormat format;
    @XmlAttribute(name = "parentEntityID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String parentEntityID;

    /**
     * Gets the value of the searchParams property.
     * 
     * @return
     *     possible object is
     *     {@link WSISearchParams }
     *     
     */
    public WSISearchParams getSearchParams() {
        return searchParams;
    }

    /**
     * Sets the value of the searchParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSISearchParams }
     *     
     */
    public void setSearchParams(WSISearchParams value) {
        this.searchParams = value;
    }

    public boolean isSetSearchParams() {
        return (this.searchParams!= null);
    }

    /**
     * Gets the value of the authenticate property.
     * 
     * @return
     *     possible object is
     *     {@link WSIAuthenticate }
     *     
     */
    public WSIAuthenticate getAuthenticate() {
        return authenticate;
    }

    /**
     * Sets the value of the authenticate property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIAuthenticate }
     *     
     */
    public void setAuthenticate(WSIAuthenticate value) {
        this.authenticate = value;
    }

    public boolean isSetAuthenticate() {
        return (this.authenticate!= null);
    }

    /**
     * Gets the value of the entity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIEntity }
     * 
     * 
     */
    public List<WSIEntity> getEntity() {
        if (entity == null) {
            entity = new ArrayList<WSIEntity>();
        }
        return this.entity;
    }

    public boolean isSetEntity() {
        return ((this.entity!= null)&&(!this.entity.isEmpty()));
    }

    public void unsetEntity() {
        this.entity = null;
    }

    /**
     * Gets the value of the project property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the project property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasProject }
     * 
     * 
     */
    public List<TridasProject> getProject() {
        if (project == null) {
            project = new ArrayList<TridasProject>();
        }
        return this.project;
    }

    public boolean isSetProject() {
        return ((this.project!= null)&&(!this.project.isEmpty()));
    }

    public void unsetProject() {
        this.project = null;
    }

    /**
     * Gets the value of the object property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the object property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasObject }
     * 
     * 
     */
    public List<TridasObject> getObject() {
        if (object == null) {
            object = new ArrayList<TridasObject>();
        }
        return this.object;
    }

    public boolean isSetObject() {
        return ((this.object!= null)&&(!this.object.isEmpty()));
    }

    public void unsetObject() {
        this.object = null;
    }

    /**
     * Gets the value of the element property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the element property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasElement }
     * 
     * 
     */
    public List<TridasElement> getElement() {
        if (element == null) {
            element = new ArrayList<TridasElement>();
        }
        return this.element;
    }

    public boolean isSetElement() {
        return ((this.element!= null)&&(!this.element.isEmpty()));
    }

    public void unsetElement() {
        this.element = null;
    }

    /**
     * Gets the value of the sample property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sample property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSample().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasSample }
     * 
     * 
     */
    public List<TridasSample> getSample() {
        if (sample == null) {
            sample = new ArrayList<TridasSample>();
        }
        return this.sample;
    }

    public boolean isSetSample() {
        return ((this.sample!= null)&&(!this.sample.isEmpty()));
    }

    public void unsetSample() {
        this.sample = null;
    }

    /**
     * Gets the value of the radius property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radius property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadius().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasRadius }
     * 
     * 
     */
    public List<TridasRadius> getRadius() {
        if (radius == null) {
            radius = new ArrayList<TridasRadius>();
        }
        return this.radius;
    }

    public boolean isSetRadius() {
        return ((this.radius!= null)&&(!this.radius.isEmpty()));
    }

    public void unsetRadius() {
        this.radius = null;
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
     * Gets the value of the derivedSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the derivedSeries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDerivedSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasDerivedSeries }
     * 
     * 
     */
    public List<TridasDerivedSeries> getDerivedSeries() {
        if (derivedSeries == null) {
            derivedSeries = new ArrayList<TridasDerivedSeries>();
        }
        return this.derivedSeries;
    }

    public boolean isSetDerivedSeries() {
        return ((this.derivedSeries!= null)&&(!this.derivedSeries.isEmpty()));
    }

    public void unsetDerivedSeries() {
        this.derivedSeries = null;
    }

    /**
     * Gets the value of the dictionaries property.
     * 
     * @return
     *     possible object is
     *     {@link WSIRequest.Dictionaries }
     *     
     */
    public WSIRequest.Dictionaries getDictionaries() {
        return dictionaries;
    }

    /**
     * Sets the value of the dictionaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIRequest.Dictionaries }
     *     
     */
    public void setDictionaries(WSIRequest.Dictionaries value) {
        this.dictionaries = value;
    }

    public boolean isSetDictionaries() {
        return (this.dictionaries!= null);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link CorinaRequestType }
     *     
     */
    public CorinaRequestType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorinaRequestType }
     *     
     */
    public void setType(CorinaRequestType value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link CorinaRequestFormat }
     *     
     */
    public CorinaRequestFormat getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorinaRequestFormat }
     *     
     */
    public void setFormat(CorinaRequestFormat value) {
        this.format = value;
    }

    public boolean isSetFormat() {
        return (this.format!= null);
    }

    /**
     * Gets the value of the parentEntityID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentEntityID() {
        return parentEntityID;
    }

    /**
     * Sets the value of the parentEntityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentEntityID(String value) {
        this.parentEntityID = value;
    }

    public boolean isSetParentEntityID() {
        return (this.parentEntityID!= null);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
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
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Dictionaries {


        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }

        @Override
        public boolean equals(Object that) {
            return EqualsBuilder.reflectionEquals(this, that);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

    }

}
