
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
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
import org.tridas.annotations.TridasCustomDictionary;
import org.tridas.annotations.TridasCustomDictionarySortType;
import org.tridas.annotations.TridasCustomDictionaryType;
import org.tridas.annotations.TridasEditProperties;
import org.tridas.interfaces.ITridasDerivedSeries;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.tridas.org/1.3}baseSeries">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}comments" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}derivationDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}type"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}linkSeries"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}objective" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}standardizingMethod" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}author" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}version" minOccurs="0"/>
 *         &lt;group ref="{http://www.tridas.org/1.3}interpretationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}location" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}values" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "identifier",
    "createdTimestamp",
    "lastModifiedTimestamp",
    "comments",
    "derivationDate",
    "type",
    "linkSeries",
    "objective",
    "standardizingMethod",
    "author",
    "version",
    "interpretationUnsolved",
    "interpretation",
    "location",
    "genericFields",
    "values"
})
@XmlRootElement(name = "derivedSeries")
public class TridasDerivedSeries implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, ITridasDerivedSeries
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected String title;
    protected TridasIdentifier identifier;
    protected DateTime createdTimestamp;
    protected DateTime lastModifiedTimestamp;
    protected String comments;
    protected Date derivationDate;
    @XmlElement(required = true)
    protected ControlledVoc type;
    @XmlElement(required = true)
    protected TridasLinkSeries linkSeries;
    protected String objective;
    protected String standardizingMethod;
    @TridasCustomDictionary(dictionary = "securityUser", identifierField = "corina.authorID", sortType = TridasCustomDictionarySortType.LASTNAME_FIRSTNAME, type = TridasCustomDictionaryType.CORINA_GENERICID)
    protected String author;
    protected String version;
    @TridasEditProperties(machineOnly = true)
    protected TridasInterpretationUnsolved interpretationUnsolved;
    protected TridasInterpretation interpretation;
    protected TridasLocation location;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected List<TridasValues> values;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    @TridasEditProperties(machineOnly = true)
    protected String id;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public boolean isSetTitle() {
        return (this.title!= null);
    }

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
     * Gets the value of the createdTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the value of the createdTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setCreatedTimestamp(DateTime value) {
        this.createdTimestamp = value;
    }

    public boolean isSetCreatedTimestamp() {
        return (this.createdTimestamp!= null);
    }

    /**
     * Gets the value of the lastModifiedTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    /**
     * Sets the value of the lastModifiedTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setLastModifiedTimestamp(DateTime value) {
        this.lastModifiedTimestamp = value;
    }

    public boolean isSetLastModifiedTimestamp() {
        return (this.lastModifiedTimestamp!= null);
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    public boolean isSetComments() {
        return (this.comments!= null);
    }

    /**
     * Gets the value of the derivationDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getDerivationDate() {
        return derivationDate;
    }

    /**
     * Sets the value of the derivationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setDerivationDate(Date value) {
        this.derivationDate = value;
    }

    public boolean isSetDerivationDate() {
        return (this.derivationDate!= null);
    }

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
     * Gets the value of the linkSeries property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLinkSeries }
     *     
     */
    public TridasLinkSeries getLinkSeries() {
        return linkSeries;
    }

    /**
     * Sets the value of the linkSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLinkSeries }
     *     
     */
    public void setLinkSeries(TridasLinkSeries value) {
        this.linkSeries = value;
    }

    public boolean isSetLinkSeries() {
        return (this.linkSeries!= null);
    }

    /**
     * Gets the value of the objective property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Sets the value of the objective property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjective(String value) {
        this.objective = value;
    }

    public boolean isSetObjective() {
        return (this.objective!= null);
    }

    /**
     * Gets the value of the standardizingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardizingMethod() {
        return standardizingMethod;
    }

    /**
     * Sets the value of the standardizingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardizingMethod(String value) {
        this.standardizingMethod = value;
    }

    public boolean isSetStandardizingMethod() {
        return (this.standardizingMethod!= null);
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    public boolean isSetAuthor() {
        return (this.author!= null);
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    public boolean isSetVersion() {
        return (this.version!= null);
    }

    /**
     * Gets the value of the interpretationUnsolved property.
     * 
     * @return
     *     possible object is
     *     {@link TridasInterpretationUnsolved }
     *     
     */
    public TridasInterpretationUnsolved getInterpretationUnsolved() {
        return interpretationUnsolved;
    }

    /**
     * Sets the value of the interpretationUnsolved property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasInterpretationUnsolved }
     *     
     */
    public void setInterpretationUnsolved(TridasInterpretationUnsolved value) {
        this.interpretationUnsolved = value;
    }

    public boolean isSetInterpretationUnsolved() {
        return (this.interpretationUnsolved!= null);
    }

    /**
     * Gets the value of the interpretation property.
     * 
     * @return
     *     possible object is
     *     {@link TridasInterpretation }
     *     
     */
    public TridasInterpretation getInterpretation() {
        return interpretation;
    }

    /**
     * Sets the value of the interpretation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasInterpretation }
     *     
     */
    public void setInterpretation(TridasInterpretation value) {
        this.interpretation = value;
    }

    public boolean isSetInterpretation() {
        return (this.interpretation!= null);
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
     * Gets the value of the values property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the values property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasValues }
     * 
     * 
     */
    public List<TridasValues> getValues() {
        if (values == null) {
            values = new ArrayList<TridasValues>();
        }
        return this.values;
    }

    public boolean isSetValues() {
        return ((this.values!= null)&&(!this.values.isEmpty()));
    }

    public void unsetValues() {
        this.values = null;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasDerivedSeries)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasDerivedSeries that = ((TridasDerivedSeries) object);
        equalsBuilder.append(this.getTitle(), that.getTitle());
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getCreatedTimestamp(), that.getCreatedTimestamp());
        equalsBuilder.append(this.getLastModifiedTimestamp(), that.getLastModifiedTimestamp());
        equalsBuilder.append(this.getComments(), that.getComments());
        equalsBuilder.append(this.getDerivationDate(), that.getDerivationDate());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getLinkSeries(), that.getLinkSeries());
        equalsBuilder.append(this.getObjective(), that.getObjective());
        equalsBuilder.append(this.getStandardizingMethod(), that.getStandardizingMethod());
        equalsBuilder.append(this.getAuthor(), that.getAuthor());
        equalsBuilder.append(this.getVersion(), that.getVersion());
        equalsBuilder.append(this.getInterpretationUnsolved(), that.getInterpretationUnsolved());
        equalsBuilder.append(this.getInterpretation(), that.getInterpretation());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getValues(), that.getValues());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasDerivedSeries)) {
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
        hashCodeBuilder.append(this.getTitle());
        hashCodeBuilder.append(this.getIdentifier());
        hashCodeBuilder.append(this.getCreatedTimestamp());
        hashCodeBuilder.append(this.getLastModifiedTimestamp());
        hashCodeBuilder.append(this.getComments());
        hashCodeBuilder.append(this.getDerivationDate());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getLinkSeries());
        hashCodeBuilder.append(this.getObjective());
        hashCodeBuilder.append(this.getStandardizingMethod());
        hashCodeBuilder.append(this.getAuthor());
        hashCodeBuilder.append(this.getVersion());
        hashCodeBuilder.append(this.getInterpretationUnsolved());
        hashCodeBuilder.append(this.getInterpretation());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getGenericFields());
        hashCodeBuilder.append(this.getValues());
        hashCodeBuilder.append(this.getId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theTitle;
            theTitle = this.getTitle();
            toStringBuilder.append("title", theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            toStringBuilder.append("identifier", theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            toStringBuilder.append("createdTimestamp", theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            toStringBuilder.append("lastModifiedTimestamp", theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            toStringBuilder.append("comments", theComments);
        }
        {
            Date theDerivationDate;
            theDerivationDate = this.getDerivationDate();
            toStringBuilder.append("derivationDate", theDerivationDate);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
        {
            TridasLinkSeries theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            toStringBuilder.append("linkSeries", theLinkSeries);
        }
        {
            String theObjective;
            theObjective = this.getObjective();
            toStringBuilder.append("objective", theObjective);
        }
        {
            String theStandardizingMethod;
            theStandardizingMethod = this.getStandardizingMethod();
            toStringBuilder.append("standardizingMethod", theStandardizingMethod);
        }
        {
            String theAuthor;
            theAuthor = this.getAuthor();
            toStringBuilder.append("author", theAuthor);
        }
        {
            String theVersion;
            theVersion = this.getVersion();
            toStringBuilder.append("version", theVersion);
        }
        {
            TridasInterpretationUnsolved theInterpretationUnsolved;
            theInterpretationUnsolved = this.getInterpretationUnsolved();
            toStringBuilder.append("interpretationUnsolved", theInterpretationUnsolved);
        }
        {
            TridasInterpretation theInterpretation;
            theInterpretation = this.getInterpretation();
            toStringBuilder.append("interpretation", theInterpretation);
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
            List<TridasValues> theValues;
            theValues = this.getValues();
            toStringBuilder.append("values", theValues);
        }
        {
            String theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasDerivedSeries copy = ((target == null)?((TridasDerivedSeries) createCopy()):((TridasDerivedSeries) target));
        {
            String sourceTitle;
            sourceTitle = this.getTitle();
            String copyTitle = ((String) copyBuilder.copy(sourceTitle));
            copy.setTitle(copyTitle);
        }
        {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        }
        {
            DateTime sourceCreatedTimestamp;
            sourceCreatedTimestamp = this.getCreatedTimestamp();
            DateTime copyCreatedTimestamp = ((DateTime) copyBuilder.copy(sourceCreatedTimestamp));
            copy.setCreatedTimestamp(copyCreatedTimestamp);
        }
        {
            DateTime sourceLastModifiedTimestamp;
            sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime copyLastModifiedTimestamp = ((DateTime) copyBuilder.copy(sourceLastModifiedTimestamp));
            copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
        }
        {
            String sourceComments;
            sourceComments = this.getComments();
            String copyComments = ((String) copyBuilder.copy(sourceComments));
            copy.setComments(copyComments);
        }
        {
            Date sourceDerivationDate;
            sourceDerivationDate = this.getDerivationDate();
            Date copyDerivationDate = ((Date) copyBuilder.copy(sourceDerivationDate));
            copy.setDerivationDate(copyDerivationDate);
        }
        {
            ControlledVoc sourceType;
            sourceType = this.getType();
            ControlledVoc copyType = ((ControlledVoc) copyBuilder.copy(sourceType));
            copy.setType(copyType);
        }
        {
            TridasLinkSeries sourceLinkSeries;
            sourceLinkSeries = this.getLinkSeries();
            TridasLinkSeries copyLinkSeries = ((TridasLinkSeries) copyBuilder.copy(sourceLinkSeries));
            copy.setLinkSeries(copyLinkSeries);
        }
        {
            String sourceObjective;
            sourceObjective = this.getObjective();
            String copyObjective = ((String) copyBuilder.copy(sourceObjective));
            copy.setObjective(copyObjective);
        }
        {
            String sourceStandardizingMethod;
            sourceStandardizingMethod = this.getStandardizingMethod();
            String copyStandardizingMethod = ((String) copyBuilder.copy(sourceStandardizingMethod));
            copy.setStandardizingMethod(copyStandardizingMethod);
        }
        {
            String sourceAuthor;
            sourceAuthor = this.getAuthor();
            String copyAuthor = ((String) copyBuilder.copy(sourceAuthor));
            copy.setAuthor(copyAuthor);
        }
        {
            String sourceVersion;
            sourceVersion = this.getVersion();
            String copyVersion = ((String) copyBuilder.copy(sourceVersion));
            copy.setVersion(copyVersion);
        }
        {
            TridasInterpretationUnsolved sourceInterpretationUnsolved;
            sourceInterpretationUnsolved = this.getInterpretationUnsolved();
            TridasInterpretationUnsolved copyInterpretationUnsolved = ((TridasInterpretationUnsolved) copyBuilder.copy(sourceInterpretationUnsolved));
            copy.setInterpretationUnsolved(copyInterpretationUnsolved);
        }
        {
            TridasInterpretation sourceInterpretation;
            sourceInterpretation = this.getInterpretation();
            TridasInterpretation copyInterpretation = ((TridasInterpretation) copyBuilder.copy(sourceInterpretation));
            copy.setInterpretation(copyInterpretation);
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
            copy.unsetGenericFields();
            List<TridasGenericField> uniqueGenericFieldsl = copy.getGenericFields();
            uniqueGenericFieldsl.addAll(copyGenericFields);
        }
        {
            List<TridasValues> sourceValues;
            sourceValues = this.getValues();
            List<TridasValues> copyValues = ((List<TridasValues> ) copyBuilder.copy(sourceValues));
            copy.unsetValues();
            List<TridasValues> uniqueValuesl = copy.getValues();
            uniqueValuesl.addAll(copyValues);
        }
        {
            String sourceId;
            sourceId = this.getId();
            String copyId = ((String) copyBuilder.copy(sourceId));
            copy.setId(copyId);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasDerivedSeries();
    }

}
