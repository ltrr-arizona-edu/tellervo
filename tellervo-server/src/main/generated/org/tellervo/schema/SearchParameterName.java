
package org.tellervo.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for searchParameterName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="searchParameterName">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="objectid"/>
 *     &lt;enumeration value="objectdbid"/>
 *     &lt;enumeration value="objecttitle"/>
 *     &lt;enumeration value="objectcreated"/>
 *     &lt;enumeration value="objectlastmodified"/>
 *     &lt;enumeration value="objectdescription"/>
 *     &lt;enumeration value="objectcreator"/>
 *     &lt;enumeration value="objectowner"/>
 *     &lt;enumeration value="objectfile"/>
 *     &lt;enumeration value="objectcoverageTemporal"/>
 *     &lt;enumeration value="objectcoverageTemporalFoundation"/>
 *     &lt;enumeration value="objectlocationtype"/>
 *     &lt;enumeration value="objectlocationprecision"/>
 *     &lt;enumeration value="objectlocationcomment"/>
 *     &lt;enumeration value="objecttype"/>
 *     &lt;enumeration value="objectcode"/>
 *     &lt;enumeration value="parentobjectid"/>
 *     &lt;enumeration value="objectlocation"/>
 *     &lt;enumeration value="countOfChildSeriesOfObject"/>
 *     &lt;enumeration value="anyparentobjectid"/>
 *     &lt;enumeration value="anyparentobjectcode"/>
 *     &lt;enumeration value="elementid"/>
 *     &lt;enumeration value="elementdbid"/>
 *     &lt;enumeration value="elementoriginaltaxonname"/>
 *     &lt;enumeration value="elementphylumname"/>
 *     &lt;enumeration value="elementclassname"/>
 *     &lt;enumeration value="elementordername"/>
 *     &lt;enumeration value="elementfamilyname"/>
 *     &lt;enumeration value="elementgenusname"/>
 *     &lt;enumeration value="elementspeciesname"/>
 *     &lt;enumeration value="elementinfraspeciesname"/>
 *     &lt;enumeration value="elementinfraspeciestype"/>
 *     &lt;enumeration value="elementauthenticity"/>
 *     &lt;enumeration value="elementshape"/>
 *     &lt;enumeration value="elementheight"/>
 *     &lt;enumeration value="elementwidth"/>
 *     &lt;enumeration value="elementdepth"/>
 *     &lt;enumeration value="elementdiameter"/>
 *     &lt;enumeration value="elementdimensionunits"/>
 *     &lt;enumeration value="elementdimensionunitspower"/>
 *     &lt;enumeration value="elementtype"/>
 *     &lt;enumeration value="elementfile"/>
 *     &lt;enumeration value="elementlocationtype"/>
 *     &lt;enumeration value="elementlocationprecision"/>
 *     &lt;enumeration value="elementlocationcomment"/>
 *     &lt;enumeration value="elementprocessing"/>
 *     &lt;enumeration value="elementmarks"/>
 *     &lt;enumeration value="elementdescription"/>
 *     &lt;enumeration value="elementcreated"/>
 *     &lt;enumeration value="elementlastmodified"/>
 *     &lt;enumeration value="elementcode"/>
 *     &lt;enumeration value="sampleid"/>
 *     &lt;enumeration value="sampledbid"/>
 *     &lt;enumeration value="samplingdate"/>
 *     &lt;enumeration value="samplefile"/>
 *     &lt;enumeration value="sampleposition"/>
 *     &lt;enumeration value="samplestate"/>
 *     &lt;enumeration value="samplehasknots"/>
 *     &lt;enumeration value="sampledescription"/>
 *     &lt;enumeration value="samplecreated"/>
 *     &lt;enumeration value="samplelastmodified"/>
 *     &lt;enumeration value="samplingdatecertainty"/>
 *     &lt;enumeration value="sampleboxid"/>
 *     &lt;enumeration value="samplecode"/>
 *     &lt;enumeration value="samplestatus"/>
 *     &lt;enumeration value="radiusid"/>
 *     &lt;enumeration value="radiusdbid"/>
 *     &lt;enumeration value="radiuspith"/>
 *     &lt;enumeration value="radiussapwood"/>
 *     &lt;enumeration value="radiusheartwood"/>
 *     &lt;enumeration value="radiusbark"/>
 *     &lt;enumeration value="radiusnumbersapwoodrings"/>
 *     &lt;enumeration value="radiuslastringunderbark"/>
 *     &lt;enumeration value="radiusmissingheartwoodringstopith"/>
 *     &lt;enumeration value="radiusmissingheartwoodringstopithfoundation"/>
 *     &lt;enumeration value="radiusmissingsapwoodringstobark"/>
 *     &lt;enumeration value="radiusmissingsapwoodringstobarkfoundation"/>
 *     &lt;enumeration value="radiuscreated"/>
 *     &lt;enumeration value="radiuslastmodified"/>
 *     &lt;enumeration value="radiusazimuth"/>
 *     &lt;enumeration value="radiustitle"/>
 *     &lt;enumeration value="radiuscode"/>
 *     &lt;enumeration value="seriesid"/>
 *     &lt;enumeration value="seriesdbid"/>
 *     &lt;enumeration value="seriesmeasuringmethod"/>
 *     &lt;enumeration value="seriesvariable"/>
 *     &lt;enumeration value="seriesunit"/>
 *     &lt;enumeration value="seriespower"/>
 *     &lt;enumeration value="seriesmeasuringdate"/>
 *     &lt;enumeration value="seriesanalyst"/>
 *     &lt;enumeration value="seriesdendrochronologist"/>
 *     &lt;enumeration value="seriescomments"/>
 *     &lt;enumeration value="seriesfirstyear"/>
 *     &lt;enumeration value="seriessproutyear"/>
 *     &lt;enumeration value="seriesdeathyear"/>
 *     &lt;enumeration value="seriesprovenance"/>
 *     &lt;enumeration value="seriestype"/>
 *     &lt;enumeration value="seriesstandardizingmethod"/>
 *     &lt;enumeration value="seriesauthor"/>
 *     &lt;enumeration value="seriesobjective"/>
 *     &lt;enumeration value="seriesversion"/>
 *     &lt;enumeration value="seriesderivationdate"/>
 *     &lt;enumeration value="serieslastmodified"/>
 *     &lt;enumeration value="seriesoperatorparameter"/>
 *     &lt;enumeration value="seriesisreconciled"/>
 *     &lt;enumeration value="seriesdatingtype"/>
 *     &lt;enumeration value="seriesdatingerrorpositive"/>
 *     &lt;enumeration value="seriesdatingerrornegative"/>
 *     &lt;enumeration value="seriesvaluecount"/>
 *     &lt;enumeration value="seriescount"/>
 *     &lt;enumeration value="seriescode"/>
 *     &lt;enumeration value="dependentseriesid"/>
 *     &lt;enumeration value="boxid"/>
 *     &lt;enumeration value="boxcode"/>
 *     &lt;enumeration value="boxcurationlocation"/>
 *     &lt;enumeration value="boxtrackinglocation"/>
 *     &lt;enumeration value="boxcomments"/>
 *     &lt;enumeration value="boxlastmodified"/>
 *     &lt;enumeration value="boxcreated"/>
 *     &lt;enumeration value="tagtext"/>
 *     &lt;enumeration value="tagid"/>
 *     &lt;enumeration value="loanid"/>
 *     &lt;enumeration value="loanissuedate"/>
 *     &lt;enumeration value="loanduedate"/>
 *     &lt;enumeration value="loanreturndate"/>
 *     &lt;enumeration value="loanfirstname"/>
 *     &lt;enumeration value="loanlastname"/>
 *     &lt;enumeration value="loanorganisation"/>
 *     &lt;enumeration value="loannotes"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "searchParameterName")
@XmlEnum
public enum SearchParameterName {

    @XmlEnumValue("objectid")
    OBJECTID("objectid"),
    @XmlEnumValue("objectdbid")
    OBJECTDBID("objectdbid"),
    @XmlEnumValue("objecttitle")
    OBJECTTITLE("objecttitle"),
    @XmlEnumValue("objectcreated")
    OBJECTCREATED("objectcreated"),
    @XmlEnumValue("objectlastmodified")
    OBJECTLASTMODIFIED("objectlastmodified"),
    @XmlEnumValue("objectdescription")
    OBJECTDESCRIPTION("objectdescription"),
    @XmlEnumValue("objectcreator")
    OBJECTCREATOR("objectcreator"),
    @XmlEnumValue("objectowner")
    OBJECTOWNER("objectowner"),
    @XmlEnumValue("objectfile")
    OBJECTFILE("objectfile"),
    @XmlEnumValue("objectcoverageTemporal")
    OBJECTCOVERAGE_TEMPORAL("objectcoverageTemporal"),
    @XmlEnumValue("objectcoverageTemporalFoundation")
    OBJECTCOVERAGE_TEMPORAL_FOUNDATION("objectcoverageTemporalFoundation"),
    @XmlEnumValue("objectlocationtype")
    OBJECTLOCATIONTYPE("objectlocationtype"),
    @XmlEnumValue("objectlocationprecision")
    OBJECTLOCATIONPRECISION("objectlocationprecision"),
    @XmlEnumValue("objectlocationcomment")
    OBJECTLOCATIONCOMMENT("objectlocationcomment"),
    @XmlEnumValue("objecttype")
    OBJECTTYPE("objecttype"),
    @XmlEnumValue("objectcode")
    OBJECTCODE("objectcode"),
    @XmlEnumValue("parentobjectid")
    PARENTOBJECTID("parentobjectid"),
    @XmlEnumValue("objectlocation")
    OBJECTLOCATION("objectlocation"),
    @XmlEnumValue("countOfChildSeriesOfObject")
    COUNT_OF_CHILD_SERIES_OF_OBJECT("countOfChildSeriesOfObject"),
    @XmlEnumValue("anyparentobjectid")
    ANYPARENTOBJECTID("anyparentobjectid"),
    @XmlEnumValue("anyparentobjectcode")
    ANYPARENTOBJECTCODE("anyparentobjectcode"),
    @XmlEnumValue("elementid")
    ELEMENTID("elementid"),
    @XmlEnumValue("elementdbid")
    ELEMENTDBID("elementdbid"),
    @XmlEnumValue("elementoriginaltaxonname")
    ELEMENTORIGINALTAXONNAME("elementoriginaltaxonname"),
    @XmlEnumValue("elementphylumname")
    ELEMENTPHYLUMNAME("elementphylumname"),
    @XmlEnumValue("elementclassname")
    ELEMENTCLASSNAME("elementclassname"),
    @XmlEnumValue("elementordername")
    ELEMENTORDERNAME("elementordername"),
    @XmlEnumValue("elementfamilyname")
    ELEMENTFAMILYNAME("elementfamilyname"),
    @XmlEnumValue("elementgenusname")
    ELEMENTGENUSNAME("elementgenusname"),
    @XmlEnumValue("elementspeciesname")
    ELEMENTSPECIESNAME("elementspeciesname"),
    @XmlEnumValue("elementinfraspeciesname")
    ELEMENTINFRASPECIESNAME("elementinfraspeciesname"),
    @XmlEnumValue("elementinfraspeciestype")
    ELEMENTINFRASPECIESTYPE("elementinfraspeciestype"),
    @XmlEnumValue("elementauthenticity")
    ELEMENTAUTHENTICITY("elementauthenticity"),
    @XmlEnumValue("elementshape")
    ELEMENTSHAPE("elementshape"),
    @XmlEnumValue("elementheight")
    ELEMENTHEIGHT("elementheight"),
    @XmlEnumValue("elementwidth")
    ELEMENTWIDTH("elementwidth"),
    @XmlEnumValue("elementdepth")
    ELEMENTDEPTH("elementdepth"),
    @XmlEnumValue("elementdiameter")
    ELEMENTDIAMETER("elementdiameter"),
    @XmlEnumValue("elementdimensionunits")
    ELEMENTDIMENSIONUNITS("elementdimensionunits"),
    @XmlEnumValue("elementdimensionunitspower")
    ELEMENTDIMENSIONUNITSPOWER("elementdimensionunitspower"),
    @XmlEnumValue("elementtype")
    ELEMENTTYPE("elementtype"),
    @XmlEnumValue("elementfile")
    ELEMENTFILE("elementfile"),
    @XmlEnumValue("elementlocationtype")
    ELEMENTLOCATIONTYPE("elementlocationtype"),
    @XmlEnumValue("elementlocationprecision")
    ELEMENTLOCATIONPRECISION("elementlocationprecision"),
    @XmlEnumValue("elementlocationcomment")
    ELEMENTLOCATIONCOMMENT("elementlocationcomment"),
    @XmlEnumValue("elementprocessing")
    ELEMENTPROCESSING("elementprocessing"),
    @XmlEnumValue("elementmarks")
    ELEMENTMARKS("elementmarks"),
    @XmlEnumValue("elementdescription")
    ELEMENTDESCRIPTION("elementdescription"),
    @XmlEnumValue("elementcreated")
    ELEMENTCREATED("elementcreated"),
    @XmlEnumValue("elementlastmodified")
    ELEMENTLASTMODIFIED("elementlastmodified"),
    @XmlEnumValue("elementcode")
    ELEMENTCODE("elementcode"),
    @XmlEnumValue("sampleid")
    SAMPLEID("sampleid"),
    @XmlEnumValue("sampledbid")
    SAMPLEDBID("sampledbid"),
    @XmlEnumValue("samplingdate")
    SAMPLINGDATE("samplingdate"),
    @XmlEnumValue("samplefile")
    SAMPLEFILE("samplefile"),
    @XmlEnumValue("sampleposition")
    SAMPLEPOSITION("sampleposition"),
    @XmlEnumValue("samplestate")
    SAMPLESTATE("samplestate"),
    @XmlEnumValue("samplehasknots")
    SAMPLEHASKNOTS("samplehasknots"),
    @XmlEnumValue("sampledescription")
    SAMPLEDESCRIPTION("sampledescription"),
    @XmlEnumValue("samplecreated")
    SAMPLECREATED("samplecreated"),
    @XmlEnumValue("samplelastmodified")
    SAMPLELASTMODIFIED("samplelastmodified"),
    @XmlEnumValue("samplingdatecertainty")
    SAMPLINGDATECERTAINTY("samplingdatecertainty"),
    @XmlEnumValue("sampleboxid")
    SAMPLEBOXID("sampleboxid"),
    @XmlEnumValue("samplecode")
    SAMPLECODE("samplecode"),
    @XmlEnumValue("samplestatus")
    SAMPLESTATUS("samplestatus"),
    @XmlEnumValue("radiusid")
    RADIUSID("radiusid"),
    @XmlEnumValue("radiusdbid")
    RADIUSDBID("radiusdbid"),
    @XmlEnumValue("radiuspith")
    RADIUSPITH("radiuspith"),
    @XmlEnumValue("radiussapwood")
    RADIUSSAPWOOD("radiussapwood"),
    @XmlEnumValue("radiusheartwood")
    RADIUSHEARTWOOD("radiusheartwood"),
    @XmlEnumValue("radiusbark")
    RADIUSBARK("radiusbark"),
    @XmlEnumValue("radiusnumbersapwoodrings")
    RADIUSNUMBERSAPWOODRINGS("radiusnumbersapwoodrings"),
    @XmlEnumValue("radiuslastringunderbark")
    RADIUSLASTRINGUNDERBARK("radiuslastringunderbark"),
    @XmlEnumValue("radiusmissingheartwoodringstopith")
    RADIUSMISSINGHEARTWOODRINGSTOPITH("radiusmissingheartwoodringstopith"),
    @XmlEnumValue("radiusmissingheartwoodringstopithfoundation")
    RADIUSMISSINGHEARTWOODRINGSTOPITHFOUNDATION("radiusmissingheartwoodringstopithfoundation"),
    @XmlEnumValue("radiusmissingsapwoodringstobark")
    RADIUSMISSINGSAPWOODRINGSTOBARK("radiusmissingsapwoodringstobark"),
    @XmlEnumValue("radiusmissingsapwoodringstobarkfoundation")
    RADIUSMISSINGSAPWOODRINGSTOBARKFOUNDATION("radiusmissingsapwoodringstobarkfoundation"),
    @XmlEnumValue("radiuscreated")
    RADIUSCREATED("radiuscreated"),
    @XmlEnumValue("radiuslastmodified")
    RADIUSLASTMODIFIED("radiuslastmodified"),
    @XmlEnumValue("radiusazimuth")
    RADIUSAZIMUTH("radiusazimuth"),
    @XmlEnumValue("radiustitle")
    RADIUSTITLE("radiustitle"),
    @XmlEnumValue("radiuscode")
    RADIUSCODE("radiuscode"),
    @XmlEnumValue("seriesid")
    SERIESID("seriesid"),
    @XmlEnumValue("seriesdbid")
    SERIESDBID("seriesdbid"),
    @XmlEnumValue("seriesmeasuringmethod")
    SERIESMEASURINGMETHOD("seriesmeasuringmethod"),
    @XmlEnumValue("seriesvariable")
    SERIESVARIABLE("seriesvariable"),
    @XmlEnumValue("seriesunit")
    SERIESUNIT("seriesunit"),
    @XmlEnumValue("seriespower")
    SERIESPOWER("seriespower"),
    @XmlEnumValue("seriesmeasuringdate")
    SERIESMEASURINGDATE("seriesmeasuringdate"),
    @XmlEnumValue("seriesanalyst")
    SERIESANALYST("seriesanalyst"),
    @XmlEnumValue("seriesdendrochronologist")
    SERIESDENDROCHRONOLOGIST("seriesdendrochronologist"),
    @XmlEnumValue("seriescomments")
    SERIESCOMMENTS("seriescomments"),
    @XmlEnumValue("seriesfirstyear")
    SERIESFIRSTYEAR("seriesfirstyear"),
    @XmlEnumValue("seriessproutyear")
    SERIESSPROUTYEAR("seriessproutyear"),
    @XmlEnumValue("seriesdeathyear")
    SERIESDEATHYEAR("seriesdeathyear"),
    @XmlEnumValue("seriesprovenance")
    SERIESPROVENANCE("seriesprovenance"),
    @XmlEnumValue("seriestype")
    SERIESTYPE("seriestype"),
    @XmlEnumValue("seriesstandardizingmethod")
    SERIESSTANDARDIZINGMETHOD("seriesstandardizingmethod"),
    @XmlEnumValue("seriesauthor")
    SERIESAUTHOR("seriesauthor"),
    @XmlEnumValue("seriesobjective")
    SERIESOBJECTIVE("seriesobjective"),
    @XmlEnumValue("seriesversion")
    SERIESVERSION("seriesversion"),
    @XmlEnumValue("seriesderivationdate")
    SERIESDERIVATIONDATE("seriesderivationdate"),
    @XmlEnumValue("serieslastmodified")
    SERIESLASTMODIFIED("serieslastmodified"),
    @XmlEnumValue("seriesoperatorparameter")
    SERIESOPERATORPARAMETER("seriesoperatorparameter"),
    @XmlEnumValue("seriesisreconciled")
    SERIESISRECONCILED("seriesisreconciled"),
    @XmlEnumValue("seriesdatingtype")
    SERIESDATINGTYPE("seriesdatingtype"),
    @XmlEnumValue("seriesdatingerrorpositive")
    SERIESDATINGERRORPOSITIVE("seriesdatingerrorpositive"),
    @XmlEnumValue("seriesdatingerrornegative")
    SERIESDATINGERRORNEGATIVE("seriesdatingerrornegative"),
    @XmlEnumValue("seriesvaluecount")
    SERIESVALUECOUNT("seriesvaluecount"),
    @XmlEnumValue("seriescount")
    SERIESCOUNT("seriescount"),
    @XmlEnumValue("seriescode")
    SERIESCODE("seriescode"),
    @XmlEnumValue("dependentseriesid")
    DEPENDENTSERIESID("dependentseriesid"),
    @XmlEnumValue("boxid")
    BOXID("boxid"),
    @XmlEnumValue("boxcode")
    BOXCODE("boxcode"),
    @XmlEnumValue("boxcurationlocation")
    BOXCURATIONLOCATION("boxcurationlocation"),
    @XmlEnumValue("boxtrackinglocation")
    BOXTRACKINGLOCATION("boxtrackinglocation"),
    @XmlEnumValue("boxcomments")
    BOXCOMMENTS("boxcomments"),
    @XmlEnumValue("boxlastmodified")
    BOXLASTMODIFIED("boxlastmodified"),
    @XmlEnumValue("boxcreated")
    BOXCREATED("boxcreated"),
    @XmlEnumValue("tagtext")
    TAGTEXT("tagtext"),
    @XmlEnumValue("tagid")
    TAGID("tagid"),
    @XmlEnumValue("loanid")
    LOANID("loanid"),
    @XmlEnumValue("loanissuedate")
    LOANISSUEDATE("loanissuedate"),
    @XmlEnumValue("loanduedate")
    LOANDUEDATE("loanduedate"),
    @XmlEnumValue("loanreturndate")
    LOANRETURNDATE("loanreturndate"),
    @XmlEnumValue("loanfirstname")
    LOANFIRSTNAME("loanfirstname"),
    @XmlEnumValue("loanlastname")
    LOANLASTNAME("loanlastname"),
    @XmlEnumValue("loanorganisation")
    LOANORGANISATION("loanorganisation"),
    @XmlEnumValue("loannotes")
    LOANNOTES("loannotes");
    private final String value;

    SearchParameterName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SearchParameterName fromValue(String v) {
        for (SearchParameterName c: SearchParameterName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
