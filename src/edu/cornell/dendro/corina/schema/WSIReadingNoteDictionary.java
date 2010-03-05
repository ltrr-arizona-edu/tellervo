
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
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
 *         &lt;element ref="{http://www.tridas.org/1.3}remark" maxOccurs="unbounded" minOccurs="0"/>
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
public class WSIReadingNoteDictionary implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    @XmlElement(name = "remark", namespace = "http://www.tridas.org/1.3")
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIReadingNoteDictionary)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIReadingNoteDictionary that = ((WSIReadingNoteDictionary) object);
        equalsBuilder.append(this.getRemarks(), that.getRemarks());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIReadingNoteDictionary)) {
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
        hashCodeBuilder.append(this.getRemarks());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<TridasRemark> theRemarks;
            theRemarks = this.getRemarks();
            toStringBuilder.append("remarks", theRemarks);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIReadingNoteDictionary copy = ((target == null)?((WSIReadingNoteDictionary) createCopy()):((WSIReadingNoteDictionary) target));
        if (this.isSetRemarks()) {
            List<TridasRemark> sourceRemarks;
            sourceRemarks = this.getRemarks();
            @SuppressWarnings("unchecked")
            List<TridasRemark> copyRemarks = ((List<TridasRemark> ) copyBuilder.copy(sourceRemarks));
            copy.setRemarks(copyRemarks);
        } else {
            copy.unsetRemarks();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIReadingNoteDictionary();
    }

}
