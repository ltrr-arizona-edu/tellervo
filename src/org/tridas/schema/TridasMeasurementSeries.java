//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-793 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.08 at 01:25:56 PM PDT 
//


package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.tridas.org/1.2}baseSeries">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2}title"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}identifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}createdTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}lastModifiedTimestamp" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}analyst" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}dendrochronologist" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}measuringMethod"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}comments" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}usage" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}usageComments" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{http://www.tridas.org/1.2}interpretationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}genericField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}values" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "measurementSeries")
public class TridasMeasurementSeries
    extends BaseSeries
{


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
