package edu.cornell.dendro.corina.webdbi;

/**
 * NOTE: Always returns CachedElements!
 */

import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import edu.cornell.dendro.corina.formats.CorinaXML;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.formats.Tridas;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.TridasWSElement;
import edu.cornell.dendro.corina.tridas.TridasIdentifier;

public class MeasurementSearchResource extends ResourceObject<ElementList> {
		/**
		 * Constructor for getting information from the server
		 */
		public MeasurementSearchResource() {
			super("measurementsearch", ResourceQueryType.SEARCH);
			
			setSearchParameters(new SearchParameters("measurementSeries"));
		}

		/**
		 * Prepare the query document, which is in the format below.
		 * The element passed is "request." 
		 * This is returned so the method can be chained.
		 * 
		 * <corina>
		 *    <request type="action">
		 *    </request>
		 * </corina>
		 * 
		 * @param requestElement 
		 */
		@Override
		protected Element prepareQuery(ResourceQueryType queryType, Element requestElement) throws ResourceException {
			// simple: prepare a search with our specified parameters...
			requestElement.addContent(getSearchParameters().getXMLElement());
			requestElement.setAttribute("format", "summary");
			return requestElement;
		}
		
		/**
		 * In this function, parse the document and populate any internal variables.
		 * 
		 * WARNING: This function *must* be threadsafe. 
		 * This means it must not change any class variables without synchronizing against them!
		 * 
		 * Any UI responses should use a ResourceListener. NO DIALOGS HERE. :)
		 * 
		 * If this function returns false, queryFailed is not called
		 * If it throws an exception, queryFailed IS called
		 * 
		 * @param doc The XML JDOM document obtained by the query function
		 * @return true on success, false on failure
		 * @throws ResourceException if error parsing
		 */
		@Override
		protected boolean processQueryResult(Document doc) throws ResourceException {
			// Extract root and specimen elements from returned XML file
			Element root = doc.getRootElement();

			Element content = root.getChild("content", edu.cornell.dendro.corina.webdbi.CorinaXML.CORINA_NS);
			if(content == null)
				throw new MalformedDocumentException(doc, "No content element in measurement");
			
			// get a list of measurements (note: can be empty!)
			List<Element> measurementElements = content.getChildren();

			// create a list of elements
			ElementList elist = new ElementList();
			Tridas tridas = new Tridas();

			for(Element meas : measurementElements) {
				// only tridas elements
				if(!meas.getNamespace().equals(edu.cornell.dendro.corina.webdbi.CorinaXML.TRIDAS_NS))
					continue;
				
				// we only want measurementSeries or derivedSeries here - and we ignore this distinction
				if(!(meas.getName().equals("measurementSeries") || meas.getName().equals("derivedSeries")))
					continue;
				
				try {
					// load it into a basic sample
					BaseSample bs = new BaseSample();
					tridas.loadBasicSeries(bs, meas);
					
					edu.cornell.dendro.corina.sample.Element cachedElement;

					// now, assign the loader to our sample
					bs.setLoader(new TridasWSElement((TridasIdentifier) bs.getMeta(Metadata.TRIDAS_IDENTIFIER)));
					
					// use that to make a cached element...
					cachedElement = new CachedElement(bs);
					
					// add it to our element list
					elist.add(cachedElement);
				} catch (IOException ioe) {
					new Bug(ioe);
				}
			}
		
			setObject(elist);
			return true;
		}
		
		/**
		 * Note this is called in the context of the parsing thread
		 * NO dialogs or anything here, just internal stuff. UI responses should
		 * use a ResourceListener
		 */
		@Override
		protected void queryFailed(Exception e) {
			super.queryFailed(e);
		}
	}