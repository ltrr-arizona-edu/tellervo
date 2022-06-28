
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tellervoRequestType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tellervoRequestType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="create"/>
 *     &lt;enumeration value="read"/>
 *     &lt;enumeration value="update"/>
 *     &lt;enumeration value="delete"/>
 *     &lt;enumeration value="plainlogin"/>
 *     &lt;enumeration value="securelogin"/>
 *     &lt;enumeration value="nonce"/>
 *     &lt;enumeration value="setpassword"/>
 *     &lt;enumeration value="logout"/>
 *     &lt;enumeration value="help"/>
 *     &lt;enumeration value="search"/>
 *     &lt;enumeration value="assign"/>
 *     &lt;enumeration value="unassign"/>
 *     &lt;enumeration value="merge"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tellervoRequestType")
@XmlEnum
public enum TellervoRequestType {

    @XmlEnumValue("create")
    CREATE("create"),
    @XmlEnumValue("read")
    READ("read"),
    @XmlEnumValue("update")
    UPDATE("update"),
    @XmlEnumValue("delete")
    DELETE("delete"),
    @XmlEnumValue("plainlogin")
    PLAINLOGIN("plainlogin"),
    @XmlEnumValue("securelogin")
    SECURELOGIN("securelogin"),
    @XmlEnumValue("nonce")
    NONCE("nonce"),
    @XmlEnumValue("setpassword")
    SETPASSWORD("setpassword"),
    @XmlEnumValue("logout")
    LOGOUT("logout"),
    @XmlEnumValue("help")
    HELP("help"),
    @XmlEnumValue("search")
    SEARCH("search"),
    @XmlEnumValue("assign")
    ASSIGN("assign"),
    @XmlEnumValue("unassign")
    UNASSIGN("unassign"),
    @XmlEnumValue("merge")
    MERGE("merge");
    private final String value;

    TellervoRequestType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TellervoRequestType fromValue(String v) {
        for (TellervoRequestType c: TellervoRequestType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
