package edu.cornell.dendro.corina.ui;

import javax.swing.KeyStroke;

import junit.framework.TestCase;
import edu.cornell.dendro.corina.core.App;

/*
  yup, this looks weird.  it's fairly generic stuff, so i can change
  the translation files (or build in a different locale) without my
  tests failing.
 */

public class UnitTests extends TestCase {
  public UnitTests(String name) {
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
	String edit = I18n.getText("edit");

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
	assertEquals(I18n.getKeyStroke("file"), null);

	// "save" should, and it should be "<accel> <key>"
	KeyStroke save = I18n.getKeyStroke("save");
	assertTrue(save != null);
	assertTrue(save.getModifiers() != 0);
    }

    public void testGetMnemonic() {
	// edit should have a mnemonic, and it should be upper-case
	Integer e = I18n.getMnemonic("edit");
	assertTrue(e != null);
	assertTrue(e >= 'A' && e <= 'Z');
    }
}
