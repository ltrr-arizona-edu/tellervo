
package org.tellervo.schema;

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
import org.tridas.schema.TridasRemark;


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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}remark" maxOccurs="unbounded" minOccurs="0"/>
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
    "remarks"
})
@XmlRootElement(name = "readingNoteDictionary")
public class WSIReadingNoteDictionary implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(name = "remark", namespace = "http://www.tridas.org/1.2.2")
    protected List<TridasRemark> remarks;

    /**
     * Gets the value of the remarks property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remarks property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemarks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasRemark }
     * 
     * 
     */
    public List<TridasRemark> getRemarks() {
        if (remarks == null) {
            remarks = new ArrayList<TridasRemark>();
        }
        return this.remarks;
    }

    public boolean isSetRemarks() {
        return ((this.remarks!= null)&&(!this.remarks.isEmpty()));
    }

    public void unsetRemarks() {
        this.remarks = null;
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
            List<TridasRemark> theRemarks;
            theRemarks = (this.isSetRemarks()?this.getRemarks():null);
            strategy.appendField(locator, this, "remarks", buffer, theRemarks);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIReadingNoteDictionary)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIReadingNoteDictionary that = ((WSIReadingNoteDictionary) object);
        {
            List<TridasRemark> lhsRemarks;
            lhsRemarks = (this.isSetRemarks()?this.getRemarks():null);
            List<TridasRemark> rhsRemarks;
            rhsRemarks = (that.isSetRemarks()?that.getRemarks():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "remarks", lhsRemarks), LocatorUtils.property(thatLocator, "remarks", rhsRemarks), lhsRemarks, rhsRemarks)) {
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
            List<TridasRemark> theRemarks;
            theRemarks = (this.isSetRemarks()?this.getRemarks():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "remarks", theRemarks), currentHashCode, theRemarks);
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
        if (draftCopy instanceof WSIReadingNoteDictionary) {
            final WSIReadingNoteDictionary copy = ((WSIReadingNoteDictionary) draftCopy);
            if (this.isSetRemarks()) {
                List<TridasRemark> sourceRemarks;
                sourceRemarks = (this.isSetRemarks()?this.getRemarks():null);
                @SuppressWarnings("unchecked")
                List<TridasRemark> copyRemarks = ((List<TridasRemark> ) strategy.copy(LocatorUtils.property(locator, "remarks", sourceRemarks), sourceRemarks));
                copy.unsetRemarks();
                List<TridasRemark> uniqueRemarksl = copy.getRemarks();
                uniqueRemarksl.addAll(copyRemarks);
            } else {
                copy.unsetRemarks();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIReadingNoteDictionary();
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param remarks
     *     allowed object is
     *     {@link TridasRemark }
     *     
     */
    public void setRemarks(List<TridasRemark> remarks) {
        this.remarks = remarks;
    }

}
