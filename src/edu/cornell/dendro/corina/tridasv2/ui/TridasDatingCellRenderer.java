package edu.cornell.dendro.corina.tridasv2.ui;

import org.tridas.schema.TridasDating;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

public class TridasDatingCellRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertToString(Object value) {
		if(value instanceof TridasDating) {
			TridasDating dating = (TridasDating) value;
			
			return dating.isSetType() ? dating.getType().value() : "Not present";
		}
		return super.convertToString(value);
	}
}
