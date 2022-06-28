/**
 * Created at Aug 22, 2010, 1:14:55 PM
 */
package org.tellervo.desktop.components.table;

import java.util.ArrayList;
import java.util.Comparator;

import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasShape;

/**
 * @author Daniel
 *
 */
public class TridasShapeComboBox extends DynamicJComboBox<TridasShape>{

	private static final long serialVersionUID = 1L;
	private ArrayList<TridasShape> data = new ArrayList<TridasShape>();
	
	public TridasShapeComboBox(){
		super(null, new Comparator<TridasShape>() {
			@Override
			public int compare(TridasShape argO1, TridasShape argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				return argO1.getNormalTridas().toString().compareTo(argO2.getNormalTridas().toString());
			}
			
		});
		
		for(NormalTridasShape normal : NormalTridasShape.values()){
			TridasShape shape = new TridasShape();
			shape.setNormalTridas(normal);
			data.add(shape);
		}
		setRenderer(new TridasShapeRenderer());
		refreshData();
	}
	
	/**
	 * @see org.tellervo.desktop.components.table.DynamicJComboBox#getData()
	 */
	@Override
	public ArrayList<TridasShape> getData() {
		return data;
	}
}
