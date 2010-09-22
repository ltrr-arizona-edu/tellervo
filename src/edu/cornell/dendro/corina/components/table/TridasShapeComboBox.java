/**
 * Created at Aug 22, 2010, 1:14:55 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasShape;

import com.dmurph.mvc.model.MVCArrayList;

/**
 * @author Daniel
 *
 */
public class TridasShapeComboBox extends DynamicJComboBox<TridasShape>{

	private ArrayList<TridasShape> data = new ArrayList<TridasShape>();
	
	public TridasShapeComboBox(){
		super(null, new IDynamicJComboBoxInterpreter<TridasShape>() {
			@Override
			public String getStringValue(TridasShape argComponent) {
				String rep = argComponent.getNormalTridas().toString().replaceAll("___", " ");
				rep = rep.toLowerCase();
				rep = StringUtils.capitaliseAllWords(rep);
				return rep;
			}
		});
		
		for(NormalTridasShape normal : NormalTridasShape.values()){
			TridasShape shape = new TridasShape();
			shape.setNormalTridas(normal);
			data.add(shape);
		}
		refreshData();
	}
	
	/**
	 * @see edu.cornell.dendro.corina.components.table.DynamicJComboBox#getData()
	 */
	@Override
	public ArrayList<TridasShape> getData() {
		return data;
	}
}
