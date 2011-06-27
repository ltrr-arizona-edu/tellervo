/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.gui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

// http://forum.java.sun.com/thread.jsp?forum=60&thread=254513
public class AccessControlContextEventQueue extends EventQueue {
  @SuppressWarnings("unchecked")
private final class PrivilegedActionEvent implements PrivilegedAction {
    private AWTEvent event;
    public Object run() {
      AccessControlContextEventQueue.this.superDispatchEvent(event);
      return null;
    }
  }
  private final AccessControlContext aContext = AccessController.getContext();
  @SuppressWarnings("unchecked")
private final ThreadLocal threadlocal = new ThreadLocal() {
    @Override
	protected synchronized Object initialValue() {
      return new PrivilegedActionEvent();
    }
  };
  private void superDispatchEvent(final AWTEvent event) {
    super.dispatchEvent(event);
  }
  @SuppressWarnings("unchecked")
@Override
protected void dispatchEvent(final AWTEvent event){
    PrivilegedActionEvent pae = (PrivilegedActionEvent) threadlocal.get();
    pae.event = event;
    AccessController.doPrivileged(pae, aContext);
  }
}
