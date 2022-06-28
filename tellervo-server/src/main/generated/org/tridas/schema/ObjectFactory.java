
package org.tridas.schema;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.tridas.adapters.IntegerAdapter;
import org.tridas.util.TridasObjectEx;


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

    private final static QName _MissingHeartwoodRingsToPith_QNAME = new QName("http://www.tridas.org/1.2.2", "missingHeartwoodRingsToPith");
    private final static QName _AverageRingWidth_QNAME = new QName("http://www.tridas.org/1.2.2", "averageRingWidth");
    private final static QName _Reference_QNAME = new QName("http://www.tridas.org/1.2.2", "reference");
    private final static QName _CoverageTemporalFoundation_QNAME = new QName("http://www.tridas.org/1.2.2", "coverageTemporalFoundation");
    private final static QName _Commissioner_QNAME = new QName("http://www.tridas.org/1.2.2", "commissioner");
    private final static QName _SamplingDate_QNAME = new QName("http://www.tridas.org/1.2.2", "samplingDate");
    private final static QName _Comments_QNAME = new QName("http://www.tridas.org/1.2.2", "comments");
    private final static QName _State_QNAME = new QName("http://www.tridas.org/1.2.2", "state");
    private final static QName _NrOfSapwoodRings_QNAME = new QName("http://www.tridas.org/1.2.2", "nrOfSapwoodRings");
    private final static QName _Creator_QNAME = new QName("http://www.tridas.org/1.2.2", "creator");
    private final static QName _Version_QNAME = new QName("http://www.tridas.org/1.2.2", "version");
    private final static QName _MeasuringDate_QNAME = new QName("http://www.tridas.org/1.2.2", "measuringDate");
    private final static QName _Position_QNAME = new QName("http://www.tridas.org/1.2.2", "position");
    private final static QName _CoverageTemporal_QNAME = new QName("http://www.tridas.org/1.2.2", "coverageTemporal");
    private final static QName _NrOfUnmeasuredInnerRings_QNAME = new QName("http://www.tridas.org/1.2.2", "nrOfUnmeasuredInnerRings");
    private final static QName _Author_QNAME = new QName("http://www.tridas.org/1.2.2", "author");
    private final static QName _SignificanceLevel_QNAME = new QName("http://www.tridas.org/1.2.2", "significanceLevel");
    private final static QName _Title_QNAME = new QName("http://www.tridas.org/1.2.2", "title");
    private final static QName _LocationType_QNAME = new QName("http://www.tridas.org/1.2.2", "locationType");
    private final static QName _DeathYear_QNAME = new QName("http://www.tridas.org/1.2.2", "deathYear");
    private final static QName _MissingSapwoodRingsToBarkFoundation_QNAME = new QName("http://www.tridas.org/1.2.2", "missingSapwoodRingsToBarkFoundation");
    private final static QName _LocationComment_QNAME = new QName("http://www.tridas.org/1.2.2", "locationComment");
    private final static QName _MissingHeartwoodRingsToPithFoundation_QNAME = new QName("http://www.tridas.org/1.2.2", "missingHeartwoodRingsToPithFoundation");
    private final static QName _Period_QNAME = new QName("http://www.tridas.org/1.2.2", "period");
    private final static QName _DerivationDate_QNAME = new QName("http://www.tridas.org/1.2.2", "derivationDate");
    private final static QName _Analyst_QNAME = new QName("http://www.tridas.org/1.2.2", "analyst");
    private final static QName _Objective_QNAME = new QName("http://www.tridas.org/1.2.2", "objective");
    private final static QName _CreatedTimestamp_QNAME = new QName("http://www.tridas.org/1.2.2", "createdTimestamp");
    private final static QName _StandardizingMethod_QNAME = new QName("http://www.tridas.org/1.2.2", "standardizingMethod");
    private final static QName _Owner_QNAME = new QName("http://www.tridas.org/1.2.2", "owner");
    private final static QName _Dendrochronologist_QNAME = new QName("http://www.tridas.org/1.2.2", "dendrochronologist");
    private final static QName _Azimuth_QNAME = new QName("http://www.tridas.org/1.2.2", "azimuth");
    private final static QName _Provenance_QNAME = new QName("http://www.tridas.org/1.2.2", "provenance");
    private final static QName _RingCount_QNAME = new QName("http://www.tridas.org/1.2.2", "ringCount");
    private final static QName _Category_QNAME = new QName("http://www.tridas.org/1.2.2", "category");
    private final static QName _MissingSapwoodRingsToBark_QNAME = new QName("http://www.tridas.org/1.2.2", "missingSapwoodRingsToBark");
    private final static QName _LastYear_QNAME = new QName("http://www.tridas.org/1.2.2", "lastYear");
    private final static QName _LocationPrecision_QNAME = new QName("http://www.tridas.org/1.2.2", "locationPrecision");
    private final static QName _Investigator_QNAME = new QName("http://www.tridas.org/1.2.2", "investigator");
    private final static QName _StatValue_QNAME = new QName("http://www.tridas.org/1.2.2", "statValue");
    private final static QName _Knots_QNAME = new QName("http://www.tridas.org/1.2.2", "knots");
    private final static QName _PithYear_QNAME = new QName("http://www.tridas.org/1.2.2", "pithYear");
    private final static QName _Type_QNAME = new QName("http://www.tridas.org/1.2.2", "type");
    private final static QName _LastModifiedTimestamp_QNAME = new QName("http://www.tridas.org/1.2.2", "lastModifiedTimestamp");
    private final static QName _Marks_QNAME = new QName("http://www.tridas.org/1.2.2", "marks");
    private final static QName _FirstYear_QNAME = new QName("http://www.tridas.org/1.2.2", "firstYear");
    private final static QName _NrOfUnmeasuredOuterRings_QNAME = new QName("http://www.tridas.org/1.2.2", "nrOfUnmeasuredOuterRings");
    private final static QName _Altitude_QNAME = new QName("http://www.tridas.org/1.2.2", "altitude");
    private final static QName _Processing_QNAME = new QName("http://www.tridas.org/1.2.2", "processing");
    private final static QName _RequestDate_QNAME = new QName("http://www.tridas.org/1.2.2", "requestDate");
    private final static QName _Taxon_QNAME = new QName("http://www.tridas.org/1.2.2", "taxon");
    private final static QName _Authenticity_QNAME = new QName("http://www.tridas.org/1.2.2", "authenticity");
    private final static QName _UsedSoftware_QNAME = new QName("http://www.tridas.org/1.2.2", "usedSoftware");
    private final static QName _Description_QNAME = new QName("http://www.tridas.org/1.2.2", "description");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tridas.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TridasSample }
     * 
     */
    public TridasSample createTridasSample() {
        return new TridasSample();
    }

    /**
     * Create an instance of {@link TridasCoverage }
     * 
     */
    public TridasCoverage createTridasCoverage() {
        return new TridasCoverage();
    }

    /**
     * Create an instance of {@link ControlledVoc }
     * 
     */
    public ControlledVoc createControlledVoc() {
        return new ControlledVoc();
    }

    /**
     * Create an instance of {@link TridasProject }
     * 
     */
    public TridasProject createTridasProject() {
        return new TridasProject();
    }

    /**
     * Create an instance of {@link TridasVocabulary }
     * 
     */
    public TridasVocabulary createTridasVocabulary() {
        return new TridasVocabulary();
    }

    /**
     * Create an instance of {@link TridasSapwood }
     * 
     */
    public TridasSapwood createTridasSapwood() {
        return new TridasSapwood();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ProjectType }
     * 
     */
    public TridasVocabulary.ProjectType createTridasVocabularyProjectType() {
        return new TridasVocabulary.ProjectType();
    }

    /**
     * Create an instance of {@link TridasObject }
     * 
     */
    public TridasObject createTridasObject() {
        return new TridasObjectEx();
    }

    /**
     * Create an instance of {@link TridasBark }
     * 
     */
    public TridasBark createTridasBark() {
        return new TridasBark();
    }

    /**
     * Create an instance of {@link TridasWoodCompleteness }
     * 
     */
    public TridasWoodCompleteness createTridasWoodCompleteness() {
        return new TridasWoodCompleteness();
    }

    /**
     * Create an instance of {@link TridasSoil }
     * 
     */
    public TridasSoil createTridasSoil() {
        return new TridasSoil();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ObjectType }
     * 
     */
    public TridasVocabulary.ObjectType createTridasVocabularyObjectType() {
        return new TridasVocabulary.ObjectType();
    }

    /**
     * Create an instance of {@link TridasLocationGeometry }
     * 
     */
    public TridasLocationGeometry createTridasLocationGeometry() {
        return new TridasLocationGeometry();
    }

    /**
     * Create an instance of {@link TridasPith }
     * 
     */
    public TridasPith createTridasPith() {
        return new TridasPith();
    }

    /**
     * Create an instance of {@link TridasLaboratory.Name }
     * 
     */
    public TridasLaboratory.Name createTridasLaboratoryName() {
        return new TridasLaboratory.Name();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ElementTaxon }
     * 
     */
    public TridasVocabulary.ElementTaxon createTridasVocabularyElementTaxon() {
        return new TridasVocabulary.ElementTaxon();
    }

    /**
     * Create an instance of {@link SeriesLink.XLink }
     * 
     */
    public SeriesLink.XLink createSeriesLinkXLink() {
        return new SeriesLink.XLink();
    }

    /**
     * Create an instance of {@link TridasVocabulary.LocationType }
     * 
     */
    public TridasVocabulary.LocationType createTridasVocabularyLocationType() {
        return new TridasVocabulary.LocationType();
    }

    /**
     * Create an instance of {@link TridasTridas }
     * 
     */
    public TridasTridas createTridasTridas() {
        return new TridasTridas();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ElementType }
     * 
     */
    public TridasVocabulary.ElementType createTridasVocabularyElementType() {
        return new TridasVocabulary.ElementType();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ValuesRemark }
     * 
     */
    public TridasVocabulary.ValuesRemark createTridasVocabularyValuesRemark() {
        return new TridasVocabulary.ValuesRemark();
    }

    /**
     * Create an instance of {@link TridasResearch }
     * 
     */
    public TridasResearch createTridasResearch() {
        return new TridasResearch();
    }

    /**
     * Create an instance of {@link TridasLocation }
     * 
     */
    public TridasLocation createTridasLocation() {
        return new TridasLocation();
    }

    /**
     * Create an instance of {@link TridasDatingReference }
     * 
     */
    public TridasDatingReference createTridasDatingReference() {
        return new TridasDatingReference();
    }

    /**
     * Create an instance of {@link TridasValue }
     * 
     */
    public TridasValue createTridasValue() {
        return new TridasValue();
    }

    /**
     * Create an instance of {@link TridasMeasurementSeries }
     * 
     */
    public TridasMeasurementSeries createTridasMeasurementSeries() {
        return new TridasMeasurementSeries();
    }

    /**
     * Create an instance of {@link TridasAddress }
     * 
     */
    public TridasAddress createTridasAddress() {
        return new TridasAddress();
    }

    /**
     * Create an instance of {@link SeriesLink }
     * 
     */
    public SeriesLink createSeriesLink() {
        return new SeriesLink();
    }

    /**
     * Create an instance of {@link SeriesLinks }
     * 
     */
    public SeriesLinks createSeriesLinks() {
        return new SeriesLinks();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ProjectCategory }
     * 
     */
    public TridasVocabulary.ProjectCategory createTridasVocabularyProjectCategory() {
        return new TridasVocabulary.ProjectCategory();
    }

    /**
     * Create an instance of {@link TridasVocabulary.SampleType }
     * 
     */
    public TridasVocabulary.SampleType createTridasVocabularySampleType() {
        return new TridasVocabulary.SampleType();
    }

    /**
     * Create an instance of {@link TridasInterpretation }
     * 
     */
    public TridasInterpretation createTridasInterpretation() {
        return new TridasInterpretation();
    }

    /**
     * Create an instance of {@link SeriesLink.IdRef }
     * 
     */
    public SeriesLink.IdRef createSeriesLinkIdRef() {
        return new SeriesLink.IdRef();
    }

    /**
     * Create an instance of {@link TridasRemark }
     * 
     */
    public TridasRemark createTridasRemark() {
        return new TridasRemark();
    }

    /**
     * Create an instance of {@link TridasShape }
     * 
     */
    public TridasShape createTridasShape() {
        return new TridasShape();
    }

    /**
     * Create an instance of {@link TridasDerivedSeries }
     * 
     */
    public TridasDerivedSeries createTridasDerivedSeries() {
        return new TridasDerivedSeries();
    }

    /**
     * Create an instance of {@link TridasDating }
     * 
     */
    public TridasDating createTridasDating() {
        return new TridasDating();
    }

    /**
     * Create an instance of {@link TridasInterpretationUnsolved }
     * 
     */
    public TridasInterpretationUnsolved createTridasInterpretationUnsolved() {
        return new TridasInterpretationUnsolved();
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
     * Create an instance of {@link TridasVariable }
     * 
     */
    public TridasVariable createTridasVariable() {
        return new TridasVariable();
    }

    /**
     * Create an instance of {@link TridasSlope }
     * 
     */
    public TridasSlope createTridasSlope() {
        return new TridasSlope();
    }

    /**
     * Create an instance of {@link TridasVocabulary.GlobalUnit }
     * 
     */
    public TridasVocabulary.GlobalUnit createTridasVocabularyGlobalUnit() {
        return new TridasVocabulary.GlobalUnit();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ValuesVariable }
     * 
     */
    public TridasVocabulary.ValuesVariable createTridasVocabularyValuesVariable() {
        return new TridasVocabulary.ValuesVariable();
    }

    /**
     * Create an instance of {@link TridasUnit }
     * 
     */
    public TridasUnit createTridasUnit() {
        return new TridasUnit();
    }

    /**
     * Create an instance of {@link TridasVocabulary.DerivedSeriesType }
     * 
     */
    public TridasVocabulary.DerivedSeriesType createTridasVocabularyDerivedSeriesType() {
        return new TridasVocabulary.DerivedSeriesType();
    }

    /**
     * Create an instance of {@link DateTime }
     * 
     */
    public DateTime createDateTime() {
        return new DateTime();
    }

    /**
     * Create an instance of {@link TridasVocabulary.ElementShape }
     * 
     */
    public TridasVocabulary.ElementShape createTridasVocabularyElementShape() {
        return new TridasVocabulary.ElementShape();
    }

    /**
     * Create an instance of {@link TridasValues }
     * 
     */
    public TridasValues createTridasValues() {
        return new TridasValues();
    }

    /**
     * Create an instance of {@link TridasStatFoundation }
     * 
     */
    public TridasStatFoundation createTridasStatFoundation() {
        return new TridasStatFoundation();
    }

    /**
     * Create an instance of {@link TridasUnitless }
     * 
     */
    public TridasUnitless createTridasUnitless() {
        return new TridasUnitless();
    }

    /**
     * Create an instance of {@link TridasLastRingUnderBark }
     * 
     */
    public TridasLastRingUnderBark createTridasLastRingUnderBark() {
        return new TridasLastRingUnderBark();
    }

    /**
     * Create an instance of {@link TridasMeasuringMethod }
     * 
     */
    public TridasMeasuringMethod createTridasMeasuringMethod() {
        return new TridasMeasuringMethod();
    }

    /**
     * Create an instance of {@link TridasIdentifier }
     * 
     */
    public TridasIdentifier createTridasIdentifier() {
        return new TridasIdentifier();
    }

    /**
     * Create an instance of {@link TridasGenericField }
     * 
     */
    public TridasGenericField createTridasGenericField() {
        return new TridasGenericField();
    }

    /**
     * Create an instance of {@link TridasBedrock }
     * 
     */
    public TridasBedrock createTridasBedrock() {
        return new TridasBedrock();
    }

    /**
     * Create an instance of {@link Year }
     * 
     */
    public Year createYear() {
        return new Year();
    }

    /**
     * Create an instance of {@link TridasRadius }
     * 
     */
    public TridasRadius createTridasRadius() {
        return new TridasRadius();
    }

    /**
     * Create an instance of {@link TridasVocabulary.MeasurementSeriesMeasuringMethod }
     * 
     */
    public TridasVocabulary.MeasurementSeriesMeasuringMethod createTridasVocabularyMeasurementSeriesMeasuringMethod() {
        return new TridasVocabulary.MeasurementSeriesMeasuringMethod();
    }

    /**
     * Create an instance of {@link TridasLaboratory }
     * 
     */
    public TridasLaboratory createTridasLaboratory() {
        return new TridasLaboratory();
    }

    /**
     * Create an instance of {@link TridasHeartwood }
     * 
     */
    public TridasHeartwood createTridasHeartwood() {
        return new TridasHeartwood();
    }

    /**
     * Create an instance of {@link SeriesLinksWithPreferred }
     * 
     */
    public SeriesLinksWithPreferred createSeriesLinksWithPreferred() {
        return new SeriesLinksWithPreferred();
    }

    /**
     * Create an instance of {@link TridasElement }
     * 
     */
    public TridasElement createTridasElement() {
        return new TridasElement();
    }

    /**
     * Create an instance of {@link TridasDimensions }
     * 
     */
    public TridasDimensions createTridasDimensions() {
        return new TridasDimensions();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "missingHeartwoodRingsToPith")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createMissingHeartwoodRingsToPith(Integer value) {
        return new JAXBElement<Integer>(_MissingHeartwoodRingsToPith_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "averageRingWidth")
    public JAXBElement<Double> createAverageRingWidth(Double value) {
        return new JAXBElement<Double>(_AverageRingWidth_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "reference")
    public JAXBElement<String> createReference(String value) {
        return new JAXBElement<String>(_Reference_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "coverageTemporalFoundation")
    public JAXBElement<String> createCoverageTemporalFoundation(String value) {
        return new JAXBElement<String>(_CoverageTemporalFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "commissioner")
    public JAXBElement<String> createCommissioner(String value) {
        return new JAXBElement<String>(_Commissioner_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "samplingDate")
    public JAXBElement<Date> createSamplingDate(Date value) {
        return new JAXBElement<Date>(_SamplingDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "comments")
    public JAXBElement<String> createComments(String value) {
        return new JAXBElement<String>(_Comments_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "state")
    public JAXBElement<String> createState(String value) {
        return new JAXBElement<String>(_State_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "nrOfSapwoodRings")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createNrOfSapwoodRings(Integer value) {
        return new JAXBElement<Integer>(_NrOfSapwoodRings_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "creator")
    public JAXBElement<String> createCreator(String value) {
        return new JAXBElement<String>(_Creator_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "version")
    public JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "measuringDate")
    public JAXBElement<Date> createMeasuringDate(Date value) {
        return new JAXBElement<Date>(_MeasuringDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "position")
    public JAXBElement<String> createPosition(String value) {
        return new JAXBElement<String>(_Position_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "coverageTemporal")
    public JAXBElement<String> createCoverageTemporal(String value) {
        return new JAXBElement<String>(_CoverageTemporal_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "nrOfUnmeasuredInnerRings")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createNrOfUnmeasuredInnerRings(Integer value) {
        return new JAXBElement<Integer>(_NrOfUnmeasuredInnerRings_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "author")
    public JAXBElement<String> createAuthor(String value) {
        return new JAXBElement<String>(_Author_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "significanceLevel")
    public JAXBElement<BigDecimal> createSignificanceLevel(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_SignificanceLevel_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NormalTridasLocationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "locationType")
    public JAXBElement<NormalTridasLocationType> createLocationType(NormalTridasLocationType value) {
        return new JAXBElement<NormalTridasLocationType>(_LocationType_QNAME, NormalTridasLocationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "deathYear")
    public JAXBElement<Year> createDeathYear(Year value) {
        return new JAXBElement<Year>(_DeathYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "missingSapwoodRingsToBarkFoundation")
    public JAXBElement<String> createMissingSapwoodRingsToBarkFoundation(String value) {
        return new JAXBElement<String>(_MissingSapwoodRingsToBarkFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "locationComment")
    public JAXBElement<String> createLocationComment(String value) {
        return new JAXBElement<String>(_LocationComment_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "missingHeartwoodRingsToPithFoundation")
    public JAXBElement<String> createMissingHeartwoodRingsToPithFoundation(String value) {
        return new JAXBElement<String>(_MissingHeartwoodRingsToPithFoundation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "period")
    public JAXBElement<String> createPeriod(String value) {
        return new JAXBElement<String>(_Period_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "derivationDate")
    public JAXBElement<Date> createDerivationDate(Date value) {
        return new JAXBElement<Date>(_DerivationDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "analyst")
    public JAXBElement<String> createAnalyst(String value) {
        return new JAXBElement<String>(_Analyst_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "objective")
    public JAXBElement<String> createObjective(String value) {
        return new JAXBElement<String>(_Objective_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "createdTimestamp")
    public JAXBElement<DateTime> createCreatedTimestamp(DateTime value) {
        return new JAXBElement<DateTime>(_CreatedTimestamp_QNAME, DateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "standardizingMethod")
    public JAXBElement<String> createStandardizingMethod(String value) {
        return new JAXBElement<String>(_StandardizingMethod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "owner")
    public JAXBElement<String> createOwner(String value) {
        return new JAXBElement<String>(_Owner_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "dendrochronologist")
    public JAXBElement<String> createDendrochronologist(String value) {
        return new JAXBElement<String>(_Dendrochronologist_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "azimuth")
    public JAXBElement<BigDecimal> createAzimuth(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Azimuth_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "provenance")
    public JAXBElement<String> createProvenance(String value) {
        return new JAXBElement<String>(_Provenance_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "ringCount")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createRingCount(Integer value) {
        return new JAXBElement<Integer>(_RingCount_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlledVoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "category")
    public JAXBElement<ControlledVoc> createCategory(ControlledVoc value) {
        return new JAXBElement<ControlledVoc>(_Category_QNAME, ControlledVoc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "missingSapwoodRingsToBark")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createMissingSapwoodRingsToBark(Integer value) {
        return new JAXBElement<Integer>(_MissingSapwoodRingsToBark_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "lastYear")
    public JAXBElement<Year> createLastYear(Year value) {
        return new JAXBElement<Year>(_LastYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "locationPrecision")
    public JAXBElement<String> createLocationPrecision(String value) {
        return new JAXBElement<String>(_LocationPrecision_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "investigator")
    public JAXBElement<String> createInvestigator(String value) {
        return new JAXBElement<String>(_Investigator_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "statValue")
    public JAXBElement<BigDecimal> createStatValue(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_StatValue_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "knots")
    public JAXBElement<Boolean> createKnots(Boolean value) {
        return new JAXBElement<Boolean>(_Knots_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "pithYear")
    public JAXBElement<Year> createPithYear(Year value) {
        return new JAXBElement<Year>(_PithYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlledVoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "type")
    public JAXBElement<ControlledVoc> createType(ControlledVoc value) {
        return new JAXBElement<ControlledVoc>(_Type_QNAME, ControlledVoc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "lastModifiedTimestamp")
    public JAXBElement<DateTime> createLastModifiedTimestamp(DateTime value) {
        return new JAXBElement<DateTime>(_LastModifiedTimestamp_QNAME, DateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "marks")
    public JAXBElement<String> createMarks(String value) {
        return new JAXBElement<String>(_Marks_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Year }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "firstYear")
    public JAXBElement<Year> createFirstYear(Year value) {
        return new JAXBElement<Year>(_FirstYear_QNAME, Year.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "nrOfUnmeasuredOuterRings")
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    public JAXBElement<Integer> createNrOfUnmeasuredOuterRings(Integer value) {
        return new JAXBElement<Integer>(_NrOfUnmeasuredOuterRings_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "altitude")
    public JAXBElement<Double> createAltitude(Double value) {
        return new JAXBElement<Double>(_Altitude_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "processing")
    public JAXBElement<String> createProcessing(String value) {
        return new JAXBElement<String>(_Processing_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Date }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "requestDate")
    public JAXBElement<Date> createRequestDate(Date value) {
        return new JAXBElement<Date>(_RequestDate_QNAME, Date.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ControlledVoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "taxon")
    public JAXBElement<ControlledVoc> createTaxon(ControlledVoc value) {
        return new JAXBElement<ControlledVoc>(_Taxon_QNAME, ControlledVoc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "authenticity")
    public JAXBElement<String> createAuthenticity(String value) {
        return new JAXBElement<String>(_Authenticity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "usedSoftware")
    public JAXBElement<String> createUsedSoftware(String value) {
        return new JAXBElement<String>(_UsedSoftware_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tridas.org/1.2.2", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

}
