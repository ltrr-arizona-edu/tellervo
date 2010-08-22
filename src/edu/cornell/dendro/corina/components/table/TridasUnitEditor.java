/**
 * Created at Aug 22, 2010, 2:56:54 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasUnitless;

/**
 * @author Daniel
 *
 */
public class TridasUnitEditor extends AbstractComboBoxEditor{

	private final HashMap<String, NormalTridasUnit> map = new HashMap<String, NormalTridasUnit>();
	/**
	 * @see edu.cornell.dendro.corina.components.table.AbstractComboBoxEditor#getComboBoxOptions()
	 */
	@Override
	protected String[] getComboBoxOptions() {
		String[] s = new String[NormalTridasUnit.values().length+1];
		map.clear();
		for(int i=0; i<s.length-1; i++){
			String rep = NormalTridasUnit.values()[i].toString().replaceAll("_", " ");
			rep = rep.toLowerCase();
			rep = StringUtils.capitaliseAllWords(rep);
			map.put(rep, NormalTridasUnit.values()[i]);
			s[i] = rep;
		}
		s[s.length-1] = "Unitless";
		
		return s;
	}

	/**
	 * @see edu.cornell.dendro.corina.components.table.AbstractComboBoxEditor#getValueFromString(java.lang.String)
	 */
	@Override
	protected Object getValueFromString(String argString) {
		if(argString.equals("Unitless")){
			return new TridasUnitless();
		}
		TridasUnit unit = new TridasUnit();
		unit.setNormalTridas(map.get(argString));
		return unit;
	}
	
}