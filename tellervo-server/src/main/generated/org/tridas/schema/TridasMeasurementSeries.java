
package org.tridas.schema;

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
 *     &lt;restriction base="{http://www.tridas.org/1.2.2}baseSeries">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}comments" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measuringDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}woodCompleteness" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}analyst" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}dendrochronologist" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measuringMethod"/>
 *         &lt;group ref="{http://www.tridas.org/1.2.2}interpretationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}values" maxOccurs="unbounded" minOccurs="0"/>
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
public class TridasMeasurementSeries implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected String title;
    protected TridasIdentifier identifier;
    protected DateTime createdTimestamp;
    protected DateTime lastModifiedTimestamp;
    protected String comments;
    protected Date measuringDate;
    protected TridasWoodCompleteness woodCompleteness;
    protected String analyst;
    protected String dendrochronologist;
    @XmlElement(required = true)
    protected TridasMeasuringMethod measuringMethod;
    protected TridasInterpretationUnsolved interpretationUnsolved;
    protected TridasInterpretation interpretation;
    @XmlElement(name = "genericField")
    protected List<TridasGenericField> genericFields;
    protected List<TridasValues> values;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
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
            String theTitle;
            theTitle = this.getTitle();
            strategy.appendField(locator, this, "title", buffer, theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            strategy.appendField(locator, this, "identifier", buffer, theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            strategy.appendField(locator, this, "createdTimestamp", buffer, theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            strategy.appendField(locator, this, "lastModifiedTimestamp", buffer, theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            strategy.appendField(locator, this, "comments", buffer, theComments);
        }
        {
            Date theMeasuringDate;
            theMeasuringDate = this.getMeasuringDate();
            strategy.appendField(locator, this, "measuringDate", buffer, theMeasuringDate);
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
        if (!(object instanceof TridasMeasurementSeries)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasMeasurementSeries that = ((TridasMeasurementSeries) object);
        {
            String lhsTitle;
            lhsTitle = this.getTitle();
            String rhsTitle;
            rhsTitle = that.getTitle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "title", lhsTitle), LocatorUtils.property(thatLocator, "title", rhsTitle), lhsTitle, rhsTitle)) {
                return false;
            }
        }
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
            DateTime lhsCreatedTimestamp;
            lhsCreatedTimestamp = this.getCreatedTimestamp();
            DateTime rhsCreatedTimestamp;
            rhsCreatedTimestamp = that.getCreatedTimestamp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "createdTimestamp", lhsCreatedTimestamp), LocatorUtils.property(thatLocator, "createdTimestamp", rhsCreatedTimestamp), lhsCreatedTimestamp, rhsCreatedTimestamp)) {
                return false;
            }
        }
        {
            DateTime lhsLastModifiedTimestamp;
            lhsLastModifiedTimestamp = this.getLastModifiedTimestamp();
            DateTime rhsLastModifiedTimestamp;
            rhsLastModifiedTimestamp = that.getLastModifiedTimestamp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastModifiedTimestamp", lhsLastModifiedTimestamp), LocatorUtils.property(thatLocator, "lastModifiedTimestamp", rhsLastModifiedTimestamp), lhsLastModifiedTimestamp, rhsLastModifiedTimestamp)) {
                return false;
            }
        }
        {
            String lhsComments;
            lhsComments = this.getComments();
            String rhsComments;
            rhsComments = that.getComments();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "comments", lhsComments), LocatorUtils.property(thatLocator, "comments", rhsComments), lhsComments, rhsComments)) {
                return false;
            }
        }
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
        int currentHashCode = 1;
        {
            String theTitle;
            theTitle = this.getTitle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "title", theTitle), currentHashCode, theTitle);
        }
        {
            TridasIdentifier theIdentifier;
            theIdentifier = this.getIdentifier();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "identifier", theIdentifier), currentHashCode, theIdentifier);
        }
        {
            DateTime theCreatedTimestamp;
            theCreatedTimestamp = this.getCreatedTimestamp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "createdTimestamp", theCreatedTimestamp), currentHashCode, theCreatedTimestamp);
        }
        {
            DateTime theLastModifiedTimestamp;
            theLastModifiedTimestamp = this.getLastModifiedTimestamp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastModifiedTimestamp", theLastModifiedTimestamp), currentHashCode, theLastModifiedTimestamp);
        }
        {
            String theComments;
            theComments = this.getComments();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "comments", theComments), currentHashCode, theComments);
        }
        {
            Date theMeasuringDate;
            theMeasuringDate = this.getMeasuringDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measuringDate", theMeasuringDate), currentHashCode, theMeasuringDate);
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
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof TridasMeasurementSeries) {
            final TridasMeasurementSeries copy = ((TridasMeasurementSeries) draftCopy);
            if (this.isSetTitle()) {
                String sourceTitle;
                sourceTitle = this.getTitle();
                String copyTitle = ((String) strategy.copy(LocatorUtils.property(locator, "title", sourceTitle), sourceTitle));
                copy.setTitle(copyTitle);
            } else {
                copy.title = null;
            }
            if (this.isSetIdentifier()) {
                TridasIdentifier sourceIdentifier;
                sourceIdentifier = this.getIdentifier();
                TridasIdentifier copyIdentifier = ((TridasIdentifier) strategy.copy(LocatorUtils.property(locator, "identifier", sourceIdentifier), sourceIdentifier));
                copy.setIdentifier(copyIdentifier);
            } else {
                copy.identifier = null;
            }
            if (this.isSetCreatedTimestamp()) {
                DateTime sourceCreatedTimestamp;
                sourceCreatedTimestamp = this.getCreatedTimestamp();
                DateTime copyCreatedTimestamp = ((DateTime) strategy.copy(LocatorUtils.property(locator, "createdTimestamp", sourceCreatedTimestamp), sourceCreatedTimestamp));
                copy.setCreatedTimestamp(copyCreatedTimestamp);
            } else {
                copy.createdTimestamp = null;
            }
            if (this.isSetLastModifiedTimestamp()) {
                DateTime sourceLastModifiedTimestamp;
                sourceLastModifiedTimestamp = this.getLastModifiedTimestamp();
                DateTime copyLastModifiedTimestamp = ((DateTime) strategy.copy(LocatorUtils.property(locator, "lastModifiedTimestamp", sourceLastModifiedTimestamp), sourceLastModifiedTimestamp));
                copy.setLastModifiedTimestamp(copyLastModifiedTimestamp);
            } else {
                copy.lastModifiedTimestamp = null;
            }
            if (this.isSetComments()) {
                String sourceComments;
                sourceComments = this.getComments();
                String copyComments = ((String) strategy.copy(LocatorUtils.property(locator, "comments", sourceComments), sourceComments));
                copy.setComments(copyComments);
            } else {
                copy.comments = null;
            }
            if (this.isSetMeasuringDate()) {
                Date sourceMeasuringDate;
                sourceMeasuringDate = this.getMeasuringDate();
                Date copyMeasuringDate = ((Date) strategy.copy(LocatorUtils.property(locator, "measuringDate", sourceMeasuringDate), sourceMeasuringDate));
                copy.setMeasuringDate(copyMeasuringDate);
            } else {
                copy.measuringDate = null;
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
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasMeasurementSeries();
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
