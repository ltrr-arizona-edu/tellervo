
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
import org.tridas.interfaces.ITridasSeries;


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
 *         &lt;element ref="{http://www.tridas.org/1.3}measuringDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}woodCompleteness" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}analyst" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}dendrochronologist" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}measuringMethod"/>
 *         &lt;group ref="{http://www.tridas.org/1.3}interpretationType" minOccurs="0"/>
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
    "measuringDate",
    "woodCompleteness",
    "analyst",
    "dendrochronologist",
    "measuringMethod",
    "interpretationUnsolved",
    "interpretation",
    "genericFields",
    "values"
})
@XmlRootElement(name = "measurementSeries")
public class TridasMeasurementSeries implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString, ITridasSeries
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    protected String title;
    protected TridasIdentifier identifier;
    protected DateTime createdTimestamp;
    protected DateTime lastModifiedTimestamp;
    protected String comments;
    protected Date measuringDate;
    protected TridasWoodCompleteness woodCompleteness;
    @TridasCustomDictionary(dictionary = "securityUser", identifierField = "corina.analystID", sortType = TridasCustomDictionarySortType.LASTNAME_FIRSTNAME, type = TridasCustomDictionaryType.CORINA_GENERICID)
    protected String analyst;
    @TridasCustomDictionary(dictionary = "securityUser", identifierField = "corina.dendrochronologistID", sortType = TridasCustomDictionarySortType.LASTNAME_FIRSTNAME, type = TridasCustomDictionaryType.CORINA_GENERICID)
    protected String dendrochronologist;
    @XmlElement(required = true)
    protected TridasMeasuringMethod measuringMethod;
    @TridasEditProperties(machineOnly = true)
    protected TridasInterpretationUnsolved interpretationUnsolved;
    protected TridasInterpretation interpretation;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected List<TridasValues> values;
    @XmlAttribute(name = "id")
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
     * Gets the value of the measuringDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getMeasuringDate() {
        return measuringDate;
    }

    /**
     * Sets the value of the measuringDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setMeasuringDate(Date value) {
        this.measuringDate = value;
    }

    public boolean isSetMeasuringDate() {
        return (this.measuringDate!= null);
    }

    /**
     * Gets the value of the woodCompleteness property.
     * 
     * @return
     *     possible object is
     *     {@link TridasWoodCompleteness }
     *     
     */
    public TridasWoodCompleteness getWoodCompleteness() {
        return woodCompleteness;
    }

    /**
     * Sets the value of the woodCompleteness property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasWoodCompleteness }
     *     
     */
    public void setWoodCompleteness(TridasWoodCompleteness value) {
        this.woodCompleteness = value;
    }

    public boolean isSetWoodCompleteness() {
        return (this.woodCompleteness!= null);
    }

    /**
     * Gets the value of the analyst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalyst() {
        return analyst;
    }

    /**
     * Sets the value of the analyst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalyst(String value) {
        this.analyst = value;
    }

    public boolean isSetAnalyst() {
        return (this.analyst!= null);
    }

    /**
     * Gets the value of the dendrochronologist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDendrochronologist() {
        return dendrochronologist;
    }

    /**
     * Sets the value of the dendrochronologist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDendrochronologist(String value) {
        this.dendrochronologist = value;
    }

    public boolean isSetDendrochronologist() {
        return (this.dendrochronologist!= null);
    }

    /**
     * Gets the value of the measuringMethod property.
     * 
     * @return
     *     possible object is
     *     {@link TridasMeasuringMethod }
     *     
     */
    public TridasMeasuringMethod getMeasuringMethod() {
        return measuringMethod;
    }

    /**
     * Sets the value of the measuringMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasMeasuringMethod }
     *     
     */
    public void setMeasuringMethod(TridasMeasuringMethod value) {
        this.measuringMethod = value;
    }

    public boolean isSetMeasuringMethod() {
        return (this.measuringMethod!= null);
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
     * Sets the value of the values property.
     * 
     * @param values
     *     allowed object is
     *     {@link TridasValues }
     *     
     */
    public void setValues(List<TridasValues> values) {
        this.values = values;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasMeasurementSeries)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasMeasurementSeries that = ((TridasMeasurementSeries) object);
        equalsBuilder.append(this.getTitle(), that.getTitle());
        equalsBuilder.append(this.getIdentifier(), that.getIdentifier());
        equalsBuilder.append(this.getCreatedTimestamp(), that.getCreatedTimestamp());
        equalsBuilder.append(this.getLastModifiedTimestamp(), that.getLastModifiedTimestamp());
        equalsBuilder.append(this.getComments(), that.getComments());
        equalsBuilder.append(this.getMeasuringDate(), that.getMeasuringDate());
        equalsBuilder.append(this.getWoodCompleteness(), that.getWoodCompleteness());
        equalsBuilder.append(this.getAnalyst(), that.getAnalyst());
        equalsBuilder.append(this.getDendrochronologist(), that.getDendrochronologist());
        equalsBuilder.append(this.getMeasuringMethod(), that.getMeasuringMethod());
        equalsBuilder.append(this.getInterpretationUnsolved(), that.getInterpretationUnsolved());
        equalsBuilder.append(this.getInterpretation(), that.getInterpretation());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getValues(), that.getValues());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasMeasurementSeries)) {
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
        hashCodeBuilder.append(this.getMeasuringDate());
        hashCodeBuilder.append(this.getWoodCompleteness());
        hashCodeBuilder.append(this.getAnalyst());
        hashCodeBuilder.append(this.getDendrochronologist());
        hashCodeBuilder.append(this.getMeasuringMethod());
        hashCodeBuilder.append(this.getInterpretationUnsolved());
        hashCodeBuilder.append(this.getInterpretation());
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
            Date theMeasuringDate;
            theMeasuringDate = this.getMeasuringDate();
            toStringBuilder.append("measuringDate", theMeasuringDate);
        }
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            toStringBuilder.append("woodCompleteness", theWoodCompleteness);
        }
        {
            String theAnalyst;
            theAnalyst = this.getAnalyst();
            toStringBuilder.append("analyst", theAnalyst);
        }
        {
            String theDendrochronologist;
            theDendrochronologist = this.getDendrochronologist();
            toStringBuilder.append("dendrochronologist", theDendrochronologist);
        }
        {
            TridasMeasuringMethod theMeasuringMethod;
            theMeasuringMethod = this.getMeasuringMethod();
            toStringBuilder.append("measuringMethod", theMeasuringMethod);
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
        final TridasMeasurementSeries copy = ((target == null)?((TridasMeasurementSeries) createCopy()):((TridasMeasurementSeries) target));
        if (this.isSetTitle()) {
            String sourceTitle;
            sourceTitle = this.getTitle();
            String copyTitle = ((String) copyBuilder.copy(sourceTitle));
            copy.setTitle(copyTitle);
        } else {
            copy.title = null;
        }
        if (this.isSetIdentifier()) {
            TridasIdentifier sourceIdentifier;
            sourceIdentifier = this.getIdentifier();
            TridasIdentifier copyIdentifier = ((TridasIdentifier) copyBuilder.copy(sourceIdentifier));
            copy.setIdentifier(copyIdentifier);
        } else {
            copy.identifier = null;
        }
        if (this.isSetCreatedTimestamp()) {
            DateTime sourceCreatedTimestamp;
            sourceCreatedTimestamp = this.getCreatedTimestamp();
            DateTime copyCreatedTimestamp = ((DateTime) copyBuilder.copy(sourceCreatedTimestamp));
            copy.setCreatedTimestamp(copyCreatedTimestamp);
        } else {
            copy.createdTimestamp = null;
        }
        if (this.isSetLastModifiedTimestamp()) {
            DateTime sourceLastModifiedTimestamp;
            sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime copyLastModifiedTimestamp = ((DateTime) copyBuilder.copy(sourceLastModifiedTimestamp));
            copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
        } else {
            copy.lastModifiedTimestamp = null;
        }
        if (this.isSetComments()) {
            String sourceComments;
            sourceComments = this.getComments();
            String copyComments = ((String) copyBuilder.copy(sourceComments));
            copy.setComments(copyComments);
        } else {
            copy.comments = null;
        }
        if (this.isSetMeasuringDate()) {
            Date sourceMeasuringDate;
            sourceMeasuringDate = this.getMeasuringDate();
            Date copyMeasuringDate = ((Date) copyBuilder.copy(sourceMeasuringDate));
            copy.setMeasuringDate(copyMeasuringDate);
        } else {
            copy.measuringDate = null;
        }
        if (this.isSetWoodCompleteness()) {
            TridasWoodCompleteness sourceWoodCompleteness;
            sourceWoodCompleteness = this.getWoodCompleteness();
            TridasWoodCompleteness copyWoodCompleteness = ((TridasWoodCompleteness) copyBuilder.copy(sourceWoodCompleteness));
            copy.setWoodCompleteness(copyWoodCompleteness);
        } else {
            copy.woodCompleteness = null;
        }
        if (this.isSetAnalyst()) {
            String sourceAnalyst;
            sourceAnalyst = this.getAnalyst();
            String copyAnalyst = ((String) copyBuilder.copy(sourceAnalyst));
            copy.setAnalyst(copyAnalyst);
        } else {
            copy.analyst = null;
        }
        if (this.isSetDendrochronologist()) {
            String sourceDendrochronologist;
            sourceDendrochronologist = this.getDendrochronologist();
            String copyDendrochronologist = ((String) copyBuilder.copy(sourceDendrochronologist));
            copy.setDendrochronologist(copyDendrochronologist);
        } else {
            copy.dendrochronologist = null;
        }
        if (this.isSetMeasuringMethod()) {
            TridasMeasuringMethod sourceMeasuringMethod;
            sourceMeasuringMethod = this.getMeasuringMethod();
            TridasMeasuringMethod copyMeasuringMethod = ((TridasMeasuringMethod) copyBuilder.copy(sourceMeasuringMethod));
            copy.setMeasuringMethod(copyMeasuringMethod);
        } else {
            copy.measuringMethod = null;
        }
        if (this.isSetInterpretationUnsolved()) {
            TridasInterpretationUnsolved sourceInterpretationUnsolved;
            sourceInterpretationUnsolved = this.getInterpretationUnsolved();
            TridasInterpretationUnsolved copyInterpretationUnsolved = ((TridasInterpretationUnsolved) copyBuilder.copy(sourceInterpretationUnsolved));
            copy.setInterpretationUnsolved(copyInterpretationUnsolved);
        } else {
            copy.interpretationUnsolved = null;
        }
        if (this.isSetInterpretation()) {
            TridasInterpretation sourceInterpretation;
            sourceInterpretation = this.getInterpretation();
            TridasInterpretation copyInterpretation = ((TridasInterpretation) copyBuilder.copy(sourceInterpretation));
            copy.setInterpretation(copyInterpretation);
        } else {
            copy.interpretation = null;
        }
        if (this.isSetGenericFields()) {
            List<TridasGenericField> sourceGenericFields;
            sourceGenericFields = this.getGenericFields();
            @SuppressWarnings("unchecked")
            List<TridasGenericField> copyGenericFields = ((List<TridasGenericField> ) copyBuilder.copy(sourceGenericFields));
            copy.setGenericFields(copyGenericFields);
        } else {
            copy.unsetGenericFields();
        }
        if (this.isSetValues()) {
            List<TridasValues> sourceValues;
            sourceValues = this.getValues();
            @SuppressWarnings("unchecked")
            List<TridasValues> copyValues = ((List<TridasValues> ) copyBuilder.copy(sourceValues));
            copy.setValues(copyValues);
        } else {
            copy.unsetValues();
        }
        if (this.isSetId()) {
            String sourceId;
            sourceId = this.getId();
            String copyId = ((String) copyBuilder.copy(sourceId));
            copy.setId(copyId);
        } else {
            copy.id = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasMeasurementSeries();
    }

}
