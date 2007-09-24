// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.core;

/**
 * For sub-system initialization and destruction.
 * @author Aaron Hamid
 */
public interface Subsystem {
  public String getName();
  public boolean isInitialized();
  public void init();
  public void destroy();
}