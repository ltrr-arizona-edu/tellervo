package corina.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import corina.Build;
import corina.util.Center;
import corina.util.OKCancel;
import corina.util.PureStringWriter;

// TODO: refactor!

// TODO: sizing: the dialog shouldn't change width -- set initial
// width based on textField.getPreferredSize()?  also, it might look
// weird for it to be centered when small, and then expand down.

// TODO: let user copy bug report
// TODO: let user print bug report (!!)

// TODO: need big bug icon!

/**
   A dialog for telling the user "You've found a bug!".

   <p>This dialog tells the user that a bug in Corina was encountered,
   and gives the option of showing detailed information (the stack
   trace, and some info about the OS and JVM).</p>

   <p>In the future, it should also lets the user copy, mail, print,
   or submit to SF's tracker the bug report.  It can also allow the
   user to easily save all data, or save all data to a special place
   (either with their default savers, or using serialization.</p>

   <p>Use this for "can't happen" blocks, when you <b>know</b>
   something can't happen, but have to catch the exception for the
   compiler to be happy.  For example,</p>

<pre>
   try {
      StringWriter s = new StringWriter();
      s.write("hello, world");
   } catch (IOException ioe) {
      // there's no way a StringWriter can throw an IOE --
      // it's just appending to a StringBuffer --
      // but java requires us to catch it, since
      // Writer declares it.  but we don't want to ignore
      // it, either.
      new Bug(ioe);
   }
</pre>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Bug extends JDialog {
  /**
   * Get the stack trace from a Throwable, as a String.
   * 
   * <p>I think Java 1.4 has a method for this, but I'm not
   * targetting 1.4 (yet).</p>
   *
   * @param t the Throwable (usually an Exception) to examine
   * @return its stack trace
   */
  public static String getStackTrace(Throwable t) {
  	// use PureStringWriter here because otherwise windows
  	// would show funny boxes when displaying it.
    // XXX: is that right... I need to check this -- Aaron
    PureStringWriter sw = new PureStringWriter();
    PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }

  /**
   * Assemble some system info useful for bug reports.
   *
   * <p>This consists of:</p>
   *
   * <ul>
   *   <li>the date and time of the Corina build
   *   <li>the name, version, and architecture of the OS
   *   <li>the name, version, and vendor for the Java runtime/VM
   *       specification/implementation
   *   <li>the current date and time
   * </ul>
   *
   * @return a string containing some system info
   */
  public static String getSystemInfo() {
	  StringBuffer buf = new StringBuffer();

  	// a nice header
  	buf.append("System Information:\n");
  	buf.append("\n");
  
  	// time/date/version of build
  	buf.append("Corina\n");
  	buf.append("   Version " + Build.VERSION + "\n");
  	buf.append("   Built at " + Build.TIMESTAMP + "\n");
  
  	// properties of the OS
  	buf.append("Operating system\n");
  	buf.append("   Name: " + System.getProperty("os.name") + "\n");
  	buf.append("   Version: " + System.getProperty("os.version") + "\n");
  	buf.append("   Architecture: " + System.getProperty("os.arch") + "\n");

  	// java runtime environment
  	buf.append("Java Runtime Environment\n");
  
  	// spec
  	{
  	    String version = System.getProperty("java.specification.version");
  	    String vendor = System.getProperty("java.specification.vendor");
  	    String name = System.getProperty("java.specification.name");
  	    buf.append("   Specification: " + name +
  		       ", version " + version +
  		       ", by " + vendor + "\n");
  	}

  	// impl
  	{
  	    String version = System.getProperty("java.version");
  	    String vendor = System.getProperty("java.vendor");
  	    buf.append("   Implementation: version " + version +
  		       ", by " + vendor + "\n");
  	}
  
  	// java VM
  	buf.append("Java Virtual Machine\n");
  
  	// spec
  	{
  	    String version = System.getProperty("java.vm.specification.version");
  	    String vendor = System.getProperty("java.vm.specification.vendor");
  	    String name = System.getProperty("java.vm.specification.name");
  	    buf.append("   Specification: " + name +
  		       ", version " + version +
  		       ", by " + vendor + "\n");
  	}
  
  	// impl
  	{
  	    String version = System.getProperty("java.vm.version");
  	    String vendor = System.getProperty("java.vm.vendor");
  	    String name = System.getProperty("java.vm.name");
  	    buf.append("   Implementation: " + name +
  		       ", version " + version +
  		       ", by " + vendor + "\n");
  	}
  
  	// do i care about java.home, java.class.version ("48.0"?),
  	// or java.class.path?  probably not.
  
  	// current date/time
  	Date now = new Date();
  	DateFormat date = DateFormat.getDateInstance(DateFormat.LONG);
  	DateFormat time = DateFormat.getTimeInstance(DateFormat.LONG);
  	buf.append("\n");
  	buf.append("Bug report generated: " + date.format(now) +
  		   " at " + time.format(now) + "\n");
  	return buf.toString();
  }

  /**
   * Make a new bug dialog.
   *
   * @param t a Throwable, usually an Exception, that is a bug
   */
  public Bug(Throwable t) {
	  super();
	  setTitle("Bug!");
	  setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	  
	  final String bugText = "Exception:\n" +
		   getStackTrace(t) + "\n\n" +
		   getSystemInfo();

  	JTextArea textArea = new JTextArea(bugText, 10, 50);
  	textArea.setEditable(false);
  	stackTrace = new JScrollPane(textArea);
  
  	JPanel message = Layout.flowLayoutL("You found a bug in Corina!");
  
  	JButton bummer = new JButton("Bummer");
  	bummer.addActionListener(new AbstractAction() {
  		public void actionPerformed(ActionEvent e) {
  		  dispose();
  		}
  	});
  
  	JButton submitreport = new JButton("Submit e-mail bug report");
  	submitreport.addActionListener(new AbstractAction() {
  		public void actionPerformed(ActionEvent e) {
  			corina.util.EmailBugReport.submitBugReportText(bugText);
  		}
  	});
  	
  	more = new JButton("Show Details");
  	more.addActionListener(new AbstractAction() {
  		public void actionPerformed(ActionEvent e) {
  	    // toggle visibility of stackTrace component
        if (visible) {
    			// hide it
    			getContentPane().remove(stackTrace);
    			pack();
  
          // change text: next op will be "show"
          more.setText("Show Details");
  		  } else {
          // show it
          getContentPane().add(stackTrace,
  				  BorderLayout.CENTER);
          pack();
  
          // change text: next op will be "hide"
          more.setText("Hide Details");
  		  }
        visible = !visible;
  		}
	  });

  	JPanel buttons = Layout.buttonLayout(more, submitreport, bummer);
  	buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
  
  	JPanel content = Layout.borderLayout(message,
  					     null, null, null,
  					     buttons);
  	content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  
  	setContentPane(content);

  	pack();
  	OKCancel.addKeyboardDefaults(bummer);
  	// setResizable(false);
  	Center.center(this);
  	show();
  }

  private JComponent stackTrace;

  private JButton more;

  private boolean visible = false;

  /**
   * Old interface! -- fix all occurances of this, then remove.
   *
   * @deprecated use new Bug(t)
   */
  public static void bug(Throwable t) {
	  new Bug(t);
  }
}