/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.text.NumberFormat;
import java.util.List;

import org.tridas.schema.TridasLocationGeometry;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * @author Lucas Madar
 *
 */
public class LocationGeometryRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.l2fprod.common.swing.renderer.DefaultCellRenderer#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(Object value) {
		TridasLocationGeometry geo = (TridasLocationGeometry) value;
		
		// only handle points, for now... 
		if(geo != null && geo.isSetPoint()) {
			// is the point useless? bail here
			if(!geo.getPoint().isSetPos() || !geo.getPoint().getPos().isSetValues())
				return null;
			
			List<Double> values = geo.getPoint().getPos().getValues();
			
			// invalid size, expecting long, lat pair
			if(values.size() != 2)
				return null;
			
			NumberFormat format = NumberFormat.getInstance();
			
			// same as in LocationGeometryUI!
			format.setMinimumFractionDigits(3);
			format.setMaximumFractionDigits(5);
			
			// lat long
			return format.format(values.get(1)) + " " + format.format(values.get(0));
		}
		
		return super.convertToString(value);
	}

}
