package corina.core;

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