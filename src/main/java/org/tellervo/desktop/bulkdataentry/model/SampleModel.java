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
/**
 * Created at Aug 23, 2010, 3:36:15 AM
 */
package org.tellervo.desktop.bulkdataentry.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.tellervo.desktop.bulkdataentry.control.BulkImportController;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * @author Daniel
 *
 */
@SuppressWarnings("unchecked")
public class SampleModel extends HashModel implements IBulkImportSectionModel{
	private static final long serialVersionUID = 1L;
	
	public static final String RADIUS_WITH_SAMPLE = "Radius with sample";
	
	public SampleModel(){
		registerProperty(ROWS, PropertyType.FINAL, new MVCArrayList<SingleSampleModel>());
		registerProperty(COLUMN_MODEL, PropertyType.FINAL, new ColumnChooserModel());
		registerProperty(TABLE_MODEL, PropertyType.FINAL, new SampleTableModel(this));
		registerProperty(RADIUS_WITH_SAMPLE, PropertyType.READ_WRITE, false);
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasSample>());
		getColumnModel().populatePossibleColumns(getModelTableProperties());
	}
	
	public MVCArrayList<SingleSampleModel> getRows(){
		return (MVCArrayList<SingleSampleModel>) getProperty(ROWS);
	}
	
	public ColumnChooserModel getColumnModel(){
		return (ColumnChooserModel) getProperty(COLUMN_MODEL);
	}
	
	public SampleTableModel getTableModel(){
		return (SampleTableModel) getProperty(TABLE_MODEL);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getImportedList()
	 */
	@Override
	public MVCArrayList<TridasSample> getImportedList() {
		return (MVCArrayList<TridasSample>) getProperty(IMPORTED_LIST);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getImportedListStrings()
	 */
	@Override
	public String[] getImportedListStrings() {
		MVCArrayList<TridasSample> imported = getImportedList();
		String[] s = new String[imported.size()];
		for(int i=0; i<s.length; i++){
			s[i] = imported.get(i).getTitle();
		}
		return s;
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getImportedDynamicComboBoxKey()
	 */
	@Override
	public String getImportedDynamicComboBoxKey() {
		return BulkImportController.SET_DYNAMIC_COMBO_BOX_SAMPLES;
	}
	
	public boolean isRadiusWithSample(){
		return (Boolean) getProperty(RADIUS_WITH_SAMPLE);
	}
	
	public void setRadiusWithSample(boolean argRadiusWithSample){
		setProperty(RADIUS_WITH_SAMPLE, argRadiusWithSample);
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#removeSelected()
	 */
	@Override
	public void removeSelected() {
		ArrayList<IBulkImportSingleRowModel> removed = new ArrayList<IBulkImportSingleRowModel>();
		getTableModel().removeSelected(removed);
		
		Iterator<IBulkImportSingleRowModel> it = removed.iterator();
		
		while(it.hasNext()){
			if(it.next().getImported() == null){
				it.remove();
			}
		}
		if(removed.size() == 0){
			return;
		}
		MVCArrayList<TridasSample> imported = getImportedList();
		for(int i=0; i< imported.size(); i++){
			TridasSample o = imported.get(i);
			for(IBulkImportSingleRowModel som : removed){
				if(o.getIdentifier().equals(som.getImported())){
					imported.remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public IBulkImportSingleRowModel createRowInstance() {
		SingleSampleModel model = new SingleSampleModel();
		if(isRadiusWithSample()){
			model.setRadiusModel(new SingleRadiusModel());
			model.getRadiusModel().setProperty(SingleRadiusModel.TITLE, "A");
		}
		return model;
	}

	/**
	 * @see org.tellervo.desktop.bulkdataentry.model.IBulkImportSectionModel#getModelTableProperties()
	 */
	@Override
	public String[] getModelTableProperties() {
		if(isRadiusWithSample()){	
			// modified a bit so imported is at the end
			String[] ret = new String[SingleSampleModel.TABLE_PROPERTIES.length - 1 + SingleRadiusModel.PROPERTIES.length ];
			System.arraycopy(SingleSampleModel.TABLE_PROPERTIES, 0, ret, 0, SingleSampleModel.TABLE_PROPERTIES.length-1);
			System.arraycopy(SingleRadiusModel.PROPERTIES, 0, ret, SingleSampleModel.TABLE_PROPERTIES.length-1, SingleRadiusModel.PROPERTIES.length);
			return ret;
		}
		return SingleSampleModel.TABLE_PROPERTIES;
	}
}
