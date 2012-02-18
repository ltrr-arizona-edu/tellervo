
package org.tellervo.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Java class for tellervo element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="tellervo">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element ref="{http://www.tellervo.org/schema/1.0}request"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element ref="{http://www.tellervo.org/schema/1.0}header"/>
 *             &lt;choice>
 *               &lt;element ref="{http://www.tellervo.org/schema/1.0}content" minOccurs="0"/>
 *               &lt;element ref="{http://www.tellervo.org/schema/1.0}help" minOccurs="0"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "help",
    "content",
    "request"
})
@XmlRootElement(name = "tellervo")
public class WSIRootElement
    implements Serializable, Cloneable, CopyTo, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected WSIHeader header;
    protected WSIHelp help;
    protected WSIContent content;
    protected WSIRequest request;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHeader }
     *     
     */
    public WSIHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHeader }
     *     
     */
    public void setHeader(WSIHeader value) {
        this.header = value;
    }

    public boolean isSetHeader() {
        return (this.header!= null);
    }

    /**
     * Gets the value of the help property.
     * 
     * @return
     *     possible object is
     *     {@link WSIHelp }
     *     
     */
    public WSIHelp getHelp() {
        return help;
    }

    /**
     * Sets the value of the help property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIHelp }
     *     
     */
    public void setHelp(WSIHelp value) {
        this.help = value;
    }

    public boolean isSetHelp() {
        return (this.help!= null);
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link WSIContent }
     *     
     */
    public WSIContent getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIContent }
     *     
     */
    public void setContent(WSIContent value) {
        this.content = value;
    }

    public boolean isSetContent() {
        return (this.content!= null);
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link WSIRequest }
     *     
     */
    public WSIRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSIRequest }
     *     
     */
    public void setRequest(WSIRequest value) {
        this.request = value;
    }

    public boolean isSetRequest() {
        return (this.request!= null);
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
            WSIHeader theHeader;
            theHeader = this.getHeader();
            strategy.appendField(locator, this, "header", buffer, theHeader);
        }
        {
            WSIHelp theHelp;
            theHelp = this.getHelp();
            strategy.appendField(locator, this, "help", buffer, theHelp);
        }
        {
            WSIContent theContent;
            theContent = this.getContent();
            strategy.appendField(locator, this, "content", buffer, theContent);
        }
        {
            WSIRequest theRequest;
            theRequest = this.getRequest();
            strategy.appendField(locator, this, "request", buffer, theRequest);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof WSIRootElement)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final WSIRootElement that = ((WSIRootElement) object);
        {
            WSIHeader lhsHeader;
            lhsHeader = this.getHeader();
            WSIHeader rhsHeader;
            rhsHeader = that.getHeader();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "header", lhsHeader), LocatorUtils.property(thatLocator, "header", rhsHeader), lhsHeader, rhsHeader)) {
                return false;
            }
        }
        {
            WSIHelp lhsHelp;
            lhsHelp = this.getHelp();
            WSIHelp rhsHelp;
            rhsHelp = that.getHelp();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "help", lhsHelp), LocatorUtils.property(thatLocator, "help", rhsHelp), lhsHelp, rhsHelp)) {
                return false;
            }
        }
        {
            WSIContent lhsContent;
            lhsContent = this.getContent();
            WSIContent rhsContent;
            rhsContent = that.getContent();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "content", lhsContent), LocatorUtils.property(thatLocator, "content", rhsContent), lhsContent, rhsContent)) {
                return false;
            }
        }
        {
            WSIRequest lhsRequest;
            lhsRequest = this.getRequest();
            WSIRequest rhsRequest;
            rhsRequest = that.getRequest();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "request", lhsRequest), LocatorUtils.property(thatLocator, "request", rhsRequest), lhsRequest, rhsRequest)) {
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
            WSIHeader theHeader;
            theHeader = this.getHeader();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "header", theHeader), currentHashCode, theHeader);
        }
        {
            WSIHelp theHelp;
            theHelp = this.getHelp();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "help", theHelp), currentHashCode, theHelp);
        }
        {
            WSIContent theContent;
            theContent = this.getContent();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "content", theContent), currentHashCode, theContent);
        }
        {
            WSIRequest theRequest;
            theRequest = this.getRequest();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "request", theRequest), currentHashCode, theRequest);
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
        if (draftCopy instanceof WSIRootElement) {
            final WSIRootElement copy = ((WSIRootElement) draftCopy);
            if (this.isSetHeader()) {
                WSIHeader sourceHeader;
                sourceHeader = this.getHeader();
                WSIHeader copyHeader = ((WSIHeader) strategy.copy(LocatorUtils.property(locator, "header", sourceHeader), sourceHeader));
                copy.setHeader(copyHeader);
            } else {
                copy.header = null;
            }
            if (this.isSetHelp()) {
                WSIHelp sourceHelp;
                sourceHelp = this.getHelp();
                WSIHelp copyHelp = ((WSIHelp) strategy.copy(LocatorUtils.property(locator, "help", sourceHelp), sourceHelp));
                copy.setHelp(copyHelp);
            } else {
                copy.help = null;
            }
            if (this.isSetContent()) {
                WSIContent sourceContent;
                sourceContent = this.getContent();
                WSIContent copyContent = ((WSIContent) strategy.copy(LocatorUtils.property(locator, "content", sourceContent), sourceContent));
                copy.setContent(copyContent);
            } else {
                copy.content = null;
            }
            if (this.isSetRequest()) {
                WSIRequest sourceRequest;
                sourceRequest = this.getRequest();
                WSIRequest copyRequest = ((WSIRequest) strategy.copy(LocatorUtils.property(locator, "request", sourceRequest), sourceRequest));
                copy.setRequest(copyRequest);
            } else {
                copy.request = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new WSIRootElement();
    }

}
