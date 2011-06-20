package edu.cornell.dendro.corina.remarks;

import javax.swing.Icon;

import org.tridas.schema.NormalTridasRemark;
import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * A wrapper for a Tridas Remark
 * 
 * @author Lucas Madar
 */

public class TridasReadingRemark extends AbstractRemark {
	private final TridasRemark remark;
	
	public TridasReadingRemark(NormalTridasRemark value) {
		remark = new TridasRemark();

		// set the normal tridas value
		remark.setNormalTridas(value);
		
		// obnoxious, jaxb...
		remark.setValue("");
	}

	@Override
	public TridasRemark asTridasRemark() {
		return remark;
	}

	public String getDisplayName() {
		return remark.getNormalTridas().value();
	}

	public Icon getIcon() {
		String iconFn = Remarks.getTridasRemarkIcons().get(remark.getNormalTridas());
		
		return (iconFn == null) ? null : Builder.getIcon(iconFn, 16);
	}

}
