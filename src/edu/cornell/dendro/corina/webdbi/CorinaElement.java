package edu.cornell.dendro.corina.webdbi;

/**
 * Helper class that sets default namespace for Corina elements
 */

import org.jdom.Element;

public class CorinaElement extends Element {
    public CorinaElement(final String name) {
        super(name, CorinaXML.CORINA_NS);
    }
}
