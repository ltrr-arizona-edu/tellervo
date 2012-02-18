
package org.tellervo.schema;

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
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}securityUser" minOccurs="0"/>
 *         &lt;element name="webserviceVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="requestType" type="{http://www.tellervo.org/schema/1.0}tellervoRequestType"/>
 *         &lt;element name="status" type="{http://www.tellervo.org/schema/1.0}tellervoRequestStatus"/>
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}message" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timing" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.tellervo.org/schema/1.0}nonce" minOccurs="0"/>
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
    "webserviceVersion",
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
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSISecurityUser securityUser;
    @XmlElement(required = true)
    protected String webserviceVersion;
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
    protected TellervoRequestType requestType;
    @XmlElement(required = true)
    protected TellervoRequestStatus status;
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
     *     {@link WSISecurityUser }
     *     
     */
    public WSISecurityUser getSecurityUser() {
        return securityUser;
    }

    /**
     * Sets the value of the securityUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSISecurityUser }
     *     
     */
    public void setSecurityUser(WSISecurityUser value) {
        this.securityUser = value;
    }

    public boolean isSetSecurityUser() {
        return (this.securityUser!= null);
    }

    /**
     * Gets the value of the webserviceVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebserviceVersion() {
        return webserviceVersion;
    }

    /**
     * Sets the value of the webserviceVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebserviceVersion(String value) {
        this.webserviceVersion = value;
    }

    public boolean isSetWebserviceVersion() {
        return (this.webserviceVersion!= null);
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
     *     {@link TellervoRequestType }
     *     
     */
    public TellervoRequestType getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TellervoRequestType }
     *     
     */
    public void setRequestType(TellervoRequestType value) {
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
     *     {@link TellervoRequestStatus }
     *     
     */
    public TellervoRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link TellervoRequestStatus }
     *     
     */
    public void setStatus(TellervoRequestStatus value) {
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
            WSISecurityUser theSecurityUser;
            theSecurityUser = this.getSecurityUser();
            strategy.appendField(locator, this, "securityUser", buffer, theSecurityUser);
        }
        {
            String theWebserviceVersion;
            theWebserviceVersion = this.getWebserviceVersion();
            strategy.appendField(locator, this, "webserviceVersion", buffer, theWebserviceVersion);
        }
        {
            String theClientVersion;
            theClientVersion = this.getClientVersion();
            strategy.appendField(locator, this, "clientVersion", buffer, theClientVersion);
        }
        {
            XMLGregorianCalendar theRequestDate;
            theRequestDate = this.getRequestDate();
            strategy.appendField(locator, this, "requestDate", buffer, theRequestDate);
        }
        {
            WSIHeader.QueryTime theQueryTime;
            theQueryTime = this.getQueryTime();
            strategy.appendField(locator, this, "queryTime", buffer, theQueryTime);
        }
        {
            String theRequestUrl;
            theRequestUrl = this.getRequestUrl();
            strategy.appendField(locator, this, "requestUrl", buffer, theRequestUrl);
        }
        {
            TellervoRequestType theRequestType;
            theRequestType = this.getRequestType();
            strategy.appendField(locator, this, "requestType", buffer, theRequestType);
        }
        {
            TellervoRequestStatus theStatus;
            theStatus = this.getStatus();
            strategy.appendField(locator, this, "status", buffer, theStatus);
        }
        {
            List<WSIMessage> theMessages;
            theMessages = (this.isSetMessages()?this.getMessages():null);
            strategy.appendField(locator, this, "messages", buffer, theMessages);
        }
        {
            List<WSIHeader.Timing> theTimings;
            theTimings = (this.isSetTimings()?this.getTimings():null);
            strategy.appendField(locator, this, "timings", buffer, theTimings);
        }
        {
            WSINonce theNonce;
            theNonce = this.getNonce();
            strategy.appendField(locator, this, "nonce", buffer, theNonce);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIHeader)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIHeader that = ((WSIHeader) object);
        {
            WSISecurityUser lhsSecurityUser;
            lhsSecurityUser = this.getSecurityUser();
            WSISecurityUser rhsSecurityUser;
            rhsSecurityUser = that.getSecurityUser();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "securityUser", lhsSecurityUser), LocatorUtils.property(thatLocator, "securityUser", rhsSecurityUser), lhsSecurityUser, rhsSecurityUser)) {
                return false;
            }
        }
        {
            String lhsWebserviceVersion;
            lhsWebserviceVersion = this.getWebserviceVersion();
            String rhsWebserviceVersion;
            rhsWebserviceVersion = that.getWebserviceVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "webserviceVersion", lhsWebserviceVersion), LocatorUtils.property(thatLocator, "webserviceVersion", rhsWebserviceVersion), lhsWebserviceVersion, rhsWebserviceVersion)) {
                return false;
            }
        }
        {
            String lhsClientVersion;
            lhsClientVersion = this.getClientVersion();
            String rhsClientVersion;
            rhsClientVersion = that.getClientVersion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "clientVersion", lhsClientVersion), LocatorUtils.property(thatLocator, "clientVersion", rhsClientVersion), lhsClientVersion, rhsClientVersion)) {
                return false;
            }
        }
        {
            XMLGregorianCalendar lhsRequestDate;
            lhsRequestDate = this.getRequestDate();
            XMLGregorianCalendar rhsRequestDate;
            rhsRequestDate = that.getRequestDate();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestDate", lhsRequestDate), LocatorUtils.property(thatLocator, "requestDate", rhsRequestDate), lhsRequestDate, rhsRequestDate)) {
                return false;
            }
        }
        {
            WSIHeader.QueryTime lhsQueryTime;
            lhsQueryTime = this.getQueryTime();
            WSIHeader.QueryTime rhsQueryTime;
            rhsQueryTime = that.getQueryTime();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "queryTime", lhsQueryTime), LocatorUtils.property(thatLocator, "queryTime", rhsQueryTime), lhsQueryTime, rhsQueryTime)) {
                return false;
            }
        }
        {
            String lhsRequestUrl;
            lhsRequestUrl = this.getRequestUrl();
            String rhsRequestUrl;
            rhsRequestUrl = that.getRequestUrl();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestUrl", lhsRequestUrl), LocatorUtils.property(thatLocator, "requestUrl", rhsRequestUrl), lhsRequestUrl, rhsRequestUrl)) {
                return false;
            }
        }
        {
            TellervoRequestType lhsRequestType;
            lhsRequestType = this.getRequestType();
            TellervoRequestType rhsRequestType;
            rhsRequestType = that.getRequestType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "requestType", lhsRequestType), LocatorUtils.property(thatLocator, "requestType", rhsRequestType), lhsRequestType, rhsRequestType)) {
                return false;
            }
        }
        {
            TellervoRequestStatus lhsStatus;
            lhsStatus = this.getStatus();
            TellervoRequestStatus rhsStatus;
            rhsStatus = that.getStatus();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "status", lhsStatus), LocatorUtils.property(thatLocator, "status", rhsStatus), lhsStatus, rhsStatus)) {
                return false;
            }
        }
        {
            List<WSIMessage> lhsMessages;
            lhsMessages = (this.isSetMessages()?this.getMessages():null);
            List<WSIMessage> rhsMessages;
            rhsMessages = (that.isSetMessages()?that.getMessages():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "messages", lhsMessages), LocatorUtils.property(thatLocator, "messages", rhsMessages), lhsMessages, rhsMessages)) {
                return false;
            }
        }
        {
            List<WSIHeader.Timing> lhsTimings;
            lhsTimings = (this.isSetTimings()?this.getTimings():null);
            List<WSIHeader.Timing> rhsTimings;
            rhsTimings = (that.isSetTimings()?that.getTimings():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "timings", lhsTimings), LocatorUtils.property(thatLocator, "timings", rhsTimings), lhsTimings, rhsTimings)) {
                return false;
            }
        }
        {
            WSINonce lhsNonce;
            lhsNonce = this.getNonce();
            WSINonce rhsNonce;
            rhsNonce = that.getNonce();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nonce", lhsNonce), LocatorUtils.property(thatLocator, "nonce", rhsNonce), lhsNonce, rhsNonce)) {
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
            WSISecurityUser theSecurityUser;
            theSecurityUser = this.getSecurityUser();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "securityUser", theSecurityUser), currentHashCode, theSecurityUser);
        }
        {
            String theWebserviceVersion;
            theWebserviceVersion = this.getWebserviceVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "webserviceVersion", theWebserviceVersion), currentHashCode, theWebserviceVersion);
        }
        {
            String theClientVersion;
            theClientVersion = this.getClientVersion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "clientVersion", theClientVersion), currentHashCode, theClientVersion);
        }
        {
            XMLGregorianCalendar theRequestDate;
            theRequestDate = this.getRequestDate();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestDate", theRequestDate), currentHashCode, theRequestDate);
        }
        {
            WSIHeader.QueryTime theQueryTime;
            theQueryTime = this.getQueryTime();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "queryTime", theQueryTime), currentHashCode, theQueryTime);
        }
        {
            String theRequestUrl;
            theRequestUrl = this.getRequestUrl();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestUrl", theRequestUrl), currentHashCode, theRequestUrl);
        }
        {
            TellervoRequestType theRequestType;
            theRequestType = this.getRequestType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "requestType", theRequestType), currentHashCode, theRequestType);
        }
        {
            TellervoRequestStatus theStatus;
            theStatus = this.getStatus();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "status", theStatus), currentHashCode, theStatus);
        }
        {
            List<WSIMessage> theMessages;
            theMessages = (this.isSetMessages()?this.getMessages():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "messages", theMessages), currentHashCode, theMessages);
        }
        {
            List<WSIHeader.Timing> theTimings;
            theTimings = (this.isSetTimings()?this.getTimings():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "timings", theTimings), currentHashCode, theTimings);
        }
        {
            WSINonce theNonce;
            theNonce = this.getNonce();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nonce", theNonce), currentHashCode, theNonce);
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
        if (draftCopy instanceof WSIHeader) {
            final WSIHeader copy = ((WSIHeader) draftCopy);
            if (this.isSetSecurityUser()) {
                WSISecurityUser sourceSecurityUser;
                sourceSecurityUser = this.getSecurityUser();
                WSISecurityUser copySecurityUser = ((WSISecurityUser) strategy.copy(LocatorUtils.property(locator, "securityUser", sourceSecurityUser), sourceSecurityUser));
                copy.setSecurityUser(copySecurityUser);
            } else {
                copy.securityUser = null;
            }
            if (this.isSetWebserviceVersion()) {
                String sourceWebserviceVersion;
                sourceWebserviceVersion = this.getWebserviceVersion();
                String copyWebserviceVersion = ((String) strategy.copy(LocatorUtils.property(locator, "webserviceVersion", sourceWebserviceVersion), sourceWebserviceVersion));
                copy.setWebserviceVersion(copyWebserviceVersion);
            } else {
                copy.webserviceVersion = null;
            }
            if (this.isSetClientVersion()) {
                String sourceClientVersion;
                sourceClientVersion = this.getClientVersion();
                String copyClientVersion = ((String) strategy.copy(LocatorUtils.property(locator, "clientVersion", sourceClientVersion), sourceClientVersion));
                copy.setClientVersion(copyClientVersion);
            } else {
                copy.clientVersion = null;
            }
            if (this.isSetRequestDate()) {
                XMLGregorianCalendar sourceRequestDate;
                sourceRequestDate = this.getRequestDate();
                XMLGregorianCalendar copyRequestDate = ((XMLGregorianCalendar) strategy.copy(LocatorUtils.property(locator, "requestDate", sourceRequestDate), sourceRequestDate));
                copy.setRequestDate(copyRequestDate);
            } else {
                copy.requestDate = null;
            }
            if (this.isSetQueryTime()) {
                WSIHeader.QueryTime sourceQueryTime;
                sourceQueryTime = this.getQueryTime();
                WSIHeader.QueryTime copyQueryTime = ((WSIHeader.QueryTime) strategy.copy(LocatorUtils.property(locator, "queryTime", sourceQueryTime), sourceQueryTime));
                copy.setQueryTime(copyQueryTime);
            } else {
                copy.queryTime = null;
            }
            if (this.isSetRequestUrl()) {
                String sourceRequestUrl;
                sourceRequestUrl = this.getRequestUrl();
                String copyRequestUrl = ((String) strategy.copy(LocatorUtils.property(locator, "requestUrl", sourceRequestUrl), sourceRequestUrl));
                copy.setRequestUrl(copyRequestUrl);
            } else {
                copy.requestUrl = null;
            }
            if (this.isSetRequestType()) {
                TellervoRequestType sourceRequestType;
                sourceRequestType = this.getRequestType();
                TellervoRequestType copyRequestType = ((TellervoRequestType) strategy.copy(LocatorUtils.property(locator, "requestType", sourceRequestType), sourceRequestType));
                copy.setRequestType(copyRequestType);
            } else {
                copy.requestType = null;
            }
            if (this.isSetStatus()) {
                TellervoRequestStatus sourceStatus;
                sourceStatus = this.getStatus();
                TellervoRequestStatus copyStatus = ((TellervoRequestStatus) strategy.copy(LocatorUtils.property(locator, "status", sourceStatus), sourceStatus));
                copy.setStatus(copyStatus);
            } else {
                copy.status = null;
            }
            if (this.isSetMessages()) {
                List<WSIMessage> sourceMessages;
                sourceMessages = (this.isSetMessages()?this.getMessages():null);
                @SuppressWarnings("unchecked")
                List<WSIMessage> copyMessages = ((List<WSIMessage> ) strategy.copy(LocatorUtils.property(locator, "messages", sourceMessages), sourceMessages));
                copy.unsetMessages();
                List<WSIMessage> uniqueMessagesl = copy.getMessages();
                uniqueMessagesl.addAll(copyMessages);
            } else {
                copy.unsetMessages();
            }
            if (this.isSetTimings()) {
                List<WSIHeader.Timing> sourceTimings;
                sourceTimings = (this.isSetTimings()?this.getTimings():null);
                @SuppressWarnings("unchecked")
                List<WSIHeader.Timing> copyTimings = ((List<WSIHeader.Timing> ) strategy.copy(LocatorUtils.property(locator, "timings", sourceTimings), sourceTimings));
                copy.unsetTimings();
                List<WSIHeader.Timing> uniqueTimingsl = copy.getTimings();
                uniqueTimingsl.addAll(copyTimings);
            } else {
                copy.unsetTimings();
            }
            if (this.isSetNonce()) {
                WSINonce sourceNonce;
                sourceNonce = this.getNonce();
                WSINonce copyNonce = ((WSINonce) strategy.copy(LocatorUtils.property(locator, "nonce", sourceNonce), sourceNonce));
                copy.setNonce(copyNonce);
            } else {
                copy.nonce = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIHeader();
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
        implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
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
                double theValue;
                theValue = (this.isSetValue()?this.getValue(): 0.0D);
                strategy.appendField(locator, this, "value", buffer, theValue);
            }
            {
                String theUnit;
                theUnit = this.getUnit();
                strategy.appendField(locator, this, "unit", buffer, theUnit);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSIHeader.QueryTime)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final WSIHeader.QueryTime that = ((WSIHeader.QueryTime) object);
            {
                double lhsValue;
                lhsValue = (this.isSetValue()?this.getValue(): 0.0D);
                double rhsValue;
                rhsValue = (that.isSetValue()?that.getValue(): 0.0D);
                if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
                    return false;
                }
            }
            {
                String lhsUnit;
                lhsUnit = this.getUnit();
                String rhsUnit;
                rhsUnit = that.getUnit();
                if (!strategy.equals(LocatorUtils.property(thisLocator, "unit", lhsUnit), LocatorUtils.property(thatLocator, "unit", rhsUnit), lhsUnit, rhsUnit)) {
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
                double theValue;
                theValue = (this.isSetValue()?this.getValue(): 0.0D);
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
            }
            {
                String theUnit;
                theUnit = this.getUnit();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "unit", theUnit), currentHashCode, theUnit);
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
            if (draftCopy instanceof WSIHeader.QueryTime) {
                final WSIHeader.QueryTime copy = ((WSIHeader.QueryTime) draftCopy);
                if (this.isSetValue()) {
                    double sourceValue;
                    sourceValue = (this.isSetValue()?this.getValue(): 0.0D);
                    double copyValue = strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue);
                    copy.setValue(copyValue);
                } else {
                }
                if (this.isSetUnit()) {
                    String sourceUnit;
                    sourceUnit = this.getUnit();
                    String copyUnit = ((String) strategy.copy(LocatorUtils.property(locator, "unit", sourceUnit), sourceUnit));
                    copy.setUnit(copyUnit);
                } else {
                    copy.unit = null;
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
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
        implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
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
                String theValue;
                theValue = this.getValue();
                strategy.appendField(locator, this, "value", buffer, theValue);
            }
            {
                String theLabel;
                theLabel = this.getLabel();
                strategy.appendField(locator, this, "label", buffer, theLabel);
            }
            return buffer;
        }

        public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
            if (!(object instanceof WSIHeader.Timing)) {
                return false;
            }
            if (this == object) {
                return true;
            }
            final WSIHeader.Timing that = ((WSIHeader.Timing) object);
            {
                String lhsValue;
                lhsValue = this.getValue();
                String rhsValue;
                rhsValue = that.getValue();
                if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
                    return false;
                }
            }
            {
                String lhsLabel;
                lhsLabel = this.getLabel();
                String rhsLabel;
                rhsLabel = that.getLabel();
                if (!strategy.equals(LocatorUtils.property(thisLocator, "label", lhsLabel), LocatorUtils.property(thatLocator, "label", rhsLabel), lhsLabel, rhsLabel)) {
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
                String theValue;
                theValue = this.getValue();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
            }
            {
                String theLabel;
                theLabel = this.getLabel();
                currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "label", theLabel), currentHashCode, theLabel);
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
            if (draftCopy instanceof WSIHeader.Timing) {
                final WSIHeader.Timing copy = ((WSIHeader.Timing) draftCopy);
                if (this.isSetValue()) {
                    String sourceValue;
                    sourceValue = this.getValue();
                    String copyValue = ((String) strategy.copy(LocatorUtils.property(locator, "value", sourceValue), sourceValue));
                    copy.setValue(copyValue);
                } else {
                    copy.value = null;
                }
                if (this.isSetLabel()) {
                    String sourceLabel;
                    sourceLabel = this.getLabel();
                    String copyLabel = ((String) strategy.copy(LocatorUtils.property(locator, "label", sourceLabel), sourceLabel));
                    copy.setLabel(copyLabel);
                } else {
                    copy.label = null;
                }
            }
            return draftCopy;
        }

        public Object createNewInstance() {
            return new WSIHeader.Timing();
        }

    }

}
