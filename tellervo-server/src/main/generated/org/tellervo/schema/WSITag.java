
package org.tellervo.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
import org.tridas.interfaces.ICoreTridas;
import org.tridas.schema.TridasIdentifier;


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
 *         &lt;element name="assignedTo" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="measurementSeries" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ownerid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "assignedTo"
})
@XmlRootElement(name = "tag")
public class WSITag implements Cloneable, CopyTo, Equals, HashCode, ToString, ICoreTridas
{

    protected WSITag.AssignedTo assignedTo;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "ownerid")
    protected String ownerid;
    @XmlAttribute(name = "value", required = true)
    protected String value;

    /**
     * Gets the value of the assignedTo property.
     * 
     * @return
     *     possible object is
     *     {@link WSITag.AssignedTo }
     *     
     */
    public WSITag.AssignedTo getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the value of the assignedTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSITag.AssignedTo }
     *     
     */
    public void setAssignedTo(WSITag.AssignedTo value) {
        this.assignedTo = value;
    }

    public boolean isSetAssignedTo() {
        return (this.assignedTo!= null);
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
     * Gets the value of the ownerid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerid() {
        return ownerid;
    }

    /**
     * Sets the value of the ownerid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerid(String value) {
        this.ownerid = value;
    }

    public boolean isSetOwnerid() {
        return (this.ownerid!= null);
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value!= null);
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
            WSITag.AssignedTo theAssignedTo;
            theAssignedTo = this.getAssignedTo();
            strategy.appendField(locator, this, "assignedTo", buffer, theAssignedTo);
        }
        {
            String theId;
            theId = this.getId();
            strategy.appendField(locator, this, "id", buffer, theId);
        }
        {
            String theOwnerid;
            theOwnerid = this.getOwnerid();
            strategy.appendField(locator, this, "ownerid", buffer, theOwnerid);
        }
        {
            String theValue;
            theValue = this.getValue();
            strategy.appendField(locator, this, "value", buffer, theValue);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSITag)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSITag that = ((WSITag) object);
        {
            WSITag.AssignedTo lhsAssignedTo;
            lhsAssignedTo = this.getAssignedTo();
            WSITag.AssignedTo rhsAssignedTo;
            rhsAssignedTo = that.getAssignedTo();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "assignedTo", lhsAssignedTo), LocatorUtils.property(thatLocator, "assignedTo", rhsAssignedTo), lhsAssignedTo, rhsAssignedTo)) {
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
        {
            String lhsOwnerid;
            lhsOwnerid = this.getOwnerid();
            String rhsOwnerid;
            rhsOwnerid = that.getOwnerid();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "ownerid", lhsOwnerid), LocatorUtils.property(thatLocator, "ownerid", rhsOwnerid), lhsOwnerid, rhsOwnerid)) {
                return false;
            }
        }
        {
            String lhsValue;
            lhsValue = this.getValue();
            String rhsValue;
            rhsValue = that.getValue();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
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
            WSITag.AssignedTo theAssignedTo;
            theAssignedTo = this.getAssignedTo();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "assignedTo", theAssignedTo), currentHashCode, theAssignedTo);
        }
        {
            String theId;
            theId = this.getId();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "id", theId), currentHashCode, theId);
        }
        {
            String theOwnerid;
            theOwnerid = this.getOwnerid();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "ownerid", theOwnerid), currentHashCode, theOwnerid);
        }
        {
            String theValue;
            theValue = this.getValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
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
        if (draftCopy instanceof WSITag) {
            final WSITag copy = ((WSITag) draftCopy);
            if (this.isSetAssignedTo()) {
                WSITag.AssignedTo sourceAssignedTo;
                sourceAssignedTo = this.getAssignedTo();
                WSITag.AssignedTo copyAssignedTo = ((WSITag.AssignedTo) strategy.copy(LocatorUtils.property(locator, "assignedTo", sourceAssignedTo), sourceAssignedTo));
                copy.setAssignedTo(copyAssignedTo);
            } else {
                copy.assignedTo = null;
            }
            if (this.isSetId()) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) strategy.copy(LocatorUtils.property(locator, "id", sourceId), sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.isSetOwnerid()) {
                String sourceOwnerid;
                sourceOwnerid = this.getOwnerid();
                String copyOwnerid = ((String) strategy.copy(LocatorUtils.property(locator, "ownerid", sourceOwnerid), sourceOwnerid));
                copy.setOwnerid(copyOwnerid);
            } else {
                copy.ownerid = null;
            }
            if (this.isSetValue()) {
                String sourceValue;
                sourceValue = this.getValue();
                String copyValue = ((String) strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue));
                copy.setValue(copyValue);
            } else {
                copy.value = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSITag();
    }


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
     *         &lt;element name="measurementSeries" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "measurementSeries"
    })
    public static class AssignedTo
        implements Cloneable, CopyTo, Equals, HashCode, ToString
    {

        @XmlElement(required = true)
        protected List<WSITag.AssignedTo.MeasurementSeries> measurementSeries;

        /**
         * Gets the value of the measurementSeries property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the measurementSeries property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMeasurementSeries().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WSITag.AssignedTo.MeasurementSeries }
         * 
         * 
         */
        public List<WSITag.AssignedTo.MeasurementSeries> getMeasurementSeries() {
            if (measurementSeries == null) {
                measurementSeries = new ArrayList<WSITag.AssignedTo.MeasurementSeries>();
            }
            return this.measurementSeries;
        }

        public boolean isSetMeasurementSeries() {
            return ((this.measurementSeries!= null)&&(!this.measurementSeries.isEmpty()));
        }

        public void unsetMeasurementSeries() {
            this.measurementSeries = null;
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
                List<WSITag.AssignedTo.MeasurementSeries> theMeasurementSeries;
                theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
                strategy.appendField(locator, this, "measurementSeries", buffer, theMeasurementSeries);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSITag.AssignedTo)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final WSITag.AssignedTo that = ((WSITag.AssignedTo) object);
            {
                List<WSITag.AssignedTo.MeasurementSeries> lhsMeasurementSeries;
                lhsMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
                List<WSITag.AssignedTo.MeasurementSeries> rhsMeasurementSeries;
                rhsMeasurementSeries = (that.isSetMeasurementSeries()?that.getMeasurementSeries():null);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "measurementSeries", lhsMeasurementSeries), LocatorUtils.property(thatLocator, "measurementSeries", rhsMeasurementSeries), lhsMeasurementSeries, rhsMeasurementSeries)) {
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
                List<WSITag.AssignedTo.MeasurementSeries> theMeasurementSeries;
                theMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "measurementSeries", theMeasurementSeries), currentHashCode, theMeasurementSeries);
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
            if (draftCopy instanceof WSITag.AssignedTo) {
                final WSITag.AssignedTo copy = ((WSITag.AssignedTo) draftCopy);
                if (this.isSetMeasurementSeries()) {
                    List<WSITag.AssignedTo.MeasurementSeries> sourceMeasurementSeries;
                    sourceMeasurementSeries = (this.isSetMeasurementSeries()?this.getMeasurementSeries():null);
                    @SuppressWarnings("unchecked")
                    List<WSITag.AssignedTo.MeasurementSeries> copyMeasurementSeries = ((List<WSITag.AssignedTo.MeasurementSeries> ) strategy.copy(LocatorUtils.property(locator, "measurementSeries", sourceMeasurementSeries), sourceMeasurementSeries));
                    copy.unsetMeasurementSeries();
                    List<WSITag.AssignedTo.MeasurementSeries> uniqueMeasurementSeriesl = copy.getMeasurementSeries();
                    uniqueMeasurementSeriesl.addAll(copyMeasurementSeries);
                } else {
                    copy.unsetMeasurementSeries();
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new WSITag.AssignedTo();
        }

        /**
         * Sets the value of the measurementSeries property.
         * 
         * @param measurementSeries
         *     allowed object is
         *     {@link WSITag.AssignedTo.MeasurementSeries }
         *     
         */
        public void setMeasurementSeries(List<WSITag.AssignedTo.MeasurementSeries> measurementSeries) {
            this.measurementSeries = measurementSeries;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class MeasurementSeries
            implements Cloneable, CopyTo, Equals, HashCode, ToString
        {

            @XmlAttribute(name = "id", required = true)
            protected String id;

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
                    String theId;
                    theId = this.getId();
                    strategy.appendField(locator, this, "id", buffer, theId);
                }
                return buffer;
            }

            public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
                if (!(object instanceof WSITag.AssignedTo.MeasurementSeries)) {
                    return false;
                }
                if (this == object) {
                    return true;
                }
                final WSITag.AssignedTo.MeasurementSeries that = ((WSITag.AssignedTo.MeasurementSeries) object);
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
                if (draftCopy instanceof WSITag.AssignedTo.MeasurementSeries) {
                    final WSITag.AssignedTo.MeasurementSeries copy = ((WSITag.AssignedTo.MeasurementSeries) draftCopy);
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
                return new WSITag.AssignedTo.MeasurementSeries();
            }

        }

    }


	public TridasIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setIdentifier(TridasIdentifier value) {
		// TODO Auto-generated method stub
		
	}

	public boolean isSetIdentifier() {
		// TODO Auto-generated method stub
		return false;
	}

}
