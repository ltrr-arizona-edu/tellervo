
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
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}object" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}element" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}sample" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}radius" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}measurementSeries" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}derivedSeries" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}box" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityUser" maxOccurs="unbounded" minOccurs="0"/>
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
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}securityGroupDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}boxDictionary" minOccurs="0"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}wmsServerDictionary" minOccurs="0"/>
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
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElements({
        @XmlElement(name = "readingNoteDictionary", type = WSIReadingNoteDictionary.class),
        @XmlElement(name = "sampleTypeDictionary", type = WSISampleTypeDictionary.class),
        @XmlElement(name = "derivedSeries", namespace = "http://www.tridas.org/1.2.2", type = TridasDerivedSeries.class),
        @XmlElement(name = "securityUser", type = WSISecurityUser.class),
        @XmlElement(name = "element", namespace = "http://www.tridas.org/1.2.2", type = TridasElement.class),
        @XmlElement(name = "securityUserDictionary", type = WSISecurityUserDictionary.class),
        @XmlElement(name = "box", type = WSIBox.class),
        @XmlElement(name = "sql"),
        @XmlElement(name = "elementAuthenticityDictionary", type = WSIElementAuthenticityDictionary.class),
        @XmlElement(name = "elementTypeDictionary", type = WSIElementTypeDictionary.class),
        @XmlElement(name = "sapwoodDictionary", type = WSISapwoodDictionary.class),
        @XmlElement(name = "regionDictionary", type = WSIRegionDictionary.class),
        @XmlElement(name = "coverageTemporalFoundationDictionary", type = WSICoverageTemporalFoundationDictionary.class),
        @XmlElement(name = "datingTypeDictionary", type = WSIDatingTypeDictionary.class),
        @XmlElement(name = "radius", namespace = "http://www.tridas.org/1.2.2", type = TridasRadius.class),
        @XmlElement(name = "securityGroupDictionary", type = WSISecurityGroupDictionary.class),
        @XmlElement(name = "taxonDictionary", type = WSITaxonDictionary.class),
        @XmlElement(name = "coverageTemporalDictionary", type = WSICoverageTemporalDictionary.class),
        @XmlElement(name = "measurementSeries", namespace = "http://www.tridas.org/1.2.2", type = TridasMeasurementSeries.class),
        @XmlElement(name = "wmsServerDictionary", type = WSIWmsServerDictionary.class),
        @XmlElement(name = "heartwoodDictionary", type = WSIHeartwoodDictionary.class),
        @XmlElement(name = "boxDictionary", type = WSIBoxDictionary.class),
        @XmlElement(name = "objectTypeDictionary", type = WSIObjectTypeDictionary.class),
        @XmlElement(name = "elementShapeDictionary", type = WSIElementShapeDictionary.class),
        @XmlElement(name = "measurementVariableDictionary", type = WSIMeasurementVariableDictionary.class),
        @XmlElement(name = "object", namespace = "http://www.tridas.org/1.2.2", type = TridasObjectEx.class),
        @XmlElement(name = "locationTypeDictionary", type = WSILocationTypeDictionary.class),
        @XmlElement(name = "sample", namespace = "http://www.tridas.org/1.2.2", type = TridasSample.class)
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
     * {@link WSIReadingNoteDictionary }
     * {@link WSISampleTypeDictionary }
     * {@link TridasDerivedSeries }
     * {@link WSISecurityUser }
     * {@link TridasElement }
     * {@link WSISecurityUserDictionary }
     * {@link WSIBox }
     * {@link Object }
     * {@link WSIElementAuthenticityDictionary }
     * {@link WSIElementTypeDictionary }
     * {@link WSISapwoodDictionary }
     * {@link WSIRegionDictionary }
     * {@link WSICoverageTemporalFoundationDictionary }
     * {@link WSIDatingTypeDictionary }
     * {@link TridasRadius }
     * {@link WSISecurityGroupDictionary }
     * {@link WSITaxonDictionary }
     * {@link WSICoverageTemporalDictionary }
     * {@link TridasMeasurementSeries }
     * {@link WSIWmsServerDictionary }
     * {@link WSIHeartwoodDictionary }
     * {@link WSIBoxDictionary }
     * {@link WSIObjectTypeDictionary }
     * {@link WSIElementShapeDictionary }
     * {@link WSIMeasurementVariableDictionary }
     * {@link TridasObject }
     * {@link WSILocationTypeDictionary }
     * {@link TridasSample }
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
            List<Object> theSqlsAndObjectsAndElements;
            theSqlsAndObjectsAndElements = (this.isSetSqlsAndObjectsAndElements()?this.getSqlsAndObjectsAndElements():null);
            strategy.appendField(locator, this, "sqlsAndObjectsAndElements", buffer, theSqlsAndObjectsAndElements);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIContent)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIContent that = ((WSIContent) object);
        {
            List<Object> lhsSqlsAndObjectsAndElements;
            lhsSqlsAndObjectsAndElements = (this.isSetSqlsAndObjectsAndElements()?this.getSqlsAndObjectsAndElements():null);
            List<Object> rhsSqlsAndObjectsAndElements;
            rhsSqlsAndObjectsAndElements = (that.isSetSqlsAndObjectsAndElements()?that.getSqlsAndObjectsAndElements():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sqlsAndObjectsAndElements", lhsSqlsAndObjectsAndElements), LocatorUtils.property(thatLocator, "sqlsAndObjectsAndElements", rhsSqlsAndObjectsAndElements), lhsSqlsAndObjectsAndElements, rhsSqlsAndObjectsAndElements)) {
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
            List<Object> theSqlsAndObjectsAndElements;
            theSqlsAndObjectsAndElements = (this.isSetSqlsAndObjectsAndElements()?this.getSqlsAndObjectsAndElements():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sqlsAndObjectsAndElements", theSqlsAndObjectsAndElements), currentHashCode, theSqlsAndObjectsAndElements);
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
        if (draftCopy instanceof WSIContent) {
            final WSIContent copy = ((WSIContent) draftCopy);
            if (this.isSetSqlsAndObjectsAndElements()) {
                List<Object> sourceSqlsAndObjectsAndElements;
                sourceSqlsAndObjectsAndElements = (this.isSetSqlsAndObjectsAndElements()?this.getSqlsAndObjectsAndElements():null);
                @SuppressWarnings("unchecked")
                List<Object> copySqlsAndObjectsAndElements = ((List<Object> ) strategy.copy(LocatorUtils.property(locator, "sqlsAndObjectsAndElements", sourceSqlsAndObjectsAndElements), sourceSqlsAndObjectsAndElements));
                copy.unsetSqlsAndObjectsAndElements();
                List<Object> uniqueSqlsAndObjectsAndElementsl = copy.getSqlsAndObjectsAndElements();
                uniqueSqlsAndObjectsAndElementsl.addAll(copySqlsAndObjectsAndElements);
            } else {
                copy.unsetSqlsAndObjectsAndElements();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIContent();
    }

    /**
     * Sets the value of the sqlsAndObjectsAndElements property.
     * 
     * @param sqlsAndObjectsAndElements
     *     allowed object is
     *     {@link WSIReadingNoteDictionary }
     *     {@link WSISampleTypeDictionary }
     *     {@link TridasDerivedSeries }
     *     {@link WSISecurityUser }
     *     {@link TridasElement }
     *     {@link WSISecurityUserDictionary }
     *     {@link WSIBox }
     *     {@link Object }
     *     {@link WSIElementAuthenticityDictionary }
     *     {@link WSIElementTypeDictionary }
     *     {@link WSISapwoodDictionary }
     *     {@link WSIRegionDictionary }
     *     {@link WSICoverageTemporalFoundationDictionary }
     *     {@link WSIDatingTypeDictionary }
     *     {@link TridasRadius }
     *     {@link WSISecurityGroupDictionary }
     *     {@link WSITaxonDictionary }
     *     {@link WSICoverageTemporalDictionary }
     *     {@link TridasMeasurementSeries }
     *     {@link WSIWmsServerDictionary }
     *     {@link WSIHeartwoodDictionary }
     *     {@link WSIBoxDictionary }
     *     {@link WSIObjectTypeDictionary }
     *     {@link WSIElementShapeDictionary }
     *     {@link WSIMeasurementVariableDictionary }
     *     {@link TridasObject }
     *     {@link WSILocationTypeDictionary }
     *     {@link TridasSample }
     *     
     */
    public void setSqlsAndObjectsAndElements(List<Object> sqlsAndObjectsAndElements) {
        this.sqlsAndObjectsAndElements = sqlsAndObjectsAndElements;
    }

}
