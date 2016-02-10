package org.tellervo.desktop.odk;

import java.io.Serializable;

public class ODKSerializedForm implements Serializable {


	private static final long serialVersionUID = 3516194818372083752L;
	private ODKTreeModel treeModel;
	private String formTitle;
	
	
	public ODKSerializedForm(ODKTreeModel model, String formTitle)
	{
		this.setTreeModel(model);
		this.setFormTitle(formTitle);
	}

	public ODKTreeModel getTreeModel() {
		return treeModel;
	}

	private void setTreeModel(ODKTreeModel treeModel) {
		this.treeModel = treeModel;
	}

	public String getFormTitle() {
		return formTitle;
	}

	private void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

}
