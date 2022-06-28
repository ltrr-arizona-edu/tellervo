
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchOperator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="searchOperator">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value=">"/>
 *     &lt;enumeration value="&lt;"/>
 *     &lt;enumeration value="="/>
 *     &lt;enumeration value="!="/>
 *     &lt;enumeration value="like"/>
 *     &lt;enumeration value="is"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "searchOperator")
@XmlEnum
public enum SearchOperator {

    @XmlEnumValue(">")
    GREATER_THAN(">"),
    @XmlEnumValue("<")
    LESS_THAN("<"),
    @XmlEnumValue("=")
    EQUALS("="),
    @XmlEnumValue("!=")
    NOT_EQUALS("!="),
    @XmlEnumValue("like")
    LIKE("like"),
    @XmlEnumValue("is")
    IS("is");
    private final String value;

    SearchOperator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SearchOperator fromValue(String v) {
        for (SearchOperator c: SearchOperator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
