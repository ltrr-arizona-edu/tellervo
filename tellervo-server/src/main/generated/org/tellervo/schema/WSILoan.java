
package org.tellervo.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
import org.tridas.interfaces.ICoreTridas;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasIdentifier;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}identifier" minOccurs="0"/>
 *         &lt;element name="issuedate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="duedate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="returndate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="firstname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sample" maxOccurs="unbounded" minOccurs="0"/>
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
    "identifier",
    "issuedate",
    "duedate",
    "returndate",
    "firstname",
    "lastname",
    "organisation",
    "files",
    "notes",
    "samples"
})
@XmlRootElement(name = "loan")
public class WSILoan implements Cloneable, CopyTo, Equals, HashCode, ToString, ICoreTridas
{

    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected TridasIdentifier identifier;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issuedate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar duedate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar returndate;
    @XmlElement(required = true)
    protected String firstname;
    @XmlElement(required = true)
    protected String lastname;
    @XmlElement(required = true)
    protected String organisation;
    @XmlElement(name = "file", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasFile> files;
    protected String notes;
    @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasSample> samples;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link TridasIdentifier }
     *     
     */
    public TridasIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasIdentifier }
     *     
     */
    public void setIdentifier(TridasIdentifier value) {
        this.identifier = value;
    }

    public boolean isSetIdentifier() {
        return (this.identifier!= null);
    }

    /**
     * Gets the value of the issuedate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssuedate() {
        return issuedate;
    }

    /**
     * Sets the value of the issuedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssuedate(XMLGregorianCalendar value) {
        this.issuedate = value;
    }

    public boolean isSetIssuedate() {
        return (this.issuedate!= null);
    }

    /**
     * Gets the value of the duedate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDuedate() {
        return duedate;
    }

    /**
     * Sets the value of the duedate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDuedate(XMLGregorianCalendar value) {
        this.duedate = value;
    }

    public boolean isSetDuedate() {
        return (this.duedate!= null);
    }

    /**
     * Gets the value of the returndate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReturndate() {
        return returndate;
    }

    /**
     * Sets the value of the returndate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReturndate(XMLGregorianCalendar value) {
        this.returndate = value;
    }

    public boolean isSetReturndate() {
        return (this.returndate!= null);
    }

    /**
     * Gets the value of the firstname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the value of the firstname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstname(String value) {
        this.firstname = value;
    }

    public boolean isSetFirstname() {
        return (this.firstname!= null);
    }

    /**
     * Gets the value of the lastname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the value of the lastname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastname(String value) {
        this.lastname = value;
    }

    public boolean isSetLastname() {
        return (this.lastname!= null);
    }

    /**
     * Gets the value of the organisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the value of the organisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisation(String value) {
        this.organisation = value;
    }

    public boolean isSetOrganisation() {
        return (this.organisation!= null);
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
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    public boolean isSetNotes() {
        return (this.notes!= null);
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
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            strategy.appendField(locator, this, "identifier", buffer, theIdentifier);
        }
        {
            XMLGregorianCalendar theIssuedate;
            theIssuedate = this.getIssuedate();
            strategy.appendField(locator, this, "issuedate", buffer, theIssuedate);
        }
        {
            XMLGregorianCalendar theDuedate;
            theDuedate = this.getDuedate();
            strategy.appendField(locator, this, "duedate", buffer, theDuedate);
        }
        {
            XMLGregorianCalendar theReturndate;
            theReturndate = this.getReturndate();
            strategy.appendField(locator, this, "returndate", buffer, theReturndate);
        }
        {
            String theFirstname;
            theFirstname = this.getFirstname();
            strategy.appendField(locator, this, "firstname", buffer, theFirstname);
        }
        {
            String theLastname;
            theLastname = this.getLastname();
            strategy.appendField(locator, this, "lastname", buffer, theLastname);
        }
        {
            String theOrganisation;
            theOrganisation = this.getOrganisation();
            strategy.appendField(locator, this, "organisation", buffer, theOrganisation);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            strategy.appendField(locator, this, "files", buffer, theFiles);
        }
        {
            String theNotes;
            theNotes = this.getNotes();
            strategy.appendField(locator, this, "notes", buffer, theNotes);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            strategy.appendField(locator, this, "samples", buffer, theSamples);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSILoan)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSILoan that = ((WSILoan) object);
        {
            TridasIdentifier lhsIdentifier;
            lhsIdentifier = this.getIdentifier();
            TridasIdentifier rhsIdentifier;
            rhsIdentifier = that.getIdentifier();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "identifier", lhsIdentifier), LocatorUtils.property(thatLocator, "identifier", rhsIdentifier), lhsIdentifier, rhsIdentifier)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsIssuedate;
            lhsIssuedate = this.getIssuedate();
            XMLGregorianCalendar rhsIssuedate;
            rhsIssuedate = that.getIssuedate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "issuedate", lhsIssuedate), LocatorUtils.property(thatLocator, "issuedate", rhsIssuedate), lhsIssuedate, rhsIssuedate)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsDuedate;
            lhsDuedate = this.getDuedate();
            XMLGregorianCalendar rhsDuedate;
            rhsDuedate = that.getDuedate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "duedate", lhsDuedate), LocatorUtils.property(thatLocator, "duedate", rhsDuedate), lhsDuedate, rhsDuedate)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsReturndate;
            lhsReturndate = this.getReturndate();
            XMLGregorianCalendar rhsReturndate;
            rhsReturndate = that.getReturndate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "returndate", lhsReturndate), LocatorUtils.property(thatLocator, "returndate", rhsReturndate), lhsReturndate, rhsReturndate)) {
                return false;
            }
        }
        {
            String lhsFirstname;
            lhsFirstname = this.getFirstname();
            String rhsFirstname;
            rhsFirstname = that.getFirstname();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "firstname", lhsFirstname), LocatorUtils.property(thatLocator, "firstname", rhsFirstname), lhsFirstname, rhsFirstname)) {
                return false;
            }
        }
        {
            String lhsLastname;
            lhsLastname = this.getLastname();
            String rhsLastname;
            rhsLastname = that.getLastname();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastname", lhsLastname), LocatorUtils.property(thatLocator, "lastname", rhsLastname), lhsLastname, rhsLastname)) {
                return false;
            }
        }
        {
            String lhsOrganisation;
            lhsOrganisation = this.getOrganisation();
            String rhsOrganisation;
            rhsOrganisation = that.getOrganisation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "organisation", lhsOrganisation), LocatorUtils.property(thatLocator, "organisation", rhsOrganisation), lhsOrganisation, rhsOrganisation)) {
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
            String lhsNotes;
            lhsNotes = this.getNotes();
            String rhsNotes;
            rhsNotes = that.getNotes();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "notes", lhsNotes), LocatorUtils.property(thatLocator, "notes", rhsNotes), lhsNotes, rhsNotes)) {
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
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "identifier", theIdentifier), currentHashCode, theIdentifier);
        }
        {
            XMLGregorianCalendar theIssuedate;
            theIssuedate = this.getIssuedate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "issuedate", theIssuedate), currentHashCode, theIssuedate);
        }
        {
            XMLGregorianCalendar theDuedate;
            theDuedate = this.getDuedate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "duedate", theDuedate), currentHashCode, theDuedate);
        }
        {
            XMLGregorianCalendar theReturndate;
            theReturndate = this.getReturndate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "returndate", theReturndate), currentHashCode, theReturndate);
        }
        {
            String theFirstname;
            theFirstname = this.getFirstname();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "firstname", theFirstname), currentHashCode, theFirstname);
        }
        {
            String theLastname;
            theLastname = this.getLastname();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastname", theLastname), currentHashCode, theLastname);
        }
        {
            String theOrganisation;
            theOrganisation = this.getOrganisation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "organisation", theOrganisation), currentHashCode, theOrganisation);
        }
        {
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "files", theFiles), currentHashCode, theFiles);
        }
        {
            String theNotes;
            theNotes = this.getNotes();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "notes", theNotes), currentHashCode, theNotes);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "samples", theSamples), currentHashCode, theSamples);
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
        if (draftCopy instanceof WSILoan) {
            final WSILoan copy = ((WSILoan) draftCopy);
            if (this.isSetIdentifier()) {
                TridasIdentifier sourceIdentifier;
                sourceIdentifier = this.getIdentifier();
                TridasIdentifier copyIdentifier = ((TridasIdentifier) strategy.copy(LocatorUtils.property(locator, "identifier", sourceIdentifier), sourceIdentifier));
                copy.setIdentifier(copyIdentifier);
            } else {
                copy.identifier = null;
            }
            if (this.isSetIssuedate()) {
                XMLGregorianCalendar sourceIssuedate;
                sourceIssuedate = this.getIssuedate();
                XMLGregorianCalendar copyIssuedate = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "issuedate", sourceIssuedate), sourceIssuedate));
                copy.setIssuedate(copyIssuedate);
            } else {
                copy.issuedate = null;
            }
            if (this.isSetDuedate()) {
                XMLGregorianCalendar sourceDuedate;
                sourceDuedate = this.getDuedate();
                XMLGregorianCalendar copyDuedate = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "duedate", sourceDuedate), sourceDuedate));
                copy.setDuedate(copyDuedate);
            } else {
                copy.duedate = null;
            }
            if (this.isSetReturndate()) {
                XMLGregorianCalendar sourceReturndate;
                sourceReturndate = this.getReturndate();
                XMLGregorianCalendar copyReturndate = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "returndate", sourceReturndate), sourceReturndate));
                copy.setReturndate(copyReturndate);
            } else {
                copy.returndate = null;
            }
            if (this.isSetFirstname()) {
                String sourceFirstname;
                sourceFirstname = this.getFirstname();
                String copyFirstname = ((String) strategy.copy(LocatorUtils.property(locator, "firstname", sourceFirstname), sourceFirstname));
                copy.setFirstname(copyFirstname);
            } else {
                copy.firstname = null;
            }
            if (this.isSetLastname()) {
                String sourceLastname;
                sourceLastname = this.getLastname();
                String copyLastname = ((String) strategy.copy(LocatorUtils.property(locator, "lastname", sourceLastname), sourceLastname));
                copy.setLastname(copyLastname);
            } else {
                copy.lastname = null;
            }
            if (this.isSetOrganisation()) {
                String sourceOrganisation;
                sourceOrganisation = this.getOrganisation();
                String copyOrganisation = ((String) strategy.copy(LocatorUtils.property(locator, "organisation", sourceOrganisation), sourceOrganisation));
                copy.setOrganisation(copyOrganisation);
            } else {
                copy.organisation = null;
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
            if (this.isSetNotes()) {
                String sourceNotes;
                sourceNotes = this.getNotes();
                String copyNotes = ((String) strategy.copy(LocatorUtils.property(locator, "notes", sourceNotes), sourceNotes));
                copy.setNotes(copyNotes);
            } else {
                copy.notes = null;
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
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSILoan();
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

}
