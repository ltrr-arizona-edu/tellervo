package org.tellervo.desktop.gui.dbbrowse;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.SampleType;

import junit.framework.TestCase;

public class ElementListTableModelTest extends TestCase {

	public void testSortableColumnClassesMatchReturnedValueTypes() {
		App.prefs = new Prefs() {
			@Override
			public String getPref(PrefKey key, String defaultValue) {
				return defaultValue;
			}
		};
		ElementListTableModel model = new ElementListTableModel();

		assertEquals(SampleType.class, model.getColumnClass(3));
		assertEquals(String.class, model.getColumnClass(7));
		assertEquals(String.class, model.getColumnClass(8));
	}
}
