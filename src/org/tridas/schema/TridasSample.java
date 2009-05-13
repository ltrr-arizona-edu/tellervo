//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.08 at 01:25:56 PM PDT 
//


package org.tridas.schema;

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
import org.apache.commons.lang.builder.ToStringStyle;


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
    "file",
    "samplingDate",
    "position",
    "state",
    "knots",
    "genericField",
    "radius",
    "radiusPlaceholder"
})
@XmlRootElement(name = "sample")
public class TridasSample
    extends TridasEntity
{

    @XmlElement(required = true)
    protected ControlledVoc type;
    protected String description;
    protected List<TridasFile> file;
    protected Date samplingDate;
    protected String position;
    protected String state;
    protected Boolean knots;
    protected List<TridasGenericField> genericField;
    protected List<TridasRadius> radius;
    protected TridasRadiusPlaceholder radiusPlaceholder;

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
     * Gets the value of the file property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the file property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasFile }
     * 
     * 
     */
    public List<TridasFile> getFile() {
        if (file == null) {
            file = new ArrayList<TridasFile>();
        }
        return this.file;
    }

    public boolean isSetFile() {
        return ((this.file!= null)&&(!this.file.isEmpty()));
    }

    public void unsetFile() {
        this.file = null;
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
     * Gets the value of the genericField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasGenericField }
     * 
     * 
     */
    public List<TridasGenericField> getGenericField() {
        if (genericField == null) {
            genericField = new ArrayList<TridasGenericField>();
        }
        return this.genericField;
    }

    public boolean isSetGenericField() {
        return ((this.genericField!= null)&&(!this.genericField.isEmpty()));
    }

    public void unsetGenericField() {
        this.genericField = null;
    }

    /**
     * Gets the value of the radius property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the radius property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRadius().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasRadius }
     * 
     * 
     */
    public List<TridasRadius> getRadius() {
        if (radius == null) {
            radius = new ArrayList<TridasRadius>();
        }
        return this.radius;
    }

    public boolean isSetRadius() {
        return ((this.radius!= null)&&(!this.radius.isEmpty()));
    }

    public void unsetRadius() {
        this.radius = null;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
