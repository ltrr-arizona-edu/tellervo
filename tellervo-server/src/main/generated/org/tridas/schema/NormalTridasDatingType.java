
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasDatingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasDatingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="absolute"/>
 *     &lt;enumeration value="dated with uncertainty"/>
 *     &lt;enumeration value="relative"/>
 *     &lt;enumeration value="radiocarbon"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasDatingType")
@XmlEnum
public enum NormalTridasDatingType {

    @XmlEnumValue("absolute")
    ABSOLUTE("absolute"),
    @XmlEnumValue("dated with uncertainty")
    DATED___WITH___UNCERTAINTY("dated with uncertainty"),
    @XmlEnumValue("relative")
    RELATIVE("relative"),
    @XmlEnumValue("radiocarbon")
    RADIOCARBON("radiocarbon");
    private final String value;

    NormalTridasDatingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasDatingType fromValue(String v) {
        for (NormalTridasDatingType c: NormalTridasDatingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
