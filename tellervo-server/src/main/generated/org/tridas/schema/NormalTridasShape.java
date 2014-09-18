
package org.tridas.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for normalTridasShape.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="normalTridasShape">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="whole section"/>
 *     &lt;enumeration value="half section"/>
 *     &lt;enumeration value="third section"/>
 *     &lt;enumeration value="quarter section"/>
 *     &lt;enumeration value="wedge where radius is smaller than circumference"/>
 *     &lt;enumeration value="wedge where radius equals the circumference"/>
 *     &lt;enumeration value="wedge where radius is bigger than the circumference"/>
 *     &lt;enumeration value="beam straightened on one side"/>
 *     &lt;enumeration value="squared beam from whole section"/>
 *     &lt;enumeration value="squared beam from half section"/>
 *     &lt;enumeration value="squared beam from quarter section"/>
 *     &lt;enumeration value="plank cut on one side"/>
 *     &lt;enumeration value="radial plank through pith"/>
 *     &lt;enumeration value="radial plank up to pith"/>
 *     &lt;enumeration value="tangential plank not including pith with breadth larger than a quarter section"/>
 *     &lt;enumeration value="plank not including pith with breadth smaller than a quarter section"/>
 *     &lt;enumeration value="small part of section"/>
 *     &lt;enumeration value="part of undetermined section"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "normalTridasShape")
@XmlEnum
public enum NormalTridasShape {

    @XmlEnumValue("whole section")
    WHOLE___SECTION("whole section"),
    @XmlEnumValue("half section")
    HALF___SECTION("half section"),
    @XmlEnumValue("third section")
    THIRD___SECTION("third section"),
    @XmlEnumValue("quarter section")
    QUARTER___SECTION("quarter section"),
    @XmlEnumValue("wedge where radius is smaller than circumference")
    WEDGE___WHERE___RADIUS___IS___SMALLER___THAN___CIRCUMFERENCE("wedge where radius is smaller than circumference"),
    @XmlEnumValue("wedge where radius equals the circumference")
    WEDGE___WHERE___RADIUS___EQUALS___THE___CIRCUMFERENCE("wedge where radius equals the circumference"),
    @XmlEnumValue("wedge where radius is bigger than the circumference")
    WEDGE___WHERE___RADIUS___IS___BIGGER___THAN___THE___CIRCUMFERENCE("wedge where radius is bigger than the circumference"),
    @XmlEnumValue("beam straightened on one side")
    BEAM___STRAIGHTENED___ON___ONE___SIDE("beam straightened on one side"),
    @XmlEnumValue("squared beam from whole section")
    SQUARED___BEAM___FROM___WHOLE___SECTION("squared beam from whole section"),
    @XmlEnumValue("squared beam from half section")
    SQUARED___BEAM___FROM___HALF___SECTION("squared beam from half section"),
    @XmlEnumValue("squared beam from quarter section")
    SQUARED___BEAM___FROM___QUARTER___SECTION("squared beam from quarter section"),
    @XmlEnumValue("plank cut on one side")
    PLANK___CUT___ON___ONE___SIDE("plank cut on one side"),
    @XmlEnumValue("radial plank through pith")
    RADIAL___PLANK___THROUGH___PITH("radial plank through pith"),
    @XmlEnumValue("radial plank up to pith")
    RADIAL___PLANK___UP___TO___PITH("radial plank up to pith"),
    @XmlEnumValue("tangential plank not including pith with breadth larger than a quarter section")
    TANGENTIAL___PLANK___NOT___INCLUDING___PITH___WITH___BREADTH___LARGER___THAN___A___QUARTER___SECTION("tangential plank not including pith with breadth larger than a quarter section"),
    @XmlEnumValue("plank not including pith with breadth smaller than a quarter section")
    PLANK___NOT___INCLUDING___PITH___WITH___BREADTH___SMALLER___THAN___A___QUARTER___SECTION("plank not including pith with breadth smaller than a quarter section"),
    @XmlEnumValue("small part of section")
    SMALL___PART___OF___SECTION("small part of section"),
    @XmlEnumValue("part of undetermined section")
    PART___OF___UNDETERMINED___SECTION("part of undetermined section"),
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final String value;

    NormalTridasShape(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NormalTridasShape fromValue(String v) {
        for (NormalTridasShape c: NormalTridasShape.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
