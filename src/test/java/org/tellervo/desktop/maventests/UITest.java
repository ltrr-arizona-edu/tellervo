/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.maventests;

import javax.swing.KeyStroke;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.I18n;

import junit.framework.TestCase;

/*
  yup, this looks weird.  it's fairly generic stuff, so i can change
  the translation files (or build in a different locale) without my
  tests failing.
 */

public class UITest extends TestCase {
  public UITest(String name) {
    super(name);
  }

  @Override
protected void setUp() throws Exception {
    super.setUp();
    if (!App.isInitialized()) App.init(null, null);
  }

    //
    // testing I18n
    //
    public void testGetText() {
	String edit = I18n.getText("menus.edit");

	// make sure it's a word
	assertTrue(edit.length() > 0);
	assertTrue(Character.isLetter(edit.charAt(0)));
	assertTrue(Character.isUpperCase(edit.charAt(0)));

	// make sure there's no []&
	assertEquals(-1, edit.indexOf('['));
	assertEquals(-1, edit.indexOf(']'));
	assertEquals(-1, edit.indexOf('&'));
    }

    public void testGetKeyStroke() {
	// "file" should not have a keystroke
	assertEquals(I18n.getKeyStroke("menus.file"), null);

	// "save" should, and it should be "<accel> <key>"
	KeyStroke save = I18n.getKeyStroke("menus.edit.copy");
	assertTrue(save != null);
	assertTrue(save.getModifiers() != 0);
    }

    public void testGetMnemonic() {
	// edit should have a mnemonic, and it should be upper-case
	Integer e = I18n.getMnemonic("menus.edit");
	assertTrue(e != null);
	assertTrue(e >= 'A' && e <= 'Z');
    }
}
