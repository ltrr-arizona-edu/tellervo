package edu.cornell.dendro.corina.gui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

// http://forum.java.sun.com/thread.jsp?forum=60&thread=254513
public class AccessControlContextEventQueue extends EventQueue {
  private final class PrivilegedActionEvent implements PrivilegedAction {
    private AWTEvent event;
    public Object run() {
      AccessControlContextEventQueue.this.superDispatchEvent(event);
      return null;
    }
  }
  private final AccessControlContext aContext = AccessController.getContext();
  private final ThreadLocal threadlocal = new ThreadLocal() {
    @Override
	protected synchronized Object initialValue() {
      return new PrivilegedActionEvent();
    }
  };
  private void superDispatchEvent(final AWTEvent event) {
    super.dispatchEvent(event);
  }
  @Override
protected void dispatchEvent(final AWTEvent event){
    PrivilegedActionEvent pae = (PrivilegedActionEvent) threadlocal.get();
    pae.event = event;
    AccessController.doPrivileged(pae, aContext);
  }
}