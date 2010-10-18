package edu.cornell.dendro.corina.wsi.util;

import org.tridas.schema.NormalTridasUnit;

import edu.cornell.dendro.corina.core.App;

public class UnitConverter {

	
	public UnitConverter(){

		NormalTridasUnit displayUnits = NormalTridasUnit.valueOf(App.prefs.getPref("corina.displayunits", NormalTridasUnit.HUNDREDTH_MM.value().toString()));

	}
}
