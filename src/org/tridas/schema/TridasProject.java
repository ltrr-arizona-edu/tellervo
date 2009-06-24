//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.24 at 01:09:38 PM PDT 
//


package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
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
 *         &lt;element ref="{http://www.tridas.org/1.2}type" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}file" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}laboratory" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}category"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}investigator"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}period"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}requestDate" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}commissioner" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}research" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
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
    "laboratory",
    "category",
    "investigator",
    "period",
    "requestDate",
    "commissioner",
    "reference",
    "research",
    "genericField",
    "object",
    "derivedSeries"
})
@XmlRootElement(name = "project")
public class TridasProject
    extends TridasEntity
    implements Serializable
{

    @XmlElement(required = true)
    protected List<ControlledVoc> type;
    protected String description;
    protected List<TridasFile> file;
    @XmlElement(required = true)
    protected List<TridasLaboratory> laboratory;
    @XmlElement(required = true)
    protected TridasCategory category;
    @XmlElement(required = true)
    protected String investigator;
    @XmlElement(required = true)
    protected String period;
    protected Date requestDate;
    protected String commissioner;
    protected List<String> reference;
    protected List<TridasResearch> research;
    protected List<TridasGenericField> genericField;
    @XmlElement(type = TridasObjectEx.class)
    protected List<TridasObject> object;
    protected List<TridasDerivedSeries> derivedSeries;

    /**
     * Gets the value of the type property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the type property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlledVoc }
     * 
     * 
     */
    public List<ControlledVoc> getType() {
        if (type == null) {
            type = new ArrayList<ControlledVoc>();
        }
        return this.type;
    }

    public boolean isSetType() {
        return ((this.type!= null)&&(!this.type.isEmpty()));
    }

    public void unsetType() {
        this.type = null;
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
     * Gets the value of the laboratory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the laboratory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLaboratory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasLaboratory }
     * 
     * 
     */
    public List<TridasLaboratory> getLaboratory() {
        if (laboratory == null) {
            laboratory = new ArrayList<TridasLaboratory>();
        }
        return this.laboratory;
    }

    public boolean isSetLaboratory() {
        return ((this.laboratory!= null)&&(!this.laboratory.isEmpty()));
    }

    public void unsetLaboratory() {
        this.laboratory = null;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link TridasCategory }
     *     
     */
    public TridasCategory getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasCategory }
     *     
     */
    public void setCategory(TridasCategory value) {
        this.category = value;
    }

    public boolean isSetCategory() {
        return (this.category!= null);
    }

    /**
     * Gets the value of the investigator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigator() {
        return investigator;
    }

    /**
     * Sets the value of the investigator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigator(String value) {
        this.investigator = value;
    }

    public boolean isSetInvestigator() {
        return (this.investigator!= null);
    }

    /**
     * Gets the value of the period property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriod() {
        return period;
    }

    /**
     * Sets the value of the period property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriod(String value) {
        this.period = value;
    }

    public boolean isSetPeriod() {
        return (this.period!= null);
    }

    /**
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setRequestDate(Date value) {
        this.requestDate = value;
    }

    public boolean isSetRequestDate() {
        return (this.requestDate!= null);
    }

    /**
     * Gets the value of the commissioner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommissioner() {
        return commissioner;
    }

    /**
     * Sets the value of the commissioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommissioner(String value) {
        this.commissioner = value;
    }

    public boolean isSetCommissioner() {
        return (this.commissioner!= null);
    }

    /**
     * Gets the value of the reference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReference() {
        if (reference == null) {
            reference = new ArrayList<String>();
        }
        return this.reference;
    }

    public boolean isSetReference() {
        return ((this.reference!= null)&&(!this.reference.isEmpty()));
    }

    public void unsetReference() {
        this.reference = null;
    }

    /**
     * Gets the value of the research property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the research property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResearch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasResearch }
     * 
     * 
     */
    public List<TridasResearch> getResearch() {
        if (research == null) {
            research = new ArrayList<TridasResearch>();
        }
        return this.research;
    }

    public boolean isSetResearch() {
        return ((this.research!= null)&&(!this.research.isEmpty()));
    }

    public void unsetResearch() {
        this.research = null;
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
     * Gets the value of the object property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the object property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasObject }
     * 
     * 
     */
    public List<TridasObject> getObject() {
        if (object == null) {
            object = new ArrayList<TridasObject>();
        }
        return this.object;
    }

    public boolean isSetObject() {
        return ((this.object!= null)&&(!this.object.isEmpty()));
    }

    public void unsetObject() {
        this.object = null;
    }

    /**
     * Gets the value of the derivedSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the derivedSeries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDerivedSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasDerivedSeries }
     * 
     * 
     */
    public List<TridasDerivedSeries> getDerivedSeries() {
        if (derivedSeries == null) {
            derivedSeries = new ArrayList<TridasDerivedSeries>();
        }
        return this.derivedSeries;
    }

    public boolean isSetDerivedSeries() {
        return ((this.derivedSeries!= null)&&(!this.derivedSeries.isEmpty()));
    }

    public void unsetDerivedSeries() {
        this.derivedSeries = null;
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
