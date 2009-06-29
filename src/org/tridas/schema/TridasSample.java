
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.2}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2}type"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}samplingDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}position" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}state" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}knots" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.tridas.org/1.2}radius" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://www.tridas.org/1.2}radiusPlaceholder" minOccurs="0"/>
 *         &lt;/choice>
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
    "type",
    "description",
    "files",
    "samplingDate",
    "position",
    "state",
    "knots",
    "genericFields",
    "radiusPlaceholder",
    "radiuses"
})
@XmlRootElement(name = "sample")
public class TridasSample
    extends TridasEntity
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(required = true)
    @TridasCustomDictionary(dictionary = "sampleType", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected ControlledVoc type;
    protected String description;
    @XmlElement(name = "file")
    protected List<TridasFile> files;
    protected Date samplingDate;
    protected String position;
    protected String state;
    protected Boolean knots;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected TridasRadiusPlaceholder radiusPlaceholder;
    @XmlElement(name = "radius")
    protected List<TridasRadius> radiuses;

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
     * Gets the value of the samplingDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getSamplingDate() {
        return samplingDate;
    }

    /**
     * Sets the value of the samplingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setSamplingDate(Date value) {
        this.samplingDate = value;
    }

    public boolean isSetSamplingDate() {
        return (this.samplingDate!= null);
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    public boolean isSetPosition() {
        return (this.position!= null);
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    public boolean isSetState() {
        return (this.state!= null);
    }

    /**
     * Gets the value of the knots property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKnots() {
        return knots;
    }

    /**
     * Sets the value of the knots property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKnots(Boolean value) {
        this.knots = value;
    }

    public boolean isSetKnots() {
        return (this.knots!= null);
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
     * Gets the value of the radiusPlaceholder property.
     * 
     * @return
     *     possible object is
     *     {@link TridasRadiusPlaceholder }
     *     
     */
    public TridasRadiusPlaceholder getRadiusPlaceholder() {
        return radiusPlaceholder;
    }

    /**
     * Sets the value of the radiusPlaceholder property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasRadiusPlaceholder }
     *     
     */
    public void setRadiusPlaceholder(TridasRadiusPlaceholder value) {
        this.radiusPlaceholder = value;
    }

    public boolean isSetRadiusPlaceholder() {
        return (this.radiusPlaceholder!= null);
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasSample)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasSample that = ((TridasSample) object);
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getDescription(), that.getDescription());
        equalsBuilder.append(this.getFiles(), that.getFiles());
        equalsBuilder.append(this.getSamplingDate(), that.getSamplingDate());
        equalsBuilder.append(this.getPosition(), that.getPosition());
        equalsBuilder.append(this.getState(), that.getState());
        equalsBuilder.append(this.isKnots(), that.isKnots());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getRadiusPlaceholder(), that.getRadiusPlaceholder());
        equalsBuilder.append(this.getRadiuses(), that.getRadiuses());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasSample)) {
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
        hashCodeBuilder.append(this.getFiles());
        hashCodeBuilder.append(this.getSamplingDate());
        hashCodeBuilder.append(this.getPosition());
        hashCodeBuilder.append(this.getState());
        hashCodeBuilder.append(this.isKnots());
        hashCodeBuilder.append(this.getGenericFields());
        hashCodeBuilder.append(this.getRadiusPlaceholder());
        hashCodeBuilder.append(this.getRadiuses());
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
            List<TridasFile> theFiles;
            theFiles = this.getFiles();
            toStringBuilder.append("files", theFiles);
        }
        {
            Date theSamplingDate;
            theSamplingDate = this.getSamplingDate();
            toStringBuilder.append("samplingDate", theSamplingDate);
        }
        {
            String thePosition;
            thePosition = this.getPosition();
            toStringBuilder.append("position", thePosition);
        }
        {
            String theState;
            theState = this.getState();
            toStringBuilder.append("state", theState);
        }
        {
            Boolean theKnots;
            theKnots = this.isKnots();
            toStringBuilder.append("knots", theKnots);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = this.getGenericFields();
            toStringBuilder.append("genericFields", theGenericFields);
        }
        {
            TridasRadiusPlaceholder theRadiusPlaceholder;
            theRadiusPlaceholder = this.getRadiusPlaceholder();
            toStringBuilder.append("radiusPlaceholder", theRadiusPlaceholder);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = this.getRadiuses();
            toStringBuilder.append("radiuses", theRadiuses);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasSample copy = ((target == null)?((TridasSample) createCopy()):((TridasSample) target));
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
            List<TridasFile> sourceFiles;
            sourceFiles = this.getFiles();
            List<TridasFile> copyFiles = ((List<TridasFile> ) copyBuilder.copy(sourceFiles));
            copy.unsetFiles();
            List<TridasFile> uniqueFilesl = copy.getFiles();
            uniqueFilesl.addAll(copyFiles);
        }
        {
            Date sourceSamplingDate;
            sourceSamplingDate = this.getSamplingDate();
            Date copySamplingDate = ((Date) copyBuilder.copy(sourceSamplingDate));
            copy.setSamplingDate(copySamplingDate);
        }
        {
            String sourcePosition;
            sourcePosition = this.getPosition();
            String copyPosition = ((String) copyBuilder.copy(sourcePosition));
            copy.setPosition(copyPosition);
        }
        {
            String sourceState;
            sourceState = this.getState();
            String copyState = ((String) copyBuilder.copy(sourceState));
            copy.setState(copyState);
        }
        {
            Boolean sourceKnots;
            sourceKnots = this.isKnots();
            Boolean copyKnots = ((Boolean) copyBuilder.copy(sourceKnots));
            copy.setKnots(copyKnots);
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
            TridasRadiusPlaceholder sourceRadiusPlaceholder;
            sourceRadiusPlaceholder = this.getRadiusPlaceholder();
            TridasRadiusPlaceholder copyRadiusPlaceholder = ((TridasRadiusPlaceholder) copyBuilder.copy(sourceRadiusPlaceholder));
            copy.setRadiusPlaceholder(copyRadiusPlaceholder);
        }
        {
            List<TridasRadius> sourceRadiuses;
            sourceRadiuses = this.getRadiuses();
            List<TridasRadius> copyRadiuses = ((List<TridasRadius> ) copyBuilder.copy(sourceRadiuses));
            copy.unsetRadiuses();
            List<TridasRadius> uniqueRadiusesl = copy.getRadiuses();
            uniqueRadiusesl.addAll(copyRadiuses);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasSample();
    }

}
