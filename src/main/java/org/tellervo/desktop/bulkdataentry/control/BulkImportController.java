/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.bulkdataentry.control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingUtilities;

import org.tellervo.desktop.bulkdataentry.command.AddRowCommand;
import org.tellervo.desktop.bulkdataentry.command.CopyRowCommand;
import org.tellervo.desktop.bulkdataentry.command.CopySelectedRowsCommand;
import org.tellervo.desktop.bulkdataentry.command.DeleteODKFormDefinitionsCommand;
import org.tellervo.desktop.bulkdataentry.command.DeleteODKFormInstancesCommand;
import org.tellervo.desktop.bulkdataentry.command.DeleteRowCommand;
import org.tellervo.desktop.bulkdataentry.command.DeleteSpecificODKFormDefinitionCommand;
import org.tellervo.desktop.bulkdataentry.command.GPXBrowseCommand;
import org.tellervo.desktop.bulkdataentry.command.HideColumnWindowCommand;
import org.tellervo.desktop.bulkdataentry.command.ImportSelectedElementsCommand;
import org.tellervo.desktop.bulkdataentry.command.ImportSelectedObjectsCommand;
import org.tellervo.desktop.bulkdataentry.command.ImportSelectedSamplesCommand;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromBoxCommand;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromDatabaseCommand;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromGeonamesCommand;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromODKCommand;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromProjectCommand;
import org.tellervo.desktop.bulkdataentry.command.PrintBarcodesCommand;
import org.tellervo.desktop.bulkdataentry.command.RemoveSelectedCommand;
import org.tellervo.desktop.bulkdataentry.command.ShowColumnWindowCommand;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ColumnListModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.view.BulkDataEntryWindow;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.schema.UserExtendableEntity;
import org.tellervo.schema.WSIUserDefinedField;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.FrontController;
import com.dmurph.mvc.model.MVCArrayList;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;


/**
 * @author daniel
 *
 */
public class BulkImportController extends FrontController {
	public static final String DISPLAY_COLUMN_CHOOSER = "BULK_IMPORT_DISPLAY_COLUMN_CHOOSER";
	public static final String HIDE_COLUMN_CHOOSER = "BULK_IMPORT_HIDE_COLUMN_CHOOSER";
	public static final String ADD_ROW = "BULK_IMPORT_ADD_ROW";
	public static final String COPY_ROW = "BULK_IMPORT_COPY_ROW";
	public static final String DELETE_ROW = "BULK_IMPORT_DELETE_ROW";
	public static final String DISPLAY_BULK_IMPORT = "BULK_IMPORT_DISPLAY_BULK_IMPORT";
	public static final String REMOVE_SELECTED = "BULK_IMPORT_REMOVE_SELECTED_ROW";
	public static final String COPY_SELECTED_ROWS = "BULK_IMPORT_COPY_ALL_SELECTED_ROWS";
	
	public static final String IMPORT_SELECTED_OBJECTS = "BULK_IMPORT_SELECTED_OBJECTS";
	public static final String IMPORT_SELECTED_ELEMENTS = "BULK_IMPORT_SELECTED_ELEMENTS";
	public static final String IMPORT_SELECTED_SAMPLES = "BULK_IMPORT_SELECTED_SAMPLES";
	
	public static final String SET_DYNAMIC_COMBO_BOX_OBJECTS = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_OBJECTS";
	public static final String SET_DYNAMIC_COMBO_BOX_ELEMENTS = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_ELEMENTS";
	public static final String SET_DYNAMIC_COMBO_BOX_SAMPLES = "BULK_IMPORT_SET_DYNAMIC_COMBO_BOX_SAMPLES";
	public static final String POPULATE_FROM_DATABASE = "BULK_IMPORT_POPULATE_FROM_DATABASE";
	public static final String POPULATE_FROM_BOX = "BULK_IMPORT_POPULATE_FROM_BOX";
	public static final String POPULATE_FROM_PROJECT = "BULK_IMPORT_POPULATE_FROM_PROJECT";

	public static final String POPULATE_FROM_GEONAMES = "BULK_IMPORT_POPULATE_FROM_GEONAMES";

	public static final String BROWSE_GPX_FILE = "BULK_IMPORT_BROWSE_GPX_FILE";
	public static final String PRINT_SAMPLE_BARCODES = "BULK_IMPORT_PRINT_BARCODES";
	public static final String POPULATE_FROM_ODK_FILE = "BULK_IMPORT_POPULATE_FROM_ODK_FILE";
	public static final String DELETE_ODK_DEFINITIONS = "BULK_IMPORT_DELETE_ODK_DEFINITIONS";
	public static final String DELETE_ODK_INSTANCES = "BULK_IMPORT_DELETE_ODK_INSTANCES";
	public static final String DELETE_SPECIFIC_ODK_DEFINITION = "BULK_IMPORT_DELETE_SPECIFIC_ODK_INSTANCE";
	

	
	public BulkImportController(){
		registerCommand(DISPLAY_COLUMN_CHOOSER, ShowColumnWindowCommand.class);
		registerCommand(IMPORT_SELECTED_OBJECTS, ImportSelectedObjectsCommand.class);
		registerCommand(HIDE_COLUMN_CHOOSER, HideColumnWindowCommand.class);
		registerCommand(ADD_ROW, AddRowCommand.class);
		registerCommand(COPY_ROW, CopyRowCommand.class);
		registerCommand(COPY_SELECTED_ROWS, CopySelectedRowsCommand.class);
		registerCommand(DELETE_ROW, DeleteRowCommand.class);
		registerCommand(REMOVE_SELECTED, RemoveSelectedCommand.class);
		registerCommand(DISPLAY_BULK_IMPORT, "display");
		registerCommand(IMPORT_SELECTED_ELEMENTS, ImportSelectedElementsCommand.class);
		registerCommand(IMPORT_SELECTED_SAMPLES, ImportSelectedSamplesCommand.class);
		registerCommand(BROWSE_GPX_FILE, GPXBrowseCommand.class);
		registerCommand(PRINT_SAMPLE_BARCODES, PrintBarcodesCommand.class);
		registerCommand(POPULATE_FROM_DATABASE, PopulateFromDatabaseCommand.class);
		registerCommand(POPULATE_FROM_BOX, PopulateFromBoxCommand.class);
		registerCommand(POPULATE_FROM_PROJECT, PopulateFromProjectCommand.class);
		registerCommand(POPULATE_FROM_GEONAMES, PopulateFromGeonamesCommand.class);
		registerCommand(POPULATE_FROM_ODK_FILE, PopulateFromODKCommand.class);
		registerCommand(DELETE_ODK_DEFINITIONS, DeleteODKFormDefinitionsCommand.class);
		registerCommand(DELETE_SPECIFIC_ODK_DEFINITION, DeleteSpecificODKFormDefinitionCommand.class);

		registerCommand(DELETE_ODK_INSTANCES, DeleteODKFormInstancesCommand.class);


	}
	
	public void display(MVCEvent argEvent){
		/*if(MVC.getTracker() == null){
			JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(new AnalyticsConfigData("UA-17109202-7"), GoogleAnalyticsVersion.V_4_7_2);
			MVC.setTracker(tracker);
		}else{
			MVC.getTracker().resetSession();
		}*/
		//MVC.showEventMonitor();
		
		if(BulkImportModel.getInstance().getMainView() != null){
			BulkDataEntryWindow window = BulkImportModel.getInstance().getMainView();
			window.setVisible(true);
			window.toFront();
			return;
		}
		
		BulkImportModel model = BulkImportModel.getInstance();
		populateObjectDefaults(model.getObjectModel().getColumnModel());
		populateElementDefaults(model.getElementModel().getColumnModel());
		populateSampleDefaults(model.getSampleModel().getColumnModel());
		
		// just put inot the sample model for now, change if we want to add extra radius functionality
		populateRadiusDefaults(model.getSampleModel().getColumnModel());
		
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
				BulkDataEntryWindow frame = new BulkDataEntryWindow();
				BulkImportModel.getInstance().setMainView(frame);
				frame.pack();
				frame.setVisible(true);
			        }
		});
		//MVC.showEventMonitor();
	}
	
	/*
	public static ArrayList<String> getChosenObjectColumns()
	{
		ArrayList<String> theArray = new ArrayList<String>();
		
		// First populate an array with all the default columns in it
		ArrayList<String> defaults = new ArrayList<String>();
		defaults.add(SingleObjectModel.IMPORTED);
		defaults.add(SingleObjectModel.OBJECT_CODE);
		defaults.add(SingleObjectModel.TITLE);
		//defaults.add(SingleObjectModel.PARENT_OBJECT);
		defaults.add(SingleObjectModel.PROJECT);
		defaults.add(SingleObjectModel.TYPE);
		defaults.add(SingleObjectModel.COMMENTS);
		defaults.add(SingleObjectModel.DESCRIPTION);
		//defaults.add(SingleObjectModel.FILES);
		defaults.add(SingleObjectModel.LATITUDE);
		defaults.add(SingleObjectModel.LONGITUDE);
		defaults.add(SingleObjectModel.LOCATION_TYPE);
		defaults.add(SingleObjectModel.LOCATION_PRECISION);
		defaults.add(SingleObjectModel.LOCATION_COMMENT);
		//defaults.add(SingleObjectModel.WAYPOINT);
		//defaults.add(SingleObjectModel.ADDRESSLINE1);
		//defaults.add(SingleObjectModel.ADDRESSLINE2);
		//defaults.add(SingleObjectModel.CITY_TOWN);
		defaults.add(SingleObjectModel.STATE_PROVINCE_REGION);
		//defaults.add(SingleObjectModel.POSTCODE);
		defaults.add(SingleObjectModel.COUNTRY);
		//defaults.add(SingleObjectModel.OWNER);
		//defaults.add(SingleObjectModel.CREATOR);
		
		// Grab preferred fields from preferences, falling back to default if not specified
		ArrayList<String> fields = App.prefs.getArrayListPref(PrefKey.OBJECT_FIELD_VISIBILITY_ARRAY, defaults);
		
		// Actually populate model with the fields
		// Done in stupid way to maintain preferred or logical/default ordering 
		for(String f: fields)
		{
			if(defaults.contains(f))
			{
				theArray.add(f);
			}
		}
		for(String f : defaults)
		{
			if(!fields.contains(f))
			{
				theArray.add(f);
			}
		}
		
		return theArray;
	}
	*/
	
	private void populateObjectDefaults(ColumnListModel ccmodel){
		
		//ccmodel.addAll(getChosenObjectColumns());
		
		
		// First populate an array with all the default columns in it
		ArrayList<String> defaults = new ArrayList<String>();
		/*defaults.add(SingleObjectModel.IMPORTED);
		defaults.add(SingleObjectModel.PROJECT);
		defaults.add(SingleObjectModel.PARENT_OBJECT);
		defaults.add(SingleObjectModel.OBJECT_CODE);
		defaults.add(SingleObjectModel.TITLE);
		defaults.add(SingleObjectModel.TYPE);
		defaults.add(SingleObjectModel.COMMENTS);
		defaults.add(SingleObjectModel.DESCRIPTION);
		defaults.add(SingleObjectModel.FILES);
		defaults.add(SingleObjectModel.WAYPOINT);
		defaults.add(SingleObjectModel.LATITUDE);
		defaults.add(SingleObjectModel.LONGITUDE);
		defaults.add(SingleObjectModel.LOCATION_TYPE);
		defaults.add(SingleObjectModel.LOCATION_PRECISION);
		defaults.add(SingleObjectModel.LOCATION_COMMENT);
		defaults.add(SingleObjectModel.ADDRESSLINE1);
		defaults.add(SingleObjectModel.ADDRESSLINE2);
		defaults.add(SingleObjectModel.CITY_TOWN);
		defaults.add(SingleObjectModel.STATE_PROVINCE_REGION);
		defaults.add(SingleObjectModel.POSTCODE);
		defaults.add(SingleObjectModel.COUNTRY);
		defaults.add(SingleObjectModel.OWNER);
		defaults.add(SingleObjectModel.CREATOR);
		
		//ccmodel.addAll(defaults);
		ccmodel.addAll(Arrays.asList(SingleObjectModel.TABLE_PROPERTIES));*/
		
		defaults.addAll(Arrays.asList(SingleObjectModel.TABLE_PROPERTIES));

		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
	
		for(WSIUserDefinedField fld : udfdictionary)
		{
			if(fld.getAttachedto().equals(UserExtendableEntity.OBJECT))
			{
				defaults.add(fld.getLongfieldname());
			}
		}
		Collections.sort(defaults, new Comparator<String>() {
		    @Override
		    public int compare(String o1, String  o2) {
		    	
		    	if(o1.equals("Selected")) return -1;
		    	if(o2.equals("Selected")) return 1;
		    	if(o1.equals("Imported")) return -1;
		    	if(o2.equals("Imported")) return 1;
		    	
		    	return o1.compareTo(o2);
		    }
		});

		ccmodel.addAll(defaults);
		
		
		


		/*
		// Grab preferred fields from preferences, falling back to default if not specified
		ArrayList<String> fields = App.prefs.getArrayListPref(PrefKey.OBJECT_FIELD_VISIBILITY_ARRAY, defaults);
		
		// Actually populate model with the fields
		// Done in stupid way to maintain preferred or logical/default ordering 
		for(String f: fields)
		{
			if(defaults.contains(f))
			{
				ccmodel.add(f);
			}
		}
		for(String f : defaults)
		{
			if(!fields.contains(f))
			{
				ccmodel.add(f);
			}
		}*/
	}
	
	private void populateElementDefaults(ColumnListModel ccmodel){
		
		// First populate an array with all the default columns in it
		ArrayList<String> defaults = new ArrayList<String>();	
		
		/*defaults.add(SingleElementModel.IMPORTED);
		defaults.add(SingleElementModel.OBJECT);
		defaults.add(SingleElementModel.TITLE);
		defaults.add(SingleElementModel.TYPE);
		defaults.add(SingleElementModel.TAXON);
		defaults.add(SingleElementModel.COMMENTS);
		defaults.add(SingleElementModel.DESCRIPTION);
		defaults.add(SingleElementModel.FILES);
		defaults.add(SingleElementModel.SHAPE);
		defaults.add(SingleElementModel.HEIGHT);
		defaults.add(SingleElementModel.WIDTH);
		defaults.add(SingleElementModel.DEPTH);
		defaults.add(SingleElementModel.DIAMETER);
		defaults.add(SingleElementModel.UNIT);
		defaults.add(SingleElementModel.AUTHENTICITY);
		defaults.add(SingleElementModel.WAYPOINT);
		defaults.add(SingleElementModel.LATITUDE);
		defaults.add(SingleElementModel.LONGITUDE);
		defaults.add(SingleElementModel.LOCATION_PRECISION);
		defaults.add(SingleElementModel.LOCATION_COMMENT);
		defaults.add(SingleElementModel.LOCATION_TYPE);
		defaults.add(SingleElementModel.ADDRESSLINE1);
		defaults.add(SingleElementModel.ADDRESSLINE2);
		defaults.add(SingleElementModel.CITY_TOWN);
		defaults.add(SingleElementModel.STATE_PROVINCE_REGION);
		defaults.add(SingleElementModel.POSTCODE);
		defaults.add(SingleElementModel.COUNTRY);
		defaults.add(SingleElementModel.MARKS);
		defaults.add(SingleElementModel.ALTITUDE);
		defaults.add(SingleElementModel.SLOPE_ANGLE);
		defaults.add(SingleElementModel.SLOPE_AZIMUTH);
		defaults.add(SingleElementModel.SOIL_DESCRIPTION);
		defaults.add(SingleElementModel.SOIL_DEPTH);
		defaults.add(SingleElementModel.BEDROCK_DESCRIPTION);
		
		//Collections.sort(defaults);
		ccmodel.addAll(defaults);*/
		
		defaults.addAll(Arrays.asList(SingleElementModel.TABLE_PROPERTIES));

		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
	
		for(WSIUserDefinedField fld : udfdictionary)
		{
			if(fld.getAttachedto().equals(UserExtendableEntity.ELEMENT))
			{
				defaults.add(fld.getLongfieldname());
			}
		}
		
		Collections.sort(defaults, new Comparator<String>() {
		    @Override
		    public int compare(String o1, String  o2) {
		    	
		    	if(o1.equals("Selected")) return -1;
		    	if(o2.equals("Selected")) return 1;
		    	if(o1.equals("Imported")) return -1;
		    	if(o2.equals("Imported")) return 1;

		        return o1.compareTo(o2);
		    }
		});
		
		
		//Collections.sort(defaults);
		

		ccmodel.addAll(defaults);
		
		

		/*
		// Grab preferred fields from preferences, falling back to default if not specified
		ArrayList<String> fields = App.prefs.getArrayListPref(PrefKey.ELEMENT_FIELD_VISIBILITY_ARRAY, defaults);
		
		// Actually populate model with the fields
		// Done in stupid way to maintain preferred or logical/default ordering 
		for(String f: fields)
		{
			if(defaults.contains(f))
			{
				ccmodel.add(f);
			}
		}
		for(String f : defaults)
		{
			if(!fields.contains(f))
			{
				ccmodel.add(f);
			}
		}*/
	}
	
	private void populateSampleDefaults(ColumnListModel ccmodel){
		
		// First populate an array with all the default columns in it
		ArrayList<String> defaults = new ArrayList<String>();		
		/*defaults.add(SingleSampleModel.IMPORTED);
		defaults.add(SingleSampleModel.OBJECT);
		defaults.add(SingleSampleModel.ELEMENT);
		defaults.add(SingleSampleModel.TITLE);
		defaults.add(SingleSampleModel.TYPE);
		defaults.add(SingleSampleModel.BOX);
		defaults.add(SingleSampleModel.COMMENTS);
		defaults.add(SingleSampleModel.DESCRIPTION);
		defaults.add(SingleSampleModel.FILES);
		defaults.add(SingleSampleModel.SAMPLING_DATE);
		defaults.add(SingleSampleModel.POSITION);
		defaults.add(SingleSampleModel.STATE);
		defaults.add(SingleSampleModel.KNOTS);*/

		
		//Collections.sort(defaults);
		//ccmodel.addAll(defaults);
		defaults.addAll(Arrays.asList(SingleSampleModel.TABLE_PROPERTIES));

		MVCArrayList<WSIUserDefinedField> udfdictionary = App.dictionary.getMutableDictionary("userDefinedFieldDictionary");
	
		for(WSIUserDefinedField fld : udfdictionary)
		{
			if(fld.getAttachedto().equals(UserExtendableEntity.SAMPLE))
			{
				defaults.add(fld.getLongfieldname());
			}
		}
		Collections.sort(defaults, new Comparator<String>() {
		    @Override
		    public int compare(String o1, String  o2) {
		    	
		    	if(o1.equals("Selected")) return -1;
		    	if(o2.equals("Selected")) return 1;
		    	if(o1.equals("Imported")) return -1;
		    	if(o2.equals("Imported")) return 1;
		    	
		    	return o1.compareTo(o2);
		    }
		});

		ccmodel.addAll(defaults);
		
		/*
		// Grab preferred fields from preferences, falling back to default if not specified
		ArrayList<String> fields = App.prefs.getArrayListPref(PrefKey.SAMPLE_FIELD_VISIBILITY_ARRAY, defaults);
		fields = new ArrayList<String>(new HashSet<String>(fields));
		
		// Actually populate model with the fields
		// Done in stupid way to maintain preferred or logical/default ordering 
		for(String f: fields)
		{
			if(defaults.contains(f))
			{
				ccmodel.add(f);
			}
		}
		for(String f : defaults)
		{
			if(!fields.contains(f))
			{
				ccmodel.add(f);
			}
		}*/
	}
	
	private void populateRadiusDefaults(ColumnListModel ccmodel){
		
	}
}
