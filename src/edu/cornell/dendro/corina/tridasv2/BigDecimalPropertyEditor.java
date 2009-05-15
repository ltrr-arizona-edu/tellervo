package edu.cornell.dendro.corina.tridasv2;

import java.math.BigDecimal;

import com.l2fprod.common.beans.editor.NumberPropertyEditor;

public class BigDecimalPropertyEditor extends NumberPropertyEditor {

	public BigDecimalPropertyEditor() {
		super(BigDecimal.class);
	}

}
