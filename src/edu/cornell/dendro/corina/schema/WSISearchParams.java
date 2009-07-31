
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
import org.tridas.adapters.IntegerAdapter;


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
 *         &lt;choice>
 *           &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}param" maxOccurs="unbounded"/>
 *           &lt;element name="all">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="returnObject" type="{http://dendro.cornell.edu/schema/corina/1.0}searchReturnObject" />
 *       &lt;attribute name="limit" type="{http://dendro.cornell.edu/schema/corina/1.0}signedInt" />
 *       &lt;attribute name="skip" type="{http://dendro.cornell.edu/schema/corina/1.0}signedInt" />
 *       &lt;attribute name="includeChildren" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "all",
    "params"
})
@XmlRootElement(name = "searchParams")
public class WSISearchParams
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSISearchParams.All all;
    @XmlElement(name = "param")
    protected List<WSIParam> params;
    @XmlAttribute(name = "returnObject")
    protected SearchReturnObject returnObject;
    @XmlAttribute(name = "limit")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    protected Integer limit;
    @XmlAttribute(name = "skip")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    protected Integer skip;
    @XmlAttribute(name = "includeChildren")
    protected Boolean includeChildren;

    /**
     * Gets the value of the all property.
     * 
     * @return
     *     possible object is
     *     {@link WSISearchParams.All }
     *     
     */
    public WSISearchParams.All getAll() {
        return all;
    }

    /**
     * Sets the value of the all property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSISearchParams.All }
     *     
     */
    public void setAll(WSISearchParams.All value) {
        this.all = value;
    }

    public boolean isSetAll() {
        return (this.all!= null);
    }

    /**
     * Gets the value of the params property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the params property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIParam }
     * 
     * 
     */
    public List<WSIParam> getParams() {
        if (params == null) {
            params = new ArrayList<WSIParam>();
        }
        return this.params;
    }

    public boolean isSetParams() {
        return ((this.params!= null)&&(!this.params.isEmpty()));
    }

    public void unsetParams() {
        this.params = null;
    }

    /**
     * Gets the value of the returnObject property.
     * 
     * @return
     *     possible object is
     *     {@link SearchReturnObject }
     *     
     */
    public SearchReturnObject getReturnObject() {
        return returnObject;
    }

    /**
     * Sets the value of the returnObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchReturnObject }
     *     
     */
    public void setReturnObject(SearchReturnObject value) {
        this.returnObject = value;
    }

    public boolean isSetReturnObject() {
        return (this.returnObject!= null);
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimit(Integer value) {
        this.limit = value;
    }

    public boolean isSetLimit() {
        return (this.limit!= null);
    }

    /**
     * Gets the value of the skip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * Sets the value of the skip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkip(Integer value) {
        this.skip = value;
    }

    public boolean isSetSkip() {
        return (this.skip!= null);
    }

    /**
     * Gets the value of the includeChildren property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIncludeChildren() {
        return includeChildren;
    }

    /**
     * Sets the value of the includeChildren property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeChildren(boolean value) {
        this.includeChildren = value;
    }

    public boolean isSetIncludeChildren() {
        return (this.includeChildren!= null);
    }

    public void unsetIncludeChildren() {
        this.includeChildren = null;
    }

    /**
     * Sets the value of the params property.
     * 
     * @param params
     *     allowed object is
     *     {@link WSIParam }
     *     
     */
    public void setParams(List<WSIParam> params) {
        this.params = params;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSISearchParams)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSISearchParams that = ((WSISearchParams) object);
        equalsBuilder.append(this.getAll(), that.getAll());
        equalsBuilder.append(this.getParams(), that.getParams());
        equalsBuilder.append(this.getReturnObject(), that.getReturnObject());
        equalsBuilder.append(this.getLimit(), that.getLimit());
        equalsBuilder.append(this.getSkip(), that.getSkip());
        equalsBuilder.append(this.isIncludeChildren(), that.isIncludeChildren());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSISearchParams)) {
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
        hashCodeBuilder.append(this.getAll());
        hashCodeBuilder.append(this.getParams());
        hashCodeBuilder.append(this.getReturnObject());
        hashCodeBuilder.append(this.getLimit());
        hashCodeBuilder.append(this.getSkip());
        hashCodeBuilder.append(this.isIncludeChildren());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            WSISearchParams.All theAll;
            theAll = this.getAll();
            toStringBuilder.append("all", theAll);
        }
        {
            List<WSIParam> theParams;
            theParams = this.getParams();
            toStringBuilder.append("params", theParams);
        }
        {
            SearchReturnObject theReturnObject;
            theReturnObject = this.getReturnObject();
            toStringBuilder.append("returnObject", theReturnObject);
        }
        {
            Integer theLimit;
            theLimit = this.getLimit();
            toStringBuilder.append("limit", theLimit);
        }
        {
            Integer theSkip;
            theSkip = this.getSkip();
            toStringBuilder.append("skip", theSkip);
        }
        {
            Boolean theIncludeChildren;
            theIncludeChildren = this.isIncludeChildren();
            toStringBuilder.append("includeChildren", theIncludeChildren);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSISearchParams copy = ((target == null)?((WSISearchParams) createCopy()):((WSISearchParams) target));
        if (this.isSetAll()) {
            WSISearchParams.All sourceAll;
            sourceAll = this.getAll();
            WSISearchParams.All copyAll = ((WSISearchParams.All) copyBuilder.copy(sourceAll));
            copy.setAll(copyAll);
        } else {
            copy.all = null;
        }
        if (this.isSetParams()) {
            List<WSIParam> sourceParams;
            sourceParams = this.getParams();
            @SuppressWarnings("unchecked")
            List<WSIParam> copyParams = ((List<WSIParam> ) copyBuilder.copy(sourceParams));
            copy.setParams(copyParams);
        } else {
            copy.unsetParams();
        }
        if (this.isSetReturnObject()) {
            SearchReturnObject sourceReturnObject;
            sourceReturnObject = this.getReturnObject();
            SearchReturnObject copyReturnObject = ((SearchReturnObject) copyBuilder.copy(sourceReturnObject));
            copy.setReturnObject(copyReturnObject);
        } else {
            copy.returnObject = null;
        }
        if (this.isSetLimit()) {
            Integer sourceLimit;
            sourceLimit = this.getLimit();
            Integer copyLimit = ((Integer) copyBuilder.copy(sourceLimit));
            copy.setLimit(copyLimit);
        } else {
            copy.limit = null;
        }
        if (this.isSetSkip()) {
            Integer sourceSkip;
            sourceSkip = this.getSkip();
            Integer copySkip = ((Integer) copyBuilder.copy(sourceSkip));
            copy.setSkip(copySkip);
        } else {
            copy.skip = null;
        }
        if (this.isSetIncludeChildren()) {
            Boolean sourceIncludeChildren;
            sourceIncludeChildren = this.isIncludeChildren();
            Boolean copyIncludeChildren = ((Boolean) copyBuilder.copy(sourceIncludeChildren));
            copy.setIncludeChildren(copyIncludeChildren);
        } else {
            copy.unsetIncludeChildren();
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSISearchParams();
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
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class All
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSISearchParams.All)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSISearchParams.All that = ((WSISearchParams.All) object);
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSISearchParams.All)) {
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
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSISearchParams.All copy = ((target == null)?((WSISearchParams.All) createCopy()):((WSISearchParams.All) target));
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSISearchParams.All();
        }

    }

}
