/**
 * Created at Aug 22, 2010, 2:56:54 AM
 */
package org.tellervo.desktop.components.table;

import java.util.ArrayList;
import java.util.Comparator;

import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;

/**
 * @author Daniel
 *
 */
@SuppressWarnings("unchecked")
public class TridasUnitComboBox extends DynamicJComboBox{
	private static final long serialVersionUID = 1L;
	
	private final ArrayList data = new ArrayList();
	
	public TridasUnitComboBox(){
		super(null, new Comparator(){
			public int compare(Object argO1, Object argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				String s1,s2;
				if(argO1 instanceof TridasUnit){
					s1 = ((TridasUnit) argO1).getNormalTridas().toString();
				}else{
					s1 = "Unitless";
				}
				
				if(argO2 instanceof TridasUnit){
					s2 = ((TridasUnit) argO2).getNormalTridas().toString();
				}else{
					s2 = "Unitless";
				}
				return s1.compareTo(s2);
			}
		});
		
		for(NormalTridasUnit normal : NormalTridasUnit.values()){
			TridasUnit unit = new TridasUnit();
			unit.setNormalTridas(normal);
			data.add(unit);
		}
		data.add(new TridasUnitless());
		setRenderer(new TridasUnitRenderer());
		refreshData();
	}
	
	/**
	 * @see org.tellervo.desktop.components.table.DynamicJComboBox#getData()
	 */
	@Override
	public ArrayList<?> getData() {
		return data;
	}
}