/**
 * Created at Aug 23, 2010, 3:36:15 AM
 */
package edu.cornell.dendro.corina.model.bulkImport;

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
		registerProperty(RADIUS_WITH_SAMPLE, PropertyType.READ_WRITE, true);
		registerProperty(IMPORTED_LIST, PropertyType.FINAL, new MVCArrayList<TridasSample>());
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
		getTableModel().removeSelected();
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
