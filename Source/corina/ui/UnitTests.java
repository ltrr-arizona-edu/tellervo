package corina.ui;

import junit.framework.TestCase;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing parsers
    //
    public void testGetText() {
	String key;

	key = "&blah";
	assertEquals(Builder.getText(key), "blah");

	key = "&blah [junk here]";
	assertEquals(Builder.getText(key), "blah");
    }

    public void testGetKeyStroke() {
	String key;

	key = "&blah";
	assertEquals(Builder.getKeyStroke(key), null);

	key = "&blah [junk here]";
	assertEquals(Builder.getKeyStroke(key), "junk here");
    }

    public void testGetMnemonic() {
	String key;

	key = "&blah";
	assertEquals(Builder.getMnemonic(key).charValue(), 'B');

	key = "blah [junk here]";
	assertEquals(Builder.getMnemonic(key), null);
    }
}
