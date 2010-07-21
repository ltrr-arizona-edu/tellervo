
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
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
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


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
 *           &lt;element ref="{http://www.tridas.org/1.2.1}project" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}object" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}element" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}sample" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}radius" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.1}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="user" type="{http://dendro.cornell.edu/schema/corina/1.0}securityUser" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}box" maxOccurs="unbounded" minOccurs="0"/>
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
    "dictionaries",
    "projects",
    "objects",
    "elements",
    "samples",
    "radiuses",
    "measurementSeries",
    "derivedSeries",
    "users",
    "boxes",
    "entities",
    "authenticate",
    "searchParams"
})
@XmlRootElement(name = "request")
public class WSIRequest
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSIRequest.Dictionaries dictionaries;
    @XmlElement(name = "project", namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasProject> projects;
    @XmlElement(name = "object", namespace = "http://www.tridas.org/1.2.1", type = TridasObjectEx.class)
    protected List<TridasObject> objects;
    @XmlElement(name = "element", namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasElement> elements;
    @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasSample> samples;
    @XmlElement(name = "radius", namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasRadius> radiuses;
    @XmlElement(namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasMeasurementSeries> measurementSeries;
    @XmlElement(namespace = "http://www.tridas.org/1.2.1")
    protected List<TridasDerivedSeries> derivedSeries;
    @XmlElement(name = "user")
    protected List<SecurityUser> users;
    @XmlElement(name = "box")
    protected List<WSIBox> boxes;
    @XmlElement(name = "entity")
    protected List<WSIEntity> entities;
    protected WSIAuthenticate authenticate;
    protected WSISearchParams searchParams;
    @XmlAttribute(name = "type")
    protected CorinaRequestType type;
    @XmlAttribute(name = "format")
    protected CorinaRequestFormat format;
    @XmlAttribute(name = "parentEntityID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String parentEntityID;

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
     * Gets the value of the projects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasProject }
     * 
     * 
     */
    public List<TridasProject> getProjects() {
        if (projects == null) {
            projects = new ArrayList<TridasProject>();
        }
        return this.projects;
    }

    public boolean isSetProjects() {
        return ((this.projects!= null)&&(!this.projects.isEmpty()));
    }

    public void unsetProjects() {
        this.projects = null;
    }

    /**
     * Gets the value of the objects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasObject }
     * 
     * 
     */
    public List<TridasObject> getObjects() {
        if (objects == null) {
            objects = new ArrayList<TridasObject>();
        }
        return this.objects;
    }

    public boolean isSetObjects() {
        return ((this.objects!= null)&&(!this.objects.isEmpty()));
    }

    public void unsetObjects() {
        this.objects = null;
    }

    /**
     * Gets the value of the elements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasElement }
     * 
     * 
     */
    public List<TridasElement> getElements() {
        if (elements == null) {
            elements = new ArrayList<TridasElement>();
        }
        return this.elements;
    }

    public boolean isSetElements() {
        return ((this.elements!= null)&&(!this.elements.isEmpty()));
    }

    public void unsetElements() {
        this.elements = null;
    }

    /**
     * Gets the value of the samples property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the samples property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSamples().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasSample }
     * 
     * 
     */
    public List<TridasSample> getSamples() {
        if (samples == null) {
            samples = new ArrayList<TridasSample>();
        }
        return this.samples;
    }

    public boolean isSetSamples() {
        return ((this.samples!= null)&&(!this.samples.isEmpty()));
    }

    public void unsetSamples() {
        this.samples = null;
    }

    /**
     * Gets the value of the radiuses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radiuses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadiuses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasRadius }
     * 
     * 
     */
    public List<TridasRadius> getRadiuses() {
        if (radiuses == null) {
            radiuses = new ArrayList<TridasRadius>();
        }
        return this.radiuses;
    }

    public boolean isSetRadiuses() {
        return ((this.radiuses!= null)&&(!this.radiuses.isEmpty()));
    }

    public void unsetRadiuses() {
        this.radiuses = null;
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
     * Gets the value of the users property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the users property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityUser }
     * 
     * 
     */
    public List<SecurityUser> getUsers() {
        if (users == null) {
            users = new ArrayList<SecurityUser>();
        }
        return this.users;
    }

    public boolean isSetUsers() {
        return ((this.users!= null)&&(!this.users.isEmpty()));
    }

    public void unsetUsers() {
        this.users = null;
    }

    /**
     * Gets the value of the boxes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the boxes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBoxes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIBox }
     * 
     * 
     */
    public List<WSIBox> getBoxes() {
        if (boxes == null) {
            boxes = new ArrayList<WSIBox>();
        }
        return this.boxes;
    }

    public boolean isSetBoxes() {
        return ((this.boxes!= null)&&(!this.boxes.isEmpty()));
    }

    public void unsetBoxes() {
        this.boxes = null;
    }

    /**
     * Gets the value of the entities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIEntity }
     * 
     * 
     */
    public List<WSIEntity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<WSIEntity>();
        }
        return this.entities;
    }

    public boolean isSetEntities() {
        return ((this.entities!= null)&&(!this.entities.isEmpty()));
    }

    public void unsetEntities() {
        this.entities = null;
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

    /**
     * Sets the value of the projects property.
     * 
     * @param projects
     *     allowed object is
     *     {@link TridasProject }
     *     
     */
    public void setProjects(List<TridasProject> projects) {
        this.projects = projects;
    }

    /**
     * Sets the value of the objects property.
     * 
     * @param objects
     *     allowed object is
     *     {@link TridasObject }
     *     
     */
    public void setObjects(List<TridasObject> objects) {
        this.objects = objects;
    }

    /**
     * Sets the value of the elements property.
     * 
     * @param elements
     *     allowed object is
     *     {@link TridasElement }
     *     
     */
    public void setElements(List<TridasElement> elements) {
        this.elements = elements;
    }

    /**
     * Sets the value of the samples property.
     * 
     * @param samples
     *     allowed object is
     *     {@link TridasSample }
     *     
     */
    public void setSamples(List<TridasSample> samples) {
        this.samples = samples;
    }

    /**
     * Sets the value of the radiuses property.
     * 
     * @param radiuses
     *     allowed object is
     *     {@link TridasRadius }
     *     
     */
    public void setRadiuses(List<TridasRadius> radiuses) {
        this.radiuses = radiuses;
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

    /**
     * Sets the value of the derivedSeries property.
     * 
     * @param derivedSeries
     *     allowed object is
     *     {@link TridasDerivedSeries }
     *     
     */
    public void setDerivedSeries(List<TridasDerivedSeries> derivedSeries) {
        this.derivedSeries = derivedSeries;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param users
     *     allowed object is
     *     {@link SecurityUser }
     *     
     */
    public void setUsers(List<SecurityUser> users) {
        this.users = users;
    }

    /**
     * Sets the value of the boxes property.
     * 
     * @param boxes
     *     allowed object is
     *     {@link WSIBox }
     *     
     */
    public void setBoxes(List<WSIBox> boxes) {
        this.boxes = boxes;
    }

    /**
     * Sets the value of the entities property.
     * 
     * @param entities
     *     allowed object is
     *     {@link WSIEntity }
     *     
     */
    public void setEntities(List<WSIEntity> entities) {
        this.entities = entities;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIRequest)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIRequest that = ((WSIRequest) object);
        equalsBuilder.append(this.getDictionaries(), that.getDictionaries());
        equalsBuilder.append(this.getProjects(), that.getProjects());
        equalsBuilder.append(this.getObjects(), that.getObjects());
        equalsBuilder.append(this.getElements(), that.getElements());
        equalsBuilder.append(this.getSamples(), that.getSamples());
        equalsBuilder.append(this.getRadiuses(), that.getRadiuses());
        equalsBuilder.append(this.getMeasurementSeries(), that.getMeasurementSeries());
        equalsBuilder.append(this.getDerivedSeries(), that.getDerivedSeries());
        equalsBuilder.append(this.getUsers(), that.getUsers());
        equalsBuilder.append(this.getBoxes(), that.getBoxes());
        equalsBuilder.append(this.getEntities(), that.getEntities());
        equalsBuilder.append(this.getAuthenticate(), that.getAuthenticate());
        equalsBuilder.append(this.getSearchParams(), that.getSearchParams());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getFormat(), that.getFormat());
        equalsBuilder.append(this.getParentEntityID(), that.getParentEntityID());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIRequest)) {
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
        hashCodeBuilder.append(this.getDictionaries());
        hashCodeBuilder.append(this.getProjects());
        hashCodeBuilder.append(this.getObjects());
        hashCodeBuilder.append(this.getElements());
        hashCodeBuilder.append(this.getSamples());
        hashCodeBuilder.append(this.getRadiuses());
        hashCodeBuilder.append(this.getMeasurementSeries());
        hashCodeBuilder.append(this.getDerivedSeries());
        hashCodeBuilder.append(this.getUsers());
        hashCodeBuilder.append(this.getBoxes());
        hashCodeBuilder.append(this.getEntities());
        hashCodeBuilder.append(this.getAuthenticate());
        hashCodeBuilder.append(this.getSearchParams());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getFormat());
        hashCodeBuilder.append(this.getParentEntityID());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            WSIRequest.Dictionaries theDictionaries;
            theDictionaries = this.getDictionaries();
            toStringBuilder.append("dictionaries", theDictionaries);
        }
        {
            List<TridasProject> theProjects;
            theProjects = this.getProjects();
            toStringBuilder.append("projects", theProjects);
        }
        {
            List<TridasObject> theObjects;
            theObjects = this.getObjects();
            toStringBuilder.append("objects", theObjects);
        }
        {
            List<TridasElement> theElements;
            theElements = this.getElements();
            toStringBuilder.append("elements", theElements);
        }
        {
            List<TridasSample> theSamples;
            theSamples = this.getSamples();
            toStringBuilder.append("samples", theSamples);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = this.getRadiuses();
            toStringBuilder.append("radiuses", theRadiuses);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = this.getMeasurementSeries();
            toStringBuilder.append("measurementSeries", theMeasurementSeries);
        }
        {
            List<TridasDerivedSeries> theDerivedSeries;
            theDerivedSeries = this.getDerivedSeries();
            toStringBuilder.append("derivedSeries", theDerivedSeries);
        }
        {
            List<SecurityUser> theUsers;
            theUsers = this.getUsers();
            toStringBuilder.append("users", theUsers);
        }
        {
            List<WSIBox> theBoxes;
            theBoxes = this.getBoxes();
            toStringBuilder.append("boxes", theBoxes);
        }
        {
            List<WSIEntity> theEntities;
            theEntities = this.getEntities();
            toStringBuilder.append("entities", theEntities);
        }
        {
            WSIAuthenticate theAuthenticate;
            theAuthenticate = this.getAuthenticate();
            toStringBuilder.append("authenticate", theAuthenticate);
        }
        {
            WSISearchParams theSearchParams;
            theSearchParams = this.getSearchParams();
            toStringBuilder.append("searchParams", theSearchParams);
        }
        {
            CorinaRequestType theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            CorinaRequestFormat theFormat;
            theFormat = this.getFormat();
            toStringBuilder.append("format", theFormat);
        }
        {
            String theParentEntityID;
            theParentEntityID = this.getParentEntityID();
            toStringBuilder.append("parentEntityID", theParentEntityID);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIRequest copy = ((target == null)?((WSIRequest) createCopy()):((WSIRequest) target));
        if (this.isSetDictionaries()) {
            WSIRequest.Dictionaries sourceDictionaries;
            sourceDictionaries = this.getDictionaries();
            WSIRequest.Dictionaries copyDictionaries = ((WSIRequest.Dictionaries) copyBuilder.copy(sourceDictionaries));
            copy.setDictionaries(copyDictionaries);
        } else {
            copy.dictionaries = null;
        }
        if (this.isSetProjects()) {
            List<TridasProject> sourceProjects;
            sourceProjects = this.getProjects();
            @SuppressWarnings("unchecked")
            List<TridasProject> copyProjects = ((List<TridasProject> ) copyBuilder.copy(sourceProjects));
            copy.setProjects(copyProjects);
        } else {
            copy.unsetProjects();
        }
        if (this.isSetObjects()) {
            List<TridasObject> sourceObjects;
            sourceObjects = this.getObjects();
            @SuppressWarnings("unchecked")
            List<TridasObject> copyObjects = ((List<TridasObject> ) copyBuilder.copy(sourceObjects));
            copy.setObjects(copyObjects);
        } else {
            copy.unsetObjects();
        }
        if (this.isSetElements()) {
            List<TridasElement> sourceElements;
            sourceElements = this.getElements();
            @SuppressWarnings("unchecked")
            List<TridasElement> copyElements = ((List<TridasElement> ) copyBuilder.copy(sourceElements));
            copy.setElements(copyElements);
        } else {
            copy.unsetElements();
        }
        if (this.isSetSamples()) {
            List<TridasSample> sourceSamples;
            sourceSamples = this.getSamples();
            @SuppressWarnings("unchecked")
            List<TridasSample> copySamples = ((List<TridasSample> ) copyBuilder.copy(sourceSamples));
            copy.setSamples(copySamples);
        } else {
            copy.unsetSamples();
        }
        if (this.isSetRadiuses()) {
            List<TridasRadius> sourceRadiuses;
            sourceRadiuses = this.getRadiuses();
            @SuppressWarnings("unchecked")
            List<TridasRadius> copyRadiuses = ((List<TridasRadius> ) copyBuilder.copy(sourceRadiuses));
            copy.setRadiuses(copyRadiuses);
        } else {
            copy.unsetRadiuses();
        }
        if (this.isSetMeasurementSeries()) {
            List<TridasMeasurementSeries> sourceMeasurementSeries;
            sourceMeasurementSeries = this.getMeasurementSeries();
            @SuppressWarnings("unchecked")
            List<TridasMeasurementSeries> copyMeasurementSeries = ((List<TridasMeasurementSeries> ) copyBuilder.copy(sourceMeasurementSeries));
            copy.setMeasurementSeries(copyMeasurementSeries);
        } else {
            copy.unsetMeasurementSeries();
        }
        if (this.isSetDerivedSeries()) {
            List<TridasDerivedSeries> sourceDerivedSeries;
            sourceDerivedSeries = this.getDerivedSeries();
            @SuppressWarnings("unchecked")
            List<TridasDerivedSeries> copyDerivedSeries = ((List<TridasDerivedSeries> ) copyBuilder.copy(sourceDerivedSeries));
            copy.setDerivedSeries(copyDerivedSeries);
        } else {
            copy.unsetDerivedSeries();
        }
        if (this.isSetUsers()) {
            List<SecurityUser> sourceUsers;
            sourceUsers = this.getUsers();
            @SuppressWarnings("unchecked")
            List<SecurityUser> copyUsers = ((List<SecurityUser> ) copyBuilder.copy(sourceUsers));
            copy.setUsers(copyUsers);
        } else {
            copy.unsetUsers();
        }
        if (this.isSetBoxes()) {
            List<WSIBox> sourceBoxes;
            sourceBoxes = this.getBoxes();
            @SuppressWarnings("unchecked")
            List<WSIBox> copyBoxes = ((List<WSIBox> ) copyBuilder.copy(sourceBoxes));
            copy.setBoxes(copyBoxes);
        } else {
            copy.unsetBoxes();
        }
        if (this.isSetEntities()) {
            List<WSIEntity> sourceEntities;
            sourceEntities = this.getEntities();
            @SuppressWarnings("unchecked")
            List<WSIEntity> copyEntities = ((List<WSIEntity> ) copyBuilder.copy(sourceEntities));
            copy.setEntities(copyEntities);
        } else {
            copy.unsetEntities();
        }
        if (this.isSetAuthenticate()) {
            WSIAuthenticate sourceAuthenticate;
            sourceAuthenticate = this.getAuthenticate();
            WSIAuthenticate copyAuthenticate = ((WSIAuthenticate) copyBuilder.copy(sourceAuthenticate));
            copy.setAuthenticate(copyAuthenticate);
        } else {
            copy.authenticate = null;
        }
        if (this.isSetSearchParams()) {
            WSISearchParams sourceSearchParams;
            sourceSearchParams = this.getSearchParams();
            WSISearchParams copySearchParams = ((WSISearchParams) copyBuilder.copy(sourceSearchParams));
            copy.setSearchParams(copySearchParams);
        } else {
            copy.searchParams = null;
        }
        if (this.isSetType()) {
            CorinaRequestType sourceType;
            sourceType = this.getType();
            CorinaRequestType copyType = ((CorinaRequestType) copyBuilder.copy(sourceType));
            copy.setType(copyType);
        } else {
            copy.type = null;
        }
        if (this.isSetFormat()) {
            CorinaRequestFormat sourceFormat;
            sourceFormat = this.getFormat();
            CorinaRequestFormat copyFormat = ((CorinaRequestFormat) copyBuilder.copy(sourceFormat));
            copy.setFormat(copyFormat);
        } else {
            copy.format = null;
        }
        if (this.isSetParentEntityID()) {
            String sourceParentEntityID;
            sourceParentEntityID = this.getParentEntityID();
            String copyParentEntityID = ((String) copyBuilder.copy(sourceParentEntityID));
            copy.setParentEntityID(copyParentEntityID);
        } else {
            copy.parentEntityID = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIRequest();
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
    public static class Dictionaries
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSIRequest.Dictionaries)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSIRequest.Dictionaries that = ((WSIRequest.Dictionaries) object);
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSIRequest.Dictionaries)) {
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
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSIRequest.Dictionaries copy = ((target == null)?((WSIRequest.Dictionaries) createCopy()):((WSIRequest.Dictionaries) target));
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSIRequest.Dictionaries();
        }

    }

}
