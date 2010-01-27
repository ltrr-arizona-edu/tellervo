
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
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
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="sql" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}object" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}element" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}sample" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}radius" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}box" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}objectTypeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}elementTypeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}sampleTypeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}coverageTemporalDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}coverageTemporalFoundationDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}locationTypeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}elementAuthenticityDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}elementShapeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}sapwoodDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}heartwoodDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}measurementVariableDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}datingTypeDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}taxonDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}regionDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}readingNoteDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityUserDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}boxDictionary" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sqlsAndObjectsAndElements"
})
@XmlRootElement(name = "content")
public class WSIContent
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElements({
        @XmlElement(name = "element", namespace = "http://www.tridas.org/1.3", type = TridasElement.class),
        @XmlElement(name = "taxonDictionary", type = WSITaxonDictionary.class),
        @XmlElement(name = "objectTypeDictionary", type = WSIObjectTypeDictionary.class),
        @XmlElement(name = "heartwoodDictionary", type = WSIHeartwoodDictionary.class),
        @XmlElement(name = "sampleTypeDictionary", type = WSISampleTypeDictionary.class),
        @XmlElement(name = "securityUserDictionary", type = WSISecurityUserDictionary.class),
        @XmlElement(name = "locationTypeDictionary", type = WSILocationTypeDictionary.class),
        @XmlElement(name = "box", type = WSIBox.class),
        @XmlElement(name = "datingTypeDictionary", type = WSIDatingTypeDictionary.class),
        @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.3", type = TridasSample.class),
        @XmlElement(name = "elementAuthenticityDictionary", type = WSIElementAuthenticityDictionary.class),
        @XmlElement(name = "object", namespace = "http://www.tridas.org/1.3", type = TridasObjectEx.class),
        @XmlElement(name = "derivedSeries", namespace = "http://www.tridas.org/1.3", type = TridasDerivedSeries.class),
        @XmlElement(name = "elementShapeDictionary", type = WSIElementShapeDictionary.class),
        @XmlElement(name = "elementTypeDictionary", type = WSIElementTypeDictionary.class),
        @XmlElement(name = "measurementSeries", namespace = "http://www.tridas.org/1.3", type = TridasMeasurementSeries.class),
        @XmlElement(name = "measurementVariableDictionary", type = WSIMeasurementVariableDictionary.class),
        @XmlElement(name = "sapwoodDictionary", type = WSISapwoodDictionary.class),
        @XmlElement(name = "sql"),
        @XmlElement(name = "coverageTemporalDictionary", type = WSICoverageTemporalDictionary.class),
        @XmlElement(name = "readingNoteDictionary", type = WSIReadingNoteDictionary.class),
        @XmlElement(name = "radius", namespace = "http://www.tridas.org/1.3", type = TridasRadius.class),
        @XmlElement(name = "boxDictionary", type = WSIBoxDictionary.class),
        @XmlElement(name = "regionDictionary", type = WSIRegionDictionary.class),
        @XmlElement(name = "coverageTemporalFoundationDictionary", type = WSICoverageTemporalFoundationDictionary.class)
    })
    protected List<Object> sqlsAndObjectsAndElements;

    /**
     * Gets the value of the sqlsAndObjectsAndElements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sqlsAndObjectsAndElements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSqlsAndObjectsAndElements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasElement }
     * {@link WSITaxonDictionary }
     * {@link WSIObjectTypeDictionary }
     * {@link WSIHeartwoodDictionary }
     * {@link WSISampleTypeDictionary }
     * {@link WSISecurityUserDictionary }
     * {@link WSILocationTypeDictionary }
     * {@link WSIBox }
     * {@link WSIDatingTypeDictionary }
     * {@link TridasSample }
     * {@link WSIElementAuthenticityDictionary }
     * {@link TridasObject }
     * {@link TridasDerivedSeries }
     * {@link WSIElementShapeDictionary }
     * {@link WSIElementTypeDictionary }
     * {@link TridasMeasurementSeries }
     * {@link WSIMeasurementVariableDictionary }
     * {@link WSISapwoodDictionary }
     * {@link Object }
     * {@link WSICoverageTemporalDictionary }
     * {@link WSIReadingNoteDictionary }
     * {@link TridasRadius }
     * {@link WSIBoxDictionary }
     * {@link WSIRegionDictionary }
     * {@link WSICoverageTemporalFoundationDictionary }
     * 
     * 
     */
    public List<Object> getSqlsAndObjectsAndElements() {
        if (sqlsAndObjectsAndElements == null) {
            sqlsAndObjectsAndElements = new ArrayList<Object>();
        }
        return this.sqlsAndObjectsAndElements;
    }

    public boolean isSetSqlsAndObjectsAndElements() {
        return ((this.sqlsAndObjectsAndElements!= null)&&(!this.sqlsAndObjectsAndElements.isEmpty()));
    }

    public void unsetSqlsAndObjectsAndElements() {
        this.sqlsAndObjectsAndElements = null;
    }

    /**
     * Sets the value of the sqlsAndObjectsAndElements property.
     * 
     * @param sqlsAndObjectsAndElements
     *     allowed object is
     *     {@link TridasElement }
     *     {@link WSITaxonDictionary }
     *     {@link WSIObjectTypeDictionary }
     *     {@link WSIHeartwoodDictionary }
     *     {@link WSISampleTypeDictionary }
     *     {@link WSISecurityUserDictionary }
     *     {@link WSILocationTypeDictionary }
     *     {@link WSIBox }
     *     {@link WSIDatingTypeDictionary }
     *     {@link TridasSample }
     *     {@link WSIElementAuthenticityDictionary }
     *     {@link TridasObject }
     *     {@link TridasDerivedSeries }
     *     {@link WSIElementShapeDictionary }
     *     {@link WSIElementTypeDictionary }
     *     {@link TridasMeasurementSeries }
     *     {@link WSIMeasurementVariableDictionary }
     *     {@link WSISapwoodDictionary }
     *     {@link Object }
     *     {@link WSICoverageTemporalDictionary }
     *     {@link WSIReadingNoteDictionary }
     *     {@link TridasRadius }
     *     {@link WSIBoxDictionary }
     *     {@link WSIRegionDictionary }
     *     {@link WSICoverageTemporalFoundationDictionary }
     *     
     */
    public void setSqlsAndObjectsAndElements(List<Object> sqlsAndObjectsAndElements) {
        this.sqlsAndObjectsAndElements = sqlsAndObjectsAndElements;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIContent)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIContent that = ((WSIContent) object);
        equalsBuilder.append(this.getSqlsAndObjectsAndElements(), that.getSqlsAndObjectsAndElements());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIContent)) {
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
        hashCodeBuilder.append(this.getSqlsAndObjectsAndElements());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Object> theSqlsAndObjectsAndElements;
            theSqlsAndObjectsAndElements = this.getSqlsAndObjectsAndElements();
            toStringBuilder.append("sqlsAndObjectsAndElements", theSqlsAndObjectsAndElements);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIContent copy = ((target == null)?((WSIContent) createCopy()):((WSIContent) target));
        if (this.isSetSqlsAndObjectsAndElements()) {
            List<Object> sourceSqlsAndObjectsAndElements;
            sourceSqlsAndObjectsAndElements = this.getSqlsAndObjectsAndElements();
            @SuppressWarnings("unchecked")
            List<Object> copySqlsAndObjectsAndElements = ((List<Object> ) copyBuilder.copy(sourceSqlsAndObjectsAndElements));
            copy.setSqlsAndObjectsAndElements(copySqlsAndObjectsAndElements);
        } else {
            copy.unsetSqlsAndObjectsAndElements();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIContent();
    }

}
