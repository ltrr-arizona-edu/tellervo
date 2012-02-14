
package org.tellervo.schema;

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
 *         &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}searchParams" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}authenticate" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}entity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}project" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}element" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}sample" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}radius" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}securityUser" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}securityGroup" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}box" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/tellervo/1.0}permission" maxOccurs="unbounded" minOccurs="0"/>
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
 *       &lt;attribute name="type" type="{http://dendro.cornell.edu/schema/tellervo/1.0}tellervoRequestType" />
 *       &lt;attribute name="format" type="{http://dendro.cornell.edu/schema/tellervo/1.0}tellervoRequestFormat" />
 *       &lt;attribute name="parentEntityID" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="mergeWithID" type="{http://www.w3.org/2001/XMLSchema}token" />
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
    "securityUsers",
    "securityGroups",
    "boxes",
    "permissions",
    "entities",
    "authenticate",
    "searchParams"
})
@XmlRootElement(name = "request")
public class WSIRequest
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSIRequest.Dictionaries dictionaries;
    @XmlElement(name = "project", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasProject> projects;
    @XmlElement(name = "object", namespace = "http://www.tridas.org/1.2.2", type = TridasObjectEx.class)
    protected List<TridasObject> objects;
    @XmlElement(name = "element", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasElement> elements;
    @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasSample> samples;
    @XmlElement(name = "radius", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasRadius> radiuses;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasMeasurementSeries> measurementSeries;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasDerivedSeries> derivedSeries;
    @XmlElement(name = "securityUser")
    protected List<WSISecurityUser> securityUsers;
    @XmlElement(name = "securityGroup")
    protected List<WSISecurityGroup> securityGroups;
    @XmlElement(name = "box")
    protected List<WSIBox> boxes;
    @XmlElement(name = "permission")
    protected List<WSIPermission> permissions;
    @XmlElement(name = "entity")
    protected List<WSIEntity> entities;
    protected WSIAuthenticate authenticate;
    protected WSISearchParams searchParams;
    @XmlAttribute(name = "type")
    protected TellervoRequestType type;
    @XmlAttribute(name = "format")
    protected TellervoRequestFormat format;
    @XmlAttribute(name = "parentEntityID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String parentEntityID;
    @XmlAttribute(name = "mergeWithID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String mergeWithID;

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
     * Gets the value of the securityUsers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityUsers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSISecurityUser }
     * 
     * 
     */
    public List<WSISecurityUser> getSecurityUsers() {
        if (securityUsers == null) {
            securityUsers = new ArrayList<WSISecurityUser>();
        }
        return this.securityUsers;
    }

    public boolean isSetSecurityUsers() {
        return ((this.securityUsers!= null)&&(!this.securityUsers.isEmpty()));
    }

    public void unsetSecurityUsers() {
        this.securityUsers = null;
    }

    /**
     * Gets the value of the securityGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSISecurityGroup }
     * 
     * 
     */
    public List<WSISecurityGroup> getSecurityGroups() {
        if (securityGroups == null) {
            securityGroups = new ArrayList<WSISecurityGroup>();
        }
        return this.securityGroups;
    }

    public boolean isSetSecurityGroups() {
        return ((this.securityGroups!= null)&&(!this.securityGroups.isEmpty()));
    }

    public void unsetSecurityGroups() {
        this.securityGroups = null;
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
     * Gets the value of the permissions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the permissions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPermissions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIPermission }
     * 
     * 
     */
    public List<WSIPermission> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<WSIPermission>();
        }
        return this.permissions;
    }

    public boolean isSetPermissions() {
        return ((this.permissions!= null)&&(!this.permissions.isEmpty()));
    }

    public void unsetPermissions() {
        this.permissions = null;
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
     *     {@link TellervoRequestType }
     *     
     */
    public TellervoRequestType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TellervoRequestType }
     *     
     */
    public void setType(TellervoRequestType value) {
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
     *     {@link TellervoRequestFormat }
     *     
     */
    public TellervoRequestFormat getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link TellervoRequestFormat }
     *     
     */
    public void setFormat(TellervoRequestFormat value) {
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
     * Gets the value of the mergeWithID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMergeWithID() {
        return mergeWithID;
    }

    /**
     * Sets the value of the mergeWithID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMergeWithID(String value) {
        this.mergeWithID = value;
    }

    public boolean isSetMergeWithID() {
        return (this.mergeWithID!= null);
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
            WSIRequest.Dictionaries theDictionaries;
            theDictionaries = this.getDictionaries();
            strategy.appendField(locator, this, "dictionaries", buffer, theDictionaries);
        }
        {
            List<TridasProject> theProjects;
            theProjects = (this.isSetProjects()?this.getProjects():null);
            strategy.appendField(locator, this, "projects", buffer, theProjects);
        }
        {
            List<TridasObject> theObjects;
            theObjects = (this.isSetObjects()?this.getObjects():null);
            strategy.appendField(locator, this, "objects", buffer, theObjects);
        }
        {
            List<TridasElement> theElements;
            theElements = (this.isSetElements()?this.getElements():null);
            strategy.appendField(locator, this, "elements", buffer, theElements);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            strategy.appendField(locator, this, "samples", buffer, theSamples);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            strategy.appendField(locator, this, "radiuses", buffer, theRadiuses);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
            strategy.appendField(locator, this, "measurementSeries", buffer, theMeasurementSeries);
        }
        {
            List<TridasDerivedSeries> theDerivedSeries;
            theDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            strategy.appendField(locator, this, "derivedSeries", buffer, theDerivedSeries);
        }
        {
            List<WSISecurityUser> theSecurityUsers;
            theSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            strategy.appendField(locator, this, "securityUsers", buffer, theSecurityUsers);
        }
        {
            List<WSISecurityGroup> theSecurityGroups;
            theSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            strategy.appendField(locator, this, "securityGroups", buffer, theSecurityGroups);
        }
        {
            List<WSIBox> theBoxes;
            theBoxes = (this.isSetBoxes()?this.getBoxes():null);
            strategy.appendField(locator, this, "boxes", buffer, theBoxes);
        }
        {
            List<WSIPermission> thePermissions;
            thePermissions = (this.isSetPermissions()?this.getPermissions():null);
            strategy.appendField(locator, this, "permissions", buffer, thePermissions);
        }
        {
            List<WSIEntity> theEntities;
            theEntities = (this.isSetEntities()?this.getEntities():null);
            strategy.appendField(locator, this, "entities", buffer, theEntities);
        }
        {
            WSIAuthenticate theAuthenticate;
            theAuthenticate = this.getAuthenticate();
            strategy.appendField(locator, this, "authenticate", buffer, theAuthenticate);
        }
        {
            WSISearchParams theSearchParams;
            theSearchParams = this.getSearchParams();
            strategy.appendField(locator, this, "searchParams", buffer, theSearchParams);
        }
        {
            TellervoRequestType theType;
            theType = this.getType();
            strategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            TellervoRequestFormat theFormat;
            theFormat = this.getFormat();
            strategy.appendField(locator, this, "format", buffer, theFormat);
        }
        {
            String theParentEntityID;
            theParentEntityID = this.getParentEntityID();
            strategy.appendField(locator, this, "parentEntityID", buffer, theParentEntityID);
        }
        {
            String theMergeWithID;
            theMergeWithID = this.getMergeWithID();
            strategy.appendField(locator, this, "mergeWithID", buffer, theMergeWithID);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIRequest)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIRequest that = ((WSIRequest) object);
        {
            WSIRequest.Dictionaries lhsDictionaries;
            lhsDictionaries = this.getDictionaries();
            WSIRequest.Dictionaries rhsDictionaries;
            rhsDictionaries = that.getDictionaries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dictionaries", lhsDictionaries), LocatorUtils.property(thatLocator, "dictionaries", rhsDictionaries), lhsDictionaries, rhsDictionaries)) {
                return false;
            }
        }
        {
            List<TridasProject> lhsProjects;
            lhsProjects = (this.isSetProjects()?this.getProjects():null);
            List<TridasProject> rhsProjects;
            rhsProjects = (that.isSetProjects()?that.getProjects():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "projects", lhsProjects), LocatorUtils.property(thatLocator, "projects", rhsProjects), lhsProjects, rhsProjects)) {
                return false;
            }
        }
        {
            List<TridasObject> lhsObjects;
            lhsObjects = (this.isSetObjects()?this.getObjects():null);
            List<TridasObject> rhsObjects;
            rhsObjects = (that.isSetObjects()?that.getObjects():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "objects", lhsObjects), LocatorUtils.property(thatLocator, "objects", rhsObjects), lhsObjects, rhsObjects)) {
                return false;
            }
        }
        {
            List<TridasElement> lhsElements;
            lhsElements = (this.isSetElements()?this.getElements():null);
            List<TridasElement> rhsElements;
            rhsElements = (that.isSetElements()?that.getElements():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "elements", lhsElements), LocatorUtils.property(thatLocator, "elements", rhsElements), lhsElements, rhsElements)) {
                return false;
            }
        }
        {
            List<TridasSample> lhsSamples;
            lhsSamples = (this.isSetSamples()?this.getSamples():null);
            List<TridasSample> rhsSamples;
            rhsSamples = (that.isSetSamples()?that.getSamples():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "samples", lhsSamples), LocatorUtils.property(thatLocator, "samples", rhsSamples), lhsSamples, rhsSamples)) {
                return false;
            }
        }
        {
            List<TridasRadius> lhsRadiuses;
            lhsRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            List<TridasRadius> rhsRadiuses;
            rhsRadiuses = (that.isSetRadiuses()?that.getRadiuses():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "radiuses", lhsRadiuses), LocatorUtils.property(thatLocator, "radiuses", rhsRadiuses), lhsRadiuses, rhsRadiuses)) {
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
        {
            List<TridasDerivedSeries> lhsDerivedSeries;
            lhsDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            List<TridasDerivedSeries> rhsDerivedSeries;
            rhsDerivedSeries = (that.isSetDerivedSeries()?that.getDerivedSeries():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "derivedSeries", lhsDerivedSeries), LocatorUtils.property(thatLocator, "derivedSeries", rhsDerivedSeries), lhsDerivedSeries, rhsDerivedSeries)) {
                return false;
            }
        }
        {
            List<WSISecurityUser> lhsSecurityUsers;
            lhsSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            List<WSISecurityUser> rhsSecurityUsers;
            rhsSecurityUsers = (that.isSetSecurityUsers()?that.getSecurityUsers():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityUsers", lhsSecurityUsers), LocatorUtils.property(thatLocator, "securityUsers", rhsSecurityUsers), lhsSecurityUsers, rhsSecurityUsers)) {
                return false;
            }
        }
        {
            List<WSISecurityGroup> lhsSecurityGroups;
            lhsSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            List<WSISecurityGroup> rhsSecurityGroups;
            rhsSecurityGroups = (that.isSetSecurityGroups()?that.getSecurityGroups():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityGroups", lhsSecurityGroups), LocatorUtils.property(thatLocator, "securityGroups", rhsSecurityGroups), lhsSecurityGroups, rhsSecurityGroups)) {
                return false;
            }
        }
        {
            List<WSIBox> lhsBoxes;
            lhsBoxes = (this.isSetBoxes()?this.getBoxes():null);
            List<WSIBox> rhsBoxes;
            rhsBoxes = (that.isSetBoxes()?that.getBoxes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "boxes", lhsBoxes), LocatorUtils.property(thatLocator, "boxes", rhsBoxes), lhsBoxes, rhsBoxes)) {
                return false;
            }
        }
        {
            List<WSIPermission> lhsPermissions;
            lhsPermissions = (this.isSetPermissions()?this.getPermissions():null);
            List<WSIPermission> rhsPermissions;
            rhsPermissions = (that.isSetPermissions()?that.getPermissions():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "permissions", lhsPermissions), LocatorUtils.property(thatLocator, "permissions", rhsPermissions), lhsPermissions, rhsPermissions)) {
                return false;
            }
        }
        {
            List<WSIEntity> lhsEntities;
            lhsEntities = (this.isSetEntities()?this.getEntities():null);
            List<WSIEntity> rhsEntities;
            rhsEntities = (that.isSetEntities()?that.getEntities():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "entities", lhsEntities), LocatorUtils.property(thatLocator, "entities", rhsEntities), lhsEntities, rhsEntities)) {
                return false;
            }
        }
        {
            WSIAuthenticate lhsAuthenticate;
            lhsAuthenticate = this.getAuthenticate();
            WSIAuthenticate rhsAuthenticate;
            rhsAuthenticate = that.getAuthenticate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "authenticate", lhsAuthenticate), LocatorUtils.property(thatLocator, "authenticate", rhsAuthenticate), lhsAuthenticate, rhsAuthenticate)) {
                return false;
            }
        }
        {
            WSISearchParams lhsSearchParams;
            lhsSearchParams = this.getSearchParams();
            WSISearchParams rhsSearchParams;
            rhsSearchParams = that.getSearchParams();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "searchParams", lhsSearchParams), LocatorUtils.property(thatLocator, "searchParams", rhsSearchParams), lhsSearchParams, rhsSearchParams)) {
                return false;
            }
        }
        {
            TellervoRequestType lhsType;
            lhsType = this.getType();
            TellervoRequestType rhsType;
            rhsType = that.getType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            TellervoRequestFormat lhsFormat;
            lhsFormat = this.getFormat();
            TellervoRequestFormat rhsFormat;
            rhsFormat = that.getFormat();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "format", lhsFormat), LocatorUtils.property(thatLocator, "format", rhsFormat), lhsFormat, rhsFormat)) {
                return false;
            }
        }
        {
            String lhsParentEntityID;
            lhsParentEntityID = this.getParentEntityID();
            String rhsParentEntityID;
            rhsParentEntityID = that.getParentEntityID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "parentEntityID", lhsParentEntityID), LocatorUtils.property(thatLocator, "parentEntityID", rhsParentEntityID), lhsParentEntityID, rhsParentEntityID)) {
                return false;
            }
        }
        {
            String lhsMergeWithID;
            lhsMergeWithID = this.getMergeWithID();
            String rhsMergeWithID;
            rhsMergeWithID = that.getMergeWithID();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "mergeWithID", lhsMergeWithID), LocatorUtils.property(thatLocator, "mergeWithID", rhsMergeWithID), lhsMergeWithID, rhsMergeWithID)) {
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
            WSIRequest.Dictionaries theDictionaries;
            theDictionaries = this.getDictionaries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dictionaries", theDictionaries), currentHashCode, theDictionaries);
        }
        {
            List<TridasProject> theProjects;
            theProjects = (this.isSetProjects()?this.getProjects():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "projects", theProjects), currentHashCode, theProjects);
        }
        {
            List<TridasObject> theObjects;
            theObjects = (this.isSetObjects()?this.getObjects():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "objects", theObjects), currentHashCode, theObjects);
        }
        {
            List<TridasElement> theElements;
            theElements = (this.isSetElements()?this.getElements():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "elements", theElements), currentHashCode, theElements);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "samples", theSamples), currentHashCode, theSamples);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "radiuses", theRadiuses), currentHashCode, theRadiuses);
        }
        {
            List<TridasMeasurementSeries> theMeasurementSeries;
            theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measurementSeries", theMeasurementSeries), currentHashCode, theMeasurementSeries);
        }
        {
            List<TridasDerivedSeries> theDerivedSeries;
            theDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "derivedSeries", theDerivedSeries), currentHashCode, theDerivedSeries);
        }
        {
            List<WSISecurityUser> theSecurityUsers;
            theSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityUsers", theSecurityUsers), currentHashCode, theSecurityUsers);
        }
        {
            List<WSISecurityGroup> theSecurityGroups;
            theSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityGroups", theSecurityGroups), currentHashCode, theSecurityGroups);
        }
        {
            List<WSIBox> theBoxes;
            theBoxes = (this.isSetBoxes()?this.getBoxes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "boxes", theBoxes), currentHashCode, theBoxes);
        }
        {
            List<WSIPermission> thePermissions;
            thePermissions = (this.isSetPermissions()?this.getPermissions():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "permissions", thePermissions), currentHashCode, thePermissions);
        }
        {
            List<WSIEntity> theEntities;
            theEntities = (this.isSetEntities()?this.getEntities():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "entities", theEntities), currentHashCode, theEntities);
        }
        {
            WSIAuthenticate theAuthenticate;
            theAuthenticate = this.getAuthenticate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "authenticate", theAuthenticate), currentHashCode, theAuthenticate);
        }
        {
            WSISearchParams theSearchParams;
            theSearchParams = this.getSearchParams();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "searchParams", theSearchParams), currentHashCode, theSearchParams);
        }
        {
            TellervoRequestType theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            TellervoRequestFormat theFormat;
            theFormat = this.getFormat();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "format", theFormat), currentHashCode, theFormat);
        }
        {
            String theParentEntityID;
            theParentEntityID = this.getParentEntityID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "parentEntityID", theParentEntityID), currentHashCode, theParentEntityID);
        }
        {
            String theMergeWithID;
            theMergeWithID = this.getMergeWithID();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "mergeWithID", theMergeWithID), currentHashCode, theMergeWithID);
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
        if (draftCopy instanceof WSIRequest) {
            final WSIRequest copy = ((WSIRequest) draftCopy);
            if (this.isSetDictionaries()) {
                WSIRequest.Dictionaries sourceDictionaries;
                sourceDictionaries = this.getDictionaries();
                WSIRequest.Dictionaries copyDictionaries = ((WSIRequest.Dictionaries) strategy.copy(LocatorUtils.property(locator, "dictionaries", sourceDictionaries), sourceDictionaries));
                copy.setDictionaries(copyDictionaries);
            } else {
                copy.dictionaries = null;
            }
            if (this.isSetProjects()) {
                List<TridasProject> sourceProjects;
                sourceProjects = (this.isSetProjects()?this.getProjects():null);
                @SuppressWarnings("unchecked")
                List<TridasProject> copyProjects = ((List<TridasProject> ) strategy.copy(LocatorUtils.property(locator, "projects", sourceProjects), sourceProjects));
                copy.unsetProjects();
                List<TridasProject> uniqueProjectsl = copy.getProjects();
                uniqueProjectsl.addAll(copyProjects);
            } else {
                copy.unsetProjects();
            }
            if (this.isSetObjects()) {
                List<TridasObject> sourceObjects;
                sourceObjects = (this.isSetObjects()?this.getObjects():null);
                @SuppressWarnings("unchecked")
                List<TridasObject> copyObjects = ((List<TridasObject> ) strategy.copy(LocatorUtils.property(locator, "objects", sourceObjects), sourceObjects));
                copy.unsetObjects();
                List<TridasObject> uniqueObjectsl = copy.getObjects();
                uniqueObjectsl.addAll(copyObjects);
            } else {
                copy.unsetObjects();
            }
            if (this.isSetElements()) {
                List<TridasElement> sourceElements;
                sourceElements = (this.isSetElements()?this.getElements():null);
                @SuppressWarnings("unchecked")
                List<TridasElement> copyElements = ((List<TridasElement> ) strategy.copy(LocatorUtils.property(locator, "elements", sourceElements), sourceElements));
                copy.unsetElements();
                List<TridasElement> uniqueElementsl = copy.getElements();
                uniqueElementsl.addAll(copyElements);
            } else {
                copy.unsetElements();
            }
            if (this.isSetSamples()) {
                List<TridasSample> sourceSamples;
                sourceSamples = (this.isSetSamples()?this.getSamples():null);
                @SuppressWarnings("unchecked")
                List<TridasSample> copySamples = ((List<TridasSample> ) strategy.copy(LocatorUtils.property(locator, "samples", sourceSamples), sourceSamples));
                copy.unsetSamples();
                List<TridasSample> uniqueSamplesl = copy.getSamples();
                uniqueSamplesl.addAll(copySamples);
            } else {
                copy.unsetSamples();
            }
            if (this.isSetRadiuses()) {
                List<TridasRadius> sourceRadiuses;
                sourceRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
                @SuppressWarnings("unchecked")
                List<TridasRadius> copyRadiuses = ((List<TridasRadius> ) strategy.copy(LocatorUtils.property(locator, "radiuses", sourceRadiuses), sourceRadiuses));
                copy.unsetRadiuses();
                List<TridasRadius> uniqueRadiusesl = copy.getRadiuses();
                uniqueRadiusesl.addAll(copyRadiuses);
            } else {
                copy.unsetRadiuses();
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
            if (this.isSetDerivedSeries()) {
                List<TridasDerivedSeries> sourceDerivedSeries;
                sourceDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
                @SuppressWarnings("unchecked")
                List<TridasDerivedSeries> copyDerivedSeries = ((List<TridasDerivedSeries> ) strategy.copy(LocatorUtils.property(locator, "derivedSeries", sourceDerivedSeries), sourceDerivedSeries));
                copy.unsetDerivedSeries();
                List<TridasDerivedSeries> uniqueDerivedSeriesl = copy.getDerivedSeries();
                uniqueDerivedSeriesl.addAll(copyDerivedSeries);
            } else {
                copy.unsetDerivedSeries();
            }
            if (this.isSetSecurityUsers()) {
                List<WSISecurityUser> sourceSecurityUsers;
                sourceSecurityUsers = (this.isSetSecurityUsers()?this.getSecurityUsers():null);
                @SuppressWarnings("unchecked")
                List<WSISecurityUser> copySecurityUsers = ((List<WSISecurityUser> ) strategy.copy(LocatorUtils.property(locator, "securityUsers", sourceSecurityUsers), sourceSecurityUsers));
                copy.unsetSecurityUsers();
                List<WSISecurityUser> uniqueSecurityUsersl = copy.getSecurityUsers();
                uniqueSecurityUsersl.addAll(copySecurityUsers);
            } else {
                copy.unsetSecurityUsers();
            }
            if (this.isSetSecurityGroups()) {
                List<WSISecurityGroup> sourceSecurityGroups;
                sourceSecurityGroups = (this.isSetSecurityGroups()?this.getSecurityGroups():null);
                @SuppressWarnings("unchecked")
                List<WSISecurityGroup> copySecurityGroups = ((List<WSISecurityGroup> ) strategy.copy(LocatorUtils.property(locator, "securityGroups", sourceSecurityGroups), sourceSecurityGroups));
                copy.unsetSecurityGroups();
                List<WSISecurityGroup> uniqueSecurityGroupsl = copy.getSecurityGroups();
                uniqueSecurityGroupsl.addAll(copySecurityGroups);
            } else {
                copy.unsetSecurityGroups();
            }
            if (this.isSetBoxes()) {
                List<WSIBox> sourceBoxes;
                sourceBoxes = (this.isSetBoxes()?this.getBoxes():null);
                @SuppressWarnings("unchecked")
                List<WSIBox> copyBoxes = ((List<WSIBox> ) strategy.copy(LocatorUtils.property(locator, "boxes", sourceBoxes), sourceBoxes));
                copy.unsetBoxes();
                List<WSIBox> uniqueBoxesl = copy.getBoxes();
                uniqueBoxesl.addAll(copyBoxes);
            } else {
                copy.unsetBoxes();
            }
            if (this.isSetPermissions()) {
                List<WSIPermission> sourcePermissions;
                sourcePermissions = (this.isSetPermissions()?this.getPermissions():null);
                @SuppressWarnings("unchecked")
                List<WSIPermission> copyPermissions = ((List<WSIPermission> ) strategy.copy(LocatorUtils.property(locator, "permissions", sourcePermissions), sourcePermissions));
                copy.unsetPermissions();
                List<WSIPermission> uniquePermissionsl = copy.getPermissions();
                uniquePermissionsl.addAll(copyPermissions);
            } else {
                copy.unsetPermissions();
            }
            if (this.isSetEntities()) {
                List<WSIEntity> sourceEntities;
                sourceEntities = (this.isSetEntities()?this.getEntities():null);
                @SuppressWarnings("unchecked")
                List<WSIEntity> copyEntities = ((List<WSIEntity> ) strategy.copy(LocatorUtils.property(locator, "entities", sourceEntities), sourceEntities));
                copy.unsetEntities();
                List<WSIEntity> uniqueEntitiesl = copy.getEntities();
                uniqueEntitiesl.addAll(copyEntities);
            } else {
                copy.unsetEntities();
            }
            if (this.isSetAuthenticate()) {
                WSIAuthenticate sourceAuthenticate;
                sourceAuthenticate = this.getAuthenticate();
                WSIAuthenticate copyAuthenticate = ((WSIAuthenticate) strategy.copy(LocatorUtils.property(locator, "authenticate", sourceAuthenticate), sourceAuthenticate));
                copy.setAuthenticate(copyAuthenticate);
            } else {
                copy.authenticate = null;
            }
            if (this.isSetSearchParams()) {
                WSISearchParams sourceSearchParams;
                sourceSearchParams = this.getSearchParams();
                WSISearchParams copySearchParams = ((WSISearchParams) strategy.copy(LocatorUtils.property(locator, "searchParams", sourceSearchParams), sourceSearchParams));
                copy.setSearchParams(copySearchParams);
            } else {
                copy.searchParams = null;
            }
            if (this.isSetType()) {
                TellervoRequestType sourceType;
                sourceType = this.getType();
                TellervoRequestType copyType = ((TellervoRequestType) strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
                copy.setType(copyType);
            } else {
                copy.type = null;
            }
            if (this.isSetFormat()) {
                TellervoRequestFormat sourceFormat;
                sourceFormat = this.getFormat();
                TellervoRequestFormat copyFormat = ((TellervoRequestFormat) strategy.copy(LocatorUtils.property(locator, "format", sourceFormat), sourceFormat));
                copy.setFormat(copyFormat);
            } else {
                copy.format = null;
            }
            if (this.isSetParentEntityID()) {
                String sourceParentEntityID;
                sourceParentEntityID = this.getParentEntityID();
                String copyParentEntityID = ((String) strategy.copy(LocatorUtils.property(locator, "parentEntityID", sourceParentEntityID), sourceParentEntityID));
                copy.setParentEntityID(copyParentEntityID);
            } else {
                copy.parentEntityID = null;
            }
            if (this.isSetMergeWithID()) {
                String sourceMergeWithID;
                sourceMergeWithID = this.getMergeWithID();
                String copyMergeWithID = ((String) strategy.copy(LocatorUtils.property(locator, "mergeWithID", sourceMergeWithID), sourceMergeWithID));
                copy.setMergeWithID(copyMergeWithID);
            } else {
                copy.mergeWithID = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIRequest();
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
     * Sets the value of the securityUsers property.
     * 
     * @param securityUsers
     *     allowed object is
     *     {@link WSISecurityUser }
     *     
     */
    public void setSecurityUsers(List<WSISecurityUser> securityUsers) {
        this.securityUsers = securityUsers;
    }

    /**
     * Sets the value of the securityGroups property.
     * 
     * @param securityGroups
     *     allowed object is
     *     {@link WSISecurityGroup }
     *     
     */
    public void setSecurityGroups(List<WSISecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
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
     * Sets the value of the permissions property.
     * 
     * @param permissions
     *     allowed object is
     *     {@link WSIPermission }
     *     
     */
    public void setPermissions(List<WSIPermission> permissions) {
        this.permissions = permissions;
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
        implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;

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
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSIRequest.Dictionaries)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            return true;
        }

        public boolean equals(Object object) {
            final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
            return equals(null, null, object, strategy);
        }

        public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
            int currentHashCode = 1;
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
            return draftCopy;
        }

        public Object createNewInstance() {
            return new WSIRequest.Dictionaries();
        }

    }

}
