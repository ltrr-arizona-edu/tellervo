package corina.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Aaron Hamid
 */
public class CorinaLog extends SimpleLog {
  /**
   * The maximum number of log messages that will be kept in memory.
   * TODO: grab this from prefs...
   */
  public static final int MAXLINES = 500;

  private static Object LOCK = new Object();
  private static String[] log = new String[MAXLINES];
  private static int logLineCount;
  private static boolean wrap;
  private static LogListModel listModel = new LogListModel();
  public final static PrintStream realOut;
  public final static PrintStream realErr;
  private static CorinaLog STDOUT = new CorinaLog("STDOUT", false);
  private static CorinaLog STDERR = new CorinaLog("STDERR", false);

  static {
    realOut = System.out;
    realErr = System.err;
  }
  
  public static void init() {
    System.setOut(createPrintStream(STDOUT, false));
    System.setErr(createPrintStream(STDERR, true));
  }

  private static PrintStream createPrintStream(final Log log, final boolean error) {
    return new PrintStream(new OutputStream() {
      public void write(int b) {
        byte[] barray = { (byte)b };
        write(barray,0,1);
      }

      public void write(byte[] b, int off, int len) {
        String str = new String(b,off,len);
        // skip any trailing EOL
        if ("\r".equals(str) ||
            "\n".equals(str) ||
            "\r\n".equals(str)) return;
        if (error) {
          log.error(str);
        } else {
          log.info(str);
        }
      }
    });
  }

  public static ListModel getLogListModel() {
    return listModel;
  }
    
  /**
   * ListModel for a JList-based log viewer
   * @author Aaron Hamid
   */
  static class LogListModel implements ListModel
  {
    ArrayList listeners = new ArrayList();

    private void fireIntervalAdded(int index1, int index2)
    {
      for(int i = 0; i < listeners.size(); i++)
      {
        ListDataListener listener = (ListDataListener)
          listeners.get(i);
        listener.intervalAdded(new ListDataEvent(this,
          ListDataEvent.INTERVAL_ADDED,
          index1,index2));
      }
    }

    private void fireIntervalRemoved(int index1, int index2)
    {
      for(int i = 0; i < listeners.size(); i++)
      {
        ListDataListener listener = (ListDataListener)
          listeners.get(i);
        listener.intervalRemoved(new ListDataEvent(this,
          ListDataEvent.INTERVAL_REMOVED,
          index1,index2));
      }
    }

    public void addListDataListener(ListDataListener listener)
    {
      listeners.add(listener);
    }

    public void removeListDataListener(ListDataListener listener)
    {
      listeners.add(listener);
    }

    public Object getElementAt(int index)
    {
      if(wrap)
      {
        if(index < MAXLINES - logLineCount)
          return log[index + logLineCount];
        else
          return log[index - MAXLINES + logLineCount];
      }
      else
        return log[index];
    }

    public int getSize()
    {
      if(wrap)
        return MAXLINES;
      else
        return logLineCount;
    }

    void update(final int lineCount, final boolean oldWrap)
    {
      if(lineCount == 0 || listeners.size() == 0)
        return;

      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          if(wrap)
          {
            if(oldWrap)
              fireIntervalRemoved(0,lineCount - 1);
            else
            {
              fireIntervalRemoved(0,
                logLineCount);
            }
            fireIntervalAdded(
              MAXLINES - lineCount + 1,
              MAXLINES);
          }
          else
          {
            fireIntervalAdded(
              logLineCount - lineCount + 1,
              logLineCount);
          }
        }
      });
    }
  }
  
  private Log chained;
  
  public CorinaLog(Class clazz) {
    super(clazz.toString());
    this.chained = LogFactory.getLog(clazz);
  }
  public CorinaLog(String name) {
    super(name);
    this.chained = LogFactory.getLog(name);
  }
  public CorinaLog(String name, boolean chain) {
    super(name);
    if (chain) this.chained = LogFactory.getLog(name);
  }
  
  /**
   * Log to central log buffer.
   */
  protected void doWrite(StringBuffer buf) {
    String str = buf.toString();
    //  If multiple threads log stuff, we don't want
    // the output to get mixed up
    synchronized(LOCK) {
      StringTokenizer st = new StringTokenizer(str,"\r\n");
      int lineCount = 0;
      boolean oldWrap = wrap;
      while(st.hasMoreTokens()) {
        lineCount++;
        log[logLineCount] = st.nextToken().replace('\t',' ');
        if(++logLineCount >= log.length) {
          wrap = true;
          logLineCount = 0;
        }
      }
      listModel.update(lineCount,oldWrap);
    }
  }  
  public void debug(Object message) {
    super.debug(message);
    if (chained != null) chained.debug(message); 
  }
  public void debug(Object message, Throwable t) {
    super.debug(message, t);
    if (chained != null) chained.debug(message, t); 
  }
  public void error(Object message) {
    super.error(message);
    if (chained != null) chained.error(message); 
  }
 
  public void error(Object message, Throwable t) {
    super.error(message, t);
    if (chained != null) chained.error(message, t);  
  }

  public void fatal(Object message) {
    super.fatal(message);
    if (chained != null) chained.fatal(message);  
  }
    
  public void fatal(Object message, Throwable t) {
    super.fatal(message, t);
    if (chained != null) chained.fatal(message, t);  
  }
    
  public void info(Object message) { 
    super.info(message);
    if (chained != null) chained.info(message); 
  }

  public void info(Object message, Throwable t) {
    super.info(message, t);
    if (chained != null) chained.info(message, t);  
  }
  
  public void trace(Object message) {
    super.trace(message);
    if (chained != null) chained.trace(message);
  }
 
  public void trace(Object message, Throwable t) { 
    super.trace(message, t);
    if (chained != null) chained.trace(message, t);
  }
 
  public void warn(Object message) {
    super.warn(message);
    if (chained != null) chained.warn(message);
  }
 
  public void warn(Object message, Throwable t){ 
    super.warn(message);
    if (chained != null) chained.warn(message);
  }
}