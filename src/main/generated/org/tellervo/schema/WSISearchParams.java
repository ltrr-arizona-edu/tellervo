
package org.tellervo.schema;

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
 *           &lt;element ref="{http://www.tellervo.org/schema/1.0}param" maxOccurs="unbounded"/>
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
 *       &lt;attribute name="returnObject" type="{http://www.tellervo.org/schema/1.0}searchReturnObject" />
 *       &lt;attribute name="limit" type="{http://www.tellervo.org/schema/1.0}signedInt" />
 *       &lt;attribute name="skip" type="{http://www.tellervo.org/schema/1.0}signedInt" />
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
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
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
            WSISearchParams.All theAll;
            theAll = this.getAll();
            strategy.appendField(locator, this, "all", buffer, theAll);
        }
        {
            List<WSIParam> theParams;
            theParams = (this.isSetParams()?this.getParams():null);
            strategy.appendField(locator, this, "params", buffer, theParams);
        }
        {
            SearchReturnObject theReturnObject;
            theReturnObject = this.getReturnObject();
            strategy.appendField(locator, this, "returnObject", buffer, theReturnObject);
        }
        {
            Integer theLimit;
            theLimit = this.getLimit();
            strategy.appendField(locator, this, "limit", buffer, theLimit);
        }
        {
            Integer theSkip;
            theSkip = this.getSkip();
            strategy.appendField(locator, this, "skip", buffer, theSkip);
        }
        {
            boolean theIncludeChildren;
            theIncludeChildren = (this.isSetIncludeChildren()?this.isIncludeChildren():false);
            strategy.appendField(locator, this, "includeChildren", buffer, theIncludeChildren);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSISearchParams)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSISearchParams that = ((WSISearchParams) object);
        {
            WSISearchParams.All lhsAll;
            lhsAll = this.getAll();
            WSISearchParams.All rhsAll;
            rhsAll = that.getAll();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "all", lhsAll), LocatorUtils.property(thatLocator, "all", rhsAll), lhsAll, rhsAll)) {
                return false;
            }
        }
        {
            List<WSIParam> lhsParams;
            lhsParams = (this.isSetParams()?this.getParams():null);
            List<WSIParam> rhsParams;
            rhsParams = (that.isSetParams()?that.getParams():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "params", lhsParams), LocatorUtils.property(thatLocator, "params", rhsParams), lhsParams, rhsParams)) {
                return false;
            }
        }
        {
            SearchReturnObject lhsReturnObject;
            lhsReturnObject = this.getReturnObject();
            SearchReturnObject rhsReturnObject;
            rhsReturnObject = that.getReturnObject();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "returnObject", lhsReturnObject), LocatorUtils.property(thatLocator, "returnObject", rhsReturnObject), lhsReturnObject, rhsReturnObject)) {
                return false;
            }
        }
        {
            Integer lhsLimit;
            lhsLimit = this.getLimit();
            Integer rhsLimit;
            rhsLimit = that.getLimit();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "limit", lhsLimit), LocatorUtils.property(thatLocator, "limit", rhsLimit), lhsLimit, rhsLimit)) {
                return false;
            }
        }
        {
            Integer lhsSkip;
            lhsSkip = this.getSkip();
            Integer rhsSkip;
            rhsSkip = that.getSkip();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "skip", lhsSkip), LocatorUtils.property(thatLocator, "skip", rhsSkip), lhsSkip, rhsSkip)) {
                return false;
            }
        }
        {
            boolean lhsIncludeChildren;
            lhsIncludeChildren = (this.isSetIncludeChildren()?this.isIncludeChildren():false);
            boolean rhsIncludeChildren;
            rhsIncludeChildren = (that.isSetIncludeChildren()?that.isIncludeChildren():false);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "includeChildren", lhsIncludeChildren), LocatorUtils.property(thatLocator, "includeChildren", rhsIncludeChildren), lhsIncludeChildren, rhsIncludeChildren)) {
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
            WSISearchParams.All theAll;
            theAll = this.getAll();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "all", theAll), currentHashCode, theAll);
        }
        {
            List<WSIParam> theParams;
            theParams = (this.isSetParams()?this.getParams():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "params", theParams), currentHashCode, theParams);
        }
        {
            SearchReturnObject theReturnObject;
            theReturnObject = this.getReturnObject();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "returnObject", theReturnObject), currentHashCode, theReturnObject);
        }
        {
            Integer theLimit;
            theLimit = this.getLimit();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "limit", theLimit), currentHashCode, theLimit);
        }
        {
            Integer theSkip;
            theSkip = this.getSkip();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "skip", theSkip), currentHashCode, theSkip);
        }
        {
            boolean theIncludeChildren;
            theIncludeChildren = (this.isSetIncludeChildren()?this.isIncludeChildren():false);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "includeChildren", theIncludeChildren), currentHashCode, theIncludeChildren);
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
        if (draftCopy instanceof WSISearchParams) {
            final WSISearchParams copy = ((WSISearchParams) draftCopy);
            if (this.isSetAll()) {
                WSISearchParams.All sourceAll;
                sourceAll = this.getAll();
                WSISearchParams.All copyAll = ((WSISearchParams.All) strategy.copy(LocatorUtils.property(locator, "all", sourceAll), sourceAll));
                copy.setAll(copyAll);
            } else {
                copy.all = null;
            }
            if (this.isSetParams()) {
                List<WSIParam> sourceParams;
                sourceParams = (this.isSetParams()?this.getParams():null);
                @SuppressWarnings("unchecked")
                List<WSIParam> copyParams = ((List<WSIParam> ) strategy.copy(LocatorUtils.property(locator, "params", sourceParams), sourceParams));
                copy.unsetParams();
                List<WSIParam> uniqueParamsl = copy.getParams();
                uniqueParamsl.addAll(copyParams);
            } else {
                copy.unsetParams();
            }
            if (this.isSetReturnObject()) {
                SearchReturnObject sourceReturnObject;
                sourceReturnObject = this.getReturnObject();
                SearchReturnObject copyReturnObject = ((SearchReturnObject) strategy.copy(LocatorUtils.property(locator, "returnObject", sourceReturnObject), sourceReturnObject));
                copy.setReturnObject(copyReturnObject);
            } else {
                copy.returnObject = null;
            }
            if (this.isSetLimit()) {
                Integer sourceLimit;
                sourceLimit = this.getLimit();
                Integer copyLimit = ((Integer) strategy.copy(LocatorUtils.property(locator, "limit", sourceLimit), sourceLimit));
                copy.setLimit(copyLimit);
            } else {
                copy.limit = null;
            }
            if (this.isSetSkip()) {
                Integer sourceSkip;
                sourceSkip = this.getSkip();
                Integer copySkip = ((Integer) strategy.copy(LocatorUtils.property(locator, "skip", sourceSkip), sourceSkip));
                copy.setSkip(copySkip);
            } else {
                copy.skip = null;
            }
            if (this.isSetIncludeChildren()) {
                boolean sourceIncludeChildren;
                sourceIncludeChildren = (this.isSetIncludeChildren()?this.isIncludeChildren():false);
                boolean copyIncludeChildren = strategy.copy(LocatorUtils.property(locator, "includeChildren", sourceIncludeChildren), sourceIncludeChildren);
                copy.setIncludeChildren(copyIncludeChildren);
            } else {
                copy.unsetIncludeChildren();
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSISearchParams();
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
        implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;

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
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSISearchParams.All)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            return true;
        }

        public boolean equals(Object object) {
            final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
            return equals(null, null, object, strategy);
        }

        public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
            int currentHashCode = 1;
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
            return draftCopy;
        }

        public Object createNewInstance() {
            return new WSISearchParams.All();
        }

    }

}
