package org.tellervo.desktop.odk;

import java.io.Serializable;

public class ODKSerializedForm implements Serializable {


	private static final long serialVersionUID = 3516194818372083752L;
	private ODKFieldListModel listModel;
	private String formTitle;
	
	
	public ODKSerializedForm(ODKFieldListModel listModel, String formTitle)
	{
		this.setListModel(listModel);
		this.setFormTitle(formTitle);
	}

	public ODKFieldListModel getListModel() {
		return listModel;
	}

	private void setListModel(ODKFieldListModel listModel) {
		this.listModel = listModel;
	}

	public String getFormTitle() {
		return formTitle;
	}

	private void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

}
