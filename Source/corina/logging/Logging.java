package corina.logging;

import corina.core.AbstractSubsystem;
import corina.core.ProgressListener;

/**
 * Logging subsystem - just initializes CorinaLog.
 * @author Aaron Hamid
 */
public class Logging extends AbstractSubsystem {
  public String getName() {
    return "Logging";
  }
  public void init() {
    super.init();
    CorinaLog.init();
    setInitialized(true);
  }
  public void destroy() {
    super.destroy();
    setInitialized(false);
    // nothing to destroy
  }
}