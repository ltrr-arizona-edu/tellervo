
package net.opengis.gml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.opengis.gml package. 
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

    private final static QName __SurfacePatch_QNAME = new QName("http://www.opengis.net/gml", "_SurfacePatch");
    private final static QName __Curve_QNAME = new QName("http://www.opengis.net/gml", "_Curve");
    private final static QName __GeometricAggregate_QNAME = new QName("http://www.opengis.net/gml", "_GeometricAggregate");
    private final static QName __GML_QNAME = new QName("http://www.opengis.net/gml", "_GML");
    private final static QName __GeometricPrimitive_QNAME = new QName("http://www.opengis.net/gml", "_GeometricPrimitive");
    private final static QName _Curve_QNAME = new QName("http://www.opengis.net/gml", "Curve");
    private final static QName _LinearRing_QNAME = new QName("http://www.opengis.net/gml", "LinearRing");
    private final static QName _LineStringSegment_QNAME = new QName("http://www.opengis.net/gml", "LineStringSegment");
    private final static QName __CurveSegment_QNAME = new QName("http://www.opengis.net/gml", "_CurveSegment");
    private final static QName _Interior_QNAME = new QName("http://www.opengis.net/gml", "interior");
    private final static QName _Exterior_QNAME = new QName("http://www.opengis.net/gml", "exterior");
    private final static QName _Surface_QNAME = new QName("http://www.opengis.net/gml", "Surface");
    private final static QName _Point_QNAME = new QName("http://www.opengis.net/gml", "Point");
    private final static QName _Description_QNAME = new QName("http://www.opengis.net/gml", "description");
    private final static QName _MultiSurface_QNAME = new QName("http://www.opengis.net/gml", "MultiSurface");
    private final static QName _PolygonPatch_QNAME = new QName("http://www.opengis.net/gml", "PolygonPatch");
    private final static QName _LineString_QNAME = new QName("http://www.opengis.net/gml", "LineString");
    private final static QName __Object_QNAME = new QName("http://www.opengis.net/gml", "_Object");
    private final static QName __Feature_QNAME = new QName("http://www.opengis.net/gml", "_Feature");
    private final static QName __Surface_QNAME = new QName("http://www.opengis.net/gml", "_Surface");
    private final static QName _Polygon_QNAME = new QName("http://www.opengis.net/gml", "Polygon");
    private final static QName __Geometry_QNAME = new QName("http://www.opengis.net/gml", "_Geometry");
    private final static QName _MultiPoint_QNAME = new QName("http://www.opengis.net/gml", "MultiPoint");
    private final static QName _MultiCurve_QNAME = new QName("http://www.opengis.net/gml", "MultiCurve");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.opengis.gml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MultiPointType }
     * 
     */
    public MultiPointType createMultiPointType() {
        return new MultiPointType();
    }

    /**
     * Create an instance of {@link AbstractFeatureBaseType }
     * 
     */
    public AbstractFeatureBaseType createAbstractFeatureBaseType() {
        return new AbstractFeatureBaseType();
    }

    /**
     * Create an instance of {@link Pos }
     * 
     */
    public Pos createPos() {
        return new Pos();
    }

    /**
     * Create an instance of {@link AbstractRingPropertyType }
     * 
     */
    public AbstractRingPropertyType createAbstractRingPropertyType() {
        return new AbstractRingPropertyType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link MultiGeometryPropertyType }
     * 
     */
    public MultiGeometryPropertyType createMultiGeometryPropertyType() {
        return new MultiGeometryPropertyType();
    }

    /**
     * Create an instance of {@link PointMember }
     * 
     */
    public PointMember createPointMember() {
        return new PointMember();
    }

    /**
     * Create an instance of {@link GeometryPropertyType }
     * 
     */
    public GeometryPropertyType createGeometryPropertyType() {
        return new GeometryPropertyType();
    }

    /**
     * Create an instance of {@link CurveMember }
     * 
     */
    public CurveMember createCurveMember() {
        return new CurveMember();
    }

    /**
     * Create an instance of {@link CurveType }
     * 
     */
    public CurveType createCurveType() {
        return new CurveType();
    }

    /**
     * Create an instance of {@link LineStringSegmentType }
     * 
     */
    public LineStringSegmentType createLineStringSegmentType() {
        return new LineStringSegmentType();
    }

    /**
     * Create an instance of {@link PolygonType }
     * 
     */
    public PolygonType createPolygonType() {
        return new PolygonType();
    }

    /**
     * Create an instance of {@link PointType }
     * 
     */
    public PointType createPointType() {
        return new PointType();
    }

    /**
     * Create an instance of {@link MultiPointPropertyType }
     * 
     */
    public MultiPointPropertyType createMultiPointPropertyType() {
        return new MultiPointPropertyType();
    }

    /**
     * Create an instance of {@link MultiCurveType }
     * 
     */
    public MultiCurveType createMultiCurveType() {
        return new MultiCurveType();
    }

    /**
     * Create an instance of {@link Segments }
     * 
     */
    public Segments createSegments() {
        return new Segments();
    }

    /**
     * Create an instance of {@link LineStringType }
     * 
     */
    public LineStringType createLineStringType() {
        return new LineStringType();
    }

    /**
     * Create an instance of {@link SurfaceMember }
     * 
     */
    public SurfaceMember createSurfaceMember() {
        return new SurfaceMember();
    }

    /**
     * Create an instance of {@link Patches }
     * 
     */
    public Patches createPatches() {
        return new Patches();
    }

    /**
     * Create an instance of {@link MeasureType }
     * 
     */
    public MeasureType createMeasureType() {
        return new MeasureType();
    }

    /**
     * Create an instance of {@link BoundedBy }
     * 
     */
    public BoundedBy createBoundedBy() {
        return new BoundedBy();
    }

    /**
     * Create an instance of {@link PolygonPatchType }
     * 
     */
    public PolygonPatchType createPolygonPatchType() {
        return new PolygonPatchType();
    }

    /**
     * Create an instance of {@link PosList }
     * 
     */
    public PosList createPosList() {
        return new PosList();
    }

    /**
     * Create an instance of {@link LinearRingType }
     * 
     */
    public LinearRingType createLinearRingType() {
        return new LinearRingType();
    }

    /**
     * Create an instance of {@link SurfaceType }
     * 
     */
    public SurfaceType createSurfaceType() {
        return new SurfaceType();
    }

    /**
     * Create an instance of {@link AbstractSurfaceType }
     * 
     */
    public AbstractSurfaceType createAbstractSurfaceType() {
        return new AbstractSurfaceType();
    }

    /**
     * Create an instance of {@link MultiSurfacePropertyType }
     * 
     */
    public MultiSurfacePropertyType createMultiSurfacePropertyType() {
        return new MultiSurfacePropertyType();
    }

    /**
     * Create an instance of {@link MultiCurvePropertyType }
     * 
     */
    public MultiCurvePropertyType createMultiCurvePropertyType() {
        return new MultiCurvePropertyType();
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link Envelope }
     * 
     */
    public Envelope createEnvelope() {
        return new Envelope();
    }

    /**
     * Create an instance of {@link MultiSurfaceType }
     * 
     */
    public MultiSurfaceType createMultiSurfaceType() {
        return new MultiSurfaceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSurfacePatchType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_SurfacePatch")
    public JAXBElement<AbstractSurfacePatchType> create_SurfacePatch(AbstractSurfacePatchType value) {
        return new JAXBElement<AbstractSurfacePatchType>(__SurfacePatch_QNAME, AbstractSurfacePatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractCurveType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_Curve", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricPrimitive")
    public JAXBElement<AbstractCurveType> create_Curve(AbstractCurveType value) {
        return new JAXBElement<AbstractCurveType>(__Curve_QNAME, AbstractCurveType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGeometricAggregateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_GeometricAggregate", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Geometry")
    public JAXBElement<AbstractGeometricAggregateType> create_GeometricAggregate(AbstractGeometricAggregateType value) {
        return new JAXBElement<AbstractGeometricAggregateType>(__GeometricAggregate_QNAME, AbstractGeometricAggregateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGMLType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_GML", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Object")
    public JAXBElement<AbstractGMLType> create_GML(AbstractGMLType value) {
        return new JAXBElement<AbstractGMLType>(__GML_QNAME, AbstractGMLType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGeometricPrimitiveType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_GeometricPrimitive", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Geometry")
    public JAXBElement<AbstractGeometricPrimitiveType> create_GeometricPrimitive(AbstractGeometricPrimitiveType value) {
        return new JAXBElement<AbstractGeometricPrimitiveType>(__GeometricPrimitive_QNAME, AbstractGeometricPrimitiveType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurveType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "Curve", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Curve")
    public JAXBElement<CurveType> createCurve(CurveType value) {
        return new JAXBElement<CurveType>(_Curve_QNAME, CurveType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinearRingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "LinearRing", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Geometry")
    public JAXBElement<LinearRingType> createLinearRing(LinearRingType value) {
        return new JAXBElement<LinearRingType>(_LinearRing_QNAME, LinearRingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LineStringSegmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "LineStringSegment", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_CurveSegment")
    public JAXBElement<LineStringSegmentType> createLineStringSegment(LineStringSegmentType value) {
        return new JAXBElement<LineStringSegmentType>(_LineStringSegment_QNAME, LineStringSegmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractCurveSegmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_CurveSegment")
    public JAXBElement<AbstractCurveSegmentType> create_CurveSegment(AbstractCurveSegmentType value) {
        return new JAXBElement<AbstractCurveSegmentType>(__CurveSegment_QNAME, AbstractCurveSegmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractRingPropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "interior")
    public JAXBElement<AbstractRingPropertyType> createInterior(AbstractRingPropertyType value) {
        return new JAXBElement<AbstractRingPropertyType>(_Interior_QNAME, AbstractRingPropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractRingPropertyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "exterior")
    public JAXBElement<AbstractRingPropertyType> createExterior(AbstractRingPropertyType value) {
        return new JAXBElement<AbstractRingPropertyType>(_Exterior_QNAME, AbstractRingPropertyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "Surface", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Surface")
    public JAXBElement<SurfaceType> createSurface(SurfaceType value) {
        return new JAXBElement<SurfaceType>(_Surface_QNAME, SurfaceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PointType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "Point", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricPrimitive")
    public JAXBElement<PointType> createPoint(PointType value) {
        return new JAXBElement<PointType>(_Point_QNAME, PointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "MultiSurface", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricAggregate")
    public JAXBElement<MultiSurfaceType> createMultiSurface(MultiSurfaceType value) {
        return new JAXBElement<MultiSurfaceType>(_MultiSurface_QNAME, MultiSurfaceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolygonPatchType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "PolygonPatch", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_SurfacePatch")
    public JAXBElement<PolygonPatchType> createPolygonPatch(PolygonPatchType value) {
        return new JAXBElement<PolygonPatchType>(_PolygonPatch_QNAME, PolygonPatchType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LineStringType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "LineString", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Curve")
    public JAXBElement<LineStringType> createLineString(LineStringType value) {
        return new JAXBElement<LineStringType>(_LineString_QNAME, LineStringType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_Object")
    public JAXBElement<Object> create_Object(Object value) {
        return new JAXBElement<Object>(__Object_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractFeatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_Feature", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GML")
    public JAXBElement<AbstractFeatureType> create_Feature(AbstractFeatureType value) {
        return new JAXBElement<AbstractFeatureType>(__Feature_QNAME, AbstractFeatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSurfaceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_Surface", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricPrimitive")
    public JAXBElement<AbstractSurfaceType> create_Surface(AbstractSurfaceType value) {
        return new JAXBElement<AbstractSurfaceType>(__Surface_QNAME, AbstractSurfaceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PolygonType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "Polygon", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_Surface")
    public JAXBElement<PolygonType> createPolygon(PolygonType value) {
        return new JAXBElement<PolygonType>(_Polygon_QNAME, PolygonType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractGeometryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "_Geometry", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GML")
    public JAXBElement<AbstractGeometryType> create_Geometry(AbstractGeometryType value) {
        return new JAXBElement<AbstractGeometryType>(__Geometry_QNAME, AbstractGeometryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiPointType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "MultiPoint", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricAggregate")
    public JAXBElement<MultiPointType> createMultiPoint(MultiPointType value) {
        return new JAXBElement<MultiPointType>(_MultiPoint_QNAME, MultiPointType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiCurveType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.opengis.net/gml", name = "MultiCurve", substitutionHeadNamespace = "http://www.opengis.net/gml", substitutionHeadName = "_GeometricAggregate")
    public JAXBElement<MultiCurveType> createMultiCurve(MultiCurveType value) {
        return new JAXBElement<MultiCurveType>(_MultiCurve_QNAME, MultiCurveType.class, null, value);
    }

}
