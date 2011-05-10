package edu.cornell.dendro.corina.admin.control;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.admin.model.UserGroupAdminModel;
//import com.dmurph.mvc.tracking.ITrackable;

public class DeleteUserEvent extends MVCEvent{ // implements ITrackable {
	private static final long serialVersionUID = 1L;
	public final String usrid;
	public final UserGroupAdminModel model;

	public DeleteUserEvent(String argUsrid, UserGroupAdminModel argModel) {
		super(UserGroupAdminController.DELETE_USER);
		usrid = argUsrid;
		model = argModel;
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
