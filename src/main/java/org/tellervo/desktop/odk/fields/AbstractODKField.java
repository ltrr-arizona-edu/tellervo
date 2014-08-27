package org.tellervo.desktop.odk.fields;


public abstract class AbstractODKField implements ODKFieldInterface {

	public String toString()
	{
		return this.getFieldName();
	}

}
