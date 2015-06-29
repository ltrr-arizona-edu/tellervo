/**
 * Created at Aug 22, 2010, 2:56:54 AM
 */
package org.tellervo.desktop.components.table;

import java.util.ArrayList;
import java.util.Comparator;

import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasVocabulary.LocationType;

/**
 * @author Daniel
 *
 */
@SuppressWarnings("unchecked")
public class LocationTypeComboBox extends DynamicJComboBox{
	private static final long serialVersionUID = 1L;
	
	private final ArrayList data = new ArrayList();
	
	public LocationTypeComboBox(){
		super(null, new Comparator(){
			public int compare(Object argO1, Object argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				String s1 = ((NormalTridasLocationType)argO1).toString();
				String s2 = ((NormalTridasLocationType)argO2).toString();
				
				return s1.compareTo(s2);
			}
		});
		
		for(NormalTridasLocationType type : NormalTridasLocationType.values())
		{
			data.add(type);
		}
		

		setRenderer(new LocationTypeRenderer());
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