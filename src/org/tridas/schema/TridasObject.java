
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
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
import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionaryType;


/**
 * 
 * An object is the item to be investigated.  Examples include: violin; excavation site; 
 * painting on a wooden panel; water well; church; carving; ship; forest. An object could 
 * also be more specific, for example: mast of a ship; roof of a church. Depending on the 
 * object type various descriptions are made possible. An object can have one or more 
 * elements and can also refer to another (sub) object.  For instance a single file 
 * may contain three objects: an archaeological site object, within which there is a 
 * building object, within which there is a beam object.  The list of possible object 
 * types is extensible and is thus flexible enough to incorporate the diversity of data 
 * required by the dendro community.  Only information that is essential for 
 * dendrochronological research is recorded here. Other related data may be provided in the 
 * form of a link to an external database such as a museum catalogue. 
 * 			
 * 
 * <p>Java class for object element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="object">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{http://www.tridas.org/1.3}tridasEntity">
 *         &lt;sequence>
 *           &lt;element ref="{http://www.tridas.org/1.3}type"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}description" minOccurs="0"/>
 *           &lt;element name="linkSeries" type="{http://www.tridas.org/1.3}seriesLinksWithPreferred" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}file" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}creator" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}owner" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}coverage" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}location" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}object" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.3}element" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/extension>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "type",
    "description",
    "linkSeries",
    "files",
    "creator",
    "owner",
    "coverage",
    "location",
    "genericFields",
    "objects",
    "elements"
})
@XmlRootElement(name = "object")
public class TridasObject
    extends TridasEntity
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    @TridasCustomDictionary(dictionary = "objectType", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected ControlledVoc type;
    protected String description;
    protected SeriesLinksWithPreferred linkSeries;
    @XmlElement(name = "file")
    protected List<TridasFile> files;
    protected String creator;
    protected String owner;
    protected TridasCoverage coverage;
    protected TridasLocation location;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    @XmlElement(name = "object", type = TridasObjectEx.class)
    protected List<TridasObject> objects;
    @XmlElement(name = "element")
    protected List<TridasElement> elements;

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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description!= null);
    }

    /**
     * Gets the value of the linkSeries property.
     * 
     * @return
     *     possible object is
     *     {@link SeriesLinksWithPreferred }
     *     
     */
    public SeriesLinksWithPreferred getLinkSeries() {
        return linkSeries;
    }

    /**
     * Sets the value of the linkSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLinksWithPreferred }
     *     
     */
    public void setLinkSeries(SeriesLinksWithPreferred value) {
        this.linkSeries = value;
    }

    public boolean isSetLinkSeries() {
        return (this.linkSeries!= null);
    }

    /**
     * Gets the value of the files property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the files property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasFile }
     * 
     * 
     */
    public List<TridasFile> getFiles() {
        if (files == null) {
            files = new ArrayList<TridasFile>();
        }
        return this.files;
    }

    public boolean isSetFiles() {
        return ((this.files!= null)&&(!this.files.isEmpty()));
    }

    public void unsetFiles() {
        this.files = null;
    }

    /**
     * Gets the value of the creator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreator(String value) {
        this.creator = value;
    }

    public boolean isSetCreator() {
        return (this.creator!= null);
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    public boolean isSetOwner() {
        return (this.owner!= null);
    }

    /**
     * Gets the value of the coverage property.
     * 
     * @return
     *     possible object is
     *     {@link TridasCoverage }
     *     
     */
    public TridasCoverage getCoverage() {
        return coverage;
    }

    /**
     * Sets the value of the coverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasCoverage }
     *     
     */
    public void setCoverage(TridasCoverage value) {
        this.coverage = value;
    }

    public boolean isSetCoverage() {
        return (this.coverage!= null);
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLocation }
     *     
     */
    public TridasLocation getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLocation }
     *     
     */
    public void setLocation(TridasLocation value) {
        this.location = value;
    }

    public boolean isSetLocation() {
        return (this.location!= null);
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
     * Sets the value of the files property.
     * 
     * @param files
     *     allowed object is
     *     {@link TridasFile }
     *     
     */
    public void setFiles(List<TridasFile> files) {
        this.files = files;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasObject)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasObject that = ((TridasObject) object);
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getDescription(), that.getDescription());
        equalsBuilder.append(this.getLinkSeries(), that.getLinkSeries());
        equalsBuilder.append(this.getFiles(), that.getFiles());
        equalsBuilder.append(this.getCreator(), that.getCreator());
        equalsBuilder.append(this.getOwner(), that.getOwner());
        equalsBuilder.append(this.getCoverage(), that.getCoverage());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getObjects(), that.getObjects());
        equalsBuilder.append(this.getElements(), that.getElements());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasObject)) {
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
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getDescription());
        hashCodeBuilder.append(this.getLinkSeries());
        hashCodeBuilder.append(this.getFiles());
        hashCodeBuilder.append(this.getCreator());
        hashCodeBuilder.append(this.getOwner());
        hashCodeBuilder.append(this.getCoverage());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getGenericFields());
        hashCodeBuilder.append(this.getObjects());
        hashCodeBuilder.append(this.getElements());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        super.toString(toStringBuilder);
        {
            ControlledVoc theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            toStringBuilder.append("description", theDescription);
        }
        {
            SeriesLinksWithPreferred theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            toStringBuilder.append("linkSeries", theLinkSeries);
        }
        {
            List<TridasFile> theFiles;
            theFiles = this.getFiles();
            toStringBuilder.append("files", theFiles);
        }
        {
            String theCreator;
            theCreator = this.getCreator();
            toStringBuilder.append("creator", theCreator);
        }
        {
            String theOwner;
            theOwner = this.getOwner();
            toStringBuilder.append("owner", theOwner);
        }
        {
            TridasCoverage theCoverage;
            theCoverage = this.getCoverage();
            toStringBuilder.append("coverage", theCoverage);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            toStringBuilder.append("location", theLocation);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = this.getGenericFields();
            toStringBuilder.append("genericFields", theGenericFields);
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
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasObject copy = ((target == null)?((TridasObject) createCopy()):((TridasObject) target));
        super.copyTo(copy, copyBuilder);
        {
            ControlledVoc sourceType;
            sourceType = this.getType();
            ControlledVoc copyType = ((ControlledVoc) copyBuilder.copy(sourceType));
            copy.setType(copyType);
        }
        {
            String sourceDescription;
            sourceDescription = this.getDescription();
            String copyDescription = ((String) copyBuilder.copy(sourceDescription));
            copy.setDescription(copyDescription);
        }
        {
            SeriesLinksWithPreferred sourceLinkSeries;
            sourceLinkSeries = this.getLinkSeries();
            SeriesLinksWithPreferred copyLinkSeries = ((SeriesLinksWithPreferred) copyBuilder.copy(sourceLinkSeries));
            copy.setLinkSeries(copyLinkSeries);
        }
        {
            List<TridasFile> sourceFiles;
            sourceFiles = this.getFiles();
            List<TridasFile> copyFiles = ((List<TridasFile> ) copyBuilder.copy(sourceFiles));
            copy.setFiles(copyFiles);
        }
        {
            String sourceCreator;
            sourceCreator = this.getCreator();
            String copyCreator = ((String) copyBuilder.copy(sourceCreator));
            copy.setCreator(copyCreator);
        }
        {
            String sourceOwner;
            sourceOwner = this.getOwner();
            String copyOwner = ((String) copyBuilder.copy(sourceOwner));
            copy.setOwner(copyOwner);
        }
        {
            TridasCoverage sourceCoverage;
            sourceCoverage = this.getCoverage();
            TridasCoverage copyCoverage = ((TridasCoverage) copyBuilder.copy(sourceCoverage));
            copy.setCoverage(copyCoverage);
        }
        {
            TridasLocation sourceLocation;
            sourceLocation = this.getLocation();
            TridasLocation copyLocation = ((TridasLocation) copyBuilder.copy(sourceLocation));
            copy.setLocation(copyLocation);
        }
        {
            List<TridasGenericField> sourceGenericFields;
            sourceGenericFields = this.getGenericFields();
            List<TridasGenericField> copyGenericFields = ((List<TridasGenericField> ) copyBuilder.copy(sourceGenericFields));
            copy.setGenericFields(copyGenericFields);
        }
        {
            List<TridasObject> sourceObjects;
            sourceObjects = this.getObjects();
            List<TridasObject> copyObjects = ((List<TridasObject> ) copyBuilder.copy(sourceObjects));
            copy.setObjects(copyObjects);
        }
        {
            List<TridasElement> sourceElements;
            sourceElements = this.getElements();
            List<TridasElement> copyElements = ((List<TridasElement> ) copyBuilder.copy(sourceElements));
            copy.setElements(copyElements);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasObject();
    }

}
