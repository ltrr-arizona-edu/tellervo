
package org.tridas.schema;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import edu.cornell.dendro.corina.tridasv2.TridasObjectEx;
import org.tridas.adapters.IntegerAdapter;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tridas.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Azimuth_QNAME = new QName("http://www.tridas.org/1.3", "azimuth");
    private final static QName _Owner_QNAME = new QName("http://www.tridas.org/1.3", "owner");
    private final static QName _Dendrochronologist_QNAME = new QName("http://www.tridas.org/1.3", "dendrochronologist");
    private final static QName _MissingSapwoodRingsToBark_QNAME = new QName("http://www.tridas.org/1.3", "missingSapwoodRingsToBark");
    private final static QName _Provenance_QNAME = new QName("http://www.tridas.org/1.3", "provenance");
    private final static QName _DerivationDate_QNAME = new QName("http://www.tridas.org/1.3", "derivationDate");
    private final static QName _Period_QNAME = new QName("http://www.tridas.org/1.3", "period");
    private final static QName _MissingHeartwoodRingsToPithFoundation_QNAME = new QName("http://www.tridas.org/1.3", "missingHeartwoodRingsToPithFoundation");
    private final static QName _MissingSapwoodRingsToBarkFoundation_QNAME = new QName("http://www.tridas.org/1.3", "missingSapwoodRingsToBarkFoundation");
    private final static QName _LocationComment_QNAME = new QName("http://www.tridas.org/1.3", "locationComment");
    private final static QName _StandardizingMethod_QNAME = new QName("http://www.tridas.org/1.3", "standardizingMethod");
    private final static QName _CreatedTimestamp_QNAME = new QName("http://www.tridas.org/1.3", "createdTimestamp");
    private final static QName _Objective_QNAME = new QName("http://www.tridas.org/1.3", "objective");
    private final static QName _Analyst_QNAME = new QName("http://www.tridas.org/1.3", "analyst");
    private final static QName _Taxon_QNAME = new QName("http://www.tridas.org/1.3", "taxon");
    private final static QName _RequestDate_QNAME = new QName("http://www.tridas.org/1.3", "requestDate");
    private final static QName _Processing_QNAME = new QName("http://www.tridas.org/1.3", "processing");
    private final static QName _Description_QNAME = new QName("http://www.tridas.org/1.3", "description");
    private final static QName _UsedSoftware_QNAME = new QName("http://www.tridas.org/1.3", "usedSoftware");
    private final static QName _Authenticity_QNAME = new QName("http://www.tridas.org/1.3", "authenticity");
    private final static QName _Type_QNAME = new QName("http://www.tridas.org/1.3", "type");
    private final static QName _Knots_QNAME = new QName("http://www.tridas.org/1.3", "knots");
    private final static QName _LocationPrecision_QNAME = new QName("http://www.tridas.org/1.3", "locationPrecision");
    private final static QName _Investigator_QNAME = new QName("http://www.tridas.org/1.3", "investigator");
    private final static QName _StatValue_QNAME = new QName("http://www.tridas.org/1.3", "statValue");
    private final static QName _Altitude_QNAME = new QName("http://www.tridas.org/1.3", "altitude");
    private final static QName _FirstYear_QNAME = new QName("http://www.tridas.org/1.3", "firstYear");
    private final static QName _Marks_QNAME = new QName("http://www.tridas.org/1.3", "marks");
    private final static QName _LastModifiedTimestamp_QNAME = new QName("http://www.tridas.org/1.3", "lastModifiedTimestamp");
    private final static QName _Comments_QNAME = new QName("http://www.tridas.org/1.3", "comments");
    private final static QName _SamplingDate_QNAME = new QName("http://www.tridas.org/1.3", "samplingDate");
    private final static QName _SproutYear_QNAME = new QName("http://www.tridas.org/1.3", "sproutYear");
    private final static QName _Commissioner_QNAME = new QName("http://www.tridas.org/1.3", "commissioner");
    private final static QName _CoverageTemporalFoundation_QNAME = new QName("http://www.tridas.org/1.3", "coverageTemporalFoundation");
    private final static QName _Reference_QNAME = new QName("http://www.tridas.org/1.3", "reference");
    private final static QName _MissingHeartwoodRingsToPith_QNAME = new QName("http://www.tridas.org/1.3", "missingHeartwoodRingsToPith");
    private final static QName _DeathYear_QNAME = new QName("http://www.tridas.org/1.3", "deathYear");
    private final static QName _LocationType_QNAME = new QName("http://www.tridas.org/1.3", "locationType");
    private final static QName _Title_QNAME = new QName("http://www.tridas.org/1.3", "title");
    private final static QName _Author_QNAME = new QName("http://www.tridas.org/1.3", "author");
    private final static QName _SignificanceLevel_QNAME = new QName("http://www.tridas.org/1.3", "significanceLevel");
    private final static QName _Creator_QNAME = new QName("http://www.tridas.org/1.3", "creator");
    private final static QName _Version_QNAME = new QName("http://www.tridas.org/1.3", "version");
    private final static QName _NrOfSapwoodRings_QNAME = new QName("http://www.tridas.org/1.3", "nrOfSapwoodRings");
    private final static QName _State_QNAME = new QName("http://www.tridas.org/1.3", "state");
    private final static QName _CoverageTemporal_QNAME = new QName("http://www.tridas.org/1.3", "coverageTemporal");
    private final static QName _MeasuringDate_QNAME = new QName("http://www.tridas.org/1.3", "measuringDate");
    private final static QName _Position_QNAME = new QName("http://www.tridas.org/1.3", "position");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tridas.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TridasBark }
     * 
     */
    public TridasBark createTridasBark() {
        return new TridasBark();
    }

    /**
     * Create an instance of {@link TridasBedrock }
     * 
     */
    public TridasBedrock createTridasBedrock() {
        return new TridasBedrock();
    }

    /**
     * Create an instance of {@link DateTime }
     * 
     */
    public DateTime createDateTime() {
        return new DateTime();
    }

    /**
     * Create an instance of {@link TridasObject }
     * 
     */
    public TridasObject createTridasObject() {
        return new TridasObjectEx();
    }

    /**
     * Create an instance of {@link TridasIdentifier }
     * 
     */
    public TridasIdentifier createTridasIdentifier() {
        return new TridasIdentifier();
    }

    /**
     * Create an instance of {@link TridasMeasurementSeries }
     * 
     */
    public TridasMeasurementSeries createTridasMeasurementSeries() {
        return new TridasMeasurementSeries();
    }

    /**
     * Create an instance of {@link TridasLocationGeometry }
     * 
     */
    public TridasLocationGeometry createTridasLocationGeometry() {
        return new TridasLocationGeometry();
    }

    /**
     * Create an instance of {@link TridasUnit }
     * 
     */
    public TridasUnit createTridasUnit() {
        return new TridasUnit();
    }

    /**
     * Create an instance of {@link TridasSample }
     * 
     */
    public TridasSample createTridasSample() {
        return new TridasSample();
    }

    /**
     * Create an instance of {@link TridasDimensions }
     * 
     */
    public TridasDimensions createTridasDimensions() {
        return new TridasDimensions();
    }

    /**
     * Create an instance of {@link TridasProject }
     * 
     */
    public TridasProject createTridasProject() {
        return new TridasProject();
    }

    /**
     * Create an instance of {@link TridasDatingReference }
     * 
     */
    public TridasDatingReference createTridasDatingReference() {
        return new TridasDatingReference();
    }

    /**
     * Create an instance of {@link TridasWoodCompleteness }
     * 
     */
    public TridasWoodCompleteness createTridasWoodCompleteness() {
        return new TridasWoodCompleteness();
    }

    /**
     * Create an instance of {@link TridasValue }
     * 
     */
    public TridasValue createTridasValue() {
        return new TridasValue();
    }

    /**
     * Create an instance of {@link SeriesLink.IdRef }
     * 
     */
    public SeriesLink.IdRef createSeriesLinkIdRef() {
        return new SeriesLink.IdRef();
    }

    /**
     * Create an instance of {@link TridasCoverage }
     * 
     */
    public TridasCoverage createTridasCoverage() {
        return new TridasCoverage();
    }

    /**
     * Create an instance of {@link TridasResearch }
     * 
     */
    public TridasResearch createTridasResearch() {
        return new TridasResearch();
    }

    /**
     * Create an instance of {@link ControlledVoc }
     * 
     */
    public ControlledVoc createControlledVoc() {
        return new ControlledVoc();
    }

    /**
     * Create an instance of {@link TridasTridas }
     * 
     */
    public TridasTridas createTridasTridas() {
        return new TridasTridas();
    }

    /**
     * Create an instance of {@link TridasUnitless }
     * 
     */
    public TridasUnitless createTridasUnitless() {
        return new TridasUnitless();
    }

    /**
     * Create an instance of {@link SeriesLinks }
     * 
     */
    public SeriesLinks createSeriesLinks() {
        return new SeriesLinks();
    }

    /**
     * Create an instance of {@link TridasVariable }
     * 
     */
    public TridasVariable createTridasVariable() {
        return new TridasVariable();
    }

    /**
     * Create an instance of {@link SeriesLink }
     * 
     */
    public SeriesLink createSeriesLink() {
        return new SeriesLink();
    }

    /**
     * Create an instance of {@link TridasLaboratory }
     * 
     */
    public TridasLaboratory createTridasLaboratory() {
        return new TridasLaboratory();
    }

    /**
     * Create an instance of {@link TridasShape }
     * 
     */
    public TridasShape createTridasShape() {
        return new TridasShape();
    }

    /**
     * Create an instance of {@link TridasGenericField }
     * 
     */
    public TridasGenericField createTridasGenericField() {
        return new TridasGenericField();
    }

    /**
     * Create an instance of {@link TridasDerivedSeries }
     * 
     */
    public TridasDerivedSeries createTridasDerivedSeries() {
        return new TridasDerivedSeries();
    }

    /**
     * Create an instance of {@link TridasLaboratory.Name }
     * 
     */
    public TridasLaboratory.Name createTridasLaboratoryName() {
        return new TridasLaboratory.Name();
    }

    /**
     * Create an instance of {@link SeriesLinksWithPreferred }
     * 
     */
    public SeriesLinksWithPreferred createSeriesLinksWithPreferred() {
        return new SeriesLinksWithPreferred();
    }

    /**
     * Create an instance of {@link TridasStatFoundation }
     * 
     */
    public TridasStatFoundation createTridasStatFoundation() {
        return new TridasStatFoundation();
    }

    /**
     * Create an instance of {@link TridasElement }
     * 
     */
    public TridasElement createTridasElement() {
        return new TridasElement();
    }

    /**
     * Create an instance of {@link TridasSlope }
     * 
     */
    public TridasSlope createTridasSlope() {
        return new TridasSlope();
    }

    /**
     * Create an instance of {@link TridasMeasurementSeriesPlaceholder }
     * 
     */
    public TridasMeasurementSeriesPlaceholder createTridasMeasurementSeriesPlaceholder() {
        return new TridasMeasurementSeriesPlaceholder();
    }

    /**
     * Create an instance of {@link TridasCategory }
     * 
     */
    public TridasCategory createTridasCategory() {
        return new TridasCategory();
    }

    /**
     * Create an instance of {@link TridasRadiusPlaceholder }
     * 
     */
    public TridasRadiusPlaceholder createTridasRadiusPlaceholder() {
        return new TridasRadiusPlaceholder();
    }

    /**
     * Create an instance of {@link TridasInterpretation }
     * 
     */
    public TridasInterpretation createTridasInterpretation() {
        return new TridasInterpretation();
    }

    /**
     * Create an instance of {@link TridasSoil }
     * 
     */
    public TridasSoil createTridasSoil() {
        return new TridasSoil();
    }

    /**
     * Create an instance of {@link TridasMeasuringMethod }
     * 
     */
    public TridasMeasuringMethod createTridasMeasuringMethod() {
        return new TridasMeasuringMethod();
    }

    /**
     * Create an instance of {@link TridasInterpretationUnsolved }
     * 
     */
    public TridasInterpretationUnsolved createTridasInterpretationUnsolved() {
        return new TridasInterpretationUnsolved();
    }

    /**
     * Create an instance of {@link TridasHeartwood }
     * 
     */
    public TridasHeartwood createTridasHeartwood() {
        return new TridasHeartwood();
    }

    /**
     * Create an instance of {@link TridasValues }
     * 
     */
    public TridasValues createTridasValues() {
        return new TridasValues();
    }

    /**
     * Create an instance of {@link TridasLocation }
     * 
     */
    public TridasLocation createTridasLocation() {
        return new TridasLocation();
    }

    /**
     * Create an instance of {@link TridasPith }
     * 
     */
    public TridasPith createTridasPith() {
        return new TridasPith();
    }

    /**
     * Create an instance of {@link Date }
     * 
     */
    public Date createDate() {
        return new Date();
    }

    /**
     * Create an instance of {@link TridasFile }
     * 
     */
    public TridasFile createTridasFile() {
        return new TridasFile();
    }

    /**
     * Create an instance of {@link Year }
     * 
     */
    public Year createYear() {
        return new Year();
    }

    /**
     * Create an instance of {@link SeriesLink.XLink }
     * 
     */
    public SeriesLink.XLink createSeriesLinkXLink() {
        return new SeriesLink.XLink();
    }

    /**
     * Create an instance of {@link TridasLastRingUnderBark }
     * 
     */
    public TridasLastRingUnderBark createTridasLastRingUnderBark() {
        return new TridasLastRingUnderBark();
    }

    /**
     * Create an instance of {@link TridasRadius }
     * 
     */
    public TridasRadius createTridasRadius() {
        return new TridasRadius();
    }

    /**
     * Create an instance of {@link TridasSapwood }
     * 
     */
    public TridasSapwood createTridasSapwood() {
        return new TridasSapwood();
    }

    /**
     * Create an instance of {@link TridasRemark }
     * 
     */
    public TridasRemark createTridasRemark() {
        return new TridasRemark();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "azimuth")
    public JAXBElement<BigDecimal> createAzimuth(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Azimuth_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "owner")
    public JAXBElement<String> createOwner(String value) {
        return new JAXBElement<String>(_Owner_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "dendrochronologist")
    public JAXBElement<String> createDendrochronologist(String value) {
        return new JAXBElement<String>(_Dendrochronologist_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "missingSapwoodRingsToBark")
    public JAXBElement<String> createMissingSapwoodRingsToBark(String value) {
        return new JAXBElement<String>(_MissingSapwoodRingsToBark_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "provenance")
    public JAXBElement<String> createProvenance(String value) {
        return new JAXBElement<String>(_Provenance_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "derivationDate")
    public JAXBElement<Date> createDerivationDate(Date value) {
        return new JAXBElement<Date>(_DerivationDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "period")
    public JAXBElement<String> createPeriod(String value) {
        return new JAXBElement<String>(_Period_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "missingHeartwoodRingsToPithFoundation")
    public JAXBElement<String> createMissingHeartwoodRingsToPithFoundation(String value) {
        return new JAXBElement<String>(_MissingHeartwoodRingsToPithFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "missingSapwoodRingsToBarkFoundation")
    public JAXBElement<String> createMissingSapwoodRingsToBarkFoundation(String value) {
        return new JAXBElement<String>(_MissingSapwoodRingsToBarkFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "locationComment")
    public JAXBElement<String> createLocationComment(String value) {
        return new JAXBElement<String>(_LocationComment_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "standardizingMethod")
    public JAXBElement<String> createStandardizingMethod(String value) {
        return new JAXBElement<String>(_StandardizingMethod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "createdTimestamp")
    public JAXBElement<DateTime> createCreatedTimestamp(DateTime value) {
        return new JAXBElement<DateTime>(_CreatedTimestamp_QNAME, DateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "objective")
    public JAXBElement<String> createObjective(String value) {
        return new JAXBElement<String>(_Objective_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "analyst")
    public JAXBElement<String> createAnalyst(String value) {
        return new JAXBElement<String>(_Analyst_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlledVoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "taxon")
    public JAXBElement<ControlledVoc> createTaxon(ControlledVoc value) {
        return new JAXBElement<ControlledVoc>(_Taxon_QNAME, ControlledVoc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "requestDate")
    public JAXBElement<Date> createRequestDate(Date value) {
        return new JAXBElement<Date>(_RequestDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "processing")
    public JAXBElement<String> createProcessing(String value) {
        return new JAXBElement<String>(_Processing_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "usedSoftware")
    public JAXBElement<String> createUsedSoftware(String value) {
        return new JAXBElement<String>(_UsedSoftware_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "authenticity")
    public JAXBElement<String> createAuthenticity(String value) {
        return new JAXBElement<String>(_Authenticity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlledVoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "type")
    public JAXBElement<ControlledVoc> createType(ControlledVoc value) {
        return new JAXBElement<ControlledVoc>(_Type_QNAME, ControlledVoc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "knots")
    public JAXBElement<Boolean> createKnots(Boolean value) {
        return new JAXBElement<Boolean>(_Knots_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "locationPrecision")
    public JAXBElement<String> createLocationPrecision(String value) {
        return new JAXBElement<String>(_LocationPrecision_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "investigator")
    public JAXBElement<String> createInvestigator(String value) {
        return new JAXBElement<String>(_Investigator_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "statValue")
    public JAXBElement<BigDecimal> createStatValue(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_StatValue_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "altitude")
    public JAXBElement<Double> createAltitude(Double value) {
        return new JAXBElement<Double>(_Altitude_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "firstYear")
    public JAXBElement<Year> createFirstYear(Year value) {
        return new JAXBElement<Year>(_FirstYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "marks")
    public JAXBElement<String> createMarks(String value) {
        return new JAXBElement<String>(_Marks_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "lastModifiedTimestamp")
    public JAXBElement<DateTime> createLastModifiedTimestamp(DateTime value) {
        return new JAXBElement<DateTime>(_LastModifiedTimestamp_QNAME, DateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "comments")
    public JAXBElement<String> createComments(String value) {
        return new JAXBElement<String>(_Comments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "samplingDate")
    public JAXBElement<Date> createSamplingDate(Date value) {
        return new JAXBElement<Date>(_SamplingDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "sproutYear")
    public JAXBElement<Year> createSproutYear(Year value) {
        return new JAXBElement<Year>(_SproutYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "commissioner")
    public JAXBElement<String> createCommissioner(String value) {
        return new JAXBElement<String>(_Commissioner_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "coverageTemporalFoundation")
    public JAXBElement<String> createCoverageTemporalFoundation(String value) {
        return new JAXBElement<String>(_CoverageTemporalFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "reference")
    public JAXBElement<String> createReference(String value) {
        return new JAXBElement<String>(_Reference_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "missingHeartwoodRingsToPith")
    public JAXBElement<String> createMissingHeartwoodRingsToPith(String value) {
        return new JAXBElement<String>(_MissingHeartwoodRingsToPith_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "deathYear")
    public JAXBElement<Year> createDeathYear(Year value) {
        return new JAXBElement<Year>(_DeathYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NormalTridasLocationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "locationType")
    public JAXBElement<NormalTridasLocationType> createLocationType(NormalTridasLocationType value) {
        return new JAXBElement<NormalTridasLocationType>(_LocationType_QNAME, NormalTridasLocationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "author")
    public JAXBElement<String> createAuthor(String value) {
        return new JAXBElement<String>(_Author_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "significanceLevel")
    public JAXBElement<BigDecimal> createSignificanceLevel(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_SignificanceLevel_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "creator")
    public JAXBElement<String> createCreator(String value) {
        return new JAXBElement<String>(_Creator_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "version")
    public JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "nrOfSapwoodRings")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createNrOfSapwoodRings(Integer value) {
        return new JAXBElement<Integer>(_NrOfSapwoodRings_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "state")
    public JAXBElement<String> createState(String value) {
        return new JAXBElement<String>(_State_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "coverageTemporal")
    public JAXBElement<String> createCoverageTemporal(String value) {
        return new JAXBElement<String>(_CoverageTemporal_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "measuringDate")
    public JAXBElement<Date> createMeasuringDate(Date value) {
        return new JAXBElement<Date>(_MeasuringDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.3", name = "position")
    public JAXBElement<String> createPosition(String value) {
        return new JAXBElement<String>(_Position_QNAME, String.class, null, value);
    }

}
