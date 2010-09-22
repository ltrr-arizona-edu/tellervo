/**
 * Created at Aug 22, 2010, 2:56:54 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;

/**
 * @author Daniel
 *
 */
public class TridasUnitComboBox extends DynamicJComboBox{
	private static final long serialVersionUID = 1L;
	
	private final ArrayList data = new ArrayList<TridasUnit>();
	
	@SuppressWarnings("unchecked")
	public TridasUnitComboBox(){
		super(null, new IDynamicJComboBoxInterpreter() {
			@Override
			public String getStringValue(Object argComponent) {
				if(argComponent instanceof TridasUnit){
					String rep = ((TridasUnit) argComponent).getNormalTridas().toString().replaceAll("_", " ");
					rep = rep.toLowerCase();
					rep = StringUtils.capitaliseAllWords(rep);
					return rep;
				}
				else if(argComponent instanceof TridasUnitless){
					return "Unitless";
				}
				return null;
			}
		});
		
		for(NormalTridasUnit normal : NormalTridasUnit.values()){
			TridasUnit unit = new TridasUnit();
			unit.setNormalTridas(normal);
			data.add(unit);
		}
		data.add(new TridasUnitless());
		refreshData();
	}
	
	/**
	 * @see edu.cornell.dendro.corina.components.table.DynamicJComboBox#getData()
	 */
	@Override
	public ArrayList<?> getData() {
		return data;
	}
}