package org.tellervo.desktop.labelgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.tridasv2.NumberThenStringComparator2;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSIBox;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class BoxLabelStyleB10By8 extends AbstractTellervoLabelStyle {

		
    private final static Logger log = LoggerFactory.getLogger(BoxLabelBase.class);
	//Font labelTitleFont = new Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD);
	//Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10f);
	//Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 14f);
	//Font subSubSectionFont = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
	//Font bodyFontLarge = new Font(Font.FontFamily.HELVETICA, 14f);
	//Font tableHeaderFontLarge = new Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD);
	
    Font titleFont = new Font(Font.FontFamily.HELVETICA, 30f);
    Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 18f);
	Font monsterFont = new Font(Font.FontFamily.HELVETICA, 40f, Font.BOLD);
	Font projectFont = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD);
	Font siteFont = new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD);
	Font sampleFont = new Font(Font.FontFamily.HELVETICA, 8f);
	Font labNameFont = new Font(Font.FontFamily.HELVETICA, 10f,Font.BOLD, BaseColor.WHITE);
	
	Boolean doHyphenSummerize = false;
	
	private boolean summerizeSamples = true;
	
	Float headerLineWidth = new Float(0.8);	
	Integer margin = 32;
	Integer pagemargin = 29;
	float padding = 8;
	float logowidth = 0.75f;
	
	Rectangle pageSize;
	
	Boolean showLabelBoundingBox = true;
	
	
	public BoxLabelStyleB10By8(String name, String description, float labelWidth, float labelHeight) {
			  
			super(name, description, 
					ItemType.BOX,
					"Country, State/Region",
					"Evacuation priority",
					"Font size",
					"Box name",
					"Agency jurisdictions");
			
			this.setIsLabelSummarizationTypeConfigurable(true);
			
			this.pageSize = new Rectangle(inchToPoint(labelWidth), inchToPoint(labelHeight));
	   }
	
	
	public BoxLabelStyleB10By8() {
	  
		super("Box label Style B - 10x8", "Box label style B with full summary of contents for a box with a 10x8\\\" face", 
				ItemType.BOX,
				"Country, State/Region",
				"Evacuation priority",
				"Font size",
				"Box name",
				"Agency jurisdictions");
		
		this.setIsLabelSummarizationTypeConfigurable(true);
		
		this.pageSize = new Rectangle(inchToPoint(10f), inchToPoint(8f));;
   }
	




	@Override
	public void outputPDFToStream(java.io.OutputStream output, ArrayList items) throws Exception {
		try {
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, output);
	       
			document.setPageSize(pageSize);			
			document.open();
		
			cb = writer.getDirectContent();			
			
			// Set basic metadata
		    document.addAuthor("Tellervo"); 
		    document.addSubject("Box Label"); 
				
		    for(Object item : items)
		    {
		    	WSIBox b = (WSIBox) item;
		    	try {
		    		writeBoxLabel(b);	
		    	} catch (Exception e)
		    	{
		    		log.debug("Problem writing label...");
		    		log.debug(e.getLocalizedMessage());
		    	}
		    	document.newPage();
		    }
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}

		// Close the document
		try {
			document.close();
		} catch (Exception e)
		{
			Alert.error("Error", "There was a problem creating your labels. \n"+ e.getLocalizedMessage());
		}
	}
	
	/**
	 * Convert dimension in inches to points
	 * 
	 * @param val
	 * @return
	 */
	private Integer inchToPoint(float val)
	{
		return (int) (val*72);
	}

	private float getLabelTop()
	{
		return pageSize.getHeight()-pagemargin;
	}
	
	private float getLabelBottom()
	{
		return pagemargin;
	}
	
	private float getLabelLeft()
	{
		return pagemargin;
	}
	
	private float getLabelLeftWithPadding()
	{
		return getLabelLeft()+this.padding;
		
	}
	
	private float getLabelRight()
	{
		return pageSize.getWidth()-pagemargin;
	}
	
	private float getLabelWidth() 
	{
		return pageSize.getWidth()-pagemargin-pagemargin;
	}
	
	private void writeBoxLabel(WSIBox b) throws Exception
	{
		// Find all objects associated with samples in this box
		SearchParameters objparam = new SearchParameters(SearchReturnObject.OBJECT);
		objparam.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, b.getIdentifier().getValue().toString());
		EntitySearchResource<TridasObject> objresource = new EntitySearchResource<TridasObject>(objparam);
		objresource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(objresource);
		
		objresource.query();	
		dialog.setVisible(true);
		if(!dialog.isSuccessful()) 
		{ 
			throw new Exception("Error getting objects");
		}
		List<TridasObject> objList = objresource.getAssociatedResult();

		log.debug("Object found count: "+objList.size());
		
		
		String countryState = "";
		String labName = App.getLabName();
		String box ="";
		ArrayList<TridasProject> projectList = null;
		
		if(objList.size()==0)
		{
			//Alert.error("Problem", "Box "+b.getTitle()+" does not contain any objects");
			//return;
			
			
		}
		else
		{
			Collections.sort(objList, new TridasComparator());
			
			HashSet<TridasProject> projectSet = new HashSet<TridasProject>();
			for(TridasObject o : objList)
			{
				String id = TridasUtils.getGenericFieldValueByName(o, "tellervo.object.projectid");
				projectSet.add(Dictionary.getTridasProjectByID(id));
			}
				
			projectList = new ArrayList<TridasProject>(new ArrayList<TridasProject>(projectSet));
			Collections.sort(projectList, new TridasComparator());
			
			TridasProject firstProject = projectList.get(0);
			// Variables
	
	    	
		}
		
		// Override some lines if requested
		if(getLine1OverrideText()!=null)
		{
			countryState = getLine1OverrideText();
		}

		if(getLine3OverrideText()!=null)
		{
			Integer fontsize = 8;
			try{
				fontsize = Integer.parseInt(getLine3OverrideText());
			} catch (NumberFormatException e) {
				
			}
			
			if(fontsize <4)
			{
				fontsize = 4;
			}
			if(fontsize >10)
			{
				fontsize = 10;
			}
			
			sampleFont = new Font(Font.FontFamily.HELVETICA, fontsize.floatValue());
			siteFont = new Font(Font.FontFamily.HELVETICA, fontsize.floatValue(), Font.BOLD);

		}
		box = b.getTitle();
		if(getLine4OverrideText()!=null)
		{
			box = getLine4OverrideText();
		}
		
		
		ColumnText ct = new ColumnText(cb);

		
		// Lab Banner Bar
		float labBarHeight = inchToPoint(0.28f);
		float labBarLLY = getLabelTop()-labBarHeight;
		
		Rectangle rect1 = new Rectangle(getLabelLeft(), 
				labBarLLY,
				getLabelRight(),
				getLabelTop());
        rect1.setBackgroundColor(BaseColor.DARK_GRAY);
        rect1.setBorder(Rectangle.BOX);
        rect1.setBorderColor(BaseColor.BLACK);
        rect1.setBorderWidth(0.5f);
        cb.rectangle(rect1);
    	ct = new ColumnText(cb);
		ct.setSimpleColumn(getLabelLeftWithPadding()+3, 
				labBarLLY, 
				getLabelRight(), 
				getLabelTop()-3, 
				10f, 
				Element.ALIGN_LEFT);
		ct.addText(new Phrase(labName, this.labNameFont));
		ct.go();
        
        
		// Box title	
		log.debug("Box title : llx lly urx ury");
		// - do next run first to see what size we need
		ct.setSimpleColumn(
				getLabelLeftWithPadding(), 
				getLabelTop()-inchToPoint(5f), 
				getLabelRight(), 
				getLabelTop(), 
				30f, 
				Element.ALIGN_LEFT);
		ct.addText(new Phrase(box, this.titleFont));
		int status = ct.go(true);
		Integer lines = ct.getLinesWritten();
		log.debug("Number of lines required for box name: "+lines);
		
		float boxTitleLLY = (float) (labBarLLY-(lines.floatValue()*titleFont.getSize()));
		float boxTitleURY = labBarLLY;
		
		if(ColumnText.hasMoreText(status))
		{
			log.debug("Box name too long!!!");
			ct = null;
		}
		else
		{
			log.debug("Title box llx lly urx ury");
			log.debug(getLabelLeftWithPadding()+", "+boxTitleLLY+", "+getLabelRight()+", "+boxTitleURY);
			ct = new ColumnText(cb);
			ct.setSimpleColumn(getLabelLeftWithPadding(), 
					boxTitleLLY, 
					getLabelRight(), 
					boxTitleURY, 
					30f, 
					Element.ALIGN_LEFT);
			ct.addText(new Phrase(box, this.titleFont));
			ct.go();	
		}
		
		// Country and State-Type
		ct = new ColumnText(cb);
		float countryLLY = boxTitleLLY-(subTitleFont.getSize())-16;
		log.debug("countryLLY "+countryLLY);;
		log.debug("Title block coords");
		log.debug(getLabelLeft()+", "+countryLLY+", "+getLabelRight()+", "+boxTitleLLY);
		ct.setSimpleColumn(
				getLabelLeftWithPadding(), 
				countryLLY,
				getLabelRight(), 
				boxTitleLLY-4, 
				20f, 
				Element.ALIGN_LEFT);
		ct.addText(new Phrase(countryState, this.subTitleFont));
		ct.go();

		// Title box outline
		cb.saveState();
		cb.setColorStroke(CMYKColor.BLACK);
		cb.rectangle(getLabelLeft(), 
				countryLLY,
				getLabelWidth(), 
				getLabelTop()-countryLLY);
		cb.stroke();
		cb.restoreState();
		
		
		// Jurisdiction Logos
		if(getLine5OverrideText()!=null)
		{
			
			float xpos = getLabelRight()-inchToPoint(logowidth)-padding;
			float ypos = countryLLY;
			Image image = null;
			
			String[] logonames = getLine5OverrideText().split(" ");
			
			for(String logo : logonames)
			{
				logo = logo.trim();
				String filename =null;
				
				if(logo==null || logo.length()==0) continue;	
				if(logo.toUpperCase().equals("NPS")
						|| logo.toUpperCase().equals("USFS")
						|| logo.toUpperCase().equals("BIA")
						|| logo.toUpperCase().equals("BLM")
						|| logo.toUpperCase().equals("BOR")
						|| logo.toUpperCase().equals("DOI")
						|| logo.toUpperCase().equals("FWS"))
				{
					
					filename = "Images/Agencies/"+logo.toLowerCase()+".png";
				
						
					image = Builder.getITextImage(filename);
			
					log.debug(filename);
				}
				else
				{
					// Custom one in Tellervo folder
					filename = logo+".png";
					
					if(!FileUtils.fileExists(filename)) {
						log.debug("File : "+filename+" does not exist");
						image = null;
					}
					try {
						image = com.itextpdf.text.Image.getInstance(filename);
					} catch (Exception e)
					{
						log.debug("Failed to load file: "+filename);
						image = null;
						
					}
				}
				
				
				if(image==null)
				{
					log.debug("Unrecognized logo");
					continue;
				}
				
	            float scaler = ((inchToPoint(logowidth) / image.getWidth())) * 100;
	            image.scalePercent(scaler);
	            ypos = ypos - image.getScaledHeight()-padding;
	            image.setAbsolutePosition(xpos, ypos);
	            //xpos = xpos + inchToPoint(logowidth) + 20;

	            cb.addImage(image);
			}
		}
		
		// Chroma label box	
		Rectangle rect2 = new Rectangle(getLabelRight()-inchToPoint(3.5f), inchToPoint(1f), getLabelRight(), inchToPoint(3f));
        //rect2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        rect2.setBorder(Rectangle.BOX);
        rect2.setBorderColor(BaseColor.BLACK);
        rect2.setBorderWidth(0.5f);
        cb.rectangle(rect2);
		
        // Side block box
        rect2 = new Rectangle(getLabelRight()-inchToPoint(3.5f), inchToPoint(1f), getLabelRight(), countryLLY);
        //rect2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        rect2.setBorder(Rectangle.BOX);
        rect2.setBorderColor(BaseColor.BLACK);
        rect2.setBorderWidth(0.5f);
        cb.rectangle(rect2);
		
		// Logo block box
		Rectangle rect3 = new Rectangle(getLabelRight()-inchToPoint(logowidth)-padding-padding, inchToPoint(3f), getLabelRight(), countryLLY);
        //rect2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		rect3.setBorder(Rectangle.BOX);
		rect3.setBorderColor(BaseColor.BLACK);
		rect3.setBorderWidth(0.5f);
        cb.rectangle(rect3);

		// Full Label outline - inc cut marks
		if(showLabelBoundingBox)
		{
	        Rectangle rect = new Rectangle(pagemargin, 0, getLabelWidth()+pagemargin, pageSize.getHeight());
	        rect.setBorder(Rectangle.BOX);
	        rect.setBorderWidth(1);
	        rect.setBorderColor(BaseColor.GRAY);
	        cb.rectangle(rect);
			
	        rect = new Rectangle(0, pagemargin, pageSize.getWidth(), pageSize.getHeight()-pagemargin);
	        rect.setBorder(Rectangle.BOX);
	        rect.setBorderWidth(1);
	        rect.setBorderColor(BaseColor.GRAY);
	        cb.rectangle(rect);
	        
	        rect = new Rectangle(pagemargin, pagemargin, getLabelWidth()+pagemargin, pageSize.getHeight()-pagemargin	);
	        rect.setBorder(Rectangle.BOX);
	        rect.setBorderWidth(1);
	        rect.setBorderColor(BaseColor.BLACK);
	        cb.rectangle(rect);
		}
		
		// Page outline box
		cb.saveState();
		cb.setColorStroke(CMYKColor.BLACK);
		cb.rectangle(0, 0, document.getPageSize().getWidth(), document.getPageSize().getHeight());
		cb.stroke();
		cb.restoreState();
       
		// Barcode
		float barheight = 43;
    	ct = new ColumnText(cb);   
		int llx = (int) (getLabelRight()-inchToPoint(3.5f));
    	int lly = (int) getLabelBottom();
    	int urx = (int) getLabelRight();
    	int ury = (int) inchToPoint(1f);
    	int leading = 1;
		log.debug("Barcode box llx lly urx ury");
		log.debug(llx+", "+lly,", "+urx+", "+ury);
		ct.setSimpleColumn(llx,lly ,urx , ury , leading,  Element.ALIGN_CENTER);
		ct.setUseAscender(true);
		ct.addText(new Chunk(LabBarcode.getBoxBarcode(b, cb, barheight, barcodeSize), 0, 0, true));
    	ct.go();
        rect2 = new Rectangle(llx, lly, urx, ury);
        rect2.setBorder(Rectangle.BOX);
        rect2.setBorderColor(BaseColor.BLACK);
        rect2.setBorderWidth(0.5f);
        cb.rectangle(rect2);
		   
        // Evacuation Priority
		if(getLine2OverrideText()!=null && !getLine2OverrideText().toLowerCase().startsWith("false") && getLine2OverrideText().trim()!="")
		{
			rect2 = new Rectangle(getLabelRight()-inchToPoint(3.5f)+3, inchToPoint(3f)+3, getLabelRight()-inchToPoint(logowidth)-padding-padding-3, countryLLY-3);
	        //rect2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        rect2.setBorder(Rectangle.BOX);
	        rect2.setBorderColor(BaseColor.BLACK);
	        rect2.setBorderWidth(5f);
	        cb.rectangle(rect2);
	        
	        ct = new ColumnText(cb);
			ct.setSimpleColumn(getLabelRight()-inchToPoint(3.5f)+3, inchToPoint(3f)+3, getLabelRight()-inchToPoint(logowidth)-padding-padding-3, countryLLY-8,
					10f, 
					Element.ALIGN_CENTER);
			ct.addText(new Phrase("EMERGENCY EVACUATION PRIORITY", this.projectFont));
			ct.go();
			
		}

		// Site and sample summary
    	float sampleListURY = countryLLY-20;
		ct = new ColumnText(cb);
		ct.setSimpleColumn(
				getLabelLeftWithPadding(),
				getLabelBottom(),
				getLabelRight()-inchToPoint(3.5f)-padding, 
				sampleListURY,
				1f,
				Element.ALIGN_LEFT);
		    	
		Collections.sort(projectList, new TridasComparator());
		
		if(projectList !=null)
		{
			for(TridasProject project : projectList)
			{
				Paragraph para = new Paragraph();
				Phrase phrase = new Phrase();
				
				phrase.add(new Chunk(project.getTitle(), this.projectFont));
				phrase.add(new Chunk("  \n\n", this.sampleFont));
				para.add(phrase);
				para.setAlignment(Element.ALIGN_JUSTIFIED);
				ct.addElement(para);
				
				Integer totalSampleCount = 0;
	
				for(TridasObject myobj : objList)
				{
					if(!TridasUtils.getGenericFieldValueByName(myobj, "tellervo.object.projectid").equals(project.getIdentifier().getValue()))
					{
						log.debug("This object isn't in this project");
						continue;
					}
					
					String objCode = "";
					if(myobj instanceof TridasObjectEx) objCode = ((TridasObjectEx)myobj).getMultiLevelLabCode()+" : "; 	
		
					//ct.addText(new Phrase(objCode+myobj.getTitle()+"\n", this.siteFont));
					
					// Search for elements associated with this object
					System.out.println("Starting search for elements associated with " + myobj.getTitle().toString());
					SearchParameters sp = new SearchParameters(SearchReturnObject.ELEMENT);		
					sp.addSearchConstraint(SearchParameterName.SAMPLEBOXID, SearchOperator.EQUALS, b.getIdentifier().getValue());
					sp.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, myobj.getIdentifier().getValue());
					EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(sp);
					resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
					TellervoResourceAccessDialog dialog2 = new TellervoResourceAccessDialog(resource);
					resource.query();
					dialog2.setVisible(true);
					if(!dialog2.isSuccessful()) 
					{ 	
						log.debug("No elements associated with "+myobj.getTitle());
					}
					//XMLDebugView.showDialog();
					List<TridasElement> elements = resource.getAssociatedResult();
					
					if(elements==null || elements.size()==0)
					{
						log.error("No elements found for object "+objCode);
					}
					else
					{
						Collections.sort(elements, new NumberThenStringComparator2());	
						
						// Loop through elements 
						Integer smpCnt = 0;
						ArrayList<String> numlist = new ArrayList<String>();
						ArrayList<String> sampleList = new ArrayList<String>();
						for(TridasElement myelem : elements)
						{
							// Add element title to string
							if(myelem.getTitle()!=null) 
							{
								String mytitle = myelem.getTitle();
								numlist.add(mytitle);
							}
			
							// Grab associated samples and add count to running total
							for(TridasSample sample : myelem.getSamples())
							{
								// Skip samples if they aren't in this box	
								if(!TridasUtils.getGenericFieldValueByName(sample, "tellervo.boxID").equals(b.getIdentifier().getValue())) {
									log.debug("Skipping sample "+sample.getIdentifier().getValue()+" because it's not in this box");
									continue;
								}
								smpCnt++;
								
								if(!this.summarizationType.equals(LabelSummarizationType.ELEMENT))
								{
									String extid = TridasUtils.getGenericFieldValueByName(sample, "tellervo.externalID");
									if(extid!=null && this.summarizationType.equals(LabelSummarizationType.EXTERNALID))
									{
										if(StringUtils.startsWith(extid, ((TridasObjectEx)myobj).getMultiLevelLabCode()))
										{
											String id = extid.substring(((TridasObjectEx)myobj).getMultiLevelLabCode().length()).trim();
											sampleList.add(id);
										}
										else
										{
											sampleList.add(extid);
										}
									}
									else
									{
										sampleList.add(myelem.getTitle()+"-"+sample.getTitle());	
									}
								}
							}
							
							
						}
						
						Collections.sort(numlist, new NumberThenStringComparator2());
						Collections.sort(sampleList, new NumberThenStringComparator2());
						
						Chunk siteNameChunk = new Chunk(objCode+myobj.getTitle()+": ", this.siteFont);
						Paragraph siteParagraph = new Paragraph();
						siteParagraph.setLeading(0, 1.2f);
						siteParagraph.add(siteNameChunk);
						
						Chunk sampleListChunk;
						if(this.summarizationType.equals(LabelSummarizationType.ELEMENT))
						{
							// Display as a summerized list of elements
							sampleListChunk = new Chunk(hyphenSummarize(numlist)+" (n = "+smpCnt+")\n", this.sampleFont);
						}
						else
						{
							// Display as a summerized list of samples
							sampleListChunk = new Chunk(hyphenSummarize(sampleList)+" (n = "+smpCnt+")\n", this.sampleFont);
						}
						siteParagraph.add(sampleListChunk);
						//siteParagraph.add(Chunk.NEWLINE);
						ct.addElement(siteParagraph);
						
						totalSampleCount = totalSampleCount+smpCnt;
					}
					
					//ct.addText(new Phrase("\n", this.sampleFont));
					
				}
			}
		}			
		else
		{
			// Null project - no samples assigned to box
			Chunk siteNameChunk = new Chunk("No samples assigned to box");
			Paragraph siteParagraph = new Paragraph();
			siteParagraph.setLeading(0, 1.2f);
			siteParagraph.add(siteNameChunk);
			ct.addElement(siteParagraph);

		}
		
		//ct.addText(new Phrase("Total samples in box: "+totalSampleCount, this.sampleFont));
		ct.go();
	}
			
	
	public String hyphenSummarize(List<String> lst)
	{
		Integer lastnum = null; 
		Boolean inSeq = false;
		Integer numOfElements = lst.size();
		String returnStr = "";
		Integer cnt = 0;
		
		if(doHyphenSummerize)
		{
		
			try {
				
				/*
				if (none==true)
				{
					for(String item : lst)
					{
						returnStr += item + ", ";
						
					}
					
					if (returnStr.length()>2) returnStr = returnStr.substring(0, returnStr.length()-2);
					return returnStr;
				}*/
		
		
	
			
				for(String item : lst)
				{
					cnt++;
					
					if(item==null || item.length()==0)
					{
						log.debug("Title of this item is empty");
					}
					
					if (containsOnlyNumbers(item))
					{
						// Contains only numbers
						if(lastnum==null)
						{
							// Lastnum is null so just add item to return string and continue to the next in list
							returnStr += ", " + item;
							lastnum = Integer.valueOf(item);
							inSeq=true;
							continue;
						}
						else
						{
							if(inSeq==true)
							{
			
								if(isNextInSeq(lastnum, Integer.valueOf(item)))
								{
									if(cnt==numOfElements)
									{
										// This is the last one in the list!
										returnStr += "-" + item;
										continue;
									}
									else
									{
										// Keep going!
										inSeq = true;
										lastnum = Integer.valueOf(item);
										continue;
									}
								}
								else
								{
									// 
									returnStr += "-" + lastnum.toString() + ", " + item;
									inSeq = false;
									lastnum = Integer.valueOf(item);
									continue;
									
								}
							}
							else
							{
								// Not in sequence yet
								
								if(isNextInSeq(lastnum, Integer.valueOf(item)))
								{
									
									if(cnt==numOfElements)
									{
										// This is the last one in the list!
										returnStr += "-" + item;
										continue;
									}
									else
									{
										// Keep going!
										inSeq = true;
										lastnum = Integer.valueOf(item);
										continue;
									}
								}
								else
								{
									returnStr += ", " + item;
									lastnum = Integer.valueOf(item);
									continue;
								}
							}
								
							
						}
					}
					else
					{
						// Contains some chars so just add as comma delimited string to return string
						returnStr += ", " + item;
						lastnum = null;
						inSeq = null;
					}
					
					
					
					
				}
			} catch (Exception e)
			{
				log.debug("Disaster recovery! ");
				cnt = 0;
			}
		}
		else
		{
			for(String item : lst)
			{
				cnt++;
				if(item==null || item.length()==0)
				{
					log.debug("Title of this item is empty");
				}
				
				returnStr += ", " + item;
			}
		}
		
		if(returnStr.length()>3)
		{
			returnStr = returnStr.substring(2, returnStr.length());
		}
		else
		{
			log.debug("Return string is short? "+returnStr);
		}
		return returnStr;
	}
	
	private Boolean isNextInSeq(Integer seqnum, Integer curnum)
	{
		if(seqnum+1==curnum)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
    /**
     * This method checks if a String contains only numbers
     */
    public boolean containsOnlyNumbers(String str) {
        
        //It can't contain only numbers if it's null or empty...
        if (str == null || str.length() == 0)
            return false;
        
        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        
        return true;
    }
		

	/**
	 * Blank iText paragraph used for padding 
	 * @return Paragraph
	 */
	protected Paragraph getParagraphSpace()
	{
		Paragraph p = new Paragraph();
		
		p.add(new Chunk(" "));
		return p;
	}
	

}