// Copyright (c) 2004-2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt

package edu.cornell.dendro.corina.logging;

import edu.cornell.dendro.corina.core.AbstractSubsystem;

/**
 * Logging subsystem - just initializes CorinaLog.
 * @author Aaron Hamid
 */
public class Logging extends AbstractSubsystem {
  public String getName() {
    return "Logging";
  }
  @Override
public void init() {
    super.init();
    CorinaLog.init();
    setInitialized(true);
  }
  @Override
public void destroy() {
    super.destroy();
    setInitialized(false);
    // nothing to destroy
  }
}