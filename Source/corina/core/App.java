package corina.core;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import corina.logging.CorinaLog;
import corina.logging.Logging;
import corina.platform.Macintosh;
import corina.platform.Netware;
import corina.platform.Platform;
import corina.prefs.Prefs;

/**
 * Contextual state of the app; holds references to all "subsystems".
 * Originally was going to be named AppContext, but shortened because it
 * has to be referenced frequently.
 * @author Aaron Hamid
 */
public class App {
  public static Prefs prefs;
  public static Platform platform;
  public static Logging logging;

  private static final CorinaLog log = new CorinaLog(App.class);
  private static boolean initialized;

  public static synchronized void init(ProgressListener app_listener, ProgressListener subsystem_listener) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
    if (initialized) throw new IllegalStateException("AppContext already initialized.");

    log.debug("initializing App");

    app_listener.setLimit(3);

    app_listener.setNote("Logging");
    logging = new Logging();
    logging.init();
    app_listener.setValue(1);
    
    app_listener.setNote("Platform");
    platform = new Platform();
    platform.init();
    app_listener.setValue(2);

    // <init prefs>
    //prefs = new Prefs();
    //prefs.init();

    // if the user hasn't specified a parser with
    // -Dorg.xml.sax.driver=..., use crimson.
    if (System.getProperty("org.xml.sax.driver") == null)
      System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
    // xerces: "org.apache.xerces.parsers.SAXParser"
    // gnu/jaxp: "gnu.xml.aelfred2.SAXDriver"

    // migrate old prefs (!!!)
    // WAS: Migrate.migrate();

    // load properties -- messagedialog here is UGLY!
    app_listener.setNote("Preferences");
    prefs = new Prefs();
    prefs.init();
    app_listener.setValue(3);
    /*try {
      //Prefs.init();
    } catch (IOException ioe) {
      JOptionPane.showMessageDialog(null,
          "While trying to load preferences:\n" + ioe.getMessage(),
          "Corina: Error Loading Preferences", JOptionPane.ERROR_MESSAGE);
    }*/
    
    initialized = true;   
  }
  
  public static synchronized void destroy(ProgressListener app, ProgressListener subsystem) {
    // throwing an error instead of simply ignoring it
    // will point out bad design and/or bugs
    if (!initialized) throw new IllegalStateException("AppContext already destroyed.");
  }
}