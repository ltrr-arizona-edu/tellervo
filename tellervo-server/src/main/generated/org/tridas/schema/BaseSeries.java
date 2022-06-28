
package org.tridas.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
 * 
 * 				A base type inherited by measurementSeries and derivedSeries.
 * 			
 * 
 * <p>Java class for baseSeries complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseSeries">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tridas.org/1.2.2}tridasEntity">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measuringDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}derivationDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}woodCompleteness" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}analyst" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}dendrochronologist" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measuringMethod" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}type" minOccurs="0"/>
 *         &lt;element name="linkSeries" type="{http://www.tridas.org/1.2.2}seriesLinks" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}objective" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}standardizingMethod" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}author" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}version" minOccurs="0"/>
 *         &lt;group ref="{http://www.tridas.org/1.2.2}interpretationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}location" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}values" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseSeries", propOrder = {
    "measuringDate",
    "derivationDate",
    "woodCompleteness",
    "analyst",
    "dendrochronologist",
    "measuringMethod",
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
public abstract class BaseSeries
    extends TridasEntity
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected Date measuringDate;
    protected Date derivationDate;
    protected TridasWoodCompleteness woodCompleteness;
    protected String analyst;
    protected String dendrochronologist;
    protected TridasMeasuringMethod measuringMethod;
    protected ControlledVoc type;
    protected SeriesLinks linkSeries;
    protected String objective;
    protected String standardizingMethod;
    protected String author;
    protected String version;
    protected TridasInterpretationUnsolved interpretationUnsolved;
    protected TridasInterpretation interpretation;
    protected TridasLocation location;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected List<TridasValues> values;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

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
            Date theMeasuringDate;
            theMeasuringDate = this.getMeasuringDate();
            strategy.appendField(locator, this, "measuringDate", buffer, theMeasuringDate);
        }
        {
            Date theDerivationDate;
            theDerivationDate = this.getDerivationDate();
            strategy.appendField(locator, this, "derivationDate", buffer, theDerivationDate);
        }
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            strategy.appendField(locator, this, "woodCompleteness", buffer, theWoodCompleteness);
        }
        {
            String theAnalyst;
            theAnalyst = this.getAnalyst();
            strategy.appendField(locator, this, "analyst", buffer, theAnalyst);
        }
        {
            String theDendrochronologist;
            theDendrochronologist = this.getDendrochronologist();
            strategy.appendField(locator, this, "dendrochronologist", buffer, theDendrochronologist);
        }
        {
            TridasMeasuringMethod theMeasuringMethod;
            theMeasuringMethod = this.getMeasuringMethod();
            strategy.appendField(locator, this, "measuringMethod", buffer, theMeasuringMethod);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            strategy.appendField(locator, this, "type", buffer, theType);
        }
        {
            SeriesLinks theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            strategy.appendField(locator, this, "linkSeries", buffer, theLinkSeries);
        }
        {
            String theObjective;
            theObjective = this.getObjective();
            strategy.appendField(locator, this, "objective", buffer, theObjective);
        }
        {
            String theStandardizingMethod;
            theStandardizingMethod = this.getStandardizingMethod();
            strategy.appendField(locator, this, "standardizingMethod", buffer, theStandardizingMethod);
        }
        {
            String theAuthor;
            theAuthor = this.getAuthor();
            strategy.appendField(locator, this, "author", buffer, theAuthor);
        }
        {
            String theVersion;
            theVersion = this.getVersion();
            strategy.appendField(locator, this, "version", buffer, theVersion);
        }
        {
            TridasInterpretationUnsolved theInterpretationUnsolved;
            theInterpretationUnsolved = this.getInterpretationUnsolved();
            strategy.appendField(locator, this, "interpretationUnsolved", buffer, theInterpretationUnsolved);
        }
        {
            TridasInterpretation theInterpretation;
            theInterpretation = this.getInterpretation();
            strategy.appendField(locator, this, "interpretation", buffer, theInterpretation);
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
            List<TridasValues> theValues;
            theValues = (this.isSetValues()?this.getValues():null);
            strategy.appendField(locator, this, "values", buffer, theValues);
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof BaseSeries)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final BaseSeries that = ((BaseSeries) object);
        {
            Date lhsMeasuringDate;
            lhsMeasuringDate = this.getMeasuringDate();
            Date rhsMeasuringDate;
            rhsMeasuringDate = that.getMeasuringDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "measuringDate", lhsMeasuringDate), LocatorUtils.property(thatLocator, "measuringDate", rhsMeasuringDate), lhsMeasuringDate, rhsMeasuringDate)) {
                return false;
            }
        }
        {
            Date lhsDerivationDate;
            lhsDerivationDate = this.getDerivationDate();
            Date rhsDerivationDate;
            rhsDerivationDate = that.getDerivationDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "derivationDate", lhsDerivationDate), LocatorUtils.property(thatLocator, "derivationDate", rhsDerivationDate), lhsDerivationDate, rhsDerivationDate)) {
                return false;
            }
        }
        {
            TridasWoodCompleteness lhsWoodCompleteness;
            lhsWoodCompleteness = this.getWoodCompleteness();
            TridasWoodCompleteness rhsWoodCompleteness;
            rhsWoodCompleteness = that.getWoodCompleteness();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "woodCompleteness", lhsWoodCompleteness), LocatorUtils.property(thatLocator, "woodCompleteness", rhsWoodCompleteness), lhsWoodCompleteness, rhsWoodCompleteness)) {
                return false;
            }
        }
        {
            String lhsAnalyst;
            lhsAnalyst = this.getAnalyst();
            String rhsAnalyst;
            rhsAnalyst = that.getAnalyst();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "analyst", lhsAnalyst), LocatorUtils.property(thatLocator, "analyst", rhsAnalyst), lhsAnalyst, rhsAnalyst)) {
                return false;
            }
        }
        {
            String lhsDendrochronologist;
            lhsDendrochronologist = this.getDendrochronologist();
            String rhsDendrochronologist;
            rhsDendrochronologist = that.getDendrochronologist();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dendrochronologist", lhsDendrochronologist), LocatorUtils.property(thatLocator, "dendrochronologist", rhsDendrochronologist), lhsDendrochronologist, rhsDendrochronologist)) {
                return false;
            }
        }
        {
            TridasMeasuringMethod lhsMeasuringMethod;
            lhsMeasuringMethod = this.getMeasuringMethod();
            TridasMeasuringMethod rhsMeasuringMethod;
            rhsMeasuringMethod = that.getMeasuringMethod();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "measuringMethod", lhsMeasuringMethod), LocatorUtils.property(thatLocator, "measuringMethod", rhsMeasuringMethod), lhsMeasuringMethod, rhsMeasuringMethod)) {
                return false;
            }
        }
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
            SeriesLinks lhsLinkSeries;
            lhsLinkSeries = this.getLinkSeries();
            SeriesLinks rhsLinkSeries;
            rhsLinkSeries = that.getLinkSeries();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "linkSeries", lhsLinkSeries), LocatorUtils.property(thatLocator, "linkSeries", rhsLinkSeries), lhsLinkSeries, rhsLinkSeries)) {
                return false;
            }
        }
        {
            String lhsObjective;
            lhsObjective = this.getObjective();
            String rhsObjective;
            rhsObjective = that.getObjective();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "objective", lhsObjective), LocatorUtils.property(thatLocator, "objective", rhsObjective), lhsObjective, rhsObjective)) {
                return false;
            }
        }
        {
            String lhsStandardizingMethod;
            lhsStandardizingMethod = this.getStandardizingMethod();
            String rhsStandardizingMethod;
            rhsStandardizingMethod = that.getStandardizingMethod();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "standardizingMethod", lhsStandardizingMethod), LocatorUtils.property(thatLocator, "standardizingMethod", rhsStandardizingMethod), lhsStandardizingMethod, rhsStandardizingMethod)) {
                return false;
            }
        }
        {
            String lhsAuthor;
            lhsAuthor = this.getAuthor();
            String rhsAuthor;
            rhsAuthor = that.getAuthor();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "author", lhsAuthor), LocatorUtils.property(thatLocator, "author", rhsAuthor), lhsAuthor, rhsAuthor)) {
                return false;
            }
        }
        {
            String lhsVersion;
            lhsVersion = this.getVersion();
            String rhsVersion;
            rhsVersion = that.getVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "version", lhsVersion), LocatorUtils.property(thatLocator, "version", rhsVersion), lhsVersion, rhsVersion)) {
                return false;
            }
        }
        {
            TridasInterpretationUnsolved lhsInterpretationUnsolved;
            lhsInterpretationUnsolved = this.getInterpretationUnsolved();
            TridasInterpretationUnsolved rhsInterpretationUnsolved;
            rhsInterpretationUnsolved = that.getInterpretationUnsolved();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "interpretationUnsolved", lhsInterpretationUnsolved), LocatorUtils.property(thatLocator, "interpretationUnsolved", rhsInterpretationUnsolved), lhsInterpretationUnsolved, rhsInterpretationUnsolved)) {
                return false;
            }
        }
        {
            TridasInterpretation lhsInterpretation;
            lhsInterpretation = this.getInterpretation();
            TridasInterpretation rhsInterpretation;
            rhsInterpretation = that.getInterpretation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "interpretation", lhsInterpretation), LocatorUtils.property(thatLocator, "interpretation", rhsInterpretation), lhsInterpretation, rhsInterpretation)) {
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
            List<TridasValues> lhsValues;
            lhsValues = (this.isSetValues()?this.getValues():null);
            List<TridasValues> rhsValues;
            rhsValues = (that.isSetValues()?that.getValues():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "values", lhsValues), LocatorUtils.property(thatLocator, "values", rhsValues), lhsValues, rhsValues)) {
                return false;
            }
        }
        {
            String lhsId;
            lhsId = this.getId();
            String rhsId;
            rhsId = that.getId();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "id", lhsId), LocatorUtils.property(thatLocator, "id", rhsId), lhsId, rhsId)) {
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
            Date theMeasuringDate;
            theMeasuringDate = this.getMeasuringDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measuringDate", theMeasuringDate), currentHashCode, theMeasuringDate);
        }
        {
            Date theDerivationDate;
            theDerivationDate = this.getDerivationDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "derivationDate", theDerivationDate), currentHashCode, theDerivationDate);
        }
        {
            TridasWoodCompleteness theWoodCompleteness;
            theWoodCompleteness = this.getWoodCompleteness();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "woodCompleteness", theWoodCompleteness), currentHashCode, theWoodCompleteness);
        }
        {
            String theAnalyst;
            theAnalyst = this.getAnalyst();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "analyst", theAnalyst), currentHashCode, theAnalyst);
        }
        {
            String theDendrochronologist;
            theDendrochronologist = this.getDendrochronologist();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dendrochronologist", theDendrochronologist), currentHashCode, theDendrochronologist);
        }
        {
            TridasMeasuringMethod theMeasuringMethod;
            theMeasuringMethod = this.getMeasuringMethod();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measuringMethod", theMeasuringMethod), currentHashCode, theMeasuringMethod);
        }
        {
            ControlledVoc theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        {
            SeriesLinks theLinkSeries;
            theLinkSeries = this.getLinkSeries();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "linkSeries", theLinkSeries), currentHashCode, theLinkSeries);
        }
        {
            String theObjective;
            theObjective = this.getObjective();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "objective", theObjective), currentHashCode, theObjective);
        }
        {
            String theStandardizingMethod;
            theStandardizingMethod = this.getStandardizingMethod();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "standardizingMethod", theStandardizingMethod), currentHashCode, theStandardizingMethod);
        }
        {
            String theAuthor;
            theAuthor = this.getAuthor();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "author", theAuthor), currentHashCode, theAuthor);
        }
        {
            String theVersion;
            theVersion = this.getVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "version", theVersion), currentHashCode, theVersion);
        }
        {
            TridasInterpretationUnsolved theInterpretationUnsolved;
            theInterpretationUnsolved = this.getInterpretationUnsolved();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "interpretationUnsolved", theInterpretationUnsolved), currentHashCode, theInterpretationUnsolved);
        }
        {
            TridasInterpretation theInterpretation;
            theInterpretation = this.getInterpretation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "interpretation", theInterpretation), currentHashCode, theInterpretation);
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
            List<TridasValues> theValues;
            theValues = (this.isSetValues()?this.getValues():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "values", theValues), currentHashCode, theValues);
        }
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
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
        if (null == target) {
            throw new IllegalArgumentException("Target argument must not be null for abstract copyable classes.");
        }
        super.copyTo(locator, target, strategy);
        if (target instanceof BaseSeries) {
            final BaseSeries copy = ((BaseSeries) target);
            if (this.isSetMeasuringDate()) {
                Date sourceMeasuringDate;
                sourceMeasuringDate = this.getMeasuringDate();
                Date copyMeasuringDate = ((Date) strategy.copy(LocatorUtils.property(locator, "measuringDate", sourceMeasuringDate), sourceMeasuringDate));
                copy.setMeasuringDate(copyMeasuringDate);
            } else {
                copy.measuringDate = null;
            }
            if (this.isSetDerivationDate()) {
                Date sourceDerivationDate;
                sourceDerivationDate = this.getDerivationDate();
                Date copyDerivationDate = ((Date) strategy.copy(LocatorUtils.property(locator, "derivationDate", sourceDerivationDate), sourceDerivationDate));
                copy.setDerivationDate(copyDerivationDate);
            } else {
                copy.derivationDate = null;
            }
            if (this.isSetWoodCompleteness()) {
                TridasWoodCompleteness sourceWoodCompleteness;
                sourceWoodCompleteness = this.getWoodCompleteness();
                TridasWoodCompleteness copyWoodCompleteness = ((TridasWoodCompleteness) strategy.copy(LocatorUtils.property(locator, "woodCompleteness", sourceWoodCompleteness), sourceWoodCompleteness));
                copy.setWoodCompleteness(copyWoodCompleteness);
            } else {
                copy.woodCompleteness = null;
            }
            if (this.isSetAnalyst()) {
                String sourceAnalyst;
                sourceAnalyst = this.getAnalyst();
                String copyAnalyst = ((String) strategy.copy(LocatorUtils.property(locator, "analyst", sourceAnalyst), sourceAnalyst));
                copy.setAnalyst(copyAnalyst);
            } else {
                copy.analyst = null;
            }
            if (this.isSetDendrochronologist()) {
                String sourceDendrochronologist;
                sourceDendrochronologist = this.getDendrochronologist();
                String copyDendrochronologist = ((String) strategy.copy(LocatorUtils.property(locator, "dendrochronologist", sourceDendrochronologist), sourceDendrochronologist));
                copy.setDendrochronologist(copyDendrochronologist);
            } else {
                copy.dendrochronologist = null;
            }
            if (this.isSetMeasuringMethod()) {
                TridasMeasuringMethod sourceMeasuringMethod;
                sourceMeasuringMethod = this.getMeasuringMethod();
                TridasMeasuringMethod copyMeasuringMethod = ((TridasMeasuringMethod) strategy.copy(LocatorUtils.property(locator, "measuringMethod", sourceMeasuringMethod), sourceMeasuringMethod));
                copy.setMeasuringMethod(copyMeasuringMethod);
            } else {
                copy.measuringMethod = null;
            }
            if (this.isSetType()) {
                ControlledVoc sourceType;
                sourceType = this.getType();
                ControlledVoc copyType = ((ControlledVoc) strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
                copy.setType(copyType);
            } else {
                copy.type = null;
            }
            if (this.isSetLinkSeries()) {
                SeriesLinks sourceLinkSeries;
                sourceLinkSeries = this.getLinkSeries();
                SeriesLinks copyLinkSeries = ((SeriesLinks) strategy.copy(LocatorUtils.property(locator, "linkSeries", sourceLinkSeries), sourceLinkSeries));
                copy.setLinkSeries(copyLinkSeries);
            } else {
                copy.linkSeries = null;
            }
            if (this.isSetObjective()) {
                String sourceObjective;
                sourceObjective = this.getObjective();
                String copyObjective = ((String) strategy.copy(LocatorUtils.property(locator, "objective", sourceObjective), sourceObjective));
                copy.setObjective(copyObjective);
            } else {
                copy.objective = null;
            }
            if (this.isSetStandardizingMethod()) {
                String sourceStandardizingMethod;
                sourceStandardizingMethod = this.getStandardizingMethod();
                String copyStandardizingMethod = ((String) strategy.copy(LocatorUtils.property(locator, "standardizingMethod", sourceStandardizingMethod), sourceStandardizingMethod));
                copy.setStandardizingMethod(copyStandardizingMethod);
            } else {
                copy.standardizingMethod = null;
            }
            if (this.isSetAuthor()) {
                String sourceAuthor;
                sourceAuthor = this.getAuthor();
                String copyAuthor = ((String) strategy.copy(LocatorUtils.property(locator, "author", sourceAuthor), sourceAuthor));
                copy.setAuthor(copyAuthor);
            } else {
                copy.author = null;
            }
            if (this.isSetVersion()) {
                String sourceVersion;
                sourceVersion = this.getVersion();
                String copyVersion = ((String) strategy.copy(LocatorUtils.property(locator, "version", sourceVersion), sourceVersion));
                copy.setVersion(copyVersion);
            } else {
                copy.version = null;
            }
            if (this.isSetInterpretationUnsolved()) {
                TridasInterpretationUnsolved sourceInterpretationUnsolved;
                sourceInterpretationUnsolved = this.getInterpretationUnsolved();
                TridasInterpretationUnsolved copyInterpretationUnsolved = ((TridasInterpretationUnsolved) strategy.copy(LocatorUtils.property(locator, "interpretationUnsolved", sourceInterpretationUnsolved), sourceInterpretationUnsolved));
                copy.setInterpretationUnsolved(copyInterpretationUnsolved);
            } else {
                copy.interpretationUnsolved = null;
            }
            if (this.isSetInterpretation()) {
                TridasInterpretation sourceInterpretation;
                sourceInterpretation = this.getInterpretation();
                TridasInterpretation copyInterpretation = ((TridasInterpretation) strategy.copy(LocatorUtils.property(locator, "interpretation", sourceInterpretation), sourceInterpretation));
                copy.setInterpretation(copyInterpretation);
            } else {
                copy.interpretation = null;
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
            if (this.isSetValues()) {
                List<TridasValues> sourceValues;
                sourceValues = (this.isSetValues()?this.getValues():null);
                @SuppressWarnings("unchecked")
                List<TridasValues> copyValues = ((List<TridasValues> ) strategy.copy(LocatorUtils.property(locator, "values", sourceValues), sourceValues));
                copy.unsetValues();
                List<TridasValues> uniqueValuesl = copy.getValues();
                uniqueValuesl.addAll(copyValues);
            } else {
                copy.unsetValues();
            }
            if (this.isSetId()) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
        }
        return target;
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

}
