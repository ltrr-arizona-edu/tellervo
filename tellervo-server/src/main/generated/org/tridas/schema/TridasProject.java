
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.2.2}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}type" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}laboratory" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}category"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}investigator"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}period"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}requestDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}commissioner" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}research" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
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
    "types",
    "description",
    "files",
    "laboratories",
    "category",
    "investigator",
    "period",
    "requestDate",
    "commissioner",
    "references",
    "researches",
    "genericFields",
    "objects",
    "derivedSeries"
})
@XmlRootElement(name = "project")
public class TridasProject
    extends TridasEntity
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "type", required = true)
    protected List<ControlledVoc> types;
    protected String description;
    @XmlElement(name = "file")
    protected List<TridasFile> files;
    @XmlElement(name = "laboratory", required = true)
    protected List<TridasLaboratory> laboratories;
    @XmlElement(required = true)
    protected ControlledVoc category;
    @XmlElement(required = true)
    protected String investigator;
    @XmlElement(required = true)
    protected String period;
    protected Date requestDate;
    protected String commissioner;
    @XmlElement(name = "reference")
    protected List<String> references;
    @XmlElement(name = "research")
    protected List<TridasResearch> researches;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    @XmlElement(name = "object", type = TridasObjectEx.class)
    protected List<TridasObject> objects;
    protected List<TridasDerivedSeries> derivedSeries;

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
     * Gets the value of the laboratories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the laboratories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLaboratories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasLaboratory }
     * 
     * 
     */
    public List<TridasLaboratory> getLaboratories() {
        if (laboratories == null) {
            laboratories = new ArrayList<TridasLaboratory>();
        }
        return this.laboratories;
    }

    public boolean isSetLaboratories() {
        return ((this.laboratories!= null)&&(!this.laboratories.isEmpty()));
    }

    public void unsetLaboratories() {
        this.laboratories = null;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledVoc }
     *     
     */
    public ControlledVoc getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setCategory(ControlledVoc value) {
        this.category = value;
    }

    public boolean isSetCategory() {
        return (this.category!= null);
    }

    /**
     * Gets the value of the investigator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigator() {
        return investigator;
    }

    /**
     * Sets the value of the investigator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigator(String value) {
        this.investigator = value;
    }

    public boolean isSetInvestigator() {
        return (this.investigator!= null);
    }

    /**
     * Gets the value of the period property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriod() {
        return period;
    }

    /**
     * Sets the value of the period property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriod(String value) {
        this.period = value;
    }

    public boolean isSetPeriod() {
        return (this.period!= null);
    }

    /**
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setRequestDate(Date value) {
        this.requestDate = value;
    }

    public boolean isSetRequestDate() {
        return (this.requestDate!= null);
    }

    /**
     * Gets the value of the commissioner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommissioner() {
        return commissioner;
    }

    /**
     * Sets the value of the commissioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommissioner(String value) {
        this.commissioner = value;
    }

    public boolean isSetCommissioner() {
        return (this.commissioner!= null);
    }

    /**
     * Gets the value of the references property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the references property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReferences() {
        if (references == null) {
            references = new ArrayList<String>();
        }
        return this.references;
    }

    public boolean isSetReferences() {
        return ((this.references!= null)&&(!this.references.isEmpty()));
    }

    public void unsetReferences() {
        this.references = null;
    }

    /**
     * Gets the value of the researches property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the researches property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResearches().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasResearch }
     * 
     * 
     */
    public List<TridasResearch> getResearches() {
        if (researches == null) {
            researches = new ArrayList<TridasResearch>();
        }
        return this.researches;
    }

    public boolean isSetResearches() {
        return ((this.researches!= null)&&(!this.researches.isEmpty()));
    }

    public void unsetResearches() {
        this.researches = null;
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
            List<ControlledVoc> theTypes;
            theTypes = (this.isSetTypes()?this.getTypes():null);
            strategy.appendField(locator, this, "types", buffer, theTypes);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            strategy.appendField(locator, this, "description", buffer, theDescription);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            strategy.appendField(locator, this, "files", buffer, theFiles);
        }
        {
            List<TridasLaboratory> theLaboratories;
            theLaboratories = (this.isSetLaboratories()?this.getLaboratories():null);
            strategy.appendField(locator, this, "laboratories", buffer, theLaboratories);
        }
        {
            ControlledVoc theCategory;
            theCategory = this.getCategory();
            strategy.appendField(locator, this, "category", buffer, theCategory);
        }
        {
            String theInvestigator;
            theInvestigator = this.getInvestigator();
            strategy.appendField(locator, this, "investigator", buffer, theInvestigator);
        }
        {
            String thePeriod;
            thePeriod = this.getPeriod();
            strategy.appendField(locator, this, "period", buffer, thePeriod);
        }
        {
            Date theRequestDate;
            theRequestDate = this.getRequestDate();
            strategy.appendField(locator, this, "requestDate", buffer, theRequestDate);
        }
        {
            String theCommissioner;
            theCommissioner = this.getCommissioner();
            strategy.appendField(locator, this, "commissioner", buffer, theCommissioner);
        }
        {
            List<String> theReferences;
            theReferences = (this.isSetReferences()?this.getReferences():null);
            strategy.appendField(locator, this, "references", buffer, theReferences);
        }
        {
            List<TridasResearch> theResearches;
            theResearches = (this.isSetResearches()?this.getResearches():null);
            strategy.appendField(locator, this, "researches", buffer, theResearches);
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
            List<TridasDerivedSeries> theDerivedSeries;
            theDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            strategy.appendField(locator, this, "derivedSeries", buffer, theDerivedSeries);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasProject)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasProject that = ((TridasProject) object);
        {
            List<ControlledVoc> lhsTypes;
            lhsTypes = (this.isSetTypes()?this.getTypes():null);
            List<ControlledVoc> rhsTypes;
            rhsTypes = (that.isSetTypes()?that.getTypes():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "types", lhsTypes), LocatorUtils.property(thatLocator, "types", rhsTypes), lhsTypes, rhsTypes)) {
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
            List<TridasFile> lhsFiles;
            lhsFiles = (this.isSetFiles()?this.getFiles():null);
            List<TridasFile> rhsFiles;
            rhsFiles = (that.isSetFiles()?that.getFiles():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "files", lhsFiles), LocatorUtils.property(thatLocator, "files", rhsFiles), lhsFiles, rhsFiles)) {
                return false;
            }
        }
        {
            List<TridasLaboratory> lhsLaboratories;
            lhsLaboratories = (this.isSetLaboratories()?this.getLaboratories():null);
            List<TridasLaboratory> rhsLaboratories;
            rhsLaboratories = (that.isSetLaboratories()?that.getLaboratories():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "laboratories", lhsLaboratories), LocatorUtils.property(thatLocator, "laboratories", rhsLaboratories), lhsLaboratories, rhsLaboratories)) {
                return false;
            }
        }
        {
            ControlledVoc lhsCategory;
            lhsCategory = this.getCategory();
            ControlledVoc rhsCategory;
            rhsCategory = that.getCategory();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "category", lhsCategory), LocatorUtils.property(thatLocator, "category", rhsCategory), lhsCategory, rhsCategory)) {
                return false;
            }
        }
        {
            String lhsInvestigator;
            lhsInvestigator = this.getInvestigator();
            String rhsInvestigator;
            rhsInvestigator = that.getInvestigator();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "investigator", lhsInvestigator), LocatorUtils.property(thatLocator, "investigator", rhsInvestigator), lhsInvestigator, rhsInvestigator)) {
                return false;
            }
        }
        {
            String lhsPeriod;
            lhsPeriod = this.getPeriod();
            String rhsPeriod;
            rhsPeriod = that.getPeriod();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "period", lhsPeriod), LocatorUtils.property(thatLocator, "period", rhsPeriod), lhsPeriod, rhsPeriod)) {
                return false;
            }
        }
        {
            Date lhsRequestDate;
            lhsRequestDate = this.getRequestDate();
            Date rhsRequestDate;
            rhsRequestDate = that.getRequestDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestDate", lhsRequestDate), LocatorUtils.property(thatLocator, "requestDate", rhsRequestDate), lhsRequestDate, rhsRequestDate)) {
                return false;
            }
        }
        {
            String lhsCommissioner;
            lhsCommissioner = this.getCommissioner();
            String rhsCommissioner;
            rhsCommissioner = that.getCommissioner();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "commissioner", lhsCommissioner), LocatorUtils.property(thatLocator, "commissioner", rhsCommissioner), lhsCommissioner, rhsCommissioner)) {
                return false;
            }
        }
        {
            List<String> lhsReferences;
            lhsReferences = (this.isSetReferences()?this.getReferences():null);
            List<String> rhsReferences;
            rhsReferences = (that.isSetReferences()?that.getReferences():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "references", lhsReferences), LocatorUtils.property(thatLocator, "references", rhsReferences), lhsReferences, rhsReferences)) {
                return false;
            }
        }
        {
            List<TridasResearch> lhsResearches;
            lhsResearches = (this.isSetResearches()?this.getResearches():null);
            List<TridasResearch> rhsResearches;
            rhsResearches = (that.isSetResearches()?that.getResearches():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "researches", lhsResearches), LocatorUtils.property(thatLocator, "researches", rhsResearches), lhsResearches, rhsResearches)) {
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
            List<TridasDerivedSeries> lhsDerivedSeries;
            lhsDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            List<TridasDerivedSeries> rhsDerivedSeries;
            rhsDerivedSeries = (that.isSetDerivedSeries()?that.getDerivedSeries():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "derivedSeries", lhsDerivedSeries), LocatorUtils.property(thatLocator, "derivedSeries", rhsDerivedSeries), lhsDerivedSeries, rhsDerivedSeries)) {
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
            List<ControlledVoc> theTypes;
            theTypes = (this.isSetTypes()?this.getTypes():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "types", theTypes), currentHashCode, theTypes);
        }
        {
            String theDescription;
            theDescription = this.getDescription();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "description", theDescription), currentHashCode, theDescription);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "files", theFiles), currentHashCode, theFiles);
        }
        {
            List<TridasLaboratory> theLaboratories;
            theLaboratories = (this.isSetLaboratories()?this.getLaboratories():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "laboratories", theLaboratories), currentHashCode, theLaboratories);
        }
        {
            ControlledVoc theCategory;
            theCategory = this.getCategory();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "category", theCategory), currentHashCode, theCategory);
        }
        {
            String theInvestigator;
            theInvestigator = this.getInvestigator();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "investigator", theInvestigator), currentHashCode, theInvestigator);
        }
        {
            String thePeriod;
            thePeriod = this.getPeriod();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "period", thePeriod), currentHashCode, thePeriod);
        }
        {
            Date theRequestDate;
            theRequestDate = this.getRequestDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestDate", theRequestDate), currentHashCode, theRequestDate);
        }
        {
            String theCommissioner;
            theCommissioner = this.getCommissioner();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "commissioner", theCommissioner), currentHashCode, theCommissioner);
        }
        {
            List<String> theReferences;
            theReferences = (this.isSetReferences()?this.getReferences():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "references", theReferences), currentHashCode, theReferences);
        }
        {
            List<TridasResearch> theResearches;
            theResearches = (this.isSetResearches()?this.getResearches():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "researches", theResearches), currentHashCode, theResearches);
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
            List<TridasDerivedSeries> theDerivedSeries;
            theDerivedSeries = (this.isSetDerivedSeries()?this.getDerivedSeries():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "derivedSeries", theDerivedSeries), currentHashCode, theDerivedSeries);
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
        if (draftCopy instanceof TridasProject) {
            final TridasProject copy = ((TridasProject) draftCopy);
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
            if (this.isSetDescription()) {
                String sourceDescription;
                sourceDescription = this.getDescription();
                String copyDescription = ((String) strategy.copy(LocatorUtils.property(locator, "description", sourceDescription), sourceDescription));
                copy.setDescription(copyDescription);
            } else {
                copy.description = null;
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
            if (this.isSetLaboratories()) {
                List<TridasLaboratory> sourceLaboratories;
                sourceLaboratories = (this.isSetLaboratories()?this.getLaboratories():null);
                @SuppressWarnings("unchecked")
                List<TridasLaboratory> copyLaboratories = ((List<TridasLaboratory> ) strategy.copy(LocatorUtils.property(locator, "laboratories", sourceLaboratories), sourceLaboratories));
                copy.unsetLaboratories();
                List<TridasLaboratory> uniqueLaboratoriesl = copy.getLaboratories();
                uniqueLaboratoriesl.addAll(copyLaboratories);
            } else {
                copy.unsetLaboratories();
            }
            if (this.isSetCategory()) {
                ControlledVoc sourceCategory;
                sourceCategory = this.getCategory();
                ControlledVoc copyCategory = ((ControlledVoc) strategy.copy(LocatorUtils.property(locator, "category", sourceCategory), sourceCategory));
                copy.setCategory(copyCategory);
            } else {
                copy.category = null;
            }
            if (this.isSetInvestigator()) {
                String sourceInvestigator;
                sourceInvestigator = this.getInvestigator();
                String copyInvestigator = ((String) strategy.copy(LocatorUtils.property(locator, "investigator", sourceInvestigator), sourceInvestigator));
                copy.setInvestigator(copyInvestigator);
            } else {
                copy.investigator = null;
            }
            if (this.isSetPeriod()) {
                String sourcePeriod;
                sourcePeriod = this.getPeriod();
                String copyPeriod = ((String) strategy.copy(LocatorUtils.property(locator, "period", sourcePeriod), sourcePeriod));
                copy.setPeriod(copyPeriod);
            } else {
                copy.period = null;
            }
            if (this.isSetRequestDate()) {
                Date sourceRequestDate;
                sourceRequestDate = this.getRequestDate();
                Date copyRequestDate = ((Date) strategy.copy(LocatorUtils.property(locator, "requestDate", sourceRequestDate), sourceRequestDate));
                copy.setRequestDate(copyRequestDate);
            } else {
                copy.requestDate = null;
            }
            if (this.isSetCommissioner()) {
                String sourceCommissioner;
                sourceCommissioner = this.getCommissioner();
                String copyCommissioner = ((String) strategy.copy(LocatorUtils.property(locator, "commissioner", sourceCommissioner), sourceCommissioner));
                copy.setCommissioner(copyCommissioner);
            } else {
                copy.commissioner = null;
            }
            if (this.isSetReferences()) {
                List<String> sourceReferences;
                sourceReferences = (this.isSetReferences()?this.getReferences():null);
                @SuppressWarnings("unchecked")
                List<String> copyReferences = ((List<String> ) strategy.copy(LocatorUtils.property(locator, "references", sourceReferences), sourceReferences));
                copy.unsetReferences();
                List<String> uniqueReferencesl = copy.getReferences();
                uniqueReferencesl.addAll(copyReferences);
            } else {
                copy.unsetReferences();
            }
            if (this.isSetResearches()) {
                List<TridasResearch> sourceResearches;
                sourceResearches = (this.isSetResearches()?this.getResearches():null);
                @SuppressWarnings("unchecked")
                List<TridasResearch> copyResearches = ((List<TridasResearch> ) strategy.copy(LocatorUtils.property(locator, "researches", sourceResearches), sourceResearches));
                copy.unsetResearches();
                List<TridasResearch> uniqueResearchesl = copy.getResearches();
                uniqueResearchesl.addAll(copyResearches);
            } else {
                copy.unsetResearches();
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
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasProject();
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
     * Sets the value of the laboratories property.
     * 
     * @param laboratories
     *     allowed object is
     *     {@link TridasLaboratory }
     *     
     */
    public void setLaboratories(List<TridasLaboratory> laboratories) {
        this.laboratories = laboratories;
    }

    /**
     * Sets the value of the references property.
     * 
     * @param references
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferences(List<String> references) {
        this.references = references;
    }

    /**
     * Sets the value of the researches property.
     * 
     * @param researches
     *     allowed object is
     *     {@link TridasResearch }
     *     
     */
    public void setResearches(List<TridasResearch> researches) {
        this.researches = researches;
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

}
