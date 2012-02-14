
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tellervoRequestFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tellervoRequestFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="standard"/>
 *     &lt;enumeration value="summary"/>
 *     &lt;enumeration value="comprehensive"/>
 *     &lt;enumeration value="minimal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tellervoRequestFormat")
@XmlEnum
public enum TellervoRequestFormat {

    @XmlEnumValue("standard")
    STANDARD("standard"),
    @XmlEnumValue("summary")
    SUMMARY("summary"),
    @XmlEnumValue("comprehensive")
    COMPREHENSIVE("comprehensive"),
    @XmlEnumValue("minimal")
    MINIMAL("minimal");
    private final String value;

    TellervoRequestFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TellervoRequestFormat fromValue(String v) {
        for (TellervoRequestFormat c: TellervoRequestFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
