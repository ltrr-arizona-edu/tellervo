package corina.core;

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