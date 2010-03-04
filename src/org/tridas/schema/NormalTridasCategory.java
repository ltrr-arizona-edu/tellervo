
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value=""/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasCategory")
@XmlEnum
public enum NormalTridasCategory {

    @XmlEnumValue("")
    VALUE_1("");
    private final String value;

    NormalTridasCategory(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasCategory fromValue(String v) {
        for (NormalTridasCategory c: NormalTridasCategory.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
