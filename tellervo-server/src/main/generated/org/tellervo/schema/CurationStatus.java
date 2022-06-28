
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for curationStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="curationStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Archived"/>
 *     &lt;enumeration value="On loan"/>
 *     &lt;enumeration value="Active research"/>
 *     &lt;enumeration value="Missing"/>
 *     &lt;enumeration value="On display"/>
 *     &lt;enumeration value="Destroyed"/>
 *     &lt;enumeration value="Returned to owner"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "curationStatus")
@XmlEnum
public enum CurationStatus {

    @XmlEnumValue("Archived")
    ARCHIVED("Archived"),
    @XmlEnumValue("On loan")
    ON___LOAN("On loan"),
    @XmlEnumValue("Active research")
    ACTIVE___RESEARCH("Active research"),
    @XmlEnumValue("Missing")
    MISSING("Missing"),
    @XmlEnumValue("On display")
    ON___DISPLAY("On display"),
    @XmlEnumValue("Destroyed")
    DESTROYED("Destroyed"),
    @XmlEnumValue("Returned to owner")
    RETURNED___TO___OWNER("Returned to owner");
    private final String value;

    CurationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurationStatus fromValue(String v) {
        for (CurationStatus c: CurationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
