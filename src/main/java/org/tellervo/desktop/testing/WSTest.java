package org.tellervo.desktop.testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.index.Index;
import org.tellervo.desktop.index.IndexSet;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.tridasv2.support.XMLDateUtils;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.Certainty;
import org.tridas.schema.ComplexPresenceAbsence;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasMeasuringMethod;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.ObjectFactory;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasAddress;
import org.tridas.schema.TridasBark;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasHeartwood;
import org.tridas.schema.TridasLaboratory;
import org.tridas.schema.TridasLocation;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasMeasuringMethod;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasPith;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasSapwood;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasValue;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;
import org.tridas.schema.TridasWoodCompleteness;
import org.tridas.spatial.SpatialUtils;
import org.tridas.util.TridasObjectEx;

public class WSTest {

	protected WSTestKey key;
	protected ITridas returnObject;
	protected String errorMessage;
	protected Boolean testResult;
	
	/**
	 * Set the WSTestKey for the type of test that is being run
	 * 
	 * @param key
	 */
	public void setKey(WSTestKey key) {
		this.key = key;
	}
	
	/**
	 * Returns True/False if the test is successful or has failed
	 * 
	 * @return
	 */
	public Boolean getTestResult() {
		return testResult;
	}

	/**
	 * Get the ITridas object that is returned from the test 
	 * 
	 * @return
	 */
	public ITridas getReturnObject() {
		return returnObject;
	}
	
	/**
	 * Get an error message asssociated with a failed test
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	
	/**
	 * Get the WSITestKey representing the type of test being run
	 * 
	 * @return
	 */
	public WSTestKey getKey() {
		return key;
	} 
	

	/**
	 * Run a test to create a project
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @return
	 */
	public boolean runTestCreateProject(Integer currentTest, Integer totalTests) 
	{
		TridasProject project = new TridasProject();
		project.setTitle("WSTEST");
		project.setInvestigator("TEST");
		project.setPeriod("TEST");
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary
				.getMutableDictionary("projectTypeDictionary");
		project.setTypes(types);
		
		@SuppressWarnings("unchecked")
		List<ControlledVoc> categories = Dictionary
				.getMutableDictionary("projectCategoryDictionary");
		project.setCategory(categories.get(0));
		
		ArrayList<TridasLaboratory> laboratories = new ArrayList<TridasLaboratory>();
		TridasLaboratory lab = new TridasLaboratory();
		TridasLaboratory.Name labName = new TridasLaboratory.Name();
		labName.setAcronym(App.getLabAcronym());
		labName.setValue(App.getLabName());
		lab.setName(labName);
		TridasAddress address = new TridasAddress();
		lab.setAddress(address);	
		laboratories.add(lab);
		project.setLaboratories(laboratories);
						
		EntityResource<TridasProject> resource = new EntityResource<TridasProject>(project, TellervoRequestType.CREATE, TridasProject.class);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}	
	}
	
	/**
	 * Run a test by deleting a project
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param inputProject - project to delete
	 * @return
	 */
	public boolean runTestDeleteProject(Integer currentTest, Integer totalTests, TridasProject inputObject) {

		EntityResource<TridasProject> resource = new EntityResource<TridasProject>(inputObject, TellervoRequestType.DELETE, TridasProject.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = inputObject;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}
	
	/**
	 * Run a test to create a TridasObject
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parentProject
	 * @return
	 */
	public boolean runTestCreateObject(Integer currentTest, Integer totalTests, TridasProject parentProject) {
		TridasObjectEx object = new TridasObjectEx();
		object.setTitle("WSTEST");	
		TridasUtils.setObjectCode(object, "WSTEST");
		TridasUtils.setProject(object, parentProject);
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary
				.getMutableDictionary("objectTypeDictionary");
		object.setType(types.get(1));
		
		TridasLocation loc = new TridasLocation();
		loc.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(
				90.0,
				175.0));
		object.setLocation(loc);

		
		EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(object, TellervoRequestType.CREATE, TridasObjectEx.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}

	/**
	 * Run a test to create a TRiDaS subobject
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parentObject
	 * @return
	 */
	public boolean runTestCreateSubobject(Integer currentTest, Integer totalTests, TridasObject parentObject) {
		TridasObjectEx object = new TridasObjectEx();
		object.setTitle("WSTEST");	
		TridasUtils.setObjectCode(object, "WSTEST");
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary
				.getMutableDictionary("objectTypeDictionary");
		object.setType(types.get(1));
		
		TridasLocation loc = new TridasLocation();
		loc.setLocationGeometry(SpatialUtils.getWGS84LocationGeometry(
				90.0,
				175.0));
		object.setLocation(loc);

		EntityResource<TridasObjectEx> resource = new EntityResource<TridasObjectEx>(object, parentObject, TridasObjectEx.class);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}
	
	/**
	 * Run a test to delete a TridasObject
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param inputObject
	 * @return
	 */
	public boolean runTestDeleteObject(Integer currentTest, Integer totalTests, TridasObject inputObject) {
	

		EntityResource<TridasObject> resource = new EntityResource<TridasObject>(inputObject, TellervoRequestType.DELETE, TridasObject.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = inputObject;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}
	
	/**
	 * Run a test to delete a TRiDaS subobject
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param inputObject
	 * @return
	 */
	public boolean runTestDeleteSubobject(Integer currentTest, Integer totalTests, TridasObject inputObject) {
		
		EntityResource<TridasObject> resource = new EntityResource<TridasObject>(inputObject, TellervoRequestType.DELETE, TridasObject.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = inputObject;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}
	
	/**
	 * Run a test to create a TridasElement
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parent
	 * @return
	 */
	public boolean runTestCreateElement(Integer currentTest, Integer totalTests, TridasObject parent) {
	
		TridasElement element = new TridasElement();
		element.setTitle("WSTEST");
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary
				.getMutableDictionary("elementTypeDictionary");
		element.setType(types.get(1));
		
		@SuppressWarnings("unchecked")
		List<ControlledVoc> taxa = Dictionary
				.getMutableDictionary("taxonDictionary");
		element.setTaxon(taxa.get(50));
		
		
		EntityResource<TridasElement> resource = new EntityResource<TridasElement>(element, parent, TridasElement.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}

	/**
	 * Run a test to delete a TridasElement
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param element
	 * @return
	 */
	public boolean runTestDeleteElement(Integer currentTest, Integer totalTests, TridasElement element) {
	

		EntityResource<TridasElement> resource = new EntityResource<TridasElement>(element, TellervoRequestType.DELETE, TridasElement.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = element;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}

	/**
	 * Run a test to create a WSIBox
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @return
	 */
	public boolean runTestCreateBox(Integer currentTest, Integer totalTests) {
	
		WSIBox box = new WSIBox();
		box.setTitle("WSTEST");
				
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(box, TellervoRequestType.CREATE, WSIBox.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}

	/**
	 * Run a test to delete a WSIBox
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param element
	 * @return
	 */
	public boolean runTestDeleteBox(Integer currentTest, Integer totalTests, WSIBox box) {
	
		EntityResource<TridasElement> resource = new EntityResource<TridasElement>(box, TellervoRequestType.DELETE, TridasElement.class);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = box;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}	
	
	/**
	 * Run a test to create a TridasSample
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parent
	 * @return
	 */
	public boolean runTestCreateSample(Integer currentTest, Integer totalTests,  TridasElement parent) {
	
		TridasSample sample = new TridasSample();
		sample.setTitle("WSTEST");
		@SuppressWarnings("unchecked")
		List<ControlledVoc> types = Dictionary
				.getMutableDictionary("sampleTypeDictionary");
		sample.setType(types.get(1));
		
		EntityResource<TridasSample> resource = new EntityResource<TridasSample>(sample, parent, TridasSample.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}

	/**
	 * Run a test to delete a TridasSample
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param sample
	 * @return
	 */
	public boolean runTestDeleteSample(Integer currentTest, Integer totalTests, TridasSample sample) {
	

		EntityResource<TridasSample> resource = new EntityResource<TridasSample>(sample, TellervoRequestType.DELETE, TridasSample.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = sample;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}	
	
	/**
	 * Run a test to create a TridasRadius
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parent
	 * @return
	 */
	public boolean runTestCreateRadius(Integer currentTest, Integer totalTests,  TridasSample parent) {
	
		TridasRadius radius = new TridasRadius();
		radius.setTitle("WSTEST");
		TridasWoodCompleteness wc = new ObjectFactory().createTridasWoodCompleteness();
		
		TridasPith pith = new ObjectFactory().createTridasPith();
		TridasHeartwood heartwd = new ObjectFactory().createTridasHeartwood();
		TridasSapwood sapwd = new ObjectFactory().createTridasSapwood();
		TridasBark bark = new ObjectFactory().createTridasBark();
		
		pith.setPresence(ComplexPresenceAbsence.UNKNOWN);
		heartwd.setPresence(ComplexPresenceAbsence.UNKNOWN);
		sapwd.setPresence(ComplexPresenceAbsence.UNKNOWN);
		bark.setPresence(PresenceAbsence.ABSENT);
		
		wc.setPith(pith);
		wc.setHeartwood(heartwd);
		wc.setSapwood(sapwd);
		wc.setBark(bark);

		radius.setWoodCompleteness(wc);
		
		EntityResource<TridasRadius> resource = new EntityResource<TridasRadius>(radius, parent, TridasRadius.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult();
			return true;
		}
	}

	public void setTestResult(Boolean testResult) {
		this.testResult = testResult;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Run a test to delete a TridasRadius
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param radius
	 * @return
	 */
	public boolean runTestDeleteRadius(Integer currentTest, Integer totalTests, TridasRadius radius) {
	

		EntityResource<TridasRadius> resource = new EntityResource<TridasRadius>(radius, TellervoRequestType.DELETE, TridasRadius.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = radius;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}	
	
	/**
	 * Run a test to create a TridasMeasurementSeries
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param parent
	 * @return
	 */
	public boolean runTestCreateSeries(Integer currentTest, Integer totalTests,  TridasRadius parent) {
	
		TridasMeasurementSeries series = new TridasMeasurementSeries();
		series.setTitle("WSTEST");

		ArrayList<TridasValue> ringWidthValues = new ArrayList<TridasValue>();
		int[] val = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100}; 
		
		for (int i = 0; i<val.length ; i++) {
			TridasValue v = new TridasValue();			
			v.setValue(String.valueOf(val[i]));
			ringWidthValues.add(v);
		}
	
		TridasValues valuesGroup = new TridasValues();
		TridasVariable variable = new TridasVariable();
		TridasUnit units = new TridasUnit();
		variable.setNormalTridas(NormalTridasVariable.RING_WIDTH);
		units.setNormalTridas(NormalTridasUnit.HUNDREDTH_MM);
		valuesGroup.setValues(ringWidthValues);
		valuesGroup.setVariable(variable);
		valuesGroup.setUnit(units);

		ArrayList<TridasValues> valuesGroupList = new ArrayList<TridasValues>();
		valuesGroupList.add(valuesGroup);
		
		series.setValues(valuesGroupList);
		series.setMeasuringDate(XMLDateUtils.toDate(new Date(), Certainty.EXACT));
		series.setAnalyst("TEST");
		series.setDendrochronologist("TEST");
		TridasMeasuringMethod method = new TridasMeasuringMethod();
		method.setNormalTridas(NormalTridasMeasuringMethod.MEASURING_PLATFORM);
		series.setMeasuringMethod(method);
		
		//SeriesResource resource = new SeriesResource(series, parent, TridasMeasurementSeries.class);
		SeriesResource resource = new SeriesResource((ITridasSeries)series, parent.getIdentifier().getValue(), TellervoRequestType.CREATE);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();
		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = null;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = resource.getAssociatedResult().get(0).getSeries();			
			return true;
		}
	}

	/**
	 * Run a test to delete a TridasMeasurementSeries
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param radius
	 * @return
	 */
	public boolean runTestDeleteSeries(Integer currentTest, Integer totalTests, TridasMeasurementSeries series) {
	

		EntityResource<TridasMeasurementSeries> resource = new EntityResource<TridasMeasurementSeries>(series, TellervoRequestType.DELETE, TridasMeasurementSeries.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = series;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}	
	
	/**
	 * Run tests to check the taxonomy dictionary is valid
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @return
	 */
	public boolean runTestTaxonDictionary(Integer currentTest, Integer totalTests) 
	{
		ArrayList<ControlledVoc> taxa = (ArrayList<ControlledVoc>) Dictionary.getDictionaryAsArrayList("taxonDictionary");
			
		for(ControlledVoc taxon : taxa) {
			
			if(taxon.getNormalId().startsWith("X-")) {
				this.errorMessage="Taxonomy dictionary contains legacy entries beginning with X-";
				this.testResult=false;
				return false;
			}
				
			if(taxon.getNormalId().equals("")){
				this.errorMessage="Taxonomy dictionary contains an entry with a blank ID";
				this.testResult=false;
				return false;
			}
			
			if(taxon.getNormalId().equals("281")){
				this.errorMessage="Taxonomy dictionary contains legacy entries (e.g. 281)";
				this.testResult=false;
				return false;
			}
			
		}
		
		if(taxa.size()<850)
		{
			this.errorMessage="Taxonomy dictionary only contains "+taxa.size()+" entries. Expecting more than 850";
			this.testResult=false;
			return false;			
		}
		
		this.testResult=true;
		return true;
	}
	
	/**
	 * Run test on remark dictionary
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @return
	 */
	public boolean runTestRemarkDictionary(Integer currentTest, Integer totalTests) 
	{
		List<ControlledVoc> remarks = Dictionary.getMutableDictionary("readingNoteDictionary");

		
		if(remarks.size()<30)
		{
			this.errorMessage="Ring remarks dictionary only contains "+remarks.size()+" entries. Expecting more than 30";
			this.testResult=false;
			return false;			
		}
		
		this.testResult=true;
		return true;
	}
	
	
	/**
	 * Run test to check we can create an index
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param series
	 * @return
	 */
	public boolean runTestCreateIndex(Integer currentTest, Integer totalTests,  TridasMeasurementSeries mseries) {
		
		Sample sample = new Sample(mseries);
		
		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// it's a new series! (to force update, set this to the id of the series to update!)
		// call gets a new identifier with the domain of our parent
		series.setIdentifier(NewTridasIdentifier.getInstance(sample.getSeries().getIdentifier()));
		series.setTitle("WSTEST");
		series.setVersion("TEST");

		// it's an index
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.INDEX.toString());
		series.setType(voc);

		// the index type
		IndexSet iset = new IndexSet(sample);
		Index index = iset.indexes.get(1);
		series.setStandardizingMethod(index.getIndexFunction().getDatabaseRepresentation());

		// the sample we're basing this index on
		SeriesLinkUtil.addToSeries(series, sample.getSeries().getIdentifier());
		
		// create a new sample to hold this all
		Sample tmp = new Sample(series);

		
		TellervoWSILoader cwe = new TellervoWSILoader(series.getIdentifier());
		try {
			if(cwe.save(tmp))
			{
				this.returnObject = tmp.getSeries();
				this.testResult = true;
				return true;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.errorMessage = e.getLocalizedMessage();
		}	

		this.returnObject = null;
		this.testResult = false;
		return false;
	}
	
	/**
	 * Run a test to delete a TridasDerivedSeries index
	 * 
	 * @param currentTest
	 * @param totalTests
	 * @param series
	 * @return
	 */
	public boolean runTestDeleteIndex(Integer currentTest, Integer totalTests, TridasDerivedSeries series) {
	

		EntityResource<TridasDerivedSeries> resource = new EntityResource<TridasDerivedSeries>(series, TellervoRequestType.DELETE, TridasDerivedSeries.class);

		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(App.mainWindow, resource, currentTest, totalTests);
		
		resource.query();
		dialog.setVisible(true);
		testResult = dialog.isSuccessful();

		if(!dialog.isSuccessful()) {
			this.errorMessage = dialog.getFailException().getLocalizedMessage();
			this.returnObject = series;
			return false;
		}
		else
		{
			this.errorMessage = null;
			this.returnObject = null;
			return true;
		}
	}	
	
	/**
	 * Enum listing the types of tests that can be run
	 * 	
	 * @author pbrewer
	 *
	 */ 
	public enum WSTestKey{
		TAXON_DICTIONARY ("Check taxonomy dictionary"),
		REMARK_DICTIONARY("Check ring remarks dictionary"),
		CREATE_PROJECT   ("Create project"),
		CREATE_OBJECT    ("Create object"),
		CREATE_SUBOBJECT ("Create subobject"),
		CREATE_ELEMENT   ("Create element"),
		CREATE_BOX       ("Create box"),
		CREATE_SAMPLE    ("Create sample"),
		CREATE_RADIUS    ("Create radius"),
		CREATE_SERIES    ("Create series"),
		CREATE_INDEX     ("Create index of series"),
		
		//SEARCH_SERIES   ("Run search for series"),
		
		DELETE_INDEX     ("Delete index of series"),
		DELETE_SERIES    ("Delete series"),
		DELETE_RADIUS    ("Delete radius"),
		DELETE_SAMPLE    ("Delete sample"),
		DELETE_BOX       ("Delete box"),
		DELETE_ELEMENT   ("Delete element"),
		DELETE_SUBOBJECT ("Delete subobject"),
		DELETE_OBJECT    ("Delete object"),
		DELETE_PROJECT   ("Delete project");
		
		private String fullname;
		
		WSTestKey(String fullname)
		{
			this.fullname = fullname;
		}
		
		public String getDescription()
		{
			return fullname;
		}
		
		public String toString()
		{
			return fullname;
		}		
	}
	
}
