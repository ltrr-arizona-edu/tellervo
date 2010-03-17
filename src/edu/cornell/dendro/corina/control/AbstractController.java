/**
 * 3:21:37 AM, Mar 17, 2010
 */
package edu.cornell.dendro.corina.control;

import edu.cornell.dendro.corina.event.IEventListener;
import edu.cornell.dendro.corina.event.MVCEventCenter;

/**
 *
 * @author daniel
 */
public abstract class AbstractController implements IEventListener{

	protected void registerEventKey(String argKey){
		MVCEventCenter.addEventListener( argKey, this);
	}
}
