package corina.ui;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.NoSuchElementException;

/**
 * Used when an appropriate resource bundle cannot be found -
 * in the future perhaps I18N can be changed from loading the
 * resource bundle in a static initializer to an explicit init
 * method called during startup which upon failure can display
 * an error to the user.
 * @author Aaron Hamid arh14 at cornell.edu
 */
public class DefaultResourceBundle extends ResourceBundle {
  protected Object handleGetObject(String key) {
    return key;
  }
  public Enumeration getKeys() {
    return EMPTY_ENUMERATION;
  }
  
  private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
    public boolean hasMoreElements() {
      return false;
    }
    public Object nextElement() {
      throw new NoSuchElementException();
    }
  };
}