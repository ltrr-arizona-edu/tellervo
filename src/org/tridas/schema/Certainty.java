
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for certainty.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="certainty">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="unknown"/>
 *     &lt;enumeration value="exact"/>
 *     &lt;enumeration value="approximately"/>
 *     &lt;enumeration value="after"/>
 *     &lt;enumeration value="before"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "certainty")
@XmlEnum
public enum Certainty {

    @XmlEnumValue("unknown")
    UNKNOWN("unknown"),
    @XmlEnumValue("exact")
    EXACT("exact"),
    @XmlEnumValue("approximately")
    APPROXIMATELY("approximately"),
    @XmlEnumValue("after")
    AFTER("after"),
    @XmlEnumValue("before")
    BEFORE("before");
    private final String value;

    Certainty(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Certainty fromValue(String v) {
        for (Certainty c: Certainty.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
