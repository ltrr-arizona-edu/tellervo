// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.core;

/**
 * Abstract implementation of subsystem.
 * @author Aaron Hamid
 */
public abstract class AbstractSubsystem implements Subsystem {
  protected boolean initialized;

  public boolean isInitialized() {
    return initialized;
  }

  protected void setInitialized(boolean i) {
    initialized = i;
  }

  public void init() {
    if (initialized) throw new IllegalStateException(getName() + " subsystem is already initialized");
  }

  public void destroy() {
    if (!initialized) throw new IllegalStateException(getName() + " subsystem is already destroyed");
    initialized = false;
  }
}