package org.tellervo.desktop.sample;

import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tridas.schema.TridasIdentifier;

import junit.framework.TestCase;

public class TellervoWSILoaderTest extends TestCase {

	public void testDerivedSampleUsesDerivedSeriesResource() {
		TellervoWSILoader loader = loaderFor(SampleType.REDATE);

		SeriesResource readResource = loader.getResource(null);
		SeriesResource deleteResource = loader.getDeletionResource();

		assertEquals("derivedSeries", readResource.getResourceName());
		assertEquals("derivedSeries", deleteResource.getResourceName());
	}

	public void testDirectSampleUsesMeasurementSeriesResource() {
		TellervoWSILoader loader = loaderFor(SampleType.DIRECT);

		assertEquals("measurementSeries", loader.getResource(null).getResourceName());
		assertEquals("measurementSeries", loader.getDeletionResource().getResourceName());
	}

	private TellervoWSILoader loaderFor(SampleType sampleType) {
		TridasIdentifier identifier = new TridasIdentifier();
		identifier.setDomain("test.example");
		identifier.setValue("00000000-0000-0000-0000-000000000001");

		BaseSample sample = new BaseSample();
		sample.setSampleType(sampleType);
		TellervoWSILoader loader = new TellervoWSILoader(identifier);
		loader.preload(sample);
		return loader;
	}
}
