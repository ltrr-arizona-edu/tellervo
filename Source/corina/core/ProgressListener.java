package corina.core;

/**
 * Abstract interface for informing a caller on progress.
 * @author Aaron Hamid
 */
public interface ProgressListener {
  public void setLimit(int max);
  public void setValue(int value);
  public void setNote(String string);
}