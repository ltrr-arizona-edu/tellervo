package edu.cornell.dendro.corina.metadata;

import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.StringUtils;

/**
A variable record, which holds a (variable => description,
values, readonly) mapping.

<p>A metadata field is defined by:</p>

<dl>
<dt>variable (like "dating")
<dd>the key to store this type of data in the metadata hash table

<dt>description (like "Dating")
<dd>a short human-readable description for displaying
in forms and table headers

<dt>values (like ["R","A"])
<dd>WRITEME ("suggested values, if choice, else null")

<dt>readonly (like <i>false</i>)
<dd>WRITEME ("is this field read-only?")

<dt>lines (like <i>1</i>)
<dd>WRITEME ("number of lines to display")
</dl>
*/
public class MetadataField {	
	public MetadataField(String variable, boolean editable) {
		this.variable = variable;
		// TODO: this'll get refactored into Field(...)! -- why?
		this.readonly = !editable;

		// set description, from |m|
		this.fieldDescription = I18n.getText("meta." + variable);
	}

	public MetadataField(String variable, boolean editable, int lines) {
		this(variable, editable);
		this.lines = lines;
		this.values = null;
	}

	public MetadataField(String variable, boolean editable, String values) {
		this(variable, editable);
		this.lines = 1;

		this.values = StringUtils.splitBy(values, ',');
		this.hasSetValues = true;
	}
	
	private String variable = "";

	public String getVariable() {
		return variable;
	}

	private String fieldDescription = "";

	public String getFieldDescription() {
		return fieldDescription;
	}

	// document: null means it's free-form! (do i need a 'type', too?)
	// TYPES: string, one-of, string (multiple lines), label (ro!).
	// subclass?
	// -- StringField
	// -- ChoiceField (getChoices(), etc.)
	// -- TextField (just StringField with lines != 1?)
	// -- LabelField (just like StringField, but users can't edit)
	private String values[] = null;

	// TODO: give this a nice public interface
	// used by: DataComponent, MetadataPanel, ElementsPanel
	public boolean isValidValue(String value) {
		for (int i = 0; i < values.length; i++)
			if (values[i].equals(value))
				return true;
		return false;
	}

	private boolean readonly = false;

	public boolean isReadOnly() {
		return readonly;
	}

	private int lines = 1;

	public int getLines() {
		return lines;
	}

	// TO ADD: "columns". ("sapwood count" doesn't need to be as
	// wide as "title" or "species")

	// TO ADD: numbers-only? (types)

	// store type information?

	// awt/swing sometimes lets you use objects directly,
	// and use the toString() value as their label.
	// so let's provide that.
	@Override
	public String toString() {
		return fieldDescription;
	}

	// Is our field just a list of selectable values?
	protected boolean hasSetValues = false;

	/**
	 * Method to determine if this field is a list (ie, combobox)
	 * 
	 * @return true if this field contains a list of values, 
	 * false if it is free-editable (ie, a text field)
	 */
	public boolean isList() {
		return hasSetValues;
	}

	// translate a machine value to a human readable value.
	// If it's not a machine value, return the passed info
	// If we can't parse, return the passed info.
	public String getReadableValue(String machineValue) {
		if (!hasSetValues)
			return machineValue;

		String description = I18n.getText("meta." + getVariable() + "."
				+ machineValue);
		if (description == null)
			return machineValue;

		return description;
	}

	public int getListSize() {
		return values.length;
	}
	
	public String getListItemValue(int index) {
		return values[index];
	}
	
	public String getListItemDescription(int index) {
		String desc = I18n.getText("meta." + variable + "." + values[index]);
		
		if(desc == null)
			return "[" + values[index] + "]";
		
		return desc;
	}
	
	/**
	 * Some methods need an array of values, I guess.
	 * @return an array of machine values
	 */
	public String[] getValuesArray() {
		String[] s = new String[getListSize()];
		for(int i = 0; i < getListSize(); i++)
			s[i] = getListItemValue(i);
		
		return s;
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	@Deprecated
	public String[] getValues() {
		return values;
	}
}
