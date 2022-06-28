
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}dating" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}firstYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}lastYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}datingReference" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}statFoundation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}pithYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}deathYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}provenance" minOccurs="0"/>
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
    "dating",
    "firstYear",
    "lastYear",
    "datingReference",
    "statFoundations",
    "pithYear",
    "deathYear",
    "provenance"
})
@XmlRootElement(name = "interpretation")
public class TridasInterpretation
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected TridasDating dating;
    protected Year firstYear;
    protected Year lastYear;
    protected TridasDatingReference datingReference;
    @XmlElement(name = "statFoundation")
    protected List<TridasStatFoundation> statFoundations;
    protected Year pithYear;
    protected Year deathYear;
    protected String provenance;

    /**
     * Gets the value of the dating property.
     * 
     * @return
     *     possible object is
     *     {@link TridasDating }
     *     
     */
    public TridasDating getDating() {
        return dating;
    }

    /**
     * Sets the value of the dating property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasDating }
     *     
     */
    public void setDating(TridasDating value) {
        this.dating = value;
    }

    public boolean isSetDating() {
        return (this.dating!= null);
    }

    /**
     * Gets the value of the firstYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getFirstYear() {
        return firstYear;
    }

    /**
     * Sets the value of the firstYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setFirstYear(Year value) {
        this.firstYear = value;
    }

    public boolean isSetFirstYear() {
        return (this.firstYear!= null);
    }

    /**
     * Gets the value of the lastYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getLastYear() {
        return lastYear;
    }

    /**
     * Sets the value of the lastYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setLastYear(Year value) {
        this.lastYear = value;
    }

    public boolean isSetLastYear() {
        return (this.lastYear!= null);
    }

    /**
     * Gets the value of the datingReference property.
     * 
     * @return
     *     possible object is
     *     {@link TridasDatingReference }
     *     
     */
    public TridasDatingReference getDatingReference() {
        return datingReference;
    }

    /**
     * Sets the value of the datingReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasDatingReference }
     *     
     */
    public void setDatingReference(TridasDatingReference value) {
        this.datingReference = value;
    }

    public boolean isSetDatingReference() {
        return (this.datingReference!= null);
    }

    /**
     * Gets the value of the statFoundations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statFoundations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatFoundations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasStatFoundation }
     * 
     * 
     */
    public List<TridasStatFoundation> getStatFoundations() {
        if (statFoundations == null) {
            statFoundations = new ArrayList<TridasStatFoundation>();
        }
        return this.statFoundations;
    }

    public boolean isSetStatFoundations() {
        return ((this.statFoundations!= null)&&(!this.statFoundations.isEmpty()));
    }

    public void unsetStatFoundations() {
        this.statFoundations = null;
    }

    /**
     * Gets the value of the pithYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getPithYear() {
        return pithYear;
    }

    /**
     * Sets the value of the pithYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setPithYear(Year value) {
        this.pithYear = value;
    }

    public boolean isSetPithYear() {
        return (this.pithYear!= null);
    }

    /**
     * Gets the value of the deathYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getDeathYear() {
        return deathYear;
    }

    /**
     * Sets the value of the deathYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setDeathYear(Year value) {
        this.deathYear = value;
    }

    public boolean isSetDeathYear() {
        return (this.deathYear!= null);
    }

    /**
     * Gets the value of the provenance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvenance() {
        return provenance;
    }

    /**
     * Sets the value of the provenance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvenance(String value) {
        this.provenance = value;
    }

    public boolean isSetProvenance() {
        return (this.provenance!= null);
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
            TridasDating theDating;
            theDating = this.getDating();
            strategy.appendField(locator, this, "dating", buffer, theDating);
        }
        {
            Year theFirstYear;
            theFirstYear = this.getFirstYear();
            strategy.appendField(locator, this, "firstYear", buffer, theFirstYear);
        }
        {
            Year theLastYear;
            theLastYear = this.getLastYear();
            strategy.appendField(locator, this, "lastYear", buffer, theLastYear);
        }
        {
            TridasDatingReference theDatingReference;
            theDatingReference = this.getDatingReference();
            strategy.appendField(locator, this, "datingReference", buffer, theDatingReference);
        }
        {
            List<TridasStatFoundation> theStatFoundations;
            theStatFoundations = (this.isSetStatFoundations()?this.getStatFoundations():null);
            strategy.appendField(locator, this, "statFoundations", buffer, theStatFoundations);
        }
        {
            Year thePithYear;
            thePithYear = this.getPithYear();
            strategy.appendField(locator, this, "pithYear", buffer, thePithYear);
        }
        {
            Year theDeathYear;
            theDeathYear = this.getDeathYear();
            strategy.appendField(locator, this, "deathYear", buffer, theDeathYear);
        }
        {
            String theProvenance;
            theProvenance = this.getProvenance();
            strategy.appendField(locator, this, "provenance", buffer, theProvenance);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasInterpretation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasInterpretation that = ((TridasInterpretation) object);
        {
            TridasDating lhsDating;
            lhsDating = this.getDating();
            TridasDating rhsDating;
            rhsDating = that.getDating();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "dating", lhsDating), LocatorUtils.property(thatLocator, "dating", rhsDating), lhsDating, rhsDating)) {
                return false;
            }
        }
        {
            Year lhsFirstYear;
            lhsFirstYear = this.getFirstYear();
            Year rhsFirstYear;
            rhsFirstYear = that.getFirstYear();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "firstYear", lhsFirstYear), LocatorUtils.property(thatLocator, "firstYear", rhsFirstYear), lhsFirstYear, rhsFirstYear)) {
                return false;
            }
        }
        {
            Year lhsLastYear;
            lhsLastYear = this.getLastYear();
            Year rhsLastYear;
            rhsLastYear = that.getLastYear();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastYear", lhsLastYear), LocatorUtils.property(thatLocator, "lastYear", rhsLastYear), lhsLastYear, rhsLastYear)) {
                return false;
            }
        }
        {
            TridasDatingReference lhsDatingReference;
            lhsDatingReference = this.getDatingReference();
            TridasDatingReference rhsDatingReference;
            rhsDatingReference = that.getDatingReference();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "datingReference", lhsDatingReference), LocatorUtils.property(thatLocator, "datingReference", rhsDatingReference), lhsDatingReference, rhsDatingReference)) {
                return false;
            }
        }
        {
            List<TridasStatFoundation> lhsStatFoundations;
            lhsStatFoundations = (this.isSetStatFoundations()?this.getStatFoundations():null);
            List<TridasStatFoundation> rhsStatFoundations;
            rhsStatFoundations = (that.isSetStatFoundations()?that.getStatFoundations():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "statFoundations", lhsStatFoundations), LocatorUtils.property(thatLocator, "statFoundations", rhsStatFoundations), lhsStatFoundations, rhsStatFoundations)) {
                return false;
            }
        }
        {
            Year lhsPithYear;
            lhsPithYear = this.getPithYear();
            Year rhsPithYear;
            rhsPithYear = that.getPithYear();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "pithYear", lhsPithYear), LocatorUtils.property(thatLocator, "pithYear", rhsPithYear), lhsPithYear, rhsPithYear)) {
                return false;
            }
        }
        {
            Year lhsDeathYear;
            lhsDeathYear = this.getDeathYear();
            Year rhsDeathYear;
            rhsDeathYear = that.getDeathYear();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "deathYear", lhsDeathYear), LocatorUtils.property(thatLocator, "deathYear", rhsDeathYear), lhsDeathYear, rhsDeathYear)) {
                return false;
            }
        }
        {
            String lhsProvenance;
            lhsProvenance = this.getProvenance();
            String rhsProvenance;
            rhsProvenance = that.getProvenance();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "provenance", lhsProvenance), LocatorUtils.property(thatLocator, "provenance", rhsProvenance), lhsProvenance, rhsProvenance)) {
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
            TridasDating theDating;
            theDating = this.getDating();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "dating", theDating), currentHashCode, theDating);
        }
        {
            Year theFirstYear;
            theFirstYear = this.getFirstYear();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "firstYear", theFirstYear), currentHashCode, theFirstYear);
        }
        {
            Year theLastYear;
            theLastYear = this.getLastYear();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastYear", theLastYear), currentHashCode, theLastYear);
        }
        {
            TridasDatingReference theDatingReference;
            theDatingReference = this.getDatingReference();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "datingReference", theDatingReference), currentHashCode, theDatingReference);
        }
        {
            List<TridasStatFoundation> theStatFoundations;
            theStatFoundations = (this.isSetStatFoundations()?this.getStatFoundations():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "statFoundations", theStatFoundations), currentHashCode, theStatFoundations);
        }
        {
            Year thePithYear;
            thePithYear = this.getPithYear();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "pithYear", thePithYear), currentHashCode, thePithYear);
        }
        {
            Year theDeathYear;
            theDeathYear = this.getDeathYear();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "deathYear", theDeathYear), currentHashCode, theDeathYear);
        }
        {
            String theProvenance;
            theProvenance = this.getProvenance();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "provenance", theProvenance), currentHashCode, theProvenance);
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
        if (draftCopy instanceof TridasInterpretation) {
            final TridasInterpretation copy = ((TridasInterpretation) draftCopy);
            if (this.isSetDating()) {
                TridasDating sourceDating;
                sourceDating = this.getDating();
                TridasDating copyDating = ((TridasDating) strategy.copy(LocatorUtils.property(locator, "dating", sourceDating), sourceDating));
                copy.setDating(copyDating);
            } else {
                copy.dating = null;
            }
            if (this.isSetFirstYear()) {
                Year sourceFirstYear;
                sourceFirstYear = this.getFirstYear();
                Year copyFirstYear = ((Year) strategy.copy(LocatorUtils.property(locator, "firstYear", sourceFirstYear), sourceFirstYear));
                copy.setFirstYear(copyFirstYear);
            } else {
                copy.firstYear = null;
            }
            if (this.isSetLastYear()) {
                Year sourceLastYear;
                sourceLastYear = this.getLastYear();
                Year copyLastYear = ((Year) strategy.copy(LocatorUtils.property(locator, "lastYear", sourceLastYear), sourceLastYear));
                copy.setLastYear(copyLastYear);
            } else {
                copy.lastYear = null;
            }
            if (this.isSetDatingReference()) {
                TridasDatingReference sourceDatingReference;
                sourceDatingReference = this.getDatingReference();
                TridasDatingReference copyDatingReference = ((TridasDatingReference) strategy.copy(LocatorUtils.property(locator, "datingReference", sourceDatingReference), sourceDatingReference));
                copy.setDatingReference(copyDatingReference);
            } else {
                copy.datingReference = null;
            }
            if (this.isSetStatFoundations()) {
                List<TridasStatFoundation> sourceStatFoundations;
                sourceStatFoundations = (this.isSetStatFoundations()?this.getStatFoundations():null);
                @SuppressWarnings("unchecked")
                List<TridasStatFoundation> copyStatFoundations = ((List<TridasStatFoundation> ) strategy.copy(LocatorUtils.property(locator, "statFoundations", sourceStatFoundations), sourceStatFoundations));
                copy.unsetStatFoundations();
                List<TridasStatFoundation> uniqueStatFoundationsl = copy.getStatFoundations();
                uniqueStatFoundationsl.addAll(copyStatFoundations);
            } else {
                copy.unsetStatFoundations();
            }
            if (this.isSetPithYear()) {
                Year sourcePithYear;
                sourcePithYear = this.getPithYear();
                Year copyPithYear = ((Year) strategy.copy(LocatorUtils.property(locator, "pithYear", sourcePithYear), sourcePithYear));
                copy.setPithYear(copyPithYear);
            } else {
                copy.pithYear = null;
            }
            if (this.isSetDeathYear()) {
                Year sourceDeathYear;
                sourceDeathYear = this.getDeathYear();
                Year copyDeathYear = ((Year) strategy.copy(LocatorUtils.property(locator, "deathYear", sourceDeathYear), sourceDeathYear));
                copy.setDeathYear(copyDeathYear);
            } else {
                copy.deathYear = null;
            }
            if (this.isSetProvenance()) {
                String sourceProvenance;
                sourceProvenance = this.getProvenance();
                String copyProvenance = ((String) strategy.copy(LocatorUtils.property(locator, "provenance", sourceProvenance), sourceProvenance));
                copy.setProvenance(copyProvenance);
            } else {
                copy.provenance = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasInterpretation();
    }

    /**
     * Sets the value of the statFoundations property.
     * 
     * @param statFoundations
     *     allowed object is
     *     {@link TridasStatFoundation }
     *     
     */
    public void setStatFoundations(List<TridasStatFoundation> statFoundations) {
        this.statFoundations = statFoundations;
    }

}
