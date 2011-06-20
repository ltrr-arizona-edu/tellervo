package edu.cornell.dendro.corina.admin.control;

import com.dmurph.mvc.MVCEvent;

import javax.swing.JTable;
import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
//import com.dmurph.mvc.tracking.ITrackable;

public class ToggleDisabledAccountsEvent extends MVCEvent{ // implements ITrackable {
	private static final long serialVersionUID = 1L;
	public final UserGroupAdminModel model;
	public final boolean show;
	public final JTable table;

	public ToggleDisabledAccountsEvent(boolean argShow, UserGroupAdminModel argModel, JTable argTable) {
		super(UserGroupAdminController.TOGGLE_DISABLED_ACCOUNTS);
		model = argModel;
		show = argShow;
		table = argTable;
	}

//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingAction()
//	 */
//	@Override
//	public String getTrackingAction() {
//		return "Show";
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingCategory()
//	 */
//	@Override
//	public String getTrackingCategory() {
//		return "Bulk Import";
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingLabel()
//	 */
//	@Override
//	public String getTrackingLabel() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/**
//	 * @see com.dmurph.mvc.tracking.ITrackable#getTrackingValue()
//	 */
//	@Override
//	public Integer getTrackingValue() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
