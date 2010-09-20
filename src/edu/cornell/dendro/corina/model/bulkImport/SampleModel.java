/**
 * Created at Aug 23, 2010, 3:36:15 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

import java.util.ArrayList;
import java.util.Iterator;

import org.tridas.schema.TridasSample;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

import edu.cornell.dendro.corina.control.bulkImport.BulkImportController;

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
		registerProperty(RADIUS_WITH_SAMPLE, PropertyType.READ_WRITE, true);
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasSample>());
		getColumnModel().poplutePossibleColumns(getModelTableProperties());
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
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#getImportedList()
	 */
	@Override
	public MVCArrayList<TridasSample> getImportedList() {
		return (MVCArrayList<TridasSample>) getProperty(IMPORTED_LIST);
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#getImportedListStrings()
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
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#getImportedDynamicComboBoxKey()
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
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#removeSelected()
	 */
	@Override
	public void removeSelected() {
		ArrayList<SingleSampleModel> removed = new ArrayList<SingleSampleModel>();
		getTableModel().removeSelected(removed);
		
		Iterator<SingleSampleModel> it = removed.iterator();
		
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
			for(SingleSampleModel som : removed){
				if(o.getIdentifier().equals(som.getImported())){
					imported.remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#createRowInstance()
	 */
	@Override
	public ISingleRowModel createRowInstance() {
		SingleSampleModel model = new SingleSampleModel();
		if(isRadiusWithSample()){
			model.setRadiusModel(new SingleRadiusModel());
			model.getRadiusModel().setProperty(SingleRadiusModel.TITLE, "A");
		}
		return model;
	}

	/**
	 * @see edu.cornell.dendro.corina.model.bulkImport.IBulkImportSectionModel#getModelTableProperties()
	 */
	@Override
	public String[] getModelTableProperties() {
		if(isRadiusWithSample()){
			String[] ret = new String[SingleSampleModel.TABLE_PROPERTIES.length + SingleRadiusModel.PROPERTIES.length];
			System.arraycopy(SingleSampleModel.TABLE_PROPERTIES, 0, ret, 0, SingleSampleModel.TABLE_PROPERTIES.length);
			System.arraycopy(SingleRadiusModel.PROPERTIES, 0, ret, SingleSampleModel.TABLE_PROPERTIES.length, SingleRadiusModel.PROPERTIES.length);
			return ret;
		}
		return SingleSampleModel.TABLE_PROPERTIES;
	}
}
