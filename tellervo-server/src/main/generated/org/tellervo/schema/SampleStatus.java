
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sampleStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sampleStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Prepped"/>
 *     &lt;enumeration value="Unprepped"/>
 *     &lt;enumeration value="Undateable"/>
 *     &lt;enumeration value="Dated"/>
 *     &lt;enumeration value="Measured"/>
 *     &lt;enumeration value="Too few rings"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sampleStatus")
@XmlEnum
public enum SampleStatus {

    @XmlEnumValue("Prepped")
    PREPPED("Prepped"),
    @XmlEnumValue("Unprepped")
    UNPREPPED("Unprepped"),
    @XmlEnumValue("Undateable")
    UNDATEABLE("Undateable"),
    @XmlEnumValue("Dated")
    DATED("Dated"),
    @XmlEnumValue("Measured")
    MEASURED("Measured"),
    @XmlEnumValue("Too few rings")
    TOO___FEW___RINGS("Too few rings");
    private final String value;

    SampleStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SampleStatus fromValue(String v) {
        for (SampleStatus c: SampleStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
