
package org.tellervo.schema;

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
 *         &lt;element name="status" type="{http://www.tellervo.org/schema/1.0}curationStatus"/>
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}securityUser" minOccurs="0"/>
 *         &lt;element name="curationtimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sample"/>
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}loan" minOccurs="0"/>
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
    "status",
    "securityUser",
    "curationtimestamp",
    "notes",
    "sample",
    "loan"
})
@XmlRootElement(name = "curation")
public class WSICuration implements Cloneable, CopyTo, Equals, HashCode, ToString, ICoreTridas
{

    @XmlElement(namespace = "http://www.tridas.org/1.2.2")
    protected TridasIdentifier identifier;
    @XmlElement(required = true)
    protected CurationStatus status;
    protected WSISecurityUser securityUser;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar curationtimestamp;
    protected String notes;
    @XmlElement(namespace = "http://www.tridas.org/1.2.2", required = true)
    protected TridasSample sample;
    protected WSILoan loan;

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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link CurationStatus }
     *     
     */
    public CurationStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link CurationStatus }
     *     
     */
    public void setStatus(CurationStatus value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the securityUser property.
     * 
     * @return
     *     possible object is
     *     {@link WSISecurityUser }
     *     
     */
    public WSISecurityUser getSecurityUser() {
        return securityUser;
    }

    /**
     * Sets the value of the securityUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSISecurityUser }
     *     
     */
    public void setSecurityUser(WSISecurityUser value) {
        this.securityUser = value;
    }

    public boolean isSetSecurityUser() {
        return (this.securityUser!= null);
    }

    /**
     * Gets the value of the curationtimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCurationtimestamp() {
        return curationtimestamp;
    }

    /**
     * Sets the value of the curationtimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCurationtimestamp(XMLGregorianCalendar value) {
        this.curationtimestamp = value;
    }

    public boolean isSetCurationtimestamp() {
        return (this.curationtimestamp!= null);
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
     * Gets the value of the sample property.
     * 
     * @return
     *     possible object is
     *     {@link TridasSample }
     *     
     */
    public TridasSample getSample() {
        return sample;
    }

    /**
     * Sets the value of the sample property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasSample }
     *     
     */
    public void setSample(TridasSample value) {
        this.sample = value;
    }

    public boolean isSetSample() {
        return (this.sample!= null);
    }

    /**
     * Gets the value of the loan property.
     * 
     * @return
     *     possible object is
     *     {@link WSILoan }
     *     
     */
    public WSILoan getLoan() {
        return loan;
    }

    /**
     * Sets the value of the loan property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSILoan }
     *     
     */
    public void setLoan(WSILoan value) {
        this.loan = value;
    }

    public boolean isSetLoan() {
        return (this.loan!= null);
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
            CurationStatus theStatus;
            theStatus = this.getStatus();
            strategy.appendField(locator, this, "status", buffer, theStatus);
        }
        {
            WSISecurityUser theSecurityUser;
            theSecurityUser = this.getSecurityUser();
            strategy.appendField(locator, this, "securityUser", buffer, theSecurityUser);
        }
        {
            XMLGregorianCalendar theCurationtimestamp;
            theCurationtimestamp = this.getCurationtimestamp();
            strategy.appendField(locator, this, "curationtimestamp", buffer, theCurationtimestamp);
        }
        {
            String theNotes;
            theNotes = this.getNotes();
            strategy.appendField(locator, this, "notes", buffer, theNotes);
        }
        {
            TridasSample theSample;
            theSample = this.getSample();
            strategy.appendField(locator, this, "sample", buffer, theSample);
        }
        {
            WSILoan theLoan;
            theLoan = this.getLoan();
            strategy.appendField(locator, this, "loan", buffer, theLoan);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSICuration)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSICuration that = ((WSICuration) object);
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
            CurationStatus lhsStatus;
            lhsStatus = this.getStatus();
            CurationStatus rhsStatus;
            rhsStatus = that.getStatus();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "status", lhsStatus), LocatorUtils.property(thatLocator, "status", rhsStatus), lhsStatus, rhsStatus)) {
                return false;
            }
        }
        {
            WSISecurityUser lhsSecurityUser;
            lhsSecurityUser = this.getSecurityUser();
            WSISecurityUser rhsSecurityUser;
            rhsSecurityUser = that.getSecurityUser();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityUser", lhsSecurityUser), LocatorUtils.property(thatLocator, "securityUser", rhsSecurityUser), lhsSecurityUser, rhsSecurityUser)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsCurationtimestamp;
            lhsCurationtimestamp = this.getCurationtimestamp();
            XMLGregorianCalendar rhsCurationtimestamp;
            rhsCurationtimestamp = that.getCurationtimestamp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "curationtimestamp", lhsCurationtimestamp), LocatorUtils.property(thatLocator, "curationtimestamp", rhsCurationtimestamp), lhsCurationtimestamp, rhsCurationtimestamp)) {
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
            TridasSample lhsSample;
            lhsSample = this.getSample();
            TridasSample rhsSample;
            rhsSample = that.getSample();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sample", lhsSample), LocatorUtils.property(thatLocator, "sample", rhsSample), lhsSample, rhsSample)) {
                return false;
            }
        }
        {
            WSILoan lhsLoan;
            lhsLoan = this.getLoan();
            WSILoan rhsLoan;
            rhsLoan = that.getLoan();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "loan", lhsLoan), LocatorUtils.property(thatLocator, "loan", rhsLoan), lhsLoan, rhsLoan)) {
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
            CurationStatus theStatus;
            theStatus = this.getStatus();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "status", theStatus), currentHashCode, theStatus);
        }
        {
            WSISecurityUser theSecurityUser;
            theSecurityUser = this.getSecurityUser();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityUser", theSecurityUser), currentHashCode, theSecurityUser);
        }
        {
            XMLGregorianCalendar theCurationtimestamp;
            theCurationtimestamp = this.getCurationtimestamp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "curationtimestamp", theCurationtimestamp), currentHashCode, theCurationtimestamp);
        }
        {
            String theNotes;
            theNotes = this.getNotes();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "notes", theNotes), currentHashCode, theNotes);
        }
        {
            TridasSample theSample;
            theSample = this.getSample();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sample", theSample), currentHashCode, theSample);
        }
        {
            WSILoan theLoan;
            theLoan = this.getLoan();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "loan", theLoan), currentHashCode, theLoan);
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
        if (draftCopy instanceof WSICuration) {
            final WSICuration copy = ((WSICuration) draftCopy);
            if (this.isSetIdentifier()) {
                TridasIdentifier sourceIdentifier;
                sourceIdentifier = this.getIdentifier();
                TridasIdentifier copyIdentifier = ((TridasIdentifier) strategy.copy(LocatorUtils.property(locator, "identifier", sourceIdentifier), sourceIdentifier));
                copy.setIdentifier(copyIdentifier);
            } else {
                copy.identifier = null;
            }
            if (this.isSetStatus()) {
                CurationStatus sourceStatus;
                sourceStatus = this.getStatus();
                CurationStatus copyStatus = ((CurationStatus) strategy.copy(LocatorUtils.property(locator, "status", sourceStatus), sourceStatus));
                copy.setStatus(copyStatus);
            } else {
                copy.status = null;
            }
            if (this.isSetSecurityUser()) {
                WSISecurityUser sourceSecurityUser;
                sourceSecurityUser = this.getSecurityUser();
                WSISecurityUser copySecurityUser = ((WSISecurityUser) strategy.copy(LocatorUtils.property(locator, "securityUser", sourceSecurityUser), sourceSecurityUser));
                copy.setSecurityUser(copySecurityUser);
            } else {
                copy.securityUser = null;
            }
            if (this.isSetCurationtimestamp()) {
                XMLGregorianCalendar sourceCurationtimestamp;
                sourceCurationtimestamp = this.getCurationtimestamp();
                XMLGregorianCalendar copyCurationtimestamp = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "curationtimestamp", sourceCurationtimestamp), sourceCurationtimestamp));
                copy.setCurationtimestamp(copyCurationtimestamp);
            } else {
                copy.curationtimestamp = null;
            }
            if (this.isSetNotes()) {
                String sourceNotes;
                sourceNotes = this.getNotes();
                String copyNotes = ((String) strategy.copy(LocatorUtils.property(locator, "notes", sourceNotes), sourceNotes));
                copy.setNotes(copyNotes);
            } else {
                copy.notes = null;
            }
            if (this.isSetSample()) {
                TridasSample sourceSample;
                sourceSample = this.getSample();
                TridasSample copySample = ((TridasSample) strategy.copy(LocatorUtils.property(locator, "sample", sourceSample), sourceSample));
                copy.setSample(copySample);
            } else {
                copy.sample = null;
            }
            if (this.isSetLoan()) {
                WSILoan sourceLoan;
                sourceLoan = this.getLoan();
                WSILoan copyLoan = ((WSILoan) strategy.copy(LocatorUtils.property(locator, "loan", sourceLoan), sourceLoan));
                copy.setLoan(copyLoan);
            } else {
                copy.loan = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSICuration();
    }

}
