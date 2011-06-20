package edu.cornell.dendro.corina.remarks;

import javax.swing.Icon;

import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.ui.Builder;

public class CorinaReadingRemark extends AbstractRemark {
	private final TridasRemark remark;
	
	public CorinaReadingRemark(TridasRemark corinaRemark) {	
		remark = (TridasRemark) corinaRemark.clone();
		corinaRemark.copyTo(remark);
		remark.setValue("");
	}
	
	@Override
	public TridasRemark asTridasRemark() {
		return remark;
	}

	public String getDisplayName() {
		return remark.getNormal();
	}
	
	public Icon getIcon() {
		
		String iconFn;
		if(Remarks.getCorinaRemarkIcons().get(remark.getNormal())!=null)
		{
			iconFn = Remarks.getCorinaRemarkIcons().get(remark.getNormal());	
		}
		else
		{
			iconFn = "note.png";
		}
		
		
		return (iconFn == null) ? null : Builder.getIcon(iconFn, 16);
	}

}
