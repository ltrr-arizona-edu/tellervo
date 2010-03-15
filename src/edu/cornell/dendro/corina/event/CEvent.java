/**
 * Created at 2:22:05 AM, Mar 12, 2010
 */
package edu.cornell.dendro.corina.event;

/**
 * Simple event, stands for Corina Event (Event is already in Java, so we don't
 * want any confusion).
 * @author daniel
 */
public class CEvent {
	public final String key;
	
	public CEvent(final String argKey) {
		key = argKey;
	}

	@Override
	public String toString() {
		return super.toString() + "-" + key;
	}
}
