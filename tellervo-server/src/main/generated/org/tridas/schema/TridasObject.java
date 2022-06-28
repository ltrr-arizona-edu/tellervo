
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
import org.tridas.util.TridasObjectEx;


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
 *       &lt;extension base="{http://www.tridas.org/1.2.2}tridasEntity">
 *         &lt;sequence>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}type"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}description" minOccurs="0"/>
 *           &lt;element name="linkSeries" type="{http://www.tridas.org/1.2.2}seriesLinksWithPreferred" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}creator" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}owner" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}coverage" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}location" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2.2}element" maxOccurs="unbounded" minOccurs="0"/>
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
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
            ControlledVoc theType;
            theType = this.getType();
            strategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            SeriesLinksWithPreferred theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            strategy.appendField(locator, this, "linkSeries", buffer, theLinkSeries);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            strategy.appendField(locator, this, "files", buffer, theFiles);
        }
        {
            String theCreator;
            theCreator = this.getCreator();
            strategy.appendField(locator, this, "creator", buffer, theCreator);
        }
        {
            String theOwner;
            theOwner = this.getOwner();
            strategy.appendField(locator, this, "owner", buffer, theOwner);
        }
        {
            TridasCoverage theCoverage;
            theCoverage = this.getCoverage();
            strategy.appendField(locator, this, "coverage", buffer, theCoverage);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            strategy.appendField(locator, this, "location", buffer, theLocation);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            strategy.appendField(locator, this, "genericFields", buffer, theGenericFields);
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
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasObject)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasObject that = ((TridasObject) object);
        {
            ControlledVoc lhsType;
            lhsType = this.getType();
            ControlledVoc rhsType;
            rhsType = that.getType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        {
            String lhsDescription;
            lhsDescription = this.getDescription();
            String rhsDescription;
            rhsDescription = that.getDescription();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "description", lhsDescription), LocatorUtils.property(thatLocator, "description", rhsDescription), lhsDescription, rhsDescription)) {
                return false;
            }
        }
        {
            SeriesLinksWithPreferred lhsLinkSeries;
            lhsLinkSeries = this.getLinkSeries();
            SeriesLinksWithPreferred rhsLinkSeries;
            rhsLinkSeries = that.getLinkSeries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "linkSeries", lhsLinkSeries), LocatorUtils.property(thatLocator, "linkSeries", rhsLinkSeries), lhsLinkSeries, rhsLinkSeries)) {
                return false;
            }
        }
        {
            List<TridasFile> lhsFiles;
            lhsFiles = (this.isSetFiles()?this.getFiles():null);
            List<TridasFile> rhsFiles;
            rhsFiles = (that.isSetFiles()?that.getFiles():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "files", lhsFiles), LocatorUtils.property(thatLocator, "files", rhsFiles), lhsFiles, rhsFiles)) {
                return false;
            }
        }
        {
            String lhsCreator;
            lhsCreator = this.getCreator();
            String rhsCreator;
            rhsCreator = that.getCreator();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "creator", lhsCreator), LocatorUtils.property(thatLocator, "creator", rhsCreator), lhsCreator, rhsCreator)) {
                return false;
            }
        }
        {
            String lhsOwner;
            lhsOwner = this.getOwner();
            String rhsOwner;
            rhsOwner = that.getOwner();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "owner", lhsOwner), LocatorUtils.property(thatLocator, "owner", rhsOwner), lhsOwner, rhsOwner)) {
                return false;
            }
        }
        {
            TridasCoverage lhsCoverage;
            lhsCoverage = this.getCoverage();
            TridasCoverage rhsCoverage;
            rhsCoverage = that.getCoverage();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "coverage", lhsCoverage), LocatorUtils.property(thatLocator, "coverage", rhsCoverage), lhsCoverage, rhsCoverage)) {
                return false;
            }
        }
        {
            TridasLocation lhsLocation;
            lhsLocation = this.getLocation();
            TridasLocation rhsLocation;
            rhsLocation = that.getLocation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "location", lhsLocation), LocatorUtils.property(thatLocator, "location", rhsLocation), lhsLocation, rhsLocation)) {
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
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = super.hashCode(locator, strategy);
        {
            ControlledVoc theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            SeriesLinksWithPreferred theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "linkSeries", theLinkSeries), currentHashCode, theLinkSeries);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "files", theFiles), currentHashCode, theFiles);
        }
        {
            String theCreator;
            theCreator = this.getCreator();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "creator", theCreator), currentHashCode, theCreator);
        }
        {
            String theOwner;
            theOwner = this.getOwner();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "owner", theOwner), currentHashCode, theOwner);
        }
        {
            TridasCoverage theCoverage;
            theCoverage = this.getCoverage();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "coverage", theCoverage), currentHashCode, theCoverage);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "location", theLocation), currentHashCode, theLocation);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "genericFields", theGenericFields), currentHashCode, theGenericFields);
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
        if (draftCopy instanceof TridasObject) {
            final TridasObject copy = ((TridasObject) draftCopy);
            if (this.isSetType()) {
                ControlledVoc sourceType;
                sourceType = this.getType();
                ControlledVoc copyType = ((ControlledVoc) strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
                copy.setType(copyType);
            } else {
                copy.type = null;
            }
            if (this.isSetDescription()) {
                String sourceDescription;
                sourceDescription = this.getDescription();
                String copyDescription = ((String) strategy.copy(LocatorUtils.property(locator, "description", sourceDescription), sourceDescription));
                copy.setDescription(copyDescription);
            } else {
                copy.description = null;
            }
            if (this.isSetLinkSeries()) {
                SeriesLinksWithPreferred sourceLinkSeries;
                sourceLinkSeries = this.getLinkSeries();
                SeriesLinksWithPreferred copyLinkSeries = ((SeriesLinksWithPreferred) strategy.copy(LocatorUtils.property(locator, "linkSeries", sourceLinkSeries), sourceLinkSeries));
                copy.setLinkSeries(copyLinkSeries);
            } else {
                copy.linkSeries = null;
            }
            if (this.isSetFiles()) {
                List<TridasFile> sourceFiles;
                sourceFiles = (this.isSetFiles()?this.getFiles():null);
                @SuppressWarnings("unchecked")
                List<TridasFile> copyFiles = ((List<TridasFile> ) strategy.copy(LocatorUtils.property(locator, "files", sourceFiles), sourceFiles));
                copy.unsetFiles();
                List<TridasFile> uniqueFilesl = copy.getFiles();
                uniqueFilesl.addAll(copyFiles);
            } else {
                copy.unsetFiles();
            }
            if (this.isSetCreator()) {
                String sourceCreator;
                sourceCreator = this.getCreator();
                String copyCreator = ((String) strategy.copy(LocatorUtils.property(locator, "creator", sourceCreator), sourceCreator));
                copy.setCreator(copyCreator);
            } else {
                copy.creator = null;
            }
            if (this.isSetOwner()) {
                String sourceOwner;
                sourceOwner = this.getOwner();
                String copyOwner = ((String) strategy.copy(LocatorUtils.property(locator, "owner", sourceOwner), sourceOwner));
                copy.setOwner(copyOwner);
            } else {
                copy.owner = null;
            }
            if (this.isSetCoverage()) {
                TridasCoverage sourceCoverage;
                sourceCoverage = this.getCoverage();
                TridasCoverage copyCoverage = ((TridasCoverage) strategy.copy(LocatorUtils.property(locator, "coverage", sourceCoverage), sourceCoverage));
                copy.setCoverage(copyCoverage);
            } else {
                copy.coverage = null;
            }
            if (this.isSetLocation()) {
                TridasLocation sourceLocation;
                sourceLocation = this.getLocation();
                TridasLocation copyLocation = ((TridasLocation) strategy.copy(LocatorUtils.property(locator, "location", sourceLocation), sourceLocation));
                copy.setLocation(copyLocation);
            } else {
                copy.location = null;
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
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasObject();
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

}
