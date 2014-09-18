
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}type" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}description" minOccurs="0"/>
 *         &lt;element name="linkSeries" type="{http://www.tridas.org/1.2.2}seriesLinksWithPreferred" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}taxon"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}shape" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}dimensions" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}authenticity" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}location" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}processing" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}marks" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}altitude" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}slope" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}soil" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}bedrock" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sample" maxOccurs="unbounded" minOccurs="0"/>
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected ControlledVoc type;
    protected String description;
    protected SeriesLinksWithPreferred linkSeries;
    @XmlElement(name = "file")
    protected List<TridasFile> files;
    @XmlElement(required = true)
    protected ControlledVoc taxon;
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
            ControlledVoc theTaxon;
            theTaxon = this.getTaxon();
            strategy.appendField(locator, this, "taxon", buffer, theTaxon);
        }
        {
            TridasShape theShape;
            theShape = this.getShape();
            strategy.appendField(locator, this, "shape", buffer, theShape);
        }
        {
            TridasDimensions theDimensions;
            theDimensions = this.getDimensions();
            strategy.appendField(locator, this, "dimensions", buffer, theDimensions);
        }
        {
            String theAuthenticity;
            theAuthenticity = this.getAuthenticity();
            strategy.appendField(locator, this, "authenticity", buffer, theAuthenticity);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            strategy.appendField(locator, this, "location", buffer, theLocation);
        }
        {
            String theProcessing;
            theProcessing = this.getProcessing();
            strategy.appendField(locator, this, "processing", buffer, theProcessing);
        }
        {
            String theMarks;
            theMarks = this.getMarks();
            strategy.appendField(locator, this, "marks", buffer, theMarks);
        }
        {
            Double theAltitude;
            theAltitude = this.getAltitude();
            strategy.appendField(locator, this, "altitude", buffer, theAltitude);
        }
        {
            TridasSlope theSlope;
            theSlope = this.getSlope();
            strategy.appendField(locator, this, "slope", buffer, theSlope);
        }
        {
            TridasSoil theSoil;
            theSoil = this.getSoil();
            strategy.appendField(locator, this, "soil", buffer, theSoil);
        }
        {
            TridasBedrock theBedrock;
            theBedrock = this.getBedrock();
            strategy.appendField(locator, this, "bedrock", buffer, theBedrock);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            strategy.appendField(locator, this, "genericFields", buffer, theGenericFields);
        }
        {
            List<TridasSample> theSamples;
            theSamples = (this.isSetSamples()?this.getSamples():null);
            strategy.appendField(locator, this, "samples", buffer, theSamples);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasElement)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasElement that = ((TridasElement) object);
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
            ControlledVoc lhsTaxon;
            lhsTaxon = this.getTaxon();
            ControlledVoc rhsTaxon;
            rhsTaxon = that.getTaxon();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "taxon", lhsTaxon), LocatorUtils.property(thatLocator, "taxon", rhsTaxon), lhsTaxon, rhsTaxon)) {
                return false;
            }
        }
        {
            TridasShape lhsShape;
            lhsShape = this.getShape();
            TridasShape rhsShape;
            rhsShape = that.getShape();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "shape", lhsShape), LocatorUtils.property(thatLocator, "shape", rhsShape), lhsShape, rhsShape)) {
                return false;
            }
        }
        {
            TridasDimensions lhsDimensions;
            lhsDimensions = this.getDimensions();
            TridasDimensions rhsDimensions;
            rhsDimensions = that.getDimensions();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dimensions", lhsDimensions), LocatorUtils.property(thatLocator, "dimensions", rhsDimensions), lhsDimensions, rhsDimensions)) {
                return false;
            }
        }
        {
            String lhsAuthenticity;
            lhsAuthenticity = this.getAuthenticity();
            String rhsAuthenticity;
            rhsAuthenticity = that.getAuthenticity();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "authenticity", lhsAuthenticity), LocatorUtils.property(thatLocator, "authenticity", rhsAuthenticity), lhsAuthenticity, rhsAuthenticity)) {
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
            String lhsProcessing;
            lhsProcessing = this.getProcessing();
            String rhsProcessing;
            rhsProcessing = that.getProcessing();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "processing", lhsProcessing), LocatorUtils.property(thatLocator, "processing", rhsProcessing), lhsProcessing, rhsProcessing)) {
                return false;
            }
        }
        {
            String lhsMarks;
            lhsMarks = this.getMarks();
            String rhsMarks;
            rhsMarks = that.getMarks();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "marks", lhsMarks), LocatorUtils.property(thatLocator, "marks", rhsMarks), lhsMarks, rhsMarks)) {
                return false;
            }
        }
        {
            Double lhsAltitude;
            lhsAltitude = this.getAltitude();
            Double rhsAltitude;
            rhsAltitude = that.getAltitude();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "altitude", lhsAltitude), LocatorUtils.property(thatLocator, "altitude", rhsAltitude), lhsAltitude, rhsAltitude)) {
                return false;
            }
        }
        {
            TridasSlope lhsSlope;
            lhsSlope = this.getSlope();
            TridasSlope rhsSlope;
            rhsSlope = that.getSlope();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "slope", lhsSlope), LocatorUtils.property(thatLocator, "slope", rhsSlope), lhsSlope, rhsSlope)) {
                return false;
            }
        }
        {
            TridasSoil lhsSoil;
            lhsSoil = this.getSoil();
            TridasSoil rhsSoil;
            rhsSoil = that.getSoil();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "soil", lhsSoil), LocatorUtils.property(thatLocator, "soil", rhsSoil), lhsSoil, rhsSoil)) {
                return false;
            }
        }
        {
            TridasBedrock lhsBedrock;
            lhsBedrock = this.getBedrock();
            TridasBedrock rhsBedrock;
            rhsBedrock = that.getBedrock();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "bedrock", lhsBedrock), LocatorUtils.property(thatLocator, "bedrock", rhsBedrock), lhsBedrock, rhsBedrock)) {
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
            ControlledVoc theTaxon;
            theTaxon = this.getTaxon();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "taxon", theTaxon), currentHashCode, theTaxon);
        }
        {
            TridasShape theShape;
            theShape = this.getShape();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "shape", theShape), currentHashCode, theShape);
        }
        {
            TridasDimensions theDimensions;
            theDimensions = this.getDimensions();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dimensions", theDimensions), currentHashCode, theDimensions);
        }
        {
            String theAuthenticity;
            theAuthenticity = this.getAuthenticity();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "authenticity", theAuthenticity), currentHashCode, theAuthenticity);
        }
        {
            TridasLocation theLocation;
            theLocation = this.getLocation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "location", theLocation), currentHashCode, theLocation);
        }
        {
            String theProcessing;
            theProcessing = this.getProcessing();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "processing", theProcessing), currentHashCode, theProcessing);
        }
        {
            String theMarks;
            theMarks = this.getMarks();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "marks", theMarks), currentHashCode, theMarks);
        }
        {
            Double theAltitude;
            theAltitude = this.getAltitude();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "altitude", theAltitude), currentHashCode, theAltitude);
        }
        {
            TridasSlope theSlope;
            theSlope = this.getSlope();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "slope", theSlope), currentHashCode, theSlope);
        }
        {
            TridasSoil theSoil;
            theSoil = this.getSoil();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "soil", theSoil), currentHashCode, theSoil);
        }
        {
            TridasBedrock theBedrock;
            theBedrock = this.getBedrock();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "bedrock", theBedrock), currentHashCode, theBedrock);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "genericFields", theGenericFields), currentHashCode, theGenericFields);
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
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof TridasElement) {
            final TridasElement copy = ((TridasElement) draftCopy);
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
            if (this.isSetTaxon()) {
                ControlledVoc sourceTaxon;
                sourceTaxon = this.getTaxon();
                ControlledVoc copyTaxon = ((ControlledVoc) strategy.copy(LocatorUtils.property(locator, "taxon", sourceTaxon), sourceTaxon));
                copy.setTaxon(copyTaxon);
            } else {
                copy.taxon = null;
            }
            if (this.isSetShape()) {
                TridasShape sourceShape;
                sourceShape = this.getShape();
                TridasShape copyShape = ((TridasShape) strategy.copy(LocatorUtils.property(locator, "shape", sourceShape), sourceShape));
                copy.setShape(copyShape);
            } else {
                copy.shape = null;
            }
            if (this.isSetDimensions()) {
                TridasDimensions sourceDimensions;
                sourceDimensions = this.getDimensions();
                TridasDimensions copyDimensions = ((TridasDimensions) strategy.copy(LocatorUtils.property(locator, "dimensions", sourceDimensions), sourceDimensions));
                copy.setDimensions(copyDimensions);
            } else {
                copy.dimensions = null;
            }
            if (this.isSetAuthenticity()) {
                String sourceAuthenticity;
                sourceAuthenticity = this.getAuthenticity();
                String copyAuthenticity = ((String) strategy.copy(LocatorUtils.property(locator, "authenticity", sourceAuthenticity), sourceAuthenticity));
                copy.setAuthenticity(copyAuthenticity);
            } else {
                copy.authenticity = null;
            }
            if (this.isSetLocation()) {
                TridasLocation sourceLocation;
                sourceLocation = this.getLocation();
                TridasLocation copyLocation = ((TridasLocation) strategy.copy(LocatorUtils.property(locator, "location", sourceLocation), sourceLocation));
                copy.setLocation(copyLocation);
            } else {
                copy.location = null;
            }
            if (this.isSetProcessing()) {
                String sourceProcessing;
                sourceProcessing = this.getProcessing();
                String copyProcessing = ((String) strategy.copy(LocatorUtils.property(locator, "processing", sourceProcessing), sourceProcessing));
                copy.setProcessing(copyProcessing);
            } else {
                copy.processing = null;
            }
            if (this.isSetMarks()) {
                String sourceMarks;
                sourceMarks = this.getMarks();
                String copyMarks = ((String) strategy.copy(LocatorUtils.property(locator, "marks", sourceMarks), sourceMarks));
                copy.setMarks(copyMarks);
            } else {
                copy.marks = null;
            }
            if (this.isSetAltitude()) {
                Double sourceAltitude;
                sourceAltitude = this.getAltitude();
                Double copyAltitude = ((Double) strategy.copy(LocatorUtils.property(locator, "altitude", sourceAltitude), sourceAltitude));
                copy.setAltitude(copyAltitude);
            } else {
                copy.altitude = null;
            }
            if (this.isSetSlope()) {
                TridasSlope sourceSlope;
                sourceSlope = this.getSlope();
                TridasSlope copySlope = ((TridasSlope) strategy.copy(LocatorUtils.property(locator, "slope", sourceSlope), sourceSlope));
                copy.setSlope(copySlope);
            } else {
                copy.slope = null;
            }
            if (this.isSetSoil()) {
                TridasSoil sourceSoil;
                sourceSoil = this.getSoil();
                TridasSoil copySoil = ((TridasSoil) strategy.copy(LocatorUtils.property(locator, "soil", sourceSoil), sourceSoil));
                copy.setSoil(copySoil);
            } else {
                copy.soil = null;
            }
            if (this.isSetBedrock()) {
                TridasBedrock sourceBedrock;
                sourceBedrock = this.getBedrock();
                TridasBedrock copyBedrock = ((TridasBedrock) strategy.copy(LocatorUtils.property(locator, "bedrock", sourceBedrock), sourceBedrock));
                copy.setBedrock(copyBedrock);
            } else {
                copy.bedrock = null;
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
        return new TridasElement();
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

}
