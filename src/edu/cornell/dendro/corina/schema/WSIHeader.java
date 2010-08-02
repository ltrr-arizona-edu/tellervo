
package edu.cornell.dendro.corina.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;
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
 *         &lt;element name="securityUser" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="firstname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="lastname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="wsVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clientVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="queryTime">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
 *                 &lt;attribute name="unit">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="seconds"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="requestUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestType" type="{http://dendro.cornell.edu/schema/corina/1.0}corinaRequestType"/>
 *         &lt;element name="status" type="{http://dendro.cornell.edu/schema/corina/1.0}corinaRequestStatus"/>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}message" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timing" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://dendro.cornell.edu/schema/corina/1.0}nonce" minOccurs="0"/>
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
    "securityUser",
    "wsVersion",
    "clientVersion",
    "requestDate",
    "queryTime",
    "requestUrl",
    "requestType",
    "status",
    "messages",
    "timings",
    "nonce"
})
@XmlRootElement(name = "header")
public class WSIHeader
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSIHeader.SecurityUser securityUser;
    @XmlElement(required = true)
    protected String wsVersion;
    @XmlElement(required = true)
    protected String clientVersion;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDate;
    @XmlElement(required = true)
    protected WSIHeader.QueryTime queryTime;
    @XmlElement(required = true)
    protected String requestUrl;
    @XmlElement(required = true)
    protected CorinaRequestType requestType;
    @XmlElement(required = true)
    protected CorinaRequestStatus status;
    @XmlElement(name = "message")
    protected List<WSIMessage> messages;
    @XmlElement(name = "timing")
    protected List<WSIHeader.Timing> timings;
    protected WSINonce nonce;

    /**
     * Gets the value of the securityUser property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHeader.SecurityUser }
     *     
     */
    public WSIHeader.SecurityUser getSecurityUser() {
        return securityUser;
    }

    /**
     * Sets the value of the securityUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHeader.SecurityUser }
     *     
     */
    public void setSecurityUser(WSIHeader.SecurityUser value) {
        this.securityUser = value;
    }

    public boolean isSetSecurityUser() {
        return (this.securityUser!= null);
    }

    /**
     * Gets the value of the wsVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsVersion() {
        return wsVersion;
    }

    /**
     * Sets the value of the wsVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsVersion(String value) {
        this.wsVersion = value;
    }

    public boolean isSetWsVersion() {
        return (this.wsVersion!= null);
    }

    /**
     * Gets the value of the clientVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientVersion() {
        return clientVersion;
    }

    /**
     * Sets the value of the clientVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientVersion(String value) {
        this.clientVersion = value;
    }

    public boolean isSetClientVersion() {
        return (this.clientVersion!= null);
    }

    /**
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestDate(XMLGregorianCalendar value) {
        this.requestDate = value;
    }

    public boolean isSetRequestDate() {
        return (this.requestDate!= null);
    }

    /**
     * Gets the value of the queryTime property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHeader.QueryTime }
     *     
     */
    public WSIHeader.QueryTime getQueryTime() {
        return queryTime;
    }

    /**
     * Sets the value of the queryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHeader.QueryTime }
     *     
     */
    public void setQueryTime(WSIHeader.QueryTime value) {
        this.queryTime = value;
    }

    public boolean isSetQueryTime() {
        return (this.queryTime!= null);
    }

    /**
     * Gets the value of the requestUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * Sets the value of the requestUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestUrl(String value) {
        this.requestUrl = value;
    }

    public boolean isSetRequestUrl() {
        return (this.requestUrl!= null);
    }

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link CorinaRequestType }
     *     
     */
    public CorinaRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorinaRequestType }
     *     
     */
    public void setRequestType(CorinaRequestType value) {
        this.requestType = value;
    }

    public boolean isSetRequestType() {
        return (this.requestType!= null);
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link CorinaRequestStatus }
     *     
     */
    public CorinaRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorinaRequestStatus }
     *     
     */
    public void setStatus(CorinaRequestStatus value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the messages property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messages property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessages().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIMessage }
     * 
     * 
     */
    public List<WSIMessage> getMessages() {
        if (messages == null) {
            messages = new ArrayList<WSIMessage>();
        }
        return this.messages;
    }

    public boolean isSetMessages() {
        return ((this.messages!= null)&&(!this.messages.isEmpty()));
    }

    public void unsetMessages() {
        this.messages = null;
    }

    /**
     * Gets the value of the timings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WSIHeader.Timing }
     * 
     * 
     */
    public List<WSIHeader.Timing> getTimings() {
        if (timings == null) {
            timings = new ArrayList<WSIHeader.Timing>();
        }
        return this.timings;
    }

    public boolean isSetTimings() {
        return ((this.timings!= null)&&(!this.timings.isEmpty()));
    }

    public void unsetTimings() {
        this.timings = null;
    }

    /**
     * Gets the value of the nonce property.
     * 
     * @return
     *     possible object is
     *     {@link WSINonce }
     *     
     */
    public WSINonce getNonce() {
        return nonce;
    }

    /**
     * Sets the value of the nonce property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSINonce }
     *     
     */
    public void setNonce(WSINonce value) {
        this.nonce = value;
    }

    public boolean isSetNonce() {
        return (this.nonce!= null);
    }

    /**
     * Sets the value of the messages property.
     * 
     * @param messages
     *     allowed object is
     *     {@link WSIMessage }
     *     
     */
    public void setMessages(List<WSIMessage> messages) {
        this.messages = messages;
    }

    /**
     * Sets the value of the timings property.
     * 
     * @param timings
     *     allowed object is
     *     {@link WSIHeader.Timing }
     *     
     */
    public void setTimings(List<WSIHeader.Timing> timings) {
        this.timings = timings;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof WSIHeader)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WSIHeader that = ((WSIHeader) object);
        equalsBuilder.append(this.getSecurityUser(), that.getSecurityUser());
        equalsBuilder.append(this.getWsVersion(), that.getWsVersion());
        equalsBuilder.append(this.getClientVersion(), that.getClientVersion());
        equalsBuilder.append(this.getRequestDate(), that.getRequestDate());
        equalsBuilder.append(this.getQueryTime(), that.getQueryTime());
        equalsBuilder.append(this.getRequestUrl(), that.getRequestUrl());
        equalsBuilder.append(this.getRequestType(), that.getRequestType());
        equalsBuilder.append(this.getStatus(), that.getStatus());
        equalsBuilder.append(this.getMessages(), that.getMessages());
        equalsBuilder.append(this.getTimings(), that.getTimings());
        equalsBuilder.append(this.getNonce(), that.getNonce());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WSIHeader)) {
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
        hashCodeBuilder.append(this.getSecurityUser());
        hashCodeBuilder.append(this.getWsVersion());
        hashCodeBuilder.append(this.getClientVersion());
        hashCodeBuilder.append(this.getRequestDate());
        hashCodeBuilder.append(this.getQueryTime());
        hashCodeBuilder.append(this.getRequestUrl());
        hashCodeBuilder.append(this.getRequestType());
        hashCodeBuilder.append(this.getStatus());
        hashCodeBuilder.append(this.getMessages());
        hashCodeBuilder.append(this.getTimings());
        hashCodeBuilder.append(this.getNonce());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            WSIHeader.SecurityUser theSecurityUser;
            theSecurityUser = this.getSecurityUser();
            toStringBuilder.append("securityUser", theSecurityUser);
        }
        {
            String theWsVersion;
            theWsVersion = this.getWsVersion();
            toStringBuilder.append("wsVersion", theWsVersion);
        }
        {
            String theClientVersion;
            theClientVersion = this.getClientVersion();
            toStringBuilder.append("clientVersion", theClientVersion);
        }
        {
            XMLGregorianCalendar theRequestDate;
            theRequestDate = this.getRequestDate();
            toStringBuilder.append("requestDate", theRequestDate);
        }
        {
            WSIHeader.QueryTime theQueryTime;
            theQueryTime = this.getQueryTime();
            toStringBuilder.append("queryTime", theQueryTime);
        }
        {
            String theRequestUrl;
            theRequestUrl = this.getRequestUrl();
            toStringBuilder.append("requestUrl", theRequestUrl);
        }
        {
            CorinaRequestType theRequestType;
            theRequestType = this.getRequestType();
            toStringBuilder.append("requestType", theRequestType);
        }
        {
            CorinaRequestStatus theStatus;
            theStatus = this.getStatus();
            toStringBuilder.append("status", theStatus);
        }
        {
            List<WSIMessage> theMessages;
            theMessages = this.getMessages();
            toStringBuilder.append("messages", theMessages);
        }
        {
            List<WSIHeader.Timing> theTimings;
            theTimings = this.getTimings();
            toStringBuilder.append("timings", theTimings);
        }
        {
            WSINonce theNonce;
            theNonce = this.getNonce();
            toStringBuilder.append("nonce", theNonce);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final WSIHeader copy = ((target == null)?((WSIHeader) createCopy()):((WSIHeader) target));
        if (this.isSetSecurityUser()) {
            WSIHeader.SecurityUser sourceSecurityUser;
            sourceSecurityUser = this.getSecurityUser();
            WSIHeader.SecurityUser copySecurityUser = ((WSIHeader.SecurityUser) copyBuilder.copy(sourceSecurityUser));
            copy.setSecurityUser(copySecurityUser);
        } else {
            copy.securityUser = null;
        }
        if (this.isSetWsVersion()) {
            String sourceWsVersion;
            sourceWsVersion = this.getWsVersion();
            String copyWsVersion = ((String) copyBuilder.copy(sourceWsVersion));
            copy.setWsVersion(copyWsVersion);
        } else {
            copy.wsVersion = null;
        }
        if (this.isSetClientVersion()) {
            String sourceClientVersion;
            sourceClientVersion = this.getClientVersion();
            String copyClientVersion = ((String) copyBuilder.copy(sourceClientVersion));
            copy.setClientVersion(copyClientVersion);
        } else {
            copy.clientVersion = null;
        }
        if (this.isSetRequestDate()) {
            XMLGregorianCalendar sourceRequestDate;
            sourceRequestDate = this.getRequestDate();
            XMLGregorianCalendar copyRequestDate = ((XMLGregorianCalendar) copyBuilder.copy(sourceRequestDate));
            copy.setRequestDate(copyRequestDate);
        } else {
            copy.requestDate = null;
        }
        if (this.isSetQueryTime()) {
            WSIHeader.QueryTime sourceQueryTime;
            sourceQueryTime = this.getQueryTime();
            WSIHeader.QueryTime copyQueryTime = ((WSIHeader.QueryTime) copyBuilder.copy(sourceQueryTime));
            copy.setQueryTime(copyQueryTime);
        } else {
            copy.queryTime = null;
        }
        if (this.isSetRequestUrl()) {
            String sourceRequestUrl;
            sourceRequestUrl = this.getRequestUrl();
            String copyRequestUrl = ((String) copyBuilder.copy(sourceRequestUrl));
            copy.setRequestUrl(copyRequestUrl);
        } else {
            copy.requestUrl = null;
        }
        if (this.isSetRequestType()) {
            CorinaRequestType sourceRequestType;
            sourceRequestType = this.getRequestType();
            CorinaRequestType copyRequestType = ((CorinaRequestType) copyBuilder.copy(sourceRequestType));
            copy.setRequestType(copyRequestType);
        } else {
            copy.requestType = null;
        }
        if (this.isSetStatus()) {
            CorinaRequestStatus sourceStatus;
            sourceStatus = this.getStatus();
            CorinaRequestStatus copyStatus = ((CorinaRequestStatus) copyBuilder.copy(sourceStatus));
            copy.setStatus(copyStatus);
        } else {
            copy.status = null;
        }
        if (this.isSetMessages()) {
            List<WSIMessage> sourceMessages;
            sourceMessages = this.getMessages();
            @SuppressWarnings("unchecked")
            List<WSIMessage> copyMessages = ((List<WSIMessage> ) copyBuilder.copy(sourceMessages));
            copy.setMessages(copyMessages);
        } else {
            copy.unsetMessages();
        }
        if (this.isSetTimings()) {
            List<WSIHeader.Timing> sourceTimings;
            sourceTimings = this.getTimings();
            @SuppressWarnings("unchecked")
            List<WSIHeader.Timing> copyTimings = ((List<WSIHeader.Timing> ) copyBuilder.copy(sourceTimings));
            copy.setTimings(copyTimings);
        } else {
            copy.unsetTimings();
        }
        if (this.isSetNonce()) {
            WSINonce sourceNonce;
            sourceNonce = this.getNonce();
            WSINonce copyNonce = ((WSINonce) copyBuilder.copy(sourceNonce));
            copy.setNonce(copyNonce);
        } else {
            copy.nonce = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new WSIHeader();
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
     *       &lt;attribute name="unit">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="seconds"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class QueryTime
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlValue
        protected double value;
        @XmlAttribute(name = "unit")
        protected String unit;

        /**
         * Gets the value of the value property.
         * 
         */
        public double getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         */
        public void setValue(double value) {
            this.value = value;
        }

        public boolean isSetValue() {
            return true;
        }

        /**
         * Gets the value of the unit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUnit() {
            return unit;
        }

        /**
         * Sets the value of the unit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUnit(String value) {
            this.unit = value;
        }

        public boolean isSetUnit() {
            return (this.unit!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSIHeader.QueryTime)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSIHeader.QueryTime that = ((WSIHeader.QueryTime) object);
            equalsBuilder.append(this.getValue(), that.getValue());
            equalsBuilder.append(this.getUnit(), that.getUnit());
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSIHeader.QueryTime)) {
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
            hashCodeBuilder.append(this.getValue());
            hashCodeBuilder.append(this.getUnit());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                double theValue;
                theValue = this.getValue();
                toStringBuilder.append("value", theValue);
            }
            {
                String theUnit;
                theUnit = this.getUnit();
                toStringBuilder.append("unit", theUnit);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSIHeader.QueryTime copy = ((target == null)?((WSIHeader.QueryTime) createCopy()):((WSIHeader.QueryTime) target));
            if (this.isSetValue()) {
                double sourceValue;
                sourceValue = this.getValue();
                double copyValue = copyBuilder.copy(sourceValue);
                copy.setValue(copyValue);
            } else {
            }
            if (this.isSetUnit()) {
                String sourceUnit;
                sourceUnit = this.getUnit();
                String copyUnit = ((String) copyBuilder.copy(sourceUnit));
                copy.setUnit(copyUnit);
            } else {
                copy.unit = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSIHeader.QueryTime();
        }

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
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="firstname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="lastname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SecurityUser
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "username")
        protected String username;
        @XmlAttribute(name = "firstname")
        protected String firstname;
        @XmlAttribute(name = "lastname")
        protected String lastname;

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
         * Gets the value of the username property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUsername() {
            return username;
        }

        /**
         * Sets the value of the username property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUsername(String value) {
            this.username = value;
        }

        public boolean isSetUsername() {
            return (this.username!= null);
        }

        /**
         * Gets the value of the firstname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFirstname() {
            return firstname;
        }

        /**
         * Sets the value of the firstname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFirstname(String value) {
            this.firstname = value;
        }

        public boolean isSetFirstname() {
            return (this.firstname!= null);
        }

        /**
         * Gets the value of the lastname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLastname() {
            return lastname;
        }

        /**
         * Sets the value of the lastname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLastname(String value) {
            this.lastname = value;
        }

        public boolean isSetLastname() {
            return (this.lastname!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSIHeader.SecurityUser)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSIHeader.SecurityUser that = ((WSIHeader.SecurityUser) object);
            equalsBuilder.append(this.getId(), that.getId());
            equalsBuilder.append(this.getUsername(), that.getUsername());
            equalsBuilder.append(this.getFirstname(), that.getFirstname());
            equalsBuilder.append(this.getLastname(), that.getLastname());
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSIHeader.SecurityUser)) {
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
            hashCodeBuilder.append(this.getId());
            hashCodeBuilder.append(this.getUsername());
            hashCodeBuilder.append(this.getFirstname());
            hashCodeBuilder.append(this.getLastname());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                String theId;
                theId = this.getId();
                toStringBuilder.append("id", theId);
            }
            {
                String theUsername;
                theUsername = this.getUsername();
                toStringBuilder.append("username", theUsername);
            }
            {
                String theFirstname;
                theFirstname = this.getFirstname();
                toStringBuilder.append("firstname", theFirstname);
            }
            {
                String theLastname;
                theLastname = this.getLastname();
                toStringBuilder.append("lastname", theLastname);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSIHeader.SecurityUser copy = ((target == null)?((WSIHeader.SecurityUser) createCopy()):((WSIHeader.SecurityUser) target));
            if (this.isSetId()) {
                String sourceId;
                sourceId = this.getId();
                String copyId = ((String) copyBuilder.copy(sourceId));
                copy.setId(copyId);
            } else {
                copy.id = null;
            }
            if (this.isSetUsername()) {
                String sourceUsername;
                sourceUsername = this.getUsername();
                String copyUsername = ((String) copyBuilder.copy(sourceUsername));
                copy.setUsername(copyUsername);
            } else {
                copy.username = null;
            }
            if (this.isSetFirstname()) {
                String sourceFirstname;
                sourceFirstname = this.getFirstname();
                String copyFirstname = ((String) copyBuilder.copy(sourceFirstname));
                copy.setFirstname(copyFirstname);
            } else {
                copy.firstname = null;
            }
            if (this.isSetLastname()) {
                String sourceLastname;
                sourceLastname = this.getLastname();
                String copyLastname = ((String) copyBuilder.copy(sourceLastname));
                copy.setLastname(copyLastname);
            } else {
                copy.lastname = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSIHeader.SecurityUser();
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Timing
        implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
    {

        private final static long serialVersionUID = 1001L;
        @XmlValue
        protected String value;
        @XmlAttribute(name = "label")
        protected String label;

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

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLabel() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLabel(String value) {
            this.label = value;
        }

        public boolean isSetLabel() {
            return (this.label!= null);
        }

        public void equals(Object object, EqualsBuilder equalsBuilder) {
            if (!(object instanceof WSIHeader.Timing)) {
                equalsBuilder.appendSuper(false);
                return ;
            }
            if (this == object) {
                return ;
            }
            final WSIHeader.Timing that = ((WSIHeader.Timing) object);
            equalsBuilder.append(this.getValue(), that.getValue());
            equalsBuilder.append(this.getLabel(), that.getLabel());
        }

        public boolean equals(Object object) {
            if (!(object instanceof WSIHeader.Timing)) {
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
            hashCodeBuilder.append(this.getValue());
            hashCodeBuilder.append(this.getLabel());
        }

        public int hashCode() {
            final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
            hashCode(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public void toString(ToStringBuilder toStringBuilder) {
            {
                String theValue;
                theValue = this.getValue();
                toStringBuilder.append("value", theValue);
            }
            {
                String theLabel;
                theLabel = this.getLabel();
                toStringBuilder.append("label", theLabel);
            }
        }

        public String toString() {
            final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
            toString(toStringBuilder);
            return toStringBuilder.toString();
        }

        public Object copyTo(Object target, CopyBuilder copyBuilder) {
            final WSIHeader.Timing copy = ((target == null)?((WSIHeader.Timing) createCopy()):((WSIHeader.Timing) target));
            if (this.isSetValue()) {
                String sourceValue;
                sourceValue = this.getValue();
                String copyValue = ((String) copyBuilder.copy(sourceValue));
                copy.setValue(copyValue);
            } else {
                copy.value = null;
            }
            if (this.isSetLabel()) {
                String sourceLabel;
                sourceLabel = this.getLabel();
                String copyLabel = ((String) copyBuilder.copy(sourceLabel));
                copy.setLabel(copyLabel);
            } else {
                copy.label = null;
            }
            return copy;
        }

        public Object copyTo(Object target) {
            final CopyBuilder copyBuilder = new JAXBCopyBuilder();
            return copyTo(target, copyBuilder);
        }

        public Object createCopy() {
            return new WSIHeader.Timing();
        }

    }

}
