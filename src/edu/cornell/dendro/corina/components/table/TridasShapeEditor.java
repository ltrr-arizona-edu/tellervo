/**
 * Created at Aug 22, 2010, 1:14:55 PM
 */
package edu.cornell.dendro.corina.components.table;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasShape;

/**
 * @author Daniel
 *
 */
public class TridasShapeEditor extends AbstractComboBoxEditor{

	private final HashMap<String, NormalTridasShape> map = new HashMap<String, NormalTridasShape>();
	/**
	 * @see edu.cornell.dendro.corina.components.table.AbstractComboBoxEditor#getComboBoxOptions()
	 */
	@Override
	protected String[] getComboBoxOptions() {
		String[] s = new String[NormalTridasShape.values().length];
		map.clear();
		for(int i=0; i<s.length; i++){
			String rep = NormalTridasShape.values()[i].toString().replaceAll("___", " ");
			rep = rep.toLowerCase();
			rep = StringUtils.capitaliseAllWords(rep);
			map.put(rep, NormalTridasShape.values()[i]);
			s[i] = rep;
		}
		return s;
	}

	/**
	 * @see edu.cornell.dendro.corina.components.table.AbstractComboBoxEditor#getValueFromString(java.lang.String)
	 */
	@Override
	protected Object getValueFromString(String argString) {
		TridasShape shape = new TridasShape();
		shape.setNormalTridas(map.get(argString));
		return shape;
	}

}
