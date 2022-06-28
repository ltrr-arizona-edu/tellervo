/**
 * Created at Aug 22, 2010, 2:56:54 AM
 */
package org.tellervo.desktop.components.table;

import java.util.ArrayList;
import java.util.Comparator;

import org.tellervo.schema.CurationStatus;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.TridasVocabulary.LocationType;

/**
 * @author Daniel
 *
 */
@SuppressWarnings("unchecked")
public class CurationStatusComboBox extends DynamicJComboBox{
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<CurationStatus> data = new ArrayList<CurationStatus>();
	
	public CurationStatusComboBox(){
		super(null, new Comparator<CurationStatus>(){
			public int compare(CurationStatus argO1, CurationStatus argO2) {
				if(argO1 == null){
					return -1;
				}
				if(argO2 == null){
					return 1;
				}
				String s1 = ((CurationStatus)argO1).toString();
				String s2 = ((CurationStatus)argO2).toString();
				
				return s1.compareTo(s2);
			}
		});
		
		
		
		for(CurationStatus type : CurationStatus.values())
		{
			data.add(type);
		}
		

		setRenderer(new CurationStatusRenderer());
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