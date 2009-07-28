
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
import org.tridas.annotations.TridasCustomDictionarySortType;
import org.tridas.annotations.TridasCustomDictionaryType;
import org.tridas.annotations.TridasEditProperties;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.3}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}type" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}description" minOccurs="0"/>
 *         &lt;element name="linkSeries" type="{http://www.tridas.org/1.3}seriesLinks" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}taxon"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}shape" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}dimensions" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}authenticity" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}location" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}processing" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}marks" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}altitude" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}slope" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}soil" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}bedrock" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}sample" maxOccurs="unbounded" minOccurs="0"/>
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
    "linkSeries",
    "files",
    "taxon",
    "shape",
    "dimensions",
    "authenticity",
    "location",
    "processing",
    "marks",
    "altitude",
    "slope",
    "soil",
    "bedrock",
    "genericFields",
    "samples"
})
@XmlRootElement(name = "element")
public class TridasElement
    extends TridasEntity
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @TridasCustomDictionary(dictionary = "elementType", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected ControlledVoc type;
    protected String description;
    @TridasEditProperties(machineOnly = true)
    protected SeriesLinks linkSeries;
    @XmlElement(name = "file")
    protected List<TridasFile> files;
    @XmlElement(required = true)
    @TridasCustomDictionary(dictionary = "taxon", sortType = TridasCustomDictionarySortType.NORMAL_OR_VALUE, type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected ControlledVoc taxon;
    @TridasCustomDictionary(dictionary = "elementShape", type = TridasCustomDictionaryType.CORINA_CONTROLLEDVOC)
    protected TridasShape shape;
    protected TridasDimensions dimensions;
    protected String authenticity;
    protected TridasLocation location;
    protected String processing;
    protected String marks;
    protected Double altitude;
    protected TridasSlope slope;
    protected TridasSoil soil;
    protected TridasBedrock bedrock;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    @XmlElement(name = "sample")
    protected List<TridasSample> samples;

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
     *     {@link SeriesLinks }
     *     
     */
    public SeriesLinks getLinkSeries() {
        return linkSeries;
    }

    /**
     * Sets the value of the linkSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link SeriesLinks }
     *     
     */
    public void setLinkSeries(SeriesLinks value) {
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
     * Gets the value of the taxon property.
     * 
     * @return
     *     possible object is
     *     {@link ControlledVoc }
     *     
     */
    public ControlledVoc getTaxon() {
        return taxon;
    }

    /**
     * Sets the value of the taxon property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlledVoc }
     *     
     */
    public void setTaxon(ControlledVoc value) {
        this.taxon = value;
    }

    public boolean isSetTaxon() {
        return (this.taxon!= null);
    }

    /**
     * Gets the value of the shape property.
     * 
     * @return
     *     possible object is
     *     {@link TridasShape }
     *     
     */
    public TridasShape getShape() {
        return shape;
    }

    /**
     * Sets the value of the shape property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasShape }
     *     
     */
    public void setShape(TridasShape value) {
        this.shape = value;
    }

    public boolean isSetShape() {
        return (this.shape!= null);
    }

    /**
     * Gets the value of the dimensions property.
     * 
     * @return
     *     possible object is
     *     {@link TridasDimensions }
     *     
     */
    public TridasDimensions getDimensions() {
        return dimensions;
    }

    /**
     * Sets the value of the dimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasDimensions }
     *     
     */
    public void setDimensions(TridasDimensions value) {
        this.dimensions = value;
    }

    public boolean isSetDimensions() {
        return (this.dimensions!= null);
    }

    /**
     * Gets the value of the authenticity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticity() {
        return authenticity;
    }

    /**
     * Sets the value of the authenticity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticity(String value) {
        this.authenticity = value;
    }

    public boolean isSetAuthenticity() {
        return (this.authenticity!= null);
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
     * Gets the value of the processing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessing() {
        return processing;
    }

    /**
     * Sets the value of the processing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessing(String value) {
        this.processing = value;
    }

    public boolean isSetProcessing() {
        return (this.processing!= null);
    }

    /**
     * Gets the value of the marks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarks() {
        return marks;
    }

    /**
     * Sets the value of the marks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarks(String value) {
        this.marks = value;
    }

    public boolean isSetMarks() {
        return (this.marks!= null);
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAltitude(Double value) {
        this.altitude = value;
    }

    public boolean isSetAltitude() {
        return (this.altitude!= null);
    }

    /**
     * Gets the value of the slope property.
     * 
     * @return
     *     possible object is
     *     {@link TridasSlope }
     *     
     */
    public TridasSlope getSlope() {
        return slope;
    }

    /**
     * Sets the value of the slope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasSlope }
     *     
     */
    public void setSlope(TridasSlope value) {
        this.slope = value;
    }

    public boolean isSetSlope() {
        return (this.slope!= null);
    }

    /**
     * Gets the value of the soil property.
     * 
     * @return
     *     possible object is
     *     {@link TridasSoil }
     *     
     */
    public TridasSoil getSoil() {
        return soil;
    }

    /**
     * Sets the value of the soil property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasSoil }
     *     
     */
    public void setSoil(TridasSoil value) {
        this.soil = value;
    }

    public boolean isSetSoil() {
        return (this.soil!= null);
    }

    /**
     * Gets the value of the bedrock property.
     * 
     * @return
     *     possible object is
     *     {@link TridasBedrock }
     *     
     */
    public TridasBedrock getBedrock() {
        return bedrock;
    }

    /**
     * Sets the value of the bedrock property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasBedrock }
     *     
     */
    public void setBedrock(TridasBedrock value) {
        this.bedrock = value;
    }

    public boolean isSetBedrock() {
        return (this.bedrock!= null);
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasElement)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        super.equals(object, equalsBuilder);
        final TridasElement that = ((TridasElement) object);
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getDescription(), that.getDescription());
        equalsBuilder.append(this.getLinkSeries(), that.getLinkSeries());
        equalsBuilder.append(this.getFiles(), that.getFiles());
        equalsBuilder.append(this.getTaxon(), that.getTaxon());
        equalsBuilder.append(this.getShape(), that.getShape());
        equalsBuilder.append(this.getDimensions(), that.getDimensions());
        equalsBuilder.append(this.getAuthenticity(), that.getAuthenticity());
        equalsBuilder.append(this.getLocation(), that.getLocation());
        equalsBuilder.append(this.getProcessing(), that.getProcessing());
        equalsBuilder.append(this.getMarks(), that.getMarks());
        equalsBuilder.append(this.getAltitude(), that.getAltitude());
        equalsBuilder.append(this.getSlope(), that.getSlope());
        equalsBuilder.append(this.getSoil(), that.getSoil());
        equalsBuilder.append(this.getBedrock(), that.getBedrock());
        equalsBuilder.append(this.getGenericFields(), that.getGenericFields());
        equalsBuilder.append(this.getSamples(), that.getSamples());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasElement)) {
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
        hashCodeBuilder.append(this.getTaxon());
        hashCodeBuilder.append(this.getShape());
        hashCodeBuilder.append(this.getDimensions());
        hashCodeBuilder.append(this.getAuthenticity());
        hashCodeBuilder.append(this.getLocation());
        hashCodeBuilder.append(this.getProcessing());
        hashCodeBuilder.append(this.getMarks());
        hashCodeBuilder.append(this.getAltitude());
        hashCodeBuilder.append(this.getSlope());
        hashCodeBuilder.append(this.getSoil());
        hashCodeBuilder.append(this.getBedrock());
        hashCodeBuilder.append(this.getGenericFields());
        hashCodeBuilder.append(this.getSamples());
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
            SeriesLinks theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            toStringBuilder.append("linkSeries", theLinkSeries);
        }
        {
            List<TridasFile> theFiles;
            theFiles = this.getFiles();
            toStringBuilder.append("files", theFiles);
        }
        {
            ControlledVoc theTaxon;
            theTaxon = this.getTaxon();
            toStringBuilder.append("taxon", theTaxon);
        }
        {
            TridasShape theShape;
            theShape = this.getShape();
            toStringBuilder.append("shape", theShape);
        }
        {
            TridasDimensions theDimensions;
            theDimensions = this.getDimensions();
            toStringBuilder.append("dimensions", theDimensions);
        }
        {
            String theAuthenticity;
            theAuthenticity = this.getAuthenticity();
            toStringBuilder.append("authenticity", theAuthenticity);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            toStringBuilder.append("location", theLocation);
        }
        {
            String theProcessing;
            theProcessing = this.getProcessing();
            toStringBuilder.append("processing", theProcessing);
        }
        {
            String theMarks;
            theMarks = this.getMarks();
            toStringBuilder.append("marks", theMarks);
        }
        {
            Double theAltitude;
            theAltitude = this.getAltitude();
            toStringBuilder.append("altitude", theAltitude);
        }
        {
            TridasSlope theSlope;
            theSlope = this.getSlope();
            toStringBuilder.append("slope", theSlope);
        }
        {
            TridasSoil theSoil;
            theSoil = this.getSoil();
            toStringBuilder.append("soil", theSoil);
        }
        {
            TridasBedrock theBedrock;
            theBedrock = this.getBedrock();
            toStringBuilder.append("bedrock", theBedrock);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = this.getGenericFields();
            toStringBuilder.append("genericFields", theGenericFields);
        }
        {
            List<TridasSample> theSamples;
            theSamples = this.getSamples();
            toStringBuilder.append("samples", theSamples);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasElement copy = ((target == null)?((TridasElement) createCopy()):((TridasElement) target));
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
            SeriesLinks sourceLinkSeries;
            sourceLinkSeries = this.getLinkSeries();
            SeriesLinks copyLinkSeries = ((SeriesLinks) copyBuilder.copy(sourceLinkSeries));
            copy.setLinkSeries(copyLinkSeries);
        }
        {
            List<TridasFile> sourceFiles;
            sourceFiles = this.getFiles();
            List<TridasFile> copyFiles = ((List<TridasFile> ) copyBuilder.copy(sourceFiles));
            copy.setFiles(copyFiles);
        }
        {
            ControlledVoc sourceTaxon;
            sourceTaxon = this.getTaxon();
            ControlledVoc copyTaxon = ((ControlledVoc) copyBuilder.copy(sourceTaxon));
            copy.setTaxon(copyTaxon);
        }
        {
            TridasShape sourceShape;
            sourceShape = this.getShape();
            TridasShape copyShape = ((TridasShape) copyBuilder.copy(sourceShape));
            copy.setShape(copyShape);
        }
        {
            TridasDimensions sourceDimensions;
            sourceDimensions = this.getDimensions();
            TridasDimensions copyDimensions = ((TridasDimensions) copyBuilder.copy(sourceDimensions));
            copy.setDimensions(copyDimensions);
        }
        {
            String sourceAuthenticity;
            sourceAuthenticity = this.getAuthenticity();
            String copyAuthenticity = ((String) copyBuilder.copy(sourceAuthenticity));
            copy.setAuthenticity(copyAuthenticity);
        }
        {
            TridasLocation sourceLocation;
            sourceLocation = this.getLocation();
            TridasLocation copyLocation = ((TridasLocation) copyBuilder.copy(sourceLocation));
            copy.setLocation(copyLocation);
        }
        {
            String sourceProcessing;
            sourceProcessing = this.getProcessing();
            String copyProcessing = ((String) copyBuilder.copy(sourceProcessing));
            copy.setProcessing(copyProcessing);
        }
        {
            String sourceMarks;
            sourceMarks = this.getMarks();
            String copyMarks = ((String) copyBuilder.copy(sourceMarks));
            copy.setMarks(copyMarks);
        }
        {
            Double sourceAltitude;
            sourceAltitude = this.getAltitude();
            Double copyAltitude = ((Double) copyBuilder.copy(sourceAltitude));
            copy.setAltitude(copyAltitude);
        }
        {
            TridasSlope sourceSlope;
            sourceSlope = this.getSlope();
            TridasSlope copySlope = ((TridasSlope) copyBuilder.copy(sourceSlope));
            copy.setSlope(copySlope);
        }
        {
            TridasSoil sourceSoil;
            sourceSoil = this.getSoil();
            TridasSoil copySoil = ((TridasSoil) copyBuilder.copy(sourceSoil));
            copy.setSoil(copySoil);
        }
        {
            TridasBedrock sourceBedrock;
            sourceBedrock = this.getBedrock();
            TridasBedrock copyBedrock = ((TridasBedrock) copyBuilder.copy(sourceBedrock));
            copy.setBedrock(copyBedrock);
        }
        {
            List<TridasGenericField> sourceGenericFields;
            sourceGenericFields = this.getGenericFields();
            List<TridasGenericField> copyGenericFields = ((List<TridasGenericField> ) copyBuilder.copy(sourceGenericFields));
            copy.setGenericFields(copyGenericFields);
        }
        {
            List<TridasSample> sourceSamples;
            sourceSamples = this.getSamples();
            List<TridasSample> copySamples = ((List<TridasSample> ) copyBuilder.copy(sourceSamples));
            copy.setSamples(copySamples);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasElement();
    }

}
