
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}type"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}samplingDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}position" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}state" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}knots" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}radius" maxOccurs="unbounded" minOccurs="0"/>
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
    "radiuses"
})
@XmlRootElement(name = "sample")
public class TridasSample
    extends TridasEntity
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(required = true)
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
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            strategy.appendField(locator, this, "files", buffer, theFiles);
        }
        {
            Date theSamplingDate;
            theSamplingDate = this.getSamplingDate();
            strategy.appendField(locator, this, "samplingDate", buffer, theSamplingDate);
        }
        {
            String thePosition;
            thePosition = this.getPosition();
            strategy.appendField(locator, this, "position", buffer, thePosition);
        }
        {
            String theState;
            theState = this.getState();
            strategy.appendField(locator, this, "state", buffer, theState);
        }
        {
            Boolean theKnots;
            theKnots = this.isKnots();
            strategy.appendField(locator, this, "knots", buffer, theKnots);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            strategy.appendField(locator, this, "genericFields", buffer, theGenericFields);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            strategy.appendField(locator, this, "radiuses", buffer, theRadiuses);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasSample)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final TridasSample that = ((TridasSample) object);
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
            List<TridasFile> lhsFiles;
            lhsFiles = (this.isSetFiles()?this.getFiles():null);
            List<TridasFile> rhsFiles;
            rhsFiles = (that.isSetFiles()?that.getFiles():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "files", lhsFiles), LocatorUtils.property(thatLocator, "files", rhsFiles), lhsFiles, rhsFiles)) {
                return false;
            }
        }
        {
            Date lhsSamplingDate;
            lhsSamplingDate = this.getSamplingDate();
            Date rhsSamplingDate;
            rhsSamplingDate = that.getSamplingDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "samplingDate", lhsSamplingDate), LocatorUtils.property(thatLocator, "samplingDate", rhsSamplingDate), lhsSamplingDate, rhsSamplingDate)) {
                return false;
            }
        }
        {
            String lhsPosition;
            lhsPosition = this.getPosition();
            String rhsPosition;
            rhsPosition = that.getPosition();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "position", lhsPosition), LocatorUtils.property(thatLocator, "position", rhsPosition), lhsPosition, rhsPosition)) {
                return false;
            }
        }
        {
            String lhsState;
            lhsState = this.getState();
            String rhsState;
            rhsState = that.getState();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "state", lhsState), LocatorUtils.property(thatLocator, "state", rhsState), lhsState, rhsState)) {
                return false;
            }
        }
        {
            Boolean lhsKnots;
            lhsKnots = this.isKnots();
            Boolean rhsKnots;
            rhsKnots = that.isKnots();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "knots", lhsKnots), LocatorUtils.property(thatLocator, "knots", rhsKnots), lhsKnots, rhsKnots)) {
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
            List<TridasRadius> lhsRadiuses;
            lhsRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            List<TridasRadius> rhsRadiuses;
            rhsRadiuses = (that.isSetRadiuses()?that.getRadiuses():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "radiuses", lhsRadiuses), LocatorUtils.property(thatLocator, "radiuses", rhsRadiuses), lhsRadiuses, rhsRadiuses)) {
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
            List<TridasFile> theFiles;
            theFiles = (this.isSetFiles()?this.getFiles():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "files", theFiles), currentHashCode, theFiles);
        }
        {
            Date theSamplingDate;
            theSamplingDate = this.getSamplingDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "samplingDate", theSamplingDate), currentHashCode, theSamplingDate);
        }
        {
            String thePosition;
            thePosition = this.getPosition();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "position", thePosition), currentHashCode, thePosition);
        }
        {
            String theState;
            theState = this.getState();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "state", theState), currentHashCode, theState);
        }
        {
            Boolean theKnots;
            theKnots = this.isKnots();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "knots", theKnots), currentHashCode, theKnots);
        }
        {
            List<TridasGenericField> theGenericFields;
            theGenericFields = (this.isSetGenericFields()?this.getGenericFields():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "genericFields", theGenericFields), currentHashCode, theGenericFields);
        }
        {
            List<TridasRadius> theRadiuses;
            theRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "radiuses", theRadiuses), currentHashCode, theRadiuses);
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
        if (draftCopy instanceof TridasSample) {
            final TridasSample copy = ((TridasSample) draftCopy);
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
            if (this.isSetSamplingDate()) {
                Date sourceSamplingDate;
                sourceSamplingDate = this.getSamplingDate();
                Date copySamplingDate = ((Date) strategy.copy(LocatorUtils.property(locator, "samplingDate", sourceSamplingDate), sourceSamplingDate));
                copy.setSamplingDate(copySamplingDate);
            } else {
                copy.samplingDate = null;
            }
            if (this.isSetPosition()) {
                String sourcePosition;
                sourcePosition = this.getPosition();
                String copyPosition = ((String) strategy.copy(LocatorUtils.property(locator, "position", sourcePosition), sourcePosition));
                copy.setPosition(copyPosition);
            } else {
                copy.position = null;
            }
            if (this.isSetState()) {
                String sourceState;
                sourceState = this.getState();
                String copyState = ((String) strategy.copy(LocatorUtils.property(locator, "state", sourceState), sourceState));
                copy.setState(copyState);
            } else {
                copy.state = null;
            }
            if (this.isSetKnots()) {
                Boolean sourceKnots;
                sourceKnots = this.isKnots();
                Boolean copyKnots = ((Boolean) strategy.copy(LocatorUtils.property(locator, "knots", sourceKnots), sourceKnots));
                copy.setKnots(copyKnots);
            } else {
                copy.knots = null;
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
            if (this.isSetRadiuses()) {
                List<TridasRadius> sourceRadiuses;
                sourceRadiuses = (this.isSetRadiuses()?this.getRadiuses():null);
                @SuppressWarnings("unchecked")
                List<TridasRadius> copyRadiuses = ((List<TridasRadius> ) strategy.copy(LocatorUtils.property(locator, "radiuses", sourceRadiuses), sourceRadiuses));
                copy.unsetRadiuses();
                List<TridasRadius> uniqueRadiusesl = copy.getRadiuses();
                uniqueRadiusesl.addAll(copyRadiuses);
            } else {
                copy.unsetRadiuses();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasSample();
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
     * Sets the value of the radiuses property.
     * 
     * @param radiuses
     *     allowed object is
     *     {@link TridasRadius }
     *     
     */
    public void setRadiuses(List<TridasRadius> radiuses) {
        this.radiuses = radiuses;
    }

}
