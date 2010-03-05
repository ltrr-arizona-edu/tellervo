package edu.cornell.dendro.corina.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ListModel;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Document;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.ui.Alert;

public class BugReport {
	private final Throwable bug;

	private String fromEmail;
	private String comments;
	
	/**
	 * Create a bug report from this throwable
	 * @param t
	 */
	public BugReport(Throwable t) {
		this.bug = t;
		
		addCompressedLogHistory();
		fromEmail = comments = null;		
	}
	
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	private void addCompressedLogHistory() {		
		try {
			// create a byte buffer with an initial 1024-byte capacity
			ByteArrayOutputStream bytebuf = new ByteArrayOutputStream(1024);
			ZipOutputStream out = new ZipOutputStream(bytebuf);
			
			// create a new file, loghistory.txt
			out.putNextEntry(new ZipEntry("loghistory.txt"));
			// make it a utf-8 representation of the 
			ByteArrayInputStream in = new ByteArrayInputStream(getLogTrace().getBytes("utf-8"));

			int len;
			byte[] buf = new byte[1024];
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			
			in.close();
			out.closeEntry();
			out.close();
			
			// ok, if all went well, bytebuf is technically a .zip file...
			
			in = new ByteArrayInputStream(bytebuf.toByteArray());
			addDocument("loghistory.zip", in);
		} catch (IOException ioe) {
			// just don't include it, then
		}
	}
	
	public static String getUserName() {
		return System.getProperty("user.name", "(unknown user)");
	}
	
	public static String getStackTrace(Throwable t) {
		PureStringWriter sw = new PureStringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		
		return sw.toString();
	}
	
	public static String getLogTrace() {
		StringBuffer errors = new StringBuffer();	
		ListModel logEntries = CorinaLog.getLogListModel();

		for(int i = 0; i < logEntries.getSize(); i++) {
			errors.append(logEntries.getElementAt(i));
			errors.append("\r\n");
		}

		return errors.toString();
	}
	
	public static String getUserInfo() {
		StringBuffer buf = new StringBuffer();

		// a nice header
		buf.append("User Information:\n");
		buf.append("\n");
		
		buf.append("   User: " + System.getProperty("user.name") + "\n");
		buf.append("   Home: " + System.getProperty("user.home") + "\n");
		buf.append("   Language: " + System.getProperty("user.language") + "\n");
		buf.append("   Region: " + System.getProperty("user.region") + "\n");
		buf.append("   TZ: " + System.getProperty("user.timezone") + "\n");
		
		return buf.toString();
	}
		
	/**
	 * Return the type of bug and message for this bug report
	 */
	public String toString() {
		return bug.toString();
	}
	
	/**
	 * Get information about the current system
	 * @return
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
			buf.append("   Specification: " + name + ", version " + version
					+ ", by " + vendor + "\n");
		}

		// impl
		{
			String version = System.getProperty("java.version");
			String vendor = System.getProperty("java.vendor");
			buf.append("   Implementation: version " + version + ", by "
					+ vendor + "\n");
		}

		// java VM
		buf.append("Java Virtual Machine\n");

		// spec
		{
			String version = System
					.getProperty("java.vm.specification.version");
			String vendor = System.getProperty("java.vm.specification.vendor");
			String name = System.getProperty("java.vm.specification.name");
			buf.append("   Specification: " + name + ", version " + version
					+ ", by " + vendor + "\n");
		}

		// impl
		{
			String version = System.getProperty("java.vm.version");
			String vendor = System.getProperty("java.vm.vendor");
			String name = System.getProperty("java.vm.name");
			buf.append("   Implementation: " + name + ", version " + version
					+ ", by " + vendor + "\n");
		}

		// do i care about java.home, java.class.version ("48.0"?),
		// or java.class.path? probably not.

		// current date/time
		Date now = new Date();
		DateFormat date = DateFormat.getDateInstance(DateFormat.LONG);
		DateFormat time = DateFormat.getTimeInstance(DateFormat.LONG);
		buf.append("\n");
		buf.append("Bug report generated: " + date.format(now) + " at "
				+ time.format(now) + "\n");
		return buf.toString();
	}
	
	// a list of attached documents
	private List<DocumentHolder> documents = new ArrayList<DocumentHolder>();
	
	/**
	 * Retrieve a list of attached documents
	 * @return
	 */
	public List<DocumentHolder> getDocuments() {
		return documents;
	}
	
	public void addDocument(String filename, Object document) {
		documents.add(new DocumentHolder(filename, document));
	}
	
	public class DocumentHolder {
		public DocumentHolder(String filename, Object document) {
			this.filename = filename;
			this.document = document;
		}
		
		private String filename;
		private Object document;
		
		public String getFilename() { return filename; }
		public Object getDocument() { return document; }
		public String toString() { return filename; }
	}
	
	//// web code to submit bug report
	public boolean submit() {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://dendro.cornell.edu/bug/submit.php");
			MultipartEntity postEntity = new MultipartEntity();
			
			// add our required parts
			postEntity.addPart("username", new StringBody(getUserName()));
			postEntity.addPart("version", new StringBody(Build.VERSION));
			postEntity.addPart("timestamp", new StringBody(Build.TIMESTAMP));
			postEntity.addPart("error", new StringBody(toString()));
			postEntity.addPart("sysinfo", new StringBody(getSystemInfo()));
			postEntity.addPart("stacktrace", new StringBody(getStackTrace(bug)));
			postEntity.addPart("userinfo", new StringBody(getUserInfo()));

			if(fromEmail != null)
				postEntity.addPart("replyto", new StringBody(fromEmail));
			if(comments != null)
				postEntity.addPart("comments", new StringBody(comments));

			// add any attached documents
			for(DocumentHolder dh : documents) {
				if(dh.getDocument() == null) {
					String emptyDoc = "<Provided document was null>";
					postEntity.addPart("attachments[]", new InputStreamBody(
							stringAsStream(emptyDoc), dh.getFilename()));
				}
				else if(dh.getDocument() instanceof File)
					postEntity.addPart("attachments[]", 
							new FileBody((File)dh.getDocument()));
				else if(dh.getDocument() instanceof Document)
					postEntity.addPart("attachments[]", 
							new XMLBody((Document)dh.getDocument(), dh.getFilename()).setPretty(true));
				else if(dh.getDocument() instanceof InputStream)
					postEntity.addPart("attachments[]", 
							new InputStreamBody((InputStream)dh.getDocument(), dh.getFilename()));
				else
					postEntity.addPart("attachments[]", 
							new UTF8StringBody(dh.getDocument().toString(), dh.getFilename()));
			}
				
			post.setEntity(postEntity);
			
			BasicResponseHandler handler = new BasicResponseHandler();
			String result = client.execute(post, handler);
			
			Alert.error("Bug report submitted", "<html>" + result);
		} catch (Exception e) {
			Alert.error("Error submitting bug report", "Couldn't submit bug report: " + e.toString());
			return false;
		}
		
		return true;
	}
	
	private InputStream stringAsStream(String s) {
		byte[] bytes;
		
		try {
			bytes = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			bytes = s.getBytes();
		}
		
		return new ByteArrayInputStream(bytes);
	}
}
