package corina.ui;

import corina.util.Platform;

import junit.framework.TestCase;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing I18n
    //
    public void testGetText() {
	assertEquals(I18n.getText("edit"), "Edit");
    }

    public void testGetKeyStroke() {
	assertEquals(I18n.getKeyStroke("file"), null);
	assertEquals(I18n.getKeyStroke("save"), (Platform.isMac ? "meta S" : "control S"));
    }

    public void testGetMnemonic() {
	assertEquals(I18n.getMnemonic("edit").charValue(), 'E');
    }
}
