
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tellervoRequestStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tellervoRequestStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="Warning"/>
 *     &lt;enumeration value="Notice"/>
 *     &lt;enumeration value="Error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tellervoRequestStatus")
@XmlEnum
public enum TellervoRequestStatus {

    OK("OK"),
    @XmlEnumValue("Warning")
    WARNING("Warning"),
    @XmlEnumValue("Notice")
    NOTICE("Notice"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    TellervoRequestStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TellervoRequestStatus fromValue(String v) {
        for (TellervoRequestStatus c: TellervoRequestStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
