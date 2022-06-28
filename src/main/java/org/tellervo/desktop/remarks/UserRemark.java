package org.tellervo.desktop.remarks;

import javax.swing.Icon;

import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasRemark;

public class UserRemark extends AbstractRemark {
	
	private final TridasRemark remark;
	
	public UserRemark(TridasRemark remark)
	{
		this.remark = remark;
	}
	
	@Override
	public String getDisplayName() {
		return remark.getValue();
	}

	@Override
	public Icon getIcon() {
		return Builder.getIcon("user.png", 16);
	}

	@Override
	public TridasRemark asTridasRemark() {
		return remark;
	}

}
